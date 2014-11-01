package uk.ac.standrews.s120001757.errorcorrection;

public class Main {
	public static void main(String[] args) {

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





		// Is this correct? Spec says proportion of DATA bits. Am I doing this, or looking at all bits?





		System.out.println("Optimum r: " + r);
		System.out.println("Information rate: " + HammingCode.getInformationRate(r));
		System.out.println("Predicted corruption: " + HammingCode.getCorruptionRate(r, errorProb));
	}

	public static void encodeAndTransmit() {
		double errorProb = 0.001;
		int r = 3;
		int length = 100;


	}
}
