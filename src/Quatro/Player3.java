package Quatro;


/**
 * @author Usuari
 */
public class Player3 extends Player{


    Player3(Tauler entrada){
        super(entrada);
    }


    public int[] tirada(int colorin, int formain, int foratin, int tamanyin) {
        //colorin - Color de la peça a colocar  -> 	0 = Blanc 	1 = Negre
        //formain - Forma de la peça a colocar  -> 	0 = Rodona 	1 = Quadrat
        //foratin - Forat de la peça a colocar  -> 	0 = No  	1 = Si
        //tamanyin - Forat de la peça a colocar ->  0 = Petit 	1 = Gran

        // Desordenem tant les peces com les posicions per realitzar actuacions totalment aleatòries.
      /*  desordena(tree.positions, random);

        if(tree == null) {
            tree = new Node(0, true, new int[]{colorin, formain, foratin, tamanyin}, random, this.pecesGenerades, this.positions);
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    int val = meutaulell.getpos(i, j);
                    tree.setValorTauler(i,j,val);
                    if(val != -1){
                        step++;
                    }
                }
            }
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

            // Aquí tinc la posició on va la peça que ha posat l'altre. Actualitzo l'arbre
            tree = new Node(tree, i/4, i%4, new int[]{colorin, formain, foratin, tamanyin}, true);
            step++;
        }
        tree =new AlphaBeta().alphaBeta(tree, calculateStep(step), new Node(Integer.MIN_VALUE), new Node(Integer.MAX_VALUE));
        ultimaJugada = tree.combination[2]*1000 + tree.combination[3] *100 + tree.combination[4] * 10 + tree.combination[5];
        step++;
        return tree.combination;*/
        return new int[]{0,0,0,0,0,0};
    }

    protected int calculateStep(int level){
        if(level == 0 || level == 2){
            return 1;
        }
        else if(level > 2 && level < 6){
            return 2;
        }
        else if(level > 5 && level < 10){
            return 4;
        }
        else if(level == 10){
            return 5;
        }

        return 6;

    }


/*


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

            copyPieces();

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

            copyPieces();

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

        private void copyPieces(){
            // Transfereixo el tauler i empleno la llista de fitxes per jugar
            this.perJugar = new HashSet<>();
            Iterator<Integer> iterator = pecesGenerades.iterator();
            while (iterator.hasNext()){
                this.perJugar.add(new Integer(iterator.next().intValue()));
            }
        }

        public void evalHeuristic(){
            int [][] matriuResultats = new int[10][8];
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    int valor = tauler[i][j];
                    if(valor != -1){

                        int[] peca = Utils.getArrayFromInteger(valor);

                        suma4 = actualitzaFilaMatriu(matriuResultats[i], peca);
                        suma4 = suma4 || actualitzaFilaMatriu(matriuResultats[4+j],peca);
                        if(i == j){
                            suma4 = suma4 || actualitzaFilaMatriu(matriuResultats[8], peca);
                        }
                        // Miro si actualitzar la contra diagonal
                        if(i == 3 - j){
                            suma4 = suma4 || actualitzaFilaMatriu(matriuResultats[9], peca);
                        }
                    }
                }
            }

            if(suma4){
                if(max) this.heuristic = 1;
                else this.heuristic = -1;
            }
            else {
                this.heuristic = 0;
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

        public boolean isEmpty(){
            return this.nodes.isEmpty();
        }

        public int getLevel(){
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
        }


        @Override
        public int compareTo(Node node) {
            return this.heuristic.compareTo(node.heuristic);
        }

        public void setHeuristic(int heuristic) {
            this.heuristic = heuristic;
        }
    }
*/


}
