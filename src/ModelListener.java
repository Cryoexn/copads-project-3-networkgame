//*******************************************************************
//
// File       : ModelListener.java
// Assignment : Project3
// Author     : David Pitoniak dhp6397@rit.edu
// Date       : 04/06/2021
//
//*******************************************************************

import java.util.ArrayList;

/**
 * Interface to be implemented by an object that takes reports from
 * the Model in a Nim game.
 *
 * @author David Pitoniak dhp6397@rit.edu
 */
public interface ModelListener {

    /**
     * Report new game started.
     */
    void newGame(ArrayList<Integer> piles);

    /**
     * Report that a piles were updated.
     *
     * @param pn pile index ( 0 based ).
     * @param sp start pin index ( 0 based ).
     * @param np number of pins.
     */
    void updatePile(int pn, int sp, int np);

    /**
     * Report that the player is waiting for another player.
     */
    void waitingForOpponent();

    /**
     *  Report that it is your turn.
     */
    void yourTurn();

    /**
     * Report that is it the other players turn.
     */
    void otherTurn();

    /**
     * Report that you won.
     */
    void youWin();

    /**
     * Report that the other player won.
     */
    void otherWin();

    /**
     * Report that the game ended in a draw.
     */
    void draw();

    /**
     * Report that a player left.
     */
    void quit();

} // end ModelListener.