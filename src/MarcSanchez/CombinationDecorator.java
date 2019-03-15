package MarcSanchez;


import java.util.HashSet;

public class CombinationDecorator extends Decorator {

    public CombinationDecorator(Node node, Position pos, Piece piece){
        this.decorated = node;
        this.position = new Position(pos);
        this.piece = piece;
        this.level = node.level + 1;
        this.nodes = new HashSet<>();
        this.max = !node.max;
    }

    @Override
    public boolean isMax() {
        return this.max;
    }

    @Override
    public Heuristic evalHeuristic() {

        if(heuristic == null){
            this.heuristic = decorated.ieval();
            heuristic.add(this.position.x(), this.position.y(), heuristic.getPiece());
            heuristic.nextPiece = new Piece(this.piece);
            // TODO : Els heurísitcs es calculen malament. EL valor de les propietats de la peca crec que està malament.
        }
        return heuristic;

    }

    @Override
    public int getHeuristic() {
        if(this.heuristic == null){
            evalHeuristic();
        }
        return this.heuristic.getValue(this.level, this.max);
    }

    @Override
    public boolean isLeaf() {
        if(this.heuristic == null)
            evalHeuristic();
        return this.heuristic.finished;
    }

    @Override
    protected Heuristic ieval(){
        Heuristic h = decorated.ieval();
        h.add(this.position.x(), this.position.y(), h.getPiece());
        h.nextPiece = new Piece(this.piece);
        return h;
    }


    @Override
    public int[] getResult() {
        int value = this.piece.getInt();
        int size = value % 2; value /= 10;
        int hole = value % 2; value /= 10;
        int form = value % 2; value /= 10;
        int color = value % 2;
        return new int[]{this.position.x(), this.position.y(), color, form, hole, size};
    }



}
