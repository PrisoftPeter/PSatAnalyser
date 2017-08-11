package gla.server.kernel.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FilenameUtils;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.file.FileSource;
import org.graphstream.stream.file.FileSourceFactory;

import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import gla.prisoft.shared.Agent;
import gla.prisoft.shared.ConfigInstance;
import gla.prisoft.shared.KLink;
import gla.prisoft.shared.KNode;
import gla.server.session.ServerConfigInstance;

public class GMLGraphLoader {
	private Graph g;

	public void loadGraph(ServerConfigInstance instance,ConfigInstance cinstance, File gfile) {

		String ext = FilenameUtils.getExtension(gfile.getAbsolutePath());
		if(ext.equals("GraphML")){
	    	new ServerKNetworkGraph().createNetworkFromGraphML(gfile.getAbsolutePath(), instance, cinstance);
			
		}
		else{
			g = new DefaultGraph("g");
			FileSource fs = null;
			try {
				fs = FileSourceFactory.sourceFor(gfile.getAbsolutePath());
				fs.addSink(g);

				fs.begin(gfile.getAbsolutePath());

				while (fs.nextEvents()) {
					// Optionally some code here ...
				}
				fs.end();

			}
			catch( IOException e) {
				e.printStackTrace();
			}
			finally {
				if(fs != null)
					fs.removeSink(g);
			}
		}
		
//		JFileChooser chooser = new JFileChooser(".");
//		FileNameExtensionFilter xFilter = new FileNameExtensionFilter("agent network (*.gml,.graphML)", "gml","graphML");
//		chooser.addChoosableFileFilter(xFilter);
//		chooser.setFileFilter(xFilter);
//
//		int retrival = chooser.showOpenDialog(null);
//		if (retrival == JFileChooser.APPROVE_OPTION) {
////			File file =chooser.getSelectedFile();
//			
//		}

	}
	
	@SuppressWarnings("rawtypes")
	public static edu.uci.ics.jung.graph.DirectedGraph loadGraph1() {
		Graph gx = null;
		JFileChooser chooser = new JFileChooser(".");
		FileNameExtensionFilter xFilter = new FileNameExtensionFilter("agent network (*.gml, graphML)", "gml","graphML");
		chooser.addChoosableFileFilter(xFilter);
		chooser.setFileFilter(xFilter);

		int retrival = chooser.showOpenDialog(null);
		if (retrival == JFileChooser.APPROVE_OPTION) {
			File file =chooser.getSelectedFile();

			gx = new DefaultGraph("gx");
			FileSource fs = null;
			try {
				fs = FileSourceFactory.sourceFor(file.getAbsolutePath());
				fs.addSink(gx);

				fs.begin(file.getAbsolutePath());

				while (fs.nextEvents()) {
					// Optionally some code here ...
				}
				fs.end();

			}
			catch( IOException e) {
				e.printStackTrace();
			}
			finally {
				if(fs != null)
					fs.removeSink(gx);
			}

		}
		return convertToJung(gx);

	}
	
	static File file;
	@SuppressWarnings("rawtypes")
	public static edu.uci.ics.jung.graph.DirectedGraph loadGraph2() {
		Graph gx = null;
		//File file = new File("config_info/protocols.gml");
				
		gx = new DefaultGraph("gx");
		FileSource fs = null;
		try {
			if(file == null){
				URL furl = ResourceLoader.load1("config_info/protocols.gml");
//				URL furl = GMLGraphLoader.class.getResource("/protocols.gml");
								
				file = new File(furl.getPath());
				
				if (!file.isFile()){
					Object[] options = {"cancel","Yes"};
					int n = JOptionPane.showOptionDialog(null,
							"Unable to load protocol.gml, load manually","Problem loading protocol suite",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.QUESTION_MESSAGE,
							null,options,options[0]);
					if(n==1){
						JFileChooser chooser = new JFileChooser(".");
						FileNameExtensionFilter xFilter = new FileNameExtensionFilter(" agent network (*.gml, graphML)", "gml","graphML");
						chooser.addChoosableFileFilter(xFilter);
						chooser.setFileFilter(xFilter);

						int retrival = chooser.showOpenDialog(null);
						if (retrival == JFileChooser.APPROVE_OPTION) {
							file =chooser.getSelectedFile();
						}
					}
				}	
			}			
			
			fs = FileSourceFactory.sourceFor(file.getAbsolutePath());
			
			fs.addSink(gx);

			fs.begin(file.getAbsolutePath());

			while (fs.nextEvents()) {
				// Optionally some code here ...
			}
			fs.end();

		}
		catch( IOException e) {
			e.printStackTrace();
		}
		finally {
			if(fs != null)
				fs.removeSink(gx);
		}
		return convertToJung(gx);

	}
	
	@SuppressWarnings("rawtypes")
	public static edu.uci.ics.jung.graph.DirectedGraph convertToJung(Graph gx){
		edu.uci.ics.jung.graph.DirectedGraph<KNode, KLink> ng = new DirectedSparseMultigraph<KNode, KLink>();
		
		for (Node n : gx.getNodeSet()) {
			String id = n.getId();
        	KNode node = new KNode(id);
			ng.addVertex(node);			
		}
		
		for(Edge edge:gx.getEdgeSet()){
			Node source = edge.getSourceNode();
			Node target = edge.getTargetNode();
						
			KNode source_kn = null;
			KNode target_kn = null;
			
			for(KNode ngn: ng.getVertices()){
				if(ngn.toString().equals(source.toString())){
					source_kn = ngn;
				}
				else if(ngn.toString().equals(target.toString())){
					target_kn = ngn;
				}
				
				if(target_kn != null && source_kn != null){
					break;
				}
			}
				
			KLink link = new KLink(1.0, 10, ServerKNetworkGraph.edgeCount++);
			ng.addEdge(link,source_kn, target_kn, EdgeType.DIRECTED);
		}
		
		return ng;
	}
		
	public void extractNodes(ServerConfigInstance instance){
		if(g!=null){
			for (Node n : g.getNodeSet()) {
				String id = n.getId();
				
				Agent agent = new Agent(id);
								
				ServerAgentFactory.addAgent(agent, instance);			
			}
			
			
			for(Edge e: g.getEdgeSet()){
				Node source_n = e.getSourceNode();			
				String sn_id = source_n.getId();			
				Agent source = ServerAgentFactory.getAgent(sn_id, instance);
											
				Node target_n = e.getTargetNode();
				String tn_id = target_n.getId();			
				Agent target = ServerAgentFactory.getAgent(tn_id, instance);
							
				if(source != null && target !=null){
					source.addToOtherKnownAgentNames(tn_id);
				}
			}
			
		}

		
	}
	
	public void extractNodes1(ServerConfigInstance instance){

		Random rand = new Random();
				
		for (Node n : g.getNodeSet()) {
			String id = n.getId();
			
			if(Helper.isNumeric(id)){
				id = "e"+id;
				Agent agent = new Agent(id);
				ServerAgentFactory.addAgent(agent, instance);	
			}
			else{
				Agent agent = new Agent(id);
				ServerAgentFactory.addAgent(agent, instance);	
			}			
		}			
			
		for(Edge e: g.getEdgeSet()){
			Node source_n = e.getSourceNode();			
			String sn_id = source_n.getId();
			if(Helper.isNumeric(sn_id)){
				sn_id = "e"+sn_id;
			}
			Agent source = ServerAgentFactory.getAgent(sn_id, instance);
			
			Node target_n = e.getTargetNode();
			String tn_id = target_n.getId();
			if(Helper.isNumeric(tn_id)){
				tn_id = "e"+tn_id;	
			}			
			Agent target = ServerAgentFactory.getAgent(tn_id, instance);
			
			ArrayList<String> knownToTarget = new ArrayList<String>();
			ArrayList<String> knownToSource = new ArrayList<String>();
			for(Agent en: instance.agents){
				for(String enn: en.getOtherKnownAgentNames()){
					if(enn.equals(target.getAgentName())){
						knownToTarget.add(enn);
					}
				}
			}
			for(Agent en: instance.agents){
				for(String enn: en.getOtherKnownAgentNames()){
					if(enn.equals(source.getAgentName())){
						knownToSource.add(enn);
					}
				}
			}
			
			if(knownToTarget.size() <=knownToSource.size()){
				target.addToOtherKnownAgentNames(source.getAgentName());
				ServerAgentFactory.addAgent(target, instance);	
			}
			else{
				source.addToOtherKnownAgentNames(target.getAgentName());
				ServerAgentFactory.addAgent(source, instance);
			}
								
		}
		
		for(int i=0;i<20;i++){
			for(Agent en: instance.agents){
				ArrayList<String> knownToEn= new ArrayList<String>();
				
				for(Agent en1: instance.agents){
					if(!en1.getAgentName().equals(en.getAgentName())){
						for(String ens: en1.getOtherKnownAgentNames()){
							if(ens.equals(en.getAgentName())){
								knownToEn.add(en1.getAgentName());
							}
						}
					}
				}
				
				if(knownToEn.size() == 0){
					String otherNe = en.getOtherKnownAgentNames()[0];
					Agent oe = ServerAgentFactory.getAgent(otherNe, instance);
					en.removeFromOtherKnownAgentNames(otherNe);
					oe.addToOtherKnownAgentNames(en.getAgentName());
					
					ServerAgentFactory.addAgent(en, instance);
					ServerAgentFactory.addAgent(oe, instance);
				}

			}	
		}
		
		
		for(int i=0;i<40;i++){
			for(Agent en: instance.agents){
				if(en.getOtherKnownAgentNames().length > 5){
					int val1 = rand.nextInt(7)-1;
					if(val1 <0){
						val1 = 0;
					}
					Agent ex = instance.agents[val1];
					if(!ex.getAgentName().equals(en.getAgentName())){
						en.removeFromOtherKnownAgentNames(ex.getAgentName());
						ex.addToOtherKnownAgentNames(en.getAgentName());						
					}

					ServerAgentFactory.addAgent(en, instance);
					ServerAgentFactory.addAgent(ex, instance);
				}
			}			
		}
		
		for(int i=0;i<150;i++){
			for(Agent en: instance.agents){
				ArrayList<String> knownToEn= new ArrayList<String>();
				
				for(Agent en1: instance.agents){
					if(!en1.getAgentName().equals(en.getAgentName())){
						for(String ens: en1.getOtherKnownAgentNames()){
							if(ens.equals(en.getAgentName())){
								knownToEn.add(en1.getAgentName());
							}
						}
					}
				}
				
				if(knownToEn.size() > 6){
					Agent e1 = ServerAgentFactory.getAgent(knownToEn.get(0), instance);
					e1.removeFromOtherKnownAgentNames(en.getAgentName());
					en.addToOtherKnownAgentNames(e1.getAgentName());
					
					ServerAgentFactory.addAgent(en, instance);
					ServerAgentFactory.addAgent(e1, instance);
				}

			}	
		}
		
		for(int i=0;i<20;i++){
			for(Agent en: instance.agents){
				if(en.getOtherKnownAgentNames().length ==0){
					
					ArrayList<String> knownToEn= new ArrayList<String>();
					
					for(Agent en1: instance.agents){
						if(!en1.getAgentName().equals(en.getAgentName())){
							for(String ens: en1.getOtherKnownAgentNames()){
								if(ens.equals(en.getAgentName())){
									knownToEn.add(en1.getAgentName());
								}
							}
						}
					}
					
//					Attribute h = new Attribute();
//					h.setSubjectName(en.getAgentName());
//					h.setKey("h");
//					int val1 = rand.nextInt(10) + 1;
//					h.setValue(""+val1);		
//					en.addToPersonalAttributes(h);	
					
					if(knownToEn.size() >1){
						String trans = knownToEn.get(0);
						Agent ex = ServerAgentFactory.getAgent(trans, instance);
						en.addToOtherKnownAgentNames(trans);
						ex.removeFromOtherKnownAgentNames(en.getAgentName());
						
//						Attribute h1 = new Attribute();
//						h1.setSubjectName(ex.getAgentName());
//						h1.setKey("h");
//						int val11 = rand.nextInt(10) + 1;
//						h1.setValue(""+val11);		
//						ex.addToPersonalAttributes(h1);	
						
						ServerAgentFactory.addAgent(en, instance);
						ServerAgentFactory.addAgent(ex, instance);
						
						if(knownToEn.size() >2){
							String trans1 = knownToEn.get(1);
							Agent ex1 = ServerAgentFactory.getAgent(trans1, instance);
							en.addToOtherKnownAgentNames(trans1);
							ex1.removeFromOtherKnownAgentNames(en.getAgentName());
							
							ServerAgentFactory.addAgent(en, instance);
							ServerAgentFactory.addAgent(ex1, instance);
						}
					}
				}
			}			
		}
				
		renameEntities(instance);
	}
	
	private static void renameEntities(ServerConfigInstance instance){

		int lastcount = 1;
		int overflow = 0;
		for(Agent agent: instance.agents){
			String newName = "";
			String oldName = agent.getAgentName();
			
			try {
				InputStream f = ResourceLoader.load("config_info/names");
				InputStreamReader reader = new InputStreamReader(f);
				BufferedReader buff = new BufferedReader (reader);
				String line;
				int i=0;
				
				while((line=buff.readLine())!=null && i<lastcount){
					newName = line;
					i = i+1;
				}
				lastcount = lastcount +1;
				buff.close();
				
				if(newName.length()==0){
					newName = "e"+overflow;
					overflow = overflow +1;
				}
				
				agent.setAgentName(newName);
				for(Agent agent2:instance.agents){
					String [] oken = agent2.getOtherKnownAgentNames();
					String [] temp = new String[oken.length];
					
					for(int j=0;j<oken.length;j++){
						if(oken[j].equals(oldName)){
							temp[j] = newName;
						}
						else{
							temp[j] = oken[j];
						}
					}
					agent2.setOtherKnownAgentNames(temp);
				}
				
				for(Agent agent2:instance.agents){
					for(int j=0;j<agent2.getPersonalAttributes().length;j++){
						if(agent2.getPersonalAttributes()[j].getSubjectName().equals(oldName)){
							agent2.getPersonalAttributes()[j].setSubjectName(newName);	
						}
					}
					for(int j=0;j<agent2.getSharedAttributes().length;j++){
						if(agent2.getSharedAttributes()[j].getSubjectName().equals(oldName)){
							agent2.getSharedAttributes()[j].setSubjectName(newName);	
						}
					}
				}				
				
			}
			catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			ServerAgentFactory.deleteFromDatastore(oldName, instance.sessionid);
		}
		
		for(Agent agent: instance.agents){
			ServerAgentFactory.writeAgent(agent, instance);
		}
		
	}
}
