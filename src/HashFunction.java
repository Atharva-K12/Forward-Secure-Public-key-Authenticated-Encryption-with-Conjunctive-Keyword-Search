import it.unisa.dia.gas.jpbc.Element;

public interface HashFunction {
    // public byte[] hash(byte[] message, Element kappa);
    public Element hash(byte[] message, Element kappa);
    public byte[][] hash(byte[][] message);
}
