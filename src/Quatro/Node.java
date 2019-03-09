package Quatro;


import java.util.*;

public class Node implements Comparable<Node>{

    Integer heuristic;
    int level;
    int[] combination;
    boolean max;
    private int[][] tauler;
    List<Node> nodes;
    Set<Integer> perJugar;
    boolean suma4;
    private Random random;
    protected List<int[]> positions;
    protected Set<Integer> pecesGenerades;

    // Constructor per nodes fantasma que només han de desar el valor de l'heurístic.
    public Node(int heuristic){
        this.heuristic = heuristic;
    }

    public Node(int level, boolean isMax, int[] piece, Random random, Set<Integer> pecesGenerades, List<int[]> positions){
        this.positions = positions;
        this.pecesGenerades = pecesGenerades;
        this.random = random;
        // Aquest constructor només serà cridat per nivell == 0
        this.level = level;
        this.max = isMax;
        this.heuristic = 0;
        this.combination = new int[]{-1,-1, piece[0], piece[1], piece[2], piece[3]};

        copyPieces(this);

        this.perJugar.remove(combination[2]*1000+combination[3]*100+combination[4]*10+combination[5]);
        this.tauler = new int[4][4];
        // Inicialitzo
        for(int i = 0; i < 16; i++){
            this.tauler[i/4][i%4] = -1;
        }

    }

    public Node(Node node, int fila, int col, int[] perEll, boolean contrari){
        this(node);
        node.tauler[fila][col] = node.combination[2]*1000+node.combination[3]*100+node.combination[4]*10+node.combination[5];
        transferBoard(node);
        if(contrari){
            this.combination = new int[]{-1 ,-1, perEll[0],perEll[1],perEll[2], perEll[3]};
        }else {
            node.tauler[fila][col] = -1;
            this.combination = new int[]{fila, col, perEll[0], perEll[1], perEll[2], perEll[3]};
        }

    }

    public Node(Node node){this.random = node.random;
        this.level = node.level + 1;
        this.max = !node.max;
        this.heuristic = 0;
        this.tauler = new int[4][4];
        this.pecesGenerades = node.pecesGenerades;
        this.positions = node.positions;
    }



    public Node(Node node, int fila, int col, int[] perEll){
        this(node, fila, col, perEll, false);
    }


    public void generate(){
        nodes = new ArrayList<>();
        for(int[] posicio : positions){
            int fila = posicio[0];
            int col = posicio[1];
            if(this.tauler[fila][col] == -1){
                Iterator<Integer> iterator = perJugar.iterator();
                while (iterator.hasNext()){
                    int [] proposadaPerPosar = getArrayFromInteger(iterator.next());
                    if(!(proposadaPerPosar[0] == this.combination[2] && proposadaPerPosar[1] == this.combination[3] && proposadaPerPosar[2] == this.combination[4] && proposadaPerPosar[3] == this.combination[5]))
                        nodes.add(new Node(this, fila, col, proposadaPerPosar));
                }
            }
        }
    }


    private void transferBoard(Node node){

        copyPieces(node);

        Set<Integer> tmp = new HashSet<>();
        for(int[] posicio : positions){
            int valor = node.tauler[posicio[0]][posicio[1]];
            this.tauler[posicio[0]][posicio[1]] = valor;
            if(valor != -1){
                tmp.add(valor);
            }
        }
        this.perJugar.removeAll(tmp);
    }

    private void copyPieces(Node node){
        // Transfereixo el tauler i empleno la llista de fitxes per jugar
        this.perJugar = new HashSet<>();
        Iterator<Integer> iterator = node.pecesGenerades.iterator();
        while (iterator.hasNext()){
            this.perJugar.add(new Integer(iterator.next().intValue()));
        }
    }

    public boolean actualitzaMatriu(int valor, int fila, int col, int[][] matriuResultats){
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
                int valor = tauler[i][j];
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


            int valor = combination[2]*1000+combination[3]*100+combination[4]*10+combination[5];

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
                    this.heuristic = 10000;
                }
                else{
                    this.heuristic = -10000;
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

    /*// Actualitza el taulell amb la peça de la combinació posant-la on li toca.
    public void updateWithcontrincantMovement(int i, int j){
        tauler[i][j] = combination[2]*1000+combination[3]*100+combination[4]*10+combination[5];
    }*/

    public int getHeuristic(){
        return this.heuristic;
    }


    public boolean isFulla(){
        return suma4;
    }

    public boolean isMin(){
        return !max;
    }

    public boolean isEmpty(){
        return this.nodes.isEmpty();
    }

    /*public int getLevel(){
        return this.level;
    }

    public Node getChildAt(int[] combination){
        for(Node n : this.nodes){
            boolean equals = true;
            int i = 0;
            while (equals && i < 6){
                equals = combination[i] == n.combination[i];
                i++;
            }
            if(equals){
                return n;
            }
        }
        return null;
    }*/


    @Override
    public int compareTo(Node node) {
        return this.heuristic.compareTo(node.heuristic);
    }

    public void setHeuristic(int heuristic) {
        this.heuristic = heuristic;
    }


    protected int[] getArrayFromInteger(int value){
        int tamany = value%2;value /=10;
        int forat = value%2;value /=10;
        int forma = value%2;value /=10;
        int color = value%2;

        return new int[]{color, forma, forat, tamany};
    }


    public void setValorTauler(int i, int j, int val) {
        tauler[i][j] = val;
    }
}

