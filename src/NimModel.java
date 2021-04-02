public class NimModel implements ViewListener {

    private String name1;
    private String name2;

    private ModelListener view1;
    private ModelListener view2;
    private ModelListener turn;

    private boolean isFinished;
    private PileState pileState = new PileState(3, 4, 5);

    public NimModel() {

    }

    @Override
    public void moveRequest(int pn, int sp, int np) {
        pileState.updatePile(pn, sp, np);
        view1.updatePile(pn, sp, np);
        view2.updatePile(pn, sp, np);
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
