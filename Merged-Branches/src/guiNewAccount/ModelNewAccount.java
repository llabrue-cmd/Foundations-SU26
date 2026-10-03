package guiNewAccount;

import userNameRecognizer.UserNameRecognizer;
import java.util.List;
import passwordValidator.PasswordValidator;

/*******
 * <p> Title: ModelNewAccount Class. </p>
 * 
 * <p> Description: The NewAccount Page Model. This class contains the input-validation
 * rules used while creating an account. Keeping these rules in the model prevents the
 * controller and view from duplicating validation logic.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-15 Initial version
 * @version 1.01		2026-09-15 Added MVC-based username and password validation
 *  
 */
public class ModelNewAccount {

	/**
	 * Validates a proposed username using the HW1 finite-state recognizer.
	 *
	 * @param username proposed account username
	 * @return an empty string when valid; otherwise, a helpful error message
	 */
	protected static String validateUsername(String username) {
		return UserNameRecognizer.checkForValidUserName(username);
	}

	/**
	 * Validates that the password and confirmation fields match.
	 *
	 * @param password proposed account password
	 * @param confirmation repeated password entered by the user
	 * @return an empty string when valid; otherwise, a helpful error message
	 */
	protected static String validatePasswords(String password, String confirmation) {
		if (!password.equals(confirmation)) {
			return "The two passwords must be identical.";
		}

		/** Check password validity against full ruleset */
		PasswordValidator.ValidationResult validationResult = passwordValidator.PasswordValidator.validatePassword(password, confirmation);
		List<String> errors = validationResult.getErrors();
		String returnError = errors.get(0);
		
		return returnError;
	}

}
