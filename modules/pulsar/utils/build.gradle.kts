plugins {
    id("sollecitom.kotlin-library-conventions")
    id("sollecitom.maven-publish-conventions")
}

dependencies {
    api(libs.pulsar.client.admin) {
        exclude(group = "com.google.protobuf", module = "protobuf-java")
        // `bouncy-castle-bc` is an executable-packer jar that nests unpatched bcprov/bcpkix/bcutil jars under
        // `lib/`, so a version pin cannot reach them and every Bouncy Castle CVE stays reported against the image.
        // Nothing on the classpath references its `org.apache.pulsar.bcloader.BouncyCastleLoader` entry point, and
        // pulsar-client-admin already depends on the plain Bouncy Castle artifacts, which the version pins do reach.
        exclude(group = "org.apache.pulsar", module = "bouncy-castle-bc")
    }
    api(libs.apache.avro.core)
    api(projects.coreDomain)
    api(projects.messagingDomain)
    api(projects.readinessDomain)
    api(projects.configurationUtils)
    api(projects.jsonUtils)

    implementation(projects.loggerCore)

    testImplementation(projects.coreTestUtils)
}