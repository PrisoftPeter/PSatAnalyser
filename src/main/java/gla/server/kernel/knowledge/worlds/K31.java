package gla.server.kernel.knowledge.worlds;

import java.io.Serializable;

import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;

public class K31 extends World implements Serializable {
	private static final long serialVersionUID = 1L;
	private Agent self;
	private Agent agent1;
	private Attribute att;
	public K31a k31a;
	public K31b k31b;
	public static String htmlType1;
	
	public K31(Agent self,Agent agent1, Attribute att){
		k31a = new K31a(self,agent1, att);
		k31b = new K31b(self,agent1, att);
		
		super.type = "K31";
		super.htmlType = "<b>K</b><sub>31</sub>";
		htmlType1 = super.htmlType;
		this.self = self;
		this.agent1 = agent1;
		this.att = att;
	}
	
	@Override
	public String toHtmlString(){
		String res = "<html>";
		res = res+k31a.toLimitHtmlString()+"|"+k31b.toLimitHtmlString();		
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
		res = res+k31a.toLimitHtmlString()+"|"+k31b.toLimitHtmlString();		
				
		return res;
	}

	@Override
	public String toString() {
		String res = "";
				
		String os = System.getProperty("os.name");
		if(os.toLowerCase().contains("win")){
			res = res+k31a.toString()+"I"+k31b.toString();		
		}
		else{
			res = res+k31a.toString()+"|"+k31b.toString();		
		}	
		
		return res;
	}

	@Override
	public String getGenericMeaning(String genericSelf, String genericAgent1, String genericAgent2) {
		return genericSelf+" is uncertain that "+genericAgent1+" knows "+att.toHtmlString();
	}
	
	@Override
	public String getMeaning() {
		return self.getAgentName()+" is uncertain that "+agent1.getAgentName()+" knows "+att.toHtmlString();
	}

	@Override
	public String getGenericFormula(String genericSelf, String genericAgent1,String genericAgent2) {
		String s = "<i>B</i><sub>"+genericSelf+"</sub>"+
				   "<i>K</i><sub>"+genericAgent1+"</sub>"+att.toHtmlString()+
				   "|¬"+
				   "<i>B</i><sub>"+genericSelf+"</sub>"+
				   "<i>K</i><sub>"+genericAgent1+"</sub>"+att.toHtmlString();
		return s;
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
