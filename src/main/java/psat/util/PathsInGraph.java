package psat.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import psat.PSatAPI;


public class PathsInGraph {

	private List<List<KLink>> allPaths;

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String [] getPaths(){

		String paths [] = new String[0]; 
		
		if(PSatAPI.instance == null){
			return paths;
		}

		PSatAPI.instance.pathAgentNames = new ArrayList<String>();
		
		KNode source = null;
		KNode target = null;
		if(PSatAPI.instance.g !=null) {
			Collection<KNode> vertices = PSatAPI.instance.g.getVertices();
			
			for(KNode vertice:vertices){
				if(vertice !=null){
					if(vertice.toString().equals(PSatAPI.instance.sourceAgentName)){
						source = vertice;
					}
					else if(vertice.toString().equals(PSatAPI.instance.targetAgentName)){
						target = vertice;
					}
					if(source != null && target != null){
						break;
					}	
				}			
			}	
		}
		
		
		DFS<Integer> alg = new DFS(PSatAPI.instance.g,source,target, PSatAPI.instance.max_analysis_path_length);
		alg.Initialize();
		List<ArrayList<KNode>> algpaths = alg.getPaths();		
		//System.out.println(g.getVertices());
		
		String tpaths [] = new String[0];
		for(ArrayList<KNode> path: algpaths){
			String path_desc ="";
			for(KNode knode:path){
				path_desc = path_desc +" "+ knode.toString();
				
				boolean contained =false;
				for(String s:PSatAPI.instance.pathAgentNames){
					if(s.equals(knode.toString())){
						contained = true;
					}
				}
				
				if(!contained){
					PSatAPI.instance.pathAgentNames.add(knode.toString());					
				}
			}
			
			String temp [] =new String[tpaths.length+1];
			for(int j=0;j<tpaths.length;j++){
				temp[j] = tpaths[j];
			}
			temp[tpaths.length] = path_desc;
			
			tpaths = temp;
		}
		Arrays.sort(tpaths);
		
//		Collections.shuffle(Arrays.asList(tpaths));//tempory shuffle array.. to be removed
		
		int i = 1;
		for(String path: tpaths){
			String path_desc = i+": "+path;
			
			String temp [] =new String[paths.length+1];
			for(int j=0;j<paths.length;j++){
				temp[j] = paths[j];
			}
			temp[paths.length] = path_desc;
			
			paths = temp;
			
			i = i+1;
		}
		
		if(PSatAPI.instance.networkType == NetworkType.SEQUENTIAL){
			int j =1;
			String[] pathsadd = new String[0];
			String[] newpaths = new String[0]; 
			pathsadd = additionalSequencePaths(paths);
			for(String apath:pathsadd){
				String path_desc = j+":"+apath;
				newpaths = Helper.addUniqueStringToArray(newpaths, path_desc);
				
				j = j+1;
			}
			return newpaths;
			
		}
		
		return paths;
	}
	
	@SuppressWarnings("unchecked")
	public static String [] loadProtocolSuite(){
		DirectedGraph<KNode, KLink> g = GMLGraphLoader.loadGraph2();

		String protocolSuite [] = new String[0]; 
		PathsInGraph pig = new PathsInGraph();
		List<List<KLink>> apaths = pig.processAllPathsLocalcall(g);
		
		int i = 1;
		for(List<KLink> path: apaths){
			String path_desc = "(";
//			String path_desc = i+" (";
			boolean first = true;
			for(KLink kl:path){
				KNode source = g.getSource(kl);
				KNode target = g.getDest(kl);
				if(first){
					path_desc = path_desc+source.toString() +","+target.toString();
					first = false;
				}
				else{
					path_desc = path_desc+","+target.toString();	
				}
			}
			path_desc = path_desc.replace("Start,", "");
			path_desc = path_desc.replace(",End", "");
			path_desc = path_desc+")";
			
			String temp [] =new String[protocolSuite.length+1];
			for(int j=0;j<protocolSuite.length;j++){
				temp[j] = protocolSuite[j];
			}
			temp[protocolSuite.length] = path_desc;
			
			protocolSuite = temp;
			
			i = i+1;
		}
		return protocolSuite;
	}
	
	public List<List<KLink>> processAllPathsLocalcall(DirectedGraph<KNode, KLink> g){

		KNode source_kn = null;
		KNode target_kn = null;
		
		for(KNode ngn: g.getVertices()){
			if(ngn.toString().equals("Start")){
				source_kn = ngn;
			}
			else if(ngn.toString().equals("End")){
				target_kn = ngn;
			}
			
			if(target_kn != null && source_kn != null){
				break;
			}
		}
		
		List<List<KLink>> aps = getAllPathsBetweenNodes(g, source_kn, target_kn, 100); //limit path length to 100
		return aps;
	}
		
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<List<KLink>> processAllPaths(DirectedGraph<KNode, KLink> g){

		Object nodeNames [] = g.getVertices().toArray();
		JComboBox start_c = new JComboBox(nodeNames);
//		
		JPanel disc_panel1 = new JPanel();
		disc_panel1.add(new JLabel("Source:"));
		disc_panel1.add(start_c);
		
		JComboBox end_c = new JComboBox(nodeNames);
		disc_panel1.add(new JLabel("Target:"));
		disc_panel1.add(end_c);
		
		JOptionPane.showConfirmDialog(null, disc_panel1, "Path",  JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
		KNode start = (KNode)start_c.getSelectedItem();
		KNode end = (KNode)end_c.getSelectedItem();
		
		KNode source_kn = null;
		KNode target_kn = null;
		
		for(KNode ngn: g.getVertices()){
			if(ngn.toString().equals(start.toString())){
				source_kn = ngn;
			}
			else if(ngn.toString().equals(end.toString())){
				target_kn = ngn;
			}
			
			if(target_kn != null && source_kn != null){
				break;
			}
		}
		
		List<List<KLink>> aps = getAllPathsBetweenNodes(g, source_kn, target_kn, 100); //limit path length to 100
		return aps;
	}

	
	public List<List<KLink>> getAllPathsBetweenNodes(DirectedGraph<KNode, KLink> graph,	KNode startNode, KNode endNode, int maxDepth) {

		allPaths = new ArrayList<List<KLink>>();

		List<KLink> currentPath = new ArrayList<KLink>();

		findAllPaths(startNode, startNode, endNode, currentPath, graph,	maxDepth, 0);
		
		return allPaths;
	}

	public List<List<KLink>> getAllUniqePathsBetweenNodes(DirectedGraph<KNode, KLink> graph, KNode startNode, KNode endNode, int maxDepth) {
		allPaths = new ArrayList<List<KLink>>();

		List<KLink> currentPath = new ArrayList<KLink>();

		findAllUniquePaths(startNode, startNode, endNode, currentPath, graph,maxDepth, 0);

		return allPaths;
	}

	private void findAllPaths(KNode currentNode, KNode startNode, KNode endNode,List<KLink> currentPath, DirectedGraph<KNode, KLink> graph, int maxDepth,int currentDepth) {
		Collection<KLink> outgoingEdges = graph.getOutEdges(currentNode);

		if (currentDepth < maxDepth) {
			for (KLink outEdge : outgoingEdges) {	
				KNode outNode = graph.getDest(outEdge);
				// String outNode = outEdge.getSupertype();

				if (outNode.equals(startNode)) {
					List<KLink> cyclePath = new ArrayList<KLink>(currentPath);
					cyclePath.add(outEdge);
					System.out.println("Found cycle provoked by path "+ cyclePath);
					continue;
				}

				List<KLink> newPath = new ArrayList<KLink>(currentPath);
				newPath.add(outEdge);

				if (outNode.equals(endNode)) {
					allPaths.add(newPath);
					continue;
				}

				findAllPaths(outNode, startNode, endNode, newPath, graph,maxDepth, currentDepth + 1);
			}
		}
	}
	
	private void findAllUniquePaths(KNode currentNode, KNode startNode, KNode endNode,List<KLink> currentPath, DirectedGraph<KNode, KLink> graph, int maxDepth,	int currentDepth) {
		Collection<KLink> outgoingEdges = graph.getOutEdges(currentNode);

		if (currentDepth < maxDepth) {
			for (KLink outEdge : outgoingEdges) {
				KNode outNode = graph.getDest(outEdge);
				// String outNode = outEdge.getSupertype();

				if (outNode.equals(startNode)) {
					List<KLink> cyclePath = new ArrayList<KLink>(currentPath);
					cyclePath.add(outEdge);
					System.out.println("Found cycle provoked by path "+ cyclePath);
					continue;
				}

				List<KLink> newPath = new ArrayList<KLink>(currentPath);
				newPath.add(outEdge);

				if (outNode.equals(endNode)) {
					// Check for unique-ness before adding.
					boolean unique = true;
					// Check each existing path.
					for (int pathItr = 0; pathItr < allPaths.size(); pathItr++) {
						// Compare the edges of the paths.
						for (int edgeItr = 0; edgeItr < allPaths.get(pathItr).size(); edgeItr++) {
							// If the edges are the same, this path is not
							// unique.
							if (allPaths.get(pathItr).get(edgeItr)
									.compareTo(newPath.get(edgeItr)) == 0) {
								unique = false;
							}
						}

					}
					// After we check all the edges, in all the paths,
					// if it is still unique, we add it.
					if (unique) {
						allPaths.add(newPath);
					}
					continue;
				}
				findAllUniquePaths(outNode, startNode, endNode, newPath, graph,
						maxDepth, currentDepth + 1);
			}
		}
	}
	
	public String[] getAllKNearestNeighbours(UndirectedGraph<KNode, KLink> g, int k, String selfAgentName){		
		KNode source = null;
		Collection<KNode> vertices = g.getVertices();
		
		for(KNode vertice:vertices){
			if(vertice.toString().equals(selfAgentName)){
				source = vertice;
				break;
			}
		}
		k_n = k;
		if(k_n >0){
			kneighbours = new String[0];
			extractNeighbours(g,source, 0);	
		}
		
		return kneighbours;
	}
	private static String [] kneighbours;
	private static int k_n;
	private void extractNeighbours(UndirectedGraph<KNode, KLink> g, KNode currentNode, int kcount){
		
		Collection <KNode> neighbors = g.getNeighbors(currentNode);
		ArrayList<String> aneighbours = new ArrayList<String>();
		for(String s: kneighbours){
			aneighbours.add(s);
		}
				
		for(KNode knode:neighbors){
			boolean contained = false;
			for(String s: kneighbours){
				if(s.equals(knode.toString())){
					contained = true;
					break;
				}
			}
			if(!contained){
				aneighbours.add(knode.toString());	
			}
		}
		String [] temp = new String[aneighbours.size()];
		for(int i=0; i<aneighbours.size();i++){
			temp[i] = aneighbours.get(i);
		}
		kneighbours = temp;
		
		kcount = kcount +1;
		if(kcount>=k_n){
			return;
		}
		else{
			for(KNode knode: neighbors){
				extractNeighbours(g, knode,kcount);
			}
		}		
	}
	
	///	
	
	public String [] findKNearestNeighbours(){
		String paths [] = new String[0]; 
		
		KNode source = null;
		Collection<KNode> vertices = PSatAPI.instance.g.getVertices();
		
		for(KNode vertice:vertices){
			if(vertice.toString().equals(PSatAPI.instance.selfAgentName)){
				source = vertice;
				break;
			}
		}
		
		PSatAPI.instance.pathAgentNames = new ArrayList<String>();
		if(source != null){
			paths = extractPaths(PSatAPI.instance.g,source,paths, "", 0);
		}
		
		int pcounter = 1;
		String [] paths_update = new String[paths.length];
		for(int i=0;i<paths.length;i++){
			paths_update[i] = pcounter+":"+paths[i];
			pcounter = pcounter+1;
		}
		
		if(PSatAPI.instance.networkType == NetworkType.SEQUENTIAL){
			paths = additionalSequencePaths(paths);
		}
		
		return paths_update;
	}
	
	public Properties getSequenceSourceandTarget(){
		Properties properties = new Properties();
		
		KNode source = null;
		KNode target = null;
		Collection<KNode> vertices = PSatAPI.instance.g.getVertices();
		
		for(KNode vertice:vertices){
			Collection<KLink>  edges = PSatAPI.instance.g.getIncidentEdges(vertice);
			if(edges.size() == 1){
				if(source == null){
					source = vertice;
				}
				else{
					target = vertice;
				}				
			}			
			if(source != null && target !=null){
				break;
			}
		}
		
		properties.put("source", source.id);
		properties.put("target", target.id);
		
		return properties;
	}
	
	public String [] extractPaths(UndirectedGraph<KNode, KLink> g, KNode currentNode,String [] paths, String tempPath, int tempk){
		Collection <KNode> neighbors = g.getNeighbors(currentNode);
		
		tempPath = tempPath+" "+currentNode.toString();
		tempk = tempk +1;
		
		boolean exist = false;
		for(String s:PSatAPI.instance.pathAgentNames){
			if(s.equals(currentNode.toString())){
				exist = true;
			}
		}
				
		if(!exist){
			PSatAPI.instance.pathAgentNames.add(currentNode.toString());			
		}
				
		for(KNode nextNode: neighbors){
			if(tempPath.contains(nextNode.toString())){
				if(tempk == PSatAPI.instance.k+1 || neighbors.size()==1){
					boolean contained = false;
					for(String p:paths){
						if(p.equals(tempPath)){
							contained =true;
						}
					}
					if(!contained){
						String []tempPaths = new String[paths.length +1];
						for(int i=0;i<paths.length;i++){
							tempPaths[i] = paths[i];
						}
						if(paths.length >= PSatAPI.instance.max_no_analysis_paths){
							//do nothing
						}
						else{
							tempPaths[paths.length] = tempPath;
							paths = tempPaths;	
						}							
					}
				}
				if(paths.length >= PSatAPI.instance.max_no_analysis_paths){
			    	break;
			    }
				continue;
			}
			if(tempk == PSatAPI.instance.k+1){ //check that k nearest neighbor is reached
				boolean contained = false;
				for(String p:paths){
					if(p.equals(tempPath)){
						contained =true;
					}
				}
				if(!contained){
					String []tempPaths = new String[paths.length +1];
					for(int i=0;i<paths.length;i++){
						tempPaths[i] = paths[i];
					}
					if(paths.length >= PSatAPI.instance.max_no_analysis_paths){
						//do nothing
					}
					else{
						tempPaths[paths.length] = tempPath;
						paths = tempPaths;		
					}	
					
				}				
			}
			else{
				if(paths.length >= PSatAPI.instance.max_no_analysis_paths){
					//do nothing
				}
				else{
					paths = extractPaths(g,nextNode,paths, tempPath, tempk);
				}	
				
			}
		}
		Arrays.sort(paths);
		
		if(PSatAPI.instance.networkType == NetworkType.SEQUENTIAL){
			paths = additionalSequencePaths(paths);
		}
		
		return paths;
		
	}
	
	private static String [] additionalSequencePaths(String[] paths){

		if(paths.length ==0){
			return paths;
		}
		//add other possible sequence permutation	
		otherpossiblesequences = new String[0];
		if(paths[0].contains(":")){
			paths[0]= paths[0].split(": ")[1];
		}
		String [] agentn = paths[0].split(" ");			
		String [] agentNames = new String[0];
		for(String s:agentn){
			if(s != null && s.length()>0){
				agentNames = Helper.addUniqueStringToArray(agentNames, s);
			}
		}
        permute(java.util.Arrays.asList(agentNames), 0);
        for(String ss:otherpossiblesequences){
    		ss = ss.replace("[", "");
    		ss = ss.replace("]", "");
    		String ssa[] = ss.split(",");
    		
    		String seq = " ";
			for(String se:ssa){
				seq = seq+se;
			}
			
    		if(PSatAPI.instance.is_role_run){
    			if(ssa[0].equals(PSatAPI.instance.sourceAgentName)){       
					paths = Helper.addUniqueStringToArray(paths, seq);
				}
	        }
    		else{
    			if(ssa[0].trim().equals(PSatAPI.instance.sourceAgentName) && ssa[ssa.length-1].trim().equals(PSatAPI.instance.targetAgentName)){
					paths = Helper.addUniqueStringToArray(paths, seq);
				}
    		}        		
		}	
        
        return paths;
	}
	
	private static String[] otherpossiblesequences;	
	static void permute(java.util.List<String> arr, int k){
		if(otherpossiblesequences.length >= 100){ //maximum of 100 alternative possible sequences
			return;
		}
        for(int i = k; i < arr.size(); i++){
            java.util.Collections.swap(arr, i, k);
            permute(arr, k+1);
            java.util.Collections.swap(arr, k, i);
        }
        if (k == arr.size() -1){
        	String seq = java.util.Arrays.toString(arr.toArray());
        	seq = seq.replace("[", "");
        	seq = seq.replace("]", "");
        	String seqbreak[] = seq.split(",");
        	String seqstart = seqbreak[0].trim();
        	String seqend = seqbreak[seqbreak.length-1].trim();
        	if(PSatAPI.instance.is_role_run){
        		seqstart = "";
    			if(seqstart.equals(PSatAPI.instance.sourceAgentName)){       
    	        	otherpossiblesequences = Helper.addUniqueStringToArray(otherpossiblesequences, java.util.Arrays.toString(arr.toArray()));
				}
	        }
    		else{
    			if(seqstart.trim().equals(PSatAPI.instance.sourceAgentName) && seqend.equals(PSatAPI.instance.targetAgentName)){
    	        	otherpossiblesequences = Helper.addUniqueStringToArray(otherpossiblesequences, java.util.Arrays.toString(arr.toArray()));
				}
    		}
        }
    }
}

