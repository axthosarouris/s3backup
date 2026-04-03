
.DEFAULT_GOAL := build

.PHONY: build local-build

ifeq ($(OS),Windows_NT)
    GRADLEW := gradlew.bat
else
    GRADLEW := ./gradlew
endif

local-build:
	$(GRADLEW) spotlessApply
	$(GRADLEW) build

build:
	$(GRADLEW) build
