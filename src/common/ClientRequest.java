/*
Represents a client request to the server to access or manipulate the charge database.
Possible types of client requests are listed in the RequestType enum.

Once instantiated, should be sent over a socket connection to the server through
an ObjectOutputStream. The server can then receive the client request through its 
ObjectOutputStream and process the request.

TODO:
*/

import java.io.Serializable;

public class ClientRequest implements Serializable
{
	public enum RequestType
	{
		GET_ACCOUNTS, // client wants to get list of existing accounts
		GET_CHARGES, // client wants to get list of charges in specified account made during specified month
		ADD_ACCOUNT, // client wants to add a new account
		REMOVE_ACCOUNT, // client wants to remove an account
		ADD_CHARGE, // client wants to add a new charge to specified account
		REMOVE_CHARGE // client wants to remove an existing from from specified account
	}
	
	private RequestType requestType = null;
	private String accountName = "";
	private int month;
	private Charge charge = null;
	
	// constructor used for GET_ACCOUNTS request type
	public ClientRequest(RequestType requestType)
	{
		this.requestType = requestType; // client wants to get list of existing accounts
	}
	
	// constructor used for ADD_ACCOUNT, REMOVE_ACCOUNT request types
	public ClientRequest(RequestType requestType, String accountName)
	{
		this.requestType = requestType; // client wants to add/remove an account
		this.accountName = accountName; // name of account to add/remove
	}
	
	// constructor used for GET_CHARGES request type
	public ClientRequest(RequestType requestType, String accountName, int month)
	{
		this.requestType = requestType; // client wants to get charges from an account
		this.accountName = accountName; // name of account to get charges from
		this.month = month;
	}
	
	// constructor used for ADD_CHARGE, REMOVE_CHARGE request types
	public ClientRequest(RequestType requestType, String accountName, Charge charge)
	{
		this.requestType = requestType; // client wants to add/remove a charge from an account
		this.accountName = accountName; // name of account to add/remove charge to/from
		this.charge = charge; // charge to add/remove to/from database
	}
	
	// getter method for request type
	public RequestType getRequestType()
	{
		return requestType;
	}
	
	// getter method for account name
	public String getAccountName()
	{
		return accountName;
	}
	// getter method for month
	public int getMonth()
	{
		return month;
	}
	// getter method for charge
	public Charge getCharge()
	{
		return charge;
	}
}