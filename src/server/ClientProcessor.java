/*
Processes a client connected over a socket. First authenticates the client, then 
listens for client requests incoming through to socket. When a request is received, 
the request is read and the appropriate operation is called on the chargeDAO object 
to access and or manipulate the database. The appropriate information is sent through 
the socket back to the client.

CONFIGURATION:
Change PASSWORD to desired password on server
*/
import javax.net.ssl.SSLSocket;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

import java.util.List;
import java.util.LinkedList;

public class ClientProcessor implements Runnable
{
	private SSLSocket clientSocket = null;		// holds socket connected to client passed by server
	private PrintWriter out = null;				// stream to send info to client
	private BufferedReader in = null;			// stream to get password entered by client
	private ObjectOutputStream objOut = null;	// stream to send database data to client
	private ObjectInputStream objIn = null;		// stream to listen for client requests
	
	private final String PASSWORD = "password";	// password to server, CHANGE THIS
	
	private ChargeDAO chargeDAO = null;
	
	// constructor
	public ClientProcessor(SSLSocket clientSocket)
	{
		this.clientSocket = clientSocket;
		chargeDAO = new ChargeDAO();
	}
	
	/* reads password received from client through socket
	returns true if client entered correct password to server, otherwise false */
	private boolean validateClient()
	{
		boolean validClient = false;
		
		try
		{
			System.out.println("Validating client...");
			String receivedPassword = in.readLine();
			if(receivedPassword != null && receivedPassword.equals(PASSWORD))
			{
				validClient = true;
				System.out.println("Valid user.");	// debugging
				out.println("AUTH_SUCCESS");
			}
		}
		catch(IOException ioe)
		{
			System.out.println("Failed to validate client.");
		}
		return validClient;
	}
	
	/* connects chargeDAO object to Oracle database
	returns true if successfully connected and is able to acccess database
	
	returns false if failed to connect */
	private boolean activateDAO()
	{
		boolean connectedToDB = chargeDAO.connectToDB();
		if(connectedToDB)
			out.println("READY");
		else
			out.println("DB_FAIL");
		
		return connectedToDB;
	}
	
	/* waits for client request to be sent through socket
	returns the received client request */
	private ClientRequest getClientRequest()
	{
		ClientRequest clientRequest = null;
		System.out.println("Awaiting client request...");
		try
		{
			clientRequest = (ClientRequest) objIn.readObject();
		}
		catch(IOException ioe)
		{
			System.out.println("Failed to get client request. Client disconnected.");
		}
		catch(ClassNotFoundException cnfe)
		{
			System.out.println("Couldn't find definition of class sent by client.");
		}
		return clientRequest;
	}
	
	/* reads the type of client request received, calls the appropriate operation on 
		chargeDAO object to access or manipulate the database */
	private void processClientRequest(ClientRequest clientRequest)
	{
		try
		{
			ClientRequest.RequestType requestType = clientRequest.getRequestType();
			switch(requestType)
			{
				case GET_ACCOUNTS:
					List<String> accounts = chargeDAO.fetchAccounts();
					objOut.writeObject(accounts);
					break;
				case GET_CHARGES:
					String accountName = clientRequest.getAccountName();
					int month = clientRequest.getMonth();
					ChargeList charges = chargeDAO.fetchCharges(accountName, month);
					objOut.writeObject(charges);
					break;
				case ADD_ACCOUNT:
					accountName = clientRequest.getAccountName();
					boolean accountAdded = chargeDAO.addAccount(accountName);
					if(accountAdded)
						out.println("ADD_ACCOUNT_SUCCESS");
					else
						out.println("ADD_ACCOUNT_FAIL");
					break;
				case REMOVE_ACCOUNT:
					accountName = clientRequest.getAccountName();
					boolean accountRemoved = chargeDAO.removeAccount(accountName);
					if(accountRemoved)
						out.println("REMOVE_ACCOUNT_SUCCESS");
					else
						out.println("REMOVE_ACCOUNT_FAIL");
					break;
				case ADD_CHARGE:
					accountName = clientRequest.getAccountName();
					Charge charge = clientRequest.getCharge();
					boolean chargeAdded = chargeDAO.addCharge(accountName, charge);
					if(chargeAdded)
						out.println("ADD_CHARGE_SUCCESS");
					else
						out.println("ADD_CHARGE_FAIL");
					break;
				case REMOVE_CHARGE:
					accountName = clientRequest.getAccountName();
					charge = clientRequest.getCharge();
					boolean chargeRemoved = chargeDAO.removeCharge(accountName, charge);
					if(chargeRemoved)
						out.println("REMOVE_CHARGE_SUCCESS");
					else
						out.println("REMOVE_CHARGE_FAIL");
					break;
			}
		}
		catch(IOException ioe)
		{
			System.out.println("Failed to process client request.");
		}
	}
	
	// closes streams and socket connection to client
	private void disconnectClient()
	{
		try
		{
			System.out.println("Closing socket.");
			out.close();
			in.close();
			clientSocket.close();
			System.out.println("Connection with client ended.");
		}
		catch(IOException ioe)
		{
			System.out.println("Failed to disconnect client.");
		}
	}
	
	/* implements run() function of Runnable interface
	main process loop */
	@Override
	public void run()
	{
		try
		{
			// get streams
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			objOut = new ObjectOutputStream(clientSocket.getOutputStream());
			objIn = new ObjectInputStream(clientSocket.getInputStream());
			
			// get password entered by client
			boolean validClient = validateClient();
			if(validClient) // is the client authorized?
			{
				boolean connectedToDB = activateDAO();
				if(connectedToDB) // is there a connection to the Oracle database?
				{
					// repeatedly receive and process client requests
					ClientRequest clientRequest = getClientRequest();
					while(clientRequest != null)
					{
						processClientRequest(clientRequest);
						clientRequest = getClientRequest();
					}
					disconnectClient();
				}
			}
			else // user is not authorized
			{
				System.out.println("Invalid user.");
				disconnectClient();
			}
		}
		catch(IOException ioe)
		{
			System.out.println("Failed to get streams. Disconnecting client.");
		}
	}
}