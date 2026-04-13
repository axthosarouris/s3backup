package com.example.localfilelisting

import java.nio.file.Files
import java.nio.file.Path

interface FileSystem {
  fun list(folder: Path): List<Path>

  fun listRecursively(folder: Path): List<Path>
}

class LocalFileSystem : FileSystem {
  override fun list(folder: Path): List<Path> {
    val absoluteFolder = folder.toAbsolutePath()
    validateInput(absoluteFolder)
    return Files.list(folder).filter { !Files.isSymbolicLink(it) }.toList()
  }

  override fun listRecursively(folder: Path): List<Path> {
    val absoluteFolder = folder.toAbsolutePath()
    validateInput(absoluteFolder)
    return allFilesListedRecursively(absoluteFolder)
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
