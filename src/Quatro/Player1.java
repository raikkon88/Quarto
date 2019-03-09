package Quatro;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;



import java.util.*;

/**
 * @author Usuari
 */
public class Player1 extends Player{

    Player1(Tauler entrada){
        super(entrada);
        System.out.println("------------------------");
        System.out.println("NEW GAME");
        System.out.println("------------------------");
    }

    public int[] tirada(int colorin, int formain, int foratin, int tamanyin) {
        //colorin - Color de la peça a colocar  -> 	0 = Blanc 	1 = Negre
        //formain - Forma de la peça a colocar  -> 	0 = Rodona 	1 = Quadrat
        //foratin - Forat de la peça a colocar  -> 	0 = No  	1 = Si
        //tamanyin - Forat de la peça a colocar ->  0 = Petit 	1 = Gran


        if(tree == null) {
            tree = new Node(0, true, new int[]{colorin, formain, foratin, tamanyin}, random, this.pecesGenerades, this.positions);
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    int val = meutaulell.getpos(i, j);
                    tree.setValorTauler(i,j,val);
                    if(val != -1){
                        step++;
                    }
                }
            }
        }
        else{
            // Haig de mirar on ha posat la peça que li he entregat anteriorment.
            int i = 0;
            boolean trobat = false;
            while(!trobat && i < 16){
                trobat = ultimaJugada == meutaulell.getpos(i/4, i%4);
                i++;
            }
            i--;

            // Aquí tinc la posició on va la peça que ha posat l'altre. Actualitzo l'arbre meu
            tree = new Node(tree, i/4, i%4, new int[]{colorin, formain, foratin, tamanyin}, true);
            step++;
        }

        Collections.shuffle(tree.positions);


        //tree = new MinIMax().minIMax(tree, calculateStep(step));
        tree =new AlphaBeta().alphaBeta(tree, calculateStep(step), new Node(Integer.MIN_VALUE), new Node(Integer.MAX_VALUE));
        String pos = "[" + tree.combination[0] + "," + tree.combination[1] + "]";
        String piece = "[" + tree.combination[2] + "," + tree.combination[3] + "," + tree.combination[4] + "," + tree.combination[5] + "]";
        System.out.println("Heurístic        : " + tree.getHeuristic() + " -> " + step + " -> " + pos + " -> " + piece);
        ultimaJugada = tree.combination[2]*1000 + tree.combination[3] *100 + tree.combination[4] * 10 + tree.combination[5];
        step++;
        return tree.combination;
    }

    @Override
    protected int calculateStep(int level){
        if(level >= 0 || level <= 2){
            return 3;
        }
        return 3;
        /*
        else if(level > 2 && level < 6){
            return 10;
        }
        else if(level > 5 && level < 10){
            return 7;
        }
        return 9;*/
    }

}