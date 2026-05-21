package io.github.axthosarouris.s3backup.core

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import java.nio.file.NoSuchFileException
import java.nio.file.Path

private val RESOURCES_PATH = Path.of("src", "test", "resources")

class UserConfigurationTest :
    FunSpec({
      context("userConfiguration reads valid user configuration") {
        test("should read valid toml file ") {
          val config =
              UserConfiguration.fromFile(
                  RESOURCES_PATH.resolve("valid_config.toml").toAbsolutePath(),
              )

          config.folderList shouldContainExactlyInAnyOrder
              listOf(
                  Path.of("/a/b/c/d/e"),
                  Path.of("/some/other/folder"),
              )
        }

        test("should throw when configuration is missing the folders entry") {
          val inputFile = """[core]""".trimIndent()
          shouldThrow<InvalidConfigurationException> { UserConfiguration.parse(inputFile) }
        }

        test("should throw when configuration is invalid toml") {
          val inputFile =
              """
              |[core
              |folders = not a list
              """
                  .trimMargin()
          shouldThrow<InvalidConfigurationException> { UserConfiguration.parse(inputFile) }
        }

        test("should throw Exception when configuration file does not exist") {
          val missingFile = Path.of("/nonexistent/configuration.toml")
          shouldThrow<NoSuchFileException> { UserConfiguration.fromFile(missingFile) }
        }
      }
    })
