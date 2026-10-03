package guiNewAccount;

import java.sql.SQLException;
import java.time.LocalDateTime;

import database.Database;
import entityClasses.Invitation;
import entityClasses.User;

/*******
 * <p> Title: ControllerNewAccount Class. </p>
 * 
 * <p> Description: The Java/FX-based New Account Page.  This class provides the controller actions
 * to allow the user to establish a new account after responding to an invitation and the use of a
 * one time code.
 * 
 * The controller deals with the user pressing the "User Step" button widget being click.  If also
 * supports the user click on the "Quit" button widget.
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
 *  
 */

public class ControllerNewAccount {
	
	/*-********************************************************************************************

	The User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/

	/**
	 * Default constructor is not used.
	 */
	public ControllerNewAccount() {
	}
	
	
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	/**********
	 * <p> Method: public doCreateUser() </p>
	 * 
	 * <p> Description: This method is called when the user has clicked on the User Setup
	 * button.  This method checks the input fields to see that they are valid.  If so, it then
	 * creates the account by adding information to the database.
	 * 
	 * The method reaches batch to the view page and to fetch the information needed rather than
	 * passing that information as parameters.
	 * 
	 */	
	protected static void doCreateUser() {
		
		// Fetch the username and password. (We use the first of the two here, but we will validate
		// that the two password fields are the same before we do anything with it.)
		String username = ViewNewAccount.text_Username.getText();
		String password = ViewNewAccount.text_Password1.getText();
		String passwordConfirmation = ViewNewAccount.text_Password2.getText();

		// Recheck immediately before account creation in case the deadline passed while this page
		// was open or an administrator changed the invitation settings.
		Invitation invitation = theDatabase.getInvitation(ViewNewAccount.theInvitationCode);
		if (invitation == null || !invitation.hasAtLeastOneRole()) {
			ViewNewAccount.alertInvitationCodeIsInvalid.showAndWait();
			return;
		}
		if (invitation.isExpired(LocalDateTime.now())) {
			ViewNewAccount.alertInvitationExpired.showAndWait();
			return;
		}

		// Ask the model to validate input. The controller only coordinates the result with the view.
		String userNameError = ModelNewAccount.validateUsername(username);

		if (!userNameError.isEmpty()) {
			ViewNewAccount.alertUsernamePasswordError.setTitle("Invalid Username");
			ViewNewAccount.alertUsernamePasswordError.setHeaderText(
					"The username does not satisfy the required format.");
			ViewNewAccount.alertUsernamePasswordError.setContentText(userNameError);
			ViewNewAccount.alertUsernamePasswordError.showAndWait();
			return;
		}

		String passwordError =
				ModelNewAccount.validatePasswords(password, passwordConfirmation);

		if (!passwordError.isEmpty()) {
			ViewNewAccount.text_Password1.setText("");
			ViewNewAccount.text_Password2.setText("");
			ViewNewAccount.alertUsernamePasswordError.setTitle("Passwords Do Not Match");
			ViewNewAccount.alertUsernamePasswordError.setHeaderText(passwordError);
			ViewNewAccount.alertUsernamePasswordError.setContentText(
					"Correct the passwords and try again.");
			ViewNewAccount.alertUsernamePasswordError.showAndWait();
			return;
		}
		
		// Display key information to the log
		System.out.println("** Account for Username: " + username + "; theInvitationCode: "+
				ViewNewAccount.theInvitationCode + "; email address: " + 
				invitation.getEmailAddress() + "; Roles: " + invitation.getRoleNames());
		
		// Initialize local variables that will be created during this process
		int roleCode = 0;
		if (invitation.getAdminRole()) roleCode = 1;
		else if (invitation.getRole1()) roleCode = 2;
		else roleCode = 3;

		// The invitation's complete role selection is copied into the new account.
		User user = new User(username, password, "", "", "", "", "",
				invitation.getAdminRole(), invitation.getRole1(), invitation.getRole2());

		// Unlike the FirstAdmin, we know the email address, so set that into the user as well.
		user.setEmailAddress(invitation.getEmailAddress());

		// Inform the system about which role will be played
		applicationMain.FoundationsMain.activeHomePage = roleCode;

		// Create the account based on user and proceed to the user account update page
		try {
			// Create a new User object with the pre-set role and register in the database
			theDatabase.register(user);
		} catch (SQLException e) {
			System.err.println("*** ERROR *** Database error: " + e.getMessage());
			ViewNewAccount.alertUsernamePasswordError.setTitle("Account Creation Failed");
			ViewNewAccount.alertUsernamePasswordError.setHeaderText(
					"The account could not be saved.");
			ViewNewAccount.alertUsernamePasswordError.setContentText(
					"Check the username and try again.");
			ViewNewAccount.alertUsernamePasswordError.showAndWait();
			return;
		}

		// The account has been set, so remove the invitation from the system
		theDatabase.removeInvitationAfterUse(ViewNewAccount.theInvitationCode);

		// Set the database so it has this user and the current user
		theDatabase.getUserAccountDetails(username);

		// Navigate to the Welcome Login Page
		guiUserUpdate.ViewUserUpdate.displayUserUpdate(ViewNewAccount.theStage, user);
	}

	
	/**********
	 * <p> Method: public performQuit() </p>
	 * 
	 * <p> Description: This method is called when the user has clicked on the Quit button.  Doing
	 * this terminates the execution of the application.  All important data must be stored in the
	 * database, so there is no cleanup required.  (This is important so we can minimize the impact
	 * of crashed.)
	 * 
	 */	
	protected static void performQuit() {
		System.out.println("Perform Quit");
		System.exit(0);
	}	
}
