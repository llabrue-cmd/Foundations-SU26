# Section 5: Invitation Settings Test Cases

## Automated Test Results

- `InvitationSettingsTest`: passed 22 checks.
- `ManageInvitationsViewSmokeTest`: passed 7 checks.
- Full Java compilation: passed with Java 25.0.4 and JavaFX 25.0.4.
- Section 5-created files were checked for trailing-whitespace errors: passed.

The automated tests use isolated in-memory H2 databases and do not modify the user's normal
`~/FoundationDatabase` file.

## Required Functional Test Cases

| ID | Test | Steps | Expected result | Verification |
|---|---|---|---|---|
| S5-01 | Valid invitation before expiration | Create an invitation with a future deadline, enter its code on Account Setup, and create the account. | Account creation is allowed and the invitation is consumed after registration. | Database validity and consumption are automated. Demonstrate the complete GUI flow in the video. |
| S5-02 | Expired invitation | Create an invitation, change its deadline to a past time for test setup, and enter its code. | The application displays "Expired Invitation" and does not open or create the account. | Expiration logic is automated. Demonstrate the alert in the video. |
| S5-03 | Invitation with Role1 | Create an invitation with only Role1 and create the account. | The new user's `newRole1` value is true; the other roles are false. | Role persistence is automated. |
| S5-04 | Invitation with Admin | Create an invitation with only Admin and create the account. | The new user's `adminRole` value is true; the other roles are false. | Role persistence is automated. |
| S5-05 | Invitation with multiple roles | Select Admin and Role1, send the invitation, and create the account. | Both selected roles are true for the user and Role2 remains false. | Multiple-role user creation is automated. |
| S5-06 | Manage Invitations with no invitations | Consume or remove all invitations, then click Manage Invitations. | The page displays "There are no invitations to manage" and does not crash. | JavaFX scene test passed. |
| S5-07 | Return without saving | Open Manage Invitations, change controls without pressing Save, then press Return. | Stored settings remain unchanged. | JavaFX return-path test passed. |
| S5-08 | Change expiration | Select an invitation, enter a future date and `HH:mm` time, then press Save Invitation. Reopen it. | The new deadline is displayed and remains stored. | Database update/reload is automated; demonstrate GUI editing in the video. |
| S5-09 | Change roles | Select an invitation, change its role checkboxes, save, and reopen it. | Exactly the newly selected roles are displayed and stored. | Database update/reload is automated; demonstrate GUI editing in the video. |
| S5-10 | Restart persistence | Create or edit an invitation, close the database/application, restart, and reopen Manage Invitations. | The invitation, deadline, and roles are still present. | Database reconnect test passed. |

## Input and Error Tests

| ID | Test | Expected result |
|---|---|---|
| S5-11 | Send an invitation without selecting a role | "Select at least one role" is displayed; no invitation is inserted. |
| S5-12 | Save without selecting an invitation | "No Invitation Selected" is displayed; no crash occurs. |
| S5-13 | Save without a date | "Invalid Expiration" is displayed. |
| S5-14 | Enter a time not formatted as `HH:mm` | "Invalid Expiration" is displayed. |
| S5-15 | Save a deadline that is not in the future | The deadline is rejected. |
| S5-16 | Reuse an already consumed code | The normal invalid-invitation message is displayed. |
| S5-17 | Open account setup before expiration but submit after expiration | The second deadline check rejects account creation. |
| S5-18 | Upgrade an original single-role invitation database | The old role is retained and the invitation receives a seven-day compatibility deadline. |

## Regression Checklist

Manually verify these after merging the latest teammate code:

1. First-administrator setup and administrator login.
2. Sending a normal invitation.
3. Creating an account from a normal invitation.
4. Normal login for an existing account.
5. Admin Home navigation.
6. Multiple-role dispatch after a multi-role invited account logs in.
7. Password validation supplied by the Section 1 owner.
8. Delete User, One-Time Password, and List All Users supplied by their section owners.
