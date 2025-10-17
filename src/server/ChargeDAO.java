/*
Data access object that provides methods to view, add, or remove accounts or charges 
from an Oracle database hosted on the same machine.

CONFIGURATION:
Change DB_URL, USERNAME, PASSWORD global variables to appropriate values to access 
your Oracle database located on the server.

TODO:
Validate charge ID when adding a charge.

Make improvements to fetchCharges(). As of now, it fetches the charges for the specified 
month as well as the charges on the first day of the following month. Look into using 
Java Date objects
*/

import java.sql.*;

import java.util.List;
import java.util.ArrayList;

public class ChargeDAO
{
	private Connection connection = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	
	private final String DB_URL = "jdbc:oracle:thin:@localhost:[PORT]:[GLOBAL DATABASE NAME]";
	private final String DB_USERNAME = "username";
	private final String DB_PASSWORD = "password";
	
	public boolean connectToDB()
	{
		boolean connectedToDB = false;
		try
		{
			// load or register driver, requires "ojdbc8.jar" JAR file in project library
			Class.forName("oracle.jdbc.OracleDriver"); // loads Oracle driver
			
			// establish connection
			connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
			
			if(connection != null)
			{
				statement = connection.createStatement();
				connectedToDB = true;
			}
		}
		catch(ClassNotFoundException e)
		{
			System.out.println("SQL Driver class not found");
		}
		catch(Exception e)
		{
			System.out.println("Failed to load SQL driver");
		}
		return connectedToDB;
	}
	
	// fetches and returns list of existing accounts in database
	public List<String> fetchAccounts()
	{
		List<String> accounts = new ArrayList<>();
		
		try
		{
			// execute query
			resultSet = statement.executeQuery("SELECT * FROM accounts");
			
			// process results
			while(resultSet.next())
			{
				String accountName = resultSet.getString("account_name").trim();
				accounts.add(accountName);
			}
		}
		catch(SQLException sqle)
		{
			System.out.println("Failed to fetch accounts from database.");
		}
		return accounts;
	}
	
	/* takes account name, month as arguments, fetches and returns list of 
		existing charges made in specified month within specified account */
	public ChargeList fetchCharges(String accountName, int month)
	{
		// NEEDS IMPROVEMENT, SHOULDN'T INCLUDE FIRST OF NEXT MONTH
		ChargeList charges = new ChargeList();
		int yearPart = month / 100;
		int monthPart = month % 100;
		
		String earliestDate = String.valueOf(yearPart) + "-" + String.format("%02d", monthPart) + "-01";
		String latestDate = "";
		if(monthPart < 12) // if month NOT december
			latestDate = String.valueOf(yearPart) + "-" + String.format("%02d", monthPart + 1) + "-01";
		else // month is december
			latestDate = String.valueOf(yearPart + 1) + "-01-01";
		
		try
		{
			statement = connection.createStatement();
			
			// execute query
			String query = String.format("SELECT charge_id, charge_amount, charge_reason FROM charges WHERE account_charged='%s' AND charge_date BETWEEN DATE '%s' AND DATE '%s'", accountName, earliestDate, latestDate);
			resultSet = statement.executeQuery(query);
			
			// process results
			while(resultSet.next())
			{
				int chargeID = resultSet.getInt("charge_id");
				float chargeAmount = resultSet.getFloat("charge_amount");
				String chargeReason = resultSet.getString("charge_reason").trim();
				Charge charge = new Charge(chargeID, chargeAmount, chargeReason);
				charges.add(charge);
			}
		}
		catch(SQLException sqle)
		{
			System.out.println("Failed to fetch charges from database.");
			sqle.printStackTrace();
		}
		return charges;
	}
	
	/* takes account name as argument, adds specified account to database
	returns true if account added successfully, otherwise false */
	public boolean addAccount(String accountName)
	{
		boolean accountAdded = false;
		String query = "INSERT INTO accounts VALUES(?)";
		try
		{
			PreparedStatement prepStatement = connection.prepareStatement(query);
			prepStatement.setString(1, accountName);
			prepStatement.executeUpdate();
			accountAdded = true;
		}
		catch(SQLException sqle)
		{
			System.out.println("Failed to add account to database.");
		}
		return accountAdded;
	}
	
	/* takes account name as argument, removes specified account from database
	returns true if account removed successfully, otherwise false */
	public boolean removeAccount(String accountName)
	{
		boolean accountRemoved = false;
		String query = "DELETE FROM accounts WHERE account_name = ?";
		try
		{
			PreparedStatement prepStatement = connection.prepareStatement(query);
			prepStatement.setString(1, accountName);
			prepStatement.executeUpdate();
			accountRemoved = true;
		}
		catch(SQLException sqle)
		{
			System.out.println("Failed to remove account from database.");
		}
		return accountRemoved;
	}
	
	/* takes account name, charge object as arguments, adds specified charge to 
		specified account in database
	returns true if charge added successfully, otherwise false */
	public boolean addCharge(String accountName, Charge charge)
	{
		boolean chargeAdded = false;
		
		// ADD CHARGE ID VALIDATION LATER
		
		String query = "INSERT INTO charges VALUES(?,?,?,?,?)";
		try
		{
			PreparedStatement prepStatement = connection.prepareStatement(query);
			prepStatement.setInt(1, charge.getId());
			prepStatement.setString(2, accountName);
			
			int id = charge.getId();
			int year = id / 1000000;
			int month = id / 10000 % 100;
			int day = id / 100 % 100;
			String date = String.format("%s-%s-%s", year, month, day);
			System.out.println(date); // debugging
			prepStatement.setDate(3, java.sql.Date.valueOf(date));
			
			prepStatement.setFloat(4, charge.getAmount());
			prepStatement.setString(5, charge.getReason());
			prepStatement.executeUpdate();
			chargeAdded = true;
		}
		catch(SQLException sqle)
		{
			System.out.println("Failed to add charge to database.");
		}
		return chargeAdded;
	}
	
	/* takes account name, charge object as arguments, removes specified charge 
		from specified account in database
	returns true if charge removed successfully, otherwise false */
	public boolean removeCharge(String accountName, Charge charge)
	{
		boolean chargeRemoved = false;
		String query = "DELETE FROM charges WHERE charge_id = ?";
		try
		{
			PreparedStatement prepStatement = connection.prepareStatement(query);
			prepStatement.setInt(1, charge.getId());
			prepStatement.executeUpdate();
			chargeRemoved = true;
		}
		catch(SQLException sqle)
		{
			System.out.println("Failed to add charge to database.");
		}
		return chargeRemoved;
	}
	
	// closes connection to Oracle database
	public void disconnectFromDB()
	{
		try
		{
			// close statement and connection
			statement.close();
			connection.close();
		}
		catch(SQLException sqle)
		{
			System.out.println("Failed to disconnect from database.");
		}
	}
}