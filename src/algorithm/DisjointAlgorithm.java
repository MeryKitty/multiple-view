package algorithm

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class DisjointAlgorithm {
    private class DisjointSet{
        private boolean Start ;
        private boolean End ;
        private int Rank ;
        private int indexOfRoot;
//        private Integer parOfRoot;
        private ArrayList<Integer> indexOfNodes ;

        public DisjointSet(boolean start, boolean end, Integer indexOfRoot){
            setStart(start);
            setEnd(end);
            setRank(1);
            setIndexOfRoot(indexOfRoot);
//            setParOfRoot();
            setIndexOfNodes();
        }

        public void setStart(boolean start){ Start = start;}
        public void setEnd(boolean end){End = end;}
        public void setRank(int Rank){this.Rank= Rank;}
        public void updateRank(int increase){Rank += increase;}
        public void setIndexOfRoot(int indexOfRoot){ this.indexOfRoot = indexOfRoot;}
        public void setIndexOfNodes(){indexOfNodes = new ArrayList<>();}
        public void setIndexOfNodes( int addnode){indexOfNodes.add(addnode);}
        public void setIndexOfNodes(ArrayList<Integer> addNode){indexOfNodes.addAll(addNode);}
//        public void setParOfRoot(Integer parOfRoot ){ this.parOfRoot = parOfRoot;}
//        public void setParOfRoot(){setParOfRoot(-1);}

        public boolean getStart(){return Start;}
        public boolean getEnd(){return End;}
        public Integer getIndexOfRoot(){return indexOfRoot;}
//        public Integer getParOfRoot(){return parOfRoot;}
        public int getRank(){return Rank;}
        public ArrayList<Integer> getIndexOfNodes(){return indexOfNodes; }

        public void printInfo(){
            int i;
            System.out.println("Root of this path : Node[" + indexOfRoot + "]" );
//            System.out.println("Parent of Root is : Node[" + parOfRoot +"]");
            for(i=0; i<indexOfNodes.size(); ++i)
                System.out.print("Node["+ indexOfNodes.get(i)+ "] ") ;
            System.out.println("\nStatus of this path Start: " + Start + " End: " + End +"\n");
        }
    }

    /**
     * Default constructor
     */
    public DisjointAlgorithm(){}

    /**
     * the first Integer is the index of node and the second one is index of its parents
     * parent  = -1 if the path whose this node doesn't belong to any paths.
     */
    private Map<Integer, Integer> par = new HashMap<>() ;
    private Map<Integer, DisjointSet> pathSNode = new HashMap<>() ;

    /**
     * Sort the List of Nodes before executing the DisjointPath Algorithm
     * @param nodes
     */
    public void sortNode(List<Node> nodes){
        ExposureComparator expComp= new ExposureComparator();
        Collections.sort(nodes, expComp);
    }

    public void indexingNodes(List<List<Node>> myNodes, List<Node> sortedNodes){
        int i;
        for(i=0; i<sortedNodes.size() ; ++i) {
//            sortedNodes.get(i).printCoor();
            myNodes.get(sortedNodes.get(i).getY()).get(sortedNodes.get(i).getX()).setIndex(i);
        }
    }

    /**
     * Find the origin node of root i
     * @param i
     * @return
     */
    public Integer originRoot( Integer i){
        if(par.get(i) == -1)
            return i;
        else{
            while(par.get(i)!= -1){
                i = par.get(i);
            }
            return i;
        }
    }

    /**
     * union 2 paths whose roots are i and j
     * @param i
     * @param j
     */
    public DisjointSet union(Integer i, Integer j){
        Integer temp ;
        if((i=originRoot(i)) == (j=originRoot(j))){}
        else {
            if (pathSNode.get(i).getRank() < pathSNode.get(j).getRank()){
                temp = i ;
                i= j;
                j = temp ;
            }
            pathSNode.get(i).setIndexOfNodes(j);
            pathSNode.get(i).setIndexOfNodes(pathSNode.get(j).getIndexOfNodes());
            pathSNode.get(i).setStart( pathSNode.get(i).getStart() | pathSNode.get(j).getStart());
            pathSNode.get(i).setEnd( pathSNode.get(i).getEnd() | pathSNode.get(j).getEnd());
            par.replace(j, i) ;

            if( pathSNode.get(i).getRank() == pathSNode.get(j).getRank()){
                pathSNode.get(i).updateRank(1);
            }

            pathSNode.remove(j) ;
        }
        return pathSNode.get(i);
    }

    /**
     * Main Algorithms
     * @param myNodes
     * @param sortedNodes
     * @param topLine
     * @param bottomLine
     */
    public void DisjointAlgorithms(List<List<Node>> myNodes, List<Node> sortedNodes, int topLine , int bottomLine){
        int i ; // loops through sortedNodes
        int index ,indexX, indexY ; // store coordinate of a specific node
        int leftBound = 0, rightBound = myNodes.get(0).size()-1;
        double tempExps ;
        DisjointSet tempSet ;

        // sorting nodes before executing
        sortNode(sortedNodes);

        // indexing the original nodes
        indexingNodes(myNodes, sortedNodes);

        for(i = 0 ; i<sortedNodes.size(); ++i){
            par.put(i, -1) ;
            tempSet =  new DisjointSet(sortedNodes.get(i).checkTop(topLine), sortedNodes.get(i).checkBottom(bottomLine), i);
            pathSNode.put(i,tempSet) ;
            tempExps = sortedNodes.get(i).getExposure();
            indexX = sortedNodes.get(i).getX() ;
            indexY = sortedNodes.get(i).getY() ;

            // compare this node with its neighbors
            if (indexX < rightBound){
                if(tempExps < myNodes.get(indexY).get(indexX + 1).getExposure()){
                    index = myNodes.get(indexY).get(indexX + 1).getIndex();
                    tempSet = union(i, index) ;
                }
            }
            if(indexX > leftBound){
                if(tempExps < myNodes.get(indexY).get(indexX - 1).getExposure()){
                    index = myNodes.get(indexY).get(indexX - 1).getIndex();
                    tempSet = union(i, index);
                }
            }
            if(indexY > topLine){ // topLine usually is 0
                if(tempExps < myNodes.get(indexY-1).get(indexX).getExposure()){
                    index = myNodes.get(indexY-1).get(indexX).getIndex();
                    tempSet = union(i, index);
                }
            }
            if(indexY < bottomLine){
                if(tempExps < myNodes.get(indexY+1).get(indexX).getExposure()){
                    index = myNodes.get(indexY+1).get(indexX).getIndex();
                    tempSet = union(i, index);
                }
            }

            // check if this path go through the region
            if(tempSet.getStart() && tempSet.getEnd()){
                tempSet.printInfo();
                sortedNodes.get(i).printCoor();
                System.out.println("Min exps is " + sortedNodes.get(i).getExposure());
                break;
            }
        }
    }
}
