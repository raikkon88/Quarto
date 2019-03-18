package Quatro;



import java.util.Iterator;
import java.util.Set;

public class Player1 extends Player {


    protected Node tree;

    public Player1(Tauler entrada) {
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
            return 3;
        }
        else if(level >= 6 && level < 8){
            return 4;
        }
        else {
            return 10;
        }
    }


    public int[] tirada(int colorin, int formain, int foratin, int tamanyin) {

        Set<Piece> toPlay = Piece.generateNPieces(16);
        Set<Position> free = Position.generateNPositions(4, 4);

        Piece in = new Piece(colorin, formain, foratin, tamanyin);
        toPlay.remove(in);

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

        int levelToGo = 0;
        // Comprovem si estic com a player 1 o com a player 2.
        if(free.size() % 2 == 1){ // Nombre imparell de peces al tauler estic com a player 2
            // S'ha de girar el max / min dels nodes de l'arbre.
            tree.alterOrder();
            levelToGo = calculateStep(tree.getLevel());
        }
        else {
            levelToGo = calculateStep(tree.getLevel());
        }

        if(toPlay.size() == 0){
            if(free.size() > 1){
                System.out.println("it can't be possible... ");
            }
            else{
                Iterator<Position> position = free.iterator();
                Position lastPosition = position.next();
                return new int[]{lastPosition.x(), lastPosition.y(), 2,2,2,2};
            }
        }
        else{
            tree = new AlphaBeta().alphaBeta(tree, levelToGo, new NodeFantasma(Integer.MIN_VALUE), new NodeFantasma(Integer.MAX_VALUE));
        }

        System.out.println("-----------------------------------------------");
        System.out.println("MARC -> " + tree);
        System.out.println("Heurístic : " + tree.getHeuristic());
        System.out.println("Posició : " + tree.getResult()[0] + " , " + tree.getResult()[1]);
        System.out.println("Peça que entrego : " + tree.piece);
        System.out.println("-----------------------------------------------");

        return tree.getResult();
    }






}
