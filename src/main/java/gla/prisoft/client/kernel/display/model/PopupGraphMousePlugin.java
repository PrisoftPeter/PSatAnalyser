package gla.prisoft.client.kernel.display.model;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import edu.uci.ics.jung.algorithms.layout.GraphElementAccessor;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractPopupGraphMousePlugin;
import gla.prisoft.client.Display;
import gla.prisoft.client.PSatClient;
import gla.prisoft.client.session.ClientServerBroker;
import gla.prisoft.shared.Agent;
import gla.prisoft.shared.KNode;

public class PopupGraphMousePlugin extends AbstractPopupGraphMousePlugin implements MouseListener {
	
	public static boolean isrequirements = false;
    public PopupGraphMousePlugin() {
        this(MouseEvent.BUTTON3_MASK);
    }
    public PopupGraphMousePlugin(int modifiers) {
        super(modifiers);
    }

    /**
     * If this event is over a station (vertex), pop up a menu to
     * allow the user to perform a few actions; else, pop up a menu over the layout/canvas
     *
     * @param e
     */
    @SuppressWarnings("unchecked")
    protected void handlePopup(MouseEvent e) {
        final VisualizationViewer<String,String> vv =(VisualizationViewer<String,String>)e.getSource();
        final Point2D p = e.getPoint();
        final Point2D ivp = p;
        JPopupMenu popup = new JPopupMenu();

        GraphElementAccessor<String,String> pickSupport = vv.getPickSupport();

        if(pickSupport != null) {
        	Object v = pickSupport.getVertex(vv.getGraphLayout(), ivp.getX(), ivp.getY());
        	final KNode pickV = (KNode)v;

            if(pickV != null) {
            	JMenuItem nameMenuItem = new JMenuItem(pickV.id);
            	nameMenuItem.setEnabled(false);
            	nameMenuItem.setBackground(Color.yellow);
            	popup.add(nameMenuItem);
            	
            	popup.addSeparator();

           		//ImageIcon confIcon = new ImageIcon("img/settings.png");
           		ImageIcon confIcon = new ImageIcon(getClass().getResource("/settings.png"));
           		//ImageIcon dIcon = new ImageIcon("img/download.png");
           		ImageIcon dIcon = new ImageIcon(getClass().getResource("/download.png"));
           		JMenu pwsOpenMenu = new JMenu("Memory Stores");
           		if(Display.instance.sourceAgentName == null){
           			pwsOpenMenu.setEnabled(false);
           		}
//           		pwsOpenMenu.addActionListener(new ActionListener(){
//					public void actionPerformed(ActionEvent e) {
//						Thread queryThread = new Thread() {
//							public void run() {
//								Display.updateProgressComponent(0, "");
////								Display.selfAgentName = pickV.id;
//								MemoryFactory.dumpMemoryStoreOnDisplay(pickV.id);        						
//							}
//						};
//						queryThread.start();						
//					}            		
//           		});
           		
           		pwsOpenMenu.setIcon(dIcon);
           		JLabel l1 = new JLabel("Instances:");
           		l1.setIcon(confIcon);
           		l1.setEnabled(false);
           		pwsOpenMenu.add(l1);
           		
           		String [] memoryStorePaths = PSatClient.netGetMemoryStorePaths(pickV.id);
           		
           		if(memoryStorePaths.length ==0){
           			JLabel l2 = new JLabel("          <<none>>");
           			l2.setEnabled(false);
           			pwsOpenMenu.add(l2);
           		}
           		else{
           			for(final String storePath: memoryStorePaths){
           				
               			JMenuItem configMenu = new JMenuItem(storePath);
               			configMenu.addActionListener(new ActionListener(){
        					public void actionPerformed(ActionEvent e) {
        						if(Display.instance.is_generating_memory_store){
        						    JOptionPane.showMessageDialog(Display.iframeNet, "Memory Stores generation in progress...",  "Wait!", JOptionPane.NO_OPTION);
                        			return;
        						}
        						Thread queryThread = new Thread() {
        							public void run() {
        								Display.instance.isMemoryStoreMode = true;
        								Display.updateProgressComponent(0, "");
//        								Display.selfAgentName = pickV.id;
                						PSatClient.netSerialiseConfigInstance();
                						ClientServerBroker.triggerDumpMemoryStoreOnDisplay(pickV.id, storePath, -10);
                						Display.instance.isMemoryStoreMode = false;
                						PSatClient.netSerialiseConfigInstance();
                						Display.updateProgressComponent(100, "");
        							}
        						};
        						queryThread.start();
        					}            		
                   		});
               			pwsOpenMenu.add(configMenu);           			
               		}	
           		}
           		
           		popup.add(pwsOpenMenu);
           		
            	popup.addSeparator();

            	//ImageIcon privacyAssIcon = new ImageIcon("img/pa.png");
           		ImageIcon privacyAssIcon = new ImageIcon(getClass().getResource("/pa.png"));

//           		JMenu paMenu = new JMenu("privacy requirements");
           		JMenu paMenu = new JMenu("privacy requirements");
           		paMenu.setIcon(privacyAssIcon);
           		           		
           		
           		////goal
           		ImageIcon goalIcon = new ImageIcon(getClass().getResource("/instance.png"));
       			JMenuItem goalMenu = new JMenuItem("<html>Global Privacy Goal:v<sub>pr</sub></html>",goalIcon);       			
       			goalMenu.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						Agent selectedAgent = PSatClient.netGetAgent(pickV.id);
						if(selectedAgent !=null){
							String input = JOptionPane.showInputDialog(null, "<html>current goal(v<sub>pr</sub>)="+selectedAgent.getGlobalPrivacyGoal_v()+"</html>:", "Set new privacy goal",JOptionPane.PLAIN_MESSAGE);
							double globalGoal_v = 0;
							boolean ok = false;
							if(Display.isNumeric(input)){
								double v = new Double(input);
								if(v >=0 && v <=1){
									globalGoal_v = v;
									ok = true;
									Display.opgset = false;
								}
							}
							if(ok){
								selectedAgent.setGlobalPrivacyGoal_v(globalGoal_v);
								PSatClient.netWriteAgent(selectedAgent);
								PSatClient.netSerialiseConfigInstance();
							}
							else{
								JOptionPane.showMessageDialog(Display.iframeLogProTabPages, "valid goal(v): >=0 && v <=1", "invalid",JOptionPane.ERROR_MESSAGE);					
							}							
						}
						
					}            		
           		});
       			paMenu.add(goalMenu); 
           		
           		
           		
           		///
           		
           		
           		
        		ImageIcon aspectIcon = new ImageIcon(getClass().getResource("/aspect.png"));
           		JMenuItem aspectsMenu = new JMenuItem("Aspects",aspectIcon);       			
           		aspectsMenu.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						
						if(Display.instance.is_generating_memory_store){
						    JOptionPane.showMessageDialog(Display.iframeNet, "Memory Stores generation in progress...",  "Wait!", JOptionPane.NO_OPTION);
                			return;
						}
						
						isrequirements = true;
						Display.instance.selfAgentName = pickV.id;
						PSatClient.netSerialiseConfigInstance();
						
						PSatClient.netPrivacyRequirementAspects(pickV.id);
						Display.updateAssertionsPage(pickV.id, "privacy requirement Aspects");
					}            		
           		});
           		if(Display.instance.is_aspect_run){
           			aspectsMenu.setEnabled(true);
           		}
           		else{
           			aspectsMenu.setEnabled(false);
           		}
       			paMenu.add(aspectsMenu);
       			
       			ImageIcon instanceIcon = new ImageIcon(getClass().getResource("/instance.png"));
       			JMenuItem instanceMenu = new JMenuItem("Instances",instanceIcon);       			
       			instanceMenu.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						if(Display.instance.is_generating_memory_store){
						    JOptionPane.showMessageDialog(Display.iframeNet, "Memory Stores generation in progress...",  "Wait!", JOptionPane.NO_OPTION);
                			return;
						}
						
						isrequirements = true;
						Display.instance.selfAgentName = pickV.id;
						PSatClient.netSerialiseConfigInstance();

						Display.updateAssertionsPage(pickV.id, "privacy requirement Instances");
					}            		
           		});
       			
       			boolean nodeok = false;
       			if(Display.instance.listPathsData.length >0){
       				for(String path:Display.instance.listPathsData){
       					path = path.replace("[", "");
       					path = path.replace("]", "");
       					String [] comps = path.split(" ");
       					
       					for(String s: comps){
       						if(s.equals(pickV.id)){
       							nodeok = true;
       						}
       					}
       				}
       				
       			}
       			
       			if(Display.instance.is_aspect_run ||!nodeok){
       				
       				instanceMenu.setEnabled(false);
           		}
           		else{
           			if(Display.instance.subjectName !=null && Display.instance.targetAgentName !=null){
           				instanceMenu.setEnabled(true);
           			}           			
           		}
       			paMenu.add(instanceMenu); 
           		
//           		JLabel l4 = new JLabel("Instances");
//           		l4.setIcon(confIcon);
//           		l4.setEnabled(false);
//           		paMenu.add(l4);
//           		
//           		if(memoryStorePaths.length ==0){
//           			JLabel l2 = new JLabel("         <<none>>");
//           			l2.setEnabled(false);
//           			paMenu.add(l2);
//           		}
//           		else{
//           			for(final String storePath: memoryStorePaths){
//               			JMenuItem configMenu = new JMenuItem(storePath);
//               			
//               			configMenu.addActionListener(new ActionListener(){
//        					public void actionPerformed(ActionEvent e) {
//        						MemoryFactory.privacyRequirements(pickV.id,storePath);
//        					}            		
//                   		});
//               			paMenu.add(configMenu);           			
//               		}	
//           		}
            	
           		popup.add(paMenu);
           		
           		
//           		JMenuItem prMenuItem = new JMenuItem("privacy requirements");
//           		prMenuItem.setIcon(privacyAssIcon);           		
//           		prMenuItem.addActionListener(new ActionListener(){
//					public void actionPerformed(ActionEvent e) {
//						MemoryFactory.privacyRequirements(pickV.id);
//					}            		
//           		});
//           		popup.add(prMenuItem);  
            	        		
           		
            	popup.addSeparator();

           		//ImageIcon sourceIcon = new ImageIcon("img/source.png");
           		ImageIcon sourceIcon = new ImageIcon(getClass().getResource("/source.png"));
            	JMenuItem sourceMenuItem = new JMenuItem("set as source (subject)",sourceIcon);
            	
            	sourceMenuItem.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						if(Display.instance.is_generating_memory_store){
						    JOptionPane.showMessageDialog(Display.iframeNet, "Memory Stores generation in progress...",  "Wait!", JOptionPane.NO_OPTION);
                			return;
						}
						Display.updateProgressComponent(-1, "");
						if(Display.instance.selfAgentName !=null && !Display.instance.selfAgentName.equals(pickV.id)){
							Display.instance.is_new_principal = true;
							
							Display.instance.listPathsData = new String[0];
							Display.pathsListModel.removeAllElements();
							Display.instance.selectedAgentPaths = null;
							Display.prPanel.removeAll();
							
						}
						else{
							Display.instance.is_new_principal = false;
						}
						Display.instance.sourceAgentName = pickV.id;
						Display.instance.subjectName = pickV.id;
						Display.instance.selfAgentName = pickV.id;
									
						PSatClient.netSerialiseConfigInstance();
						
						Thread queryThread = new Thread() {
							public void run() {
								if(Display.instance.is_dynamic_memory_store){
									//Do nothing, memory stores will be generated based Display.listPathsData
								}
								else{
									int noagents = PSatClient.netGetNoAgents();
									boolean valuesSet = Display.configPercentagePossibleWorldsAndNoAgentsRangeDisplay();
									if(!valuesSet){
									Display.instance. maxNoOfknowAgents = noagents-1;
									Display.instance.minNoOfknowAgents = noagents-1;
									Display.instance.noOfKnownAgentsGenerator = new Random();
								}
								Display.instance.noMemoryStores = 0;	
								PSatClient.netSerialiseConfigInstance();
								PSatClient.netNewMemoryStore();	
								}
								
								//generate paths list
								if(Display.instance.targetAgentName !=null && Display.instance.targetAgentName.length() !=0){
									if(Display.instance.sourceAgentName !=null && Display.instance.sourceAgentName.length() !=0){
										Display.updatePathsList();										
									}
								}
						
								Display.updateNetworkNode();
								Display.updateProgressComponent(100, "");
							}
						};
						queryThread.start();
					}            		
            	});
            	popup.add(sourceMenuItem);
            	
            	ImageIcon targetIcon = new ImageIcon(getClass().getResource("/target.png"));
            	JMenuItem targetMenuItem = new JMenuItem("set as target",targetIcon);
            	if(Display.instance.is_aspect_run){
            		targetMenuItem.setEnabled(false);
           		}
           		else{
           			targetMenuItem.setEnabled(true);
           		}
            	targetMenuItem.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent e) {
						if(Display.instance.is_generating_memory_store){
						    JOptionPane.showMessageDialog(Display.iframeNet, "Memory Stores generation in progress...",  "Wait!", JOptionPane.NO_OPTION);
                			return;
						}
						Display.instance.targetAgentName = pickV.id;
						PSatClient.netSerialiseConfigInstance();

//						if(Display.targetAgentName !=null && Display.targetAgentName.length() !=0){
//							if(Display.sourceAgentName !=null && Display.sourceAgentName.length() !=0){
//								Thread queryThread = new Thread() {
//									public void run() {
//										Display.updateProgressComponent(-1,"");
//										if(Display.targetAgentName != null && !Display.targetAgentName.equals(pickV.id)){
//											Display.is_new_target = true;
//										}
//										else{
//											Display.is_new_target = false;
//										}
//
//									}
//								};
//								queryThread.start();
//							}
//						}
						Display.updateProgressComponent(-1,"");
						if(Display.instance.targetAgentName != null && Display.instance.targetAgentName.equals(pickV.id)){
							Display.instance.is_new_target = true;
							
							Display.instance.listPathsData = new String[0];
							Display.pathsListModel.removeAllElements();
							Display.instance.selectedAgentPaths = null;
							Display.prPanel.removeAll();
							
							Display.updatePathsList();

							
							if(Display.instance.listPathsData.length == 0){
								Display.instance.targetAgentName = "";
							}
							
							Display.updateNetworkNode();

						}
						else{
							Display.instance.is_new_target = false;
						}
						
						PSatClient.netSerialiseConfigInstance();
						Display.updateProgressComponent(100, "");

					}            		
            	});
            	popup.add(targetMenuItem);
            	               
//            	//ImageIcon targetIcon = new ImageIcon("img/target.png");
//           		JMenu targetMenu = new JMenu("select paths target");
//           		if(Display.sourceAgentName == null){
//           			targetMenu.setEnabled(false);
//           		}
//           		targetMenu.setIcon(targetIcon);
//           		
//           		if(Display.sourceAgentName != null && !Display.sourceAgentName.equals(pickV.id)){
//               		//ImageIcon errorIcon = new ImageIcon("img/error.png");
//					JLabel l5 = new JLabel("<only source agents (subject) can set path targets>");
//					//l5.setIcon(errorIcon);
//	           		//l5.setEnabled(false);
//	           		l5.setForeground(Color.red);
//	           		targetMenu.add(l5);
//				}
//           		
//           		else{
//           			//ImageIcon settingsIcon = new ImageIcon("img/settings.png");
//           			ImageIcon settingsIcon = new ImageIcon(getClass().getResource("/settings.png"));
//					JLabel l5 = new JLabel("Possible Info. Targets");
//					l5.setIcon(settingsIcon);
//					l5.setEnabled(false);
//	           		targetMenu.add(l5);
//	           			           		
//           			for(final String otherAgent: AgentFactory.getOtherAgentNames(pickV.id)){
//           				JMenuItem otherPwMenu = new JMenuItem(otherAgent);               			
//           				otherPwMenu.addActionListener(new ActionListener(){
//           					
//        					public void actionPerformed(ActionEvent e) {
//        						Display.is_aspect_run = false;
//        						Display.targetAgentName = otherAgent;
//        						Config.serialiseLastTargetAgentName();	
//        						Config.serialiseConfigInstance();
//        						
//        						if(Display.targetAgentName !=null && Display.targetAgentName.length() !=0){
//        							if(Display.sourceAgentName !=null && Display.sourceAgentName.length() !=0){
//        								Thread queryThread = new Thread() {
//        									public void run() {
//        										Display.updateProgressComponent(-1,"");
//        										if(Display.targetAgentName != null && !Display.targetAgentName.equals(pickV.id)){
//        											Display.is_new_target = true;
//        										}
//        										else{
//        											Display.is_new_target = false;
//        										}
//        										Display.updatePathsList();
//        									}
//        								};
//        								queryThread.start();
//        								
////        								Display.pathButton.setEnabled(true);
////        								Display.listbox.setBackground(new Color(255,255,240));
//        							}
//        						}
////        						Display.runButton.setEnabled(false);
////        						Display.pwsButton.setEnabled(false);
////        						Display.menuItemGenpws.setEnabled(false);
////        						Display.menuItemDelete.setEnabled(false);
////        						Display.pwsinactiveButton.setEnabled(false);
//        						Display.updateNetworkNode();
//        					}            		
//                   		});
//           				targetMenu.add(otherPwMenu); 
//           			}
//           		}
//           		popup.add(targetMenu);
           		
           		//if(Display.selectedAgentPath != null){
           		if(Display.instance.sourceAgentName !=null){
	        		//for(Object ename:Display.selectedAgentPath){
           			for(String ename:PSatClient.netGetAgentNames()){
	            		//String name = ename.toString();
	                	//if(pickV.id.toString().equals(name)){
           				if(pickV.id.toString().equals(ename)){
           					paMenu.setEnabled(true);
//	                		pwsMenuItem.setEnabled(true);
	                		break;
	                	}
           				paMenu.setEnabled(false);
//	            		pwsMenuItem.setEnabled(false);
	            	}
            	}
            	else{            		
            		paMenu.setEnabled(false);
//            		pwsMenuItem.setEnabled(false);
            	}
           		if(Display.instance.sourceAgentName !=null && Display.instance.targetAgentName != null){
        			Display.activateRun(true);	
        		}
               
           		popup.show(vv, e.getX(), e.getY());
            }            
        }        
    }
}