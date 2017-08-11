package gla.client.kernel.display.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.JCheckBox;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import gla.client.Display;
import gla.client.PSatClient;
import gla.prisoft.shared.CollectiveStrategy;
import gla.server.PSatAPI;
import gla.server.kernel.verification.ServerAssertionsFactory;

public class PSatConfigView extends javax.swing.JPanel{
	private static final long serialVersionUID = 1L;

	public PSatConfigView(){
		JTabbedPane config = new JTabbedPane();
		
		PrConfig prconfigpanel = new PrConfig();
		config.addTab("Privacy Requirements", prconfigpanel);
		config.setMnemonicAt(0, KeyEvent.VK_1);

		AnalysisConfig analysisconfigpanel = new AnalysisConfig();
		config.addTab("Analysis", analysisconfigpanel);
		config.setMnemonicAt(1, KeyEvent.VK_2);
		
		LogConfig logconfigpanel = new LogConfig();
		config.addTab("Logs", logconfigpanel);
		config.setMnemonicAt(2, KeyEvent.VK_3);
		
		add(config);
		config.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}
}

class PrConfig extends javax.swing.JPanel {
	private static final long serialVersionUID = 1L;
	
	PrConfig() {
        initComponents();
    }

    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
	private void initComponents() {

        prtype_bg = new javax.swing.ButtonGroup();
        type_label = new javax.swing.JLabel();
        rolebased_rb = new javax.swing.JRadioButton();
        instancebased_rb = new javax.swing.JRadioButton();
        mode_label = new javax.swing.JLabel();
        modes_sp = new javax.swing.JScrollPane();
        aspects_label = new javax.swing.JLabel();
        aspects_sp = new javax.swing.JScrollPane();

        type_label.setText("Type:");
        rolebased_rb.setText("role-based specification");
        rolebased_rb.addActionListener(new ActionListener() {	    	 

	        public void actionPerformed(ActionEvent event) {
	        	PSatAPI.instance.is_role_run = true;
	        	PSatAPI.instance.listPathsData = new String[0];
				Display.pathsListModel.removeAllElements();
				PSatAPI.instance.selectedPath = null;
				Display.prPanel.removeAll();
				PSatAPI.instance.sourceAgentName = null;
				PSatAPI.instance.subjectName = null;
				PSatAPI.instance.selfAgentName = null;
				PSatAPI.instance.targetAgentName =null;
				ServerAssertionsFactory.clearAllAgentAssertions();
				
				PSatClient.netSerialiseConfigInstance();

				Display.updatePathsList();
				Display.updateNetworkNode();	
				Display.updateProgressComponent(100, "");
				
	        }
	    });
        
        instancebased_rb.setText("instance-based specification");
        instancebased_rb.addActionListener(new ActionListener() {	    	 

	    	public void actionPerformed(ActionEvent event) {
	        	PSatAPI.instance.is_role_run = false;
	        	PSatAPI.instance.listPathsData = new String[0];
				Display.pathsListModel.removeAllElements();
				PSatAPI.instance.selectedPath = null;
				Display.prPanel.removeAll();
				PSatAPI.instance.sourceAgentName = null;
				PSatAPI.instance.subjectName = null;
				PSatAPI.instance.selfAgentName = null;
				PSatAPI.instance.targetAgentName =null;
				ServerAssertionsFactory.clearAllAgentAssertions();
				
				PSatClient.netSerialiseConfigInstance();
				
				Display.updatePathsList();
				Display.updateNetworkNode();
				
    	    	PSatClient.netSerialiseConfigInstance();
    	    	Display.updateProgressComponent(100, "");
	        }
	    });
        
        if(PSatAPI.instance.is_role_run){
        	rolebased_rb.setSelected(true);
        }
        else{
        	instancebased_rb.setSelected(true);
        }
        prtype_bg.add(rolebased_rb);
        prtype_bg.add(instancebased_rb);         
        
        
        mode_label.setText("Mode:");
        
        
        javax.swing.DefaultListModel<String> modedata = new javax.swing.DefaultListModel<String>();
        modedata.addElement("Pick belief/uncertainty elements");
        modedata.addElement("Regulate belief/uncertainty levels");
        modedata.addElement("Regulate entropy levels");
        
        modes_list = new javax.swing.JList<>(modedata);
        modes_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        if(PSatAPI.instance.isModePick){
        	modes_list.setSelectedIndex(0);
        }
        else if(PSatAPI.instance.isModeUncertainty){
        	modes_list.setSelectedIndex(1);
        }
        else if(PSatAPI.instance.isModeEntropy){
        	modes_list.setSelectedIndex(2);
        }
        else{
        	modes_list.setSelectedIndex(0);
        	PSatAPI.instance.isModePick = true;
        	PSatAPI.instance.isModeUncertainty = false;
        	PSatAPI.instance.isModeEntropy = false;
        	
        	PSatAPI.instance.collectiveStrategy = CollectiveStrategy.NONE;
        	aspects_list.setSelectedIndex(0);
        	ServerAssertionsFactory.clearAllAgentAssertions();
        	aspects_list.setEnabled(true);
        	
        	PSatClient.netSerialiseConfigInstance();
        }
        
        modes_sp.setViewportView(modes_list);
        modes_list.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				int selectedindex = modes_list.getSelectedIndex();
				if(selectedindex == 0){
					PSatAPI.instance.isModePick = true;
		        	PSatAPI.instance.isModeUncertainty = false;
		        	PSatAPI.instance.isModeEntropy = false;
		        	
		        	PSatAPI.instance.collectiveStrategy = CollectiveStrategy.NONE;
		        	aspects_list.setSelectedIndex(0);
		        	ServerAssertionsFactory.clearAllAgentAssertions();
		        	aspects_list.setEnabled(true);
		        	
		        	PSatClient.netSerialiseConfigInstance();
				}
				else if(selectedindex ==1){
					PSatAPI.instance.isModePick = false;
		        	PSatAPI.instance.isModeUncertainty = true;
		        	PSatAPI.instance.isModeEntropy = false;
		        	
		        	PSatAPI.instance.collectiveStrategy = CollectiveStrategy.NONE;
		        	aspects_list.setSelectedIndex(0);
		        	ServerAssertionsFactory.clearAllAgentAssertions();
		        	aspects_list.setEnabled(false);
		        	
					PSatClient.netSerialiseConfigInstance();
				}
				else if(selectedindex ==2){
					PSatAPI.instance.isModePick = false;
		        	PSatAPI.instance.isModeUncertainty = false;
		        	PSatAPI.instance.isModeEntropy = true;
		        	
		        	PSatAPI.instance.collectiveStrategy = CollectiveStrategy.NONE;
		        	aspects_list.setSelectedIndex(0);
		        	ServerAssertionsFactory.clearAllAgentAssertions();
		        	aspects_list.setEnabled(false);

					PSatClient.netSerialiseConfigInstance();
				}				
			}
        	
        });
                
        aspects_label.setText("Aspects:");
        
        javax.swing.DefaultListModel<String> aspectdata = new javax.swing.DefaultListModel<String>();
        aspectdata.addElement("None");
        aspectdata.addElement("<html>Mutual Knowledge:<i>E</i><sub>G</sub>(&psi;), Everyone in G beliefs that <i>&psi;</i></html>");
        aspectdata.addElement("<html>Introspection:<i>EE</i><sub>G</sub>(&psi;), Everyone in G beliefs that everyone knows <i>&psi;</i></html>");
//      aspectdata.addElement("<html>Introspection:<i>EEE</i><sub>G</sub>(&psi;), Everyone in G beliefs that everyone knows that everyone knows that <i>&psi;</i> </html>");
//      aspectdata.addElement("<html>Common Knowledge:<i>C</i><sub>G</sub>(&psi;)&#8872; <i>E</i><sub>G</sub>(&psi;) + <i>EE</i><sub>G</sub>(&psi;) + <i>EEE</i><sub>G</sub>(&psi;) </html>");
        aspectdata.addElement("<html>Common Knowledge:<i>C</i><sub>G</sub>(&psi;)&#8872; &psi; + <i>E</i><sub>G</sub>(&psi;) + <i>EE</i><sub>G</sub>(&psi;)</html>");
//      aspectdata.addElement("<html>Distributed Knowledge:<i>S</i><sub>G</sub>(&psi;)- More than one user beliefs/uncertain that <i>&psi;</i></html>");
//      aspectdata.addElement("<html>Distributed Knowledge:<i>B</i><sub>G</sub>(&psi;)- Atleast one user beliefs/uncertain that <i>&psi;</i></html>");
//      aspectdata.addElement("<html>Distributed Knowledge:<i>D</i><sub>G</sub>(&psi;), The belief/uncertain of <i>&psi;</i> can be inferred by a user </html>"); 
        
        aspects_list = new javax.swing.JList<>(aspectdata);
        aspects_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        

        if(PSatAPI.instance.collectiveStrategy ==  CollectiveStrategy.NONE){
        	aspects_list.setSelectedIndex(0);
        }
        else if(PSatAPI.instance.collectiveStrategy ==  CollectiveStrategy.EG){
        	aspects_list.setSelectedIndex(1);
        }
        else if(PSatAPI.instance.collectiveStrategy ==  CollectiveStrategy.EEG){
        	aspects_list.setSelectedIndex(2);
        }
//        else if(PSatAPI.instance.collectiveStrategy ==  CollectiveStrategy.EEEG){
//        	aspects_list.setSelectedIndex(3);
//        }
        else if(PSatAPI.instance.collectiveStrategy ==  CollectiveStrategy.CG){
        	aspects_list.setSelectedIndex(3);
        }
//        else if(PSatAPI.instance.collectiveStrategy ==  CollectiveStrategy.CG){
//        	aspects_list.setSelectedIndex(4);
//        }
//        else if(PSatAPI.instance.collectiveStrategy ==  CollectiveStrategy.DG){
//        	aspects_list.setSelectedIndex(5);
//        }
        else{
        	aspects_list.setSelectedIndex(0);
        	PSatAPI.instance.collectiveStrategy = CollectiveStrategy.NONE;
        	ServerAssertionsFactory.clearAllAgentAssertions();
			PSatClient.netSerialiseConfigInstance();
        }
        
        aspects_sp.setViewportView(aspects_list);
        aspects_list.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent e) {
				int selectedindex = aspects_list.getSelectedIndex();
				
				if(selectedindex ==0){
					PSatAPI.instance.collectiveStrategy = CollectiveStrategy.NONE;
		        	ServerAssertionsFactory.clearAllAgentAssertions();
					PSatClient.netSerialiseConfigInstance();
				}
				else if(selectedindex ==1){
					PSatAPI.instance.collectiveStrategy = CollectiveStrategy.EG;
		        	ServerAssertionsFactory.clearAllAgentAssertions();
					PSatClient.netSerialiseConfigInstance();
				}
				else if(selectedindex ==2){
					PSatAPI.instance.collectiveStrategy = CollectiveStrategy.EEG;
		        	ServerAssertionsFactory.clearAllAgentAssertions();
					PSatClient.netSerialiseConfigInstance();
				}
//				else if(selectedindex ==3){
//					PSatAPI.instance.collectiveStrategy = CollectiveStrategy.EEEG;
//		        	ServerAssertionsFactory.clearAllAgentAssertions();
//					PSatClient.netSerialiseConfigInstance();
//				}
				else if(selectedindex == 3){
					PSatAPI.instance.collectiveStrategy = CollectiveStrategy.CG;
		        	ServerAssertionsFactory.clearAllAgentAssertions();
					PSatClient.netSerialiseConfigInstance();
				}
//				else if(selectedindex == 4){
//					PSatAPI.instance.collectiveStrategy = CollectiveStrategy.CG;
//		        	ServerAssertionsFactory.clearAllAgentAssertions();
//					PSatClient.netSerialiseConfigInstance();
//				}
//				else if(selectedindex == 5){
//					PSatAPI.instance.collectiveStrategy = CollectiveStrategy.DG;
//		        	ServerAssertionsFactory.clearAllAgentAssertions();
//					PSatClient.netSerialiseConfigInstance();
//				}				
			}        	
        });
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mode_label)
                    .addComponent(aspects_label)
                    .addComponent(type_label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(rolebased_rb)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                        .addComponent(instancebased_rb)
                        .addGap(31, 31, 31))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(aspects_sp)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(modes_sp)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(type_label)
                    .addComponent(rolebased_rb)
                    .addComponent(instancebased_rb))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mode_label)
                    .addComponent(modes_sp, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(aspects_sp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(aspects_label))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }                            
    
    private javax.swing.JLabel aspects_label;
    private javax.swing.JList<String> aspects_list;
    private javax.swing.JScrollPane aspects_sp;
    private javax.swing.JRadioButton instancebased_rb;
    private javax.swing.JLabel mode_label;
    private javax.swing.JList<String> modes_list;
    private javax.swing.JScrollPane modes_sp;
    private javax.swing.ButtonGroup prtype_bg;
    private javax.swing.JRadioButton rolebased_rb;
    private javax.swing.JLabel type_label;
}

class LogConfig extends javax.swing.JPanel {
	private static final long serialVersionUID = 1L;
	
    LogConfig() {
        initComponents();
    }
                       
    private void initComponents() {

        knowledgeTransLog_cb = new JCheckBox("Log object knowledge transformations");
        if(PSatAPI.instance.log_knowledge_transformation){
        	knowledgeTransLog_cb.setSelected(true);
		}
		else{
			knowledgeTransLog_cb.setSelected(false);
		}
        knowledgeTransLog_cb.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent arg0) {
	        	if(knowledgeTransLog_cb.isSelected()){
	        		PSatAPI.instance.log_knowledge_transformation = true;
	  	    	}	
	  	    	else{
	  	    		PSatAPI.instance.log_knowledge_transformation = false;
	  	    	}
	        	PSatClient.netSerialiseConfigInstance();
	          }          
			});
		
        knowledgeStateLog_cb1 = new JCheckBox("Log object knowledge state");
        if(PSatAPI.instance.log_agent_knowledge_state){
        	knowledgeStateLog_cb1.setSelected(true);
	    }
	    else{
	    	knowledgeStateLog_cb1.setSelected(false);
	    }
        knowledgeStateLog_cb1.addActionListener(new ActionListener() {
	          public void actionPerformed(ActionEvent arg0) {
	        	if(knowledgeStateLog_cb1.isSelected()){
	        		PSatAPI.instance.log_agent_knowledge_state = true;
	  	    	}	
	  	    	else{
	  	    		PSatAPI.instance.log_agent_knowledge_state = false;
	  	    	}
    	    	PSatClient.netSerialiseConfigInstance();

	          }          
			});
	            
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(knowledgeTransLog_cb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(knowledgeStateLog_cb1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(292, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(knowledgeTransLog_cb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(knowledgeStateLog_cb1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(202, Short.MAX_VALUE))
        );
    }// </editor-fold>                        


    private JCheckBox knowledgeStateLog_cb1;
    private JCheckBox knowledgeTransLog_cb;
}

class AnalysisConfig extends javax.swing.JPanel {
	private static final long serialVersionUID = 1L;
	
    AnalysisConfig() {
        initComponents();
    }

    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        decisionCatLimit_cb1 = new javax.swing.JComboBox<>();

        jLabel1.setText("Max. # of objects on a path:");
	    Integer maxPathLengths[] = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,
	    							22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,
	    							40,41,42,43,44,45,46,47,48,49,50};
	    maxPathLength_cb = new javax.swing.JComboBox<Integer>(maxPathLengths);
        for(int i=0;i<maxPathLengths.length;i++){
	    	if(maxPathLengths[i] == PSatAPI.instance.max_analysis_path_length){
	    		maxPathLength_cb.setSelectedIndex(i);		
	    	}
	    }	    
	    maxPathLength_cb.addItemListener(new ItemListener() {
	        public void itemStateChanged(ItemEvent itemEvent) {
	        	PSatAPI.instance.max_analysis_path_length = (Integer)maxPathLength_cb.getSelectedItem();
	        	PSatAPI.instance.no_agents = PSatAPI.instance.max_analysis_path_length;//remove to distinguish bw no of agents and path length
	        	PSatAPI.instance.k = PSatAPI.instance.max_analysis_path_length;//remove to distinguish bw no of agents and path length
	        	PSatClient.netSerialiseConfigInstance();
	          }
	        });
	    

        jLabel2.setText("Decision category limit:");
        decisionCatLimit_cb1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "cat3[NO]", "cat2[MAYBE]", "cat1[YES]" }));

        if(RecommendationPanel.limit ==1){
        	decisionCatLimit_cb1.setSelectedIndex(2);
        }
        else if(RecommendationPanel.limit ==2){
        	decisionCatLimit_cb1.setSelectedIndex(1);
        }
        else if(RecommendationPanel.limit ==3){
        	decisionCatLimit_cb1.setSelectedIndex(0);
        }
        decisionCatLimit_cb1.addItemListener(new ItemListener() {
	        public void itemStateChanged(ItemEvent itemEvent) {
	        	String cat = (String)decisionCatLimit_cb1.getSelectedItem();
	        	if(cat.equals("cat1[YES]")){
	        		RecommendationPanel.limit=1;
	            }
	            else if(cat.equals("cat2[MAYBE]")){
	        		RecommendationPanel.limit=2;
	            }
	            else if(cat.equals("cat3[NO]")){
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
	    
        
        
        
        
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(maxPathLength_cb, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(38, 38, 38)
                        .addComponent(decisionCatLimit_cb1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(244, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(maxPathLength_cb, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(decisionCatLimit_cb1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(189, Short.MAX_VALUE))
        );
    }// </editor-fold>                        


    private javax.swing.JComboBox<String> decisionCatLimit_cb1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JComboBox<Integer> maxPathLength_cb;
}


