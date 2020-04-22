public class Node {
    private int x, y ; // index of this node in the array
    private double exps ; // exposure
    int index ; // index of node after sorting

    public int getX() { return x; }
    public int getY() { return y; }
    public double getExposure() { return exps; }
    public int getIndex(){return index;}

    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setExposure(double exps) { this.exps = exps; }
    public void setIndex(int index){this.index = index ;}
    public void setIndex(){index = 0;}

    public Node(int x, int y, double exps){
        setX(x);
        setY(y);
        setExposure(exps);
        setIndex();
    }

    public boolean checkTop(int top){
        if (y== top)
            return true ;
        else
            return false;
    }
    public boolean checkBottom( int bottom){
        return checkTop(bottom);
    }

//    public boolean checkRight(Node other){
//        return ( ((x+1)== other.getX())&&( y == other.getY()));
//    }
//    public boolean checkLeft(Node other){
//        return  other.checkRight(this);
//    }
//    public boolean checkAbove(Node other){
//        return ((x== other.getX())&&((y+1)==other.getY()));
//    }
//    public boolean checkBelow(Node other){
//        return other.checkAbove(this) ;
//    }
//    public boolean checkNeighbor(Node other){
//        return ( (checkRight(other)) || (checkLeft(other)) || (checkAbove(other)) || (checkBelow(other))) ;
//    }

    public void printCoor(){
        System.out.print("( "+ x +", "+ y+") ");
    }
}
