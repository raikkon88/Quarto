package Quatro.Utilities;

/**
 * Author : Marc Sànchez Pifarré
 * Udg Code : u1939705
 * Classe Heuristic
 * ------------------------------
 * Objecte que permet el càlcul i l'emmegatzematge dels valors necessaris per l'heurístic.
 */
public class Heuristic {

    /**
     * Constants bàsiques... (Totalment innecessari)
     */
    private static final int HEU_ROWS = 10;
    public static final int HEURISTIC_MAX = 100000;

    /**
     * Atributs
     */
    HeuristicRow[] matrix; // Matriu de files heurístiques.
    int value; // Valor total enter de l'heurístic per un node.
    boolean finished; // Conté informació sobre si és un node fulla (hi ha 4 fitxes amb la mateixa propietat en una fila, columna o diagonal)
    Piece nextPiece; // Atribut que s'utilitza per el càlcul recursiu donat un node.
    boolean calculated; // Boleà que determina si un node ja ha estat calculat.

    /**
     * Constructor
     * @param value peça utilitzada per seguir amb el càlcul donat un node.
     */
    public Heuristic(Piece value){
        this.value = 0;
        this.nextPiece = new Piece(value);
        this.matrix = new HeuristicRow[HEU_ROWS];
        this.finished = false;
        this.calculated = false;
        for(int i = 0; i < HEU_ROWS; i++){
            matrix[i] = new HeuristicRow();
        }
    }

    /**
     * Retorna el valor per l'heurístic donat una matriu d'heurístic Rows.
     * @return Un enter que fa referència a la qualitat del node.
     */
    public int getValue(){
        if(!calculated){
            for(int i = 0; i < Heuristic.HEU_ROWS; i++){
                value +=  matrix[i].finalPoints;
            }
            calculated = true;
            return value;
        }
        return value;
    }


    /**
     * @return Retorna la peça que té dintre l'objecte Heuristic.
     */
    public Piece getPiece(){
        return this.nextPiece;
    }

    public void setNextPiece(Piece piece){
        this.nextPiece = piece;
    }

    /**
     * Afegeix una peça donades una fila i una columna a la matriu de càlcul heurístic.
     * @param row Fila escollida
     * @param col Columna escollida
     * @param piece Peça a situar a row i col
     */
    public void add(int row, int col, Piece piece){
        finished = matrix[row].addValue(piece.getProperties()) || finished;
        finished = matrix[4+col].addValue(piece.getProperties()) || finished;
        if(row == col) finished = matrix[8].addValue(piece.getProperties()) || finished;
        if(row == 3-col) finished = matrix[9].addValue(piece.getProperties()) || finished;
    }

    /**
     * Getter per saver si un node és fulla
     * @return Cert si és fulla, falç altrament
     */
    public boolean isFinished(){
        return finished;
    }

    /**
     * @return Representació en cadena de caràcters de l'objecte Heurístic.
     */
    @Override
    public String toString() {
        return getValue() + "  |  " + (isFinished() ? "Final" : "No Final");
    }

    /**
     * Author : Marc Sànchez Pifarré
     * Udg Code : u1939705
     * Classe HeuristicRow
     * ------------------------------
     * Classe aniuada que fa referència a la fila de la matriu de càlcul heurístic.
     * Simplement es realitza el comptatge del nombre de 0's, 1's, 2's i 4's corresponents a la instància.
     * Cada instància d'aquests objectes pot representar una fila, una columna o una diagonal.
     *
     * Les puntuacions estan representades més endavant.
     */
    public class HeuristicRow {

        /**
         * Atributs necessaris
         */
        int value;
        int finalPoints;
        int ones;
        int twos;
        int zeros;
        int fours;

        /**
         * Constructor per defecte (Tot a 0)
         */
        public HeuristicRow(){
            value = 0;
            ones = 0;
            twos = 0;
            zeros = 0;
            fours = 0;
            finalPoints = 0;
        }

        /**
         * La mare dels ous...
         *
         * Afegeix un valor a la fila, columna o diagonal.
         * @param properties Donada una peça, properties son les seves propietats representades en 8 dígits binaris (1 Byte)
         * @return cert si aquesta fila, columna o diagonal conté un nombre de 4's suficient com per poder determinar que el node és una fulla.
         *
         * Es realitza el comptatge de 0's, 1's, 2's i 4's suficient per al càlcul heurístic. No es compten els 3's!! (Son el dimoni).
         */
        public boolean addValue(int properties){
            // giving a value for each kind of number that can give.
            // If is a 0 -> 2 points.
            // If is a 1 -> 3 points
            // If is a 2 -> 5 points
            // If is a 4 -> 8 points
            // If is a 3 -> 0 points
            this.value += properties;
            int copy = value;
            while (copy > 0){
                int partial = copy % 5;
                if(partial == 1){
                    ones++;
                }
                else if(partial == 2){
                    twos++;
                }
                else if(partial == 0){
                    zeros++;
                }
                else if (partial == 4){
                    fours++;
                }
                else {
                    // Nothing to do, we don't want to evaluate the 3's.
                }
                copy /= 10;
            }
            finalPoints = zeros * 2 + ones * 3 + (8 * fours) + (5 * twos);
            return isFinal();
        }


        /**
         * @return Cert si la fila considera que tot el node pot ser una fulla, falç altrament.
         */
        public boolean isFinal(){
            return fours > 0;
        }

        /**
         * @return Representació de la HeurísticRow com una cadena de caràcters.
         */
        @Override
        public String toString() {
            return  "VALUE = " + value + " | 0s = " + zeros + " | 1s = " + ones + " | 2s = " + twos +
                    " | 4s = " + fours + " -----> (" + finalPoints + ")";
        }
    }
}
