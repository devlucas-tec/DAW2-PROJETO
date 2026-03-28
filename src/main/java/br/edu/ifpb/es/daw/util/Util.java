package br.edu.ifpb.es.daw.util;

import java.util.Calendar;
import java.util.Date;

public class Util {

	public static Date getDate(int ano, int mes, int dia) {
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, ano);
		cal.set(Calendar.MONTH, mes);
		cal.set(Calendar.DAY_OF_MONTH, dia);
		
		return cal.getTime();
	}

	public static Date removeTime(Date date) {    
	    Calendar cal = Calendar.getInstance();  
	    cal.setTime(date);  
	    cal.set(Calendar.HOUR_OF_DAY, 0);  
	    cal.set(Calendar.MINUTE, 0);  
	    cal.set(Calendar.SECOND, 0);  
	    cal.set(Calendar.MILLISECOND, 0);  
	    return cal.getTime(); 
	}

	public static long getRandomCC() {
		long min = 1000000000000000L;
		long max = 9999999999999999L;
		return (long) Math.floor(Math.random() * (max - min + 1)) + min;
	}

}
