# Section 2: Delete_User

## Test Results

- `Delete_User`: passed 5 checks.
- Full Java compilation: passed with Java 25.0.4 and JavaFX 25.0.4.

## Required Functional Test Cases

| ID | Test | Steps | Expected result | Verification |
|---|---|---|---|---|
| S2-01 | Window Shows All Users For Deletion | Create 3-4 Users, and use the Delete_User button on an admin account | All users will be displayed in the dropdown | Will Demonstrate the complete GUI flow in the video. |
| S2-02 | Deleting a user fully removes them  | Delete one of the 3-4 users made in test 01 | User count should drop, and user should no longer be present in the database | Will Demonstrate the complete GUI flow in the video. |
| S2-03 | Clicking 'no' stops user deletion   | Select one of the users in test 01 then click 'no' when asked for confirmation | User shouldn't be deleted and window should close | Will Demonstrate the complete GUI flow in the video. |

## Input and Error Tests

| ID | Test | Expected result |
|---|---|---|
| S2-04 | Check that you cannot delete yourself if you're the only user | Error window should display 'Cannot delete yourself' |
| S2-05 | Check that you cannot delete yourself if there are other users | Dropdown should not contain your name |

## Regression Checklist

Manually verify these after merging the latest teammate code:

1. Users still appear in the drop down list
2. You still can't delete yourself
3. Users are still deleted successfully
