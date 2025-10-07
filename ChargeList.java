/*
Holds list of Charge objects in increasing order of their IDs.
Provided methods allow for:
*	retrieving information on charge in list
*	adding or removing charges in list
*/

import java.util.Iterator;

public class ChargeList implements Iterable<Charge>
{
	private Charge headCharge = null;	// holds first charge in list
	private int numCharges = 0;			// holds number of charges in list
	
	// constructor
	public ChargeList()
	{
		
	}
	
	// getter method for first charge in list
	public Charge getHead()
	{
		return headCharge;
	}
	
	// getter method for number of charges in list
	public int size()
	{
		return numCharges;
	}
	
	/* takes charge ID as argument, returns charge object in list with specified charge ID
	returns null if charge ID not found
	*/
	public Charge get(int id)
	{
		Charge targetCharge = null;
		if(headCharge != null)
		{
			Charge curr = headCharge;
			Charge prev = null;
			boolean chargeFound = false;
			while(!chargeFound && curr != null)
			{
				if(curr.getId() == id)
				{
					chargeFound = true;
					targetCharge = curr;
				}
				else
				{
					prev = curr;
					curr = curr.getNext();
				}
			}
		}
		return targetCharge;
	}
	
	// takes charge ID as argument, returns true if list contains charge with specified ID, otherwise false
	public boolean exists(int id)
	{
		if(this.get(id) == null)
			return false;
		else
			return true;
	}
	
	// empties list of all charges
	public void clear()
	{
		this.headCharge = null;
		this.numCharges = 0;
	}
	
	/* takes charge object as argument, adds charge to appropriate position in list
	returns true if charge successfully added
	returns false if charge with same ID already exists in list
	*/
	public boolean add(Charge charge)
	{
		boolean chargeAdded = false;
		int id = charge.getId();

		if(headCharge == null) // is list empty?
		{
			headCharge = charge;
			chargeAdded = true;
			numCharges = 1;
		}
		else if(this.get(id) == null) // is specified charge ID not already in list?				// IS THIS EFFICIENT?
		{
			// current pointer starts at first charge in list
			Charge curr = headCharge;
			Charge prev = null;
			while(!chargeAdded && curr != null) // loop until end of list reached or charge added
			{
				if(curr.getId() > id) // is charge ID in current pointer > specified charge ID?
				{
					if(prev == null) // is current pointer at first charge in list?
					{
						// specified charge becomes first charge in list
						headCharge = charge;
						headCharge.setNext(curr);
						chargeAdded = true;
						numCharges++;
					}
					else // current pointer position > first position
					{
						// place specified charge at current pointer position, move rest of list down by 1 position
						prev.setNext(charge);
						charge.setNext(curr);
						chargeAdded = true;
						numCharges++;
					}
				}
				else // charge ID in current pointer < specified charge ID
				{
					// look at next charge in list
					prev = curr;
					curr = curr.getNext();
				}
			}
			if(!chargeAdded) // has charge been added yet?
			{
				// either charge list empty, or specified charge being placed at end of list?
				prev.setNext(charge);
				chargeAdded = true;
				numCharges++;
			}
		}
		return chargeAdded;
	}
	
	// takes charge object as argument, removes charge with specified ID from list
	public boolean remove(Charge charge)
	{
		return this.remove(charge.getId());
	}
	
	// takes charge ID as argument, removes charge with specified ID from list
	public boolean remove(int id)
	{
		boolean chargeRemoved = false;
		
		if(headCharge != null) // does list contain charges?
		{
			if(headCharge.getId() == id) // does first charge in list match specified ID?
			{
				// remove first charge from list
				headCharge = headCharge.getNext();
				chargeRemoved = true;
				numCharges--;
			}
			else // first charge doesn't match specified ID
			{
				// look at second charge in list
				Charge curr = headCharge.getNext();
				Charge prev = headCharge;
				while(!chargeRemoved && curr != null) // loop until end of list reached or charge removed
				{
					if(curr.getId() == id) // does charge ID in current pointer match specified ID?
					{
						// remove charge at current pointer, move rest of list up by 1 position
						prev.setNext(curr.getNext());
						chargeRemoved = true;
						numCharges--;
					}
					else // current pointer charge ID doesn't match specified charge ID
					{
						// look at next charge in list
						prev = curr;
						curr = curr.getNext();
					}
				}
			}
		}
		return chargeRemoved;
	}
	
	// returns iterator for list
	public Iterator<Charge> iterator()
	{
		return new ChargeListIterator(this);
	}
}