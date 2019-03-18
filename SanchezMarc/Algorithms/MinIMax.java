package Quatro.Algorithms;


import Quatro.Decorator.Node;
import Quatro.Decorator.NodeFantasma;

/**
 * @Author : Marc Sànchez Pifarré
 * Udg Code : u1939705
 * Classe MinIMax
 * ------------------------------
 * S'empra per executar l'algoritme Min i Max sobre un node en concret.
 */
public class MinIMax {

    private boolean enoughtDeep(int level){
        return level == 0;
    }


    /**
     * No s'utilitza a la solucio final, es va voler començar la pràctica desenvolupant aquest algoritme.
     * @param node Node del que es partirà per generar l'arbre de cerca.
     * @param deep Profunditat que queda per assolir. La conta és decreixent i va a 0.
     * @return El Node escollit com a node següent segons l'algoritme MinIMax.
     */
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
            for(Node n : node.getNodes()){
                Node value = minIMax(n, deep - 1);
                if(value.getHeuristic() < best.getHeuristic()) {
                    best = n;
                    n.setHeuristic(value.getHeuristic());
                }
            }
        }
        else {
            best = new NodeFantasma(Integer.MIN_VALUE);
            for(Node n : node.getNodes()){
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
