package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a space on the game board where players and actions exist.
 * This class now includes a method to determine the neighboring space based on heading.
 *
 * @author Muhammad Feyaz
 */
public class Space extends Subject {

    public final Board board;
    public final int x;
    public final int y;
    private Player player;
    private List<Heading> walls = new ArrayList<>();
    private List<FieldAction> actions = new ArrayList<>();

    public Space(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
        this.player = null;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        Player oldPlayer = this.player;
        if (player != oldPlayer && (player == null || board == player.board)) {
            this.player = player;
            if (oldPlayer != null) {
                oldPlayer.setSpace(null);
            }
            if (player != null) {
                player.setSpace(this);
            }
            notifyChange();
        }
    }

    public List<Heading> getWalls() {
        return walls;
    }

    public List<FieldAction> getActions() {
        return actions;
    }

    void playerChanged() {
        notifyChange();
    }

    /**
     * Returns the neighboring space in the given direction.
     * Ensures the coordinates remain within board boundaries.
     *
     * @param heading The direction to check for a neighbor.
     * @return The neighboring space or null if out of bounds.
     */
    public Space getNeighbor(Heading heading) {
        int newX = this.x + heading.getDeltaX();
        int newY = this.y + heading.getDeltaY();

        if (newX >= 0 && newX < board.width && newY >= 0 && newY < board.height) {
            return board.getSpace(newX, newY);
        }
        return null;
    }
}
