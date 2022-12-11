# SureThing Evidence Specification (ST-ES)
This is the required specification for implementating of the SureThing Evidence library. The supported evidence types (i.e. locality-sensitive and ambinet sensing) are described along with their verification techniques.

### Requirements Language

The key words "MUST", "MUST NOT", "REQUIRED", "SHALL", "SHALL NOT", "SHOULD", "SHOULD NOT", "RECOMMENDED", "MAY", and "OPTIONAL" in this document are to be interpreted as described in [RFC 2119](https://datatracker.ietf.org/doc/html/rfc2119).

### Copyright Notice

Copyright (c) 2022 INESC-ID

---

## Introduction

### Evidence Types
The types of location evidence used to prove the location of a device (or user).
* Locality-senstive
* Ambience sensing
  

| Evidence Type   |      Description      |  Requirements |
|-----------------|---------------------|--------------|
| Location based on GNSS such as GPS  |  When client has location described in latitude/longitude. Location is within certain geographic range. | Client shoud have a GPS receiver |
| Location based on IP address |    When client's location is estimated from IP address. Loaction is within certain geographic range.   |   Client should have an IP address which directly or routed to the internet |
| Wireless (Wi-Fi) | When client connects to access point based on the name of access point | Client should have access to wireless network |
| Bluetooth | When client detects Bluetooth or BLE beacon | Client should have bluetooth receiver |
| RFID| When client is in proximity to a device which can be detected by RFID|  Client should have RFID receiver |
| NFC| When client is in proximity to a device which can be detected by NFC | Client should have NFC receiver |


#### Locality-sensitive
locality-sensitive network measurements such as Wi-Fi and Bluettoth. It is important to note that we exclude **expensive** techniques, like GPS and image processing, because we are targeting limited devices; we are considering low cost, small sensors, not smartphones only.

##### Wi-Fi
The Wi-Fi location evidence may contain a list of detected access points (APs) at the location and their measured signals strengths. APs are represented by their Media Access Control (MAC) addresses and the associated received signal strength indicators (rssi). The following is an example of Wi-Fi evidence can be represented in the framework data types.
```protobuf
syntax = "proto3";

package eu.surething_project.evidence.wi_fi;

option java_multiple_files = true;
option java_package = "eu.surething_project.evidence.wi_fi";
option java_outer_classname = "WiFiProto";

// Wi-Fi networks evidence 
message WiFiNetworksEvidence {
    // unique id
    string id = 1;

    // Access point (AP) definition
    message AP{
        // AP identifier
        string ssid = 1;
        // measured RSSI 
        string rssi = 2;
    }

    // detected APs
    repeated AP aps = 2;
}
```
##### Bluetooh
The bluetooth evidence can be represented as follows:
```protobuf
syntax = "proto3";

package eu.surething_project.evidence.bluetooth;

option java_multiple_files = true;
option java_package = "eu.surething_project.evidence.bluetooth";
option java_outer_classname = "BluetoothProto";

// Bluetooth evidence 
message BluetoothEvidence {
    
    // unique id
    string id = 1;

    // Bluetooth beacon
    message BluetoothBeacon{
        // beacon identifier
        string beaconId = 1;
        // measured RSSI 
        string rssi = 2;
    }

    // detected beacons
    repeated BluetoothBeacon beacons = 2;

}
```

#### Ambience Sensing
Ambience Sensing (e.g., noise levels, audio, light, and humidity) can be used to define an ambience fingerprint for a location. The fingerprint can be used as evidence to prove the location of the user. This approach was developed for smartphones but can be adapted to limited and small devices as well.
##### Audio
The audio evidence can be represented as follows:
```protobuf
syntax = "proto3";

package eu.surething_project.evidence.audio;

option java_multiple_files = true;
option java_package = "eu.surething_project.evidence.audio";
option java_outer_classname = "AudioProto";

// Audio evidence 
message AudioEvidence {

    // unique id
    string id = 1;

    // Audio Source
    message AudioSource{
        // audio Source identifier
        string id = 1;
        // measured intensity (amplitude)
        string intensity = 2;
        // measured audio frequency (pitch)
        string frequency = 3;
        // measured audio overtones (background noise)
        string overtones = 4;
        // sampling rate
        string samplingRate = 5;
    }

    // detected audio sources - should we condiered several audio sources?
    repeated AudioSource audioSources = 2;
    
}
```
##### Light
The light evidence can be repesented as follow:
```protobuf
syntax = "proto3";

package eu.surething_project.evidence.light;

option java_multiple_files = true;
option java_package = "eu.surething_project.evidence.light";
option java_outer_classname = "LightProto";

// Light evidence 
message LightEvidence {
    
    // unique id
    string id = 1;

    // Audio Source
    message LightSource{
        // light Source identifier
        string id = 1;
        // measured intensity (amplitude)
        string intensity = 2;
        // measured audio frequency (pitch)
        string frequency = 3;
        // measured propagation direction
        string propagationDirection = 4;
        // sampling rate
        string samplingRate = 5;
    }

    // detected light sources - should we condiered several light sources?
    repeated LightSource lightSources = 2;
}
```
### Evidence Verification

#### Fingerprinting
##### Wi-Fi
The similarity between two Wi-Fi location evidence can be identified by looking at the common APs seen in the two evidence and if the signals strengths from those APs are similar as well, then the two evidence were more likely generated at the same location.

##### Bluetooth

### Challenge-response
### Distance-bounding
### Latency measurement


## Terminology


---

## References

