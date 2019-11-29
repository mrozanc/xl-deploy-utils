plugins {
    id("kotlin-config")
}

dependencies {
    implementation(enforcedPlatform(project(":xl-deploy-utils-dependencies")))
    implementation("com.github.kittinunf.fuel:fuel-jackson")
    implementation("com.github.kittinunf.fuel:fuel")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib-jdk8"))

    testImplementation("com.willowtreeapps.assertk:assertk-jvm")
    testImplementation("org.spekframework.spek2:spek-dsl-jvm")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5")
}
