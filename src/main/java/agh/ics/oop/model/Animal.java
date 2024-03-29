package agh.ics.oop.model;

import agh.ics.oop.model.enums.MapDirection;
import agh.ics.oop.model.interfaces.WorldElement;
import agh.ics.oop.model.interfaces.WorldMap;

public class Animal implements WorldElement {
    // Atrybuty
    private MapDirection orientation;
    private Vector2d position;
    private WorldMap map;
    private Gene gene;
    private int energy;
    private int age = 0;
    private int childCount = 0;
    private int amountOfGrassEaten = 0;

    // Konstruktory

    public Animal(WorldMap map){
        this.orientation = MapDirection.NORTH;
        this.position = new Vector2d(2, 2);
        this.map = map;
    }
    public Animal(WorldMap map, Vector2d initialPosition, Gene gene) {
        this.orientation = MapDirection.NORTH;
        this.position = initialPosition;
        this.map = map;
        this.gene = gene;
    }

    // Settery

    public void setOrientation(MapDirection mapDirection){
        this.orientation = mapDirection;
    }
    public void setEnergy(int energy) { this.energy = energy;}
    public void reduceEnergy(int value){ this.energy -= value; }
    public void addEnergy(int value) { this.energy += value; }
    public void setPosition(Vector2d position){ this.position = position;}
    public void increaseChildCount(){
        this.childCount += 1;
    }
    public void increaseGrassEaten(){
        this.amountOfGrassEaten += 1;
    }
    public void age(){
        this.age += 1;
    }

    // Gettery

    public MapDirection getOrientation() { return orientation; }
    public Vector2d getPosition(){
        return position;
    }
    public Gene getGene() { return this.gene; }
    public int getAge(){
        return this.age;
    }
    public int getEnergy() {
        return energy;
    }
    public int getChildCount(){ return this.childCount;}
    public int getAmountOfGrassEaten(){
        return this.amountOfGrassEaten;
    }

    // Metody

    public boolean isAt(Vector2d second_position) {
        return this.position.equals(second_position);
    }

    public void move() {
        this.orientation = this.orientation.rotate(this.gene.getCurrent());
        Vector2d newPosition = new Vector2d(this.position.getX(), this.position.getY());
        newPosition.add(this.orientation.toUnitVector());

        if (this.map.canMoveTo(newPosition)){
            this.position = newPosition;
        }
    }

    public Vector2d wantToMove(){
        this.orientation = this.orientation.rotate(this.gene.getCurrent());
        Vector2d newPosition = new Vector2d(this.position.getX(), this.position.getY());
        newPosition = newPosition.add(this.orientation.toUnitVector());
        return newPosition;
    }

    public Animal createChild(Animal other, int minMutations, int maxMutations){
        Gene childGene = this.gene.createChild(other.getGene(), this.energy, other.getEnergy(), minMutations, maxMutations);
        Vector2d childPosition = this.position;
        WorldMap childMap = this.map;
        this.increaseChildCount();
        other.increaseChildCount();
        return new Animal(childMap, childPosition, childGene);
    }

    public String toString() {
        return switch(orientation){
            case NORTH -> "N";
            case NORTHEAST -> "NE";
            case EAST -> "E";
            case SOUTHEAST -> "SE";
            case SOUTH -> "S";
            case SOUTHWEST -> "SW";
            case WEST -> "W";
            case NORTHWEST -> "NW";

        };
    }
}
