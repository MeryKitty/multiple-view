package kwExamine;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

import basic.*;

public class Operate {
	public static long timer, timer2;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		Files.createDirectories(Paths.get("./data/output/"));
		File kReport = new File("./data/output/kReport.txt");
		PrintStream kReportStream = new PrintStream(kReport);
		kReportStream.append("K Value Examination:\n");
		for (int k : Config.K) {
			kReportStream.append("\nK = " + k);
			double maxCoverage = 0;
			for (double omega : Config.OMEGA.get(k)) {
				for (int sensorIndex = 6; sensorIndex < 16; sensorIndex++)
				{
					Files.createDirectories(Paths.get("./data/output/" + k + "/" + omega + "/report"));
					File report = new File("./data/output/" + k + "/" + omega + "/report/" + (sensorIndex * Config.SENSOR_NUMBER) + ".txt");
					PrintStream reportStream = new PrintStream(report);
					reportStream.append("COVERAGE EXAMINATION:\n");
					reportStream.append("\nK = " + k + "\nOmega = " + omega + "\nNumber Of Sensors: " + (Config.SENSOR_NUMBER * sensorIndex));
					int counter = 0;
					double abSum = 0, reSum = 0;
					double totalCoverage = 0;
					long timeCounter = 0;
					StringBuilder output = new StringBuilder();
					for (int time = 0; time < Config.INSTANCE; time++)
					{
						System.out.println("\nSensor Number: " + (sensorIndex * Config.SENSOR_NUMBER) + "\nField Number: " + time);
						timer = 0; timer2 = 0;
					//	BufferedImage outPic = new BufferedImage((int)(Config.WIDTH + Config.RADIUS * 2), (int)(Config.HEIGHT + Config.RADIUS * 2), BufferedImage.TYPE_INT_ARGB);
					//	BufferedImage outPath = new BufferedImage((int)(Config.WIDTH + Config.RADIUS * 2), (int)(Config.HEIGHT + Config.RADIUS * 2), BufferedImage.TYPE_INT_ARGB);
					//	Graphics2D g2_Pic = outPic.createGraphics();
					//	Graphics2D g2_Path = outPath.createGraphics();
						Node.coveredNodes.clear();
						ArrayList<Sensor> setOfSensors = Sensor.readSensorInput(sensorIndex, time);	
						Point.clearCache();
					//	DrawOutput.drawSetOfSensors(setOfSensors, g2_Pic);
						
						//set features for g2_Pic
					//	g2_Pic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					//	g2_Pic.setColor(Color.BLACK); 
						
						
						
						//phase 1: split and check
						long start = System.currentTimeMillis();
				//		---------------------------------------------------------------------------------
						TestingKW.splitAndCheck(setOfSensors, k, omega);
					//	DrawOutput.drawCoveredNodes(Node.coveredNodes, g2_Pic);
						
						//write to file
					//	Path outDirectories = Paths.get("./data/output/KWExamine/" + k + "/" + omega + "/" + (sensorIndex * Config.SENSOR_NUMBER));
					//	Files.createDirectories(Paths.get(outDirectories.toString() + "/cover"));
					//	File outPicFile = new File(outDirectories.toString() + "/cover/" + time + ".png");
					//	try {
					//		ImageIO.write(outPic, "png", outPicFile);
					//	}
					//	catch(IOException e) {
					//		e.printStackTrace();
					//	}
						
						//print run time
				//		long middle = System.currentTimeMillis();
				//		System.out.println("Runtime phase 1 : " + (middle - start));
				//		---------------------------------------------------------------------------------
						
						
						
				//		Estimating errors
		/*				double tempAbSum = 0, tempReSum = 0;
						for (int i = 0; i < 1000; i++) {
							Node tempNode = Node.coveredNodes.get((int)(Config.RANDOM_GENERATE.nextDouble() * Node.coveredNodes.size()));
							Point[] tempCheck = TestingKW.nodeRandomPoint(tempNode, 6);
							double coverage1 = Testing.checkResult(Testing.getCoverInfoSet(tempCheck[0], tempNode.coverSensors())).coverageValue();
							double coverage2 = Testing.checkResult(Testing.getCoverInfoSet(tempCheck[1], tempNode.coverSensors())).coverageValue();
							tempAbSum += Math.abs(coverage1 - coverage2);
							tempReSum += Math.abs((coverage1 - coverage2) / coverage1);
						}
						output.append("\n\nField Number: " + time + "\nAverage Absolute Difference: " + (tempAbSum / 1000) + "\nAverage Relative Difference: " + (tempReSum / 1000));
						abSum += tempAbSum / 1000;
						reSum += tempReSum / 1000; */
						
						
						
						//phase 2: build graph and find path
				//		---------------------------------------------------------------------------------
						Node source = new Node();
						Node end = new Node();
						//build graph
						Tools_Phase2.buildGraph(Node.coveredNodes, source, end);
						
						//find a path from s to t
						Tools_Phase2.findPath(Node.coveredNodes, source, end);
						
						//temp is housekeeper variable
						if (end.getTrace() == null)
						{
							long finish = System.currentTimeMillis();
							System.out.println("Barrier not found");
							System.out.println("Runtime: " + (finish - start));
							timeCounter += finish - start;
						}
						else
						{
							counter++;
							//path is to store the path from left side to right side
							ArrayList<Node> path = new ArrayList<Node>();
							System.out.println("Barrier found");
							Node temp = end;
							while (temp.getTrace() != null) {
								temp = temp.getTrace();
								path.add(temp);
							}
							path.remove(path.size() - 1); // Remove the Node source;
						
							//set features for g2_Path
						//	g2_Path.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
						//	g2_Path.setColor(Color.BLACK); 
							
							//use g2_Path to draw
						//	DrawOutput.drawCoveredNodes(path, g2_Path);
						//	g2_Path.draw(new Rectangle2D.Double(Config.RADIUS, Config.RADIUS, Config.WIDTH, Config.HEIGHT));
							
							//write to file
						//	Files.createDirectories(Paths.get(outDirectories.toString() + "/path"));
						//	File outPathFile = new File(outDirectories.toString() + "/path/" + time + ".png");
						//	try {
						//		ImageIO.write(outPath, "png", outPathFile);
						//	} catch(IOException e) {
						//		e.printStackTrace();
						//	}
							
						//	long finish = System.currentTimeMillis();
						//	System.out.println("Runtime phase 2 : " + (finish - middle));
							
							
							
							//calculate average coverage
							double coverageSum = 0;
							int weightCounter = 0;
							for (Node n : path) {
								weightCounter += ToolPhase3.nodeWeight(n);
								coverageSum += ToolPhase3.nodeCoverage(n) * ToolPhase3.nodeWeight(n);
							}
							totalCoverage += coverageSum / weightCounter;
							output.append("\n\nField Number: " + time + "\nAverage Coverage: " + (coverageSum / weightCounter));
							long finish = System.currentTimeMillis();
							System.out.println("Runtime: " + (finish - start));
							timeCounter += finish - start;
						}
						
						//run time
					//	System.out.println("Runtime Something 1: " + timer);
					//	System.out.println("Runtime Something 2: " + timer2);
						
					}
					if (counter > 0) {
						double averageCoverage = totalCoverage / counter;
						if (maxCoverage < averageCoverage && counter > 20) {
							maxCoverage = averageCoverage;
						}
						reportStream.append("\n\nNumber Of Barriers: " + counter + "\nAverage Coverage On Barrier: " + averageCoverage + "\nAverage Computation Time: " + (timeCounter / Config.INSTANCE));
						reportStream.append(output.toString());
					//	reportStream.append("\n\nOverall Average Absolute Difference: " + (abSum / 100) + "\nOverall Average Relative Difference: " + (reSum / 100));
					} else {
						reportStream.append("\n\nNo Barrier Found!\nAverage Computation Time: " + (timeCounter / Config.INSTANCE));
					}
					reportStream.close();
				}
			}
			kReportStream.append("\nMax Coverage Reached: " + maxCoverage + "\n");
		}
		kReportStream.close();
//      ---------------------------------------------------------------------------------
	}

}
