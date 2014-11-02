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
	public void testGetSyndrome() {
		HammingCode code = new HammingCode(2);

		assertTrue(Arrays.equals(code.getSyndrome(new boolean[]{true, true, false}), new boolean[]{false, true}));
		assertTrue(Arrays.equals(code.getSyndrome(new boolean[]{true, true, true}), new boolean[]{false, false}));
	}

	@Test
	public void testBooleanArrayToInt() {
		boolean[] one = new boolean[]{true};
		boolean[] two = new boolean[]{true, false};
		boolean[] three = new boolean[]{true, true};
		boolean[] ten = new boolean[]{true, false, true, false};

		assertEquals(HammingCode.booleanArrayToInt(one), 1);
		assertEquals(HammingCode.booleanArrayToInt(two), 2);
		assertEquals(HammingCode.booleanArrayToInt(three), 3);
		assertEquals(HammingCode.booleanArrayToInt(ten), 10);
	}
}