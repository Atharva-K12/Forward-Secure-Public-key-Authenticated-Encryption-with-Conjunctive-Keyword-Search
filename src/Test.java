import java.math.BigInteger;
import java.util.*;
import it.unisa.dia.gas.jpbc.Element;

public class Test {

/*
    public int intersection(ArrayList<Integer> set1, ArrayList<Integer> set2){

        int retval = 0;
        for(int i = 0; i<set1.length; i++ ) {
         for(int j = 0; j<set2.length; j++) {
            if(set1[i]==set2[j]) {
                retval=1;
                break;
            }

        return retval;
    }
*/

    public Byte[] intersection(byte[][] hYi, byte[][] hYi_dash) {
        Set<byte[]> set1 = new HashSet<byte[]>();
        Set<byte[]> set2 = new HashSet<byte[]>();

        for (byte b[] : hYi) {
            set1.add(b);
        }

        for (byte b[] : hYi_dash) {
            set2.add(b);
        }

        set1.retainAll(set2);

        return set1.toArray(new Byte[set1.size()]);
    }

    Test(CipherKeyword ct, Trapdoor td, GlobalParameters pp, PrivateKey skR, PublicKey pkS) {

        Element K = pkS.pk.powZn(skR.sk);

        Element[] ri = ct.getRi();       // R_i

        byte[][] hYi = ct.gethYi();           // H4() of Y_i
        byte[][] hYi_dash = td.hYi_dash;   // H4() of Y_i'

        Byte[] yi = intersection(hYi, hYi_dash);

        Element mu = ri[0];

        if (yi.length > 0) {
            for (Byte b : yi) {
                Element piY = pp.getH1().hash(new byte[]{b}, K);
                for (int i = 1; i < ri.length; i++) {
                    mu.mul(ri[i].powZn(piY.pow(BigInteger.valueOf(i))));
                }
                
                // Test condition 
                // RHS
                Element rhs = pp.getE().pairing(ct.getX(), td.pi2).mul(pp.getE().pairing(mu, td.pi3));

                // LHS
                Element cti_prod = pp.getg().setToOne();
                for (Element c : ct.getCti()) {
                    cti_prod.mul(c);
                }
                Element lhs = pp.getE().pairing(td.pi1, cti_prod);

                if (lhs.equals(rhs)) {
                    System.out.println("Success!");
                }
            }
            System.out.println("Failure!");
        }
        else {
            System.out.println("Failure!");
        }
    }
}

