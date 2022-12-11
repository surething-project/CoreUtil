package eu.surething_project.signature.util;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import com.google.protobuf.InvalidProtocolBufferException;
import eu.surething_project.core.*;



/**
 * 
 * SureThing Framework Signature Manager Class
 * 
 * 
 * @author Samih
 *
 */
public class SignatureManager {

	
	/**
	 * sign a message with private key
	 * 
	 */
	public static byte[] sign(byte[] message, PrivateKey privateKey) throws Exception {
    	Signature signature = Signature.getInstance(SignatureProperties.CRYPTO_SIGNATURE_ALGO.SHA256withRSA);
        signature.initSign(privateKey);
        signature.update(message);
        byte[] generatedSignature = signature.sign();
        return generatedSignature;
	}
	
	
	/**
	 * verify that the received signature is generated from the received message.
	 * 
	 */
	public static boolean verify(byte [] SenderSignature, byte [] message, PublicKey publicKey) throws Exception {
		Signature signature = Signature.getInstance(SignatureProperties.CRYPTO_SIGNATURE_ALGO.SHA256withRSA);
        signature.initVerify(publicKey);
        signature.update(message);
        boolean isCorrect = signature.verify(SenderSignature);
    	return isCorrect;
	}
	

	/**
	 *  verify the signature of a signed message. 
	 * 
	 */
	public static boolean verify(byte [] signedMessage, PublicKey publicKey, SignatureProperties.MESSAGE_TYPE type) throws Exception {
		
		// Signature Properties
		Signature signature = Signature.getInstance(SignatureProperties.CRYPTO_SIGNATURE_ALGO.SHA256withRSA);
        signature.initVerify(publicKey);
        
        /**
         *  extract the sender signature from the received signedMessage and then verify
         *  
         *  Check the type of the message first
         */
        boolean isCorrect = false;
        
        switch(type) {
        
        // Location Claim
        case CLAIM:{
        	
        	SignedLocationClaim signedlocationClaim = null;
    		try{
    			signedlocationClaim = SignedLocationClaim.parseFrom(signedMessage);
    			byte [] claim = signedlocationClaim.getClaim().toByteArray();
    			byte [] proverSignature = signedlocationClaim.getProverSignature().getValue().toByteArray();
    			signature.update(claim);
    			isCorrect = signature.verify(proverSignature);

    		}catch(InvalidProtocolBufferException e){
    			e.printStackTrace();
    		}
        	
        } break;
        
        // Location Endorsement
        case ENDORSEMENT: {
        	
        	SignedLocationEndorsement signedlocationEndorsement = null;
    		try{
    			signedlocationEndorsement = SignedLocationEndorsement.parseFrom(signedMessage);
    			byte [] endorsement = signedlocationEndorsement.getEndorsement().toByteArray();
    			byte [] witnessSignature = signedlocationEndorsement.getWitnessSignature().getValue().toByteArray();
    			signature.update(endorsement);
    			isCorrect = signature.verify(witnessSignature);

    		}catch(InvalidProtocolBufferException e){
    			e.printStackTrace();
    		}
        	
        } break;
        
        // Location Certificate
        case CERTIFICATE: {
        	
        	LocationCertificate locationCertificate  = null;
    		try{
    			locationCertificate = LocationCertificate.parseFrom(signedMessage);
    			byte [] verification = locationCertificate.getVerification().toByteArray();
    			byte [] verifierSignature = locationCertificate.getVerifierSignature().getValue().toByteArray();
    			signature.update(verification);
    			isCorrect = signature.verify(verifierSignature);

    		}catch(InvalidProtocolBufferException e){
    			e.printStackTrace();
    		}
        	        	
        } break;
        }
        
        return isCorrect;
	}
	
	
	
	

}
