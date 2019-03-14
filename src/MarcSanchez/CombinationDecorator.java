package MarcSanchez;

public class CombinationDecorator extends Decorator {

    public CombinationDecorator(Node node, Position pos){
        this.position = new Position(pos);
        this.level = node.level + 1;
    }

    @Override
    public int getHeuristic() {
        return 0;
    }
}
