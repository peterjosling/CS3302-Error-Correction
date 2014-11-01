package uk.ac.standrews.s120001757.errorcorrection;

import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.*;

public class HammingCodeTest extends TestCase {

	@Test
	public void testNCr() throws Exception {
		assertEquals(HammingCode.nCr(1, 1), 1);
		assertEquals(HammingCode.nCr(5, 2), 10);
		assertEquals(HammingCode.nCr(10, 5), 252);
	}

	@Test
	public void testFactorial() throws Exception {
		assertEquals(HammingCode.factorial(1), 1);
		assertEquals(HammingCode.factorial(2), 2);
		assertEquals(HammingCode.factorial(3), 6);
		assertEquals(HammingCode.factorial(5), 120);

		// Should not error.
		assertEquals(HammingCode.factorial(-2), HammingCode.factorial(-2));
	}
}