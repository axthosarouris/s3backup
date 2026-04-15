package com.example.localfilelisting

import com.github.awsjavakit.misc.paths.UnixPath
import java.nio.file.Path

private const val WINDOWS_PATH_DELIMITER = "\\"

object UnixPathUtils {
  @Suppress("SpreadOperator", "ReturnCount")
  fun toUnixPath(path: Path): UnixPath {
    val relativePath = UnixPath.of(*toArray(path)).removeRoot()
    if (path.isAbsolute && osIsWindows(path)) {
      System.out.println(path.root.toString())
      val root = path.root.toString().replace(WINDOWS_PATH_DELIMITER, "")
      return UnixPath.of(root).addChild(relativePath)
    }
    if (path.isAbsolute && !osIsWindows(path)) {
      return relativePath.addRoot()
    }
    return relativePath
  }

  private fun osIsWindows(path: Path): Boolean =
      path.root.toString().contains(WINDOWS_PATH_DELIMITER)

  private fun toArray(path: Path): Array<String> =
      path.toList().map { i -> i.toString() }.toTypedArray()
}
