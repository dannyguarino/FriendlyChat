package android.example.friendlychat.cryptography;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.example.friendlychat.MainActivity;
import android.util.Base64;
import android.util.Log;

import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSA {

    private static final String LOG_TAG = MainActivity.class.getName();

    private static Key publicKey = null;
    private static Key privateKey = null;

    private static String publicKeyBytesBase64 = null;
    private static String privateKeyBytesBase64 = null;
    private static String friendPublicKey = null;

    public static void generateKeys(){
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            KeyPair kp = kpg.genKeyPair();
            publicKey = kp.getPublic();
            privateKey = kp.getPrivate();
            publicKeyBytesBase64 = convertKeyToString(publicKey);
            privateKeyBytesBase64 = convertKeyToString(privateKey);

        } catch (Exception e){
            Log.e("Crypto", "RSA key pair error");
        }
    }

    public static String convertKeyToString(Key key){
        byte[] keyBytes = key.getEncoded();
        String keyBytesBase64 = new String(Base64.encode(keyBytes, Base64.DEFAULT));
        return keyBytesBase64;
    }

    public static String encrypt(String text, String publicKey){

        String encryptedBase64 = "";
        try {
            KeyFactory keyFac = KeyFactory.getInstance("RSA");
            KeySpec keySpec = new X509EncodedKeySpec(Base64.decode(publicKey.trim().getBytes(), Base64.DEFAULT));
            Key key = keyFac.generatePublic(keySpec);

            // get an RSA cipher object
            final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encryptedBytes = cipher.doFinal(text.getBytes("UTF-8"));
            encryptedBase64 = new String(Base64.encode(encryptedBytes, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // replace all newline and carriage return with the empty String
        return encryptedBase64.replaceAll("(\\r|\\n)", "");
    }

    public static String decrypt(String encryptedBase64, String privateKey){

        String decryptedString = "";
        try {
            KeyFactory keyFac = KeyFactory.getInstance("RSA");
            KeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey.trim().getBytes(), Base64.DEFAULT));
            Key key = keyFac.generatePrivate(keySpec);

            // get an RSA cipher object
            final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
            // decrypt the encoded String with the private Key
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] encryptedBytes = Base64.decode(encryptedBase64, Base64.DEFAULT);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            decryptedString = new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decryptedString;
    }

    public static void saveKeysToSharedPreferences(Context context){

        Activity activity = (Activity) context;
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();
        Log.e(LOG_TAG, "Keys stored in SharedPreferenes");
        editor.putString("publicKeyBytesBase64", publicKeyBytesBase64);
        editor.putString("privateKeyBytesBase64", privateKeyBytesBase64);
        editor.apply();
    }

    public static void loadKeysFromSharedPreferences(Context context){

        Activity activity = (Activity) context;
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);

        Log.e(LOG_TAG, "Keys loaded from SharedPreferences");
        publicKeyBytesBase64 = sharedPref.getString("publicKeyBytesBase64", null);
        privateKeyBytesBase64 = sharedPref.getString("privateKeyBytesBase64", null);
    }

    public static String getPrivateKeyBytesBase64() {
        return privateKeyBytesBase64;
    }

    public static String getPublicKeyBytesBase64() {
        return publicKeyBytesBase64;
    }

    public static void setFriendPublicKey(String friendPublicKey) {
        RSA.friendPublicKey = friendPublicKey;
    }

    public static String getFriendPublicKey() {
        return friendPublicKey;
    }

    //    public static void stringToKey(String publicKeyB64) {
//
//        if (publicKeyB64.contains("-----BEGIN PUBLIC KEY-----") || publicKeyB64.contains("-----END PUBLIC KEY-----"))
//            publicKeyB64 = publicKeyB64.replace("-----BEGIN PUBLIC KEY-----", "").replace("-----END PUBLIC KEY-----", "");
//        if (publicKeyB64.contains("-----BEGIN RSA PUBLIC KEY-----") || publicKeyB64.contains("-----END RSA PUBLIC KEY-----"))
//            publicKeyB64 = publicKeyB64.replace("-----BEGIN RSA PUBLIC KEY-----", "").replace("-----END RSA PUBLIC KEY-----", "");
//
//        // ok, you may need to use the Base64 decoder of bouncy or Android instead
//        try{
//            byte[] decoded = Base64.decode(publicKeyB64, Base64.DEFAULT);
//            org.bouncycastle.asn1.pkcs.RSAPublicKey pkcs1PublicKey = org.bouncycastle.asn1.pkcs.RSAPublicKey.getInstance(decoded);
//            BigInteger modulus = pkcs1PublicKey.getModulus();
//            BigInteger publicExponent = pkcs1PublicKey.getPublicExponent();
//            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, publicExponent);
//            KeyFactory kf = KeyFactory.getInstance("RSA");
//            PublicKey generatedPublic = kf.generatePublic(keySpec);
//            friendPublicKey =generatedPublic;
//        }
//        catch (Exception e){
//            Log.i("your log","errro in dear function");
//        }
//
//    }
}
