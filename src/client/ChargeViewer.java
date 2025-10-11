/* Provides methods to display lists of registered accounts and charges 
in the server charge database in a console interface.

TODO:
*/
import java.util.List;
import java.util.ArrayList;

public class ChargeViewer
{
	protected ChargeClient chargeClient = null;
	
	protected List<String> accounts = null;	// list of existing accounts
	protected String accountName = "";		// name of selected account
	protected int month = 0;				// selected month of selected account
	protected ChargeList charges = null;	// contains registered charges in selected month of selected account
	
	public String infoMessage = "";			// gives helpful error message
	
	// constructor
	public ChargeViewer()
	{
		chargeClient = new ChargeClient();	// NOTE: SUBCLASS MUST CONNECT TO SERVER
		this.charges = new ChargeList();	// instantiates empty charges list
	}
	
	// deselects account and month, empties charges list
	public void resetAll()
	{
		this.accountName = "";
		this.resetMonth();
	}
	
	// deselects month, empties charges list (account still selected)
	public void resetMonth()
	{
		this.month = 0;
		this.charges.clear();
	}
	
	// displays list of existing accounts in system
	public void displayAccounts()
	{
		this.printHeader();
		
		accounts = chargeClient.getAccounts();
		System.out.println("Cuentas registradas:");
		if(accounts.size() <= 0)
			System.out.println("No hay ninguna cuenta.");
		else
		{
			for(String accountName : accounts)
				System.out.println(accountName);
		}
	}
	
	/* displays list of charges registered in selected month of selected account
	charges displayed in column format (day of month + charge of day, amount, reason)
	*/
	public void displayCharges()
	{
		this.printHeader();
		
		this.charges = chargeClient.getCharges(this.accountName, this.month);
		String format = "%-12s%12s%40s%n";
		
		System.out.println("Cargas:");
		if(this.charges.size() <= 0)
			System.out.println("No hay ninguna carga.");
		else
		{
			System.out.printf(format, "id", "cantidad", "razon");
			for(Charge charge : this.charges)
				System.out.printf(format, String.format("%04d", charge.getId() % 1000), String.format("%.02f", charge.getAmount()), charge.getReason());
		}
	}
	
	// clears console screen
	public void clearScreen()
	{
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
	
	// displays name of currently selected account, currently selected month, info/error message if any
	public void printHeader()
	{
		this.clearScreen();
		
		if(accountName.equals(""))
			System.out.println("Ninguna cuenta seleccionada.");
		else
		{
			System.out.print(accountName);
			if(month > 0)
				System.out.println(" " + String.valueOf(month));
			else
				System.out.println();
		}
		System.out.println(infoMessage);
		infoMessage = "";
		System.out.println();
	}
}