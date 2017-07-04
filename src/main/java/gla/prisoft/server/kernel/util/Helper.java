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
	
	public static String [] generatePermutations(String[] s,int i,int k,String[] buff) {
		String [] permutations = new String[0];
        if(i<k) {
            for(int j=0;j<s.length;j++) {

                buff[i] = s[j];
                generatePermutations(s,i+1,k,buff);
            }
        }       
        else {
        	String seq = "";
	        for(String ss:buff){
	            seq = seq+ss+" ";
	        }
	        permutations = (String[])addObjectToArray(permutations, seq) ;
        }
        
        return permutations;
    }
	
}
