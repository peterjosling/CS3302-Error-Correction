package uk.ac.standrews.s120001757.errorcorrection;

import java.util.Random;

public class NoisyChannel {
	private double p;
	Random generator = new Random();

	public NoisyChannel(double p) {
		this.p = p;
	}

	public boolean[] transmit(boolean[] input) {
		boolean[] output = input.clone();

		for (int i = 0; i < output.length; i++) {
			if (generator.nextDouble() <= p) {
				output[i] = !output[i];
			}
		}

		return output;
	}
}
