package com.example.localfilelisting

import com.github.awsjavakit.misc.paths.UnixPath
import java.nio.file.Path

class FileListGenerator(
    private val fs: FileSystem,
) {
  @Suppress("SpreadOperator")
  fun listFiles(selectedFolder: UnixPath): List<UnixPath> =
      fs.listRecursively(selectedFolder.toPath())
          .asSequence()
          .map { toList(it) }
          .map { UnixPath.of(*it) }
          .toList()

  private fun toList(it: Path): Array<String> = it.toList().map { i -> i.toString() }.toTypedArray()
}
