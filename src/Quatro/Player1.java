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

    Player1(Tauler entrada){
        ultimaJugada = -1;
        meutaulell = entrada;
        step = 0;
    }


    public int[] tirada(int colorin, int formain, int foratin, int tamanyin) {
        //colorin - Color de la peça a colocar -> 	0 = Blanc 	1 = Negre
        //formain - Forma de la peça a colocar -> 	0 = Rodona 	1 = Quadrat
        //foratin - Forat de la peça a colocar -> 	0 = No  	1 = Si
        //tamanyin - Forat de la peça a colocar -> 0 = Petit 	1 = Gran


        if(tree == null) {
            tree = new Node(0, false, new int[]{-1,-1, colorin, formain, foratin, tamanyin});
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
            // En aquest punt ja sé on sóc i tinc el taulell que faré servir de generador actualitzat.
        }

        tree = minIMax(tree, 2, new int[]{colorin, formain, foratin, tamanyin});

        return tree.combination;
    }


    private void modificaTauler(int[] peca){
        modificaTauler(peca[0], peca[1],peca[2]+1000+peca[3]*100+peca[4]*10+peca[5]);
    }

    private void modificaTauler(int x, int y, int val){
        tauler[x][y] = val;
    }
    
    private Node minIMax(Node node, int deep, int[] peca){
        Node finalValue;
        if(enoughtDeep(deep)) {
            return node; // heuristic(node)
        }

        node.generate(tauler, peca);

        if(node.isEmpty()) {
            return node; //heuristic(node);
        }
        
        if(node.isMin()){
            finalValue = new Node(Integer.MAX_VALUE);
            for(Node n : node.nodes){

                Node value = minIMaxShout(n, peca, deep);

                if (finalValue.getHeuristic() > value.getHeuristic()) {
                    n.setHeuristic(value.getHeuristic());
                    finalValue = n;
                }
            }
        }
        else {
            finalValue = new Node(Integer.MIN_VALUE);
            for(Node n : node.nodes){
                Node value = minIMaxShout(n, peca, deep);

                if (finalValue.getHeuristic() < value.getHeuristic()) {
                    finalValue = n;
                    n.setHeuristic(value.getHeuristic());
                }
            }
        }
        return finalValue;
    }

    private Node minIMaxShout(Node n,  int[] peca, int deep){
        modificaTauler( n.combination);
        Node value = minIMax(n, deep - 1,  peca);
        modificaTauler( n.combination[0], n.combination[1], -1);
        return value;
    }

    private boolean enoughtDeep(int level){
        return level == 0;
    }
    

    private class Node implements Comparable<Node>{

        Integer heuristic;
        int level;
        int[] combination;
        boolean max;
        List<int[]> lliures;
        List<int[]> perJugar;
        Node[] nodes;
        boolean suma4;

        public Node(int heuristic){
            this.heuristic = heuristic;
        }

        public Node(int level, boolean isMax, int[] piece){
            // Aquest constructor només serà cridat per nivell == 0
            this.level = level;
            this.max = isMax;
            this.heuristic = 0;
            this.combination = piece;

            this.perJugar = new ArrayList<>();
            this.lliures = new ArrayList<>();
            for(int i = 0; i < STEPS; i++){
                // Genero totes les posicions del tauler
                this.lliures.add(new int[]{i / 4, i % 4});
                // Genero totes les peces
                String peca = Integer.toBinaryString( (1 << 4) | i ).substring( 1 );
                // Construeixo les peces una per una i comprovo que la que he jugat no hi sigui com a pendent.
                int[] jugada = new int[]{peca.charAt(0) - 48, peca.charAt(1) - 48, peca.charAt(2)- 48, peca.charAt(3)- 48};
                if(!(jugada[0] == piece[2] && jugada[1] == piece[3] && jugada[2] == piece[4] && jugada[3] == piece[5]))
                    this.perJugar.add(new int[]{jugada[0], jugada[1], jugada[2], jugada[3]});
            }

        }

        public Node(Node node, int fila, int col, int[] perEll, int[][] tauler){

            this.perJugar = new ArrayList<>();
            this.lliures = new ArrayList<>();
            this.combination = new int[]{fila, col, perEll[0],perEll[1],perEll[2], perEll[3]};
            this.level = node.level + 1;
            this.max = !node.max;
            this.heuristic = 0;

            for(int[] peca : node.perJugar){
                if(!(peca[0] == combination[2] && peca[1] == combination[3] && peca[2] == combination[4] && peca[3] == combination[5])){
                    this.perJugar.add(peca);
                }
            }

            for(int[] posicio : node.lliures){
                if(!(posicio[0] == combination[0] && posicio[1] == combination[1] )){
                    this.lliures.add(posicio);
                }
            }

            // Només vull la informació de l'heurístic
            evalHeuristic(tauler);
            // System.out.println("Node : " + combination[0] + ":"+ combination[1] + ":"+ combination[2] + ":"+ combination[3] + ":"+ combination[4] + ":"+ combination[5]);

        }


        public void generate(int [][] tauler, int[] perPosar){

            nodes = new Node[(STEPS - level) * (STEPS - level - 1)];
            int pos = 0;
            for(int[] posicio : lliures) {
                int  i = posicio[0];
                int j = posicio[1];
                for (int[] perEll : perJugar) {
                    tauler[i][j] = perPosar[0] * 1000 + perPosar[1] * 100 + perPosar[2] * 10 + perPosar[3];
                    nodes[pos] = new Node(this, i, j, new int[]{perEll[0], perEll[1], perEll[2], perEll[3]}, tauler);
                    tauler[i][j] = -1;
                    pos++;
                }

            }
        }

        // TODO : Acabar de mirar aquest tema...
        public void evalHeuristic(int [][] tauler){
            int [][] matriuResultats = new int[10][8];
            for(int i = 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    int valor = tauler[i][j];
                    if(valor != -1){
                        int tamany = valor %2; valor /=10;
                        int forat = valor %2; valor /= 10;
                        int forma = valor %2; valor /=10;
                        int color = valor % 2;

                        int[] peca = new int[]{color, forma, forat, tamany};

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
                if(max){
                    heuristic = 100;
                }
                else{
                    heuristic = -100;
                }
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
                if(max)
                    heuristic = Math.round(global);
                else
                    heuristic = (-1) * Math.round(global);
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
            return this.nodes.length == 0;
        }

        public int getLevel(){
            return this.level;
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


            /*
            // Genero totes les posicions del tauler
            this.perOcupar = new ArrayList<>();
            // GEnero totes les peces que queden per jugar
            this.perJugar = new ArrayList<>();
            for(int i = 0; i < STEPS; i++){
                // Genero totes les peces
                String peca = Integer.toBinaryString( (1 << 4) | i ).substring( 1 );
                this.perOcupar.add(new int[]{i / 4, i % 4, -1, -1, -1, -1});
                int[] jugada = new int[]{-1, -1, peca.charAt(0) - 48, peca.charAt(1) - 48, peca.charAt(2)- 48, peca.charAt(3)- 48};
                if(!(jugada[2] == piece[2] && jugada[3] == piece[3] && jugada[4] == piece[4] && jugada[5] == piece[5]))
                    this.perJugar.add(new int[]{-1, -1, jugada[2], jugada[3], jugada[4], jugada[5]});
            }



            matriuPuntuació = new int[10][8];
            for(int i  = 0; i < matriuPuntuació.length; i++){
                for (int j = 0; j < matriuPuntuació[i].length; j++){
                    matriuPuntuació[i][j] = 0;
                }
            }*/


            /*this.tauler = new int[4][4];
            // Clonem el tauler.
            for(int i= 0; i < 4; i++){
                for(int j = 0; j < 4; j++){
                    this.tauler[i][j] = node.tauler[i][j];
                }
            }*/

    // Hi afegeixo la peça
    //this.tauler[piece[0]][piece[1]] = piece[2] * 1000 + piece[3] * 100 + piece[4] * 10 + piece[5];

    // Extrec la peça que s'ha jugat de les pendents.


            /*


            for(int[] casella : node.perOcupar){
                if(!(casella[0] == jugada[0] && casella[1] == jugada[1])){
                    this.perOcupar.add(casella);
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
*/



/*
        int step = 0;
        if(ultimaJugada == -1){
            int[] posIPeca = new int[]{-1, -1, -1};
            for(int i =0; i < meutaulell.getX(); i++){
                for(int j = 0; j < meutaulell.getY(); j++){
                    int valor = meutaulell.getpos(i, j);
                    if(valor != -1){
                        step++;
                        posIPeca[0] = i;
                        posIPeca[1] = j;
                        posIPeca[2] = valor;
                    }
                }
            }

            // Pot ser que jugui jo o que jugui ell
            if(step == 1) { // Ha jugat ell i m'ha donat la peca per situar-la.

                int valUltimaTrobada = posIPeca[2];
                int tamany = valUltimaTrobada % 2;
                valUltimaTrobada = valUltimaTrobada / 10;
                int forat = valUltimaTrobada % 2;
                valUltimaTrobada = valUltimaTrobada / 10;
                int forma = valUltimaTrobada % 2;
                valUltimaTrobada = valUltimaTrobada / 10;
                int color = valUltimaTrobada % 2;

                // Per tant genero la jugada i situo el node com a primer de l'arbre.
                tree = new Node(tree, new int[]{posIPeca[0], posIPeca[1], color, forma, forat, tamany });
            }

            // Situo l'arbre com a primer node a començar a generar.
            frontera.add(tree);
        }

        //int nodesToGenerate = calculateNodesToGenerate(step);

        generate(calculateNodesToGenerate(step));
*/

/*
        else {

            // Recopero l'última peça que li he donat.
            int lastMovement = tree.getNumericPiece();

            // La busco al taulell per saber on l'ha posat
            int i = 0, j = 0;
            boolean trobat = false;
            while (i != meutaulell.getX()) {
                while (j != meutaulell.getY()) {
                    if (meutaulell.getpos(i, j) == lastMovement) break;
                    j++;
                }
                i++;
            }

            // En aquest moment tinc la posició de la peça que li he donat anteriorment.
            // i = fila,
            // j = columna.
            // Haig de buscar la jugada a dins de l'arbre de nodes (si hi és)

            // Si no l'he trobat haig de genarar-la.


            // Ho tinc tot llest, seguim fent cerca per triar el node.


        }


*/


}
