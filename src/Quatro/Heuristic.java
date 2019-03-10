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

        // El primer que faig és mirar el tauler i muntar l'array.
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
            this.heuristic = HEURISTIC_MAX;
        }else{
            boolean willEnd = false;
            i = 0;
            while (!willEnd && i < HEU_ROWS){
                int res = matrix[i].value + node.combination.getProperties();
                willEnd = Integer.toString(res).contains("4");
                i++;
            }

            if(willEnd) {
                willEndRow = i - 1;
                this.heuristic = -50000;
            }
            else{
                //if(finished && node.max ) this.heuristic = -100; // Si puc perdre, puntúo per pedre.
                //else if(finished && !node.max) this.heuristic = 100; // Si puc guanyar, Puntúo per guanyar.
                //else {
                double ones = 0, twos = 0, threes = 0;
                for(int row = 0; row < HEU_ROWS ; row++){
                    ones += matrix[row].oneProportion * 0.1;
                    twos += matrix[row].twoProportion * 0.1;
                    threes += matrix[row].threeProportion * 0.1;
                }

                //Si és el meu torn busco que tingui el màxim de 2's i 4's possibles.
                // Tinc la proporció de x/8 per cada possible valor que poden prendre els nodes peró de 1 a 4.
                // - Els zeros no els puntúo.
                // - Els uns els puntúo amb un 10%
                // - Els dos els puntúo amb un 40%
                // - Els 3's no m'interessen gens, els puntúo amb un 10%
                // - Els 4's els puntúo amb un 40% -> quants més quatres més maneres de guanyar.
                if(node.max) { // EL MEU TORRNNN!!!
                    this.heuristic =  (int)Math.round((ones * 0.1 + threes * 0.1 + twos * 0.4) * 1000);
                }
                else { // EL TORN DE L'ALTREEEE!!!
                    this.heuristic = (int)Math.round((ones * 0.1 + threes * 0.8 + twos * 0.1) * 1000);
                }
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



    public class HeuristicRow {

        int rowName;
        int value;
        double oneProportion;
        double twoProportion;
        double threeProportion;
        double fourProportion;

        public HeuristicRow(int rowValue){
            this.rowName = rowValue;
            value = 0;
            oneProportion = 0;
            twoProportion = 0;
            threeProportion = 0;
            fourProportion = 0;
        }

        public boolean addValue(int properties){
            this.value += properties;
            oneProportion       = digitsProportion(1);
            twoProportion       = digitsProportion(2);
            threeProportion     = digitsProportion(3);
            fourProportion      = digitsProportion(4);
            return isFinal();
        }

        private double digitsProportion(int digit){
            int apparitions = 0;
            int copy = value;
            while (copy > 0){
                if(copy % 5 == digit)
                    apparitions++;
                copy /= 10;
            }
            return apparitions / 8.0 * 100;
        }

        public boolean isFinal(){
            return fourProportion > 0;
        }

        @Override
        public String toString() {
            if(rowName < 4) return "Fila : " + rowName;
            else if(rowName < 8) return "Columna : " + (rowName - 4);
            else if(rowName == 8) return "Diagonal 1 ";
            else return "Diagonal 2";
        }
    }
















    /*public boolean actualitzaMatriu(int valor, int fila, int col, int[][] matriuResultats){
        int[] peca = getArrayFromInteger(valor);

        suma4 = actualitzaFilaMatriu(matriuResultats[fila], peca);
        suma4 = suma4 || actualitzaFilaMatriu(matriuResultats[4+col],peca);
        if(fila == col){
            suma4 = suma4 || actualitzaFilaMatriu(matriuResultats[8], peca);
        }
        // Miro si actualitzar la contra diagonal
        if(fila == 3 - col){
            suma4 = suma4 || actualitzaFilaMatriu(matriuResultats[9], peca);
        }
        return suma4;
    }

    public void evalHeuristic(){
        int [][] matriuResultats = new int[10][8];
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4; j++){
                int valor = board[i][j];
                if(valor != -1){
                    suma4 = actualitzaMatriu(valor, i, j, matriuResultats);
                }
            }
        }
        if(suma4){
            if(max){
                this.heuristic = -10000;
            }
            else{
                this.heuristic = 10000;
            }
        }
        else {
            float global = 0;
            for(int i = 0; i < 10; i++){
                int suma = 0;
                for(int j = 0; j < 8; j++){
                    suma += matriuResultats[i][j];
                }
                global += ((((float)suma)/24) * 10);
            }


            int valor = combination.getInt();

            boolean acabaRetorn = false;
            for(int[] posicio : positions){
                if(acabaRetorn) break;
                int fila = posicio[0];
                int col = posicio[1];
                int [][] matriuResParcial = new int[10][8];
                if(tauler[fila][col] == -1){
                    tauler[fila][col] = valor;
                    acabaRetorn = actualitzaMatriu(valor, fila, col, matriuResParcial);
                    tauler[fila][col] = -1;
                }
            }
            if(acabaRetorn) {
                if(max) {
                    this.heuristic =-10000;
                }
                else{
                    this.heuristic = 10000;
                }
            }
            else {
                this.heuristic = Math.round(global * 100);
            }

        }

    }

    public boolean actualitzaFilaMatriu(int[] fila, int[] jugada){
        fila[0] += jugada[0] == 0 ? 1 : 0;
        fila[1] += jugada[0];
        fila[2] += jugada[1] == 0 ? 1 : 0;
        fila[3] += jugada[1];
        fila[4] += jugada[2] == 0 ? 1 : 0;
        fila[5] += jugada[2];
        fila[6] += jugada[3] == 0 ? 1 : 0;
        fila[7] += jugada[3];

        // Miro si en aquest node guanyo o perdo.
        int i =0;
        boolean suma4 = false;
        while(i < 8 && !(suma4)){
            suma4 = fila[i] == 4;
            i++;
        }
        return suma4;
    }
*/

}
