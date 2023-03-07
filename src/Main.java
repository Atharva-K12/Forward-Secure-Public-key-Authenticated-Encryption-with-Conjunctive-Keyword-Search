import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
public class Main {
    public static void main(String[] args){
        Pairing pairing = PairingFactory.getPairing("jpbc-2.0.0\\params\\curves\\a.properties");

        Element g = pairing.getG1().newRandomElement().getImmutable();
        Element h = pairing.getG1().newRandomElement().getImmutable();

        Element x = pairing.getZr().newRandomElement().getImmutable();
        Element y = pairing.getZr().newRandomElement().getImmutable();

        Element gx = g.powZn(x).getImmutable();
        Element gy = g.powZn(y).getImmutable();

        Element hxy = h.powZn(x.mul(y)).getImmutable();

        System.out.println("g: " + g);
        System.out.println("h: " + h);
        System.out.println("x: " + x);
        System.out.println("y: " + y);
        System.out.println("gx: " + gx);
        System.out.println("gy: " + gy);
        System.out.println("hxy: " + hxy);

        Element e1 = pairing.pairing(gx, h).getImmutable();
        Element e2 = pairing.pairing(g, hxy).getImmutable();

        System.out.println("e(gx, h): " + e1);
        System.out.println("e(g, hxy): " + e2);

    }
    
}
