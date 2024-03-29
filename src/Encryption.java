import java.util.HashSet;
import java.util.Set;

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

        Element X = pp.getg().duplicate().powZn(r1);

        Element k = pkR.pk.duplicate().powZn(skS.sk);

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
        Set<byte[]> T = zeroEncoding(binaryT);
        /*Compute xi = H1(yi, κ) for yi ∈ T and 1 ≤
        i ≤ n, and then set x0 = H1(0, κ) individually
        for 0 /∈ T. Finally, use these n + 1 points
        {(x0 , r3), (x1 , r2), (x2 , r2), · · · , (xn, r2)} to build a
        Lagrange polynomial */
        // print all values of T

        // Set<byte[]> uniqueValues = new HashSet<>(T);
        // for (byte[] y : uniqueValues) {

        //     // convert byte array to string
        //     String str = new String(y);
        //     // System.out.println(str);
        // }

        

        // System.out.println("-T");
        HashFunction H1 = pp.getH1();
        Element x0 = H1.hash(new byte[]{0}, k);
        Element[] xi = new Element[T.size()+1];
        xi[0]=x0;
        byte[][] yi = new byte[T.size()+1][T.size()+1];
        yi[0]=new byte[]{0};
        int i = 1;
        for (byte[] y : T) {
            xi[i] = H1.hash(y, k);
            Boolean tempBoolean = xi[i].equals(xi[i-1]);
            yi[i] = y;
            i++;
        }
        int d=0;
        for ( i = 0; i < xi.length; i++) {
            for (int j = i + 1; j < xi.length; j++) {
                if (xi[i].equals(xi[j])) {
                    // System.out.println("Duplicate Element : " + xi[i]);
                    d++;
                }
            }
        }
        // System.out.println("d");
        // System.out.println(d);


        // System.out.println("xi");
        // for (int j = 0; j < xi.length; j++) {
        //     System.out.println(xi[j]);
        // }
        // System.out.println("-xi");
        
        Element[][] A = LagrangePolynomial(xi, pp);
        Element [] ai = aiCoefficient(A,r2,r3);
        
        Element[] Ri = new Element[xi.length];
        for(int j = 0; j < xi.length; j++){
            Ri[j] = pkR.pk.duplicate().powZn(ai[j]);
        }
        
        Element[] hi = new Element[W.length];
        Element[] fi = new Element[W.length];
        Element[] CTi = new Element[W.length];
        
        for(int j = 0; j < W.length; j++){
            // W[j] is a string, so we need to convert it to a byte array
            byte[] Wj = W[j].getBytes();
            hi[j] = pp.getH2().hash(Wj, k);
            fi[j] = pp.getH3().hash(Wj, k);
            CTi[j] = hi[j].duplicate().powZn(r1).mul(fi[j].duplicate().powZn(r2));
        }
        // YI = H4(yi)
        for (int j = 0; j < yi.length; j++) {   
        }
        byte [][] HashYi = pp.getH4().hash(yi);


        this.ct = new CipherKeyword(HashYi, X, Ri, CTi);
        this.hi = hi;
        this.fi = fi;
    
    }
    public Element[] aiCoefficient(Element[][]A,Element r2,Element r3){
        int n = A[0].length;
        // System.out.println(n);
        // System.out.println(A.length);
        Element []ai = new Element[A[0].length];
        for (int i = 0; i<n ;i++){
            Element temp = r2.duplicate().set(0);
            for (int j = 1; j< n;j++){
                temp = temp.add(A[j][i]);
            }
            ai[i] = r3.duplicate().mul(A[0][i]).add(temp.mul(r2));
        }
        return ai;
        
    }

    public static Set<byte[]> zeroEncoding(String m) {
        Set<byte[]> S0 = new HashSet<byte[]>();
        for (int i = 0; i < m.length(); i++) {
            if (m.charAt(i) == '0') {
                S0.add((m.substring(0, i) + "1").getBytes());
            }
        }
        // System.out.println(S0.size());
        return S0;
    }



    private Element[] vietaFElements(Element[] roots, int ind) {
        // int n = roots.length;
        // Element[] f = new Element[n];
        // f[0] = roots[0].getField().newOneElement();
        // // for (int i = 0; i < roots.length-1; i++) {
        // //     f[i+1]=pp.getE().getZr().newZeroElement();
        // //     for (int j = i+1; j > 0; j--) {
        // //         if (i != ind)
        // //             f[j] = f[j].add(f[j - 1].mul(roots[i]));
        // //     }
        // //     // for (int j = 0; j <= i; j++) {
        // //     //     Element root_j = roots[j];
        // //     //     Element prev = f[i-j];
        // //     //     f[i+1].add(prev.mul(root_j));
        // //     // }
        // // }
        // for (int i = 0; i<n;i++){
        //     if (i==0)
        //     {
        //         f[i] = roots[0].getField().newOneElement();
        //     }
        //     else
        //     {
        //         f[i] = roots[0].getField().newZeroElement();
        //     }
        // }

        // for (int i = 0; i < n; i++) {
        //     if (i != ind) {
        //         Element root_i = roots[i];
        //         for (int j = n-1; j > 0; j--) {
        //             f[j] = f[j].add(f[j - 1].mul(root_i));
        //         }
        //     }
        // }

        int n = roots.length - 1;
        Element[] coeff = new Element[n + 1];
        // Set all coefficients as zero initially
        for (int i = 0; i <= n; i++) {
            coeff[i] = roots[0].duplicate().setToZero();
        }
        // Set highest order coefficient as 1
        coeff[0].setToOne();
        for (int i = 0; i < n; i++) {
            if (i == ind) continue; // Skip the root at index ind
            Element root = roots[i];
            for (int j = i + 1; j > 0; j--) {
                coeff[j] = coeff[j].add(root.duplicate().mul(coeff[j - 1]));
            }
        }
        return coeff;
    }
    public Element calculateDenominator(Element[] elements, int i) {
        Element result = elements[i].duplicate().set(1); // start with element at index i
        for (int j = 0; j < elements.length; j++) {
            if (j != i) {
                if (elements[i].equals(elements[j]))
                {
                    // System.out.println("Duplicate elements in array");
                    // System.out.println(elements[i]);
                    // System.out.println(elements[j]);
                    // elements[i].setToRandom(); //TODO: fix this
                }
                // throw new IllegalArgumentException("Duplicate elements in array");
                Element diff = elements[i].duplicate().sub(elements[j]); // calculate difference between i and j
                result = result.mul(diff); // multiply difference into running product
            }
        }
        return result;
    }
    
    public Element[][] LagrangePolynomial(Element[] xi, GlobalParameters pp){
        Element[][] A = new Element[xi.length][xi.length];
        // System.out.println(xi.length);
        // System.out.println(xi[0]);
        // System.out.println(xi[1]);
        for (int i = 0; i < xi.length; i++) {
            A[i] = vietaFElements(xi, i);
            Element a_denominator = calculateDenominator(xi, i);
            // System.out.println(a_denominator);
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
        String[] W = new String[]{"String1", "String2"};//,"c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};

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
