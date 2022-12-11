package eu.surething_project.signature.util;

import eu.surething_project.core.Location;
import eu.surething_project.core.LocationCertificate;
import eu.surething_project.core.LocationClaim;
import eu.surething_project.core.LocationEndorsement;
import eu.surething_project.core.LocationVerification;
import eu.surething_project.core.Signature;
import eu.surething_project.core.SignedLocationClaim;
import eu.surething_project.core.SignedLocationEndorsement;
import eu.surething_project.core.Time;
import eu.surething_project.core.wi_fi.WiFiNetworksEvidence;
import static com.google.protobuf.util.Timestamps.fromMillis;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.type.LatLng;

/**
 * 
 * SureThing Framework Signature Util library Test suit - Multiple Signature Test
 * 
 * 
 * @author Samih
 *
 */
public class MultipleSignatureTest {
	
	
	static long timeInMillis = System.currentTimeMillis(); // current time in milliseconds
	
	/**
	 * 
	 * testMultipleSignatures
	 * 
	 * 1- Claim 
	 * 		- create a claim
	 * 		- sign
	 * 
	 * 2- Endorsement
	 * 		- received signed claim
	 * 		- verify prover's signature
	 * 		- create an endorsement
	 * 		- sign
	 * 
	 * 3- Certificate
	 *  	- receive claim/endorsement
	 *  	- verify sender
	 *  	- create certificate
	 *  	- sign
	 *  
	 *  4- Application -LBS
	 *		- verify verifier's signature
	 *
	 *
	 * @throws Exception 
	 *  
	 */
	
	public void testMultipleSignatures() throws Exception {
		
		// First : PROVER
		
		// create a claim
		LocationClaim locationClaim = createLocationClaim();
		
		// sign claim
		
		// serialize the claim
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
		
		
		// second: WITNESS
				
		// verify Prover's signature
		boolean isCorrect = SignatureManager.verify(signedLocationClaimSerialized, KeysManager.getPublicKey("Prover"), SignatureProperties.MESSAGE_TYPE.CLAIM);
		assertTrue(isCorrect);
		
		if(isCorrect) {
			// create an endorsement
			LocationEndorsement locationEndorsement = createEndorsement(locationClaim.getClaimId());
			
			// sign endorsement

			// serialize location endorsement
			byte [] locationEndorsementSerialized = locationEndorsement.toByteArray();

			// generate Witness's signature
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
			

			// Third: VERIFIER
			
			// verify Witness's signature
			isCorrect = SignatureManager.verify(signedLocationEndorsementSerialized, KeysManager.getPublicKey("Witness"), SignatureProperties.MESSAGE_TYPE.ENDORSEMENT);
			assertTrue(isCorrect);
			
			if(isCorrect) {
				
				// create certificate
				LocationCertificate locationCertificate = createLocationCertificate(locationClaim.getClaimId(), locationEndorsement.getWitnessId() /* should be endorsemetId */);
				
				
				// serialize location certificate
				byte [] locationCertificateSerialized =locationCertificate.toByteArray();
				
				
				// Fourth: APPLICATION (e.g. LBS (Location Based Service)
				
				// verify verifier's signature
				isCorrect = SignatureManager.verify(locationCertificateSerialized, KeysManager.getPublicKey("Verifier"), SignatureProperties.MESSAGE_TYPE.CERTIFICATE);
				assertTrue(isCorrect);
			}
		}
	}
	
	private static LocationClaim createLocationClaim() {
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
	
		return locationClaim;
	}
	
	
	private static LocationEndorsement createEndorsement(String claimId) {
		LocationEndorsement locationEndorsement = LocationEndorsement.newBuilder()
				.setWitnessId("1")
				.setClaimId (claimId)
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

		return locationEndorsement;
	}
	
	
	private static LocationCertificate createLocationCertificate(String claimId, String endorsementId) throws Exception {
		
		LocationVerification  locationVerification  = LocationVerification .newBuilder()
				.setVerifierId ("1")
				.setClaimId (claimId)
				.addEndorsementIds(endorsementId)
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
				
		// create signed location certificate
		LocationCertificate locationCertificate = LocationCertificate.newBuilder()
		                .setVerification(locationVerification)
		                .setVerifierSignature(Signature.newBuilder()
		                		.setValue(ByteString.copyFrom(verifierSignature))
		                		.setCryptoAlgo(SignatureProperties.CRYPTO_SIGNATURE_ALGO.SHA256withRSA)
		                		.setNonce(timeInMillis)
		                		.build())
		                .build();
		return locationCertificate;
	}

}


