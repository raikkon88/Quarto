package MarcSanchez;

public class NodeFantasma extends Node {

    protected int heuristic;

    public NodeFantasma(int heuristic){
        this.heuristic = heuristic;
    }

    @Override
    public Heuristic evalHeuristic() {
        return new Heuristic(new Piece(2222));
    }

    @Override
    public int getHeuristic() {
        return heuristic;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    protected Heuristic ieval() {
        return null;
    }

    @Override
    public void generate() {
        // DO nothing...
    }

    @Override
    public int[] getResult() {
        return new int[]{-1,-1,-1,-1,-1,-1};
    }
}
