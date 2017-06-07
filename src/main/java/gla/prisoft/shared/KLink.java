package gla.prisoft.shared;

import java.io.Serializable;


public class KLink implements Comparable<KLink>, Serializable{
	private static final long serialVersionUID = -536723880395656989L;
	public double capacity;
    public double weight;
    public int id;
    
    public KLink(double weight, double capacity, int edgeCount) {
        this.id = edgeCount;
        this.weight = weight;
        this.capacity = capacity;
    } 

    public String toString() {
        return "E"+id;
    }

    public int compareTo(KLink kl) {
    	if(id >kl.id){
    		return 1;
    	}
    	else if(id <kl.id){
    		return -1;
    	}
    	else{
    		return 0;
    	}
	} 
    
}