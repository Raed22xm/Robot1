package dk.dtu.compute.se.pisd.roborally.model;

/**
 * Represents the four possible movement directions in the game.
 * Provides utility methods to determine movement changes in x and y coordinates.
 *
 * @author Muhammad Feyaz
 */
public enum Heading {

    SOUTH, WEST, NORTH, EAST;

    public Heading next() {
        return values()[(this.ordinal() + 1) % values().length];
    }

    public Heading prev() {
        return values()[(this.ordinal() + values().length - 1) % values().length];
    }

    /**
     * Determines the opposite heading relative to the current one.
     *
     * @return the opposite Heading of the current enum constant
     */
    public Heading opposite() {
        switch (this) {
            case NORTH: return SOUTH;
            case SOUTH: return NORTH;
            case EAST: return WEST;
            case WEST: return EAST;
            default: throw new IllegalStateException("Unexpected heading: " + this);
        }
    }

    /**
     * Returns the change in the X-coordinate when moving in this direction.
     *
     * @return the change in the X-axis
     */
    public int getDeltaX() {
        switch (this) {
            case EAST: return 1;
            case WEST: return -1;
            default: return 0;
        }
    }

    /**
     * Returns the change in the Y-coordinate when moving in this direction.
     *
     * @return the change in the Y-axis
     */
    public int getDeltaY() {
        switch (this) {
            case NORTH: return -1;
            case SOUTH: return 1;
            default: return 0;
        }
    }
}
