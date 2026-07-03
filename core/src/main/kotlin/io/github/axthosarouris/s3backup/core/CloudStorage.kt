package io.github.axthosarouris.s3backup.core

import com.github.awsjavakit.misc.paths.UnixPath
import java.io.InputStream

interface CloudStorage {
  fun listFiles(): List<UnixPath>

  fun uploadFile(
      file: UnixPath,
      content: InputStream,
  )
}
