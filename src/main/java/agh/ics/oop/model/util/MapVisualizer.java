package agh.ics.oop.model.util;

import agh.ics.oop.model.AbstractWorldMap;
import agh.ics.oop.model.MapCell;
import agh.ics.oop.model.Vector2d;
import agh.ics.oop.model.interfaces.WorldMap;


//moved drawHeader to main draw function
//drawObject needed extending, done this in main draw function too


/**
 * The map visualizer converts the {@link WorldMap} map into a string
 * representation.
 *
 * @author apohllo, idzik
 */
public class MapVisualizer {
    private static final String EMPTY_CELL = " ";
    private static final String FRAME_SEGMENT = "-";
    private static final String CELL_SEGMENT = "|";
    private static final String GRASS_CELL = "*";
    private static final String JUNGLE_CELL = "J";
    private final AbstractWorldMap map;

    /**
     * Initializes the MapVisualizer with an instance of map to visualize.
     *
     * @param map
     */
    //gettery
    public MapVisualizer(AbstractWorldMap map) {this.map = map;}

    /**
     * Convert selected region of the map into a string. It is assumed that the
     * indices of the map will have no more than two characters (including the
     * sign).
     *
     * @param lowerLeft  The lower left corner of the region that is drawn.
     * @param upperRight The upper right corner of the region that is drawn.
     * @return String representation of the selected region of the map.
     */
    //Funkcjonalność
    public String draw(Vector2d lowerLeft, Vector2d upperRight) {
        StringBuilder builder = new StringBuilder();
        builder.append(" y\\x ");
        for (int j = lowerLeft.getX(); j < upperRight.getX(); j++) {
            builder.append(String.format("%2d", j));
        }
        builder.append(System.lineSeparator());

        for (int i = upperRight.getY() -1; i >= lowerLeft.getY() ; i--) {
            builder.append(String.format("%3d: ", (i)));
            for (int j = lowerLeft.getX(); j < upperRight.getX(); j++) {
                Vector2d position = new Vector2d(j,i);
                MapCell cell = map.getElement(position);
                if (i < lowerLeft.getY() || i > upperRight.getY()) {
                    builder.append(drawFrame(j <= upperRight.getX() + 1));
                } else {
                    if(cell == null){
                        System.out.println("cell unavailable");
                    }
                    else {
                        builder.append(CELL_SEGMENT);
                        if (j <= upperRight.getX()) {
                            if (!cell.getAnimals().isEmpty()) {
                                builder.append(cell.getFirstAnimal().toString());
                            } else if (cell.isGrassPresent()) {
                                builder.append(GRASS_CELL);
                            } else if (cell.isJungle()) {
                                builder.append(JUNGLE_CELL);
                            } else {
                                builder.append(EMPTY_CELL);
                            }

                        }
                    }
                }
            }
            builder.append(CELL_SEGMENT);
            builder.append(System.lineSeparator());
        }
        return builder.toString();
    }
    private String drawFrame(boolean innerSegment) {
        if (innerSegment) {
            return FRAME_SEGMENT + FRAME_SEGMENT;
        } else {
            return FRAME_SEGMENT;
        }
    }
    public String drawObject(Vector2d position){
        MapCell cell = map.getElement(position);
        if(cell == null){
            return " ";
        }
        else{
            if (!cell.getAnimals().isEmpty()) {
                return cell.getFirstAnimal().toString();
            } else if (cell.isGrassPresent()) {
                return "*" ;
            } else if (cell.isJungle()) {
                return "J" ;
            } else {
                return " ";
            }
        }
    }
}