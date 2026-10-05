package com.grim3212.assorted.buckets.gametest;

import net.minecraft.gametest.framework.GameTestHelper;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Automated in-world checks for Assorted Buckets. The tests live in small {@code <Feature>Tests} classes;
 * this only lists them. Each name needs a matching {@code data/assortedbuckets/test_instance/<name>.json}.
 */
public final class BucketsGameTests {

    private BucketsGameTests() {
    }

    /** Every test in this mod, named once, so both loaders register the same set. */
    public static void forEach(BiConsumer<String, Consumer<GameTestHelper>> out) {
        AssetTests.register(out);
        CrossLoaderDataTests.register(out);
        AliasTests.register(out);
        MigrationTests.register(out);
        BucketTests.register(out);
        BucketTooltipTests.register(out);
        BucketTagTests.register(out);
        FamilyTests.register(out);
    }
}
