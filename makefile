
.DEFAULT_GOAL := build

.PHONY: build

local-build:
	./gradlew spotlessApply
	./gradlew build

build:
	./gradlew build
