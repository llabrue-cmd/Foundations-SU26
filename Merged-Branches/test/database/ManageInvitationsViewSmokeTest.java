package database;

import java.time.LocalDateTime;

import applicationMain.FoundationsMain;
import entityClasses.User;
import guiManageInvitations.ViewManageInvitations;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * Starts the real JavaFX Manage Invitations scene and checks its populated and empty states.
 */
public class ManageInvitationsViewSmokeTest extends Application {
	private static Throwable failure;

	public static void main(String[] args) {
		launch(args);
		if (failure != null) throw new AssertionError("Manage Invitations view failed", failure);
		System.out.println("ManageInvitationsViewSmokeTest passed 7 checks.");
	}

	@Override
	public void start(Stage stage) {
		Database database = null;
		try {
			database = new Database("jdbc:h2:mem:section5view;DB_CLOSE_DELAY=-1");
			database.connectToDatabase();
			FoundationsMain.database = database;
			String code = database.generateInvitationCode("view@example.com",
					LocalDateTime.now().plusDays(2), true, false, true);
			User admin = new User("admin", "password", "", "", "", "", "", true, false, false);

			ViewManageInvitations.displayManageInvitations(stage, admin);
			check(stage.isShowing(), "Manage Invitations stage should be displayed");
			check(stage.getTitle().contains("Manage Invitations"), "window title should identify the page");
			ComboBox<?> invitationList = findFirst(stage.getScene().getRoot(), ComboBox.class);
			check(invitationList != null && invitationList.getItems().size() == 1,
					"stored invitation should appear in the selector");
			check(hasLabel(stage.getScene().getRoot(), "Invitation settings loaded."),
					"selected invitation details should load");

			Button returnButton = findButton(stage.getScene().getRoot(), "Return to Admin Home");
			check(returnButton != null, "Return button should be available");
			returnButton.fire();
			check(database.getInvitation(code) != null,
					"returning without saving should not change the invitation");

			database.removeInvitationAfterUse(code);
			ViewManageInvitations.displayManageInvitations(stage, admin);
			check(hasLabel(stage.getScene().getRoot(), "There are no invitations to manage."),
					"empty invitation state should be shown without crashing");
		} catch (Throwable throwable) {
			failure = throwable;
		} finally {
			if (database != null) database.closeConnection();
			stage.close();
		}
	}

	private static boolean hasLabel(Node node, String text) {
		if (node instanceof Label && text.equals(((Label) node).getText())) return true;
		if (node instanceof Parent) {
			for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
				if (hasLabel(child, text)) return true;
			}
		}
		return false;
	}

	private static <T> T findFirst(Node node, Class<T> type) {
		if (type.isInstance(node)) return type.cast(node);
		if (node instanceof Parent) {
			for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
				T result = findFirst(child, type);
				if (result != null) return result;
			}
		}
		return null;
	}

	private static Button findButton(Node node, String text) {
		if (node instanceof Button && text.equals(((Button) node).getText())) return (Button) node;
		if (node instanceof Parent) {
			for (Node child : ((Parent) node).getChildrenUnmodifiable()) {
				Button result = findButton(child, text);
				if (result != null) return result;
			}
		}
		return null;
	}

	private static void check(boolean condition, String message) {
		if (!condition) throw new AssertionError(message);
	}
}
