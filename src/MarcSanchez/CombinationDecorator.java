package MarcSanchez;


import java.util.HashSet;

public class CombinationDecorator extends Decorator {


    protected Heuristic heuristic;

    public CombinationDecorator(Node node, Position pos, Piece piece){
        this.decorated = node;
        this.position = new Position(pos);
        this.piece = new Piece(piece);
        this.level = node.level + 1;
        this.heuristic = null;
        this.nodes = new HashSet<>();
        this.max = !node.max;
    }

    @Override
    public boolean isMax() {
        return this.max;
    }


    public void evalHeuristic() {
        this.heuristic = decorated.ieval();
        heuristic.add(this.position.x(), this.position.y(), heuristic.getPiece(), this.max);
        heuristic.nextPiece = new Piece(this.piece);
    }

    @Override
    protected Heuristic ieval(){
        Heuristic h = decorated.ieval();
        h.add(this.position.x(), this.position.y(), h.getPiece(), this.max);
        h.nextPiece = new Piece(this.piece);
        return h;
    }

    @Override
    public int getHeuristic() {
        if(this.heuristic == null){
            evalHeuristic();
        }
        if(this.heuristicValue == 0) {
            if (this.heuristic.isFinished()) {
                this.heuristicValue = Heuristic.HEURISTIC_MAX * (16 - level);
            } else {
                this.heuristicValue = this.heuristic.getValue();
            }
            if (this.max) {
                this.heuristicValue *= -1;
            }
        }
        return this.heuristicValue;
    }

    @Override
    public boolean isLeaf() {
        if(this.heuristic == null)
            evalHeuristic();
        return this.heuristic.isFinished();
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
