package Quatro;


/**
 * Author : Marc Sànchez Pifarré
 * Udg Code : u1939705
 * Classe Node
 * ------------------------------
 */
public class MinIMax {

    private boolean enoughtDeep(int level){
        return level == 0;
    }


    public Node minIMax(Node node, int deep){

        if(enoughtDeep(deep)) {
            return node;
        }

        if(!node.isLeaf())
            node.generate();

        if(node.isEmpty()) {
            return node;
        }
        Node best;
        if(!node.isMax()){
            best = new NodeFantasma(Integer.MAX_VALUE);
            for(Node n : node.nodes){
                Node value = minIMax(n, deep - 1);
                if(value.getHeuristic() < best.getHeuristic()) {
                    best = n;
                    n.setHeuristic(value.getHeuristic());
                }
            }
        }
        else {
            best = new NodeFantasma(Integer.MIN_VALUE);
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
