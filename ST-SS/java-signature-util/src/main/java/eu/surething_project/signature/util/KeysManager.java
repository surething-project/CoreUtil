package eu.surething_project.signature.util;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

/**
 * 
 * SureThing Framework KeysManager Class  
 * 
 * 
 * helper class to manager the retrieval of private/public keys.
 * 
 * 
 * It is for testing purposes.
 * 
 * 
 * The functionalities in this class should be handled by the application who uses the signature library.
 *  
 * 
 * 
 * @author Samih
 *
 */
public class KeysManager {
	
	/** Keys generation related properties, they should be provided by the application **/
	static final String STORE_TYPE = "PKCS12";
	static final char[] PASSWORD = "mypass".toCharArray();
	static final String SENDER_KEYSTORE = "sender_keystore.p12";
	static final String SENDER_ALIAS = "senderKeyPair";
	static final String RECEIVER_KEYSTORE = "receiver_keystore.p12";
    static final String RECEIVER_ALIAS = "receiverKeyPair";
 	
	/**
	 * 
	 * returns private key of the entity from local repository
	 * 
	 * @return
	 * @throws Exception
	 */
	public static PrivateKey getPrivateKey() throws Exception{
		KeyStore keyStore = KeyStore.getInstance(STORE_TYPE);
    	InputStream inputFile = SignatureProperties.class.getClassLoader().getResourceAsStream(SENDER_KEYSTORE);
		keyStore.load(inputFile, PASSWORD);
		return (PrivateKey)keyStore.getKey(SENDER_ALIAS, PASSWORD);
	}
	
	/**
	 * 
	 * returns the public key of an entity from local / public repository
	 * 
	 * @return
	 * @throws Exception
	 */
	public static PublicKey getPublicKey(String entityName /** param entityName to retrieve specific public key  **/) throws Exception{
		KeyStore keyStore = KeyStore.getInstance(STORE_TYPE);
    	InputStream inputFile = SignatureProperties.class.getClassLoader().getResourceAsStream(RECEIVER_KEYSTORE);
		keyStore.load(inputFile, PASSWORD);
		Certificate certificate = keyStore.getCertificate(RECEIVER_ALIAS);
    	return certificate.getPublicKey();
	}
	

}
