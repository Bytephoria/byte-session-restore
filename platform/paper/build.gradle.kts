plugins {
    id("com.gradleup.shadow") version ("9.1.0")
    id("de.eldoria.plugin-yml.paper") version ("0.8.0")
}

repositories {
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.20.2-R0.1-SNAPSHOT")

    // Internal project modules
    implementation(project(":api"))
    implementation(project(":spi"))
    implementation(project(":core"))
    implementation(project(":platform:common-bukkit"))
    implementation(project(":infra:config"))
    implementation(project(":infra:bootstrap"))
    implementation(project(":providers:serializer"))
    implementation(project(":providers:compression"))
    implementation(project(":providers:compression:none"))
    implementation(project(":providers:compression:zstd"))
    implementation(project(":providers:storage-sql"))
    implementation(project(":providers:storage-sql:h2"))

    // Embedded libraries (bundled via Shadow)
    implementation(files("libraries/common-2.0.0.jar"))
    implementation(files("libraries/item-2.0.0.jar"))
    implementation(files("libraries/layout-2.0.0.jar"))
    implementation("org.bstats:bstats-bukkit:3.0.2")

    // Runtime libraries included in plugin
    paperLibrary("com.github.luben:zstd-jni:1.5.7-5")
    paperLibrary("com.zaxxer:HikariCP:7.0.2")
    paperLibrary("com.h2database:h2:2.4.240")
    paperLibrary("com.github.ben-manes.caffeine:caffeine:3.2.2")

}

paper {
    name = getProjectName(rootProject.name)
    main = "team.bytephoria.bytesessionrestore.platform.paper.PaperPlugin"
    description = rootProject.description
    loader = "team.bytephoria.bytesessionrestore.platform.paper.PaperPluginLoader"
    version = rootProject.version.toString()
    generateLibrariesJson = true
    author = "Bytephoria"
    website = "https://bytephoria.team"
    apiVersion = "1.20"
}

tasks {

    generatePaperPluginDescription {
        useGoogleMavenCentralProxy()
    }

    shadowJar {
        archiveBaseName.set(paper.name)
        archiveVersion.set(rootProject.version.toString())
        archiveClassifier.set(project.name)
    }

}

/**
 * Converts a hyphen-separated project name into PascalCase.
 *
 * Example:
 *  "byte-session-restore" -> "ByteSessionRestore"
 */
fun getProjectName(baseName: String): String {
    return baseName.split("-")
        .joinToString("") {
                part -> part.replaceFirstChar {
            it.uppercase()
        }}
}