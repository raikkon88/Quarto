package MarcSanchez;

public class NodeFantasma extends Node {

    protected int heuristic;

    public NodeFantasma(int heuristic){
        this.heuristic = heuristic;
    }

    @Override
    public int getHeuristic() {
        return heuristic;
    }

    @Override
    public void generate() {
        // DO nothing...
    }
}
