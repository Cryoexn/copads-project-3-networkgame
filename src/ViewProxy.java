//*******************************************************************
//
// File       : ViewProxy.java
// Assignment : Project3
// Author     : David Pitoniak dhp6397@rit.edu
// Date       : 04/06/2021
//
//*******************************************************************

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Proxy for the NimModel to communicate with NimView.
 *
 * @author David Pitoniak dhp6397@rit.edu
 */
public class ViewProxy implements ModelListener {

    // Server socket.
    private Socket socket;

    // Server input and output streams.
    private DataInputStream in;
    private DataOutputStream out;

    // listener for the Model.
    private ViewListener listener;

    /**
     * Constructor.
     *
     * @param socket server socket.
     */
    public ViewProxy (Socket socket) {

        // Try to initialize server in and out streams.
        try {
            this.socket = socket;
            this.socket.setTcpNoDelay(true);

            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());

        } catch (IOException e) {
            handleErrorMessage(e.getMessage());
        }

    } // end ViewProxy Constructor.

    /**
     * Set the listener for communication with Model.
     *
     * @param listener listener for Model.
     */
    public void setListener(ViewListener listener) {

        // Set the listener to update on messages.
        this.listener = listener;

        // Start the reader thread to read messages from ModelProxy.
        new ReaderThread().start();

    } // end setListener.

    @Override
    public void newGame(ArrayList<Integer> piles) {

        try {
            // Write out the header Byte of 'N' for New Game.
            out.writeByte('N');

            for(Integer i : piles) {
                out.writeByte(i);
            }

            // Indicate end of values.
            out.writeByte(-1);

            // Flush the output.
            out.flush();

        } catch (IOException e) {
            handleErrorMessage(e.getMessage());
        }

    } // end newGame.

    @Override
    public void updatePile(int pn, int sp, int np) {

        try {
            // Write out the header Byte of 'R' for Request.
            out.writeByte('R');

            // Write out move details.
            out.writeByte(pn);
            out.writeByte(sp);
            out.writeByte(np);

            // Flush the output.
            out.flush();

        } catch (IOException e) {
            handleErrorMessage(e.getMessage());
        }

    } // end updatePile.

    @Override
    public void waitingForOpponent() {
        try {
            // Write out the header Byte of 'W' for Waiting for opponent.
            out.writeByte('W');

            // Flush the output.
            out.flush();

        } catch (IOException e) {
            handleErrorMessage(e.getMessage());
        }

    } // end waitingForOpponent.

    @Override
    public void yourTurn() {
        try {
            // Write out the header Byte of 'Y' for Your turn.
            out.writeByte('Y');

            // Flush the output.
            out.flush();

        } catch (IOException e) {
            handleErrorMessage(e.getMessage());
        }

    } // end yourTurn.

    @Override
    public void opponentTurn(String oUsername) {
        try {
            // Write out the header Byte of 'O' Other turn.
            out.writeByte('O');

            // Write out the opponent username.
            out.writeUTF(oUsername);

            // Flush the output.
            out.flush();

        } catch (IOException e) {
            handleErrorMessage(e.getMessage());
        }

    } // end otherTurn.

    @Override
    public void youWin() {
        try {
            // Write out the header Byte of 'U' for you win.
            out.writeByte('U');

            // Flush the output.
            out.flush();

        } catch (IOException e) {
            handleErrorMessage(e.getMessage());
        }

    } // end youWin.

    @Override
    public void opponentWin(String name) {
        try {
            // Write out the header Byte of 'L' for other win.
            out.writeByte('L');

            // Write out name of opponent.
            out.writeUTF(name);

            // Flush the output.
            out.flush();

        } catch (IOException e) {
            handleErrorMessage(e.getMessage());
        }

    } // end otherWin.

    @Override
    public void error(String errorMsg) {

        try {
            // Write out the header Byte of 'E' for error.
            out.writeByte('E');

            // Write out the errorMsg.
            out.writeUTF(errorMsg);

            // Flush the output.
            out.flush();

        } catch (IOException e) {
            handleErrorMessage(e.getMessage());
        }

    } // end error.

    @Override
    public void help(String msg) {

        try {
            // Write out the header Byte of 'H' for help.
            out.writeByte('H');

            // Write out the help msg.
            out.writeUTF(msg);

            // Flush the output.
            out.flush();

        } catch (IOException e) {
            handleErrorMessage(e.getMessage());
        }

    } // end help.

    @Override
    public void quit() {

        try {
            // Write out the header Byte of 'Q' for quit.
            out.writeByte('Q');

            // Flush the output.
            out.flush();

        } catch (IOException e) {
            handleErrorMessage(e.getMessage());
        }

    } // end quit.

    /**
     * Prints msg to System.err and exits with code 1.
     *
     * @param msg message to print to err.
     */
    private void handleErrorMessage(String msg) {

        System.err.println(msg);
        System.exit(1);

    } // end handleErrorMessage.

/* ######################### Helper Class ######################## */

    /**
     * Helper thread that reads input from ModelProxy output stream.
     */
    private class ReaderThread extends Thread {

        /**
         * When the thread is started it will receive
         * input from ModelProxy output stream and update the model.
         */
        public void run() {

            // Message header.
            int header;

            try {
                while(true) {

                    header = in.readByte();

                    // Create storage variables.
                    // Get input while not -1 Byte.
                    // Send piles to newGame.
                    switch (header) {
                        case 'J' -> {
                            listener.join(ViewProxy.this, in.readUTF());
                        }
                        case 'R' -> {
                            listener.moveRequest(
                                    ViewProxy.this,
                                    in.readByte(),  /* pn */
                                    in.readByte(),  /* sp */
                                    in.readByte()); /* np */
                        }
                        case 'N' -> {
                            listener.newGame(ViewProxy.this);
                        }
                        case 'Q' -> listener.quit(ViewProxy.this);
                        case 'H' -> listener.help(ViewProxy.this);
                        default -> {
                            System.err.println("Bad Message.");
                            System.exit(1);
                            throw new IOException("Bad Message");
                        }
                    }


                }

            } catch (IOException ignored) { } finally {
                try {
                    socket.close();
                } catch (IOException ignored) { }
            }

        } // end run.

    } // end ReaderThread.

} // end ViewProxy.
