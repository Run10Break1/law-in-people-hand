package com.project.lawinpeoplehand.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TimeUtil {
	
	public static LocalDateTime asStartOfDay(LocalDate d) {
		return d.atStartOfDay();
	}
	
	public static LocalDateTime asEndOfDay(LocalDate d) {
		return d.atTime(23, 59, 59);
	}
	
	public static String toString(LocalDateTime dt) {
		String [] s = dt.toString().split("T");
		return s[0] + " " + s[1];
	}
}
