import java.util.HashSet;
import java.util.Set;

import it.unisa.dia.gas.jpbc.Element;

public class Trapdoor {

    public Element[] piYi;
    public Set<Integer> T_dash;
    public Element pi1;
    public Element pi2;
    public Element pi3;

    public Trapdoor(PrivateKey skR, PublicKey pkS, String [] Q, GlobalParameters pp, Element[] hi, Element[] fi)
    {
        Element K = pkS.pk.powZn(skR.sk);

        Element S = pp.getE().getZr().newRandomElement().getImmutable();

        this.pi1 = pp.getg().powZn(S);
        this.pi2 = pp.getE().getG1().newOneElement();
        this.pi3 = pp.getE().getG1().newOneElement();

        for (int i = 0; i < Q.length; i++) {
            pi2 = pi2.mul(hi[i].powZn(S));
            pi3 = pi3.mul(fi[i].powZn(S.div(skR.sk)));
        }

        long t_dash = System.currentTimeMillis();
        String binaryT_dash = Long.toBinaryString(t_dash);
        Set<Integer> T_dash = oneEncoding(binaryT_dash);
        Element[] piYi = new Element[T_dash.size()];
        int i = 0;
        for (Integer Y: T_dash){
            piYi[i] = pp.getH1().hash(new byte[]{Y.byteValue()}, K);
            i++;
        }
        
        this.piYi = piYi;
        this.T_dash = T_dash;       
    }
    public Set<Integer> oneEncoding(String m) {
        Set<Integer> S1 = new HashSet<>();
        for (int i = 0; i < m.length(); i++) {
            if (m.charAt(i) == '1') {
                String substring = m.substring(i, Math.min(i+1, m.length()));
                int value = Integer.parseInt(substring, 2);
                S1.add(value);
            }
        }
        return S1;
    }
    
}
