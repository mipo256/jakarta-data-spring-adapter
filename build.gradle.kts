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
    implementation(platform("org.springframework.boot:spring-boot-dependencies:3.1.6"))
    annotationProcessor("org.hibernate.orm:hibernate-jpamodelgen:$hibernateCoreVersion")

    implementation("org.springframework:spring-context")
    implementation("org.hibernate:hibernate-core:$hibernateCoreVersion")
    implementation("jakarta.data:jakarta-data-api:1.0.0-M1")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(files("test_libs/jakarta-data-test-repos-1.0.0.jar"))
}

tasks.test {
    useJUnitPlatform()
}