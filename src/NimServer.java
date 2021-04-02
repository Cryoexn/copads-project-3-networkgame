import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class NimServer implements ViewListener {

    static int [] piles;
    static boolean verbose = false;

    public static void main(String[] args) {
        if(args.length < 2) { usage(); }
        else {
            try {

                // Create new ServerSocket.
                ServerSocket serverSocket = new ServerSocket();

                // Get host and port from commandline arguments.
                String host = args[0];
                int port = Integer.parseInt(args[1]);

                // Bind serverSocket to host and port from arguments.
                serverSocket.bind(new InetSocketAddress(host, port));

                // Check if the verbose option was specified.
                // initialize accordingly.

                piles = processArgs(args);

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    public static int[] processArgs(String[] args) {

        if(args.length != 2) {
            if(args[2].equals("true")){

                // Verbose option is set.
                verbose = true;
                return initPiles(3, args);

            } else {

                // Start at position after port.
                return initPiles(2, args);
            }
        }

        return initPiles(0, args);
    }

    public static int[] initPiles(int startIndex, String[] args) {
        int [] p = new int[args.length - startIndex];

        for(int i = startIndex; i < args.length; i++) {
            piles[i - startIndex] = Integer.parseInt(args[i]);
        }

        return p;
    }

    public static void usage() {
        System.err.println("java NimServer <host-name> <port-number> [true] [pile-info...]");
        System.exit(1);
    }

    @Override
    public void moveRequest(int pn, int sp, int np) {

    }

    @Override
    public void newGame() {

    }

    @Override
    public void quit() {

    }

    @Override
    public void help() {

    }
}
