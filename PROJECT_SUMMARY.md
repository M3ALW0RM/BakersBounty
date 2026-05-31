# Baker's Bounty — Project Summary

> **Note:** This document summarizes the *design vision and planned features* drawn from the original design documents. The actual codebase may have diverged from these plans — features may have been added, cut, simplified, or reimplemented differently during development. Treat this as context for understanding intent, not as a specification of what currently exists in the code.

## What Is Baker's Bounty?

Baker's Bounty (originally called "Wheat & Bread Overhaul") is a NeoForge mod for Minecraft 1.21.1 that replaces the vanilla wheat-to-bread pipeline with a multi-step grain processing and baking system. The goal is to make food production feel like a meaningful craft — something worth investing in — without punishing players who don't engage with it. Vanilla Minecraft's baseline is never reduced; the mod only adds upward from there.

The mod is the foundational piece of a larger planned ecosystem of interconnected food mods (dairy, meat, vegetables, beverages, etc.), but it is designed to stand alone as a complete experience.

**Mod ID:** `bakersbounty`
**Package:** `com.mcmodders.bakersbounty`
**Platform:** NeoForge for Minecraft 1.21.1

## Core Design Philosophy

- **Additive, not punitive** — Players are never worse off than vanilla. All bonuses stack on top of the baseline.
- **"Realism is the enemy of fun"** — Mechanics are inspired by real baking but prioritized for gameplay satisfaction.
- **"It's about the journey, not the destination"** — The process of making bread should be engaging, not just the end product.
- **"Food is meant to be shared"** — Systems encourage multiplayer cooperation and trade.
- **Every addition has at least two uses** — Items and tools are multi-functional (e.g., the Ground Stone is both a grinding tool and a throwable projectile; the flail is a threshing tool and a weapon).

## Planned Feature Set

### 1. Wheat & Seed Overhaul

Wheat plants no longer drop wheat items — only seeds. This creates a processing chain where seeds must be ground into flour before bread can be made.

- **Ancient Wheat** — A wild grain grown from seeds dropped by grass. Lower yield (~2 seeds), serves as the entry point to the system. Has a small chance (1/8) of producing regular wheat seeds, creating a breeding/domestication progression.
- **Standard Wheat** — Higher yield (~4 seeds on average, with 4–7 from threshing). The staple crop.
- **Straw Bundles** — Drop alongside seeds from harvested wheat. Replace wheat in vanilla recipes like packed mud and straw bales.
- **Threshing** — A flail tool is used to thresh harvested wheat, yielding seeds. The flail doubles as a combat weapon.

The math preserves the "3 wheat plants per bread" principle: 3 plants yield ~12 seeds, 9 go to dough, 3 are replanted.

### 2. Flour Processing (Tiered Grinding)

Seeds are ground into flour using progressively better tools. Each tier produces a higher quality flour.

| Tool | Flour Output | Stage |
|---|---|---|
| Hand Grind (Ground Stone) | Gritty Flour | Early game |
| Quern (block) | Coarse Flour | Mid game |
| Hand-Cranked Mill | Fine Flour | Late game (post-MVP) |

- **Ground Stone** — A reusable item. Also functions as a throwable projectile.
- **Quern** — A placeable block with click-to-process mechanics (4 right-clicks per rotation). Uses an abstract class system designed for easy creation of new processor blocks.
- **Mill** — Planned third tier for batch processing. Targeted for post-MVP.
- **Flour Bags** — 9 flour can be batched into 1 flour bag for compact storage and use in dough recipes.

### 3. Dough & Bread System

Flour bags combine with water (and optionally yeast/mushrooms) to produce dough, which is then baked.

**Dough types:**
- **Flatbread Dough** — Flour + water. Simple, early-game.
- **Yeasted Bread Dough** — Flour + water + yeast. Requires sourdough starter infrastructure.

**Bread varieties (6 planned):** Vary based on flour quality × dough type, producing a matrix of bread options with increasing hunger/saturation values.

**Bread tiers:**
- **Tier 1 (Raw/Boiled):** Cooked wild grain, gruel, porridge — minimal processing.
- **Tier 2 (Unleavened):** Flatbreads and hardtack — baked but no yeast. Hardtack is stackable travel food.
- **Tier 3 (Leavened):** Full loaves — best nutrition, lower stack size, requires yeast infrastructure.

### 4. Bread Oven

A custom block for baking dough into bread, replacing furnace-based cooking.

**Planned features:**
- Wood fuel system with temperature management (0–400°C range).
- Batch processing for multiple dough blocks.
- Multiblock upgrade system — additional blocks for wood storage, extra baking slots.
- Potential specialized variants: Rack Oven, Tandoor Oven, Stone Hearth Oven, Industrial Conveyor.

### 5. Sourdough Starter & Yeast

- **Starter Jar** — Craftable block maintained by feeding flour + water.
- Produces yeast items required for leavened bread.
- Neglecting the starter causes decay after 3 in-game days.

### 6. Loot Table Integration

Naturally spawning chest loot is modified to include hardtack instead of vanilla bread:
- Villages: Coarse hardtack
- Shipwrecks: Fine hardtack
- Dungeons/Strongholds: Gritty hardtack

### 7. Nutrition Commitment System (Design Phase)

A planned buff system using visual wheat sheaf icons with Roman numerals to provide tiered food bonuses. Designed with colorblind accessibility in mind.

### 8. Oinkorn (Nether Content)

A Nether tree nut that serves as the staple food for Piglins. Features a boar-tusk-adorned shell with a comedic pig-hoof-shaped interior flesh. Designed with a detailed 16×16 item texture spec and 3D model using cube primitives with rotated elements for an organic look.

## Broader Ecosystem Vision

Baker's Bounty is designed as the first mod in a planned suite of interconnected food mods:

1. **Wheat & Bread** (Baker's Bounty) — this mod
2. **Dairy & Fermentation** — milk processing, cheese aging, butter churning
3. **Meat Processing & Preservation** — butchering, smoking, curing, sausage making
4. **Vegetable Cultivation & Preservation** — seasonal crops, pickling, canning
5. **Fisheries & Aquaculture** — fish breeding, large fish butchering, fish sauce
6. **Foraging & Wild Harvest** — seasonal foraging, rare wild ingredients
7. **Cooking & Kitchen Equipment** — specialized stations, cookware, active cooking
8. **Beverages & Brewing** — beer, wine, mead, spirits, teas
9. **Spices, Herbs & Seasonings** — flavor multipliers, regional spice systems

Each mod is designed to stand alone but unlock synergies when combined (e.g., Bread + Dairy enables butter-enriched breads; Bread + Meat enables meat pies). The full ecosystem aims to support multiplayer specialization where players develop food-based professions and trade.

## Development Context

- **Team:** Benji (systems/logic) and Thomas (GUI/testing/assets), both learning Java and Minecraft modding during development.
- **Approach:** Levels of Quality (LOQ) framework — features progress through Basic Function → Correct Implementation → Balanced & Complete, with no regressions between levels.
- **Technical patterns:** DeferredRegister setup, comprehensive data generation (preferred over manual JSON), modern NeoForge APIs (HolderLookup.Provider for NBT, codec-based block properties, ItemDisplayContext). Custom Gradle task chain: `runData` → `build` → `runClient`.
- **Key NeoForge learnings:** Singular `loot_table` folder (not `loot_tables`), recipe JSON uses `"id"` not `"item"` in results, data generation is strongly preferred over hand-written JSON.
