plugins {
    `java-platform`
    id("publish-config")
}

javaPlatform {
    allowDependencies()
}

dependencies {
    constraints {
        val assertkVersion: String by project
        val fuelVersion: String by project
        val spekVersion: String by project
        api("com.github.kittinunf.fuel:fuel:$fuelVersion")
        api("com.github.kittinunf.fuel:fuel-jackson:$fuelVersion")
        api("com.willowtreeapps.assertk:assertk-jvm:$assertkVersion")
        api("org.spekframework.spek2:spek-dsl-jvm:$spekVersion")
        api("org.spekframework.spek2:spek-runner-junit5:$spekVersion")
    }

    val jacksonBomVersion: String by project
    val junitJupiterVersion: String by project
    val kotlinVersion: String by project
    api(enforcedPlatform(project(":xl-deploy-utils-platform")))
    api(enforcedPlatform(kotlin("bom", kotlinVersion)))
    api(enforcedPlatform("com.fasterxml.jackson:jackson-bom:$jacksonBomVersion"))
    api(enforcedPlatform("org.junit:junit-bom:$junitJupiterVersion"))
}
