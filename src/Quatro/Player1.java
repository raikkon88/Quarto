package Quatro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * @author Usuari
 */
public class Player1 {
    
    private int step;   
    private Tauler meutaulell;
    boolean[] restPieces;
    ArrayList<Integer> restPlaces; 
    private Node tree;
    
    Player1(Tauler entrada){
        meutaulell = entrada;
        tree = new Node(false, "");
        step = 0;
        restPieces = new boolean[16];
        restPlaces = new ArrayList<>();
        for(int i = 0; i < 16; i++){
            restPieces[i] = true;
            restPlaces.add(i);
        }
    }
    
    private int minIMax(Node node, int deep){
        int finalValue; 
        if(enoughtDeep(deep)) return heuristic(node);
        generate(node);
        if(node.nodes.size() == 0) return heuristic(node);
        
        if(node.isMin()){
            finalValue = Integer.MAX_VALUE;
            for(Node n : node.nodes){
                int value = minIMax(n, deep -1);
                if(finalValue > value) finalValue = value;
            }
        }
        else {
            finalValue = Integer.MIN_VALUE;
            for(Node n : node.nodes){
                int value = minIMax(n, deep -1);
                if(finalValue < value) finalValue = value;
            }
        }
        return finalValue;
    }
   
    private int heuristic(Node node){
        return 1;
    }
    
    private void generate(Node node){
        if(node.isMin()){
            for(int i = 0; i < restPlaces.size(); i++){
                int value = restPlaces.get(i);
                node.addNode(new Node(true, "" + value/4 + value%4));
            }
        }
        else{
            for(int i=0; i < restPlaces.size(); i++){
                int value = restPlaces.get(i);
                for(int piece = 0; piece < restPieces.length; piece++){
                    if(restPieces[piece]){
                        node.addNode(new Node(false, "" + value/4 + value%4 + piece));
                    }
                }
            }
        }
        
    }
    
    private boolean enoughtDeep(int level){
        return level == 0;
    }

    public int[] tirada(int colorin, int formain, int foratin, int tamanyin){
    //colorin - Color de la peça a colocar -> 	0 = Blanc 	1 = Negre
    //formain - Forma de la peça a colocar -> 	0 = Rodona 	1 = Quadrat
    //foratin - Forat de la peça a colocar -> 	0 = No  	1 = Si
    //tamanyin - Forat de la peça a colocar -> 0 = Petit 	1 = Gran
        
        int value = minIMax(this.tree, 3);
        return new int[]{0,0,0,0,0,0};
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
    

    private class Node implements Comparable<Node>{
        
        String name;
        Integer heuristic;
        boolean max;
        Set<Node> nodes;
        // TODO : Li falten el rest places i el rest pieces
        
        public Node(boolean max, String name){
            this.max = max;
            this.nodes = new HashSet<Node>();
            this.heuristic = 0;
            this.name = name;
        }
        
        public void setHeuristic(int heuristic){
            this.heuristic = heuristic;
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
        
        public void addNode(Node node){
            this.nodes.add(node);
        }
        
        @Override
        public int hashCode(){
            return name.hashCode();
        }

        @Override
        public int compareTo(Node node) {
            return this.heuristic.compareTo(node.heuristic);
        }
    }
    
    
}
