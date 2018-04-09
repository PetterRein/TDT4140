package tdt4140.gr1844.app.core;

import org.junit.Assert;
import org.junit.Test;

public class CookieValueGeneratorTest {
	@Test
	public void cookieValueLengthTest() {
		int length = 32;
		String cookieValue = CookieValueGenerator.generateCookieValue(length);
		Assert.assertEquals(length, cookieValue.length());
	}
}
