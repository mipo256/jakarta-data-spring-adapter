plugins {
    id("java")
}

group = "io.mpolivaha"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

val hibernateCoreVersion = "6.6.4.Final"

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:3.1.6"))
    annotationProcessor("org.hibernate.orm:hibernate-jpamodelgen:$hibernateCoreVersion")

    implementation("org.springframework:spring-context")
    implementation("org.hibernate:hibernate-core:$hibernateCoreVersion")
    implementation("jakarta.data:jakarta-data-api:1.0.0-M1")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.test {
    useJUnitPlatform()
}