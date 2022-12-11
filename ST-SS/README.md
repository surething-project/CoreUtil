# SureThing Signature Specification (ST-SS)

## Abstract

This document presents the *SureThing Signature Specification* (ST-SS) that states how to use cryptographic algorithms to create and verify digital signatures of the location claims, endorsements and verifications data structures specified in the SureThing Core Data.

The procedures specified in this document MAY be implemented in different programming languages, as a library, that can then be used in applications making use of location proofs.

The procedures specified in this document MUST be implemented in the *SureThing Signature Specification Reference Implementation* (ST-SS-RI), that is authoritative in regard to implementation details, whenever there are omissions in this document.

### Requirements Language

The key words "MUST", "MUST NOT", "REQUIRED", "SHALL", "SHALL NOT", "SHOULD", "SHOULD NOT", "RECOMMENDED", "MAY", and "OPTIONAL" in this document are to be interpreted as described in [RFC 2119](https://datatracker.ietf.org/doc/html/rfc2119).

### Copyright Notice

Copyright (c) 2021 INESC-ID

---

## Introduction

SureThing

Roles: Prover, Witness, Verifier

Core Data

Core Util

## Terminology

Digital Signature -- For the purposes of this specification, we use this term for digital signatures using public key algorithms, which can provide both authenticity and  non-repudiation.

Public Key --

Certification Authority --

## Signature Overview and Examples

Prover makes a claim.
Claim is signed.

Witness receives a signed claim.
Verifies signature.
Decides to endorse the signed claim.
Endorsement is signed.

Verifier verifies a signed claim with signed endorsements.
A location certificate is produced.

An application receives a location certificate and verifies its signature.
If the signature is valid and the application trusts the Verifier, it can accept the claim as true.

The required private keys for making the signatures are known only to the respective entities.

The required public keys for verifying the signatures are either known in advance, or are certified by a trusted CA and be obtained and verified.
The public key of the trusted CAs have to be known in advance.

## Processing Rules

### Signature Generation

The cryptographic functions -- e.g. hash and encryption algorithms -- operate over byte arrays, and produce byte arrays.

For all of the following procedures, we rely on an ordered list of byte arrays, that we call the *ByteArrayList*.
At the beginning of each procedure, the ByteArrayList is inicialized and empty.
This list is important because it is allows the signatures to cover one or more message structures in the same signature.

*To produce a signature*, all of the items in the ByteArrayList are hashed in sequence, from the first on the list, until the last of the last.
The final hash is encrypted with the private key to produce the signature value.

*To verify a signature*, all of the items in the ByteArrayList are again hashed in sequence.

#### Claim Signature

To sign a claim:

The `proverId` field must be set.
The prover must have its private key.

Serialize the claim protobuf message.
Add the result to the tail of the ByteArrayList.

Create a `Signature` message.

Pick one signature algorithm from the supported list.
Set the `cryptoAlgo` identifier using the identifier.

Generate a random number and set the `nonce` field.

Set the `value` field to 0 (default value).
Serialize the signature message.
Add the result to the tail of the ByteArrayList.

Process ByteArrayList in order to produce the hash.

Encrypt the resulting hash with the asymmetric cipher.

Store the resulting byte array in the `value` field.

Create a `SignedLocationClaim` message.
Set the `claim` and the `proverSignature` fields.

This is the result.

#### Endorsement Signature

Resolve the `claimId`.

Serialize `SignedLocationClaim` and add to ByteArrayList.

Fill in the `witnessId`.

Serialize `LocationEndorsement` and add to ByteArrayList.

Create and fill-in a `Signature` message (same as claim).
Process the ByteArrayList.

Create a `SignedLocationEndorsement` message.
Set the `endorsement` and the `witnessSignature` fields.

This is the result.

#### Verification Signature

This procedure outputs a Location Certificate.

Resolve claim, serialize, add to ByteArrayList.

For each endorsement: serialize and add to ByteArrayList.

Create and fill-in `Signature`.

Create `LocationCertificate` message.
Set the `verification` and the `verifierSignature` fields.

This is the result.

### Signature Validation

#### Claim signature validation

Receive a `SignedLocationClaim`.

Resolve proverId and retrieve its public key.

Go to `Signature`, extract `value`.
Decrypt with public key.
This is the deciphered hash value.

Serialize claim, add to tail of ByteArrayList.

Zero the signature value. Add to tail of ByteArrayList.

Produce hash from ByteArrayList.

If computed hash is exactly the same (byte-to-byte comparison) with deciphered hash, then the signature is valid.

#### Endorsement signature validation

#### Certificate signature validation

## Core Signature Syntax

### The Signature element

### Metadata

### Identifier reference resolving

## Algorithms

### Identifiers and Implementation Requirements

### Message Digests

SHA-256, SHA-384, SHA-512

### Signature Algorithms

RSA (PKCS#1 v1.5), ECDSA

---

## References

XML Signature Syntax and Processing Version 1.1 - 2013 - <http://www.w3.org/TR/2013/REC-xmldsig-core1-20130411/>

JSON Web Signature (JWS) - 2015 - <https://datatracker.ietf.org/doc/html/rfc7515>
