package uk.ac.standrews.s120001757.errorcorrection;

public class HammingCode {
	private static final int R_BOUND = 100;

	private int r;
	private int length;
	private boolean[][] parityCheck;
	private boolean[][] generator;

	public HammingCode(int r) {
		this.r = r;
		this.length = (int)Math.pow(2, r) - 1;

		parityCheck = new boolean[length][r];
		generator = new boolean[length - r][length];

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
		for (int i = 0; i < length; i++) {
			int dataLength = length - r;

			// Insert line of identity matrix.
			for (int j = 0; j < dataLength; j++) {
				generator[i][j] = i == j;
			}

			// Copy in line of parity check matrix.
			for (int j = 0; j < r; j++) {
				generator[i][dataLength + j] = parityCheck[i][j];
			}
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

	public boolean[] decode(boolean[] code) {
		if (code.length != length) {
			return new boolean[0];
		}

		boolean[] data = new boolean[length - r];

		

		return data;
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

		// Proportion of corrupted bits is 1 - (probability of 0 corrupted + probability of 1 corrupted).
		double probZeroErrors = Math.pow(iChannelNoise, length);
		double probOneError = length * Math.pow(iChannelNoise, length - 1) * channelNoise;

		return 1 - (probZeroErrors + probOneError);
	}
}
