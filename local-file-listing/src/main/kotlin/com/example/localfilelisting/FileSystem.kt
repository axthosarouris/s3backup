package com.example.localfilelisting

import java.nio.file.Files
import java.nio.file.Path

interface FileSystem {
  fun list(folder: Path): List<Path>
}

class LocalFileSystem : FileSystem {
  override fun list(folder: Path): List<Path> {
    val absoluteFolder = folder.toAbsolutePath()
    require(Files.isDirectory(absoluteFolder)) { "Expected a directory but got: $absoluteFolder" }
    return Files.walk(absoluteFolder).use { stream ->
      stream.filter { Files.isRegularFile(it) }.map { it.toAbsolutePath() }.toList()
    }
  }
}
