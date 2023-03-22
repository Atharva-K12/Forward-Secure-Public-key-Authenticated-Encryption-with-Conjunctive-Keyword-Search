import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import javax.swing.text.ElementIterator;

import it.unisa.dia.gas.jpbc.Element;

public class Encryption {

    /*Enc(pkR, skS, W) : Given a selected keyword set W =
    {w1 , · · · , wl}, this algorithm is executed in the following
    way */
    CipherKeyword ct;
    Element[] hi;
    Element[] fi;
    public Encryption(PublicKey pkR, PrivateKey skS, String[] W, GlobalParameters pp){
        /*Select three random values r1 , r2 and r3 from Z∗p
        and compute X = g^r1 and κ = pk skS
        R = gαβ*/

        Element r1 = pp.getE().getZr().newRandomElement().getImmutable();
        Element r2 = pp.getE().getZr().newRandomElement().getImmutable();
        Element r3 = pp.getE().getZr().newRandomElement().getImmutable();

        Element X = pp.getg().powZn(r1);

        Element k = pkR.pk.powZn(skS.sk);

        /*Get the system time t and t is encoded as set T via
        0-encoding */

        //t is encoded as set T via 0-encoding
        //TODO  0-encoding
        long t = System.currentTimeMillis();

        // Convert t to a binary string and pad with zeros to ensure it has the desired length
        String binaryT = Long.toBinaryString(t);
        // binaryT = String.format("%64s", binaryT).replace(' ', '0');

        // // Convert the binary string to an array of bytes
        // byte[] bytesT = new BigInteger(binaryT, 2).toByteArray();

        // // Encode the byte array as a set T using 0-encoding
        // Set<Integer> T = new HashSet<>();
        // for (int i = 0; i < bytesT.length; i++) {
        //     if (bytesT[i] == 0) {
        //         T.add(i);
        //     }
        //     System.out.println(i);
        //     System.out.println(bytesT[i]);
        // }
        Set<Integer> T = zeroEncoding(binaryT);

        /*Compute xi = H1(yi, κ) for yi ∈ T and 1 ≤
        i ≤ n, and then set x0 = H1(0, κ) individually
        for 0 /∈ T. Finally, use these n + 1 points
        {(x0 , r3), (x1 , r2), (x2 , r2), · · · , (xn, r2)} to build a
        Lagrange polynomial */

        Element x0 = pp.getH1().hash(new byte[]{0}, k);
        Element[] xi = new Element[T.size()+1];
        xi[0]=x0;
        byte[] yi = new byte[T.size()+1];
        yi[0]=0;
        int i = 1;
        for (Integer y : T) {
            xi[i] = pp.getH1().hash(new byte[]{(byte) y.intValue()}, k);
            // System.out.println(xi[i]);
            yi[i] = y.byteValue();
            i++;
        }
        
        
        Element[][] A = LagrangePolynomial(xi, pp);
        Element [] ai = aiCoefficient(A,r2,r3);
        
        Element[] Ri = new Element[xi.length];
        for(int j = 0; j < xi.length; j++){
            Ri[j] = pkR.pk.powZn(ai[j]);
        }
        
        Element[] hi = new Element[W.length];
        Element[] fi = new Element[W.length];
        Element[] CTi = new Element[W.length];
        
        for(int j = 0; j < W.length; j++){
            // W[j] is a string, so we need to convert it to a byte array
            byte[] Wj = W[j].getBytes();
            hi[j] = pp.getH2().hash(Wj, k);
            fi[j] = pp.getH3().hash(Wj, k);
            CTi[j] = hi[j].powZn(r1).mul(fi[j].powZn(r2));
        }
        // YI = H4(yi)
        for (int j = 0; j < yi.length; j++) {   
        }
        byte [] HashYi = pp.getH4().hash(yi);


        this.ct = new CipherKeyword(HashYi, X, Ri, CTi);
        this.hi = hi;
        this.fi = fi;
    
    }
    public Element[] aiCoefficient(Element[][]A,Element r2,Element r3){
        int n = A[0].length;
        System.out.println(n);
        System.out.println(A.length);
        Element []ai = new Element[A[0].length];
        for (int i = 0; i<n ;i++){
            Element temp = r2.duplicate().set(0);
            for (int j = 1; j< n;j++){
                temp = temp.add(A[j][i]);
            }
            ai[i] = r3.mul(A[0][i]).add(temp.mul(r2));
        }
        return ai;
        
    }

    public Set<Integer> zeroEncoding(String m) {
        Set<Integer> S0 = new HashSet<>();
        for (int i = 0; i < m.length(); i++) {
            if (m.charAt(i) == '0') {
                String substring = m.substring(i, Math.min(i+2, m.length()));
                int value = Integer.parseInt(substring, 2);
                S0.add(value);
            }
        }
        return S0;
    }

    private Element[] vietaFElements(Element[] roots, GlobalParameters pp, int ind) {
        Element[] f = new Element[roots.length+1];
        f[0] = pp.getE().getZr().newOneElement();
        for (int i = 0; i < roots.length; i++) {
            f[i+1]=pp.getE().getZr().newZeroElement();
            for (int j = i+1; j > 0; j--) {
                if (i != ind)
                    f[j] = f[j].add(f[j - 1].mul(roots[i]));
            }
            // for (int j = 0; j <= i; j++) {
            //     Element root_j = roots[j];
            //     Element prev = f[i-j];
            //     f[i+1].add(prev.mul(root_j));
            // }
        }
        return f;
    }
    public Element calculateDenominator(Element[] elements, int i) {
        Element result = elements[i].duplicate().set(1); // start with element at index i
        for (int j = 0; j < elements.length; j++) {
            if (j != i) {
                Element diff = elements[i].duplicate().sub(elements[j]); // calculate difference between i and j
                result = result.mul(diff); // multiply difference into running product
            }
        }
        return result;
    }
    
    public Element[][] LagrangePolynomial(Element[] xi, GlobalParameters pp){
        Element[][] A = new Element[xi.length+1][xi.length+1];
        System.out.println(xi.length+1);
        for (int i = 0; i <=xi.length; i++) {
            A[i] = vietaFElements(xi, pp, i);
            Element a_denominator = calculateDenominator(xi, i);
            System.out.println(a_denominator);
            for (Element a : A[i]){
                a = a.mul(a_denominator.invert());
            }
        }
        return A;
    }

    public static void main(String[] args) {  

        Setup setup = new Setup();
        GlobalParameters pp = setup.pp;
        KeyGenSender KGenSend = new KeyGenSender(pp);
        KeyGenReceiver KGenRec = new KeyGenReceiver(pp);
        PublicKey pkR = KGenRec.pk_r;
        PrivateKey skS = KGenSend.sk_s;
        String[] W = new String[]{"a", "b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};

        Encryption e = new Encryption(pkR, skS, W, pp);

        System.out.println("Encryption");
        System.out.println(e.ct);
        System.out.println("X");
        System.out.println(e.ct.getX());
        System.out.println("Ri");
        System.out.println("Size: " + e.ct.getRi().length);
        System.out.println(e.ct.getRi()[0]);
        System.out.println(e.ct.getCti()[0]);
        System.out.println(e.ct.getCti().length);
        System.out.println(e.ct.gethYi());

        



        // Encryption e = new Encryption(new PublicKey(), new PrivateKey(), new String[]{"a", "b"}, new GlobalParameters());

    }

}
