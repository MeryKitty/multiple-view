package kwExamine;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

import basic.*;

public class DrawOutput {
	public static double LENGTH;
	public static double WIDTH;
	public static double RADIUS;
	public static double ANGLE;
	
	/**
	 * draw a set of sensors
	 * @param groupSensors
	 * @param g2
	 */
	public static void drawSetOfSensors(ArrayList<Sensor> groupSensors, Graphics2D g2) {
		for (int i = 0; i < groupSensors.size(); i++) {
			//drawOneSensor
			drawOneSensor(groupSensors.get(i), g2);
		}
	}

	/**
	 * Draw one sensor
	 * @param s
	 * @param g2
	 */
	public static void drawOneSensor(Sensor s, Graphics2D g2) {
		double finishAngle = s.direction() + Config.ALPHA;
		if (finishAngle > 360)
			finishAngle -= 360;
		double startAngle = s.direction() - Config.ALPHA;
		if (startAngle < 0)
			startAngle += 360;
		double xCen = s.x() + Config.RADIUS;
		double yCen = s.y() + Config.RADIUS;
		
//		double angle = s.direction();
		
//		g2.draw(new Arc2D.Double(xCen - Config.RADIUS, yCen - Config.RADIUS, Config.RADIUS * 2, Config.RADIUS * 2, startAngle, Config.ALPHA * 2, Arc2D.OPEN));
		
//		double x1 = Config.RADIUS * Math.cos((angle + Config.ALPHA) / 180 * Math.PI) + xCen;
//		double y1 = Config.RADIUS * Math.sin((angle + Config.ALPHA) / 180 * Math.PI) * (-1) + yCen;
//		double x2 = Config.RADIUS * Math.cos((angle - Config.ALPHA) / 180 * Math.PI) + xCen;
//		double y2 = Config.RADIUS * Math.sin((angle - Config.ALPHA) / 180 * Math.PI) * (-1) + yCen;
//		g2.draw(new Line2D.Double(xCen, yCen, x2, y2));
//		g2.draw(new Line2D.Double(xCen, yCen, x1, y1));
		g2.fill(new Arc2D.Double(xCen - 2, yCen - 2, 4, 4, 0, 360, Arc2D.OPEN));
	}
	
	/**
	 * fill all nodes that (k-w)-angle-covered
	 */
	public static void drawCoveredNodes(ArrayList<Node> coveredNodes, Graphics2D g2) {
		for (int i = 0; i < coveredNodes.size(); i++) {
			Node node = coveredNodes.get(i);
			double x = node.getX() + Config.RADIUS;
			double y = node.getY() + Config.RADIUS;
			double sizeX = node.getSizeX();
			double sizeY = node.getSizeY();
			g2.fill(new Rectangle2D.Double(x - sizeX, y - sizeY, sizeX * 2, sizeY * 2));
		}
	}
	
	public static void drawCenter(ArrayList<Sensor> sensors, Graphics2D g2) {
		for (int i = 0; i < sensors.size(); i++) {
			Sensor s = sensors.get(i);
			double x = s.x() + Config.RADIUS;
			double y = s.y() + Config.RADIUS;
			g2.fill(new Arc2D.Double(x - 5, y - 5, 10, 10, 0, 360, Arc2D.OPEN));
		}
	}
}