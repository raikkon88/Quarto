package Quatro;

public class Combination extends Piece {

    private Position position;

    public Combination(int[] pieceValues) {
        super(pieceValues);
        position = new Position(-1, -1);
    }

    public Combination(Piece piece){
        super(piece);
        position = new Position(-1, -1);
    }

    public Combination(Position pos, Piece piece){
        super(piece);
        position = new Position(pos);
    }

    public Combination(int posX, int posY, Piece piece){
        super(piece);
        position = new Position(posX, posY);
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
