package io.github.axthosarouris.s3backup.core.testutils

import com.github.awsjavakit.misc.ioutils.IoUtils
import com.github.awsjavakit.misc.paths.UnixPath
import io.github.axthosarouris.localfilelisting.FileSystem
import io.github.axthosarouris.localfilelisting.UnixPathUtils
import java.io.InputStream
import java.nio.file.Path

typealias Content = String

private val EMPTY_CONTENT: Content = String()

class FakeFileSystem : FileSystem {
  private val contents: MutableMap<Path, Content> = mutableMapOf()

  fun createFile(
      file: Path,
      content: Content,
  ) {
    contents[file] = content
  }

  override fun list(folder: Path): List<UnixPath> =
      contents.keys
          .asSequence()
          .filter { entry -> entry.parent.equals(folder) }
          .map({ path -> UnixPathUtils.toUnixPath(path) })
          .toList()

  override fun listRecursively(folder: Path): List<UnixPath> =
      contents.keys
          .asSequence()
          .filter { entry -> entry.startsWith(folder) }
          .map({ UnixPathUtils.toUnixPath(it) })
          .toList()

  override fun readFile(path: UnixPath): InputStream {
    val content =
        if (contents.containsKey(path.toPath())) {
          contents[path.toPath()]
        } else {
          emptyContent()
        }
    return IoUtils.stringToStream(content.toString())
  }

  private fun emptyContent(): Content = EMPTY_CONTENT
}
