package tdt4140.gr1844.app.core;

import java.security.SecureRandom;

public class CookieValueGenerator {
	private final static String acceptedChars = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890";
	private final static SecureRandom random = new SecureRandom();
	public static String generateCookieValue() {
		StringBuilder cookieValueBuilder = new StringBuilder();
		while (cookieValueBuilder.length() < 32) {
			int index = random.nextInt(acceptedChars.length());
			cookieValueBuilder.append(acceptedChars.charAt(index));
		}
		return cookieValueBuilder.toString();
	}
}
