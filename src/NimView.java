//*******************************************************************
//
// File       : NimView.java
// Assignment : Project3
// Author     : David Pitoniak dhp6397@rit.edu
// Date       : 04/06/2021
//
//*******************************************************************

import java.util.ArrayList;
import java.util.Scanner;

/**
 * NimView exposes the user interface for Nim gameplay.
 *
 * @author David Pitoniak dhp6397@rit.edu
 */
public class NimView implements ModelListener {

    // View Listener for user input.
    private ViewListener listener;

    // State of the game for the user to view.
    private GameState gameState;

    // Scanner to get user input.
    Scanner scan;

    /**
     * Create a new NimView with the username, and starting piles.
     */
    public NimView() {

        gameState = new GameState(new ArrayList<>());
        scan = new Scanner(System.in);

    } // end NimView Constructor.

    /**
     * Listener for Model Proxy based on user input.
     *
     * @param listener listener for model proxy.
     */
    public void setListener(ViewListener listener) {

        this.listener = listener;

    } // end setListener.

    /**
     * Set the initial gameState for a new game.
     * Tell user game as started.
     *
     * @param piles initial piles.
     */
    @Override
    public void newGame(ArrayList<Integer> piles) {

        gameState.setInitialPiles(piles);

        System.out.println("new game started.");
        System.out.println("Piles:" + gameState);

    } // end newGame

    /**
     * Update the users View of gameState.
     *
     * @param pn pile index ( 0 based ).
     * @param sp start pin index ( 0 based ).
     * @param np number of pins.
     */
    @Override
    public void updatePile(int pn, int sp, int np) {

        gameState.updatePiles(pn, sp, np);
        System.out.println("Piles:" + gameState);

    } // end updatePile.

    /**
     * Tell user they are waiting for an opponent.
     */
    @Override
    public void waitingForOpponent() {

        System.out.println("Waiting for an opponent.");

    } // end waitingForOpponent.

    /**
     * Get user input for their turn and validate it.
     */
    @Override
    public void yourTurn() {

        System.out.print("Your turn > ");

        String move = scan.nextLine();

        boolean validMove = false;

        String[] moves = move.split(" ");

        int pn;
        int sp;
        int np;

        while(!validMove) {
            try {
                if (moves.length == 3) {
                    pn = Integer.parseInt(moves[0]);
                    sp = Integer.parseInt(moves[1]);
                    np = Integer.parseInt(moves[2]);
                    if (np > 0) {
                        listener.moveRequest(NimView.this, pn, sp, np);
                        validMove = true;
                    } else {
                        throw new NumberFormatException();
                    }
                } else if (moves.length >= 1) {
                    switch (moves[0]) {
                        case "q", "quit" -> {
                            listener.quit(NimView.this);
                            validMove = true;
                        }
                        case "n", "new game"-> {
                            listener.newGame(NimView.this);
                            validMove = true;
                        }
                        case "h", "help" -> {
                            listener.help(NimView.this);
                            validMove = true;
                        }
                        default -> {
                            throw new NumberFormatException();
                        }
                    }
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("invalid turn. use h for help.");

                // Get turn again.
                System.out.print("Your turn > ");
                move = scan.nextLine();
                moves = move.split(" ");
            }
        }

    } // end yourTurn.

    /**
     * Tell client that it is the opponents turn.
     *
     * @param oUsername name of opponent.
     */
    @Override
    public void opponentTurn(String oUsername) {

        System.out.printf("%s planning move.\n", oUsername);

    } // end otherTurn.

    /**
     * Tell client that they have won.
     */
    @Override
    public void youWin() {

        //System.out.println("Piles:" + gameState);
        System.out.println("You win!");
        System.exit(0);

    } // end youWin.

    /**
     * Tell the client that the opponent won.
     *
     * @param name name of player who won.
     */
    @Override
    public void opponentWin(String name) {

        //System.out.println("Piles:" + gameState);
        System.out.printf("%s Wins!\n", name);
        System.exit(0);

    } // end otherWin.

    /**
     * Alerts client of errors from the server.
     *
     * @param msg error message.
     */
    @Override
    public void error(String msg) {

        System.out.println("Error: " + msg);
        yourTurn();

    } // end error.

    /**
     * Display help message from the server.
     *
     * @param msg help message.
     */
    @Override
    public void help(String msg) {

        System.out.print("\n"+msg);
        yourTurn();

    } // end help.

    /**
     * Make client quit.
     */
    @Override
    public void quit() {

        listener.quit(NimView.this);
        System.out.println("\nquitting");
        System.exit(0);

    } // end quit.

} // end NimView.
