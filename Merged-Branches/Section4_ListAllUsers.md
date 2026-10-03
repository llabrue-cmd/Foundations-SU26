# Section 4: List All Users

## Purpose

The purpose of this section is to make the existing `List All Users` button work.

The page will show every valid user account in the system along with their username first name last name email and roles.

## Architecture

This feature will follow the same structure already used in the project.

- `ViewAdminHome` already has the List All Users button
- `ControllerAdminHome` will open the List All Users page
- `ViewListUsers` will display the user information
- `ControllerListUsers` will handle the buttons on the page
- `Database` will get the users from the database
- `User` already stores the information needed for each account

## Stored Data

The user information is already stored in the `userDB` table.

This feature will only read the information. It will not change or delete anything.

The page will display:

- Username
- Last name
- First name
- Email address
- Roles

## List All Users Flow

1. The admin logs into the program
2. The admin clicks `List All Users`
3. The program gets all users from the database
4. The List All Users page opens
5. The account information is shown in a table
6. The admin can return to the admin home page

## Error Handling

- The page should not crash if a user has missing information
- Users with multiple roles should show all of their roles
- The list should refresh when the page is opened again
