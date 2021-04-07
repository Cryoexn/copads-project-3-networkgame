//*******************************************************************
//
// File       : NimModel.java
// Assignment : Project3
// Author     : David Pitoniak dhp6397@rit.edu
// Date       : 04/06/2021
//
//*******************************************************************

import java.util.ArrayList;

/**
 * Model that listens to the requests of players and updates the GameState.
 *
 * @author David Pitoniak dhp6397@rit.edu
 */
public class NimModel implements ViewListener {

    // Username for the players.
    private String name1;
    private String name2;

    // ModelListeners for the players.
    private ModelListener view1;
    private ModelListener view2;

    // ModelListener signifying players turn.
    private ModelListener turn;

    // If the game is finished.
    private boolean isFinished;

    // State of the game.
    private GameState gameState;

    public NimModel(ArrayList<Integer> piles) {
        gameState = new GameState(piles);

    } // end NimModel.

    /**
     * Check if there is already a player waiting
     * for a game to start.
     *
     * @param view Calling view object.
     * @param username Name of player.
     */
    @Override
    public synchronized void join(ModelListener view, String username) {

        // If there isn't a player1.
        if(name1 == null) {
            name1 = username;
            view1 = view;

            // Notify player1 that they need to wait for an opponent.
            view1.waitingForOpponent();

        } else {

            name2 = username;
            view2 = view;

            // Once both players are in start a Game with default values.
            startGame(gameState.INITIAL_PILES);
        }

    } // end join.

    /**
     * Update the model with the move by calling player.
     *
     * @param view Calling view object.
     * @param pn pile index ( 0 based ).
     * @param sp start pin index ( 0 based ).
     * @param np number of pins to take.
     */
    @Override
    public synchronized void moveRequest(ModelListener view,
                                         int pn, int sp, int np) {

        // check if the player who make the move is up.
        if (view != turn) {

            // Check which player made the move.
            if (view == view1) {
                makeMove(view1, pn, sp, np);
            } else {
                makeMove(view2, pn, sp, np);
            }
        }

    } // end moveRequest.

    /**
     * Report that a new game has been requested.
     *
     * @param view Calling view object.
     */
    @Override
    public synchronized void newGame(ModelListener view, ArrayList<Integer> piles) {

        // Check that there is still a second player.
        if(name2 != null) {
            if(piles == null) {

                // Initialize piles.
                piles = new ArrayList<>();

                // Add default pile values.
                piles.add(3);
                piles.add(4);
                piles.add(5);

                // Start the game with default values.
                startGame(piles);

            } else {

                // Start the game with initial values.
                startGame(piles);
            }
        }

    } // end newGame.

    /**
     * Report that a player quit.
     *
     * @param view Calling view object.
     */
    @Override
    public synchronized void quit(ModelListener view) {
        if(view1 != null) {
            view1.quit();
        }

        if(view2 != null) {
            view2.quit();
        }

        turn = null;
        isFinished = true;

    } // end quit.

    @Override
    public synchronized void help(ModelListener view) {

    }

    /**
     * Check if the game has finished.
     *
     * @return if the game has finished.
     */
    public boolean isFinished() {

        return isFinished;

    } // end isFinished.

//********************** Private Methods ****************************

    /**
     * Start a new game of Nim and let players know.
     */
    private void startGame(ArrayList<Integer> piles) {

        // Clear the piles.
        this.gameState.clearPiles();

        // Notify players of the new game.
        view1.newGame(piles);
        view2.newGame(piles);

        // Allow player1 to make first move.
        turn = view1;

        // Notify players of the turns.
        view1.yourTurn();
        view2.otherTurn();

    } // end startGame.

    /**
     * Update the gameState with the players move and change the turns.
     *
     * @param view Calling object.
     * @param pn pile index ( 0 based ).
     * @param sp start pin index ( 0 based ).
     * @param np number of pins.
     */
    private void makeMove(ModelListener view, int pn, int sp, int np) {

        // Update the game state with move.
        gameState.updatePiles(pn, sp, np);

        // Notify player of the gameState update.
        view1.updatePile(pn, sp, np);
        view2.updatePile(pn, sp, np);

        // Check if its player1s turn.
        boolean isPlayerOnesTurn = (view == view1);

        // See if the game is over.
        if (gameState.arePilesEmpty()) {

            // Current player loses.
            turn = null;

            // If the current player is player1.
            if(isPlayerOnesTurn) {
                view1.otherWin();
                view2.youWin();
            } else {
                view1.youWin();
                view2.otherWin();
            }

            isFinished = true;

        } else {
            if (isPlayerOnesTurn) {
                turn = view2;
                view1.otherTurn();
                view2.yourTurn();
            } else {
                turn = view1;
                view1.yourTurn();
                view2.otherTurn();
            }
        }

    } // end makeMove.

} // end NimModel.
