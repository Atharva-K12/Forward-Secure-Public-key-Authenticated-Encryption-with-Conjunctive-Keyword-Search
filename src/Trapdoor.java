import java.util.HashSet;
import java.util.Set;

import it.unisa.dia.gas.jpbc.Element;

public class Trapdoor {

    public Element[] piYi;
    public Set<byte[]> T_dash;
    public Element pi1;
    public Element pi2;
    public Element pi3;
    public byte [][] hYi_dash;

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
        Set<byte[]> T_dash = oneEncoding(binaryT_dash);
        Element[] piYi = new Element[T_dash.size()];
        byte[][] yi = new byte[T_dash.size()+1][];
        yi[0]=new byte[]{0};
        int i = 0;
        for (byte[] Y: T_dash){
            piYi[i] = pp.getH1().hash(Y, K);
            yi[i+1] = Y;
            i++;
        }
        
        this.hYi_dash = pp.getH4().hash(yi);
        this.piYi = piYi;
        this.T_dash = T_dash;       
    }
    public static Set<byte[]> oneEncoding(String m) {
        Set<byte []> S1 = new HashSet<byte []>();
        for (int i = 0; i < m.length(); i++) {
            if (m.charAt(i) == '1') {
                S1.add((m.substring(0, i+1)).getBytes());
            }
        }
        return S1;
    }
    
}

