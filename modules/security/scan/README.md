# Security Scan

Docker image vulnerability scanning using Trivy (via Docker). Provides `assertDockerImageHasNoVulnerabilities()` for use in security scan tests.

Requires Docker to be running. Trivy runs as a container — no host installation needed. Supports CVE suppression via `.trivyignore` content and configurable severity thresholds.
