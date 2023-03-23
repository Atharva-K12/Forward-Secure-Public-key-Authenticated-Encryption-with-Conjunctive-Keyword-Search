import java.util.HashSet;
import java.util.Set;

public class encodingTest {
    
    

    public static Set<byte[]> zeroEncoding(String m) {
        Set<byte[]> S0 = new HashSet<byte[]>();
        for (int i = 0; i < m.length(); i++) {
            if (m.charAt(i) == '0') {
                S0.add((m.substring(0, i) + "1").getBytes());
            }
        }
        return S0;
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
        public Byte[] intersection(Set<byte[]> hYi, Set<byte[]> hYi_dash) {
            
    
            hYi.retainAll(hYi_dash);
    
            return hYi.toArray(new Byte[hYi.size()]);
        }
        public static void main(String[] args) {
            String m = "0001100110";
            String m1 = "01";
            Set<byte[]> S0 = zeroEncoding(m);
            Set<byte[]> S1 = oneEncoding(m1);
            System.out.println("S0 = " + S0);
            System.out.println("S1 = " + S1);
            // Byte[] b = intersection(S0, S1);
            // System.out.println("b = " + b);
            
            System.out.println(S0.retainAll(S1));
            System.out.println("S0 = " + S0);
        }
    }
