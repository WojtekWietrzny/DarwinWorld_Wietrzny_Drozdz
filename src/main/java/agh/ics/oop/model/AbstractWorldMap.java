package agh.ics.oop.model;

import agh.ics.oop.model.enums.BehaviourType;
import agh.ics.oop.model.interfaces.MapChangeListener;
import agh.ics.oop.model.interfaces.WorldMap;
import agh.ics.oop.model.records.Boundary;
import agh.ics.oop.model.util.MapVisualizer;

import java.util.*;

public abstract class AbstractWorldMap implements WorldMap {

    //Atrybuty
    protected final Map<Vector2d, MapCell> elements = new HashMap<>();
    protected final List<Animal> animals = new ArrayList<>();
    protected List<Vector2d> allPositions = new ArrayList<>();
    protected List<Vector2d> emptyPositionsPreferred = new ArrayList<>();
    protected List<Vector2d> emptyPositionsNotPreferred = new ArrayList<>();
    protected int animalsQuantity = 0;
    protected int totalAnimalEnergy = 0;
    protected int totalDeadAnimalAge = 0;
    protected int deathCount = 0;
    private final int energyToReproduce = 1;
    private final int energyConsumedByReproduction = 2;
    private final int grassNutritionalValue = 3;
    private final BehaviourType behaviourType;
    private final int genomeSize;
    private final int minMutations;
    private final int maxMutations;
    private Map<ArrayList<Integer>, Integer> genotypes = new HashMap<>();
    private ArrayList<Integer> mostPopularDna = null;
    protected final Boundary bounds;
    private final List<MapChangeListener> observers = new ArrayList<>();
    private static int nextId = 0;
    private final int id = nextId++;

    //Konstruktory
    public AbstractWorldMap(int width, int height, BehaviourType behaviourType, int genomeSize, int minMutations, int maxMutations){
        Vector2d lowerLeft = new Vector2d(0,0);
        Vector2d upperRight = new Vector2d(width, height);
        Boundary mapBounds = new Boundary(lowerLeft, upperRight);
        this.bounds = mapBounds;
        this.behaviourType = behaviourType;
        this.genomeSize = genomeSize;
        this.minMutations = minMutations;
        this.maxMutations = maxMutations;
        float midPoint = Math.round(height/2);
        startMap(width, height);
        allPositions.sort((o1, o2) -> Float.compare(Math.abs(o1.getY() - midPoint), Math.abs(o2.getY() - midPoint)));

        for(int i = 0; i < (int) Math.round(0.2*width*height);i++){
            Vector2d position = allPositions.get(i);
            emptyPositionsPreferred.add(position);
        }
        for(int j = (int) Math.round(0.2*width*height); j < allPositions.size();j++){
            Vector2d position = allPositions.get(j);
            emptyPositionsNotPreferred.add(position);
        }
        for(Vector2d position : emptyPositionsPreferred){
            this.elements.get(position).setJungle();
        }

    }
    private void startMap(int width, int height){
        for(int i = 0; i < width;i++){
            for(int j = 0; j < height; j++){
                Vector2d position = new Vector2d(i,j);
                MapCell cell = new MapCell();
                elements.put(position, cell);
                allPositions.add(position);
            }
        }
    }

    //gettery
    public ArrayList<Integer> getMostPopularDna() {return mostPopularDna;}
    public MapCell getElement(Vector2d position) {return elements.get(position);}
    public int getAnimalsQuantity(){return animalsQuantity;}
    public List<Vector2d> getEmptyPositionsNotPreferred() {return emptyPositionsNotPreferred;}
    public List<Vector2d> getEmptyPositionsPreferred() {return emptyPositionsPreferred;}
    public float getAverageLifespan(){return (float) this.totalDeadAnimalAge / this.deathCount;}
    @Override
    public abstract List<Animal> getOrderedAnimals(List<Animal> animals_listed);
    @Override
    public Boundary getCurrentBounds() {return bounds;}
    @Override
    public int getId() {return id;}
    public int getGrassCount(){
        int positionsAmount = bounds.upperRight().getX() * bounds.upperRight().getY();
        return positionsAmount  - emptyPositionsPreferred.size() - emptyPositionsNotPreferred.size();
    }
    public int getEmptyPositionCount(){
        int answ = 0;
        for(MapCell mapCell : this.elements.values()){
            if (!mapCell.isOccupied()){
                answ += 1;
            }
        }
        return answ;
    }
    public float getAverageAnimalEnergy(){
        if (animalsQuantity == 0){
            return 0;
        }
        return (float) this.totalAnimalEnergy /this.animalsQuantity;
    }
    public float getAverageChildCount(){
        if (animalsQuantity == 0){
            return 0;
        }
        int totalChildren = 0;
        for (Animal animal  : this.animals){
            totalChildren += animal.getChildCount();
        }
        return (float) totalChildren / this.animalsQuantity;
    }
    //settery
    public void setAnimalEnergy(int energy){
        this.totalAnimalEnergy = 0;
        for (Animal animal : this.animals){
            animal.setEnergy(energy);
            this.totalAnimalEnergy += energy;
        }
    }
    //logika do Obserwatora
    public  void addObserver(MapChangeListener observer){
        observers.add(observer);
    }
    public  void removeObserver(MapChangeListener observer){
        observers.remove(observer);
    }
    protected void notifyObservers() {
        for (MapChangeListener observer : observers) {
            observer.mapChanged(this);
        }
    }
    //Funkcjonalność
    public void generateAnimals(int amount){
        Random random = new Random();
        int x, y;
        for(int i=0; i<amount; i++){
            x = random.nextInt(this.bounds.upperRight().getX() - this.bounds.lowerLeft().getX()) + this.bounds.lowerLeft().getX();
            y = random.nextInt(this.bounds.upperRight().getY() - this.bounds.lowerLeft().getY()) + this.bounds.lowerLeft().getY();
            Animal animal = new Animal(this, new Vector2d(x, y), Gene.generateRandomGene(this.genomeSize, this.behaviourType));
            place(animal);
        }
        notifyObservers();
    }
    public void advanceAnimals(){}
    public boolean canMoveTo(Vector2d position){
        return position.follows(bounds.lowerLeft()) && position.precedes(bounds.upperRight());
    }

    public void place(Animal animal) {
        if(canMoveTo(animal.getPosition())){
            this.animalsQuantity += 1;
            this.totalAnimalEnergy += animal.getEnergy();

            if (this.genotypes.containsKey(animal.getGene().getDna())){
                this.genotypes.put(animal.getGene().getDna(), this.genotypes.get(animal.getGene().getDna()) + 1);
            }else{
                this.genotypes.put(animal.getGene().getDna(), 1);
            }
            if (this.mostPopularDna == null){
                this.mostPopularDna = animal.getGene().getDna();
            }else{
                if (this.genotypes.get(animal.getGene().getDna()) > this.genotypes.get(this.mostPopularDna)){
                    this.mostPopularDna = animal.getGene().getDna();
                }
            }


            elements.get(animal.getPosition()).addAnimal(animal);
            animals.add(animal);
        }
    }
    public void move(Animal animal, Vector2d newPosition){
        Vector2d oldPosition = animal.getPosition();
        this.elements.get(oldPosition).removeAnimal(animal);
        animal.setPosition(newPosition);
        this.elements.get(newPosition).addAnimal(animal);
    }
    public void reduceAnimalEnergy(){
        for (Animal animal : this.animals){
            animal.reduceEnergy(1);
            if (animal.getEnergy() >= 0){
                this.totalAnimalEnergy -= 1;
            }
        }
    }
    public void eat(){
        for (Animal animal : this.animals){
            Vector2d position = animal.getPosition();
            if (this.elements.get(position).isGrassPresent()){
                this.elements.get(position).eatGrass();
                animal.addEnergy(this.grassNutritionalValue);
                animal.increaseGrassEaten();
                this.totalAnimalEnergy += this.grassNutritionalValue;
                if(this.elements.get(position).isJungle()){
                    emptyPositionsPreferred.add(position);
                }
                else{
                    emptyPositionsNotPreferred.add(position);
                }
            }
        }

    }
    public void ageAnimals(){
        for (Animal animal : this.animals){
            animal.age();
        }
    }
    public void removeDead(){
        ArrayList<Animal> animalsToRemove = new ArrayList<>();
        for (Animal animal : this.animals){
            if (animal.getEnergy() < 0){
                this.elements.get(animal.getPosition()).animalDied(animal);
                animalsToRemove.add(animal);
            }
        }
        for (Animal animal : animalsToRemove){
            this.animals.remove(animal);
            this.animalsQuantity -= 1;
            this.deathCount += 1;
            this.totalDeadAnimalAge += animal.getAge();
        }
    }
    public void reproduce(){
        ArrayList<Animal> children = new ArrayList<>();
        for(int i=0; i< animalsQuantity; i++){
            Animal potentialParent1 = this.animals.get(i);
            if (potentialParent1.getEnergy() < this.energyToReproduce){
                continue;
            }
            for (int j=i+1; j<animalsQuantity; j++){
                Animal potentialParent2 = this.animals.get(j);
                if (potentialParent1 == potentialParent2){
                    continue;
                }
                if(!potentialParent1.getPosition().equals(potentialParent2.getPosition())) {
                    continue;
                }
                if (potentialParent2.getEnergy() < this.energyToReproduce){
                    continue;
                }
                Animal child = potentialParent1.createChild(potentialParent2, this.minMutations, this.maxMutations);
                potentialParent1.reduceEnergy(this.energyConsumedByReproduction);
                potentialParent1.increaseChildCount();
                potentialParent2.reduceEnergy(this.energyConsumedByReproduction);
                potentialParent2.increaseChildCount();
                child.setEnergy(2*energyConsumedByReproduction);
                children.add(child);
                this.totalAnimalEnergy = this.totalAnimalEnergy - 2*this.energyConsumedByReproduction + child.getEnergy();
            }
        }

        for (Animal child : children){
            place(child);
        }
    }
    //funkcja odpowiedzialna za losowanie kolejnego pola na trawę
    public Vector2d randomNextPosition(){
        Random random = new Random();
        Vector2d position;
        int number = random.nextInt(100);
        if (number < 80){
            if(!(emptyPositionsPreferred.isEmpty())) {
                position = emptyPositionsPreferred.get(random.nextInt(emptyPositionsPreferred.size()));
                emptyPositionsPreferred.remove(position);
            }
            else{
                position = emptyPositionsNotPreferred.get(random.nextInt(emptyPositionsNotPreferred.size()));
                emptyPositionsNotPreferred.remove(position);
            }
        }
        else{
            if(!(emptyPositionsNotPreferred.isEmpty())) {
                position = emptyPositionsNotPreferred.get(random.nextInt(emptyPositionsNotPreferred.size()));
                emptyPositionsNotPreferred.remove(position);
            }
            else{
                position = emptyPositionsPreferred.get(random.nextInt(emptyPositionsPreferred.size()));
                emptyPositionsPreferred.remove(position);
            }

        }
        return position;
    }
    //sprawdzanie czy istnieją miejsca na trawę
    public boolean freePlaces(){
            return  ((emptyPositionsNotPreferred.size() > 0) || (emptyPositionsPreferred.size() > 0));
    }
    public void addGrass(Vector2d position){
        MapCell cell = elements.get(position);
        cell.growGrass();
    }
    //wyrastanie określonej ilości trawy - cała 1 faza dnia
    public void growGrass(int grassDaily){
        for(int i=0; i < grassDaily; i++){
            if(freePlaces()){
                Vector2d position = randomNextPosition();
                addGrass(position);
            }
        }
    }
    public String toString() {
        return new MapVisualizer(this).draw(this.getCurrentBounds().lowerLeft(), getCurrentBounds().upperRight());
    }
}
