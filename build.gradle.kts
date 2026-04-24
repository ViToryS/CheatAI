// Top-level build file where you can add configuration options common to all sub-projects/modules.
import java.util.Properties
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.devtoolsKsp)
}
val mapkitApiKey: String by lazy {
    val properties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        properties.load(localPropertiesFile.inputStream())
        properties.getProperty("MAPKIT_API_KEY") ?: ""
    } else {
        ""
    }
}
extra["mapkitApiKey"] = mapkitApiKey

