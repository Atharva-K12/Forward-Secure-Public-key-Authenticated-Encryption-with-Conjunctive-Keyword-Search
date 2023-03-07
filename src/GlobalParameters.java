import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

public class GlobalParameters {

    private Field G1;
    private Field G;
    private Pairing e;
    private HashFunction h3;
    private HashFunction h2;
    private HashFunction h1;
    private Element g;

    public GlobalParameters(Field G1, Field G, Pairing e, HashFunction h3, HashFunction h2, HashFunction h1,
            Element g) {
        this.G1 = G1;
        this.G = G;
        this.e = e;
        this.h3 = h3;
        this.h2 = h2;
        this.h1 = h1;
        this.g = g;
    }

    // Getters
    public Field getG1() {
        return G1;
    }

    public Field getG() {
        return G;
    }

    public Pairing getE() {
        return e;
    }

    public HashFunction getH3() {
        return h3;
    }

    public HashFunction getH2() {
        return h2;
    }

    public HashFunction getH1() {
        return h1;
    }

    public Element getg() {
        return g;
    }

}
