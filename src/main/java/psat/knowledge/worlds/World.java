package psat.knowledge.worlds;

import java.io.Serializable;

import psat.util.Agent;
import psat.util.Attribute;
import psat.util.KnowledgeBase;

public abstract class World implements Serializable {

	private static final long serialVersionUID = 1L;
	public abstract String toHtmlString();
	public abstract String toLimitHtmlString();
	public abstract Agent getSelf();
	public abstract Agent getAgent1();
	public abstract Agent getAgent2();
	public abstract String getGenericMeaning(String genericSelf, String genericAgent1, String genericAgent2);
	public abstract String getGenericFormula(String genericSelf, String genericAgent1, String genericAgent2);
	public abstract String getMeaning();
	public abstract String toString();
	
	public String type;
	public String htmlType;
	
	public static World createWorld(KnowledgeBase knowledgeBase, String type, 
									Agent subject, Agent sender, Agent recipient, Attribute att){
		
//		if(type.equals(K0.htmlType1)){
//			K0 w1 = new K0(att);
//			return w1;
//		}
//		else if(type.equals(K0a.htmlType1)){
//			K0a w1 = new K0a(att);
//			return w1;
//		}
//		else 
		if(type.equals(K1.htmlType1)){
			if(knowledgeBase.equals(KnowledgeBase.SUBJECT)){
				K1 w1 = new K1(subject, att);
				return w1;
			}
			else if(knowledgeBase.equals(KnowledgeBase.RECIPIENT)){
				K1 w1 = new K1(recipient, att);
				return w1;
			}
			else if(knowledgeBase.equals(KnowledgeBase.SENDER)){
				K1 w1 = new K1(sender, att);
				return w1;
			}			
		}
		
		else if(type.equals(K1a.htmlType1)){
			if(knowledgeBase.equals(KnowledgeBase.SUBJECT)){
				K1a w1 = new K1a(subject, att);
				return w1;
			}
			else if(knowledgeBase.equals(KnowledgeBase.RECIPIENT)){
				K1a w1 = new K1a(recipient, att);
				return w1;
			}
			else if(knowledgeBase.equals(KnowledgeBase.SENDER)){
				K1a w1 = new K1a(sender, att);
				return w1;
			}	
		}
		
		else if(type.equals(K21.htmlType1)){
			if(knowledgeBase.equals(KnowledgeBase.SUBJECT)){
				if(!subject.getAgentName().equals(sender.getAgentName())){
					K21 w1 = new K21(subject,sender, att);
					return w1;
				}
			}
			else if(knowledgeBase.equals(KnowledgeBase.RECIPIENT)){
				K21 w1 = new K21(recipient,sender, att);	
				return w1;
			}
			else if(knowledgeBase.equals(KnowledgeBase.SENDER)){
				if(!subject.getAgentName().equals(sender.getAgentName())){
					K21 w1 = new K21(sender,subject, att);	
					return w1;
				}
				
			}			
		}
		
		else if(type.equals(K21a.htmlType1)){
			if(knowledgeBase.equals(KnowledgeBase.SUBJECT)){
				if(!subject.getAgentName().equals(sender.getAgentName())){
					K21a w1 = new K21a(subject,sender, att);
					return w1;
				}
			}
			else if(knowledgeBase.equals(KnowledgeBase.RECIPIENT)){
				K21a w1 = new K21a(recipient,sender, att);	
				return w1;
			}
			else if(knowledgeBase.equals(KnowledgeBase.SENDER)){
				if(!subject.getAgentName().equals(sender.getAgentName())){
					K21a w1 = new K21a(sender,subject, att);	
					return w1;
				}
			}			
		}
		
		else if(type.equals(K22.htmlType1)){
			if(knowledgeBase.equals(KnowledgeBase.SUBJECT)){
				K22 w1 = new K22(subject,recipient, att);
				return w1;
			}
			else if(knowledgeBase.equals(KnowledgeBase.RECIPIENT)){
				K22 w1 = new K22(recipient,subject, att);	
				return w1;
			}
			else if(knowledgeBase.equals(KnowledgeBase.SENDER)){
				K22 w1 = new K22(sender,recipient, att);
				return w1;
			}
		}
		
		else if(type.equals(K22a.htmlType1)){
			if(knowledgeBase.equals(KnowledgeBase.SUBJECT)){
				K22a w1 = new K22a(subject,recipient, att);
				return w1;
			}
			else if(knowledgeBase.equals(KnowledgeBase.RECIPIENT)){
				K22a w1 = new K22a(recipient,subject, att);	
				return w1;
			}
			else if(knowledgeBase.equals(KnowledgeBase.SENDER)){
				K22a w1 = new K22a(sender,recipient, att);
				return w1;
			}
		}
		
		else if(type.equals(K23.htmlType1)){
			if(knowledgeBase.equals(KnowledgeBase.SUBJECT)){
				if(!subject.getAgentName().equals(sender.getAgentName())){
					K23 w1 = new K23(subject,sender, att);
					return w1;
				}
				
			}
			else if(knowledgeBase.equals(KnowledgeBase.RECIPIENT)){
				K23 w1 = new K23(recipient,sender, att);	
				return w1;
			}
			else if(knowledgeBase.equals(KnowledgeBase.SENDER)){
				if(!subject.getAgentName().equals(sender.getAgentName())){
					K23 w1 = new K23(sender,subject, att);
					return w1;	
				}				
			}
		}
		
		else if(type.equals(K23a.htmlType1)){
			if(knowledgeBase.equals(KnowledgeBase.SUBJECT)){
				if(!subject.getAgentName().equals(sender.getAgentName())){
					K23a w1 = new K23a(subject,sender, att);
					return w1;
				}
				
			}
			else if(knowledgeBase.equals(KnowledgeBase.RECIPIENT)){
				K23a w1 = new K23a(recipient,sender, att);	
				return w1;
			}
			else if(knowledgeBase.equals(KnowledgeBase.SENDER)){
				if(!subject.getAgentName().equals(sender.getAgentName())){
					K23a w1 = new K23a(sender,subject, att);
					return w1;	
				}				
			}
		}
		
		else if(type.equals(K24.htmlType1)){
			if(knowledgeBase.equals(KnowledgeBase.SUBJECT)){
				K24 w1 = new K24(subject,recipient, att);
				return w1;				
			}
			else if(knowledgeBase.equals(KnowledgeBase.RECIPIENT)){
				K24 w1 = new K24(recipient,subject, att);	
				return w1;
			}
			else if(knowledgeBase.equals(KnowledgeBase.SENDER)){
				K24 w1 = new K24(sender,recipient, att);
				return w1;
			}
		}
		
		else if(type.equals(K24a.htmlType1)){
			if(knowledgeBase.equals(KnowledgeBase.SUBJECT)){
				K24a w1 = new K24a(subject,recipient, att);
				return w1;				
			}
			else if(knowledgeBase.equals(KnowledgeBase.RECIPIENT)){
				K24a w1 = new K24a(recipient,subject, att);	
				return w1;
			}
			else if(knowledgeBase.equals(KnowledgeBase.SENDER)){
				K24a w1 = new K24a(sender,recipient, att);
				return w1;
			}
		}
		
		else if(type.equals(K31.htmlType1)){
			if(knowledgeBase.equals(KnowledgeBase.SUBJECT)){
				if(!subject.getAgentName().equals(sender.getAgentName())){
					K31 w1 = new K31(subject,sender, att);
					return w1;	
				}								
			}
			else if(knowledgeBase.equals(KnowledgeBase.RECIPIENT)){
				K31 w1 = new K31(recipient,sender, att);	
				return w1;
			}
			else if(knowledgeBase.equals(KnowledgeBase.SENDER)){
				if(!subject.getAgentName().equals(sender.getAgentName())){
					K31 w1 = new K31(sender,subject, att);
					return w1;					
				}
			}
		}
		
		else if(type.equals(K31a.htmlType1)){
			if(knowledgeBase.equals(KnowledgeBase.SUBJECT)){
				if(!subject.getAgentName().equals(sender.getAgentName())){
					K31a w1 = new K31a(subject,sender, att);
					return w1;	
				}								
			}
			else if(knowledgeBase.equals(KnowledgeBase.RECIPIENT)){
				K31a w1 = new K31a(recipient,sender, att);	
				return w1;
			}
			else if(knowledgeBase.equals(KnowledgeBase.SENDER)){
				if(!subject.getAgentName().equals(sender.getAgentName())){
					K31a w1 = new K31a(sender,subject, att);
					return w1;	
				}				
			}
		}

		else if(type.equals(K32.htmlType1)){
			if(knowledgeBase.equals(KnowledgeBase.SUBJECT)){
				K32 w1 = new K32(subject,recipient, att);
				return w1;								
			}
			else if(knowledgeBase.equals(KnowledgeBase.RECIPIENT)){
				K32 w1 = new K32(recipient,subject, att);	
				return w1;
			}
			else if(knowledgeBase.equals(KnowledgeBase.SENDER)){
				K32 w1 = new K32(sender,recipient, att);
				return w1;
			}
		}
		
		else if(type.equals(K32a.htmlType1)){
			if(knowledgeBase.equals(KnowledgeBase.SUBJECT)){
				K32a w1 = new K32a(subject,recipient, att);
				return w1;								
			}
			else if(knowledgeBase.equals(KnowledgeBase.RECIPIENT)){
				K32a w1 = new K32a(recipient,subject, att);	
				return w1;
			}
			else if(knowledgeBase.equals(KnowledgeBase.SENDER)){
				K32a w1 = new K32a(sender,recipient, att);
				return w1;
			}
		}
		
		else if(type.equals(K41.htmlType1)){
			if(knowledgeBase.equals(KnowledgeBase.SUBJECT)){
				if(!subject.getAgentName().equals(sender.getAgentName())){
					K41 w1 = new K41(subject,sender,recipient, att);
					return w1;
				}				
			}
			else if(knowledgeBase.equals(KnowledgeBase.RECIPIENT)){
				if(!subject.getAgentName().equals(sender.getAgentName())){
					K41 w1 = new K41(recipient,sender,subject, att);
					return w1;
				}				
			}
			else if(knowledgeBase.equals(KnowledgeBase.SENDER)){
				if(!subject.getAgentName().equals(sender.getAgentName())){
					K41 w1 = new K41(sender,subject,recipient, att);
					return w1;
				}				
			}
		}
		
		else if(type.equals(K41a.htmlType1)){
			if(knowledgeBase.equals(KnowledgeBase.SUBJECT)){
				if(!subject.getAgentName().equals(sender.getAgentName())){
					K41a w1 = new K41a(subject,sender,recipient, att);
					return w1;
				}				
			}
			else if(knowledgeBase.equals(KnowledgeBase.RECIPIENT)){
				if(!subject.getAgentName().equals(sender.getAgentName())){
					K41a w1 = new K41a(recipient,sender,subject, att);
					return w1;
				}				
			}
			else if(knowledgeBase.equals(KnowledgeBase.SENDER)){
				if(!subject.getAgentName().equals(sender.getAgentName())){
					K41a w1 = new K41a(sender,subject,recipient, att);
					return w1;
				}				
			}
		}	
		
		else if(type.equals(K42.htmlType1)){
			if(knowledgeBase.equals(KnowledgeBase.SUBJECT)){
				K42 w1 = new K42(subject,recipient,sender, att);
				return w1;				
			}
			else if(knowledgeBase.equals(KnowledgeBase.RECIPIENT)){
				if(!subject.getAgentName().equals(sender.getAgentName())){
					K42 w1 = new K42(recipient,subject,sender, att);
					return w1;			
				}						
			}
			else if(knowledgeBase.equals(KnowledgeBase.SENDER)){
				K42 w1 = new K42(sender,recipient,subject, att);
				return w1;				
			}
		}
		
		else if(type.equals(K42a.htmlType1)){
			if(knowledgeBase.equals(KnowledgeBase.SUBJECT)){
				K42a w1 = new K42a(subject,recipient,sender, att);
				return w1;				
			}
			else if(knowledgeBase.equals(KnowledgeBase.RECIPIENT)){
				if(!subject.getAgentName().equals(sender.getAgentName())){
					K42a w1 = new K42a(recipient,subject,sender, att);
					return w1;			
				}						
			}
			else if(knowledgeBase.equals(KnowledgeBase.SENDER)){
				K42a w1 = new K42a(sender,recipient,subject, att);
				return w1;				
			}
		}
		
		return null;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getHtmlType() {
		return htmlType;
	}
	public void setHtmlType(String htmlType) {
		this.htmlType = htmlType;
	}
}