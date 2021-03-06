//TODO: switch axioms 3 for 2 and vice versa.

package psat;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;

import psat.behaviour.protocol.ProtocolFactory;
import psat.display.model.AssertionsView;
import psat.display.model.FeasibilityView;
import psat.display.model.KNetworkGraph;
import psat.display.model.KProgress;
import psat.display.model.LayeredBarChart;
import psat.display.model.PSatConfigView;
import psat.display.model.PopupGraphMousePlugin;
import psat.display.model.ProtocolView;
import psat.display.model.RecommendationPanel;
import psat.display.util.DropDownButton;
import psat.display.util.EppsteinPowerLawSettings;
import psat.display.util.KleinbergSmallWorldSettings;
import psat.display.util.PreferentialAttachmentSettings;
import psat.util.Agent;
import psat.util.AssertionsFactory;
import psat.util.Config;
import psat.util.NetworkType;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;


public class Display extends JFrame {

	private static final long serialVersionUID = 1L;

	private static JLabel tempProgress;
	private static KProgress progressPanel;
	private static Container pane_bottom;
	private JPanel logPanel;
	public static JTabbedPane logProTabbedPane;
	//	public static JTabbedPaneWithoutTabs reqTabbedPane;	

//	private static JTextPane logTextPane;
	
	private static JPanel netPanel;
	private static JPanel netMemoryPanel;
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

	public static String hostname;
	public static boolean loadingsession;
	public static ArrayList<String> sessions;
	public static String sessionids_store_file_path = "localsessionidsstore";

	public static boolean graphloaded ;

	private static JButton loadButton;
	private static DropDownButton dropDownLoadButton;

	public static boolean goalchange;
	public static boolean opgset;
	public static boolean iscommonknowledgecheck;

	public static int noiterations;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		launch();		
	}
	
	private static void launch() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window = new Display(false);
					window.setVisible(true);
					window.setTitle("PSat: Privacy requirements Satisfaction Analysis on Information-Flow Paths("+PSatAPI.instance.sessionid+")");								
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Display(boolean reloading) {
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
			initialize();			
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
		createFrameNetMemoryPage();
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
				if(PSatAPI.instance.busy){
					Display.updateLogPage("...", true);
					return;
				}
				window.setVisible(false);
				PSatAPI.instance =null;
				PSatAPI.forcenewsession = true;
				launch();
//				reinitialise();
//				Display.graphloaded = false;
//
//				window.setVisible(false);
//				PSatAPI.instance.kgraph = null;
//				loadingsession = true;
//				EventQueue.invokeLater(new Runnable() {
//					public void run() {
//						try {
//							forcenewsession = true;
//							window = new Display(false);
//							window.setVisible(true);
//							window.setTitle("PSat: Privacy requirements Satisfaction Analysis on Information-Flow Paths("+PSatAPI.instance.sessionid+")");								
//							Display.updatePathsList();
//							loadingsession = false;
//
//							if(PSatAPI.instance.g != null && PSatAPI.instance.g.getVertexCount()>0){
//								loadButton.setEnabled(false);
//								dropDownLoadButton.setEnabled(false);
//							}
//							else{
//								loadButton.setEnabled(true);
//								dropDownLoadButton.setEnabled(true);
//							}
//
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				});
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
					if(PSatAPI.instance.busy){
						Display.updateLogPage("...", true);
						return;
					}
					Display.graphloaded = false;

					final String selectedSession = menu_sessions.getText();//e.get//get sessionid from menu_sessions
					window.setVisible(false);
					PSatAPI.instance.kgraph = null;
					loadingsession = true;
					PSatAPI.instance = PSatAPI.netGetSession(selectedSession);
					PSatAPI.instance.sessionid = selectedSession;
					PSatAPI.instance.runningTraining = false;
					PSatAPI.instance.stop = false;
					PSatAPI.netSerialiseConfigInstance();

					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								window = new Display(false);
								window.setVisible(true);
								window.setTitle("PSat: Analysing the Satisfaction of Privacy requirements on Information-Flow Paths("+PSatAPI.instance.sessionid+")");								
								Display.updatePathsList();
								loadingsession = false;

								if(PSatAPI.instance.g != null && PSatAPI.instance.g.getVertexCount()>0){
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
				if(PSatAPI.instance.busy){
					Display.updateLogPage("...", true);
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

						String s = "Version: (1.2.0) \n"+
								"PSat is a design time platform for analying the satisfaction of privacy requirements along information-flow paths.";

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

				PSatAPI.netEmptySerialisedContent();
				//selected_source_config_path = null;

				//				datastore_file_path = DatastoreChooser.show("Save datastore as", false);
				PSatAPI.instance.networkType = NetworkType.CUSTOM;

				PSatAPI.netSerialiseConfigInstance();
				PSatAPI.instance.kgraph.createNetworkFromGmlOrGraphML();

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

				PSatAPI.netEmptySerialisedContent();
				//selected_source_config_path = null;

				//				datastore_file_path = DatastoreChooser.show("Save datastore as", false);
				PSatAPI.instance.networkType = NetworkType.RANDOM;

				PSatAPI.netSerialiseConfigInstance();
				//				ckgraph.createRandomGraph();

				PSatAPI.netAutoGenAgents();	
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

				PSatAPI.netEmptySerialisedContent();
				//selected_source_config_path = null;

				//				datastore_file_path = DatastoreChooser.show("Save datastore as", false);
				PSatAPI.instance.networkType = NetworkType.BARABASIALBERT;

				PreferentialAttachmentSettings.configure();
				PSatAPI.netAutoGenAgents();
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

				PSatAPI.netEmptySerialisedContent();
				//selected_source_config_path = null;

				//				datastore_file_path = DatastoreChooser.show("Save datastore as", false);
				PSatAPI.instance.networkType = NetworkType.KLEINBERGSMALLWORLD;

				KleinbergSmallWorldSettings.configure();
				PSatAPI.netAutoGenAgents();				

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

				PSatAPI.netEmptySerialisedContent();
				//selected_source_config_path = null;

				//datastore_file_path = DatastoreChooser.show("Save datastore as", false);
				PSatAPI.instance.networkType = NetworkType.EPPESTEINPOWERLAW;

				EppsteinPowerLawSettings.configure();
				PSatAPI.netAutoGenAgents();
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

				PSatAPI.netEmptySerialisedContent();
				//selected_source_config_path = null;

				//				datastore_file_path = DatastoreChooser.show("Save datastore as", false);
				PSatAPI.instance.networkType = NetworkType.NODESONLY;

				PSatAPI.netSerialiseConfigInstance();
				PSatAPI.netAutoGenAgents();				
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

				PSatAPI.netEmptySerialisedContent();
				//selected_source_config_path = null;

				//				datastore_file_path = DatastoreChooser.show("Save datastore as", false);
				PSatAPI.instance.networkType = NetworkType.SEQUENTIAL;

				PSatAPI.netSerialiseConfigInstance();
				PSatAPI.netAutoGenAgents();		
				updateNetworkPage();
				setDefaultSourceTargetForSequenceGraph(); //
				//loadPossibleSequences();
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


		if(PSatAPI.instance.sourceAgentName ==null || PSatAPI.instance.targetAgentName == null){
			startTrainMaxAnalysisButton.setEnabled(false);
		}

		startTrainMaxAnalysisButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(PSatAPI.instance.runningTraining){
					String message = "Analysis is still running, are you sure you want to stop!";
					int option = JOptionPane.showConfirmDialog(iframeNet, message, "Stop", JOptionPane.YES_NO_OPTION);

					if (option == JOptionPane.YES_OPTION){
						PSatAPI.instance.stop = true;
						PSatAPI.netSerialiseConfigInstance();
						PSatAPI.instance.costTradeoff = 1;
						startTrainMaxAnalysisButton.setIcon(startIcon);
					}
				}
				else{

					if(PSatAPI.instance.busy){
						Display.updateLogPage("...", true);
						return;
					}
					if(PSatAPI.instance.evaluatedProtocols == null ||PSatAPI.instance.evaluatedProtocols.length==0){
						JOptionPane.showMessageDialog(Display.iframeNet, "No disclosure protocol selected...",  "Disclosure Protocol", JOptionPane.NO_OPTION);
						return;
					}
					if(AssertionsFactory.getTotalNoOfAssertionsForAllAgents() ==0){
						JOptionPane.showMessageDialog(Display.iframeNet, "No privacy requirement specified...",  "Privacy Requirements", JOptionPane.NO_OPTION);
						return;
					}
					startTrainMaxAnalysisButton.setIcon(stopNormalIcon);
					Display.noiterations=1;
					PSatAPI.instance.costTradeoff = 1;

					runSatAnalysis();

					PSatAPI.instance.stop = false;
					PSatAPI.netSerialiseConfigInstance();
				}
			}			
		});	

		if(PSatAPI.instance.g != null && PSatAPI.instance.g.getVertexCount()>0){
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
		Thread queryThread = new Thread() {
			public void run() {		
//				PSatAPI.netSerialiseConfigInstance();
				final ImageIcon stopNormalIcon = new ImageIcon(getClass().getResource("/stopnormal.png"));
				startTrainMaxAnalysisButton.setIcon(stopNormalIcon);

				PSatAPI.instance.isTraining = true;
				PSatAPI.roleAssertionsPrinted = false;
				PSatAPI.instance.currentPrivacyGoal = new HashMap<String, Double>();
				if(PSatAPI.instance.originalPrivacyGoal == null){
					PSatAPI.instance.originalPrivacyGoal = new HashMap<String, Double>();							
				}

				PSatAPI.netSerialiseConfigInstance();

				if(iframeDecisionBarView !=null){
					iframeDecisionBarView.setVisible(false);
				}
				if(feasibilityView == null){
					feasibilityView = new FeasibilityView(PSatAPI.instance);
					createFeasibilityViewPage();
				}
				else{
					iframeFeasibilityView.remove(feasibilityView);
					iframeFeasibilityView.setVisible(false);
					feasibilityView = new FeasibilityView(PSatAPI.instance);
					createFeasibilityViewPage();		        			
				}

				PSatAPI.netAnalysePaths();
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
				listbox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				listbox.addListSelectionListener(new ListSelectionListener(){

					public void valueChanged(ListSelectionEvent e) {
						if(PSatAPI.instance.busy){
							Display.updateLogPage("processing Sequence:"+PSatAPI.instance.selectedPath, true);
							return;
						}
						//						
						Thread queryThread = new Thread() {
							public void run() {
								Display.updateProgressComponent(-1, "");
								PSatAPI.instance.busy = true;

								String selectedpath = listbox.getSelectedValue();
								if(selectedpath !=null && selectedpath.trim().length()>0){
									PSatAPI.instance.selectedPath = selectedpath;
									PSatAPI.netSerialiseConfigInstance();

									if(PSatAPI.instance.networkType == NetworkType.SEQUENTIAL){
										AssertionsFactory.clearAllAgentAssertions();
										PSatAPI.netSerialiseConfigInstance();

										//reload sequence graph for new path
										PSatAPI.netRegenerateSequence(selectedpath);
										createFrameNetworkPage();
									}
									else{
										KNetworkGraph.resetColoredLinks();
										KNetworkGraph.resetColoredNodes();

										updateNetworkNode();	
									}

									activateRun(true);				
								}

								Display.updateProgressComponent(100, "");
								PSatAPI.instance.busy = false;

							}
						};
						queryThread.start();
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
		if(iframeNet !=null){
			try {
				iframeNet.setClosed(true);
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}
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
	
	public static InternalFrame iframeMemory;
	private void createFrameNetMemoryPage(){
		if(iframeMemory !=null){
			try {
				iframeMemory.setClosed(true);
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}
		iframeMemory = new InternalFrame("NetMemory",600,390,true,false,true,true,15,14);

		JComponent memoryPane = createNetMemoryPage();

		iframeMemory.setContentPane(memoryPane);
		iframeMemory.setVisible(true);
		jdpDesktop.add(iframeMemory);
		try {
			iframeMemory.setSelected(true);
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
	private void initialize() {
		try{
			InetAddress addr = InetAddress.getLocalHost();
			hostname = addr.getHostName();
			loadingsession = true;
			graphloaded = false;

			sessions = Config.deserialiseSessionIds();
			if(sessions == null){
				sessions = new ArrayList<String>();
			}

			if(PSatAPI.instance == null || PSatAPI.instance.sessionid == null){//if(PSatAPI.instance.sessionid == null){
				if(PSatAPI.forcenewsession){
					PSatAPI.netGenNewSession();
					sessions.add(PSatAPI.instance.sessionid);
					Config.serialiseSessionIds(sessions);

					loadButton.setEnabled(true);
					dropDownLoadButton.setEnabled(true);

					PSatAPI.forcenewsession = false;
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
							PSatAPI.netGenNewSession();
							sessions.add(PSatAPI.instance.sessionid);
							Config.serialiseSessionIds(sessions);
							PSatAPI.netSerialiseConfigInstance();
						}
						else{
							final String tsessionid = String.valueOf(sessionslist_cb.getSelectedItem());
							PSatAPI.instance = PSatAPI.netGetSession(tsessionid);
							PSatAPI.instance.runningTraining = false;
							PSatAPI.instance.stop = false;

							EventQueue.invokeLater(new Runnable() {
								public void run() {
									try {
										window.setVisible(false);								
										window = new Display(true);
										window.setVisible(true);
										window.setTitle("PSat: Analysing the Satisfaction of Privacy requirements on Information-Flow Paths("+PSatAPI.instance.sessionid+")");								
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
				PSatAPI.instance = PSatAPI.netGetSession(PSatAPI.instance.sessionid);
			}

			loadingsession = false;

			PSatAPI.instance.kgraph = new KNetworkGraph();
			PSatAPI.instance.busy = false;

		}
		catch (UnknownHostException ex){
			System.out.println("Hostname can not be resolved");
		}

		//misc
		//		protocolSuite = PathsInGraph.loadProtocolSuite();
		PSatAPI.netDeserialiseProcessPossibleWorldsPathToFile();
		if(PSatAPI.instance.processedPossibleWorldsPaths == null){
			PSatAPI.instance.processedPossibleWorldsPaths = new String[0];	
			PSatAPI.netSerialiseConfigInstance();
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

				for (Map.Entry<String, Double> entry :KNetworkGraph.maxPathSats.entrySet()) {
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
				tempPaths = PSatAPI.instance.processedPossibleWorldsPaths;
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

				PSatAPI.instance.completness = 0;
				PSatAPI.netSerialiseConfigInstance();
			}
		});	
	}

	public RSyntaxTextArea logTextPane;
	public JComponent createLogPage(){
		logPanel = new JPanel(new BorderLayout());
		
		logTextPane = new RSyntaxTextArea(20, 60);
		logTextPane.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
		logTextPane.setCodeFoldingEnabled(true);
		RTextScrollPane sp = new RTextScrollPane(logTextPane);
		logTextPane.setEditable(false);

		logPanel.add(sp,BorderLayout.CENTER);	

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

	public void appendToPane( String msg, boolean isError) {
		if(isError){
			msg = msg+"[error]";
		}
		
		logTextPane.append(msg+"\n");
		
	}

	public static void updateLogPage(final String text, final boolean isError){
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					iframeLogProTabPages.setSelected(true);
				} catch (java.beans.PropertyVetoException e) {
				}

				logProTabbedPane.setSelectedIndex(0);

				if(text.contains("<html>")) {
					appendNetMemoryText(text);
				}
				else {

					if(isError){
						window.appendToPane(text, isError);
					}
					else{
						window.appendToPane(text,isError);
					}	

					try {
						iframeLogProTabPages.setSelected(true);
					} 
					catch (java.beans.PropertyVetoException e) {
					}
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

				ProtocolFactory.displayProtocols(psView);
			}
		};
		queryThread.start();

	}

	public JComponent createAssertionsPage(){
		prPanel = new JPanel(new BorderLayout(0,0));
		prPanel.setBackground(Color.white);
		return prPanel;
	}


	public static AssertionsFactory afactory;
	public static AssertionsView ksView;
	public static void updateAssertionsPage(final String agentName,  final String command){
		if(PSatAPI.instance.busy){
			Display.updateLogPage("...", true);
			return;
		}
		Agent agent = PSatAPI.netGetAgent(agentName);
		if(agent != null){
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					//		        	try {
					//		    			iframeReqPages.setSelected(true);
					//		    		} catch (java.beans.PropertyVetoException e) {
					//		    		}

					Display.updateProgressComponent(-1, "");

					if (command.equals("privacy requirement instances")) {
						Thread queryThread = new Thread() {
							public void run() {
								logProTabbedPane.setSelectedIndex(2);

								afactory = new AssertionsFactory(agentName);
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
					else if (command.equals("privacy requirement roles")) {
						Thread queryThread = new Thread() {
							public void run() {

								afactory = new AssertionsFactory(agentName);
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
	
	public void updateAssertionsOwnersEditor() {
		ksView.clearAssertionOwnersEditor();
		
		ArrayList<String> aowners = PSatAPI.retrieveAssertionOwners();
//		go through aowners and get his assertions as string
//		
//		for (Map.Entry<String, HashSet<String>> entry : instance.assertionOwners.entrySet()) {
//		    String assertion_temp = entry.getKey();
//		    HashSet<String> owners_temp = entry.getValue();
//		    if(assertion_temp.equals(assertion)){
//		    	owners = owners_temp;
//		    	break;
//		    }
//		}
	}


	public JComponent createNetworkPage(){
		netPanel = new JPanel();
		PSatAPI.netAgentFactoryInitGraph();
		PSatAPI.instance.kgraph.show(netPanel);				

		netPanel.repaint();

		try {
			iframeNet.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
		}

		return netPanel;
	}
	
	private static void appendNetMemoryText(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					HTMLDocument doc=(HTMLDocument) netMemoryEditor.getStyledDocument();

					// Insert the text
					String text_t = text;
					text_t = text_t.replace("<html>", "");
					text_t = text_t.replace("</html>", "");
					try {
						doc.insertAfterEnd(doc.getCharacterElement(doc.getLength()),text_t+"<br>");
					} catch (IOException e) {
						e.printStackTrace();
					}

					// Convert the new end location to view co-ordinates
					Rectangle r = netMemoryEditor.modelToView(doc.getLength());

					// Finally, scroll so that the new text is visible
					if (r != null) {
						netMemoryEditor.scrollRectToVisible(r);
					}
				} catch (BadLocationException e) {
					Display.updateLogPage("Failed to print to NetMemory: "+text, true);
				}			
			}
		});
		
	}
	  
	JPopupMenu clearpop;
	static JTextPane netMemoryEditor;
	public JComponent createNetMemoryPage(){
		netMemoryPanel = new JPanel();
		netMemoryPanel.setLayout(new BorderLayout());
		netMemoryPanel.setBackground(Color.white);
		
		netMemoryEditor = new JTextPane();
		netMemoryEditor.setSize(300,390);
		netMemoryEditor.setContentType("text/html");
		netMemoryEditor.setEditable(false);
		netMemoryEditor.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
		netMemoryEditor.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		
		clearpop = new JPopupMenu();
		JMenuItem clearmi = new JMenuItem("Clear");
		clearmi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearLogPane(netMemoryEditor);
			}

		}); 
		clearpop.add(clearmi);
		PopupMenuListener pml = new PopupMenuListener();
		netMemoryEditor.addMouseListener(pml);

		netMemoryPanel.add(new JScrollPane(netMemoryEditor));

		try {
			iframeMemory.setSelected(true);
		} catch (java.beans.PropertyVetoException e) {
		}
		netMemoryPanel.repaint();

		return netMemoryPanel;
	}
	
	public static void updateNetworkPage(){

		if(PSatAPI.instance.busy){
			Display.updateLogPage("...", true);
			return;
		}
		Thread queryThread = new Thread() {
			public void run() {
				netPanel.removeAll();
				KNetworkGraph.resetColoredLinks();
				KNetworkGraph.resetColoredNodes();
				PSatAPI.instance.kgraph.show(netPanel);
				netPanel.repaint();

				try {
					if(iframeNet !=null)
						iframeNet.setSelected(true);
				} catch (java.beans.PropertyVetoException e) {
				}

				if(PSatAPI.instance.g != null && PSatAPI.instance.g.getVertexCount()>0){
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

		if(PSatAPI.instance.busy){
			Display.updateLogPage("...", true);
			return;
		}
		Thread queryThread = new Thread() {
			public void run() {
				KNetworkGraph.resetColoredLinks();
				KNetworkGraph.resetColoredNodes();
				PSatAPI.instance.kgraph.show(netPanel);
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

		KNetworkGraph.resetColoredLinks();
		KNetworkGraph.resetColoredNodes();
		PSatAPI.instance.kgraph.show(netPanel);
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
		//		if(instance.is_role_run){
		Properties sequencesourcetarget = PSatAPI.netFindSequenceSourceandTarget();

		PSatAPI.instance.is_new_principal = true;
		PSatAPI.instance.is_new_target = true;

		PSatAPI.instance.listPathsData = new String[0];
		Display.pathsListModel.removeAllElements();
		PSatAPI.instance.selectedPath = null;
		Display.prPanel.removeAll();

		PSatAPI.instance.sourceAgentName = sequencesourcetarget.getProperty("source");
		PSatAPI.instance.subjectName = sequencesourcetarget.getProperty("source");
		PSatAPI.instance.selfAgentName = sequencesourcetarget.getProperty("source");
		PSatAPI.instance.targetAgentName = sequencesourcetarget.getProperty("target");

		PSatAPI.netSerialiseConfigInstance();

		Thread queryThread = new Thread() {
			public void run() {
				if(PSatAPI.instance.is_dynamic_memory_store){
					//Do nothing, memory stores will be generated based Display.listPathsData
				}
				else{
					int noagents = PSatAPI.netGetNoAgents();
					boolean valuesSet = Display.configPercentagePossibleWorldsAndNoAgentsRangeDisplay();
					if(!valuesSet){
						PSatAPI.instance. maxNoOfknowAgents = noagents-1;
						PSatAPI.instance.minNoOfknowAgents = noagents-1;
						PSatAPI.instance.noOfKnownAgentsGenerator = new Random();
					}
					PSatAPI.instance.noMemoryStores = 0;	
					PSatAPI.netSerialiseConfigInstance();
					PSatAPI.netNewMemoryStore();	
				}

				//generate paths list
				if(PSatAPI.instance.targetAgentName !=null && PSatAPI.instance.targetAgentName.length() !=0){
					if(PSatAPI.instance.sourceAgentName !=null && PSatAPI.instance.sourceAgentName.length() !=0){
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

		if(PSatAPI.instance.busy){
			Display.updateLogPage("...", true);
			return;
		}
		if(PSatAPI.instance.is_role_run){
			PSatAPI.instance.listPathsData = PSatAPI.netFindKNearestneighbours();
		}
		else{
			PSatAPI.instance.listPathsData = PSatAPI.netGetPaths(); 
		}	
		if(PSatAPI.instance.listPathsData.length == 0){
			if(PSatAPI.instance.g !=null)
				Display.updateLogPage("no path matching setup distance of source from target(s)", true);
		}

		SwingUtilities.invokeLater(new Runnable() {
			@SuppressWarnings("unchecked")

			public void run() {
				activateRun(false);	
				pathsListModel.removeAllElements();

				for (String path: PSatAPI.instance.listPathsData) {
					pathsListModel.addElement(path);
				}

				PSatAPI.netSerialiseConfigInstance();

				listbox.setBackground(Color.WHITE);
				listbox.revalidate();
				listbox.repaint();


				Thread queryThread = new Thread() {
					public void run() {
						if(PSatAPI.instance.is_dynamic_memory_store && (PSatAPI.instance.is_new_principal || PSatAPI.instance.is_new_target)){
							Thread queryThreadx = new Thread() {
								public void run() {
									PSatAPI.netNewMemoryStore();	//for dynamic memory store generation
									PSatAPI.instance.old_k = PSatAPI.instance.k;
									PSatAPI.netSerialiseConfigInstance();
									Display.updateProgressComponent(100, "");
								}
							};
							queryThreadx.start();

						}
						else if(PopupGraphMousePlugin.isrequirements){
							Thread queryThreadx = new Thread() {
								public void run() {
									PSatAPI.netNewMemoryStore();	//for dynamic memory store generation
									PopupGraphMousePlugin.isrequirements = false;	

									PSatAPI.instance.old_k = PSatAPI.instance.k;
									PSatAPI.netSerialiseConfigInstance();
								}
							};
							queryThreadx.start();

						}
						else if(PSatAPI.instance.k != PSatAPI.instance.old_k){
							Thread queryThreadx = new Thread() {
								public void run() {
									PSatAPI.netNewMemoryStore();	//for dynamic memory store generation
									PSatAPI.instance.old_k = PSatAPI.instance.k;
									PSatAPI.netSerialiseConfigInstance();
								}
							};
							queryThreadx.start();

						}

						if(!PSatAPI.instance.is_role_run){
							if(PSatAPI.instance.sourceAgentName !=null && PSatAPI.instance.targetAgentName != null){
								activateRun(true);	
							}	
						}
						else{
							if(PSatAPI.instance.selfAgentName !=null && PSatAPI.instance.k >0){
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

		if(!PSatAPI.instance.externalViewMode){
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
			iframeDecisionBarView = new InternalFrame("pr convergence outcome",1170, 590,true,true,true,true,8,4);
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

		if(!PSatAPI.instance.externalViewMode){
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

	private InternalFrame iframeConfigView;
	private JPanel configpanel;
	public void configProperties(){

		if(iframeConfigView == null ||iframeConfigView.isClosed()){
			iframeConfigView = new InternalFrame("PSat Configuration",580, 330,true,true,true,true,8,4);
			jdpDesktop.add(iframeConfigView);	
		}	
		iframeConfigView.repaint();

		configpanel = new JPanel();

		configpanel.setLayout(new BoxLayout(configpanel,BoxLayout.Y_AXIS));
		configpanel.setBackground(Color.WHITE);		

		PSatConfigView configViewPanel = new PSatConfigView();
		configpanel.add(configViewPanel);

		if(!PSatAPI.instance.externalViewMode){
			iframeConfigView.setVisible(true);
			try {
				iframeConfigView.setSelected(true);
			} catch (java.beans.PropertyVetoException e) {
			}
		}

		iframeConfigView.add(configpanel);		
		iframeConfigView.setVisible(true);

	}

	public static boolean configPercentagePossibleWorldsAndNoAgentsRangeDisplay(){
		if(PSatAPI.instance.sourceAgentName == null){
			Display.updateLogPage("source agent not selected", true);
			return false;
		}

		int noagents = PSatAPI.netGetNoAgents();

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
					PSatAPI.instance.maxNoOfknowAgents = new Integer(maxNoOfknowAgents_s) ;
					PSatAPI.instance.minNoOfknowAgents = new Integer(minNoOfknowAgents_s);
					PSatAPI.instance.noOfKnownAgentsGenerator = new Random();
					if(propergateBelief.isSelected()){
						PSatAPI.instance.beliefReasoningActive = true;
						System.err.println("TODO: Belief propergation not yet implemented");
					}	    	    		
					PSatAPI.netSerialiseConfigInstance();

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
		PSatAPI.instance.sourceAgentName = null;
		PSatAPI.instance.targetAgentName = null;
		PSatAPI.instance.selectedPath = null;
		PSatAPI.instance.listPathsData = new String[0];
		pathsListModel.removeAllElements();
		if(PSatAPI.instance.sourceAgentName ==null || PSatAPI.instance.targetAgentName == null){
			activateRun(false);	
		}
		PSatAPI.netSerialiseConfigInstance();
	}

	private static void runGraphStatistics(){

		Thread queryThread = new Thread() {
			public void run() {
				updateProgressComponent(-1,"");

				double averageClusteringCoefficient = (double) Math.round((PSatAPI.netAverageClusteringCoefficient())*100)/100;
				double averageDistance = (double) Math.round((PSatAPI.netAverageofAverageDistance())*100)/100;
				double averageDiameter = (double) Math.round((PSatAPI.netDiameter())*100)/100;
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
