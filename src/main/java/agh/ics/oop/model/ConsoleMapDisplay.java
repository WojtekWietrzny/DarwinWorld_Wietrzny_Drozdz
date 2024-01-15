package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener {
    private int updateCount = 0 ;

    @Override
    public synchronized void mapChanged(AbstractWorldMap worldMap) {
        updateCount++;
        System.out.println("Map id: " + worldMap.getId() + " Number of updates: " + updateCount);
        System.out.println("Animals quantity: " + worldMap.getAnimalsQuantity());
        System.out.println("Empty positions: " + worldMap.getEmptyPositionCount());
        System.out.println("Average energy level: " + worldMap.getAverageAnimalEnergy());
        System.out.println("Average lifespan: " + worldMap.getAverageLifespan());
        System.out.println("Average child count: " + worldMap.getAverageChildCount());
        System.out.println(worldMap);
    }
}
