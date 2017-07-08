package gla.prisoft.client.kernel.display.model;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSlider;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import gla.prisoft.client.Display;
import gla.prisoft.client.PSatClient;
import gla.prisoft.server.PSatAPI;
import gla.prisoft.shared.Agent;
import gla.prisoft.shared.AssertionRole;
import gla.prisoft.shared.CollectiveStrategy;
import gla.prisoft.shared.KnowledgeBase;
import gla.prisoft.shared.KnowledgeLevel;

import javax.swing.JScrollPane;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Vector;

public class AssertionsView extends Container {
	private static final long serialVersionUID = -7959814412480468751L;
	public JTable table;
	public AssertionsTableModel model;
	private Agent agent;
	private String agentName;

	JSlider slider_uncertainty;
	// JSlider slider_certainty;
	JSlider slider_entropy;

	private JLabel controlDescLabel;
	private JLabel kblabel;
	
	private JRadioButton kb_rb1;
	private JRadioButton kb_rb2;
	private JRadioButton kb_rb3;

	private DecimalFormat df = new DecimalFormat("####0.00");


	/**
	 * Create the panel.
	 */
	public AssertionsView(final String agentName) {
//		PSatClient.netDeseraliseConfigInstance();
		
		this.agentName = agentName;
		setLayout(new BorderLayout(0, 0));
		setBackground(Color.white);
		agent = PSatClient.netGetAgent(agentName);

		if(PSatAPI.instance.is_role_run){
//			//set range of role
//			Integer [] k = new Integer[Display.instance.max_analysis_path_length];
//			for(int i=0;i<Display.instance.max_analysis_path_length;i++){
//				k[i] = i;
//			}
//			//Integer[] k = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
//			
//			final JComboBox<Integer> comboBox = new JComboBox<Integer>(k);
//			comboBox.setBackground(Color.white);
//
//			Object[] message = {"set range of role",comboBox};
//			Object[] options = {"proceed"};
//			int n= JOptionPane.showOptionDialog(Display.iframeNet,message,"AssertionRole:"+agentName, JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
//
//			if (n == JOptionPane.YES_OPTION){
//				if(Display.instance.busy){
//					JOptionPane.showMessageDialog(Display.iframeNet, "Memory Stores generation in progress...", "Wait!", JOptionPane.NO_OPTION);
//					return;
//				}
//				Display.instance.k = comboBox.getSelectedIndex();
//				Display.instance.selfAgentName = agentName;
//				PSatClient.netSerialiseConfigInstance();
//
//				if(Display.instance.k >0){
//					Display.updatePathsList();
//					Display.updateNetworkNode();
//
//					Display.activateRun(true);
//				}
//				else{
//					Display.activateRun(false);
//				}
//
//			}
			
			if(PSatAPI.instance.busy){
				JOptionPane.showMessageDialog(Display.iframeNet, PSatAPI.instance.busymessage, "Wait!", JOptionPane.NO_OPTION);
				return;
			}
			PSatAPI.instance.selfAgentName = agentName;
			PSatClient.netSerialiseConfigInstance();

			if(PSatAPI.instance.k >0){
				Display.updatePathsList();
				Display.updateNetworkNode();

				Display.activateRun(true);
			}
			else{
				Display.activateRun(false);
			}

		}

		GridLayout mLayout = new GridLayout(1,3);
		setLayout(mLayout);

		JPanel lpanel = null;

		if(PSatAPI.instance.isModePick){
			GridLayout lLayout = new GridLayout(5,1);
			lpanel = new JPanel(lLayout);
		}
		else{
			GridLayout lLayout = new GridLayout(7,1);
			lpanel = new JPanel(lLayout);
		}
		lpanel.setBackground(Color.white);

		JLabel labelmode = new JLabel("KnowledgeBase: subject-su, sender-s, recipient-r");
		labelmode.setForeground(new Color(54,133,47));
		labelmode.setBackground(Color.white);
		if(!PSatAPI.instance.is_role_run && PSatAPI.instance.isModePick){
			labelmode.setEnabled(false);
		}

//		final JRadioButton kb_rb1 = new JRadioButton("[su]");
		kb_rb1 = new JRadioButton("[su]");
		kb_rb1.setBackground(Color.white);
		kb_rb1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				PSatAPI.instance.knowledgeBase = KnowledgeBase.SUBJECT;
				PSatClient.netSerialiseConfigInstance();
				
				KnowledgeLevel kl = agent.getKnowledgeLevel(agentName,PSatAPI.instance.knowledgeBase);

				if(PSatAPI.instance.is_role_run && PSatAPI.instance.isModePick){
					model.setRowCount(0);

					Display.afactory.displayAssertions(Display.ksView);
				}

				if(PSatAPI.instance.isModeUncertainty){
					if(kl !=null){
						double uncertaintyLevel = kl.getUncertaintyLevel();
						// double beliefLevel = kl.getBeliefLevel();

						double ul = uncertaintyLevel*100;
						// double bl = beliefLevel;

						slider_uncertainty.setValue(new Double(ul).intValue());
						// slider_certainty.setValue(new Double(bl).intValue());
					}
					else{
						slider_uncertainty.setValue(new Double(0).intValue());
						// slider_certainty.setValue(new Double(0).intValue());
					}
				}


			}
		});

//		final JRadioButton kb_rb2 = new JRadioButton("[s]");
		kb_rb2 = new JRadioButton("[s]");
		kb_rb2.setBackground(Color.white);

		kb_rb2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
				PSatAPI.instance.knowledgeBase = KnowledgeBase.SENDER;
				PSatClient.netSerialiseConfigInstance();

				KnowledgeLevel kl = agent.getKnowledgeLevel(agentName,PSatAPI.instance.knowledgeBase);

				if(PSatAPI.instance.is_role_run && PSatAPI.instance.isModePick){
					model.setRowCount(0);
					Display.afactory.displayAssertions(Display.ksView);
				}

				if(PSatAPI.instance.isModeUncertainty){
					if(kl !=null){
						double uncertaintyLevel = kl.getUncertaintyLevel();
						// double beliefLevel = kl.getBeliefLevel();

						double ul = uncertaintyLevel*100;
						// double bl = beliefLevel;

						slider_uncertainty.setValue(new Double(ul).intValue());
						// slider_certainty.setValue(new Double(bl).intValue());
					}
					else{
						slider_uncertainty.setValue(new Double(0).intValue());
						// slider_certainty.setValue(new Double(0).intValue());
					}
				}

			}
		});

//		final JRadioButton kb_rb3 = new JRadioButton("[r]");
		kb_rb3 = new JRadioButton("[r]");
		kb_rb3.setBackground(Color.white);

		kb_rb3.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent event) {
//				PSatClient.netDeseraliseConfigInstance();
				PSatAPI.instance.knowledgeBase = KnowledgeBase.RECIPIENT;

				PSatClient.netSerialiseConfigInstance();

				KnowledgeLevel kl = agent.getKnowledgeLevel(agentName,PSatAPI.instance.knowledgeBase);
				if(PSatAPI.instance.is_role_run && PSatAPI.instance.isModePick){
					model.setRowCount(0);
					Display.afactory.displayAssertions(Display.ksView);
				}
				if(PSatAPI.instance.isModeUncertainty){
					if(kl !=null){
						double uncertaintyLevel = kl.getUncertaintyLevel();
						// double beliefLevel = kl.getBeliefLevel();

						double ul = uncertaintyLevel*100;
						// double bl = beliefLevel;

						slider_uncertainty.setValue(new Double(ul).intValue());
						// slider_certainty.setValue(new Double(bl).intValue());
					}
					else{
						slider_uncertainty.setValue(new Double(0).intValue());
						// slider_certainty.setValue(new Double(0).intValue());
					}
				}

			}
		});

		//default kb
		PSatAPI.instance.knowledgeBase = KnowledgeBase.SENDER;
		PSatClient.netSerialiseConfigInstance();

		KnowledgeLevel kl = agent.getKnowledgeLevel(agentName,PSatAPI.instance.knowledgeBase);
		kb_rb2.setSelected(true);

		String text = "<html><font size='2'><b>Verified in:</b>";
		int i=0;
		for(String an: PSatClient.netGetPathAgentNames()){
			text = text +" "+an;
			if(i==20){
				text = text+"<br>";
			}
			i = i+1;
		}
		text = text +"</font><html>";
		if(kblabel == null){
			kblabel = new JLabel(text);
			// kblabel.setPreferredSize(new Dimension(100, 100));
		}
		else{
			kblabel.setText(text);
		}

		ButtonGroup kb_g_rb = new ButtonGroup();
		kb_g_rb.add(kb_rb1);
		kb_g_rb.add(kb_rb2);
		kb_g_rb.add(kb_rb3);

		if((PSatAPI.instance.isModePick && !PSatAPI.instance.is_role_run)||PSatAPI.instance.isModeEntropy){
			kb_rb1.setSelected(false);
			kb_rb2.setSelected(false);
			kb_rb3.setSelected(false);
			kb_rb1.setEnabled(false);
			kb_rb2.setEnabled(false);
			kb_rb3.setEnabled(false);
		}
		else{

		}

		GridLayout kbLayout = null;
		kbLayout = new GridLayout(1,6);


		JPanel kbpanel = new JPanel(kbLayout);
		kbpanel.setBackground(Color.white);
		GridLayout kbmLayout = null;
		kbmLayout = new GridLayout(2,1);

		JPanel kbmpanel = new JPanel(kbmLayout);
		kbmpanel.setBackground(Color.white);
		kbmpanel.add(labelmode);

		kbpanel.add(kb_rb1);
		kbpanel.add(kb_rb2);
		kbpanel.add(kb_rb3);

		JLabel blankl = new JLabel("                ");
		JLabel blankl2 = new JLabel("                ");
		kbpanel.add(blankl);
		kbpanel.add(blankl2);

		kbpanel.setBackground(Color.white);
		kbmpanel.add(kbpanel); 


		lpanel.add(kbmpanel); //lpanel 1

		String aori = "";
		if(PSatAPI.instance.is_role_run){
			aori = "RoleRange="+(PSatAPI.instance.k-1);
		}
		else{
			aori = "source="+PSatAPI.instance.sourceAgentName+" target="+PSatAPI.instance.targetAgentName;
		}

		if(PSatAPI.instance.is_role_run){
			if(kl !=null){
				double uncertaintyLevel = kl.getUncertaintyLevel();
				// double beliefLevel = kl.getBeliefLevel();

				double ul = uncertaintyLevel;
				// double bl = beliefLevel;

				if(PSatAPI.instance.isModeUncertainty){
					// controlDescLabel= new JLabel("<html><font size='2'>"+agentName+"'s requirements configuration: uncertainty="+df.format(ul)+", belief="+df.format(bl)+", "+aori+"</font></html>");
					controlDescLabel= new JLabel("<html><font size='2'>"+agentName+"'s requirements configuration: uncertainty="+df.format(ul)+", "+aori+", global privacy goal (v<sub>pr</sub>)="+agent.getGlobalPrivacyGoal_v()+"</font></html>");
				}
				else{
					controlDescLabel= new JLabel("<html><font size='2'>"+agentName+"'s requirements configuration: "+aori+", global privacy goal (v<sub>pr</sub>)="+agent.getGlobalPrivacyGoal_v()+"</font></html>");
				}
			}
			else{
				if(PSatAPI.instance.isModeUncertainty){
					// controlDescLabel= new JLabel("<html><font size='2'>"+agentName+"'s requirements configuration: uncertainty=NA, belief=NA, "+aori+"</font></html>");
					controlDescLabel= new JLabel("<html><font size='2'>"+agentName+"'s requirements configuration: uncertainty=NA,"+aori+", global privacy goal (v<sub>pr</sub>)="+agent.getGlobalPrivacyGoal_v()+"</font></html>");
				}
				else{
					controlDescLabel= new JLabel("<html><font size='2'>"+agentName+"'s requirements configuration: "+aori+", global privacy goal (v<sub>pr</sub>)="+agent.getGlobalPrivacyGoal_v()+"</font></html>");
				}
			}

		}
		else{
			if(kl !=null){
				double uncertaintyLevel = kl.getUncertaintyLevel();
				// double beliefLevel = kl.getBeliefLevel();

				double ul = uncertaintyLevel;
				// double bl = beliefLevel;

				if(PSatAPI.instance.isModeUncertainty){
					String ult = "";
					// String blt = "";
					if(ul == 0){
						ult = "NA";
						controlDescLabel= new JLabel("<html><font size='2'>"+agentName+"'s requirements configuration: uncertainty="+ult+", "+aori+", global privacy goal (v<sub>pr</sub>)="+agent.getGlobalPrivacyGoal_v()+"</font></html>");

					}
					else{
						controlDescLabel= new JLabel("<html><font size='2'>"+agentName+"'s requirements configuration: uncertainty="+df.format(ul)+", "+aori+", global privacy goal (v<sub>pr</sub>)="+agent.getGlobalPrivacyGoal_v()+"</font></html>");
					}
					// if(bl == 0){
					// blt = "NA";
					// }
					// controlDescLabel= new JLabel("<html><font size='2'>"+agentName+"'s requirements configuration: uncertainty="+df.format(ult)+", belief="+df.format(blt)+", "+aori+"</font></html>");
				}
			}
			else{
				if(PSatAPI.instance.isModeUncertainty){
					// controlDescLabel= new JLabel("<html><font size='2'>"+agentName+"'s requirements configuration: uncertainty=NA, belief=NA, "+aori+"</font></html>");
					controlDescLabel= new JLabel("<html><font size='2'>"+agentName+"'s requirements configuration: uncertainty=NA, "+aori+", global privacy goal (v<sub>pr</sub>)="+agent.getGlobalPrivacyGoal_v()+"</font></html>");
				}
			}
		}
		if(controlDescLabel == null){
			controlDescLabel= new JLabel("");
		}
		controlDescLabel.setOpaque(true);
		controlDescLabel.setBackground(Color.white);

		GridLayout dLayout = new GridLayout(1,1);
		JPanel dpanel = new JPanel(dLayout);
		dpanel.setBackground(Color.white);
		dpanel.add(controlDescLabel); 

		lpanel.add(dpanel); //lpanel 2


		if(PSatAPI.instance.isModePick){
			model = new AssertionsTableModel();

			model.setRowCount(0);

			table = new JTable(model);

			table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
				private static final long serialVersionUID = -8305142193885321738L;
				@Override
				public Component getTableCellRendererComponent(JTable table, 
						Object value, boolean isSelected, boolean hasFocus, int row, int column) {
					Component c = super.getTableCellRendererComponent(table, 
							value, isSelected, hasFocus, row, column);
					c.setBackground(row%2==0 ? Color.white : new Color(226,225,213));                        
					return c;
				};
			});
			table.setFont(new Font("Verdana", Font.PLAIN, 10));
			((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer())
			.setHorizontalAlignment(JLabel.LEFT);
			table.getTableHeader().setFont(new Font("Verdana", Font.BOLD, 10));
			table.setAutoResizeMode( JTable.AUTO_RESIZE_OFF );
			TableColumn column = null;
			for (int n = 0; n < 5; n++) {
				column = table.getColumnModel().getColumn(n);
				if (n == 0) {
					column.setMaxWidth(35);
				}
				if (n == 1){
					column.setMinWidth(30);
					column.setMaxWidth(30);
				}
				if (n == 2){
					column.setMinWidth(192);
					column.setMaxWidth(250);
				}
				if (n == 3){
					column.setMaxWidth(60);
				}
				if (n == 4){
					column.setMinWidth(300);
					//column.setMaxWidth(462);
				}
			}

			JScrollPane scrollPane = new JScrollPane(table,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);			
//			JPanel tablePanel = new JPanel(new GridLayout());
//			tablePanel.add(scrollPane);
//			tablePanel.setBackground(Color.red);
//			add(tablePanel);
			add(scrollPane);

		}
		else if(PSatAPI.instance.isModeUncertainty){
			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			java.util.Hashtable<Integer,JLabel> labelTable = new java.util.Hashtable<Integer,JLabel>();
			labelTable.put(new Integer(100), new JLabel("<html><font size='1'>1</font></html>"));
			labelTable.put(new Integer(95), new JLabel("<html><font size='1'></font></html>"));
			labelTable.put(new Integer(90), new JLabel("<html><font size='1'>0.9</font></html>"));
			labelTable.put(new Integer(85), new JLabel("<html><font size='1'></font></html>"));
			labelTable.put(new Integer(80), new JLabel("<html><font size='1'>0.8</font></html>"));
			labelTable.put(new Integer(75), new JLabel("<html><font size='1'></font></html>"));
			labelTable.put(new Integer(70), new JLabel("<html><font size='1'>0.7</font></html>"));
			labelTable.put(new Integer(65), new JLabel("<html><font size='1'></font></html>"));
			labelTable.put(new Integer(60), new JLabel("<html><font size='1'>0.6</font></html>"));
			labelTable.put(new Integer(55), new JLabel("<html><font size='1'></font></html>"));
			labelTable.put(new Integer(50), new JLabel("<html><font size='1'>0.5</font></html>"));
			labelTable.put(new Integer(45), new JLabel("<html><font size='1'></font></html>"));
			labelTable.put(new Integer(40), new JLabel("<html><font size='1'>0.4</font></html>"));
			labelTable.put(new Integer(35), new JLabel("<html><font size='1'></font></html>"));
			labelTable.put(new Integer(30), new JLabel("<html><font size='1'>0.3</font></html>"));
			labelTable.put(new Integer(25), new JLabel("<html><font size='1'></font></html>"));
			labelTable.put(new Integer(20), new JLabel("<html><font size='1'>0.2</font></html>"));
			labelTable.put(new Integer(15), new JLabel("<html><font size='1'></font></html>"));
			labelTable.put(new Integer(10), new JLabel("<html><font size='1'>0.1</font></html>"));
			labelTable.put(new Integer(5), new JLabel("<html><font size='1'></font></html>"));
			labelTable.put(new Integer(0), new JLabel("<html><font size='1'>NA</font></html>"));



			//
			GridLayout gLayout = new GridLayout(2,2);
			//
			JPanel ppanel = new JPanel(gLayout);

			JLabel u_label = new JLabel("<html><body bgcolor='#E6E6FA'><font size='2'>UncertaintyLevel</font></body></html>");
			u_label.setBackground(Color.white);
			// lpanel.add(u_label); //3 lpanel

			//
			ppanel.add(u_label);
			//ppanel.add(b_label);

			
			slider_uncertainty = new JSlider(JSlider.HORIZONTAL, 0, 100, 0);
			slider_uncertainty.setMajorTickSpacing(50);
			slider_uncertainty.setPaintTicks(true);
			slider_uncertainty.setLabelTable( labelTable );
			slider_uncertainty.setPaintLabels(true);  
			slider_uncertainty.setBackground(Color.white);
			slider_uncertainty.setToolTipText("<html><font size='2'>uncertainty control</font></html>");
			slider_uncertainty.addChangeListener(new javax.swing.event.ChangeListener(){
				public void stateChanged(javax.swing.event.ChangeEvent ce){

					JSlider source = (JSlider) ce.getSource();
					if (!source.getValueIsAdjusting()) {
						Display.updateProgressComponent(-1, "");
						updateUncertaintyBeliefLevels();
						Display.updateProgressComponent(100, "");
					}

				}
			});
			if(kl !=null){
				double uncertaintyLevel = kl.getUncertaintyLevel();       
				double ul = 0;
				ul = uncertaintyLevel;
				slider_uncertainty.setValue(new Double(ul*100).intValue());
			}
			
			// lpanel.add(slider_uncertainty); // 4 lpanel

			// JLabel b_label = new JLabel("<html><body bgcolor='#E6E6FA'><font size='2'>Operands</font></body></html>");
			// b_label.setBackground(Color.white);
			// lpanel.add(b_label); // 5 lpanel
			// lpanel.setBackground(Color.white);

			// if(kl !=null){
			// double beliefLevel = kl.getBeliefLevel();
			// double bl = 0;
			// bl = beliefLevel; 
			// slider_certainty = new JSlider(JSlider.HORIZONTAL, 0, 100, new Double(bl).intValue());
			// }
			// else{
			// slider_certainty = new JSlider(JSlider.HORIZONTAL, 0, 100, new Double(0).intValue());
			// }
			// slider_certainty.setMajorTickSpacing(50);
			// slider_certainty.setPaintTicks(true);
			// slider_certainty.setLabelTable( labelTable );
			// slider_certainty.setPaintLabels(true);  
			// slider_certainty.setBackground(Color.white);
			// slider_certainty.setToolTipText("<html><font size='2'>belief control</font></html>");
			// slider_certainty.addChangeListener(new javax.swing.event.ChangeListener(){
			// public void stateChanged(javax.swing.event.ChangeEvent ce){
			//
			// JSlider source = (JSlider) ce.getSource();
			// if (!source.getValueIsAdjusting()) {
			// Display.updateProgressComponent(-1, "");
			// updateUncertaintyBeliefLevels();
			// Display.updateProgressComponent(100, "");
			// }
			// }
			// });

			JPanel oopanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
			oopanel.setBackground(Color.white);
			oopanel.add(u_label);

			JRadioButton gtetrb = new JRadioButton("≥");

			gtetrb.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent event) {
					PSatAPI.instance.greaterThanOrEqualTo = true;
					PSatAPI.instance.lessThanOrEqualTo = false;
					
					PSatClient.netSerialiseConfigInstance();
				}
			});
			oopanel.add(gtetrb);

			JRadioButton ltetrb = new JRadioButton("≤");

			ltetrb.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent event) {


					PSatAPI.instance.greaterThanOrEqualTo = false;
					PSatAPI.instance.lessThanOrEqualTo = true;
					
					PSatClient.netSerialiseConfigInstance();
				}
			});

			if(PSatAPI.instance.greaterThanOrEqualTo){
				gtetrb.setSelected(true);
				ltetrb.setSelected(false);

			}
			else{
				gtetrb.setSelected(false);
				ltetrb.setSelected(true);
			}
			gtetrb.setBackground(Color.white);
			ltetrb.setBackground(Color.white);
			oopanel.add(ltetrb);


			ButtonGroup pick_g_rb = new ButtonGroup();
			pick_g_rb.add(ltetrb);
			pick_g_rb.add(gtetrb);

			lpanel.add(oopanel); // 6 lpanel 
			lpanel.setBackground(Color.white);
			lpanel.add(slider_uncertainty);

			// lpanel.add(slider_certainty); // 6 lpanel  

			////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

		}
		else if(PSatAPI.instance.isModeEntropy){
			java.util.Hashtable<Integer,JLabel> labelTable = new java.util.Hashtable<Integer,JLabel>();
			labelTable.put(new Integer(100), new JLabel("<html><font size='1'>1</font></html>"));
			labelTable.put(new Integer(95), new JLabel("<html><font size='1'></font></html>"));
			labelTable.put(new Integer(90), new JLabel("<html><font size='1'>0.9</font></html>"));
			labelTable.put(new Integer(85), new JLabel("<html><font size='1'></font></html>"));
			labelTable.put(new Integer(80), new JLabel("<html><font size='1'>0.8</font></html>"));
			labelTable.put(new Integer(75), new JLabel("<html><font size='1'></font></html>"));
			labelTable.put(new Integer(70), new JLabel("<html><font size='1'>0.7</font></html>"));
			labelTable.put(new Integer(65), new JLabel("<html><font size='1'></font></html>"));
			labelTable.put(new Integer(60), new JLabel("<html><font size='1'>0.6</font></html>"));
			labelTable.put(new Integer(55), new JLabel("<html><font size='1'></font></html>"));
			labelTable.put(new Integer(50), new JLabel("<html><font size='1'>0.5</font></html>"));
			labelTable.put(new Integer(45), new JLabel("<html><font size='1'></font></html>"));
			labelTable.put(new Integer(40), new JLabel("<html><font size='1'>0.4</font></html>"));
			labelTable.put(new Integer(35), new JLabel("<html><font size='1'></font></html>"));
			labelTable.put(new Integer(30), new JLabel("<html><font size='1'>0.3</font></html>"));
			labelTable.put(new Integer(25), new JLabel("<html><font size='1'></font></html>"));
			labelTable.put(new Integer(20), new JLabel("<html><font size='1'>0.2</font></html>"));
			labelTable.put(new Integer(15), new JLabel("<html><font size='1'></font></html>"));
			labelTable.put(new Integer(10), new JLabel("<html><font size='1'>0.1</font></html>"));
			labelTable.put(new Integer(5), new JLabel("<html><font size='1'></font></html>"));
			labelTable.put(new Integer(0), new JLabel("<html><font size='1'>0</font></html>"));

			JLabel u_label = new JLabel("<html><body bgcolor='#E6E6FA'><font size='2'>Desired Knowledge Entropy</font></body></html>");
			lpanel.add(u_label); //3 lpanel

			BorderLayout oLayout = new BorderLayout();
			JPanel opanel = new JPanel(oLayout);
			JPanel oopanel = new JPanel();

			JRadioButton gtetrb = new JRadioButton("≥");

			gtetrb.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent event) {
					PSatAPI.instance.greaterThanOrEqualTo = true;
					PSatAPI.instance.lessThanOrEqualTo = false;

					PSatClient.netSerialiseConfigInstance();
				}
			});
			oopanel.add(gtetrb);

			JRadioButton ltetrb = new JRadioButton("≤");

			ltetrb.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent event) {
					PSatAPI.instance.greaterThanOrEqualTo = false;
					PSatAPI.instance.lessThanOrEqualTo = true;

					PSatClient.netSerialiseConfigInstance();
				}
			});

			if(PSatAPI.instance.greaterThanOrEqualTo){
				gtetrb.setSelected(true);
				ltetrb.setSelected(false);

			}
			else{
				gtetrb.setSelected(false);
				ltetrb.setSelected(true);
			}
			gtetrb.setBackground(Color.white);
			ltetrb.setBackground(Color.white);
			oopanel.add(ltetrb);

			ButtonGroup pick_g_rb = new ButtonGroup();
			pick_g_rb.add(ltetrb);
			pick_g_rb.add(gtetrb);

			opanel.add(oopanel, BorderLayout.WEST);
			lpanel.setBackground(Color.white);

//			slider_entropy = new JSlider(JSlider.HORIZONTAL, 0, 100, new Double((Display.instance.desiredEntropy*100)).intValue());
			slider_entropy = new JSlider(JSlider.HORIZONTAL, 0, 100, new Double((agent.getDesiredEntropy()*100)).intValue());

			slider_entropy.setMajorTickSpacing(50);
			slider_entropy.setPaintTicks(true);
			slider_entropy.setLabelTable( labelTable );
			slider_entropy.setPaintLabels(true);  
			slider_entropy.setBackground(Color.white);
			slider_entropy.setToolTipText("<html><font size='2'>knowledge entropy regulation</font></html>");
			slider_entropy.addChangeListener(new javax.swing.event.ChangeListener(){
				public void stateChanged(javax.swing.event.ChangeEvent ce){

					JSlider source = (JSlider) ce.getSource();
					if (!source.getValueIsAdjusting()) {
						double desiredEntropy = ((double)slider_entropy.getValue())/(double)100;
//						Display.instance.desiredEntropy = desiredEntropy;
						agent.setDesiredEntropy(desiredEntropy);
						PSatClient.netAddAgent(agent);
						
						PSatClient.netSerialiseConfigInstance();

//						slider_entropy.setValue(new Double((Display.instance.desiredEntropy*100)).intValue());
						slider_entropy.setValue(new Double((agent.getDesiredEntropy()*100)).intValue());

						String aori = "";

						if(PSatAPI.instance.is_role_run){

							aori = "RoleRange="+(PSatAPI.instance.k-1);

						}

						else{
							aori = "source="+PSatAPI.instance.sourceAgentName+" target="+PSatAPI.instance.targetAgentName;

						}

						String eldescription = "if[";
						if(PSatAPI.instance.is_role_run){
							eldescription = eldescription+ PSatAPI.instance.subjectName+";"+ PSatAPI.instance.sourceAgentName+";"+PSatAPI.instance.k+"]";
						}
						else{
							eldescription = eldescription+ PSatAPI.instance.subjectName+";"+ PSatAPI.instance.sourceAgentName+";"+PSatAPI.instance.targetAgentName+"]";	
						}
						eldescription = eldescription +"then[KnowledgeEntropy(f)";
						if(PSatAPI.instance.greaterThanOrEqualTo){
							eldescription = eldescription+"≥"+desiredEntropy;
						}
						else if(PSatAPI.instance.lessThanOrEqualTo){
							eldescription = eldescription+"≤"+desiredEntropy;
						}
						eldescription = eldescription+"]";
						PSatAPI.instance.desiredEntropyDesc = eldescription;
						

						controlDescLabel.setText("<html><font size='2'>"+agentName+"'s requirements configuration: knowledge entropy="+desiredEntropy+", "+aori+", global privacy goal (v<sub>pr</sub>)="+agent.getGlobalPrivacyGoal_v()+"</font></html>");

					}
				}
			});
			controlDescLabel.setText("<html><font size='2'>"+agentName+"'s requirements configuration: knowledge entropy="+agent.getDesiredEntropy()+", "+aori+", global privacy goal (v<sub>pr</sub>)="+agent.getGlobalPrivacyGoal_v()+"</font></html>");
			opanel.add(slider_entropy, BorderLayout.CENTER);

			lpanel.add(opanel); // 4 lpanel 

			opanel.setBackground(Color.white);
			oopanel.setBackground(Color.white);
			lpanel.setBackground(Color.white);
		}
//		else if(PSatAPI.instance.isModeCommonKnowledge){
//			if(PSatAPI.instance.is_role_run){
//				// roles knowledge constructs
//			}
//			else{
//				//instances knowledge constructs
//			}
//		}


		int k=0;
		text = "<html><font size='2'><b>Verified in:</b>";
		for(String an: PSatClient.netGetPathAgentNames()){
			text = text +" "+an;
			if(k==20){
				text = text+"<br>";
			}
			k = k+1;

		}
		if(PSatAPI.instance.isModePick){			
			text = text +"<br/> global privacy goal (v<sub>pr</sub>)="+agent.getGlobalPrivacyGoal_v()+"</font><html>";
		}
		else{
			text = text +"</font><html>";
		}
		if(kblabel == null){
			kblabel = new JLabel(text);
			// kblabel.setPreferredSize(new Dimension(100, 100));
		}
		else{
			kblabel.setText(text);
		}

		lpanel.add(kblabel);//lpanel 7
		add(lpanel);

		if(PSatAPI.instance.isModeUncertainty || PSatAPI.instance.isModeEntropy){
			JLabel blank1 =new JLabel("       ");
			add(blank1);
		}

		//
//		JLabel blank2 =new JLabel("        ");
//		//
//		add(blank2);

		Display.updateProgressComponent(100, "");
	}



	private void updateUncertaintyBeliefLevels(){
		if(kb_rb1.isSelected()){
			PSatAPI.instance.knowledgeBase = KnowledgeBase.SUBJECT;
		}
		else if(kb_rb2.isSelected()){
			PSatAPI.instance.knowledgeBase = KnowledgeBase.SENDER;
		}
		else if(kb_rb3.isSelected()){
			PSatAPI.instance.knowledgeBase = KnowledgeBase.RECIPIENT;
		}
		
//		PSatClient.netDeseraliseConfigInstance();
		int uncertaintyLevel_temp = slider_uncertainty.getValue();
		// int beliefLevel_temp = slider_certainty.getValue();


		double uncertaintyLevel = -1;
		// double beliefLevel = -1;
		uncertaintyLevel = (double)uncertaintyLevel_temp/(double)100;
		// beliefLevel = (double)beliefLevel_temp/(double)100;

		// double max_beliefLevel = 1-uncertaintyLevel;
		// if(beliefLevel >max_beliefLevel){
		// beliefLevel = max_beliefLevel;
		// }

		String [] zoneAgents = null;
		String [] panes = PSatClient.netGetPathAgentNames();
		zoneAgents = new String[panes.length];
		int i=0;
		for(String an: panes){
			zoneAgents[i] = an;
			i = i+1;

		}  

		KnowledgeLevel knowledgeLevel = new KnowledgeLevel(agentName, zoneAgents,PSatAPI.instance.knowledgeBase);		
			
		//
		// if(uncertaintyLevel !=0 || beliefLevel != 0){
		// knowledgeLevel.setUncertaintyLevel(uncertaintyLevel);
		// knowledgeLevel.setBeliefLevel(beliefLevel);
		// }
		// if(uncertaintyLevel !=0){
		knowledgeLevel.setUncertaintyLevel(uncertaintyLevel);
		// }
		

		double uncertaintyLevelx = knowledgeLevel.getUncertaintyLevel();
		// double beliefLevelx = knowledgeLevel.getBeliefLevel();

		double ul = uncertaintyLevelx*100;
		// double bl = beliefLevelx*100;

		slider_uncertainty.setValue(new Double(ul).intValue());
		// slider_certainty.setValue(new Double(bl).intValue());

		double uncertaintyLevely = knowledgeLevel.getUncertaintyLevel();
		// double beliefLevely = knowledgeLevel.getBeliefLevel();

		String uly = "";
		if(uncertaintyLevely ==0){
			uly = "NA";
		}
		else{
			uly = ""+(df.format(uncertaintyLevely));
		}

		// String bly = "";
		// if(beliefLevely ==0){
		// bly = "NA";
		// }
		// else{
		// bly = ""+(df.format(beliefLevely));
		// }		
		
		String aori = "";
		
		if(PSatAPI.instance.is_role_run){
			aori = "RoleRange="+(PSatAPI.instance.k-1);
		}
		else{
			aori = "source="+PSatAPI.instance.sourceAgentName+" target="+PSatAPI.instance.targetAgentName;
		}
				

		if(PSatAPI.instance.is_role_run){
			if(PSatAPI.instance.isModeUncertainty){
				// controlDescLabel.setText("<html><font size='2'>"+agentName+"'s requirements configuration: uncertainty="+uly+", belief="+bly+", "+aori+"</font></html>");
				controlDescLabel.setText("<html><font size='2'>"+agentName+"'s requirements configuration: uncertainty="+uly+", "+aori+", global privacy goal (v<sub>pr</sub>)="+agent.getGlobalPrivacyGoal_v()+"</font></html>");
			}
			else{
				controlDescLabel.setText("<html><font size='2'>"+agentName+"'s requirements configuration: "+aori+", global privacy goal (v<sub>pr</sub>)="+agent.getGlobalPrivacyGoal_v()+"</font></html>");
			}
		}
		else{
			if(PSatAPI.instance.isModeUncertainty){
				// controlDescLabel.setText("<html><font size='2'>"+agentName+"'s requirements configuration: uncertainty="+uly+", belief="+bly+", "+aori+"</font></html>");
				controlDescLabel.setText("<html><font size='2'>"+agentName+"'s requirements configuration: uncertainty="+uly+","+aori+", global privacy goal (v<sub>pr</sub>)="+agent.getGlobalPrivacyGoal_v()+"</font></html>");
			}
		}

		String kldescription = "if[";
		if(PSatAPI.instance.is_role_run){
			kldescription = kldescription+ PSatAPI.instance.subjectName+";"+ PSatAPI.instance.sourceAgentName+";"+PSatAPI.instance.k+"]";
		}
		else{
			kldescription = kldescription+ PSatAPI.instance.subjectName+";"+ PSatAPI.instance.sourceAgentName+";"+PSatAPI.instance.targetAgentName+"]";	
		}
		kldescription = kldescription +"then[UncertaintyLevel(";
		if(PSatAPI.instance.knowledgeBase == KnowledgeBase.RECIPIENT){
			kldescription = kldescription+"r,f)";
		}
		else if(PSatAPI.instance.knowledgeBase == KnowledgeBase.SENDER){
			kldescription = kldescription+"s,f)";
		}
		else if(PSatAPI.instance.knowledgeBase == KnowledgeBase.SUBJECT){
			kldescription = kldescription+"su,f)";
		}
		if(PSatAPI.instance.greaterThanOrEqualTo){
			kldescription = kldescription+"≥"+uly;
		}
		else if(PSatAPI.instance.lessThanOrEqualTo){
			kldescription = kldescription+"≤"+uly;
		}
		kldescription = kldescription+"]";
		
		knowledgeLevel.setKldescription(kldescription);
		
		agent.addKnowledgeLevel(knowledgeLevel);

		PSatClient.netWriteAgent(agent);
//		agent = PSatClient.netGetAgent(agentName);
		
		controlDescLabel.repaint();
	}
	// 
	public class AssertionsTableModel extends DefaultTableModel {
		private static final long serialVersionUID = -4775356027813366490L;

		public AssertionsTableModel() {
			super(new String[]{"No", "", "<html>Assertion(<i>A<i/>)</html>","<html>Goal(<i>v<sub>A</sub><i/>)</html>","<html>Description</html>"}, 0);
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			Class clazz = String.class;
			switch (columnIndex) {
			case 0:
				if(PSatAPI.instance.is_role_run){
					clazz = String.class;

				}
				else{
					clazz = Integer.class;

				}
				break;
			case 1:
				clazz = Boolean.class;
				break;
			case 2:
				clazz = String.class;
				break;
			case 3:
				clazz = Double.class;
				break;
			case 4:
				clazz = String.class;
			}
			return clazz;
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			switch(column){             
	           case 0:  // select the cell you want make it not editable 
	             return false;
	           case 2:
	        	   return false;
	           case 4:
	        	   return false;
	           default: 
	        	   return true;
	        } 
			
			//return column == 1;
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public void setValueAt(Object aValue, int row, int column) {
			if(kb_rb1.isSelected()){
				PSatAPI.instance.knowledgeBase = KnowledgeBase.SUBJECT;
			}
			else if(kb_rb2.isSelected()){
				PSatAPI.instance.knowledgeBase = KnowledgeBase.SENDER;
			}
			else if(kb_rb3.isSelected()){
				PSatAPI.instance.knowledgeBase = KnowledgeBase.RECIPIENT;
			}
			
			if(aValue instanceof Double && column ==3){
				double v = (double)aValue;
				if(v >=0 && v <=1){
					Vector rowData = (Vector)getDataVector().get(row);					
					String assertionDesc = (String)rowData.get(2);
					double goal_v =  v;
					if(PSatAPI.instance.is_role_run){
						String roleTypeHtml = (String)rowData.get(0);
						String [] zoneAgents = null;
						String[] panes = PSatClient.netGetPathAgentNames();
						
						zoneAgents = new String[panes.length];
						int i=0;
						for(String an: panes){
							zoneAgents[i] = an;
							i = i+1;        
						}
						AssertionRole role = new AssertionRole(agentName, roleTypeHtml, zoneAgents,PSatAPI.instance.knowledgeBase,goal_v, PSatAPI.instance.collectiveStrategy);
						agent.updateRole(role);
						
					}
					else{
						agent.updateAssertionInstance(assertionDesc, goal_v, PSatAPI.instance.collectiveStrategy);	
					}					
					
					PSatClient.netAddAgent(agent);
					PSatClient.netWriteAgent(agent);
					agent = PSatClient.netGetAgent(agentName);
										
					rowData.set(3, (double)aValue);
					fireTableCellUpdated(row, column);
				}
				else{
					JOptionPane.showMessageDialog(Display.iframeLogProTabPages, "valid goal(v): >=0 && v <=1", "invalid",JOptionPane.ERROR_MESSAGE);					
				}				
			}
			else if (aValue instanceof Boolean && column == 1) {
				boolean b = new Boolean(aValue.toString()).booleanValue();

				Vector rowData = (Vector)getDataVector().get(row);
				double goal_v =  (Double)rowData.get(3);
				
				if(!PSatAPI.instance.is_role_run){
					String assertionDesc = (String)rowData.get(2);
					
					if(assertionDesc.contains("(")){
						String [] h3 = assertionDesc.split("\\(");
						String [] h2 = h3[1].split("\\)");
						String h1 = h2[0];
						assertionDesc = "<html>"+h1+"</html>";
						
					}
					
					if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.NONE && assertionDesc.equals("<html><i>f</i></html>")){
						return;
					}
					if(PSatAPI.instance.collectiveStrategy == CollectiveStrategy.NONE && assertionDesc.equals("<html><i>C</i><sub>G</sub>(<i>f</i>|¬<i>f</i>)</html>")){
						return;
					}
						
					if(b){
						agent.addAssertionInstance(assertionDesc, goal_v, PSatAPI.instance.collectiveStrategy);
					}
					else{
						agent.removeAssertionInstance(assertionDesc);
					}
				}
				else{        
					String roleTypeHtml = (String)rowData.get(0);
					String [] zoneAgents = null;
					String[] panes = PSatClient.netGetPathAgentNames();
					
					zoneAgents = new String[panes.length];

					int i=0;

					for(String an: panes){
						zoneAgents[i] = an;
						i = i+1;        
					}

					AssertionRole role = new AssertionRole(agentName, roleTypeHtml, zoneAgents,PSatAPI.instance.knowledgeBase,goal_v, PSatAPI.instance.collectiveStrategy);

					if(b){
						agent.addRole(role);
					}
					else{
						agent.removeRole(role);
					}
				}
				//         updateKConfigLabel();

				PSatClient.netAddAgent(agent);
				PSatClient.netWriteAgent(agent);
				agent = PSatClient.netGetAgent(agentName);
				rowData.set(1, (Boolean)aValue);
				fireTableCellUpdated(row, column);

				if(PSatAPI.instance.k >0){
					Display.activateRun(true);
				}
			}
		}

	}

}