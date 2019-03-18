package Quatro.Decorator;


import Quatro.Utilities.Heuristic;
import Quatro.Utilities.Piece;
import Quatro.Utilities.Position;

import java.util.HashSet;

/**
 * @Author : Marc Sànchez Pifarré
 * Udg Code : u1939705
 * Classe CombinationDecorator
 * ------------------------------
 * Classe que representa la unió de les accions que es realitzen en un torn quan en tauler té com a mínim una peça assignada.
 * Complint amb el patró decorator :
 * - Combination -> Fa referència a la combinació de la posició on s'ha posat la peça rebuda i la peça que es retornarà.
 * El tauler comença buit i es decora amb peces.
 */
public class CombinationDecorator extends Decorator {


    // S'utilitza per el càlcul de l'heurístic.
    protected Heuristic heuristic;

    /** Constructor
     * @param node Que serà decorat (representa la jugada anterior).
     * @param pos Que se li assigna al node decorador
     * @param piece Que se li assigna al node decorador.
     */
    public CombinationDecorator(Node node, Position pos, Piece piece){
        this.decorated = node;
        this.position = new Position(pos);
        this.piece = new Piece(piece);
        this.level = node.level + 1;
        this.heuristic = null;
        this.nodes = new HashSet<>();
        this.max = !node.max;
    }


    @Override
    public boolean isMax() {
        return this.max;
    }

    @Override
    public void alterOrder() {
        decorated.alterOrder();
        this.max = !this.max;
    }

    /**
     * Executa el càlcul recursiu del valor de l'heurístic donat un node.
     *
     * Viatja a través de tots els nodes decorats.
     */
    public void evalHeuristic() {
        this.heuristic = decorated.ieval();
        heuristic.add(this.position.x(), this.position.y(), heuristic.getPiece());
        heuristic.setNextPiece(new Piece(this.piece));
    }

    @Override
    protected Heuristic ieval(){
        Heuristic h = decorated.ieval();
        h.add(this.position.x(), this.position.y(), h.getPiece());
        h.setNextPiece(new Piece(this.piece));
        return h;
    }

    @Override
    public int getHeuristic() {
        if(this.heuristic == null){
            evalHeuristic();
        }
        if(this.heuristicValue == 0) {
            if (this.heuristic.isFinished()) {
                this.heuristicValue = Heuristic.HEURISTIC_MAX * (16 - level);
            } else {
                this.heuristicValue = this.heuristic.getValue();
            }
            if (this.max) {
                this.heuristicValue *= -1;
            }
        }
        return this.heuristicValue;
    }

    @Override
    public boolean isLeaf() {
        if(this.heuristic == null)
            evalHeuristic();
        return this.heuristic.isFinished();
    }


    @Override
    public int[] getResult() {
        int value = this.piece.getInt();
        int size = value % 2; value /= 10;
        int hole = value % 2; value /= 10;
        int form = value % 2; value /= 10;
        int color = value % 2;
        return new int[]{this.position.x(), this.position.y(), color, form, hole, size};
    }



}
