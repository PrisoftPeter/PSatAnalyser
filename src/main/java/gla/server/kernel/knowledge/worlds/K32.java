package gla.server.kernel.knowledge.worlds;

import java.io.Serializable;

import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;

public class K32 extends World implements Serializable {
	private static final long serialVersionUID = 1L;
	private Agent self;
	private Agent agent2;
	private Attribute att;
	public K32a k32a;
	public K32b k32b;
	public static String htmlType1;
	
	public K32(Agent self,Agent agent2, Attribute att){
		k32a = new K32a(self,agent2, att);
		k32b = new K32b(self,agent2, att);
		
		super.type = "K32";
		super.htmlType = "<b>K</b><sub>32</sub>";
		htmlType1 = super.htmlType;
		this.self = self;
		this.agent2 = agent2;
		this.att = att;
	}
	
	@Override
	public String toHtmlString(){
		String res = "<html>";
		res = res+k32a.toLimitHtmlString()+"|"+k32b.toLimitHtmlString();		
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
		res = res+k32a.toLimitHtmlString()+"|"+k32b.toLimitHtmlString();		
				
		return res;
	}

	@Override
	public String toString() {
		String res = "";
				
		String os = System.getProperty("os.name");
		if(os.toLowerCase().contains("win")){
			res = res+k32a.toString()+"I"+k32b.toString();		
		}
		else{
			res = res+k32a.toString()+"|"+k32b.toString();		
		}	
		
		
		
		return res;
	}

	@Override
	public String getGenericMeaning(String genericSelf, String genericAgent1, String genericAgent2) {
		return genericSelf+" is uncertain that "+genericAgent2+" knows "+att.toHtmlString();
	}
	
	@Override
	public String getMeaning() {
		return self.getAgentName()+" is uncertain that "+agent2.getAgentName()+" knows "+att.toHtmlString();
	}

	@Override
	public String getGenericFormula(String genericSelf, String genericAgent1,String genericAgent2) {
		String s = "<i>B</i><sub>"+genericSelf+"</sub>"+
				   "<i>K</i><sub>"+genericAgent2+"</sub>"+att.toHtmlString()+
				   "|¬"+
				   "<i>B</i><sub>"+genericSelf+"</sub>"+
				   "<i>K</i><sub>"+genericAgent2+"</sub>"+att.toHtmlString();
		
		return s;
	}

	@Override
	public Agent getAgent1() {
		return null;
	}

	@Override
	public Agent getAgent2() {
		return agent2;
	}
}
