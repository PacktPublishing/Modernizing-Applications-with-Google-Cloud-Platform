package uk.me.jasonmarston.mvc.alerts;

public abstract class AbstractAlert {
	protected String type;
	private String key;
	
	public AbstractAlert(final String key) {
		this.key = key;
	}
	
	public String getKey() {
		return key;
	}
	
	public String getType() {
		return type;
	}
}
