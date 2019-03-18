package MarcSanchez;



import Quatro.Tauler;

import java.util.Set;

public class Player1 extends Player {


    protected Node tree;

    public Player1(Tauler entrada) {
        super(entrada);
    }


    public int[] tirada(int colorin, int formain, int foratin, int tamanyin) {

        Set<Piece> toPlay = Piece.generateNPieces(16);
        Set<Position> free = Position.generateNPositions(4, 4);

        Piece in = new Piece(colorin, formain, foratin, tamanyin);
        toPlay.remove(in);

        // TODO : Estic proposant peces repetides... k'estic liant a algun lloc i no sé veure on...
        Position p = null;
        boolean first = true;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                int value = meutaulell.getpos(i, j);
                if(value != -1){
                    free.remove(new Position(i, j));
                    if(first) {
                        this.tree = new TaulerInicial(new Piece(value));
                        first = false;
                    }
                    else{
                        tree = new CombinationDecorator(tree, new Position(p), new Piece(value));
                    }
                    toPlay.remove(new Piece(value));
                    p = new Position(i,j);
                }
            }
        }
        if(tree == null){
            tree = new TaulerInicial(in);
        }
        else{
            tree = new CombinationDecorator(tree, p, in);
        }
        tree.setPieces(toPlay);
        tree.setFreePositions(free);

        tree = new AlphaBeta().alphaBeta(tree, calculateStep(tree.getLevel()), new NodeFantasma(Integer.MIN_VALUE), new NodeFantasma(Integer.MAX_VALUE));

        System.out.println("-----------------------------------------------");
        System.out.println("MARC -> " + tree);
        System.out.println("Heurístic : " + tree.getHeuristic());
        System.out.println("Posició : " + tree.getResult()[0] + " , " + tree.getResult()[1]);
        System.out.println("Peça que entrego : " + tree.piece);
        System.out.println("-----------------------------------------------");

        return tree.getResult();
    }






}
