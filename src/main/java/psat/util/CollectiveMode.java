package psat.util;

import java.io.Serializable;

public class CollectiveMode implements Serializable{
	private static final long serialVersionUID = 1L;
		
	public static String getModeDesc(CollectiveStrategy str){
		String desc = "";
		switch (str) {
	        case NONE:
	             desc = "";
	             break;
	        case DG:
	             desc = "it can be inferred that"; //distributed knowledge //?? reword
	             break;
//	        case BP:
//		       	 desc = "atleast one user beliefs/uncertain that";
//		       	 break;
//	        case SG:
//		       	 desc = "more than one user beliefs/uncertain that";
//		       	 break;
	        case EG:
		       	 desc = "everyone beliefs/uncertain that";
		       	 break;
	        case EEG:
		       	 desc = "everyone beliefs/uncertain that everyone knows that";
		       	 break;
	        case EEEG:
		       	 desc = "everyone beliefs/uncertain that everyone knows that everyone knows that";
		       	 break;
	        case CG:
		       	 desc = "it is common knowledge that"; //common knowledge EG+ EEG +EEEG
		       	 break;
	        default:
				 break;
	        
	   } 
	  return desc;
	}
	
	public static CollectiveStrategy getCollectiveStrategy(String desc){
		if(desc.equals("")){
			return CollectiveStrategy.NONE;
		}
		else if(desc.equals("it can be inferred that")){ //?? reword
			return CollectiveStrategy.DG;
		}
//		else if(desc.equals("atleast one user beliefs/uncertain that")){
//			return CollectiveStrategy.BP;
//		}
//		else if(desc.equals("more than one user beliefs/uncertain that")){
//			return CollectiveStrategy.SG;
//		}
		else if(desc.equals("everyone beliefs/uncertain that")){
			return CollectiveStrategy.EG;
		}
		else if(desc.equals("everyone beliefs/uncertain that everyone knows that")){
			return CollectiveStrategy.EEG;
		}
		else if(desc.equals("everyone beliefs/uncertain that everyone knows that everyone knows that")){
			return CollectiveStrategy.EEEG;
		}
		else if(desc.equals("it is common knowledge that")){//EG+ EEG +EEEG
			return CollectiveStrategy.CG;
		}
		return null;
		
	}
	
	public static String getModeLimitHtmlDesc(CollectiveStrategy str){
		String desc = "";
		switch (str) {
	        case NONE:
	            desc = "";
	            break;
	        case DG:
	            desc = "<i>D</i><sub>G</sub>"; //distributed knowledge
	            break;
//	        case BP:
//		       	 desc = "<i>B</i><sub>G</sub>";
//		       	 break;
//	        case SG:
//		       	 desc = "<i>S</i><sub>G</sub>";
//		       	 break;
	        case EG:
		       	 desc = "<i>E</i><sub>G</sub>";
		       	 break;
	        case EEG:
		       	 desc = "<i>EE</i><sub>G</sub>";
		       	 break;
	        case EEEG:
		       	 desc = "<i>EEE</i><sub>G</sub>";
		       	 break;
	        case CG:
		       	 desc = "<i>C</i><sub>G</sub>"; //common knowledge
		       	 break;
	        default:
				break;
	        
	   } 
	  return desc;
	}
}

