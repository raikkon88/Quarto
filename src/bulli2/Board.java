/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bulli2;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fbullich
 */
public class Board  implements Cloneable  {
    
    public final static int DIM_X =4;
    public final static int DIM_Y =4;
    
  
    private ArrayList<Integer> _availablePositions;
    private ArrayList<Integer> _availablePieces;
    int[][] _rows;
    int[][] _columns;
    int[] _diagonal ={0,0,0,0,0,0};
    int[] _invDiagonal ={0,0,0,0,0,0};
    
    public Board(ArrayList<Integer> positions, ArrayList<Integer> pieces){
       
        
       
        _availablePositions = positions;
        _availablePieces = pieces;
        _rows = new int[4][6];
        
        _columns = new int[4][6];
        
        for(int i=0;i<4;i++){
            for(int j=0;j<6;j++){
                _rows[i][j]=0;
                _columns[i][j]=0;
            }
        }
    }
    
    @Override
    public Object clone(){
        Board b= null;
        try {
            b = (Board)super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        b._rows=new int[4][6];
        b._columns = new int [4][6];
        b._diagonal = new int[6];
        b._invDiagonal = new int[6];
        for(int i =0;i<4;i++){
            for(int j =0; j<6;j++){
              
                b._rows[i][j]=_rows[i][j];
                b._columns[i][j]=_columns[i][j];
              
            }

            b._diagonal[i]=_diagonal[i];
            b._invDiagonal[i]=_diagonal[i];
        }
        b._diagonal[4]=_diagonal[4];
        b._diagonal[5]=_diagonal[5];
        b._invDiagonal[4]=_invDiagonal[4];
        b._invDiagonal[5]=_invDiagonal[5];

        
        b._availablePieces = (ArrayList<Integer>)(_availablePieces).clone();
        b._availablePositions = (ArrayList<Integer>)(_availablePositions).clone();
        return b;
    }
    
    public List<Integer> getRemainingPieces(){
        return (ArrayList<Integer>)(_availablePieces).clone();
    }
    
    public List<Integer> getRemainigPositions(){
        return (ArrayList<Integer>)(_availablePositions).clone();
    }
    
    public void setPiece(Piece p, int x, int y, int player){
        if(p != null && x >=0 && y >=0 && x < DIM_X && y < DIM_Y){
           
            int position = x*4+y;
            _availablePositions.remove(_availablePositions.indexOf(position));
            _availablePieces.remove(_availablePieces.indexOf(p.getNumericValue()));
            _rows[x][0]++;
            _columns[y][0]++;
            _rows[x][5]=player;
            _columns[y][5]=player;
            if(x==y ){
                _diagonal[0]++;
                _diagonal[5]=player;
            }
            if(y==x-3){
                _invDiagonal[0]++;
                _invDiagonal[5]=player;
            }
            int[] properties=p.getProperties();
            for(int i=0;i<4;i++){
                _rows[x][i+1]+=properties[i];
                _columns[y][i+1]+=properties[i];
                if(x==y)
                    _diagonal[i+1]=properties[i];
                if(y == 3-x)
                    _invDiagonal[i+1]=properties[i];
            }
        }
    }
    
   
   
   public boolean isQuarto(int color, int shape, int hole, int size){
       return color == 0 || color == 4 || shape==0 || shape==4 || hole==0 || hole==4 || size==0 || size==4;
   }
   
   public int heuristicValue(int x, int y){

       int diagonal=0;
       int rows=0;
       int cols=0;
     
      
        if(_diagonal[0]==4 && isQuarto(_diagonal[1],_diagonal[2],_diagonal[3],_diagonal[4]) )
            if(_diagonal[5]==1)
               diagonal+= 10000;
            else
               diagonal-= 10000;
            
        if(_invDiagonal[0]==4 && isQuarto(_invDiagonal[1],_invDiagonal[2],_invDiagonal[3],_invDiagonal[4]) )
            if(_invDiagonal[5]==1)
                diagonal+= 10000;
            else
                diagonal-= 10000;
        
        for(int i=0;i<4;i++){
           //mirem files i columnes;
        
            if(_rows[i][0]==4){
                if(isQuarto(_rows[i][1],_rows[i][2],_rows[i][3],_rows[i][4]) )
                    if(_rows[i][5]==1)
                        rows+= 10000;
                    else
                        rows-= 10000;
            }

            if(_columns[i][0]==4){
                if(isQuarto(_columns[i][1],_columns[i][2],_columns[i][3],_columns[i][4]) )
                     if(_columns[i][5]==1)
                        cols+= 10000;
                    else
                        cols-= 10000;
            }
           
            
           
            for(int j=1;j<5;j++){
                if(_rows[i][0]==_rows[i][j] || _rows[i][0]>0 &&_rows[i][j]==0)
                    if(_rows[i][0]==3 && _rows[i][5]== 1 )
                        rows-=_rows[i][0]*350;
                    else if(_rows[i][0]==2 && _rows[i][5]== 1 || _rows[i][0]==3 && _rows[i][5]== 2 )
                        rows+=_rows[i][0]*200;
                    else
                        rows+=_rows[i][0]*100;
                
                if(_columns[i][0]==_columns[i][j] || _columns[i][0]>0 &&_columns[i][j]==0)
                    if(_columns[i][0]==3 && _columns[i][5]== 1 )
                        cols-=_columns[i][0]*350;
                    else if(_columns[i][0]==2 && _columns[i][5]== 1 || _columns[i][0]==3 && _columns[i][5]== 2 )
                        cols+=_columns[i][0]*200;
                    else
                        cols+=_columns[i][0]*100;      
            }
            /*
            if(_invDiagonal[0]==_invDiagonal[i+1] || _invDiagonal[0]>0 &&_invDiagonal[i+1]==0)
                    if(_invDiagonal[0]==3 && _invDiagonal[5]== 1 )
                        diagonal-=_invDiagonal[0]*250;
                    else if(_invDiagonal[0]==2 && _invDiagonal[5]== 1 || _invDiagonal[0]==3 && _invDiagonal[5]== 2 )
                        diagonal+=_invDiagonal[0]*200;
                    else
                        diagonal+=_invDiagonal[0]*10; 
             if(_diagonal[0]==_diagonal[i+1] || _diagonal[0]>0 &&_diagonal[i+1]==0)
                    if(_diagonal[0]==3 && _diagonal[5]== 1 )
                        diagonal-=_diagonal[0]*250;
                    else if(_diagonal[0]==2 && _diagonal[5]== 1 || _diagonal[0]==3 && _diagonal[5]== 2 )
                        diagonal+=_diagonal[0]*200;
                    else
                         diagonal+=_diagonal[0]*10;
            */
            
       }
       
       //tractar diagonals
       
        /*
        for(int k=1;k<5;k++){
             
        }
       */
       return cols+rows+diagonal;
   }
     
}
