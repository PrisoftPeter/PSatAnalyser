package gla.prisoft.server.kernel.verification;

import gla.prisoft.shared.KnowledgeBase;

public class FlowResult {
	public KnowledgeBase knowledgebase;
	
	public double self_sat;
	public double r_sat;
	public double s_sat;
	public double pathsat;
	public double ratio_viableProtocols;
	public double ratio_MaximumProtocols;
	
	public String aspectTypeRaw;
	public String aspectTypeHtml;
	
	public double desiredEntropy;
	
	
}
