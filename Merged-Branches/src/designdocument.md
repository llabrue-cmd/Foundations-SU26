# Section 1: Password Validation

## Purpose

Section 1 adds input validation to every screen where a password is created or changed: the initial admin account setup page, the new user account creation page, and the (future) update-password functionality on the account update and admin home pages. It ensures a password meets the project's password policy before it is accepted, and reports every rule the password fails so the user can correct all issues at once.

## Architecture

The implementation follows the existing JavaFX MVC-style package structure:

- `PasswordValidator` holds all logic for checking a password and its confirmation against the password policy.
- `ValidationResult` (nested class) holds the outcome of a validation call: whether the password passed, and the list of human-readable error messages if it did not.
- Controllers (e.g. `ControllerAdminSetup`, `ControllerUserSetup`, and the future Update Password controller) call `PasswordValidator.validatePassword(password, confirmPassword)` when a password is submitted and display the returned errors, if any.

`PasswordValidator` is a stateless utility class — it does not read from or write to any database, and holds no instance state. All validation happens on the two `String` values passed in.

## Stored Data

`PasswordValidator` does not interact with `userDB` or any other stored data. It performs validation only on the values passed into `validatePassword()` and does not persist, cache, or modify anything. Any change to a user's actual stored password happens elsewhere (in the calling controller/database layer), after validation succeeds.

## Password_Validation Steps

1. The user enters a password and re-enters it in the confirmation field.
2. The calling screen submits both values to `PasswordValidator.validatePassword(password, confirmPassword)`.
3. The method checks the password against every rule in the policy (see below) and collects a message for each rule that fails.
4. A `ValidationResult` is returned. `isValid()` is `true` only if no rules failed.
5. If invalid, the calling screen displays each returned error message so the user can fix every problem in one pass, rather than one at a time.

## Validation and Failure Handling

Rules are evaluated in the following order, and a password may fail more than one at once — every applicable message is returned together:

- **Match** — `password` and `confirmPassword` must be equal.
  Error: `Passwords must match.`
- **Length** — must be at least 8 characters (`longEnough`).
  Error: `password must be atleast 8 character`
- **Allowed characters** — must not contain any character outside `A-Z`, `a-z`, `0-9`, or the allowed special characters (`otherChar`).
  Error: `password can only contain A-Z, a-z, 0-9, ~`!@#$%^&*()_-+{}[]|:,.?/`
- **Uppercase** — must contain at least one uppercase letter A-Z (`upperCase`).
  Error: `password must contain an uppercase letter A-Z`
- **Lowercase** — must contain at least one lowercase letter a-z (`lowerCase`).
  Error: `password must contain a lowercase letter a-z`
- **Numeric** — must contain at least one digit 0-9 (`numericChar`).
  Error: `password must contain a number 0-9`
- **Special character** — must contain at least one character from the allowed special set (`specialChar`).
  Error: `password must contain a special character ~`!@#$%^&*()_-+{}[]|:,.?/`

Allowed special characters (used in both the allowed-character check and the special-character check):
`~ \` ! @ # $ % ^ & * ( ) _ - + { } [ ] | : , . ? /`
