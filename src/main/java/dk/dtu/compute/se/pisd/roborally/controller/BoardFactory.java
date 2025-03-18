package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.controller.Checkpoint;


import java.util.List;
import java.util.Collections;

/**
 * A factory for creating boards. The factory itself is implemented as a singleton.
 *
 * @author Muhammad Feyaz
 */
public class BoardFactory {

    private static BoardFactory instance = null;
    private static final List<String> BOARD_NAMES = List.of("SimpleBoard", "AdvancedBoard");

    private BoardFactory() {
    }

    public static BoardFactory getInstance() {
        if (instance == null) {
            instance = new BoardFactory();
        }
        return instance;
    }

    public List<String> getBoardNames() {
        return Collections.unmodifiableList(BOARD_NAMES);
    }

    public Board createBoard(String name) {
        if (name == null) {
            name = "<none>";
        }
        Board board = new Board(8, 8, name);

        // 🔹 Tilføj vægge
        addWall(board, 2, 2, Heading.WEST);
        addWall(board, 4, 4, Heading.EAST);
        addWall(board, 1, 1, Heading.WEST);
        addWall(board, 5, 5, Heading.SOUTH);

        // 🔹 Tilføj Conveyor Belts
        addConveyorBelt(board, 3, 3, Heading.NORTH);
        addConveyorBelt(board, 2, 4, Heading.EAST);
        addConveyorBelt(board, 5, 5, Heading.SOUTH);
        addConveyorBelt(board, 6, 2, Heading.WEST);

        // 🔹 Tilføj Checkpoints
        addCheckpoint(board, 3, 1, 1);
        addCheckpoint(board, 3, 7, 2);
        addCheckpoint(board, 6, 3, 3);

        return board;
    }

    /**
     * 🔹 Tilføjer et checkpoint til et felt
     */
    private void addCheckpoint(Board board, int x, int y, int number) {
        Checkpoint checkpoint = new Checkpoint(number);
        board.getSpace(x, y).getActions().add(checkpoint);
        System.out.println("🏁 Checkpoint " + number + " added at (" + x + ", " + y + ")");
    }

    /**
     * 🔹 Tilføjer en væg til et felt
     */
    private void addWall(Board board, int x, int y, Heading heading) {
        Space space = board.getSpace(x, y);
        if (space != null) {
            space.getWalls().add(heading);
        }
    }

    /**
     * 🔹 Tilføjer en transportbånd (Conveyor Belt)
     */
    private void addConveyorBelt(Board board, int x, int y, Heading direction) {
        ConveyorBelt conveyor = new ConveyorBelt(direction);
        board.getSpace(x, y).getActions().add(conveyor);
        System.out.println("🔄 Conveyor Belt added at (" + x + ", " + y + ") heading " + direction);
    }
}
