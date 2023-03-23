import it.unisa.dia.gas.jpbc.Element;

public class CipherKeyword {
    private byte[][] hYi;
    private Element X;
    private Element[] Ri;
    private Element[] Cti;
    public CipherKeyword(byte [][] hYi, Element X , Element[] Ri, Element[] Cti) {
        this.hYi = hYi;
        this.X = X;
        this.Ri = Ri;
        this.Cti = Cti;
    }
    public byte[][] gethYi() {
        return hYi;
    }
    public Element getX() {
        return X;
    }
    public Element[] getRi() {
        return Ri;
    }
    public Element[] getCti() {
        return Cti;
    }

}
