package Quatro;

public class NodeFantasma extends Node {

    protected int heuristic;

    public NodeFantasma(int heuristic){
        this.heuristic = heuristic;
    }

    @Override
    public void alterOrder() {
        // Nothing to do
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
