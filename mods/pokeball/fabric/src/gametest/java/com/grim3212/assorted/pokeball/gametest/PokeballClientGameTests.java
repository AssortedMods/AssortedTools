package com.grim3212.assorted.pokeball.gametest;

import com.grim3212.assorted.pokeball.common.item.CapturedEntity;
import com.grim3212.assorted.pokeball.common.item.PokeballDataComponents;
import com.grim3212.assorted.pokeball.common.item.PokeballItems;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * The pokeball's tooltip as Fabric builds it, which only happens on the client; the server gametests cover
 * NeoForge. Run with {@code ./gradlew :pokeball:fabric:runClientGameTest}; it exits non-zero on a failure.
 */
public class PokeballClientGameTests implements FabricClientGameTest {

    @Override
    public void runTest(ClientGameTestContext context) {
        // An ItemStack cannot be made on the title screen: default components only bind once a world's registries load.
        try (TestSingleplayerContext world = context.worldBuilder().create()) {
            context.runOnClient(client -> {
                List<String> empty = tooltipKeys(client, new ItemStack(PokeballItems.POKEBALL.get()));
                if (!empty.contains("tooltip.pokeball.empty")) {
                    throw new AssertionError("an empty pokeball's tooltip is " + empty);
                }

                CompoundTag cow = new CompoundTag();
                cow.putString("id", "minecraft:cow");
                cow.putString("pokeball_name", "entity.minecraft.cow");
                ItemStack full = new ItemStack(PokeballItems.POKEBALL.get());
                full.set(PokeballDataComponents.CAPTURED_ENTITY.get(), new CapturedEntity(cow));
                List<String> stored = tooltipKeys(client, full);
                if (!stored.contains("tooltip.pokeball.stored")) {
                    throw new AssertionError("a full pokeball's tooltip is " + stored);
                }
            });
        }
    }

    private static List<String> tooltipKeys(Minecraft client, ItemStack stack) {
        return stack.getTooltipLines(Item.TooltipContext.of(client.level), client.player, TooltipFlag.NORMAL).stream()
                .map(line -> line.getContents() instanceof TranslatableContents translatable ? translatable.getKey() : line.getString())
                .toList();
    }
}
