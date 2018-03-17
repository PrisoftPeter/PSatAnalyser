package psat.util;

public class DecisionCategory {
	 public static final int CAT1 =1;
	 public static final int CAT2 =2;
	 public static final int CAT3 =3;
	 public static final int CAT4 =4;
	 public static final int CAT5 =5;
	 public static final int CAT6 =6;

	 public static String getDecision(int cat){
		 String decision = null;
		 
		 switch(cat){
		 	case 1:decision = "YES";
		 		   break;
		 	case 2:decision = "MAYBE";
		 		   break;
		 	case 3:decision = "NO";
		 		break;
		 	case 4:decision = "YES";
	 		   break;
		 	case 5:decision = "MAYBE";
	 		   break;
		 	case 6:decision = "NO";
	 		   break;			 
		 }
		 
		 return decision;
	 }
	 
	 public static String getCatDescription(int cat){
		 String catdesc = null;
		 
		 switch(cat){
		 	case 1:catdesc = "cat.1";
		 		   break;
		 	case 2:catdesc = "cat.2";
		 		   break;
		 	case 3:catdesc = "cat.3";
		 		break;
		 	case 4:catdesc = "cat.4";
	 		   break;
		 	case 5:catdesc = "cat.5";
	 		   break;
		 	case 6:catdesc = "cat.6";
	 		   break;
		 }
		 
		 return catdesc;		 
	 }
	 
	 
}
