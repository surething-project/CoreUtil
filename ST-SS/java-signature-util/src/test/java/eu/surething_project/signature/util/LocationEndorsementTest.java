package eu.surething_project.signature.util;

import eu.surething_project.core.LocationEndorsement;
import eu.surething_project.core.Signature;
import eu.surething_project.core.SignedLocationEndorsement;
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
 * SureThing Framework Signature Util library Test suit - Location Endorsement Test
 * 
 * 
 */
public class LocationEndorsementTest {
	
	/**
	 * 
	 * Location Endorsement Test
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLocationEndorsementSignature() throws Exception {
		long timeInMillis = System.currentTimeMillis(); // current time in milliseconds
		LocationEndorsement locationEndorsement = LocationEndorsement.newBuilder()
							.setWitnessId("1")
							.setClaimId ("1")
							.setTime(Time.newBuilder()
											.setTimestamp(fromMillis(timeInMillis))
											.build())
							.setEvidenceType("eu.surething_project.core.wi_fi.WiFiNetworksEvidence")
							.setEvidence(Any.pack(WiFiNetworksEvidence.newBuilder()
											.setId("DEF")
											.addAps(WiFiNetworksEvidence.AP.newBuilder()
												.setSsid("ssid-B")
												.setRssi("-90")			
												.build())
											.build()))
							.build();


		// serialize location endorsement
		byte [] locationEndorsementSerialized = locationEndorsement.toByteArray();

		// generate signature - witness private key should be used here
		byte[] witnessSignature = SignatureManager.sign(locationEndorsementSerialized, KeysManager.getPrivateKey());
        
		// verify signature - witness public key should be used here
		boolean isCorrect = SignatureManager.verify(witnessSignature, locationEndorsementSerialized, KeysManager.getPublicKey("Witness"));

		assertTrue(isCorrect);
	}
	
	/**
	 * 
	 * Tampered Location Endorsement Test
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTamperedLocationClaim() throws Exception {
		long timeInMillis = System.currentTimeMillis(); // current time in milliseconds
		LocationEndorsement locationEndorsement = LocationEndorsement.newBuilder()
							.setWitnessId("1")
							.setClaimId ("1")
							.setTime(Time.newBuilder()
											.setTimestamp(fromMillis(timeInMillis))
											.build())
							.setEvidenceType("eu.surething_project.core.wi_fi.WiFiNetworksEvidence")
							.setEvidence(Any.pack(WiFiNetworksEvidence.newBuilder()
											.setId("DEF")
											.addAps(WiFiNetworksEvidence.AP.newBuilder()
												.setSsid("ssid-B")
												.setRssi("-90")			
												.build())
											.build()))
							.build();

		// serialize location endorsement
		byte [] locationEndorsementSerialized = locationEndorsement.toByteArray();

		// generate signature - witness private key should be used here
		byte[] witnessSignature = SignatureManager.sign(locationEndorsementSerialized, KeysManager.getPrivateKey());
        
		// tamper Location Endorsement
		locationEndorsementSerialized[4] = 100;
     	
		
		// verify signature - witness public key should be used here
		boolean isCorrect = SignatureManager.verify(witnessSignature, locationEndorsementSerialized, KeysManager.getPublicKey("Witness"));

		assertFalse(isCorrect);

	}
	
	
	/**
	 * 
	 * Signed Location Endorsement Test
	 * 
	 * 
	 * @throws Exception
	 */
	
	@Test
	public void testSignedLocationClaim() throws Exception {
		long timeInMillis = System.currentTimeMillis(); // current time in milliseconds
		LocationEndorsement locationEndorsement = LocationEndorsement.newBuilder()
							.setWitnessId("1")
							.setClaimId ("1")
							.setTime(Time.newBuilder()
											.setTimestamp(fromMillis(timeInMillis))
											.build())
							.setEvidenceType("eu.surething_project.core.wi_fi.WiFiNetworksEvidence")
							.setEvidence(Any.pack(WiFiNetworksEvidence.newBuilder()
											.setId("DEF")
											.addAps(WiFiNetworksEvidence.AP.newBuilder()
												.setSsid("ssid-B")
												.setRssi("-90")			
												.build())
											.build()))
							.build();

		// serialize location endorsement
		byte [] locationEndorsementSerialized = locationEndorsement.toByteArray();

		// generate signature - witness private key should be used here
		byte[] witnessSignature = SignatureManager.sign(locationEndorsementSerialized, KeysManager.getPrivateKey());
		
		
		// create signed location endorsement
		SignedLocationEndorsement signedLocationEndorsement = SignedLocationEndorsement.newBuilder()
                .setEndorsement(locationEndorsement)
                .setWitnessSignature(Signature.newBuilder()
                		.setValue(ByteString.copyFrom(witnessSignature))
                		.setCryptoAlgo(SignatureProperties.CRYPTO_SIGNATURE_ALGO.SHA256withRSA)
                		.setNonce(timeInMillis)
                		.build())
                .build();
		
		// serialize signed location endorsement
		byte [] signedLocationEndorsementSerialized =signedLocationEndorsement.toByteArray();
		
		// verify signature - witness public key should be used here 
		boolean isCorrect = SignatureManager.verify(signedLocationEndorsementSerialized, KeysManager.getPublicKey("Witness"), SignatureProperties.MESSAGE_TYPE.ENDORSEMENT);
		
		assertTrue(isCorrect);
	}

}
