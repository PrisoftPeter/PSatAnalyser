package gla.prisoft.client.kernel.display.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.jfree.data.general.SeriesException;

import gla.prisoft.server.PSatAPI;
import gla.prisoft.shared.CollectiveStrategy;
import gla.prisoft.shared.ConfigInstance;
import gla.prisoft.shared.PSatTableResult;
import gla.prisoft.shared.RowType;

/**
 *
 * @author inah
 */

public class FeasibilityView extends javax.swing.JPanel {

	private static final long serialVersionUID = 1L;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private DefaultTableModel dtm;
    public TimeSeriesChart timeSeriesChart;
    private JLabel reqDescLabel;
	public static String export_file_path;
	private boolean export;
	private static int f_counter = 1;
	private File exportFile;
	public static double sumCollectiveGoal;
	public static double collectiveGoalCount;
    public static ArrayList<PSatTableResult> ptrs;
    public static String reqDesc;
    public static String path;
    /**
     * Creates new form NewJPanel
     */
    public FeasibilityView(ConfigInstance instance) {
    	if(export_file_path == null && !export){
    		int n = JOptionPane.showConfirmDialog(this,
        		    "Export Pr analysis",
        		    "Export", JOptionPane.YES_NO_OPTION);
        	
        	if(n == 0){
        		export_file_path = DatastoreChooser.show("select export folder", false);
    			createExportStore(instance);
    			export = true;
        	}
        	else{
        		export = false;
        	}
    	}
    	else if(export_file_path != null){
    		createExportStore(instance);
			export = true;
    	}
    	else{
    		export = false;
    	}
    	
        initComponents(instance);
        
        sumCollectiveGoal=0;
        collectiveGoalCount =0;
        
        countpathsatgtgoalno=0;
        countpathsatltgoalyes=0;
        countpathsatgtgoalmaybe=0;
        countpathsatltgoalmaybe=0;
        totalno=0;
        totalyes=0;
        totalmaybe=0;
        ptrs = new ArrayList<PSatTableResult>();
    }

    private void initComponents(final ConfigInstance instance) {  	
        
        jPanel2 = new javax.swing.JPanel();
        
        reqDescLabel = new JLabel("Pr:");

        jScrollPane1 = new javax.swing.JScrollPane();      
                
        jTable1 = new javax.swing.JTable();
        
        dtm = new DefaultTableModel(0,0) {
			private static final long serialVersionUID = 1L;
			@Override
            public boolean isCellEditable(int row, int column) {
               return false;
            }
        };
        String tableheader[] = null;
//		if(!instance.isModeEntropy && !instance.isModeCommonKnowledge){
		if(!instance.isModeEntropy){
        	tableheader = new String [] {	
        			"<html>Path</html>",
        			"<html>Flow</html>", 
            		"<html>Protocol:&#945;</html>", 
            		"<html>su:sat(<i>pr<i/>)</html>", 
            		"<html>s:sat(<i>pr<i/>)</html>", 
            		"<html>r:sat(<i>pr<i/>)</html>", 
            		"<html>PathSat(<i>pr<i/>)</html>", 
            		"<html>Assertion</html>",
            		"<html>Goal(v)</html>",  //collective goal value
            		"<html>Benefit</html>",  
            		"<html>Cost(<i>pr<i/>,&#945;)</html>", 
            		"<html>Feasibility(<i>pr<i/>,&#945;)</html>", 
            		"<html>Decision</html>"
            };
        }
		else{
			tableheader = new String [] {	
					"<html>Path</html>",
					"<html>Flow</html>", 
            		"<html>Protocol:&#945;</html>", 
//            		"<html>su:sat(<i>pr<i/>)</html>", 
//            		"<html>s:sat(<i>pr<i/>)</html>", 
//            		"<html>r:sat(<i>pr<i/>)</html>", 
            		"<html>PathSat(<i>pr<i/>)</html>", 
            		"<html>Assertion</html>",
            		"<html>Goal(v<sub>pr</sub>)</html>", //collective goal value
            		"<html>Benefit</html>",  
            		"<html>Cost(<i>pr<i/>,&#945;)</html>", 
            		"<html>Feasibility(<i>pr<i/>,&#945;)</html>", 
            		"<html>Decision</html>"
            };
		}
		dtm.setColumnIdentifiers(tableheader);
        jTable1.setModel(dtm);  
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
//        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
//        leftRenderer.setHorizontalAlignment( JLabel.LEFT );  
    	jTable1.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
//        if(instance.isModeEntropy || instance.isModeCommonKnowledge){
        if(instance.isModeEntropy){
        	jTable1.getColumnModel().getColumn(5).setCellRenderer( centerRenderer );
        	jTable1.getColumnModel().getColumn(6).setCellRenderer( centerRenderer );
        	jTable1.getColumnModel().getColumn(7).setCellRenderer( centerRenderer );
        	jTable1.getColumnModel().getColumn(8).setCellRenderer( centerRenderer );
        	jTable1.getColumnModel().getColumn(9).setCellRenderer( centerRenderer );

        }
        else{
        	jTable1.getColumnModel().getColumn(8).setCellRenderer( centerRenderer );
            jTable1.getColumnModel().getColumn(9).setCellRenderer( centerRenderer );
            jTable1.getColumnModel().getColumn(10).setCellRenderer( centerRenderer );
            jTable1.getColumnModel().getColumn(11).setCellRenderer( centerRenderer );
            jTable1.getColumnModel().getColumn(12).setCellRenderer( centerRenderer );
        }
             
        jScrollPane1.setViewportView(jTable1);
        
        timeSeriesChart = new TimeSeriesChart();

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
//                	.addComponent(exportButton)
                	.addComponent(reqDescLabel)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 656, Short.MAX_VALUE)
                    .addComponent(timeSeriesChart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        
        
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                //.addGap(18, 18, 18)
//                .addComponent(exportButton)
            	.addComponent(reqDescLabel)
            	.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 218, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(timeSeriesChart, javax.swing.GroupLayout.DEFAULT_SIZE, 580, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 680, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 506, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        
        this.setVisible(true);
    }    
    
    public void clearTable(){
    	dtm.setRowCount(0);
    }
        
    public static int countpathsatgtgoalno;
    public static int countpathsatltgoalyes;
    public static int countpathsatgtgoalmaybe;
    public static int countpathsatltgoalmaybe;
    public static int totalno;
    public static int totalyes;
    public static int totalmaybe;
    public static String prdesc;
    public void addTableRow(ConfigInstance instance, PSatTableResult ptr){
    	ptrs.add(ptr);
    	
    	String f_s = "";
		if(ptr.getFeasibility() > 100){
			f_s = "∞";
		}
		else{
			f_s = ptr.getFeasibilityColumn();
		}
		
    	if(ptr.getRowType() == RowType.MF){    		
//    		prdesc = ptr.getRequirementHtmlFullDesc()+", COST Tradeoff:"+String.format("%.2f", Display.instance.costTradeoff);
    		prdesc = "pr:";
    		if(instance.collectiveStrategy != CollectiveStrategy.NONE){
    			String ckdescs [] = ptr.getRequirementHtmlFullDesc().split(" Actual");
    			prdesc = prdesc+ ckdescs[0];
    			//prdesc = prdesc;
    		}
    		else{
        		prdesc = prdesc+ ptr.getRequirementHtmlFullDesc();
    		}
    		prdesc= prdesc.replace("<html>", " ");
    		prdesc= prdesc.replace("<body>", " ");
    		prdesc= prdesc.replace("</html>", " ");
    		prdesc= prdesc.replace("</body>", " ");
    		prdesc = "<html><body>"+prdesc+"</body></html>";		
    		reqDescLabel.setText(prdesc);
    		reqDesc = ptr.getRequirementHtmlFullDesc();
    		path = ptr.getPath();
//    		if(!instance.isModeEntropy && !instance.isModeCommonKnowledge){
    		if(!instance.isModeEntropy){
    			dtm.addRow(new Object[] { 	
            			ptr.getPath(),
            			ptr.getFlowColumn(), 
                		ptr.getProtocolColumn(), 
                		ptr.getSuSatColumn(), 
                		ptr.getsSatColumn(), 
                		ptr.getrSatColumn(), 
                		ptr.getPathSatColumn(), 
                		ptr.getAssertionColumn(),
                		String.format("%.2f",ptr.getVgoal()),
                		ptr.getBenefit(),
                		ptr.getCostColumn(), 
                		f_s, 
                		ptr.getDecisionColumn()
                });
            }
    		else{
    			dtm.addRow(new Object[] {
    					ptr.getPath(),
    					ptr.getFlowColumn(), 
                		ptr.getProtocolColumn(), 
//                		ptr.getSelfSatColumn(), 
//                		ptr.getsSatColumn(), 
//                		ptr.getrSatColumn(), 
                		ptr.getPathSatColumn(), 
                		ptr.getAssertionColumn(), 
                		String.format("%.2f",ptr.getVgoal()),
                		ptr.getBenefit(),
                		ptr.getCostColumn(), 
                		f_s, 
                		ptr.getDecisionColumn()
                });
    		}
    		//add data to timeseries
    		try {
				timeSeriesChart.insertData1(ptr.getPathSat());
				timeSeriesChart.insertData2(ptr.getFeasibility());
				timeSeriesChart.insertData3(ptr.getCost());
				timeSeriesChart.insertData4(ptr.getBenefit());
			} catch (SeriesException e) {
				//e.printStackTrace();
			}
    		
    		if(export){
    			exportptr(ptr);
    		}
    	}
    	else if(ptr.getRowType() == RowType.EMPTY){
    		dtm.addRow(new Object[] {
        			null,
        			null, 
        			null, 
        			null,
        			null, 
        			null,
        			null,
        			null,
        			null,
        			null,
        			null,
        			null,
        			null
            });
    	}
    	jTable1.scrollRectToVisible(jTable1.getCellRect(jTable1.getRowCount()-1, 0, true));

    	if(ptr.getDecision() !=null && ptr.getDecision().equals("no")){
			FeasibilityView.totalno = FeasibilityView.totalno+1;
			if(ptr.getPathSat()>=ptr.getVgoal()){
				FeasibilityView.countpathsatgtgoalno = FeasibilityView.countpathsatgtgoalno+1;
			}
		}
		else if(ptr.getDecision() !=null && ptr.getDecision().equals("yes")){
			FeasibilityView.totalyes = FeasibilityView.totalyes+1;
			if(ptr.getPathSat()<=ptr.getVgoal()){
				FeasibilityView.countpathsatltgoalyes = FeasibilityView.countpathsatltgoalyes+1;
			}
		}
		else if(ptr.getDecision() !=null && ptr.getDecision().equals("maybe")){
			FeasibilityView.totalmaybe = FeasibilityView.totalmaybe+1;
			if(ptr.getPathSat()>=ptr.getVgoal()){
				FeasibilityView.countpathsatgtgoalmaybe = FeasibilityView.countpathsatgtgoalmaybe+1;
			}
			else if(ptr.getPathSat()<ptr.getVgoal()){
				FeasibilityView.countpathsatltgoalmaybe = FeasibilityView.countpathsatltgoalmaybe+1;
			}
		}
    	
    	if(ptr.getVgoal()>0){
    		sumCollectiveGoal = sumCollectiveGoal+ptr.getVgoal();
    		collectiveGoalCount = collectiveGoalCount+1;
    	}
    }
    
	public void createExportStore(ConfigInstance instance){
		String sessionid = instance.sessionid;
		String folderName1 = export_file_path+"/"+sessionid;
		String folderName2 = export_file_path+"/"+sessionid+"/prfeasibility";
		String fileName ="";
		if(instance.is_role_run){
			fileName = folderName2+"/role_iter"+f_counter+".csv";	
		}
		else{
			fileName = folderName2+"/instance_iter"+f_counter+".csv";
		}
		f_counter = f_counter +1;
		
		File folder1 = new File(folderName1);
		boolean exist1 = false;
		if(folder1.exists()){
			if(folder1.isDirectory()){
				exist1 = true;
			}				
		}
		if(!exist1){
			folder1.mkdir();
		}
		
		File folder2 = new File(folderName2);
		boolean exist2 = false;
		if(folder2.exists()){
			if(folder2.isDirectory()){
				exist2 = true;
			}				
		}
		if(!exist2){
			folder2.mkdir();
		}

		exportFile = new File(fileName);
		if(exportFile.exists()){
			exportFile.delete();
		}

    	try {
    		exportFile.createNewFile();

    		FileWriter writer = new FileWriter(exportFile, true);
   		 
    		
    		writer.append("Pr");
    		writer.append(',');
    		writer.append("Path");
    	    writer.append(',');
    	    writer.append("Flow");
    	    writer.append(',');
    	    writer.append("Protocol");
    	    writer.append(',');
    	    if(!instance.isModeEntropy){
    	    	writer.append("su:sat");
        	    writer.append(',');
        	    writer.append("s:sat");
        	    writer.append(',');
        	    writer.append("r:sat");
        	    writer.append(',');
    		}    	    
    	    writer.append("PathSat");
    	    writer.append(',');
    	    writer.append("Assertion");
    	    writer.append(',');
    	    writer.append("CollectiveGoal(v)");
    	    writer.append(',');
    	    writer.append("Benefit");
    	    writer.append(',');
    	    writer.append("Cost");
    	    writer.append(',');
    	    writer.append("Feasibility");
    	    writer.append(',');
    	    writer.append("Decision");
    	    writer.append('\n');
    	    
    	    writer.flush();
    	    writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  
	
	}
	
	public void exportptr(PSatTableResult ptr){

		try {

			FileWriter writer = new FileWriter(exportFile, true);

			String pr = ptr.getRequirementHtmlFullDesc();
			pr = pr.replace("<html><body>", "");
			pr = pr.replace("</body></html>", "");
			pr = pr.replace("<i>", "");
			pr = pr.replace("</i>", "");
			pr = pr.replace("<sub>", "_(");
			pr = pr.replace("</sub>", "_)");
    	    writer.append(pr);
			writer.append(',');
			writer.append(ptr.getPath());
    	    writer.append(',');
    	    writer.append(ptr.getFlow());
    	    writer.append(',');
    	    String protocol =ptr.getProtocol();
    	    protocol = protocol.replace(",", ";");
    	    protocol = protocol.replace("&#945;","");
    	    protocol = protocol.replace("<sub>","");
    	    protocol = protocol.replace("</sub>","");
    	    writer.append(protocol);
    	    writer.append(',');
    	    if(!PSatAPI.instance.isModeEntropy){
    	    	if(ptr.getSuSat() <0){
    	    		writer.append("");
        		}
    	    	else{
    	    		writer.append(""+ptr.getSuSat());
    	    	}
        	    writer.append(',');
        	    if(ptr.getsSat() <0){
    	    		writer.append("");
        		}
    	    	else{
    	    		writer.append(""+ptr.getsSat());
    	    	}
        	    writer.append(',');
        	    if(ptr.getrSat() <0){
    	    		writer.append("");
        		}
    	    	else{
    	    		writer.append(""+ptr.getrSat());
    	    	}
        	    writer.append(',');
    	    }
    	    
    	    writer.append(""+ptr.getPathSat());
    	    writer.append(',');
    	    String assertion = ptr.getAssertion();
    	    assertion = assertion.replace("<i>", "");
    	    assertion = assertion.replace("</i>", "");
    	    assertion = assertion.replace("<sub>", "_(");
    	    assertion = assertion.replace("</sub>", "_)");
    	    assertion = assertion.replace("Â", "");
    	    writer.append(assertion);
    	    writer.append(',');
    	    writer.append(""+ptr.getVgoal());
    	    writer.append(',');
    	    writer.append(""+ptr.getBenefit());
    	    writer.append(',');
    	    writer.append(""+ptr.getCost());
    	    writer.append(',');
    	    if(ptr.getFeasibility() <0){
    	    	writer.append("na");
    	    }
    	    else{
    	    	writer.append(""+ptr.getFeasibility());
    	    }    	    
    	    writer.append(',');
    	    writer.append(ptr.getDecision());
    	    writer.append('\n');
			
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
