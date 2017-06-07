package gla.prisoft.server.kernel.verification;

import java.io.Serializable;
import java.util.Comparator;


public class Path implements Serializable, Comparable<Path>{
	private static final long serialVersionUID = 1L;
	
	public String [] pathAgents;	
	public String protocolDesc;
	public String protocolId;
	public double entropyValue;	
	public double pathsat;
	public String pathDesc;
	
	@Override
	public int compareTo(Path o) {
		//descending order
		double difference = o.pathsat - this.pathsat;
		if(difference <=0){
			return 0;
		}
		else{
			return 1;
		}
		//return (int) (o.pathsat - this.pathsat);
	}
	
	public static Comparator<Path> pathComparator = new Comparator<Path>() {

		public int compare(Path path1, Path path2) {
			//descending order
			//return path1.compareTo(path2);
			return Double.compare(path2.pathsat, path1.pathsat);
		}

	};
}
