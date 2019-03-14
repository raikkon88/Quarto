package MarcSanchez;

public class TaulerInicial extends Node{

    Piece firstPiece;

    public TaulerInicial(Piece piece){
        this.firstPiece = piece;
        this.level = 0;
    }

    @Override
    public int getHeuristic() {
        return 0;
    }

    @Override
    public void generate() {

    }
}
