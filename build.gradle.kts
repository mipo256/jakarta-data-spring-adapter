plugins {
    id("java")
}

group = "com.mpolivaha.jdsa"
version = "1.0.0"

repositories {
    mavenCentral()
}

val hibernateCoreVersion = "6.6.4.Final"

dependencies {
    // APT and dependency management
    implementation(platform("org.springframework.boot:spring-boot-dependencies:3.1.6"))
    annotationProcessor("org.hibernate.orm:hibernate-jpamodelgen:$hibernateCoreVersion")

    // For development
    compileOnly("org.hibernate:hibernate-core:$hibernateCoreVersion")

    // Actual dependencies
    implementation("org.springframework:spring-context")
    implementation("jakarta.data:jakarta-data-api:1.0.0-M1")

    // Test dependencies
    testImplementation("org.hibernate:hibernate-core:$hibernateCoreVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(files("test_libs/jakarta-data-test-repos-1.0.0.jar"))
}

tasks.test {
    useJUnitPlatform()
}