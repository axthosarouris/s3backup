package com.example.localfilelisting

import com.github.awsjavakit.misc.paths.UnixPath
import java.nio.file.Path

object UnixPathUtils {
  @Suppress("SpreadOperator") fun toUnixPath(path: Path): UnixPath = UnixPath.of(*toArray(path))

  private fun toArray(path: Path): Array<String> =
      path.toList().map { i -> i.toString() }.toTypedArray()
}
