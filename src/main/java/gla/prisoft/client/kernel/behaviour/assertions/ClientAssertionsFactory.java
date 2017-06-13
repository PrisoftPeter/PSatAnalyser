package gla.prisoft.client.kernel.behaviour.assertions;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Properties;

import gla.prisoft.client.PSatClient;
import gla.prisoft.client.kernel.display.model.AssertionsView;
import gla.prisoft.server.PSatAPI;
import gla.prisoft.shared.Agent;
import gla.prisoft.shared.CollectiveMode;
import gla.prisoft.shared.CollectiveStrategy;


public class ClientAssertionsFactory implements Serializable{
	private static final long serialVersionUID = 1L;
			
	private String agentName;

	@SuppressWarnings("unused")
	private AssertionsView av;
	
//	private double p_uncertainty_ckecked;
//	private double p_certainty_ckecked;
//	private int count_uncertainty_ckecked;
//	private int count_certainty_ckecked;
//	private int total_certainty;
//	private int total_uncertainty;
//	private int actual_total_certainty;
//	private int actual_total_uncertainty;
	
	public ClientAssertionsFactory(String agentName){		
		this.agentName = agentName;
		PSatClient.netClientAssertionsFactory(agentName);
	}
	
		
	public void displayAssertions(final AssertionsView av){
		
		if(PSatAPI.instance.isModePick){
			this.av = av;
			
			if(PSatAPI.instance.is_aspect_run){
				String partialPath = agentName+"_"+PSatAPI.instance.sourceAgentName+"_Aspect";
				if(partialPath != null){
					displayAssertionsStore(agentName,partialPath,av);						
				}
//				Thread queryThread2 = new Thread() {
//					public void run() {						
//						//Display.updateProgressComponent(100,"");
//					}					
//				};
//				queryThread2.start();
			}
			else{
				Agent a = PSatClient.netGetAgent(agentName);
				if(!a.containedInMemoryStores(PSatAPI.instance.sourceAgentName)){
					PSatClient.netNewMemoryStore(a.getAgentName());
				}						
				String [] partialPaths = PSatClient.netGetAssertionsStorePaths(agentName);
				for(String partialPath: partialPaths){	
					if(partialPath != null){
						displayAssertionsStore(agentName,partialPath,av);							
					}
				}
//				Thread queryThread2 = new Thread() {
//					public void run() {						
						//Display.updateProgressComponent(100,"");
//					}
//				};
//				queryThread2.start();
			}
				
		}
	}
	
	public void init(){
		PSatClient.netClientAssertionsFactoryInit();
	}
		
	private void displayAssertionsStore(String agentName, String partialPath, AssertionsView av) {
//		PSatClient.netDeseraliseConfigInstance();
		Properties [] ppties = PSatClient.netDisplayAssertionsStore(agentName, partialPath);
		
		if(PSatAPI.instance.is_aspect_run){
			for(Properties ppty: ppties){
				String aspectType = ppty.getProperty("aspectType");
				boolean checked = false;
				if(ppty.getProperty("checked").equals("true")){
					checked =true;
				}
				String genericFormula = ppty.getProperty("genericFormula");
				double goal_v = new Double(ppty.getProperty("goalv"));
				String meaning = ppty.getProperty("meaning");
				String collectiveStrDesc = ppty.getProperty("collectiveStrategy"); 
				
				CollectiveStrategy cs = CollectiveMode.getCollectiveStrategy(collectiveStrDesc);
				String cs_limithtmldesc = CollectiveMode.getModeLimitHtmlDesc(cs);
				if(cs != CollectiveStrategy.NONE){
					meaning = collectiveStrDesc+" "+meaning;
					meaning = collectiveStrDesc+" "+meaning;
					meaning = meaning.replace("<html>", "");
					meaning = "<html>"+meaning;
					
					genericFormula = cs_limithtmldesc+"("+genericFormula+")";
					genericFormula =genericFormula.replace("<html>", "");
					genericFormula =genericFormula.replace("</html>", "");
					genericFormula = "<html>"+genericFormula+"</html>";
				}
				
				av.model.addRow(new Object[]{aspectType,checked,genericFormula,goal_v,meaning});
				av.model.fireTableDataChanged();
				Rectangle cellBounds = av.table.getCellRect(av.table.getRowCount() - 1, 0, true);
				av.table.scrollRectToVisible(cellBounds);
			}
		}
		else{
			for(Properties ppty: ppties){
				String a_counter = ppty.getProperty("a_counter");
				boolean checked = false;
				if(ppty.getProperty("checked").equals("true")){
					checked =true;
				}
				String w = ppty.getProperty("w");
				double goal_v = new Double(ppty.getProperty("goalv"));
				String meaning = ppty.getProperty("meaning");
				String collectiveStrDesc = ppty.getProperty("collectiveStrategy"); 
				
				CollectiveStrategy cs = CollectiveMode.getCollectiveStrategy(collectiveStrDesc);
				String cs_limithtmldesc = CollectiveMode.getModeLimitHtmlDesc(cs);
				if(cs != CollectiveStrategy.NONE){
					meaning = collectiveStrDesc+" "+meaning;
					meaning = meaning.replace("<html>", "");
					meaning = "<html>"+meaning;
					w = cs_limithtmldesc+"("+w+")";
					w =w.replace("<html>", "");
					w =w.replace("</html>", "");
					w = "<html>"+w+"</html>";

				}
				
				av.model.addRow(new Object[]{a_counter,checked,w, goal_v,meaning});
				av.model.fireTableDataChanged();
				Rectangle cellBounds = av.table.getCellRect(av.table.getRowCount() - 1, 0, true);
				av.table.scrollRectToVisible(cellBounds);
			}

		}
		
	}
}
