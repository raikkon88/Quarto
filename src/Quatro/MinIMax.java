package Quatro;

public class MinIMax {


    public MinIMax(){

    }

    private boolean enoughtDeep(int level){
        return level == 0;
    }


    public Node minIMax(Node node, int deep){
        // Node finalValue;
        if(enoughtDeep(deep)) {
            node.computeHeuristic();
            return node; // heuristic(node)
        }

        if(!node.isLeaf())
            node.generate();

        if(node.isEmpty()) {
            node.computeHeuristic();
            return node; // heuristic(node)
        }
        Node best;
        if(node.isMin()){
            best = new Node(Integer.MAX_VALUE);
            for(Node n : node.nodes){
                //if (alfa.getHeuristic() >= beta.getHeuristic()) break;
                Node value = minIMax(n, deep - 1);
                if(value.getHeuristic() < best.getHeuristic()) {
                    best = n;
                    n.setHeuristic(value.getHeuristic());
                }
            }
        }
        else {
            best = new Node(Integer.MIN_VALUE);
            for(Node n : node.nodes){
                Node value = minIMax(n, deep - 1);
                if( value.getHeuristic() > best.getHeuristic()) {
                    best = n;
                    n.setHeuristic(value.getHeuristic());
                }
            }
        }
        return best;
    }

}
