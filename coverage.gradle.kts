
tasks.withType<Test> {
    configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

val fileFilter = mutableSetOf(
    // Don't calcualte coverage on generated JsonAdapter files
    "**/*JsonAdapter.*",
    // Don't calculate coverage on the debugmenu
    "**/debugmenu/**"
)

val debugTree = fileTree("${project.layout.buildDirectory.get()}/tmp/kotlin-classes/debug") {
    exclude(fileFilter)
}

val jacocoExecutionData = fileTree(project.layout.buildDirectory.get()) {
    include(
        listOf(
            "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec",
            "outputs/code_coverage/debugAndroidTest/connected/**/*.ec"
        )
    )
}

val mainSrc = "${project.projectDir}/src/main/java"

tasks.register<JacocoReport>("jacocoTestReport") {

    dependsOn("testDebugUnitTest", "createDebugUnitTestCoverageReport")
    group = "reporting"
    description = "Generate Jacoco coverage reports for the debug build."

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(debugTree))
    executionData.setFrom(jacocoExecutionData)
}
