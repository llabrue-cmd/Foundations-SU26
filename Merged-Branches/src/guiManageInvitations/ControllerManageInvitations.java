package guiManageInvitations;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

import database.Database;
import entityClasses.Invitation;

/**
 * Controller actions for the Manage Invitations page.
 */
public class ControllerManageInvitations {
	private static final Database DATABASE = applicationMain.FoundationsMain.database;
	private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm")
			.withResolverStyle(ResolverStyle.STRICT);

	private ControllerManageInvitations() {
	}

	protected static void populateSelectedInvitation() {
		Invitation invitation = ViewManageInvitations.combobox_Invitations.getValue();
		if (invitation == null) return;

		ViewManageInvitations.label_Email.setText("Email address: " + invitation.getEmailAddress());
		ViewManageInvitations.label_Code.setText("Invitation code: " + invitation.getCode());
		ViewManageInvitations.checkbox_Admin.setSelected(invitation.getAdminRole());
		ViewManageInvitations.checkbox_Role1.setSelected(invitation.getRole1());
		ViewManageInvitations.checkbox_Role2.setSelected(invitation.getRole2());

		LocalDateTime expiration = invitation.getExpiration();
		if (expiration == null) {
			expiration = LocalDateTime.now().plusDays(7).withSecond(0).withNano(0);
			ViewManageInvitations.label_Status.setText(
					"Legacy invitation: save it to add an expiration deadline.");
		} else if (invitation.isExpired(LocalDateTime.now())) {
			ViewManageInvitations.label_Status.setText("This invitation is expired.");
		} else {
			ViewManageInvitations.label_Status.setText("Invitation settings loaded.");
		}

		ViewManageInvitations.date_Expiration.setValue(expiration.toLocalDate());
		ViewManageInvitations.text_ExpirationTime.setText(expiration.toLocalTime().format(TIME_FORMAT));
	}

	protected static void saveInvitation() {
		Invitation invitation = ViewManageInvitations.combobox_Invitations.getValue();
		if (invitation == null) {
			showMessage("No Invitation Selected", "Select an invitation before saving.");
			return;
		}

		boolean adminRole = ViewManageInvitations.checkbox_Admin.isSelected();
		boolean role1 = ViewManageInvitations.checkbox_Role1.isSelected();
		boolean role2 = ViewManageInvitations.checkbox_Role2.isSelected();
		if (!adminRole && !role1 && !role2) {
			showMessage("Role Required", "Select at least one role.");
			return;
		}

		if (ViewManageInvitations.date_Expiration.getValue() == null) {
			showMessage("Invalid Expiration", "Select an expiration date.");
			return;
		}

		LocalTime time;
		try {
			time = LocalTime.parse(ViewManageInvitations.text_ExpirationTime.getText(), TIME_FORMAT);
		} catch (DateTimeParseException e) {
			showMessage("Invalid Expiration", "Enter the time in 24-hour HH:mm format.");
			return;
		}

		LocalDateTime expiration = LocalDateTime.of(
				ViewManageInvitations.date_Expiration.getValue(), time);
		if (!expiration.isAfter(LocalDateTime.now())) {
			showMessage("Invalid Expiration", "The expiration must be in the future.");
			return;
		}

		if (!DATABASE.updateInvitationSettings(invitation.getCode(), expiration,
				adminRole, role1, role2)) {
			showMessage("Save Failed", "The invitation settings could not be saved.");
			return;
		}

		ViewManageInvitations.refreshInvitations(invitation.getCode());
		showMessage("Invitation Saved", "The invitation settings were saved successfully.");
	}

	protected static void performReturn() {
		guiAdminHome.ViewAdminHome.displayAdminHome(
				ViewManageInvitations.theStage, ViewManageInvitations.theUser);
	}

	private static void showMessage(String header, String content) {
		ViewManageInvitations.alert_Message.setHeaderText(header);
		ViewManageInvitations.alert_Message.setContentText(content);
		ViewManageInvitations.alert_Message.showAndWait();
	}
}
