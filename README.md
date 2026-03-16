# NekoproofEnhancement

A PaperMC plugin that provides vanilla enhancements for the **nekoproof** Minecraft server.

[![Build](https://github.com/Rorical/nekoproof-enhancement/actions/workflows/main.yml/badge.svg)](https://github.com/Rorical/nekoproof-enhancement/actions/workflows/main.yml)

## About

NekoproofEnhancement is a collection of quality-of-life improvements and gameplay tweaks built on top of vanilla Minecraft mechanics for the nekoproof server.

## Features

- **Elytra Duplication** — Craft 2 elytra by placing an elytra in the center, a Diamond at top center, and Phantom Membranes in the remaining slots.
- **Iron Golem Egg Crafting** — Craft an Iron Golem spawn egg using the same T-shape as building an iron golem: Carved Pumpkin on top, Iron Blocks in a cross.
- **Dispenser Cauldron Interaction** — Dispensers with buckets can interact with cauldrons like source blocks: empty buckets collect from lava/full water cauldrons, and water/lava buckets fill empty cauldrons.

## Requirements

- Java 21+
- PaperMC 1.21.11+

## Building

```bash
./gradlew build
```

JARs can be found in `build/libs/`.

## Installation

1. Build the plugin or download a release JAR
2. Place the JAR in your server's `plugins/` directory
3. Restart the server

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).

## License

This project is licensed under the GNU General Public License v3.0 - see [LICENSE](LICENSE) for details.
