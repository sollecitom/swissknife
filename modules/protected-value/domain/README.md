# Protected Value Domain

Encrypted value protection framework: `ProtectedValue` (with an accessible variant for decryption), `ProtectedValueFactory` for encrypting arbitrary values, and typed factory wrappers supporting serialization-aware protection.

Decryption goes through a required `ProtectedValue.AccessHook`, called before every access with the access context and the value (name, owner, metadata). The hook records who accessed what and when, and refuses access by throwing. `ProtectedValue.loggingAccessHook()` logs every access and allows it.
