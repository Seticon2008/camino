/*
Provides console UI for user to view server database of credit card charges made in 
different months on different accounts.

TODO:
*/
import java.util.Scanner;

public class ChargeInterface extends ChargeViewer
{
	private Scanner scanner;	// scanner object for reading user input
	
	private boolean quitProgramRequest = false;	// if user wants to exit program
	private boolean exitAccountRequest = false;	// if user wants to back out of account
	
	// constructor
	public ChargeInterface()
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
	
	/* prompts user to select existing account, or quit program (entering 0)
	returns true if account successfully selected
	
	returns false if:
	*	user desires to quit program (enters 0)
	*	specified account doesn't exist in list of accounts provided by database
	*/
	public boolean selectAccount()
	{
		boolean accountSelected = false;
		String input = "";
		String accountName = "";
		
		System.out.print("\nCuenta deseada: ");
		accountName = input = this.scanner.nextLine();
		
		if(accountName.equals("0")) // does user want to quit program?
			this.quitProgramRequest = true;
		else if(!accounts.contains(accountName)) // does specified account not exist?
			this.infoMessage = "ERROR: Cuenta " + accountName + " no existe.";
		else // specified account exists
		{
			this.accountName = accountName;
			accountSelected = true;
		}
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
			else if(month <= 190000 || month >= 220000) // is the month value within the valid range?
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
	
	// program loop
	private void mainProcess()
	{
		// prompt user to enter password to server
		System.out.print("Enter password: ");
		String enteredPassword = scanner.nextLine();
		
		// connect to server using specified password
		boolean connected = this.chargeClient.connectToServer(enteredPassword);
		if(connected) // successful connection to server, valid password?
		{
			Scanner enter = new Scanner(System.in);
			
			boolean selected = false; // specifies whether user successfully selects existing account/month
			
			while(quitProgramRequest == false) // loops until user wants to exit program
			{
				resetAll();
				displayAccounts();
				selected = selectAccount(); // prompt user to select account or exit program
				if(selected)
				{
					while(exitAccountRequest == false) // loops until user wants to exit account selected
					{
						printHeader();
						selected = selectMonth(); // prompt user to select month or exit account
						if(selected) // did user successfully select existing month in selected account?
						{
							displayCharges();
							System.out.println("\nEntrar para continuar.");
							enter.nextLine(); // get user input to exit month
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
		ChargeInterface chargeInterface = new ChargeInterface();
		chargeInterface.mainProcess(); // runs program loop
	}
}