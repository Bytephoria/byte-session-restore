repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")

    api(project(":api"))
    api(project(":spi"))
    api(project(":core"))

    api(project(":providers:serializer"))

}