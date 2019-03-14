package MarcSanchez;

import java.util.Set;

public abstract class Node {

    protected int level;
    protected Set<Node> nodes;
    protected Piece piece;
    protected Position position;

    public void setPiece(Piece piece){this.piece = piece;}
    public void setPosition(Position pos){this.position = pos;}
    public int getLevel(){ return level;}

    public abstract int getHeuristic();

    public abstract void generate();

}
