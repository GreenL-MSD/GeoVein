# GeoVein

GeoVein is a NeoForge 1.21.1 world generation and ore processing mod that in addition to small vanilla-style ore blobs adds extremely large configurable ore deposits.

Instead of every ore block being the same, GeoVein deposits have graded ore chunks. The closer to the core of a deposit you mine, the better the ore can be.

## Features
### All Features bellow are Data Driven - Meaning you the user can configure them through the mods .JSON files

- Large configurable ore deposits
- JSON-defined ore types
- Configurable deposit shape, size, density and spawn rate
- Configurable ore grade distribution
- Poor, Common, Rich and Native ore chunks
- Ore chunks craft into ore billets
- Metal billets smelt or blast into ingots
- Coal billets craft directly into coal
- Deepslate ore switching by Y-level
- Custom item textures using `custom_model_data`
- Modpack-friendly ore definitions
- No Java editing needed to add or change ores

## Current Built-In Ores

GeoVein currently includes:

- Copper
- Iron
- Gold
- Coal

## How GeoVein Works

The basic gameplay loop is:

```text
Find a GeoVein deposit
→ Mine ore blocks
→ Receive graded ore chunks
→ Craft ore chunks into ore billets
→ Smelt/craft Billets into Ingots
```
## GeoVein uses four ore grades:
| Grade     | Meaning                 | Default Use               |
|-----------|-------------------------|---------------------------|
| Poor      | Low quality ore         | 4 chunks make 1 billet    |
| Common    | Normal ore              | 1 chunk makes 1 billet    |
| Rich      | High quality ore        | 2 chunks make 3 billets   |
| Native    | Very high quality ore   | 1 chunk makes 3 billets   |
| --------- | ----------------------- | ------------------------- |
```text 
The ore grade is decided by where the ore block is inside the deposit.

By default:

Deposit core gives mostly Native ore
Inner deposit gives mostly Rich ore
Outer deposit gives mostly Common ore
Edge gives mostly Poor ore
```

## Commands
GeoVein currently includes debug/helper commands:

/geovein deposits
Lists loaded GeoVein ore deposit definitions.

/geovein test_deposit <ore>
Generates a test deposit for the chosen ore near the player.

## Ore Definition Files
```text
This is where the Data Driven aspect of this mod starts. These files Declare a New Type of Ore 'Deposit', what its source block is, its size, shape, rarity etc.
These Files can be found in the path: data/geovein/ore_definitions/.
See Below for a Example Config;
```
### Example
```text
{
  "id": "copper",
  "display_name": "Copper",
  "source_ore_block": "minecraft:copper_ore",
  "processed_item": "minecraft:copper_ingot",
  "deepslate_ore_block": "minecraft:deepslate_copper_ore",
  "deepslate_below_y": 0,
  
  "chunk_model_data_base": 1000,
  "billet_model_data": 1100,
  
  "shape": "ellipsoid",
  "min_y": -48,
  "max_y": 80,
  "length": 180,
  "width": 60,
  "height": 35,
  "density": 0.35,
  "region_size_chunks": 20,
  "spawn_chance_per_region": 0.45,
  
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
## Ore Definition Field Guide
### Basic Fields
| Field                 | Type      | Description                                     |
|-----------------------|-----------|-------------------------------------------------|
| id                    | string    | Internal ore ID. Example: copper                |
| display_name          | string    | Name shown to the player. Example: Copper       |
| source_ore_block      | string    | Main ore block placed in deposits               |
| processed_item        | string    | Item received after processing billets          |
| deepslate_ore_block   | string    | Optional deepslate version of the ore           |
| deepslate_below_y     | integer   | Y-level where deepslate ore starts being used   |
| --------------------- | --------- | ----------------------------------------------- |

### Model Data Fields
``` text
GeoVein uses custom_model_data for ore chunk and billet textures.

"chunk_model_data_base": 1000,
"billet_model_data": 1100

Ore chunks use:
chunk_model_data_base + grade index
```
### Grade indexes:
| Grade    | Index   |
|----------|---------|    
| Poor     | 0       |
| Common   | 1       |
| Rich     | 2       |
| Native   | 3       |
| -------- | ------- |
```text
Example for copper:
"chunk_model_data_base": 1000

Gives:
Poor Copper Chunk   = 1000
Common Copper Chunk = 1001
Rich Copper Chunk   = 1002
Native Copper Chunk = 1003

Billets use one exact value:
"billet_model_data": 1100

Example:
Copper Ore Billet = 1100
```
| Ore                 |   Chunk Base |   Billet |
|---------------------|-------------:|---------:|
| Copper              |       `1000` |   `1100` |
| Iron                |       `2000` |   `2100` |
| Gold                |       `3000` |   `3100` |
| Coal                |       `4000` |   `4100` |
| Emerald             |       `5000` |   `5100` |
| Default / Unknown   |       `9000` |   `9100` |
| ------------------- | -----------: | -------: |
### World Generation Fields
| Field                       | Type      | Description                                 |
|-----------------------------|-----------|---------------------------------------------|
| `shape`                     | string    | Deposit shape                               |
| `min_y`                     | integer   | Lowest possible deposit centre              |
| `max_y`                     | integer   | Highest possible deposit centre             |
| `length`                    | integer   | Deposit length                              |
| `width`                     | integer   | Deposit width                               |
| `height`                    | integer   | Deposit height                              |
| `density`                   | decimal   | How filled-in the deposit is                |
| `region_size_chunks`        | integer   | Size of the generation region               |
| `spawn_chance_per_region`   | decimal   | Chance of one deposit spawning per region   |
| --------------------------- | --------- | ------------------------------------------- |   
### Deposit Shapes
| Shape       | Use                         |
|-------------|-----------------------------|
| `ellipsoid` | Good general ore deposit    |
| `sphere`    | Round ore body              |
| `cone`      | Tapered deposit             |
| `sheet`     | Flat layer-style deposit    |
| `pipe`      | Vertical pipe-style deposit |
| ----------- | --------------------------- |
```text
WARNING I have not Personally Tested any Generation except Ellipsoid, Tho the others have been coded in with values just not tested.
```
### Density
```text
Density controls how much ore appears inside the deposit.

Example:
"density": 0.35

Higher values make deposits more solid, Lower values make deposits more scattered,
The centre of a deposit is always denser than the edge.
```
### Region Size and Spawn Chance
```text
These two fields control how common deposits are.

"region_size_chunks": 20,
"spawn_chance_per_region": 0.45
```
#### region_size_chunks
```text
This controls how large each deposit spawn region is.

Lower value:
more possible deposits closer together

Higher value:
fewer possible deposits farther apart
```
#### spawn_chance_per_region
```text
This controls the chance of a deposit spawning in each region.

"spawn_chance_per_region": 0.45

Means each region has a 45% chance to contain a deposit.
```
### Drop Settings
```text
Ore drops are controlled by the drops object.

"drops": {
"min_chunks_per_block": 4,
"max_chunks_per_block": 4,
"grade_bands": []
}
```
#### Chunks Per Block
```text
"min_chunks_per_block": 4,
"max_chunks_per_block": 4

This means every mined ore block drops exactly 4 chunks.

For random drops, use different values:
"min_chunks_per_block": 2,
"max_chunks_per_block": 6
```
#### Grade Bands
```text
Grade bands control what grade of chunk drops depending on distance from the deposit centre.
Think of each iteration of this part of the JSON as details for a where(max_distance), and what(Grades poor->native) is in a Ring Round a planet, 
that idea just Round a central block in a ore deposit, Use the Example Above as A Guide, But WARNING, i have not personally tested More than 4 'Rings',
Please Report any Bugs :).
{
"max_distance": 0.25,
"poor": 0.0833,
"common": 0.0833,
"rich": 0.0834,
"native": 0.75
}
```
#### Max Distance
```text
max_distance uses a value from 0.0 to 1.0.

0.0 = deposit centre
1.0 = deposit edge

Example meaning:

0.25 = inner core
0.50 = inner area
0.75 = outer area
1.00 = edge

```
#### Grades
```text
The grade chances should usually add up to 1.0.

Example:

"poor": 0.25,
"common": 0.50,
"rich": 0.20,
"native": 0.05

Means:

25% Poor
50% Common
20% Rich
5% Native
```
### Adding a New Ore Deposit
```text
To add a new ore, you need:
1. An ore definition JSON
2. Ore chunk textures
3. Ore chunk model JSONs
4. Ore billet texture
5. Ore billet model JSON
6. Model overrides
7. Recipes

No Java changes are required.
```
### Example: Adding Emerald
#### 1. Add the Ore Definition
```text
Create A .JSON File Here:
data/geovein/ore_definitions/emerald.json

Example Content:
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
    "max_y": 80,
    "length": 120,
    "width": 45,
    "height": 30,
    "density": 0.25,
    "region_size_chunks": 28,
    "spawn_chance_per_region": 0.25,
    
    "drops": {
        "min_chunks_per_block": 3,
        "max_chunks_per_block": 4,
        "grade_bands": [
            {
            "max_distance": 0.25,
            "poor": 0.05,
            "common": 0.10,
            "rich": 0.20,
            "native": 0.65
            },
            {
            "max_distance": 0.50,
            "poor": 0.10,
            "common": 0.20,
            "rich": 0.60,
            "native": 0.10
            },
            {
            "max_distance": 0.75,
            "poor": 0.20,
            "common": 0.70,
            "rich": 0.08,
            "native": 0.02
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
#### 2. Add Ore Chunk Textures
```text
Place textures here:
assets/geovein/textures/item/ore_chunks/emerald/poor.png
assets/geovein/textures/item/ore_chunks/emerald/common.png
assets/geovein/textures/item/ore_chunks/emerald/rich.png
assets/geovein/textures/item/ore_chunks/emerald/native.png

Textures should be 16x16 PNG files with transparency.
```
#### 3. Add Ore Chunk Models
```text
Create The Following .JSON Files:
assets/geovein/models/item/ore_chunks/emerald/poor.json
assets/geovein/models/item/ore_chunks/emerald/common.json
assets/geovein/models/item/ore_chunks/emerald/rich.json
assets/geovein/models/item/ore_chunks/emerald/native.json

Example Content:
Each Pointing to there Respective names ie geovein:item/ore_chunks/emerald/poor or geovein:item/ore_chunks/emerald/native

{
    "parent": "minecraft:item/generated",
    "textures": {
        "layer0": "geovein:item/ore_chunks/emerald/poor"
    }
}
```

#### 4. Add Ore Chunk Model Overrides
```text
Open or override:
assets/geovein/models/item/ore_chunk.json

Add:

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
#### 5. Add Ore Billet Texture
```text
The billet should not look like a A rough lump, pressed slab, puck, or compacted ore piece works better.

Place The Texture With the name of the material it is:
assets/geovein/textures/item/ore_billets/emerald.png
```
#### 6. Add Ore Billet Model
```text
Create:
assets/geovein/models/item/ore_billets/emerald.json

Example Of What to Add Within the File:
{
    "parent": "minecraft:item/generated",
    "textures": {
        "layer0": "geovein:item/ore_billets/emerald"
    }
}
````
#### 7. Add Ore Billet Override
```text
Open or override:
assets/geovein/models/item/ore_billet.json

Example Of What to Add Within the File:

{
    "predicate": {
        "custom_model_data": 5100
    },
    "model": "geovein:item/ore_billets/emerald"
}
NOTE Insure that the '"custom_model_data": 5100' Number at the end is Unique Ore else it will Not Work as intended 1100,2100,3100,4100 are Take By Default.
```
#### 8. Add Recipes
```text
Create ore chunk to billet recipes and billet processing recipes.

Example File Structure:
data/geovein/recipe/crafting_emerald_poor_chunks_to_billet.json
data/geovein/recipe/crafting_emerald_common_chunk_to_billet.json
data/geovein/recipe/crafting_emerald_rich_chunks_to_billets.json
data/geovein/recipe/crafting_emerald_native_chunk_to_billets.json
data/geovein/recipe/crafting_emerald_ore_billet_to_emerald.json
```
##### Example Recipe: Poor Emerald Chunks to Billet
```text
{
    "type": "minecraft:crafting_shapeless",
    "category": "misc",
    "ingredients": [
    {
        "type": "neoforge:components",
        "items": "geovein:ore_chunk",
        "strict": false,
        "components": {
            "geovein:ore_id": "emerald",
            "geovein:ore_grade": "POOR"
        }
    },
    {
        "type": "neoforge:components",
        "items": "geovein:ore_chunk",
        "strict": false,
        "components": {
            "geovein:ore_id": "emerald",
            "geovein:ore_grade": "POOR"
        }
    },
       {
        "type": "neoforge:components",
        "items": "geovein:ore_chunk",
        "strict": false,
        "components": {
            "geovein:ore_id": "emerald",
            "geovein:ore_grade": "POOR"
        }
    },
        {
        "type": "neoforge:components",
        "items": "geovein:ore_chunk",
        "strict": false,
        "components": {
            "geovein:ore_id": "emerald",
            "geovein:ore_grade": "POOR"
        }
    }
    ],
    "result": {
    "id": "geovein:ore_billet",
    "count": 1,
    "components": {
        "geovein:ore_id": "emerald",
        "minecraft:custom_model_data": 5100
    }
  }
```

##### Example Recipe: Common Emerald Chunk to Billet
```text
{
    "type": "minecraft:crafting_shapeless",
    "category": "misc",
    "ingredients": [
    {
    "type": "neoforge:components",
    "items": "geovein:ore_chunk",
    "strict": false,
    "components": {
        "geovein:ore_id": "emerald",
        "geovein:ore_grade": "COMMON"
        }
    }
    ],
    "result": {
    "id": "geovein:ore_billet",
    "count": 1,
    "components": {
        "geovein:ore_id": "emerald",
        "minecraft:custom_model_data": 5100
        }
    }
}
NOTE, Make sure you Set the correct '"minecraft:custom_model_data": 5100' for the Billet Or the Texture Will be Wrong.
```


## Final Notes
Please Share any Bugs Features or Improvements I can do, Whatever it is. Also keep in mind this is my first Mod on Minecraft, though i hope it can get used alot.