package com.example.localfilelisting

import com.github.awsjavakit.misc.paths.UnixPath
import com.github.awsjavakit.testingutils.RandomDataGenerator.randomString
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.MockKAnswerScope
import io.mockk.every
import io.mockk.mockk
import java.nio.file.Path
import org.junit.jupiter.api.Test

class FileListGeneratorTest {
  @Test
  fun shouldReturnListOfUnixPathFiles() {
    val fs = mockk<FileSystem>()
    val selectedFolder = randomUnixPath()
    val expectedOutput = randomUnixPaths(selectedFolder)
    val mockFsOutput = expectedOutput.map { it.toPath() }
    every { fs.listRecursively(any<Path>()) } answers
        {
          generateFileList(selectedFolder, mockFsOutput)
        }
    val fileListGenerator = FileListGenerator(fs)
    val result = fileListGenerator.listFiles(selectedFolder)
    result shouldContainExactlyInAnyOrder expectedOutput
  }

  @Test
  fun shouldReturnEmptyIfFsReturnsEmpty() {
    val fs = mockk<FileSystem>()
    val selectedFolder = randomUnixPath()
    val expectedOutput = randomUnixPaths(selectedFolder)
    val mockFsOutput = expectedOutput.map { it.toPath() }
    every { fs.listRecursively(any<Path>()) } answers
        {
          generateFileList(selectedFolder, mockFsOutput)
        }
    val fileListGenerator = FileListGenerator(fs)
    val result = fileListGenerator.listFiles(randomUnixPath())
    result shouldBeEqual emptyList()
  }

  private fun MockKAnswerScope<List<Path>, List<Path>>.generateFileList(
      selectedFolder: UnixPath,
      mockFsOutput: List<Path>,
  ): List<Path> {
    val inputPath = firstArg<Path>()
    return if (inputPath.isUnderFolder(selectedFolder)) {
      mockFsOutput
    } else {
      emptyList()
    }
  }

  private fun Path.isUnderFolder(selectedFolder: UnixPath): Boolean =
      this.startsWith(selectedFolder.toPath())

  private fun randomUnixPaths(folder: UnixPath): List<UnixPath> =
      listOf(randomUnixPath(folder), randomUnixPath(folder))

  private fun randomUnixPath(folder: UnixPath): UnixPath = folder.addChild(randomString())

  private fun randomUnixPath(): UnixPath = UnixPath.of(randomString())
}
