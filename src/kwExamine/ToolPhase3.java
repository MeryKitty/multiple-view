package kwExamine;

import java.util.ArrayList;

import basic.Config;
import basic.Node;
import basic.Point;
import basic.Sensor;
import basic.Testing;

public class ToolPhase3 {
	protected static int nodeWeight(Node n) {
		int reverseRank = Config.MAX_RANK - n.getRank();
		int result = 1;
		for (int i = 0; i < reverseRank; i++) {
			result <<= 2;
		}
		return result;
	}
	
	protected static double nodeCoverage(Node n, ArrayList<Sensor> sensorList) {
		int reverseRank = Config.MAX_RANK - n.getRank();
		double minDistance = Config.HEIGHT;
		for (int i = 0; i < Config.MAX_RANK; i++) {
			minDistance /= 2;
		}
		int splitCells = 1;
		for (int i = 0; i < reverseRank; i++) {
			splitCells <<= 1;
		}
		double result = 0;
		for (int i = 0; i < splitCells; i++) {
			for (int j = 0; j < splitCells; j++) {
				Point tempPoint = Point.getPoint(n.getX() - n.getSizeX() + (i + 1/2) * minDistance, n.getY() - n.getSizeY() + (j + 1/2) * minDistance);
				result += Testing.checkResult(Testing.getCoverInfoSet(tempPoint, sensorList)).coverageValue();
			}
		}
		return result / ToolPhase3.nodeWeight(n);
	}
	
	protected static double nodeCoverage(Node n) {
		return nodeCoverage(n, n.coverSensors());
	}
}
