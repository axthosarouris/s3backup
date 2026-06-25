package io.github.axthosarouris.s3backup.core

import com.github.awsjavakit.testingutils.RandomDataGenerator.randomString
import io.github.axthosarouris.localfilelisting.FileSystem
import io.github.axthosarouris.localfilelisting.UnixPathUtils
import io.kotest.matchers.collections.shouldContainAllInAnyOrder
import java.io.BufferedWriter
import java.nio.file.Files
import java.nio.file.Path
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

class ApplicationIntegrationTest {
  private var fs: FileSystem = FileSystem.local()

  @BeforeEach
  fun setup() {
    fs = FileSystem.local()
  }

  @Test
  @Tag("slow")
  fun shouldListFilesFromLocalFileSystem() {
    val tempFolder: Path = Files.createTempDirectory("parentFolder")
    val nestedFolder =
        tempFolder.resolve("nestedFolder").also { path -> Files.createDirectories(path) }

    val fileInParent = tempFolder.resolve("fileInParent")
    val fileInNested = nestedFolder.resolve("fileInNested")

    addSomeContent(fileInParent)
    addSomeContent(fileInNested)
    val listResult = fs.listRecursively(tempFolder)
    val expectedOutput = listOf(fileInParent, fileInNested).map({ UnixPathUtils.toUnixPath(it) })
    listResult shouldContainAllInAnyOrder expectedOutput
  }

  private fun addSomeContent(file: Path) {
    Files.newBufferedWriter(file).use { writer -> extracted(writer) }
  }

  private fun extracted(writer: BufferedWriter) {
    writer.write(randomString())
    writer.newLine()
    writer.flush()
  }
}
