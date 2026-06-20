package io.github.axthosarouris.s3backup.core

import io.github.axthosarouris.localfilelisting.FileSystem
import java.nio.file.Path
import org.slf4j.LoggerFactory

class Application(
    private val userConfig: UserConfiguration,
    private val fileSystem: FileSystem,
) {
  companion object {
    fun create(
        userConfiguration: UserConfiguration,
        fileSystem: FileSystem,
    ): Application = Application(userConfiguration, fileSystem)

    fun create(
        configLocation: Path,
        fileSystem: FileSystem,
    ): Application = Application(UserConfiguration.fromFile(configLocation), fileSystem)
  }

  private val logger = LoggerFactory.getLogger(Application::class.java)

  fun runApp() {
    for (entry in userConfig.folderList) {
      logger.info("{}", entry)
    }
    userConfig.folderList
        .map(Path::of)
        .flatMap({ path -> fileSystem.listRecursively(path) })
        .forEach { logger.info("{}", it) }
  }
}
