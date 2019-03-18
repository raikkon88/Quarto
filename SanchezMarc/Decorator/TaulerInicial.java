package Quatro.Decorator;

import Quatro.Utilities.Heuristic;
import Quatro.Utilities.Piece;

import java.util.HashSet;

/**
 * @Author : Marc Sànchez Pifarré
 * Udg Code : u1939705
 * Classe TaulerInicial
 * ------------------------------
 * Representa un tauler sense cap peça
 *
 * Es tracta com un node per poder realitzar el patró decorator i poder decorar-lo amb combinacions de peça + posició.
 */
public class TaulerInicial extends Node{

    // Fa referència a la primera peça que reb el jugador que comença a tirar.
    Piece firstPiece;

    /**
     * Constructor
     * @param piece Primera peça entregada al primer jugador a jugar-la
     */
    public TaulerInicial(Piece piece){
        this.firstPiece = piece;
        this.level = 0;
        this.nodes = new HashSet<>();
        this.max = true;
    }

    @Override
    public void alterOrder() {
        this.max = !this.max;
    }

    @Override
    public int getHeuristic() {
        return 0;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    protected Heuristic ieval() {
        return new Heuristic(this.firstPiece);
    }


    @Override
    public int[] getResult() {
        return new int[0];
    }
}
