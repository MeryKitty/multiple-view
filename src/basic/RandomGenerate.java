package basic;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

@SuppressWarnings("unused")
public class RandomGenerate {

	private static void sensorGenerate() throws FileNotFoundException {
		int sensorNumber;
		double xPosition, yPosition, direction;
		File inputFile;
		PrintStream inputPrint = null;
		StringBuilder inputString;
		for (int sensIndex = 5; sensIndex < 20; sensIndex++) {
			sensorNumber = Config.SENSOR_NUMBER * sensIndex;
			for (int time = 0; time < Config.INSTANCE; time++) {
				inputFile = new File("./data/input/" + sensorNumber + "_" + time + ".txt");
				inputPrint = new PrintStream(inputFile);
				inputString = new StringBuilder();
				for (int i = 0; i < sensorNumber; i++) {
					xPosition = Config.RANDOM_GENERATE.nextDouble() * (Config.WIDTH + 2 * Config.RADIUS) - Config.RADIUS;
					yPosition = Config.RANDOM_GENERATE.nextDouble() * (Config.HEIGHT + 2 * Config.RADIUS) - Config.RADIUS;
					direction = Config.RANDOM_GENERATE.nextDouble() * 360;
					inputString.append(xPosition + " " + yPosition + " " + direction);
					if (i < sensorNumber - 1) {
						inputString.append("\n");
					}
				}
				inputPrint.print(inputString.toString());
			}
		}
		inputPrint.close();
	}
	
	private static void pointGenerate() throws FileNotFoundException {
		File inputFile = new File("./data/input/_Points.txt");
		PrintStream inputPrint = new PrintStream(inputFile);
		double x, y;
		StringBuilder inputString = new StringBuilder();
		for (int i = 0; i < 1000; i++) {
			x = Config.RANDOM_GENERATE.nextDouble() * Config.WIDTH;
			y = Config.RANDOM_GENERATE.nextDouble() * Config.HEIGHT;
			inputString.append(x + " " + y);
			if (i != 999) {
				inputString.append("\n");
			}
		}
		inputPrint.print(inputString.toString());
		inputPrint.close();
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		sensorGenerate();
	}

}
