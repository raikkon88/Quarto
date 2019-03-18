package Quatro.Utilities;

import java.util.HashSet;
import java.util.Set;


/**
 * @Author : Marc Sànchez Pifarré
 * Udg Code : u1939705
 * Classe Position
 * ------------------------------------------------------------
 * INVARIANT PER AQUESTA PRÀCTICA : 0 <= x < 4 && 0 <= y < 4
 * ------------------------------------------------------------
 * Contenidor que emmagatzema 2 nombres, 1 per la posició x i l'altre per la posició y
 *
 * És cloneable, comparable i el seu hash code és random per poder-les tenir desordenades en un hashset.
 */
public class Position implements Cloneable {

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

    /**
     * Permet la generació de les 16 posicions inserides sense ordre en un set.
     * @param rows Nombre de files que es volen generar
     * @param cols Nombre de columnes per fila que es generaran
     * @return Un Conjunt sense ordre amb les row * col posicions.
     */
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
    protected Object clone() {
        return new Position(x, y);
    }

    @Override
    public String toString() {
        return x + "," + y;
    }
}
