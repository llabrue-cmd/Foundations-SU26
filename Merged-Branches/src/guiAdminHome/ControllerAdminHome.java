package guiAdminHome;

import java.time.LocalDateTime;

import database.Database;
import java.util.List;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;

/*******
 * <p> Title: GUIAdminHomePage Class. </p>
 * 
 * <p> Description: The Java/FX-based Admin Home Page.  This class provides the controller actions
 * basic on the user's use of the JavaFX GUI widgets defined by the View class.
 * 
 * This page contains a number of buttons that have not yet been implemented.  WHen those buttons
 * are pressed, an alert pops up to tell the user that the function associated with the button has
 * not been implemented. Also, be aware that What has been implemented may not work the way the
 * final product requires and there maybe defects in this code.
 * 
 * The class has been written assuming that the View or the Model are the only class methods that
 * can invoke these methods.  This is why each has been declared at "protected".  Do not change any
 * of these methods to public.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-17 Initial version
 * @version 1.01		2025-09-16 Update Javadoc documentation *  
 */

public class ControllerAdminHome {
	
	/*-*******************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/
	
	/**
	 * Default constructor is not used.
	 */
	public ControllerAdminHome() {
	}
	
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	/**********
	 * <p> 
	 * 
	 * Title: performInvitation () Method. </p>
	 * 
	 * <p> Description: Protected method to send an email inviting a potential user to establish
	 * an account and a specific role. </p>
	 */
	protected static void performInvitation () {
		// Verify that the email address is valid - If not alert the user and return
		String emailAddress = ViewAdminHome.text_InvitationEmailAddress.getText();
		if (invalidEmailAddress(emailAddress)) {
			return;
		}
		
		// Check to ensure that we are not sending a second message with a new invitation code to
		// the same email address.  
		if (theDatabase.emailaddressHasBeenUsed(emailAddress)) {
			ViewAdminHome.alertEmailError.setContentText(
					"An invitation has already been sent to this email address.");
			ViewAdminHome.alertEmailError.showAndWait();
			return;
		}
		
		boolean adminRole = ViewAdminHome.checkbox_InvitationAdmin.isSelected();
		boolean role1 = ViewAdminHome.checkbox_InvitationRole1.isSelected();
		boolean role2 = ViewAdminHome.checkbox_InvitationRole2.isSelected();
		if (!adminRole && !role1 && !role2) {
			ViewAdminHome.alertEmailError.setContentText("Select at least one role.");
			ViewAdminHome.alertEmailError.showAndWait();
			return;
		}

		Integer validityDays = ViewAdminHome.combobox_ValidityDays.getValue();
		if (validityDays == null || validityDays <= 0) {
			ViewAdminHome.alertEmailError.setContentText("Select a valid invitation duration.");
			ViewAdminHome.alertEmailError.showAndWait();
			return;
		}

		LocalDateTime expiration = LocalDateTime.now().plusDays(validityDays);
		String invitationCode = theDatabase.generateInvitationCode(emailAddress, expiration,
				adminRole, role1, role2);
		if (invitationCode.isEmpty()) {
			ViewAdminHome.alertEmailError.setContentText(
					"The invitation could not be saved. Please try again.");
			ViewAdminHome.alertEmailError.showAndWait();
			return;
		}

		String roles = selectedRoles(adminRole, role1, role2);
		String msg = "Code: " + invitationCode + " for role(s) " + roles +
				" was sent to: " + emailAddress;
		System.out.println(msg);
		ViewAdminHome.alertEmailSent.setContentText(msg);
		ViewAdminHome.alertEmailSent.showAndWait();
		
		// Update the Admin Home pages status
		ViewAdminHome.text_InvitationEmailAddress.setText("");
		ViewAdminHome.checkbox_InvitationAdmin.setSelected(false);
		ViewAdminHome.checkbox_InvitationRole1.setSelected(false);
		ViewAdminHome.checkbox_InvitationRole2.setSelected(false);
		ViewAdminHome.label_NumberOfInvitations.setText("Number of outstanding invitations: " + 
				theDatabase.getNumberOfInvitations());
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: manageInvitations () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void manageInvitations () {
		guiManageInvitations.ViewManageInvitations.displayManageInvitations(
				ViewAdminHome.theStage, ViewAdminHome.theUser);
	}

	private static String selectedRoles(boolean adminRole, boolean role1, boolean role2) {
		String roles = "";
		if (adminRole) roles = "Admin";
		if (role1) roles += roles.isEmpty() ? "Role1" : ", Role1";
		if (role2) roles += roles.isEmpty() ? "Role2" : ", Role2";
		return roles;
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: setOnetimePassword () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void setOnetimePassword () {
		System.out.println("\n*** WARNING ***: One-Time Password Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.setTitle("*** WARNING ***");
		ViewAdminHome.alertNotImplemented.setHeaderText("One-Time Password Issue");
		ViewAdminHome.alertNotImplemented.setContentText("One-Time Password Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.showAndWait();
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: deleteUser () Method. </p>
	 * 
	 * <p> Description: Protected method that allows admin users to get a list of every user aside
	 * from themself, and gives them the option to delete them. </p>
	 */
	protected static void deleteUser() {
		List<String> userList = theDatabase.getUserList();
		
		if (userList != null && !userList.isEmpty()) {
			userList.remove(0);
		}
		
		if (userList == null || userList.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Delete User");
			alert.setHeaderText("No Users Available");
			alert.setContentText("There are no users available to delete.");
			alert.showAndWait();
			return;
		}
		
		String currentUsername = ViewAdminHome.theUser.getUserName();
		userList.remove(currentUsername);
		
		if (userList.isEmpty()) {
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setTitle("Delete User");
			alert.setHeaderText("Cannot Delete User");
			alert.setContentText("You cannot delete yourself.");
			alert.showAndWait();
			return;
		}
		
		ChoiceDialog<String> dialog = new ChoiceDialog<>(userList.get(0), userList);
		dialog.setTitle("Delete User");
		dialog.setHeaderText("Select a user to delete");
		dialog.setContentText("User:");
		
		dialog.showAndWait().ifPresent(selectedUsername -> {
			
			Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
			confirmation.setTitle("Confirm Delete");
			confirmation.setHeaderText("Delete User");
			confirmation.setContentText(
					"Are you sure you want to delete user \"" + selectedUsername + "\"?");
			
			confirmation.getButtonTypes().setAll(ButtonType.YES, ButtonType.NO);

			confirmation.showAndWait().ifPresent(response -> {
				
			
				if (response == ButtonType.YES) {
					
					boolean deleted = theDatabase.deleteUser(selectedUsername);
					
					if (deleted) {
						Alert success = new Alert(Alert.AlertType.INFORMATION);
						success.setTitle("Delete User");
						success.setHeaderText("User Deleted");
						success.setContentText(
								"User \"" + selectedUsername + "\" has been deleted.");
						success.showAndWait();

						// Update the number of users displayed on the Admin Home page
						ViewAdminHome.label_NumberOfUsers.setText(
								"Number of users: " + theDatabase.getNumberOfUsers());
						
					}
					
					else {
						
						Alert error = new Alert(Alert.AlertType.ERROR);
						error.setTitle("Delete User");
						error.setHeaderText("Delete Failed");
						error.setContentText(
								"Unable to delete user \"" + selectedUsername + "\".");
						error.showAndWait();
					}
				
				}
			});
		});
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: listUsers () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void listUsers() {
		System.out.println("\n*** WARNING ***: List Users Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.setTitle("*** WARNING ***");
		ViewAdminHome.alertNotImplemented.setHeaderText("List User Issue");
		ViewAdminHome.alertNotImplemented.setContentText("List Users Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.showAndWait();
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: addRemoveRoles () Method. </p>
	 * 
	 * <p> Description: Protected method that allows an admin to add and remove roles for any of
	 * the users currently in the system.  This is done by invoking the AddRemoveRoles Page. There
	 * is no need to specify the home page for the return as this can only be initiated by and
	 * Admin.</p>
	 */
	protected static void addRemoveRoles() {
		guiAddRemoveRoles.ViewAddRemoveRoles.displayAddRemoveRoles(ViewAdminHome.theStage, 
				ViewAdminHome.theUser);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: invalidEmailAddress () Method. </p>
	 * 
	 * <p> Description: Protected method that is intended to check an email address before it is
	 * used to reduce errors.  The code currently only checks to see that the email address is not
	 * empty.  In the future, a syntactic check must be performed and maybe there is a way to check
	 * if a properly email address is active.</p>
	 * 
	 * @param emailAddress	This String holds what is expected to be an email address
	 */
	protected static boolean invalidEmailAddress(String emailAddress) {
		if (emailAddress.length() == 0) {
			ViewAdminHome.alertEmailError.setContentText(
					"Correct the email address and try again.");
			ViewAdminHome.alertEmailError.showAndWait();
			return true;
		}
		return false;
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performLogout () Method. </p>
	 * 
	 * <p> Description: Protected method that logs this user out of the system and returns to the
	 * login page for future use.</p>
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewAdminHome.theStage);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performQuit () Method. </p>
	 * 
	 * <p> Description: Protected method that gracefully terminates the execution of the program.
	 * </p>
	 */
	protected static void performQuit() {
		System.exit(0);
	}
}
