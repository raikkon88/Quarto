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

        Heuristic tmp = decorated.evalHeuristic();

        // Rebo la peça de baix i tinc la posició aquí mateix.
        tmp.add(tmp.getPieceValue(), this.position.x(), this.position.y());
        tmp.setNextPiece(this.piece);

        // Retorno la peça que tinc jo i la suma  del que porto acumolat.
        return tmp;
    }

    @Override
    public int getHeuristic() {
        if(decorated != null) {
            this.heuristic = evalHeuristic();
            return heuristic.getValue(this.level, this.max);
        }
        else{
            return heuristicValue;
        }
    }

    @Override
    public boolean isLeaf() {
        return decorated.iisLeaf().finished;
    }

    @Override
    protected Heuristic iisLeaf(){
        Heuristic h = decorated.iisLeaf();
        h.add(h.nextPiece.getInt(), this.position.x(), this.position.y());
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
