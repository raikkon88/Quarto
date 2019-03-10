/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Gil;

import Quatro.Tauler;

import java.util.*;

/**
 * * @author Usuari
 */
public class Gil {
    /**
     Classe que em serveix per controlar les opcions per tirar una peca
     **/

    private Tauler meutaulell;
    private ArrayList<Integer> peces;
    //private Tria triada;
    private Tria triaP1;
    //private Tria triada;
    private int[][] filesG;
    private int[][] columnesG;
    private int[][] diagonalsG;

    public Gil(Tauler entrada){

        meutaulell = entrada;
        peces = new ArrayList<Integer>();
        peces.add(0);peces.add(1);peces.add(10);peces.add(11);peces.add(100);peces.add(101);peces.add(110);peces.add(111);
        peces.add(1000);peces.add(1001);peces.add(1010);peces.add(1011);peces.add(1100);peces.add(1101);peces.add(1110);peces.add(1111);
        Collections.shuffle(peces);
        filesG = new int[4][2];
        columnesG= new int[4][2];
        diagonalsG= new int[4][2];
        for(int i=0;i<4;i++){
            for(int j=0;j<2;j++){
                filesG[i][j]=0;
                columnesG[i][j]=0;
                diagonalsG[(i/2)][j]=0;
            }
        }
    }

    public void actualitzarDadesTauler(){
        for(int p=0;p<4;p++){
            columnesG[p][0]=0;columnesG[p][1]=0;
            filesG[p][0]=0;filesG[p][1]=0;
            diagonalsG[p][0]=0;diagonalsG[p][1]=0;
        }
        for(int i=0;i<meutaulell.getX();i++){
            for(int j=0;j<meutaulell.getY();j++){
                if (meutaulell.getpos(i,j) != -1){
                    int val = meutaulell.getpos(i, j);

                    int idx = peces.indexOf(val);
                    if(idx!=-1){
                        peces.remove(idx);
                    }
                    columnesG[i][0]+=val;columnesG[i][1]+=1;
                    filesG[j][0]+=val;filesG[j][1]+=1;
                    if(i-j==0){
                        diagonalsG[0][0]=val;diagonalsG[0][1]+=1;
                    }else if(i+j==3){
                        diagonalsG[1][0]=val;diagonalsG[1][1]+=1;
                    }
                }
            }
        }
    }


    public int[] tirada(int colorin, int formain, int foratin, int tamanyin){
        //colorin - Color de la peça a colocar -> 	0 = Blanc 	1 = Negre
        //formain - Forma de la peça a colocar -> 	0 = Rodona 	1 = Quadrat
        //foratin - Forat de la peça a colocar -> 	0 = No  	1 = Si
        //tamanyin - Forat de la peça a colocar -> 0 = Petit 	1 = Gran

        int x,y,color,forma,forat,tamany;
        color=-1;
        forma=-1;
        forat=-1;
        tamany=-1;
        boolean trobat=true;
        
        /*while( trobat){
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
        }*/

        actualitzarDadesTauler();

        int peca= colorin*1000+formain*100+foratin*10+tamanyin;
        int idx = peces.indexOf(peca);
        peces.remove(idx);
        //triada = new Tria();
        triaP1 = new Tria();
        triaP1.setDades(filesG,columnesG,diagonalsG);
        triaP1.setPosicions(meutaulell.getTaulell());
        triaP1.setPecaAPassar(peca);
        int n = 2;
        if(peces.size()<12)n=4;
        //minimax(triaP1,peces,n,1);
        alfabeta(triaP1,peces,3,3,1,Integer.MIN_VALUE,Integer.MAX_VALUE);
        //peces.remove(peces.indexOf(triaP1.getPecaAPassar()));
        int[] pecaAPassar = convertirNumAtributsPeca(triaP1.getPecaAPassar());
        return new int[]{triaP1.getX(),triaP1.getY(),pecaAPassar[0],pecaAPassar[1],pecaAPassar[2],pecaAPassar[3]};

        //Un retorn per defecte
        //return new int[]{0,0,0,0,0,0};
        //format del retorn vector de 6 int {posX[0a3], posY[0a3], color[0o1] forma[0o1], forat[0o1], tamany[0o1]}
        //posX i posY es la posicio on es coloca la peça d'entrada
        //color forma forat i tamany descriuen la peça que colocara el contrari
        //color -  	0 = Blanc 	1 = Negre
        //forma -  	0 = Rodona 	1 = Quadrat
        //forat - 	0 = No  	1 = Si
        //tamany -      0 = Petit 	1 = Gran
    }

    private int[] convertirNumAtributsPeca(int p){
        int valor = p;
        int cl=(int) (valor/1000);
        valor = valor - cl * 1000;
        int fr=(int) (valor%1000/100);
        valor = valor - fr * 100;
        int fo=(int) (valor%1000/10);
        valor = valor - fo * 10;
        int tm=(int) (valor%1000/1);

        return new int[]{cl,fr,fo,tm};
    }


    //funcio heuristica

    private int heuristica(Tria t,int player){
        // mirar en quines files,columnes o diagonals guanya o fa 2.
        int valor = 0;
        for(int a=0;a<4;a++){
            int[] fila = convertirNumAtributsPeca(t.getFV()[a][0]);
            int[] columna = convertirNumAtributsPeca(t.getCV()[a][0]);
            int[] diagonal = convertirNumAtributsPeca(t.getDV()[a/2][0]);
            if(t.getFV()[a][1]==4){
                for(int v=0;v<4;v++){
                    if(fila[v]==4 || fila[v]==0){

                        valor += 1;
                        if(player==1){
                            valor-=1000;
                        }
                    }
                }
            }
            if(t.getCV()[a][1]==4){
                for(int v=0;v<4;v++){
                    if(columna[v]==4 || columna[v]==0){
                        valor += 1;
                        if(player==1){
                            valor-=1000;
                        }
                    }
                }
            }
            if(t.getDV()[a/2][1]==4){
                for(int v=0;v<4;v++){
                    if(diagonal[v]==4 || diagonal[v]==0){
                        valor += 1;
                        if(player==1){
                            valor-=1000;
                        }
                    }
                }
            }
        }
        t.setVal(valor);
        return valor;
    }

    //generarMoviments
    private List<Tria> generaMoviments(Tria t,List<Integer> p){
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                if (t.getPos(i,j) == -1){
                    for(int g=0;g<p.size();g++){
                        Tria t1 = new Tria();
                        t1.setDades(t.getFV(),t.getCV(),t.getDV());
                        t1.setPosicions(t.getPosicions());
                        t1.setPeca(t.getPecaAPassar());
                        t1.setPecPos(i,j);
                        t1.setPecaAPassar(p.get(g));
                        t.setFills(t1);
                    }
                }
            }
        }

        return t.getFills();
    }

    private int minimax(Tria t,List<Integer> pcs,int nivell,int player){
        int millor;
        if(nivell == 0){
            return heuristica(t,player);

        }else{
            generaMoviments(t,pcs);
            if(t.getFills().isEmpty()){
                return heuristica(t,player);
            }else{
                if(player==0){
                    millor=Integer.MAX_VALUE;
                    for(Tria tria : t.getFills()){
                        ArrayList<Integer> pecescp = new ArrayList<Integer>(pcs);
                        int idx = pecescp.indexOf(tria.getPecaAPassar());
                        pecescp.remove(idx);
                        int valor = minimax(tria,pecescp,nivell-1,1);
                        if(valor<millor){
                            millor=valor;
                        }
                    }
                    millor=(millor*-1);
                }else{
                    millor=Integer.MIN_VALUE;
                    for(Tria tria : t.getFills()){
                        ArrayList<Integer> pecescp = new ArrayList<Integer>(pcs);
                        int idx = pecescp.indexOf(tria.getPecaAPassar());
                        pecescp.remove(idx);
                        int valor = minimax(tria,pecescp,nivell-1,0);
                        if(valor>millor){
                            millor = valor;
                            triaP1=tria;

                        }
                    }

                }
                return millor;
            }
        }
    }



    private int alfabeta(Tria t,List<Integer> pcs,int nivell,int nivellInicial,int player,int alfa, int beta){
        if(nivell == 0){
            return heuristica(t,player);

        }else{
            generaMoviments(t,pcs);
            if(t.getFills().isEmpty()){
                return heuristica(t,player);
            }else{
                if(player==0){
                    for(Tria tria : t.getFills()){
                        if(alfa>=beta) break;
                        ArrayList<Integer> pecescp = new ArrayList<Integer>(pcs);
                        int idx = pecescp.indexOf(tria.getPecaAPassar());
                        pecescp.remove(idx);
                        int valor = alfabeta(tria,pecescp,nivell-1,nivellInicial,1,alfa,beta);
                        if(valor<beta){
                            beta=valor;
                        }
                    }
                    t.setVal(beta);
                    return beta;
                }else{
                    for(Tria tria : t.getFills()){
                        if(alfa>=beta) break;
                        ArrayList<Integer> pecescp = new ArrayList<Integer>(pcs);
                        int idx = pecescp.indexOf(tria.getPecaAPassar());
                        pecescp.remove(idx);
                        int valor = alfabeta(tria,pecescp,nivell-1,nivellInicial,0,alfa,beta);
                        if(valor>alfa){
                            alfa = valor;
                            if(nivell==nivellInicial){
                                triaP1=tria;
                            }
                        }
                    }
                    t.setVal(alfa);
                    return alfa;
                }
            }
        }
    }

}