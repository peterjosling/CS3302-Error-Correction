package uk.ac.standrews.s120001757.errorcorrection;


public class Benchmark {
	private static final int BENCHMARK_DATA_SIZE = 8 * 1000 * 1000; // 1MB
	private static final int CROSSOVER_DATA_SIZE = 50 * 1000 * 1000; // 50Mb

	public static void benchmarkCorruption() {
		double[] pVals = new double[]{0.01, 0.02, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0.09, 0.1, 0.11, 0.12, 0.13, 0.14, 0.15, 0.16, 0.17, 0.18, 0.19, 0.20};

		System.out.print("r");

		for (double p : pVals) {
			System.out.print("," + p);
		}

		for (int r = 2; r < HammingCode.R_BOUND; r++) {
			System.out.println();
			System.out.print(r);

			for (double p : pVals) {
				double c = CLI.encodeAndTransmit(r, p, BENCHMARK_DATA_SIZE, true);
				System.out.print("," + String.format("%.8f", c));
			}
		}
	}

	public static void findCrossover() {
		for (int r = 2; r <= HammingCode.R_BOUND; r++) {
			double error = 0.25;
			double corruption = 0;

			while (Math.abs(error - corruption) > 0.00001) {
				error += (error - corruption) / 2;

				corruption = CLI.encodeAndTransmit(r, error, CROSSOVER_DATA_SIZE, true);
			}

			System.out.println("r=" + r + ", p=" + error);
		}
	}

	public static void benchmarkPredictions() {
		double[] pVals = new double[]{0.001, 0.005, 0.01, 0.02, 0.03, 0.04, 0.05, 0.1, 0.2, 0.5};

		for (int r = 2; r <= HammingCode.R_BOUND; r++) {
			System.out.println("r=" + r);
			System.out.println("p,predicted,actual");

			for (double p : pVals) {
				System.out.print(p + ",");

				double prediction = HammingCode.getCorruptionRate(r, p);
				double actual = CLI.encodeAndTransmit(r, p, BENCHMARK_DATA_SIZE, true);

				System.out.print(String.format("%.8f", prediction) + ",");
				System.out.println(String.format("%.8f", actual));
			}
		}
	}
}
