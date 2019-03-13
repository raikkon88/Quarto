package Quatro;


import java.util.*;

public class Node {

    Heuristic heuristic; // Valor de l'heurístic
    int level; // Nivell al que pertany el node.
    Combination combination; // La combinació a retornar de posició on pos la peça i la següent a peça a entregar
    boolean max; // Aquest node és un node maximitzat o minimitzat
    List<Node> nodes; // Fills
    List<Piece> toPlay; // Peces que queden per jugar
    List<Position> free; // Posicions lliures que queden al tauler
    boolean isFinal; // Aquest node és guanyador


    /** Constructor per nodes fantasma que només han de desar el valor de l'heurístic.
     * SERVEIX DE COMPARADOR
     * @param heuristic, el valor per defecte que obtindrà aquest node.
     *                   Només pot ser :
     *                   - Integer.MAX_VALUE
     *                   - Integer.MIN_VALUE
     */
    public Node(int heuristic){
        this.heuristic = new Heuristic(heuristic);
    }

    /**
     * Constructor bàsic, es construeix un fill a partir d'un pare
     * @param node Node pare amb el que construïr les propietats bàsiques del fill.
     */
    public Node(Node node){
        this.level = node.level + 1;
        this.max = !node.max;
        this.heuristic = new Heuristic(this, node.heuristic);
        this.toPlay = new ArrayList<>();
        this.free = new ArrayList<>();
        this.nodes = new ArrayList<>();
    }

    /**
     * Constructor inicial (Serveix per inicialitzar el primer node arrel)
     * @param level Nivell que tenim jugat de l'arbre
     * @param piece Peça que ens ha aportat el contrincant
     */
    public Node(int level, Piece piece, Tauler meutaulell){

        // Aquest constructor només serà cridat per nivell == 0 o nivell == 1
        this.level = level;
        this.max = true;
        this.heuristic = new Heuristic(0);
        this.combination = new Combination(piece);
        // Genero el set de posicions (S'emplena a cada node).
        this.free = new ArrayList<>();
        // Genero les peces i de passada trec la que s'ha jugat en aquest moment.
        this.toPlay = Piece.generateNPieces(16);
        this.toPlay.remove(piece);
        // INstancio l'objecte Heurístic que aniré alimentant a mesura que vagi adelantant a l'arbre per saber si és node final o no i per aplicar la poda heurística.
        //this.heuristic = new Heuristic(this);
        updateBoard(meutaulell);
        this.nodes = new ArrayList<>();
    }

    /**
     * Constructor per la jugada de l'oponent
     * - El tauler s'instancia en un altre costat. (cosa que potser estaria bé que el rebés aquí)
     * @param node Node del que partim per generar l'actual
     * @param forHim La peça que ens ha entregat l'oponent per situar.
     */
    public Node(Node node, Piece forHim, Tauler meutaulell){
        this(node);
        this.combination = new Combination(forHim);
        this.heuristic = new Heuristic(this, node.heuristic);

        for(Piece piece : node.toPlay){
            this.toPlay.add(new Piece(piece));
        }

        updateBoard(meutaulell);
    }

    /**
     * Constructor que serveix per generar un node fill.
     * @param node Node pare del que es parteix per la generació.
     * @param son Combinació del fill que es genera.
     */
    public Node(Node node, Combination son) {
        this(node);
        this.combination = son;

        this.isFinal = this.heuristic.updateMatrix(this.combination.x(), this.combination.y(), node.combination.getInt());

        // En aquest punt cada node té un heurístic i el que hai de fer és afegir la combinació actual al mateix heurístic i treure les peces i la posició que ocupa.
        for(Piece p : node.toPlay){
            if(p.getInt() != node.combination.getInt()) {
                this.toPlay.add(new Piece(p));
            }
        }

        for(Position pos : node.free){
            if(!(pos.x() == son.x() && pos.y() == son.y())){
                this.free.add(new Position(pos));
            }
        }
    }


    /**
     * Actualitza les variables, tauler, free i toPlay amb la info del nou tauler.
     * @param meutaulell Tauler que es reb de la part principal del joc.
     */
    private void updateBoard(Tauler meutaulell){

        Set<Piece> tmp = new HashSet<>();
        for(int f = 0; f < meutaulell.getX(); f++){
            for(int c = 0; c < meutaulell.getY(); c++){
                int value = meutaulell.getpos(f, c);
                if(value == -1){
                    this.free.add(new Position(f, c));
                }
                else{
                    isFinal = this.heuristic.add(value, f, c) || isFinal;
                    Piece toGenerate = new Piece(value);
                    if(toGenerate.getInt() != this.combination.getInt())
                        tmp.add(toGenerate);
                }
            }
        }
        this.toPlay.removeAll(tmp);
    }

    public void generate(){
        for(Piece piece : toPlay){
            if(piece.getInt() != this.combination.getInt() || free.size() == 1){
                for(Position p : free){
                    nodes.add(new Node(this, new Combination(p, piece)));
                }
            }
        }
    }

    public Combination getCombination(){
        return this.combination;
    }

    public boolean isLeaf(){
        return isFinal;
    }

    public int getHeuristic(){
        return this.heuristic.getValue();
    }

    public void shuffle(){
        Collections.shuffle(this.toPlay);
        Collections.shuffle(this.free);
    }

    public boolean isMin(){
        return !max;
    }

    public boolean isEmpty(){
        return this.nodes.isEmpty();
    }

    public void setHeuristic(int value){
        this.heuristic = new Heuristic(value);
    }

    @Override
    public String toString() {
        return this.combination.toString() + " -> " + this.heuristic;
    }
}

