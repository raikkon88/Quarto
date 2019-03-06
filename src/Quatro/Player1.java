package Quatro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author Usuari
 */
public class Player1 {

    private final int STEPS = 16;

    private Tauler meutaulell;
    private Node tree;

    
    Player1(Tauler entrada){
        meutaulell = entrada;
        tree = new Node(0, false);
    }
    
    private Node minIMax(Node node, int deep){
        Node finalValue;
        if(enoughtDeep(deep)) {
            node.evalHeuristic();
            return node; // heuristic(node)
        }

        node.generate();

        if(node.isEmpty()) {
            node.evalHeuristic();
            return node; //heuristic(node);
        }
        
        if(node.isMin()){
            finalValue = new Node(Integer.MAX_VALUE);
            for(Node n : node.nodes){
                Node value = minIMax(n, deep -1);
                if(finalValue.getHeuristic() > value.getHeuristic()) {
                    n.setHeuristic(value.getHeuristic());
                    finalValue = n;
                }
            }
        }
        else {
            finalValue = new Node(Integer.MIN_VALUE);
            for(Node n : node.nodes){
                Node value = minIMax(n, deep -1);
                if(finalValue.getHeuristic() < value.getHeuristic()) { finalValue = n; n.setHeuristic(value.getHeuristic()); }
            }
        }
        return finalValue;
    }

    private boolean enoughtDeep(int level){
        return level == 0;
    }

    public int[] tirada(int colorin, int formain, int foratin, int tamanyin){
    //colorin - Color de la peça a colocar -> 	0 = Blanc 	1 = Negre
    //formain - Forma de la peça a colocar -> 	0 = Rodona 	1 = Quadrat
    //foratin - Forat de la peça a colocar -> 	0 = No  	1 = Si
    //tamanyin - Forat de la peça a colocar -> 0 = Petit 	1 = Gran


        // Li poso la peça al node de l'arbre.
        tree.setPiece(new int[]{-1, -1, colorin, formain, foratin, tamanyin});

        // Calculo el minimax
        Node value = minIMax(this.tree, 4);
        int posX = value.piece[0];
        int posY = value.piece[1];

        tree = value;

        Node toGive = minIMax(tree, 3);

        return new int[]{posX, posY, toGive.piece[2], toGive.piece[3], toGive.piece[4], toGive.piece[5]};
    }
    

    private class Node implements Comparable<Node>{

        Integer heuristic;
        int level;
        int[] piece;
        boolean max;
        List<int[]> perOcupar;
        List<int[]> perJugar;
        int[][] matriuPuntuació;
        Node[] nodes;
        boolean guanyador;

        public Node(int heuristic){
            this.heuristic = heuristic;
        }

        public Node(int level, boolean isMax){
            this.level = level;
            this.max = isMax;
            // Genero totes les posicions del tauler
            this.perOcupar = new ArrayList<>();
            // GEnero totes les peces que queden per jugar
            this.perJugar = new ArrayList<>();
            for(int i = 0; i < STEPS; i++){
                // Genero totes les peces
                String peca = Integer.toBinaryString( (1 << 4) | i ).substring( 1 );
                this.perOcupar.add(new int[]{i / 4, i % 4, -1, -1, -1, -1});
                this.perJugar.add(new int[]{-1, -1, peca.charAt(0) - 48, peca.charAt(1) - 48, peca.charAt(2)- 48, peca.charAt(3)- 48});
            }
            if(isMax)
                nodes = new Node[(STEPS - level) * (STEPS - level)];
            else
                nodes = new Node[STEPS - level];

            matriuPuntuació = new int[10][8];
            for(int i  = 0; i < matriuPuntuació.length; i++){
                for (int j = 0; j < matriuPuntuació[i].length; j++){
                    matriuPuntuació[i][j] = 0;
                }
            }
        }

        public Node(Node node, int[] jugada){

            this.perOcupar = new ArrayList<>();
            this.perJugar = new ArrayList<>();
            this.piece = new int[]{jugada[0],jugada[1],jugada[2], jugada[3], jugada[4], jugada[5]};
            this.level = node.level + 1;
            this.max = !node.max;

            if(node.isMax())
                nodes = new Node[16-level];
            else
                nodes = new Node[(16 - level) * (16 - level)];

            for(int[] casella : node.perOcupar){
                if(!(casella[0] == jugada[0] && casella[1] == jugada[1])){
                    this.perOcupar.add(casella);
                }
            }

            for(int[] peca : node.perJugar){
                if(!(peca[2] == jugada[2] && peca[3] == jugada[3] && peca[4] == jugada[4] && peca[5] == jugada[5])){
                    this.perJugar.add(peca);
                }
            }

            matriuPuntuació = new int[10][8];
            for(int i = 0; i < 10; i++){
                for(int j = 0; j < 8; j++){
                    this.matriuPuntuació[i][j] = node.matriuPuntuació[i][j];
                }
            }

            // Actualitzo fila
            actualitzaFilaMatriu(matriuPuntuació[jugada[0]], jugada);
            // Actualitzo columna
            actualitzaFilaMatriu(matriuPuntuació[4+jugada[1]], jugada);
            // Miro si actualitzar la diagonal
            if(jugada[0] == jugada[1]){
                actualitzaFilaMatriu(matriuPuntuació[8], jugada);
            }
            // Miro si actualitzar la contra diagonal
            if(jugada[0] == 3 - jugada[1]){
                actualitzaFilaMatriu(matriuPuntuació[9], jugada);
            }

        }

        public void actualitzaFilaMatriu(int[] fila, int[] jugada){
            fila[0] += jugada[2] == 0 ? 1 : 0;
            fila[1] += jugada[2];
            fila[2] += jugada[3] == 0 ? 1 : 0;
            fila[3] += jugada[3];
            fila[4] += jugada[4] == 0 ? 1 : 0;
            fila[5] += jugada[4];
            fila[6] += jugada[5] == 0 ? 1 : 0;
            fila[7] += jugada[5];

            // Miro si en aquest node guanyo o perdo.
            int i =0;
            while(!guanyador && i < 8){
                guanyador = fila[i] == 4 && max;
                i++;
            }
        }




        public void generate(){
            if(max) { // Genero tots els nodes min a sota (posicions tauler lliures + peces per tirar)
                int i = 0;
                for(int[] pos : perOcupar){
                    for(int[] peca : perJugar){
                        nodes[i] = new Node(this, new int[]{pos[0], pos[1], peca[2], peca[3], peca[4], peca[5]});
                        i++;
                    }
                }
            }
            else { // Genero tots els nodes max a sota (posicions tauler lliures)
                int i = 0;
                for(int[] pos : perOcupar){
                    nodes[i] = new Node(this, new int[]{pos[0], pos[1], piece[2], piece[3], piece[4], piece[5]});
                    i++;
                }
            }
        }

        public void setPiece(int[] piece){
            this.piece = piece;
        }

        public void evalHeuristic(){
            if(guanyador && max) heuristic = 100;
            else if(guanyador && !max) heuristic = -100;
            else{
                float global = 0;
                for(int[] fila : matriuPuntuació){
                    int col = 0;
                    for(int valor : fila){
                        col += valor;
                    }
                    global += (10 * col) / 12;
                }
                heuristic = Math.round(global);
            }

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
            return this.nodes[0] == null;
        }

        public int getLevel(){
            return this.level;
        }

        public void addNode(Node node, int pos){
            this.nodes[pos] = node;
        }

        @Override
        public int compareTo(Node node) {
            return this.heuristic.compareTo(node.heuristic);
        }

        public void setHeuristic(int heuristic) {
            this.heuristic = heuristic;
        }
    }
    





    /*
        int x,y,color,forma,forat,tamany;
        color=-1;
        forma=-1;
        forat=-1;
        tamany=-1;
        boolean trobat=true;

        while( trobat){
            //mentres la trobem al taulell genero peçes
            //La peça que posarem
            color= (int) java.lang.Math.round( java.lang.Math.random() );
            forma=(int) java.lang.Math.round( java.lang.Math.random() );
            forat= (int) java.lang.Math.round( java.lang.Math.random() );
            tamany= (int) java.lang.Math.round( java.lang.Math.random() );

            trobat = color==colorin && forma==formain && forat==foratin && tamany==tamanyin;

            int valor= color*1000+forma*100+forat*10+tamany;
            //busco la peça
            for(int i=0;i<meutaulell.getX();i++){
                for(int j=0;j<meutaulell.getY();j++){
                    if (meutaulell.getpos(i,j) == valor){
                        trobat=true;
                    }
                }
            }
        }


        //busco una posicio buida on posar la peça
        for(int i=0;i<meutaulell.getX();i++){
            for(int j=0;j<meutaulell.getY();j++){
                if (meutaulell.getpos(i,j) == -1){
                    return new int[]{i,j,color, forma, forat, tamany};
                }
            }
        }

        //Un retorn per defecte
        return new int[]{0,0,0,0,0,0};
        //format del retorn vector de 6 int {posX[0a3], posY[0a3], color[0o1] forma[0o1], forat[0o1], tamany[0o1]}
        //posX i posY es la posicio on es coloca la peça d'entrada
        //color forma forat i tamany descriuen la peça que colocara el contrari
        //color -  	0 = Blanc 	1 = Negre
        //forma -  	0 = Rodona 	1 = Quadrat
        //forat - 	0 = No  	1 = Si
        //tamany -      0 = Petit 	1 = Gran

*/
}
