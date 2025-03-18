package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a conveyor belt on the board that moves players in a specified direction.
 *
 * @author Muhammad Feyaz
 */
public class ConveyorBelt extends FieldAction {

    private Heading heading;

    /**
     * Constructor that requires a heading direction.
     *
     * @param heading The direction the conveyor belt moves players.
     */
    public ConveyorBelt(Heading heading) {
        this.heading = heading;
    }

    /**
     * Default constructor with a predefined heading (NORTH).
     */
    public ConveyorBelt() {
        this.heading = Heading.NORTH; // Default direction
    }

    public Heading getHeading() {
        return heading;
    }

    public void setHeading(Heading heading) {
        this.heading = heading;
    }

    /**
     * Moves the player in the direction of the conveyor belt if possible.
     *
     * @param gameController the game controller handling the board
     * @param space the space where the conveyor belt is located
     * @return true if the player was successfully moved, false otherwise
     */
    @Override
    public boolean doAction(@NotNull GameController gameController, @NotNull Space space) {
        Player player = space.getPlayer();
        if (player == null) {
            return false; // No player on this space
        }

        // Determine the target space based on conveyor belt direction
        Space targetSpace = space.getNeighbor(heading);
        if (targetSpace == null) {
            return false; // No valid space to move to
        }

        // Check if there is a wall blocking movement
        if (space.getWalls().contains(heading)) {
            return false; // Wall is blocking movement
        }

        // Check if the target space is occupied
        if (targetSpace.getPlayer() != null) {
            return false; // Another player is blocking the movement
        }

        // Move the player to the new space
        space.setPlayer(null);
        targetSpace.setPlayer(player);
        player.setSpace(targetSpace);

        // Print a message for debugging
        System.out.println("Player " + player.getName() + " moved via Conveyor Belt to " + targetSpace);

        return true;
    }
}
