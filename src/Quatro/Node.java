package Quatro;

import java.util.HashSet;
import java.util.Set;

public abstract class Node {

    protected int level;
    protected Set<Node> nodes;
    protected Piece piece;
    protected Position position;
    protected Set<Piece> toPlay;
    protected Set<Position> free;
    protected boolean max;
    protected int heuristicValue;

    public void setPiece(Piece piece){this.piece = piece;}
    public int getLevel(){ return level;}
    public boolean isMax(){return max;}
    public void setHeuristic(int heuristic){ this.heuristicValue= heuristic;}

    public abstract void alterOrder();

    public boolean isEmpty(){
        return nodes.isEmpty();
    }

    public void setPieces(Set<Piece> piecesToPlay){
        this.toPlay = piecesToPlay;
    }

    public void setFreePositions(Set<Position> freePositions){
        this.free = freePositions;
    }

    public abstract int getHeuristic();

    public abstract boolean isLeaf();

    protected abstract Heuristic ieval();

    public void generate(){

        // Combino les peces amb les posicions.
        for(Piece p : toPlay){
            for(Position pos : free){
                Node n = new CombinationDecorator(this, new Position(pos), new Piece(p));
                nodes.add(n);
                HashSet<Position> tmpPos = new HashSet<>(free);
                HashSet<Piece> tmpPiece  = new HashSet<>(toPlay);
                tmpPos.remove(pos);
                tmpPiece.remove(p);
                n.setFreePositions(tmpPos);
                n.setPieces(tmpPiece);
            }
        }
    }

    public abstract int[] getResult();

    @Override
    public String toString() {
        return this.position + " - P > " + this.piece + " - H > " + this.heuristicValue + " - N > " + this.level + " - M > " + this.isMax();
    }
}
