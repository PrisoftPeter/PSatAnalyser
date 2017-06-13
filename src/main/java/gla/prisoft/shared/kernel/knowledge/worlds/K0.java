package gla.prisoft.shared.kernel.knowledge.worlds;

import java.io.Serializable;

import gla.prisoft.shared.Agent;
import gla.prisoft.shared.Attribute;

public class K0 extends World implements Serializable {
	private static final long serialVersionUID = 1L;
	private Agent self;
	private Attribute att;
	public static String htmlType1;
		
	public K0(Attribute att){
		super.type = "K0";
		super.htmlType = "<i>f</i>";
		htmlType1 = "<i>f</i>";
		this.self = null;
		this.att =att;
	}
	
	@Override
	public String toHtmlString(){
		String res = "<html>";
		res = res+"<i>f</i>";		
		res = res+"</html>";
				
		return res;
	}

	@Override
	public String toLimitHtmlString() {
		String res = "";
		res = res+"<i>f</i>";		
				
		return res;
	}

	@Override
	public String toString() {
		String res = "";
		res = res+"f";					
				
		return res;
	}

	@Override
	public Agent getSelf() {
		return self;
	}

	@Override
	public String getGenericMeaning(String genericSelf, String genericAgent1, String genericAgent2) {
		return genericSelf+" "+att.toHtmlString();
	}
	
	@Override
	public String getMeaning() {
		return att.toHtmlString();
	}

	@Override
	public String getGenericFormula(String genericSelf, String genericAgent1, String genericAgent2) {
		return att.toHtmlString();
	}

	@Override
	public Agent getAgent1() {
		return null;
	}

	@Override
	public Agent getAgent2() {
		return null;
	}
}
