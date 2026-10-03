# Section 3: One Time Password Functionality

## Test Results

- All checks below passed.

## Required Functional Test Cases

| ID | Test | Steps | Expected Result | Verification |
|---|---|---|---|---|
| S3-01 | One Time Password set properly | Navigate to the admin home menu and set a one time password for any user | The password set for the user should be the same as the one time password the admin enters | Demonstrated in Video |
| S3-02 | User can log in with One Time Password | Enter the user and associated one time password and attempt to log in | The user should be allowed in and taken to the designated home page | Demonstrated in Video |
| S3-03 | User can change password | Navigate to user account info and input new password | The new password should replace the previous one | Demonstrated in Video |
| S3-04 | User can log in with new password | Attempt to log in using new password with same username | The user should be allowed in and taken to the designated homepage | Demonstrated in Video |
| S3-05 | One time password fails on second use | Attempt to log in using the one time password again | Log in system should provide an error stating invalid combination | Demonstrated in Video |

## Input and Error Tests

| ID | Test | Expected Result |
|---|---|---|
| S3-06 | Attempt to log in with null password | Error message stating invalid combination |
| S3-07 | Attempt to log in with one time password again without having set new password | Log in fails and must get new one time password |

## Regression Checklist 

Manually verify these after merging with latest teammate code:
1. Drop down list of users still works
2. Passwords are validated properly by passwordvalidator
3. One time password is cleared properly
4. Users can still log in with one time password
