package psat.display.model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

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
import edu.uci.ics.jung.io.graphml.HyperEdgeMetadata;
import edu.uci.ics.jung.io.graphml.NodeMetadata;
import edu.uci.ics.jung.io.graphml.GraphMetadata.EdgeDefault;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.PickingGraphMousePlugin;
import edu.uci.ics.jung.visualization.control.ScalingGraphMousePlugin;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedState;
import psat.Display;
import psat.PSatAPI;
import psat.knowledge.MemoryFactory;
import psat.util.Agent;
import psat.util.AgentFactory;
import psat.util.Attribute;
import psat.util.Config;
import psat.util.ConfigInstance;
import psat.util.GMLGraphLoader;
import psat.util.KLink;
import psat.util.KNode;
import psat.util.NetworkType;
import psat.util.ResourceLoader;
import edu.uci.ics.jung.algorithms.generators.random.BarabasiAlbertGenerator;
import edu.uci.ics.jung.algorithms.generators.random.EppsteinPowerLawGenerator;
import edu.uci.ics.jung.algorithms.generators.random.KleinbergSmallWorldGenerator;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.TransformerUtils;
import org.apache.commons.io.FilenameUtils;

public class KNetworkGraph implements Serializable{
	private static final long serialVersionUID = 187778308350391012L;

	public static int edgeCount = 0;

	//	private DirectedGraph<KNode, KLink> g;
//	public static UndirectedGraph<KNode, KLink> g;

	public static KNode [] networkNodes;

	private static ArrayList<KLink> coloredLinks;
	private static ArrayList<KLink> tickenedLinks;
	private static ArrayList<String> coloredNodes;

	private static Object [][] attributeColorList;

	@SuppressWarnings("rawtypes")
	PickedState pickedState = null;
	private static KNode mutant_source;
	private static KNode mutant_target;

	@SuppressWarnings({ "rawtypes", "unused" })
	private Layout gridLayout;

	public KNetworkGraph() { 
		networkNodes = new KNode[0];  
		attributeColorList = new Object[0][2];
	}

	public static void resetColoredLinks(){
		coloredLinks = new ArrayList<KLink>();
		resetTickenedLinks();
	}

	public static void resetTickenedLinks(){
		tickenedLinks = new ArrayList<KLink>();
	}

	public static void resetColoredNodes(){
		coloredNodes = new ArrayList<String>();
	}

	public void addColoredNode(String agentName){
		coloredNodes.add(agentName);
	}    

	public static HashMap<String, Double> maxPathSats;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void show(JPanel panel){
		if(PSatAPI.instance == null || PSatAPI.instance.sessionid == null ||PSatAPI.instance.g == null){
			return;
		}
		panel.setBackground(Color.WHITE);
		VisualizationViewer vs = null;
		//		if(g !=null){
		//			int cv =g.getVertexCount();
		//			System.out.println(cv);
		//		}
		//		
		//if(g==null|| g.getVertexCount()==0){
		if(!Display.graphloaded){
//			ConfigInstance instance = Config.deserialiseServerConfigInstance(Display.hostname);
//			ClientKNetworkGraph.g = sinstance.g;
			if(PSatAPI.instance.g!=null && PSatAPI.instance.g.getVertexCount()>0){
				Display.graphloaded = true;
			}
		}		

		//		if(Display.networkType == NetworkType.CUSTOM){
		//			vs =new VisualizationViewer(gridLayout, new Dimension(800, 400));
		//		}
		//		else{			
		//			vs =new VisualizationViewer(new CircleLayout(g), new Dimension(800, 400));

		//			SpringLayout sl =  new SpringLayout(g);
		//			sl.setRepulsionRange(30);
		//			sl.setStretch(0.1);
		//			vs = new VisualizationViewer(sl, new Dimension(800, 400));    	

		//			vs = new VisualizationViewer(new KKLayout(g), new Dimension(800, 400));

		Layout layout1 = null;
		if(PSatAPI.instance.networkType == NetworkType.SEQUENTIAL){
			layout1 = new CircleLayout(PSatAPI.instance.g);
		}
		else{
			layout1 = new FRLayout(PSatAPI.instance.g);
		}

		layout1.setSize(new Dimension(800,600));

		vs = new VisualizationViewer(layout1); 
		vs.setPreferredSize(new Dimension(1000,700));

		//			vs = new VisualizationViewer(new SpringLayout2(g), new Dimension(800, 400));    	
		//			vs = new VisualizationViewer(new ISOMLayout(g), new Dimension(800, 400)); 
		//		}

		vs.setBackground(Color.WHITE);
		Transformer vertexPaint = new Transformer() {
			public Paint transform(Object i) {
				//            	PSatClient.netDeseraliseConfigInstance();
				KNode node = (KNode)i;

				if(node.id.equals(PSatAPI.instance.sourceAgentName)){
					PSatAPI.instance.subjectName = PSatAPI.instance.sourceAgentName;
					return Color.green;
				}
				else if(node.id.equals(PSatAPI.instance.targetAgentName) && !PSatAPI.instance.is_role_run){
					//            		return Color.ORANGE;            		
					return Color.GRAY;
				}
				else if(Display.isTresholdSlider){
					//            		maxPathSats = null;
					//            		ClientServerBroker.triggerPSatHostEvent("InformationFlows.maxPathSats","");
					//            		int waittime= 0;
					//            		while(maxPathSats == null){
					//            			try {
					//							Thread.sleep(1000);
					//							if(waittime >ClientServerBroker.MAXWAITTIME){
					//	            				Display.updateLogPage("Wait Time: Message Server not Responding", true);
					//	            			}
					//						} catch (InterruptedException e) {
					//							e.printStackTrace();
					//						}
					//            			
					//            		}

					for (Map.Entry<String, Double> entry : maxPathSats.entrySet()) {
						String path = entry.getKey();
						double value = entry.getValue();
						if(value >= Display.sliderValue){

							String [] selectedAgentPath =null;
							if(path.contains(":")){
								String pathAgents1[] =path.split(": ");
								String pathAgents2 = pathAgents1[1];
								selectedAgentPath =pathAgents2.split(" ");
							}
							else{
								selectedAgentPath =path.split(" ");
							}

							for(Object ename:selectedAgentPath){
								String name = ename.toString();
								if(node.toString().equals(name)){
									return new Color(111,191,240);                    		
								}
							}	
						}
					}            		
				}
				else if(PSatAPI.instance.selectedPath !=null){

					String spath = PSatAPI.instance.selectedPath;
					String [] selectedAgentPath =null;
					if(spath.contains(":")){
						String pathAgents1[] =spath.split(": ");
						String pathAgents2 = pathAgents1[1];
						selectedAgentPath =pathAgents2.split(" ");
					}
					else{
						selectedAgentPath =spath.split(" ");
					}


					for(Object ename:selectedAgentPath){
						String name = ename.toString();
						if(node.toString().equals(name)){
							//                    		return new Color(30, 77, 43);
							if(PSatAPI.instance.is_role_run){
								return Color.GRAY; 
							}

						}
					}          		
				}             		
				//        		return Color.RED;
				return Color.WHITE; 
			}

		};
		vs.getRenderContext().setVertexFillPaintTransformer(vertexPaint);


		Transformer<KLink, Stroke> edgeStroke = new Transformer<KLink, Stroke>() {
			//          float dash[] = { 10.0f };
			public Stroke transform(KLink link) {
				boolean linkexist = false;
				Collection<KLink> klinks = PSatAPI.instance.g.getEdges();
				for(KLink l: klinks){
					if(l.toString().equals(link.toString())){
						linkexist = true;
					}
				}

				if(!linkexist){
					return null;
				}

				Collection<KNode> in = PSatAPI.instance.g.getIncidentVertices(link);
				KNode source = (KNode)in.toArray()[0];
				KNode dest = (KNode)in.toArray()[1];

				List<String> tempPaths = new ArrayList<String>();

				if(Display.isTresholdSlider){
					//  				maxPathSats = null;
					//        		ClientServerBroker.triggerPSatHostEvent("InformationFlows.maxPathSats","");
					//        		int waittime= 0;
					//        		while(maxPathSats == null){
					//        			try {
					//						Thread.sleep(1000);
					//	        			if(waittime >ClientServerBroker.MAXWAITTIME){
					//	        				Display.updateLogPage("Wait Time: Message Server not Responding", true);
					//	        			}
					//					} catch (InterruptedException e) {
					//						e.printStackTrace();
					//					}
					//        		}
					for (Map.Entry<String, Double> entry : maxPathSats.entrySet()) {
						String path = entry.getKey();
						double value = entry.getValue();
						if(value >= Display.sliderValue){
							tempPaths.add(path);
						}
					} 

				}
				else if(PSatAPI.instance.selectedPath == null){
					return null;          		
				}
				else{
					tempPaths.add(PSatAPI.instance.selectedPath);
				}


				for(String spath: tempPaths){
					String [] selectedAgentPath = null;
					if(spath.contains(":")){
						String pathAgents1[] =spath.split(": ");
						String pathAgents2 = pathAgents1[1];
						selectedAgentPath =pathAgents2.split(" ");
					}
					else{
						selectedAgentPath =spath.split(" ");
					}

					if(selectedAgentPath != null && tickenedLinks !=null){        			
						for(KLink t_link:tickenedLinks){
							if(t_link.id == link.id){
								//                              return new BasicStroke(4.5f);
								//                            return new BasicStroke(4.0f, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
							}
						}

						for(int j=0;j<selectedAgentPath.length-1;j++){
							if(selectedAgentPath[j].equals(source.id) 
									&& selectedAgentPath[j+1].equals(dest.id) ||
									selectedAgentPath[j].equals(dest.id) 
									&& selectedAgentPath[j+1].equals(source.id)){
								tickenedLinks.add(link);
								//                              return new BasicStroke(4.5f);
								//                            return new BasicStroke(4.0f, BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
							}            		
						}
					}             			
				}


				return null;
			}
		};
		vs.getRenderContext().setEdgeStrokeTransformer(edgeStroke);


		Transformer edgePaint = new Transformer() {
			public Paint transform(Object i) {
				//            	PSatClient.netDeseraliseConfigInstance();

				KLink link = (KLink)i;

				boolean linkexist = false;
				Collection<KLink> klinks = PSatAPI.instance.g.getEdges();
				for(KLink l: klinks){
					if(l.toString().equals(link.toString())){
						linkexist = true;
					}
				}

				if(!linkexist){
					return null;
				}           	 

				Collection<KNode> in = PSatAPI.instance.g.getIncidentVertices(link);
				KNode source = (KNode)in.toArray()[0];
				KNode dest = (KNode)in.toArray()[1];

				List<String> tempPaths = new ArrayList<String>();

				if(Display.isTresholdSlider){
					//      				maxPathSats = null;
					//            		ClientServerBroker.triggerPSatHostEvent("InformationFlows.maxPathSats","");
					//            		int waittime= 0;
					//            		while(maxPathSats == null){
					//            			try {
					//							Thread.sleep(1000);
					//
					//	            			if(waittime >ClientServerBroker.MAXWAITTIME){
					//	            				Display.updateLogPage("Wait Time: Message Server not Responding", true);
					//	            			}
					//						} catch (InterruptedException e) {
					//							e.printStackTrace();
					//						}
					//            		}
					for (Map.Entry<String, Double> entry : maxPathSats.entrySet()) {
						String path = entry.getKey();
						double value = entry.getValue();
						if(value >= Display.sliderValue){
							tempPaths.add(path);
						}
					} 

				}
				else if(PSatAPI.instance.selectedPath != null){
					tempPaths.add(PSatAPI.instance.selectedPath);          		
				}

				for(String spath: tempPaths){
					String [] selectedAgentPath = null;
					if(spath.contains(":")){
						String pathAgents1[] =spath.split(": ");
						String pathAgents2 = pathAgents1[1];
						selectedAgentPath =pathAgents2.split(" ");
					}
					else{
						selectedAgentPath =spath.split(" ");
					}

					if(selectedAgentPath != null && coloredLinks!=null){        			
						for(KLink t_link:coloredLinks){
							if(t_link.id == link.id){
								if(Display.isTresholdSlider){
									return new Color(4,65,110);	
								}
								return new Color(30,150,44);	
							}
						}

						for(int j=0;j<selectedAgentPath.length-1;j++){
							if(selectedAgentPath[j].equals(source.id) 
									&& selectedAgentPath[j+1].equals(dest.id) ||
									selectedAgentPath[j].equals(dest.id) 
									&& selectedAgentPath[j+1].equals(source.id)){
								coloredLinks.add(link);
								if(Display.isTresholdSlider){
									return new Color(4,65,110);	
								}
								return new Color(30,150,44);
							}

						}	
					}  
				}

				return new Color(80,80,80);    	
				//            	return Color.BLACK;
			}

		};

		vs.getRenderContext().setEdgeDrawPaintTransformer(edgePaint);
		vs.getRenderContext().setArrowFillPaintTransformer(edgePaint);

		Transformer<Object,Shape> vertexSize = new Transformer<Object,Shape>(){
			public Shape transform(Object i){
				//            	PSatClient.netDeseraliseConfigInstance();

				Ellipse2D circle = new Ellipse2D.Double(-15, -15, 20, 20);
				KNode node = (KNode)i;

				boolean selected = false;

				List<String> tempPaths = new ArrayList<String>();

				if(Display.isTresholdSlider){
					//      				maxPathSats = null;
					//            		ClientServerBroker.triggerPSatHostEvent("InformationFlows.maxPathSats","");
					//            		int waittime= 0;
					//            		while(maxPathSats == null){
					//            			try {
					//							Thread.sleep(1000);
					//
					//	            			if(waittime >ClientServerBroker.MAXWAITTIME){
					//	            				Display.updateLogPage("Wait Time: Message Server not Responding", true);
					//	            			}
					//						} catch (InterruptedException e) {
					//							e.printStackTrace();
					//						}
					//            		}
					for (Map.Entry<String, Double> entry : maxPathSats.entrySet()) {
						String path = entry.getKey();
						double value = entry.getValue();
						if(value >= Display.sliderValue){
							tempPaths.add(path);
						}
					} 

				}
				else if(PSatAPI.instance.selectedPath != null){
					tempPaths.add(PSatAPI.instance.selectedPath);          		
				}

				for(String spath: tempPaths){
					String [] selectedAgentPath = null;
					if(spath.contains(":")){
						String pathAgents1[] =spath.split(": ");
						String pathAgents2 = pathAgents1[1];
						selectedAgentPath =pathAgents2.split(" ");
					}
					else{
						selectedAgentPath =spath.split(" ");
					}

					if(selectedAgentPath !=null){
						for(Object ename:selectedAgentPath){
							String name = ename.toString();
							if(node.toString().equals(name)){
								selected = true;
								break;
							}
						}	
					}
				}


				if(selected){
					return AffineTransform.getScaleInstance(1.5, 1.5).createTransformedShape(circle);           		
				}
				else{
					return circle;
				}
			}
		};

		vs.getRenderContext().setVertexShapeTransformer(vertexSize);
		vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());

		final PickedState<KNode> pickedState = vs.getPickedVertexState();
		pickedState.addItemListener(new ItemListener() {

			public void itemStateChanged(ItemEvent e) {            	
				Object object = e.getItem();

				//            	PSatClient.netDeseraliseConfigInstance();

				if (object instanceof KNode) {
					KNode node = (KNode)object; 
					if (pickedState.isPicked(node)) {
						if(PSatAPI.instance.networkMutationMode){
							if(mutant_source != null){
								mutateEdges(mutant_source, node);
								mutant_target = node;
							}
							if(mutant_target == null){
								mutant_source = node;	
							}
							else{
								mutant_source = null;
								mutant_target = null;
							}

						}
						else{
							mutant_source = null;
						}
					}
				}
			}
		});


		AbstractModalGraphMouse gm = new DefaultModalGraphMouse<Object, Object>();
		vs.setGraphMouse(gm); //Add the mouse to our Visualization-Viewer.
		gm.add(new PickingGraphMousePlugin<Object, Object>());
		gm.add(new PopupGraphMousePlugin());
		gm.add(new ScalingGraphMousePlugin(new CrossoverScalingControl(), 0, 1 / 1.1f, 1.1f));

		gm.setMode(ModalGraphMouse.Mode.PICKING);
		vs.addKeyListener(gm.getModeKeyListener());
		vs.setGraphMouse(gm); 

		panel.add(vs);	

		//
		if(PSatAPI.instance.sourceAgentName !=null){
			if(PSatAPI.instance.targetAgentName !=null){
				Display.activateRun(true);
			}
		}
	}       

	//    private void addKNode(KNode node){
	//    	KNode temp[] = new KNode[networkNodes.length +1];
	//    	for(int i=0;i<networkNodes.length;i++){
	//    		temp[i] = networkNodes[i];
	//    	}
	//    	temp[networkNodes.length] = node;
	//    	networkNodes = temp;
	//    }
	//    
	//    private KNode getKNode(String nodeId){
	//    	KNode node = null;
	//    	for(KNode n: networkNodes){
	//    		if(n.toString().equals(nodeId)){
	//    			node = n;
	//    			break;
	//    		}
	//    	}
	//    	return node;
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
	public void calcUnweightedShortestPath(KNode source, KNode target) {
		DijkstraShortestPath<KNode,KLink> alg = new DijkstraShortestPath(PSatAPI.instance.g);
		List<KLink> l = alg.getPath(source, target);
		System.out.println("The shortest unweighted path from " + source + " to " + target + " is:");
		System.out.println(l.toString());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void calcWeightedShortestPath(KNode source, KNode target) {
		Transformer<KLink, Double> wtTransformer = new Transformer<KLink,Double>() {
			public Double transform(KLink link) {
				return link.weight;
			}
		};
		DijkstraShortestPath<KNode,KLink> alg = new DijkstraShortestPath(PSatAPI.instance.g, wtTransformer);
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

	public void createNewSequence(String path){

		//	    	g = new DirectedSparseMultigraph<KNode, KLink>();
		PSatAPI.instance.g = new UndirectedSparseMultigraph<KNode, KLink>();

		if(path.contains(":")){
			path = path.split(":")[1];
		}

		ArrayList<Agent> pathAgents = new ArrayList<Agent>();
		String agentNames [] = path.split(" ");
		for(String agentName: agentNames){
			if(agentName.trim().length()>0){
				Agent agent = AgentFactory.getAgent(agentName.trim());
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
				PSatAPI.instance.g.addEdge(link,knode_s, knode_t, EdgeType.UNDIRECTED);
			}

		}

		Config.serialiseServerConfigInstance();
		writeGraphGML();
	}

	public void createGraph2() {

		//	    	g = new DirectedSparseMultigraph<KNode, KLink>();
		PSatAPI.instance.g = new UndirectedSparseMultigraph<KNode, KLink>();

		for(Agent agent:PSatAPI.instance.agents){
			KNode node = new KNode(agent.getAgentName());
			addKNode(node);

		}

		// Add directed edges along with the vertices to the graph
		String seq = "";
		for(Agent agent:PSatAPI.instance.agents){
			KNode eknode = getKNode(agent.getAgentName());

			boolean seqcontained = false;
			if(seq.contains(agent.getAgentName())){
				seqcontained = true;
			}
			if(!seqcontained){
				seq = seq+agent.getAgentName()+" ";	
			}

			if(eknode !=null){
				if(agent.getOtherKnownAgentNames().length >0){
					String [] connections = agent.getOtherKnownAgentNames();
					for(String connection:connections){
						KNode conknode = getKNode(connection);

						if(conknode != null){
							KLink link = new KLink(1.0, 10,edgeCount++);
							//	                			g.addEdge(link,eknode, conknode, EdgeType.DIRECTED);
							PSatAPI.instance.g.addEdge(link,eknode, conknode, EdgeType.UNDIRECTED);

							seq = seq+connection+" ";
						}
					}
				}
				else{
					PSatAPI.instance.g.addVertex(eknode);
				}

			}
		}         

		if(PSatAPI.instance.networkType == NetworkType.SEQUENTIAL){
			if(seq.length()>0){
				PSatAPI.instance.selectedPath = seq;
				MemoryFactory.seq=seq;
				Config.serialiseConfigInstance(PSatAPI.instance.sessionid);
			}
		}
		writeGraphGML();

	}
	
	public static String getSeq(){
		String seq = "";
		
		Collection<KNode> knodes = PSatAPI.instance.g.getVertices();
		KNode source = null;
		for(KNode knode:knodes){
			if(knode.toString().equals(PSatAPI.instance.sourceAgentName)){
				source = knode;
				seq = seq+knode.toString()+" ";
				break;
			}
		}
		
		KNode nextnode = source;
		boolean end = false;
		while(!end){
			Collection<KNode> neighburs = PSatAPI.instance.g.getNeighbors(nextnode);
			if(neighburs.size()>0){
				boolean allcontained = true;
				for(KNode knode:neighburs){
					if(!seq.contains(knode.toString())){
						seq = seq+knode.toString()+" ";
						nextnode = knode;
						allcontained = false;
					}
				}
				if(allcontained){
					end = true;
				}
			}
		}
		return seq;
	}
	

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void writeGraphGML() {

		GraphMLWriter wr = new GraphMLWriter();
		java.io.FileWriter fwr = null;
		try {
			fwr = new java.io.FileWriter(PSatAPI.datastore_file_path+"/"+PSatAPI.instance.sessionid+"/temp.graphml");
			wr.save(PSatAPI.instance.g,fwr);
			fwr.close();
		} catch (IOException e) {
			System.err.println("Can not save graph to simplegraph.gml!");
			e.printStackTrace();
			System.exit(1);
		}
    }
    
    
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void createGraph() {

		//
		Map<KNode,Point2D> map = new HashMap<KNode,Point2D>();
		Dimension preferredSize = new Dimension(800,400);
		int interval = 100;
		int count = 0;

		PSatAPI.instance.kgraph.createGraph();

		Config.serialiseServerConfigInstance();			


		for(KNode node:networkNodes){
			int x = interval*count;
			int y = x / preferredSize.width * interval;
			x %= preferredSize.width;
			Point2D location = new Point2D.Float(x, y);
			map.put(node, location);

			count = count +1;
			//
		}

		//        // Add directed edges along with the vertices to the graph
		//    	for(String agentName: PSatClient.netGetAgentNames()){
		//			Agent agent = PSatClient.netGetAgent(agentName);
		//			KNode eknode = getKNode(agent.getAgentName());
		//        	
		//        	if(eknode !=null){
		//        		if(agent.getOtherKnownAgentNames().length >0){
		//        			String [] connections = agent.getOtherKnownAgentNames();
		//                	for(String connection:connections){
		//                		KNode conknode = getKNode(connection);
		//                		
		//                		if(conknode != null){
		//                			KLink link = new KLink(1.0, 10);
		////                			g.addEdge(link,eknode, conknode, EdgeType.DIRECTED);
		//                			g.addEdge(link,eknode, conknode, EdgeType.UNDIRECTED);
		//                		}
		//                	}
		//        		}
		//        		else{
		//        			g.addVertex(eknode);
		//        		}
		//            	
		//        	}
		//		}


		Transformer<KNode,Point2D> vlf = TransformerUtils.mapTransformer(map);
		gridLayout = new StaticLayout(PSatAPI.instance.g, vlf, preferredSize);

	}
	//    @SuppressWarnings({ "unchecked", "rawtypes" })
	//	private void writeGraphGML() {
	//
	//		GraphMLWriter wr = new GraphMLWriter();
	//		java.io.FileWriter fwr = null;
	//		try {
	//			fwr = new java.io.FileWriter("temp.graphml");
	//			wr.save(g,fwr);
	//			fwr.close();
	//		} catch (IOException e) {
	//			System.err.println("Can not save graph to simplegraph.gml!");
	//			e.printStackTrace();
	//			System.exit(1);
	//		}
	//    }


	// //   public Graph<KNode, KLink> createEppsteinPowerLawGraph(int noNodes, int noEdges, int degreeExponent){ // power law
	//    public void createEppsteinPowerLawGraph(int noNodes, int noEdges, int degreeExponent){ // power law
	//    	ClientServerBroker.triggerPSatHostEvent("ServerKNetworkGraph.createEppsteinPowerLawGraph()","£"+noNodes+"£"+noEdges+"£"+degreeExponent);
	// 		int waittime= 0;
	// 		while(!ClientServerBroker.createEppsteinPowerLawGraphDone){
	// 			try {
	// 				Thread.sleep(1000);
	// 				if(waittime >MAXWAITTIME){
	//     				Display.updateLogPage("Wait Time: Message Server not Responding", true);
	//     			}
	// 			} catch (InterruptedException e) {
	// 				e.printStackTrace();
	// 			}			
	// 		}
	// 		ClientServerBroker.createEppsteinPowerLawGraphDone = false; 		    	
	//    }

	////    public Graph<KNode, KLink> createKleinbergSmallWorldGraph(int noNodes, int noEdges, int clusteringExponent){ //small worlds
	//    public void createKleinbergSmallWorldGraph(int noNodes, int noEdges, double clusteringExponent){ //small worlds
	//    	ClientServerBroker.triggerPSatHostEvent("ServerKNetworkGraph.createKleinbergSmallWorldGraph()","£"+noNodes+"£"+noEdges+"£"+clusteringExponent);
	//  		int waittime= 0;
	//  		while(!ClientServerBroker.createKleinbergSmallWorldGraphDone){
	//  			try {
	//  				Thread.sleep(1000);
	//  				if(waittime >MAXWAITTIME){
	//      				Display.updateLogPage("Wait Time: Message Server not Responding", true);
	//      			}
	//  			} catch (InterruptedException e) {
	//  				e.printStackTrace();
	//  			}			
	//  		}
	//  		ClientServerBroker.createKleinbergSmallWorldGraphDone = false;
	//  		
	//    }

	//    @SuppressWarnings({ "rawtypes", "unchecked" })
	////	public Graph<KNode, KLink> createBarabasiAlbertGraph(int init_no_seeds, int numEdgesToAttach, int noAgents){ //preferential attachment
	//	public void createBarabasiAlbertGraph(int init_no_seeds, int numEdgesToAttach, int noAgents, int no_iterations){ //preferential attachment
	//    	ClientServerBroker.triggerPSatHostEvent("ServerKNetworkGraph.createBarabasiAlbertGraph()","£"+init_no_seeds+"£"+numEdgesToAttach+"£"+noAgents+"£"+no_iterations);
	//    	int waittime= 0;
	//  		while(!ClientServerBroker.createBarabasiAlbertGraphDone){
	//  			try {
	//  				Thread.sleep(1000);
	//  				if(waittime >MAXWAITTIME){
	//      				Display.updateLogPage("Wait Time: Message Server not Responding", true);
	//      			}
	//  			} catch (InterruptedException e) {
	//  				e.printStackTrace();
	//  			}			
	//  		}
	//  		ClientServerBroker.createBarabasiAlbertGraphDone = false;
	//    	
	//    }

	//    public void createSequentialGraph(){
	//    	ClientServerBroker.triggerPSatHostEvent("ServerKNetworkGraph.createSequentialGraph()","");
	//    	int waittime= 0;
	//  		while(!ClientServerBroker.createSequentialGraphDone){
	//  			try {
	//  				Thread.sleep(1000);
	//  				if(waittime >MAXWAITTIME){
	//      				Display.updateLogPage("Wait Time: Message Server not Responding", true);
	//      			}
	//  			} catch (InterruptedException e) {
	//  				e.printStackTrace();
	//  			}			
	//  		}
	//  		ClientServerBroker.createSequentialGraphDone = false;
	//    }
	//    
	public void createNetworkFromGmlOrGraphML(){
		JFileChooser chooser = new JFileChooser(".");
		FileNameExtensionFilter xFilter = new FileNameExtensionFilter("agent network (*.gml,.graphML)", "gml","graphML");
		chooser.addChoosableFileFilter(xFilter);
		chooser.setFileFilter(xFilter);

		int retrival = chooser.showOpenDialog(null);
		if (retrival == JFileChooser.APPROVE_OPTION) {
			File file =chooser.getSelectedFile();
			if(file.length() >Integer.MAX_VALUE){
				Display.updateLogPage("Graph file size too large:"+file.length()+"bytes", true);
			}
			else{
				Display.updateLogPage("transferring:"+file.length()+"bytes", false);
				String filename = file.getName();
				String ext = FilenameUtils.getExtension(file.getAbsolutePath());

				byte[] bytes = new byte[(int) file.length()];

				FileInputStream fis;
				try {
					fis = new FileInputStream(file);
					fis.read(bytes); //read file into bytes[]
					fis.close();

					filename = filename+"."+ext;

					PSatAPI.instance.agents = new Agent[0];
					AgentFactory.clearAgents();
					PSatAPI.instance.kgraph = new KNetworkGraph();

					File gfile = PSatAPI.writeGraphMlGmlToFile(Display.hostname,filename,bytes);

					PSatAPI.instance.kgraph.createNetworkFromGmlOrGraphML(gfile);

					AgentFactory.setAgentsPersonalAttributes();

					for(int i=0;i<PSatAPI.instance.agents.length;i++){
						AgentFactory.writeAgent(PSatAPI.instance.agents[i]);
					}				
					Config.serialiseServerConfigInstance();


				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}


	//	public void createRandomGraph(){
	//		ClientServerBroker.triggerPSatHostEvent("ServerKNetworkGraph.createRandomGraph()","");
	//  		int waittime= 0;
	//  		while(!ClientServerBroker.createRandomGraphDone){
	//  			try {
	//  				Thread.sleep(1000);
	//  				if(waittime >MAXWAITTIME){
	//      				Display.updateLogPage("Wait Time: Message Server not Responding", true);
	//      			}
	//  			} catch (InterruptedException e) {
	//  				e.printStackTrace();
	//  			}			
	//  		}
	//  		ClientServerBroker.createRandomGraphDone = false;
	//  		
	//  		
	//		AgentFactory.agents = new Agent[0];
	//		AgentFactory.clearAgents();
	//		
	//		try {
	//			InputStream f = ResourceLoader.load("config_info/names");
	//			InputStreamReader reader = new InputStreamReader(f);
	//			BufferedReader buff = new BufferedReader (reader);
	//			String line;
	//			int i=0;
	//			
	//			while((line=buff.readLine())!=null && i<Display.no_agents){
	//				Agent e = new Agent(line);
	//				boolean added = AgentFactory.addAgent(e);
	//				
	////				Attribute h = new Attribute();
	////				h.setSubjectName(e.getAgentName());
	////				h.setKey("h");
	////				int val1 = rand.nextInt(10) + 1;
	////				h.setValue(""+val1);		
	////				e.addToPersonalAttributes(h);
	//				if(added){
	//					i = i+1;	
	//				}				
	//			}
	//						
	//			buff.close();
	//		} 
	//		catch (FileNotFoundException e1) {
	//			e1.printStackTrace();
	//		}
	//		catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//		
	//		Random rn = new Random();	
	//		for(int i=0;i<AgentFactory.agents.length;i++){
	//			int bond = 8;
	//			if(bond >AgentFactory.agents.length){
	//				bond = AgentFactory.agents.length;
	//			}
	//			int limit = rn.nextInt(AgentFactory.agents.length/bond)+1;
	//			if(limit == 0){
	//				limit = 1;
	//			}
	//			Agent e = AgentFactory.agents[i];
	//			for(int j=0;j<limit;j++){
	//				if(j!=i){
	//					int dice = rn.nextInt(AgentFactory.agents.length -1);
	//					while(dice == i){
	//						dice = rn.nextInt(AgentFactory.agents.length -1);
	//					}
	//					e.addToOtherKnownAgentNames(AgentFactory.agents[dice].getAgentName());
	//				}
	//			}
	//		}
	//		createGraph();
	//	}

	//	public void createNodesOnlyGraph(){
	//		AgentFactory.agents = new Agent[0];
	//		AgentFactory.clearAgents();
	//		
	//		try {
	//			InputStream f = ResourceLoader.load("config_info/names");
	//			InputStreamReader reader = new InputStreamReader(f);
	//			BufferedReader buff = new BufferedReader (reader);
	//			String line;
	//			int i=0;
	//			
	//			while((line=buff.readLine())!=null && i<Display.no_agents){
	//				Agent e = new Agent(line);
	//				boolean added = AgentFactory.addAgent(e);
	//				
	////				Attribute h = new Attribute();
	////				h.setSubjectName(e.getAgentName());
	////				h.setKey("h");
	////				int val1 = rand.nextInt(10) + 1;
	////				h.setValue(""+val1);		
	////				e.addToPersonalAttributes(h);
	//				if(added){
	//					i = i+1;	
	//				}				
	//			}
	//						
	//			buff.close();
	//		} 
	//		catch (FileNotFoundException e1) {
	//			e1.printStackTrace();
	//		}
	//		catch (IOException e) {
	//			e.printStackTrace();
	//		}
	//		createGraph();
	//	}

	private void mutateEdges(KNode source, KNode target){
		if(edgeExist(source, target)){
			PSatAPI.netMutateEdges(source, target, "removeedge");
		}
		else{
			PSatAPI.netMutateEdges(source, target, "addedge");
		}
	}

//	public static boolean addEdge(KNode source, KNode target, int edgecount){
//		return g.addEdge(new EdgeFactory().create(), source, target);
//	}

	public static boolean nodeExist(KNode node){
		for(KNode n:PSatAPI.instance.g.getVertices()){
			if(node.id.equals(n.id)){
				return true;
			}
		}
		return false;
	}

	public static boolean edgeExist(KNode source, KNode target){
		for(KLink link:PSatAPI.instance.g.getEdges()){
			Collection<KNode> nodes  =PSatAPI.instance.g.getIncidentVertices(link);
			boolean sourceLink = false;
			boolean targetLink = false;
			for(KNode knode: nodes){
				if(knode.id.equals(source.id)){
					sourceLink = true;
				}
				if(knode.id.equals(target.id)){
					targetLink = true;
				}
				if(targetLink && sourceLink){
					return true;
				}
			}
		}
		return false;
	}

	public static KLink getEdge(KNode source, KNode target){
		for(KLink link:PSatAPI.instance.g.getEdges()){
			Collection<KNode> nodes  =PSatAPI.instance.g.getIncidentVertices(link);
			boolean sourceLink = false;
			boolean targetLink = false;
			for(KNode knode: nodes){
				if(knode.id.equals(source.id)){
					sourceLink = true;
				}
				if(knode.id.equals(target.id)){
					targetLink = true;
				}
				if(targetLink && sourceLink){
					return link;
				}
			}
		}
		return null;
	}


	public static KNode getNode(String nodeid){
		for(KNode n:PSatAPI.instance.g.getVertices()){
			if(n.id.equals(nodeid)){
				return n;
			}
		}
		return null;
	}



//class GraphFactory implements Factory<Graph<KNode,KLink>> {
//	public Graph<KNode,KLink> create() {
//		return new SparseMultigraph<KNode,KLink>();
//	}
//}
//
//class NodeFactory implements Factory<KNode> {
//	private int n = 0;
//
//	public KNode create() {
//		KNode node = new KNode(""+n++);
//		return node;
//	}
//}
//class EdgeFactory implements Factory<KLink> {
//	int edgeCount;
//	EdgeFactory(int edgeCount){
//		this.edgeCount = edgeCount;
//	}
//	public KLink create() {
//		KLink link = new KLink(1.0, 10,edgeCount);
//		return link;
//	}
//}
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
    
//  public Graph<KNode, KLink> createEppsteinPowerLawGraph(int noNodes, int noEdges, int degreeExponent){ // power law
   public void createEppsteinPowerLawGraph(int noNodes, int noEdges, int degreeExponent){ // power law
   	Graph<KNode, KLink>  eppstein = 
   			new EppsteinPowerLawGenerator<KNode,KLink>(new GraphFactory(), new NodeFactory(),
   										new EdgeFactory(), noNodes, noEdges, degreeExponent).create();
   	
   	eppstein = transformGraphNodes(eppstein);
       createGraph2();
   	//return eppstein;
   }
   
//   public Graph<KNode, KLink> createKleinbergSmallWorldGraph(int noNodes, int noEdges, int clusteringExponent){ //small worlds
   public void createKleinbergSmallWorldGraph(int noNodes, int noEdges, double clusteringExponent, ConfigInstance instance){ //small worlds
   	if(noEdges < 2){
   		noEdges = 2;
   	}
   	Graph<KNode, KLink>  smallworld = 
   			new KleinbergSmallWorldGenerator<KNode,KLink>(new GraphFactory(), new NodeFactory(),
   										new EdgeFactory(), noNodes, noEdges, clusteringExponent).create();
   	smallworld = transformGraphNodes(smallworld);
       createGraph2();

       //return smallworld;
   }
   
   @SuppressWarnings({ "rawtypes", "unchecked" })
//	public Graph<KNode, KLink> createBarabasiAlbertGraph(int init_no_seeds, int numEdgesToAttach, int noAgents){ //preferential attachment
	public void createBarabasiAlbertGraph(int init_no_seeds, int numEdgesToAttach, int noAgents, int no_iterations, ConfigInstance instance){ //preferential attachment
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
       
       gx = transformGraphNodes(gx);
   	
       createGraph2();
   	//return gx;
   }
   
   private Graph<KNode, KLink> transformGraphNodes(Graph<KNode, KLink> graph){
   	String[] names = AgentFactory.getAllPossibleNames();
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
   		Agent agent = AgentFactory.autoGenAgentWoutConnections(name);
   		AgentFactory.addAgent(agent);
       	knode.id = name;
       	
       	PSatAPI.instance.agentCollectionNames.add(agent.getAgentName());
   	}
   	
   	for(KLink link: graph.getEdges()){
   		Collection<KNode> in = graph.getIncidentVertices(link);

   		
   		KNode source = (KNode)in.toArray()[0];
   		KNode dest = (KNode)in.toArray()[1];
   		
   		Agent s_agent = AgentFactory.getAgent(source.id);
   		Agent d_agent = AgentFactory.getAgent(dest.id);
   		s_agent.addToOtherKnownAgentNames(d_agent.getAgentName());
   		AgentFactory.addAgent(s_agent);
   	}
   	
   	return graph;
   }
   
   public void createSequentialGraph(){

	   PSatAPI.instance.agents = new Agent[0];
		AgentFactory.clearAgents();
		
		try {
			InputStream f = ResourceLoader.load("config_info/names");
			InputStreamReader reader = new InputStreamReader(f);
			BufferedReader buff = new BufferedReader (reader);
			String line;
			int i=0;
			
			while((line=buff.readLine())!=null && i<PSatAPI.instance.no_agents){
				Agent e = new Agent(line);
				
//				Attribute h = new Attribute();
//				h.setSubjectName(e.getAgentName());
//				h.setKey("h");
//				int val1 = rand.nextInt(10) + 1;
//				h.setValue(""+val1);		
//				e.addToPersonalAttributes(h);
				
				AgentFactory.addAgent(e);	
				PSatAPI.instance.agentCollectionNames.add(e.getAgentName());
				
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
		
		for(int i=0;i<PSatAPI.instance.agents.length;i++){
			Agent e = PSatAPI.instance.agents[i];
			if(i+1 < PSatAPI.instance.agents.length){
				e.addToOtherKnownAgentNames(PSatAPI.instance.agents[i+1].getAgentName());
			}
		}
		createGraph2();
   }
   
   public void createNetworkFromGmlOrGraphML(File gfile){
   	GMLGraphLoader gloader = new GMLGraphLoader();
   	gloader.loadGraph(gfile);
   	gloader.extractNodes();
		createGraph2();
   }
   	
   public void createNetworkFromGraphML(String fileName){
   	
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
					AgentFactory.addAgent(agent);
					
					PSatAPI.instance.agentCollectionNames.add(agent.getAgentName());
				}
				
				Collection< KLink> edges = graph.getEdges();
				for(KLink klink:edges){
					Pair<KNode> pair =graph.getEndpoints(klink);
					
					KNode n1 = pair.getFirst();
					KNode n2 = pair.getSecond();
					Agent agent1 = AgentFactory.getAgent(n1.id);
					Agent agent2 = AgentFactory.getAgent(n2.id);
					
					if(agent1 != null && agent2 !=null){
						agent1.addToOtherKnownAgentNames(agent2.getAgentName());
					}
				}				
				
			} catch (GraphIOException e) {
				e.printStackTrace();
			}
				
		}
		 
		 createGraph2();
		    	
   }
   
	public void createRandomGraph(){

		PSatAPI.instance.agents = new Agent[0];
		AgentFactory.clearAgents();
		
		try {
			InputStream f = ResourceLoader.load("config_info/names");
			InputStreamReader reader = new InputStreamReader(f);
			BufferedReader buff = new BufferedReader (reader);
			String line;
			int i=0;
			
			while((line=buff.readLine())!=null && i<PSatAPI.instance.no_agents){
				Agent e = new Agent(line);
				boolean added = AgentFactory.addAgent(e);
				
//				Attribute h = new Attribute();
//				h.setSubjectName(e.getAgentName());
//				h.setKey("h");
//				int val1 = rand.nextInt(10) + 1;
//				h.setValue(""+val1);		
//				e.addToPersonalAttributes(h);
				if(added){
					i = i+1;	
				}	
				
				PSatAPI.instance.agentCollectionNames.add(e.getAgentName());
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
		for(int i=0;i<PSatAPI.instance.agents.length;i++){
			int bond = 8;
			if(bond >PSatAPI.instance.agents.length){
				bond = PSatAPI.instance.agents.length;
			}
			int limit = rn.nextInt(PSatAPI.instance.agents.length/bond)+1;
			if(limit == 0){
				limit = 1;
			}
			Agent e = PSatAPI.instance.agents[i];
			for(int j=0;j<limit;j++){
				if(j!=i){
					int dice = rn.nextInt(PSatAPI.instance.agents.length -1);
					while(dice == i){
						dice = rn.nextInt(PSatAPI.instance.agents.length -1);
					}
					e.addToOtherKnownAgentNames(PSatAPI.instance.agents[dice].getAgentName());
				}
			}
		}
		createGraph2();
	}
   
	public void createNodesOnlyGraph(){

		PSatAPI.instance.agents = new Agent[0];
		AgentFactory.clearAgents();
		
		try {
			InputStream f = ResourceLoader.load("config_info/names");
			InputStreamReader reader = new InputStreamReader(f);
			BufferedReader buff = new BufferedReader (reader);
			String line;
			int i=0;
			
			while((line=buff.readLine())!=null && i<PSatAPI.instance.no_agents){
				Agent e = new Agent(line);
				boolean added = AgentFactory.addAgent(e);
				
//				Attribute h = new Attribute();
//				h.setSubjectName(e.getAgentName());
//				h.setKey("h");
//				int val1 = rand.nextInt(10) + 1;
//				h.setValue(""+val1);		
//				e.addToPersonalAttributes(h);
				if(added){
					i = i+1;	
				}	
				
				PSatAPI.instance.agentCollectionNames.add(e.getAgentName());
			}
						
			buff.close();
		} 
		catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		createGraph2();
	}
	
	
	public void mutateEdges(KNode source, KNode target, String mutationType){
		
		if(source.id.equals(target.id)){
			return;
		}
		synchronized(PSatAPI.instance.g){
			Collection< KLink> edges = PSatAPI.instance.g.getEdges();
			
			if(mutationType.equals("removeedge")){
				for(KLink klink:edges){
					Pair<KNode> pair =PSatAPI.instance.g.getEndpoints(klink);
					
					KNode n1 = pair.getFirst();
					KNode n2 = pair.getSecond();
					
					if((n1.id.equals(source.id) && n2.id.equals(target.id)) 
							|| (n1.id.equals(target.id) && n2.id.equals(source.id))){
						//link exist - remove link

						Agent agent1 = AgentFactory.getAgent(n1.id);
						Agent agent2 = AgentFactory.getAgent(n2.id);
						
						if(agent1 != null && agent2 !=null){
							agent1.removeFromOtherKnownAgentNames(agent2.getAgentName());
							agent2.removeFromOtherKnownAgentNames(agent1.getAgentName());
							
							AgentFactory.writeAgent(agent1);
							AgentFactory.writeAgent(agent2);
						}
						
						PSatAPI.instance.g.removeEdge(klink);
						Config.serialiseServerConfigInstance();
						
						Display.repaintListbox();
						
						return;
					}						
					
				}
			}
			else if(mutationType.equals("addedge")){
				boolean linkexist = false;
				for(KLink klink:edges){
					Pair<KNode> pair =PSatAPI.instance.g.getEndpoints(klink);
					
					KNode n1 = pair.getFirst();
					KNode n2 = pair.getSecond();
										
					if((n1.id.equals(source.id) && n2.id.equals(target.id)) 
							|| (n1.id.equals(target.id) && n2.id.equals(source.id))){
						linkexist = true;
						break;
					}
				}
				if(!linkexist){//if link already exist - then don't add another link
					Agent agent1 = AgentFactory.getAgent(source.id);
					Agent agent2 = AgentFactory.getAgent(target.id);
					
					if(agent1 != null && agent2 !=null){
						agent1.addToOtherKnownAgentNames(agent2.getAgentName());
						AgentFactory.writeAgent(agent1);
						AgentFactory.writeAgent(agent2);
					}
					
					PSatAPI.instance.g.addEdge(new EdgeFactory().create(), source, target);
//					boolean successful = instance.g.addEdge(new EdgeFactory().create(), source, target);
					Config.serialiseServerConfigInstance();
				}
			}

		}

	}
		

}


