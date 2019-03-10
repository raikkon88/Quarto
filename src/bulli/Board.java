/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bulli;

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
    
    private int[][] _board;
    private ArrayList<Integer> _availablePositions;
    private ArrayList<Integer> _availablePieces;
    
    
    public Board(ArrayList<Integer> positions, ArrayList<Integer> pieces){
        _board= new int[DIM_X][DIM_Y];
        
        for(int i = 0; i <DIM_X; i++){
            for (int j = 0; j <DIM_Y; j++) {
                _board[i][j]=-1;
            }
        } 
        _availablePositions = positions;
        _availablePieces = pieces;
    }
    
    @Override
    public Object clone(){
        Board b= null;
        try {
            b = (Board)super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
        }
        b._board = new int[4][4];
        for(int i =0;i<4;i++){
            for(int j =0; j<4;j++){
                b._board[i][j]=_board[i][j];
            }
        }
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
    
    public void setPiece(Piece p, int x, int y){
        if(p != null && x >=0 && y >=0 && x < DIM_X && y < DIM_Y){
            _board[x][y]=p.getNumericValue();
            int position = x*4+y;
            _availablePositions.remove(_availablePositions.indexOf(position));
            _availablePieces.remove(_availablePieces.indexOf(p.getNumericValue()));
        }
    }
    
    public boolean isQuarto(){
       if (guanya(_board[0][0],_board[0][1],_board[0][2],_board[0][3])){
            
            return true;
        }
        
        if (guanya(_board[1][0],_board[1][1],_board[1][2],_board[1][3])){
            
            return true;
        }           
        
        if (guanya(_board[2][0],_board[2][1],_board[2][2],_board[2][3])){
           
            return true;
        }

        if (guanya(_board[3][0],_board[3][1],_board[3][2],_board[3][3])){
           
            return true;
        }

        
         if (guanya(_board[0][0],_board[1][0],_board[2][0],_board[3][0])){
           
            return true;
        }
        
        if (guanya(_board[0][1],_board[1][1],_board[2][1],_board[3][1])){
           
            return true;
        }           
        
        if (guanya(_board[0][2],_board[1][2],_board[2][2],_board[3][2])){
           
            return true;
        }

        if (guanya(_board[0][3],_board[1][3],_board[2][3],_board[3][3])){
           
            return true;
        }

        
        if (guanya(_board[0][0],_board[1][1],_board[2][2],_board[3][3])){
           
            return true;
        }

        
        if (guanya(_board[0][3],_board[1][2],_board[2][1],_board[3][0])){
           
            return true;
        }
        
        return false;
    }
    
   private boolean guanya(int p1, int p2, int p3, int p4){
        // 1 si es una combinacio guanyadora o si no
        if( p1==-1 || p2==-1 || p3==-1 || p4==-1){
            //hi ha algun dels 4 buit no podem guanyar
            return false;
        }else{
            //les 4 peçes tenen valor    

            //peça p1              
            int Color1=(int) (p1/1000);
            p1 = p1 - Color1 * 1000;
            int Forma1=(int) (p1%1000/100);
            p1 = p1 - Forma1 * 100;
            int Forat1=(int) (p1%1000/10);
            p1 = p1 - Forat1 * 10;
            int Tamany1=(int) (p1%1000/1);

            //peça p2
            int Color2=(int) (p2/1000);
            p2 = p2 - Color2 * 1000;
            int Forma2=(int) (p2%1000/100);
            p2 = p2 - Forma2 * 100;
            int Forat2=(int) (p2%1000/10);
            p2 = p2 - Forat2 * 10;
            int Tamany2=(int) (p2%1000/1);  

            //peça p3
            int Color3=(int) (p3/1000);
            p3 = p3 - Color3 * 1000;
            int Forma3=(int) (p3%1000/100);
            p3 = p3 - Forma3 * 100;
            int Forat3=(int) (p3%1000/10);
            p3 = p3 - Forat3 * 10;
            int Tamany3=(int) (p3%1000/1);

            //peça p4  
            int Color4=(int) (p4/1000);
            p4 = p4 - Color4 * 1000;
            int Forma4=(int) (p4%1000/100);
            p4 = p4 - Forma4 * 100;
            int Forat4=(int) (p4%1000/10);
            p4 = p4 - Forat4 * 10;
            int Tamany4=(int) (p4%1000/1); 

            int Color = Color1 + Color2 + Color3 + Color4;
            int Forma = Forma1 + Forma2 + Forma3 + Forma4;
            int Forat = Forat1 + Forat2 + Forat3 + Forat4;
            int Tamany = Tamany1 + Tamany2 + Tamany3 + Tamany4;

            return Color == 0 || Color == 4 || Forma==0 || Forma==4 || Forat==0 || Forat==4 || Tamany==0 || Tamany==4;
        }
    }
   
   public int heuristicValue(int x, int y){

       int heuristic=0;
       int[] properties = (new Piece(_board[x][y])).getProperties();
       for(int i=0;i<4;i++){
           //mirem files i columnes;
           
            for(int j=0;j<4;j++){
              if((i!=y) && propertiesMatch(properties,j,x,i))
                  heuristic++;
              if(i!=x && propertiesMatch(properties,j,y,i)){
                   heuristic++;
              }
              if(x==y || y == 3-x){
                  if(i!=x){
                    if(propertiesMatch(properties,i,i,i))
                        heuristic++;
                    else if (propertiesMatch(properties,i,i,3-i))
                        heuristic++;
                    }
                }
            }
       }
       
       return heuristic;
   }
   
   private boolean propertiesMatch(int[] properties, int property, int x, int y){
       Piece p = new Piece(_board[x][y]);
       
       return p.getNumericValue() !=-1 && properties[property] == p.getProperties()[property];
   }
   
}
