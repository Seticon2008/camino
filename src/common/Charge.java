/*
Represents a credit card charge.
A charge has 3 bits of information:
*	charge ID: 10-digit integer supposedly from 1900010101-2199123199. The first 8 digits 
	correspond to the year, month, day that the charge was made. The last 2 digits 
	correspond to the charge of the day.
*	charge amount
*	reason for the charge

TODO:
*/

import java.io.Serializable;

public class Charge implements Serializable
{
	private int id = 0;
	private float amount = 0.0f;
	private String reason = "";
	private Charge next = null;
	
	// constructor
	public Charge(int id, float amount, String reason)
	{
		this.id = id;
		this.amount = amount;
		this.reason = reason;
	}
	
	// getter method for charge ID
	public int getId()
	{
		return this.id;
	}
	
	// getter method for charge amount
	public float getAmount()
	{
		return this.amount;
	}
	
	// getter method for charge reason
	public String getReason()
	{
		return this.reason;
	}
	
	// getter method for next charge in ChargeList object
	public Charge getNext()
	{
		return this.next;
	}
	
	// setter method for next charge in ChargeList object
	public void setNext(Charge charge)
	{
		this.next = charge;
	}
	
	// prints charge information
	public String toString()
	{
		String str = this.getId() + " " + this.getAmount() + " " + this.getReason();
		return str;
	}
}