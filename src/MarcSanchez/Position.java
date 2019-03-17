package MarcSanchez;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Author : Marc Sànchez Pifarré
 * Udg Code : u1939705
 * Classe Node
 * ------------------------------
 * TODO : Comentar punts crítics.
 */
public class Position {

    private int x;
    private int y;

    public Position(int x, int y){
        this.x = x;
        this.y = y;
    }

    public Position(Position p){
        this.x = p.x;
        this.y = p.y;
    }

    public int x(){
        return x;
    }

    public int y(){
        return y;
    }

    public static Set<Position> generateNPositions(int rows, int cols){
        Set<Position> positions = new HashSet<>();
        for(int i = 0 ; i < rows * cols; i++){
            positions.add(new Position(i / rows, i % cols));
        }
        return positions;
    }

    @Override
    public int hashCode() {
        return (int) Math.random() % 16;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return this == null;
        else if(!(obj instanceof Position)) return false;
        else  {
            Position pos = ((Position) obj);
            return pos.x == this.x && pos.y == this.y;
        }
    }


    @Override
    public String toString() {
        return x + "," + y;
    }
}
