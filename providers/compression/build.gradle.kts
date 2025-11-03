dependencies {
    api(project(":spi"))
    implementation(project(":core"))

    compileOnly(project(":providers:compression:none"))
    compileOnly(project(":providers:compression:zstd"))
}
