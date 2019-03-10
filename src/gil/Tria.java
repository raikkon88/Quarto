/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gil;

import java.util.ArrayList;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author Gil
 */
public class Tria {
    private ArrayList<Tria> fills;
    private int posX;
    private int posY; 
    private int valor;
    private int peca;
    private int pecaAPassar;
    private boolean nivell;
    private int[][] posicions;
    private int[][] files;
    private int[][] columnes;
    private int[][] diagonals;
        
       Tria(){
        posicions = new int[4][4];
        
        files = new int[4][2];
        columnes = new int[4][2];
        diagonals = new int[2][2];
        fills = new ArrayList<Tria>();
        for(int i=0;i<4;i++){
            for(int y=0;y<4;y++){
               posicions[i][y]=-1; 
            }
            for(int j=0;j<2;j++){
                files[i][j]=0;
                columnes[i][j]=0;
                diagonals[(i/2)][j]=0;
            }
        }
       }
       public void show(){
           for(int po=0;po<4;po++){
               System.out.print(files[po][0]+" "+files[po][1]);
           }
           System.out.println("");
           for(int po=0;po<4;po++){
               System.out.print(columnes[po][0]+" "+columnes[po][1]);           
           }
           System.out.println("");
           for(int po=0;po<2;po++){
               System.out.print(diagonals[po][0]+" "+diagonals[po][1]);           
           }
           System.out.println("");
           System.out.println("Valor: "+valor);
       }
       public void setDades(int[][] f,int[][] c,int[][] d){
           for(int i=0;i<4;i++){
               for(int j=0;j<2;j++){
                   files[i][j]=f[i][j];
                   columnes[i][j]=c[i][j];
                   diagonals[i/2][j]=d[i/2][j];
               }
           }
       }
       public void setFills(Tria a){
           fills.add(a);
       }
       public List<Tria> getFills(){
           return fills;
       }
       public int[][] getFV(){
           return files;
       }
       public int[][] getCV(){
           return columnes;
       }
       public int[][] getDV(){
           return diagonals;
       }
       public int getX(){
           return posX;
       }
       public int getY(){
           return posY;
       }
       public int getVal(){
           return valor;
       }
       public void setVal(int v){
           valor = v;
       }
       public void setPecPos(int i,int j){
           posX=i;
           posY=j;
           posicions[i][j]=1;
           files[j][0]+=peca;files[j][1]+=1;
           columnes[i][0]+=peca;columnes[i][1]+=1;
           if(i-j==0){
               diagonals[0][0]+=peca;diagonals[0][1]+=1;
           }else if(i+j==3){
               diagonals[1][0]+=peca;diagonals[1][1]+=1;
           }
       }
       public void setPeca(int p){
           peca = p;
       }
       public int getPeca(){
           return peca;
       }
       public int getPos(int i,int j){
           return posicions[i][j];
       }
       public int[][] getPosicions(){
           return posicions;
       }
       public void setPosicions(int[][] p){
           for(int i=0;i<4;i++){
               for(int j=0;j<4;j++){
                   posicions[i][j]=p[i][j];
               }
           }
       }
       public void setPecaAPassar(int p){
           pecaAPassar=p;
       }
       public int getPecaAPassar(){
           return pecaAPassar;
       }
}
