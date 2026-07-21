# GeoVein

**GeoVein** is a Minecraft **NeoForge 1.21.1** ore-generation mod that replaces small vanilla ore blobs with large configurable ore deposits.

Instead of every ore block dropping normal raw ore, GeoVein ore blocks drop graded **ore chunks**. These chunks are crafted into **ore billets**, then processed into useful materials. Ore deposits, drop rates, grade bands, textures, and models can be changed without editing the mod jar.

---

## Features

| Feature | Description |
|---|---|
| Large ore deposits | Replaces small scattered ore blobs with large configurable underground deposits. |
| Config-based ore definitions | Ore and deposit settings are loaded from `config/geovein/ore_definitions/`. |
| Auto-exported configs | Default ore JSON files are generated automatically after first launch. |
| Config resource pack | GeoVein automatically loads `config/geovein/resource_pack/` for external textures and models. |
| Ore grades | Ore chunks can be `Poor`, `Common`, `Rich`, or `Native`. |
| Distance-based quality | Ore near the deposit core can be higher grade than ore near the edge. |
| Ore billets | Chunks are crafted into ore billets before becoming ingots/materials. |
| Configurable deposits | Deposit size, shape, Y-level, density, and spawn chance can be changed per ore. |
| Modpack friendly | New ores can be added by config as long as the block/item already exists in Minecraft or another mod. |
| Debug/test commands | Commands are included for checking loaded deposits and spawning test deposits. |

---

## Requirements

| Requirement | Version |
|---|---|
| Minecraft | `1.21.1` |
| Mod Loader | NeoForge |
| Java | Java 21 |

---

## What GeoVein Changes

Vanilla mining normally works like this:

```text
mine ore block
→ get raw ore
→ smelt raw ore
→ get ingot
```

GeoVein changes the loop to this:

```text
mine GeoVein deposit ore
→ get graded ore chunks
→ craft chunks into ore billets
→ process billets into ingots/items
```

This makes mining feel more like finding and working a real ore body, rather than just collecting random ore blobs.

---

## Default Ores

GeoVein currently ships with these default ore definitions:

| Ore | Source Block | Deepslate Block | Output Item | Chunk Model Data | Billet Model Data |
|---|---|---|---|---:|---:|
| Copper | `minecraft:copper_ore` | `minecraft:deepslate_copper_ore` | `minecraft:copper_ingot` | `1000–1003` | `1100` |
| Iron | `minecraft:iron_ore` | `minecraft:deepslate_iron_ore` | `minecraft:iron_ingot` | `2000–2003` | `2100` |
| Gold | `minecraft:gold_ore` | `minecraft:deepslate_gold_ore` | `minecraft:gold_ingot` | `3000–3003` | `3100` |
| Coal | `minecraft:coal_ore` | `minecraft:deepslate_coal_ore` | `minecraft:coal` | `4000–4003` | `4100` |

Emerald is **not** included by default. It is used in this README as an example custom ore.

---

## Ore Grades

| Grade | Meaning | Typical Use |
|---|---|---|
| Poor | Low-quality ore | Needs more chunks per billet. |
| Common | Normal ore | Baseline grade. |
| Rich | High-quality ore | Gives better billet output. |
| Native | Very high-quality ore | Best grade, usually found near the deposit core. |

Default crafting behaviour:

| Recipe | Output |
|---|---|
| `4 Poor Chunks` | `1 Ore Billet` |
| `1 Common Chunk` | `1 Ore Billet` |
| `2 Rich Chunks` | `3 Ore Billets` |
| `1 Native Chunk` | `3 Ore Billets` |

Default billet processing:

| Billet | Processing |
|---|---|
| Copper Billet | Smelts/blasts into `minecraft:copper_ingot` |
| Iron Billet | Smelts/blasts into `minecraft:iron_ingot` |
| Gold Billet | Smelts/blasts into `minecraft:gold_ingot` |
| Coal Billet | Crafts into `minecraft:coal` |

---

## Generated Config Folders

After launching the game once, GeoVein creates:

```text
config/geovein/
├─ README.txt
├─ ore_definitions/
│  ├─ coal.json
│  ├─ copper.json
│  ├─ gold.json
│  └─ iron.json
└─ resource_pack/
   ├─ pack.mcmeta
   ├─ README.txt
   └─ assets/
      └─ geovein/
         ├─ textures/
         │  └─ item/
         │     ├─ ore_chunks/
         │     └─ ore_billets/
         └─ models/
            └─ item/
               ├─ ore_chunks/
               └─ ore_billets/
```

---

## Config Folder Purposes

| Folder | Purpose |
|---|---|
| `config/geovein/ore_definitions/` | Ore and deposit JSON configs. |
| `config/geovein/resource_pack/` | Automatically loaded resource pack for textures and item models. |
| `config/geovein/resource_pack/assets/geovein/textures/` | Custom PNG textures. |
| `config/geovein/resource_pack/assets/geovein/models/` | Custom item model JSON files. |

---

## Commands

| Command | Description |
|---|---|
| `/geovein deposits` | Lists loaded GeoVein ore definitions. |
| `/geovein test_deposit <ore>` | Generates a test deposit for the selected ore. |

Example:

```text
/geovein deposits
```

```text
/geovein test_deposit copper
```

---

# Adding a New Ore

This section shows how to add a new ore using **Emerald** as an example.

Important: GeoVein configs do **not** register new blocks or new items. The block and output item must already exist in Minecraft or another mod.

For example, this works because vanilla already has these:

```text
minecraft:emerald_ore
minecraft:deepslate_emerald_ore
minecraft:emerald
```

A modded ore also works as long as the IDs exist, such as:

```text
examplemod:tin_ore
examplemod:deepslate_tin_ore
examplemod:tin_ingot
```

---

## Step 1 — Create the Ore Definition

Create this file:

```text
config/geovein/ore_definitions/emerald.json
```

Paste this example:

```json
{
  "id": "emerald",
  "display_name": "Emerald",
  "source_ore_block": "minecraft:emerald_ore",
  "processed_item": "minecraft:emerald",
  "deepslate_ore_block": "minecraft:deepslate_emerald_ore",
  "deepslate_below_y": 0,

  "chunk_model_data_base": 5000,
  "billet_model_data": 5100,

  "shape": "ellipsoid",
  "min_y": -32,
  "max_y": 32,
  "length": 120,
  "width": 40,
  "height": 25,
  "density": 0.25,
  "region_size_chunks": 32,
  "spawn_chance_per_region": 0.12,

  "drops": {
    "min_chunks_per_block": 4,
    "max_chunks_per_block": 4,
    "grade_bands": [
      {
        "max_distance": 0.25,
        "poor": 0.0833,
        "common": 0.0833,
        "rich": 0.0834,
        "native": 0.75
      },
      {
        "max_distance": 0.50,
        "poor": 0.0667,
        "common": 0.0667,
        "rich": 0.80,
        "native": 0.0666
      },
      {
        "max_distance": 0.75,
        "poor": 0.0333,
        "common": 0.90,
        "rich": 0.0334,
        "native": 0.0333
      },
      {
        "max_distance": 1.00,
        "poor": 1.00,
        "common": 0.00,
        "rich": 0.00,
        "native": 0.00
      }
    ]
  }
}
```

Then restart the world or run:

```text
/reload
```

Check it loaded with:

```text
/geovein deposits
```

---

# Ore Definition Field Guide

## Basic Fields

| Field | Example | Description |
|---|---:|---|
| `id` | `"emerald"` | Internal ore ID used by GeoVein. Keep it lowercase and unique. |
| `display_name` | `"Emerald"` | Name shown in chunk and billet item names/tooltips. |
| `source_ore_block` | `"minecraft:emerald_ore"` | Main ore block placed by the deposit. |
| `processed_item` | `"minecraft:emerald"` | Item the billet should eventually process into. |
| `deepslate_ore_block` | `"minecraft:deepslate_emerald_ore"` | Optional deepslate version. |
| `deepslate_below_y` | `0` | Uses the deepslate block at or below this Y-level. |

---

## Model Data Fields

| Field | Example | Description |
|---|---:|---|
| `chunk_model_data_base` | `5000` | Base custom model data for ore chunks. |
| `billet_model_data` | `5100` | Custom model data for the ore billet. |

Chunk model data uses four values:

| Grade | Custom Model Data |
|---|---:|
| Poor | `chunk_model_data_base + 0` |
| Common | `chunk_model_data_base + 1` |
| Rich | `chunk_model_data_base + 2` |
| Native | `chunk_model_data_base + 3` |

For emerald using base `5000`:

| Grade | Custom Model Data |
|---|---:|
| Poor Emerald Chunk | `5000` |
| Common Emerald Chunk | `5001` |
| Rich Emerald Chunk | `5002` |
| Native Emerald Chunk | `5003` |
| Emerald Billet | `5100` |

---

## Deposit Generation Fields

| Field | Example | Description |
|---|---:|---|
| `shape` | `"ellipsoid"` | Deposit shape. |
| `min_y` | `-32` | Lowest Y-level the deposit center can spawn at. |
| `max_y` | `32` | Highest Y-level the deposit center can spawn at. |
| `length` | `120` | Deposit length in blocks. |
| `width` | `40` | Deposit width in blocks. |
| `height` | `25` | Deposit height in blocks. |
| `density` | `0.25` | How filled-in the deposit is. Higher means more ore blocks. |
| `region_size_chunks` | `32` | Size of each generation region in chunks. |
| `spawn_chance_per_region` | `0.12` | Chance that this ore spawns in each region. |

---

## Supported Deposit Shapes

| Shape | Description |
|---|---|
| `ellipsoid` | Large rounded vein/deposit. Good default. |
| `sphere` | Round compact deposit. |
| `cone` | Cone-shaped deposit. |
| `sheet` | Flat layered deposit. |
| `pipe` | Vertical pipe-like deposit. |

---

## Drop Settings

| Field | Example | Description |
|---|---:|---|
| `min_chunks_per_block` | `4` | Minimum chunks dropped per ore block. |
| `max_chunks_per_block` | `4` | Maximum chunks dropped per ore block. |
| `grade_bands` | list | Controls ore grade chance by distance from deposit core. |

`max_distance` is normalized:

| Value | Meaning |
|---:|---|
| `0.00` | Deposit center/core |
| `0.25` | Inner core |
| `0.50` | Middle deposit |
| `0.75` | Outer deposit |
| `1.00` | Edge of deposit |

Example:

```json
{
  "max_distance": 0.25,
  "poor": 0.0833,
  "common": 0.0833,
  "rich": 0.0834,
  "native": 0.75
}
```

This means blocks within the inner 25% of the deposit have a 75% chance to drop Native-grade chunks.

---

# Adding Custom Textures

GeoVein automatically loads this folder as a client resource pack:

```text
config/geovein/resource_pack/
```

This means textures and item models can be added without editing the jar.

After changing textures or models, press:

```text
F3 + T
```

or restart the game.

---

## Step 1 — Add Chunk Textures

For emerald, create this folder:

```text
config/geovein/resource_pack/assets/geovein/textures/item/ore_chunks/emerald/
```

Add these PNG files:

```text
poor.png
common.png
rich.png
native.png
```

Final structure:

```text
config/geovein/resource_pack/assets/geovein/textures/item/ore_chunks/emerald/
├─ poor.png
├─ common.png
├─ rich.png
└─ native.png
```

Recommended texture size:

```text
16x16 PNG
```

---

## Step 2 — Add Billet Texture

Create or use this folder:

```text
config/geovein/resource_pack/assets/geovein/textures/item/ore_billets/
```

Add:

```text
emerald.png
```

Final path:

```text
config/geovein/resource_pack/assets/geovein/textures/item/ore_billets/emerald.png
```

---

# Adding Item Models

Textures alone are not enough. Minecraft also needs item model JSON files.

---

## Step 1 — Add Chunk Model Files

Create:

```text
config/geovein/resource_pack/assets/geovein/models/item/ore_chunks/emerald/
```

Add these four files:

```text
poor.json
common.json
rich.json
native.json
```

### `poor.json`

```json
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "geovein:item/ore_chunks/emerald/poor"
  }
}
```

### `common.json`

```json
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "geovein:item/ore_chunks/emerald/common"
  }
}
```

### `rich.json`

```json
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "geovein:item/ore_chunks/emerald/rich"
  }
}
```

### `native.json`

```json
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "geovein:item/ore_chunks/emerald/native"
  }
}
```

Final structure:

```text
config/geovein/resource_pack/assets/geovein/models/item/ore_chunks/emerald/
├─ poor.json
├─ common.json
├─ rich.json
└─ native.json
```

---

## Step 2 — Add Billet Model File

Create:

```text
config/geovein/resource_pack/assets/geovein/models/item/ore_billets/emerald.json
```

Paste:

```json
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "geovein:item/ore_billets/emerald"
  }
}
```

---

# Adding Custom Model Data Overrides

GeoVein uses one universal item for chunks:

```text
geovein:ore_chunk
```

and one universal item for billets:

```text
geovein:ore_billet
```

The texture is chosen using `minecraft:custom_model_data`.

This means new ores need override entries in:

```text
config/geovein/resource_pack/assets/geovein/models/item/ore_chunk.json
config/geovein/resource_pack/assets/geovein/models/item/ore_billet.json
```

Important: resource pack model files replace the whole built-in file. Copy the default model file first, then add your new entries.

---

## Emerald Chunk Override Entries

Add these entries to the `overrides` array in:

```text
config/geovein/resource_pack/assets/geovein/models/item/ore_chunk.json
```

```json
{
  "predicate": {
    "custom_model_data": 5000
  },
  "model": "geovein:item/ore_chunks/emerald/poor"
},
{
  "predicate": {
    "custom_model_data": 5001
  },
  "model": "geovein:item/ore_chunks/emerald/common"
},
{
  "predicate": {
    "custom_model_data": 5002
  },
  "model": "geovein:item/ore_chunks/emerald/rich"
},
{
  "predicate": {
    "custom_model_data": 5003
  },
  "model": "geovein:item/ore_chunks/emerald/native"
}
```

Make sure the final entry in a JSON array does not end with a comma.

Bad:

```json
{
  "model": "geovein:item/ore_chunks/emerald/native"
},
```

Good:

```json
{
  "model": "geovein:item/ore_chunks/emerald/native"
}
```

---

## Emerald Billet Override Entry

Add this entry to the `overrides` array in:

```text
config/geovein/resource_pack/assets/geovein/models/item/ore_billet.json
```

```json
{
  "predicate": {
    "custom_model_data": 5100
  },
  "model": "geovein:item/ore_billets/emerald"
}
```

---

# Complete Example File Structure for Emerald

```text
config/geovein/
├─ ore_definitions/
│  └─ emerald.json
└─ resource_pack/
   ├─ pack.mcmeta
   └─ assets/
      └─ geovein/
         ├─ textures/
         │  └─ item/
         │     ├─ ore_chunks/
         │     │  └─ emerald/
         │     │     ├─ poor.png
         │     │     ├─ common.png
         │     │     ├─ rich.png
         │     │     └─ native.png
         │     └─ ore_billets/
         │        └─ emerald.png
         └─ models/
            └─ item/
               ├─ ore_chunk.json
               ├─ ore_billet.json
               ├─ ore_chunks/
               │  └─ emerald/
               │     ├─ poor.json
               │     ├─ common.json
               │     ├─ rich.json
               │     └─ native.json
               └─ ore_billets/
                  └─ emerald.json
```

---

# Adding Deposits for Modded Ores

For a modded ore, use the block and item IDs from that mod.

Example tin ore:

```json
{
  "id": "tin",
  "display_name": "Tin",
  "source_ore_block": "examplemod:tin_ore",
  "processed_item": "examplemod:tin_ingot",
  "deepslate_ore_block": "examplemod:deepslate_tin_ore",
  "deepslate_below_y": 0,

  "chunk_model_data_base": 6000,
  "billet_model_data": 6100,

  "shape": "ellipsoid",
  "min_y": -32,
  "max_y": 96,
  "length": 180,
  "width": 55,
  "height": 30,
  "density": 0.28,
  "region_size_chunks": 32,
  "spawn_chance_per_region": 0.20,

  "drops": {
    "min_chunks_per_block": 4,
    "max_chunks_per_block": 4,
    "grade_bands": [
      {
        "max_distance": 0.25,
        "poor": 0.0833,
        "common": 0.0833,
        "rich": 0.0834,
        "native": 0.75
      },
      {
        "max_distance": 0.50,
        "poor": 0.0667,
        "common": 0.0667,
        "rich": 0.80,
        "native": 0.0666
      },
      {
        "max_distance": 0.75,
        "poor": 0.0333,
        "common": 0.90,
        "rich": 0.0334,
        "native": 0.0333
      },
      {
        "max_distance": 1.00,
        "poor": 1.00,
        "common": 0.00,
        "rich": 0.00,
        "native": 0.00
      }
    ]
  }
}
```

Then create matching texture/model files using:

```text
tin
```

instead of:

```text
emerald
```

Recommended model data:

| Ore | Chunk Model Data | Billet Model Data |
|---|---:|---:|
| Custom ore 1 | `5000–5003` | `5100` |
| Custom ore 2 | `6000–6003` | `6100` |
| Custom ore 3 | `7000–7003` | `7100` |
| Custom ore 4 | `8000–8003` | `8100` |

Avoid reusing model data values already used by another ore.

---

# Recommended Deposit Settings

## Common Ore

Good for ores like copper, iron, tin, lead, zinc:

```json
"length": 220,
"width": 75,
"height": 40,
"density": 0.30,
"region_size_chunks": 32,
"spawn_chance_per_region": 0.30
```

## Valuable Ore

Good for gold, diamond, emerald, rare modded ores:

```json
"length": 100,
"width": 35,
"height": 22,
"density": 0.22,
"region_size_chunks": 32,
"spawn_chance_per_region": 0.08
```

## Large Industrial Ore

Good for coal, salt, sulfur, low-value bulk ores:

```json
"length": 280,
"width": 100,
"height": 55,
"density": 0.35,
"region_size_chunks": 32,
"spawn_chance_per_region": 0.35
```

---

# Testing a New Ore

## 1 — Check the ore loaded

Run:

```text
/geovein deposits
```

Your new ore ID should appear in the list.

---

## 2 — Generate a test deposit

Run:

```text
/geovein test_deposit emerald
```

Replace `emerald` with your ore ID.

---

## 3 — Mine the ore

Mine blocks from the deposit.

Expected result:

```text
Poor/Common/Rich/Native Emerald Ore Chunks
```

The exact grades depend on where the block was inside the deposit.

---

## 4 — Check textures

Give yourself chunks or billets and check the textures.

After changing models/textures, press:

```text
F3 + T
```

---

# Important Notes

## Existing worlds

GeoVein generation affects newly generated chunks. Existing explored chunks will not automatically regenerate ore deposits.

Use the test command for quick checking:

```text
/geovein test_deposit <ore>
```

---

## Blocks and items must exist

GeoVein can use blocks/items from Minecraft or other mods, but it does not create new registered blocks/items from config files.

This is valid:

```json
"source_ore_block": "minecraft:emerald_ore"
```

This only works if another mod actually adds the block:

```json
"source_ore_block": "examplemod:tin_ore"
```

---

## Textures are client-side

The config resource pack is for client resources such as:

```text
textures
models
lang files
```

Ore definitions are loaded separately from:

```text
config/geovein/ore_definitions/
```

---

## Recipes for new ores

Default GeoVein ores include default recipes.

Custom ore definitions can generate deposits and chunks, but full processing recipes for completely new ores may require extra recipe files or future dynamic recipe support.

---

# Troubleshooting

| Problem | Cause | Fix |
|---|---|---|
| New ore does not appear in `/geovein deposits` | JSON failed to load or file is in the wrong folder. | Check `config/geovein/ore_definitions/` and the game log. |
| Game says block ID is invalid | The block does not exist. | Check the exact block ID from Minecraft or the mod adding it. |
| Item has missing purple/black texture | Missing PNG or model JSON. | Check texture path, model path, and custom model data override. |
| Built-in ore textures disappeared | You replaced `ore_chunk.json` or `ore_billet.json` without keeping default overrides. | Copy the built-in model file first, then add new entries. |
| JSON model fails to load | Missing comma, extra comma, or invalid JSON. | Validate the JSON and check the line number in the log. |
| Ore generates too often | `spawn_chance_per_region` is too high. | Lower the value. |
| Deposit is too huge | `length`, `width`, or `height` is too high. | Lower size values. |
| Deposit is too sparse | `density` is too low. | Increase density. |
| Only poor chunks drop | Grade bands are configured with too much `poor` chance. | Adjust `grade_bands`. |
| Changes do not appear | Resources not reloaded or world not reloaded. | Use `/reload`, `F3 + T`, or restart. |

---

# Example JSON Mistakes

## Bad trailing comma

Bad:

```json
{
  "id": "emerald",
}
```

Good:

```json
{
  "id": "emerald"
}
```

## Bad model override trailing comma

Bad:

```json
"overrides": [
  {
    "predicate": {
      "custom_model_data": 5100
    },
    "model": "geovein:item/ore_billets/emerald"
  },
]
```

Good:

```json
"overrides": [
  {
    "predicate": {
      "custom_model_data": 5100
    },
    "model": "geovein:item/ore_billets/emerald"
  }
]
```

---

# For Modpack Authors

GeoVein is designed so modpacks can change ore generation without editing the jar.

Recommended workflow:

1. Install GeoVein.
2. Launch once to generate config folders.
3. Edit or add files in:

```text
config/geovein/ore_definitions/
```

4. Add textures and models to:

```text
config/geovein/resource_pack/
```

5. Test with:

```text
/geovein deposits
/geovein test_deposit <ore>
```

# Current Status

| System | Status |
|---|---|
| Config ore definitions | Working |
| Auto-exported default configs | Working |
| Natural deposit generation | Working |
| Test deposit command | Working |
| Graded chunk drops | Working |
| Ore billets | Working |
| Default recipes | Working |
| Config resource pack | Working |
| External textures/models | Working |
| Runtime block registration from config | Not planned/currently unsupported |
| Dynamic recipe generation for new config ores | Future improvement |

---

# Credits

Created by TheCaptain.

## Note
This is my first mod so please let me know about any Bugs or Problem so i can resolve them.