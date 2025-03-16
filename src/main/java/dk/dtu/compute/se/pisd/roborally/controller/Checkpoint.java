package dk.dtu.compute.se.pisd.roborally.controller;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.model.Phase;



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
        if (space == null || space.getPlayer() == null) {
            return false; // No player on this space
        }

        Player player = space.getPlayer();

        // ✅ Print a message in the console
        System.out.println("Player " + player.getName() + " reached Checkpoint " + number);

        if (number > player.getReachedCheckpoints()) {
            player.incrementCheckpoints();
            gameController.getBoard().setTotalCheckpoints(number); // ✅ Keep track of total checkpoints
            gameController.updateStatusMessage("Player " + player.getName() +
                    " reached Checkpoint " + number + " | Total Reached: " + player.getReachedCheckpoints());
        }


        // ✅ If it's the last checkpoint, show a winner popup
        if (number == gameController.getBoard().getTotalCheckpoints()) {
            gameController.setGamePhase(Phase.FINISHED);
            gameController.showWinnerPopup(player);
            return true;
        }

        return false;
    }



}
