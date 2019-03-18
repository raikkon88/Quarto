package MarcSanchez;

import Quatro.Node;
import Quatro.Tauler;

/**
 * Author : Marc Sànchez Pifarré
 * Udg Code : u1939705
 * Classe Node
 * ------------------------------
 * TODO : Aplicar un patró template.
 */
public class Player {

    protected Tauler meutaulell;
    protected Node tree;
    // S'utilitza per tenir un control de l'última peça que ha posat el contrincant (la deso perquè li he donat jo).

    public Player(Tauler entrada){
        meutaulell = entrada;
    }

    protected int calculateStep(int level){
        if(level >= 0 && level < 2){
            return 1;
        }
        else if(level >= 2 && level < 4){
            return 2;
        }
        else if(level >= 4  && level < 8){
            return 4;
        }
        else if(level >= 8 && level < 10){
            return 16;
        }
        else
            return 8;

    }
}
