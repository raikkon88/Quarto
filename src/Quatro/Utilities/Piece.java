package Quatro.Utilities;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author : Marc Sànchez Pifarré
 * Udg Code : u1939705
 * Classe Piece
 * ------------------------------
 * INVARIANT: this.piece == Combinacions de 0s i 1a entre 0000 i 1111
 * ------------------------------
 * Classe que representa una peça. La seva representació està dictada per l'enunciat del problema, s'ha decidit que és prou òptima com per poder reutilitzar-la
 *
 * TODO : Es podria canviar la representació de un enter de 0000 a 1111 amb combinatòria dels digits 0s i 1s per un enter de 0 a 16.
 * TODO : Els enters son binaris en java i les operacions es podrien definir com operacions binàries.
 * TODO : Es podria haver heredat directament de la classe Integer
 *
 * Ha de ser cloneable per què pot ser representada dins de HashSet's.
 */
public class Piece implements Cloneable{

    protected int piece;

    /**
     * Constructor a partir d'un vector de 4 enters
     * @param pieceValues pieceValues.size() == 4 && V val € pieceValues -> val == 0 || val == 1
     */
    public Piece(int [] pieceValues) {
        piece = pieceValues[0] * 1000+pieceValues[1]*100+ pieceValues[2]*10+pieceValues[3];
    }

    /**
     * Constructor còpia.
     * @param piece Peça des de la que es generarà la nova instància.
     */
    public Piece(Piece piece){
        this.piece = piece.piece;
    }

    /**
     * Constructor a partir d'un enter.
     * @param value count(value.digits) == 4 && V digit € value -> val == 0 || val == 1
     */
    public Piece(int value){
        piece = value;
    }

    /**
     * Constructor a partir de 4 enters.
     * @param color primer digit, 0 -> Blanc, 1 -> negre
     * @param forma segon digit, 0 -> rodona, 1 -> quadrada
     * @param forat tercer dogot, 0 -> sense forat, 1 -> amb Forat
     * @param tamany quart digit, 0 -> petita, 1 -> gran
     */
    public Piece(int color, int forma, int forat, int tamany){
        this(new int[]{color, forma, forat, tamany});
    }

    /**
     * @return Retorna el valor decimal referent a la peça
     */
    public int getInt(){
        return piece;
    }

    /**
     * Transforma el valor enter de la peça en un valor referent a les propietats de la peça
     * @return Un valor enter que correspon a :
     *         // 1r dígit -> blanca
     *         // 2n digit -> negra
     *         // 3n digit -> rodona
     *         // 4n digit -> quadrada
     *         // 5n digit -> sense forat
     *         // 6n digit -> amb forat
     *         // 7n digit -> petita
     *         // 8n digit -> gran
     */
    public int getProperties(){


        int tmp = piece;
        int tamany = tmp % 2; tmp /= 10;
        int forat  = tmp % 2; tmp /= 10;
        int forma  = tmp % 2; tmp /= 10;
        int color  = tmp % 2;
        int gran = tamany;
        int petita = tamany == 0 ? 1 : 0;
        int foradada = forat;
        int noForadada = forat == 0 ? 1 : 0;
        int quadrada = forma;
        int rodona = forma == 0 ? 1 : 0;
        int negre = color;
        int blanca = color == 0 ? 1 : 0;
        return  blanca * 10000000 +
                negre * 1000000 +
                rodona * 100000 +
                quadrada * 10000 +
                noForadada * 1000 +
                foradada * 100 +
                petita * 10 +
                gran;
    }


    /**
     * Mètode estàtic que permet la creació i generació de n peces (Mètode Factory)
     * @param n nombre de peces a generar.
     * @return les n peces generades.
     */
    public static Set<Piece> generateNPieces(int n){
        Set<Piece> pieces = new HashSet<>();
        for(int i = 0; i < n; i++){
            String value = Integer.toBinaryString(i);
            pieces.add(new Piece(Integer.valueOf(value)));
        }
        return pieces;
    }

    /**
     * Funció de hash. -> Serveix per tenir els sets de peces desordenats
     * @return Un random de 0 a 15 que estableix la funció de hash amb la que es desaran les peces.
     */
    @Override
    public int hashCode() {
        return (int) Math.random() % 16;
    }

    /**
     * Comparador
     * @param obj Objecte a comparar.
     * @return Cert si obj i this son iguals (el mateix tipus i valor de peça), fals altrament
     */
    @Override
    public boolean equals(Object obj) {
        if(obj == null) return this == null;
        else if(!(obj instanceof Piece)) return false;
        else  {
            return ((Piece) obj).piece == piece;
        }
    }

    /**
     * Mètode clone obligat a fer per el cloneable. Serveix per poder aprofitar la potència dels algoritmes de java a l'hora de clonar estructures de dades.
     * @return Una nova peça a partir de this.
     */
    @Override
    protected Object clone() {
        return new Piece(piece);
    }

    /**
     * @return Representació en caràcters de una peça
     */
    @Override
    public String toString() {
        return getInt() + "";
    }
}
