package gla.prisoft.shared;

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
	             desc = "it can be inferred that"; //distributed knowledge
	             break;
	        case BP:
		       	 desc = "atleast one user knows that";
		       	 break;
	        case SG:
		       	 desc = "more than one user knows that";
		       	 break;
	        case EG:
		       	 desc = "every user knows that";
		       	 break;
	        case CG:
		       	 desc = "every one knows that every one knows"; //common knowledge
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
		else if(desc.equals("it can be inferred that")){
			return CollectiveStrategy.DG;
		}
		else if(desc.equals("atleast one user knows that")){
			return CollectiveStrategy.BP;
		}
		else if(desc.equals("more than one user knows that")){
			return CollectiveStrategy.SG;
		}
		else if(desc.equals("every user knows that")){
			return CollectiveStrategy.EG;
		}
		else if(desc.equals("every one knows that every one knows")){
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
	        case BP:
		       	 desc = "<i>B</i><sub>p</sub>";
		       	 break;
	        case SG:
		       	 desc = "<i>S</i><sub>G</sub>";
		       	 break;
	        case EG:
		       	 desc = "<i>E</i><sub>G</sub>";
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

