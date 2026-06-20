package io.github.axthosarouris.s3backup.core

import com.github.awsjavakit.logutils.LogUtils
import com.github.awsjavakit.misc.paths.UnixPath
import com.github.awsjavakit.testingutils.RandomDataGenerator.randomString
import io.github.axthosarouris.localfilelisting.FileSystem
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.string.shouldContain
import io.mockk.every
import io.mockk.mockk
import java.nio.file.Path

private val RESOURCES_PATH = Path.of("src", "test", "resources")
private val MOCK_FS = mockFileSystem()

private fun mockFileSystem(): FileSystem {
  val fs = mockk<FileSystem>()
  every { fs.listRecursively(any<Path>()) } returns randomUnixPathListing()
  return fs
}

private fun randomPath(): Path = Path.of(randomString(), randomString())

private fun randomUnixPathListing(): List<UnixPath> =
    listOf(randomPath(), randomPath(), randomPath()).map(Path::toString).map(UnixPath::fromString)

class ApplicationTest :
    FunSpec({
      context("Application expects user configuration") {
        test("should read UserConfiguration from provided location") {
          val defaultConfigLocation = RESOURCES_PATH.resolve("valid_config.toml")
          val logger = LogUtils.getTestingAppender(Application::class.java)
          val application = Application.create(configLocation = defaultConfigLocation, MOCK_FS)
          application.runApp()
          logger.messages shouldContain "/a/b/c/d/e"
          logger.messages shouldContain "/some/other/folder"
        }

        test(
            "should list recursively all files found under folders provided in User configuration",
        ) {
          val fileSystem = mockk<FileSystem>()
          val foldersInConfiguration = listOf(randomPath(), randomPath())
          val contents = foldersInConfiguration.associateWith { randomUnixPathListing() }

          for (folder in foldersInConfiguration) {
            every { fileSystem.listRecursively(folder) }.returns(contents[folder].orEmpty())
          }

          val configuration = UserConfiguration(foldersInConfiguration.map(Path::toString))
          val application = Application.create(configuration, fileSystem)
          val logger = LogUtils.getTestingAppender(Application::class.java)
          application.runApp()
          val expectedFiles = contents.values.flatten()
          for (file in expectedFiles) {
            logger.messages shouldContain file.toString()
          }
        }
      }
    })
