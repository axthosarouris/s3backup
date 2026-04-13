package com.example.localfilelisting

import com.example.localfilelisting.UnixPathUtils.toUnixPath
import com.github.awsjavakit.misc.paths.UnixPath
import java.nio.file.Files
import java.nio.file.Path

interface FileSystem {
  fun list(folder: Path): List<UnixPath>

  fun listRecursively(folder: Path): List<UnixPath>
}

class LocalFileSystem : FileSystem {
  override fun list(folder: Path): List<UnixPath> {
    val absoluteFolder = folder.toAbsolutePath()
    validateInput(absoluteFolder)
    return Files.list(folder).filter { !Files.isSymbolicLink(it) }.map { toUnixPath(it) }.toList()
  }

  override fun listRecursively(folder: Path): List<UnixPath> {
    val absoluteFolder = folder.toAbsolutePath()
    validateInput(absoluteFolder)
    return allFilesListedRecursively(absoluteFolder).map { toUnixPath(it) }
  }

  private fun validateInput(absoluteFolder: Path) {
    require(Files.isDirectory(absoluteFolder)) { "Expected a directory but got: $absoluteFolder" }
  }

  private fun allFilesListedRecursively(absoluteFolder: Path): List<Path> =
      Files.walk(absoluteFolder).use { stream ->
        stream
            .filter { Files.isRegularFile(it) }
            .filter { !Files.isSymbolicLink(it) }
            .map { it.toAbsolutePath() }
            .toList()
      }
}
