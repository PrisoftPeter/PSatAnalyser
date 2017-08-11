package gla.server.kernel.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.uci.ics.jung.algorithms.shortestpath.Distance;
import edu.uci.ics.jung.algorithms.shortestpath.UnweightedShortestPath;
import edu.uci.ics.jung.graph.UndirectedGraph;
import gla.prisoft.shared.KLink;
import gla.prisoft.shared.KNode;

public class GraphAnalyser {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map clusteringCoefficient(UndirectedGraph<KNode, KLink> g){
		Map coefficients = new HashMap();

		Collection<KNode> vertices = g.getVertices();

		for(KNode vertice:vertices){
			int n = g.getNeighborCount(vertice);

			if (n == 0){
				coefficients.put(vertice, new Double(0));
			}
			else if (n == 1){
				coefficients.put(vertice, new Double(1));
			}
			else{
				// how many of v's neighbors are connected to each other?
				ArrayList neighbors = new ArrayList(g.getNeighbors(vertice));
				double edge_count = 0;
				for (int i = 0; i < neighbors.size(); i++){
					KNode w = (KNode)neighbors.get(i);
					for (int j = i+1; j < neighbors.size(); j++ ){
						KNode x = (KNode)neighbors.get(j);
						edge_count += g.isNeighbor(w, x) ? 1 : 0;
					}
				}
				double possible_edges = (n * (n - 1))/2.0;
				coefficients.put(vertice, new Double(edge_count / possible_edges));
			}
		}
		return coefficients;
	}

	@SuppressWarnings("rawtypes")
	public static double averageClusteringCoefficient(UndirectedGraph<KNode, KLink> g){
		Map coefficients = clusteringCoefficient(g);

		Iterator it = coefficients.entrySet().iterator();
		double nodeCounter=0;
		double coeffSum=0;

		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			nodeCounter = nodeCounter +1;
			coeffSum = coeffSum +(Double)pair.getValue();
		}

		double ave = coeffSum/nodeCounter;

		return ave;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map averageDistances(UndirectedGraph<KNode, KLink> g, Distance d) {
		Map avg_dist = new HashMap();
		Collection<KNode> vertices = g.getVertices();
		int n = g.getVertexCount();
		for (Iterator outer = vertices.iterator(); outer.hasNext(); ){
			KNode v = (KNode)outer.next();
			double avgPathLength = 0;
			for (Iterator inner = vertices.iterator(); inner.hasNext(); ){
				KNode w = (KNode)inner.next();
				if (v != w) // don't include self-distances
				{
					Number dist = d.getDistance(v, w);
					if (dist == null)
					{
						avgPathLength = Double.POSITIVE_INFINITY;
						break;
					}
					avgPathLength += dist.doubleValue();
				}
			}
			avgPathLength /= (n - 1);
			avg_dist.put(v, new Double(avgPathLength));
		}
		return avg_dist;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map averageDistances(UndirectedGraph<KNode, KLink> g)	{
		return averageDistances(g, new UnweightedShortestPath((UndirectedGraph<KNode, KLink>)g));
	}
	
	@SuppressWarnings("rawtypes")
	public static double averageofAverageDistance(UndirectedGraph<KNode, KLink> g){
		Map averages = averageDistances(g);
		
		Iterator it = averages.entrySet().iterator();
		double counter = 0;
		double distanceSum = 0;
	    while (it.hasNext()) {
	    	counter = counter +1;
	        Map.Entry pair = (Map.Entry)it.next();
	        distanceSum = distanceSum + (Double)pair.getValue();
	    }
	    
	    double aveOfave = distanceSum/counter;
	    
	    return aveOfave;
	}
	
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static double diameter(UndirectedGraph<KNode, KLink> g, Distance d, boolean use_max){
        double diameter = 0;
		Collection<KNode> vertices = g.getVertices();
        for (Iterator outer = vertices.iterator(); outer.hasNext(); ){
        	KNode v = (KNode)outer.next();
            for (Iterator inner = vertices.iterator(); inner.hasNext(); ){
            	KNode w = (KNode)inner.next();
                if (v != w) // don't include self-distances
                {
                    Number dist = d.getDistance(v, w);
                    if (dist == null)
                    {
                        if (!use_max)
                            return Double.POSITIVE_INFINITY;
                    }
                    else
                        diameter = Math.max(diameter, dist.doubleValue());
                }
            }
        }
        return diameter;
    }
    
    @SuppressWarnings("rawtypes")
	public static double diameter(UndirectedGraph<KNode, KLink> g, Distance d){
        return diameter(g, d, false);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
	public static double diameter(UndirectedGraph<KNode, KLink> g)
    {
        return diameter(g, new UnweightedShortestPath((UndirectedGraph<KNode, KLink>)g));
    }
}
