// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ktlint) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
}

tasks.register("ktlintCheckAll") {
    group = "verification"
    description = "Run ktlint check on all modules"
    dependsOn(subprojects.map { "${it.path}:ktlintCheck" })
}

tasks.register("ktlintFormatAll") {
    group = "formatting"
    description = "Run ktlint format on all modules"
    dependsOn(subprojects.map { "${it.path}:ktlintFormat" })
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        version.set("1.3.1")
        android.set(true)
        ignoreFailures.set(false)

        reporters {
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
        }
    }
}
