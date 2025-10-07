August 6, 2025
Version 1.1.1


MAIN COMPONENTS:
ChargeFileManager.java: Superclass, handles all file operations such as creating and removing account files, month files within accounts, and charges within a month.

ChargeViewer.java: extends ChargeFileManager.java, provides essential console UI methods inherited by driver classes

ChargeInterface.java: Driver class, extends ChargeViewer.java, provides console UI for user to view database. Doesn't allow user to manipulate database.

ChargeOpInterface.java: Driver class, extends ChargeViewer.java, provides console UI for user to view database as well as add and remove accounts, months within accounts, and charges within months.


THE USER CAN:
View list of registered accounts, months within an account, or registered charges within a month of an account.

Add or remove accounts, months within an account, or charges within a month of an account.


PROGRAM LOOP:
1. User prompted to select an existing account, add a new account, remove an existing account (entering -128), or quit program (entering 0)

2. With an account selected, user prompted to select a month registered in that account, add a new month, remove an existing month (entering -128), or exit selected account (entering 0)

3. With an account and month selected, user prompted to exit selected month (entering 0), add a new charge, or remove an existing charge (entering -128)


SETUP:
1. Rename directory to "camino"

2. Place directory in file system like so: C:\programs\camino

3. Compile and run a driver class from command prompt


ALSO:
"simbolos" directory contains headers you can use for different banks
To use them:
-once new account created, replace simbolo.txt in account in camino/cuentas/*accountname*/simbolos directory with desired header text file from camino/simbolos directory

-rename header text file to simbolo.txt