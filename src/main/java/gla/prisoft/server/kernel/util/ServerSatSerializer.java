package gla.prisoft.server.kernel.util;

import java.io.Serializable;
import java.util.ArrayList;

import gla.prisoft.client.session.ClientServerBroker;
import gla.prisoft.server.PSatAPI;
import gla.prisoft.server.kernel.behaviour.InformationFlows;
import gla.prisoft.server.kernel.util.ServerAgentFactory;
import gla.prisoft.server.kernel.verification.FlowResult;
import gla.prisoft.server.kernel.verification.ViabilitySpectrum;
import gla.prisoft.server.session.ServerConfigInstance;
import gla.prisoft.shared.Agent;
import gla.prisoft.shared.AssertionInstance;
import gla.prisoft.shared.ConfigInstance;
import gla.prisoft.shared.DecisionCategory;
import gla.prisoft.shared.PSatTableResult;
import gla.prisoft.shared.RowType;

public class ServerSatSerializer implements Serializable{
	private static final long serialVersionUID = 7596273423902691695L;
	
	//	private File satTrainingStoreFile;
//	private File satAnalysisStoreFile;

//	public static String requirementHtmlFullDesc = "";
//	public static String protocolHtmlFullDesc = "";
	public String requirementRawDesc = "";
	public String requirementHtmlDesc="";
	public String currentPath;	
	
	public ArrayList<String> treatedReqs;
	public ArrayList<String> treatedProtocols;
	
	public String protocolDesc;
	public String iflow;
	
	public void resetLongSatVals(){
		treatedReqs = new ArrayList<String>();
		treatedProtocols = new ArrayList<String>();
//		requirementHtmlFullDesc = "";
//		protocolHtmlFullDesc = "";
	}
	
	public void updateRequirementHtmlFullDesc(String desc){
		boolean treated = false;
		for(String s: treatedReqs){
			if(s!=null && desc != null && s.contains(desc)){
				treated = true;
				break;
			}
		}
		if(!treated && desc !=null){
//			requirementHtmlFullDesc = requirementHtmlFullDesc+desc;
			treatedReqs.add(desc);
		}
	}
	
	public String getRequirementHtmlFullDesc(){
		String sdes="Pr={ ";
		int i=0;
		for(String s: treatedReqs){
			sdes=sdes+s;
			if(i < treatedReqs.size() -1){
				sdes=sdes+" ; ";
			}
			i = i+1;
		}
		sdes=sdes+" }";
		return sdes;
	}
	
	public void updateProtocolHtmlFullDesc(String desc){
		boolean treated = false;
		for(String s: treatedProtocols){
			if(s.equals(desc)){
				treated = true;
				break;
			}
		}
		if(!treated){
//			protocolHtmlFullDesc = protocolHtmlFullDesc+desc;
			treatedProtocols.add(desc);
		}
	}
	
	public void resetRequirementDesc(){
		requirementRawDesc = "";
		requirementHtmlDesc = "";
	}

	public void displayReq(InformationFlows ifs, ServerConfigInstance sinstance,ConfigInstance instance){

		String requirementHtmlDesc = "";
		for(String agentName: ifs.pathAgents){
			Agent agent = ServerAgentFactory.getAgent(agentName, sinstance);
			if(!instance.is_role_run){
				for(AssertionInstance ai: agent.getAssertionInstances()){
					String req = ai.getAssertion();
					req = req.replace("<html>", "");
					req = req.replace("</html>", "");
					
					requirementHtmlDesc = requirementHtmlDesc + req +" ; ";
				}
			}
//			else{
//				
//				String f[] = agent.getAssertionRoles();
//				World reqs [] = new World[f.length];
//				if(f.length>0){
//					requirementHtmlDesc = requirementHtmlDesc+"<b>AssertionRole(";
//				}
//				for(int i=0;i<f.length;i++){
//					reqs[i] = AssertionsFactory.getAssertionRole(agent.getAgentName(), f[i]);
//					
//					requirementHtmlDesc = requirementHtmlDesc + reqs[i].htmlType+ "; ";
//				}
//				if(f.length>0){
//					requirementHtmlDesc = requirementHtmlDesc+")<sub>"+agentName+"</sub></b>";					
//				}
//			}
			
		}
		
		updateRequirementHtmlFullDesc(requirementHtmlDesc);
		
		if(!instance.is_role_run){
			requirementHtmlDesc = "Instance("+requirementHtmlDesc+")";
		}
		
//		requirementHtmlDesc = requirementHtmlDesc + "<table>"
//												  + "<tr>"
//												  + "<td bgcolor='white' WIDTH=320></td>"
//												  + "<td bgcolor='white' WIDTH=100>Principal</td>"
//												  + "<td bgcolor='white' WIDTH=100>Sender</td>"
//												  + "<td bgcolor='white' WIDTH=100>Recipient</td>"
//												 // + "<td bgcolor='white' WIDTH=100>Mean</td>"
//												  + "</tr>"
//												  + "</table>";
		
		String req = "<html><b>"+requirementHtmlDesc+"<b/></html>";
		ClientServerBroker.messageEvent("updateLogPage", req+"₦"+false,null,null);
		
	}
	
	public  void displayPathsSatMeanSummary(double pathsSatMean, String sessionid){
		ClientServerBroker.messageEvent("updateLogPage", "----"+"₦"+true,null,null);

		String satresultHtml = 
				  "<table>"
				+ "<tr><td bgcolor='white' WIDTH=708>Summary</td></tr>"
//				+ "<td bgcolor='white' WIDTH=708>"+requirementHtmlFullDesc+"</td></tr>"
//				+ "<td bgcolor='white' WIDTH=708>"+protocolHtmlFullDesc+"</td></tr>"
				;
		
		if(pathsSatMean <=0.4){
			satresultHtml = satresultHtml + "<td bgcolor='#F4C2C2' WIDTH=708>"+"PathsSatMean(pr)="+Helper.RoundTo2Decimals(pathsSatMean)+"</td>";
		}
		else if(pathsSatMean > 0.4 && pathsSatMean <=0.7){
			satresultHtml = satresultHtml + "<td bgcolor='#FFFF99' WIDTH=708>"+"PathsSatMean(pr)="+Helper.RoundTo2Decimals(pathsSatMean)+"</td>";
		}
		else{
			satresultHtml = satresultHtml + "<td bgcolor='#CCFFCC' WIDTH=708>"+"PathsSatMean(pr)="+Helper.RoundTo2Decimals(pathsSatMean)+"</td>";
		}
		satresultHtml = satresultHtml+ "</table>";
		
		ClientServerBroker.messageEvent("updateLogPage", satresultHtml+"₦"+false,null,null);
	}
	
	public void displaySat(String path, String subjectName, String senderName, String recipientName, 
						   String protocolDesc, double su_sat, double s_sat, double r_sat,
						   double path_sat, ViabilitySpectrum spectrum, FlowResult flowresult,
						   double protocol_cost,double collectiveGoalValue, double benefit, double feasibility,String decision,
						   ConfigInstance instance){
		
		PSatTableResult ptr_row = new PSatTableResult();
		ptr_row.setIndex(PSatAPI.fvindex);
		PSatAPI.fvindex = PSatAPI.fvindex+1;
		ptr_row.setRowType(RowType.MF);
		
		ptr_row.setPath(path);
		ptr_row.setFlowColumn("<html>"+senderName+"&#10142;"+recipientName+"</html>");
		ptr_row.setFlow(senderName+"->"+recipientName);
		ptr_row.setProtocolColumn("<html>"+protocolDesc+"</html>");
		ptr_row.setProtocol(protocolDesc);
		
		ptr_row.setCostColumn(""+protocol_cost);
		ptr_row.setCost(protocol_cost);
		if(feasibility <0){
			ptr_row.setFeasibilityColumn("<html><body style=background-color:red;>na</body></html>");
 	    }
 	    else{
 	    	ptr_row.setFeasibilityColumn(""+feasibility);
 	    }		
		ptr_row.setBenefit(benefit);
		ptr_row.setFeasibility(feasibility);
		ptr_row.setVgoal(collectiveGoalValue);
		ptr_row.setDecisionColumn(decision);
		ptr_row.setDecision(decision);
		
		if(path_sat >= collectiveGoalValue && feasibility < 1){
			ptr_row.setDecisionCategory(DecisionCategory.CAT1);
		}
		else if(path_sat >= collectiveGoalValue && feasibility == 1){
			ptr_row.setDecisionCategory(DecisionCategory.CAT2);
		}
		else if(path_sat >= collectiveGoalValue && feasibility > 1){
			ptr_row.setDecisionCategory(DecisionCategory.CAT3);
		}
//		else if(path_sat < collectiveGoalValue && feasibility < 1){
//			ptr_row.setDecisionCategory(DecisionCategory.CAT4);
//		}
//		else if(path_sat < collectiveGoalValue && feasibility == 1){
//			ptr_row.setDecisionCategory(DecisionCategory.CAT5);
//		}
//		else if(path_sat < collectiveGoalValue && feasibility > 1){
//			ptr_row.setDecisionCategory(DecisionCategory.CAT6);
//		}
		
		ptr_row.setSuSat(su_sat);
		ptr_row.setsSat(s_sat);
		ptr_row.setrSat(r_sat);
		ptr_row.setPathSat(path_sat);
		ptr_row.setAssertion(requirementRawDesc);		
		
		if(instance.isModeEntropy){
			ptr_row.setRequirementHtmlFullDesc("<html><body>"+instance.desiredEntropyDesc+"</body></html>");
		}
//		else if(instance.isModeCommonKnowledge){
//			ptr_row.setRequirementHtmlFullDesc("<html><body>"+instance.desiredCommonKnowledgeDesc+"</body></html>");
//		}			
			
		else{
			ptr_row.setRequirementHtmlFullDesc("<html><body>"+getRequirementHtmlFullDesc()+"</body></html>");			
		}
		
//		if(!instance.isModeEntropy && !instance.isModeCommonKnowledge){			
		if(!instance.isModeEntropy){			
			if(su_sat ==-1){
				ptr_row.setSuSatColumn("<html><body style=background-color:white;>"+subjectName+":sat(pr)=na</body></html>");
			}
			else if(su_sat <=0.4){
				ptr_row.setSuSatColumn("<html><body style=background-color:#F4C2C2;>"+subjectName+":sat(pr)="+su_sat+"</body></html>");
			}
			else if(su_sat > 0.4 && su_sat <=0.7){
				ptr_row.setSuSatColumn("<html><body style=background-color:#FFFF99;>"+subjectName+":sat(pr)="+su_sat+"</body></html>");
			}
			else{
				ptr_row.setSuSatColumn("<html><body style=background-color:#CCFFCC;>"+subjectName+":sat(pr)="+su_sat+"</body></html>");
			}
			//
			if(s_sat ==-1){
				ptr_row.setsSatColumn("<html><body style=background-color:white;>"+senderName+":sat(pr)=na</body></html>");
			}
			else if(s_sat <=0.4){
				ptr_row.setsSatColumn("<html><body style=background-color:#F4C2C2;>"+senderName+":sat(pr)="+s_sat+"</body></html>");
			}
			else if(s_sat > 0.4 && s_sat <=0.7){
				ptr_row.setsSatColumn("<html><body style=background-color:#FFFF99;>"+senderName+":sat(pr)="+s_sat+"</body></html>");
			}
			else{
				ptr_row.setsSatColumn("<html><body style=background-color:#CCFFCC;>"+senderName+":sat(pr)="+s_sat+"</body></html>");
			}
			//
			if(r_sat ==-1){
				ptr_row.setrSatColumn("<html><body style=background-color:white;>"+recipientName+":sat(pr)=na</body></html>");
			}
			else if(r_sat <=0.4){
				ptr_row.setrSatColumn("<html><body style=background-color:#F4C2C2;>"+recipientName+":sat(pr)="+r_sat+"</body></html>");
			}
			else if(r_sat > 0.4 && r_sat <=0.7){
				ptr_row.setrSatColumn("<html><body style=background-color:#FFFF99;>"+recipientName+":sat(pr)="+r_sat+"</body></html>");
			}
			else{
				ptr_row.setrSatColumn("<html><body style=background-color:#CCFFCC;>"+recipientName+":sat(pr)="+r_sat+"</body></html>");
			}
			//
			if(path_sat ==-1){
				ptr_row.setPathSatColumn("<html><body style=background-color:white;>PathSat(pr)=na</body></html>");
			}
			else if(path_sat <=0.4){
				ptr_row.setPathSatColumn("<html><body style=background-color:#F4C2C2;>PathSat(pr)="+path_sat+"</body></html>");

			}
			else if(path_sat > 0.4 && path_sat <=0.7){
				ptr_row.setPathSatColumn("<html><body style=background-color:#FFFF99;>PathSat(pr)="+path_sat+"</body></html>");
			}
			else{
				ptr_row.setPathSatColumn("<html><body style=background-color:#CCFFCC;>PathSat(pr)="+path_sat+"</body></html>");
			}
			
			if(requirementHtmlDesc.length() !=0){
				ptr_row.setAssertionColumn("<html><body style=background-color:white;>"+requirementHtmlDesc+"</body></html>");
			}					
			
		}
		else{
			if(path_sat ==-1){
				ptr_row.setPathSatColumn("<html><body style=background-color:white;>PathSat(pr)=na</body></html>");
			}
			else if(path_sat <=0.4){
				ptr_row.setPathSatColumn("<html><body style=background-#F4C2C2;>PathSat(pr)="+path_sat+"</body></html>");
			}
			else if(path_sat > 0.4 && path_sat <=0.7){
				ptr_row.setPathSatColumn("<html><body style=background-#FFFF99;>PathSat(pr)="+path_sat+"</body></html>");
			}
			else{
				ptr_row.setPathSatColumn("<html><body style=background-#CCFFCC;>PathSat(pr)="+path_sat+"</body></html>");
			}
			
			if(requirementHtmlDesc.length() !=0){
				ptr_row.setAssertionColumn("<html><body style=background-color:white;>"+requirementHtmlDesc+"</body></html>");
			}
		}
		
		if(ptr_row.getDecision() !=null){
			ClientServerBroker.messageEvent("FeasibilityView.addRow()",null,null,ptr_row);			
		}
	
	}	
	
//	public boolean writeToSatPathAnalysisStore(String path, String protocolSequence, double mean_sat, ConfigInstance instance, ServerConfigInstance sinstance){
//		boolean written = false;
//		
//		if(sinstance.satAnalysisMeanStoreFile == null){
//			createAnalysisSatPathStore(instance, sinstance);
//		}
//		if(sinstance.satAnalysisMeanStoreFile.exists()){
//			try {
//				sinstance.satAnalysisMeanStoreFile.createNewFile();
//
//				FileWriter writer = new FileWriter(sinstance.satAnalysisMeanStoreFile, true);
//
//				writer.append(path);
//				writer.append(',');
//				writer.append(protocolSequence);
//				writer.append(',');
//				writer.append(""+mean_sat);
//				writer.append('\n');
//
//				writer.flush();
//				writer.close();
//				
//				written =true;
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return written;
//	}
//	
//	public boolean writeToSatPathTrainingStore(String path, String protocolSequence, double mean_sat, ServerConfigInstance sinstance){
//		boolean written = false;
//		
//		if(sinstance.satTrainingMeanStoreFile.exists()){
//			try {
//				sinstance.satTrainingMeanStoreFile.createNewFile();
//
//				FileWriter writer = new FileWriter(sinstance.satTrainingMeanStoreFile, true);
//
//				writer.append(path);
//				writer.append(',');
//				writer.append(protocolSequence);
//				writer.append(',');
//				writer.append(""+mean_sat);
//				writer.append('\n');
//
//				writer.flush();
//				writer.close();
//				written = true;
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		return written;
//	}
//	
//	public void displayAveViableProtocolRatio(FlowResult [] flowresults, int pathlength, ConfigInstance instance, double collectiveEntropy, ServerConfigInstance sinstance){
//
//		//calculate flowresults summaries
//		double viable_protocol_count = 0;
//		double viable_protocol_sum = 0;
//		
//		for(FlowResult flowresult:flowresults){
//			if(flowresult.pathsat !=-1){
//				viable_protocol_sum = viable_protocol_sum +flowresult.ratio_viableProtocols;
//				viable_protocol_count = viable_protocol_count +1;
//			}
//		}	
//		
//		double ave_viable_protocol_ratio = viable_protocol_sum/viable_protocol_count;
//		
//		String text = "";
//		if(instance.isModePick){
//			text = "PathLength:"+pathlength+
//				   ", KnowledgeBase:"+flowresults[0].knowledgebase +
//				   ", RoleType:"+flowresults[0].roleTypeHtml+
//				   ", PathSat:"+flowresults[flowresults.length-1].pathsat+
//				   ", AveViableProtocolRatio:"+ave_viable_protocol_ratio;
//		}
//		else if(instance.isModeEntropy){
//			text = "PathLength:"+pathlength+
////					   ", DesiredEntropy:"+instance.desiredEntropy +
//					   ", DesiredEntropy:"+collectiveEntropy +
//					   ", PathSat:"+flowresults[flowresults.length-1].pathsat+
//					   ", AveViableProtocolRatio:"+ave_viable_protocol_ratio;
//		}
//					  
//		
//		text = text.replace("<html>", "");
//		text = text.replace("</html>", "");
//		
//		ClientServerBroker.messageEvent("updateLogPage", text+"₦"+false,null,null);
//	
//		writeToAnalysisVaibleProtocolRatioStore(pathlength, flowresults[0].knowledgebase, 
//				flowresults[0].roleTypeRaw,flowresults[flowresults.length-1].pathsat, 
//				ave_viable_protocol_ratio, collectiveEntropy, instance, sinstance);		
//	}
//	
//	public void createTrainingSatPathStore(ServerConfigInstance sinstance, ConfigInstance instance){
//		String sessionid = instance.sessionid;
//		String folderName1 = PSatAPI.datastore_file_path+"/"+sessionid;
//		String folderName2 = PSatAPI.datastore_file_path+"/"+sessionid+"/SATTrainingPaths";
//		String folderName3 = "";
//		String fileName ="";
//		if(!instance.is_role_run){
//			folderName3 = PSatAPI.datastore_file_path+"/"+sessionid+"/SATTrainingPaths/"+instance.sourceAgentName+"_"+instance.targetAgentName;
//			fileName = folderName3+"/source_"+instance.targetAgentName+".csv";	
//		}
//		else{
//			folderName3 = PSatAPI.datastore_file_path+"/"+sessionid+"/SATTrainingPaths/"+instance.sourceAgentName+"_Role";
//			fileName = folderName3+"/source_Role.csv";
//
//		}
//		
//		File folder1 = new File(folderName1);
//		boolean exist1 = false;
//		if(folder1.exists()){
//			if(folder1.isDirectory()){
//				exist1 = true;
//			}				
//		}
//		if(!exist1){
//			folder1.mkdir();
//		}
//		
//		File folder2 = new File(folderName2);
//		boolean exist2 = false;
//		if(folder2.exists()){
//			if(folder2.isDirectory()){
//				exist2 = true;
//			}				
//		}
//		if(!exist2){
//			folder2.mkdir();
//		}
//		
//		File folder3 = new File(folderName3);
//		boolean exist3 = false;
//		if(folder3.exists()){
//			if(folder3.isDirectory()){
//				exist3 = true;
//			}				
//		}
//		if(!exist3){
//			folder3.mkdir();
//		}
//
//		sinstance.satTrainingMeanStoreFile = new File(fileName);
//		if(sinstance.satTrainingMeanStoreFile.exists()){
//			sinstance.satTrainingMeanStoreFile.delete();
//		}
//
//    	try {
//    		sinstance.satTrainingMeanStoreFile.createNewFile();
//			Config.serialiseServerConfigInstance(sinstance.sessionid, sinstance);
//
//    		FileWriter writer = new FileWriter(sinstance.satTrainingMeanStoreFile, true);
//   		 
//    		writer.append("Path");
//    	    writer.append(',');
//    	    writer.append("ProtocolSequence");
//    	    writer.append(',');
//    	    writer.append("SATPath");
//    	    writer.append('\n');
//    			        			
//    	    writer.flush();
//    	    writer.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}  
//	
//	}
//	
//	public void createAnalysisSatPathStore(ConfigInstance instance, ServerConfigInstance sinstance){
//		String sessionid = instance.sessionid;
//		
//		String folderName1 = PSatAPI.datastore_file_path+"/"+sessionid;
//		String folderName2 = PSatAPI.datastore_file_path+"/"+sessionid+"/SATAnalysisPaths";
//		String folderName3 = "";
//		String fileName ="";
//		if(!instance.is_role_run){
//			folderName3 = PSatAPI.datastore_file_path+"/"+sessionid+"/SATAnalysisPaths/"+instance.sourceAgentName+"_"+instance.targetAgentName;
//			fileName = folderName3+"/source_"+instance.targetAgentName+".csv";	
//		}
//		else{
//			folderName3 = PSatAPI.datastore_file_path+"/"+sessionid+"/SATAnalysisPaths/"+instance.sourceAgentName+"_Role";
//			fileName = folderName3+"/source_Role.csv";
//
//		}
//		
//		File folder1 = new File(folderName1);
//		boolean exist1 = false;
//		if(folder1.exists()){
//			if(folder1.isDirectory()){
//				exist1 = true;
//			}				
//		}
//		if(!exist1){
//			folder1.mkdir();
//		}
//		
//		File folder2 = new File(folderName2);
//		boolean exist2 = false;
//		if(folder2.exists()){
//			if(folder2.isDirectory()){
//				exist2 = true;
//			}				
//		}
//		if(!exist2){
//			folder2.mkdir();
//		}
//
//		File folder3 = new File(folderName3);
//		boolean exist3 = false;
//		if(folder3.exists()){
//			if(folder3.isDirectory()){
//				exist3 = true;
//			}				
//		}
//		if(!exist3){
//			folder3.mkdir();
//		}
//
//		sinstance.satAnalysisMeanStoreFile = new File(fileName);
//		if(sinstance.satAnalysisMeanStoreFile.exists()){
//			sinstance.satAnalysisMeanStoreFile.delete();
//		}
//
//    	try {
//    		sinstance.satAnalysisMeanStoreFile.createNewFile();
//			Config.serialiseServerConfigInstance(sinstance.sessionid, sinstance);
//
//    		
//    		FileWriter writer = new FileWriter(sinstance.satAnalysisMeanStoreFile, true);
//   		 
//    		writer.append("Path");
//    	    writer.append(',');
//    	    writer.append("ProtocolSequence");
//    	    writer.append(',');
//    	    writer.append("SATPath");
//    	    writer.append('\n');
//    			        			
//    	    writer.flush();
//    	    writer.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public boolean createAnalysisVaibleProtocolRatioStore(ConfigInstance instance, ServerConfigInstance sinstance){
//		boolean created = false;
//		String sessionid = instance.sessionid;
//
//		String folderName1 = PSatAPI.datastore_file_path+"/"+sessionid;
//		String folderName2 = PSatAPI.datastore_file_path+"/"+sessionid+"/SATAnalysisPaths";
//		String folderName3 = "";
//		String fileName ="";
//
//		folderName3 = folderName2+"/"+instance.sourceAgentName+"_Role";
//		if(instance.isModePick){
//			fileName = folderName3+"/source_Role_viableProtocol_Range_"+instance.k+"_Pick.csv";			
//		}
//		else{
//			 if(instance.greaterThanOrEqualTo){
//				fileName = folderName3+"/source_Role_viableProtocol_Range_"+instance.k+"_Entropy≥.csv";	
//			}
////			else if(AssertionsView.lessThanOrEqualTo){
////				fileName = folderName2+"/source_Role_viableProtocol_Range_"+PathsInGraph.k+"_Entropy≤.csv";
////			}
//			else{
//				fileName = folderName3+"/source_Role_viableProtocol_Range_"+instance.k+"_Entropy≤.csv";
//			}		 
//					
//		}
//		
//		File folder1 = new File(folderName1);
//		boolean exist1 = false;
//		if(folder1.exists()){
//			if(folder1.isDirectory()){
//				exist1 = true;
//			}				
//		}
//		if(!exist1){
//			folder1.mkdir();
//		}
//		
//		File folder2 = new File(folderName2);
//		boolean exist2 = false;
//		if(folder2.exists()){
//			if(folder2.isDirectory()){
//				exist2 = true;
//			}				
//		}
//		if(!exist2){
//			folder2.mkdir();
//		}
//		
//		File folder3 = new File(folderName3);
//		boolean exist3 = false;
//		if(folder3.exists()){
//			if(folder3.isDirectory()){
//				exist3 = true;
//			}				
//		}
//		if(!exist3){
//			folder3.mkdir();
//		}
//
//		sinstance.viableProtocolRatioAnalysisMeanStoreFile = new File(fileName);
//		Config.serialiseServerConfigInstance(sinstance.sessionid, sinstance);
//
//		if(sinstance.viableProtocolRatioAnalysisMeanStoreFile.exists()){
//			sinstance.viableProtocolRatioAnalysisMeanStoreFile.delete();
//		}
//
//    	try {
//    		sinstance.viableProtocolRatioAnalysisMeanStoreFile.createNewFile();
//    		
//    		FileWriter writer = new FileWriter(sinstance.viableProtocolRatioAnalysisMeanStoreFile, true);
//   		 
//    		if(instance.isModePick){
//    			writer.append("PathLength");
//        	    writer.append(',');
//        	    writer.append("KnowledgeBase");
//        	    writer.append(',');
//        	    writer.append("RoleType");
//        	    writer.append(',');
//        	    writer.append("MaxPathSat");
//        	    writer.append(',');
//        	    writer.append("AveViableProtocolRatio");
//        	    writer.append('\n');	
//    		}
//    		else if(instance.isModeEntropy){
//    			writer.append("PathLength");
//        	    writer.append(',');
//        	    if(instance.greaterThanOrEqualTo){
//					writer.append("DesiredEntropy≥");
//				}
//				else if(instance.lessThanOrEqualTo){
//					writer.append("DesiredEntropy≤");
//				}
//        	    writer.append(',');
//        	    writer.append("MaxPathSat");
//        	    writer.append(',');
//        	    writer.append("AveViableProtocolRatio");
//        	    writer.append('\n');
//    		}
//    		
//    		
//    	    writer.flush();
//    	    writer.close();
//    	    created = true;
//		} catch (IOException e) {
//			System.out.println(e.toString());
//			//e.printStackTrace();
//			created = false;
//		}
//    	return created;
//	}
//	
//	public void writeToAnalysisVaibleProtocolRatioStore(int pathLength, KnowledgeBase knowledgebase, String roleType
//			,double pathsat, double aveViableProtocolRatio, double desiredEntropy, ConfigInstance instance, ServerConfigInstance sinstance){
//
//
//		if(sinstance.viableProtocolRatioAnalysisMeanStoreFile == null){
//			createAnalysisVaibleProtocolRatioStore(instance,sinstance);
//		}
//		if(sinstance.viableProtocolRatioAnalysisMeanStoreFile.exists()){
//			try {
//				sinstance.viableProtocolRatioAnalysisMeanStoreFile.createNewFile();
//
//				FileWriter writer = new FileWriter(sinstance.viableProtocolRatioAnalysisMeanStoreFile, true);
//
//				if(instance.isModePick){
//					writer.append(""+pathLength);
//		    	    writer.append(',');
//		    	    writer.append(""+knowledgebase);
//		    	    writer.append(',');
//		    	    writer.append(roleType);
//		    	    writer.append(',');
//		    	    writer.append(""+pathsat);
//		    	    writer.append(',');
//		    	    writer.append(""+aveViableProtocolRatio);
//		    	    writer.append('\n');
//				}
//				else if(instance.isModeEntropy){
//					writer.append(""+pathLength);
//	        	    writer.append(',');
//	        	    writer.append(""+desiredEntropy);
//	        	    writer.append(',');
//		    	    writer.append(""+pathsat);
//	        	    writer.append(',');
//		    	    writer.append(""+aveViableProtocolRatio);
//	        	    writer.append('\n');
//				}
//				
//
//				writer.flush();
//				writer.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
	
	

	
//	public void writeBeliefUncertaintyLevel(String agentName, double uncertaintyLevel, 
//											double beliefLevel, double entropy, double pathsat,
//											ConfigInstance instance, ServerConfigInstance sinstance){
//
//		if(sinstance.beliefUncertaintyLevelFile == null){
//			createBeliefUncertaintyLevelFile(instance.sourceAgentName, instance, sinstance);
//		}
//		
//		if(sinstance.beliefUncertaintyLevelFile.exists()){
//			try {
//				sinstance.beliefUncertaintyLevelFile.createNewFile();
//
//				FileWriter writer = new FileWriter(sinstance.beliefUncertaintyLevelFile, true);
//
//				writer.append(agentName);
//	    	    writer.append(',');
//	    	    writer.append(iflow);
//	    	    writer.append(',');
//	    	    writer.append(protocolDesc);
//	    	    writer.append(',');
//	    	    writer.append(""+uncertaintyLevel);
//	    	    writer.append(',');
//	    	    writer.append(""+beliefLevel);
//	    	    writer.append(',');
//	    	    if(entropy < 0){
//		    	    writer.append("NA");
//	    		}
//	    	    else{
//		    	    writer.append(""+entropy);	    	    	
//	    	    }
//	    	    writer.append(',');
//	    	    if(pathsat < 0){
//		    	    writer.append("NA");
//	    		}
//	    	    else{
//		    	    writer.append(""+pathsat);	    	    	
//	    	    }
//	    	    
//	    	    writer.append('\n');
//
//				writer.flush();
//				writer.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//	
//	
//	public void createBeliefUncertaintyLevelFile(String subjectName, ConfigInstance instance, ServerConfigInstance sinstance){
//		String sessionid = instance.sessionid;
//		
//		String folderName0 =PSatAPI.datastore_file_path+"/"+sessionid;
//		String folderName1 ="";
//		String folderName2 = "";
//		if(instance.runningTraining){
//			folderName1 = PSatAPI.datastore_file_path+"/"+sessionid+"/TrainingUncEntropyMode";			 
//		}
////		if(instance.runningAnalysis){
////			folderName1 = PSatAPI.datastore_file_path+"/"+sessionid+"/AnalysisUncEntropyMode";
////		}
//		folderName2 = folderName1+"/"+subjectName;
//		String fileName =  folderName2+"/"+currentPath+".csv";
//				
//		File folder0 = new File(folderName0);
//		boolean exist0 = false;
//		if(folder0.exists()){
//			if(folder0.isDirectory()){
//				exist0 = true;
//			}				
//		}
//		if(!exist0){
//			folder0.mkdir();
//		}
//		
//		File folder1 = new File(folderName1);
//		boolean exist1 = false;
//		if(folder1.exists()){
//			if(folder1.isDirectory()){
//				exist1 = true;
//			}				
//		}
//		if(!exist1){
//			folder1.mkdir();
//		}
//		
//		File folder2 = new File(folderName2);
//		boolean exist2 = false;
//		if(folder2.exists()){
//			if(folder2.isDirectory()){
//				exist2 = true;
//			}				
//		}
//		if(!exist2){
//			folder2.mkdir();
//		}
//		
//		sinstance.beliefUncertaintyLevelFile = new File(fileName);
//		Config.serialiseServerConfigInstance(sinstance.sessionid, sinstance);
//
//		if(sinstance.beliefUncertaintyLevelFile.exists()){
//			sinstance.beliefUncertaintyLevelFile.delete();
//		}
//		
//		try {
//			sinstance.beliefUncertaintyLevelFile.createNewFile();
//    		
//    		FileWriter writer = new FileWriter(sinstance.beliefUncertaintyLevelFile, true);
//   		 
//    		writer.append("agentName");
//    	    writer.append(',');
//    	    writer.append("iflow");
//    	    writer.append(',');
//    	    writer.append("protocol");
//    	    writer.append(',');
//    	    writer.append("uncertaintyLevel");
//    	    writer.append(',');
//    	    writer.append("beliefLevel");
//    	    writer.append(',');
//    	    writer.append("entropy");
//    	    writer.append(',');
//    	    writer.append("pathsat");
//    	    writer.append('\n');
//    			        			
//    	    writer.flush();
//    	    writer.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
