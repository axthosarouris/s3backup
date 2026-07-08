package io.github.axthosarouris.s3backup.core

import com.github.awsjavakit.logutils.LogUtils
import com.github.awsjavakit.testingutils.RandomDataGenerator.randomString
import io.github.axthosarouris.localfilelisting.UnixPathUtils
import io.github.axthosarouris.s3backup.core.testutils.Content
import io.github.axthosarouris.s3backup.core.testutils.FakeCloudStorage
import io.github.axthosarouris.s3backup.core.testutils.FakeFileSystem
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldContainAllInAnyOrder
import io.kotest.matchers.string.shouldContain
import java.nio.file.Path

private val RESOURCES_PATH = Path.of("src", "test", "resources")

private fun randomPath(): Path = Path.of(randomString(), randomString())

class ApplicationTest :
    FunSpec({
      lateinit var fileSystem: FakeFileSystem
      lateinit var cloudStorage: FakeCloudStorage
      lateinit var content: TestContent
      beforeTest {
        fileSystem = FakeFileSystem()
        cloudStorage = FakeCloudStorage()
        content = TestContent()
      }

      context("Application expects user configuration") {
        test("should read UserConfiguration from provided location") {
          content.populateFileSystem(fileSystem)

          val defaultConfigLocation = RESOURCES_PATH.resolve("valid_config.toml")
          val logger = LogUtils.getTestingAppender(Application::class.java)
          val application =
              Application.create(
                  configLocation = defaultConfigLocation,
                  fileSystem,
                  cloudStorage,
              )
          application.runApp()
          logger.messages shouldContain "/a/b/c/d/e"
          logger.messages shouldContain "/some/other/folder"
        }
      }
      context("Application lists and uploads files to s3") {
        test(
            "should list recursively all files found under folders provided in User configuration",
        ) {
          content.populateFileSystem(fileSystem)

          val configuration = UserConfiguration(content.topFolders.map { it.toString() })
          val application = Application.create(configuration, fileSystem, cloudStorage)
          val logger = LogUtils.getTestingAppender(Application::class.java)
          application.runApp()
          val expectedFiles = content.fileContents.keys
          for (file in expectedFiles) {
            logger.messages shouldContain file.toString()
          }
        }
      }
      test("should upload files to cloud storage") {
        content.populateFileSystem(fileSystem)
        val userConfig = UserConfiguration(content.topFolders.map(Path::toString))
        val app = Application.create(userConfig, fileSystem, cloudStorage)
        app.runApp()
        val expectedFilesInCloudStorage = content.fileContents.keys.map(UnixPathUtils::toUnixPath)
        cloudStorage.listFiles() shouldContainAllInAnyOrder expectedFilesInCloudStorage
      }
    })

private class TestContent {
  val topFolders: List<Path>
  val nestedFolders: List<Path>
  val fileContents: Map<Path, Content>

  init {
    topFolders = listOf(randomPath(), randomPath())
    nestedFolders = topFolders.map { top -> top.resolve(randomString()) }
    val filesInTopFolders = topFolders.map { it.resolve(randomString()) }
    val filesInNestedFolders = nestedFolders.map { it.resolve(randomString()) }
    val files = filesInTopFolders + filesInNestedFolders
    fileContents = files.associateWith { randomString() }
  }

  fun populateFileSystem(fs: FakeFileSystem) {
    fileContents.entries.forEach { entry -> fs.createFile(entry.key, entry.value) }
  }
}
