package eu.surething_project.signature.util;

import eu.surething_project.core.Location;
import eu.surething_project.core.LocationClaim;
import eu.surething_project.core.Signature;
import eu.surething_project.core.SignedLocationClaim;
import eu.surething_project.core.Time;
import eu.surething_project.core.wi_fi.WiFiNetworksEvidence;
import static com.google.protobuf.util.Timestamps.fromMillis;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.type.LatLng;

/**
 * 
 * 
 * SureThing Framework Signature Util library Test suit - Location Claim Test
 * 
 * 
 */
public class LocationClaimTest {
	
	/**
	 * 
	 * Location Claim Test
	 * 
	 * 
	 * @throws Exception
	 */
	@Test
	public void testLocationClaimSignature() throws Exception {
		long timeInMillis = System.currentTimeMillis(); // current time in milliseconds
		LocationClaim locationClaim = LocationClaim.newBuilder()
							.setClaimId("1")
							.setProverId("1")
							.setLocation(Location.newBuilder()
											.setLatLng(LatLng.newBuilder()
												.setLatitude(53.3)
												.setLongitude(85.3)
												.build())
											.build())
							.setTime(Time.newBuilder()
											.setTimestamp(fromMillis(timeInMillis))
											.build())
							.setEvidenceType("eu.surething_project.core.wi_fi.WiFiNetworksEvidence")
							.setEvidence(Any.pack(WiFiNetworksEvidence.newBuilder()
											.setId("ABC")
											.addAps(WiFiNetworksEvidence.AP.newBuilder()
												.setSsid("ssid-A")
												.setRssi("-89")			
												.build())
											.build()))
							.build();

		// serialize location claim
		byte [] locationClaimSerialized = locationClaim.toByteArray();
		
		// generate signature
		byte[] proverSignature = SignatureManager.sign(locationClaimSerialized, KeysManager.getPrivateKey());
        
		// verify signature
		boolean isCorrect = SignatureManager.verify(proverSignature, locationClaimSerialized, KeysManager.getPublicKey("Prover"));

		assertTrue(isCorrect);
	}
	
	/**
	 * 
	 * Tampered Location Claim Test
	 * 
	 * @throws Exception
	 */
	@Test
	public void testTamperedLocationClaim() throws Exception {
    	long timeInMillis = System.currentTimeMillis(); // current time in milliseconds
		LocationClaim locationClaim = LocationClaim.newBuilder()
							.setClaimId("1")
							.setProverId("1")
							.setLocation(Location.newBuilder()
											.setLatLng(LatLng.newBuilder()
												.setLatitude(53.3)
												.setLongitude(85.3)
												.build())
											.build())
							.setTime(Time.newBuilder()
											.setTimestamp(fromMillis(timeInMillis))
											.build())
							.setEvidenceType("eu.surething_project.core.wi_fi.WiFiNetworksEvidence")
							.setEvidence(Any.pack(WiFiNetworksEvidence.newBuilder()
											.setId("ABC")
											.addAps(WiFiNetworksEvidence.AP.newBuilder()
												.setSsid("ssid-A")
												.setRssi("-89")			
												.build())
											.build()))
							.build();

		// serialize location claim
		byte [] locationClaimSerialized = locationClaim.toByteArray();
		
		// generate signature
		byte[] proverSignature = SignatureManager.sign(locationClaimSerialized, KeysManager.getPrivateKey());
        
		// tamper Location claim
		locationClaimSerialized[4] = 100;
     	
		// verify signature
		boolean isCorrect = SignatureManager.verify(proverSignature, locationClaimSerialized, KeysManager.getPublicKey("Prover"));
		
		assertFalse(isCorrect);
	}
	
	
	
	/**
	 * 
	 * Signed Location Claim Test
	 * 
	 * 
	 * @throws Exception
	 */
	
	@Test
	public void testSignedLocationClaim() throws Exception {
		long timeInMillis = System.currentTimeMillis(); // current time in milliseconds
		LocationClaim locationClaim = LocationClaim.newBuilder()
							.setClaimId("1")
							.setProverId("1")
							.setLocation(Location.newBuilder()
											.setLatLng(LatLng.newBuilder()
												.setLatitude(53.3)
												.setLongitude(85.3)
												.build())
											.build())
							.setTime(Time.newBuilder()
											.setTimestamp(fromMillis(timeInMillis))
											.build())
							.setEvidenceType("eu.surething_project.core.wi_fi.WiFiNetworksEvidence")
							.setEvidence(Any.pack(WiFiNetworksEvidence.newBuilder()
											.setId("ABC")
											.addAps(WiFiNetworksEvidence.AP.newBuilder()
												.setSsid("ssid-A")
												.setRssi("-89")			
												.build())
											.build()))
							.build();

		// serialize location claim
		byte [] locationClaimSerialized = locationClaim.toByteArray();
		
		// generate signature
		byte[] proverSignature = SignatureManager.sign(locationClaimSerialized, KeysManager.getPrivateKey());
		
		// create signed location Claim
		SignedLocationClaim signedLocationClaim = SignedLocationClaim.newBuilder()
                .setClaim(locationClaim)
                .setProverSignature(Signature.newBuilder()
                		.setValue(ByteString.copyFrom(proverSignature))
                		.setCryptoAlgo(SignatureProperties.CRYPTO_SIGNATURE_ALGO.SHA256withRSA)
                		.setNonce(timeInMillis)
                		.build())
                .build();
		
		// serialize signed location claim
		byte [] signedLocationClaimSerialized =signedLocationClaim.toByteArray(); 
		
		// verify 
		boolean isCorrect = SignatureManager.verify(signedLocationClaimSerialized, KeysManager.getPublicKey("Prover"), SignatureProperties.MESSAGE_TYPE.CLAIM);
		
		assertTrue(isCorrect);
	}

}
