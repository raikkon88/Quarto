package MarcSanchez;

import java.util.HashSet;
import java.util.Set;

/**
 * Author : Marc Sànchez Pifarré
 * Udg Code : u1939705
 * Classe Node
 * ------------------------------
 * TODO : Comentar punts crítics.
 */
public class Piece {

    protected int piece;

    public Piece(int [] pieceValues) {
        piece = pieceValues[0] * 1000+pieceValues[1]*100+ pieceValues[2]*10+pieceValues[3];
    }

    public Piece(Piece piece){
        this.piece = piece.piece;
    }

    public Piece(int value){
        piece = value;
    }

    public int getInt(){
        return piece;
    }

    public Piece(int color, int forma, int forat, int tamany){
        this(new int[]{color, forma, forat, tamany});
    }


    public int getProperties(){
        int tmp = piece;
        int tamany = tmp % 2; tmp /= 10;
        int forat  = tmp % 2; tmp /= 10;
        int forma  = tmp % 2; tmp /= 10;
        int color  = tmp % 2;
        int gran = tamany;
        int petita = tamany == 0 ? 1 : 0;
        int foradada = forat;
        int noForadada = forat == 0 ? 1 : 0;
        int quadrada = forma;
        int rodona = forma == 0 ? 1 : 0;
        int negre = color;
        int blanca = color == 0 ? 1 : 0;
        return  blanca * 10000000 +
                negre * 1000000 +
                rodona * 100000 +
                quadrada * 10000 +
                noForadada * 1000 +
                foradada * 100 +
                petita * 10 +
                negre;
    }



    public static Set<Piece> generateNPieces(int n){
        Set<Piece> pieces = new HashSet<>();
        for(int i = 0; i < n; i++){
            String value = Integer.toBinaryString(i);
            pieces.add(new Piece(Integer.valueOf(value)));
        }
        return pieces;

    }

    @Override
    public int hashCode() {
        return (int) Math.random() % 16;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return this == null;
        else if(!(obj instanceof Piece)) return false;
        else  {
            return ((Piece) obj).piece == piece;
        }
    }

    @Override
    public String toString() {
        return getInt() + "";
    }
}
