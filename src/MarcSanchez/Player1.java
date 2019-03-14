package MarcSanchez;



import Quatro.Tauler;

import java.util.HashSet;
import java.util.Set;

public class Player1 extends Player {


    protected Node tree;

    public int[] tirada(int colorin, int formain, int foratin, int tamanyin) {

        Set<Piece> toPlay = Piece.generateNPieces(16);
        Set<Position> free = new HashSet<>();

        Piece in = new Piece(colorin, formain, foratin, tamanyin);

        tree = new TaulerInicial(in);

        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                int value = meutaulell.getpos(i, j);
                if(value != -1){
                    free.add(new Position(i,j));
                    tree = new CombinationDecorator(tree, new Position(i, j));
                    toPlay.remove(new Piece(value));
                    tree.setPiece(new Piece(value));
                }
            }
        }

        tree = new AlphaBeta().alphaBeta(tree, calculateStep(tree.getLevel()), new NodeFantasma(Integer.MIN_VALUE), new NodeFantasma(Integer.MAX_VALUE));

        return new int[]{0,0,0,0,0,0};
    }

    public Player1(Tauler entrada) {
        super(entrada);
    }





}
