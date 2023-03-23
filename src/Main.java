public class Main {
    public static void main(String[] args){
        Setup setup = new Setup();
        GlobalParameters pp = setup.pp;
        KeyGenSender KGenSend = new KeyGenSender(pp);
        KeyGenReceiver KGenRec = new KeyGenReceiver(pp);
        PublicKey pkR = KGenRec.pk_r;
        PrivateKey skS = KGenSend.sk_s;
        String[] W = new String[]{"String1", "String2"};

        Encryption e = new Encryption(pkR, skS, W, pp);
        String[] Q = new String[]{"String1", "String2"};
        Trapdoor td = new Trapdoor(KGenSend.sk_s, KGenRec.pk_r, Q, pp, e.hi, e.fi);
        
        Test test = new Test(e.ct,td,pp,KGenRec.sk_r,KGenSend.pk_s);
    }
}