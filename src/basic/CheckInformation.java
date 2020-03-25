package basic;

import java.util.Comparator;

/**
 * The class used to contains information about the check operation to
 * keep track of the performance of each condition in various aspects.
 * May be extended further in the future.
 * 
 * @author MeryKitty
 */
public class CheckInformation {
	/**
	 * The coverage quality of a sensor group to an intruder, which is modeled as a standing
	 * cylinder with a very small radius, obtains by calculate the sensing quality of the group
	 * to every point on the surface of the intruder
	 */
	private double coverageValue;
	
	/**
	 * The sensor number considered in the sensor group
	 */
	private int sensorNumber = 0;
	
	public static Comparator<CheckInformation> coverageComparator = new Comparator<>() {
		@Override
		public int compare(CheckInformation c1, CheckInformation c2) {
			if (c1.coverageValue < c2.coverageValue) {
				return -1;
			} else if (c1.coverageValue > c2.coverageValue) {
				return 1;
			} else {
				return 0;
			}
		}
	};
	
	public static Comparator<CheckInformation> senNumComparator = new Comparator<>() {
		@Override
		public int compare(CheckInformation c1, CheckInformation c2) {
			if (c1.sensorNumber < c2.sensorNumber) {
				return -1;
			} else if (c1.sensorNumber > c2.sensorNumber) {
				return 1;
			} else {
				return 0;
			}
		}
	};
	
	public CheckInformation(double coverageValue) {
		this.coverageValue = coverageValue;
	}
	
	public void setSensorNummber(int number) {
		if (this.sensorNumber == 0) {
			this.sensorNumber = number;
		} else {
			throw new UnsupportedOperationException();
		}
	}
	
	public double coverageValue() {
		return this.coverageValue;
	}
	
	public int sensorNumber() {
		return this.sensorNumber;
	}
}
