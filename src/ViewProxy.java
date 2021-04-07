
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ViewProxy implements ModelListener {

    private Socket socket;

    private DataInputStream in;
    private DataOutputStream out;

    private ViewListener listener;

    public ViewProxy (Socket socket) {

        try {
            this.socket = socket;
            this.socket.setTcpNoDelay(true);

            this.in = new DataInputStream(socket.getInputStream());
            this.out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            handleErrorMessage(e.getMessage());
        }

    } // end ViewProxy Constructor.

    public void setListener(ViewListener listener) {

        this.listener = listener;
        new ReaderThread().start();

    } // end setListener.

    @Override
    public void newGame(ArrayList<Integer> piles) {

        try {
            // Write out the header Byte of 'N' for New Game.
            out.writeByte('N');

            // Write out all piles.
            for(Integer i : piles) {
                out.writeByte(i);
            }

            // Signal that the pile is finished.
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
    }

    @Override
    public void yourTurn() {
        try {
            // Write out the header Byte of 'W' for Waiting for opponent.
            out.writeByte('Y');

            // Flush the output.
            out.flush();

        } catch (IOException e) {
            handleErrorMessage(e.getMessage());
        }
    }

    @Override
    public void otherTurn() {
        try {
            // Write out the header Byte of 'W' for Waiting for opponent.
            out.writeByte('O');

            // Flush the output.
            out.flush();

        } catch (IOException e) {
            handleErrorMessage(e.getMessage());
        }
    }

    @Override
    public void youWin() {
        try {
            // Write out the header Byte of 'W' for Waiting for opponent.
            out.writeByte('U');

            // Flush the output.
            out.flush();

        } catch (IOException e) {
            handleErrorMessage(e.getMessage());
        }
    }

    @Override
    public void otherWin() {
        try {
            // Write out the header Byte of 'W' for Waiting for opponent.
            out.writeByte('L');

            // Flush the output.
            out.flush();

        } catch (IOException e) {
            handleErrorMessage(e.getMessage());
        }
    }

    @Override
    public void draw() {

    }

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
    }

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
     *
     */
    private class ReaderThread extends Thread {

        /**
         *
         */
        public void run() {

            int header, pn, sp, np;
            String name;

            try {

                while(true) {

                    header = in.readByte();

                    switch (header) {
                        case 'J': name = in.readUTF(); listener.join(ViewProxy.this, name);
                        case 'R': listener.moveRequest(
                                ViewProxy.this,
                                in.readByte(),  /* pn */
                                in.readByte(),  /* sp */
                                in.readByte()); /* np */
                        case 'N':
                            // Create storage variables.
                            ArrayList<Integer> piles = new ArrayList<>();
                            int val = in.readByte();

                            // Get input while not -1 Byte.
                            while(val != -1) {
                                piles.add(val);
                                val = in.readByte();
                            }

                            // Send piles to newGame.
                            listener.newGame(ViewProxy.this, piles);
                        case 'Q': listener.quit(ViewProxy.this);
                        case 'H': listener.help(ViewProxy.this);
                        default:
                            System.err.println("Bad Message.");
                            System.exit(1);
                    }
                }

            } catch (Exception e) {

                System.err.println(e.getMessage());
                System.exit(1);

            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("socket.close" + e.getMessage());
                }
            }

        } // end run.

    } // end ReaderThread.

} // end ViewProxy.
