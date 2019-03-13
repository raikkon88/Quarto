package Quatro;


/**
 * Author : Marc Sànchez Pifarré
 * Udg Code : u1939705
 * Classe Node
 * ------------------------------
 * TODO : Comentar punts crítics.
 */
public class Combination extends Piece {

    private Position position;

    public Combination(Piece piece){
        super(piece);
        position = new Position(-1, -1);
    }

    public Combination(Position pos, Piece piece){
        super(piece);
        position = new Position(pos);
    }

    public int x(){
        return position.x();
    }

    public int y(){
        return position.y();
    }

    public int[] toIntArray(){
        return new int[]{position.x(), position.y(), piece[0], piece[1], piece[2], piece[3]};
    }

    @Override
    public String toString() {
        return "[ " + position.toString() + " ] -> [ " + super.toString() + " ]";
    }
}
