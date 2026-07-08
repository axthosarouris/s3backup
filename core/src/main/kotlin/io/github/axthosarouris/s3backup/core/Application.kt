package io.github.axthosarouris.s3backup.core

import io.github.axthosarouris.localfilelisting.FileSystem
import java.nio.file.Path
import org.slf4j.LoggerFactory

class Application(
    private val userConfig: UserConfiguration,
    private val fileSystem: FileSystem,
    private val cloudStorage: CloudStorage,
) {
  companion object {
    fun create(
        userConfiguration: UserConfiguration,
        fileSystem: FileSystem,
        cloudStorage: CloudStorage,
    ): Application = Application(userConfiguration, fileSystem, cloudStorage)

    fun create(
        configLocation: Path,
        fileSystem: FileSystem,
        cloudStorage: CloudStorage,
    ): Application =
        Application(UserConfiguration.fromFile(configLocation), fileSystem, cloudStorage)
  }

  private val logger = LoggerFactory.getLogger(Application::class.java)

  fun runApp() {
    for (entry in userConfig.folderList) {
      logger.info("{}", entry)
    }
    userConfig.folderList
        .asSequence()
        .map(Path::of)
        .flatMap({ path -> fileSystem.listRecursively(path).asSequence() })
        .onEach { logger.info("{}", it.toPath()) }
        .forEach { path ->
          fileSystem.readFile(path).use { stream -> cloudStorage.uploadFile(path, stream) }
        }
  }
}
