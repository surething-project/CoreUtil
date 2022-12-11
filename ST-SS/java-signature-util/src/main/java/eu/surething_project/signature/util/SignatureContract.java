package eu.surething_project.signature.util;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * 
 * SureThing Framework Signature Contract Class
 * 
 * 
 * @author Samih
 *
 */
public interface SignatureContract {
	
	
	/**
	 * 
	 * sign a message with private key
	 * 
	 * @param message
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	public byte[] sign(byte[] message, PrivateKey privateKey) throws Exception;
	
	
	/**
	 * 
	 * verify the signature of a signed message.
	 * 
	 * @param signedMessage
	 * @param publicKey 
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public boolean verify(byte [] signedMessage, PublicKey publickey, SignatureProperties.MESSAGE_TYPE type) throws Exception;
	
	
	/**
	 * 
	 * verify that the received signature is generated from the received message. 
	 * 
	 * @param signature
	 * @param message
	 * @param publickey
	 * @return
	 * @throws Exception
	 */
	public boolean verify(byte [] signature, byte [] message, PublicKey publickey) throws Exception;
	
	

}
