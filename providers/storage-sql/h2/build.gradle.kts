dependencies {
    implementation(project(":spi"))
    implementation(project(":providers:storage-sql"))

    compileOnly("com.h2database:h2:2.2.224")
}