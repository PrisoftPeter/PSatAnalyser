package gla.prisoft.server.kernel.knowledge.worlds;

import java.io.Serializable;

import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;

public class K23 extends World implements Serializable {
	private static final long serialVersionUID = 1L;
	private Agent self;
	private Agent agent1;
	private Attribute att;
	public K23a k23a;
	public K23b k23b;
	public static String htmlType1;
	
	public K23(Agent self,Agent agent1, Attribute att){
		k23a = new K23a(self,agent1, att);
		k23b = new K23b(self,agent1, att);
		
		super.type = "K23";
		super.htmlType = "<b>K</b><sub>23</sub>";
		htmlType1 = super.htmlType;
		this.self = self;
		this.agent1 = agent1;
		this.att = att;
	}
	
	@Override
	public String toHtmlString(){
		String res = "<html>";
		res = res+k23a.toLimitHtmlString()+"|"+k23b.toLimitHtmlString();		
		res = res+"</html>";
				
		return res;
	}
	
	@Override
	public Agent getSelf() {
		return self;
	}

	@Override
	public String toLimitHtmlString() {
		String res = "";
		res = res+k23a.toLimitHtmlString()+"|"+k23b.toLimitHtmlString();		
				
		return res;
	}

	@Override
	public String toString() {
		String res = "";
		
		String os = System.getProperty("os.name");
		if(os.toLowerCase().contains("win")){
			res = res+k23a.toString()+"I"+k23b.toString();			
		}
		else{
			res = res+k23a.toString()+"|"+k23b.toString();				
		}			
					
		return res;
	}

	@Override
	public String getGenericMeaning(String genericSelf, String genericAgent1, String genericAgent2) {
		return genericSelf+" is uncertain if "+genericAgent1+" knows that "+genericSelf+" does not know "+att.toHtmlString();
	}
	
	@Override
	public String getMeaning() {
		return self.getAgentName()+" is uncertain if "+agent1.getAgentName()+" knows that "+self.getAgentName()+" does not know "+att.toHtmlString();
	}

	@Override
	public String getGenericFormula(String genericSelf, String genericAgent1,String genericAgent2) {
		String res = "<i>B</i><sub>"+genericSelf+"</sub>"+
				     "<i>K</i><sub>"+genericAgent1+"</sub>"+
				     "¬"+
				     "<i>K</i><sub>"+genericSelf+"</sub>"+att.toHtmlString()+
				     "|¬"+
				     "<i>B</i><sub>"+genericSelf+"</sub>"+
				     "<i>K</i><sub>"+genericAgent1+"</sub>"+
				     "¬"+
				     "<i>K</i><sub>"+genericSelf+"</sub>"+att.toHtmlString();

		return res;
		
	}

	@Override
	public Agent getAgent1() {
		return agent1;
	}

	@Override
	public Agent getAgent2() {
		return null;
	}
}
