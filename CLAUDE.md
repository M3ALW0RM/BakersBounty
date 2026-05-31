# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Baker's Bounty** is a NeoForge mod for Minecraft 1.21.1 that adds a tiered grain-processing and baking system. It is additive-only — vanilla is never worse off.

- **Mod ID:** `bakersbounty`
- **Package:** `com.mcmodders.bakersbounty`
- **Platform:** NeoForge 21.1.209, Java 21

## Build Commands

```powershell
# Generate data files (recipes, loot tables) into src/generated/resources/
./gradlew runData

# Build the mod jar
./gradlew build

# Launch the game client for in-game testing
./gradlew runClient

# Launch a headless dedicated server
./gradlew runServer

# Refresh dependencies if the IDE has missing libraries
./gradlew --refresh-dependencies
```

The standard dev cycle is `runData` → `build` → `runClient`. The `src/generated/resources/` folder is auto-included in the build and should be committed to git.

## Architecture

### Entry Points

- **`BakersBounty.java`** — Main `@Mod` class. Wires all registries to the mod event bus and registers the common config.
- **`BakersBountyClient.java`** — `@Mod(dist = Dist.CLIENT)` class. Registers the config screen. Client-only setup (render layers) lives in `ClientEvents.java`.

### Registry Pattern

All content is registered via `DeferredRegister` instances defined as `public static final` fields in the `registry/` package:

| Class | What it registers |
|---|---|
| `ModBlocks` | Blocks |
| `ModItems` | Items + the `CREATIVE_MODE_TABS` deferred register |
| `ModBlockEntities` | Block entity types |
| `ModRecipes` | Recipe serializers and recipe types |
| `ModLootModifiers` | Global loot modifier serializer codecs |

Each registry class has a `register(IEventBus)` method called from `BakersBounty`'s constructor.

### Block + Block Entity Pattern

Blocks that need state go in `blocks/`, block entities in `blockentities/`. The Quern is the reference implementation:

- `QuernBlock` extends `BaseEntityBlock`, implements `MapCodec`, holds a `FACING` `DirectionProperty`, and handles `useWithoutItem` interaction to drive the block entity.
- `QuernBlockEntity` stores `inputItem`, `outputItem`, and `grindingProgress`. It uses `HolderLookup.Provider` in `saveAdditional`/`loadAdditional` and sends client updates via `ClientboundBlockEntityDataPacket`.
- `QuernBlockEntityRenderer` renders the floating item above the block. Registered in `ClientEvents.registerRenderers`.

### Custom Recipe Type Pattern

`QuernRecipe` is the reference for adding new machine recipe types:

1. **Recipe class** (`recipes/QuernRecipe.java`) — implements `Recipe<SingleRecipeInput>`, has an inner `Type` singleton and an inner `Serializer` with `MapCodec` and `StreamCodec`.
2. **Registry entries** (`registry/ModRecipes.java`) — registers the serializer and type via `DeferredRegister`.
3. **Recipe builder** (`datagen/QuernRecipeBuilder.java`) — implements `RecipeBuilder` for use inside `ModRecipeProvider`.

### Data Generation

`DataGenerators.java` is annotated with `@EventBusSubscriber` and registers providers on `GatherDataEvent`. Providers output to `src/generated/resources/`.

- **Recipes:** `ModRecipeProvider` (extends `RecipeProvider`). Use `QuernRecipeBuilder.grinding(...)` for quern recipes; use standard NeoForge builders for crafting/shapeless.
- **Loot tables:** `ModBlockLootTableProvider`.
- Loot modifier JSONs (`src/main/resources/data/bakersbounty/loot_modifiers/`) are hand-written, not generated.

### Loot Modifiers

`SwapLootModifier` replaces one item with another in any loot table. It is codec-based (`MapCodec`) and registered in `ModLootModifiers`. Active modifiers are listed in `data/neoforge/loot_modifiers/global_loot_modifiers.json`.

### Client / Server Split

- Server-only logic stays in `blocks/`, `blockentities/`, `recipes/`, `loot/`.
- Client-only code lives under `client/` and is guarded by `@EventBusSubscriber(value = Dist.CLIENT)` or `@Mod(dist = Dist.CLIENT)`.

## Critical NeoForge Gotchas

- Loot table output folder is `loot_table` (singular), not `loot_tables`.
- Recipe JSON result field is `"id"`, not `"item"`.
- NBT serialization requires `HolderLookup.Provider` — never use the old `CompoundTag` APIs that don't take a registries parameter.
- Block `MapCodec` must be implemented and returned from `codec()`.
- Always prefer data generation over hand-written JSON.

## Current Feature State

| Feature | Status |
|---|---|
| Einkorn crop (`EinkornBlock`) | Implemented |
| Ground Stone item (crafting remainder) | Implemented |
| Quern block + block entity + renderer | Implemented |
| Flour items (gritty, coarse) + flour sacs | Implemented |
| Flatbread items + dough items | Implemented |
| QuernRecipe custom recipe type | Implemented |
| SwapLootModifier (grass → einkorn seeds) | Implemented |
| Creative tab with all items | Implemented |
| Wheat threshing recipe | Implemented (shapeless) |
| Mill, Bread Oven, Sourdough Starter | Not yet started |