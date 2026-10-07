# Protected Value AES Factory

AES-256-GCM implementation of the protected value factory: encrypts values using symmetric keys looked up via `SymmetricKeyRepository`, with per-owner key management. Every value gets a random IV, and its owner is bound to the ciphertext as associated data, so a ciphertext moved to another owner fails to decrypt.
