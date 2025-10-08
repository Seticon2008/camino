import java.util.Scanner;

import java.util.Map;
import java.util.TreeMap;

public class ChargeCalculatorInterface extends ChargeInterface
{
	public void displaySummary()
	{
		this.printHeader();
		
		ChargeList charges = chargeFileManager.getCharges();
		Map<String, Float> summary = ChargeCalculator.summarize(charges);
		String format = "%-40s%8s%n";
		
		System.out.println("Resumen de cargas:");
		System.out.printf(format, "razón", "cantidad");
		for(Map.Entry<String, Float> sum : summary.entrySet())
			System.out.printf(format, sum.getKey(), String.format("%.02f", sum.getValue()));
	}
	
	// driver
	public static void main(String[] args)
	{
		ChargeCalculatorInterface chargeCalcInterface = new ChargeCalculatorInterface();
		Scanner enter = new Scanner(System.in);
		
		boolean selected = false; // specifies whether user successfully selects existing account/month
		
		while(chargeCalcInterface.quitProgramRequest == false) // loops until user wants to exit program
		{
			chargeCalcInterface.resetAll();
			chargeCalcInterface.displayAccounts();
			selected = chargeCalcInterface.selectAccount(); // prompt user to select account or exit program
			if(selected) // did user successfully select existing account?
			{
				while(chargeCalcInterface.exitAccountRequest == false) // loops until user wants to exit account selected
				{
					chargeCalcInterface.displayMonths();
					selected = chargeCalcInterface.selectMonth(); // prompt user to select month or exit account
					if(selected) // did user successfully select existing month in selected account?
					{
						chargeCalcInterface.displaySummary();
						System.out.println("\nEntrar para continuar.");
						enter.nextLine(); // get user input to exit month
						chargeCalcInterface.chargeFileManager.resetMonth(); // exit month
					}
				}
			}
		}
		System.out.println("Terminando...");
	}
}