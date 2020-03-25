package basic;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * A node represents a rectangle
 * @author Sanji
 *
 */
public class Node {
	/**
	 * xCoor, yCoor are coordinates of the center of the node
	 */
	private double xCoor;
	private double yCoor;
	
	/**
	 * sizeX is half of the horizontal size of the rectangle
	 * sizeY is half of the vertical size of the rectangle 
	 */
	private double sizeX;
	private double sizeY;
	
	/**
	 * children is an array of 4 elements, each of which represent a sub-rectangle
	 * locating at 4 corners of this node. When a node is instantiated, value of children is null.
	 * We will instantiate 4 sub-nodes when splitting this node.
	 */
	private Node[] children;
	
	/**
	 * rank represents depth of the recursive tree. The more rank is, the deeper the tree is
	 * and the smaller the node is. Rank of the root node is 0, rank of the sub-node is one more
	 * than the parent node.
	 */
	private int rank;
	
	/**
	 * use to build graph
	 * neighbor is set of all vertices that are adjacent to this node.
	 */
	private ArrayList<Edge> neighbor = new ArrayList<Edge>();
	
	/**
	 * trace is the previous node in the path
	 */
	private Node trace;
	
	/**
	 * next is the next node in the path
	 */
	private Node next;
	
	/**
	 * use to mark if this node has been gone through
	 * use in findPath in Tools_Phase2
	 */
	public boolean flag = false;
	
	/**
	 * The set of sensors that cover the rectangle represented by the node follow the condition.
	 */
	private ArrayList<Sensor> coverSensors = null;
	
	/**
	 * the node always has isCovered = false after instantiating.
	 */
	public boolean isCovered = false;
	
	/**
	 * indicate the current smallest set of sensors to cover the path from source to this node.
	 */
	public HashSet<Sensor> currentPath;
	
	/**
	 * indicate the smallest set of sensors to cover the path from source to this node.
	 */
	public HashSet<Sensor> path;
	
	/**
	 * Indicate the list of nodes covered by the current sensor list
	 */
	public static ArrayList<Node> coveredNodes = new ArrayList<>();
	
	public static class Edge
	{
		public final Node value;
		public final int weight;
		
		public Edge(Node value, int weight)
		{
			this.value = value;
			this.weight = weight;
		}
		
		protected Node value()
		{
			return this.value;
		}
		
		protected int weight()
		{
			return this.weight;
		}
	}
	
	/**
	 * default constructor
	 */
	public Node() {}
	
	/**
	 * Version 2 of constructor
	 * @param xCoor
	 * @param yCoor
	 * @param sizeX
	 * @param sizeY
	 */
	public Node(double xCoor, double yCoor, double sizeX, double sizeY){
		this.xCoor = xCoor;
		this.yCoor = yCoor;
		this.sizeX = sizeX;
		this.sizeY = sizeY;
	}
	
	/**
	 * split the node to 4 sub-nodes
	 */
	public void split(){
		// instantiate the children here, not in declaration.
		children = new Node[4];
		
		//upper-left
		children[0] = new Node(xCoor - sizeX / 2, yCoor - sizeY / 2, sizeX / 2, sizeY / 2);
		//lower-left
		children[1] = new Node(xCoor - sizeX / 2, yCoor + sizeY / 2, sizeX / 2, sizeY / 2);
		//lower-right
		children[2] = new Node(xCoor + sizeX / 2, yCoor + sizeY / 2, sizeX / 2, sizeY / 2);
		//upper-right
		children[3] = new Node(xCoor + sizeX / 2, yCoor - sizeY / 2, sizeX / 2, sizeY / 2);
		
		for (int i = 0; i < 4; i++){
			children[i].setRank(this.rank + 1); //rank of sub-node is one more than parent.
		}
	}
	
	/**
	 * Getters and setters
	 * @return
	 */
	public Node[] getChildren(){
		return this.children;
	}
	public void setRank(int rank){
		this.rank = rank;
	}
	public int getRank(){
		return this.rank;
	}
	public double getX(){
		return this.xCoor;
	}
	public double getY(){
		return this.yCoor;
	}
	public double getSizeX(){
		return this.sizeX;
	}
	public double getSizeY(){
		return this.sizeY;
	}
	public ArrayList<Edge> getNeighbor(){
		return this.neighbor;
	}
	public void setTrace(Node node) {
		this.trace = node;
	}
	
	/**
	 * Get the previous node in the path
	 */
	public Node getTrace() {
		return this.trace;
	}
	public void setNext(Node node) {
		this.next = node;
	}
	
	/**
	 * Get the next node in the path
	 */
	public Node getNext() {
		return this.next;
	}
	
	public void setCoverSensors(ArrayList<Sensor> coverSensors)
	{
		if (this.coverSensors == null)
		{
			this.coverSensors = coverSensors;
		}
		else
		{
			throw new AssertionError();
		}
	}
	
	/**
	 * The set of sensors that cover the rectangle represented by the node follow the condition.
	 */
	public ArrayList<Sensor> coverSensors()
	{
		return this.coverSensors;
	}
}