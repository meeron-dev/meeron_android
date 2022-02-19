buildscript {

    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://devrepo.kakao.com/nexus/content/groups/public/") }

    }
    dependencies {
        classpath(Dep.androidGradlePlugin)
        classpath(Dep.Dagger.hiltGradlePlugin)
        classpath(Dep.Kotlin.gradlePlugin)
        classpath(Dep.Kotlin.serializationPlugin)
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }

}// Top-level build file where you can add configuration options common to all sub-projects/modules.

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}