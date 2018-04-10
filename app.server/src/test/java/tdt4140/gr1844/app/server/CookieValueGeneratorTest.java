package tdt4140.gr1844.app.server;

import org.junit.Assert;
import org.junit.Test;
import tdt4140.gr1844.app.core.CookieValueGenerator;

public class CookieValueGeneratorTest {
	@Test
	public void cookieValueLengthTest() {
		int length = 32;
		String cookieValue = CookieValueGenerator.generateCookieValue();
		Assert.assertEquals(length, cookieValue.length());
	}
}
