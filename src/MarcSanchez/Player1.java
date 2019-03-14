package MarcSanchez;



import Quatro.Tauler;

import java.util.HashSet;
import java.util.Set;

public class Player1 extends Player {


    protected Node tree;

    public Player1(Tauler entrada) {
        super(entrada);
    }


    public int[] tirada(int colorin, int formain, int foratin, int tamanyin) {

        Set<Piece> toPlay = Piece.generateNPieces(16);
        Set<Position> free = new HashSet<>();

        Piece in = new Piece(colorin, formain, foratin, tamanyin);
        toPlay.remove(in);

        tree = new TaulerInicial(in);

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                int value = meutaulell.getpos(i, j);
                if(value != -1){
                    tree = new CombinationDecorator(tree, new Position(i,j), new Piece(value));
                    toPlay.remove(new Piece(value));
                }
                else {
                    free.add(new Position(i,j));
                }
            }
        }
        tree.setPiece(in);
        tree.setPieces(toPlay);
        tree.setFreePositions(free);

        tree = new AlphaBeta().alphaBeta(tree, calculateStep(tree.getLevel()), new NodeFantasma(Integer.MIN_VALUE), new NodeFantasma(Integer.MAX_VALUE));

        System.out.println("----------------------");
        System.out.println("MARC -> " + tree);
        System.out.println("Heurístic : " + tree.heuristic);
        System.out.println("Posició : " + tree.getResult()[0] + " , " + tree.getResult()[1]);
        System.out.println("Peça que entrego : " + tree.piece);

        return tree.getResult();
    }






}
