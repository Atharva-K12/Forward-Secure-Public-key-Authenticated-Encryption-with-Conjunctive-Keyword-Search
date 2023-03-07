import java.math.BigInteger;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;

public class Setup {
/* Setup(λ) : Chooses a bilinear map e : G × G → G1,
where G1 and G are groups of prime order p. Let g be
a generator of G. Finally, it outputs global parameters
pp = (G1 , G, e, H3 , H2 , H1 , g), where H1 : {0, 1}∗ →
Z∗
p, H2 : {0, 1}∗ → G,H3 : {0, 1}∗ → G and H4 :
{0, 1}∗ → {0, 1}∗ are a couple of various hash functions
with collision-resistance. Note that the system divides its
lifetime into some periods 1, 2, · · · , poly(λ). */
    public Setup() {
    // Choose bilinear map e : G × G → G1
    Pairing e = PairingFactory.getPairing("jpbc-2.0.0/params/curves/a.properties");

    // Choose G1 and G as groups of prime order p
    Field G1 = e.getGT();
    Field G = e.getG1();
    // Choose g as a generator of G
    Element g = G.newRandomElement().getImmutable();

    // H1 : {0, 1}∗ → Z∗ p is a hash function with collision-resistance
    HashFunction H1 = new HashFunction() {
        public byte[] hash(byte[] message) {
            BigInteger p = e.getG1().getOrder();
            BigInteger result = new BigInteger(message).mod(p);
            return result.toByteArray();
        }
    };

    // H2 : {0, 1}∗ → G is a hash function with collision-resistance
    HashFunction H2 = new HashFunction() {
        public byte[] hash(byte[] message) {
            BigInteger p = e.getG1().getOrder();
            BigInteger result = new BigInteger(message).mod(p);
            return result.toByteArray();
        }
    };

    // H3 : {0, 1}∗ → G is a hash function with collision-resistance
    HashFunction H3 = new HashFunction() {
        public byte[] hash(byte[] message) {
            BigInteger p = e.getG1().getOrder();
            BigInteger result = new BigInteger(message).mod(p);
            return result.toByteArray();
        }
    };

    // H4 : {0, 1}∗ → {0, 1}∗ is a hash function with collision-resistance
    HashFunction H4 = new HashFunction() {
        public byte[] hash(byte[] message) {
            //XOR with x00ff
            BigInteger xorval = new BigInteger("00ff", 16);
            BigInteger result = new BigInteger(message).xor(xorval);
            return result.toByteArray();
        }
    };
    
    // Output global parameters pp = (G1 , G, e, H3 , H2 , H1 , g)
    GlobalParameters pp = new GlobalParameters(G1, G, e, H3, H2, H1, g);
    }  
}
