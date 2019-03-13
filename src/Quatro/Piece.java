package Quatro;

import java.util.*;

/**
 * Author : Marc Sànchez Pifarré
 * Udg Code : u1939705
 * Classe Node
 * ------------------------------
 * TODO : Comentar punts crítics.
 */
public class Piece {

    protected int [] piece;

    public Piece(int [] pieceValues) {
        piece = pieceValues.clone();
    }

    public Piece(Piece piece){
        this.piece = piece.piece.clone();
    }

    public Piece(int value){
        piece = new int[4];
        piece[3] = value % 2; value /= 10;
        piece[2] = value % 2; value /= 10;
        piece[1] = value % 2; value /= 10;
        piece[0] = value % 2;
    }

    public int getInt(){
        return fromArrayToInt(piece);
    }

    public Piece(int color, int forma, int forat, int tamany){
        this.piece = new int[4];
        this.piece[0] = color;
        this.piece[1] = forma;
        this.piece[2] = forat;
        this.piece[3] = tamany;
    }


    private int fromArrayToInt(int[] array){
        int value = 0;
        for(int i = 0; i < array.length; i++){
            value *= 10;
            value +=array[i];
        }
        return value;
    }

    public int[] getPropertiesArray(){
        int[] properties = new int[8];
        properties[0] = piece[0] == 0 ? 1 : 0;
        properties[1] = piece[0];
        properties[2] = piece[1] == 0 ? 1 : 0;
        properties[3] = piece[1];
        properties[4] = piece[2] == 0 ? 1 : 0;
        properties[5] = piece[2];
        properties[6] = piece[3] == 0 ? 1 : 0;
        properties[7] = piece[3];
        return properties;
    }

    public int getProperties(){
        return fromArrayToInt(getPropertiesArray());
    }

    public static List<Piece> generateNPieces(int n){
        List<Piece> pieces = new ArrayList<>();
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
            return ((Piece) obj).getInt() == getInt();
        }
    }

    @Override
    public String toString() {
        return getInt() + "";
    }
}
