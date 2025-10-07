/* 
Provides console UI of Bank.java for user to operate on database of credit card charges made 
in different months on different accounts. User can view, add, and remove accounts, months 
of different accounts, and charges made in different months.

TODO:
*/

import java.util.Scanner;

public class ChargeOpInterface extends ChargeViewer
{
	private Scanner scanner;	// scanner object for reading user input
	
	private boolean quitProgramRequest = false;	// if user wants to exit program
	private boolean exitAccountRequest = false;	// if user wants to back out of account
	public boolean exitMonthRequest = false;	// if user wants to back out of month
	
	public String addRequest = "";				// holds account/month name user wants to add
	public boolean removeRequest = false;		// if user wants to remove account/month
	
	// constructor
	public ChargeOpInterface()
	{
		super();
		this.scanner = new Scanner(System.in); // instantiates scanner object
	}
	
	// deselects account and month, resets user requests
	public void resetAll()
	{
		chargeFileManager.resetAll();
		this.exitAccountRequest = false;
		this.resetMonth();
	}
	
	// deselects month, resets user requests
	public void resetMonth()
	{
		chargeFileManager.resetMonth();
		this.exitMonthRequest = false;
		this.addRequest = "";
		this.removeRequest = false;
	}
	
	/* prompts user to select existing account, or add new account, 
		or remove existing account (entering -128), or quit program (entering 0)
	returns true if existing account successfully selected
	
	returns false if:
	*	user desires to quit program (enters 0)
	*	user desires to remove existing account (enters -128)
	*	superclass selectAccount() method returns false
	*	user desires to add new account (enters non-existing account name)
	*/
	public boolean selectAccountOperation()
	{
		boolean accountSelected = false;
		String input = "";
		String operation = "";
		
		System.out.print("\nCuenta deseada: ");
		operation = input = this.scanner.nextLine();
		
		if(operation.equals("0")) // does user want to quit program?
			this.quitProgramRequest = true;
		else if(operation.equals("-128")) // does user want to remove an account?
			this.removeRequest = true;
		else if(chargeFileManager.selectAccount(operation)) // can specified account be selected?
			accountSelected = true;
		else // user entered non-existing account
			this.addRequest = input;
		
		return accountSelected;
	}
	
	/* prompts user to select existing month in selected account, or add new month, 
		or remove existing month (entering -128), or exit account (entering 0)
	returns true if month successfully selected
	
	returns false if:
	*	user enters invalid integer
	*	user desires to exit selected account (enters 0)
	*	user desires to remove existing month in selected account (enters -128)
	*	superclass selectMonth() method returns false
	*	user desires to add new month to selected account (enters non-existing month name)
	*/
	public boolean selectMonthOperation()
	{
		boolean monthSelected = false;
		String input = "";
		int operation = 0;

		System.out.print("\nMes deseado (formato: AAAAMM): ");
		input = this.scanner.nextLine();
		try
		{
			operation = Integer.valueOf(input); // potentially throws exception
			if(operation == 0) // does user want to back out of selected account?
				this.exitAccountRequest = true;
			else if(operation == -128) // does user want to remove a month?
				this.removeRequest = true;
			else if(chargeFileManager.selectMonth(operation)) // can specified month be selected?
				monthSelected = true;
			else // user entered non-existing month
				this.addRequest = input;
		}
		catch(Exception e) // user entered invalid integer
		{
			chargeFileManager.infoMessage = "ERROR: Valor no válida. Inténtalo de nuevo.";
		}
		return monthSelected;
	}
	
	/* prompts user to add new charge to selected month of selected account (entering
		charge ID), or remove existing charge (entering -128), or exit month (entering 0)
	*/
	public void selectChargeOperation()
	{
		String input = "";
		int operation = 0;
		
		System.out.print("\nCarga deseada (formato: DD##): ");
		input = this.scanner.nextLine();
		try
		{
			operation = Integer.valueOf(input); // potentially throws exception
			if(operation == 0) // does user want to back out of month?
				this.exitMonthRequest = true;
			else if(operation == -128) // does user want to remove a charge?
				this.removeRequest = true;
			else // user entered charge ID
				this.addRequest = input;
		}
		catch(Exception e) // user entered invalid integer
		{
			chargeFileManager.infoMessage = "ERROR: Valor no válida. Inténtalo de nuevo.";
		}
	}
	
	/* prompts user to confirm their account creation request, 
		attempts to create new account requested by user
	returns true if account created successfully
	
	returns false if:
	*	user denies confirmation
	*	superclass addAccount() method returns false
	*/
	public boolean addAccount()
	{
		boolean accountAdded = false;
		String accountName = this.addRequest;
		
		if(confirmMessage("Agregar cuenta especificada?")) // does user confirm their request?
			accountAdded = chargeFileManager.addAccount(accountName); // can specified account be added?
		else // user denied confirmation
			chargeFileManager.infoMessage = "Operación detenida.";
		
		this.addRequest = "";
		return accountAdded;
	}
	
	/* prompts user to enter name of account to delete and confirm their 
		account deletion request, attempts to remove account specified by user
	returns true if account deleted successfully
	
	returns false if:
	*	user denies confirmation
	*	superclass removeAccount() method returns false
	*/
	public boolean removeAccount()
	{
		boolean accountRemoved = false;
		String input = "";
		String accountName = "";
		
		System.out.print("Cuenta para borrar: ");
		accountName = input = this.scanner.nextLine();
		
		if(confirmMessage("Borrar cuenta especificada?")) // does user confirm their request?
			accountRemoved = chargeFileManager.removeAccount(accountName); // can specified account be removed?
		else // user denied confirmation
			chargeFileManager.infoMessage = "Operación detenida.";
		
		this.removeRequest = false;
		return accountRemoved;
	}
	
	/* prompts user to enter new month to add to selected account and confirm their
		month creation request, attempts to add month specified by user to selected account
	returns true if month successfully added to selected account
	
	returns false if:
	*	user enters invalid integer
	*	user denies confirmation
	*	superclass addMonth() method returns false
	*/
	public boolean addMonth()
	{
		boolean monthAdded = false;
		int month = 0;
		
		try
		{
			month = Integer.valueOf(this.addRequest); // potentially throws exception
			if(confirmMessage("Agregar mes especificado?")) // does user confirm their request?
				monthAdded = chargeFileManager.addMonth(month); // can specified month be added?
			else // user denied confirmation
				chargeFileManager.infoMessage = "Operación detenida.";
		}
		catch(Exception e) // user entered invalid integer
		{
			chargeFileManager.infoMessage = "ERROR: Valor no válida.";
		}
		this.addRequest = "";
		return monthAdded;
	}
	
	/* prompts user to enter existing month to remove from selected account and confirm 
		their month deletion request, attempts to remove month specified by user from selected account
	returns true if month successfully removed from selected account
	
	returns false if:
	*	user enters invalid integer
	*	user denies confirmation
	*	superclass removeMonth() method returns false
	*/
	public boolean removeMonth()
	{
		boolean monthRemoved = false;
		String input = "";
		int month = 0;

		System.out.print("Mes para borrar: ");
		input = this.scanner.nextLine();
		try
		{
			month = Integer.valueOf(input); // potentially throws exception
			if(confirmMessage("Borrar mes especificado?")) // does user confirm their request?
				monthRemoved = chargeFileManager.removeMonth(month); // can specified month be removed?
			else // user denied confirmation
				chargeFileManager.infoMessage = "Operación detenida.";
		}
		catch(Exception e) // user entered invalid integer
		{
			chargeFileManager.infoMessage = "ERROR: Valor no válida.";
		}
		this.removeRequest = false;
		return monthRemoved;
	}
	
	/* prompts user to enter charge to charges list and confirm their charge creation request, 
		attempts to add charge specified by user to charges list
	WARNING: CHARGE NOT SAVED TO MONTH UNTIL SUPERCLASS saveCharges() METHOD EXECUTED, MUST BE DONE 
		BEFORE EXITING MONTH, OTHERWISE CHANGES LOST
	returns true if charge successfully added to charges list
	
	returns false if:
	*	user enters invalid integer (charge ID)
	*	user enters invalid float (charge amount)
	*	user denies confirmation
	*	superclass addCharge() method returns false
	*/
	public boolean addCharge()
	{
		boolean chargeAdded = false;
		
		String input = this.addRequest;
		int chargeId = 0;
		float chargeAmount = 0f;
		String chargeReason = "";
		
		try
		{
			// get charge id
			chargeId = Integer.valueOf(input); // potentially throws exception
			
			// get charge amount
			System.out.print("Cantidad especificada: ");
			input = this.scanner.nextLine();
			chargeAmount = Float.valueOf(input); // potentially throws exception
			
			// get charge reason
			System.out.print("Razón especificado: ");
			chargeReason = input = this.scanner.nextLine();
			
			// add charge
			if(confirmMessage("Agregar carga especificada?")) // does user confirm their request?
			{
				Charge charge = new Charge(chargeId, chargeAmount, chargeReason);
				chargeAdded = chargeFileManager.addCharge(charge); // can specified charge be added?
			}
			else // user denied confirmation
				chargeFileManager.infoMessage = "Operación detenida.";
		}
		catch(Exception e) // user entered invalid integer or float
		{
			chargeFileManager.infoMessage = "ERROR: Valor no válida.";
		}
		this.addRequest = "";
		return true;
	}
	
	/* prompts user to enter charge ID to remove from charges list and confirm their charge deletion
		request, attempts to remove charge specified by user from charges list
	WARNING: CHARGE NOT REMOVED FROM MONTH UNTIL SUPERCLASS saveCharges() METHOD EXECUTED, MUST BE DONE 
		BEFORE EXITING MONTH, OTHERWISE CHANGES LOST
	returns true if charge successfully removed from charges list
	
	returns false if:
	*	user enters invalid integer
	*	user denies confirmation
	*	superclass removeCharge() method returns false
	*/
	public boolean removeCharge()
	{
		boolean chargeRemoved = false;
		String input = "";
		int chargeId = 0;
		
		// get charge id
		System.out.print("Carga para borrar: ");
		input = this.scanner.nextLine();
		try
		{
			chargeId = Integer.valueOf(input); // potentially throws exception
			if(confirmMessage("Borrar carga especificada?")) // does user confirm their request?
				chargeRemoved = chargeFileManager.removeCharge(chargeId); // can specified charge be removed?
			else // user denied confirmation
				chargeFileManager.infoMessage = "Operación detenida.";
		}
		catch(Exception e) // user entered invalid integer
		{
			chargeFileManager.infoMessage = "ERROR: Valor no válida.";
		}
		this.removeRequest = false;
		return chargeRemoved;
	}	
	
	/* prompts user with confirmation message to confirm their request of operation to database
	returns true if user confirms request (enters "c"/"C", otherwise false */
	public boolean confirmMessage(String message)
	{
		boolean confirmed = false;
		String input = "";
		
		System.out.print(message + " (c/r): ");
		input = this.scanner.nextLine();
		if(input.toLowerCase().equals("c")) // does user confirm their request?
			confirmed = true;
		
		return confirmed;
	}
	
	// driver
	public static void main(String[] args)
	{
		ChargeOpInterface chargeOpInterface = new ChargeOpInterface();
		
		boolean selected = false; // specifies whether user successfully selects existing account/month
		
		while(chargeOpInterface.quitProgramRequest == false) // loops until user wants to exit program
		{
			chargeOpInterface.resetAll();
			chargeOpInterface.displayAccounts();
			selected = chargeOpInterface.selectAccountOperation(); // prompt user to select/add/remove account or exit program
			if(!chargeOpInterface.addRequest.equals("")) // does user want to add an account?
				chargeOpInterface.addAccount();
			else if(chargeOpInterface.removeRequest) // does user want to remove an account?
				chargeOpInterface.removeAccount();
			else if(selected) // did user successfully select existing account?
			{
				while(chargeOpInterface.exitAccountRequest == false) // loops until user wants to exit account selected
				{
					chargeOpInterface.displayMonths();
					selected = chargeOpInterface.selectMonthOperation(); // prompt user to select/add/remove month or exit account
					if(!chargeOpInterface.addRequest.equals("")) // does user want to add a month?
						chargeOpInterface.addMonth();
					else if(chargeOpInterface.removeRequest) // does user want to remove a month?
						chargeOpInterface.removeMonth();
					else if(selected) // did user successfully select existing month in selected account?
					{
						while(chargeOpInterface.exitMonthRequest == false) // loops until user wants to exit month selected
						{
							chargeOpInterface.displayCharges();
							chargeOpInterface.selectChargeOperation(); // prompt user to add/remove charge or exit month
							if(!chargeOpInterface.addRequest.equals("")) // does user want to add a charge?
								chargeOpInterface.addCharge();
							else if(chargeOpInterface.removeRequest) // does user want to remove a charge?
								chargeOpInterface.removeCharge();
						}
						chargeOpInterface.chargeFileManager.saveCharges(); // save charges list to month file
						chargeOpInterface.resetMonth(); // exit month
					}
				}
			}
		}
		System.out.println("Terminando...");
	}
}