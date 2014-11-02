package uk.ac.standrews.s120001757.errorcorrection;

import java.util.Arrays;
import java.util.Random;

public class Main {
	public static void main(String[] args) {
		getOptimumR();
		encodeAndTransmit();
	}

	public static void getOptimumR() {
		double errorProb = 0.001;
		double maxCorruption = 0.01;

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
	}

	public static void encodeAndTransmit() {
		double errorProb = 0.001;
		int r = 7;
		int length = 1200000;

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
	}
}
