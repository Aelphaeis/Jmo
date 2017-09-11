package jmo.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class JmoDate extends Date {
	
	public static boolean isOn(Date onDate, Date dateToCompare){
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
		return fmt.format(onDate).equals(fmt.format(dateToCompare));
	}
	

	public static Date truncateTime(Date d){
		Calendar c = Calendar.getInstance();
		c.setTime(d);
		c.set(Calendar.HOUR_OF_DAY, c.getActualMinimum(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, c.getActualMinimum(Calendar.MINUTE));
		c.set(Calendar.SECOND, c.getActualMinimum(Calendar.SECOND));
		c.set(Calendar.MILLISECOND, c.getActualMinimum(Calendar.MILLISECOND));
		return c.getTime();
	}
	
	private static final long serialVersionUID = 1L;
	
	public void truncateTime(){
		this.setTime(truncateTime(this).getTime());
	}
	
	public boolean isOn(Date date){
		return isOn(this, date);
	}
}
