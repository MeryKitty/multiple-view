package basic;

import java.util.HashMap;
import java.util.Random;

public class Config {
	/**
	 * The width of the sensing field
	 */
	public static final double WIDTH = 2000;
	
	/**
	 * The height of the sensing field
	 */
	public static final double HEIGHT = 500;
	
	/**
	 * The number of sensor in a field with {@code sensorIndex = 1}
	 */
	public static final int SENSOR_NUMBER = 50;
	
	/**
	 * The number of instances for each set of parameters
	 */
	public static final int INSTANCE = 100;
	
	/**
	 * The sensing radius of each sensor
	 */
	public static final double RADIUS = 300;
	
	/**
	 * The distance at which the exposure is maximised
	 */
	public static final double MIN_RADIUS = 50;
	
	/**
	 * The maximum sensing angle of each sensor
	 */
	public static final double ALPHA = 45;
	
	/**
	 * 
	 */
	public static final double LAMBDA = 2;
	
	/**
	 * Information theta of the full view model
	 */
	public static final double THETA = 100;
	
	/**
	 * Information k of the KW model
	 */
	public static final int K[] = {3};
	
	/**
	 * Information omega of the KW model
	 */
	public static final HashMap<Integer, double[]> OMEGA = new HashMap<>();
	static {
		OMEGA.put(3, new double[] {115., 110., 105., 100., 95., 90.});
		OMEGA.put(4, new double[] {80., 75., 70., 65., 60., 55.});
		OMEGA.put(5, new double[] {60., 55., 50., 45., 40.});
		OMEGA.put(6, new double[] {50., 45., 40., 35.});
		OMEGA.put(7, new double[] {45., 40., 35., 30.});
	}
	
	public static final int MAX_RANK = 6;
	
	public static final Random RANDOM_GENERATE = new Random();
	
}
