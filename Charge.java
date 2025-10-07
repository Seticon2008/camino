/*
Represents a credit card charge.
A charge has 3 bits of information:
*	charge ID: 4-digit integer from 0101-3199. The first 2 digits correspond to the day
	of the month that the charge was made. The last 2 digits correspond to the charge of the
	day.
*	charge amount
*	reason for the charge

TODO:
Validate the charge ID here (0100-3200), rather than in Bank.java. The charge amount
should be validated as well, and shouldn't be able to equal a negative value.
*/

public class Charge
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
	public double getAmount()
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