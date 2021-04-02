import java.util.ArrayList;
import java.util.Arrays;

public class PileState {

    ArrayList<Integer> piles;

    public PileState(int pileOne, int pileTwo, int pileThree) {
        this.piles = new ArrayList<>();
    }

    public void clearPiles() {
        this.piles.clear();
    }

    public void updatePile(int i, int j, int val) {
        updatePiles(this.piles.get(i), i, j, val);
    }

    private void updatePiles(int num, int pileIndex, int i, int j) {
        int [] pins = new int[num];

        Arrays.fill(pins, 1);

        for(;i < j; i++) {
            pins[i] = 0;
        }

        int pinCount = 0;
        int count = 0;

        for(int pin : pins){
            if(pin == 1) {
                pinCount++;
            } else {
                this.piles.set(pileIndex + count, pinCount);
                count++;
                pinCount = 0;
            }
        }
    }
}
