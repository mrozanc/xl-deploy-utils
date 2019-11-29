plugins {
    `java-platform`
    id("publish-config")
}

dependencies {
    constraints {
        api(project(":xl-deploy-client"))
    }
}
