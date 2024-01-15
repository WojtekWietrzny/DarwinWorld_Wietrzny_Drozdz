package agh.ics.oop.model;

import agh.ics.oop.model.enums.MapDirection;

import java.util.Objects;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Vector2d {
    //Atrybuty
    private int x;
    private int y;

    //Konstruktory
    public Vector2d(int x, int y) {
        this.x = x;
        this.y = y;
    }

    //gettery
    public int getX() {return x;}
    public int getY() {return y;}

    //settery
    public void setX(int val){this.x = val;}
    public void setY(int val){this.y = val;}

    //Funkcjonalność
    public String toString(){return "(" + this.x + "," + this.y + ")";}
    public boolean precedes(Vector2d other){return this.x <= other.x && this.y <= other.y;}
    public boolean follows(Vector2d other){return this.x >= other.x && this.y >= other.y;}

    public Vector2d add(Vector2d other){
        int x = this.x + other.x;
        int y = this.y + other.y;
        return new Vector2d(x,y);
    }
    public Vector2d subtract(Vector2d other){
        int x = this.x - other.x;
        int y = this.y - other.y;
        return new Vector2d(x,y);
    }

    public Vector2d upperRight(Vector2d other){
        return new Vector2d (max(this.x,other.x), max(this.y, other.y));
    }
    public Vector2d lowerLeft(Vector2d other){
        return new Vector2d (min(this.x,other.x), min(this.y, other.y));
    }
    public Vector2d opposite(){
        int x = -this.x;
        int y = -this.y;
        return new Vector2d(x,y);
    }
    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof Vector2d))
            return false;
        Vector2d that = (Vector2d) other;

        // Porównujemy pola obu obiektów
        return this.x == that.x && this.y == that.y;
    }
    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y);
    }
}
