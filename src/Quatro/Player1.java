package Quatro;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;



import java.util.*;

/**
 * @author Usuari
 */
public class Player1 extends Player{

    public Player1(Tauler entrada){
        super(entrada);
        System.out.println("------------------------");
        System.out.println("NEW GAME");
        System.out.println("------------------------");
    }

    int lastHeuristic;

    public int[] tirada(int colorin, int formain, int foratin, int tamanyin) {
        //colorin - Color de la peça a colocar  -> 	0 = Blanc 	1 = Negre
        //formain - Forma de la peça a colocar  -> 	0 = Rodona 	1 = Quadrat
        //foratin - Forat de la peça a colocar  -> 	0 = No  	1 = Si
        //tamanyin - Forat de la peça a colocar ->  0 = Petit 	1 = Gran

        // És el primer cop que tiro.
        if(isMyFirstStep()) {
            tree = new Node(0, new Piece(colorin, formain, foratin, tamanyin), meutaulell);
        }
        // Rebo la tirada de l'altre.
        else{
            tree = new Node(tree, new Piece(colorin, formain, foratin, tamanyin), meutaulell);
        }
        // Desordeno les peces i les posicions.
        tree.shuffle();

        //tree = new MinIMax().minIMax(tree, calculateStep(tree.level));
        // Faig cerca amb poda heurística. (Els nodes guanyadors no generen de més.)
        tree = new AlphaBeta().alphaBeta(tree, calculateStep(tree.level), new Node(Integer.MIN_VALUE), new Node(Integer.MAX_VALUE));
        System.out.println("Heurístic step " + tree.level + " : " + tree.getHeuristic() + " -> " + tree);
        lastHeuristic = tree.getHeuristic();
        ultimaJugada = tree.getCombination().getInt();
        if(tree.level > 14){
            return new int[]{tree.getCombination().x(), tree.getCombination().y(), 1, 1 ,1 ,2};
        }
        else{
            return tree.getCombination().toIntArray();
        }
    }


    private boolean isMyFirstStep(){
        return tree == null;
    }

    @Override
    protected int calculateStep(int level) {
        if(level < 3)
            return 3;
        else if(level < 5){
            return 3;
        }
        else if(level < 7){
            return 4;
        }
        else if(level < 9){
            return 6;
        }
        else{
            return 7;
        }
    }

}

