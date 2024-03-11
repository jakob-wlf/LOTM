import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/central")
    }
    maven {
        url = uri("https://maven.citizensnpcs.co/repo")
    }
    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
    maven {
        url = uri("https://jitpack.io")
    }
    maven {
        url = uri("https://repo.alessiodp.com/releases")
    }
}

dependencies {
    implementation("fr.mrmicky:fastboard:2.1.0")
    implementation("com.github.ForestTechMC:ForestColorAPI:1.4")
    compileOnly("org.spigotmc:spigot:1.20.4-R0.1-SNAPSHOT:remapped-mojang")
    compileOnly("net.citizensnpcs:citizens-main:2.0.33-SNAPSHOT")
    compileOnly("net.byteflux:libby-bukkit:1.1.5")
}

group = "dev.ua.ikeepcalm"
version = "1.0-SNAPSHOT"
description = "LordOfTheMinecraft"
java.sourceCompatibility = JavaVersion.VERSION_17

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<ShadowJar> {
    archiveFileName.set("LordOfTheMinecraft-SNAPSHOT.jar")
    relocate("fr.mrmicky.fastboard", "dev.ua.ikeepcalm.lordoftheminecraft.fastboard")
    relocate("net.byteflux.libby", "dev.ua.ikeepcalm.lordoftheminecraft.libby")
}
tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-8"
}