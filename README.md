# Assorted Tools

An assortment of tools and armor. Each group of tools is also its own mod if you only want some of them.

- [Assorted Tools](mods/tools) has all of them in one download
- [Assorted Gear Sets](mods/gearsets) adds tools, armor and spears in tin, steel, ruby and more
- [Assorted Hammers](mods/hammers) adds hammers that break any block in one hit
- [Assorted Multitools](mods/multitools) adds one tool that does the work of five
- [Assorted Machetes](mods/machetes) adds machetes that cut through undergrowth
- [Assorted Shears](mods/shears) adds shears in every material and the Coral Cutter enchantment
- [Assorted Portable Workbench](mods/portableworkbench) adds a crafting table you can carry
- [Assorted Throwing Spears](mods/throwingspears) adds spears you can throw
- [Assorted Boomerangs](mods/boomerangs) adds boomerangs that come back to you
- [Assorted Buckets](mods/buckets) adds buckets that hold more than one bucket
- [Assorted Suits](mods/suits) adds the chicken, scuba and lava suits
- [Assorted Wands](mods/wands) adds wands that build, break and mine whole areas
- [Assorted Staffs](mods/staffs) adds the Neptune, Phoenix and power staffs
- [Assorted Ultimate Fist](mods/ultimatefist) adds the ultimate fist and its fragments
- [Assorted Pokeball](mods/pokeball) adds the pokeball

Worlds made with Assorted Tools 11.x work with any of these.

Requires [Assorted Lib](https://github.com/AssortedMods/AssortedLib). Branches are per Minecraft version and `26.2` is the current one.

## Issue Reporting

Please include the following

* Minecraft version
* NeoForge version, or Fabric Loader and Fabric API versions
* Which of these mods you have and their versions
* Assorted Lib version
* The full `latest.log`, and the crash report if the game crashed

## Building

You need JDK 25. Each mod is its own folder under `mods`. The build setup comes from
[AssortedBuild](https://github.com/AssortedMods/AssortedBuild) and `assortedbuild_version` in `gradle.properties`
picks the version.

To build against a local copy of Assorted Lib, publish it first.

```bash
cd ../AssortedLib && ./gradlew publishToMavenLocal
```

Some useful commands

```bash
./gradlew build                                  # build every mod
./gradlew :hammers:neoforge:runClient            # run one mod
./gradlew :all:neoforge:runClient                # run every tool together
./gradlew runGameTestServer runGameTest          # gametests on NeoForge and Fabric
./gradlew runClientData runServerData            # datagen
```

## License

[LGPL-3.0-only](LICENSE).
