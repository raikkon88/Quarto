package Quatro;

import java.util.ArrayList;
import java.util.List;



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

    public static List<Position> generateNPositions(int rows, int cols){
        List<Position> positions = new ArrayList<>();
        for(int i = 0 ; i < rows * cols; i++){
            positions.add(new Position(i / rows, i % cols));
        }
        return positions;
    }

    @Override
    public String toString() {
        return x + "," + y;
    }
}
