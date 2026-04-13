package com.example.localfilelisting

import com.example.localfilelisting.UnixPathUtils.toUnixPath
import com.google.common.jimfs.Configuration
import com.google.common.jimfs.Jimfs
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.nio.file.FileSystem
import java.nio.file.Path
import org.junit.jupiter.api.Test

class UnixPathUtilsTest {
  @Test
  fun shouldTransformAbsoluteUnixPathAndBackToTheSamePath() {
    val fileSystem: FileSystem = Jimfs.newFileSystem(Configuration.unix())
    val original = fileSystem.getPath("/home/user/file.txt")
    val unixPath = toUnixPath(original)
    unixPath.toString() shouldBe "/home/user/file.txt"
    unixPath.toPath() shouldBe Path.of("/home/user/file.txt")
  }

  @Test
  fun shouldTransformRelativeUnixPathAndBackToTheSamePath() {
    val fileSystem: FileSystem = Jimfs.newFileSystem(Configuration.unix())
    val original = fileSystem.getPath("home/user/file.txt")
    val unixPath = toUnixPath(original)
    unixPath.toString() shouldBe "home/user/file.txt"
    unixPath.toPath() shouldBe Path.of("home/user/file.txt")
  }

  @Test
  fun shouldTransformWindowsPathAndBackToTheSamePath() {
    val fileSystem: FileSystem = Jimfs.newFileSystem(Configuration.windows())
    val original = fileSystem.getPath("C:\\Users\\user\\file.txt")
    val unixPath = toUnixPath(original)
    unixPath.toString() shouldBe "C:/Users/user/file.txt"
    unixPath.toPath() shouldBeEqual Path.of("C:", "Users", "user", "file.txt")
  }
}
