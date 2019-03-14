package Quatro;

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

    int heuristic;
    boolean finished;
    HeuristicRow[] matrix;
    Node node;

    public Heuristic(Node node, Heuristic h){
        this.node = node;
        this.matrix = new HeuristicRow[HEU_ROWS];
        for(int i = 0; i < HEU_ROWS; i++){
            HeuristicRow row = h.matrix[i];
            matrix[i] = new HeuristicRow(row);
            this.heuristic += row.finalPoints;
        }
        if(this.finished){
            this.heuristic = HEURISTIC_MAX * (16 - node.level);
        }
        if (node.max) {
            this.heuristic *= -1;
        }
    }

    public Heuristic(int value){
        this.heuristic = value;
        this.matrix = new HeuristicRow[HEU_ROWS];
        for(int i = 0; i < HEU_ROWS; i++){
            matrix[i] = new HeuristicRow(i);
        }

    }

    public int getValue(){
        return heuristic;
    }

    public boolean add(int value, int f, int c){
        return updateMatrix(f, c, value);
    }

    public void compute(){

        // TODO : If wants to do something more on heuristics here is the place.
    }

    public boolean updateMatrix(int row, int col, int piece){
        Piece p = new Piece(piece);
        finished = matrix[row].addValue(p.getProperties()) || finished;
        finished = matrix[4+col].addValue(p.getProperties())|| finished;
        if(row == col) finished = matrix[8].addValue(p.getProperties()) || finished;
        if(row == 3-col) finished = matrix[9].addValue(p.getProperties()) || finished;
        return finished;
    }

    @Override
    public String toString() {
        return this.heuristic + "  |  " + (this.finished ? "Final" : "No Final");
    }

    // giving a value for each kind of number that can give.
    // If is a 0 -> 1 points.
    // If is a 1 -> 10 points
    // If is a 2 -> 100 points
    // If is a 3 -> 1000 points
    public class HeuristicRow {

        int rowName;
        int value;
        int finalPoints;
        int piecesNumber;

        int zeros;
        int ones;
        int twos;
        int threes;
        int fours;


        public HeuristicRow(int rowValue){
            this.rowName = rowValue;
            value = 0;
            zeros = 0;
            ones = 0;
            twos = 0;
            threes = 0;
            fours = 0;
            piecesNumber = 0;
            finalPoints = 0;
        }

        public HeuristicRow(HeuristicRow row){
            this.rowName = row.rowName;
            this.value = row.value;
            this.zeros = row.zeros;
            this.ones = row.ones;
            this.twos = row.twos;
            this.threes = row.threes;
            this.fours = row.fours;
            this.piecesNumber = row.piecesNumber;
            this.finalPoints = row.finalPoints;
        }

        public boolean addValue(int properties){
            this.value += properties;
            int copy = value;
            while (copy > 0){
                int partial = copy % 5;
                if(partial == 0){
                    zeros++;
                    finalPoints+=1;
                }
                else if(partial == 1){
                    ones++;
                    finalPoints += 10;
                }
                else if(partial == 2){
                    twos++;
                    finalPoints += 100;
                }
                else if(partial == 3){
                    threes++;
                    finalPoints += 1000;
                }
                else{
                    fours++;
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
            if(rowName < 4) return "Fila : " + rowName + " -> " + value;
            else if(rowName < 8) return "Columna : " + (rowName - 4) + " -> " + value;
            else if(rowName == 8) return "Diagonal 1 " + " -> " + value;
            else return "Diagonal 2" + " -> " + value;
        }
    }
}
