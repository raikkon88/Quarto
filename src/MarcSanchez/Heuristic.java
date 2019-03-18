package MarcSanchez;

/**
 * Author : Marc Sànchez Pifarré
 * Udg Code : u1939705
 * Classe Node
 * ------------------------------
 * TODO : Comentar punts crítics un cop acabat de fer l'improve de l'heurístic.
 */
public class Heuristic {

    private static final int HEU_ROWS = 10;
    public static final int HEURISTIC_MAX = 100000;

    HeuristicRow[] matrix;
    int value;
    boolean finished;
    Piece nextPiece;
    boolean calculated;

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

    public Piece getPiece(){
        return this.nextPiece;
    }


    public void add(int row, int col, Piece piece, boolean isMax){
        finished = matrix[row].addValue(piece.getProperties(), isMax) || finished;
        finished = matrix[4+col].addValue(piece.getProperties(), isMax) || finished;
        if(row == col) finished = matrix[8].addValue(piece.getProperties(), isMax) || finished;
        if(row == 3-col) finished = matrix[9].addValue(piece.getProperties(), isMax) || finished;
    }

    public boolean isFinished(){
        return finished;
    }

    @Override
    public String toString() {
        return getValue() + "  |  " + (isFinished() ? "Final" : "No Final");
    }

    // giving a value for each kind of number that can give.
    // If is a 0 -> 1 points.
    // If is a 1 -> 10 points
    // If is a 2 -> 100 points
    // If is a 3 -> 1000 points
    public class HeuristicRow {

        int value;
        int finalPoints;
        int piecesNumber;

        int ones;
        int twos;
        int threes;
        int fours;


        public HeuristicRow(){
            value = 0;
            ones = 0;
            twos = 0;
            threes = 0;
            fours = 0;
            piecesNumber = 0;
            finalPoints = 0;
        }

        public boolean addValue(int properties, boolean isMax){
            this.value += properties;
            int copy = value;
            while (copy > 0){
                int partial = copy % 5;
                if(partial == 1){
                    ones++;
                    finalPoints += 10;
                }
                else if(partial == 2){
                    twos++;
                    finalPoints += 100;
                }
                else if(partial == 3){
                    threes++;
                    if(!isMax)
                        finalPoints += 10000;
                    else
                        finalPoints += 100;
                }
                else if (partial == 4){
                    fours++;
                }
                else {
                    // Nothing to do, we don't want to evaluate the 0's.
                }
                copy /= 10;
            }
            piecesNumber++;
            return isFinal();
        }


        public boolean isFinal(){
            return fours > 0;
        }

        @Override
        public String toString() {
            return  "VALUE = " + value + " | 1s = " + ones + " | 2s = " + twos +
                    " | 3s = " + threes + " | 4s = " + fours + " -----> (" + finalPoints + ")";
        }
    }
}
