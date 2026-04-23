package io.github.axthosarouris.s3backup.core

import com.github.awsjavakit.misc.ioutils.IoUtils
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import java.nio.file.Path

class UserConfigurationTest :
    FunSpec({
      context("userConfiguration reads valid user configuration") {
        test("should read valid toml file ") {
          val inputFile = IoUtils.stringFromResources(Path.of("valid_config.toml"))
          val config = UserConfiguration.parse(inputFile)
          config.folderList shouldContainExactlyInAnyOrder
              listOf(
                  Path.of("/a/b/c/d/e"),
                  Path.of("/some/other/folder"),
              )
        }
      }
    })
