package uk.ac.standrews.s120001757.errorcorrection;

import org.apache.commons.cli.*;

import org.apache.commons.cli.ParseException;
import java.util.Arrays;
import java.util.Random;

public class CLI {
	private Options options = new Options();

	public CLI() {
		options.addOption("h", "help", false, "Show this help information.");
		options.addOption("optimise", false, "Run the program in optimise mode to find the optimum Hamming code of length r for a given error probability and maximum corruption.");
		options.addOption("encode", false, "Run the program in encode mode to encode and transmit some random data of a specified length for a given error probability and r value. The proportion of data bits corrupted will be printed.");
		options.addOption("p", true, "The probability of an error occurring in the transmission channel, between 0 and 1. Required.");
		options.addOption("maxCorruption", true, "The maximum proportion of data bits which may become corrupted, between 0 and 1. Required for optimise.");
		options.addOption("r", true, "The value of r for the Hamming code, minimum 2. Required for encode.");
		options.addOption("length", true, "The length of random data to generate and encode. Required for encode.");
	}

	public void parse(String[] args) {
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);
		} catch(ParseException e) {
			System.err.println("Failed to parse input arguments.");
			System.exit(1);
		}

		if (cmd.hasOption("h")) {
			help();
			return;
		}

		if (cmd.hasOption("optimise")) {
			if (!cmd.hasOption("p")) {
				System.err.println("Missing required parameter p.");
				System.exit(1);
			}

			if (!cmd.hasOption("maxCorruption")) {
				System.err.println("Missing required parameter maxCorruption.");
				System.exit(1);
			}

			double p = 0;
			double maxCorruption = 0;

			try {
				p = Double.parseDouble(cmd.getOptionValue("p"));
				maxCorruption = Double.parseDouble(cmd.getOptionValue("maxCorruption"));

				if (p < 0 || p > 1 || maxCorruption < 0 || maxCorruption > 1) {
					throw new NumberFormatException();
				}
			} catch(NumberFormatException e) {
				System.err.println("Invalid arguments. Please specify floating point numbers between 0 and 1 for p and maxCorruption.");
				System.exit(1);
			}

			getOptimumR(p, maxCorruption);
			return;
		}

		if (cmd.hasOption("encode")) {
			if (!cmd.hasOption("p")) {
				System.err.println("Missing required parameter p.");
				System.exit(1);
			}

			if (!cmd.hasOption("r")) {
				System.err.println("Missing required parameter r.");
				System.exit(1);
			}

			if (!cmd.hasOption("length")) {
				System.err.println("Missing required parameter length.");
				System.exit(1);
			}

			double p = 0;
			int r = 0;
			int length = 0;

			try {
				p = Double.parseDouble(cmd.getOptionValue("p"));
				r = Integer.parseInt(cmd.getOptionValue("r"));
				length = Integer.parseInt(cmd.getOptionValue("length"));

				if (p < 0 || p > 1 || r < 2 || length < 1) {
					throw new NumberFormatException();
				}
			} catch(NumberFormatException e) {
				System.err.println("Invalid arguments. Please specify a floating point number between 0 and 1 for p, an integer value of at least 2 for r and a positive integer value for length.");
				System.exit(1);
			}

			encodeAndTransmit(r, p, length);
			return;
		}

		// No valid arguments - show help.
		help();
	}

	public void help() {
		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp("Main", options);
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

		// Ensure length is a multiple of data length so we can divide it up.
		int remainder = length % (int)(Math.pow(2, r) - 1 - r);

		if (remainder != 0) {
			length -= remainder;
			System.out.println("Given length not a multiple of the word length - trimming down to " + length);
		}

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
