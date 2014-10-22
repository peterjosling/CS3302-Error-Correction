package uk.ac.standrews.s120001757.errorcorrection;

import java.util.BitSet;
import java.util.Random;

public class NoisyChannel {
	private double p;
	Random generator = new Random();

	public NoisyChannel(double p) {
		this.p = p;
	}

	public BitSet transmit(BitSet input) {
		BitSet output = (BitSet)input.clone();
		int length = output.size();

		for (int i = 0; i < length; i++) {
			if (generator.nextDouble() >= p) {
				output.flip(i);
			}
		}

		return output;
	}
}
