import java.math.BigInteger;
import java.util.Random;
import it.unisa.dia.gas.jpbc.Element;

public class KeyGenSender {
    /*KeyGenS(pp) : Chooses an element β ∈ Z∗p at random
    and computes pkS = g^β , skS = β for a sender. */
    
    PublicKey pk_s;
    PrivateKey sk_s;

    public KeyGenSender(GlobalParameters pp){
        
        // Choose an element β ∈ Z∗p at random
        BigInteger p = pp.getG().getOrder();
        Element beta = (Element) new BigInteger(p.bitLength(), new Random());
        
        // Compute pkS = g^β
        Element pkS = pp.getG().newRandomElement().getImmutable();
        pkS.powZn(beta);
        // Compute skS = β
        Element skS = pp.getG().newRandomElement().getImmutable();
        skS.set(beta);
        // Output (pkS , skS)
        this.pk_s = new PublicKey(pkS);
        this.sk_s = new PrivateKey(skS);        
    }
    
}
