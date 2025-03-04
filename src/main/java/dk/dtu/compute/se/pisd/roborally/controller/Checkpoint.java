package dk.dtu.compute.se.pisd.roborally.controller;
import dk.dtu.compute.se.pisd.roborally.model.Space;


/**
 * Represents a checkpoint on the board.
 * Players must pass through checkpoints in order.
 */
public class Checkpoint extends FieldAction {

    private final int number;

    public Checkpoint(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "Checkpoint " + number;
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {
        // TODO: Implement logic when a player reaches a checkpoint
        return true;
    }
}
