package uk.ac.standrews.s120001757.errorcorrection;

import java.math.BigInteger;
import java.util.Arrays;

public class HammingCode {
	private static final int R_BOUND = 10;
	private static final long[][] PASCAL_TRIANGLE = new long[R_BOUND + 1][];

	private int r;
	private int length;
	private boolean[][] parityCheck;
	private boolean[][] generator;
	private boolean[][] errorCodes;

	public HammingCode(int r) {
		this.r = r;
		this.length = (int)Math.pow(2, r) - 1;

		parityCheck = new boolean[length][r];
		generator = new boolean[length - r][length];
		errorCodes = new boolean[length + 1][length];

		// Populate parity check matrix.
		int currentRow = 0;
		int currentIdentityRow = length - r;

		for (int i = length; i > 0; i--) {
			int row;

			// If i is a power of two, it's part of the identity matrix and goes at the bottom.
			if ((i & (i - 1)) == 0) {
				row = currentIdentityRow++;
			} else {
				row = currentRow++;
			}

			// Insert the current int as binary to the correct index.
			for (int j = 0; j < r; j++) {
				parityCheck[row][j] = (i & (1 << (r - 1 - j))) != 0;
			}
		}

		// Transform to generator matrix.
		int dataLength = length - r;

		for (int i = 0; i < dataLength; i++) {
			// Insert line of identity matrix.
			for (int j = 0; j < dataLength; j++) {
				generator[i][j] = i == j;
			}

			// Copy in line of parity check matrix.
			for (int j = 0; j < r; j++) {
				generator[i][dataLength + j] = parityCheck[i][j];
			}
		}

		// Precalculate syndromes and error codes.
		for (int i = 0; i < length; i++) {
			boolean[] code = new boolean[length];
			code[i] = true;

			int syndrome = booleanArrayToInt(getSyndrome(code));
			errorCodes[syndrome][i] = true;
		}
	}

	public boolean[] encode(boolean[] data) {
		if (data.length != length - r) {
			return new boolean[0];
		}

		boolean[] output = new boolean[length];

		for (int i = 0; i < data.length; i++) {
			if (!data[i]) {
				continue;
			}

			boolean[] row = generator[i];

			for (int j = 0; j < length; j++) {
				output[j] ^= row[j];
			}
		}

		return output;
	}

	public boolean[] decode(boolean[] codeword) {
		if (codeword.length != length) {
			return new boolean[0];
		}

		boolean[] syndrome = getSyndrome(codeword);
		int syndromeInt = booleanArrayToInt(syndrome);
		boolean[] errorCode = errorCodes[syndromeInt];

		// Correct input data using error code.
		boolean[] corrected = new boolean[length];

		for (int i = 0; i < length; i++) {
			corrected[i] = codeword[i] ^ errorCode[i];
		}

		// Return data with parity bits removed.
		return Arrays.copyOf(corrected, length - r);
	}

	public boolean[] getSyndrome(boolean[] codeword) {
		if (codeword.length != length) {
			return new boolean[0];
		}

		boolean[] error = new boolean[r];

		for (int i = 0; i < r; i++) {
			for (int j = 0; j < length; j++) {
				error[i] ^= codeword[j] & parityCheck[j][i];
			}
		}

		return error;
	}

	public static int getOptimumR(double channelNoise, double maxCorruption) {
		int r = 1;
		double totalCorruption = 0;

		while (totalCorruption < maxCorruption && r <= R_BOUND) {
			r++;
			totalCorruption = getCorruptionRate(r, channelNoise);
		}

		return r - 1;
	}

	public static double getInformationRate(int r) {
		int length = (int)Math.pow(2, r) - 1;

		return (double)(length - r) / length;
	}

	public static double getCorruptionRate(int r, double channelNoise) {
		int length = (int)Math.pow(2, r) - 1;
		double iChannelNoise = 1 - channelNoise;

		double rate = 0;

		for (int i = 2; i <= length; i++) {
			int bitsCorrupted = (i + 1) / 3 * 3;

			rate += nCr(length, i) * Math.pow(channelNoise, i) * Math.pow(iChannelNoise, length - i) * ((double)bitsCorrupted / length);
		}

		return rate;
	}

	public static int booleanArrayToInt(boolean[] booleans) {
		int n = 0;

		for (int i = 0; i < booleans.length; i++) {
			n = (n << 1) | (booleans[i] ? 1 : 0);
		}

		return n;
	}

	static double nCr(final int N, final int K) {
		BigInteger ret = BigInteger.ONE;
		for (int k = 0; k < K; k++) {
			ret = ret.multiply(BigInteger.valueOf(N-k))
					.divide(BigInteger.valueOf(k+1));
		}
		return ret.doubleValue();
	}

//	public static double nCr(int n, int r) {
//		return factorial(n) / (factorial(n - r) * factorial(r));
//	}

//	public static long nCr(int n, int r) {
//		if (PASCAL_TRIANGLE[n] == null) {
//			generatePascalRow(n);
//		}
//
//		return PASCAL_TRIANGLE[n][r];
//	}
//
//	private static void generatePascalRow(int n) {
//		if (n > 1 && PASCAL_TRIANGLE[n - 1] == null) {
//			generatePascalRow(n - 1);
//		}
//
//		long[] row = new long[n + 1];
//		long[] prevRow = PASCAL_TRIANGLE[n - 1];
//
//		row[0] = row[n] = 1;
//
//		for (int i = 1; i < n; i++) {
//			row[i] = prevRow[i - 1] + prevRow[i];
//		}
//
//		PASCAL_TRIANGLE[n] = row;
//	}

	public static double factorial(int n) {
		if (n <= 1) {
			return 1;
		}

		return n * factorial(n - 1);
	}
}
