package gla.server.kernel.util;


import edu.uci.ics.jung.graph.Graph;
import gla.prisoft.shared.ConfigInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author joao
 */
public class DFS<E> {
    @SuppressWarnings("rawtypes")
	private Graph g;
    private E sourceNode;
    private E targetNode;
    private List<ArrayList<E>> paths;
    private int maxPathSize;
    
    @SuppressWarnings("rawtypes")
	public DFS(Graph g, E sourceNode, E targetNode, int maxPathSize){
        this.g = g;
        this.sourceNode = sourceNode;
        this.targetNode = targetNode;
        this.maxPathSize = maxPathSize;
        paths = new ArrayList<ArrayList<E>>();
    }
    
    public void Initialize(ConfigInstance instance){
        if(sourceNode != null){
        	List<E> visited = new ArrayList<E>();
            visited.add(sourceNode);
            this.findAllPaths_(g, visited, paths, sourceNode, instance);	
        }        
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	private void findAllPaths_(Graph graph, List<E> visited, List<ArrayList<E>> paths, Object currentNode, ConfigInstance instance) {        

	    if(paths.size() >= instance.max_no_analysis_paths){
	    	return;
	    }
	    else if (targetNode != null && currentNode.equals(targetNode)) { 
	        paths.add(new ArrayList(Arrays.asList(visited.toArray())));
	        //System.out.println(new ArrayList(Arrays.asList(visited.toArray())));
	        return;
	    }
	    else {
	        List<E> nodes = new ArrayList(g.getNeighbors(currentNode));    
	        for (E node : nodes) {
	            if (visited.contains(node)) {
	                continue;
	            } 
	            List<E> temp = new ArrayList<>();
	            temp.addAll(visited);
	            temp.add(node);
	            if(temp.size() <= this.maxPathSize) findAllPaths_(graph, temp, paths, node, instance);
	        }
	    }
	}

    @SuppressWarnings("rawtypes")
	public List getPaths(){
        return paths;
    }
    
    @SuppressWarnings("rawtypes")
	public List getShortestPaths(){
        List<List> result = new ArrayList<>();
        int pathSize = 0;
        int minPathSize = Integer.MAX_VALUE;
        
        for(List path : paths){
            pathSize = path.size();
            if(pathSize < minPathSize){
                minPathSize = pathSize;
                result.clear();
                result.add(path);
            }else if(pathSize == minPathSize){
                result.add(path);
            }
        }
        
        return result;
    }

}