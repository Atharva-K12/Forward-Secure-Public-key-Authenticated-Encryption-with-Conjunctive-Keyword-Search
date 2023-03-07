import java.math.BigInteger;
import java.util.Random;

import it.unisa.dia.gas.jpbc.Element;

public class KeyGenReceiver {
    /*KeyGenR(pp) : Chooses an element α ∈ Z∗p at random
    and computes pkR = gα , skR = α for a receiver. */
    PublicKey pk_r;
    PrivateKey sk_r;

    public KeyGenReceiver(GlobalParameters pp){
        
        // Choose an element α ∈ Z∗p at random
        BigInteger p = pp.getG().getOrder();
        Element alpha = (Element) new BigInteger(p.bitLength(), new Random());
        
        // Compute pkR = g^α
        Element pkR = pp.getG().newRandomElement().getImmutable();
        pkR.powZn(alpha);
        // Compute skR = α
        Element skR = pp.getG().newRandomElement().getImmutable();
        skR.set(alpha);
        // Output (pkR , skR)
        this.pk_r = new PublicKey(pkR);
        this.sk_r = new PrivateKey(skR);        
    }
    
}
