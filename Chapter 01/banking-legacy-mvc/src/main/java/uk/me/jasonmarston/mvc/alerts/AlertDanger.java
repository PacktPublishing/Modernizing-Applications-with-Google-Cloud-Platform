package uk.me.jasonmarston.mvc.alerts;

public class AlertDanger extends AbstractAlert {
	public AlertDanger(final String key) {
		super(key);
		type = "alert-danger";
	}
}
