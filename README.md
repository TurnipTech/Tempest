# Tempest 🌦️

A modern Android weather forecast application built with Kotlin and Jetpack Compose.

## Overview

Tempest is a weather forecast app designed to showcase clean architecture principles and modern Android development practices. The app provides current weather conditions and weekly forecasts for your current location or any selected city.

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: Clean Architecture with MVVM
- **Async Programming**: Kotlin Coroutines
- **Android SDK**: Target 35, Min 24
- **Build System**: Gradle with Kotlin DSL

## Getting Started

### Prerequisites

- Android Studio Flamingo or newer
- JDK 11 or higher
- Android SDK with API level 35
- Git

### Building the Project

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd Tempest
   ```

2. **Build the project**
   ```bash
   ./gradlew build
   ```

3. **Install on device/emulator**
   ```bash
   ./gradlew assembleDebug
   ./gradlew installDebug
   ```

## Development

### Code Quality

The project uses **KtLint** for code formatting and style enforcement:

```bash
# Format code automatically
./gradlew ktlintFormat

# Check code style compliance
./gradlew ktlintCheck

# Run checks on all modules
./gradlew ktlintCheckAll
```

### Testing

```bash
# Run unit tests
./gradlew testDebugUnitTest

# Run all tests
./gradlew check

# Run instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest
```

## Continuous Integration

GitHub Actions automatically runs on pull requests:

- **Code Style**: KtLint formatting checks
- **Unit Tests**: Automated test execution
- **Build Verification**: Ensures successful compilation

The CI pipeline runs:
1. `./gradlew ktlintCheckAll` - Code style verification
2. `./gradlew testDebugUnitTest` - Unit test execution  
3. `./gradlew assembleDebug` - Build verification

## Code Style

This project enforces consistent code style using:

- **KtLint**: Kotlin code formatting and linting
- **EditorConfig**: IDE-agnostic code style configuration
- **Compose Rules**: Additional linting rules for Jetpack Compose

Style is automatically checked in CI and should be verified before committing:

```bash
./gradlew ktlintFormat
```
