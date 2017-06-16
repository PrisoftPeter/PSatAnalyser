package gla.prisoft.server.kernel.knowledge.worlds;

import java.io.Serializable;

import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;

public class K22 extends World implements Serializable {
	private static final long serialVersionUID = 1L;
	private Agent self;
	private Agent agent2;
	private Attribute att;
	public K22a k22a;
	public K22b k22b;
	public static String htmlType1;
	
	public K22(Agent self,Agent agent2, Attribute att){
		k22a = new K22a(self,agent2, att);
		k22b = new K22b(self,agent2, att);
		
		super.type = "K22";
		super.htmlType = "<b>K</b><sub>22</sub>";
		htmlType1 = super.htmlType;
		this.self = self;
		this.agent2 = agent2;
		this.att = att;
	}
	
	@Override
	public String toHtmlString(){
		String res = "<html>";
		res = res+k22a.toLimitHtmlString()+"|"+k22b.toLimitHtmlString();		
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
		res = res+k22a.toLimitHtmlString()+"|"+k22b.toLimitHtmlString();		
				
		return res;
	}

	@Override
	public String toString() {
		String res = "";
		
		String os = System.getProperty("os.name");
		if(os.toLowerCase().contains("win")){
			res = res+k22a.toString()+"I"+k22b.toString();	
		}
		else{
			res = res+k22a.toString()+"|"+k22b.toString();		
		}			
		
		
		return res;
	}

	@Override
	public String getGenericMeaning(String genericSelf, String genericAgent1, String genericAgent2) {
		return genericSelf+" is uncertain if "+genericAgent2+" knows that "+genericSelf+" knows "+att.toHtmlString();
	}
	
	@Override
	public String getMeaning() {
		return self.getAgentName()+" is uncertain if "+agent2.getAgentName()+" knows that "+self.getAgentName()+" knows "+att.toHtmlString();
	}

	@Override
	public String getGenericFormula(String genericSelf, String genericAgent1,String genericAgent2) {
		String s = "<i>B</i><sub>"+genericSelf+"</sub>"+
				   "<i>K</i><sub>"+genericAgent2+"</sub>"+
				   "<i>K</i><sub>"+genericSelf+"</sub>"+att.toHtmlString()+
				   "|¬"+
				   "<i>B</i><sub>"+genericSelf+"</sub>"+
				   "<i>K</i><sub>"+genericAgent2+"</sub>"+
				   "<i>K</i><sub>"+genericSelf+"</sub>"+att.toHtmlString();
	
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
