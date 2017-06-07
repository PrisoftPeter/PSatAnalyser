package gla.prisoft.shared;

import java.io.Serializable;

public class Attribute implements Serializable{
	
	private static final long serialVersionUID = -3877073036486314688L;
	private String attributeId;
	private String subjectName;
	private Object key;
	private Object value;
	
	public String getSubjectName() {
		return subjectName;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	public Object getKey() {
		return key;
	}
	public void setKey(Object key) {
		this.key = key;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	
	public String getAttributeId() {
		return attributeId;
	}
	public void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}
	public String toString(){
		return "("+key+"_"+subjectName+")";		
	}
	public String toHtmlString(){
		return "<i>"+key+"</i>";		
//		return "<i>"+key+"</i><sub>"+subjectName+"</sub>";		
	}
	
	public String toStringExtended(){
		return "("+key+"="+value+":"+subjectName+")";		
	}
	
	public boolean sameAs(Attribute att){
		if(att != null){
			if(subjectName == null && att.getSubjectName()==null){
				return true;
			}
			else if(att.getSubjectName() != null)
				if(subjectName != null)
					if(subjectName.equals(att.getSubjectName())){			
						if(key.equals(att.getKey())){
							if(value.equals(att.getValue())){
								return true;
							}					
						}
			}	
		}		
		return false;
	}
}
