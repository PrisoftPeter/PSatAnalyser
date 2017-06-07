//TODO: switch axioms 3 for 2 and vice versa.

package gla.prisoft.client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.swing.JToolBar;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;

import gla.prisoft.client.kernel.behaviour.assertions.ClientAssertionsFactory;
import gla.prisoft.client.kernel.behaviour.protocol.ClientProtocolFactory;
import gla.prisoft.client.kernel.display.model.AssertionsView;
import gla.prisoft.client.kernel.display.model.ClientKNetworkGraph;
import gla.prisoft.client.kernel.display.model.FeasibilityView;
import gla.prisoft.client.kernel.display.model.KProgress;
import gla.prisoft.client.kernel.display.model.LayeredBarChart;
import gla.prisoft.client.kernel.display.model.PopupGraphMousePlugin;
import gla.prisoft.client.kernel.display.model.ProtocolView;
import gla.prisoft.client.kernel.display.model.RecommendationPanel;
import gla.prisoft.client.kernel.display.util.DropDownButton;
import gla.prisoft.client.kernel.display.util.EppsteinPowerLawSettings;
import gla.prisoft.client.kernel.display.util.KleinbergSmallWorldSettings;
import gla.prisoft.client.kernel.display.util.PreferentialAttachmentSettings;
import gla.prisoft.client.session.ClientConfig;
import gla.prisoft.shared.Agent;
import gla.prisoft.shared.CombinationStrategy;
import gla.prisoft.shared.ConfigInstance;
import gla.prisoft.shared.NetworkType;

import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;


public class Display extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static JLabel tempProgress;
	private static KProgress progressPanel;
	private static Container pane_bottom;
	private JPanel logPanel;
	public static JTabbedPane logProTabbedPane;
//	public static JTabbedPaneWithoutTabs reqTabbedPane;	

	private static JTextPane logTextPane;
	private static JPanel netPanel;
	public static JPanel prPanel;
	private static JPanel protocolPanel;
	
	public static JButton startTrainMaxAnalysisButton;
//	public static JButton pauseAnalysisButton;
//	public static JButton finiteAnalysisButton;
//	public static JButton startMaxAnalysisButton;
//	public static TimeSeriesChart trainMaxTimeSeriesChart;
//	public static TimeSeriesChart analysisMaxTimeSeriesChart;
	
	public static FeasibilityView feasibilityView;
	
	private JScrollPane scrollPane_left;
	public static JList<String> listbox;
	@SuppressWarnings("rawtypes")
	public static DefaultListModel pathsListModel;
	
	public static  Display window;
	
	JDesktopPane jdpDesktop;
	static int openFrameCount = 0;

    public static ConfigInstance instance;
	
	public static ClientKNetworkGraph ckgraph;
	
	public static String hostname;
	public static boolean loadingsession;
	public static ArrayList<String> sessions;
	public static String sessionids_store_file_path = "localsessionidsstore";
	
	public static boolean graphloaded ;
	
	private static JButton loadButton;
	private static DropDownButton dropDownLoadButton;
	private static boolean forcenewsession;
	
	public static boolean goalchange;
	public static boolean opgset;
	public static boolean iscommonknowledgecheck;
	
	public static int noiterations;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {					
					window = new Display(null, false);
					window.setVisible(true);
					window.setTitle("PSat: Privacy requirements Satisfaction Analysis on Information-Flow Paths("+instance.sessionid+")");								
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Display(String sessionid, boolean reloading) {
		super("");

		try {
			UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}

		int inset = 20;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(inset, inset, screenSize.width - inset * 2,
				screenSize.height - inset * 2);
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		jdpDesktop = new JDesktopPane(){
			private static final long serialVersionUID = 1L;

			@Override
		    protected void paintComponent(Graphics g) {
		        super.paintComponent(g);
		        g.setColor(new Color(225,232,232));
		        g.fillRect(0, 0, getWidth(), getHeight());
		    }
		};

		if(!reloading){
			initialize(sessionid);			
		}
		createFrames();
		setContentPane(jdpDesktop);
		jdpDesktop.putClientProperty("JDesktopPane.dragMode", "outline");
		
		updateProtocolPage();
		
		addWindowListener(new WindowAdapter(){
		    @Override
		    public void windowDeactivated(WindowEvent e) {
		    	//ClientResponseBroker.close();
//		    	if(Display.externalViewMode && barChart !=null){
//    				if(barChart.barChartStandAlone !=null){
//    					barChart.barChartStandAlone.close();
//    				}
//    	    	}
		    }
		});

	}
	
	private void createFrames(){
		createFrameToolBar();
		createFramePathsList();
		createFrameNetworkPage();
		createFrameLogProTabPages();
//		createFrameReqPages();
	}
	
	public class InternalFrame extends JInternalFrame {
		private static final long serialVersionUID = 1L;
		static final int xPosition = 30, yPosition = 30;
		public InternalFrame(String frameTitle, int width, int height,boolean resizable,
							boolean closable, boolean maximizable, boolean iconfiable,
							int xfactor, int yfactor) {			
			super(frameTitle, //title
					resizable, // resizable
					closable, // closable
					maximizable, // maximizable
					iconfiable);// iconifiable
			setSize(width, height);
			// Set the window's location.
//			setLocation(xPosition * openFrameCount*xfactor, yPosition * openFrameCount*yfactor);
			setLocation(xPosition *xfactor, yPosition *yfactor);
			//setUndecorated(true);
			++openFrameCount;
		}
	}
	
	private static InternalFrame iframeToolBar;
	private void createFrameToolBar(){
		iframeToolBar = new InternalFrame("Toolbar",570,57,true,false,false,false,0,0);
		
		JToolBar toolBarPanel1 = new JToolBar();
		toolBarPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		////
		JPopupMenu pptiesPopupMenu = new JPopupMenu();		 
		
		ImageIcon newSessionIcon = new ImageIcon("img/database2.png");
		JMenuItem menuNewSession = new JMenuItem("New Session",newSessionIcon);
		menuNewSession.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//check server if generating memory store
				if(instance.is_generating_memory_store){
				    JOptionPane.showMessageDialog(Display.iframeNet, "Memory Stores generation in progress...",  "Wait!", JOptionPane.NO_OPTION);

					return;
				}
				reinitialise();
				Display.graphloaded = false;

				window.setVisible(false);
				Display.ckgraph = null;
				loadingsession = true;
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							forcenewsession = true;
							window = new Display(null, false);
							window.setVisible(true);
							window.setTitle("PSat: Privacy requirements Satisfaction Analysis on Information-Flow Paths("+instance.sessionid+")");								
							Display.updatePathsList();
							loadingsession = false;

							if(ClientKNetworkGraph.g != null && ClientKNetworkGraph.g.getVertexCount()>0){
						    	loadButton.setEnabled(false);
						    	dropDownLoadButton.setEnabled(false);
						    }
							else{
								loadButton.setEnabled(true);
						    	dropDownLoadButton.setEnabled(true);
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}			
		});		
		pptiesPopupMenu.add(menuNewSession);
		 
		// all existing sessions
		ImageIcon datastoreNatIcon = new ImageIcon("img/database.png");
		JMenu menuItemDatastore = new JMenu("Existing Sessions");
		menuItemDatastore.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				reinitialise();
//				updateProgressComponent(-1,"loading");
			}			
		});

		for(String session: sessions){
			final JMenuItem menu_sessions = new JMenuItem(session,datastoreNatIcon);
			menu_sessions.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					if(instance.is_generating_memory_store){
					    JOptionPane.showMessageDialog(Display.iframeNet, "Memory Stores generation in progress...",  "Wait!", JOptionPane.NO_OPTION);

						return;
					}
					Display.graphloaded = false;

					final String selectedSession = menu_sessions.getText();//e.get//get sessionid from menu_sessions
					window.setVisible(false);
					Display.ckgraph = null;
					loadingsession = true;
					instance = PSatClient.netGetSession(selectedSession);
					instance.runningTraining = false;
					instance.stop = false;
					PSatClient.netSerialiseConfigInstance();

					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								window = new Display(selectedSession, false);
								window.setVisible(true);
								window.setTitle("PSat: Privacy requirements Satisfaction Analysis on Information-Flow Paths("+instance.sessionid+")");								
								Display.updatePathsList();
								loadingsession = false;
								
								if(ClientKNetworkGraph.g != null && ClientKNetworkGraph.g.getVertexCount()>0){
							    	loadButton.setEnabled(false);
							    	dropDownLoadButton.setEnabled(false);
							    }
								else{
									loadButton.setEnabled(true);
							    	dropDownLoadButton.setEnabled(true);
								}

							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}			
			});
			menuItemDatastore.add(menu_sessions);
		}
		pptiesPopupMenu.add(menuItemDatastore);	

		ImageIcon settingsIcon = new ImageIcon(getClass().getResource("/settings.png"));
		JMenuItem menuItemppties = new JMenuItem("Setup",settingsIcon);
		menuItemppties.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				//check server if generating memory store
				if(instance.is_generating_memory_store){
				    JOptionPane.showMessageDialog(Display.iframeNet, "Memory Stores generation in progress...",  "Wait!", JOptionPane.NO_OPTION);

					return;
				}
				configProperties();
			}			
		});		
		pptiesPopupMenu.add(menuItemppties);
		
		ImageIcon aboutIcon = new ImageIcon(getClass().getResource("/about.png"));
		JMenuItem menuItemAbout = new JMenuItem("About...",aboutIcon);
		menuItemAbout.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Thread queryThread = new Thread() {
					public void run() {
						ImageIcon prisoftIcon = new ImageIcon(getClass().getResource("/prisoft.png"));

						String s = "Version: (1.0.2) \n"+
								   "PSat is a Platform for analying the satisfaction of privacy requirements along information-flow paths.\n"+
								   "Developed at PriSoft Lab, School of Computing Science, University of Glasgow.\n"+
								   "All rights reserved.";
						
						JOptionPane.showMessageDialog(iframeToolBar,s," ", JOptionPane.PLAIN_MESSAGE,prisoftIcon);
						
					}
				};
				queryThread.start();
			}			
		});
		pptiesPopupMenu.add(menuItemAbout);
		
		JButton aPetButton = new JButton("PSat");
		aPetButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		DropDownButton dropDownPSatButton = new DropDownButton(aPetButton, pptiesPopupMenu);
		toolBarPanel1.add(dropDownPSatButton);

		JSeparator apetseparator = new JSeparator(SwingConstants.VERTICAL);
		Dimension apetmaximumSize = apetseparator.getPreferredSize();
		apetmaximumSize.height = aPetButton.getPreferredSize().height;
		apetseparator.setPreferredSize(apetmaximumSize);
		toolBarPanel1.add(apetseparator);
				
		////
		JPopupMenu loadPopupMenu = new JPopupMenu();	
		JMenu singlePathMenuItem = new JMenu("Single-Path Flows");
		JMenu multiPathMenuItem= new JMenu("Multi-Path Flows");
		multiPathMenuItem.setEnabled(false); //remove for next version
					
		//ImageIcon customNatIcon = new ImageIcon("img/treest.png");
		ImageIcon customNatIcon = new ImageIcon(getClass().getResource("/genpws.png"));
//		ImageIcon customNatIcon = new ImageIcon(getClass().getResource("/treest.png"));
		JMenuItem menuItemCustom = new JMenuItem("Custom File(.gml,.graphML)",customNatIcon);
		menuItemCustom.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				reinitialise();
				updateProgressComponent(-1,"loading");
				
				PSatClient.netEmptySerialisedContent();
				//selected_source_config_path = null;
				
//				datastore_file_path = DatastoreChooser.show("Save datastore as", false);
				instance.networkType = NetworkType.CUSTOM;
								
				PSatClient.netSerialiseConfigInstance();
				ckgraph.createNetworkFromGmlOrGraphML();
				
				updateNetworkPage();
				runGraphStatistics();
				updateProgressComponent(100,"");

			}			
		});		
		multiPathMenuItem.add(menuItemCustom);	
		
		//ImageIcon randomNatIcon = new ImageIcon("img/random.png");
		ImageIcon randomNatIcon = new ImageIcon(getClass().getResource("/random.png"));
		JMenuItem menuItemRandom = new JMenuItem("Create Random",randomNatIcon);
		menuItemRandom.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				reinitialise();
				updateProgressComponent(-1,"loading");
				
				PSatClient.netEmptySerialisedContent();
				//selected_source_config_path = null;
				
//				datastore_file_path = DatastoreChooser.show("Save datastore as", false);
				instance.networkType = NetworkType.RANDOM;
				
				PSatClient.netSerialiseConfigInstance();
//				ckgraph.createRandomGraph();
				
				PSatClient.netAutoGenAgents();	
				updateNetworkPage();
				runGraphStatistics();
				updateProgressComponent(100,"");
			}			
		});
		multiPathMenuItem.add(menuItemRandom);
				
		
		JMenuItem menuItemPreferentialAttachment = new JMenuItem("Preferential attachment");
		menuItemPreferentialAttachment.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				reinitialise();
				updateProgressComponent(-1,"loading");
				
				PSatClient.netEmptySerialisedContent();
				//selected_source_config_path = null;
				
//				datastore_file_path = DatastoreChooser.show("Save datastore as", false);
				instance.networkType = NetworkType.BARABASIALBERT;
				
				PreferentialAttachmentSettings.configure();
				PSatClient.netAutoGenAgents();
//				ckgraph.createBarabasiAlbertGraph(instance.init_no_seeds,
//												  instance.numEdgesToAttach,
//												  instance.no_agents,
//												  instance.no_iterations);
				updateNetworkPage();
				runGraphStatistics();
				updateProgressComponent(100,"");
			}			
		});
		multiPathMenuItem.add(menuItemPreferentialAttachment);

		JMenuItem menuItemSmallWorld = new JMenuItem("Small World");
		menuItemSmallWorld.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				reinitialise();
				updateProgressComponent(-1,"loading");
				
				PSatClient.netEmptySerialisedContent();
				//selected_source_config_path = null;
				
//				datastore_file_path = DatastoreChooser.show("Save datastore as", false);
				instance.networkType = NetworkType.KLEINBERGSMALLWORLD;
								
				KleinbergSmallWorldSettings.configure();
				PSatClient.netAutoGenAgents();				
						 
				updateNetworkPage();
				runGraphStatistics();
				updateProgressComponent(100,"");
			}			
		});
		multiPathMenuItem.add(menuItemSmallWorld);
		
		//ImageIcon plNatIcon = new ImageIcon("img/net2.png");
		ImageIcon plNatIcon = new ImageIcon(getClass().getResource("/net2.png"));
		JMenuItem menuItemPowerLaw = new JMenuItem("power-law",plNatIcon);
		menuItemPowerLaw.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				reinitialise();
				updateProgressComponent(-1,"loading");
				
				PSatClient.netEmptySerialisedContent();
				//selected_source_config_path = null;
				
				//datastore_file_path = DatastoreChooser.show("Save datastore as", false);
				instance.networkType = NetworkType.EPPESTEINPOWERLAW;
								
				EppsteinPowerLawSettings.configure();
				PSatClient.netAutoGenAgents();
				updateNetworkPage();
				runGraphStatistics();
				updateProgressComponent(100,"");
			}			
		});
		multiPathMenuItem.add(menuItemPowerLaw);	

		JMenuItem menuItemNodesOnly = new JMenuItem("Create Nodes Only");
		menuItemNodesOnly.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				reinitialise();
				updateProgressComponent(-1,"loading");
				
				PSatClient.netEmptySerialisedContent();
				//selected_source_config_path = null;
				
//				datastore_file_path = DatastoreChooser.show("Save datastore as", false);
				instance.networkType = NetworkType.NODESONLY;

				PSatClient.netSerialiseConfigInstance();
				PSatClient.netAutoGenAgents();				
				updateNetworkPage();
				runGraphStatistics();
				updateProgressComponent(100,"");
			}			
		});
		multiPathMenuItem.add(menuItemNodesOnly);
					
		//ImageIcon sequentialNatIcon = new ImageIcon("img/sequence.png");
//		ImageIcon sequentialNatIcon = new ImageIcon(getClass().getResource("/sequence.png"));
		ImageIcon sequentialNatIcon = new ImageIcon(getClass().getResource("/path.png"));
		JMenuItem menuItemSequentialGraph = new JMenuItem("sequential graph",sequentialNatIcon);
		menuItemSequentialGraph.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				reinitialise();
				updateProgressComponent(-1,"loading");
				
				PSatClient.netEmptySerialisedContent();
				//selected_source_config_path = null;
				
//				datastore_file_path = DatastoreChooser.show("Save datastore as", false);
				instance.networkType = NetworkType.SEQUENTIAL;
								
				PSatClient.netSerialiseConfigInstance();
				PSatClient.netAutoGenAgents();		
				updateNetworkPage();
				setDefaultSourceTargetForSequenceGraph(); //
				runGraphStatistics();
				updateProgressComponent(100,"");
			}			
		});
		singlePathMenuItem.add(menuItemSequentialGraph);	
		
		
		loadPopupMenu.add(singlePathMenuItem);
		loadPopupMenu.add(multiPathMenuItem);
		
		
		//ImageIcon loadIcon = new ImageIcon("img/net.png");
		ImageIcon loadIcon = new ImageIcon(getClass().getResource("/net.png"));
		loadButton = new JButton("FlowPaths",loadIcon);
		loadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		//loadButton.setRolloverIcon(new ImageIcon("img/net2.png"));
		ImageIcon setRolloverIcon = new ImageIcon(getClass().getResource("/net2.png"));
		loadButton.setRolloverIcon(setRolloverIcon);
		loadButton.setRolloverEnabled(true);	      
	    dropDownLoadButton = new DropDownButton(loadButton, loadPopupMenu);
	   
	    toolBarPanel1.add(dropDownLoadButton);

	    JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
	    Dimension maximumSize = separator.getPreferredSize();
	    maximumSize.height = loadButton.getPreferredSize().height;
	    separator.setPreferredSize(maximumSize);
	    toolBarPanel1.add(separator);
	    		
		//ImageIcon runIcon = new ImageIcon("img/run.png");
		final ImageIcon startIcon = new ImageIcon(getClass().getResource("/starttraining.png"));
		final ImageIcon stopNormalIcon = new ImageIcon(getClass().getResource("/stopnormal.png"));
		startTrainMaxAnalysisButton = new JButton("",startIcon);
		
		String info1 ="<html>Run Satisfaction and Feasibility Analysis<br></html>";
		//"A feasibility disclosure protocol generates pr satisfaction equal or greater than specified privacy goal
		//for each information-flow on selected path(s)<br>"+
		//"i.e. max sat(pr)</html>";
		startTrainMaxAnalysisButton.setToolTipText(info1);
		

		if(instance.sourceAgentName ==null || instance.targetAgentName == null){
			startTrainMaxAnalysisButton.setEnabled(false);
		}
		
		startTrainMaxAnalysisButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(instance.runningTraining){
					String message = "Analysis is still running, are you sure you want to stop!";
					int option = JOptionPane.showConfirmDialog(iframeNet, message, "Stop", JOptionPane.YES_NO_OPTION);
		    	    
				    if (option == JOptionPane.YES_OPTION){
				    	instance.stop = true;
				    	PSatClient.netSerialiseConfigInstance();
				    	Display.instance.costTradeoff = 1;
				    	startTrainMaxAnalysisButton.setIcon(startIcon);
				    }
				}
				else{
					startTrainMaxAnalysisButton.setIcon(stopNormalIcon);
					Display.noiterations=1;
			    	Display.instance.costTradeoff = 1;

					runSatAnalysis();
					
			    	instance.stop = false;
			    	PSatClient.netSerialiseConfigInstance();
				}
			}			
		});	
		
		if(ClientKNetworkGraph.g != null && ClientKNetworkGraph.g.getVertexCount()>0){
	    	loadButton.setEnabled(false);
	    	dropDownLoadButton.setEnabled(false);
	    }
		else{
			loadButton.setEnabled(true);
	    	dropDownLoadButton.setEnabled(true);
		}
				
		toolBarPanel1.add(startTrainMaxAnalysisButton);

		JSeparator separatora = new JSeparator(SwingConstants.VERTICAL);
	    Dimension maximumSizea = separatora.getPreferredSize();
	    maximumSizea.height = loadButton.getPreferredSize().height;
	    separatora.setPreferredSize(maximumSizea);
	    toolBarPanel1.add(separatora);
		
		toolBarPanel1.setRollover(true);
		
		
		pane_bottom = new JPanel();
		pane_bottom.setPreferredSize(new Dimension(200, 30));
		pane_bottom.add(createProgressComponent(),BorderLayout.LINE_END);
		toolBarPanel1.add(pane_bottom);
		
		iframeToolBar.setContentPane(toolBarPanel1);
		iframeToolBar.setVisible(true);
		jdpDesktop.add(iframeToolBar);
		try {
			iframeToolBar.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
		}
	}
	
	
	public void runSatAnalysis(){		
		if(instance.is_generating_memory_store){
		    JOptionPane.showMessageDialog(Display.iframeNet, "Memory Stores generation in progress...",  "Wait!", JOptionPane.NO_OPTION);
		    return;
		}
		if(instance.evaluatedProtocols == null ||instance.evaluatedProtocols.length==0){
			JOptionPane.showMessageDialog(Display.iframeNet, "You need to select one or more disclosure protocol...",  "Disclosure Protocol", JOptionPane.NO_OPTION);
		    return;
		}
		
		PSatClient.netSerialiseConfigInstance();
				
		Thread queryThread = new Thread() {
			public void run() {					
				instance.noPaths = instance.selectedAgentPaths.size();
				instance.isTraining = true;
				instance.currentPrivacyGoal = new HashMap<String, Double>();
				if(instance.originalPrivacyGoal == null){
					instance.originalPrivacyGoal = new HashMap<String, Double>();							
				}
				
				PSatClient.netSerialiseConfigInstance();
				
//				startMaxAnalysisButton.setEnabled(false); 
				if(iframeDecisionBarView !=null){
//					iframeDecisionBarView.remove(decisionviewpanel);
					iframeDecisionBarView.setVisible(false);
				}
				if(feasibilityView == null){
					feasibilityView = new FeasibilityView(instance);
					createFeasibilityViewPage();
				}
				else{
					iframeFeasibilityView.remove(feasibilityView);
					iframeFeasibilityView.setVisible(false);
					feasibilityView = new FeasibilityView(instance);
					createFeasibilityViewPage();		        			
				}
				                			
				PSatClient.netAnalysePaths();
				
				PSatClient.netSerialiseConfigInstance();
				
			}
		};
		queryThread.start();
	}

	public static void activateRun(boolean status){		
		startTrainMaxAnalysisButton.setEnabled(status);
		
		try {
			iframeToolBar.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
		}
	}
	
	public static InternalFrame iframePathList;
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void createFramePathsList(){

		Thread queryThreadx = new Thread() {
			public void run() {
				iframePathList = new InternalFrame("Paths",239,330,true,false,false,true,0,2);
				
				pathsListModel = new DefaultListModel();
				
				listbox = new JList(pathsListModel);
				listbox.setFont(new Font("Verdana", Font.PLAIN, 10));
				listbox.setBackground(new Color(255,255,240));
				listbox.setCellRenderer(new PathsListCellRenderer());		
				listbox.addListSelectionListener(new ListSelectionListener(){

					public void valueChanged(ListSelectionEvent e) {
						if(instance.is_generating_memory_store){
						    JOptionPane.showMessageDialog(Display.iframeNet, "Memory Stores generation in progress...",  "Wait!", JOptionPane.NO_OPTION);
		        			return;
		        		}
						List<String> ll = listbox.getSelectedValuesList();
						ArrayList<String> saps = new ArrayList<String>();
						for(String s:ll){
							saps.add(s);
						}
						instance.selectedAgentPaths = saps;
						PSatClient.netSerialiseConfigInstance();

			            if(instance.selectedAgentPaths.size() >0){
			            	ClientKNetworkGraph.resetColoredLinks();
							ClientKNetworkGraph.resetColoredNodes();
							
							updateNetworkNode();		
							activateRun(true);
			            }
						
					}			
				});
				
				scrollPane_left = new JScrollPane(listbox);
				iframePathList.setContentPane(scrollPane_left);
				iframePathList.setVisible(true);
				jdpDesktop.add(iframePathList);
				try {
					iframePathList.setSelected(true);
				} catch (java.beans.PropertyVetoException e) {
				}
			}
		};
		queryThreadx.start(); 
		
				
	}
	
	public static InternalFrame iframeNet;
	private void createFrameNetworkPage(){
		iframeNet = new InternalFrame("Network",610,390,true,false,true,true,19,0);
				
		JComponent networkPane = createNetworkPage();
		
		iframeNet.setContentPane(networkPane);
		iframeNet.setVisible(true);
		jdpDesktop.add(iframeNet);
		try {
			iframeNet.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
		}
		
	}
	
	public static InternalFrame iframeLogProTabPages;
	private void createFrameLogProTabPages(){
		iframeLogProTabPages = new InternalFrame("Info.",1179,285, true,false,true,true,0,13);
		
		logProTabbedPane = new JTabbedPane();
		ImageIcon logIcon = new ImageIcon(getClass().getResource("/log.png"));
		logProTabbedPane.addTab("Log", logIcon, createLogPage());
		
		ImageIcon protocolIcon = new ImageIcon(getClass().getResource("/protocol.png"));
		logProTabbedPane.addTab("disclosure protocol suite", protocolIcon, createProtocolPage());
		
		ImageIcon privacyRequirementsIcon = new ImageIcon(getClass().getResource("/pa.png"));
		logProTabbedPane.addTab("privacy requirements", privacyRequirementsIcon, createAssertionsPage());
				
		iframeLogProTabPages.setContentPane(logProTabbedPane);
		iframeLogProTabPages.setVisible(true);
		jdpDesktop.add(iframeLogProTabPages);
		try {
			iframeLogProTabPages.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
		}
	}
	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String sessionid) {
		try{
			InetAddress addr = InetAddress.getLocalHost();
		    hostname = addr.getHostName();
		    loadingsession = true;
		    graphloaded = false;
		    
		    sessions = ClientConfig.deserialiseSessionIds();
		    if(sessions == null){
		    	sessions = new ArrayList<String>();
		    }
		    
		    if(sessionid == null){
		    	if(forcenewsession){
		    		instance = PSatClient.netGenNewSession();
					sessions.add(instance.sessionid);
					ClientConfig.serialiseSessionIds(sessions);
					PSatClient.netSerialiseConfigInstance();

					loadButton.setEnabled(true);
			    	dropDownLoadButton.setEnabled(true);
			    	
		    		forcenewsession = false;
		    	}
		    	else{
		    		JLabel opensession_l = new JLabel("open existing session:");
				    String[] sessionslist = new String[sessions.size()];
				    sessions.toArray(sessionslist);
				    final JComboBox<String> sessionslist_cb = new JComboBox<String>(sessionslist);
				    if(sessionslist.length>0){
					    sessionslist_cb.setSelectedIndex(0);
				    }		    
				    final JCheckBox newsession_cb= new JCheckBox("create new session");
				    newsession_cb.addActionListener(new ActionListener() {
				          public void actionPerformed(ActionEvent arg0) {
				        	if(newsession_cb.isSelected()){
				        		sessionslist_cb.setEnabled(false);
				  	    	}	
				  	    	else{
				        		sessionslist_cb.setEnabled(true);
				  	    	}
				          }          
					});
				   
				    Object[] sessionobjects = {newsession_cb,opensession_l,sessionslist_cb};
				    
				    int sessionoption = -1;
				    if(sessions.size() == 0){
				    	sessionoption = 1;
				    }
				    else{
						sessionoption= JOptionPane.showOptionDialog(null,sessionobjects,"Session Intialisation", JOptionPane.DEFAULT_OPTION,JOptionPane.DEFAULT_OPTION, null, new Object[]{"cancel", "ok"}, null);
				    }
				   
				   if(sessionoption==1){
					   if(newsession_cb.isSelected() ||(sessions.size() == 0)){
						   instance = PSatClient.netGenNewSession();
						   sessions.add(instance.sessionid);
						   ClientConfig.serialiseSessionIds(sessions);
						   PSatClient.netSerialiseConfigInstance();
					   }
					   else{
						   final String tsessionid = String.valueOf(sessionslist_cb.getSelectedItem());
						   instance = PSatClient.netGetSession(tsessionid);
						   instance.runningTraining = false;
						   instance.stop = false;
							
							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {
										window.setVisible(false);								
										window = new Display(tsessionid, true);
										window.setVisible(true);
										window.setTitle("PSat: Privacy requirements Satisfaction Analysis on Information-Flow Paths("+instance.sessionid+")");								
										Display.updatePathsList();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							});
					   }
				   }
				   else{
					  System.exit(0);
				   }	
		    	}		    		
		    }
		    else{
				  instance = PSatClient.netGetSession(sessionid);
		    }
		    
		   loadingsession = false;
			   
		   ckgraph = new ClientKNetworkGraph();
		   
		}
		catch (UnknownHostException ex){
		    System.out.println("Hostname can not be resolved");
		}
		
		//misc
//		protocolSuite = PathsInGraph.loadProtocolSuite();
		PSatClient.netDeserialiseProcessPossibleWorldsPathToFile(sessionid);
		if(instance.processedPossibleWorldsPaths == null){
			instance.processedPossibleWorldsPaths = new String[0];	
			PSatClient.netSerialiseConfigInstance();
		}		

	}
		
//    private static HashMap<String, Double> maxPathSats;
    private class PathsListCellRenderer extends DefaultListCellRenderer {
		private static final long serialVersionUID = 1L;
		
		@SuppressWarnings("rawtypes")
		@Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			
			
			String [] tempPaths = new String[0];
						
  			if(Display.isTresholdSlider){		
  				
        		for (Map.Entry<String, Double> entry :ClientKNetworkGraph.maxPathSats.entrySet()) {
        		    String path = entry.getKey();
        		    double pathsatvalue = entry.getValue();
        		    if(pathsatvalue >= Display.sliderValue){
        		    	String [] temp = new String[tempPaths.length+1];
        		    	for(int i=0;i<tempPaths.length;i++){
        		    		temp[i] = tempPaths[i];
        		    	}
        		    	temp[tempPaths.length] = path;
        		    	tempPaths = temp;
        		    }
        		} 
        		
        	}
  			else{
  				tempPaths = instance.processedPossibleWorldsPaths;
  			}
			
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            
            for(String p: tempPaths){
            	if(p.equals((String)value)) { 
            		if(Display.isTresholdSlider){
                        label.setBackground(new Color(111,191,240));  
            		}
            		else{
                        label.setBackground(new Color(226,225,213));              			
            		}
                }
            }
            
            return label;
        }
    }
	
	public JComponent createProgressComponent(){
		tempProgress = new JLabel("");		
		progressPanel = new KProgress();
		return progressPanel;
	}
	
	public static void updateProgressComponent(final int newvalue, final String info){
		
		SwingUtilities.invokeLater(new Runnable() {
	          public void run() {
	        	 
//	        	tempProgress.setText(info);
	        	tempProgress.setText("<html>&#x2600;"+info+"</html>");	        	

	        	Font font = new Font("Verdana", Font.ITALIC, 8);
	        	tempProgress.setFont(font);
	        	tempProgress.setForeground(Color.white);
	        	
	        	pane_bottom.revalidate();
	        	pane_bottom.repaint();
	        	
	        	progressPanel.setVisible(true);
	      		progressPanel.add(tempProgress);
	      		if(newvalue >0){
		        	progressPanel.updateBar(newvalue);
	      		}
	      		else{
	      			progressPanel.updateBar();	
	      		}
	      		
	      		if(newvalue ==100){
	      			try {
						java.lang.Thread.sleep(400);
						progressPanel.setVisible(false);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	      		}
	      		
	      		instance.completness = 0;
	      		PSatClient.netSerialiseConfigInstance();
	          }
	    });	
	}
	
	JPopupMenu clearpop;
	public JComponent createLogPage(){
		logPanel = new JPanel(new BorderLayout());
		
		logTextPane = new JTextPane();
		logTextPane.setContentType("text/html");
        EmptyBorder eb = new EmptyBorder(new Insets(10, 10, 10, 10));
        logTextPane.setBorder(eb);
        logTextPane.setMargin(new Insets(5, 5, 5, 5));
        logTextPane.setFont(new Font("Monospaced",Font.PLAIN,9));
		
        clearpop = new JPopupMenu();
        JMenuItem clearmi = new JMenuItem("Clear");
        clearmi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	clearLogPane(logTextPane);
            }

        }); 
        clearpop.add(clearmi);
        PopupMenuListener pml = new PopupMenuListener();
        logTextPane.addMouseListener(pml);
        
		JScrollPane scroll = new JScrollPane(logTextPane,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				
		scroll.setViewportView(logTextPane);
		logTextPane.setEditable(false);
		
		
		HTMLDocument doc1 = (HTMLDocument)logTextPane.getDocument();		
		HTMLEditorKit editorKit1 = (HTMLEditorKit)logTextPane.getEditorKit();
		
		 Font font = new Font("Verdana", Font.PLAIN, 9);
		    String bodyRule = "body { font-family: " + font.getFamily() + "; font-size: " + font.getSize() + "pt; }";
		    doc1.getStyleSheet().addRule(bodyRule);
		    
		try {
			editorKit1.insertHTML(doc1, doc1.getLength(),"", 0, 0, null);
		} 
		catch (BadLocationException e) {
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}				
		logPanel.add(scroll,BorderLayout.CENTER);	
		
		return logPanel;
	}
	class PopupMenuListener extends MouseAdapter {
       	public void mousePressed(MouseEvent me) {
            showPopup(me);
        }

        public void mouseReleased(MouseEvent me) {
            showPopup(me);
        }

        private void showPopup(MouseEvent me) {
            if (me.isPopupTrigger()) {
            	clearpop.show(me.getComponent(),
                               me.getX(), me.getY());
            }
        }
    }
	
	public static void clearLogPane(JTextPane tp){
		tp.setText("");
	}
	
	public static void appendToPane(JTextPane tp, String msg, boolean isError) {
		if(isError){
			msg = "<html><font color='red'>"+msg+"</font></html>";
		}
		else{
			msg = "<html>"+msg+"</html>";
		}
		HTMLDocument doc = (HTMLDocument)tp.getDocument();
		HTMLEditorKit editorKit = (HTMLEditorKit)tp.getEditorKit();
				
		try {
			editorKit.insertHTML(doc, doc.getLength(), msg, 0, 0, null);				
		}
		catch (BadLocationException e) {
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		int len = tp.getDocument().getLength();
		tp.setCaretPosition(len);
		//tp.setCharacterAttributes(aset, false);
		tp.replaceSelection(msg);
	}
	
	public static void updateLogPage(final String text, final boolean isError){
		 SwingUtilities.invokeLater(new Runnable() {
		        public void run() {
		        	try {
		    			iframeLogProTabPages.setSelected(true);
		    		} catch (java.beans.PropertyVetoException e) {
		    		}
		        	
					logProTabbedPane.setSelectedIndex(0);

		        	if(isError){
			    		appendToPane(logTextPane, text, isError);
		        	}
		        	else{
			    		appendToPane(logTextPane, text,isError);
		        	}	
		        	
		        	try {
		    			iframeLogProTabPages.setSelected(true);
		    		} catch (java.beans.PropertyVetoException e) {
		    		}
		        }
		    });	
	}
	
	
			
	public JComponent createProtocolPage(){
		protocolPanel = new JPanel(new BorderLayout(0,0));	
		
		ProtocolView pv = new ProtocolView();
		protocolPanel.add(pv,BorderLayout.CENTER);
		
		logProTabbedPane.revalidate();
		logProTabbedPane.repaint();
		logProTabbedPane.setSelectedIndex(0);	
		
		return protocolPanel;
	}
	
	public static void updateProtocolPage(){

		Thread queryThread = new Thread() {
			public void run() {
				try {
					iframeLogProTabPages.setSelected(true);
				} catch (java.beans.PropertyVetoException e) {
				}
				
				protocolPanel.removeAll();
				
				ProtocolView psView = new ProtocolView();
				
				protocolPanel.add(psView,BorderLayout.CENTER);

				ClientProtocolFactory.displayProtocols(psView);
			}
		};
		queryThread.start();
		
	}
	
	public JComponent createAssertionsPage(){
		prPanel = new JPanel(new BorderLayout(0,0));
		prPanel.setBackground(Color.white);
		return prPanel;
	}
	
	
	public static ClientAssertionsFactory afactory;
	public static AssertionsView ksView;
	public static void updateAssertionsPage(final String agentName,  final String command){
		if(Display.instance.is_generating_memory_store){
		    JOptionPane.showMessageDialog(Display.iframeNet, "Memory Stores generation in progress...",  "Wait!", JOptionPane.NO_OPTION);
			return;
		}
		Agent agent = PSatClient.netGetAgent(agentName);
		if(agent != null){
			SwingUtilities.invokeLater(new Runnable() {
		        public void run() {
//		        	try {
//		    			iframeReqPages.setSelected(true);
//		    		} catch (java.beans.PropertyVetoException e) {
//		    		}

					Display.updateProgressComponent(-1, "");
					
		        	if (command.equals("privacy requirement Instances")) {
		        		Thread queryThread = new Thread() {
							public void run() {
				    			logProTabbedPane.setSelectedIndex(2);
								
								afactory = new ClientAssertionsFactory(agentName);
								afactory.init();
								prPanel.removeAll();

								ksView = new AssertionsView(agentName);
								prPanel.add(ksView,BorderLayout.CENTER);

								logProTabbedPane.revalidate();
								logProTabbedPane.repaint();
																				
								afactory.displayAssertions(ksView);	

								Display.updateProgressComponent(100, "");
							}
						};
						queryThread.start();
					} 	
		        	else if (command.equals("privacy requirement Aspects")) {
		        		Thread queryThread = new Thread() {
							public void run() {
								
								afactory = new ClientAssertionsFactory(agentName);
								afactory.init();
								prPanel.removeAll();

								ksView = new AssertionsView(agentName);
								prPanel.add(ksView);

								logProTabbedPane.revalidate();
								logProTabbedPane.repaint();
																
								afactory.displayAssertions(ksView);	
								
								Display.updateProgressComponent(100, "");
							}
						};
						queryThread.start();
		        	}
		        }
		    });		 
		}
		
	}
		
	
	public JComponent createNetworkPage(){
		netPanel = new JPanel();
		PSatClient.netAgentFactoryInitGraph();
		ckgraph.show(netPanel);				

		netPanel.repaint();

		try {
			iframeNet.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
		}
				
		return netPanel;
	}
		
	public static void updateNetworkPage(){
		
		if(instance.is_generating_memory_store){
		    JOptionPane.showMessageDialog(Display.iframeNet, "Memory Stores generation in progress...",  "Wait!", JOptionPane.NO_OPTION);
			return;
		}
		Thread queryThread = new Thread() {
			public void run() {
				netPanel.removeAll();
				ClientKNetworkGraph.resetColoredLinks();
				ClientKNetworkGraph.resetColoredNodes();
				ckgraph.show(netPanel);
				netPanel.repaint();

				try {
					iframeNet.setSelected(true);
				} catch (java.beans.PropertyVetoException e) {
				}
				
				if(ClientKNetworkGraph.g != null && ClientKNetworkGraph.g.getVertexCount()>0){
			    	loadButton.setEnabled(false);
			    	dropDownLoadButton.setEnabled(false);
			    }
				else{
					loadButton.setEnabled(true);
			    	dropDownLoadButton.setEnabled(true);
				}
			}
		};
		queryThread.start();
		
	}
	public static void updateNetworkNode(){
		Display.isTresholdSlider = false;

		if(instance.is_generating_memory_store){
		    JOptionPane.showMessageDialog(Display.iframeNet, "Memory Stores generation in progress...",  "Wait!", JOptionPane.NO_OPTION);
			return;
		}
		Thread queryThread = new Thread() {
			public void run() {
				ClientKNetworkGraph.resetColoredLinks();
				ClientKNetworkGraph.resetColoredNodes();
				ckgraph.show(netPanel);
				netPanel.repaint();
				
				try {
					iframeNet.setSelected(true);
	    		} catch (java.beans.PropertyVetoException e) {
	    		}
			}
		};
		queryThread.start();		
	}
	
	public static boolean isTresholdSlider = false;
	public static double sliderValue;
	public static void updateTresholdNetworkNode(){

		ClientKNetworkGraph.resetColoredLinks();
		ClientKNetworkGraph.resetColoredNodes();
		ckgraph.show(netPanel);
		netPanel.repaint();
		
		try {
			iframeNet.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
		}
	}
	
	public static void repaintListbox(){
		listbox.setBackground(new Color(255,255,240));
		listbox.revalidate();
		listbox.repaint();
	}
	
	private static void setDefaultSourceTargetForSequenceGraph(){
//		if(instance.is_aspect_run){
			Properties sequencesourcetarget = PSatClient.netFindSequenceSourceandTarget();
			
			Display.instance.is_new_principal = true;
			Display.instance.is_new_target = true;
			
			Display.instance.listPathsData = new String[0];
			Display.pathsListModel.removeAllElements();
			Display.instance.selectedAgentPaths = null;
			Display.prPanel.removeAll();
						
			Display.instance.sourceAgentName = sequencesourcetarget.getProperty("source");
			Display.instance.subjectName = sequencesourcetarget.getProperty("source");
			Display.instance.selfAgentName = sequencesourcetarget.getProperty("source");
			Display.instance.targetAgentName = sequencesourcetarget.getProperty("target");
						
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
//		}
		
	}
	public static void updatePathsList(){
		
		if(instance.is_generating_memory_store){
		    JOptionPane.showMessageDialog(Display.iframeNet, "Memory Stores generation in progress...",  "Wait!", JOptionPane.NO_OPTION);

			return;
		}
		if(instance.is_aspect_run){
			instance.listPathsData = PSatClient.netFindKNearestneighbours();
			instance.selectedAgentPaths = new ArrayList<String>();
			for(String s:instance.listPathsData){
				instance.selectedAgentPaths.add(s);
			}
		}
		else{
			instance.listPathsData = PSatClient.netGetPaths(); 
			instance.selectedAgentPaths = new ArrayList<String>();
			for(String s:instance.listPathsData){
				instance.selectedAgentPaths.add(s);
			}
		}	
		if(instance.listPathsData.length == 0){
			Display.updateLogPage("no path matching setup distance of source from target(s)", true);
		}

		SwingUtilities.invokeLater(new Runnable() {
            @SuppressWarnings("unchecked")

            public void run() {
            	activateRun(false);	
            	pathsListModel.removeAllElements();
        		//selectedAgentPaths = new ArrayList<String>();	
            	
        		ArrayList<String> al = new ArrayList<String>();
            	for (String path: instance.listPathsData) {
                	pathsListModel.addElement(path);
                	al.add(path);
                }
            	instance.selectedAgentPaths = al;
            	PSatClient.netSerialiseConfigInstance();
            	
				listbox.setBackground(Color.WHITE);
				listbox.revalidate();
				listbox.repaint();

				
				Thread queryThread = new Thread() {
					public void run() {
						if(instance.is_dynamic_memory_store && (instance.is_new_principal || instance.is_new_target)){
							Thread queryThreadx = new Thread() {
								public void run() {
									PSatClient.netNewMemoryStore();	//for dynamic memory store generation
									instance.old_k = instance.k;
									PSatClient.netSerialiseConfigInstance();
									Display.updateProgressComponent(100, "");

								}
							};
							queryThreadx.start();
							
						}
						else if(PopupGraphMousePlugin.isrequirements){
							Thread queryThreadx = new Thread() {
								public void run() {
									PSatClient.netNewMemoryStore();	//for dynamic memory store generation
									PopupGraphMousePlugin.isrequirements = false;	
									
									instance.old_k = instance.k;
									PSatClient.netSerialiseConfigInstance();
								}
							};
							queryThreadx.start();
																
						}
						else if(instance.k != instance.old_k){
							Thread queryThreadx = new Thread() {
								public void run() {
									PSatClient.netNewMemoryStore();	//for dynamic memory store generation
									instance.old_k = instance.k;
									PSatClient.netSerialiseConfigInstance();
								}
							};
							queryThreadx.start();
							
						}
						
						if(!instance.is_aspect_run){
							if(instance.sourceAgentName !=null && instance.targetAgentName != null){
								activateRun(true);	
							}	
						}
						else{
							if(instance.selfAgentName !=null && instance.k >0){
								Display.activateRun(true);	
							}
						}
					}
				};
				queryThread.start();										
			}
        });	
				
		try {
			iframePathList.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
		}
		
	}
	
	private static InternalFrame iframeFeasibilityView;
	private void createFeasibilityViewPage(){
		decisionBarcreated = false;
		
		if(iframeDecisionBarView !=null){
			try {
				iframeDecisionBarView.setClosed(true);
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}
		
		if(iframeFeasibilityView == null){
			iframeFeasibilityView = new InternalFrame("SAT/Feasibility",jdpDesktop.getWidth()-10,620,true,false,true,true,8,4);
			jdpDesktop.add(iframeFeasibilityView);
		}
		iframeFeasibilityView.repaint();
		
//		iframePlots.add(barChart);	
		iframeFeasibilityView.add(feasibilityView);	
		
		if(!instance.externalViewMode){
			iframeFeasibilityView.setVisible(true);
			try {
				iframeFeasibilityView.setSelected(true);
			} catch (java.beans.PropertyVetoException e) {
			}
		}	
	}
	

	private InternalFrame iframeDecisionBarView;
	private JPanel decisionviewpanel;
	public boolean decisionBarcreated;
	private RecommendationPanel recommendationPanel;
	public void createLayeredDecisionBarViewPage(LayeredBarChart layeredbarchart){		
		if(iframeDecisionBarView == null ||iframeDecisionBarView.isClosed()){
			iframeDecisionBarView = new InternalFrame("Path Protocol Feasibiity Rankings",1170, 590,true,true,true,true,8,4);
			jdpDesktop.add(iframeDecisionBarView);	
		}	
		iframeDecisionBarView.repaint();
		
		decisionviewpanel = new JPanel();

		decisionviewpanel.setLayout(new BoxLayout(decisionviewpanel,BoxLayout.Y_AXIS));
		decisionviewpanel.setBackground(Color.WHITE);		

		String prdesc = FeasibilityView.reqDesc;
		prdesc= prdesc.replace("</html>", " ");
		prdesc= prdesc.replace("</body>", " ");
		prdesc = prdesc+"</body></html>";
		
		JLabel pr = new JLabel(prdesc);
		decisionviewpanel.add(pr);
		
		JPanel panel1 = new JPanel();
		panel1.setBackground(Color.WHITE);
		panel1.add(layeredbarchart);
		
		if(!instance.externalViewMode){
			iframeDecisionBarView.setVisible(true);
			try {
				iframeDecisionBarView.setSelected(true);
			} catch (java.beans.PropertyVetoException e) {
			}
		}
		decisionviewpanel.add(panel1);
		
		recommendationPanel = new RecommendationPanel(layeredbarchart);	
		recommendationPanel.setPreferredSize(new Dimension(1000, 160));
		decisionviewpanel.add(recommendationPanel);
		
		iframeDecisionBarView.add(decisionviewpanel);
		
		iframeDecisionBarView.setVisible(true);

				
		final ImageIcon startIcon = new ImageIcon(getClass().getResource("/starttraining.png"));
		startTrainMaxAnalysisButton.setIcon(startIcon);
		
		decisionBarcreated = true;
	}

	
	
	public static void configProperties(){
		JLabel label0 = new JLabel("Log Mode:");
		label0.setForeground(new Color(54,133,47));
		final JCheckBox ktransformCheckbox= new JCheckBox("Log object knowledge transformations");
		if(instance.log_knowledge_transformation){
			ktransformCheckbox.setSelected(true);
		}
		else{
			ktransformCheckbox.setSelected(false);
		}
		ktransformCheckbox.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent arg0) {
	        	if(ktransformCheckbox.isSelected()){
	        		instance.log_knowledge_transformation = true;
	  	    	}	
	  	    	else{
	  	    		instance.log_knowledge_transformation = false;
	  	    	}
	        	PSatClient.netSerialiseConfigInstance();
	          }          
			});
		
	    
	    final JCheckBox agentKStateCheckbox= new JCheckBox("Log object knowledge state");
	    if(instance.log_agent_knowledge_state){
	    	agentKStateCheckbox.setSelected(true);
	    }
	    else{
	    	agentKStateCheckbox.setSelected(false);
	    }
	    agentKStateCheckbox.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent arg0) {
	        	if(agentKStateCheckbox.isSelected()){
	        		instance.log_agent_knowledge_state = true;
	  	    	}	
	  	    	else{
	  	    		instance.log_agent_knowledge_state = false;
	  	    	}
    	    	PSatClient.netSerialiseConfigInstance();

	          }          
			});
	    
	    
	    JLabel label = new JLabel("Privacy Requirements:");
	    label.setForeground(new Color(54,133,47));
	    JLabel labeltype = new JLabel("Type");
	    JRadioButton instance_rb = new JRadioButton("privacy instance");
	    instance_rb.addActionListener(new ActionListener() {	    	 

	    	public void actionPerformed(ActionEvent event) {
	        	instance.is_aspect_run = false;
	        	instance.listPathsData = new String[0];
				Display.pathsListModel.removeAllElements();
				instance.selectedAgentPaths = null;
				Display.prPanel.removeAll();
				instance.sourceAgentName = null;
				instance.subjectName = null;
				instance.selfAgentName = null;
				instance.targetAgentName =null;
				PSatClient.netSerialiseConfigInstance();
				
				Display.updatePathsList();
				Display.updateNetworkNode();
				
    	    	PSatClient.netSerialiseConfigInstance();
    	    	Display.updateProgressComponent(100, "");
	        }
	    });
        JRadioButton aspects_rb = new JRadioButton("privacy aspects");
        aspects_rb.addActionListener(new ActionListener() {	    	 

	        public void actionPerformed(ActionEvent event) {
	        	instance.is_aspect_run = true;
	        	instance.listPathsData = new String[0];
				Display.pathsListModel.removeAllElements();
				instance.selectedAgentPaths = null;
				Display.prPanel.removeAll();
				instance.sourceAgentName = null;
				instance.subjectName = null;
				instance.selfAgentName = null;
				instance.targetAgentName =null;
				PSatClient.netSerialiseConfigInstance();

				Display.updatePathsList();
				Display.updateNetworkNode();	
				Display.updateProgressComponent(100, "");
				
	        }
	    });
        if(instance.is_aspect_run){
        	aspects_rb.setSelected(true);
        }
        else{
        	instance_rb.setSelected(true);
        }
        ButtonGroup analysis_g_rb = new ButtonGroup();
        analysis_g_rb.add(instance_rb);
        analysis_g_rb.add(aspects_rb); 
        
        JLabel labelmode = new JLabel("Mode");
	    label.setForeground(new Color(54,133,47));
	    JRadioButton pick_rb = new JRadioButton("Pick belief/uncertainty elements");
	    pick_rb.addActionListener(new ActionListener() {	    	 

	    	public void actionPerformed(ActionEvent event) {
	        	instance.isModePick = true;
	        	instance.isModeUncertainty = false;
	        	instance.isModeEntropy = false;
	        	instance.isModeCommonKnowledge = false;
				PSatClient.netSerialiseConfigInstance();

	        }
	    });
        JRadioButton uncertainty_rb = new JRadioButton("Regulate belief/uncertainty levels");
        
        uncertainty_rb.addActionListener(new ActionListener() {	    	 

        	public void actionPerformed(ActionEvent event) {
	        	instance.isModePick = false;
	        	instance.isModeUncertainty = true;
	        	instance.isModeEntropy = false;
	        	instance.isModeCommonKnowledge = false;
				PSatClient.netSerialiseConfigInstance();
	        }
	    });
        JRadioButton entropy_rb = new JRadioButton("Regulate entropy levels");
        
        entropy_rb.addActionListener(new ActionListener() {	    	 

        	public void actionPerformed(ActionEvent event) {
	        	instance.isModePick = false;
	        	instance.isModeUncertainty = false;
	        	instance.isModeEntropy = true;
	        	instance.isModeCommonKnowledge = false;
				PSatClient.netSerialiseConfigInstance();
	        }
	    });
        JRadioButton ck_rb = new JRadioButton("Regulate common knowledge");
        
        ck_rb.addActionListener(new ActionListener() {	    	 

        	public void actionPerformed(ActionEvent event) {
	        	instance.isModePick = false;
	        	instance.isModeUncertainty = false;
	        	instance.isModeEntropy = false;
	        	instance.isModeCommonKnowledge = true;
				PSatClient.netSerialiseConfigInstance();
	        }
	    });
        
        if(instance.isModePick){
        	pick_rb.setSelected(true);
        	uncertainty_rb.setSelected(false);
        	entropy_rb.setSelected(false);
        	ck_rb.setSelected(false);
        }
        else if(instance.isModeUncertainty){
        	pick_rb.setSelected(false);
        	uncertainty_rb.setSelected(true);
        	entropy_rb.setSelected(false);
        	ck_rb.setSelected(false);
        }
        else if(instance.isModeEntropy){
        	pick_rb.setSelected(false);
        	uncertainty_rb.setSelected(false);
        	entropy_rb.setSelected(true);
        	ck_rb.setSelected(false);
        }
        else if(instance.isModeCommonKnowledge){
        	pick_rb.setSelected(false);
        	uncertainty_rb.setSelected(false);
        	entropy_rb.setSelected(false);
        	ck_rb.setSelected(true);
        }
        else{
        	pick_rb.setSelected(true);
        	uncertainty_rb.setSelected(false);
        	entropy_rb.setSelected(false);
        	ck_rb.setSelected(false);
        }
        ButtonGroup pick_g_rb = new ButtonGroup();
        pick_g_rb.add(pick_rb);
        pick_g_rb.add(uncertainty_rb);
        pick_g_rb.add(entropy_rb);
        pick_g_rb.add(ck_rb); 
                
        JSeparator sep = new JSeparator(SwingConstants.HORIZONTAL);
        JSeparator sep2 = new JSeparator(SwingConstants.HORIZONTAL);
//        JSeparator sep3 = new JSeparator(SwingConstants.HORIZONTAL);
//        JSeparator sep4 = new JSeparator(SwingConstants.HORIZONTAL);
        
        JLabel label2 = new JLabel("Analysis:");
        label2.setForeground(new Color(54,133,47));
        
	    Integer maxPathLengths[] = {1,2,3,4,5,6,7,8,9};
	    final JComboBox<Integer> maxPathLength_cb = new JComboBox<Integer>(maxPathLengths);
	    for(int i=0;i<maxPathLengths.length;i++){
	    	if(maxPathLengths[i] == instance.max_analysis_path_length){
	    		maxPathLength_cb.setSelectedIndex(i);		
	    	}
	    }	    
	    maxPathLength_cb.addItemListener(new ItemListener() {
	        public void itemStateChanged(ItemEvent itemEvent) {
	        	instance.max_analysis_path_length = (Integer)maxPathLength_cb.getSelectedItem();
	        	instance.no_agents = instance.max_analysis_path_length;//remove to distinguish bw no of agents and path length
	        	instance.k = instance.max_analysis_path_length;//remove to distinguish bw no of agents and path length
	        	PSatClient.netSerialiseConfigInstance();
	          }
	        });
	    
	    Integer maxNo_path_Analysis[] = {2,3,5,10,20,30,40,50,50,70,80,90,100,120,140,160,200};
	    final JComboBox<Integer> max_No_path_Analysis_cb = new JComboBox<Integer>(maxNo_path_Analysis);
	    for(int i=0;i<maxNo_path_Analysis.length;i++){
	    	if(maxNo_path_Analysis[i] == instance.max_no_analysis_paths){
	    		max_No_path_Analysis_cb.setSelectedIndex(i);		
	    	}
	    }
	    max_No_path_Analysis_cb.addItemListener(new ItemListener() {
	        public void itemStateChanged(ItemEvent itemEvent) {
	        	instance.max_no_analysis_paths = (Integer)max_No_path_Analysis_cb.getSelectedItem();
	        	PSatClient.netSerialiseConfigInstance();

	          }
	        });
	    
	    Double sat_tresholds[] = {0.0,0.1,0.2,0.3,0.4,0.5,0.6,0.7,0.8,0.9,1.0};
	    final JComboBox<Double> satTreshold_cb = new JComboBox<Double>(sat_tresholds);
	    for(int i=0;i<sat_tresholds.length;i++){
	    	if(sat_tresholds[i] == instance.sat_treshold){
	    		satTreshold_cb.setSelectedIndex(i);		
	    	}
	    }	    
	    satTreshold_cb.addItemListener(new ItemListener() {
	        public void itemStateChanged(ItemEvent itemEvent) {
	        	instance.sat_treshold = (Double)satTreshold_cb.getSelectedItem();
	        	PSatClient.netSerialiseConfigInstance();

	          }
	        });
	    
	    final JCheckBox unlimitedps_cb= new JCheckBox("Complete path analysis");
	    if(instance.unlimitedPathSatAnalysis){
	    	unlimitedps_cb.setSelected(true);
	    	satTreshold_cb.setEnabled(false);
	    }
	    else{
	    	unlimitedps_cb.setSelected(false);
	    	satTreshold_cb.setEnabled(true);
	    }
	    unlimitedps_cb.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent arg0) {
	        	if(unlimitedps_cb.isSelected()){
	        		instance.unlimitedPathSatAnalysis = true;
	        		satTreshold_cb.setEnabled(false);
	  	    	}	
	  	    	else{
	  	    		instance.unlimitedPathSatAnalysis = false;
	  	    		satTreshold_cb.setEnabled(true);
	  	    	}
	        	PSatClient.netSerialiseConfigInstance();
	          }          
			});
	     	    
	    
	    JLabel labelStragegy = new JLabel("Uncertainities combination strategy");
	    labelStragegy.setForeground(new Color(54,133,47));
	    
	    JRadioButton minimum_rb = new JRadioButton("minimum uncertainty value");
	    minimum_rb.addActionListener(new ActionListener() {	    	 
	        public void actionPerformed(ActionEvent event) {
	        	instance.combinationStrategy = CombinationStrategy.MINIMUM;
	        	PSatClient.netSerialiseConfigInstance();
	        }
	    });
	    
	    JRadioButton maximum_rb = new JRadioButton("maximum uncertainty value");
	    maximum_rb.setSelected(true);
        maximum_rb.addActionListener(new ActionListener() {	    	 
	        public void actionPerformed(ActionEvent event) {
	        	instance.combinationStrategy = CombinationStrategy.MAXIMUM;
	        	PSatClient.netSerialiseConfigInstance();

	        }
	    });
        
        JRadioButton average_rb = new JRadioButton("average uncertainty value");        
        average_rb.addActionListener(new ActionListener() {	    	 
	        public void actionPerformed(ActionEvent event) {
	        	instance.combinationStrategy = CombinationStrategy.AVERAGE;
	        	PSatClient.netSerialiseConfigInstance();
	        }
	    });
	    
        ButtonGroup combination_g_rb = new ButtonGroup();
        combination_g_rb.add(minimum_rb);
        combination_g_rb.add(maximum_rb);
        combination_g_rb.add(average_rb); 
	        
	    
	    JLabel label01 = new JLabel("Network Mutation Mode:");
	    label01.setForeground(new Color(54,133,47));
		
	    final JCheckBox networkMutationMode_cb= new JCheckBox("add/remove edges");
	    if(instance.networkMutationMode){
	    	networkMutationMode_cb.setSelected(true);
	    }
	    else{
	    	networkMutationMode_cb.setSelected(false);
	    }
	    networkMutationMode_cb.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent arg0) {
	        	if(networkMutationMode_cb.isSelected()){
	        		instance.networkMutationMode = true;	  	    		
	  	    	}	
	  	    	else{
	  	    		instance.networkMutationMode = false;
	  	    	}
        		PSatClient.netSerialiseConfigInstance();

	          }          
		});
	    
//	    JLabel noagents_l = new JLabel("# of objects:");
//	    final JTextField nagents_ft = new JTextField(10);
//	    if(instance != null){
//		    nagents_ft.setText(""+instance.no_agents);
//	    }
//	    else{
//	    	nagents_ft.setText("5");	
//	    }
//	    nagents_ft.addActionListener(new ActionListener(){
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				String tinput = nagents_ft.getText();
//				if ((tinput != null) && (tinput.length() > 0) && Display.isNumeric(tinput)) {
//					Display.instance.no_agents = new Integer(tinput);	
//					PSatClient.netSerialiseConfigInstance();
//				}
//				else{
//					System.err.println("#number expected");
//					return;
//				}
//			}
//	    	
//	    });
	    
//	    JLabel tradeoff_l = new JLabel("Cost Tradeoff:");
//	    final JTextField tradeofftf = new JTextField(10);
//	    if(instance != null){
//	    	if(instance.costTradeoff == 0.01){
//	    		tradeofftf.setText("0");
//	    	}
//	    	else{
//		    	tradeofftf.setText(""+instance.costTradeoff);	    		
//	    	}
//	    }
//	    else{
//	    	tradeofftf.setText("1.0");	
//	    }
//	    tradeofftf.addActionListener(new ActionListener(){
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				String tinput = tradeofftf.getText();
//				if ((tinput != null) && (tinput.length() > 0) && Display.isNumeric(tinput)) {
//					if(new Double(tinput) >1){
//						System.err.println("#number <=1 expected");
//					}
//					else{
//						if(new Double(tinput) <0.01){
//							Display.instance.costTradeoff = 0.01;
//						}
//						else{
//							Display.instance.costTradeoff = new Double(tinput);							
//						}
//						PSatClient.netSerialiseConfigInstance();
//					}					
//				}
//				else{
//					System.err.println("#number <=1 expected");
//					return;
//				}
//			}	    	
//	    });
	    
	    JLabel decisioncategory_l = new JLabel("Decision Category Limit:");
        
//	    String decisioncategories[] = {"cat6","cat5", "cat4", "cat3", "cat2", "cat1"};
	    String decisioncategories[] = {"cat3", "cat2", "cat1"};
	    final JComboBox<String> decisioncategories_cb = new JComboBox<String>(decisioncategories);
	    
//	    if(RecommendationPanel.limit ==1){
//	    	decisioncategories_cb.setSelectedIndex(5);
//        }
//        else if(RecommendationPanel.limit ==2){
//        	decisioncategories_cb.setSelectedIndex(4);
//        }
//        else if(RecommendationPanel.limit ==3){
//        	decisioncategories_cb.setSelectedIndex(3);
//        }
//        else if(RecommendationPanel.limit ==4){
//        	decisioncategories_cb.setSelectedIndex(2);
//        }
//        else if(RecommendationPanel.limit ==5){
//        	decisioncategories_cb.setSelectedIndex(1);
//        }
//        else if(RecommendationPanel.limit ==6){
//        	decisioncategories_cb.setSelectedIndex(0);
//        } 
	    if(RecommendationPanel.limit ==1){
	    	decisioncategories_cb.setSelectedIndex(2);
        }
        else if(RecommendationPanel.limit ==2){
        	decisioncategories_cb.setSelectedIndex(1);
        }
        else if(RecommendationPanel.limit ==3){
        	decisioncategories_cb.setSelectedIndex(0);
        }
	    decisioncategories_cb.addItemListener(new ItemListener() {
	        public void itemStateChanged(ItemEvent itemEvent) {
	        	String cat = (String)decisioncategories_cb.getSelectedItem();
	        	if(cat.equals("cat1")){
	        		RecommendationPanel.limit=1;
	            }
	            else if(cat.equals("cat2")){
	        		RecommendationPanel.limit=2;
	            }
	            else if(cat.equals("cat3")){
	        		RecommendationPanel.limit=3;
	            }
//	            else if(cat.equals("cat4")){
//	        		RecommendationPanel.limit=4;
//	            }
//	            else if(cat.equals("cat5")){
//	        		RecommendationPanel.limit=5;
//	            }
//	            else if(cat.equals("cat6")){
//	        		RecommendationPanel.limit=6;
//	            }
	        }
	    });
	    
	    
//	    Object[] message = {noagents_l,nagents_ft,label, labeltype, aspects_rb,instance_rb, labelmode,pick_rb,entropy_rb,uncertainty_rb,
//	    					sep,label0, ktransformCheckbox, viabledpCheckbox,agentKStateCheckbox,entropyBeliefUncertaintyCheckbox,sep2,
//	    					"","", label2, "Max. length of path",maxPathLength_cb,
//	    					"Max. no of paths",max_No_path_Analysis_cb,"limited path analysis(set pathsat threshold)",satTreshold_cb, unlimitedps_cb,
//	    					sep3,label3,request_cb,consent_cb,notice_cb,label01, networkMutationMode_cb};
	    
//	    Object[] message = {noagents_l,nagents_ft,label, labeltype, aspects_rb,instance_rb, labelmode,pick_rb,entropy_rb,uncertainty_rb,
//				sep,label0, ktransformCheckbox, viabledpCheckbox,agentKStateCheckbox,entropyBeliefUncertaintyCheckbox,sep2,
//				"","", label2, "Max. length of path",maxPathLength_cb,
//				"Max. no of paths",max_No_path_Analysis_cb,"limited path analysis(set pathsat threshold)",satTreshold_cb, unlimitedps_cb,
//				sep4, labelStragegy,minimum_rb,maximum_rb,average_rb,sep3,label3,request_cb,consent_cb,notice_cb,label01, networkMutationMode_cb};	  
	    
//	    Object[] message = {noagents_l,nagents_ft,label, labeltype, aspects_rb,instance_rb, labelmode,pick_rb,entropy_rb,uncertainty_rb,
//				sep,label0, ktransformCheckbox, viabledpCheckbox,agentKStateCheckbox,entropyBeliefUncertaintyCheckbox,sep2,
//				"","", label2, "Max. length of path",maxPathLength_cb,
//				"Max. no of paths",max_No_path_Analysis_cb,"limited path analysis(set pathsat threshold)",satTreshold_cb, unlimitedps_cb,
//				sep4, labelStragegy,minimum_rb,maximum_rb,average_rb,sep3,label01, networkMutationMode_cb};	
	    
//	    Object[] message = {noagents_l,nagents_ft,label, labeltype, aspects_rb,instance_rb, labelmode,pick_rb,entropy_rb,ck_rb,uncertainty_rb,
//				sep,label0, ktransformCheckbox,agentKStateCheckbox,sep2,
//				"","", label2, "Max. no objects on a path",maxPathLength_cb,
//				"Max. no of paths",max_No_path_Analysis_cb,"limited path analysis(set pathsat threshold)",satTreshold_cb, unlimitedps_cb,
//				sep4, labelStragegy,minimum_rb,maximum_rb,average_rb,sep3,tradeoff_l,tradeofftf,label01, networkMutationMode_cb};	
	    	    
//	    Object[] message = {label, labeltype, aspects_rb,instance_rb, labelmode,pick_rb,entropy_rb,ck_rb,uncertainty_rb,
//				sep,label0, ktransformCheckbox,agentKStateCheckbox,sep2,
//				"","", label2, "Max. # of objects on a path",maxPathLength_cb,tradeoff_l,tradeofftf};	
	    Object[] message = {label, labeltype, aspects_rb,instance_rb, labelmode,pick_rb,entropy_rb,uncertainty_rb,
				sep,label0, ktransformCheckbox,agentKStateCheckbox,sep2,
				"","", label2, "Max. # of objects on a path",maxPathLength_cb,decisioncategory_l,decisioncategories_cb};	
	    
	    JOptionPane.showOptionDialog(iframeNet,message,"PSat configuration.", JOptionPane.DEFAULT_OPTION,JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);
	    
//	   int option= JOptionPane.showOptionDialog(iframeNet,message,"PSat configuration.", JOptionPane.DEFAULT_OPTION,JOptionPane.DEFAULT_OPTION, null, new Object[]{"cancel", "ok"}, null);
//	   if(option==0){
//		   
//	   }
//	   else{
//		   String no_agents_s = nagents_ft.getText();
//		   if ((no_agents_s != null) && (no_agents_s.length() > 0) && Display.isNumeric(no_agents_s)) {
//				Display.instance.no_agents = new Integer(no_agents_s);	
//				PSatClient.netSerialiseConfigInstance();
//			}
//			else{
//				JOptionPane.showMessageDialog(iframeNet,
//					    "# of nodes",
//					    "Error",
//					    JOptionPane.ERROR_MESSAGE);
//			}
//		   
//		   String trafeoff_s = tradeofftf.getText();
//			if ((trafeoff_s != null) && (trafeoff_s.length() > 0) && Display.isNumeric(trafeoff_s)) {
//				if(new Double(trafeoff_s) >1){
//					JOptionPane.showMessageDialog(iframeNet,
//							"#number <=1 expected",
//						    "Error",
//						    JOptionPane.ERROR_MESSAGE);
//				}
//				else{
//					if(new Double(trafeoff_s) <=0.01){
//						Display.instance.costTradeoff = 0.01;
//					}
//					else{
//						Display.instance.costTradeoff = new Double(trafeoff_s);							
//					}
//					PSatClient.netSerialiseConfigInstance();
//				}					
//			}
//			else{
//				JOptionPane.showMessageDialog(iframeNet,
//						"#number <=1 expected",
//					    "Error",
//					    JOptionPane.ERROR_MESSAGE);
//			}	
//	   }
	}
	 
	public static boolean configPercentagePossibleWorldsAndNoAgentsRangeDisplay(){
		if(instance.sourceAgentName == null){
			Display.updateLogPage("source agent not selected", true);
			return false;
		}
		
		int noagents = PSatClient.netGetNoAgents();
		
		JTextField maxNoOfknowAgents_t = new JTextField();
		maxNoOfknowAgents_t.setText(""+(noagents-1));
		
	    JTextField minNoOfknowAgents_t = new JTextField();
	    minNoOfknowAgents_t.setText(""+(noagents-1));
	    
	    JCheckBox propergateBelief= new JCheckBox("activate belief propergation for n <"+(noagents));

	    Object[] message = {
	        "Max no of other agents that an agent in the network knows(Range 0 ≤ n < "+(noagents)+")", maxNoOfknowAgents_t,
	        "Min no of other agents that an agent in the network knows(Range 0 ≤ n < "+(noagents)+")", minNoOfknowAgents_t,
	        " ", propergateBelief,
	    };
	    		
	    int option = JOptionPane.showConfirmDialog(iframeNet, message, "Memory Store config.", JOptionPane.OK_CANCEL_OPTION);
	    	    
	    if (option == JOptionPane.OK_OPTION)
	    {
	        String maxNoOfknowAgents_s = maxNoOfknowAgents_t.getText();
	        String minNoOfknowAgents_s = minNoOfknowAgents_t.getText();
	        
	        if(!isNumeric(maxNoOfknowAgents_s) ||!isNumeric(minNoOfknowAgents_s) 
	        		){
				Display.updateLogPage("Only numbers expected: process aborted", true);
				return false;
	        }
	        else{
	        	if(new Integer(maxNoOfknowAgents_s) >=noagents){
					Display.updateLogPage("Max no of known agents over limit: process aborted", true);
					return false;
	        	}
	        	else if(new Integer(maxNoOfknowAgents_s) <0){
					Display.updateLogPage("Max no of known agents out of range: process aborted", true);
					return false;
	        	}
	        	else if(new Integer(minNoOfknowAgents_s) <0){
					Display.updateLogPage("Min no of known agents below limit: process aborted", true);	    
					return false;
	        	}
	        	else if(new Integer(minNoOfknowAgents_s) >=noagents){
					Display.updateLogPage("Min no of known agents out of range: process aborted", true);	    
					return false;
	        	}
	        	else{
	    	        instance.maxNoOfknowAgents = new Integer(maxNoOfknowAgents_s) ;
	    	    	instance.minNoOfknowAgents = new Integer(minNoOfknowAgents_s);
	    	    	instance.noOfKnownAgentsGenerator = new Random();
	    	    	if(propergateBelief.isSelected()){
	    	    		instance.beliefReasoningActive = true;
	    	    		System.err.println("TODO: Belief propergation not yet implemented");
	    	    	}	    	    		
	    	    	PSatClient.netSerialiseConfigInstance();
	    	    	
	    	    	return true;
	        	}
	        }	        
	    }
		return false;
	}
	
	public static boolean isNumeric(String str) {
		if(str ==null){
			return false;
		}
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
	public static void reinitialise(){
		//datastore_file_path = null;
		instance.sourceAgentName = null;
		instance.targetAgentName = null;
		instance.selectedAgentPaths = null;
		instance.listPathsData = new String[0];
		pathsListModel.removeAllElements();
		if(instance.sourceAgentName ==null || instance.targetAgentName == null){
			activateRun(false);	
		}
		PSatClient.netSerialiseConfigInstance();
	}
	
	private static void runGraphStatistics(){
		
		Thread queryThread = new Thread() {
			public void run() {
				updateProgressComponent(-1,"");
								
				double averageClusteringCoefficient = (double) Math.round((PSatClient.netAverageClusteringCoefficient())*100)/100;
				double averageDistance = (double) Math.round((PSatClient.netAverageofAverageDistance())*100)/100;
				double averageDiameter = (double) Math.round((PSatClient.netDiameter())*100)/100;
				updateLogPage("Ave. clustering coefficient:"
								+RoundTo2Decimals(averageClusteringCoefficient)+
								", Ave distance:"+RoundTo2Decimals(averageDistance)+
								", Ave. diameter:"+RoundTo2Decimals(averageDiameter), false);
				updateProgressComponent(100,"");
			}
		};
		queryThread.start();
	}
	
	
	
	public static double RoundTo2Decimals(double val) {
		if(Double.isNaN(val)){
			return val;
		}
		double roundOff = Math.round(val * 100.0) / 100.0;
		return roundOff;
	}
	
	public static double RoundTo3Decimals(double val) {
		if(Double.isNaN(val)){
			return val;
		}
		double roundOff = Math.round(val * 1000.0) / 1000.0;
		return roundOff;
	}

}
