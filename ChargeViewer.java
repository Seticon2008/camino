/* Provides methods to display lists of registered accounts, months, and charges.

TODO:
*/
import java.util.List;

public class ChargeViewer
{
	protected ChargeFileManager chargeFileManager = null;
	
	public ChargeViewer()
	{
		chargeFileManager = new ChargeFileManager();
	}
	
	// displays list of existing accounts in system
	public void displayAccounts()
	{
		this.printHeader();
		
		System.out.println("Cuentas registradas:");
		List<String> accounts = chargeFileManager.getAccounts();
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
		List<String> months = chargeFileManager.getMonths();
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
		
		ChargeList charges = chargeFileManager.getCharges();
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
		String accountName = chargeFileManager.getAccount();
		int month = chargeFileManager.getMonth();
		
		clearScreen();
		System.out.println(chargeFileManager.getAccountHeader());
		
		System.out.print(accountName);
		if(chargeFileManager.getMonth() > 0)
			System.out.println(" " + String.valueOf(month));
		else
			System.out.println();
		
		System.out.println(chargeFileManager.infoMessage);
		chargeFileManager.infoMessage = "";
		System.out.println();
	}
}