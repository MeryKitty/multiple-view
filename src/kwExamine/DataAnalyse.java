package kwExamine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

public class DataAnalyse {
	private static final int K = 4;
	private static final double OMEGA = 75;
	private static final String[] ALGORITHM = {"Differentiation", "All", "Max"};

	public static void main(String[] args) throws IOException {
		StringBuilder outputBuilder = new StringBuilder();
		for (int sensorNumber = 600; sensorNumber < 901; sensorNumber += 50) {
			double[] expectation = new double[3];
			double[] variance = new double[3];
			double[] standardDeviation = new double[3];
			double[] relativeStandardDeviation = new double[3];
			for (int algorithm = 0; algorithm < 3; algorithm++) {
				BufferedReader input = new BufferedReader(new FileReader("./data/output/kwExamine/Output/" + ALGORITHM[algorithm] + "/" + K + "_" + OMEGA + "/" + sensorNumber + ".txt"));
				ArrayList<Double> data = new ArrayList<>();
				while (true) {
					String temp = input.readLine();
					if (temp == null) {
						break;
					}
					data.add(Double.parseDouble(temp));
				}
				input.close();
				double sum = 0;
				for (double d : data) {
					sum += d;
				}
				expectation[algorithm] = sum / data.size();
				sum = 0;
				for (double d : data) {
					sum += (d - expectation[algorithm]) * (d - expectation[algorithm]);
				}
				variance[algorithm] = sum / data.size();
				standardDeviation[algorithm] = Math.sqrt(variance[algorithm]);
				relativeStandardDeviation[algorithm] = standardDeviation[algorithm] / expectation[algorithm];
			}
			outputBuilder.append("Sensor Number: " + sensorNumber + "\n");
			outputBuilder.append("                           Dif            All            Max\n");
			outputBuilder.append("Expect             :" + String.format("%15.2f", expectation[0]) + String.format("%15.2f", expectation[1]) + String.format("%15.2f", expectation[2])).append("\n");
			outputBuilder.append("Deviation          :" + String.format("%15.2f", standardDeviation[0]) + String.format("%15.2f", standardDeviation[1]) + String.format("%15.2f", standardDeviation[2])).append("\n");
			outputBuilder.append("Relative           :" + String.format("%15.2f", relativeStandardDeviation[0]) + String.format("%15.2f", relativeStandardDeviation[1]) + String.format("%15.2f", relativeStandardDeviation[2])).append("\n");
			outputBuilder.append("\n");
		}
		PrintStream output = new PrintStream("./data/compare/" + K + "_" + OMEGA + ".txt");
		output.append(outputBuilder);
		output.close();
	}

}
