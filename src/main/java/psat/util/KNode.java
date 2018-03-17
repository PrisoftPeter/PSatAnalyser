package psat.util;

import java.io.Serializable;

public class KNode implements Comparable<KNode>, Serializable{
	private static final long serialVersionUID = -5709727867868254567L;
	public String id;
    public String name;
    public KNode(String id) {
        this.id = id;
    }
    public String toString() {
        return id;
    }
    
	public int compareTo(KNode kn) {
		int last = this.id.compareTo(kn.id);
        return last == 0 ? this.id.compareTo(kn.id) : last;
	} 
}