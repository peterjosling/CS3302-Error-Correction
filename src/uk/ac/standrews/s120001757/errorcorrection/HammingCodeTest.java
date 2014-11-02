package uk.ac.standrews.s120001757.errorcorrection;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

public class HammingCodeTest extends TestCase {

	@Test
	public void testGetInformationRate() {
		assertEquals(HammingCode.getInformationRate(3), 4d/7);
		assertEquals(HammingCode.getInformationRate(4), 11d/15);
		assertEquals(HammingCode.getInformationRate(5), 26d/31);
	}

	@Test
	public void testgetErrorCode() {
		HammingCode code = new HammingCode(2);

		assertTrue(Arrays.equals(code.getErrorCode(new boolean[]{true, true, false}), new boolean[]{false, true}));
		assertTrue(Arrays.equals(code.getErrorCode(new boolean[]{true, true, true}), new boolean[]{false, false}));
	}
}