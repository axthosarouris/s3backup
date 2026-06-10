package io.github.axthosarouris.s3backup.core

import java.nio.file.Files
import java.nio.file.Path
import org.tomlj.Toml
import org.tomlj.TomlArray
import org.tomlj.TomlParseResult

class UserConfiguration(
    val folderList: List<Path>,
) {
  companion object {
    private const val FOLDERS_KEY = "core.folders"

    fun fromFile(path: Path): UserConfiguration = parse(Files.readString(path))

    fun parse(input: String): UserConfiguration {
      val parsed = Toml.parse(input).also(::failOnParseErrors)
      return UserConfiguration(parsed.requireFolders().asPathList())
    }

    private fun failOnParseErrors(result: TomlParseResult) {
      if (result.hasErrors()) {
        throw InvalidConfigurationException(result.errors().joinToString(separator = "; "))
      }
    }

    private fun TomlParseResult.requireFolders(): TomlArray =
        getArray(FOLDERS_KEY) ?: throw InvalidConfigurationException("Missing '$FOLDERS_KEY'")

    private fun TomlArray.asPathList(): List<Path> = List(size()) { Path.of(getString(it)) }
  }
}
