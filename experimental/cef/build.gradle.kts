import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import de.undercouch.gradle.tasks.download.Download
import kotlin.text.capitalize

plugins {
    kotlin("jvm") version "1.6.10"
    id("org.jetbrains.compose") version "1.1.1"
    id("de.undercouch.download") version "4.1.1"
    application
}

val libraryBasePath = "$rootDir/third_party/java-cef/bin"
val libraryPath = "$libraryBasePath/jcef_app.app/Contents/Java"
val cefDownloadTar = run {
    val tarFile = File("third_party/macosx-amd64.tar.gz")

    tasks.register("downloadCef", Download::class) {
        onlyIf { !tarFile.exists() }
        src("https://github.com/jcefmaven/jcefbuild/releases/download/1.0.25/macosx-amd64.tar.gz")
        dest(tarFile)
        onlyIfModified(true)
    }.map { tarFile }
}

val cefUnTar = run {
    val targetDir = File("third_party/java-cef").apply { mkdirs() }
    tasks.register("untarCef", Copy::class) {
        from(cefDownloadTar.map { tarTree(it) })
        into(targetDir)
    }.map { targetDir }
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    // temp
    maven("https://packages.jetbrains.team/maven/p/ui/dev")
    maven("https://packages.jetbrains.team/maven/p/skija/maven")
}

dependencies {
    implementation("org.jetbrains.jcef:jcef-skiko:0.1")
    implementation("org.jetbrains.skija:skija-macos-x64:0.93.6")
    implementation(compose.desktop.currentOs)
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    dependsOn(cefUnTar)
}

application {
    applicationDefaultJvmArgs = listOf("-Djava.library.path=$libraryPath")
    mainClassName = "org.jetbrains.compose.desktop.AppKt"
}