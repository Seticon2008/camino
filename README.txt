October 11, 2025
Version 2.0.0


PURPOSE:
The user can host their own home server running an Oracle database that stores money transactions made through different means (credit card swipes, bank transfers, cash transactions, etc.).

With the server running, the user can then run one of the client programs on either the same machine or a different machine to connect to the server from anywhere over the Internet in order to access the database and view lists of registered accounts and registered charges within an account.

The client can also manipulate the database to add or remove accounts or charges within an account.


MAIN COMPONENTS:
build_server.sh/bat: Script, compiles and executes Server.java driver class. Should be running 24/7 on server machine for clients to access database at any time.

build_client.sh/bat: Script, compiles and executes ClientInterface.java driver class. Used to access database and view lists of registered accounts and registered charges within an account.

build_client_op.sh/bat: Script, compiles and executes ClientOpInterface.java driver class. Used to view lists of accounts/charges and also to manipulate the database to add or remove accounts or charges within an account.


CLIENT PROGRAM LOOP:
1. User prompted to select an existing account, add a new account, remove an existing account (entering -128), or quit program (entering 0)

2. With an account selected, user prompted to select a month to view or exit selected account (entering 0)

3. With an account and month selected, user prompted to exit selected month (entering 0), add a new charge, or remove an existing charge (entering -128)


SETUP:
Read SETUP.txt