/*
Provides client socket connection to server hosting charge database. ALso provides methods 
for sending client requests to the server in order to access database information and 
to manipulate the database.

Meant to be used within a driver class to interface with the server.

CONFIGURATION:
Change PORT_NUMBER to desired port that server will listen on.
Change "localhost" to IP address of server

TODO:
*/

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.net.UnknownHostException;
import java.net.ConnectException;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

import java.util.List;

public class ChargeClient
{
	private final int PORT_NUMBER = 12345;			// port number server listening to
	private final String IP_ADDRESS = "localhost";	// IP address of server
	
	private SSLSocketFactory factory = null;	// creates SSLSocket object
	private SSLSocket clientSocket = null;		// holds socket connection to server
	private PrintWriter out = null;				// stream to send password to server
	private BufferedReader in = null;			// stream to listen to server info
	private ObjectOutputStream objOut = null;	// stream to send client requests to server
	private ObjectInputStream objIn = null;		// stream to get database contents
	
	/* constructor to specify client truststore file
	NOTE: this is instead handled by the .bat/.sh files */
	public ChargeClient()
	{
		//System.setProperty("javax.net.ssl.trustStore", "clienttruststore.jks");
		//System.setProperty("javax.net.ssl.trustStorePassword", "password");
	}
	
	/* takes password as argument, attempts to connect to server using specified password
	returns true if SSL handshake with server succeeds and specified password is correct,
		otherwise false */
	public boolean connectToServer(String enteredPassword)
	{
		boolean connected = false;
		try
		{
			// create SSLSocket, connect to server
			factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			System.out.println("Attempting to connect to server.");
			clientSocket = (SSLSocket) factory.createSocket(IP_ADDRESS, PORT_NUMBER);
			
			// get input/output streams
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			objOut = new ObjectOutputStream(clientSocket.getOutputStream());
			objIn = new ObjectInputStream(clientSocket.getInputStream());
			
			// send password to server
			out.println(enteredPassword);
			
			// wait for server to respond if password correct
			String serverMessage = in.readLine();
			if(serverMessage != null) // did the server say something? ("AUTH_SUCCESS")
			{
				System.out.println("\nServer says: " + serverMessage);
				serverMessage = in.readLine();
				if(serverMessage != null && serverMessage.equals("READY")) // did server say "READY"?
				{
					// server has access to database, ready for client requests
					System.out.println("\nServer says: " + serverMessage);
					connected = true;
				}
				else // server said "DB_FAIL"
					System.out.println("\nServer says: " + serverMessage);
			}
		}
		catch(UnknownHostException uhe)
		{
			System.out.println("Unknown host.");
		}
		catch(ConnectException ce)
		{
			System.out.println("Timed out: server not running.");
		}
		catch(IOException ioe)
		{
			System.out.println("Connection refused.");
			ioe.printStackTrace();
		}
		return connected;
	}
	
	/* sends client request to server to get list of existing accounts in database
	returns list of accounts */
	public List<String> getAccounts()
	{
		List<String> accounts = null;
		
		try
		{
			// create client request object, send through stream to server
			ClientRequest accountsRequest = new ClientRequest(ClientRequest.RequestType.GET_ACCOUNTS);
			objOut.writeObject(accountsRequest);
			accounts = (List<String>) objIn.readObject(); // CREATES WARNING EVERY TIME COMPILED
		}
		catch(IOException ioe)
		{
			System.out.println("Failed to get accounts.");
		}
		catch(ClassNotFoundException cnfe)
		{
			System.out.println("Couldn't find definition of class sent by server.");
		}
		return accounts;
	}
	
	/* takes account name, month as arguments, sends client request to server to get 
		list of charges in specified account made in specified month
	returns list of charges */
	public ChargeList getCharges(String accountName, int month)
	{
		ChargeList charges = null;
		
		try
		{
			// create client request object, send through stream to server
			ClientRequest getChargesRequest = new ClientRequest(ClientRequest.RequestType.GET_CHARGES, accountName, month);
			objOut.writeObject(getChargesRequest);
			charges = (ChargeList) objIn.readObject(); // MIGHT CREATE WARNING DURING COMPILE, DON'T KNOW
		}
		catch(IOException ioe)
		{
			System.out.println("Failed to get accounts.");
		}
		catch(ClassNotFoundException cnfe)
		{
			System.out.println("Couldn't find definition of class sent by server.");
		}
		return charges;
	}
	
	/* takes account name as argument, sends client request to server to add specified 
		account to database
	returns true if account successfully created, otherwise false */
	public boolean addAccount(String accountName)
	{
		boolean accountAdded = false;
		try
		{
			// create client request object, send through stream to server
			ClientRequest addAccountRequest = new ClientRequest(ClientRequest.RequestType.ADD_ACCOUNT, accountName);
			objOut.writeObject(addAccountRequest);
			String serverMessage = in.readLine();
			if(serverMessage != null && serverMessage.equals("ADD_ACCOUNT_SUCCESS"))
			{
				System.out.println("Server says: " + serverMessage); // debugging
				accountAdded = true;
			}
		}
		catch(IOException ioe)
		{
			System.out.println("Failed to add account.");
		}
		return accountAdded;
	}
	
	/* takes account name as argument, sends client request to server to remove specified 
		account from database
	returns true if account successfully removed, otherwise false */
	public boolean removeAccount(String accountName)
	{
		boolean accountRemoved = false;
		try
		{
			// create client request object, send through stream to server
			ClientRequest removeAccountRequest = new ClientRequest(ClientRequest.RequestType.REMOVE_ACCOUNT, accountName);
			objOut.writeObject(removeAccountRequest);
			String serverMessage = in.readLine();
			if(serverMessage != null && serverMessage.equals("REMOVE_ACCOUNT_SUCCESS"))
			{
				System.out.println("Server says: " + serverMessage); // debugging
				accountRemoved = true;
			}
		}
		catch(IOException ioe)
		{
			System.out.println("Failed to remove account.");
		}
		return accountRemoved;
	}
	
	/* takes account name, charge object as arguments, sends client request to server to 
		add specified charge to specified account in database
	returns true if charge successfully added to account, otherwise false */
	public boolean addCharge(String accountName, Charge charge)
	{
		boolean chargeAdded = false;
		try
		{
			// create client request object, send through stream to server
			ClientRequest addChargeRequest = new ClientRequest(ClientRequest.RequestType.ADD_CHARGE, accountName, charge);
			objOut.writeObject(addChargeRequest);
			String serverMessage = in.readLine();
			if(serverMessage != null && serverMessage.equals("ADD_CHARGE_SUCCESS"))
			{
				System.out.println("Server says: " + serverMessage); // debugging
				chargeAdded = true;
			}
		}
		catch(IOException ioe)
		{
			System.out.println("Failed to add charge.");
		}
		return chargeAdded;
	}
	
	/* takes account name, charge object as arguments, sends client request to server to 
		remove specified charge from specified account in database
	returns true if charge successfully removed from account, otherwise false */
	public boolean removeCharge(String accountName, Charge charge)
	{
		boolean chargeRemoved = false;
		try
		{
			// create client request object, send through stream to server
			ClientRequest removeChargeRequest = new ClientRequest(ClientRequest.RequestType.REMOVE_CHARGE, accountName, charge);
			objOut.writeObject(removeChargeRequest);
			String serverMessage = in.readLine();
			if(serverMessage != null && serverMessage.equals("REMOVE_CHARGE_SUCCESS"))
			{
				System.out.println("Server says: " + serverMessage); // debugging
				chargeRemoved = true;
			}
		}
		catch(IOException ioe)
		{
			System.out.println("Failed to remove charge.");
		}
		return chargeRemoved;
	}
	
	// closes streams and socket connection to server
	public void disconnectFromServer()
	{
		try
		{
			out.close();
			in.close();
			clientSocket.close();
			System.out.println("Connection ended.");
		}
		catch(IOException ioe)
		{
			System.out.println("Failed to disconnect.");
		}
	}
}