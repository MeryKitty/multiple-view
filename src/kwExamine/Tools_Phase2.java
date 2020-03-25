package kwExamine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import basic.*;

public class Tools_Phase2 {
	/**
	 * check if node1 and node2 are adjacent or not
	 * @param node1
	 * @param node2
	 * @return 1 if node1 and node 2 share a segment, 2 if only share a vertex, 0 if not adjacent
	 */
	public static int isIntersect(Node node1, Node node2) {
		//retrieve four corners of node1
		double left_1 = node1.getX() - node1.getSizeX();
		double right_1 = node1.getX() + node1.getSizeX();
		double bottom_1 = node1.getY() + node1.getSizeY();
		double top_1 = node1.getY() - node1.getSizeY();

		//retrieve four corners of node2
		double left_2 = node2.getX() - node2.getSizeX();
		double right_2 = node2.getX() + node2.getSizeX();
		double bottom_2 = node2.getY() + node2.getSizeY();
		double top_2 = node2.getY() - node2.getSizeY();
		
		//check if node1 and node2 intersect each other
		if (left_2 > right_1 || left_1 > right_2)
			return 0;
		if (bottom_1 < top_2 || bottom_2 < top_1) 
			return 0;
		if (left_2 == right_1)
		{
			if (top_1 == bottom_2 || top_2 == bottom_1)
			{
				return 2;
			}
		}
		else if (left_1 == right_2)
		{
			if (top_1 == bottom_2 || top_2 == bottom_1)
			{
				return 2;
			}
		}
		return 1;
	}
	
	/**
	 * make bond among nodes
	 * @param setOfNodes
	 * @param source
	 * @param end
	 */
	@SuppressWarnings("unchecked")
	public static void buildGraph(ArrayList<Node> setOfNodes, Node source, Node end) {
		/**
		 * use temp variable to temporary store the elements in setOfNodes.
		 * the purpose is not to change setOfNodes. we just need to change 
		 * elements of setOfNodes
		 */
		ArrayList<Node> temp = (ArrayList<Node>) setOfNodes.clone();
		
		/**
		 * make bond between 
		 * 1. source node and nodes adjacent to source
		 * 2. nodes adjacent to end and end
		 */
		for (int i = 0; i < temp.size(); i++) {
			if (temp.get(i).getX() - temp.get(i).getSizeX() < 0.00001) {
				source.getNeighbor().add(new Node.Edge(temp.get(i), 1));
			}
			if (temp.get(i).getX() + temp.get(i).getSizeX() > Config.WIDTH - 0.00001) {
				temp.get(i).getNeighbor().add(new Node.Edge(end, 1));
			}
		}
		
		/**
		 * make bond among nodes of setOfNodes
		 */
		Node first;
		while (!temp.isEmpty()) {
			first = temp.get(temp.size() - 1);
			for (int i = 1; i < temp.size(); i++) {
				int weight = isIntersect(first, temp.get(i));
				if (weight > 0) {
					first.getNeighbor().add(new Node.Edge(temp.get(i), weight));
					temp.get(i).getNeighbor().add(new Node.Edge(first, weight));
				}
			}
			temp.remove(temp.size() - 1);
		}
	}
	
	/**
	 * find a path from source to end
	 * @param setOfNodes
	 * @param source
	 * @param end
	 */
	@SuppressWarnings("unchecked")
	public static void findPathDijiktra(ArrayList<Node> setOfNodes, Node source, Node end) {
		/**
		 * Dijiktra traversing
		 */
		ArrayList<Node> traverseQueue = new ArrayList<Node>();
		traverseQueue.add(source);
		source.path = new HashSet<Sensor>();
		source.currentPath = source.path;
		while (!traverseQueue.isEmpty())
		{
			Node tempNode = Collections.min(traverseQueue, new Comparator<Node>() {
				@Override
				public int compare(Node n1, Node n2)
				{
					if (n1.currentPath.size() > n2.currentPath.size())
					{
						return 1;
					}
					else if (n1.currentPath.size() == n2.currentPath.size())
					{
						return 0;
					}
					else
					{
						return -1;
					}
				}
			});
			traverseQueue.remove(tempNode);
			tempNode.path = tempNode.currentPath;
			tempNode.flag = true;
			if (tempNode == end)
			{
				break;
			}
			for (Node.Edge e : tempNode.getNeighbor())
			{
				Node n = e.value;
				if (!n.flag)
				{
					HashSet<Sensor> tempSet = (HashSet<Sensor>) tempNode.path.clone();
					if (n != end)
					{
						for (Sensor s : n.coverSensors())
						{
							tempSet.add(s);
						}
					}
					if (n.currentPath != null && n.currentPath.size() > tempSet.size())
					{
						n.currentPath = tempSet;
						n.setTrace(tempNode);
					}
					else if (n.currentPath == null)
					{
						n.currentPath = tempSet;
						n.setTrace(tempNode);
						traverseQueue.add(n);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void findPath(ArrayList<Node> setOfNodes, Node source, Node end)
	{
		/**
		 * BFS traversing
		 */
		ArrayList<Node> QUEUE = new ArrayList<Node>();
		QUEUE.add(source);
		Node cur;
		HashSet<Sensor> currentPath;
		boolean stop = false;
		
		while (!QUEUE.isEmpty() && !stop)
		{
			cur = QUEUE.remove(0);			
			if (cur != source)
			{
				currentPath = cur.path;
			}
			else
			{
				currentPath = new HashSet<Sensor>();
			}
			for (int i = 0; i < cur.getNeighbor().size(); i++) {
				if (!cur.getNeighbor().get(i).value.flag)
				{
					Node neighbour = cur.getNeighbor().get(i).value;
					HashSet<Sensor> tempSet = (HashSet<Sensor>) currentPath.clone();
					neighbour.flag = true;
					neighbour.setTrace(cur);
					QUEUE.add(neighbour);
					if (neighbour != end)
					{
						for (Sensor s : neighbour.coverSensors())
						{
							tempSet.add(s);
						}
						neighbour.path = tempSet;
					}
					else
					{
						neighbour.path = tempSet;
						stop = true;
						break;
					}
				}	
			}
		}
	}
}
