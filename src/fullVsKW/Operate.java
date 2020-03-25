package fullVsKW;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import basic.*;

public class Operate {	
/*	public static void main(String[] args) throws IOException {
		File outputFile;
		PrintStream printOutput = null;
		ArrayList<Point> pointList = Point.readPointInput();
		Files.createDirectories(Paths.get("data/output/" + Config.THETA + "v" + Config.K + "_" + Config.OMEGA));
		for (int sensorIndex = 5; sensorIndex < 20; sensorIndex++) {
			// The total of the field average difference, will be divided by instance to get the overall average difference
			double sumKW = 0, sumFull = 0;
			// The total number of sensors needed to obtain the coverage in each conditions
			double kwSensorNumber = 0, fullViewSensorNumber = 0;
			// Number of fields considered that has points that both full view and KW covered
			int instance = 0;
			// The total of probability of point being covered by conditions
			int totalFull = 0, totalKW = 0;
			outputFile = new File("./data/output/" + Config.THETA + "v" + Config.K + "_" + Config.OMEGA + "/" + (sensorIndex * Config.SENSOR_NUMBER) + ".txt");
			printOutput = new PrintStream(outputFile);
			StringBuilder outputString = new StringBuilder();
			// Check the process for every considered sensing field, keep updating sum, kwSensorNumber,
			// fullViewSensorNumber, instance
			for (int fieldIndex = 0; fieldIndex < 100; fieldIndex++) {
				ArrayList<Sensor> sensorList = Sensor.readSensorInput(sensorIndex, fieldIndex);
				// The current total difference between the coverage quality of the 2 conditions, will be
				// divided by both to get the average difference
				double tempSumKW = 0, tempSumFull = 0;
				// The total number of sensors needed to obtain the coverage in each conditions
				int tempKWSensorNumber = 0, tempFullViewSensorNumber = 0;
				// The number of points that is kw covered, full view covered and covered by both conditions
				int kw = 0, full = 0, both = 0;
				// Check the process for every considered point, keep updating tempSum, tempKWSensorNumber,
				// tempFullViewSensorNumber, kw, full, both
				for (Point tempPoint : pointList) {
					ArrayList<CoverInformation> tempCoverList = Testing.getCoverSet(tempPoint, sensorList);
					ArrayList<CoverInformation> tempKW = Testing.getKWSet(tempCoverList);
					ArrayList<CoverInformation> tempFull = Testing.getFullViewSet(tempCoverList);
					if (tempKW != null && tempFull != null) {
						kw++; full++; both++;
						CheckInformation fullInfo = Testing.checkResult(tempFull);
						CheckInformation kwInfo = Testing.checkResult(tempKW);
						tempKWSensorNumber += kwInfo.sensorNumber();
						tempFullViewSensorNumber += fullInfo.sensorNumber();
						tempSumFull += fullInfo.coverageValue();
						tempSumKW += kwInfo.coverageValue();
					} else if (tempKW != null) {
						CheckInformation kwInfo = Testing.checkResult(tempKW);
						tempKWSensorNumber += kwInfo.sensorNumber();
						tempSumKW += kwInfo.coverageValue();
						kw++;
					} else if (tempFull != null) {
						CheckInformation fullInfo = Testing.checkResult(tempFull);
						tempFullViewSensorNumber += fullInfo.sensorNumber();
						tempSumFull += fullInfo.coverageValue();
						full++;
					}
				}
				if (both != 0) {
					instance++;
					double currentAverageKW = tempSumKW / kw;
					sumKW += currentAverageKW;
					double currentAverageFull = tempSumFull / full;
					sumFull += currentAverageFull;
					double currentFullSensors = (double) tempFullViewSensorNumber / full;
					fullViewSensorNumber += currentFullSensors;
					double currentKWSensors = (double) tempKWSensorNumber / kw;
					kwSensorNumber += currentKWSensors;
					outputString.append("\n\nField Number: " + fieldIndex + "\nFull View Covered Points: " + full + "\nAverage Needed Sensors: " + currentFullSensors + "\nAverage Coverage Quality: " + currentAverageFull + "\nKW Covered Points: " + kw + "\nAverage Needed Sensors: " + currentKWSensors + "\nAverage Coverage Quality: " + currentAverageKW + "\nCovered Both: " + both);
				} else {
					outputString.append("\n\nField Number: " + fieldIndex + "\nFull View Covered Points: " + full + "\nKW Covered Points: " + kw + "\nCovered Both: " + both + "\nNo Point Covered By Both Conditions");
				}
				totalKW += kw;
				totalFull += full;
			}
			printOutput.print("FULL VIEW VS KW REPORT:\n\nTheta = " + Config.THETA + "\nK = " + Config.K + "\nOmega = " + Config.OMEGA + "\n\nSensor Number: " + (sensorIndex * Config.SENSOR_NUMBER));
			printOutput.print("\n\nAverage Full View Quality: " + (sumFull / instance) + "\nAverage KW Quality: " + (sumKW / instance) + "\nAverage Sensors In Full View: " + (fullViewSensorNumber / instance) + "\nAverage Sensors In KW: " + (kwSensorNumber / instance) + "\nAverage Probability Of Full View: " + (totalFull / 100) + "\nAverage Probability Of KW: " + (totalKW / 100));
			printOutput.print(outputString.toString());
		}
		printOutput.close();
	}*/
	
	public static void main(String[] args) throws IOException {
		ArrayList<Point> pointList = Point.readPointInput();
		Files.createDirectories(Paths.get("data/output/" + Config.THETA + "v" + Config.K + "_" + Config.OMEGA));
		for (int sensorIndex = 5; sensorIndex < 20; sensorIndex++) {
			// OperatingThread tempThread = new OperatingThread(sensorIndex, pointList);
			// tempThread.start();
		}
	}

}

