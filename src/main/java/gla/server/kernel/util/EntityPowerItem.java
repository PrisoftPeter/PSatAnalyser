package gla.server.kernel.util;

import java.io.Serializable;
import java.util.ArrayList;

public class EntityPowerItem implements Serializable{
	private static final long serialVersionUID = 739438116400199068L;
	private String [] enames;
	private int pid;
	
	public EntityPowerItem(int pid){
		enames = new String[0];
		this.pid = pid;
	}
	
	public void addToEnames(String s){
		boolean exist = false;
		for(String n:enames){
			if(n.equals(s)){
				exist =true;
				break;
			}
		}
		if(exist){
			return;
		}
		String [] temp = new String[enames.length+1];
		for(int i=0;i<enames.length;i++){
			temp[i] = enames[i];
		}
		temp[enames.length] = s;			
		enames = temp;
		
	}
	
	public void removeEname(int k){
		String [] temp = new String[enames.length-1];
		ArrayList<String> tws= new ArrayList<String>();
		
		for(int i=0;i<enames.length;i++){
			if(i != k){
				tws.add(enames[i]);							
			}
		}
		for(int i=0; i<tws.size();i++){
			temp[i] = tws.get(i);	
		}
		enames = temp;		
	}
	
	
	public int getId(){
		return pid;
	}
	
	public String[] getEnames(){
		return enames;
	}
}
