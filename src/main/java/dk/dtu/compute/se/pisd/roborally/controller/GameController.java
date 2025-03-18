/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.scene.control.Alert;
import org.jetbrains.annotations.NotNull;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Command;
import javafx.scene.control.Alert;

import java.util.ArrayList;


/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class GameController {

    final public Board board;

    public GameController(@NotNull Board board) {
        this.board = board;
    }

    /**
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
     *
     * @param space the space to which the current player should move
     */
    public void moveCurrentPlayerToSpace(Space space) {
        // 1. Retrieve the current player from the board
        Player currentPlayer = board.getCurrentPlayer();
        if (currentPlayer == null) {
            return; // Edge case: no current player
        }

        // 2. Check if the clicked space is empty (i.e., has no player)
        if (space.getPlayer() == null) {
            // 3. Clear the old space (if any)
            Space oldSpace = currentPlayer.getSpace();
            if (oldSpace != null) {
                oldSpace.setPlayer(null);
            }

            // 4. Move the current player to the new space
            space.setPlayer(currentPlayer);
            currentPlayer.setSpace(space);

            if(space.getActions() != null) {
                FieldAction action = space.getActions().getFirst();
                if (action instanceof Checkpoint) {
                    Checkpoint checkpoint = (Checkpoint) action;
                    checkpoint.doAction(this,space);
                }
                if(action instanceof ConveyorBelt){
                    ConveyorBelt belt = (ConveyorBelt) action;
                    belt.doAction(this,space);
                }
            }




            // 5. Increment the board’s move counter
            board.setCounter(board.getCounter() + 1);

            // 6. Switch the current player to whoever comes next
            //    (Implementation depends on how 'next player' is determined)

            board.setCurrentPlayer(board.getNextPlayer());

            // If you need the GUI to update immediately:

        }
    }


    // XXX V2
    public void startProgrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }

    // XXX V2
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    // XXX V2
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setCurrentPlayer(board.getPlayer(0));
        board.setStep(0);
    }

    // XXX V2
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    // XXX V2
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    // XXX V2
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    // XXX V2
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }

    // XXX V2
    private void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }

    // XXX V2
    private void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (card != null) {
                    Command command = card.command;
                    executeCommand(currentPlayer, command);
                }
                int nextPlayerNumber = board.getPlayerNumber(currentPlayer) + 1;
                if (nextPlayerNumber < board.getPlayersNumber()) {
                    board.setCurrentPlayer(board.getPlayer(nextPlayerNumber));
                } else {
                    step++;
                    if (step < Player.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                        board.setCurrentPlayer(board.getPlayer(0));
                    } else {
                        startProgrammingPhase();
                    }
                }
            } else {
                // this should not happen
                assert false;
            }
        } else {
            // this should not happen
            assert false;
        }
    }

    // XXX V2

    /**
     * Executes a command for the specified player if the conditions are met
     * (player is not null, belongs to the current board, and the command is valid).
     * Depending on the type of command, the player will move or turn accordingly.
     *
     * @param player the player for whom the command is being executed; must not be null
     * @param command the command to execute; must not be null
     */
    private void executeCommand(@NotNull Player player, Command command) {
        // Check if the player and the command are valid
        if ( player.board == board && command != null) {
            // XXX This is a very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).

            switch (command) {
                case BACKWARD:
                    this.moveBackward( player);
                    break;
                case FORWARD:
                    this.moveForward( player);
                    break;
                case RIGHT:
                    this.turnRight(player);
                    break;
                case LEFT:
                    this.turnLeft(player);
                    break;
                case FAST_FORWARD:
                    this.fastForward(player);
                case U_TURN:
                    this.uTurn(player);
                    break;
                default:
                    // DO NOTHING (for now)
            }
        }
    }

    // TODO V2
    public void moveForward(@NotNull Player player) {
        // Check if the player is valid and belongs to the current board
     if(player.board == board){
         Space space = player.getSpace(); // Get the current space of the player
         Heading heading = player.getHeading(); // Get the current heading of the player
         if (space != null && heading != null) {
             // Get the next space based on the player's heading
             Space target = board.getNeighbour(space, heading);
             if (target != null) {
                 // Move the player to the next space
                 this.moveCurrentPlayerToSpace(target);
             }
         }

     }
    }

    // TODO V2

    /**
     * Moves the specified player forward by up to two spaces in the direction of their current heading.
     * If the first target space exists, the player moves to it. If the second target space also exists
     * after the first move, the player advances to it as well.
     *
     * @param player the player to be moved forward; must not be null
     * @author Raed
     */
    public void fastForward(@NotNull Player player) {
     moveForward(player);
     moveForward(player);
    }

    // TODO V2

    /**
     * Turns the specified player 90 degrees to the right.
     * The player's heading will be updated to the next direction
     * in the clockwise order on the board.
     * @param player the player whose heading is to be adjusted; must not be null
     * @author Raed
     */
    public void turnRight(@NotNull Player player) {
      if (player.board == board) {
          player.setHeading(player.getHeading().next());
      }
    }

    // TODO V2

    /**
     * Turns the specified player 90 degrees to the left.
     * The player's heading will be updated to the previous direction
     * in the counter-clockwise order on the board.
     *
     * @param player the player whose heading is to be adjusted; must not be null
     * @author Raed
     */
    public void turnLeft(@NotNull Player player) {
     if(player.board == board){
         player.setHeading(player.getHeading().prev());
     }
    }

    /**
     * Moves the specified player one step backward based on their current heading.
     * @param player the player to move backward; must not be null
     * @author Raed
     */
    public void moveBackward(@NotNull Player player) {
     // Insur that the player is valid and belongs to the current board
        if(player.board == board){
            Space space = player.getSpace(); // Get the current space of the player
            Heading oppositeHeading = player.getHeading().opposite();// Get the current heading of the player
            if (space != null && oppositeHeading != null) {
                // Get the next space based on the player's heading
                Space target = board.getNeighbour(space, oppositeHeading);
                if (target != null) {
                    // Move the player to the next space
                    this.moveCurrentPlayerToSpace(target);
                }
            }
        }
    }

    /**
     * Adjusts the player's heading by performing a U-turn. The player's heading
     * will be changed to the opposite direction from its current heading.
     * @param player the player performing the U-turn; must not be null
     * @author Raed
     */
    public void uTurn(@NotNull Player player) {
        player.setHeading(player.getHeading().opposite());

    }

    /**
     * Moves a command card from the source field to the target field if the source field contains
     * a card and the target field is empty. After the operation, the source field is cleared and
     * the target field holds the card from the source.
     *
     * @param source the CommandCardField containing the card to be moved, must not be null
     * @param target the CommandCardField where the card will be placed, must not be null
     * @return true if the card was successfully moved, false otherwise
     */
    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
       //get the cards from both source and target
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        //check if the source card is not null and the target card is null
        //so meaning the source card has a card and target is empty
        if (sourceCard != null && targetCard == null) {
            //move the card from source to target
            target.setCard(sourceCard);
            //remove the card from source, meaning clear the sources filed
            source.setCard(null);
            return true;
        } else {
            //Cannot move cards if source is empty  or target is not empty
            return false;
        }
    }

    /**
     * A method called when no corresponding controller operation is implemented yet.
     * This should eventually be removed.
     */
    public void notImplemented() {
        // XXX just for now to indicate that the actual method is not yet implemented
        assert false;
    }

    public void showWinnerPopup(Player player) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText("Winner!");
        alert.setContentText(player.getName() + " has won the game!");
        alert.showAndWait();
    }
    public Board getBoard() {
        return board;
    }
    public void setGamePhase(Phase phase) {
        board.setPhase(phase);
    }

    /**
     * Updates the status message in the game UI based on the current game state.
     * This message typically shows whose turn it is and what phase the game is in.
     *
     * @param message The message to display in the status area.
     */
    public void updateStatusMessage(String message) {
        System.out.println("[STATUS] " + message); // ✅ Print message in console for debugging
        board.setStatusMessage(message); // ✅ Make sure `board.setStatusMessage()` exists
    }




}
