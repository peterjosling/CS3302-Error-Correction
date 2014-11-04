package uk.ac.standrews.s120001757.errorcorrection;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.Arrays;

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

	@Test
	public void testEncode() {
		HammingCode code2 = new HammingCode(2);
		HammingCode code3 = new HammingCode(3);

		assertTrue(Arrays.equals(code2.encode(new boolean[]{true}), new boolean[]{true, true, true}));
		assertTrue(Arrays.equals(code2.encode(new boolean[]{false}), new boolean[]{false, false, false}));

		assertTrue(Arrays.equals(code3.encode(new boolean[]{false, true, false, true}), new boolean[]{false, true, false, true, true, false, true}));
	}

	@Test
	public void testDecode() {
		HammingCode code2 = new HammingCode(2);
		HammingCode code3 = new HammingCode(3);

		boolean[] input1 = new boolean[]{true};
		boolean[] input2 = new boolean[]{false, true, false, true};

		boolean[] enc1 = code2.encode(input1);
		boolean[] enc2 = code3.encode(input2);
		boolean[] corrupted = enc2.clone();

		corrupted[3] = !corrupted[3];

		assertTrue(Arrays.equals(code2.decode(enc1), input1));
		assertTrue(Arrays.equals(code3.decode(enc2), input2));
		assertTrue(Arrays.equals(code3.decode(corrupted), input2));
	}

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