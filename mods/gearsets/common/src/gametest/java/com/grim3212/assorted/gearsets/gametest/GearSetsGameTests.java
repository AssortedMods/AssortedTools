package com.grim3212.assorted.gearsets.gametest;

import net.minecraft.gametest.framework.GameTestHelper;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Automated in-world checks for Assorted Gear Sets. The tests live in small {@code <Feature>Tests} classes;
 * this only lists them. Each name needs a matching {@code data/assortedgearsets/test_instance/<name>.json}.
 */
public final class GearSetsGameTests {

    private GearSetsGameTests() {
    }

    /** Every test in this mod, named once, so both loaders register the same set. */
    public static void forEach(BiConsumer<String, Consumer<GameTestHelper>> out) {
        AssetTests.register(out);
        CrossLoaderDataTests.register(out);
        AliasTests.register(out);
        MigrationTests.register(out);
        MaterialSetTests.register(out);
        SpearTests.register(out);
        MiningToolTests.register(out);
        FamilyTests.register(out);
    }
}
