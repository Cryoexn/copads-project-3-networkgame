import java.security.spec.RSAOtherPrimeInfo;
import java.util.ArrayList;

public class Testing {
    public static void main(String[] args) {
        ArrayList<Integer> testing = new ArrayList<>();

        testing.add(1);
        testing.add(2);
        testing.add(3);

        GameState gs = new GameState(testing);

        System.out.println(gs);

        gs.updatePiles(1, 1, 4);

        System.out.println(gs);

        //gs.updatePiles(1, 1, 1);

        //System.out.println(gs);
    }
}
