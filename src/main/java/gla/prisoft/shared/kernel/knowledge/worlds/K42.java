package gla.prisoft.shared.kernel.knowledge.worlds;

import java.io.Serializable;

import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;

public class K42 extends World implements Serializable {
	private static final long serialVersionUID = 1L;
	private Agent self;
	private Agent agent1;
	private Agent agent2;
	private Attribute att;
	public K42a k42a;
	public K42b k42b;
	public static String htmlType1;
	
	public K42(Agent self,Agent agent1,Agent agent2, Attribute att){
		k42a = new K42a(self,agent1, agent2, att);
		k42b = new K42b(self,agent1, agent2, att);
		
		super.type = "K42";
		super.htmlType = "<b>K</b><sub>42</sub>";
		this.self = self;
		this.agent1 = agent1;
		this.agent2 = agent2;
		this.att = att;
		htmlType1 = super.htmlType;
	}
	
	@Override
	public String toHtmlString(){
		String res = "<html>";
		res = res+k42a.toLimitHtmlString()+"|"+k42b.toLimitHtmlString();		
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
		res = res+k42a.toLimitHtmlString()+"|"+k42b.toLimitHtmlString();		
				
		return res;
	}

	@Override
	public String toString() {
		String res = "";
		
		String os = System.getProperty("os.name");
		if(os.toLowerCase().contains("win")){
			res = res+k42a.toString()+"I"+k42b.toString();		
		}
		else{
			res = res+k42a.toString()+"|"+k42b.toString();		
		}
		
		return res;
	}

	@Override
	public String getGenericMeaning(String genericSelf, String genericAgent1, String genericAgent2) {
		return genericSelf+" is uncertain that "+genericAgent2+" knows that "+genericAgent1+" knows "+att.toHtmlString();
	}
	
	@Override
	public String getMeaning() {
		return self.getAgentName()+" is uncertain that "+agent2.getAgentName()+" knows that "+agent1.getAgentName()+" knows "+att.toHtmlString();
	}

	@Override
	public String getGenericFormula(String genericSelf, String genericAgent1,String genericAgent2) {
		String s = "<i>B</i><sub>"+genericSelf+"</sub>"+
				   "<i>K</i><sub>"+genericAgent2+"</sub>"+
				   "<i>K</i><sub>"+genericAgent1+"</sub>"+att.toHtmlString()+
				   "|¬"+
				   "<i>B</i><sub>"+genericSelf+"</sub>"+
				   "<i>K</i><sub>"+genericAgent2+"</sub>"+
				   "<i>K</i><sub>"+genericAgent1+"</sub>"+att.toHtmlString();
		
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
