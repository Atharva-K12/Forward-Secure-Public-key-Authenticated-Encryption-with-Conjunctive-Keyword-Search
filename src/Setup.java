import java.math.BigInteger;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class Setup {
    /*
     * Setup(λ) : Chooses a bilinear map e : G × G → G1,
     * where G1 and G are groups of prime order p. Let g be
     * a generator of G. Finally, it outputs global parameters
     * pp = (G1 , G, e, H3 , H2 , H1 , g), where H1 : {0, 1}∗ →
     * Z∗
     * p, H2 : {0, 1}∗ → G,H3 : {0, 1}∗ → G and H4 :
     * {0, 1}∗ → {0, 1}∗ are a couple of various hash functions
     * with collision-resistance. Note that the system divides its
     * lifetime into some periods 1, 2, · · · , poly(λ).
     */
    Pairing e;
    Field G1, G;
    Element g;
    HashFunction H1, H2, H3, H4;
    GlobalParameters pp;

    public Setup() {
        // Choose bilinear map e : G × G → G1
        this.e = PairingFactory.getPairing("jpbc-2.0.0/params/curves/a.properties");

        // Choose G1 and G as groups of prime order p
        this.G1 = e.getGT();
        this.G = e.getG1();
        // Choose g as a generator of G
        this.g = G.newRandomElement().getImmutable();

        // H1 : {0, 1}∗ → Z∗ p is a hash function with collision-resistance
        this.H1 = new HashFunction() {
            public byte[] hash(byte[] message) {
                BigInteger p = e.getZr().getOrder();
                BigInteger result = new BigInteger(message).mod(p);
                return result.toByteArray();
            }

            @Override
            public Element hash(byte[] message, Element kappa){
                // BigInteger p = e.getZr().getOrder();
                // byte[] kappaBytes = kappa.toBytes();

                // // concatenate kappa to message
                // byte[]concatenated = new byte[message.length + kappaBytes.length];
                // System.arraycopy(message, 0, concatenated, 0, message.length);
                // System.arraycopy(kappaBytes, 0, concatenated, message.length, kappaBytes.length);
    
                // BigInteger result = new BigInteger(concatenated).mod(p);

                // // Convert the BigInteger result to Element Object

                // byte[] resultBytes = result.toByteArray();
                // Element rElement = e.getZr().newElementFromBytes(resultBytes);

                // return rElement;

                BigInteger p = e.getZr().getOrder();
                byte[] kappaBytes = kappa.toBytes();

                byte[]concatenated = new byte[message.length + kappaBytes.length];
                System.arraycopy(message, 0, concatenated, 0, message.length);
                System.arraycopy(kappaBytes, 0, concatenated, message.length, kappaBytes.length);

                // hash the concatenated message using SHA-256
                // MessageDigest digest = MessageDigest.getInstance("SHA-256");
                // byte[] hashBytes = digest.digest(concatenated);
                 

                // return the hash value as Element
                Element rElement = e.getZr().newElementFromHash(concatenated, 0, concatenated.length);
                return rElement;

            }
        };

        // H2 : {0, 1}∗ → G is a hash function with collision-resistance
        this.H2 = new HashFunction() {
            public byte[] hash(byte[] message) {
                BigInteger p = e.getG1().getOrder();
                BigInteger result = new BigInteger(message).mod(p);
                return result.toByteArray();
            }

            @Override
            public Element hash(byte[] message, Element kappa)  {
                BigInteger p = e.getG1().getOrder();
                byte[] kappaBytes = kappa.toBytes();

                byte[]concatenated = new byte[message.length + kappaBytes.length];
                System.arraycopy(message, 0, concatenated, 0, message.length);
                System.arraycopy(kappaBytes, 0, concatenated, message.length, kappaBytes.length);

                // BigInteger result = new BigInteger(concatenated).mod(p);

                // MessageDigest digest = MessageDigest.getInstance("SHA-256");
                // byte[] hashBytes = digest.digest(concatenated);

                // Convert the BigInteger result to Element Object

                // byte[] resultBytes = result.toByteArray();
                // Element rElement = e.getZr().newElementFromBytes(resultBytes);

                // Element rElement = e.getZr().newElementFromBytes(hashBytes);

                Element rElement = e.getG1().newElementFromHash(concatenated, 0, concatenated.length);

                return rElement;
            }
        };

        // H3 : {0, 1}∗ → G is a hash function with collision-resistance
        this.H3 = new HashFunction() {
            public byte[] hash(byte[] message) {
                BigInteger p = e.getG1().getOrder();
                BigInteger result = new BigInteger(message).mod(p);
                return result.toByteArray();
            }

            @Override
            public Element hash(byte[] message, Element kappa) {
                BigInteger p = e.getG1().getOrder();
                byte[] kappaBytes = kappa.toBytes();

                byte[]concatenated = new byte[message.length + kappaBytes.length];
                System.arraycopy(message, 0, concatenated, 0, message.length);
                System.arraycopy(kappaBytes, 0, concatenated, message.length, kappaBytes.length);

                // BigInteger result = new BigInteger(concatenated).mod(p);

                // MessageDigest digest = MessageDigest.getInstance("SHA-256");
                // byte[] hashBytes = digest.digest(concatenated);



                // Convert the BigInteger result to Element Object

                // byte[] resultBytes = result.toByteArray();
                // Element rElement = e.getZr().newElementFromBytes(resultBytes);

                // Element rElement = e.getZr().newElementFromBytes(hashBytes);
                Element rElement = e.getG1().newElementFromHash(concatenated, 0, concatenated.length);

                return rElement;
            }
        };

        // H4 : {0, 1}∗ → {0, 1}∗ is a hash function with collision-resistance
        this.H4 = new HashFunction() {
            public byte[] hash(byte[] message) {
                // XOR with x00ff
                BigInteger xorval = new BigInteger("00ff", 16);
                // if message is zero, return 0
                if (message.length == 0) {
                    return new byte[0];
                }
                BigInteger result = new BigInteger(message).xor(xorval);
                return result.toByteArray();
            }

            public Element hash(byte[] message, Element kappa) {
                return null;
            }
        };

        // Output global parameters pp = (G1 , G, e, H3 , H2 , H1 , g)
        this.pp = new GlobalParameters(G1, G, e, H4, H3, H2, H1, g);
    }

    public static void main(String[] args) {
        System.out.println("Setup");
        Setup setup = new Setup();
        System.out.println("Global parameters: " + setup.pp.getG1().getCanonicalRepresentationLengthInBytes());
        System.out.println("Global parameters: " + setup.pp.getG().getCanonicalRepresentationLengthInBytes());
        System.out.println("Global parameters: " + setup.pp.getE().getPairingPreProcessingLengthInBytes());
    }
}
