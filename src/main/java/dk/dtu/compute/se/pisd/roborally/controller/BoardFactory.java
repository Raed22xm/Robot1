package dk.dtu.compute.se.pisd.roborally.controller;
import dk.dtu.compute.se.pisd.roborally.controller.Checkpoint;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import java.util.List;
import java.util.Collections;
import java.util.ArrayList;
/**
 * A factory for creating boards. The factory itself is implemented as a singleton.
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
// XXX A3: might be used for creating a first slightly more interesting board.
public class BoardFactory {

    /**
     * The single instance of this class, which is lazily instantiated on demand.
     */
    static private BoardFactory instance = null;
    private static final List<String> BOARD_NAMES = List.of("SimpleBoard", "AdvancedBoard");

    /**
     * Constructor for BoardFactory. It is private in order to make the factory a singleton.
     */
    private BoardFactory() {
    }

    /**
     * Returns the single instance of this factory. The instance is lazily
     * instantiated when requested for the first time.
     *
     * @return the single instance of the BoardFactory
     */
    public static BoardFactory getInstance() {
        if (instance == null) {
            instance = new BoardFactory();
        }
        return instance;
    }

    /**
     * Retrieves the list of board names available in the factory.
     * The returned list is unmodifiable.
     *
     * @return an unmodifiable list of board names
     */
    public List<String> getBoardNames() {
        return Collections.unmodifiableList(BOARD_NAMES);
    }
    /**
     * Creates a new board of given name of a board, which indicates
     * which type of board should be created. For now the name is ignored.
     *
     * @param name the given name board
     * @return the new board corresponding to that name
     */
    public Board createBoard(String name) {
        if (name == null) {
            name = "<none>";
        }
        Board board = new Board(8, 8, name);

        // Configure board elements (walls)
        board.getSpace(0, 0).getWalls().add(Heading.SOUTH);
        board.getSpace(2, 2).getWalls().add(Heading.WEST);
        board.getSpace(4, 4).getWalls().add(Heading.EAST);

        // Add Conveyor Belt
        ConveyorBelt conveyor = new ConveyorBelt();
        conveyor.setHeading(Heading.NORTH);
        board.getSpace(3, 3).getActions().add(conveyor);

        // 🔹 Add Checkpoints
        addCheckpoint(board, 1, 1, 1);
        addCheckpoint(board, 5, 5, 2);
        addCheckpoint(board, 7, 7, 3);
        // Configure board elements using addWall helper method
        addWall(board, 0, 0, Heading.SOUTH);
        addWall(board, 2, 2, Heading.WEST);
        addWall(board, 4, 4, Heading.EAST);

        // Add walls for the specified spaces using addWall
        addWall(board, 1, 0, Heading.NORTH);
        addWall(board, 1, 1, Heading.WEST);
        addWall(board, 5, 5, Heading.SOUTH);
        // 🔹 Add Conveyor Belts
        addConveyorBelt(board, 3, 3, Heading.NORTH);
        addConveyorBelt(board, 2, 4, Heading.EAST);
        addConveyorBelt(board, 5, 5, Heading.SOUTH);
        addConveyorBelt(board, 6, 2, Heading.WEST);

        // add some walls, actions and checkpoints to some spaces
        Space space = board.getSpace(0,0);
        space.getWalls().add(Heading.SOUTH);
        ConveyorBelt action  = new ConveyorBelt();
        action.setHeading(Heading.WEST);
        space.getActions().add(action);

        space = board.getSpace(1,0);
        space.getWalls().add(Heading.NORTH);
        action  = new ConveyorBelt();
        action.setHeading(Heading.WEST);
        space.getActions().add(action);

        space = board.getSpace(1,1);
        space.getWalls().add(Heading.WEST);
        action  = new ConveyorBelt();
        action.setHeading(Heading.NORTH);
        space.getActions().add(action);

        space = board.getSpace(5,5);
        space.getWalls().add(Heading.SOUTH);
        action  = new ConveyorBelt();
        action.setHeading(Heading.WEST);
        space.getActions().add(action);

        space = board.getSpace(6,5);
        action  = new ConveyorBelt();
        action.setHeading(Heading.WEST);
        space.getActions().add(action);

        return board;
    }
    // 🔹 Helper method to add a checkpoint to the board
    private void addCheckpoint(Board board, int x, int y, int number) {
        Checkpoint checkpoint = new Checkpoint(number);
        board.getSpace(x, y).getActions().add(checkpoint);
    }
    private void addWall(Board board, int x, int y, Heading heading) {
        // First, get the space at the specified coordinates
        Space space = board.getSpace(x, y);

        // Check if the space exists
        if (space != null) {
            // Add a wall in the specified direction to that space
            space.getWalls().add(heading);
        }
    }

    // ✅ Helper method to add conveyor belts
    private void addConveyorBelt(Board board, int x, int y, Heading direction) {
        ConveyorBelt conveyor = new ConveyorBelt();
        conveyor.setHeading(direction);
        board.getSpace(x, y).getActions().add(conveyor);
        System.out.println("🔄 Conveyor Belt added at (" + x + ", " + y + ") heading " + direction);
    }

}
