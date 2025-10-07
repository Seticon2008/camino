/*
Operates on database of credit card charges made in different months on different 
accounts.
Provided methods allow for:
*	getting list of existing accounts, or existing months within an account, or 
	registered charges within a month
*	adding or removing accounts, months within an account, and charges within a month

TODO:
Selecting an account allows for illegal filename characters. User can cause program to throw exception 
if they enter "disc5111/meses" for an account name, for example. Need to create a separate method to 
validate account names that will be used not only when adding an account, but also selecting and removing
accounts.
*/

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.file.Files;

import java.util.List;
import java.util.LinkedList;

public class ChargeFileManager
{
	private String mainPath = "C:/programs/camino";				// main directory
	private String accountsPath = mainPath + "/cuentas";		// directory storing accounts
	private String binPath = mainPath + "/papelera";			// trash directory for deleted files
	
	private String accountName = "";	// name of selected account
	private int month = 0;				// selected month of selected account
	private ChargeList charges = null;	// contains registered charges in selected month of selected account
										/* WARNING: IF CHANGES MADE TO LIST, EXECUTE saveCharges() METHOD BEFORE
											EXITING MONTH */
	
	public String infoMessage = "";		// gives helpful error message
	
	// constructor
	public ChargeFileManager()
	{
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
	
	/* takes existing account name as argument, selects specified account
	returns true if account successfully selected
	
	returns false if
	*	argument is empty string
	*	specified account doesn't exist */
	public boolean selectAccount(String accountName)
	{
		boolean accountSelected = false;
		if(accountName.equals(""))
			this.infoMessage = "ERROR: Valor de cuenta no válida.";
		else if(!this.accountExists(accountName))
			this.infoMessage = "ERROR: Cuenta " + accountName + " no existe.";
		else
		{
			this.resetAll();
			this.accountName = accountName;
			accountSelected = true;
		}
		return accountSelected;
	}
	
	/* takes existing month of selected account as argument, selects specified month of currently selected account
	returns true if month successfully selected
	
	returns false if
	*	no account currently selected
	*	specified month doesn't exist */
	public boolean selectMonth(int month)
	{
		boolean monthSelected = false;
		
		if(this.accountName.equals(""))
			this.infoMessage = "ERROR: No hay cuenta especificada.";
		else if(!this.monthExists(month, this.accountName))
			this.infoMessage = "ERROR: Mes " + String.valueOf(month) + " no existe.";
		else
		{
			this.resetMonth();
			this.month = month;
			monthSelected = true;
			this.fetchCharges();	// charges list filled with registered charges of specified month of selected account
		}
		return monthSelected;
	}
	
	// fills charges list with charges registered in selected month of selected account
	private void fetchCharges()
	{
		this.charges.clear();
		
		if(!this.accountName.equals("") && this.month > 0)
		{
			try
			{
				String monthPath = this.accountsPath + "/" + this.accountName + "/meses/" + String.valueOf(this.month) + ".txt";
				BufferedReader reader = new BufferedReader(new FileReader(monthPath));
				String line = reader.readLine();
				while(line != null)
				{
						String[] charge = line.split("█");
						charges.add(new Charge(Integer.valueOf(charge[0]), Float.valueOf(charge[1]), charge[2]));
						line = reader.readLine();
				}
				reader.close();
			}
			catch(Exception e) {}
		}
	}
	
	// takes account name as argument, returns true/false whether specified account exists in directory of accounts
	public boolean accountExists(String accountName)
	{
		boolean accountExists = false;
		String accountPath = this.accountsPath + "/" + accountName;
		
		try
		{
			File account = new File(accountPath);
			accountExists = account.isDirectory();
		}
		catch(Exception e) {}
		
		return accountExists;
	}
	
	// takes month + account name as arguments, returns true/false whether specified month exists in specified account
	public boolean monthExists(int month, String accountName)
	{
		boolean monthExists = false;
		String monthPath = this.accountsPath + "/" + accountName + "/meses/" + String.valueOf(month) + ".txt";
		
		try
		{
			File monthFile = new File(monthPath);
			monthExists = monthFile.isFile();
		}
		catch(Exception e) {}
		
		return monthExists;
	}
	
	// takes charge ID as argument, returns true/false whether specified charge exists in currently selected month of selected account
	public boolean chargePresent(int chargeId)
	{
		return charges.exists(chargeId);
	}
	
	// getter method for currently selected account
	public String getAccount()
	{
		return this.accountName;
	}
	
	// getter method for currently selected month of selected account
	public int getMonth()
	{
		return this.month;
	}
	
	// returns charges registered in currently selected month of selected account
	public ChargeList getCharges()
	{
		return this.charges;
	}
	
	// takes directory path as argument, returns list of names of existing files in specified directory
	private List<String> getFiles(String path)
	{
		List<String> filenames = new LinkedList<>();
		
		try
		{
			File directory = new File(path);
			File[] files = directory.listFiles();
			
			if(files != null)
			{
				for(File file : files)
				{
					String filename = file.getName();
					int extIndex = filename.lastIndexOf(".");
					if(extIndex != -1)
						filename = filename.substring(0, extIndex);
					
					filenames.add(filename);
				}
			}
		}
		catch(Exception e) {}
		return filenames;
	}
	
	// returns list of existing accounts in system
	public List<String> getAccounts()
	{
		return this.getFiles(this.accountsPath);
	}
	
	// returns list of months registered in currently selected account
	public List<String> getMonths()
	{
		return getMonths(this.accountName);
	}
	
	/* takes account name as argument, returns list of months registered in specified account
	returns empty list if argument is empty string */
	public List<String> getMonths(String accountName)
	{
		if(accountName.equals(""))
			return new LinkedList<>();
		else
			return this.getFiles(this.accountsPath + "/" + accountName + "/meses");
	}
	
	/* returns banner text for currently selected account, meant to be printed in console interface (pretty)
	banner contains logo of bank of selected account */
	public String getAccountHeader()
	{
		String headerPath = "";
		String accountHeader = "";
		
		if(accountName.equals("")) // if no account selected, return general banner for program (B logo)
			headerPath = this.mainPath + "/simbolos/ministry.txt";
		else
			headerPath = this.accountsPath + "/" + this.accountName + "/simbolos/simbolo.txt";
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(headerPath));
			String line = reader.readLine();
			while(line != null)
			{
					accountHeader = accountHeader + line + "\n";
					line = reader.readLine();
			}
			reader.close();
		}
		catch(Exception e) {}
		
		accountHeader = accountHeader.substring(0, accountHeader.length() - 1); // get rid of last newline character at end
		return accountHeader;
	}
	
	/* takes nonexisting account name as argument, creates specified account
	returns true if account created successfully
	
	returns false if:
	*	specified account already exists
	*	specified account name contains illegal filename characters
	*	file creation operation fails */
	public boolean addAccount(String accountName)
	{
		boolean accountAdded = false;
		String invalidCharsList = ".\\/:*?\"<>|";
		boolean isValidName = true;
		
		if(this.accountExists(accountName)) // does account already exist?
			this.infoMessage = "ERROR: Cuenta " + accountName + " ya existe.";
		else // account doesn't exist
		{
			// validate input, shouldn't contain illegal filename characters
			int i = 0;
			while(isValidName && i < accountName.length())
			{
				int j = 0;
				while(isValidName && j < invalidCharsList.length())
				{
					if(accountName.charAt(i) == invalidCharsList.charAt(j))
					{
						this.infoMessage = "ERROR: Nombre de cuenta contiene letras prohibidas: " + invalidCharsList;
						isValidName = false;
					}
					else
						j++;
				}
				i++;
			}
			if(isValidName) // if input valid
			{
				// create directory, fill with standard files
				String accountPath = this.accountsPath + "/" + accountName;
				String monthsPath = accountPath + "/meses";
				String symbolsPath = accountPath + "/simbolos";
				String headerPath = symbolsPath + "/simbolo.txt";
				
				String templateHeaderPath = this.mainPath + "/simbolos/nuevo.txt";
				try
				{
					File accountDir = new File(accountPath);
					if(accountDir.mkdir())
					{
						File monthsDir = new File(monthsPath);
						File symbolsDir = new File(symbolsPath);
						if(monthsDir.mkdir() && symbolsDir.mkdir())
						{
							accountAdded = true;
							this.infoMessage = "Cuenta " + accountName + " agregada.";
							
							File templateHeaderFile = new File(templateHeaderPath);
							File headerFile = new File(headerPath);
							Files.copy(templateHeaderFile.toPath(), headerFile.toPath());
						}
					}
				}
				catch(Exception e)
				{
					this.infoMessage = "ERROR: Operación ha fallado.";
				}
			}
		}
		return accountAdded;
	}
	
	/* takes existing account name as argument, removes specified account
	account must be emptied of charges before deleting
	returns true if account removed successfully
	
	returns false if:
	*	specified account doesn't exist
	*	specified account still contains charges
	*	file deletion operation fails */
	public boolean removeAccount(String accountName)
	{
		boolean accountRemoved = false;
		
		if(!this.accountExists(accountName)) // does account not exist?
			this.infoMessage = "ERROR: Cuenta " + accountName + " no existe.";
		else if(this.getMonths(accountName).size() > 0) // is account not emptied?
			this.infoMessage = "ERROR: Cuenta contiene unos meses. Deben ser borrados primero.";
		else // account file ready for deletion
		{
			// delete file
			String accountPath = this.accountsPath + "/" + accountName;
			String monthsPath = accountPath + "/meses";
			String symbolsPath = accountPath + "/simbolos";
			String headerPath = symbolsPath + "/simbolo.txt";
			try
			{
				File headFile = new File(headerPath);
				if(headFile.delete())
				{
					File monthsDir = new File(monthsPath);
					File symbolsDir = new File(symbolsPath);
					if(monthsDir.delete() && symbolsDir.delete())
					{
						File accountDir = new File(accountPath);
						if(accountDir.delete())
						{
							accountRemoved = true;
							this.infoMessage = "Cuenta " + accountName + " borrada.";
						}
					}
				}
			}
			catch(Exception e)
			{
				this.infoMessage = "ERROR: Operación ha fallado.";
			}
		}
		return accountRemoved;
	}
	
	/* takes nonexisting month of selected account as argument, creates specified month in selected account
	returns true if month created successfully
	
	returns false if:
	*	no account selected
	*	specified month already exists within selected account
	*	specified month value not within valid range
	*	file creation operation fails
	*/
	public boolean addMonth(int month)
	{
		boolean monthAdded = false;

		if(this.accountName.equals("")) // is an account not selected?
			this.infoMessage = "ERROR: No hay cuenta especificada.";
		if(this.monthExists(month, this.accountName)) // does the month already exist?
			this.infoMessage = "ERROR: Mes " + String.valueOf(month) + " ya existe.";
		else if(month < 190000 || month > 220000) // is the month value within the valid range?
			this.infoMessage = "ERROR: Valor fuera de rango.";
		else // month file ready for creation
		{
			// create file
			String monthPath = this.accountsPath + "/" + this.accountName + "/meses/" + String.valueOf(month) + ".txt";
			try
			{
				File monthFile = new File(monthPath);
				if(monthFile.createNewFile())
				{
					monthAdded = true;
					this.infoMessage = "Mes " + String.valueOf(month) + " agregado.";
				}
			}
			catch(Exception e)
			{
				this.infoMessage = "ERROR: Operación ha fallado.";
			}
		}
		return monthAdded;
	}
	/* takes existing month of selected account as argument, removes specified month from selected account
	returns true if month removed successfully
	
	returns false if:
	*	no account selected
	*	specified month doesn't exist within selected account
	*	file deletion operation fails
	*/
	public boolean removeMonth(int month)
	{
		boolean monthRemoved = false;
		
		if(this.accountName.equals("")) // is an account not selected?
			this.infoMessage = "ERROR: No hay cuenta especificada.";
		else if(!this.monthExists(month, this.accountName)) // does the month not exist?
			this.infoMessage = "ERROR: Mes " + String.valueOf(month) + " no existe.";
		else // month file ready for deletion
		{
			// delete file
			String monthPath = this.accountsPath + "/" + this.accountName + "/meses/" + String.valueOf(month) + ".txt";
			try
			{
				File monthFile = new File(monthPath);
				if(monthFile.delete())
				{
					monthRemoved = true;
					this.infoMessage = "Mes " + String.valueOf(month) + " borrado.";
				}
			}
			catch(Exception e)
			{
				this.infoMessage = "ERROR: Operación ha fallado.";
			}
		}
		return monthRemoved;
	}
	
	/* takes new charge as argument, adds charge to charges list containing registered charges in 
		selected month of selected account
	WARNING: CHARGE NOT SAVED TO MONTH UNTIL saveCharges() METHOD EXECUTED, MUST BE DONE BEFORE
		EXITING MONTH, OTHERWISE CHANGES LOST
	returns true if charge successfully added to charges list
	
	returns false if:
	*	no account selected
	*	no month selected
	*	specified charge ID not within valid range
	*	specified charge ID already exists within charges list
	*/
	public boolean addCharge(Charge charge)
	{
		boolean chargeAdded = false;
		
		if(this.accountName.equals("")) // is an account not selected?
			this.infoMessage = "ERROR: No hay cuenta especificada.";
		else if(this.month == 0) // is a month not selected?
			this.infoMessage = "ERROR: No hay mes especificado.";
		else if(charge.getId() <= 0 || charge.getId() >= 3200) // is the charge ID value within the valid range?
			this.infoMessage = "ERROR: Valor de día fuera de rango.";
		else if(charges.add(charge)) // add charge to charges list
		{
			chargeAdded = true;
			this.infoMessage = "Carga " + String.valueOf(charge.getId()) + " agregada.";
		}
		else // charge ID already exists in charges list
			this.infoMessage = "Carga " + String.valueOf(charge.getId()) + " ya existe.";
		
		return chargeAdded;
	}
	
	/* takes existing charge ID as argument, removes charge from charges list containing registered 
		charges selected month of selected account
	WARNING: CHARGE NOT REMOVED FROM MONTH UNTIL saveCharges() METHOD EXECUTED, MUST BE DONE BEFORE
		EXITING MONTH, OTHERWISE CHANGES LOST
	returns true if charge successfully removed from charges list
	
	returns false if:
	*	no account selected
	*	no month selected
	*	specified charge ID doesn't exist within charges list
	*/
	public boolean removeCharge(int chargeId)
	{
		boolean chargeRemoved = false;
		if(this.accountName.equals("")) // is an account not selected?
			this.infoMessage = "ERROR: No hay cuenta especificada.";
		else if(this.month == 0) // is a month not selected?
			this.infoMessage = "ERROR: No hay mes especificado.";
		else if(charges.remove(chargeId)) // remove charge from charges list
		{
			chargeRemoved = true;
			this.infoMessage = "Carga " + String.valueOf(chargeId) + " borrada.";
		}
		else // charge ID doesn't exist in charges list
			this.infoMessage = "ERROR: Carga " + String.valueOf(chargeId) + " no existe.";
		
		return chargeRemoved;
	}
	
	/* overwrites charges in file of selected month of selected account with charges in charges list variable
	saves changes made to selected month file of selected account
	overwritten charge file renamed to basura.txt if need to be recovered
	returns true if charges successfully written to file
	
	returns false if:
	*	no account selected
	*	no month selected
	*	file deletion/rename/creation operations fail
	*/
	public boolean saveCharges()
	{
		boolean chargesSaved = false;
		
		if(!this.accountName.equals("")) // is an account selected?
		{
			if(this.month != 0) // is a month selected?
			{
				// text file paths
				String oldMonthPath = this.accountsPath + "/" + this.accountName + "/meses/" + String.valueOf(this.month) + ".txt";
				String newMonthPath = this.binPath + "/temp.txt";
				String trashPath = this.binPath + "/basura.txt";
				try
				{
					File oldMonthFile = new File(oldMonthPath);
					File newMonthFile = new File(newMonthPath);
					File trashFile = new File(trashPath);
					
					BufferedWriter writer = new BufferedWriter(new FileWriter(newMonthFile));
					
					// write new charges list to temp.txt
					for(Charge charge : charges)
					{
						String line = String.format("%04d", charge.getId()) + "█" + String.format("%.02f", charge.getAmount()) + "█" + charge.getReason();
						writer.write(line + System.getProperty("line.separator")); // line.separator is newline character, needed for bufferedwriter to write newline, otherwise all on one line
					}
					writer.close();
					
					// delete basura.txt (really old charge list)
					trashFile.delete();
					if(oldMonthFile.renameTo(trashFile)) // rename month file (old charge list) to basura.txt
						chargesSaved = newMonthFile.renameTo(oldMonthFile); // rename temp.txt (new charge list) to month file
				}
				catch(Exception e) {}
			}
		}
		return chargesSaved;
	}
}