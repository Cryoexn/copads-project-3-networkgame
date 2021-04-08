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

    // If verbose mode was selected.
    private boolean verbose;

    // State of the game.
    private GameState gameState;

    /**
     * Constructor to initialize the starting piles
     * and if verbose output is selected.
     *
     * @param piles initial piles.
     * @param verbose verbose option.
     */
    public NimModel(ArrayList<Integer> piles, boolean verbose) {

        name1 = null;
        name2 = null;

        view1 = null;
        view2 = null;

        turn = null;

        isFinished = false;

        gameState = new GameState(piles);

        this.verbose = verbose;

    } // end NimModel.

    /**
     * Allow players to join and be initialized to the
     * correct player slot based on their arrival.
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

            logMsg(String.format("%s vs. %s  start game.\n", name1, name2));

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
        if (view == turn) {

            // Make move for player that is up.
            makeMove(turn, pn, sp, np);
        }

    } // end moveRequest.

    /**
     * Report that a new game has been requested.
     *
     * @param view Calling view object.
     */
    @Override
    public synchronized void newGame(ModelListener view) {

        // Check that there is still a second player.
        if(name2 != null) {

            logMsg(String.format("%s vs. %s  start game.\n", name1, name2));
            logMsg(String.format("%s vs. %s  restarting game.\n", name1, name2));

            // Start the game with initial values.
            startGame(gameState.INITIAL_PILES);
        }

    } // end newGame.

    /**
     * Report that a player quit.
     *
     * @param view Calling view object.
     */
    @Override
    public synchronized void quit(ModelListener view) {

        // If the client hasn't quit
        if(view1 != null) {

            // Tell them to quit.
            view1.quit();
            view1 = null;
        }

        // If the client hasn't quit
        if(view2 != null) {

            // Tell them to quit.
            view2.quit();
            view2 = null;
            logMsg(String.format("%s vs. %s  ending game\n", name1, name2));
        }

        // Update variable to signify quit.
        turn = null;
        isFinished = true;

    } // end quit.

    /**
     * Pass along the help message to the ViewProxy.
     *
     * @param view Calling view object.
     */
    @Override
    public void help(ModelListener view) {
        // Constant help message.
        final String HELP_MSG = "" +
                "Command  Example/Description\n" +
                "q        quit the game\n" +
                "n        request new restarted game\n" +
                "p# i# q# remove q# pins starting at index i# from pile p#\n" +
                "Commands use 0-based indexing.\n";

        view.help(HELP_MSG);

    } // end help.

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
     *
     * @param piles initial value of piles to start.
     */
    private void startGame(ArrayList<Integer> piles) {

        // Set Piles.
        this.gameState.setInitialPiles(piles);

        // Notify players of the new game.
        view1.newGame(piles);
        view2.newGame(piles);

        // Allow player1 to make first move.
        turn = view1;

        // Notify players of the turns.
        view1.yourTurn();
        view2.opponentTurn(name1);

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

        try {

            // Update the game state with move.
            gameState.updatePiles(pn, sp, np);

            logMsg(String.format("%s vs. %s new state:%s\n",
                    name1, name2, gameState.toString()));

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
                if (isPlayerOnesTurn) {
                    view1.opponentWin(name2);
                    view2.youWin();
                } else {
                    view1.youWin();
                    view2.opponentWin(name1);
                }

                isFinished = true;

                logMsg(String.format("%s vs. %s  ending game\n",
                        name1, name2));

            } else {

                if (isPlayerOnesTurn) {
                    turn = view2;
                    view1.opponentTurn(name2);
                    view2.yourTurn();
                    logMsg(String.format("%s vs. %s whose turn: %s\n",
                            name1, name2, name2));
                } else {
                    turn = view1;
                    view1.yourTurn();
                    view2.opponentTurn(name1);
                    logMsg(String.format("%s vs. %s whose turn: %s\n",
                            name1, name2, name1));
                }
            }

        } catch (Exception e) {
            if (turn != null)
                turn.error(e.getMessage());
        }

    } // end makeMove.

    /**
     * If verbose option is true.
     * log messages to System.out with no new line added.
     *
     * @param msg message to log.
     */
    private void logMsg(String msg) {

        if (verbose)
            System.out.print(msg);

    } // end logMsg.

} // end NimModel.
