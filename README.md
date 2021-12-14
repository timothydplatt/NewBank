# New Bank
Group 9 Coursework 1, Software Engineering 2 - December 2021, University of Bath.

## Features
Interactive banking portal for enhancing user experience. 

In the welcome page user is presented with options:
- Either to login into system, or
- Sign-up as a new user.

New user can sign-up with his/her details and upon successful validation user’s account is created.

User once logged into the Banking Portal, he/she is provided with below options: 
- Option 1 : Show balance
- Option 2 : Add balance
- Option 3 : Withdraw balance
- Option 4 : Transfer balance
- Option 5 : Send money/pay bill
- Option 6 : Create Bank Account
- Option 7 : Apply for loan
- Option 8 : Logout

## Classes/Functions/Features
NewBankServer Class: Exposing a http service on host and port localhost: 14004

NewBankClient Class: Client service connecting to server running on localhost: 14004

NewBankClientHandler Class: Handling user’s request and input, invoking respective transaction/function to carry-out a banking task.
- Function run() is the main function performing user specific task/function.
This function is also taking care of new user creation and their inputs.

NewBank Class: Constitutes of various Banking transaction specific functions like creating user account, bank account, show balance, transactions.
- Function createUserAccount() is used of creating new user banking account.
- Function createBankAccount() helps user create either Current or Saving account.
- Function showAccountBalance(), show all user’s account and available respective balances
- Function cashTransaction() is used for adding or withdrawing balance from a user’s account
- Function accountTransaction() does the banking transaction like transfer of amount between accounts inter or intra.

Account Class, Customer Class: For creating respective class objects.

PasswordHash Class: Has functionalities for hashing user’s credentials, validating them
- Function createHash() returns a hashed value for a string input
- Function validatePassword() verifies a string value and its respective hash value

Loan Class: Used for handling loan functionalities for bank. It has features like loan request, loan approval and loan repayment.

initializeBankDatabase Class: Constitutes for initializing SQL Lite database tables account and customer, and add dummy data into respective tables.

## Components
Java

PBKDF2_ALGORITHM hashing algorithm

SQL Lite Database - sqlite3
