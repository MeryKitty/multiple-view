package basic;

import java.io.*;
import java.util.ArrayList;

/**
 * The class contains the information about a sensor in the sensing field, extends the
 * Point class.
 * 
 * @author MeryKitty
 */
public class Sensor extends Point{
	/**
	 * The information of the sensor
	 */
	private final double direction, radius, alpha;
	
	public Sensor(double x, double y, double direction) {
		super(x, y);
		this.direction = direction;
		this.radius = Config.RADIUS;
		this.alpha = Config.ALPHA;
	}
	
	public double direction() {
		return this.direction;
	}
	
	public double alpha() {
		return this.alpha;
	}
	
	public double radius() {
		return this.radius;
	}
	
	/**
	 * Verify if a point is in the coverage of the current sensor.
	 * 
	 * @param A the point needs verification
	 * @return if A is being covered by this.
	 */
	public CoverInformation getCoverInfo(Point A) {
		CoverInformation result = CoverInformation.getCoverInfo(this, A);
		return result;
	}
	
	public boolean checkCover(Point A) {
		if (Point.distance(this, A) > this.radius) {
			return false;
		} else {
			double tempAngle = this.angleToward(A);
			if (Point.geometricAngle(this.direction, tempAngle) > this.alpha) {
				return false;
			} else {
				return true;
			}
		}
	}
	
	/**
	 * 
	 * 
	 * @param sensorIndex
	 * @param time
	 * @return
	 * @throws IOException
	 */
	public static ArrayList<Sensor> readSensorInput(int sensorIndex, int time) throws IOException {
		ArrayList<Sensor> sensorList = new ArrayList<Sensor>();
		File inputFile = new File("data/input/" + (sensorIndex * 50) + "_" + time + ".txt");
		BufferedReader inputReader = new BufferedReader(new FileReader(inputFile));
		String inputLine;
		String[] tokens;
		double x, y, direction;
		while (true) {
			inputLine = inputReader.readLine();
			if (inputLine == null) {
				break;
			}
			tokens = inputLine.split(" ");
			x = Double.parseDouble(tokens[0]);
			y = Double.parseDouble(tokens[1]);
			direction = Double.parseDouble(tokens[2]);
			sensorList.add(new Sensor(x, y, direction));
		}
		inputReader.close();
		return sensorList;
	}
}
