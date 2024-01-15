package agh.ics.oop;

import agh.ics.oop.model.*;
import agh.ics.oop.model.enums.MapType;
import agh.ics.oop.model.records.SimulationParameters;

public class Simulation implements Runnable{

    // Atrybuty

    private int currentDay;
    private SetupParameters setupParameters;
    private SimulationParameters simulationParameters;
    private AbstractWorldMap worldMap;

    // Konstruktory

    public Simulation(SetupParameters allSetupParameters){
        this.currentDay = 0;
        this.setupParameters = allSetupParameters;
        this.simulationParameters = new SimulationParameters(setupParameters.getWidth(), setupParameters.getHeight(), setupParameters.getMapType(),
                setupParameters.getBehaviourType(), setupParameters.getStartingPlantAmount(), setupParameters.getPlantGrowthRate(), setupParameters.getStartingAnimalAmount(),
                setupParameters.getStartingAnimalEnergy(), setupParameters.getEnergyToReproduce(), setupParameters.getEnergyConsumedByReproduction(), setupParameters.getMinMutations(), allSetupParameters.getMaxMutations(), setupParameters.getGenomeSize());

        if (this.simulationParameters.mapType() == MapType.SphereMap){
            this.worldMap = new SphereWorldMap(simulationParameters.width(), simulationParameters.height(), simulationParameters.behaviourType(), simulationParameters.genomeSize(), simulationParameters.minMutations(), simulationParameters.maxMutations());
        }else{
            this.worldMap = new TunnelWorldMap(simulationParameters.width(), simulationParameters.height(), simulationParameters.behaviourType(), simulationParameters.genomeSize(), simulationParameters.minMutations(), simulationParameters.maxMutations());
        }

        ConsoleMapDisplay consoleMapDisplay = new ConsoleMapDisplay();
        worldMap.addObserver(consoleMapDisplay);

        this.worldMap.generateAnimals(simulationParameters.startingAnimalAmount());
        this.worldMap.setAnimalEnergy(simulationParameters.startingAnimalEnergy());
        this.worldMap.growGrass(simulationParameters.startingPlantAmount());
    }

    // Gettery
    public AbstractWorldMap getWorldMap() {
        return worldMap;
    }

    //Metody

    @Override
    public void run() {
        while (this.worldMap.getAnimalsQuantity() > 0) {
            this.worldMap.reduceAnimalEnergy();
            this.worldMap.removeDead();
            this.worldMap.ageAnimals();
            this.worldMap.advanceAnimals();
            this.worldMap.eat();
            this.worldMap.reproduce();
            this.worldMap.growGrass(simulationParameters.plantGrowthRate());
            this.currentDay += 1;
        }
    }
}
