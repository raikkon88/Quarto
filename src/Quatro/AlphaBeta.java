package Quatro;




/**
 * Author : Marc Sànchez Pifarré
 * Udg Code : u1939705
 * Classe AlphaBeta
 * ------------------------------
 * // TODO : Aplicar patró strategy
 * S'empra per executar l'algoritme Alpha Beta sobre un node en concret.
 */
public class AlphaBeta {



    public AlphaBeta(){}

    /**
     * Mira si ja s'ha explorat prou profunditat.
     * @param level
     * @return
     */
    private boolean enoughtDeep(int level){
        return level == 0;
    }

    /**
     * Alogitme Alpha Beta -> Versió recursiva amb **poda heurística**
     * ---------------------------------------------------------------------------------
     * S'ha adaptat l'algoritme per que en comptes de tornar el valor enter de l'heurístic es retorni el node que passarà ser el node escollit.
     * Per fer-ho s'ha modificat el tipus de retorn (de int a Node) i s'ha canviat la lògica d'assignació dels nodes.
     * Alfa i Beta son nodes fantasma que s'instancien per aconseguir tenir el valor MAX i MIN.
     *
     * @param node Node sobre el que s'executarà l'algoritme (arrel del nivell actual)
     * @param deep Nombre de nivells que li queden per baixar (crides recursives màx)
     * @param alfa Valor per llindar Alfa
     * @param beta Valor per llindar Beta
     * @param beta Valor per llindar Beta
     * @return El Node seleccionat segons la cerca heurística,.
     */
    public Node alphaBeta(Node node, int deep, Node alfa, Node beta){
        // Node finalValue;
        if(enoughtDeep(deep)) {
            return node;
        }

        if(!node.isLeaf())
            node.generate();

        // Aquí es realitza Poda Heurística!
        if(node.isEmpty()) {
            return node;
        }

        if(!node.isMax()){
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
            for(Node n : node.nodes){
                if (alfa.getHeuristic() >= beta.getHeuristic()) break;
                Node value = alphaBeta(n, deep - 1, alfa, beta);
                if( value.getHeuristic() > alfa.getHeuristic()) {
                    alfa = n;
                    n.setHeuristic(value.getHeuristic());
                }
            }
            return alfa;
        }
    }

}
