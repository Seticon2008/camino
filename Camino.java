import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;

public class Camino
{
	String mainDir = "C:/programs/camino";
	String accountsDir = mainDir + "/cuentas";
	String binDir = mainDir + "/papelera";
	String currentDir = accountsDir;
	Scanner scanner;
	
	int accountsMenu = 0;
	int monthsMenu = 1;
	int chargesMenu = 2;
	
	public static void main(String[] args)
	{
		Camino obj = new Camino();
		obj.scanner = new Scanner(System.in);
		
		String accountName = "";
		int month = -1;
		byte option = -1;
		String[] charge = null;
		
		while(!accountName.equals("0"))
		{
			accountName = "";
			month = -1;
			option = -1;
			charge = null;
			obj.unselectAccounts();
			
			obj.clearScreen();
			obj.printHeader(obj.mainDir + "/simbolos/ministry.txt");
			
			System.out.println("...\n...\n...\nCuentas registradas:");
			boolean existingAccounts = obj.displayFiles();
			
			System.out.print("\nCuenta deseada: ");
			accountName = obj.getAccountName();
			
			if(accountName.equals("0"))
			{
				obj.clearScreen();
				System.out.println("Terminando...");
			}
			else if(accountName.equals("-1"))
				if(!existingAccounts)
					System.out.println("No hay ninguna cuenta para borrar.");
				else
					obj.promptDeleteAccount(accountName);
				
			else if(!obj.accountExists(accountName))
				obj.promptCreateAccount(accountName);
			
			else if(obj.accountExists(accountName))
			{
				obj.selectAccount(accountName);
				
				while(month != 0)
				{
					month = -1;
					option = -1;
					charge = null;
					
					obj.clearScreen();
					obj.printHeader(obj.accountsDir + "/" + accountName + "/simbolos/simbolo.txt");
					
					System.out.println("...\n...\n...\nMeses registrados:");
					boolean existingMonths = obj.displayFiles();
					
					System.out.print("\nMes deseado (formato: AAAAMM): ");
					month = obj.getMonth();
					if(month == 0)
						System.out.println("Atras...");
					
					else if(month == -1)
						if(!existingMonths)
							System.out.println("No hay ningun mes para borrar.");
						else
							obj.promptDeleteMonth(month, accountName);
						
					else if(!obj.monthExists(month, accountName))
						obj.promptAddMonth(month, accountName);
					
					else if(obj.monthExists(month, accountName))
					{
						while(option != 0)
						{
							System.out.println("...\n...\n...\nCargas:");
							boolean existingCharges = obj.displayCharges(month, accountName);
							obj.displayMenu(existingCharges);
							if(existingCharges)
								option = obj.getOption(3);
							else
								option = obj.getOption(2);
							
							if(option == 0)
								System.out.println("Atras...");
							else
							{
								System.out.println("...\n...\n...\nCarga especificada: ");
								charge = obj.getCharge();
								if(charge != null)
									if(option == 1)
										obj.promptAddCharge(charge, month, accountName);
									
									else if(!obj.chargeExists(charge, month, accountName))
										System.out.println("Carga especificada no existe.");
									
									else if(option == 2)
										obj.promptRemoveCharge(charge, month, accountName);
							}
						}
					}
				}
			}
		}
		obj.scanner.close();
	}
	
	// Compound menu methods
	
	private void promptCreateAccount(String accountName)
	{
		System.out.println("Crear cuenta especificada?");
		if(confirmMessage())
			if(createAccount(accountName))
				System.out.println("Cuenta creada.");
			else
				System.out.println("Operación ha fallado.");
		else
			System.out.println("Operación detenida.");
	}
	
	private void promptDeleteAccount(String accountName)
	{
		System.out.print("Cuenta para borrar: ");
		accountName = getAccountName();
		if(accountExists(accountName))
			if(confirmMessage())
				if(deleteAccount(accountName))
					System.out.println("Cuenta borrada.");
				else
					System.out.println("Operación ha fallado.");
			else
				System.out.println("Operación detenida.");
		else
			System.out.println("Cuenta especificada no existe.");
	}
	
	private void promptAddMonth(int month, String accountName)
	{
		System.out.println("Agregar mes especificado?");
		if(confirmMessage())
			if(addMonth(month, accountName))
				System.out.println("Mes agregado.");
			else
				System.out.println("Operación ha fallado.");
		else
			System.out.println("Operación detenida.");
	}
	
	private void promptDeleteMonth(int month, String accountName)
	{
		System.out.print("Mes para borrar: ");
		month = getMonth();
		if(monthExists(month, accountName))
			if(confirmMessage())
				if(deleteMonth(month, accountName))
					System.out.println("Mes borrado.");
				else
					System.out.println("Operación ha fallado.");
			else
				System.out.println("Operación detenida.");
		else
			System.out.println("Mes especificado no existe.");
	}
	
	private void promptAddCharge(String[] charge, int month, String accountName)
	{
		if(confirmMessage())
			if(addCharge(charge, month, accountName))
				System.out.println("Carga agregada.");
			else
				System.out.println("Operación ha fallado.");
		else
			System.out.println("Operación detenida.");
	}
	
	private void promptRemoveCharge(String[] charge, int month, String accountName)
	{
		if(confirmMessage())
			if(removeCharge(charge, month, accountName))
				System.out.println("Carga borrada.");
			else
				System.out.println("Operación ha fallado.");
		else
			System.out.println("Operación detenida.");
	}
	
	// Simple menu methods
	
	private boolean displayFiles()
	{
		boolean existingFiles = false;
		
		try
		{
			File directory = new File(currentDir);
			File[] files = directory.listFiles();
			
			if(files == null)
			{
				System.out.println("No hay ningun registrado.");
			}
			else if(files.length <= 0)
			{
				System.out.println("No hay ningun registrado.");
			}
			else
			{
				existingFiles = true;
				
				for(File file : files)
				{
					String filename = file.getName();
					int extIndex = filename.lastIndexOf(".");
					if(extIndex != -1)
						filename = filename.substring(0, extIndex);
					
					System.out.println(filename);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		return existingFiles;
	}
	
	private boolean displayCharges(int month, String accountName)
	{
		boolean existingCharges = false;
		String dir = accountsDir + "/" + accountName + "/meses/" + String.valueOf(month) + ".txt";
		String format = "%-8s%-16s%s%n";
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(dir));
			String line = reader.readLine();
			if(line == null)
			{
				System.out.println("No hay ninguna carga.");
			}
			else
			{
				existingCharges = true;
				
				System.out.printf(format, "fecha", "cantidad", "razon");
				
				while(line != null)
				{
					String[] charge = line.split("█");
					System.out.printf(format, charge[0], charge[1], charge[2]);
					line = reader.readLine();
				}
			}
			reader.close();
		}
		catch(Exception e) {}
		
		return existingCharges;
	}
	
	private void displayMenu(boolean existingCharges)
	{
		System.out.println("\nOpciones:");
		System.out.println("0: Volver");
		System.out.println("1: Agregar carga");
		if(existingCharges)
			System.out.println("2: Borrar carga");
	}
	
	private boolean confirmMessage()
	{
		String input = "";
		
		System.out.print("Confirmar? (c/r): ");
		input = scanner.nextLine();
		
		if(input.toLowerCase().equals("c"))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private void printHeader(String path)
	{
		try
		{
			Scanner printer = new Scanner(new File(path));
			
			while(printer.hasNextLine())
				System.out.println(printer.nextLine());
			
			printer.close();
		}
		catch(Exception e) {}
	}
	
	private void clearScreen()
	{
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}
	
	// Simple backend methods
	
	private byte getOption(int numOptions)
	{
		String input = "";
		byte option = 0;
		boolean validInput = false;
		boolean validOption = false;
		
		System.out.print("Opción deseada: ");
		while(!validOption)
		{
			validInput = false;
			while(!validInput)
			{
				input = scanner.nextLine();
				try
				{
					option = Byte.valueOf(input);
					validInput = true;
				}
				catch(Exception e)
				{
					System.out.print("Opción no válida. Inténtalo de nuevo: ");
				}
			}
			if(option < 0 || option >= numOptions)
			{
				System.out.print("Opción no válida. Inténtalo de nuevo: ");
			}
			else
			{
				validOption = true;
			}
		}
		return option;
	}
	
	private String getAccountName()
	{
		String input = "";
		String accountName = "";
		
		accountName = input = scanner.nextLine();
		
		return accountName;
	}
	
	private int getMonth()
	{
		String input = "";
		int inputNum = 0;
		int month = 0;
		boolean validInput = false;
		
		while(!validInput)
		{
			input = scanner.nextLine();
			try
			{
				inputNum = Integer.valueOf(input);
				if(inputNum > 190000 && inputNum < 220000 || inputNum == 0 || inputNum == -1)
				{
					validInput = true;
					month = inputNum;
				}
				else
				{
					System.out.print("Valor fuera de rango. Inténtalo de nuevo: ");
				}
			}
			catch(Exception e)
			{
				System.out.print("Valor no válida. Inténtalo de nuevo: ");
			}
		}
		return month;
	}
	
	// returns charge if successful, returns null if unsuccessful
	private String[] getCharge()
	{
		int day = 0;
		float amount = 0;
		String reason = "";
		
		boolean validDay = false;
		boolean validAmount = false;
		
		System.out.print("Dia especificado: ");
		try
		{
			day = Integer.valueOf(scanner.nextLine());
			if(day > 0 && day < 32)
				validDay = true;
		}
		catch(Exception e) {}
		
		if(!validDay)
		{
			System.out.println("Dia no valido");
		}
		else
		{
			System.out.print("Cantidad especificada: ");
			try
			{
				amount = Float.valueOf(scanner.nextLine());
				validAmount = true;
			}
			catch(Exception e) {}
			
			if(!validAmount)
			{
				System.out.println("Cantidad no valida.");
			}
			else
			{
				System.out.print("Razon especificado: ");
				reason = scanner.nextLine();

				return new String[] { String.valueOf(day), String.format("%.02f", amount), reason };
			}
		}
		return null;
	}
	
	private boolean accountExists(String accountName)
	{
		boolean accountExists = false;
		String dir = accountsDir + "/" + accountName;
		
		try
		{
			File account = new File(dir);
			accountExists = account.isDirectory();
		}
		catch(Exception e) {}
		
		return accountExists;
	}
	
	private boolean monthExists(int month, String accountName)
	{
		boolean monthExists = false;
		String dir = accountsDir + "/" + accountName + "/meses/" + String.valueOf(month) + ".txt";
		
		try
		{
			File monthFile = new File(dir);
			monthExists = monthFile.isFile();
		}
		catch(Exception e) {}
		
		return monthExists;
	}
	
	private boolean chargeExists(String[] chargeToFind, int month, String accountName)
	{
		boolean chargeExists = false;
		String dir = accountsDir + "/" + accountName + "/meses/" + String.valueOf(month) + ".txt";
		
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(dir));
			
			String line = reader.readLine();
			
			while(line != null)
			{
				String[] charge = line.split("█");
				if(charge[0].equals(chargeToFind[0]))
					if(charge[1].equals(chargeToFind[1]))
						if(charge[2].equals(chargeToFind[2]))
							chargeExists = true;
				
				line = reader.readLine();
			}
			reader.close();
		}
		catch(Exception e) {}
		
		return chargeExists;
	}
	
	private void selectAccount(String accountName)
	{
		currentDir = currentDir + "/" + accountName + "/meses";
	}
	
	private void unselectAccounts()
	{
		currentDir = accountsDir;
	}
	
	private boolean createAccount(String accountName)
	{
		boolean accountCreated = false;
		String dir = accountsDir + "/" + accountName;
		
		try
		{
			File account = new File(dir);
			accountCreated = account.mkdir();
		}
		catch(Exception e) {}
		
		return accountCreated;
	}
	
	private boolean deleteAccount(String accountName)
	{
		boolean accountDeleted = false;
		String dir = accountsDir + "/" + accountName;
		
		try
		{
			File account = new File(dir);
			
			File[] monthFiles = account.listFiles();
			for(File monthFile : monthFiles)
			{
				monthFile.delete();
			}
			
			accountDeleted = account.delete();
		}
		catch(Exception e) {}
		
		return accountDeleted;
	}
	
	private boolean addMonth(int month, String accountName)
	{
		boolean monthAdded = false;
		String filename = accountsDir + "/" + accountName + "/meses/" + String.valueOf(month) + ".txt";
		
		try
		{
			File monthFile = new File(filename);
			monthAdded = monthFile.createNewFile();
		}
		catch(Exception e) {}
		
		return monthAdded;
	}
	
	private boolean deleteMonth(int month, String accountName)
	{
		boolean monthDeleted = false;
		String dir = accountsDir + "/" + accountName + "/meses/" + String.valueOf(month) + ".txt";
		
		try
		{
			File monthFile = new File(dir);
			monthDeleted = monthFile.delete();
		}
		catch(Exception e) {}
		
		return monthDeleted;
	}
	
	private boolean addCharge(String[] chargeToAdd, int month, String accountName)
	{
		boolean chargeAdded = false;	// true when entire operation complete/successful
		boolean positionFound = false;	// true as soon as charge line written to a file
		
		String lineToAdd = chargeToAdd[0] + "█" + chargeToAdd[1] + "█" + chargeToAdd[2];
		int chargeToAddDate = Integer.valueOf(chargeToAdd[0]);
		
		String dir = accountsDir + "/" + accountName + "/meses/" + String.valueOf(month) + ".txt";
		
		try
		{
			File monthFile = new File(dir);
			File tempFile = new File(binDir + "/temp.txt");
			File trashFile = new File(binDir + "/basura.txt");
			
			BufferedReader reader = new BufferedReader(new FileReader(monthFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
			
			String line = reader.readLine();
			if(line == null)
			{
				writer.write(lineToAdd + System.getProperty("line.separator"));
				positionFound = true;
			}
			else
			{
				while(line != null)
				{
					String[] charge = line.split("█");
					
					if(Integer.valueOf(charge[0]) >= chargeToAddDate && !positionFound)
					{
						writer.write(lineToAdd + System.getProperty("line.separator"));
						positionFound = true;
					}
					writer.write(line + System.getProperty("line.separator"));
					line = reader.readLine();
				}
				if(!positionFound)
				{
					writer.write(lineToAdd + System.getProperty("line.separator"));
					positionFound = true;
				}
			}
			writer.close();
			reader.close();
			
			trashFile.delete();
			if(monthFile.renameTo(trashFile));
				chargeAdded = tempFile.renameTo(monthFile);
			
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
		
		return chargeAdded;
	}
	
	private boolean removeCharge(String[] chargeToRemove, int month, String accountName)
	{
		boolean chargeRemoved = false;
		boolean chargeFound = false;	// so that only one charge gets removed if there are multiple charges with same exact values
		
		String dir = accountsDir + "/" + accountName + "/meses/" + String.valueOf(month) + ".txt";
		
		try
		{
			File monthFile = new File(dir);
			File tempFile = new File(binDir + "/temp.txt");
			File trashFile = new File(binDir + "/basura.txt");
			
			BufferedReader reader = new BufferedReader(new FileReader(monthFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
			
			String line = reader.readLine();
			while(line != null)
			{
				String[] charge = line.split("█");
				boolean isChargeToRemove = false;
				
				if(charge[0].equals(chargeToRemove[0]))
					if(charge[1].equals(chargeToRemove[1]))
						if(charge[2].equals(chargeToRemove[2]))
							isChargeToRemove = true;
				
				if(!isChargeToRemove || chargeFound)
					writer.write(line + System.getProperty("line.separator"));
				else
					chargeFound = true;
				
				line = reader.readLine();
			}
			writer.close();
			reader.close();
			
			trashFile.delete();
			if(monthFile.renameTo(trashFile));
				chargeRemoved = tempFile.renameTo(monthFile);
		}
		catch(Exception e) {}
		
		return chargeRemoved;
	}
}