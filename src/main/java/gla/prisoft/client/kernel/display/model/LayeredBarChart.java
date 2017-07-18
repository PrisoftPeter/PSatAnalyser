package gla.prisoft.client.kernel.display.model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LayeredBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.general.DatasetUtilities;

import gla.prisoft.client.Display;
import gla.prisoft.server.PSatAPI;
import gla.prisoft.shared.ConfigInstance;
import gla.prisoft.shared.DecisionCategory;
import gla.prisoft.shared.PSatTableResult;

public class LayeredBarChart extends javax.swing.JPanel {
	private static final long serialVersionUID = 5693232081699795422L;
	
	public ArrayList<Flow> flows;
	public LayeredBarChart() {
		flows = new ArrayList<Flow>();
		
    	//extract unique flows
    	ArrayList<String> uniqueFlowso = new ArrayList<String>();
    	for(PSatTableResult ptr: FeasibilityView.ptrs){
    		boolean exist = false;
    		for(String s:uniqueFlowso){
    			if(s !=null && ptr != null && ptr.getFlow() !=null && s.equals(ptr.getFlow())){
    				exist = true;
    				break;
    			}
    		}
    		if(!exist && ptr != null&& ptr.getFlow() != null){
				uniqueFlowso.add(ptr.getFlow());
			}
    	}
    	String[] uniqueFlows = new String[uniqueFlowso.size()];
    	uniqueFlows = uniqueFlowso.toArray(uniqueFlows);
    	
    	double currentGoal = FeasibilityView.sumCollectiveGoal/FeasibilityView.collectiveGoalCount;
    	if(Double.isNaN(currentGoal)){
    		currentGoal = 0;
    	}
    	PSatAPI.instance.currentPrivacyGoal.put(PSatAPI.instance.currentPath, currentGoal);
    	String goalv = String.format("%.5f", currentGoal);
    	
        String reqDesc = FeasibilityView.prdesc;
        reqDesc= reqDesc.split(", COST")[0];
        reqDesc = reqDesc.replace("<html>", " ");
    	reqDesc = reqDesc.replace("</html>", " ");
    	reqDesc = reqDesc.replace("<i>", "");
    	reqDesc = reqDesc.replace("</i><sub>", "_");
    	reqDesc = reqDesc.replace("</sub><i>", "_");
    	reqDesc = reqDesc.replace("</i> ", "");
    	reqDesc = reqDesc.replace("<body>", "");
    	reqDesc = reqDesc.replace("</sub>", "_");

        LayeredBarChart.createExportFile(PSatAPI.instance);
        
    	//sort ptrs
    	HashMap<String,ArrayList<PSatTableResult>> sortedptrs = new HashMap<String,ArrayList<PSatTableResult>>();
    	for(String uniqueflow:uniqueFlows){
    		ArrayList<PSatTableResult> ptrs_uf = new ArrayList<PSatTableResult>();
    		for(PSatTableResult ptr: FeasibilityView.ptrs){
        		if(ptr != null && ptr.getFlow()!=null && ptr.getFlow().equals(uniqueflow)){
        			ptrs_uf.add(ptr);
        		}        		
        	}
    		sortedptrs.put(uniqueflow, ptrs_uf);
    	}
    	
    	//extract decision spread    	
//    	int M = 6; 
    	int M = 3; 
    	int N = uniqueFlows.length;
    	final double[][] data = new double[N][M];
    	
    	for(int i=0;i<uniqueFlows.length;i++){
    		Flow flow = new Flow();
    		flow.senderToRecipient = uniqueFlows[i];
    		
    		int cat1count = 0;
    	    ArrayList<String> cat1Protocols = new ArrayList<String>();
    		int cat2count = 0;
    	    ArrayList<String> cat2Protocols = new ArrayList<String>();
    		int cat3count = 0;
    	    ArrayList<String> cat3Protocols = new ArrayList<String>();
    		int cat4count = 0;
    	    ArrayList<String> cat4Protocols = new ArrayList<String>();
    		int cat5count = 0;
    	    ArrayList<String> cat5Protocols = new ArrayList<String>();
    		int cat6count = 0;
    	    ArrayList<String> cat6Protocols = new ArrayList<String>();
    	    
    	    ArrayList<PSatTableResult> ptrs_uf = sortedptrs.get(uniqueFlows[i]);
    	    
    	    Collections.sort(ptrs_uf, new PSatTableResultComparator());  	    
    	    
    	    for(PSatTableResult ptr:ptrs_uf){
    	    	String protocol =ptr.getProtocol();
        	    protocol = protocol.replace(",", ";");
        	    protocol = protocol.replace("&#945;","");
        	    protocol = protocol.replace("<sub>","");
        	    protocol = protocol.replace("</sub>","");
        	    
        	    String pid = protocol.split("=")[0];
        	    
    	    	if(ptr.getDecisionCategory()==DecisionCategory.CAT1){
    	    		cat1count = cat1count+1;
    	    		cat1Protocols.add(pid);
    	    		flow.cat1Ptrs.add(ptr);
    	    	}
    	    	else if(ptr.getDecisionCategory()==DecisionCategory.CAT2){
    	    		cat2count = cat2count+1;
    	    		cat2Protocols.add(pid);
    	    		flow.cat2Ptrs.add(ptr);
    	    	}
    	    	else if(ptr.getDecisionCategory()==DecisionCategory.CAT3){
    	    		cat3count = cat3count+1;
    	    		cat3Protocols.add(pid);
    	    		flow.cat3Ptrs.add(ptr);
    	    	}
    	    	else if(ptr.getDecisionCategory()==DecisionCategory.CAT4){
    	    		cat4count = cat4count+1;
    	    		cat4Protocols.add(pid);
    	    		flow.cat4Ptrs.add(ptr);
    	    	}
    	    	else if(ptr.getDecisionCategory()==DecisionCategory.CAT5){
    	    		cat5count = cat5count+1;
    	    		cat5Protocols.add(pid);
    	    		flow.cat5Ptrs.add(ptr);
    	    	}
    	    	else if(ptr.getDecisionCategory()==DecisionCategory.CAT6){
    	    		cat6count = cat6count+1;
    	    		cat6Protocols.add(pid);
    	    		flow.cat6Ptrs.add(ptr);
    	    	} 	    	
    	    }
//			data[i] = new double[]{cat1count,cat2count,cat3count,cat4count,cat5count,cat6count};
			data[i] = new double[]{cat1count,cat2count,cat3count};

			flows.add(flow);
    	}
    	
    	//transpose data
    	
    	double[][] data2 = new double[M][N];
    	for (int i = 0; i < M; i++)
            for (int j = 0; j < N; j++)
                data2[i][j] = data[j][i];
       	
    	String series[] = new String[]{DecisionCategory.getDecision(DecisionCategory.CAT1)+"("+DecisionCategory.getCatDescription(DecisionCategory.CAT1)+")",
    								   DecisionCategory.getDecision(DecisionCategory.CAT2)+"("+DecisionCategory.getCatDescription(DecisionCategory.CAT2)+")",
    								   DecisionCategory.getDecision(DecisionCategory.CAT3)+"("+DecisionCategory.getCatDescription(DecisionCategory.CAT3)+")",
//    								   DecisionCategory.getDecision(DecisionCategory.CAT4)+"("+DecisionCategory.getCatDescription(DecisionCategory.CAT4)+")",
//    								   DecisionCategory.getDecision(DecisionCategory.CAT5)+"("+DecisionCategory.getCatDescription(DecisionCategory.CAT5)+")",
//    								   DecisionCategory.getDecision(DecisionCategory.CAT6)+"("+DecisionCategory.getCatDescription(DecisionCategory.CAT6)+")"
    								   };
    	
        final CategoryDataset dataset = DatasetUtilities.createCategoryDataset(series,uniqueFlows,data2);

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        // create the chart...
		double catt = 0;
		for (Map.Entry<String,ArrayList<PSatTableResult>> entry : sortedptrs.entrySet()) {

			ArrayList<PSatTableResult> ptrs = entry.getValue();

			for(@SuppressWarnings("unused") PSatTableResult ptr:ptrs){
				catt = catt +1;
			}
			break;
		}
        JPanel layeredbarchart = new JPanel();
        
        final CategoryAxis categoryAxis = new CategoryAxis("Decisions");
        final ValueAxis valueAxis = new NumberAxis("# of α");
        try {
			valueAxis.setRange(0, catt);
		} catch (java.lang.IllegalArgumentException e) {
			//e.printStackTrace();
		}


        final CategoryPlot plot = new CategoryPlot(dataset,
                                             categoryAxis, 
                                             valueAxis, 
                                             new LayeredBarRenderer());
        
        plot.setOrientation(PlotOrientation.VERTICAL);
        Font font = new Font("Courier", Font.PLAIN,15);
        final JFreeChart chart = new JFreeChart(
            "Goal(v):"+goalv, 
//            JFreeChart.DEFAULT_TITLE_FONT, 
            font,
            plot, 
            true
        );
        
        // set the background color for the chart...
        chart.setBackgroundPaint(Color.WHITE);

        LayeredBarRenderer renderer = (LayeredBarRenderer) plot.getRenderer();
        // we can set each series bar width individually or let the renderer manage a standard view.
        // the width is set in percentage, where 1.0 is the maximum (100%).
        renderer.setSeriesBarWidth(0, 1.0);
        renderer.setSeriesBarWidth(1, 0.76);
        renderer.setSeriesBarWidth(2, 0.63);
//        renderer.setSeriesBarWidth(3, 0.48);
//        renderer.setSeriesBarWidth(4, 0.30);
//        renderer.setSeriesBarWidth(5, 0.16);
        
        renderer.setSeriesFillPaint(4, Color.GRAY);
        renderer.setSeriesPaint(4, Color.GRAY);

        renderer.setItemMargin(0.01);

        final CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryMargin(0.25);
        domainAxis.setUpperMargin(0.05);
        domainAxis.setLowerMargin(0.05);
        
        // add the chart to a panel...
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(580, 305));
        layeredbarchart.add(chartPanel);
        add(layeredbarchart);
                
        JPanel rankpanel = new JPanel(); 
        rankpanel.setLayout(new BoxLayout(rankpanel,BoxLayout.Y_AXIS));
        rankpanel.setBackground(Color.WHITE);
        JScrollPane jScrollPane = new JScrollPane(rankpanel);
        

		String keydesc="<html>YES(cat.1) - pathsat ≥ v (cost of α<sub>x</sub> is low enough and extent of satisfaction of Pr close enough to privacy goal to keep feasibility below 1.)<br/>";
		keydesc=keydesc+"MAYBE(cat.2) - pathsat ≥ v (cost of α<sub>x</sub> and the distance between extent of satisfaction of Pr and privacy goal is just enough to keep feasibility at 1.)<br/>";
		keydesc=keydesc+"NO(cat.3) - pathsat ≥ v (cost of α<sub>x</sub> is too high and extent of satisfaction of Pr is not close enough to privacy goal resulting in feasibility above 1.<br/>";
//		keydesc=keydesc+"YES(cat.4) - pathsat &lt; v (cost of α<sub>x</sub> is low enough and extent of satisfaction of Pr close enough to privacy goal to keep feasibility below 1.)<br/>";
//		keydesc=keydesc+"MAYBE(cat.5) - pathsat &lt; v (cost of α<sub>x</sub> and the distance between extent of satisfaction of Pr and privacy goal is just enough to keep feasibility at 1.)<br/>";
//		keydesc=keydesc+"NO(cat.6) - pathsat &lt; v (cost of α<sub>x</sub> is too high and extent of satisfaction of Pr is not close enough to privacy goal resulting in feasibility above 1.<br/></html>";
		        
        for (Map.Entry<String,ArrayList<PSatTableResult>> entry : sortedptrs.entrySet()) {
        	
            String flow = entry.getKey();
            ArrayList<PSatTableResult> ptrs = entry.getValue();
            flow = ptrs.get(0).getFlowColumn();
            
        	double cat1total = 0;
        	double cat2total = 0;
        	double cat3total = 0;
//        	double cat4total = 0;
//        	double cat5total = 0;
//        	double cat6total = 0;
        	double cattotal = 0;
        	for(PSatTableResult ptr:ptrs){
        		if(ptr.getDecisionCategory() == DecisionCategory.CAT1){
        			cat1total = cat1total+1;
        		}
        		else if(ptr.getDecisionCategory() == DecisionCategory.CAT2){
        			cat2total = cat2total+1;
        		}
        		else if(ptr.getDecisionCategory() == DecisionCategory.CAT3){
        			cat3total = cat3total+1;
        		}
//        		else if(ptr.getDecisionCategory() == DecisionCategory.CAT4){
//        			cat4total = cat4total+1;
//        		}
//        		else if(ptr.getDecisionCategory() == DecisionCategory.CAT5){
//        			cat5total = cat5total+1;
//        		}
//        		else if(ptr.getDecisionCategory() == DecisionCategory.CAT6){
//        			cat6total = cat6total+1;
//        		}
        		cattotal = cattotal +1;
        	}
        	
            int i= 1;
            double cat1p = Display.RoundTo2Decimals((cat1total/cattotal)*100);
            double cat2p = Display.RoundTo2Decimals((cat2total/cattotal)*100);
            double cat3p = Display.RoundTo2Decimals((cat3total/cattotal)*100);
//            double cat4p = Display.RoundTo2Decimals((cat4total/cattotal)*100);
//            double cat5p = Display.RoundTo2Decimals((cat5total/cattotal)*100);
//            double cat6p = Display.RoundTo2Decimals((cat6total/cattotal)*100);
            String row_s = flow+"<br/>";
            row_s = row_s+ "cat.1="+cat1p+"%, "
            			 + "cat.2="+cat2p+"%, "
            			 + "cat.3="+cat3p+"% "
//            			 + "cat.4="+cat4p+"%, "
//            			 + "cat.5="+cat5p+"%, "
//               			 + "cat.6="+cat6p+"%"
            			 + "<br/>";
            
//            exportSummary(reqDesc,flow, goalv, Display.instance.costTradeoff,cat1p, cat2p, cat3p, cat4p, cat5p, cat6p);
            exportSummary(reqDesc,flow, goalv, PSatAPI.instance.costTradeoff,cat1p, cat2p, cat3p);
                        
            ArrayList<PSatTableResult> cat1Ptrs = new ArrayList<PSatTableResult>();
            ArrayList<PSatTableResult> cat2Ptrs = new ArrayList<PSatTableResult>();
            ArrayList<PSatTableResult> cat3Ptrs = new ArrayList<PSatTableResult>();
            ArrayList<PSatTableResult> cat4Ptrs = new ArrayList<PSatTableResult>();
            ArrayList<PSatTableResult> cat5Ptrs = new ArrayList<PSatTableResult>();
            ArrayList<PSatTableResult> cat6Ptrs = new ArrayList<PSatTableResult>();
            
            for(PSatTableResult ptr:ptrs){
            	if(ptr.getDecisionCategory() == DecisionCategory.CAT1){
            		cat1Ptrs.add(ptr);
            	}
            	else if(ptr.getDecisionCategory() == DecisionCategory.CAT2){
            		cat2Ptrs.add(ptr);
            	}
            	else if(ptr.getDecisionCategory() == DecisionCategory.CAT3){
            		cat3Ptrs.add(ptr);
            	}
            	else if(ptr.getDecisionCategory() == DecisionCategory.CAT4){
            		cat4Ptrs.add(ptr);
            	}
            	else if(ptr.getDecisionCategory() == DecisionCategory.CAT5){
            		cat5Ptrs.add(ptr);
            	}
            	else if(ptr.getDecisionCategory() == DecisionCategory.CAT6){
            		cat6Ptrs.add(ptr);
            	}
            }
            
            Collections.sort(cat1Ptrs, new PSatTableResultComparator());
            Collections.sort(cat2Ptrs, new PSatTableResultComparator());
            Collections.sort(cat3Ptrs, new PSatTableResultComparator());
            Collections.sort(cat4Ptrs, new PSatTableResultComparator());
            Collections.sort(cat5Ptrs, new PSatTableResultComparator());
            Collections.sort(cat6Ptrs, new PSatTableResultComparator());
            
            for(PSatTableResult ptr:cat1Ptrs){ 
            	String pathsat_s = "";
        		if(ptr.getPathSat()==-1){
        			pathsat_s = "NA";
        		}
        		else{
        			pathsat_s = ""+Display.RoundTo3Decimals(ptr.getPathSat());
        		}
        		
            	row_s = row_s +i+": ";
            	if(i<10){
                	row_s = row_s +"&nbsp;&nbsp;";
            	}
            	else if(i<100){
                	row_s = row_s +"&nbsp;";
            	}
            	String d = ptr.getDecisionColumn();
            	if(d.equals("NO")){
            		d = "NO &nbsp;";
            	}
            	row_s = row_s +d;
            	String cat = DecisionCategory.getCatDescription(ptr.getDecisionCategory());            	
            	row_s = row_s +"&nbsp;("+cat+")";            	
            	row_s = row_s +ptr.getProtocolColumn();
            	if(ptr.getFeasibility() < 1){
                	row_s = row_s +"&nbsp; <font color='blue'>F="+Display.RoundTo3Decimals(ptr.getFeasibility())+", pathsat="+pathsat_s+"</font>";
            	}
            	else if(ptr.getFeasibility() >1){
            		String f_s = "";
            		if(ptr.getFeasibility() > 100){
            			f_s = "∞";
            		}
            		else{
            			f_s = ""+Display.RoundTo3Decimals(ptr.getFeasibility());
            		}
                	row_s = row_s +"&nbsp; <font color='red'>F="+f_s+", pathsat="+pathsat_s+"</font>";
            	}
            	else{
                	row_s = row_s +"&nbsp; F="+Display.RoundTo3Decimals(ptr.getFeasibility())+", pathsat="+pathsat_s;
            	}
            	row_s = row_s +"<br/>";
            	i = i+1;
            } 
            for(PSatTableResult ptr:cat2Ptrs){ 
            	String pathsat_s = "";
        		if(ptr.getPathSat()==-1){
        			pathsat_s = "NA";
        		}
        		else{
        			pathsat_s = ""+Display.RoundTo3Decimals(ptr.getPathSat());
        		}
        		
            	row_s = row_s +i+": ";
            	if(i<10){
                	row_s = row_s +"&nbsp;&nbsp;";
            	}
            	else if(i<100){
                	row_s = row_s +"&nbsp;";
            	}
            	String d = ptr.getDecisionColumn();
            	if(d.equals("NO")){
            		d = "NO &nbsp;";
            	}
            	row_s = row_s +d;
            	String cat = DecisionCategory.getCatDescription(ptr.getDecisionCategory());            	
            	row_s = row_s +"&nbsp;("+cat+")";            	
            	row_s = row_s +ptr.getProtocolColumn();
            	if(ptr.getFeasibility() < 1){
                	row_s = row_s +"&nbsp; <font color='blue'>F="+Display.RoundTo3Decimals(ptr.getFeasibility())+", pathsat="+pathsat_s+"</font>";
            	}
            	else if(ptr.getFeasibility() >1){
            		String f_s = "";
            		if(ptr.getFeasibility() > 100){
            			f_s = "∞";
            		}
            		else{
            			f_s = ""+Display.RoundTo3Decimals(ptr.getFeasibility());
            		}
                	row_s = row_s +"&nbsp; <font color='red'>F="+f_s+", pathsat="+pathsat_s+"</font>";
            	}
            	else{
                	row_s = row_s +"&nbsp; F="+Display.RoundTo3Decimals(ptr.getFeasibility())+", pathsat="+pathsat_s;
            	}
            	row_s = row_s +"<br/>";
            	i = i+1;
            } 
            for(PSatTableResult ptr:cat3Ptrs){ 
            	String pathsat_s = "";
        		if(ptr.getPathSat()==-1){
        			pathsat_s = "NA";
        		}
        		else{
        			pathsat_s = ""+Display.RoundTo3Decimals(ptr.getPathSat());
        		}
        		
            	row_s = row_s +i+": ";
            	if(i<10){
                	row_s = row_s +"&nbsp;&nbsp;";
            	}
            	else if(i<100){
                	row_s = row_s +"&nbsp;";
            	}
            	String d = ptr.getDecisionColumn();
            	if(d.equals("NO")){
            		d = "NO &nbsp;";
            	}
            	row_s = row_s +d;
            	String cat = DecisionCategory.getCatDescription(ptr.getDecisionCategory());            	
            	row_s = row_s +"&nbsp;("+cat+")";            	
            	row_s = row_s +ptr.getProtocolColumn();
            	if(ptr.getFeasibility() < 1){
                	row_s = row_s +"&nbsp; <font color='blue'>F="+Display.RoundTo3Decimals(ptr.getFeasibility())+", pathsat="+pathsat_s+"</font>";
            	}
            	else if(ptr.getFeasibility() >1){
            		String f_s = "";
            		if(ptr.getFeasibility() > 100){
            			f_s = "∞";
            		}
            		else{
            			f_s = ""+Display.RoundTo3Decimals(ptr.getFeasibility());
            		}
                	row_s = row_s +"&nbsp; <font color='red'>F="+f_s+", pathsat="+pathsat_s+"</font>";
            	}
            	else{
                	row_s = row_s +"&nbsp; F="+Display.RoundTo3Decimals(ptr.getFeasibility())+", pathsat="+pathsat_s;
            	}
            	row_s = row_s +"<br/>";
            	i = i+1;
            } 
//            for(PSatTableResult ptr:cat4Ptrs){ 
//            	String pathsat_s = "";
//        		if(ptr.getPathSat()==-1){
//        			pathsat_s = "NA";
//        		}
//        		else{
//        			pathsat_s = ""+Display.RoundTo3Decimals(ptr.getPathSat());
//        		}
//        		
//            	row_s = row_s +i+": ";
//            	if(i<10){
//                	row_s = row_s +"&nbsp;&nbsp;";
//            	}
//            	else if(i<100){
//                	row_s = row_s +"&nbsp;";
//            	}
//            	String d = ptr.getDecisionColumn();
//            	if(d.equals("NO")){
//            		d = "NO &nbsp;";
//            	}
//            	row_s = row_s +d;
//            	String cat = DecisionCategory.getCatDescription(ptr.getDecisionCategory());            	
//            	row_s = row_s +"&nbsp;("+cat+")";            	
//            	row_s = row_s +ptr.getProtocolColumn();
//            	if(ptr.getFeasibility() < 1){
//                	row_s = row_s +"&nbsp; <font color='blue'>F="+Display.RoundTo3Decimals(ptr.getFeasibility())+", pathsat="+pathsat_s+"</font>";
//            	}
//            	else if(ptr.getFeasibility() >1){
//            		String f_s = "";
//            		if(ptr.getFeasibility() > 100){
//            			f_s = "∞";
//            		}
//            		else{
//            			f_s = ""+Display.RoundTo3Decimals(ptr.getFeasibility());
//            		}
//                	row_s = row_s +"&nbsp; <font color='red'>F="+f_s+", pathsat="+pathsat_s+"</font>";
//            	}
//            	else{
//                	row_s = row_s +"&nbsp; F="+Display.RoundTo3Decimals(ptr.getFeasibility())+", pathsat="+pathsat_s;
//            	}
//            	row_s = row_s +"<br/>";
//            	i = i+1;
//            } 
//            for(PSatTableResult ptr:cat5Ptrs){ 
//            	String pathsat_s = "";
//        		if(ptr.getPathSat()==-1){
//        			pathsat_s = "NA";
//        		}
//        		else{
//        			pathsat_s = ""+Display.RoundTo3Decimals(ptr.getPathSat());
//        		}
//        		
//            	row_s = row_s +i+": ";
//            	if(i<10){
//                	row_s = row_s +"&nbsp;&nbsp;";
//            	}
//            	else if(i<100){
//                	row_s = row_s +"&nbsp;";
//            	}
//            	String d = ptr.getDecisionColumn();
//            	if(d.equals("NO")){
//            		d = "NO &nbsp;";
//            	}
//            	row_s = row_s +d;
//            	String cat = DecisionCategory.getCatDescription(ptr.getDecisionCategory());            	
//            	row_s = row_s +"&nbsp;("+cat+")";            	
//            	row_s = row_s +ptr.getProtocolColumn();
//            	if(ptr.getFeasibility() < 1){
//                	row_s = row_s +"&nbsp; <font color='blue'>F="+Display.RoundTo3Decimals(ptr.getFeasibility())+", pathsat="+pathsat_s+"</font>";
//            	}
//            	else if(ptr.getFeasibility() >1){
//            		String f_s = "";
//            		if(ptr.getFeasibility() > 100){
//            			f_s = "∞";
//            		}
//            		else{
//            			f_s = ""+Display.RoundTo3Decimals(ptr.getFeasibility());
//            		}
//                	row_s = row_s +"&nbsp; <font color='red'>F="+f_s+", pathsat="+pathsat_s+"</font>";
//            	}
//            	else{
//                	row_s = row_s +"&nbsp; F="+Display.RoundTo3Decimals(ptr.getFeasibility())+", pathsat="+pathsat_s;
//            	}
//            	row_s = row_s +"<br/>";
//            	i = i+1;
//            } 
//            for(PSatTableResult ptr:cat6Ptrs){ 
//            	String pathsat_s = "";
//        		if(ptr.getPathSat()==-1){
//        			pathsat_s = "NA";
//        		}
//        		else{
//        			pathsat_s = ""+Display.RoundTo3Decimals(ptr.getPathSat());
//        		}
//        		
//            	row_s = row_s +i+": ";
//            	if(i<10){
//                	row_s = row_s +"&nbsp;&nbsp;";
//            	}
//            	else if(i<100){
//                	row_s = row_s +"&nbsp;";
//            	}
//            	String d = ptr.getDecisionColumn();
//            	if(d.equals("NO")){
//            		d = "NO &nbsp;";
//            	}
//            	row_s = row_s +d;
//            	String cat = DecisionCategory.getCatDescription(ptr.getDecisionCategory());            	
//            	row_s = row_s +"&nbsp;("+cat+")";            	
//            	row_s = row_s +ptr.getProtocolColumn();
//            	if(ptr.getFeasibility() < 1){
//                	row_s = row_s +"&nbsp; <font color='blue'>F="+Display.RoundTo3Decimals(ptr.getFeasibility())+", pathsat="+pathsat_s+"</font>";
//            	}
//            	else if(ptr.getFeasibility() >1){
//            		String f_s = "";
//            		
//            		if(ptr.getFeasibility() > 100){
//            			f_s = "∞";
//            		}
//            		else{
//            			f_s = ""+Display.RoundTo3Decimals(ptr.getFeasibility());
//            		}
//            		
//                	row_s = row_s +"&nbsp; <font color='red'>F="+f_s+", pathsat="+pathsat_s+"</font>";
//            	}
//            	else{
//                	row_s = row_s +"&nbsp; F="+Display.RoundTo3Decimals(ptr.getFeasibility())+", pathsat="+pathsat_s;
//            	}
//            	row_s = row_s +"<br/>";
//            	i = i+1;
//            } 
            
            row_s = row_s +"<br/>";
            
            row_s = row_s.replace("<html>", " ");
            row_s = row_s.replace("</html>", " ");
            row_s = "<html>"+row_s+"</html>";
            JLabel label  = new JLabel(row_s);
            label.setBackground(Color.WHITE);
            Border border = BorderFactory.createLineBorder(new Color(207,207,207), 3);
            label.setBorder(border);
            label.setToolTipText(keydesc);

            rankpanel.add(label);            
        }
        
        jScrollPane.setPreferredSize(new java.awt.Dimension(550, 120));

        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(jScrollPane, BorderLayout.CENTER);
    }
	
	static File exportFile;
//	public void exportSummary(String pr,String flow, String goal, double ω, double cat1,double cat2,double cat3,double cat4,double cat5,double cat6){
	public void exportSummary(String pr,String flow, String goal, double ω, double cat1,double cat2,double cat3){
		
		try {
			FileWriter writer = new FileWriter(exportFile, true);

			writer.append(pr);
			writer.append(',');
			writer.append(""+goal);
			writer.append(',');
			flow = flow.replace("<html>", "");
			flow = flow.replace("</html>", "");
			flow = flow.replace("&#10142;", "->");
			writer.append(flow);
			writer.append(',');
			writer.append(""+ω);
			writer.append(',');
			writer.append(""+cat1);
			writer.append(',');
			writer.append(""+cat2);
			writer.append(',');
			writer.append(""+cat3);
//			writer.append(',');   	    
//			writer.append(""+cat4);
//			writer.append(',');
//			writer.append(""+cat5);
//			writer.append(',');
//			writer.append(""+cat6);
			writer.append('\n');

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  

	}
	
	public static void createExportFile(ConfigInstance instance){
		if(exportFile != null){
			return;
		}
		String folderName1 = Display.sessionids_store_file_path;
		String folderName2 = folderName1+"/prsummary";
		String fileName =folderName2+"/summary.csv";

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
			writer.append("goal");
			writer.append(',');
			writer.append("Flow");
			writer.append(',');
			writer.append("tradeoff");
			writer.append(',');
			writer.append("cat1");
			writer.append(',');
			writer.append("cat2");
			writer.append(',');
			writer.append("cat3");
//			writer.append(',');   	    
//			writer.append("cat4");
//			writer.append(',');
//			writer.append("cat5");
//			writer.append(',');
//			writer.append("cat6");
			writer.append('\n');

			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}  

	}
    
    class PSatTableResultComparator implements Comparator<PSatTableResult> {
        public int compare(PSatTableResult s1, PSatTableResult s2) {
            return Double.compare(s1.getFeasibility(), s2.getFeasibility());
        }
    }


}