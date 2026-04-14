# s3backup

Kotlin/Gradle multi-module project for S3 backup tooling.

## Modules

- `app` — main application
- `client` — S3 client code
- `local-file-listing` — local filesystem listing
- `buildSrc` — Gradle convention plugins (`root-conventions`, `kotlin-conventions`)
- `gradle-plugin-config` — shared plugin configuration

Note: `client` is not currently included in `settings.gradle.kts`.

## Build

Use the `makefile` wrapper, which picks the right `gradlew` for the OS:

- `make build` — runs `./gradlew build`
- `make local-build` — runs `spotlessApply` then `build`

Direct Gradle: `./gradlew build`, `./gradlew check`.

## Conventions

- Formatting is enforced by Spotless — run `./gradlew spotlessApply` before committing.
- Dependencies live in the Gradle version catalog (`libs.*`).
- Test dependencies use `libs.bundles.tests`.

## Architecture

- We try to follow Domain Driven Design

## Conventions

- We try to have small functions (around 5 lines)
- We apply the Functional Programming and the Object-Oriented Programming principles. We delegate
  tasks to the associated objects/classes as much as possible.