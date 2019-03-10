
package bulli2;

/**
 *
 * @author Francesc
 */
public class Piece {
    public final static int LIMIT =16;
    //Cada index de la primera dimensió conte una array amb la codificació de cada propietat d'una peça
    public final static int[][] VALUES = {{0,0,0,0},{0,0,0,1},{0,0,1,0},{0,0,1,1},
                                    {0,1,0,0},{0,1,0,1},{0,1,1,0},{0,1,1,1},
                                    {1,0,0,0},{1,0,0,1},{1,0,1,0},{1,0,1,1},
                                    {1,1,0,0},{1,1,0,1},{1,1,1,0},{1,1,1,1}};
    
    private int _value;
    //per obtenir les propietats de la peça es fara servir com a index de VALUES
    
    public Piece(){
       _value=-1;
    }
    
    public Piece(int value){
        _value = value;
    }
    
    
    public int[] getProperties(){
       
        if(_value>-1 && _value <LIMIT)
            return VALUES[_value];
        else 
            return null;
    }
    
    //retorna el valor en format de 4 numeros --> 1000
    public int getValue(){
        if(_value>-1 && _value <LIMIT){
            return VALUES[_value][0]*1000 + VALUES[_value][1]*100 + VALUES[_value][2]*10 + VALUES[_value][3];
        }
        else 
            return -1;
        
    }
    
    //retorna el valor de la peca entra 0 i 15
    public int getNumericValue(){
        return _value;
    }
    
    public static boolean compare(int p1, int p2, int p3, int p4){
        if(evalLimits(p1) && evalLimits(p2) && evalLimits(p3) && evalLimits(p4)){
            int color = VALUES[p1][0] +VALUES[p2][0]+VALUES[p3][0]+VALUES[p4][0];
            int shape  = VALUES[p1][1] +VALUES[p2][1]+VALUES[p3][1]+VALUES[p4][1];
            int hole = VALUES[p1][2] +VALUES[p2][2]+VALUES[p3][2]+VALUES[p4][2];
            int size  = VALUES[p1][3] +VALUES[p2][3]+VALUES[p3][4]+VALUES[p4][4];
            return color == 4 || color == 0 || shape == 4 || shape == 0 || hole == 4 || hole == 0 || size == 4 || size == 0;
        }
        else 
            return false;
    }
    
    private static boolean evalLimits(int eval){
        return eval>-1 && eval < LIMIT;
    }
}
