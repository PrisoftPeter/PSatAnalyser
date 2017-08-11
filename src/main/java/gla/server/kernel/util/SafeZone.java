package gla.server.kernel.util;

import java.io.Serializable;

public class SafeZone implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public String subjectName;
	public String selfAgentName;
    public int distance;
    
    public SafeZone(String selfAgentName, String subjectName, int k) {
		this.selfAgentName = selfAgentName;
    	this.subjectName = subjectName;
		this.distance = k;
	}
    
}
