# GeoVein

GeoVein is a NeoForge Minecraft mod focused on replacing traditional small ore blobs with large, data-driven geological ore deposits.

The goal is to make mining feel more like discovering and exploiting real mineral deposits: fewer discoveries, much larger yields, and ore quality that matters.

> Status: Early development / prototype

---

## Current Features

- NeoForge 1.21.1 mod setup
- Universal `ore_chunk` item
- Ore chunks store data using Minecraft data components
- Four ore quality tiers:
    - Poor
    - Common
    - Rich
    - Native
- Dynamic ore chunk names, for example:
    - Poor Copper Ore Chunk
    - Rich Iron Ore Chunk
- Tooltips showing:
    - Ore type
    - Grade
    - Yield multiplier
- JSON-loaded ore definitions
- Automatic ore definition reload listener
- Support for adding new ore definitions through JSON
- Grade-based item textures using custom model data
- Copper, iron, and default grade textures

---

## Planned Features

GeoVein is planned to become a full replacement ore generation system.

Planned systems include:

- Massive multi-chunk ore deposits
- Configurable vein shapes:
    - Ellipsoid
    - Sphere
    - Cone
    - Sheet
    - Pipe
    - Branching vein
- Ore grade based on distance from deposit core
- Deposit density and falloff
- Noise-based irregular ore bodies
- JSON-configurable ore definitions
- Modpack-friendly ore registration
- Support for modded ores
- Prospecting tools
- Surface indicators
- Optional replacement of vanilla ore generation

---

## Ore Grades

GeoVein uses four ore grades:

| Grade | Description | Yield Multiplier |
|---|---|---:|
| Poor | Low quality ore | 0.25x |
| Common | Standard ore | 1.0x |
| Rich | High quality ore | 1.75x |
| Native | Very pure ore | 3.0x |

These values may change during balancing.

---