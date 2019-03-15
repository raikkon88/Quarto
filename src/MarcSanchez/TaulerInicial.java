package MarcSanchez;

import java.util.HashSet;

public class TaulerInicial extends Node{

    Piece firstPiece;

    public TaulerInicial(Piece piece){
        this.firstPiece = piece;
        this.level = 0;
        this.nodes = new HashSet<>();
        this.max = true;
        this.heuristic = new Heuristic(this.firstPiece);
    }

    @Override
    public Heuristic evalHeuristic() {
        return new Heuristic(this.firstPiece);
    }

    @Override
    public int getHeuristic() {
        return 0;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    protected Heuristic ieval() {
        return new Heuristic(this.firstPiece);
    }


    @Override
    public int[] getResult() {
        return new int[0];
    }
}
