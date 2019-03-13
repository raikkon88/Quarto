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

        if(isMyFirstStep()) {
            tree = new Node(0, new Piece(colorin, formain, foratin, tamanyin), meutaulell);
        }
        else{
            tree = new Node(tree, new Piece(colorin, formain, foratin, tamanyin), meutaulell);
        }
        //tree = new MinIMax().minIMax(tree, calculateStep(tree.level));
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

        if (level < 3) {
            return 2;
        } else if(level < 5) {
            return 2;
        }
        else if(level < 7){
            return 2;
        }
        else if(level < 9){
            return 5;
        }
        else{
            return 7;
        }
    }

}


/*
        boolean youDie = tree.checkYouWin();
        if(youDie){

            Position killerPosition = new Position(0,0);

            // BUsca la posició on cascar l'altre i palante.
            int value = tree.getWillEnd();
            int pos = 0;
            boolean end = false;
             // És una fila
            while(pos < 4 && !end){
                if(value < 4) {
                    end = tree.board[value][pos] == -1;
                    killerPosition = new Position(value, pos);
                }
                else if(value < 8) {// És una columna
                    end = tree.board[pos][value] == -1;
                    killerPosition = new Position(pos, value);
                }
                else if(value == 8){ // és la primera diagonal
                    end = tree.board[pos][pos] == -1;
                    killerPosition = new Position(pos, pos);
                }
                else{ // És la segona diagonal.
                    end = tree.board[pos][3-pos] == -1;
                    killerPosition = new Position(pos, 3 - pos);
                }
                pos++;
            }
            if(!end) {
                System.out.println("noi.. no saps programar...");
            }
            Iterator<Piece> piece = tree.toPlay.iterator();

            Combination killerCombination = new Combination(killerPosition, piece.next());
            return killerCombination.toIntArray();
        }
        else{*/