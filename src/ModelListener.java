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
     *
     * @param piles initial piles.
     */
    void newGame(ArrayList<Integer> piles);

    /**
     * Report that piles were updated.
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
     *
     * @param name name of player taking turn.
     */
    void opponentTurn(String name);

    /**
     * Report that you won.
     */
    void youWin();

    /**
     * Report that the opponent won.
     *
     * @param name name of player who won.
     */
    void opponentWin(String name);

    /**
     * Report that an error occurred.
     */
    void error(String msg);

    /**
     * Report player wants help msg.
     */
    void help(String msg);

    /**
     * Report that a player left.
     */
    void quit();

} // end ModelListener.