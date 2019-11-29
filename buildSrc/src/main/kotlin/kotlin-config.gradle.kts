import nebula.plugin.publishing.publications.SourceJarPlugin
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("nebula.dependency-lock")
    id("org.jetbrains.dokka")
    id("nebula.maven-publish")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test> {
    useJUnitPlatform {
        includeEngines("spek2")
    }
}

apply<SourceJarPlugin>()

val dokkaJavadoc = tasks.register<DokkaTask>("dokkaJavadoc") {
    outputFormat = "javadoc"
    outputDirectory = "$buildDir/javadoc"
}

val javadocJar = tasks.register<Jar>("javadocJar") {
    dependsOn(dokkaJavadoc)
    archiveClassifier.set("javadoc")
    from(dokkaJavadoc.map { it.outputDirectory })
}

lateinit var javadocArchive: PublishArtifact
artifacts {
    javadocArchive = archives(javadocJar)
}

publishing {
    publications {
        named<MavenPublication>("nebula") {
            artifact(javadocArchive)
        }
    }
}
