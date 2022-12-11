package eu.surething_project.signature.util;

import eu.surething_project.core.LocationCertificate;
import eu.surething_project.core.LocationVerification;
import eu.surething_project.core.Signature;
import eu.surething_project.core.Time;
import eu.surething_project.core.wi_fi.WiFiNetworksEvidence;
import static com.google.protobuf.util.Timestamps.fromMillis;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import com.google.protobuf.Any;
import com.google.protobuf.ByteString;

/**
 * 
 * 
 * SureThing Framework Signature Util library Test suit - Location Verification/Certificate Test
 * 
 * 
 */
public class LocationCertificateTest {
	
	/**
	 * 
	 * Location Verification/Certificate Test
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLocationVerificateSignature() throws Exception {
		long timeInMillis = System.currentTimeMillis(); // current time in milliseconds
		LocationVerification  locationVerification  = LocationVerification .newBuilder()
				.setVerifierId ("1")
				.setClaimId ("1")
				.addEndorsementIds("1")
				.setTime(Time.newBuilder()
								.setTimestamp(fromMillis(timeInMillis))
								.build())
				.setEvidenceType("eu.surething_project.core.wi_fi.WiFiNetworksEvidence")
				.setEvidence(Any.pack(WiFiNetworksEvidence.newBuilder()
								.setId("GHI")
								.addAps(WiFiNetworksEvidence.AP.newBuilder()
									.setSsid("ssid-C")
									.setRssi("-70")			
									.build())
								.build()))
				.build();

		// serialize location certificate
		byte [] locationVerificationSerialized = locationVerification.toByteArray();

		// generate signature - verifier private key should be used here
		byte[] verifierSignature = SignatureManager.sign(locationVerificationSerialized, KeysManager.getPrivateKey());
        
		// verify signature - verifier public key should be used here
		boolean isCorrect = SignatureManager.verify(verifierSignature, locationVerificationSerialized, KeysManager.getPublicKey("Verifier"));

		assertTrue(isCorrect);
	}
	
	/**
	 * 
	 * Tampered Location Certificate Test
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTamperedLocationVerification() throws Exception {
		long timeInMillis = System.currentTimeMillis(); // current time in milliseconds
		LocationVerification  locationVerification  = LocationVerification .newBuilder()
				.setVerifierId ("1")
				.setClaimId ("1")
				.addEndorsementIds("1")
				.setTime(Time.newBuilder()
								.setTimestamp(fromMillis(timeInMillis))
								.build())
				.setEvidenceType("eu.surething_project.core.wi_fi.WiFiNetworksEvidence")
				.setEvidence(Any.pack(WiFiNetworksEvidence.newBuilder()
								.setId("GHI")
								.addAps(WiFiNetworksEvidence.AP.newBuilder()
									.setSsid("ssid-C")
									.setRssi("-70")			
									.build())
								.build()))
				.build();

		// serialize location certificate
		byte [] locationVerificationSerialized = locationVerification.toByteArray();

		// generate signature - verifier private key should be used here
		byte[] verifierSignature = SignatureManager.sign(locationVerificationSerialized, KeysManager.getPrivateKey());
        
		// tamper Location Endorsement
		locationVerificationSerialized[4] = 100;
     	
		
		// verify signature - verifier public key should be used here
		boolean isCorrect = SignatureManager.verify(verifierSignature, locationVerificationSerialized, KeysManager.getPublicKey("Verifier"));

		assertFalse(isCorrect);

	}
	
	
	/**
	 * 
	 * Location Certificate Test
	 * 
	 * 
	 * @throws Exception
	 */
	
	@Test
	public void testLocationCertificate() throws Exception {
		long timeInMillis = System.currentTimeMillis(); // current time in milliseconds
		LocationVerification  locationVerification  = LocationVerification .newBuilder()
				.setVerifierId ("1")
				.setClaimId ("1")
				.addEndorsementIds("1")
				.setTime(Time.newBuilder()
								.setTimestamp(fromMillis(timeInMillis))
								.build())
				.setEvidenceType("eu.surething_project.core.wi_fi.WiFiNetworksEvidence")
				.setEvidence(Any.pack(WiFiNetworksEvidence.newBuilder()
								.setId("GHI")
								.addAps(WiFiNetworksEvidence.AP.newBuilder()
									.setSsid("ssid-C")
									.setRssi("-70")			
									.build())
								.build()))
				.build();

		// serialize location certificate
		byte [] locationVerificationSerialized = locationVerification.toByteArray();

		// generate signature - verifier private key should be used here
		byte[] verifierSignature = SignatureManager.sign(locationVerificationSerialized, KeysManager.getPrivateKey());
		
		
		// create signed location endorsement
		LocationCertificate locationCertificate = LocationCertificate.newBuilder()
                .setVerification(locationVerification)
                .setVerifierSignature(Signature.newBuilder()
                		.setValue(ByteString.copyFrom(verifierSignature))
                		.setCryptoAlgo(SignatureProperties.CRYPTO_SIGNATURE_ALGO.SHA256withRSA)
                		.setNonce(timeInMillis)
                		.build())
                .build();
		
		// serialize signed location certificate
		byte [] locationCertificateSerialized =locationCertificate.toByteArray();
		
		// verify signature - verifier public key should be used here 
		boolean isCorrect = SignatureManager.verify(locationCertificateSerialized, KeysManager.getPublicKey("Verifier"), SignatureProperties.MESSAGE_TYPE.CERTIFICATE);
		
		assertTrue(isCorrect);
	}

}
