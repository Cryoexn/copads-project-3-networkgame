public interface ModelListener {
    void updatePile(int pn, int sp, int np);
    void started();
    void stopped();
    void error();
}
