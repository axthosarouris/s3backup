package io.github.axthosarouris.s3backup.core

import java.nio.file.Path
import org.slf4j.LoggerFactory

class Application(
    private val configLocation: Path,
) {
  private val logger = LoggerFactory.getLogger(Application::class.java)

  fun runApp() {
    val config = UserConfiguration.fromFile(configLocation)
    for (entry in config.folderList) {
      logger.info("{}", entry)
    }
  }
}
