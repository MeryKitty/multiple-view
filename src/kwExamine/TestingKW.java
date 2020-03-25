package kwExamine;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.*;
import basic.*;

public class TestingKW {
	private static class SplitThread extends Thread {
		private double xRoot, yRoot, sizeX_root, sizeY_root;
		private ArrayList<Sensor> sensorList;
		private Semaphore semaphore;
		private int k;
		private double omega;
		
		public SplitThread(Semaphore semaphore, ArrayList<Sensor> sensorList, int k, double omega, double... info) {
			this.k = k;
			this.omega = omega;
			this.xRoot = info[0];
			this.yRoot = info[1];
			this.sizeX_root = info[2];
			this.sizeY_root = info.length > 3 ? info[3] : this.sizeX_root;
			this.sensorList = sensorList;
			this.semaphore = semaphore;
		}
		
		@Override
		public void run() {
			//initialize root node
	    	Node root = new Node(this.xRoot, this.yRoot, this.sizeX_root, this.sizeY_root);
	    	//initialize QUEUE and some parameters
	    	ArrayDeque<Node> QUEUE = new ArrayDeque<Node>();
	    	QUEUE.addFirst(root);
	    	double x, y;	//store the coordinates of the node being considered
	    	double sizeX, sizeY; //store sizeX and sizeY of the node being considered
	    
	    	//Start the loop
	        while (!QUEUE.isEmpty()) {
	            // retrieve node
	        	// node is the node being judged
	            Node node = QUEUE.pollFirst();

	            // retrieve information of the node
	            x = node.getX();
	            y = node.getY();
	            sizeX = node.getSizeX();
	            sizeY = node.getSizeY();

	            // four-corners of the node
	            Point upperLeft = Point.getPoint(x - sizeX, y - sizeY);
	            Point lowerLeft = Point.getPoint(x - sizeX, y + sizeY);
	            Point lowerRight = Point.getPoint(x + sizeX, y + sizeY);
	            Point upperRight = Point.getPoint(x + sizeX, y - sizeY);
	            // find coverGroup of one cell
	            ArrayList<Sensor> coverGroup = Testing.getCoverSet(sensorList, upperLeft, lowerLeft, upperRight, lowerRight);
	            ArrayList<Sensor> temp;
		    	if (coverGroup == null) {
		    		temp = null;
		    	} else {
		    		temp = TestingKW.checkNode(node, coverGroup, this.k, this.omega);
		    		
		    	}
		    	if (temp != null)
		    	{
	        		node.isCovered = true;
	        		node.setCoverSensors(temp);
	        		temp = null;
		    	}
		    	
	            // split or not
	            if (node.isCovered) {
	            	// drawOutput
	            	synchronized (Node.coveredNodes) {
	            		Node.coveredNodes.add(node);
	            	}
	            } 
	            else if (node.getRank() < Config.MAX_RANK) {
	                node.split();
	                for (int i = 0; i < 4; i++) {
	                    QUEUE.addLast(node.getChildren()[i]);
	                }
	            }
	        }
	        this.semaphore.release();
	    }
	}
	
	public static ArrayList<Sensor> checkNode(Node node, ArrayList<Sensor> sensorList, int k, double omega) {
		Point upperLeft = Point.getPoint(node.getX() - node.getSizeX(), node.getY() + node.getSizeY());
		ArrayList<ArrayList<CoverInformation>> kwCoverList = Testing.getKWSet(Testing.getCoverInfoSet(upperLeft, sensorList), k, omega);
		if (kwCoverList == null) {
			return null;
		}
		Point upperRight = Point.getPoint(node.getX() + node.getSizeX(), node.getY() + node.getSizeY());
		Point lowerLeft = Point.getPoint(node.getX() - node.getSizeX(), node.getY() - node.getSizeY());
		Point lowerRight = Point.getPoint(node.getX() + node.getSizeX(), node.getY() - node.getSizeY());
		ArrayList<ArrayList<CoverInformation>> allCoverList = new ArrayList<>(kwCoverList.size());
		ArrayList<ArrayList<Sensor>> allSensorList = new ArrayList<>(kwCoverList.size());
		for (ArrayList<CoverInformation> c : kwCoverList) {
			ArrayList<Sensor> s = CoverInformation.getSensorList(c);
			if (Testing.checkKW(lowerRight, s, k, omega) && Testing.checkKW(upperRight, s, k, omega) && Testing.checkKW(lowerLeft, s, k, omega)) {
				allCoverList.add(c);
				allSensorList.add(s);
			}
		}
		if (allCoverList.isEmpty()) {
			return null;
		}
		Collections.shuffle(allSensorList, Config.RANDOM_GENERATE);
		ArrayList<Sensor> result = allSensorList.get(0);
		double minCoverage = ToolPhase3.nodeCoverage(node, result);
		for (ArrayList<Sensor> s : allSensorList) {
			double temp = ToolPhase3.nodeCoverage(node, s);
			if (temp > minCoverage) {
				result = s;
				minCoverage = temp;
			}
		}
		/*double maxCoverage = Testing.checkResult(allCoverList.get(0)).coverageValue();
		for (ArrayList<CoverInformation> c : allCoverList) {
			double temp = Testing.checkResult(c).coverageValue();
			if (temp > maxCoverage) {
				result = c;
				maxCoverage = temp;
			}
		}*/
		
		return result;
	}
	
	public static void splitAndCheck(ArrayList<Sensor> sensorList, int k, double omega) {
		/**
    	 * information of root node
    	 */
		Semaphore sem = new Semaphore(0);
		for(int square = 0; square < 4; square++) {
			double xRoot = Config.HEIGHT / 2 + Config.HEIGHT * square;
        	double yRoot = Config.HEIGHT / 2;
        	double sizeX_root = Config.HEIGHT / 2;
        	double sizeY_root = Config.HEIGHT / 2;
        	new SplitThread(sem, sensorList, k, omega, xRoot, yRoot, sizeX_root, sizeY_root).start();
		}
        sem.acquireUninterruptibly(4);
/*        	//initialize root node
        	Node root = new Node(xRoot, yRoot, sizeX_root, sizeY_root);
        	//initialize QUEUE and some parameters
        	ArrayDeque<Node> QUEUE = new ArrayDeque<Node>();
        	QUEUE.addFirst(root);
        	double x, y;	//store the coordinates of the node being considered
        	double sizeX, sizeY; //store sizeX and sizeY of the node being considered
        
        	//Start the loop
	        while (!QUEUE.isEmpty()) {
	            // retrieve node
	        	// node is the node being judged
	            Node node = QUEUE.pollFirst();
	
	            // retrieve information of the node
	            x = node.getX();
	            y = node.getY();
	            sizeX = node.getSizeX();
	            sizeY = node.getSizeY();
	
	            // four-corners of the node
	            Point upperLeft = Point.getPoint(x - sizeX, y - sizeY);
	            Point lowerLeft = Point.getPoint(x - sizeX, y + sizeY);
	            Point lowerRight = Point.getPoint(x + sizeX, y + sizeY);
	            Point upperRight = Point.getPoint(x + sizeX, y - sizeY);
	            // find coverGroup of one cell
	            ArrayList<Sensor> coverGroup = Testing.getCoverSet(sensorList, upperLeft, lowerLeft, upperRight, lowerRight);
	            ArrayList<Sensor> temp;
		    	if (coverGroup == null) {
		    		temp = null;
		    	} else {
		    		long start = System.currentTimeMillis();
		    		temp = checkNode(node, coverGroup);
		    		Operate.timer += System.currentTimeMillis() - start;
		    	}
		    	if (temp != null)
		    	{
	        		node.isCovered = true;
	        		node.setCoverSensors(new HashSet<>(temp));
	        		temp = null;
		    	}
		    	
	            // split or not
	            if (node.isCovered) {
	            	// drawOutput
	            	Node.coveredNodes.add(node);
	            } 
	            else if (node.getRank() < 6) {
	                node.split();
	                for (int i = 0; i < 4; i++) {
	                    QUEUE.addLast(node.getChildren()[i]);
	                }
	            }
	        }
        }*/
    }
	
/*	public static void checkDeepest(ArrayList<Sensor> sensorList, int k, double omega) {
		double minDistance = Config.HEIGHT;
		for (int i = 0; i < Config.MAX_RANK; i++) {
			minDistance /= 2;
		}
		int columnNum = (int)(Config.WIDTH / minDistance);
		int rowNum = (int)(Config.HEIGHT / minDistance);
		
	}*/
	
	public static Point[] nodeRandomPoint(Node node, int distanceRank) {
		Point[] result = new Point[2];
		Point p1, p2;
		p1 = Point.getPoint((Config.RANDOM_GENERATE.nextDouble() * 2 - 1) * node.getSizeX() + node.getX(), (Config.RANDOM_GENERATE.nextDouble() * 2 - 1) * node.getSizeY() + node.getY());
		int coefficient = 1 << (distanceRank - node.getRank());
		double x = p1.x() * coefficient;
		double y = p1.y() * coefficient;
		x = Math.round(x); y = Math.round(y);
		p2 = Point.getPoint(x / coefficient, y / coefficient);
		result[0] = p2; result[1] = p1;
		return result;
	}
}
