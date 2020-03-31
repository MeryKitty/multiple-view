package Algorithm;

import java.util.ArrayList ;
import java.util.LinkedList;
import java.util.Collections;
import java.util.List;

import basic.Node;

public class DisjointPath {
    public class DisjointTree {
        /**
         * index of first node in the tree, aka root of this tree, maybe unnecessary
         */
        private int root;

        /**
         * rank of path, used in Algorithm
         */
        private int rank ;

        /**
         * status of path
         */
        private boolean Start, End ;

        /**
         * the list of nodes stored in this tree
         */
        private ArrayList<Integer> index_of_node = new ArrayList<Integer>();

        /**
         * getter
         * @return
         */
        public int getRoot(){ return root;}
        public int getRank(){return rank;}
        public boolean getStart(){return  Start;}
        public boolean getEnd(){return  End;}
        public ArrayList<Integer> getIndex_of_node(){ return index_of_node;}

        /**
         * set Root
         * @param root
         */
        public void setRoot(int root) { this.root = root; }

        /**
         * set and update Rank
         */
        public void setRank(){ setRank(0);}
        public void setRank(int rank){this.rank = rank ;}
        public void updateRank(int increase){ rank += increase;}

        /**
         * set and update status
         */
        public void setStartEnd(){ Start = false; End = false ;}
        public void updateStart(){Start = true ;}
        public void updateEnd(){End = true;}

        /**
         * add node to this tree
         * @param node
         */
        public void setNode(Integer node){ this.index_of_node.add(node) ; }

        /**
         * constructor
         * @param root
         * @param node
         */
        public DisjointTree(int root, Integer node) {
            setRoot(root);
            setRank();
            setStartEnd();
            setNode(node);
        }

        /**
         * update nodes after executing method union
         * @param index_of_node
         */
        public void updateNodes(ArrayList<Integer> index_of_node){
            this.index_of_node.addAll(index_of_node);
        }

        /*public void resetTree(){
            setRoot(0);
            setRank();
            index_of_node.clear();
        }*/
    }

    private LinkedList<DisjointTree> paths = null ;

    /**
     * Default constructor
     */

    public DisjointPath(){} ;

    /**
     * sort nodes before execute the Algorithm
     * @param nodes
     */
    public sortNode(List<Node> nodes){
        ExposureComparator expComp= new ExposureComparator();
        Collections.sort(nodes, expComp);
    }

    /**
     * union 2 tree at position i and j
     * @param i
     * @param j
     * @return
     */
    private int union(int i, int j){
        if(paths.get(i).getRank() == paths.get(j).getRank()){
            if(i<j){
                // add all nodes in tree[j] into list of node in tree[i]
                paths.get(i).updateNodes(paths.get(j).getIndex_of_node());
                // rank of tree[i] increase 1
                paths.get(i).updateRank(1);
                paths.remove(j);
                return i;
            }
            else union(j,i);
        }
        else if ( paths.get(i).getRank() > paths.get(j).getRank()){
            // add all nodes in tree[j] into list of node in tree[i]
            paths.get(i).updateNodes(paths.get(j).getIndex_of_node());
            paths.remove(j);
            return i ;
        }

        else union(j, i);
    }

    /**
     * the algorithm
     * @param nodes
     * @param topline
     * @param bottomline
     * @return
     */
    public double executeAlg(List<Node> nodes, int topline, int bottomline ){
        paths = new LinkedList<DisjointTree>();
        int i ; // used to loop through the nodes, present the considering node
        int j ; // used to loop through the trees, present the considering tree
        int k ; // used to loop through the index of nodes stored in tree
        int h ; // value of that kth element, present the index of node
        int l ; // present the index of trees storing the considering node
        //loop through the nodes
        for (i=0; i<nodes.size() ;++i){
            DisjointTree mytree = new DisjointTree( i, i);
            paths.addFirst(mytree); ;
            l = 0;
            //loop through the Trees
            for(j=1; j<=paths.size()-1; ++j){
                // loop through the nodes stored in the tree
                for(k=0; k<=paths.get(j).getIndex_of_node().size()-1; ++k ){
                    // ArrayList stores the index of node stored in the DisjointTree
                    // kth element in the array is the index of kth node stored
                    // h is the value of that kth element
                    h= paths.get(j).getIndex_of_node().get(k);

                    // check if node[i] and node[k]-in-tree[j] is neighbors
                    if(nodes.get(i).isNeighbor(nodes.get(h))){
                        l = union(l, j);
                        j--;// number of tree decrease 1 after union , so does the index of considered trees
                        // check if node[i] is on top line
                        if (nodes.get(i).checkTop(topline))
                            paths.get(l).updateStart();
                        // check id node[i] is on bottom line
                        if(nodes.get(i).checkBottom(bottomline))
                            paths.get(l).updateEnd();
                        // check id this tree aka tree[l] is connecting to bound
                        if(paths.get(l).getStart() && paths.get(l).getEnd())
                            return nodes.get(i).getExposure();
                        break ;
                    }
                }
            }
        }
        return -1 ;
    }

}
