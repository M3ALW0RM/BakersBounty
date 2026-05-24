---
name: run-client
description: Compile Baker's Bounty and launch the Minecraft client. Use when asked to compile, build, run the client, test in-game, or verify mod changes work in Minecraft.
---

## Standard Dev Cycle

The full cycle is data generation → build → client launch. Run all three when recipes, loot tables, or block models have changed:

```powershell
cd E:\MinecraftModding\BakersDelight
./gradlew runData
./gradlew build
./gradlew runClient
```

For code-only changes (no data changes), skip `runData`:

```powershell
./gradlew runClient
```

`runClient` compiles and launches in one step — no separate compile command needed.

## Notes

- The client takes 1–3 minutes to reach the Minecraft main menu.
- Run output streams to the terminal; Minecraft opens as a separate window.
- `runData` writes to `src/generated/resources/` — commit those files.
- If dependencies are missing or the IDE shows red, run `./gradlew --refresh-dependencies` first.
