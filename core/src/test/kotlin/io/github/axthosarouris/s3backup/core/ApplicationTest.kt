package io.github.axthosarouris.s3backup.core

import com.github.awsjavakit.logutils.LogUtils
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import java.nio.file.Path


private val RESOURCES_PATH = Path.of("src", "test", "resources")

class ApplicationTest : FunSpec({
context("Application expects user configuration") {
    test("should read UserConfiguration from default location"){
        val defaultConfigLocation = RESOURCES_PATH.resolve("valid_config.json")
        val logger = LogUtils.getTestingAppender(Application::class.java)
        val application= Application(configLocation= defaultConfigLocation)
        logger.messages shouldContain "/a/b/c/d/e"

    }
}
})


