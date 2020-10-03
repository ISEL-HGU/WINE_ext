package edu.handong.csee.isel.fcminer.util;

public class OSValidator {
	 private static String OS = System.getProperty("os.name").toLowerCase();
	  
	 public static String getOS() {
	     if (isWindows()) {
	         return "window";
	     } else if (isMac()) {
	         return "linux";
	     } else if (isUnix()) {
	        return "linux";
	     } 
	     return null;
	 }
	 
	 public static boolean isWindows() {
	     return (OS.indexOf("win") >= 0);
	 }
	 
	 public static boolean isMac() {
	     return (OS.indexOf("mac") >= 0);  
	 }
	
	 public static boolean isUnix() {
	     return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0 );
	 }
}
