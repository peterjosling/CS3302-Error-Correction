package uk.ac.standrews.s120001757.errorcorrection;

import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.*;

public class HammingCodeTest extends TestCase {

	@Test
	public void testGetInformationRate() throws Exception {
		assertEquals(HammingCode.getInformationRate(3), 4d/7);
		assertEquals(HammingCode.getInformationRate(4), 11d/15);
		assertEquals(HammingCode.getInformationRate(5), 26d/31);
	}
}