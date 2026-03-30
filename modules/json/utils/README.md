# JSON Utils

JSON serialization/deserialization framework with `JsonSerde`, `JsonSerializer`, and `JsonDeserializer` interfaces, JSON schema validation support, and compliance checking rules (mandatory additionalProperties, disallow const keyword, whitelisted field name alphabets).

The `getRequired*` extension functions in `JsonExtensions.kt` use `!!` and unchecked casts. This is by design — they are intended for use after the payload has been validated (e.g., JWT verification, schema validation). They assume the fields exist and have the correct types.
