package uk.me.jasonmarston.framework.authentication.impl;

import java.util.Map;

import com.google.firebase.auth.FirebaseToken;

public class Token {
	private FirebaseToken token;

	Token(FirebaseToken token) {
		this.token = token;
	}

	public boolean equals(Object obj) {
		if(obj instanceof Token) {
			return token.equals(((Token)obj).token);
		}
		if(obj instanceof FirebaseToken) {
			return token.equals(obj);
		}
		return false;
	}

	public Map<String, Object> getClaims() {
		return token.getClaims();
	}

	public String getEmail() {
		return token.getEmail();
	}

	public String getIssuer() {
		return token.getIssuer();
	}

	public String getName() {
		return token.getName();
	}

	public String getPicture() {
		return token.getPicture();
	}

	public String getUid() {
		return token.getUid();
	}

	public int hashCode() {
		return token.hashCode();
	}

	public boolean isEmailVerified() {
		return token.isEmailVerified();
	}

	public String toString() {
		return token.toString();
	}
}
