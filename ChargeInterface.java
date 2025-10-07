/*
Provides console UI of Bank.java for user to view database of credit card charges made in different months 
on different accounts

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
		chargeFileManager.resetAll();
		this.exitAccountRequest = false;
	}
	
	/* prompts user to select existing account, or quit program (entering 0)
	returns true if account successfully selected
	
	returns false if:
	*	user desires to quit program (enters 0)
	*	superclass selectAccount() method returns false
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
		else if(chargeFileManager.selectAccount(accountName)) // can specified account be selected?
			accountSelected = true;
		
		return accountSelected;
	}
	
	/* prompts user to select existing month in selected account, or exit account (entering 0)
	returns true if month successfully selected
	
	returns false if:
	*	user enters invalid integer
	*	user desires to exit selected account (enters 0)
	*	superclass selectMonth() method returns false
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
			else // user entered month value
				monthSelected = chargeFileManager.selectMonth(month); // can specified month be selected?
		}
		catch(Exception e) // user entered invalid integer
		{
			chargeFileManager.infoMessage = "ERROR: Valor no válida. Inténtalo de nuevo.";
		}
		return monthSelected;
	}
	
	// driver
	public static void main(String[] args)
	{
		ChargeInterface chargeInterface = new ChargeInterface();
		Scanner enter = new Scanner(System.in);
		
		boolean selected = false; // specifies whether user successfully selects existing account/month
		
		while(chargeInterface.quitProgramRequest == false) // loops until user wants to exit program
		{
			chargeInterface.resetAll();
			chargeInterface.displayAccounts();
			selected = chargeInterface.selectAccount(); // prompt user to select account or exit program
			if(selected) // did user successfully select existing account?
			{
				while(chargeInterface.exitAccountRequest == false) // loops until user wants to exit account selected
				{
					chargeInterface.displayMonths();
					selected = chargeInterface.selectMonth(); // prompt user to select month or exit account
					if(selected) // did user successfully select existing month in selected account?
					{
						chargeInterface.displayCharges();
						System.out.println("\nEntrar para continuar.");
						enter.nextLine(); // get user input to exit month
						chargeInterface.chargeFileManager.resetMonth(); // exit month
					}
				}
			}
		}
		System.out.println("Terminando...");
	}
}