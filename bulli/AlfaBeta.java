/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bulli;

import java.util.Iterator;

/**
 *
 * @author Francesc
 */
public class AlfaBeta {
    
     private Node _bestNode;
    
    public AlfaBeta(){
       _bestNode=null;
    }
    
    public Node getBestNode(){
        return _bestNode;
    }
    
    private int eval(Node actual, int actualDepth, int depthToReach, int firstDepth, int alpha, int beta){
         if (actualDepth == depthToReach) 
            return actual.heuristic();
        
        actual.generateChild();
        if (!actual.hasChild())
            return actual.heuristic();
        
        if(actual.isMax()){
            Iterator<Node> it = actual.getChild().iterator();
            while(it.hasNext() && alpha < beta){
                Node n = it.next();
                int value = eval(n,actualDepth+1,depthToReach,firstDepth, alpha, beta);
                if (value > alpha){                   
                    alpha = value;
                    if(actualDepth == firstDepth){
                         _bestNode = n;
                    }
                }
            }
            return alpha;
        }
        else{
            Iterator<Node> it = actual.getChild().iterator();
            while(it.hasNext() && alpha < beta){
                Node n = it.next();
                int value = eval(n,actualDepth+1,depthToReach,firstDepth, alpha, beta);
                if (value < beta){                   
                    beta = value;
                    if(actualDepth == firstDepth){
                         _bestNode = n;
                    }
                }
            }
            return beta;
        }
       
    }
    
    public void eval(Node actual, int actualDepth, int depthToReach){
        eval(actual,actualDepth,depthToReach,actualDepth,Integer.MIN_VALUE,Integer.MAX_VALUE);
        
    }
    
}
