package gla.prisoft.client.kernel.display.model;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

import gla.prisoft.client.Display;
import gla.prisoft.client.PSatClient;
import gla.prisoft.server.PSatAPI;
import gla.prisoft.server.kernel.behaviour.InformationFlows;
import gla.prisoft.shared.DecisionCategory;
import gla.prisoft.shared.PSatTableResult;

/**
 *
 * @author inah
 */
public class RecommendationPanel extends javax.swing.JPanel {
	private static final long serialVersionUID = 1L;

    /**
     * Creates new form RecommendationPanel
     */
    public RecommendationPanel(LayeredBarChart associatedChart) {
    	this.associatedChart = associatedChart;
    	
    	if(updateThread !=null){
    		updateThread.interrupt();
    	}
    	if(checkThread !=null){
    		checkThread.interrupt();
    	}
    	setOriginalPrivacyGoal();
        initComponents();
    }

    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
//        jComboBox1 = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
//        jLabel6 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        
        if(limit ==DecisionCategory.CAT1){
        	jLabel1.setText("Cat.Limit:"+DecisionCategory.getCatDescription(DecisionCategory.CAT1));        	
        }
        else if(limit ==DecisionCategory.CAT2){
        	jLabel1.setText("Cat.Limit:"+DecisionCategory.getCatDescription(DecisionCategory.CAT2));        	
        }
        else if(limit ==DecisionCategory.CAT3){
        	jLabel1.setText("Cat.Limit:"+DecisionCategory.getCatDescription(DecisionCategory.CAT3));        	
        }
//        else if(limit ==DecisionCategory.CAT4){
//        	jLabel1.setText("Cat.Limit:"+DecisionCategory.getCatDescription(DecisionCategory.CAT4));        	
//        }
//        else if(limit ==DecisionCategory.CAT5){
//        	jLabel1.setText("Cat.Limit:"+DecisionCategory.getCatDescription(DecisionCategory.CAT5));        	
//        }
//        else if(limit ==DecisionCategory.CAT6){
//        	jLabel1.setText("Cat.Limit:"+DecisionCategory.getCatDescription(DecisionCategory.CAT6));        	
//        }        
                       
        model = new RecommendationsTableModel();

		model.setRowCount(0);

		table = new JTable(model);

		table.setFont(new Font("Verdana", Font.PLAIN, 10));
		((DefaultTableCellRenderer)table.getTableHeader().getDefaultRenderer())
		.setHorizontalAlignment(JLabel.LEFT);
		table.getTableHeader().setFont(new Font("Verdana", Font.BOLD, 10));

		TableColumn column = null;
		for (int n = 0; n < 4; n++) {
			column = table.getColumnModel().getColumn(n);
			if (n == 0) 
				column.setMaxWidth(110);
			if (n == 1)
				column.setMaxWidth(800);
			if (n == 2)
				column.setMaxWidth(200);
			if (n == 3)
				column.setMaxWidth(150);
		}
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );
        table.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
    	table.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );
    	table.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );

    	JTableHeader header = table.getTableHeader();
    	header.setDefaultRenderer(new HeaderRenderer(table));
    	
		JScrollPane scrollPane = new JScrollPane(table);
		JPanel tablePanel = new JPanel(new GridLayout());
		tablePanel.add(scrollPane);
		tablePanel.setBackground(Color.red);
		tablePanel.setPreferredSize(new Dimension(100,300));
        
        jButton1.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(pause){
					pause = false;
				}
				else{
					pause = true;
				}				
			}        	
        });        
        
        jLabel2.setText("Optimal privacy goal(v'):"+Display.RoundTo3Decimals(PSatAPI.instance.currentPrivacyGoal.get(PSatAPI.instance.currentPath)));
//        jLabel2.setText("Optimal privacy goal(v'):"+String.format("%.2f", PSatAPI.instance.currentPrivacyGoal.get(PSatAPI.instance.currentPath)));
        jLabel3.setText("#iterations="+Display.noiterations);//+" |"+detRecomputeText());
        jLabel4.setText("Original privacy goal(v):"+String.format("%.2f", PSatAPI.instance.originalPrivacyGoal.get(PSatAPI.instance.currentPath)));
//        jLabel6.setText("α convergence search for pr:");
        
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
//                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
//                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addGap(39, 39, 39)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addGap(24, 24, 24))
                    .addGroup(layout.createSequentialGroup()
//                        .addComponent(jLabel6)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(tablePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
//                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
//                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jButton1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tablePanel)
                .addGap(5, 5, 5))
        );

        jLabel2.getAccessibleContext().setAccessibleName("v'");
		
        displayed = false;
        updateTable();
        checkstatus();
        
        
    }// </editor-fold>                        

    private void setOriginalPrivacyGoal(){
//    	if(!Display.opgset ||Display.instance.originalPrivacyGoal.size() ==0){
//    		
//    		Display.opgset = true;
//    	}
    	boolean pathexist = false;
		for(String pkey: PSatAPI.instance.originalPrivacyGoal.keySet()){
			if(pkey.equals(PSatAPI.instance.currentPath));
			pathexist = true;
		}		
		if(!pathexist){
			double originalgoal = InformationFlows.suggestOriginalCollectiveGoalValue(PSatAPI.instance);
			PSatAPI.instance.originalPrivacyGoal.put(PSatAPI.instance.currentPath, originalgoal);
			PSatClient.netSerialiseConfigInstance();
		}
    	
    }
    private void iterate(){    	
    	PSatAPI.instance.costTradeoff = 1;
    	double newgoalvalue = -1;
    	
    	if(pathsatvalues !=null && pathsatvalues.size()>0){
    		double maxpathsatvalues = 0;
        	
        	for(double val:pathsatvalues){
        		if(val >maxpathsatvalues){
        			maxpathsatvalues = val;
        		}
        	}        	
        	if((int)maxpathsatvalues < PSatAPI.instance.currentPrivacyGoal.get(PSatAPI.instance.currentPath).intValue()){
        		newgoalvalue = maxpathsatvalues;
        	}
        	else{
        		newgoalvalue = PSatAPI.instance.currentPrivacyGoal.get(PSatAPI.instance.currentPath) - 0.05;
        	}
        	
    	}
    	else{
    		newgoalvalue = PSatAPI.instance.currentPrivacyGoal.get(PSatAPI.instance.currentPath) - 0.05;
    	}
//		newgoalvalue = PSatAPI.instance.currentPrivacyGoal.get(PSatAPI.instance.currentPath) - 0.05;    	
		InformationFlows.resetGlobalGoalForAllPathAgents(PSatAPI.instance, PSatAPI.instance.currentPath, newgoalvalue);
		
		Display.window.runSatAnalysis();

    }
    
    int counter = 10;
    boolean checkerrunning;
    private static Thread checkThread;
    private void checkstatus(){
    	if(!checkerrunning){
    		checkerrunning = true;
    		
    		if(PSatAPI.instance.currentPrivacyGoal.get(PSatAPI.instance.currentPath) <=0.05){
				jButton1.setText("convergence failed");
				jButton1.setForeground(Color.BLACK);
				jButton1.setBackground(new Color(232,93,57));
		        jLabel3.setText("#iterations="+Display.noiterations+"| Convergence cannot be achieved with selected disclosure protocols");
		        jLabel2.setText("Optimal privacy goal(v'):"+Display.RoundTo3Decimals(PSatAPI.instance.currentPrivacyGoal.get(PSatAPI.instance.currentPath)));
			}
    		else{
        		checkThread = new Thread() {
        			public void run() {
        				boolean counting = false;
        				while(true && !this.isInterrupted()){
        					if(ufin || counting){
        						boolean nanfound = false;
    				        	for(Recommendation rec:recs){    		
    				        		if(rec.recommendedprotocols.contains("?")){
    				        			nanfound = true;
    				        			break;
    				        		}
    				        	}
    				        	if(!pause){
    				        		if(nanfound){
    				        			jButton1.setText("iterate "+counter+" [parse]");
    				        			jButton1.setForeground(Color.RED);
    				        			jButton1.setBackground(jLabel1.getBackground());
    				        			jButton1.setEnabled(true);
    				        			
    				        			if(counter <=0 && !PSatAPI.instance.runningTraining && ufin){
    				        				Display.noiterations = Display.noiterations+1;
    				        				iterate();	
    				        				counter = 10;
    				        				counting = false;
    				        			}
    				        			else{
    				        				counting = true;
    				        			}
    			        				counter = counter -1;			        				
    				        		}
    				        		else{
    				        			jButton1.setText("converged");
    				        	        jLabel3.setText("#iterations="+Display.noiterations);

    				        			jButton1.setBackground(new Color(81,193,11));
    				        		}
    				        	}
    				        	else{
    			        			jButton1.setText("iterate "+counter+" [start]");
    			        			jButton1.setForeground(Color.RED.brighter().brighter());
    				        	}
    			        		
    			        		if(!counting){
    				            	ufin = false;			        			
    			        		}
    			        	}
        					
            				try {
    							Thread.sleep(1000);
    						} catch (InterruptedException e1) {
    							e1.printStackTrace();
    						}
        				}    				
        			}
        		};
        		checkThread.start(); 
    		}    		    		
    	}    
    }
    
 
    private boolean ufin;
    private boolean displayed;
    private static Thread updateThread;
    private void updateTable(){
//    	updateThread = new Thread() {
//			public void run() {
//				while(true && !this.isInterrupted()){
					if(!PSatAPI.instance.runningTraining && !pause){
						if(!displayed || limitchanged){
							model.setRowCount(0);

				        	recs = getPathProtocolRecommendations();
				    		for(Recommendation rec:recs){
				        		String flow = "<html>"+rec.sendername+"&rarr;"+rec.recipientname+"</html>";
				        		String protocols = "<html>"+rec.recommendedprotocols+"</html>";
				        		model.addRow(new Object[]{flow,protocols,rec.decisioncats,rec.dof});
				        		model.fireTableDataChanged();
				        		Rectangle cellBounds = table.getCellRect(table.getRowCount() - 1, 0, true);
				        		table.scrollRectToVisible(cellBounds);		        		       	
				        	}
				    		try {
								Thread.sleep(1000);
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							limitchanged = false;
							displayed = true;
				    		ufin=true;
						}
						
					}					
//				}
//			}
//		};
//		updateThread.start();	

    }
        
    private ArrayList<Recommendation> getPathProtocolRecommendations(){
    	ArrayList<Recommendation> recs = new ArrayList<Recommendation>();
    	pathsatvalues = new ArrayList<Double>();
    	
    	for(Flow flow:associatedChart.flows){
    		
			ArrayList<String> recommendedprotocols = new ArrayList<String>();
			ArrayList<String> cats = new ArrayList<String>();
			double dof = 0;
			
			if(limit>=1){
				if(flow.cat1Ptrs.size()>0){
					for(PSatTableResult ptr:flow.cat1Ptrs){
						recommendedprotocols.add(ptr.getProtocolColumn()); 
						pathsatvalues.add(ptr.getPathSat());
					}
					double cat1dof = (double)flow.cat1Ptrs.size()/(double)PSatAPI.instance.evaluatedProtocols.length;
					cats.add(DecisionCategory.getCatDescription(DecisionCategory.CAT1)+"("+Display.RoundTo2Decimals(cat1dof)+")");
				}				
			}
			if(limit>=2){
				if(flow.cat2Ptrs.size()>0){
					for(PSatTableResult ptr:flow.cat2Ptrs){
						recommendedprotocols.add(ptr.getProtocolColumn());
						pathsatvalues.add(ptr.getPathSat());
					}
					double cat2dof = (double)flow.cat2Ptrs.size()/(double)PSatAPI.instance.evaluatedProtocols.length;
					cats.add(DecisionCategory.getCatDescription(DecisionCategory.CAT2)+"("+Display.RoundTo2Decimals(cat2dof)+")");
				}				
			}
			if(limit>=3){
				if(flow.cat3Ptrs.size()>0){
					for(PSatTableResult ptr:flow.cat3Ptrs){
						recommendedprotocols.add(ptr.getProtocolColumn());
						pathsatvalues.add(ptr.getPathSat());
					}
					double cat3dof = (double)flow.cat3Ptrs.size()/(double)PSatAPI.instance.evaluatedProtocols.length;
					cats.add(DecisionCategory.getCatDescription(DecisionCategory.CAT3)+"("+Display.RoundTo2Decimals(cat3dof)+")");
				}				
			}
//			if(limit>=4){
				if(flow.cat4Ptrs.size()>0){
					for(PSatTableResult ptr:flow.cat4Ptrs){
//						recommendedprotocols.add(ptr.getProtocolColumn());
						pathsatvalues.add(ptr.getPathSat());
					}
//					cats.add(DecisionCategory.getCatDescription(DecisionCategory.CAT4));
				}				
//			}
//			if(limit>=5){
				if(flow.cat5Ptrs.size()>0){
					for(PSatTableResult ptr:flow.cat5Ptrs){
//						recommendedprotocols.add(ptr.getProtocolColumn());
						pathsatvalues.add(ptr.getPathSat());
					}
//					cats.add(DecisionCategory.getCatDescription(DecisionCategory.CAT5));
				}				
//			}
//			if(limit==6){
				if(flow.cat6Ptrs.size()>0){
					for(PSatTableResult ptr:flow.cat6Ptrs){
//						recommendedprotocols.add(ptr.getProtocolColumn());
						pathsatvalues.add(ptr.getPathSat());
					}
//					cats.add(DecisionCategory.getCatDescription(DecisionCategory.CAT6));
				}				
//			}
			
			if(recommendedprotocols.size()>0){
				dof = (double)recommendedprotocols.size()/(double)PSatAPI.instance.evaluatedProtocols.length;
				dof = Display.RoundTo3Decimals(dof);
			}
			else{
				dof = 0;
			}
			

    		String[] sandr = flow.senderToRecipient.split("->");
    		String sendername = sandr[0];
    		String recipientname = sandr[1];
    		
    		String p2 ="";
    		if(recommendedprotocols.size() >0){
    			for(String protocoldesc:recommendedprotocols){
    	    		String[] p1 = protocoldesc.split("=");
    	        	p2 =p2+ p1[0].replace("<html>", " ");
    	    	}	
    			
    		}
    		else{
    			p2 = "?";
    		}
    		
    		String decisions ="";
    		if(cats.size() >0){
    			for(String cat:cats){
    				decisions=decisions +cat+" ";
    	    	}	
    			
    		}
    		else{
    			decisions = "?";
    		}
    		
    		Recommendation rec = new Recommendation();
    		rec.sendername = sendername;
    		rec.recipientname = recipientname;
    		rec.recommendedprotocols = p2;
    		rec.decisioncats= decisions;
    		rec.dof= dof;
    		
    		recs.add(rec);
    		
    	}
    	return recs;
    }
    
//    private String detRecomputeText(){
//    	String text = "";
////    	if(Display.instance.currentPrivacyGoal.get(Display.instance.currentPath) <=0.01 && Display.instance.costTradeoff<=0.01){
////    		text = "convergence failed";
////    	}
////    	else if(Display.instance.costTradeoff <= 0.01){
////    		text = "next iteration@∆v=0.05";
////		}
////		else{
////			text = "next iteration@ ∆ω=0.05";
////		}
//    	if(PSatAPI.instance.currentPrivacyGoal.get(PSatAPI.instance.currentPath) <=0.01){
//    		text = "convergence failed";
//    	}
//		else{
//    		text = "next iteration@∆v=0.05";
//		}
//    	return text;
//    }

    class Recommendation{
    	String sendername;
    	String recipientname;
    	String recommendedprotocols;
    	String decisioncats;
    	double dof = Double.NaN;
    }
    
    class RecommendationsTableModel extends DefaultTableModel{
		private static final long serialVersionUID = -4775356027813366490L;

		public RecommendationsTableModel() {
			super(new String[]{"Information-flow", "<html>Recommended Disclosure Protocol(s)</html>", "Decision cat(s)","Degree-of-Freedom"}, 0);
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Class<?> getColumnClass(int columnIndex) {
			Class clazz = String.class;
			switch (columnIndex) {
			case 0:
				clazz = String.class;
				break;
			case 1:
				clazz = String.class;
				break;
			case 2:
				clazz = String.class;
				break;
			case 3:
				clazz = Double.class;
				break;
			}
			return clazz;
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			switch(column){             
	           case 0:  // select the cell you want make it not editable 
	             return false;
	           case 1:
	        	   return false;
	           case 2:
	        	   return false;
	           case 3:
	        	   return false;
	           default: 
	        	   return true;
	        } 
		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		public void setValueAt(Object aValue, int row, int column) {
			if(aValue instanceof String && column ==0){
				Vector rowData = (Vector)getDataVector().get(row);																				
				rowData.set(0, (String)aValue);
				fireTableCellUpdated(row, column);				
			}
			else if (aValue instanceof String && column == 1) {
				Vector rowData = (Vector)getDataVector().get(row);																				
				rowData.set(1, (String)aValue);
				fireTableCellUpdated(row, column);	
			}
			else if (aValue instanceof String && column == 2) {
				Vector rowData = (Vector)getDataVector().get(row);																				
				rowData.set(1, (String)aValue);
				fireTableCellUpdated(row, column);	
			}
			else if (aValue instanceof Double && column == 3) {
				Vector rowData = (Vector)getDataVector().get(row);																				
				rowData.set(1, (Double)aValue);
				fireTableCellUpdated(row, column);	
			}
		}

	}
    
    private static class HeaderRenderer implements TableCellRenderer {

        DefaultTableCellRenderer renderer;

        public HeaderRenderer(JTable table) {
            renderer = (DefaultTableCellRenderer)
                table.getTableHeader().getDefaultRenderer();
            renderer.setHorizontalAlignment(JLabel.CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int col) {
            return renderer.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, col);
        }
    }
    
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JButton jButton1;
//    private javax.swing.JLabel jLabel6;
//    private javax.swing.JComboBox<String> jComboBox1;
    public static int limit = 3;
	private LayeredBarChart associatedChart;
//	private HashMap<String,Integer> activecat;
//	private HashMap<String,Integer> activecatindex;
	private JTable table;
	private RecommendationsTableModel model;
	private boolean pause;
	private ArrayList<Recommendation> recs;
	private boolean limitchanged;
	private ArrayList<Double> pathsatvalues;


}