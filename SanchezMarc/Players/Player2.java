package Quatro.Players;

import Quatro.Tauler;

/**
 * @Author : Marc Sànchez Pifarré
 * Udg Code : u1939705
 * Classe Player2
 * ------------------------------
 * Representació en objecte del jugador 2.
 *
 * Es diferencia del jugador 1 degut a la profunditat d'exploració que pot abarcar.
 */
public class Player2 extends Player {

    public Player2(Tauler entrada) {
        super(entrada);
    }

    @Override
    protected int calculateStep(int level) {
        if(level >= 0 && level < 2){
            return 2;
        }
        else if(level >= 2 && level < 4){
            return 3;
        }
        else if(level >= 4  && level < 6){
            return 4;
        }
        else if(level >= 6 && level < 8){
            return 5;
        }
        else {
            return 10;
        }
    }

}
