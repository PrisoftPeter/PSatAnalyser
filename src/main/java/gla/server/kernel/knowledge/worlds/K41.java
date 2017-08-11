package gla.server.kernel.knowledge.worlds;

import java.io.Serializable;

import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;

public class K41 extends World implements Serializable {
	private static final long serialVersionUID = 1L;
	private Agent self;
	private Agent agent1;
	private Agent agent2;
	private Attribute att;
	public K41a k41a;
	public K41b k41b;
	public static String htmlType1;
	
	public K41(Agent self,Agent agent1,Agent agent2, Attribute att){
		k41a = new K41a(self,agent1, agent2, att);
		k41b = new K41b(self,agent1, agent2, att);
		
		super.type = "K41";
		super.htmlType = "<b>K</b><sub>41</sub>";
		htmlType1 = super.htmlType;
		this.self = self;
		this.agent1 = agent1;
		this.agent2 = agent2;
		this.att = att;
	}
	
	@Override
	public String toHtmlString(){
		String res = "<html>";
		res = res+k41a.toLimitHtmlString()+"|"+k41b.toLimitHtmlString();		
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
		res = res+k41a.toLimitHtmlString()+"|"+k41b.toLimitHtmlString();		
				
		return res;
	}

	@Override
	public String toString() {
		String res = "";
				
		String os = System.getProperty("os.name");
		if(os.toLowerCase().contains("win")){
			res = res+k41a.toString()+"I"+k41b.toString();		
		}
		else{
			res = res+k41a.toString()+"|"+k41b.toString();		
		}		
		
		
		return res;
	}

	@Override
	public String getGenericMeaning(String genericSelf, String genericAgent1, String genericAgent2) {
		return genericSelf+" is uncertain that "+genericAgent1+" knows that "+genericAgent2+" knows "+att.toHtmlString();
	}
	
	@Override
	public String getMeaning() {
		return self.getAgentName()+" is uncertain that "+agent1.getAgentName()+" knows that "+agent2.getAgentName()+" knows "+att.toHtmlString();
	}

	@Override
	public String getGenericFormula(String genericSelf, String genericAgent1,String genericAgent2) {
		String s = "<i>B</i><sub>"+genericSelf+"</sub>"+
				   "<i>K</i><sub>"+genericAgent1+"</sub>"+
				   "<i>K</i><sub>"+genericAgent2+"</sub>"+att.toHtmlString()+
				   "|¬"+
				   "<i>B</i><sub>"+genericSelf+"</sub>"+
				   "<i>K</i><sub>"+genericAgent1+"</sub>"+
				   "<i>K</i><sub>"+genericAgent2+"</sub>"+att.toHtmlString();
		
		return s;
	}

	@Override
	public Agent getAgent1() {
		return agent1;
	}

	@Override
	public Agent getAgent2() {
		return agent2;
	}
}
