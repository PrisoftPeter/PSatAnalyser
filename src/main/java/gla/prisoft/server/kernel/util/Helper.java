package gla.prisoft.server.kernel.util;

public class Helper {
	public static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	public static double RoundTo2Decimals(double val) {
		if(Double.isNaN(val)){
			return val;
		}
		double roundOff = Math.round(val * 100.0) / 100.0;
		return roundOff;
	}
	
	public static Object[] addObjectToArray(Object [] objectArray, Object object){
		Object [] tempo = new Object[objectArray.length +1];
		
		for(int i=0;i<objectArray.length;i++){
			tempo[i] = objectArray[i];
		}
		tempo[objectArray.length] = object;
		
		return tempo;
	}
	
	public static String[] addUniqueStringToArray(String [] stringArray, String s){
		boolean unique = true;
		if(s == null){
			return stringArray;
		}
		for(String sx:stringArray){
			if(sx.trim().equals(s.trim())){
				unique = false;
			}
		}
		
		if(!unique){
			return stringArray;
		}
		
		String [] tempo = new String[stringArray.length +1];
		
		for(int i=0;i<stringArray.length;i++){
			tempo[i] = stringArray[i];
		}
		tempo[stringArray.length] = s;
		
		return tempo;
	}	
}
