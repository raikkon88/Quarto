package Quatro;

public class AlphaBeta {

    public AlphaBeta(){

    }


    private boolean enoughtDeep(int level){
        return level == 0;
    }


    public Node alphaBeta(Node node, int deep, Node alfa, Node beta){
        // Node finalValue;
        if(enoughtDeep(deep)) {
            node.evalHeuristic();
            return node; // heuristic(node)
        }

        if(!node.isFulla())
            node.generate();

        if(node.isEmpty()) {
            node.evalHeuristic();
            return node; // heuristic(node)
        }

        if(node.isMin()){
            for(Node n : node.nodes){
                if (alfa.getHeuristic() >= beta.getHeuristic()) break;
                Node value = alphaBeta(n, deep - 1, alfa, beta);
                if( value.getHeuristic() < beta.getHeuristic()){
                    beta = n;
                    n.setHeuristic(value.getHeuristic());
                }

            }
            return beta;
        }
        else {
            // finalValue = new Node(Integer.MAX_VALUE);
            for(Node n : node.nodes){
                if (alfa.getHeuristic() >= beta.getHeuristic()) break;
                Node value = alphaBeta(n, deep - 1, alfa, beta);
                if( value.getHeuristic() > alfa.getHeuristic()) alfa = n; n.setHeuristic(value.getHeuristic());

            }
            return alfa;
        }
    }

}
