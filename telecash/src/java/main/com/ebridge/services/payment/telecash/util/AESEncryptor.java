package com.ebridge.services.payment.telecash.util;

import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * @author david@tekeshe.com
 */
public class AESEncryptor {

    public static final String CLASS_NAME = AESEncryptor.class.getName();
    private static final int KEY_SIZE = 16;
    private static final String ALGORITHM_AES = "AES";
    public final static String ALGORITHM_AES_CBC = "AES/CBC/PKCS5Padding";

    private static Key generateKey(String keyValue) throws UnsupportedEncodingException {

        Key key = null ;
        if (keyValue!=null && keyValue.length()==KEY_SIZE) {

            byte[] byteKey = keyValue.substring(0, KEY_SIZE).getBytes("UTF-8");
            key = new SecretKeySpec(byteKey, ALGORITHM_AES);
        } else {
            System.out.println("Not generating the Key!! " + keyValue);
        }
        return key;
    }

    /**
     * Return Base64 Encoded value of IV *
     * @param keyValue * @return
     * @throws Exception */
    public static String generateIV( String keyValue ) throws   UnsupportedEncodingException,
                                                                NoSuchPaddingException,
                                                                NoSuchAlgorithmException,
                                                                InvalidKeyException,
                                                                InvalidParameterSpecException {

        String iv = null ;
        Key key = generateKey( keyValue );

        if ( key!=null ) {

            Cipher cipher = Cipher.getInstance(ALGORITHM_AES_CBC);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            AlgorithmParameters params = cipher.getParameters();
            iv = new BASE64Encoder().encode(params.getParameterSpec(IvParameterSpec.class).getIV());
        } else {
            System.out.println("No IV generated ...");
        }

        return iv;
    }

    /**
     * Method to perform encryption of given data with AES Algorithm / Key and IV. * @param encKey -
     *	Encryption Key value * @param plainVal -
     *	Value to be encrypted * @return - encrypted String Value * @throws Exception
     */
    public static String encrypt(String encKey, String plainVal, String currentIV) throws Exception {

        String encryptedText = null ;
        Key key = generateKey(encKey);
        if ( key!=null && currentIV!=null && plainVal!=null) {

            Cipher c = Cipher.getInstance(ALGORITHM_AES_CBC);
            c.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new BASE64Decoder().decodeBuffer(currentIV)));
            byte[] encValue = c.doFinal(plainVal.getBytes());
            encryptedText= new BASE64Encoder().encode(encValue);
        } else {

            System.out.println("Invalid input passed to encrypt !! keyValue="+encKey+", IV="+currentIV+", valueToEnc="+plainVal);
        }

        return encryptedText;
    }
}
