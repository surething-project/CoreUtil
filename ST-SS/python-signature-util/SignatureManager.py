import base64

from cryptography.hazmat.primitives.asymmetric import padding
from cryptography.hazmat.primitives import serialization, hashes
from cryptography.exceptions import InvalidSignature

"""SureThing Framework SignatureManager Module

- Helper module to manage signatures.

- The functionalities of this module should be imported by
  the prover/witness application which uses the signature library.
  
- Future work: allow the usage of different hashing/signature algorithms as arguments

- Author: Miguel Francisco (SurePresence).

"""

"""
    Sign a message with private key
"""


def sign(plaintext, privateKey):  # SHA256WithRSA
    # Check hashes. for other possible hashing algos
    # Padding must be the same on the verification side
    # Check padding. for other paddings

    sig = privateKey.sign(plaintext,
                          padding.PSS(
                              mgf=padding.MGF1(hashes.SHA256()),
                              salt_length=padding.PSS.MAX_LENGTH
                          ),
                          hashes.SHA256())

    return sig


"""
    Verify that the received signature is generated from the received message.
"""


def verify(signature, plaintext, public_key):
    # Check hashes. for other possible hashing algos
    # Padding must be the same on the signing side
    # Check padding. for other paddings
    # InvalidTag exception means there was an issue
    try:
        public_key.verify(signature,
                          plaintext,
                          padding.PSS(
                              mgf=padding.MGF1(hashes.SHA256()),
                              salt_length=padding.PSS.MAX_LENGTH
                          ),
                          hashes.SHA256())
        return True
    except InvalidSignature:
        return False
