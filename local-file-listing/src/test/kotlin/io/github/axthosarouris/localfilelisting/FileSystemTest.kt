package io.github.axthosarouris.localfilelisting

import com.github.awsjavakit.testingutils.RandomDataGenerator.randomString
import io.github.axthosarouris.localfilelisting.UnixPathUtils.toUnixPath
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.collections.shouldNotBeIn
import io.kotest.matchers.shouldBe
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.writeBytes
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir

class FileSystemTest {
  @Test
  fun shouldReturnListOfFilesWhenInputIsANonEmptyFolder(
      @TempDir folder: Path,
  ) {
    val file1 = Files.createFile(folder.resolve("file1.txt"))
    val file2 = Files.createFile(folder.resolve("file2.txt"))

    val result = LocalFileSystem().list(folder)
    val expectedResult = listOf(file1, file2).map { UnixPathUtils.toUnixPath(it) }
    result shouldContainExactlyInAnyOrder expectedResult
  }

  @Test
  fun shouldReturnEmptyListWhenInputIsAnEmptyFolder(
      @TempDir folder: Path,
  ) {
    val result = LocalFileSystem().list(folder)
    result.shouldBeEmpty()
  }

  @Test
  fun shouldReturnFileContentsAsStreamOfArrayWhenGettingAFile(
      @TempDir folder: Path,
  ) {
    val file = Files.createFile(folder.resolve(randomString()))
    val bytes = randomBytes()
    file.writeBytes(bytes)
    val fs = LocalFileSystem()
    val content = ByteArrayInputStream(bytes)
    val actualContent = fs.readFile(toUnixPath(file))
    actualContent.readAllBytes() shouldBe content.readAllBytes()
  }

  @Test
  fun shouldListOnlyTheFilesAndFoldersOfTheImmediatelyNextLevel(
      @TempDir folder: Path,
  ) {
    val nestedFolder = folder.resolve("nested")
    val fileInNestedFolder = createFileWithSomeContent(nestedFolder.resolve(randomString()))
    val fileInFolder = createFileWithSomeContent(folder.resolve(randomString()))
    val result = LocalFileSystem().list(folder)
    result shouldContainExactlyInAnyOrder listOf(nestedFolder, fileInFolder).map { toUnixPath(it) }
    toUnixPath(fileInNestedFolder) shouldNotBeIn result
  }

  @Test
  fun shouldThrowExceptionWhenInputIsNotADirectory(
      @TempDir folder: Path,
  ) {
    val file = Files.createFile(folder.resolve("file.txt"))
    val exception = assertThrows<IllegalArgumentException> { LocalFileSystem().list(file) }
    exception.message shouldBe "Expected a directory but got: $file"
  }

  @Test
  fun shouldThrowExceptionWhenInputDoesNotExist(
      @TempDir folder: Path,
  ) {
    val nonExistent = folder.resolve("does-not-exist")
    assertThrows<IllegalArgumentException> { LocalFileSystem().list(nonExistent) }
  }

  @Test
  fun shouldNotIncludeSymlinkedFiles(
      @TempDir folder: Path,
  ) {
    val realFile = createFileWithSomeContent(folder.resolve(randomString()))
    val symlink = Files.createSymbolicLink(folder.resolve("link"), realFile)
    val result = LocalFileSystem().list(folder)
    result shouldContainExactlyInAnyOrder listOf(toUnixPath(realFile))
    toUnixPath(symlink) shouldNotBeIn result
  }

  @Test
  fun shouldListRecursivelyAllRegularFilesInsideAFolderExpandingTheSubfolders(
      @TempDir folder: Path,
  ) {
    val nestedFolder = folder.resolve("nested")
    val fileInNestedFolder = createFileWithSomeContent(nestedFolder.resolve(randomString()))
    val fileB = createFileWithSomeContent(folder.resolve(randomString()))
    val result = LocalFileSystem().listRecursively(folder)
    result shouldContainExactlyInAnyOrder
        listOf(
                fileInNestedFolder,
                fileB,
            )
            .map { toUnixPath(it) }
  }

  @Test
  fun shouldNotIncludeSymlinkedFilesWhenListingRecursively(
      @TempDir folder: Path,
  ) {
    val realFile = createFileWithSomeContent(folder.resolve(randomString()))
    val symlink = Files.createSymbolicLink(folder.resolve("link"), realFile)
    val result = LocalFileSystem().listRecursively(folder)
    result shouldContainExactlyInAnyOrder listOf(realFile).map { toUnixPath(it) }
    toUnixPath(symlink) shouldNotBeIn result
  }

  private fun randomBytes(): ByteArray = randomString().toByteArray(StandardCharsets.UTF_8)

  private fun createFileWithSomeContent(path: Path): Path {
    path.parent?.createDirectories()
    path.toFile().writeText(randomString())
    return path
  }
}
