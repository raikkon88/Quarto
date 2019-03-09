package Quatro;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class Player {

    protected final int STEPS = 16;
    protected Tauler meutaulell;
    protected Node tree;
    protected int ultimaJugada;
    protected int step;
    protected Random random;
    protected List<int[]> positions;
    protected Set<Integer> pecesGenerades;

    public Player(Tauler entrada){
        meutaulell = entrada;
        random = new Random();
        step = 0;

        this.positions = new ArrayList<int[]>();

        this.pecesGenerades = new HashSet<>();
        for(int i= 0; i < 16; i++){
            String value = Integer.toBinaryString(i);
            pecesGenerades.add(Integer.valueOf(value));
            positions.add(new int[]{i/4, i%4});
        }
    }

    protected int calculateStep(int level){
        if(level >= 0 || level <= 2){
            return 3;
        }
        else if(level > 2 && level < 6){
            return 3;
        }
        else if(level > 5 && level < 10){
            return 4;
        }
        else if(level == 10){
            return 5;
        }

        return 6;

    }

    protected void desordena(List<int[]> llistat, Random random){
        // Sacsegem la bossa de posicions per actuar de manera imprevisible.
        Queue<int[]> tmpPositions = new LinkedBlockingQueue<>();
        while(!llistat.isEmpty()){
            int rand =Math.abs(random.nextInt() % llistat.size());
            tmpPositions.add(llistat.get(rand));
            llistat.remove(rand);
        }

        while (!tmpPositions.isEmpty()){
            llistat.add(tmpPositions.poll());
        }
    }
}
