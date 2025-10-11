/*
Provides console UI for user to view server database of credit card charges made in 
different months on different accounts. User can view, add, and remove accounts and 
charges made in different accounts.

TODO:
*/
import java.util.Scanner;

public class ChargeOpInterface extends ChargeViewer
{
	private Scanner scanner;	// scanner object for reading user input
	
	private boolean quitProgramRequest = false;	// if user wants to exit program
	private boolean exitAccountRequest = false;	// if user wants to back out of account
	private boolean exitMonthRequest = false;	// if user wants to back out of month
	
	private String addRequest = "";				// holds account/month name user wants to add
	private boolean removeRequest = false;		// if user wants to remove account/month
	
	// constructor
	public ChargeOpInterface()
	{
		super();
		this.scanner = new Scanner(System.in); // instantiates scanner object
	}
	
	// deselects account and month, resets user account exit request
	public void resetAll()
	{
		super.resetAll();
		this.exitAccountRequest = false;
	}
	
	// deselects month, resets user requests
	public void resetMonth()
	{
		super.resetMonth();
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
		else if(accounts.contains(operation)) // does specified account exist?
		{
			this.accountName = operation;
			accountSelected = true;
		}
		else // user entered non-existing account
			this.addRequest = input;
		
		return accountSelected;
	}
	
	/* prompts user to select month in selected account, or exit account (entering 0)
	returns true if month successfully selected
	
	returns false if:
	*	user enters invalid integer
	*	user desires to exit selected account (enters 0)
	*	user enters integer outside valid range
	*/
	public boolean selectMonth()
	{
		boolean monthSelected = false;
		String input = "";
		int month = 0;
		
		System.out.print("\nMes deseado (formato: AAAAMM): ");
		input = this.scanner.nextLine();
		try
		{
			month = Integer.valueOf(input); // potentially throws exception
			if(month == 0) // does user want to quit program?
				this.exitAccountRequest = true;
			else if(month < 190000 || month > 220000) // is the month value within the valid range?
				this.infoMessage = "ERROR: Valor fuera de rango.";
			else // user entered month value
			{
				this.month = month;
				monthSelected = true;
			}
		}
		catch(Exception e) // user entered invalid integer
		{
			infoMessage = "ERROR: Valor no válida. Inténtalo de nuevo.";
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
			infoMessage = "ERROR: Valor no válida. Inténtalo de nuevo.";
		}
	}
	
	/* prompts user to confirm their account creation request, attempts to create 
		new account requested by user in server database
	returns true if account created successfully
	
	returns false if:
	*	user denies confirmation
	*	chargeClient addAccount() method returns false
	*/
	public boolean addAccount()
	{
		boolean accountAdded = false;
		String accountName = this.addRequest;
		
		if(confirmMessage("Agregar cuenta especificada?")) // does user confirm their request?
		{
			accountAdded = chargeClient.addAccount(accountName);
			if(accountAdded) // was specified account added?
				this.infoMessage = "Cuenta agregada.";
			else // failed to create account
				this.infoMessage = "ERROR: Operación ha fallado.";
		}
		else // user denied confirmation
			infoMessage = "Operación detenida.";
		
		this.addRequest = "";
		return accountAdded;
	}
	
	/* prompts user to enter name of account to delete and confirm their 
		account deletion request, attempts to remove account specified by user
		from server database
	returns true if account deleted successfully
	
	returns false if:
	*	user denies confirmation
	*	chargeClient removeAccount() method returns false
	*/
	public boolean removeAccount()
	{
		boolean accountRemoved = false;
		String input = "";
		String accountName = "";
		
		System.out.print("Cuenta para borrar: ");
		accountName = input = this.scanner.nextLine();
		
		if(confirmMessage("Borrar cuenta especificada?")) // does user confirm their request?
		{
			accountRemoved = chargeClient.removeAccount(accountName);
			if(accountRemoved) // was specified account removed?
				this.infoMessage = "Cuenta borrada.";
			else // failed to remove account
				this.infoMessage = "ERROR: Operación ha fallado.";
		}
		else // user denied confirmation
			infoMessage = "Operación detenida.";
		
		this.removeRequest = false;
		return accountRemoved;
	}
	
	/* prompts user to enter charge to add and confirm their charge creation request, 
		attempts to add charge specified by user to server database
	returns true if charge added successfully
	
	returns false if:
	*	user enters invalid integer (charge ID)
	*	user enters invalid float (charge amount)
	*	user denies confirmation
	*	chargeClient addCharge() method returns false
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
			chargeId = Integer.valueOf(String.valueOf(month) + input); // potentially throws exception
			System.out.println(chargeId); // debugging
			
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
				chargeAdded = chargeClient.addCharge(accountName, charge);
				if(chargeAdded) // was specified charge added?
					this.infoMessage = "Carga agregada.";
				else // failed to add charge
					this.infoMessage = "ERROR: Operación ha fallado.";
			}
			else // user denied confirmation
				infoMessage = "Operación detenida.";
		}
		catch(Exception e) // user entered invalid integer or float
		{
			infoMessage = "ERROR: Valor no válida.";
		}
		this.addRequest = "";
		return chargeAdded;
	}
	
	/* prompts user to enter charge ID to remove and confirm their charge deletion
		request, attempts to remove charge specified by user from server database
	returns true if charge removed succesfully
	
	returns false if:
	*	user enters invalid integer
	*	user denies confirmation
	*	chargeClient removeCharge() method returns false
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
			chargeId = Integer.valueOf(String.valueOf(month) + input); // potentially throws exception
			Charge charge = charges.get(chargeId);
			if(charge != null)
				if(confirmMessage("Borrar carga especificada?")) // does user confirm their request?
				{
					chargeRemoved = chargeClient.removeCharge(accountName, charge);
					if(chargeRemoved) // was specified charge removed?
						this.infoMessage = "Carga borrada.";
					else // failed to remove charge
						this.infoMessage = "ERROR: Operación ha fallado.";
				}
				else // user denied confirmation
					infoMessage = "Operación detenida.";
			else // user entered non-existing charge ID
				infoMessage = "ERROR: Carga " + String.valueOf(chargeId) + " no existe.";
		}
		catch(Exception e) // user entered invalid integer
		{
			infoMessage = "ERROR: Valor no válida.";
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
	
	// program loop
	private void mainProcess()
	{
		System.out.print("Enter password: ");
		String enteredPassword = scanner.nextLine();
		boolean connected = this.chargeClient.connectToServer(enteredPassword);
		if(connected)
		{
			//Scanner enter = new Scanner(System.in);
			
			boolean selected = false; // specifies whether user successfully selects existing account/month
			
			while(quitProgramRequest == false) // loops until user wants to exit program
			{
				resetAll();
				displayAccounts();
				selected = selectAccountOperation(); // prompt user to select account or exit program
				if(!addRequest.equals(""))
					addAccount();
				else if(removeRequest == true)
					removeAccount();
				else if(selected)
				{
					while(exitAccountRequest == false) // loops until user wants to exit account selected
					{
						printHeader();
						selected = selectMonth(); // prompt user to select month or exit account
						if(selected) // did user successfully select existing month in selected account?
						{
							while(exitMonthRequest == false)
							{
								displayCharges();
								selectChargeOperation();
								if(!addRequest.equals(""))
									addCharge();
								else if(removeRequest == true)
									removeCharge();
							}
							resetMonth(); // exit month
						}
					}
				}
			}
			System.out.println("Terminando...");
		}
	}
	
	// driver
	public static void main(String[] args)
	{
		ChargeOpInterface chargeOpInterface = new ChargeOpInterface();
		chargeOpInterface.mainProcess(); // run program loop
	}
}