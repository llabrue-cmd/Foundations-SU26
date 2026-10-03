# Section 2: Deleting a User

## Purpose

Section 2 adds functionality to the 'delete user' button already present in the
admin users home page. It will allow the selection and deletion of another 
user from the user database.

## Architecture

The implementation follows the existing JavaFX MVC-style package structure:

- `ControllerAdminHome` controls and provides input boxes for the administrators homepage
- `Database` holds all the functions relating to interacting with stored data, hence the presence of the deletion function.

## Stored Data

The existing `userDB` table keeps its original contents
There's no updates to stored data initially, it's only through the deletion command that stored data is altered.

## Delete_User Steps

1. The administrator clicks the 'delete user' button on their home interface
2. The administrator is given a window with a drop down list of all users.
3. The administrator chooses whether to delete the user with a confirmation window popping up
4. The administrator is told is told if the deletion was a success

## Validation and Failure Handling

- Admin cannot be the only valid user to delete 
