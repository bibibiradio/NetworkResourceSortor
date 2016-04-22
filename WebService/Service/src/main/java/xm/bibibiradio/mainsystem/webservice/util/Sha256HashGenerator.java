package xm.bibibiradio.mainsystem.webservice.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha256HashGenerator implements HashGenerator {
    private MessageDigest md = null;

    //static private HashGenerator hashGenerator = null;

    static public HashGenerator getHashGenerator() {
        return new Sha256HashGenerator();
    }

    public Sha256HashGenerator() {
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public byte[] getHash(byte[] needHashBytes) {
        // TODO Auto-generated method stub
        md.update(needHashBytes);
        return md.digest();
    }

}
