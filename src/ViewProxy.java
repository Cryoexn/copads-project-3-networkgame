
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ViewProxy implements ModelListener {

    private Socket socket;
    private Scanner in;
    private PrintStream out;
    private ViewListener listener;

    public ViewProxy (Socket soc) throws IOException {
        this.socket = soc;
        this.socket.setTcpNoDelay(true);

        this.in = new Scanner(this.socket.getInputStream());
        this.out = new PrintStream(this.socket.getOutputStream());
    }

    public void setViewListener(ViewListener listener) {
        this.listener = listener;
        new ReaderThread().start();
    }

    @Override
    public void updatePile(int pn, int sp, int np) {

    }

    @Override
    public void started() {

    }

    @Override
    public void stopped() {

    }

    @Override
    public void error() {

    }

    private class ReaderThread extends Thread {
        public void run() {

            try {
                while(in.hasNext()) {
                    String msg = in.nextLine();
                    Scanner s = new Scanner(msg);

                    String op = s.next();

                    switch (op) {
                        case "NG" -> listener.newGame();
                        case "SP" -> listener.quit();
                        default -> System.err.println("Bad Message.");
                    }
                }

                System.out.println("Terminating.");

            } catch (Exception e) {

                System.err.println(e.getMessage());

            } finally {

                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("socket.close" + e.getMessage());
                }

            } // end Try-Catch-Finally.

        } // end run.

    } // end ReaderThread.

} // end ViewProxy.
