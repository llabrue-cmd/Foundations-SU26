# Section 5: Invitation Settings Functionality

## Purpose

Section 5 completes the existing Manage Invitations feature. Administrators can create an
invitation with a validity period and one or more roles, review outstanding invitations, and
change each invitation's deadline or roles. Account creation rejects an invitation after its
deadline and copies all selected roles into the new user account.

## Architecture

The implementation follows the existing JavaFX MVC-style package structure:

- `ViewAdminHome` collects the email address, role checkboxes, and validity duration.
- `ControllerAdminHome` validates the input and asks `Database` to create the invitation.
- `ViewManageInvitations` displays stored invitations and their editable settings.
- `ControllerManageInvitations` validates and saves changes or returns without saving.
- `Invitation` represents the invitation code, email, expiration, and role flags.
- `ViewNewAccount` validates the invitation before displaying account setup.
- `ControllerNewAccount` validates it again immediately before registration and constructs the
  `User` using every selected role.
- `Database` owns all H2 schema creation, migration, prepared queries, updates, and deletion.

## Stored Data

The existing `InvitationCodes` table keeps its original `code`, `emailAddress`, and `role`
columns. Section 5 adds:

- `expiration TIMESTAMP`
- `adminRole BOOLEAN`
- `newRole1 BOOLEAN`
- `newRole2 BOOLEAN`

The original `role` column is retained so older code and invitation rows remain compatible. On
the first upgraded startup, existing invitations receive a deadline seven days in the future and
their original single role is copied into the corresponding Boolean role field. Newly created
invitations always receive a deadline and at least one role.

## Invitation Flow

1. The administrator enters an email address on Admin Home.
2. The administrator selects one or more roles and a validity duration.
3. The controller validates all input before calling the database.
4. The database stores the generated code, expiration timestamp, and role flags.
5. Manage Invitations loads the stored `Invitation` objects into a selector.
6. The administrator can change the selected invitation's deadline or roles and save it.
7. An invited user enters the code on the login page.
8. Account setup checks that the invitation exists, has roles, and is not expired.
9. Account submission repeats the check to prevent expiration while the form is open.
10. The new `User` receives exactly the invitation's role flags.
11. After registration succeeds, the invitation is removed using its actual invitation code.

## Validation and Failure Handling

- At least one role is required.
- New invitation durations must be positive.
- Edited deadlines must include a date and a valid 24-hour `HH:mm` time.
- Edited deadlines must be in the future.
- Missing, invalid, and expired invitations are rejected before registration.
- Database insert or update failures display an error instead of terminating the application.
- Opening Manage Invitations with no rows produces a clear empty state.
- Returning from Manage Invitations without saving performs no database update.

## Design Decisions and Assumptions

- The existing three-role Boolean model is reused instead of adding a second role system.
- Admin Home uses a small list of validity durations to avoid adding complex date/time controls to
  the already crowded home page. Exact date/time editing is available on Manage Invitations.
- Expiration uses the computer's local date and time because the existing single-user application
  has no timezone or server-time abstraction.
- Invitations are consumed only after `Database.register` succeeds.
- Section 1 password validation remains owned by its teammate and was not rewritten.
