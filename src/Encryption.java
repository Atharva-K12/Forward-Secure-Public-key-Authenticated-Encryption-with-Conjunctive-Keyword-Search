import it.unisa.dia.gas.jpbc.Element;

public class Encryption {

    /*Enc(pkR, skS, W) : Given a selected keyword set W =
    {w1 , · · · , wl}, this algorithm is executed in the following
    way */
    public Encryption(PrivateKey pkR, PrivateKey skS, String[] W,GlobalParameters pp){
        /*Select three random values r1 , r2 and r3 from Z∗p
        and compute X = g^r1 and κ = pk skS
        R = gαβ*/

        Element r1 = pp.getG().newRandomElement().getImmutable();
        Element r2 = pp.getG().newRandomElement().getImmutable();
        Element r3 = pp.getG().newRandomElement().getImmutable();

        Element X = pp.getg().powZn(r1);

        Element k = pkR.pk.powZn(skS.sk); // TODO check this

        /*Get the system time t and t is encoded as set T via
        0-encoding */

        //system time t
        long t = System.currentTimeMillis();

        //t is encoded as set T via 0-encoding
        //TODO  0-encoding

        /*Compute xi = H1(yi, κ) for yi ∈ T and 1 ≤
        i ≤ n, and then set x0 = H1(0, κ) individually
        for 0 /∈ T. Finally, use these n + 1 points
        {(x0 , r3), (x1 , r2), (x2 , r2), · · · , (xn, r2)} to build a
        Lagrange polynomial */

        //TODO  Lagrange polynomial

        



    
}
