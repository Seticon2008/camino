/*
Provides console UI of Bank.java for user to view database of credit card charges made in different months 
on different accounts

TODO:
*/
import java.util.Scanner;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class BankInterface extends Bank
{
	protected Scanner scanner;	// scanner object for reading user input
	
	public boolean quitProgramRequest = false;	// if user wants to exit program
	public boolean exitAccountRequest = false;	// if user wants to back out of account
	
	// constructor
	public BankInterface()
	{
		super();
		this.scanner = new Scanner(System.in); // instantiates scanner object
	}
	
	// deselects account and month, resets user account exit request
	@Override
	public void resetAll()
	{
		super.resetAll();
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
		else if(this.selectAccount(accountName)) // can specified account be selected?
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
				monthSelected = selectMonth(month); // can specified month be selected?
		}
		catch(Exception e) // user entered invalid integer
		{
			this.infoMessage = "ERROR: Valor no válida. Inténtalo de nuevo.";
		}
		return monthSelected;
	}
	
	// displays list of existing accounts in system
	public void displayAccounts()
	{
		this.printHeader();
		
		System.out.println("Cuentas registradas:");
		List<String> accounts = this.getAccounts();
		if(accounts.size() <= 0)
			System.out.println("No hay ninguna cuenta.");
		else
		{
			for(String accountName : accounts)
				System.out.println(accountName);
		}
	}
	
	// displays list of months registered in currently selected account
	public void displayMonths()
	{
		this.printHeader();
		
		System.out.println("Meses registrados:");
		List<String> months = this.getMonths();
		if(months.size() <= 0)
			System.out.println("No hay ningún mes.");
		else
		{
			for(String month : months)
				System.out.println(month);
		}
	}
	
	/* displays list of charges registered in selected month of selected account
	charges displayed in column format (day of month + charge of day, amount, reason)
	*/
	public void displayCharges()
	{
		this.printHeader();
		
		ChargeList charges = this.getCharges();
		String format = "%-8s%16s%40s%n";
		
		System.out.println("Cargas:");
		if(charges.size() <= 0)
			System.out.println("No hay ninguna carga.");
		else
		{
			System.out.printf(format, "fecha", "cantidad", "razón");
			for(Charge charge : charges)
				System.out.printf(format, String.format("%04d", charge.getId()), String.format("%.02f", charge.getAmount()), charge.getReason());
		}
	}
	
	// clears console screen
	public void clearScreen()
	{
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
	
	// displays banner text for currently selected account, or general banner for program (B logo)
	public void printHeader()
	{
		String accountName = this.getAccount();
		int month = this.getMonth();
		
		clearScreen();
		System.out.println(this.getAccountHeader());
		
		System.out.print(accountName);
		if(this.getMonth() > 0)
			System.out.println(" " + String.valueOf(month));
		else
			System.out.println();
		
		System.out.println(this.infoMessage);
		this.infoMessage = "";
		System.out.println();
	}
	
	// driver
	public static void main(String[] args)
	{
		BankInterface bank = new BankInterface();
		Scanner enter = new Scanner(System.in);
		
		boolean selected = false; // specifies whether user successfully selects existing account/month
		
		while(bank.quitProgramRequest == false) // loops until user wants to exit program
		{
			bank.resetAll();
			bank.displayAccounts();
			selected = bank.selectAccount(); // prompt user to select account or exit program
			if(selected) // did user successfully select existing account?
			{
				while(bank.exitAccountRequest == false) // loops until user wants to exit account selected
				{
					bank.displayMonths();
					selected = bank.selectMonth(); // prompt user to select month or exit account
					if(selected) // did user successfully select existing month in selected account?
					{
						bank.displayCharges();
						System.out.println("\nEntrar para continuar.");
						enter.nextLine(); // get user input to exit month
						bank.resetMonth(); // exit month
					}
				}
			}
		}
		System.out.println("Terminando...");
	}
}