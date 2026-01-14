# EconomyPlugin

A focused economy plugin for Hytale servers. This project contains only economy-related commands and persistence.

## Features

- `/bal` — shows your balance
- `/pay {user} {amt}` — transfer funds to another online player
- `/baltop` — shows the top balances
- `/eco give {user} {amt}` — admin-only balance increase
- `/eco set {user} {amt}` — admin-only balance set

Balances are stored per-player using UUIDs and persisted to `balances.json` in the plugin data folder.

## Permissions

- `economy.admin` — required for `/eco give` and `/eco set` (ops are also allowed)

## Requirements

- Java 17+
- Hytale Server API (`HytaleServer.jar`) for local builds

## Getting HytaleServer.jar

You need the `HytaleServer.jar` file to compile plugins locally. There are two ways to obtain it:

### Option 1: From Hytale Launcher

After installing the Hytale Launcher, you can find the server files in:

| OS      | Path                                                            |
|---------|-----------------------------------------------------------------|
| Windows | `%appdata%\Hytale\install\release\package\game\latest`          |
| Linux   | `$XDG_DATA_HOME/Hytale/install/release/package/game/latest`     |
| macOS   | `~/Application Support/Hytale/install/release/package/game/latest` |

Copy `HytaleServer.jar` from that directory into the `libs/` folder of this project.

### Option 2: Hytale Downloader CLI

For production servers, you can use the official **Hytale Downloader CLI** tool to download the latest server files. This requires OAuth2 authentication.

For more details, see the official [Hytale Server Manual](https://support.hytale.com/hc/en-us/articles/45326769420827-Hytale-Server-Manual).

## Building

### Local build (with HytaleServer.jar)

1. Place `HytaleServer.jar` in the `libs/` directory.
2. Build with Gradle:

```bash
./gradlew clean build
```

### CI build (without server jar)

The project supports a stubbed build for CI via:

```bash
./gradlew clean build -Pci=true
```

The compiled plugin JAR will be located at `build/libs/EconomyPlugin-1.0.0.jar`.

## Installation

Copy the built JAR file to your Hytale server's `plugins/` directory.

## Usage

- `/bal`
- `/pay {user} {amt}`
- `/baltop`
- `/eco give {user} {amt}`
- `/eco set {user} {amt}`

## Project Structure

```
src/main/java/com/hytaleeco/plugin/
├── EconomyPlugin.java          # Main plugin class
├── command/                    # Command implementations
├── economy/                    # Economy logic + persistence
└── util/                       # Utility helpers
```

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
