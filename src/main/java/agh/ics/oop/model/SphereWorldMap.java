package agh.ics.oop.model;

import agh.ics.oop.model.enums.BehaviourType;
import agh.ics.oop.model.interfaces.WorldElement;
import agh.ics.oop.model.records.Boundary;

import java.util.List;

public class SphereWorldMap extends AbstractWorldMap {

    //Konstruktory
    public SphereWorldMap(int width, int height, BehaviourType behaviourType, int genomeSize, int minMutations, int maxMutations) {
        super(width, height, behaviourType, genomeSize, minMutations, maxMutations);
    }

    // Metody
    @Override
    public void advanceAnimals(){
        for (Animal animal : this.animals){
            Vector2d positionToCheck = animal.wantToMove();
            if (positionToCheck.getX() < 0){
                positionToCheck.setX(this.bounds.upperRight().getX() - 1);
            }
            if (positionToCheck.getX() >= this.bounds.upperRight().getX()){
                positionToCheck.setX(0);
            }
            if (positionToCheck.getY() < 0){
                positionToCheck.setY(0);
                animal.setOrientation(animal.getOrientation().opposite());
            }
            if (positionToCheck.getY() >= this.bounds.upperRight().getY()){
                positionToCheck.setY(this.bounds.upperRight().getY() - 1);
                animal.setOrientation(animal.getOrientation().opposite());
            }

            move(animal, positionToCheck);

        }
        notifyObservers();
    }

    @Override
    public Boundary getCurrentBounds() {
        return bounds;
    }

    @Override
    public boolean isOccupied(Vector2d position) {
        return false;
    }

    @Override
    public WorldElement objectAt(Vector2d position) {
        return null;
    }

    @Override
    public List<Animal> getOrderedAnimals(List<Animal> animals_listed) {
        return null;
    }

    //@Override
    public void move(Animal animal, Vector2d newPosition){
        Vector2d oldPosition = animal.getPosition();
        this.elements.get(oldPosition).removeAnimal(animal);
        animal.setPosition(newPosition);
        this.elements.get(newPosition).addAnimal(animal);
    }

    @Override
    public boolean canMoveTo(Vector2d position) {
        return position.getY() >= super.bounds.lowerLeft().getY() && position.getY() <= super.bounds.upperRight().getY();
    }
}
