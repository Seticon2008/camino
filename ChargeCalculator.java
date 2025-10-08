import java.util.Map;
import java.util.TreeMap;

public class ChargeCalculator
{
	public static Map<String, Float> summarize(ChargeList charges)
	{
		Map<String, Float> summary = new TreeMap<>();
		for(Charge charge : charges)
		{
			String reason = charge.getReason();
			float amount = charge.getAmount();
			
			if(!summary.containsKey(reason))
				summary.put(reason, amount);
			else
			{
				float sum = summary.get(reason) + amount;
				summary.put(reason, sum);
			}
		}
		return summary;
	}
}