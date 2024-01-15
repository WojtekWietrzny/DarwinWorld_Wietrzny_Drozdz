package agh.ics.oop.model;

public class ConsoleMapDisplay implements MapChangeListener {
    private int updateCount = 0 ;

    @Override
    public synchronized void mapChanged(AbstractWorldMap worldMap) {
        updateCount++;
        System.out.println("Map id: " + worldMap.getId() + " Number of updates: " + updateCount);
        System.out.println("Animals quantity: " + worldMap.getAnimalsQuantity());

        System.out.println(worldMap);

    }
}
