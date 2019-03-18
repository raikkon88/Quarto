package Quatro.Decorator;

import Quatro.Utilities.Heuristic;

/**
 * @Author : Marc Sànchez Pifarré
 * Udg Code : u1939705
 * Classe NodeFantasma
 * ------------------------------
 * Invariant this == Integer.MIN_VALUE || this == Integer.MAX_VALUE
 * ------------------------------
 * S'utilitza com a node centinella per poder inicialitzar els valors d'alfa i beta.
 * Hereda de Node per poder ser comparat.
 */
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
