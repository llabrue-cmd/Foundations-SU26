# Section 4: List All Users Test Cases

## Test Case 1: Open List All Users

Steps:

1. Log in as an admin
2. Click the `List All Users` button

Expected Result:

The List All Users page should open without any errors.

## Test Case 2: Show All Users

Steps:

1. Open the List All Users page
2. Compare the users shown on the page with the users in the database

Expected Results:

Every valid user account should be shown.

## Test Case 3: Show User Information

Steps:

1. Open the List All Users page
2. Check the information shown for each account

Expected Result:

Each user should show their username last name first name and email address.

## Test Case 4: Show User Roles

Steps:
1. Open the List All Users page
2. Check users with different roles

Expected Result:

The correct role or roles should be shown for each user.

## Test Case 5: User With Multiple Roles

Steps:

1. Use a user that has more than one role
2. Open the List All Users page
3. Find that user

Expected Result:

All roles assigned to that user should be shown.
