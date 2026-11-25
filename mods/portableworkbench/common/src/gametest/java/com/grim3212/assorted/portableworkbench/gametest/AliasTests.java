package com.grim3212.assorted.portableworkbench.gametest;

import com.google.gson.JsonParser;
import com.grim3212.assorted.portableworkbench.Constants;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** A world saved when this was all one mod, Assorted Tools, still has these items and entities in it. */
final class AliasTests {

    private AliasTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("assortedtools_ids_still_load", AliasTests::assortedtoolsIdsStillLoad);
    }

    private static void assortedtoolsIdsStillLoad(GameTestHelper helper) {
        BuiltInRegistries.ITEM.entrySet().stream().filter(entry -> entry.getKey().location().getNamespace().equals(Constants.MOD_ID)).forEach(entry -> {
            ResourceLocation old = old(entry.getKey().location());
            ItemStack stack = ItemStack.CODEC.parse(helper.getLevel().registryAccess().createSerializationContext(JsonOps.INSTANCE),
                    JsonParser.parseString("{\"id\": \"" + old + "\", \"count\": 1}")).getOrThrow();
            helper.assertTrue(stack.is(entry.getValue()), "a stack saved as " + old + " reads back as " + stack);
        });

        BuiltInRegistries.ENTITY_TYPE.entrySet().stream().filter(entry -> entry.getKey().location().getNamespace().equals(Constants.MOD_ID)).forEach(entry -> {
            // The id a chunk saved an entity under, read back the way the world reads it.
            CompoundTag saved = new CompoundTag();
            saved.putString("id", old(entry.getKey().location()).toString());
            helper.assertValueEqual(EntityType.by(saved).orElse(null), entry.getValue(),
                    "the entity saved as " + old(entry.getKey().location()));
        });
        helper.succeed();
    }

    private static ResourceLocation old(ResourceLocation id) {
        return ResourceLocation.fromNamespaceAndPath(Constants.FAMILY_ID, id.getPath());
    }
}
