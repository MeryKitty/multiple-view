package basic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import java.util.Stack;

/**
 * The class contains functions to operate the algorithm
 * 
 * @author MeryKitty
 */
public class Testing {
	public static ArrayList<CoverInformation> getCoverInfoSet(Point p, Collection<Sensor> baseSensorSet, boolean resultSort) {
		ArrayList<CoverInformation> coverSensorSet = new ArrayList<>();
		for (Sensor s : baseSensorSet) {
			CoverInformation tempInfo = s.getCoverInfo(p);
			if (tempInfo != null) {
				coverSensorSet.add(tempInfo);
			}
		}
		if (resultSort) {
			Collections.sort(coverSensorSet, CoverInformation.directionComparator);
		}
		return coverSensorSet;
	}
	
	/**
	 * Obtains the ArrayList of direction from a point to the sensors that cover it in the
	 * {@code baseSensorSet}, sorted in increasing order
	 * 
	 * @param p the considered point
	 * @param baseSensorSet the sensor set contains some sensors that cover p
	 * @return the ArrayList of direction from p to the sensors that cover
	 * 
	 * @author MeryKitty
	 */
	public static ArrayList<CoverInformation> getCoverInfoSet(Point p, Collection<Sensor> baseSensorSet) {
		return getCoverInfoSet(p, baseSensorSet, true);
	}
	
	public static ArrayList<Sensor> getCoverSet(ArrayList<Sensor> baseSensorSet, Point p) {
		ArrayList<Sensor> result = new ArrayList<>();
		if (p.coverSet != null) {
			return p.coverSet;
		} else {
			for (Sensor s : baseSensorSet) {
				if (s.checkCover(p)) {
					result.add(s);
				}
			}
			p.coverSet = result;
			return result;
		}
	}
	
	/**
	 * 
	 * 
	 * @param baseSensorSet
	 * @param pSet
	 * @return
	 */
	public static ArrayList<Sensor> getCoverSet(ArrayList<Sensor> baseSensorSet, Point... pSet) {
		ArrayList<Sensor> result = new ArrayList<>();
		if (pSet.length == 0) {
			throw new IllegalArgumentException();
		}
		Point p1 = pSet[0];
		for (Point p : pSet) {
			if (p1.coverSet != null) {
				p1 = p;
				break;
			}
		}
		ArrayList<Sensor> checkList = getCoverSet(baseSensorSet, p1);
		outerLoop:
			for (Sensor s : checkList) {
				for (Point p : pSet) {
					if (!s.checkCover(p)) {
						continue outerLoop;
					}
				}
				result.add(s);
			}
		return result.size() != 0 ? result : null;
	}
	
	/**
	 * Verify if a specific {@code Point} being k-w covered by a list of {@code Sensor}.
	 * Note that {@code sensorList} should be ordered ascendingly, or the method will
	 * automatically return {@code false}
	 * 
	 * @param p the {@code Point} to be checked
	 * @param sensorList the ascendingly ordered list of {@code Sensor}
	 * @return {@code true} if {@code p} is k-w covered by {@code sensorList} in the exact order,
	 * {@code false} otherwise
	 */
	public static boolean checkKW(Point p, ArrayList<Sensor> sensorList, int k, double omega) {
		if (sensorList.size() != k) {
			return false;
		}
		ArrayList<CoverInformation> tempInfo = getCoverInfoSet(p, sensorList, false);
		if (tempInfo.size() != k) {
			return false;
		}
		for (int i = 0; i < sensorList.size() - 1; i++) {
			if (tempInfo.get(i + 1).directedAngle() - tempInfo.get(i).directedAngle() <= omega) {
				return false;
			}
		}
		if (tempInfo.get(0).directedAngle() - tempInfo.get(k - 1).directedAngle() <= omega - 360) {
			return false;
		}
		return true;
	}
	
	public static ArrayList<ArrayList<CoverInformation>> getFullViewSet(ArrayList<CoverInformation> coverSensorSet) {
		if (!coverSensorSet.isEmpty()) {
			ArrayList<Double> angleList = new ArrayList<>(coverSensorSet.size() + 1);
			for (CoverInformation c : coverSensorSet) {
				angleList.add(c.directedAngle());
			}
			int elementNumber = coverSensorSet.size();
			for (int i = 0; i < elementNumber - 1; i++) {
				if (angleList.get(i + 1) - angleList.get(i) >= Config.THETA) {
					return null;
				}
			}
			if (angleList.get(0) - angleList.get(elementNumber - 1) >= Config.THETA - 360) {
				return null;
			}
			// Tuning the full view sensor list
			ArrayList<ArrayList<CoverInformation>> result = new ArrayList<>();
			ArrayList<CoverInformation> tempResult = new ArrayList<>();
			int beginIndex = -1, i = 0, n = 0, finalN = Integer.MAX_VALUE, lastCheckIndex = 0;
			double begin = 0, current = 0;
			current = angleList.get(angleList.size() - 1);
			while (true) {
				if (i == angleList.size() - 1 || angleList.get(i + 1) - current >= Config.THETA - 360) {
					lastCheckIndex = i;
					break;
				}
				i++;
			}
			while (true) {
				if (n == 0) {
					if (++beginIndex > lastCheckIndex) {
						break;
					}
					begin = angleList.get(beginIndex);
					current = begin;
					tempResult.clear();
					tempResult.add(coverSensorSet.get(beginIndex));
					i = beginIndex + 1;
					n++;
				}
				while (true) {
					if (i == angleList.size() - 1 || angleList.get(i + 1) - current >= Config.THETA) {
						current = angleList.get(i);
						tempResult.add(coverSensorSet.get(i));
						i++;
						n++;
						if (current > 360 + begin - Config.THETA) {
							if (n < finalN) {
								finalN = n;
							}
							result.add(new ArrayList<>(tempResult));
							n = 0;
							break;
						}
					} else {
						i++;
					}
				}
			}
			for (int j = result.size() - 1; j > -1; j--) {
				if (result.get(j).size() > finalN) {
					result.remove(j);
				} else if (result.get(j).size() < finalN) {
					throw new AssertionError();
				}
			}
			return result;
		} else {
			return null;
		}
	}
	
	public static ArrayList<ArrayList<CoverInformation>> getKWSet(ArrayList<CoverInformation> coverSensorSet, int k, double omega) {
		if (!coverSensorSet.isEmpty()) {
			Point point = coverSensorSet.get(0).point();
/*			for (CoverInformation c : coverSensorSet) {
				if (!c.point().equals(point)) {
					throw new UnsupportedOperationException();
				}
			} */
			if (point.kwSet != Point.DEFAULT_KW_SET) {
				return point.kwSet;
			}
			ArrayList<ArrayList<CoverInformation>> result = new ArrayList<>();
			ArrayList<Double> angleList = new ArrayList<>(coverSensorSet.size() + 1);
			for (CoverInformation c : coverSensorSet) {
				angleList.add(c.directedAngle());
			}
			Stack<Integer> tempSensorIndex = new Stack<Integer>();
			int i = 0, j = 0, n = 0, sensorNumber = coverSensorSet.size();
			double tempSensor, currentSensor, beginSensor = -1;
			ArrayList<CoverInformation> tempResult = null;
			while (true) {
				if (i >= sensorNumber) {
					if (n > 0) {
						i = tempSensorIndex.pop() + 1;
						if (--n != 0) {
							j = tempSensorIndex.firstElement();
						}
						continue;
					} else {
						point.kwSet = null;
						return null;
					}
				}
				tempSensor = angleList.get(i);
				if (n == 0) {
					if (tempSensor - angleList.get(0) >= 360 - (k - 1) * omega) {
						break;
					}
					beginSensor = tempSensor;
					tempSensorIndex.push(i);
					j = i;
					i++;
					n++;
					continue;
				}
				currentSensor = angleList.get(j);
				if (tempSensor - currentSensor <= omega) {
					i++;
					continue;
				} else if (tempSensor - beginSensor >= 360 - (k - 1 - n) * omega) {
					i = tempSensorIndex.pop() + 1;
					if (--n != 0) {
						j = tempSensorIndex.firstElement();
					}
					continue;
				} else {
					tempSensorIndex.push(i);
					j = i;
					n++;
					i++;
					if (n == k) {
						tempResult = new ArrayList<>(k + 1);
						for (int s : tempSensorIndex) {
							tempResult.add(coverSensorSet.get(s));
						}
						result.add(tempResult);
						i = tempSensorIndex.pop() + 1;
						if (--n != 0) {
							j = tempSensorIndex.firstElement();
						}
						continue;
					} else {
						continue;
					}
				}
			}
			if (result.size() > 0) {
//				ArrayList<CoverInformation.CoverList> finalResult = CoverInformation.CoverList.listCoverList(result);
//				finalResult.sort(CoverInformation.CoverList.coverageComparator);
				point.kwSet = result;
				return result;
			} else {
				point.kwSet = null;
				return null;
			}
		} else {
			return null;
		}
	}
	
	public static CheckInformation checkResult(ArrayList<CoverInformation> sensorList, double diff) {
		for (int i = 0; i < sensorList.size() - 1; i++) {
			if (sensorList.get(i + 1).directedAngle() < sensorList.get(i).directedAngle()) {
				throw new AssertionError();
			}
		}
		double sum = 0;
		for (double i = 0; i < 359.9999; i = i + diff) {
			double tempSum = 0;
			for (CoverInformation c : sensorList) {
				double tempAngle = Point.geometricAngle(c.directedAngle(), i);
				if (tempAngle < 90) {
					double tempExposure = c.coefficient() * Math.cos(Math.toRadians(tempAngle));
					if (tempExposure > tempSum) {
						tempSum = tempExposure;
					}
					if (tempSum > CoverInformation.MAX_EXPOSURE) {
						tempSum = CoverInformation.MAX_EXPOSURE;
						break;
					}
				}
			}
			sum += tempSum;
		}
		CheckInformation result = new CheckInformation(sum * diff / 3.6);
		result.setSensorNummber(sensorList.size());
		return result;
	}
	
	public static CheckInformation checkResult(ArrayList<CoverInformation> sensorList) {
		return checkResult(sensorList, 0.5);
	}
	
	
/*	public static CheckInformation checkResult(ArrayList<CoverInformation> sensorList) {
		double sum = 0;
		for (CoverInformation c : sensorList) {
			sum += c.coefficient();
		}
		CheckInformation result = new CheckInformation(sum * 100);
		result.setSensorNummber(sensorList.size());
		return result;
	} */
} 
	
	
