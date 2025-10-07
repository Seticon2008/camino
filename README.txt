November 22, 2024
Version 1.0.0


COMPONENTS:
Camino.java: console UI program, manages database of registered charges made in different accounts in different months.


THE USER CAN:
View list of registered accounts, months within an account, or registered charges within a month of an account

Add or remove accounts, months within an account, or charges within a month of an account


PROGRAM LOOP:
1. User prompted to select an existing account, add a new account, remove an existing account (entering -1), or quit program (entering 0)

2. With an account selected, user prompted to select a month registered in that account, add a new month, remove an existing month (entering -1), or exit selected account (entering 0)

3. With an account and month selected, user prompted to exit selected month (entering 0), add a new charge (entering 1), or remove an existing charge (entering 2)


SETUP:
Place directory in file system like so: C:\programs\camino
Compile and run Camino.java from command prompt


ALSO:
simbolos directory contains headers you can use for different banks
To use them:
-once new account created, replace simbolo.txt in account in camino/cuentas/*accountname*/simbolos
 directory with desired header text file from camino/simbolos directory
-rename header text file to simbolo.txt