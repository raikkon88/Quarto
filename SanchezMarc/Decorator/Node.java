package Quatro.Decorator;

import Quatro.Utilities.Heuristic;
import Quatro.Utilities.Piece;
import Quatro.Utilities.Position;

import java.util.HashSet;
import java.util.Set;


/**
 * @Author : Marc Sànchez Pifarré
 * Udg Code : u1939705
 * Classe Node
 * ------------------------------
 * Un node és part d'una estructura que té forma d'arbre. En aquest cas els nodes son estats que desen tota la informació referent a una jugada.
 *
 *  Herència :
 *
 *  - CombinationDecorator
 *  - TaulerInicial
 */
public abstract class Node {

    /**
     * Atributs comuns entre CombinationDecorator i TaulerInicial.
     */
    protected int level;
    protected Set<Node> nodes;
    protected Piece piece;
    protected Position position;
    protected Set<Piece> toPlay;
    protected Set<Position> free;
    protected boolean max;
    protected int heuristicValue;

    /**
     * Setter de la peça.
     * @param piece Referpencia a una peça existent.
     */
    public void setPiece(Piece piece){this.piece = piece;}

    /**
     * Getter del nivell
     * @return Retorna el nivell del node actual.
     * 0 -> si és TaulerInicial
     * >0 -> Altrament
     */
    public int getLevel(){ return level;}

    public Set<Node> getNodes(){
        return this.nodes;
    }

    /**
     * Contesta a la pregunta de si un node és un node maximitzador o minimitzador.
     * @return cert si és maximitzador, fals altrament.
     */
    public boolean isMax(){return max;}

    /**
     * Setter de l'heurístic. (L'heurístic es representa mitjançant un nombre enter positiu o negatiu)
     * @param heuristic Nombre enter positiu o negatiu.
     */
    public void setHeuristic(int heuristic){ this.heuristicValue= heuristic;}

    /**
     * Comprova si un node té nodes fill
     * @return cert si no té nodes fill, falç altrament.
     */
    public boolean isEmpty(){
        return nodes.isEmpty();
    }

    /**
     * Permet assignar una série de peces al node.
     * @param piecesToPlay
     */
    public void setPieces(Set<Piece> piecesToPlay){
        this.toPlay = piecesToPlay;
    }

    /**
     * Permet assignar una série de posicions al node.
     * @param freePositions
     */
    public void setFreePositions(Set<Position> freePositions){
        this.free = freePositions;
    }

    /**
     * Itera sobre les peces i les posicions que té assignades de tal manera que permet combinar-les i generar tots els nodes fill.
     */
    public void generate(){
        // Combino les peces amb les posicions.
        for(Piece p : toPlay){
            for(Position pos : free){
                Node n = new CombinationDecorator(this, new Position(pos), new Piece(p));
                nodes.add(n);
                HashSet<Position> tmpPos = new HashSet<>(free);
                HashSet<Piece> tmpPiece  = new HashSet<>(toPlay);
                tmpPos.remove(pos);
                tmpPiece.remove(p);
                n.setFreePositions(tmpPos);
                n.setPieces(tmpPiece);
            }
        }
    }

    /**
     * @return La representació d'un node en una cadena de caràcters.
     */
    @Override
    public String toString() {
        return this.position + " - P > " + this.piece + " - H > " + this.heuristicValue + " - N > " + this.level + " - M > " + this.isMax();
    }

    /**
     * Es cridarà a aquesta funció per donar per finalitzat el torn.
     * @return El valor del resultat a aplicar segons la jugada escollida.
     */
    public abstract int[] getResult();

    /**
     * Gira l'ordre de creació dels nodes (Max i min).
     * - S'empra per la lògica del Player2, on quan es genera el tauler ha de ser des de un node min per que al torn que li toca tirar sigui un node max.
     * Gira recursivament tots els max dels nodes decorats i del node actual.
     */
    public abstract void alterOrder();


    /**
     * Getter del valor enter de l'heurístic.
     * @return El valor enter que quantifica quan de bona és la jugada en vers a tot l'arbre que ja portem jugat.
     */
    public abstract int getHeuristic();

    /**
     * S'utilitza per la PODA HEURÍSTICA.
     * @return cert si el node en qüestió es pot considerar una fulla per alguna de les següents raons :
     * - Es compleix que una de les proipietats de una de les files, columnes o diagonals és 4 i per tant no cal que segueixi mirant.
     */
    public abstract boolean isLeaf();

    /**
     * Funció recursiva que permet extreure l'objecte heurístic plé utilitzant tots els decorats.
     * @return El valor de l'heurístic donat un node (amb totes les jugades representades)
     */
    protected abstract Heuristic ieval();




}
