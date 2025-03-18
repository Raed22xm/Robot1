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
        System.out.println("🚀 Player " + player.getName() + " reached Checkpoint " + number);

        // ✅ Tjek om spilleren allerede har registreret dette checkpoint
        if (number > player.getReachedCheckpoints()) {
            player.incrementCheckpoints();
            System.out.println("✅ Player " + player.getName() + " checkpoint updated: " + player.getReachedCheckpoints());

            // ✅ Opdater det totale antal checkpoints, hvis dette er højere end tidligere sat
            if (number > gameController.getBoard().getTotalCheckpoints()) {
                gameController.getBoard().setTotalCheckpoints(number);
            }

            // ✅ Opdater UI status
            gameController.updateStatusMessage("Player " + player.getName() +
                    " reached Checkpoint " + number + " | Total Reached: " + player.getReachedCheckpoints() + "/" + gameController.getBoard().getTotalCheckpoints());
        }

        // ✅ Hvis det er det sidste checkpoint, afslut spillet
        if (player.getReachedCheckpoints() == gameController.getBoard().getTotalCheckpoints()) {
            gameController.setGamePhase(Phase.FINISHED);
            gameController.showWinnerPopup(player);
            return true;
        }

        return false;
    }



}
