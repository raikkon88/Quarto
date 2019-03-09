package Quatro;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * @author Usuari
 */
public class Player1 {

    private final int STEPS = 16;

    private Tauler meutaulell;
    private Node tree;
    private int[][] tauler;
    private int ultimaJugada;
    private int step;
    private Random random;
    private List<int[]> positions;
    private Set<Integer> pecesGenerades;

    Player1(Tauler entrada){
        meutaulell = entrada;
        random = new Random();
        positions = new ArrayList<>();
        step = 0;
        for(int i = 0; i < 16; i++){
            positions.add(new int[]{i/4, i%4});
        }

        pecesGenerades = new HashSet<>();
        // Només construeixo 15 peces.
        for(int i = 0; i < STEPS; i++){
            String peca = Integer.toBinaryString( (1 << 4) | i ).substring( 1 );
            // Construeixo les peces una per una i comprovo que la que he jugat no hi sigui com a pendent.
            pecesGenerades.add(Integer.valueOf(peca));
        }
    }


    public void desordena(List<int[]> llistat){
        // Sacsegem la bossa de posicions per actuar de manera imprevisible.
        Queue<int[]> tmpPositions = new LinkedBlockingQueue<>();
        while(!llistat.isEmpty()){
            int rand =Math.abs(this.random.nextInt() % llistat.size());
            tmpPositions.add(llistat.get(rand));
            llistat.remove(rand);
        }

        while (!tmpPositions.isEmpty()){
            llistat.add(tmpPositions.poll());
        }
    }

    public int[] getArrayFromInteger(int value){
        int tamany = value%2;value /=10;
        int forat = value%2;value /=10;
        int forma = value%2;value /=10;
        int color = value%2;value /=10;

        return new int[]{color, forma, forat, tamany};
    }

    public int[] tirada(int colorin, int formain, int foratin, int tamanyin) {
        //colorin - Color de la peça a colocar  -> 	0 = Blanc 	1 = Negre
        //formain - Forma de la peça a colocar  -> 	0 = Rodona 	1 = Quadrat
        //foratin - Forat de la peça a colocar  -> 	0 = No  	1 = Si
        //tamanyin - Forat de la peça a colocar ->  0 = Petit 	1 = Gran

        // Desordenem tant les peces com les posicions per realitzar actuacions totalment aleatòries.
        desordena(positions);

        if(tree == null) {
            tree = new Node(0, false, new int[]{colorin, formain, foratin, tamanyin}, random);
            tauler = new int[4][4];
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    int val = meutaulell.getpos(i, j);
                    tauler[i][j] = val;
                    if(val != -1){
                        step++;
                    }
                }
            }
            // TODO : Encara em queda passar el que hi ha al tauler si no sóc el primer de tirar.
            // En aquest punt ja sé on sóc i tinc el taulell que faré servir de generador actualitzat.
        }
        else{
            // Haig de mirar on ha posat la peça que li he entregat anteriorment.
            int i = 0;
            boolean trobat = false;
            while(!trobat && i < 16){
                trobat = ultimaJugada == meutaulell.getpos(i/4, i%4);
                i++;
            }
            i--;

            // Aquí tinc la posició on va la peça que ha posat l'altre. Actualitzo l'arbre meu.
            // TODO : Aqui està fallant per que estic inicialitzant el moviment següent amb el moviment que acaba de fer l'adversari.
            tree = new Node(tree, i/4, i%4, getArrayFromInteger(ultimaJugada));
        }

        tree = minIMax(tree, 3, new int[]{colorin, formain, foratin, tamanyin}, new Node(Integer.MIN_VALUE), new Node(Integer.MAX_VALUE));
        ultimaJugada = tree.combination[2]*1000 + tree.combination[3] *100 + tree.combination[4] * 10 + tree.combination[5];
        return tree.combination;
    }

    
    private Node minIMax(Node node, int deep, int[] peca, Node alfa, Node beta){
        // Node finalValue;
        if(enoughtDeep(deep)) {
            node.evalHeuristic();
            return node; // heuristic(node)
        }

        node.generate();

        if(node.isEmpty()) {
            node.evalHeuristic();
            return node; // heuristic(node)
        }
        
        if(node.isMin()){
            for(Node n : node.nodes){
                if (alfa.getHeuristic() >= beta.getHeuristic()) break;
                Node value = minIMax(n, deep - 1,  peca, alfa, beta);
                if( value.getHeuristic() < beta.getHeuristic()){
                    beta = n;
                    n.setHeuristic(value.getHeuristic());
                }

            }
            return beta;
        }
        else {
            // finalValue = new Node(Integer.MAX_VALUE);
            for(Node n : node.nodes){
                if (alfa.getHeuristic() >= beta.getHeuristic()) break;
                Node value = minIMax(n, deep - 1,  peca, alfa, beta);
                if( value.getHeuristic() > alfa.getHeuristic()) alfa = n; n.setHeuristic(value.getHeuristic());

            }
            return alfa;
        }
    }


    private boolean enoughtDeep(int level){
        return level == 0;
    }
    

    private class Node implements Comparable<Node>{

        Integer heuristic;
        int level;
        int[] combination;
        boolean max;
        private int[][] tauler;
        List<Node> nodes;
        Set<Integer> perJugar;
        boolean suma4;
        private Random random;

        public Node(int heuristic){
            this.heuristic = heuristic;
        }

        public Node(int level, boolean isMax, int[] piece, Random random){
            this.random = random;
            // Aquest constructor només serà cridat per nivell == 0
            this.level = level;
            this.max = isMax;
            this.heuristic = 0;
            this.combination = new int[]{-1,-1, piece[0], piece[1], piece[2], piece[3]};
            this.perJugar = new HashSet<>();
            Iterator<Integer> iterator = pecesGenerades.iterator();
            while (iterator.hasNext()){
                this.perJugar.add(new Integer(iterator.next().intValue()));
            }

            this.perJugar.remove(combination[2]*1000+combination[3]*100+combination[4]*10+combination[5]);
            this.tauler = new int[4][4];
            // Inicialitzo
            for(int i = 0; i < 16; i++){
                this.tauler[i/4][i%4] = -1;
            }

        }

        public Node(Node node, int fila, int col, int[] perEll){
            this.random = node.random;
            this.combination = new int[]{fila, col, perEll[0],perEll[1],perEll[2], perEll[3]};
            this.level = node.level + 1;
            this.max = !node.max;
            this.heuristic = 0;
            node.tauler[fila][col] = node.combination[2]*1000+node.combination[3]*100+node.combination[4]*10+node.combination[5];
            this.tauler = new int[4][4];
            // Transfereixo el tauler i empleno la llista de fitxes per jugar
            perJugar = new HashSet<>();
            Iterator<Integer> iterator = pecesGenerades.iterator();
            while (iterator.hasNext()){
                perJugar.add(new Integer(iterator.next().intValue()));
            }

            Set<Integer> tmp = new HashSet<>();
            for(int[] posicio : positions){
                int valor = node.tauler[posicio[0]][posicio[1]];
                this.tauler[posicio[0]][posicio[1]] = valor;
                if(valor != -1){
                    tmp.add(valor);
                }
            }
            perJugar.removeAll(tmp);
            node.tauler[fila][col] = -1;


        }


        public void generate(){

            nodes = new ArrayList<>();

            for(int[] posicio : positions){
                int fila = posicio[0];
                int col = posicio[0];
                if(this.tauler[fila][col] == -1){
                    Iterator<Integer> iterator = perJugar.iterator();
                    while (iterator.hasNext()){
                        nodes.add(new Node(this, fila, col, getArrayFromInteger(iterator.next())));
                    }
                }
            }
        }


        public void evalHeuristic(){
            int [][] matriuResultats = new int[10][8];
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    int valor = tauler[i][j];
                    if(valor != -1){

                        int[] peca = getArrayFromInteger(valor);

                        actualitzaFilaMatriu(matriuResultats[i], peca);
                        actualitzaFilaMatriu(matriuResultats[4+j],peca);
                        if(i == j){
                            actualitzaFilaMatriu(matriuResultats[8], peca);
                        }
                        // Miro si actualitzar la contra diagonal
                        if(i == 3 - j){
                            actualitzaFilaMatriu(matriuResultats[9], peca);
                        }
                    }
                }
            }

            if(suma4){
                heuristic = 100;
            }
            else{
                float global = 0;
                for(int[] fila : matriuResultats){
                    int col = 0;
                    for(int valor : fila){
                        col += valor;
                    }
                    global += (10 * col) / 24;
                }

                heuristic = Math.round(global);
            }

        }

        public void actualitzaFilaMatriu(int[] fila, int[] jugada){
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
            while(!suma4 && i < 8){
                suma4 = fila[i] == 4;
                i++;
            }
        }

        // Actualitza el taulell amb la peça de la combinació posant-la on li toca.
        public void updateWithcontrincantMovement(int i, int j){
            tauler[i][j] = combination[2]*1000+combination[3]*100+combination[4]*10+combination[5];
        }

        public int getHeuristic(){
            return this.heuristic;
        }

        public boolean isMin(){
            return !max;
        }
        
        public boolean isMax(){
            return max;
        }

        public boolean isEmpty(){
            return this.nodes.isEmpty();
        }

        public int getLevel(){
            return this.level;
        }

        public Node getChildAt(int[] combination){
            Node tmp = null;
            for(Node n : this.nodes){
                if(n.combination.equals(combination)){
                    tmp = n;
                    break;
                }
            }
            return tmp;
        }


        @Override
        public int compareTo(Node node) {
            return this.heuristic.compareTo(node.heuristic);
        }

        public void setHeuristic(int heuristic) {
            this.heuristic = heuristic;
        }
    }
    

}
