package io.github.axthosarouris.s3backup.core

import java.nio.file.Path
import org.tomlj.Toml
import org.tomlj.TomlArray

class UserConfiguration(
    val folderList: List<Path>,
) {
  companion object {
    fun parse(input: String): UserConfiguration {
      val folders = Toml.parse(input).getArrayOrEmpty("core.folders").asStringList()
      val folderPaths = folders.map { Path.of(it) }
      return UserConfiguration(folderPaths)
    }

    private fun TomlArray.asStringList(): List<String> = List(size()) { getString(it) }
  }
}
