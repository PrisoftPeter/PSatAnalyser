package psat.util;

import java.io.Serializable;

public class Agent implements Serializable{
	private static final long serialVersionUID = -1905130217368602591L;
	private String agentName;
	private String agentId;
	private String [] otherKnownAgentNames; //other agents on the platform
	private Attribute [] personalAttributes;
	private Attribute [] sharedAttributes;
	private String [] createdMemoryStores;

	private AssertionInstance [] assertionInstances;
	private double desiredEntropy;
	private double desiredCommonKnowledge;
	
	private AssertionRole[] roles;
	private KnowledgeLevel[] knowledgeLevels;
	private double globalPrivacyGoal_v;

	public Agent(String agentName){
		this.agentName = agentName;
		this.globalPrivacyGoal_v = 1;
		this.setDesiredEntropy(0.0);
		
		setOtherKnownAgentNames(new String[0]);
		setPersonalAttributes(new Attribute[0]);
		setSharedAttributes(new Attribute[0]);
		setAssertionInstances(new AssertionInstance[0]);
//		setAssertionRoles(new String[0]);
		setCreatedMemoryStores(new String[0]);
//		setSubjectSafeZones(new SafeZone[0]);
		setKnowledgeLevels(new KnowledgeLevel[0]);
	}

	public void setKnowledgeLevels(KnowledgeLevel[] knowledgeLevels) {
		this.knowledgeLevels = knowledgeLevels;
	}

	//clone for verification purpose only
	private Agent(){
		setPersonalAttributes(new Attribute[0]);
		setSharedAttributes(new Attribute[0]);
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String [] getOtherKnownAgentNames() {
		return otherKnownAgentNames;
	}

	public void setOtherKnownAgentNames(String [] otherKnownAgentNames) {
		this.otherKnownAgentNames = otherKnownAgentNames;
	}

	public void addToOtherKnownAgentNames(String agentName){
		synchronized (Agent.class) {
			boolean contained = false;
			for(String s:otherKnownAgentNames){
				if(s.equals(agentName)){
					contained = true;
					break;
				}
			}
			if(!contained){
				String [] temp = new String[otherKnownAgentNames.length+1];
				for(int i=0;i<otherKnownAgentNames.length;i++){
					temp[i] = otherKnownAgentNames[i];
				}
				temp[otherKnownAgentNames.length] = agentName;		
				otherKnownAgentNames = temp;	
			}		}

	}

	public void removeFromOtherKnownAgentNames(String agentName){
		synchronized (Agent.class) {
			boolean contained = false;
			for(String agentN: otherKnownAgentNames){
				if(agentN.equals(agentName)){
					contained = true;
					break;
				}
			}

			if(contained){
				int j=0;
				String [] temp = new String[otherKnownAgentNames.length-1];
				for(int i=0;i<otherKnownAgentNames.length;i++){
					if(!otherKnownAgentNames[i].equals(agentName)){
						temp[j] = otherKnownAgentNames[i];
						j = j+1;
					}				
				}
				otherKnownAgentNames = temp;
			}
		}
	}

	public Attribute [] getPersonalAttributes() {
		return personalAttributes;
	}

	public void setPersonalAttributes(Attribute [] personalAttributes) {
		this.personalAttributes = personalAttributes;
	}
	
	public void resetPersonalAttributes() {
		this.personalAttributes = new Attribute[0];
	}

	public void addToPersonalAttributes(Attribute attribute){
		synchronized (Agent.class) {
			boolean exist = false;
			for(Attribute att: personalAttributes){
				if(att.sameAs(attribute)){
					exist = true;
					break;
				}
			}

			if(exist){
				return;
			}
			Attribute [] temp = new Attribute[personalAttributes.length+1];
			for(int i=0;i<personalAttributes.length;i++){
				temp[i] = personalAttributes[i];
			}
			temp[personalAttributes.length] = attribute;		
			personalAttributes = temp;		}

	}

	public void removeFromPersonalAttributes(Attribute attribute){
		synchronized (Agent.class) {
			boolean contained = false;
			for(Attribute att: personalAttributes){
				if(att.sameAs(attribute)){
					contained = true;
					break;
				}
			}

			if(contained){
				int j=0;
				Attribute [] temp = new Attribute[personalAttributes.length-1];
				for(int i=0;i<personalAttributes.length;i++){
					if(!personalAttributes[i].sameAs(attribute)){
						temp[j] = personalAttributes[i];
						j = j+1;
					}				
				}
				personalAttributes = temp;
			}
		}

	}
	
	public AssertionInstance [] getAssertionInstances() {
		assertionInstances = ArrayCleaner.clean(assertionInstances);
		return assertionInstances;
	}
//	public String [] getAssertionRoles() {
//		return assertionRoles;
//	}

	public void setAssertionInstances(AssertionInstance [] assertionInstances) {
		this.assertionInstances = assertionInstances;
	}
//	public void setAssertionRoles(String [] assertionRoles) {
//		this.assertionRoles = assertionRoles;
//	}
		
//	public void resetAssertionRoles() {
//		this.assertionRoles = new String[0];
//	}
	
	public void resetAssertionInstances() {
		this.assertionInstances = new AssertionInstance[0];
	}
	
	public void updateAssertionInstance(String world_text, double goalv, CollectiveStrategy cs){
		assertionInstances = ArrayCleaner.clean(assertionInstances);
		boolean exist = false;
		for(AssertionInstance w_s: assertionInstances){
			if(w_s.getAssertion().equals(world_text)){
				exist = true;
				break;
			}
		}

		if(exist){//update goal
			AssertionInstance [] temp = new AssertionInstance[assertionInstances.length+1];
			for(int i=0;i<assertionInstances.length;i++){
				if(assertionInstances[i].getAssertion().equals(world_text)){
					assertionInstances[i].setGoalv(goalv);
					assertionInstances[i].setCollectiveStragegy(cs);
				}
				temp[i] = assertionInstances[i];
			}
			assertionInstances = temp;	
		}		
	}

	public void addAssertionInstance(String world_text, double goalv, CollectiveStrategy cs){
		assertionInstances = ArrayCleaner.clean(assertionInstances);
		boolean exist = false;
		for(AssertionInstance w_s: assertionInstances){
			if(w_s.getAssertion().equals(world_text)){
				exist = true;
				break;
			}
		}

		if(exist){//update goal
			AssertionInstance [] temp = new AssertionInstance[assertionInstances.length+1];
			for(int i=0;i<assertionInstances.length;i++){
				if(assertionInstances[i].getAssertion().equals(world_text)){
					assertionInstances[i].setGoalv(goalv);
					assertionInstances[i].setCollectiveStragegy(cs);;
				}
				temp[i] = assertionInstances[i];
			}
			assertionInstances = temp;	
		}
		else{
			AssertionInstance [] temp = new AssertionInstance[assertionInstances.length+1];
			for(int i=0;i<assertionInstances.length;i++){
				temp[i] = assertionInstances[i];
			}
			temp[assertionInstances.length] = new AssertionInstance(world_text, goalv,cs);		
			assertionInstances = temp;	
		}		
		assertionInstances = ArrayCleaner.clean(assertionInstances);
	}
	
//	public void addAssertionRole(String world_text){
//		ArrayCleaner.clean(assertionRoles);
//		boolean exist = false;
//		for(String w_s: assertionRoles){
//			if(w_s.equals(world_text)){
//				exist = true;
//				break;
//			}
//		}
//
//		if(exist){
//			return;
//		}
//		String [] temp = new String[assertionRoles.length+1];
//		for(int i=0;i<assertionRoles.length;i++){
//			temp[i] = assertionRoles[i];
//		}
//		temp[assertionRoles.length] = world_text;		
//		assertionRoles = temp;	
//	}

	public void removeAssertionInstance(String world_text){
		assertionInstances= ArrayCleaner.clean(assertionInstances);
		boolean contained = false;
		for(AssertionInstance w_s: assertionInstances){
			if(w_s.getAssertion().equals(world_text)){
				contained = true;
				break;
			}
		}

		if(contained){
			int j=0;
			AssertionInstance [] temp = new AssertionInstance[assertionInstances.length-1];
			for(int i=0;i<assertionInstances.length;i++){
				if(!assertionInstances[i].getAssertion().equals(world_text)){
					temp[j] = assertionInstances[i];
					j = j+1;
				}				
			}
			assertionInstances = temp;
		}
	}
	
//	public void removeAssertionRole(String world_text){
//		ArrayCleaner.clean(assertionRoles);
//		boolean contained = false;
//		for(String w_s: assertionRoles){
//			if(w_s.equals(world_text)){
//				contained = true;
//				break;
//			}
//		}
//
//		if(contained){
//			int j=0;
//			String [] temp = new String[assertionRoles.length-1];
//			for(int i=0;i<assertionRoles.length;i++){
//				if(!assertionRoles[i].equals(world_text)){
//					temp[j] = assertionRoles[i];
//					j = j+1;
//				}				
//			}
//			assertionRoles = temp;
//		}
//	}
	
//	public void setSubjectSafeZones(SafeZone [] subjectSafeZones) {
//		this.subjectSafeZones = subjectSafeZones;
//	}
//	
//	public SafeZone[] getSubjectSafeZones(){
//		return subjectSafeZones;
//	}
//	
//	public void addToSubjectSafeZones(SafeZone safezone){
//		boolean exist = false;
//		for(SafeZone sz: subjectSafeZones){
//			if(safezone.subjectName.equals(sz.subjectName)){
//				exist = true;
//				break;
//			}			
//		}
//
//		int len = subjectSafeZones.length;
//		if(exist){
//			SafeZone []temp = new SafeZone[len];
//			for(int i=0;i<len;i++){
//				if(safezone.subjectName.equals(subjectSafeZones[i].subjectName)){
//					temp[i]= safezone;
//				}
//				else{
//					temp[i]= subjectSafeZones[i];	
//				}
//				
//			}
//			subjectSafeZones = temp;
//		}
//		else{
//			SafeZone []temp = new SafeZone[len+1];
//			for(int i=0;i<len;i++){
//				temp[i]= subjectSafeZones[i];				
//			}
//			temp[len] = safezone;
//			
//			subjectSafeZones = temp;
//		}
//
//	}
//	
//	public void removeFromSubjectSafeZones(String subjectName){
//		boolean exist = false;
//		for(SafeZone sz: subjectSafeZones){
//			if(sz.subjectName.equals(subjectName)){
//				exist = true;
//				break;
//			}			
//		}
//
//		int len = subjectSafeZones.length;
//		if(exist){
//			SafeZone []temp = new SafeZone[len-1];
//			int j=0;
//			for(int i=0;i<len;i++){
//				if(!subjectSafeZones[i].subjectName.equals(subjectName)){
//					temp[j]= subjectSafeZones[i];
//					j = j+1;
//				}
//			}
//			subjectSafeZones = temp;
//		}
//	}
//
//	
//	public SafeZone getSubjectSafeZone(String subjectName){
//		SafeZone safezone = null;
//		for(SafeZone sz: subjectSafeZones){
//			if(sz.subjectName.equals(subjectName)){
//				safezone = sz;
//				break;
//			}			
//		}
//		return safezone;
//
//	}
	
	
	public void setCreatedMemoryStores(String [] createdMemoryStores) {
		this.createdMemoryStores = createdMemoryStores;
	}
	
	public String[] getCreatedMemoryStores(){
		return createdMemoryStores;
	}
	
	public void addToCreatedMemoryStores(String subjectName){
		boolean exist = false;
		for(String s: createdMemoryStores){
			if(s.equals(subjectName)){
				exist = true;
				break;
			}
		}

		if(exist){
			return;
		}

		int len = createdMemoryStores.length;
		String [] temp = new String[len+1];
		for(int i=0;i<len;i++){
			temp[i] = createdMemoryStores[i];
		}
		temp[len] = subjectName;		
		createdMemoryStores = temp;
	}
	
	public boolean containedInMemoryStores(String subjectName){
		boolean exist = false;
		for(String s: createdMemoryStores){
			if(s.equals(subjectName)){
				exist = true;
				break;
			}
		}
		return exist;

	}


	public Attribute [] getSharedAttributes() {
		return sharedAttributes;
	}

	public void setSharedAttributes(Attribute [] sharedAttributes) {
		this.sharedAttributes = sharedAttributes;
	}

	public void addToSharedAttributes(Attribute attribute){
		synchronized (Agent.class) {
			boolean exist = false;
			for(Attribute att: sharedAttributes){
				if(att.sameAs(attribute)){
					exist = true;
					break;
				}
			}

			if(exist){
				return;
			}

			int len = sharedAttributes.length;
			Attribute [] temp = new Attribute[len+1];
			for(int i=0;i<len;i++){
				temp[i] = sharedAttributes[i];
			}
			temp[len] = attribute;		
			sharedAttributes = temp;
		}

	}

	public void clearSharedAttributes(){
		synchronized (Agent.class) {
			for(Attribute att:sharedAttributes){
				if(att.getSubjectName().equals(agentName)){
					addToPersonalAttributes(att);
				}
			}
			sharedAttributes = new Attribute[0];
		}

	}

	//clone for verification purpose only
	public Agent clone(){
		Agent eclone = new Agent();
		eclone.agentName = agentName;
		eclone.agentId = agentId;

		for(Attribute att: personalAttributes){
			eclone.addToPersonalAttributes(att);
		}
		for(Attribute att: sharedAttributes){
			eclone.addToSharedAttributes(att);
		}

		return eclone;
	}

	public String toString(){
		String eol = System.getProperty("line.separator");
		String s = "AgentName:";
		s = s+ agentName+ eol;
		s = s+ "KnownEntities:"+ eol;

		for(int i =0;i< otherKnownAgentNames.length; i++){
			s = s +otherKnownAgentNames[i];
			if(i<otherKnownAgentNames.length-1){
				s =s+", ";
			}
		}
		s = s + eol;
		s = s+ "PersonalAttributes:"+ eol;
		for(int i =0;i< personalAttributes.length; i++){
			s = s +personalAttributes[i];
			if(i<personalAttributes.length-1){
				s =s+", ";
			}
		}
		s = s + eol;
		s = s+ "SharedAttributes:"+ eol;
		for(int i =0;i< sharedAttributes.length; i++){
			s = s +sharedAttributes[i];
			if(i<sharedAttributes.length-1){
				s =s+", ";
			}
		}
		return s;
	}
	

	public AssertionRole[] getRoles() {
		return roles;
	}

	public void setRoles(AssertionRole[] roles) {
		this.roles = roles;
	}
	
	public void resetRoles() {
		this.roles = new AssertionRole[0];
	}
	
	public void updateRole(AssertionRole role){
		if(roles == null){
			roles = new AssertionRole[0];
		}
		//update role
		AssertionRole [] temp = new AssertionRole[roles.length];
		for(int i=0;i< roles.length;i++){
			if(roles[i].getSelfAgentName().equals(role.getSelfAgentName()) 
					&& roles[i].getRoleType().equals(role.getRoleType())
					&& roles[i].getKnowledgeBase() != null
					&& roles[i].getKnowledgeBase().equals(role.getKnowledgeBase())){
				temp[i] = role;

			}
			else if(roles[i].getSelfAgentName().equals(role.getSelfAgentName()) 
					&& roles[i].getRoleType().equals(role.getRoleType())
					&& roles[i].getKnowledgeBase() == null && role.getKnowledgeBase() == null){
				temp[i] = role;

			}
			else{
				temp[i] = roles[i];
			}
		}
		
		roles = temp;
	}
	

	public void addRole(AssertionRole role){
		boolean exist = false;
		if(roles == null){
			roles = new AssertionRole[0];
		}
		for(AssertionRole a:roles){
			if(a.getSelfAgentName().equals(role.getSelfAgentName()) 
					&& a.getRoleType().equals(role.getRoleType())
					&& a.getKnowledgeBase() !=null
					&& a.getKnowledgeBase().equals(role.getKnowledgeBase())){
				exist =true;
				break;
			}
		}
		
		if(!exist){
			//add role
			AssertionRole [] temp = new AssertionRole[roles.length +1];
			for(int i=0;i< roles.length;i++){
				temp[i] = roles[i];
			}
			temp[roles.length] = role;
			
			roles = temp;
		}
		else{
			//update role
			AssertionRole [] temp = new AssertionRole[roles.length];
			for(int i=0;i< roles.length;i++){
				if(roles[i].getSelfAgentName().equals(role.getSelfAgentName()) 
						&& roles[i].getRoleType().equals(role.getRoleType())
						&& roles[i].getKnowledgeBase() != null
						&& roles[i].getKnowledgeBase().equals(role.getKnowledgeBase())){
					temp[i] = role;

				}
				else if(roles[i].getSelfAgentName().equals(role.getSelfAgentName()) 
						&& roles[i].getRoleType().equals(role.getRoleType())
						&& roles[i].getKnowledgeBase() == null && role.getKnowledgeBase() == null){
					temp[i] = role;

				}
				else{
					temp[i] = roles[i];
				}
			}
			
			roles = temp;
		}
	}
	
	
	public void removeRole(AssertionRole role){
		boolean exist = false;
		if(roles == null){
			roles = new AssertionRole[0];
		}
		for(AssertionRole a:roles){
			if(a.getSelfAgentName().equals(role.getSelfAgentName()) 
					&& a.getRoleType().equals(role.getRoleType())
					&& a.getKnowledgeBase().equals(role.getKnowledgeBase())){
				exist =true;
				break;
			}
		}
		
		if(exist){
			AssertionRole temp[] = new AssertionRole[roles.length-1];
			int j=0;
			for(int i=0;i< roles.length;i++){
				AssertionRole a = roles[i];
				if(a.getSelfAgentName().equals(role.getSelfAgentName()) 
						&& a.getRoleType().equals(role.getRoleType())
						&& a.getKnowledgeBase().equals(role.getKnowledgeBase())){
					//do nothing
				}
				else{
					temp[j] = a;
					j = j+1;
				}
			}
			roles = temp;
		}
	}
	
	public boolean roleExist(AssertionRole role){
		boolean exist = false;
		if(roles == null){
			roles = new AssertionRole[0];
		}
		for(AssertionRole a:roles){
			if(a.getSelfAgentName().equals(role.getSelfAgentName()) 
					&& a.getRoleType().equals(role.getRoleType())
					&& a.getKnowledgeBase().equals(role.getKnowledgeBase())){
				exist =true;
				break;
			}
		}
		
		return exist;
	}
	
	public void emptyRoles(){
		roles = new AssertionRole[0];
	}
	
	public AssertionRole getRole(String selfAgentName, String roleType, KnowledgeBase knowledgeBase){
		AssertionRole role = null;
		if(roles == null){
			roles = new AssertionRole[0];
		}
		for(AssertionRole a:roles){
			if(a.getSelfAgentName().equals(selfAgentName) 
					&& a.getRoleType().equals(roleType)
					&& a.getKnowledgeBase().equals(knowledgeBase)){
				role = a;
				break;
			}
		}
		return role;
	}
	
	public boolean roleExist(String selfAgentName, String roleType, KnowledgeBase knowledgeBase){
		boolean exist = false;
		if(roles == null){
			roles = new AssertionRole[0];
		}
		for(AssertionRole a:roles){
			if(a.getSelfAgentName().equals(selfAgentName) 
					&& a.getRoleType().equals(roleType)
					&& a.getKnowledgeBase() !=null
					&& a.getKnowledgeBase().equals(knowledgeBase)){
				exist =true;
				break;
			}
		}
		
		return exist;
	}
	
	public double getRoleVGoal(String selfAgentName, String roleType, KnowledgeBase knowledgeBase){
		if(roles == null){
			roles = new AssertionRole[0];
		}
		for(AssertionRole a:roles){
			if(a.getSelfAgentName().equals(selfAgentName) 
					&& a.getRoleType().equals(roleType)
					&& a.getKnowledgeBase() !=null
					&& a.getKnowledgeBase().equals(knowledgeBase)){
				
				return a.getGoalv();
				
			}
		}
		
		return -1;
	}
	
	public void addKnowledgeLevel(KnowledgeLevel knowledgeLevel){
		boolean exist = false;
		if(knowledgeLevels == null){
			knowledgeLevels = new KnowledgeLevel[0];
		}
		for(KnowledgeLevel k:knowledgeLevels){
			if(k.getKnowledgeBase() !=null && k.getSelfAgentName().equals(knowledgeLevel.getSelfAgentName())
					&& k.getKnowledgeBase().equals(knowledgeLevel.getKnowledgeBase())){
				exist =true;
				break;
			}
		}
		
		if(!exist){
			//add knowledgeLevel
			KnowledgeLevel [] temp = new KnowledgeLevel[knowledgeLevels.length +1];
			for(int i=0;i< knowledgeLevels.length;i++){
				temp[i] = knowledgeLevels[i];
			}
			temp[knowledgeLevels.length] = knowledgeLevel;
			
			knowledgeLevels = temp;
		}
		else{
			//update knowledgeLevel
			KnowledgeLevel [] temp = new KnowledgeLevel[knowledgeLevels.length];
			for(int i=0;i< knowledgeLevels.length;i++){
				if(knowledgeLevels[i].getSelfAgentName().equals(knowledgeLevel.getSelfAgentName())
						&& knowledgeLevels[i].getKnowledgeBase() !=null && knowledgeLevel.getKnowledgeBase() !=null 
						&& knowledgeLevels[i].getKnowledgeBase().equals(knowledgeLevel.getKnowledgeBase())){
					temp[i] = knowledgeLevel;

				}
				else{
					temp[i] = knowledgeLevels[i];
				}
			}
			knowledgeLevels = temp;
		}
		cleanKnowledgeLevels();
	}
	
	public void cleanKnowledgeLevels(){
		KnowledgeLevel [] dirty = new KnowledgeLevel[0];
		for(KnowledgeLevel kl:knowledgeLevels){
			if(kl.getBeliefLevel() ==0 && kl.getUncertaintyLevel() ==0){
				KnowledgeLevel temp [] = new KnowledgeLevel[dirty.length+1];
				for(int i=0;i <dirty.length;i++){
					temp[i] = dirty[i];
				}
				dirty = temp;
			}
		}
		
		for(KnowledgeLevel kl: dirty){
			if(kl!=null){
				removeKnowledgeLevel(kl);				
			}
		}
	}
	
	
	public void removeKnowledgeLevel(KnowledgeLevel knowledgeLevel){
		boolean exist = false;
		if(knowledgeLevels == null){
			knowledgeLevels = new KnowledgeLevel[0];
		}
		for(KnowledgeLevel k:knowledgeLevels){
			if(k.getSelfAgentName().equals(knowledgeLevel.getSelfAgentName())
					&& k.getKnowledgeBase().equals(knowledgeLevel.getKnowledgeBase())){
				exist =true;
				break;
			}
		}
		
		if(exist){
			KnowledgeLevel temp[] = new KnowledgeLevel[knowledgeLevels.length-1];
			int j=0;
			for(int i=0;i< knowledgeLevels.length;i++){
				KnowledgeLevel k= knowledgeLevels[i];
				if(k.getSelfAgentName().equals(knowledgeLevel.getSelfAgentName())
						&& k.getKnowledgeBase().equals(knowledgeLevel.getKnowledgeBase())){
					//do nothing
				}
				else{
					temp[j] = k;
					j = j+1;
				}
			}
			knowledgeLevels = temp;
		}
	}
	
	
	public KnowledgeLevel getKnowledgeLevel(String selfAgentName, KnowledgeBase knowledgeBase){
		KnowledgeLevel knowledgeLevel = null;
		if(knowledgeLevels == null){
			knowledgeLevels = new KnowledgeLevel[0];
		}
		for(KnowledgeLevel k:knowledgeLevels){
			if(k.getKnowledgeBase() != null && k.getSelfAgentName().equals(selfAgentName)
					&& k.getKnowledgeBase().equals(knowledgeBase)){
				knowledgeLevel = k;
				break;
			}
		}
		return knowledgeLevel;
	}
	
	public KnowledgeLevel [] getKnowledgeLevels(){
		if(knowledgeLevels == null){
			knowledgeLevels = new KnowledgeLevel[0];
		}
		return knowledgeLevels;
	}
	
	public boolean knowledgeLevelExist(String selfAgentName, KnowledgeBase knowledgeBase){
		boolean exist = false;
		if(knowledgeLevels == null){
			knowledgeLevels = new KnowledgeLevel[0];
		}
		for(KnowledgeLevel kl:knowledgeLevels){
			if(kl.getSelfAgentName().equals(selfAgentName)
					&& kl.getKnowledgeBase().equals(knowledgeBase)){
				exist =true;
				break;
			}
		}
		
		return exist;
	}

	public double getGlobalPrivacyGoal_v() {
		return globalPrivacyGoal_v;
	}

	public void setGlobalPrivacyGoal_v(double globalPrivacyGoal_v) {
		this.globalPrivacyGoal_v = globalPrivacyGoal_v;
	}

	public double getDesiredEntropy() {
		return desiredEntropy;
	}

	public void setDesiredEntropy(double desiredEntropy) {
		this.desiredEntropy = desiredEntropy;
	}
	
	public double getDesiredCommonKnowledge() {
		return desiredCommonKnowledge;
	}

	public void setDesiredCommonKnowledge(double desiredCommonKnowledge) {
		this.desiredCommonKnowledge = desiredCommonKnowledge;
	}
}
