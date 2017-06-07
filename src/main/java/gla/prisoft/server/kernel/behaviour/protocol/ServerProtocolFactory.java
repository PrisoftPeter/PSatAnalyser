package gla.prisoft.server.kernel.behaviour.protocol;

import java.util.HashMap;
import java.util.Map;

//import kernel.util.PathsInGraph;
import gla.prisoft.server.session.Config;
import gla.prisoft.shared.ConfigInstance;

public class ServerProtocolFactory {
		
//	public static void resetEvaluatedProtocols(String sessionid, ConfigInstance instance){
//		ConfigInstance instance = Config.deserialiseConfigInstance(sessionid);
//		instance.evaluatedProtocols = new String[0]; 
//		
//		Config.serialiseConfigInstance(sessionid, instance);
//	}
	
	//number of processes in transaction
	private static final int REQUEST = 1;
	private static final int CONSENT1 = 3;
	private static final int CONSENT2 = 2;
	private static final int CONSENT3 = 1;
	private static final int CONSENT4 = 2;

	private static final int SENT1 = 2;
	private static final int SENT2 = 1;
	private static final int NOTICE1SU = 2;
	private static final int NOTICE2SU = 1;
	private static final int NOTICE1R = 2;
	private static final int NOTICE2R = 1;
	
	private static final double WRNCPLEVEL =1;
	private static final double WNCPLEVEL =0.924;
	private static final double WRNPLEVEL =0.913;
	private static final double WRCPLEVEL =0.769;
	private static final double WNPLEVEL =0.769;
	private static final double WCPLEVEL =0.192;
	private static final double WRPLEVEL =0.00009;
	private static final double FPLEVEL =0.00007;	
	
	public static String[] getEvaluatedProtocols(String sessionid){
		ConfigInstance instance = Config.deserialiseConfigInstance(sessionid);

		return instance.evaluatedProtocols;
	}
	
	public static String[] getProtocolSuite(String sessionid){
		ConfigInstance instance = Config.deserialiseConfigInstance(sessionid);

		return instance.protocolSuite;
	}
	
	public static String getProtocolId(String protocolDesc,String sessionid){
		String protocolId = null;
		ConfigInstance instance = Config.deserialiseConfigInstance(sessionid);
		
		for(String p: instance.protocolSuite){
			String pats1[] = p.split(" \\(");
			String pid = pats1[0];
			
			String pdesc = pats1[1].replace(")", "");
			
			if(pdesc.equals(protocolDesc)){
				protocolId = pid;
				break;
			}
		}
		
		return protocolId;
	}
	
	public static String getProtocolDesc(String protocolId,String sessionid){
		String protocolDesc = null;
		ConfigInstance instance = Config.deserialiseConfigInstance(sessionid);

		for(String p: instance.protocolSuite){
			String pats1[] = p.split(" \\(");
			String pid = pats1[0];
			
			String pdesc = pats1[1].replace(")", "");
			
			if(pid.equals(protocolId)){
				protocolDesc = pdesc;
				break;
			}
		}
		
		return protocolDesc;
	}
	
	public static boolean initProtocolSuite(ConfigInstance instance){
//		String []protocolSuitex = PathsInGraph.loadProtocolSuite();
//		for(String s: protocolSuitex){
//			System.out.println(s);
//		}
//		ConfigInstance instance = Config.deserialiseConfigInstance(sessionid);

		
		
		instance.protocolSuite = new String[140];
		instance.protocolCost = new HashMap<String, Double>();
		double cost = 0;
		double noprocesses = 0;
		double maxNoProcesses = 10;
		double dof =0;
		
		instance.protocolSuite[0] = "1 (Request,Sent1)";
		noprocesses = REQUEST+SENT1;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WNCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[0], cost);
		
		instance.protocolSuite[1] = "2 (Request,Sent1,Notice1-su,Notice1-r)";
		noprocesses = REQUEST+SENT1+ NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[1], cost);
		
		instance.protocolSuite[2] = "3 (Request,Sent1,Notice1-su)";
		noprocesses = REQUEST+SENT1+ NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[2], cost);
		
		instance.protocolSuite[3] = "4 (Request,Sent1,Notice1-su,Notice2-r)";
		noprocesses = REQUEST+SENT1+ NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[3], cost);
		
		instance.protocolSuite[4] = "5 (Request,Sent1,Notice2-su,Notice2-r)";
		noprocesses = REQUEST+SENT1+ NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[4], cost);
		
		instance.protocolSuite[5] = "6 (Request,Sent1,Notice2-su)";
		noprocesses = REQUEST+SENT1+ NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[5], cost);
		
		instance.protocolSuite[6] = "7 (Request,Sent1,Notice2-su,Notice1-r)";
		noprocesses = REQUEST+SENT1+ NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[6], cost);
		
		instance.protocolSuite[7] = "8 (Request,Sent2)";
		noprocesses = REQUEST+SENT2;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WNCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[7], cost);
		
		instance.protocolSuite[8] = "9 (Request,Sent2,Notice2-su,Notice2-r)";
		noprocesses = REQUEST+SENT2+ NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[8], cost);
		
		instance.protocolSuite[9] = "10 (Request,Sent2,Notice2-su)";
		noprocesses = REQUEST+SENT2+ NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[9], cost);
		
		instance.protocolSuite[10] = "11 (Request,Sent2,Notice2-su,Notice1-r)";
		noprocesses = REQUEST+SENT2+ NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[10], cost);
		
		instance.protocolSuite[11] = "12 (Request,Sent2,Notice1-su,Notice1-r)";
		noprocesses = REQUEST+SENT2+ NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[11], cost);
		
		instance.protocolSuite[12] = "13 (Request,Sent2,Notice1-su)";
		noprocesses = REQUEST+SENT2+ NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[12], cost);
		
		instance.protocolSuite[13] = "14 (Request,Sent2,Notice1-su,Notice2-r)";
		noprocesses = REQUEST+SENT2+ NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[13], cost);
		
		instance.protocolSuite[14] = "15 (Request,Consent1,Sent1)";
		noprocesses = REQUEST+CONSENT1+SENT1;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WNPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[14], cost);
		
		instance.protocolSuite[15] = "16 (Request,Consent1,Sent1,Notice1-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT1+SENT1+NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[15], cost);
		
		instance.protocolSuite[16] = "17 (Request,Consent1,Sent1,Notice1-su)";
		noprocesses = REQUEST+CONSENT1+SENT1+NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[16], cost);
		
		instance.protocolSuite[17] = "18 (Request,Consent1,Sent1,Notice1-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT1+SENT1+NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[17], cost);
		
		instance.protocolSuite[18] = "19 (Request,Consent1,Sent1,Notice2-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT1+SENT1+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[18], cost);
		
		instance.protocolSuite[19] = "20 (Request,Consent1,Sent1,Notice2-su)";
		noprocesses = REQUEST+CONSENT1+SENT1+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[19], cost);
		
		instance.protocolSuite[20] = "21 (Request,Consent1,Sent1,Notice2-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT1+SENT1+NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[20], cost);
		
		instance.protocolSuite[21] = "22 (Request,Consent1,Sent2)";
		noprocesses = REQUEST+CONSENT1+SENT2;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WNPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[21], cost);
		
		instance.protocolSuite[22] = "23 (Request,Consent1,Sent2,Notice2-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT1+SENT2+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[22], cost);
		
		instance.protocolSuite[23] = "24 (Request,Consent1,Sent2,Notice2-su)";
		noprocesses = REQUEST+CONSENT1+SENT2+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[23], cost);
		
		instance.protocolSuite[24] = "25 (Request,Consent1,Sent2,Notice2-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT1+SENT2+NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[24], cost);
		
		instance.protocolSuite[25] = "26 (Request,Consent1,Sent2,Notice1-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT1+SENT2+NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[25], cost);
		
		instance.protocolSuite[26] = "27 (Request,Consent1,Sent2,Notice1-su)";
		noprocesses = REQUEST+CONSENT1+SENT2+NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[26], cost);
		
		instance.protocolSuite[27] = "28 (Request,Consent1,Sent2,Notice1-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT1+SENT2+NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[27], cost);
		
		instance.protocolSuite[28] = "29 (Request,Consent2,Sent1)";
		noprocesses = REQUEST+CONSENT3+SENT1;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WNPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[28], cost);
		
		instance.protocolSuite[29] = "30 (Request,Consent2,Sent1,Notice1-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT2+SENT1+ NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[29], cost);
		
		instance.protocolSuite[30] = "31 (Request,Consent2,Sent1,Notice1-su)";
		noprocesses = REQUEST+CONSENT2+SENT1+ NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[30], cost);
		
		instance.protocolSuite[31] = "32 (Request,Consent2,Sent1,Notice1-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT2+SENT1+ NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[31], cost);
		
		instance.protocolSuite[32] = "33 (Request,Consent2,Sent1,Notice2-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT2+SENT1+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[32], cost);
		
		instance.protocolSuite[33] = "34 (Request,Consent2,Sent1,Notice2-su)";
		noprocesses = REQUEST+CONSENT2+SENT1+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[33], cost);
		
		instance.protocolSuite[34] = "35 (Request,Consent2,Sent1,Notice2-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT2+SENT1+ NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[34], cost);
		
		instance.protocolSuite[35] = "36 (Request,Consent2,Sent2)";
		noprocesses = REQUEST+CONSENT2+SENT2;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WNPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[35], cost);
		
		instance.protocolSuite[36] = "37 (Request,Consent2,Sent2,Notice2-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT2+SENT2+ NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[36], cost);
		
		instance.protocolSuite[37] = "38 (Request,Consent2,Sent2,Notice2-su)";
		noprocesses = REQUEST+CONSENT2+SENT2+ NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[37], cost);
		
		instance.protocolSuite[38] = "39 (Request,Consent2,Sent2,Notice2-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT2+SENT2+ NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[38], cost);
		
		instance.protocolSuite[39] = "40 (Request,Consent2,Sent2,Notice1-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT2+SENT2+ NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[39], cost);
		
		instance.protocolSuite[40] = "41 (Request,Consent2,Sent2,Notice1-su)";
		noprocesses = REQUEST+CONSENT2+SENT2+ NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[40], cost);
		
		instance.protocolSuite[41] = "42 (Request,Consent2,Sent2,Notice1-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT2+SENT2+ NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[41], cost);
		
		instance.protocolSuite[42] = "43 (Request,Consent3,Sent1)";
		noprocesses = REQUEST+CONSENT3+SENT1;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WNPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[42], cost);
		
		instance.protocolSuite[43] = "44 (Request,Consent3,Sent1,Notice1-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT3+SENT1+NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[43], cost);
		
		instance.protocolSuite[44] = "45 (Request,Consent3,Sent1,Notice1-su)";
		noprocesses = REQUEST+CONSENT3+SENT1+NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[44], cost);
		
		instance.protocolSuite[45] = "46 (Request,Consent3,Sent1,Notice1-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT3+SENT1+NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[45], cost);
		
		instance.protocolSuite[46] = "47 (Request,Consent3,Sent1,Notice2-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT3+SENT1+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[46], cost);
		
		instance.protocolSuite[47] = "48 (Request,Consent3,Sent1,Notice2-su)";
		noprocesses = REQUEST+CONSENT3+SENT1+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[47], cost);
		
		instance.protocolSuite[48] = "49 (Request,Consent3,Sent1,Notice2-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT3+SENT1+NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[48], cost);
		
		instance.protocolSuite[49] = "50 (Request,Consent3,Sent2)";
		noprocesses = REQUEST+CONSENT3+SENT2;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WNPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[49], cost);
		
		instance.protocolSuite[50] = "51 (Request,Consent3,Sent2,Notice2-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT3+SENT2+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[50], cost);
		
		instance.protocolSuite[51] = "52 (Request,Consent3,Sent2,Notice2-su)";
		noprocesses = REQUEST+CONSENT3+SENT2+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[51], cost);
		
		instance.protocolSuite[52] = "53 (Request,Consent3,Sent2,Notice2-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT3+SENT2+NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[52], cost);
		
		instance.protocolSuite[53] = "54 (Request,Consent3,Sent2,Notice1-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT3+SENT2+NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[53], cost);
		
		instance.protocolSuite[54] = "55 (Request,Consent3,Sent2,Notice1-su)";
		noprocesses = REQUEST+CONSENT3+SENT2+NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[54], cost);
		
		instance.protocolSuite[55] = "56 (Request,Consent3,Sent2,Notice1-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT3+SENT2+NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[55], cost);
		
		instance.protocolSuite[56] = "57 (Request,Consent4,Sent1)";
		noprocesses = REQUEST+CONSENT4+SENT1;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[56], cost);
		
		instance.protocolSuite[57] = "58 (Request,Consent4,Sent1,Notice1-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT4+SENT1+NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[57], cost);
		
		instance.protocolSuite[58] = "59 (Request,Consent4,Sent1,Notice1-su)";
		noprocesses = REQUEST+CONSENT4+SENT1+NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[58], cost);
		
		instance.protocolSuite[59] = "60 (Request,Consent4,Sent1,Notice1-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT4+SENT1+NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[59], cost);
		
		instance.protocolSuite[60] = "61 (Request,Consent4,Sent1,Notice2-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT4+SENT1+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[60], cost);
		
		instance.protocolSuite[61] = "62 (Request,Consent4,Sent1,Notice2-su)";
		noprocesses = REQUEST+CONSENT4+SENT1+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[61], cost);
		
		instance.protocolSuite[62] = "63 (Request,Consent4,Sent1,Notice2-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT4+SENT1+NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[62], cost);
		
		instance.protocolSuite[63] = "64 (Request,Consent4,Sent2)";
		noprocesses = REQUEST+CONSENT4+SENT2;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WNPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[63], cost);
		
		instance.protocolSuite[64] = "65 (Request,Consent4,Sent2,Notice2-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT4+SENT2+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[64], cost);
		
		instance.protocolSuite[65] = "66 (Request,Consent4,Sent2,Notice2-su)";
		noprocesses = REQUEST+CONSENT4+SENT2+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[65], cost);
		
		instance.protocolSuite[66] = "67 (Request,Consent4,Sent2,Notice2-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT4+SENT2+NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[66], cost);
		
		instance.protocolSuite[67] = "68 (Request,Consent4,Sent2,Notice1-su,Notice1-r)";
		noprocesses = REQUEST+CONSENT4+SENT2+NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[67], cost);
		
		instance.protocolSuite[68] = "69 (Request,Consent4,Sent2,Notice1-su)";
		noprocesses = REQUEST+CONSENT4+SENT2+NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[68], cost);
		
		instance.protocolSuite[69] = "70 (Request,Consent4,Sent2,Notice1-su,Notice2-r)";
		noprocesses = REQUEST+CONSENT4+SENT2+NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[69], cost);
		
		//
		instance.protocolSuite[70] = "71 (Sent1)";
		noprocesses = SENT1;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRNCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[70], cost);
		
		instance.protocolSuite[71] = "72 (Sent1,Notice1-su,Notice1-r)";
		noprocesses = SENT1+ NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[71], cost);
		
		instance.protocolSuite[72] = "73 (Sent1,Notice1-su)";
		noprocesses = SENT1+ NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[72], cost);
		
		instance.protocolSuite[73] = "74 (Sent1,Notice1-su,Notice2-r)";
		noprocesses = SENT1+ NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[73], cost);
		
		instance.protocolSuite[74] = "75 (Sent1,Notice2-su,Notice2-r)";
		noprocesses = SENT1+ NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[74], cost);
		
		instance.protocolSuite[75] = "76 (Sent1,Notice2-su)";
		noprocesses = SENT1+ NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[75], cost);
		
		instance.protocolSuite[76] = "77 (Sent1,Notice2-su,Notice1-r)";
		noprocesses = SENT1+ NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[76], cost);
		
		instance.protocolSuite[77] = "78 (Sent2)";
		noprocesses = SENT2;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRNCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[77], cost);
		
		instance.protocolSuite[78] = "79 (Sent2,Notice2-su,Notice2-r)";
		noprocesses = SENT2+ NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[78], cost);
		
		instance.protocolSuite[79] = "80 (Sent2,Notice2-su)";
		noprocesses = SENT2+ NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[79], cost);
		
		instance.protocolSuite[80] = "81 (Sent2,Notice2-su,Notice1-r)";
		noprocesses = SENT2+ NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[80], cost);
		
		instance.protocolSuite[81] = "82 (Sent2,Notice1-su,Notice1-r)";
		noprocesses = SENT2+ NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[81], cost);
		
		instance.protocolSuite[82] = "83 (Sent2,Notice1-su)";
		noprocesses = SENT2+ NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[82], cost);
		
		instance.protocolSuite[83] = "84 (Sent2,Notice1-su,Notice2-r)";
		noprocesses = SENT2+ NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRCPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[83], cost);
		
		instance.protocolSuite[84] = "85 (Consent1,Sent1)";
		noprocesses = CONSENT1+SENT1;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRNPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[84], cost);
		
		instance.protocolSuite[85] = "86 (Consent1,Sent1,Notice1-su,Notice1-r)";
		noprocesses = CONSENT1+SENT1+NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[85], cost);
		
		instance.protocolSuite[86] = "87 (Consent1,Sent1,Notice1-su)";
		noprocesses = CONSENT1+SENT1+NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[86], cost);
		
		instance.protocolSuite[87] = "88 (Consent1,Sent1,Notice1-su,Notice2-r)";
		noprocesses = CONSENT1+SENT1+NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[87], cost);
		
		instance.protocolSuite[88] = "89 (Consent1,Sent1,Notice2-su,Notice2-r)";
		noprocesses = CONSENT1+SENT1+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[88], cost);
		
		instance.protocolSuite[89] = "90 (Consent1,Sent1,Notice2-su)";
		noprocesses = CONSENT1+SENT1+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[89], cost);
		
		instance.protocolSuite[90] = "91 (Consent1,Sent1,Notice2-su,Notice1-r)";
		noprocesses = CONSENT1+SENT1+NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[90], cost);
		
		instance.protocolSuite[91] = "92 (Consent1,Sent2)";
		noprocesses = CONSENT1+SENT2;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRNPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[91], cost);
		
		instance.protocolSuite[92] = "93 (Consent1,Sent2,Notice2-su,Notice2-r)";
		noprocesses = CONSENT1+SENT2+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[92], cost);
		
		instance.protocolSuite[93] = "94 (Consent1,Sent2,Notice2-su)";
		noprocesses = CONSENT1+SENT2+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-FPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[93], cost);
		
		instance.protocolSuite[94] = "95 (Consent1,Sent2,Notice2-su,Notice1-r)";
		noprocesses = CONSENT1+SENT2+NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[94], cost);
		
		instance.protocolSuite[95] = "96 (Consent1,Sent2,Notice1-su,Notice1-r)";
		noprocesses = CONSENT1+SENT2+NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[95], cost);
		
		instance.protocolSuite[96] = "97 (Consent1,Sent2,Notice1-su)";
		noprocesses = CONSENT1+SENT2+NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[96], cost);
		
		instance.protocolSuite[97] = "98 (Consent1,Sent2,Notice1-su,Notice2-r)";
		noprocesses = CONSENT1+SENT2+NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[97], cost);
		
		instance.protocolSuite[98] = "99 (Consent2,Sent1)";
		noprocesses = CONSENT3+SENT1;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRNPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[98], cost);
		
		instance.protocolSuite[99] = "100 (Consent2,Sent1,Notice1-su,Notice1-r)";
		noprocesses = CONSENT2+SENT1+ NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[99], cost);
		
		instance.protocolSuite[100] = "101 (Consent2,Sent1,Notice1-su)";
		noprocesses = CONSENT2+SENT1+ NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[100], cost);
		
		instance.protocolSuite[101] = "102 (Consent2,Sent1,Notice1-su,Notice2-r)";
		noprocesses = CONSENT2+SENT1+ NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[101], cost);
		
		instance.protocolSuite[102] = "103 (Consent2,Sent1,Notice2-su,Notice2-r)";
		noprocesses = CONSENT2+SENT1+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[102], cost);
		
		instance.protocolSuite[103] = "104 (Consent2,Sent1,Notice2-su)";
		noprocesses = CONSENT2+SENT1+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[103], cost);
		
		instance.protocolSuite[104] = "105 (Consent2,Sent1,Notice2-su,Notice1-r)";
		noprocesses = CONSENT2+SENT1+ NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[104], cost);
		
		instance.protocolSuite[105] = "106 (Consent2,Sent2)";
		noprocesses = CONSENT2+SENT2;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRNPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[105], cost);
		
		instance.protocolSuite[106] = "107 (Request,Consent2,Sent2,Notice2-su,Notice2-r)";
		noprocesses = CONSENT2+SENT2+ NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[106], cost);
		
		instance.protocolSuite[107] = "108 (Request,Consent2,Sent2,Notice2-su)";
		noprocesses = CONSENT2+SENT2+ NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[107], cost);
		
		instance.protocolSuite[108] = "109 (Request,Consent2,Sent2,Notice2-su,Notice1-r)";
		noprocesses = CONSENT2+SENT2+ NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[108], cost);
		
		instance.protocolSuite[109] = "110 (Consent2,Sent2,Notice1-su,Notice1-r)";
		noprocesses = CONSENT2+SENT2+ NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[109], cost);
		
		instance.protocolSuite[110] = "111 (Consent2,Sent2,Notice1-su)";
		noprocesses = CONSENT2+SENT2+ NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[110], cost);
		
		instance.protocolSuite[111] = "112 (Consent2,Sent2,Notice1-su,Notice2-r)";
		noprocesses = CONSENT2+SENT2+ NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[111], cost);
		
		instance.protocolSuite[112] = "113 (Consent3,Sent1)";
		noprocesses = CONSENT3+SENT1;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRNPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[112], cost);
		
		instance.protocolSuite[113] = "114 (Consent3,Sent1,Notice1-su,Notice1-r)";
		noprocesses = CONSENT3+SENT1+NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[113], cost);
		
		instance.protocolSuite[114] = "115 (Consent3,Sent1,Notice1-su)";
		noprocesses = CONSENT3+SENT1+NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[114], cost);
		
		instance.protocolSuite[115] = "116 (Consent3,Sent1,Notice1-su,Notice2-r)";
		noprocesses = CONSENT3+SENT1+NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[115], cost);
		
		instance.protocolSuite[116] = "117 (Consent3,Sent1,Notice2-su,Notice2-r)";
		noprocesses = CONSENT3+SENT1+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[116], cost);
		
		instance.protocolSuite[117] = "118 (Consent3,Sent1,Notice2-su)";
		noprocesses = CONSENT3+SENT1+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[117], cost);
		
		instance.protocolSuite[118] = "119 (Consent3,Sent1,Notice2-su,Notice1-r)";
		noprocesses = CONSENT3+SENT1+NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[118], cost);
		
		instance.protocolSuite[119] = "120 (Consent3,Sent2)";
		noprocesses = CONSENT3+SENT2;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRNPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[119], cost);
		
		instance.protocolSuite[120] = "121 (Consent3,Sent2,Notice2-su,Notice2-r)";
		noprocesses = CONSENT3+SENT2+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[120], cost);
		
		instance.protocolSuite[121] = "122 (Consent3,Sent2,Notice2-su)";
		noprocesses = CONSENT3+SENT2+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[121], cost);
		
		instance.protocolSuite[122] = "123 (Consent3,Sent2,Notice2-su,Notice1-r)";
		noprocesses = CONSENT3+SENT2+NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[122], cost);
		
		instance.protocolSuite[123] = "124 (Consent3,Sent2,Notice1-su,Notice1-r)";
		noprocesses = CONSENT3+SENT2+NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[123], cost);
		
		instance.protocolSuite[124] = "125 (Consent3,Sent2,Notice1-su)";
		noprocesses = CONSENT3+SENT2+NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[124], cost);
		
		instance.protocolSuite[125] = "126 (Consent3,Sent2,Notice1-su,Notice2-r)";
		noprocesses = CONSENT3+SENT2+NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[125], cost);
		
		instance.protocolSuite[126] = "127 (Consent4,Sent1)";
		noprocesses = CONSENT4+SENT1;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRNPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[126], cost);
		
		instance.protocolSuite[127] = "128 (Consent4,Sent1,Notice1-su,Notice1-r)";
		noprocesses = CONSENT4+SENT1+NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[127], cost);
		
		instance.protocolSuite[128] = "129 (Consent4,Sent1,Notice1-su)";
		noprocesses = CONSENT4+SENT1+NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[128], cost);
		
		instance.protocolSuite[129] = "130 (Consent4,Sent1,Notice1-su,Notice2-r)";
		noprocesses = CONSENT4+SENT1+NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[129], cost);
		
		instance.protocolSuite[130] = "131 (Consent4,Sent1,Notice2-su,Notice2-r)";
		noprocesses = CONSENT4+SENT1+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[130], cost);
		
		instance.protocolSuite[131] = "132 (Consent4,Sent1,Notice2-su)";
		noprocesses = CONSENT4+SENT1+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[131], cost);
		
		instance.protocolSuite[132] = "133 (Consent4,Sent1,Notice2-su,Notice1-r)";
		noprocesses = CONSENT4+SENT1+NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[132], cost);
		
		instance.protocolSuite[133] = "134 (Consent4,Sent2)";
		noprocesses = CONSENT4+SENT2;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRNPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[133], cost);
		
		instance.protocolSuite[134] = "135 (Consent4,Sent2,Notice2-su,Notice2-r)";
		noprocesses = CONSENT4+SENT2+NOTICE2SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[134], cost);
		
		instance.protocolSuite[135] = "136 (Consent4,Sent2,Notice2-su)";
		noprocesses = CONSENT4+SENT2+NOTICE2SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[135], cost);
		
		instance.protocolSuite[136] = "137 (Consent4,Sent2,Notice2-su,Notice1-r)";
		noprocesses = CONSENT4+SENT2+NOTICE2SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[136], cost);
		
		instance.protocolSuite[137] = "138 (Consent4,Sent2,Notice1-su,Notice1-r)";
		noprocesses = CONSENT4+SENT2+NOTICE1SU+NOTICE1R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[137], cost);
		
		instance.protocolSuite[138] = "139 (Consent4,Sent2,Notice1-su)";
		noprocesses = CONSENT4+SENT2+NOTICE1SU;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[138], cost);
		
		instance.protocolSuite[139] = "140 (Consent4,Sent2,Notice1-su,Notice2-r)";
		noprocesses = CONSENT4+SENT2+NOTICE1SU+NOTICE2R;
		dof = 1 -(noprocesses/maxNoProcesses);
		cost = 1-WRPLEVEL +1-dof;
		instance.protocolCost.put(instance.protocolSuite[139], cost);
		
		
		instance.evaluatedProtocols = new String[0]; 
		
		return true;
	}
	
	public static double getMaxCost(ConfigInstance instance){
		double maxvalue = 0;
		for (Map.Entry<String, Double> entry : instance.protocolCost.entrySet()){
			//String key = entry.getKey();
			double value = entry.getValue();

			if(value > maxvalue){
				maxvalue = value;
			}
		}
		return maxvalue;
	}
}
