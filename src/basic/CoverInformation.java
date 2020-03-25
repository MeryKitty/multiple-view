package basic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

/**
 * The class used to contain the information of a coverage between a sensor and a point
 * 
 * @author MeryKitty
 */
public class CoverInformation {	
/*	public static class CoverList extends ArrayList<CoverInformation> {
		private static final long serialVersionUID = -7790717900325268377L;
		private final CheckInformation checkInfo;
		
		public static Comparator<CoverList> coverageComparator = new Comparator<>() {
			@Override
			public int compare(CoverList c1, CoverList c2) {
				if (c1.checkInfo.coverageValue() > c2.checkInfo.coverageValue()) {
					return -1;
				} else if (c1.checkInfo.coverageValue() < c1.checkInfo.coverageValue()) {
					return 1;
				} else {
					return -1;
				}
			}
		};
		
		public CoverList(Collection<CoverInformation> source) {
			super(source);
			this.checkInfo = Testing.checkResult(this);
		}
		
		public CheckInformation checkInfo() {
			return this.checkInfo;
		}
		
		public static ArrayList<CoverList> listCoverList(ArrayList<ArrayList<CoverInformation>> c) {
			ArrayList<CoverList> result = new ArrayList<>(c.size());
			for (ArrayList<CoverInformation> a : c) {
				result.add(new CoverInformation.CoverList(a));
			}
			return result;
		}
	} */
	
	private static final double COEFFICIENT = 10000;
	public static final double MAX_EXPOSURE = COEFFICIENT / Math.pow(Config.MIN_RADIUS, Config.LAMBDA);
	
	private final double directedAngle, distance, exposureCoefficient;
	private final Point point;
	private final Sensor sensor;
	
	/**
	 * The comparator to sort the sensor in the ascending order of {@code directedAngle}
	 */
	public static final Comparator<CoverInformation> directionComparator = new Comparator<>() {
		@Override
		public int compare(CoverInformation info1, CoverInformation info2) {
			if (info1.directedAngle < info2.directedAngle) {
				return -1;
			} else if (info1.directedAngle > info2.directedAngle) {
				return 1;
			} else {
				return 0;
			}
		}
	};
	
	/**
	 * Construct an object contain the coverage information of a sensor toward a point
	 * 
	 * @param directedAngle the angle from the point toward the sensor
	 * @param distance the distance between the point and the sensor
	 */
	private CoverInformation(Sensor sensor, Point point) {
		this.sensor = sensor;
		this.point = point;
		this.distance = Point.distance(this.sensor, this.point);
		this.directedAngle = point.angleToward(this.sensor);
		this.exposureCoefficient = 10000 / Math.pow(this.distance, Config.LAMBDA);
	}
	
	public static CoverInformation getCoverInfo(Sensor sensor, Point point) {
		if (Point.distance(sensor, point) > sensor.radius()) {
			return null;
		} else {
			double tempAngle = sensor.angleToward(point);
			if (Point.geometricAngle(sensor.direction(), tempAngle) > sensor.alpha()) {
				return null;
			} else {
				synchronized (point) {
					if (point.infoPool.containsKey(sensor)) {
						return point.infoPool.get(sensor);
					} else {
						CoverInformation result = new CoverInformation(sensor, point);
						point.infoPool.put(sensor, result);
						return result;
					}
				}
			}
		}
	}
	
	/**
	 * The angle of the point toward the sensor
	 * @return
	 */
	public double directedAngle() {
		return this.directedAngle;
	}
	
	public double distance() {
		return this.distance;
	}
	
	/**
	 * Obtain exposure coefficient that is the part of the formula not containing the
	 * trigonometric function to reduce the computation time
	 * 
	 * @return exposureCoefficient
	 */
	public double coefficient() {
		return this.exposureCoefficient;
	}
	
	public Point point() {
		return this.point;
	}
	
	public static ArrayList<Sensor> getSensorList(Collection<CoverInformation> infoList) {
		ArrayList<Sensor> result = new ArrayList<>((int)(infoList.size() * 1.2));
		for (CoverInformation c : infoList) {
			result.add(c.sensor);
		}
		return result;
	}
}
