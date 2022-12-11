package eu.surething_project.signature.util;

/**
 * 
 * SureThing Framework SignatureProperties Class
 * 
 * @author Samih
 *
 */
public class SignatureProperties {
	
	/** supported cryptographic algorithms */
	public static class CRYPTO_ALGO{
		static final String RSA ="RSA", ECDSA = "ECDSA";
	}
	
	/** supported cryptographic signature algorithms */
	public static class CRYPTO_SIGNATURE_ALGO{
		static final String SHA256withRSA ="SHA256withRSA";
	}
	
	/** supported cryptographic digest algorithms */
	public static class CRYPTO_DIGEST_ALGORITHM{
		static final String SHA256 = "SHA-256", SHA384 ="SHA-384", SHA512 ="SHA-512";
	}
	
	/** supported key size */
	public static class CRYPTO_KEY_SIZE{
		static final int K1024= 1024,
						 K2048 = 2048,
						 K4096 = 4096;	
	}
	
	/** Supported message types */
	public enum MESSAGE_TYPE{
		CLAIM,
		ENDORSEMENT,
		CERTIFICATE
	}

}
