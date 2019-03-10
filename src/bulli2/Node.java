/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bulli2;

import bulli2.Piece;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author fbullich
 */
public class Node {
    
    private int _posX;
    private int _posY;
    private boolean _max; 
    private Piece _piece;
    private List<Node> _child;  
    private Board _board;
    
    public Node(Piece p, Board board){
        this(-1,-1,true,p, board);
    }
    
    public Node(int x, int y, boolean max, Piece p, Board board){
        _piece=p;
        _posX=x;
        _posY=y;
        _max=max;
        _child= new ArrayList();
        _board=board;
        //updateState();
    }
    
    public boolean isMax(){
        return _max;
    }
    
    public boolean hasChild(){
        return _child.size()>0;
    }
    
    public List<Node> getChild(){
        return _child;
    }
   
    public Piece getPiece(){
        return _piece;
    }
    
    public boolean isQuarto(){
        return heuristic() ==1000;
    }
    
    public int[] getChoice(){
        int[] properties = _piece.getProperties();
        int[] result = {_posX,_posY,properties[0],properties[1],properties[2],properties[3]};
        return result;
        
    }
    
    public Node getChild(int x, int y, Piece p){
        if(!hasChild()){
            generateChild();
        }
        Iterator<Node> it = _child.iterator();
        Node n =null;
        while(n == null && it.hasNext()){
            Node aux = it.next();
            if(aux._posX == x && aux._posY == y && aux._piece.getNumericValue()==p.getNumericValue())
                n = aux;
        }
        
        return n;
    }
    
    public void generateChild(){
        
        if(!hasChild()){
            List<Integer> remPositions = _board.getRemainigPositions();
            List<Integer> remPieces = _board.getRemainingPieces();
            remPieces.remove(remPieces.indexOf(_piece.getNumericValue()));
            for(int i: remPositions){
                
                for(int j : remPieces){
                    Board b = (Board)_board.clone();
                    int x = i/4;
                    int y = i%4;
                    b.setPiece(_piece, x,y);
                    
                    Node n = new Node(x,y,!_max, new Piece(j), b);
                    
                    addChild(n);
                }
            }
        
        }
    }
    
    public int heuristic(){
        //boolean isQuarto = _board.isQuarto();
        
        
        int value = _board.heuristicValue(_posX,_posY);
       
        /*
        if (_max)
            return -value;
        else 
            return value;
        
        */
        return value;
    }
    
    public void addChild(Node n){
        _child.add(n);
    }
    
    
    private void updateState(){
        if (_posX != -1){
            _board.setPiece(_piece,_posX,_posY);
            
        }
    }

   
    
}
