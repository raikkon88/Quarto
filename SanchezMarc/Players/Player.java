package Quatro.Players;

import Quatro.Algorithms.AlphaBeta;
import Quatro.Decorator.CombinationDecorator;
import Quatro.Decorator.Node;
import Quatro.Decorator.NodeFantasma;
import Quatro.Decorator.TaulerInicial;
import Quatro.Utilities.Piece;
import Quatro.Utilities.Position;
import Quatro.Tauler;

import java.util.Iterator;
import java.util.Set;

/**
 * @Author : Marc Sànchez Pifarré
 * Udg Code : u1939705
 * Classe Player
 * ------------------------------
 * Classe abstracte que permet la interacció amb el taulell proporcionat.
 *
 * TODO : Es planteja com a classe abstracte per poder realitzar el patró template i poder templatitzar les jugades per tots els players
 * No s'ha complert per falta de temps, ens hem centrat en l'algoritme.
 */
public abstract class Player {

    protected Tauler meutaulell;
    protected Node tree;

    public Player(Tauler entrada){
        meutaulell = entrada;
    }

    /**
     * Mètode que estipula la profunditat a navegar per els algoritmes min i max i alfa beta donat un step (pas o torn de joc)
     * @param level torn actual. (de 1 a 16)
     * @return La profunditat del nivell que s'ha d'explorar
     */
    protected abstract int calculateStep(int level);


    /**
     * Mètode de tirada genèric tant per el jugador 1 com per el jugador 2
     * @param colorin Color de la peça
     * @param formain forma de la peça
     * @param foratin forat o no a la peça
     * @param tamanyin tamany de la peça
     * @return La posició on situa la peça formada per {colorin, formain, foratin, tamanyin} i la peça nova que escull per el contrincant
     */
    public int[] tirada(int colorin, int formain, int foratin, int tamanyin) {

        /** A cada torn genero totes les posicions i totes les peces. */
        Set<Piece> toPlay = Piece.generateNPieces(16);
        Set<Position> free = Position.generateNPositions(4, 4);

        // Trec la peça que em donen com entrada del set de peces que acabo de generar
        Piece in = new Piece(colorin, formain, foratin, tamanyin);
        toPlay.remove(in);

        /** Cerco per el tauler per saber l'estat en el que està.
         * No m'importa l'ordre en el que s'hagin jugat les peces anteriorment el que m'importa és com està el tauler en aquest moment.
         * Per aquest motiu considero que tornar a generar el node decorat amb el tauler inicial és la manera menys costosa i més eficient de desar els nodes.
         * No deso el tauler a cada node sinó que a cada node hi deso la modificació.
         */
        Position p = null;
        boolean first = true;
        // Per totes les files
        for(int i = 0; i < 4; i++){
            // per totes les columnes
            for(int j = 0; j < 4; j++){
                int value = meutaulell.getpos(i, j);
                if(value != -1){
                    // SI la posició que estic observant està ocupada.
                    free.remove(new Position(i, j)); // Extrec la posició de les pendents
                    if(first) { // Si és el primer cop que instancio l'arbre significa que sóc el player 2 ja que hi ha peces al tauler.
                        // També genero el tauler inicial com a primer node a decorar i li passo la primera peça llegida.
                        // Recordar que quan començo la partida la peça inicial no té posició fins al torn del player que decideix on posar-la
                        // Escenifiquem el que es comenta generant un tauler buit sense posició amb una peça
                        this.tree = new TaulerInicial(new Piece(value));
                        first = false; // Ja no és la primera lectura
                    }
                    else{
                        // En cas que ja s'hagi inicialitzart el tauler, es decora amb la posició anterior i la peça nova
                        tree = new CombinationDecorator(tree, new Position(p), new Piece(value));
                    }
                    // Es treu la peça de les visitades
                    toPlay.remove(new Piece(value));
                    // Es genera la posició per que sigui inserida amb la següent peça
                    p = new Position(i,j);
                }
            }
        }
        // Si No he trobat cap peça posada significa que el tauler és buit i que estic realitzant la primera tirada de totes
        if(tree == null){
            // Instancio un node que serà decorat
            tree = new TaulerInicial(in);
        }
        else{
            // En aquest no és la primera vegada que es tira i haig de generar una peça decorada amb la última posició escollida i la peça que he rebut.
            tree = new CombinationDecorator(tree, p, in);
        }

        // --------------------------------------------------------------------
        // EN AQUEST PUNT TINC LA PARTIDA GENERADA FINS A L'ESTAT QUE TOCA.
        // --------------------------------------------------------------------
        // tree ara mateix és un node decorat que conté com a nucli el tauler inicial amb una peça
        // i que està decorat amb totes les peces que s'ha anat trobant i les posicions. (S'ha decorat amb les jugades)


        // Assigno les peces que queden per jugar i les posicions que queden per ocupar al node arrel del que parteixo.
        tree.setPieces(toPlay);
        tree.setFreePositions(free);

        // Es realitza un petit càlcul en funció de si és un player 1 el que tira o si és un player 2, en cas que sigui el player 2 se li ha de donar el sentit contrari a l'arbre de cerca
        // Quan dic el sentit contrari significa que els nodes min han de ser max i els nodes max han de ser min.
        // Aixó passa per que per defecte el node Tauler inicial s'instancia com a max. I els decoradors son els !max dels decorats.
        int levelToGo = 0;
        // Comprovem si estic com a player 1 o com a player 2.
        if(free.size() % 2 == 1){ // Nombre imparell de peces al tauler estic com a player 2
            // S'ha de girar el max / min dels nodes de l'arbre.
            tree.alterOrder();
            levelToGo = calculateStep(tree.getLevel());
            // Mirem si és l'últim moviment (en cas que sigui jugador 2) ja que s'ha de retornar una peça que no existeixi per que el tauler respongui correctament
            if(toPlay.size() == 0){
                if(free.size() > 1){ // Aquesta situació no es pot donar
                    System.out.println("it can't be possible... ");
                }
                else{
                    // Recullo la última posició i l'assigno per retornar juntament amb la peça 2,2,2,2
                    Iterator<Position> position = free.iterator();
                    Position lastPosition = position.next();
                    return new int[]{lastPosition.x(), lastPosition.y(), 2,2,2,2};
                }
            }
        }
        else {
            // Calculo el nivell per el jugador 1
            levelToGo = calculateStep(tree.getLevel());
        }

        // Realitzo la cerca
        tree = new AlphaBeta().alphaBeta(tree, levelToGo, new NodeFantasma(Integer.MIN_VALUE), new NodeFantasma(Integer.MAX_VALUE));

        /** Descomentar per veure una mica d'informació extra per debug
        System.out.println("-----------------------------------------------");
        System.out.println("MARC -> " + tree);
        System.out.println("Heurístic : " + tree.getHeuristic());
        System.out.println("Posició : " + tree.getResult()[0] + " , " + tree.getResult()[1]);
        System.out.println("Peça que entrego : " + tree.piece);
        System.out.println("-----------------------------------------------");
        */
        return tree.getResult();
    }

}
