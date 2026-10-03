# Section 1: Password_Validation

## Test Results

- `PasswordValidator`: passed all checks below.
- Full Java compilation: passed with Java 25.0.4 and JavaFX 25.0.4.

## Required Functional Test Cases

| ID | Test | Steps | Expected result | Verification |
|----|------|-------|------------------|---------------|
| S1-01 | Valid password is accepted | Enter `Passw0rd!` in both password fields and submit | `isValid()` returns true, no error messages returned | Will demonstrate the complete GUI flow in the video. |
| S1-02 | Non-matching passwords are rejected | Enter `Passw0rd!` in the first field and `Different1!` in the confirmation field | Error message `Passwords must match.` is returned | Will demonstrate the complete GUI flow in the video. |
| S1-03 | Matching passwords pass the match check | Enter the same valid password in both fields | `Passwords must match.` is NOT in the returned errors | Will demonstrate the complete GUI flow in the video. |

## Input and Error Tests

| ID | Test | Expected result |
|----|------|------------------|
| S1-04 | Password shorter than 8 characters (e.g. `Pw0!`) | Error `password must be atleast 8 character` is returned |
| S1-05 | Password of exactly 8 characters meeting all other rules (e.g. `Passw0r!`) | No length error is returned |
| S1-06 | Password containing a character outside the allowed set (e.g. `Passw0rd!"`, using `"`) | Error `password can only contain A-Z, a-z, 0-9, ~`!@#$%^&*()_-+{}[]|:,.?/` is returned |
| S1-07 | Password with no uppercase letter (e.g. `passw0rd!`) | Error `password must contain an uppercase letter A-Z` is returned |
| S1-08 | Password with no lowercase letter (e.g. `PASSW0RD!`) | Error `password must contain a lowercase letter a-z` is returned |
| S1-09 | Password with no digit (e.g. `Password!`) | Error `password must contain a number 0-9` is returned |
| S1-10 | Password with no special character (e.g. `Passw0rd`) | Error `password must contain a special character ~`!@#$%^&*()_-+{}[]|:,.?/` is returned |
| S1-11 | Password failing multiple rules at once (e.g. `abc`) | All applicable errors are returned together (match if confirmation differs, length, uppercase, number, special character) |
| S1-12 | Null password or confirmation passed in | Treated as an empty string; does not throw an exception, fails length/uppercase/lowercase/numeric/special checks as expected |

## Regression Checklist

Manually verify these after merging the latest teammate code:

1. A password meeting every rule is still accepted with no errors.
2. Mismatched password/confirmation still returns the match error.
3. Every one of the six character-class/length rules still returns its own distinct, correctly worded error message when violated.
