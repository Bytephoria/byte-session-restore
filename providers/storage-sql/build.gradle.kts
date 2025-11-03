allprojects {
    dependencies {
        compileOnly("com.zaxxer:HikariCP:5.1.0")
    }
}

dependencies {
    compileOnly(project(":api"))
    compileOnly(project(":spi"))
}