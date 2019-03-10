package bulli;

import Quatro.Tauler;

import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;


/**
 * @author Usuari
 */
public class Player1 {
    
    public final static int MAX_DEPTH =16;
    
    private Tauler meutaulell;
    
    private ArrayList<Integer> _availables;
    private ArrayList<Integer> _positions;
    
    private Piece _givenPiece;
    
    private Node _root;
    
    private int _depth;
    private int _next;
    
    Player1(Tauler entrada){
        meutaulell = entrada;
        _availables = new ArrayList();
        _positions = new ArrayList();
        initializePieces();
        _givenPiece = null;        
        _root= null;
        _depth = 0;
        _next =3;
        
   }
    public int[] tirada(int colorin, int formain, int foratin, int tamanyin){
     //colorin - Color de la peça a colocar -> 	0 = Blanc 	1 = Negre
     //formain - Forma de la peça a colocar -> 	0 = Rodona 	1 = Quadrat
     //foratin - Forat de la peça a colocar -> 	0 = No  	1 = Si
     //tamanyin - Forat de la peça a colocar -> 0 = Petit 	1 = Gran
        
        int numericPlayPiece = colorin*8+formain*4+foratin*2+tamanyin;
        updateState(numericPlayPiece);
        //MiniMax solver = new MiniMax();
        AlfaBeta solver = new AlfaBeta();
        int nextDepth = _depth + _next;
        if (nextDepth > MAX_DEPTH) nextDepth = MAX_DEPTH;
        if (_depth>=10){
            int i =1;
        }
        solver.eval(_root, _depth, nextDepth);
        _depth++;
        if(_depth>=6)
            _next =7;
       
        
        Node best = solver.getBestNode();
        if(best != null){
            _root = best;
           
        }
        _givenPiece = _root.getPiece();
        return _root.getChoice();
       
    }
    
    
    private void initializePieces(){
       
        for(int i=0;i<Piece.LIMIT;i++){
            _availables.add(i);
            _positions.add(i);
        }
        Collections.shuffle(_availables);
        Collections.shuffle(_positions);
    }
    
    private void updateState(int piece){
        //si la peça que li he donat es null he de trobar la primera que ha posat ell, si no buscare on ha posat
        int x=0, y=0;
        while(x < meutaulell.getX() && y < meutaulell.getY() && 
                (_givenPiece == null  && meutaulell.getpos(x,y)==-1 || _givenPiece != null && meutaulell.getpos(x, y)!=_givenPiece.getValue())){
            
            if (y+1 >= meutaulell.getY())
                x++;
            y=(y+1)%(int)meutaulell.getY();
        }
        
        if (x < meutaulell.getX() && y < meutaulell.getY()){
            if (_givenPiece == null){
                /*
                String binary = String.format("%04d", new BigInteger(Integer.toBinaryString(meutaulell.getpos(x,y))));
                _givenPiece = new Piece(Character.getNumericValue(binary.charAt(0)),
                                        Character.getNumericValue(binary.charAt(1)),
                                        Character.getNumericValue(binary.charAt(2)),
                                        Character.getNumericValue(binary.charAt(3)));
               */
                int value = meutaulell.getpos(x, y);
                int v8=value/1000*8;
                value = value%1000;
                int v4= value/100*4;
                value = value%100;
                int v2 = value/10*2;
                int v1 = value%10;
                        
                _givenPiece = new Piece(v8+v4+v2+v1);
            }
           // _availables.remove(_availables.indexOf(_givenPiece.getNumericValue()));
           // _positions.remove(_positions.indexOf(x*4+y));
            
        }
        if(_root == null){
            Board b;
            if(_givenPiece==null){
               // _availables.remove(_availables.indexOf(piece));
                b= new Board(_positions,_availables);
                
            }
            else{
                b= new Board(_positions,_availables);
                b.setPiece(_givenPiece, x, y);
                _depth++;
               
            }
            _root = new Node(new Piece(piece),b);
        }
        else{
            //baixem el nivell segons on hagi colocat la peca el contrari
            _root = _root.getChild(x,y,new Piece(piece));
            _depth++;
        }
    }
}
