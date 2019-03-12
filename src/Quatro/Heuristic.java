package Quatro;

public class Heuristic {

    private static final int HEU_ROWS = 10;
    public static final int HEURISTIC_MAX = 100000;
    public static final int HEURISTIC_MIN = -100000;

    int heuristic;
    boolean finished;
    HeuristicRow[] matrix;
    Node node;
    int willEndRow;

    public Heuristic(Node node){
        willEndRow = 0;
        this.matrix = new HeuristicRow[HEU_ROWS];
        for(int i = 0; i < HEU_ROWS; i++){
            matrix[i] = new HeuristicRow(i);
        }
        this.node = node;
    }

    public Heuristic(int value){
        this.heuristic = value;
    }

    public int getValue(){
        return heuristic;
    }

    public void compute(){

        // El primer que faig és mirar el tauler i muntar l'array amb les files heurístiques.
        int i = 0;
        while(!finished && i < 4){
            int j = 0;
            while(!finished && j < 4){
                int value = node.board[i][j];
                if(value != -1)
                    finished =  updateMatrix(i, j, value) || finished;
                j++;
            }
            i++;
        }

        if(finished){
            // Puntuaré millor guanyar aviat que tard en funció del nivell.
            this.heuristic = HEURISTIC_MAX * (16 - node.level);
        }
        else{
            // Miro les propietats de la peça que li puc donar.
            for(HeuristicRow row : matrix){
                this.heuristic += row.finalPoints;
            }
        }

        if(node.max){
            this.heuristic *= -1;
        }

    }

    public boolean updateMatrix(int row, int col, int piece){
        boolean hasFinalRow = false;
        Piece p = new Piece(piece);
        hasFinalRow = matrix[row].addValue(p.getProperties()) || hasFinalRow;
        hasFinalRow = matrix[4+col].addValue(p.getProperties())|| hasFinalRow;
        if(row == col) hasFinalRow = matrix[8].addValue(p.getProperties()) || hasFinalRow;
        if(row == 3-col) hasFinalRow = matrix[9].addValue(p.getProperties()) || hasFinalRow;

        return hasFinalRow;
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
/*
        private int digitsCounter(int digit){
            int apparitions = 0;
            int copy = value;
            while (copy > 0){


                if(copy % 5 == digit)
                    apparitions++;
                copy /= 10;
            }
            return apparitions;
        }*/

        public boolean isFinal(){
            return fours > 0;
        }

        @Override
        public String toString() {
            if(rowName < 4) return "Fila : " + rowName;
            else if(rowName < 8) return "Columna : " + (rowName - 4);
            else if(rowName == 8) return "Diagonal 1 ";
            else return "Diagonal 2";
        }
    }

    /**
     *
     *  if(finished){
     *             this.heuristic = HEURISTIC_MAX;
     *         }else{
     *             boolean willEnd = false;
     *             i = 0;
     *             while (!willEnd && i < HEU_ROWS){
     *                 int res = matrix[i].value + node.combination.getProperties();
     *                 willEnd = Integer.toString(res).contains("4");
     *                 i++;
     *             }
     *
     *             if(willEnd) {
     *                 willEndRow = i - 1;
     *                 this.heuristic = -50000;
     *             }
     *             else{
     *                 //if(finished && node.max ) this.heuristic = -100; // Si puc perdre, puntúo per pedre.
     *                 //else if(finished && !node.max) this.heuristic = 100; // Si puc guanyar, Puntúo per guanyar.
     *                 //else {
     *                 double ones = 0, twos = 0, threes = 0, fours = 0;
     *                 for(int row = 0; row < HEU_ROWS ; row++){
     *                     ones += matrix[row].oneProportion * 0.1;
     *                     twos += matrix[row].twoProportion * 0.1;
     *                     threes += matrix[row].threeProportion * 0.1;
     *                     fours += matrix[row].fourProportion * 0.1;
     *                 }
     *
     *                 //Si és el meu torn busco que tingui el màxim de 2's i 4's possibles.
     *                 // Tinc la proporció de x/8 per cada possible valor que poden prendre els nodes peró de 1 a 4.
     *                 // - Els zeros no els puntúo.
     *                 // - Els uns els puntúo amb un 10%
     *                 // - Els dos els puntúo amb un 40%
     *                 // - Els 3's no m'interessen gens, els puntúo amb un 10%
     *                 // - Els 4's els puntúo amb un 40% -> quants més quatres més maneres de guanyar.
     *
     *                 //this.heuristic =  (int)Math.round((ones * 0.1 + threes * 0.1 + twos * 0.4 + ((fours * 0.4) / node.level * 1000))*100);
     *
     *                 if(node.max) { // EL MEU TORRNNN!!!
     *                     this.heuristic =  (int)Math.round((ones * 0.1 + threes * 0.1 + twos * 0.4 + ((fours * 0.4) / node.level * 100))*100);
     *                 }
     *                 else { // EL TORN DE L'ALTREEEE!!!
     *                      this.heuristic = (int) Math.round((ones * 0.1 + threes * 0.7 + twos * 0.2) * 1000);
     *                  }
     *             }
     *         }
     *         if(!node.max){
     *             this.heuristic *= -1;
     *         }
     */


/*

    //if(finished && node.max ) this.heuristic = -100; // Si puc perdre, puntúo per pedre.
    //else if(finished && !node.max) this.heuristic = 100; // Si puc guanyar, Puntúo per guanyar.
    //else {
    double ones = 0, twos = 0, threes = 0, fours = 0;
        for(int row = 0; row < HEU_ROWS ; row++){
        ones += matrix[row].oneProportion * 0.1;
        twos += matrix[row].twoProportion * 0.1;
        threes += matrix[row].threeProportion * 0.1;
        fours += matrix[row].fourProportion * 0.1;
    }

    ones /= node.level;
    twos /= node.level;
    threes /= node.level;
    fours /= node.level;

        if(node.max) { // EL MEU TORRNNN!!!
        this.heuristic =  (int)Math.round((ones * 0 + threes * 0.1 + twos * 0.3 + fours * 0.6) * 1000);
    }
        else { // EL TORN DE L'ALTREEEE!!!
        this.heuristic = (int)Math.round((ones * 0.2 + threes * 0.7 + twos * 0.1) * 1000);
    }*/

}
