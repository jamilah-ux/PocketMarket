buildscript {
    val agp_version by extra("8.6.0")
}
plugins {
    id("com.android.application") version "9.4.1" apply false
    id("org.jetbrains.kotlin.android") version "2.2.10" apply false
}