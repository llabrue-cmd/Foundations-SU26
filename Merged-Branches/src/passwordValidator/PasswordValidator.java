package passwordValidator;

import java.util.ArrayList;
import java.util.List;

/**
 * PasswordValidator - Spencer Rich
 * Usage:
 *
 *   ValidationResult passwordResult = PasswordValidator.validatePassword("Passw0rd!", "Passw0rd!");
 *   if (!passwordResult.isValid()) {
 *       for (String error : passwordResult.getErrors()) {
 *           System.out.println(error);
 *       }
 *   }
 */

public class PasswordValidator {

    // Allowed special characters for passwords:
    // ~ ` ! @ # $ % ^ & * ( ) _ - + { } [ ] | : , . ? /
    private static final String ALLOWED_SPECIAL_CHARS = "~`!@#$%^&*()_-+{}[]|:,.?/";
    private static final int PASSWORD_MIN_LENGTH = 8;

    /**
     * Validates a password (and its confirmation) against the following rules,
     * checked in this order:
     *  1. Password and confirmation must match.
     *  2. Must be at least 8 characters long.
     *  3. Must not contain any character outside the allowed set.
     *  4. Must contain an uppercase letter (A-Z).
     *  5. Must contain a lowercase letter (a-z).
     *  6. Must contain a number (0-9).
     *  7. Must contain a special character from the allowed set.
     */

    public static ValidationResult validatePassword(String password, String confirmPassword) {
        List<String> errors = new ArrayList<>();

        if (password == null) {
            password = "";
        }
        if (confirmPassword == null) {
            confirmPassword = "";
        }

        // Rule: passwords must match
        boolean match = password.equals(confirmPassword);
        if (!match) {
            errors.add("Passwords must match.");
        }

        // Set each condition flag to track validity
        boolean longEnough = password.length() >= PASSWORD_MIN_LENGTH;
        boolean otherChar = false;
        boolean upperCase = false;
        boolean lowerCase = false;
        boolean numericChar = false;
        boolean specialChar = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (Character.isUpperCase(c)) {
                upperCase = true;
            } else if (Character.isLowerCase(c)) {
                lowerCase = true;
            } else if (Character.isDigit(c)) {
                numericChar = true;
            } else if (isSpecialChar(c)) {
                specialChar = true;
            } else {
                // Character is not a letter, digit, or an allowed special character
                otherChar = true;
            }
        }

        // Rule: must be at least 8 characters
        if (!longEnough) {
            errors.add("password must be atleast 8 character");
        }

        // Rule: no disallowed characters
        if (otherChar) {
            errors.add("password can only contain A-Z, a-z, 0-9, " + ALLOWED_SPECIAL_CHARS);
        }

        // Rule: must contain an uppercase letter
        if (!upperCase) {
            errors.add("password must contain an uppercase letter A-Z");
        }

        // Rule: must contain a lowercase letter
        if (!lowerCase) {
            errors.add("password must contain a lowercase letter a-z");
        }

        // Rule: must contain a number
        if (!numericChar) {
            errors.add("password must contain a number 0-9");
        }

        // Rule: must contain a special character
        if (!specialChar) {
            errors.add("password must contain a special character " + ALLOWED_SPECIAL_CHARS);
        }

        return new ValidationResult(errors);
    }

    // Helper methods for validation

    private static boolean isSpecialChar(char c) {
        return ALLOWED_SPECIAL_CHARS.indexOf(c) >= 0;
    }

    /**
     * Simple container for validation results: whether the input passed,
     * and the list of human-readable error messages (if any).
     */
    public static class ValidationResult {
        private final List<String> errors;

        public ValidationResult(List<String> errors) {
            this.errors = errors;
        }

        public boolean isValid() {
            return errors.isEmpty();
        }

        public List<String> getErrors() {
            return errors;
        }
    }
}
