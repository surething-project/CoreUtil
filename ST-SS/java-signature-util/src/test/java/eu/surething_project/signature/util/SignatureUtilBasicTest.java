package eu.surething_project.signature.util;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import org.junit.jupiter.api.Test;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

/**
 * 
 * 
 * SureThing Framework Signature Util library Test suit - Basic Test
 * 
 * 
 */
public class SignatureUtilBasicTest {

	/** Plain text message */
	private final String message = "This is a message!";
	
	/**
	 * Message Signature Test 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMessageSignature()throws Exception {
		System.out.println("Origianl Message Test");
		System.out.println("Message : " + message);
		byte[] messageBytes = message.getBytes();
		System.out.println("Message Bytes: " + printHexBinary(messageBytes));
		System.out.println("Digital Signature");
		byte[] digitalSignature = SignatureManager.sign(messageBytes, KeysManager.getPrivateKey());
		System.out.println("Veriy Digital Signature");
		boolean isCorrect = SignatureManager.verify(digitalSignature, messageBytes, KeysManager.getPublicKey("Prover"));
		System.out.println("Signature " + (isCorrect ? "correct" : "incorrect"));
		assertTrue(isCorrect);
	}
	
	
	/**
	 * Tampered Message Test
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTamperedMessage() throws Exception {
		System.out.println("Tampered Message Test");
		System.out.println("Message : " + message);
		byte[] messageBytes = message.getBytes();
		System.out.println("Message Bytes: " + printHexBinary(messageBytes));
		System.out.println("Digital Signature");
		byte[] digitalSignature = SignatureManager.sign(messageBytes, KeysManager.getPrivateKey());
		
		// tamper message
		messageBytes[4] = 100;
		System.out.println("Tampered Message Bytes: " + printHexBinary(messageBytes));
		System.out.println("Veriy Digital Signature");
		boolean isCorrect = SignatureManager.verify(digitalSignature, messageBytes, KeysManager.getPublicKey("Prover"));
		System.out.println("Signature " + (isCorrect ? "correct" : "incorrect"));
		assertFalse(isCorrect);
	}
}