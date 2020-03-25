package basic;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The class contains the information of every point in the plane of the sensing field.
 * 
 * @author MeryKitty
 */
public class Point {
	/**
	 * The position of a specific point, cannot be changed after instantiation.
	 */
	private final double x, y;
	
	/**
	 * The pool contains any points created, used to reduced duplication of points.
	 */
	private static HashMap<Point, Point> pointPool = new HashMap<Point, Point>(20000);
	protected HashMap<Sensor, CoverInformation> infoPool = new HashMap<>();
	public static final ArrayList<ArrayList<CoverInformation>> DEFAULT_KW_SET = new ArrayList<>();
	protected ArrayList<ArrayList<CoverInformation>> kwSet = DEFAULT_KW_SET;
	protected ArrayList<Sensor> coverSet = null;
	
	/**
	 * Construct a point at a given location on the plane of the sensing field.
	 * 
	 * @param x
	 * @param y
	 */
	protected Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public static synchronized Point getPoint(double x, double y)
	{
		Point p = new Point(x, y);
		if (Point.pointPool.containsKey(p))
		{
			return Point.pointPool.get(p);
		}
		else
		{
			pointPool.put(p, p);
			return p;
		}
	}
	
	/**
	 * Obtain the abscissa of the point
	 *
	 * @return x
	 */
	public double x() {
		return this.x;
	}
	
	/**
	 * Obtain the ordinate of the point
	 * 
	 * @return y
	 */
	public double y() {
		return this.y;
	}
	
	/**
	 * Calculate the distance between 2 points (may or may not distinguished on the plane.
	 * 
	 * @param A the first point
	 * @param B the second point
	 * @return The distance between A and B
	 */
	public static double distance(Point A, Point B) {
		double x = A.x - B.x;
		double y = A.y - B.y;
		return Math.sqrt(x * x + y * y);
	}
	
	/**
	 * Calculate the directed angle of the vector from this point to another point
	 * on the plane compared to the x-axis (the direction of the vector).
	 * 
	 * @param other the end point of the considered vector
	 * @return the angle in degree, take the value from 0 to 360.
	 */
	public double angleToward(Point other) {
		double x = other.x - this.x;
		double y = other.y - this.y;
		if (x == 0) {
			if (y > 0) {
				return 90;
			} else if (y < 0) {
				return 270;
			} else {
				throw new UnsupportedOperationException();
			}
		} else {
			double firstAngle = Math.toDegrees(Math.atan(y / x));
			if (x < 0) {
				return firstAngle + 180;
			} else {
				if (y < 0) {
					return firstAngle + 360;
				} else {
					return firstAngle;
				}
			}
		}
	}
	
	/**
	 * Calculate the geometric angle between two vector on the plane
	 * 
	 * @param angle1 the direction of the first vector
	 * @param angle2 the direction of the second vector
	 * @return the geometric angle between the 2 vectors, take value from 0 to 180
	 */
	public static double geometricAngle(double angle1, double angle2) {
		double tempAngle = angle2 - angle1;
		if (tempAngle > 180) {
			tempAngle = tempAngle - 360;
		} else if (tempAngle < -180) {
			tempAngle = tempAngle + 360;
		}
		return Math.abs(tempAngle);
	}
	
	public static void clearCache() {
		for (Point p : pointPool.values()) {
			p.infoPool.clear();
			p.kwSet = DEFAULT_KW_SET;
			p.coverSet = null;
		}
	}
	
	/**
	 * Read the sensors in a sensing field file and transform to an ArrayList of sensors
	 * contains all the sensors saved in the input file
	 * 
	 * @return an ArrayList of listed sensors
	 * @throws IOException
	 */
	public static ArrayList<Point> readPointInput() throws IOException {
		ArrayList<Point> pointList = new ArrayList<Point>(1500);
		File inputFile = new File("data/input/_Points.txt");
		BufferedReader inputReader = new BufferedReader(new FileReader(inputFile));
		String inputLine;
		String[] tokens;
		double x, y;
		while (true) {
			inputLine = inputReader.readLine();
			if (inputLine == null) {
				break;
			}
			tokens = inputLine.split(" ");
			x = Double.parseDouble(tokens[0]);
			y = Double.parseDouble(tokens[1]);
			pointList.add(getPoint(x, y));
		}
		inputReader.close();
		return pointList;
	}
	
	@Override
	public int hashCode() {
		return (int)this.x * 1000 + (int)this.y;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Point) {
			Point p = (Point) o;
			return ((int)p.x == (int)this.x && (int)p.y == (int)this.y);
		} else {
			return false;
		}
	}
}
