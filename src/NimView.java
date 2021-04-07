//*******************************************************************
//
// File       : NimView.java
// Assignment : Project3
// Author     : David Pitoniak dhp6397@rit.edu
// Date       : 04/06/2021
//
//*******************************************************************

import java.util.ArrayList;

/**
 * NimView exposes the user interface for Nim gameplay.
 *
 * @author David Pitoniak dhp6397@rit.edu
 */
public class NimView implements ModelListener {

    //private ViewListener listener;

    // State of the game for the user to view.
    private GameState gameState;

    // Name of user seeing the view.
    private static String username;

    /**
     * Create a new NimView with the username, and starting piles.
     *
     * @param name players name.
     */
    public NimView(String name) {

        gameState = new GameState();
        username = name;

    } // end NimView Constructor.

    public void setListener(ViewListener listener) {

        //this.listener = listener;

    } // end setListener.

    @Override
    public void newGame(ArrayList<Integer> piles) {

        // Clear remaining piles.
        gameState.clearPiles();

        // Reset game to initial state.
        gameState.setPiles(piles);
    }

    @Override
    public void updatePile(int pn, int sp, int np) {

        gameState.updatePiles(pn, sp, np);

    } // end updatePile.

    @Override
    public void waitingForOpponent() {

        System.out.println("Waiting for an opponent.");

    } // end waitingForOpponent.

    @Override
    public void yourTurn() {

        System.out.println("Your turn >" + gameState);

    } // end yourTurn.

    @Override
    public void otherTurn() {

        System.out.printf("%s planning move.\n", "temp");

    } // end otherTurn.

    @Override
    public void youWin() {

        System.out.println("You win!");

    } // end youWin.

    @Override
    public void otherWin() {

        System.out.printf("%s Wins!\n", "temp");

    } // end otherWin.

    @Override
    public void draw() {

        System.out.println("draw");

    } // end draw.

    @Override
    public void quit() {

        System.out.println("quitting");

    } // end quit.

} // end NimView.
