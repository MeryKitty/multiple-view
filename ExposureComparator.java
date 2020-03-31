package Algorithm;

import basic.Node;

import java.util.Comparator;

public class ExposureComparator implements Comparator<Node> {
    public int compare(Node n1, Node n2){
        if(n1.getExposure() < n2.getExposure()){
            return -1;
        }
        else if( n1.getExposure() == n2.getExposure()){
            return 0;
        }
        else
            return 1;
    }
}
