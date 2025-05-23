package com.codingshop.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	//date -> String
	public static String convertToString(Date d) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = sdf.format(d); // 날짜가 문자로 변경
		return str;
	}
	
	//String -> date
	public static Date convertToDate(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d = sdf.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return d;
	}

	public static java.sql.Date convertTosqlDate(Date d) {
		return new java.sql.Date(d.getTime());
	}
}
