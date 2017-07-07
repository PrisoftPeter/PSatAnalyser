package gla.prisoft.server.kernel.util;

import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
//import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
//import java.util.Map;
import java.util.Random;

import gla.prisoft.client.kernel.display.model.ClientKNetworkGraph;
import gla.prisoft.client.session.ClientServerBroker;
import gla.prisoft.server.PSatAPI;
import gla.prisoft.server.kernel.util.GMLGraphLoader;
import gla.prisoft.server.kernel.util.ResourceLoader;
import gla.prisoft.server.kernel.util.ServerAgentFactory;
import gla.prisoft.server.session.Config;
import gla.prisoft.server.session.ServerConfigInstance;
import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;
import gla.prisoft.shared.ConfigInstance;
import gla.prisoft.shared.KLink;
import gla.prisoft.shared.KNode;
import edu.uci.ics.jung.algorithms.generators.random.BarabasiAlbertGenerator;
import edu.uci.ics.jung.algorithms.generators.random.EppsteinPowerLawGenerator;
import edu.uci.ics.jung.algorithms.generators.random.KleinbergSmallWorldGenerator;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.io.GraphIOException;
import edu.uci.ics.jung.io.GraphMLWriter;
import edu.uci.ics.jung.io.graphml.EdgeMetadata;
import edu.uci.ics.jung.io.graphml.GraphMLReader2;
import edu.uci.ics.jung.io.graphml.GraphMetadata;
import edu.uci.ics.jung.io.graphml.GraphMetadata.EdgeDefault;
import edu.uci.ics.jung.io.graphml.HyperEdgeMetadata;
import edu.uci.ics.jung.io.graphml.NodeMetadata;
//import edu.uci.ics.jung.visualization.VisualizationViewer;
//import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
//import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
//import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
//import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
//import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
//import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedState;
//import edu.uci.ics.jung.algorithms.layout.FRLayout;
//import edu.uci.ics.jung.algorithms.layout.Layout;
//import edu.uci.ics.jung.algorithms.layout.StaticLayout;
//import edu.uci.ics.jung.visualization.control.ModalGraphMouse;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
//import org.apache.commons.collections15.TransformerUtils;

public class ServerKNetworkGraph implements Serializable{

	private static final long serialVersionUID = 187778308350391012L;

	public static int edgeCount = 0;
	
//	private DirectedGraph<KNode, KLink> g;
//	public UndirectedGraph<KNode, KLink> g;	
	public KNode [] networkNodes;
	
//	private static ArrayList<KLink> coloredLinks;
//	private static ArrayList<KLink> tickenedLinks;
//	private static ArrayList<String> coloredNodes;
	
	private static Object [][] attributeColorList;
				
	@SuppressWarnings("rawtypes")
	PickedState pickedState = null;
//	private static KNode mutant_source;
//	private static KNode mutant_target;
		
    public ServerKNetworkGraph() { 
		networkNodes = new KNode[0];  
		attributeColorList = new Object[0][2];
    }
    
//    public static void resetColoredLinks(){
//    	coloredLinks = new ArrayList<KLink>();
//    	resetTickenedLinks();
//    }
//    
//    public static void resetTickenedLinks(){
//    	tickenedLinks = new ArrayList<KLink>();
//    }
//    
//    public static void resetColoredNodes(){
//    	coloredNodes = new ArrayList<String>();
//    }
//    
//    public void addColoredNode(String agentName){
//    	coloredNodes.add(agentName);
//    }    
    
    private void addKNode(KNode node){
    	KNode temp[] = new KNode[networkNodes.length +1];
    	for(int i=0;i<networkNodes.length;i++){
    		temp[i] = networkNodes[i];
    	}
    	temp[networkNodes.length] = node;
    	networkNodes = temp;
    }
    
    private KNode getKNode(String nodeId){
    	KNode node = null;
    	for(KNode n: networkNodes){
    		if(n.toString().equals(nodeId)){
    			node = n;
    			break;
    		}
    	}
    	return node;
    }
    
	private Object [][] addToAttributeColorList(Object [][] list,Attribute att, Color color){
		Object [][] temp = new Object[list.length+1][2];		

		int row, column;
		for(row = 0;row < list.length; row++){
			for(column=0;column <list[0].length;column++){
				temp[row][column] = list[row][column];
			}
		}
		temp[list.length][0] = att;
		temp[list.length][1] = color;
				
		return temp;
	}
	
	@SuppressWarnings("unused")
	private Color getAttributeColor(Attribute att){
		Color c = null;
		for(int row = 0;row < attributeColorList.length; row++){
			Attribute at1 = (Attribute)attributeColorList[row][0];
			if(at1.sameAs(att)){
				c = (Color)attributeColorList[row][1];
				break;
			}
		}
		if(c == null){
			c = Color.BLUE;
			addToAttributeColorList(attributeColorList,att,c);
		}		
		return c;
	}
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public void calcUnweightedShortestPath(KNode source, KNode target, ServerConfigInstance instance) {
    	
        DijkstraShortestPath<KNode,KLink> alg = new DijkstraShortestPath(instance.g);
        List<KLink> l = alg.getPath(source, target);
        System.out.println("The shortest unweighted path from " + source + " to " + target + " is:");
        System.out.println(l.toString());
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void calcWeightedShortestPath(KNode source, KNode target, ServerConfigInstance instance) {
    	
        Transformer<KLink, Double> wtTransformer = new Transformer<KLink,Double>() {
            public Double transform(KLink link) {
                return link.weight;
            }
        };
        DijkstraShortestPath<KNode,KLink> alg = new DijkstraShortestPath(instance.g, wtTransformer);
        List<KLink> l = alg.getPath(source, target);
        Number dist = alg.getDistance(source, target);
        System.out.println("The shortest weighted path from " + source + " to " + target + " is:");
        System.out.println(l.toString());
        System.out.println("and the length of the path is: " + dist);
    }
    
        
//    @SuppressWarnings({ "rawtypes", "unchecked" })
//    public void calcMaxFlow(KNode source, KNode target) {
//        // For the Edmonds-Karp Max Flow algorithm we have the following
//        // parameters: the graph, source vertex, sink vertex, transformer of edge capacities,
//        // map of edge flow values.
//        
//        Transformer<KLink, Double> capTransformer = new Transformer<KLink, Double>(){
//          public Double transform(KLink link)  {
//              return link.capacity;
//          }
//        };
//        Map<KLink, Double> edgeFlowMap = new HashMap<KLink, Double>();
//        // This Factory produces new edges for use by the algorithm
//        Factory<KLink> edgeFactory = new Factory<KLink>() {
//            public KLink create() {
//                return new KLink(1.0, 1.0);
//            }
//        };
//        
//        EdmondsKarpMaxFlow<KNode, KLink> alg = new EdmondsKarpMaxFlow(g,source, target, capTransformer, edgeFlowMap,edgeFactory);
//        alg.evaluate(); // If you forget this you won't get an error but you will get a wrong answer!!!
//        System.out.println("The max flow is: " + alg.getMaxFlow());
//        System.out.println("The edge set is: " + alg.getMinCutEdges().toString());
//    }
    

    public void createNewSequence(ServerConfigInstance instance, String path){

//    	g = new DirectedSparseMultigraph<KNode, KLink>();
    	instance.g = new UndirectedSparseMultigraph<KNode, KLink>();
    	
    	if(path.contains(":")){
    		path = path.split(":")[1];
    	}
    	
    	ArrayList<Agent> pathAgents = new ArrayList<Agent>();
    	String agentNames [] = path.split(" ");
    	for(String agentName: agentNames){
    		if(agentName.trim().length()>0){
    			Agent agent = ServerAgentFactory.getAgent(agentName.trim(), instance);
    			if(agent != null){
    				pathAgents.add(agent);
            		KNode node = new KNode(agent.getAgentName());
                	addKNode(node);
    			}        			
    		}    		
    	}
    	
        // Add directed edges along with the vertices to the graph
    	for(int i=1; i<pathAgents.size();i++){
    		Agent agent_t = pathAgents.get(i);
    		Agent agent_s = pathAgents.get(i-1);
    		
    		KNode knode_t = getKNode(agent_t.getAgentName());
    		KNode knode_s = getKNode(agent_s.getAgentName());
    		if(knode_t!=null && knode_s !=null){
    			KLink link = new KLink(1.0, 10,edgeCount++);
    			instance.g.addEdge(link,knode_s, knode_t, EdgeType.UNDIRECTED);
    		}
    		
    	}
    	
        Config.serialiseServerConfigInstance(instance.sessionid, instance);
        ClientKNetworkGraph.g = instance.g;
        writeGraphGML(instance);
    }
   
    
	public void createGraph(ServerConfigInstance instance) {
		
//    	g = new DirectedSparseMultigraph<KNode, KLink>();
    	instance.g = new UndirectedSparseMultigraph<KNode, KLink>();
    	        
    	for(Agent agent:instance.agents){
        	KNode node = new KNode(agent.getAgentName());
        	addKNode(node);
        	
        }
        
        // Add directed edges along with the vertices to the graph
        for(Agent agent:instance.agents){
        	KNode eknode = getKNode(agent.getAgentName());
        	
        	if(eknode !=null){
        		if(agent.getOtherKnownAgentNames().length >0){
        			String [] connections = agent.getOtherKnownAgentNames();
                	for(String connection:connections){
                		KNode conknode = getKNode(connection);
                		
                		if(conknode != null){
                			KLink link = new KLink(1.0, 10,edgeCount++);
//                			g.addEdge(link,eknode, conknode, EdgeType.DIRECTED);
                			instance.g.addEdge(link,eknode, conknode, EdgeType.UNDIRECTED);
                		}
                	}
        		}
        		else{
        			instance.g.addVertex(eknode);
        		}
            	
        	}
        }         
        writeGraphGML(instance);
        
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void writeGraphGML(ServerConfigInstance instance) {

		GraphMLWriter wr = new GraphMLWriter();
		java.io.FileWriter fwr = null;
		try {
			fwr = new java.io.FileWriter(PSatAPI.datastore_file_path+"/"+instance.sessionid+"/temp.graphml");
			wr.save(instance.g,fwr);
			fwr.close();
		} catch (IOException e) {
			System.err.println("Can not save graph to simplegraph.gml!");
			e.printStackTrace();
			System.exit(1);
		}
    }
    
    class GraphFactory implements Factory<Graph<KNode,KLink>> {
        public Graph<KNode,KLink> create() {
            return new SparseMultigraph<KNode,KLink>();
        }
    }

    class NodeFactory implements Factory<KNode> {
    	private int n = 0;
    	
        public KNode create() {
        	KNode node = new KNode(""+n++);
        	return node;
        }
    }
    class EdgeFactory implements Factory<KLink> {
        public KLink create() {
        	KLink link = new KLink(1.0, 10, edgeCount++);
            return link;
        }
    }
        
 //   public Graph<KNode, KLink> createEppsteinPowerLawGraph(int noNodes, int noEdges, int degreeExponent){ // power law
    public void createEppsteinPowerLawGraph(int noNodes, int noEdges, int degreeExponent, ServerConfigInstance instance, ConfigInstance cinstance){ // power law
    	Graph<KNode, KLink>  eppstein = 
    			new EppsteinPowerLawGenerator<KNode,KLink>(new GraphFactory(), new NodeFactory(),
    										new EdgeFactory(), noNodes, noEdges, degreeExponent).create();
    	
    	eppstein = transformGraphNodes(eppstein, instance, cinstance);
        createGraph(instance);
    	//return eppstein;
    }
    
//    public Graph<KNode, KLink> createKleinbergSmallWorldGraph(int noNodes, int noEdges, int clusteringExponent){ //small worlds
    public void createKleinbergSmallWorldGraph(int noNodes, int noEdges, double clusteringExponent, ServerConfigInstance instance,ConfigInstance cinstance){ //small worlds
    	if(noEdges < 2){
    		noEdges = 2;
    	}
    	Graph<KNode, KLink>  smallworld = 
    			new KleinbergSmallWorldGenerator<KNode,KLink>(new GraphFactory(), new NodeFactory(),
    										new EdgeFactory(), noNodes, noEdges, clusteringExponent).create();
    	smallworld = transformGraphNodes(smallworld, instance, cinstance);
        createGraph(instance);

        //return smallworld;
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
//	public Graph<KNode, KLink> createBarabasiAlbertGraph(int init_no_seeds, int numEdgesToAttach, int noAgents){ //preferential attachment
	public void createBarabasiAlbertGraph(int init_no_seeds, int numEdgesToAttach, int noAgents, int no_iterations, ServerConfigInstance instance,ConfigInstance cinstance){ //preferential attachment
    	GraphFactory graphFactory = new GraphFactory();
    	NodeFactory nodeFactory = new NodeFactory();
    	EdgeFactory edgeFactory = new EdgeFactory();
    	
    	HashSet<KNode> seedVertices = new HashSet(); 
    	for(int i = 0; i < noAgents; i++) {
         	KNode node = new KNode(""+i);
    	    seedVertices.add(node);
    	}
    	
    	BarabasiAlbertGenerator<KNode, KLink> barabasiGen = new 
                BarabasiAlbertGenerator<KNode, KLink>(graphFactory,nodeFactory, edgeFactory,
                		init_no_seeds, numEdgesToAttach,1, seedVertices);
                
    	barabasiGen.evolveGraph(no_iterations); 
        Graph gx = barabasiGen.create();
        
        gx = transformGraphNodes(gx, instance, cinstance);
    	
        createGraph(instance);
    	//return gx;
    }
    
    private Graph<KNode, KLink> transformGraphNodes(Graph<KNode, KLink> graph, ServerConfigInstance instance,  ConfigInstance cinstance){
    	String[] names = ServerAgentFactory.getAllPossibleNames();
    	if(graph.getVertices().size() > names.length){
    		System.err.println("Only "+names.length+" available in repository "+graph.getVertices().size()+" required. Excess will remain unchanged..");//, true);
    	}
    	
    	int i= 0;
    	for(KNode knode: graph.getVertices()){
    		
    		String name = knode.id;
    		if(i < names.length){
    			name = names[i];
    			i = i +1;
    		}
    		Agent agent = ServerAgentFactory.autoGenAgentWoutConnections(name);
    		ServerAgentFactory.addAgent(agent, instance);
        	knode.id = name;
        	
        	cinstance.agentCollectionNames.add(agent.getAgentName());
    	}
    	
    	for(KLink link: graph.getEdges()){
    		Collection<KNode> in = graph.getIncidentVertices(link);

    		
    		KNode source = (KNode)in.toArray()[0];
    		KNode dest = (KNode)in.toArray()[1];
    		
    		Agent s_agent = ServerAgentFactory.getAgent(source.id, instance);
    		Agent d_agent = ServerAgentFactory.getAgent(dest.id, instance);
    		s_agent.addToOtherKnownAgentNames(d_agent.getAgentName());
    		ServerAgentFactory.addAgent(s_agent, instance);
    	}
    	
    	return graph;
    }
    
    public void createSequentialGraph(ConfigInstance instance,ServerConfigInstance serverInstance){

    	serverInstance.agents = new Agent[0];
		ServerAgentFactory.clearAgents(serverInstance);
		
		try {
			InputStream f = ResourceLoader.load("config_info/names");
			InputStreamReader reader = new InputStreamReader(f);
			BufferedReader buff = new BufferedReader (reader);
			String line;
			int i=0;
			
			while((line=buff.readLine())!=null && i<instance.no_agents){
				Agent e = new Agent(line);
				
//				Attribute h = new Attribute();
//				h.setSubjectName(e.getAgentName());
//				h.setKey("h");
//				int val1 = rand.nextInt(10) + 1;
//				h.setValue(""+val1);		
//				e.addToPersonalAttributes(h);
				
				ServerAgentFactory.addAgent(e, serverInstance);	
				instance.agentCollectionNames.add(e.getAgentName());
				
				i = i+1;
			}
			
			buff.close();
		} 
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		for(int i=0;i<serverInstance.agents.length;i++){
			Agent e = serverInstance.agents[i];
			if(i+1 < serverInstance.agents.length){
				e.addToOtherKnownAgentNames(serverInstance.agents[i+1].getAgentName());
			}
		}
		createGraph(serverInstance);
    }
    
    public void createNetworkFromGmlOrGraphML(ServerConfigInstance instance, ConfigInstance cinstance, File gfile){
    	GMLGraphLoader gloader = new GMLGraphLoader();
    	gloader.loadGraph(instance,cinstance, gfile);
    	gloader.extractNodes(instance);
		createGraph(instance);
    }
    	
    public void createNetworkFromGraphML(String fileName, ServerConfigInstance instance, ConfigInstance cinstance){
    	
    	GraphMLReader2<Graph<KNode, KLink>, KNode, KLink> graphReader = null;
		
    	try {
			BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
			
			/* Create the Graph Transformer */
			Transformer<GraphMetadata, Graph<KNode, KLink>>
			graphTransformer = new Transformer<GraphMetadata,Graph<KNode, KLink>>() {
			  public Graph<KNode, KLink>
			      transform(GraphMetadata metadata) {
			        metadata.getEdgeDefault();
					if (metadata.getEdgeDefault().equals(
			        EdgeDefault.DIRECTED)) {
			            return new DirectedSparseGraph<KNode, KLink>();
			        } else {
			            return new UndirectedSparseGraph<KNode, KLink>();
			        }
			      }
			};
			
			/* Create the Vertex Transformer */
			Transformer<NodeMetadata, KNode> vertexTransformer
			= new Transformer<NodeMetadata, KNode>() {
			    public KNode transform(NodeMetadata metadata) {
			        KNode v = new NodeFactory().create();
			        v.id = metadata.getId();
			        return v;
			    }
			};
			
			/* Create the Edge Transformer */
			 Transformer<EdgeMetadata, KLink> edgeTransformer =
			 new Transformer<EdgeMetadata, KLink>() {
			     public KLink transform(EdgeMetadata metadata) {
			         KLink e = new EdgeFactory().create();
			         return e;
			     }
			 };
			 
			 /* Create the Hyperedge Transformer */
			 Transformer<HyperEdgeMetadata, KLink> hyperEdgeTransformer
			 = new Transformer<HyperEdgeMetadata, KLink>() {
			      public KLink transform(HyperEdgeMetadata metadata) {
			          KLink e = new EdgeFactory().create();
			          return e;
			      }
			 };

			 /* Create the graphMLReader2 */
			 graphReader = new GraphMLReader2<Graph<KNode, KLink>, KNode, KLink>(
				fileReader, graphTransformer, vertexTransformer,
				edgeTransformer, hyperEdgeTransformer);
					
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
    	 //extract graphml nodes
		 if(graphReader!=null){
			 try {
				 Graph<KNode, KLink> graph = graphReader.readGraph();
				 Collection<KNode> vertices = graph.getVertices();
				for(KNode knode:vertices){
					String id = knode.id;
					Agent agent = new Agent(id);
					ServerAgentFactory.addAgent(agent, instance);
					
					cinstance.agentCollectionNames.add(agent.getAgentName());
				}
				
				Collection< KLink> edges = graph.getEdges();
				for(KLink klink:edges){
					Pair<KNode> pair =graph.getEndpoints(klink);
					
					KNode n1 = pair.getFirst();
					KNode n2 = pair.getSecond();
					Agent agent1 = ServerAgentFactory.getAgent(n1.id, instance);
					Agent agent2 = ServerAgentFactory.getAgent(n2.id, instance);
					
					if(agent1 != null && agent2 !=null){
						agent1.addToOtherKnownAgentNames(agent2.getAgentName());
					}
				}				
				
			} catch (GraphIOException e) {
				e.printStackTrace();
			}
				
		}
		 
		 createGraph(instance);
		    	
    }
    
	public void createRandomGraph(ServerConfigInstance instance, ConfigInstance ginstance){

		instance.agents = new Agent[0];
		ServerAgentFactory.clearAgents(instance);
		
		try {
			InputStream f = ResourceLoader.load("config_info/names");
			InputStreamReader reader = new InputStreamReader(f);
			BufferedReader buff = new BufferedReader (reader);
			String line;
			int i=0;
			
			while((line=buff.readLine())!=null && i<ginstance.no_agents){
				Agent e = new Agent(line);
				boolean added = ServerAgentFactory.addAgent(e, instance);
				
//				Attribute h = new Attribute();
//				h.setSubjectName(e.getAgentName());
//				h.setKey("h");
//				int val1 = rand.nextInt(10) + 1;
//				h.setValue(""+val1);		
//				e.addToPersonalAttributes(h);
				if(added){
					i = i+1;	
				}	
				
				ginstance.agentCollectionNames.add(e.getAgentName());
			}
						
			buff.close();
		} 
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		Random rn = new Random();	
		for(int i=0;i<instance.agents.length;i++){
			int bond = 8;
			if(bond >instance.agents.length){
				bond = instance.agents.length;
			}
			int limit = rn.nextInt(instance.agents.length/bond)+1;
			if(limit == 0){
				limit = 1;
			}
			Agent e = instance.agents[i];
			for(int j=0;j<limit;j++){
				if(j!=i){
					int dice = rn.nextInt(instance.agents.length -1);
					while(dice == i){
						dice = rn.nextInt(instance.agents.length -1);
					}
					e.addToOtherKnownAgentNames(instance.agents[dice].getAgentName());
				}
			}
		}
		createGraph(instance);
	}
    
	public void createNodesOnlyGraph(ServerConfigInstance instance, ConfigInstance ginstance){

		instance.agents = new Agent[0];
		ServerAgentFactory.clearAgents(instance);
		
		try {
			InputStream f = ResourceLoader.load("config_info/names");
			InputStreamReader reader = new InputStreamReader(f);
			BufferedReader buff = new BufferedReader (reader);
			String line;
			int i=0;
			
			while((line=buff.readLine())!=null && i<ginstance.no_agents){
				Agent e = new Agent(line);
				boolean added = ServerAgentFactory.addAgent(e, instance);
				
//				Attribute h = new Attribute();
//				h.setSubjectName(e.getAgentName());
//				h.setKey("h");
//				int val1 = rand.nextInt(10) + 1;
//				h.setValue(""+val1);		
//				e.addToPersonalAttributes(h);
				if(added){
					i = i+1;	
				}	
				
				ginstance.agentCollectionNames.add(e.getAgentName());
			}
						
			buff.close();
		} 
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		createGraph(instance);
	}
	
	
	public void mutateEdges(KNode source, KNode target, ServerConfigInstance instance, String mutationType){
		
		if(source.id.equals(target.id)){
			return;
		}
		synchronized(instance.g){
			Collection< KLink> edges = instance.g.getEdges();
			
			if(mutationType.equals("removeedge")){
				for(KLink klink:edges){
					Pair<KNode> pair =instance.g.getEndpoints(klink);
					
					KNode n1 = pair.getFirst();
					KNode n2 = pair.getSecond();
					
					if((n1.id.equals(source.id) && n2.id.equals(target.id)) 
							|| (n1.id.equals(target.id) && n2.id.equals(source.id))){
						//link exist - remove link

						Agent agent1 = ServerAgentFactory.getAgent(n1.id, instance);
						Agent agent2 = ServerAgentFactory.getAgent(n2.id, instance);
						
						if(agent1 != null && agent2 !=null){
							agent1.removeFromOtherKnownAgentNames(agent2.getAgentName());
							agent2.removeFromOtherKnownAgentNames(agent1.getAgentName());
							
							ServerAgentFactory.writeAgent(agent1, instance);
							ServerAgentFactory.writeAgent(agent2, instance);
						}
						
						instance.g.removeEdge(klink);
						Config.serialiseServerConfigInstance(instance.sessionid, instance);
						
						
						HashMap<String, Object> hm = new HashMap<String, Object>();
						hm.put("source", source);
						hm.put("target", target);
						hm.put("klink", klink);
						
						Properties ppties1 = new Properties();
						ppties1.setProperty("instanceproperty", "removeEdge");
						ClientServerBroker.messageEvent("PSatClient.ConfigInstanceUpdateRequest()", null, ppties1, hm);

						Properties ppties2 = new Properties();
						ppties2.setProperty("instanceproperty", "repaintListbox");
						ClientServerBroker.messageEvent("PSatClient.ConfigInstanceUpdateRequest()", null, ppties2, new Object());

						return;
					}						
					
				}
			}
			else if(mutationType.equals("addedge")){
				boolean linkexist = false;
				for(KLink klink:edges){
					Pair<KNode> pair =instance.g.getEndpoints(klink);
					
					KNode n1 = pair.getFirst();
					KNode n2 = pair.getSecond();
										
					if((n1.id.equals(source.id) && n2.id.equals(target.id)) 
							|| (n1.id.equals(target.id) && n2.id.equals(source.id))){
						linkexist = true;
						break;
					}
				}
				if(!linkexist){//if link already exist - then don't add another link
					Agent agent1 = ServerAgentFactory.getAgent(source.id,instance);
					Agent agent2 = ServerAgentFactory.getAgent(target.id,instance);
					
					if(agent1 != null && agent2 !=null){
						agent1.addToOtherKnownAgentNames(agent2.getAgentName());
						ServerAgentFactory.writeAgent(agent1,instance);
						ServerAgentFactory.writeAgent(agent2, instance);
					}
					
					instance.g.addEdge(new EdgeFactory().create(), source, target);
//					boolean successful = instance.g.addEdge(new EdgeFactory().create(), source, target);
					Config.serialiseServerConfigInstance(instance.sessionid, instance);
					
					HashMap<String, Object> hm = new HashMap<String, Object>();
					hm.put("source", source);
					hm.put("target", target);
					hm.put("edgeCount", edgeCount+1);
					
					Properties ppties = new Properties();
					ppties.setProperty("instanceproperty", "addEdge");
					ClientServerBroker.messageEvent("PSatClient.ConfigInstanceUpdateRequest()", null, ppties, hm);

				}
			}

		}

	}
		
	
}

