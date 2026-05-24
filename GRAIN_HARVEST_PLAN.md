# Grain Harvest — Sickle, Sheaves, and Stooks

## Overview

This document covers the full design for sickle harvesting, sheaf items, stook blocks, and the drying mechanic. Pile building and threshing are called out as future work but are accounted for in the block/item naming so nothing needs to be renamed later.

---

## New Content

### Items

| Registry Name | Class | Notes |
|---|---|---|
| `wheat_sheaf` | `registerSimpleItem` | Dropped by wheat when harvested with sickle |
| `einkorn_sheaf` | `registerSimpleItem` | Dropped by einkorn when harvested with sickle |
| `sickle` | `SickleItem` | Durability tool, crafted from iron (TBD recipe) |
| `wheat_stook` | Block item (auto) | Placed to create `wheat_stook` block |
| `einkorn_stook` | Block item (auto) | Placed to create `einkorn_stook` block |
| `dried_wheat_stook` | `registerSimpleItem` | Dropped by dried stook block; future: placeable pile |
| `dried_einkorn_stook` | `registerSimpleItem` | Same for einkorn |

`dried_wheat_stook` and `dried_einkorn_stook` are registered as plain items now. When piling is implemented they become `BlockItem`s backed by their respective pile blocks.

### Blocks

| Registry Name | Class | Notes |
|---|---|---|
| `wheat_stook` | `StookBlock` | Wheat variant; has `DRIED` blockstate |
| `einkorn_stook` | `StookBlock` | Einkorn variant; has `DRIED` blockstate |

Future blocks (do not implement yet, names reserved):

| Registry Name | Notes |
|---|---|
| `wheat_stook_pile` | Snow-layer-style; used for threshing |
| `einkorn_stook_pile` | Same for einkorn |

---

## Sickle Item (`SickleItem`)

**Package:** `com.mcmodders.bakersbounty.items`

- Extends `Item`; uses `Item.Properties().durability(N)` (recommend 64–128 uses; exact value TBD)
- No special logic in the class itself — harvesting behaviour lives in the loot system (see below)
- The item belongs to an item tag `bakersbounty:sickles` so future sickle tiers work automatically

**Crafting recipe (shapeless or shaped TBD):** 2 iron ingots + 1 stick → 1 sickle. Exact shape to be decided and added to `ModRecipeProvider`.

---

## Sickle Harvesting — Loot Modifier Approach

Harvesting is implemented as two global loot modifiers so the vanilla wheat loot table is not patched. Both modifiers share the same `SickleLootModifier` class.

### `SickleLootModifier`

**Package:** `com.mcmodders.bakersbounty.loot`

Logic in `doApply`:

1. Check `context.getParam(LootContextParams.TOOL)` — if tool is not in `bakersbounty:sickles` tag, return loot unchanged.
2. Check `context.getParam(LootContextParams.BLOCK_STATE)` — if block age is not at max (`age == CropBlock.MAX_AGE`, i.e., 7), return loot unchanged.
3. Otherwise: clear all generated loot, return a single `ItemStack` of the configured sheaf item.

This replaces seeds + grain with just the sheaf. Seeds are recovered later via threshing (future), which is intentional — the sickle trades immediate grain/seeds for the stook → dried stook → threshing pipeline.

**Codec fields:**

```java
// Item to drop when the sickle condition is met
private final Item sheafItem;
```

### JSON Modifiers (hand-written, not generated)

`src/main/resources/data/bakersbounty/loot_modifiers/wheat_sickle_harvest.json`
```json
{
  "type": "bakersbounty:sickle_harvest",
  "conditions": [
    {
      "condition": "minecraft:block_state_property",
      "block": "minecraft:wheat",
      "properties": { "age": "7" }
    }
  ],
  "sheaf_item": "bakersbounty:wheat_sheaf"
}
```

`src/main/resources/data/bakersbounty/loot_modifiers/einkorn_sickle_harvest.json`
```json
{
  "type": "bakersbounty:sickle_harvest",
  "conditions": [
    {
      "condition": "minecraft:block_state_property",
      "block": "bakersbounty:einkorn_crop",
      "properties": { "age": "7" }
    }
  ],
  "sheaf_item": "bakersbounty:einkorn_sheaf"
}
```

Both entries are added to `global_loot_modifiers.json` alongside the existing grass entries.

Register `SickleLootModifier.CODEC` in `ModLootModifiers` the same way `SwapLootModifier.CODEC` is registered.

---

## Crafting Recipes

Both stook recipes are shapeless 2×2 crafting (added to `ModRecipeProvider`):

```
[ sheaf ][ sheaf ]       →   1× wheat_stook
[ sheaf ][ sheaf ]
```

Same pattern for einkorn sheaf → einkorn stook.

---

## Stook Block (`StookBlock`)

**Package:** `com.mcmodders.bakersbounty.blocks`

One reusable class parameterised by the item to drop when dried. Two instances are registered in `ModBlocks`.

### Block State

```java
public static final BooleanProperty DRIED = BooleanProperty.create("dried");
```

Default: `false`. Flipped to `true` by the block entity when the drying threshold is met.

### Placement

- Placed on solid surfaces (top face only); 1×1×1 full cube is fine for now
- `MapColor.COLOR_YELLOW` / straw-like appearance
- No tool requirement, low strength (~0.8f), flammable

### Drops

Override `getDrops` (or use a loot table):

- `DRIED == false` → drops the stook block item (undried; player reclaims it)
- `DRIED == true` → drops the corresponding `dried_wheat_stook` or `dried_einkorn_stook` item

Pass the drop item into the constructor so the single class handles both variants:

```java
public StookBlock(Item driedDropItem, Properties properties) { … }
```

### `createBlockStateDefinition`

```java
stateDefinition.add(DRIED);
```

---

## Stook Block Entity (`StookBlockEntity`)

**Package:** `com.mcmodders.bakersbounty.blockentities`

Tracks accumulated daylight ticks and flips `DRIED` when the threshold is reached.

### Drying Threshold

1 Minecraft day = 24 000 game ticks. Daylight (sky light level 15) is available for roughly 12 000 ticks per day. Requiring 12 000 ticks of sky-light exposure means the stook dries after approximately one full in-game day of sun.

```java
private static final int DRYING_TICKS_REQUIRED = 12_000;
private int dryingProgress = 0; // persisted in NBT
```

### Tick Logic (server-side only)

Called from a static `serverTick` method registered via `BlockEntityType` on the block. Each server tick:

1. If `blockState.getValue(StookBlock.DRIED)` is already `true`, do nothing.
2. Get sky light at the block's position: `level.getBrightness(LightLayer.SKY, worldPosition)`.
3. If sky light ≥ 15 (full daylight) and the game time is in the daytime window (optional: `level.isDay()`), increment `dryingProgress`.
4. When `dryingProgress >= DRYING_TICKS_REQUIRED`:
   - Set the block state to `DRIED = true` via `level.setBlock(...)`.
   - Call `setChanged()` to mark dirty.

### NBT Persistence

Save/load `dryingProgress` using `HolderLookup.Provider` pattern matching `QuernBlockEntity`.

### Client Sync

Use `ClientboundBlockEntityDataPacket` + `getUpdateTag` / `handleUpdateTag` as in `QuernBlockEntity`. Progress does not need to be synced to the client unless a progress bar UI is added later.

---

## Registration Checklist

### `ModBlocks`
- `WHEAT_STOOK` — `StookBlock` with `driedDropItem = ModItems.DRIED_WHEAT_STOOK`
- `EINKORN_STOOK` — `StookBlock` with `driedDropItem = ModItems.DRIED_EINKORN_STOOK`

### `ModItems`
- `WHEAT_SHEAF` — `registerSimpleItem`
- `EINKORN_SHEAF` — `registerSimpleItem`
- `SICKLE` — `register("sickle", () -> new SickleItem(...))`
- `WHEAT_STOOK` — `registerSimpleBlockItem(ModBlocks.WHEAT_STOOK)`
- `EINKORN_STOOK` — `registerSimpleBlockItem(ModBlocks.EINKORN_STOOK)`
- `DRIED_WHEAT_STOOK` — `registerSimpleItem`
- `DRIED_EINKORN_STOOK` — `registerSimpleItem`

Add all new items to the creative tab display list.

### `ModBlockEntities`
- `STOOK` — one `BlockEntityType<StookBlockEntity>` bound to both `WHEAT_STOOK` and `EINKORN_STOOK`

### `ModLootModifiers`
- `SICKLE_HARVEST` — `SickleLootModifier.CODEC`

---

## Data Generation

### `ModBlockLootTableProvider`
- `WHEAT_STOOK`: conditional on `DRIED` blockstate (see above in Drops section)
- `EINKORN_STOOK`: same

### `ModRecipeProvider`
- Shapeless 2×2: 4× `wheat_sheaf` → 1× `wheat_stook`
- Shapeless 2×2: 4× `einkorn_sheaf` → 1× `einkorn_stook`
- Sickle crafting recipe (shape TBD)

### Item / Block Models & Textures
- Sheaf items: unique textures needed
- Stook block: two blockstates (undried, dried) — can share geometry, different texture or tint
- Sickle: standard tool item texture
- Dried stook items: unique textures needed

---

## Future Work (Not Implemented Yet)

- **Dried stook piling** — `wheat_stook_pile` and `einkorn_stook_pile` blocks using `LAYERS` blockstate (1–4 or 1–8), backed by `BlockItem`-subclasses that stack onto existing pile blocks. This is where `dried_wheat_stook` and `dried_einkorn_stook` become `BlockItem`s.
- **Threshing** — interaction with the pile block (right-click with flail? auto-process over time?) to produce grain and seeds.
- **Sickle tiers** — stone/iron/gold/diamond; add them to the `bakersbounty:sickles` tag so the loot modifier picks them up automatically.
- **Stook visual drying progress** — client-side particle effects or tint shift as `dryingProgress` increases.
