package agh.ics.oop;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimulationEngine implements Runnable{
    private ArrayList<Simulation> simulations;
    private ArrayList<Thread> threads = new ArrayList<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(4);

    public SimulationEngine(ArrayList<Simulation> simulations){
        this.simulations = simulations;
    }

    @Override
    public void run() {
        runAsyncInThreadPool();
    }
    public void runSync(){
        for(Simulation simulation : simulations){
            simulation.run();
        }
    }

    public void runAsync(){
        for(Simulation simulation : this.simulations){
            threads.add(new Thread(simulation));
        }

        for(Thread thread: this.threads){
            thread.start();
        }
    }


    public void runAsyncInThreadPool(){
        for(Simulation simulation : simulations){
            executorService.submit(new Thread(simulation));
        }
        executorService.shutdown();
    }
}