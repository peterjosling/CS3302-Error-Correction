package uk.ac.standrews.s120001757.errorcorrection;

import java.util.Arrays;
import java.util.Random;

public class Main {
	public static void main(String[] args) {
		double errorProb = 0.001;

//		int r = getOptimumR(errorProb, 0.001);
//		encodeAndTransmit(r, errorProb, 12000000);
		encodeAndTransmit(3, 0.1, 280000);
	}

	public static int getOptimumR(double errorProb, double maxCorruption) {
		System.out.println("Error probability: " + errorProb);
		System.out.println("Max corruption: " + maxCorruption);

		int r = HammingCode.getOptimumR(errorProb, maxCorruption);

		if (r == 1) {
			System.out.println("No hamming code meets these requirements.");
			System.exit(0);
		}

		System.out.println("Optimum r: " + r);
		System.out.println("Information rate: " + HammingCode.getInformationRate(r));
		System.out.println("Predicted corruption: " + HammingCode.getCorruptionRate(r, errorProb));

		return r;
	}

	public static void encodeAndTransmit(int r, double errorProb, int length) {
		HammingCode code = new HammingCode(r);
		NoisyChannel channel = new NoisyChannel(errorProb);

		// TODO verify length is a multiple of the data length.

		boolean[] input = new boolean[length];
		Random generator = new Random();

		for (int i = 0; i < length; i++) {
			input[i] = generator.nextBoolean();
		}

		boolean[] output = new boolean[length];

		// Encode the data in dataLength-size chunks.
		int dataLength = (int)Math.pow(2, r) - 1 - r;

		for (int i = 0; i < length; i += dataLength) {
			boolean[] word = Arrays.copyOfRange(input, i, i + dataLength);
			boolean[] encoded = code.encode(word);
			boolean[] corrupted = channel.transmit(encoded);
			boolean[] outputWord = code.decode(corrupted);

			for (int j = 0; j < dataLength; j++) {
				output[i + j] = outputWord[j];
			}
		}

		int corruptedBits = 0;

		for (int i = 0; i < length; i++) {
			if (input[i] != output[i]) {
				corruptedBits++;
			}
		}

		System.out.println("Proportion of bits corrupted: " + (double)corruptedBits / length);
		System.out.println("Predicted proportion: " + HammingCode.getCorruptionRate(r, errorProb));
	}
}
