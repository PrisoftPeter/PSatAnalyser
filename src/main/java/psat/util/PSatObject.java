package psat.util;

import java.io.Serializable;
import java.util.Properties;

public class PSatObject implements Serializable{
	private static final long serialVersionUID = -822095705140175654L;
	
	private String sendersSessionId;
	private String intendedRecipientSessionId;
	private String actionType;
	private Properties properties; //metadata	
	private Object object;
	
	public Properties getProperties() {
		return properties;
	}
	public void setProperties(Properties properties) {
		this.properties = properties;
	}
	public String getSendersSessionId() {
		return sendersSessionId;
	}
	public void setSendersSessionId(String sendersSessionId) {
		this.sendersSessionId = sendersSessionId;
	}
	public String getIntendedRecipientSessionId() {
		return intendedRecipientSessionId;
	}
	public void setIntendedRecipientSessionId(String intendedRecipientSessionId) {
		this.intendedRecipientSessionId = intendedRecipientSessionId;
	}
	public String getActionType() {
		return actionType;
	}
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
}
