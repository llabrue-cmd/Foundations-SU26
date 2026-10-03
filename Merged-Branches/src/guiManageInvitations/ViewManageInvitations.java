package guiManageInvitations;

import java.util.List;

import database.Database;
import entityClasses.Invitation;
import entityClasses.User;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * JavaFX view used to edit the deadline and roles of outstanding invitations.
 */
public class ViewManageInvitations {
	private static final double WIDTH = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static final double HEIGHT = applicationMain.FoundationsMain.WINDOW_HEIGHT;
	private static final Database DATABASE = applicationMain.FoundationsMain.database;

	protected static Stage theStage;
	protected static User theUser;
	private static ViewManageInvitations theView;
	private static Scene theScene;

	protected static ComboBox<Invitation> combobox_Invitations = new ComboBox<Invitation>();
	protected static Label label_Status = new Label();
	protected static Label label_Email = new Label();
	protected static Label label_Code = new Label();
	protected static DatePicker date_Expiration = new DatePicker();
	protected static TextField text_ExpirationTime = new TextField();
	protected static CheckBox checkbox_Admin = new CheckBox("Admin");
	protected static CheckBox checkbox_Role1 = new CheckBox("Role1");
	protected static CheckBox checkbox_Role2 = new CheckBox("Role2");
	protected static Button button_Save = new Button("Save Invitation");
	private static Button button_Return = new Button("Return to Admin Home");
	protected static Alert alert_Message = new Alert(AlertType.INFORMATION);

	public static void displayManageInvitations(Stage stage, User user) {
		theStage = stage;
		theUser = user;
		if (theView == null) theView = new ViewManageInvitations();
		refreshInvitations(null);
		theStage.setTitle("CSE 360 Foundation Code: Manage Invitations");
		theStage.setScene(theScene);
		theStage.show();
	}

	protected static void refreshInvitations(String codeToSelect) {
		List<Invitation> invitations = DATABASE.getInvitations();
		combobox_Invitations.setItems(FXCollections.observableArrayList(invitations));

		if (invitations.isEmpty()) {
			combobox_Invitations.getSelectionModel().clearSelection();
			label_Status.setText("There are no invitations to manage.");
			clearDetails();
			setDetailsDisabled(true);
			return;
		}

		setDetailsDisabled(false);
		Invitation selection = invitations.get(0);
		if (codeToSelect != null) {
			for (Invitation invitation : invitations) {
				if (invitation.getCode().equals(codeToSelect)) selection = invitation;
			}
		}
		combobox_Invitations.setValue(selection);
		ControllerManageInvitations.populateSelectedInvitation();
	}

	private ViewManageInvitations() {
		Pane root = new Pane();
		theScene = new Scene(root, WIDTH, HEIGHT);

		Label title = new Label("Manage Invitations");
		setupLabel(title, 28, WIDTH, Pos.CENTER, 0, 15);

		Label selectInvitation = new Label("Invitation");
		setupLabel(selectInvitation, 17, 140, Pos.BASELINE_LEFT, 40, 85);
		setupComboBox(combobox_Invitations, 500, 180, 80);
		combobox_Invitations.setOnAction((_) ->
				ControllerManageInvitations.populateSelectedInvitation());

		setupLabel(label_Status, 16, 700, Pos.BASELINE_LEFT, 40, 135);
		setupLabel(label_Email, 17, 700, Pos.BASELINE_LEFT, 40, 185);
		setupLabel(label_Code, 17, 700, Pos.BASELINE_LEFT, 40, 225);

		Label expiration = new Label("Expiration date and time");
		setupLabel(expiration, 17, 220, Pos.BASELINE_LEFT, 40, 285);
		date_Expiration.setLayoutX(265);
		date_Expiration.setLayoutY(280);
		text_ExpirationTime.setPromptText("HH:mm");
		text_ExpirationTime.setPrefWidth(90);
		text_ExpirationTime.setLayoutX(445);
		text_ExpirationTime.setLayoutY(280);

		Label roles = new Label("Assigned roles");
		setupLabel(roles, 17, 170, Pos.BASELINE_LEFT, 40, 345);
		setupCheckBox(checkbox_Admin, 220, 345);
		setupCheckBox(checkbox_Role1, 320, 345);
		setupCheckBox(checkbox_Role2, 420, 345);

		setupButton(button_Save, 180, 40, 420);
		button_Save.setOnAction((_) -> ControllerManageInvitations.saveInvitation());
		setupButton(button_Return, 230, 285, 520);
		button_Return.setOnAction((_) -> ControllerManageInvitations.performReturn());

		alert_Message.setTitle("Manage Invitations");
		root.getChildren().addAll(title, selectInvitation, combobox_Invitations, label_Status,
				label_Email, label_Code, expiration, date_Expiration, text_ExpirationTime,
				roles, checkbox_Admin, checkbox_Role1, checkbox_Role2, button_Save, button_Return);
	}

	private static void clearDetails() {
		label_Email.setText("");
		label_Code.setText("");
		date_Expiration.setValue(null);
		text_ExpirationTime.setText("");
		checkbox_Admin.setSelected(false);
		checkbox_Role1.setSelected(false);
		checkbox_Role2.setSelected(false);
	}

	private static void setDetailsDisabled(boolean disabled) {
		date_Expiration.setDisable(disabled);
		text_ExpirationTime.setDisable(disabled);
		checkbox_Admin.setDisable(disabled);
		checkbox_Role1.setDisable(disabled);
		checkbox_Role2.setDisable(disabled);
		button_Save.setDisable(disabled);
	}

	private void setupLabel(Label label, double size, double width, Pos position, double x, double y) {
		label.setFont(Font.font("Arial", size));
		label.setMinWidth(width);
		label.setAlignment(position);
		label.setLayoutX(x);
		label.setLayoutY(y);
	}

	private void setupComboBox(ComboBox<?> comboBox, double width, double x, double y) {
		comboBox.setStyle("-fx-font: 16 Arial;");
		comboBox.setMinWidth(width);
		comboBox.setLayoutX(x);
		comboBox.setLayoutY(y);
	}

	private void setupCheckBox(CheckBox checkBox, double x, double y) {
		checkBox.setFont(Font.font("Arial", 17));
		checkBox.setLayoutX(x);
		checkBox.setLayoutY(y);
	}

	private void setupButton(Button button, double width, double x, double y) {
		button.setFont(Font.font("Dialog", 17));
		button.setMinWidth(width);
		button.setLayoutX(x);
		button.setLayoutY(y);
	}
}
