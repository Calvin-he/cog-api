package com.cog.api;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class Utils {

	public static String sha1(String key, String salt) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(salt.getBytes("UTF-8"));
			byte[] bytes = md.digest(key.getBytes("UTF-8"));
			return toHexString(bytes);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}	
	}
	
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String toHexString(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	private static long DAY_IN_MILLISECONDS = 24*3600*1000;
	public static boolean isSameDay(Date d1, Date d2) {
		return (d1.getTime()/DAY_IN_MILLISECONDS) == (d2.getTime()/DAY_IN_MILLISECONDS);
	}
}
