# Section 3: One Time Password

## Purpose

Section 3 adds the One Time Password functionality, which allows admin users to set one time passwords for users. Users can then use this password to log into their accounts and reset their passwords. The one time passwords are temporary and cleared after the first usage.

## Architecture

The implementation follows the existing JavaFX MVC-style package structure:
- 'ControllerAdminHome' contains the button to create the one time password and asks for admin user input. It stores the one time password into a global string, which is passed to different sections of the project.
- 'ControllerUserLogin' allows users to log in with the one time password and checks for validity.
- 'Database' holds functions which are used to allow one time password submissions while bypassing the usual check for a matching password in the database table.
- 'ViewUserUpdate' allows users to update their passwords after logging in with the one time password.

## Stored Data
The 'userDB' table password value is altered after users update their passwords. Aside from that, no other changes are made.

## One Time Password Set Up Steps
1. The admin presses the set one time password button
2. They are provided a drop down menu of users and select a user.
3. They are asked to create the password.
4. This password is stored in a global variable for validation purposes in the log in process.
5. The log in fields check whether the given password matches the one-time password and allows users in if so.
6. The one time password is set to null and users are prompted to create a new password.
7. The new password is stored and replaces the user's previous password.

## Validation and Failure Handling
1. Log in checks whether the given password is null, reports it as incorrect if so.
2. Log in stores newly set password and checks it separately in case of old password not being set properly.
