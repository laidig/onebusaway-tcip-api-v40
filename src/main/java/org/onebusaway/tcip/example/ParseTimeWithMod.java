package example;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.supercsv.cellprocessor.CellProcessorAdaptor;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.exception.SuperCsvCellProcessorException;
import org.supercsv.util.CsvContext;

public class ParseTimeWithMod extends CellProcessorAdaptor {
	public ParseTimeWithMod(){
		super();
	}
	
	public ParseTimeWithMod(final CellProcessor next){
		super(next);
	}
	
    public Object execute(final Object value, final CsvContext context) {
        
        validateInputNotNull(value, context); // throws an Exception if the input is null
        
        String str = value.toString();
        
		String[] parts= str.split(":");
		String modifier= parts[1].split("[A-Z]")[1];
		int hour = Integer.parseInt(parts[0]);
		int minute = Integer.parseInt(parts[1]); 
		
		DateTime time = new DateTime();
		time.withHourOfDay(hour);
		time.withMinuteOfHour(minute);
        
		if (modifier ==String.valueOf("B")){
			time.minusDays(1);
		}
		
		if (modifier ==String.valueOf("X")){
			time.plusDays(1);
		}
		
		try {
			return next.execute(time, context);
		} catch (Exception e) {
			throw new SuperCsvCellProcessorException(
	                String.format("Could not parse '%s' as a time", value), context, this);
		}
		
        
}

}
