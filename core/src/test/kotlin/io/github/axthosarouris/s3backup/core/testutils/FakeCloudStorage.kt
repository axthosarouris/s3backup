package io.github.axthosarouris.s3backup.core.testutils

import com.github.awsjavakit.misc.paths.UnixPath
import io.github.axthosarouris.s3backup.core.CloudStorage
import java.io.InputStream
import java.nio.charset.StandardCharsets

class FakeCloudStorage : CloudStorage {
  private val contents: MutableMap<UnixPath, Content> = mutableMapOf()

  override fun listFiles(): List<UnixPath> =
      contents.keys.asSequence().sortedBy { it.toString() }.toList()

  override fun uploadFile(
      file: UnixPath,
      content: InputStream,
  ) {
    contents[file] = content.readAllBytes().toString(StandardCharsets.UTF_8)
  }
}
