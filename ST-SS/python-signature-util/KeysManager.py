import base64
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives import serialization, hashes
from cryptography.hazmat.primitives.asymmetric import rsa


"""SureThing Framework KeysManager Module

- Helper module to manage the retrieval/generation of private/public keys.

- The functionalities of this module should be imported by
  the prover/witness application which uses the signature library.
  
- Future Work: Include SMIME as an encoding format.
- NOTE: With cryptography module, only PEM, DER and SMIME format encoded keys 
        can be used to sign with RSA.
        Visit: https://cryptography.io/en/latest/hazmat/primitives/asymmetric/rsa/

- Author: Miguel Francisco (SurePresence).

"""

################# RSA KEYS GENERATOR #################

def generateRSAKeyPair():
    private_key = rsa.generate_private_key(public_exponent=65537, key_size=2048, backend=default_backend())
    return private_key, private_key.public_key()

def storeKeyPair(privateKey, publicKey, private_path, public_path, encoding):
    encoding_format = None
    if(encoding == 'PEM'):
        encoding_format = serialization.Encoding.PEM
    elif(encoding == 'DER'):
        encoding_format = serialization.Encoding.DER
    elif(encoding == 'SSH'):
        encoding_format = serialization.Encoding.OpenSSH
    else:
        print("Incorrect encoding: Must be PEM, DER, SSH or SMIME")
        return

    private_pem = privateKey.private_bytes(
        encoding=encoding_format,
        format=serialization.PrivateFormat.PKCS8,
        encryption_algorithm=serialization.NoEncryption()
    )
    with open(private_path, 'wb') as f:
        f.write(private_pem)

    public_pem = publicKey.public_bytes(
        encoding=encoding_format,
        format=serialization.PublicFormat.SubjectPublicKeyInfo)

    with open(public_path, 'wb') as f:
        f.write(public_pem)

################## PEM ENCODED KEYS ##################

def getPrivateKey_PEM(path, password):
    with open(path, "rb") as key_file:
        return serialization.load_pem_private_key(key_file.read(), password=password, backend=default_backend())

def getPublicKey_PEM(path):
    with open(path, "rb") as key_file:
        return serialization.load_pem_public_key(key_file.read(), backend=default_backend())


################## DER ENCODED KEYS ##################

def getPrivateKey_DER(path, password):
    with open(path, "rb") as key_file:
        return serialization.load_der_private_key(key_file.read(), password=password, backend=default_backend())

def getPublicKey_DER(path):
    with open(path, "rb") as key_file:
        return serialization.load_der_public_key(key_file.read(), backend=default_backend())

############## SSH ENCODED PRIVATE KEYS ##############

def getPrivateKey_SSH(path, password):
    with open(path, "rb") as key_file:
        return serialization.load_ssh_private_key(key_file.read(), password=password, backend=default_backend())

################# PKCS12 ENCODED KEYS ################

def getFile_PKCS12(path, password):
    with open(path, "rb") as key_file:
        return serialization.pkcs12.load_ley_and_certificates(key_file.read(), password=password, backend=default_backend())

def getPrivateKey_PKCS12(path, password):
    return getFile_PKCS12(path, password)[0]

def getCertificate_PKCS12(path, password):
    return getFile_PKCS12(path, password)[1]

