package Quatro;

/**
 * Author : Marc Sànchez Pifarré
 * Udg Code : u1939705
 * Classe Node
 * ------------------------------
 * TODO : Aplicar un patró template.
 */
public abstract class Player {

    protected Tauler meutaulell;
    protected Node tree;
    // S'utilitza per tenir un control de l'última peça que ha posat el contrincant (la deso perquè li he donat jo).

    public Player(Tauler entrada){
        meutaulell = entrada;
    }

    protected abstract int calculateStep(int level);

}
