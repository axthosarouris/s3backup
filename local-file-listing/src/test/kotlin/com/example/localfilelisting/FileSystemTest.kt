package com.example.localfilelisting

import com.github.awsjavakit.testingutils.RandomDataGenerator.randomString
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.io.TempDir
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectories

class FileSystemTest {
    @Test
    fun shouldReturnListOfFilesWhenInputIsANonEmptyFolder(
        @TempDir folder: Path,
    ) {
        val file1 = Files.createFile(folder.resolve("file1.txt"))
        val file2 = Files.createFile(folder.resolve("file2.txt"))

        val result = LocalFileSystem().list(folder)

        result shouldContainExactlyInAnyOrder listOf(file1, file2)
    }

    @Test
    fun shouldReturnEmptyListWhenInputIsAnEmptyFolder(
        @TempDir folder: Path,
    ) {
        val result = LocalFileSystem().list(folder)
        result.shouldBeEmpty()
    }

    @Test
    fun shouldTraverseAllFoldersInsideInputFolderAndListAllContainedRegularFiles(
        @TempDir folder: Path,
    ) {
        val nestedFolder = folder.resolve("nested")
        val fileA = createFileWithSomeContent(nestedFolder.resolve(randomString()))
        val fileB = createFileWithSomeContent(folder.resolve(randomString()))
        val result = LocalFileSystem().list(folder)
        result shouldContainExactlyInAnyOrder listOf(fileA, fileB)
    }

    @Test
    fun shouldThrowExceptionWhenInputIsNotADirectory(
        @TempDir folder: Path,
    ) {
        val file = Files.createFile(folder.resolve("file.txt"))
        val exception =
            assertThrows<IllegalArgumentException> {
                LocalFileSystem().list(file)
            }
        exception.message shouldBe "Expected a directory but got: $file"
    }

    @Test
    fun shouldThrowExceptionWhenInputDoesNotExist(
        @TempDir folder: Path,
    ) {
        val nonExistent = folder.resolve("does-not-exist")
        assertThrows<IllegalArgumentException> {
            LocalFileSystem().list(nonExistent)
        }
    }

    @Test
    fun shouldIncludeSymlinkedFiles(
        @TempDir folder: Path,
    ) {
        val realFile = createFileWithSomeContent(folder.resolve(randomString()))
        val symlink = Files.createSymbolicLink(folder.resolve("link"), realFile)
        val result = LocalFileSystem().list(folder)
        result shouldContainExactlyInAnyOrder listOf(realFile, symlink)
    }

    private fun createFileWithSomeContent(path: Path): Path {
        path.parent?.createDirectories()
        path.toFile().writeText(randomString())
        return path
    }
}
