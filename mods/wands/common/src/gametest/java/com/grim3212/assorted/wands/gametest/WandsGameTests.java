package com.grim3212.assorted.wands.gametest;

import net.minecraft.gametest.framework.GameTestHelper;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Automated in-world checks for Assorted Wands. The tests live in small {@code <Feature>Tests} classes;
 * this only lists them. Each name needs a matching {@code data/assortedwands/test_instance/<name>.json}.
 */
public final class WandsGameTests {

    private WandsGameTests() {
    }

    /** Every test in this mod, named once, so both loaders register the same set. */
    public static void forEach(BiConsumer<String, Consumer<GameTestHelper>> out) {
        AssetTests.register(out);
        CrossLoaderDataTests.register(out);
        AliasTests.register(out);
        MigrationTests.register(out);
        WandTests.register(out);
        WandTooltipTests.register(out);
        FamilyTests.register(out);
    }
}
