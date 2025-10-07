// Allows iterating through ChargeList object using enhanced for loop

import java.util.Iterator;

public class ChargeListIterator implements Iterator<Charge>
{
	Charge curr = null; // current pointer, holds charge in list currently looking at
	
	// constructor
	public ChargeListIterator(ChargeList list)
	{
		curr = list.getHead(); // set current pointer to first charge in list
	}
	
	// returns true if not yet at end of list
	public boolean hasNext()
	{
		return curr != null;
	}
	
	// moves current pointer to next charge in list
	public Charge next()
	{
		Charge charge = curr;
		curr = curr.getNext();
		return charge;
	}
}