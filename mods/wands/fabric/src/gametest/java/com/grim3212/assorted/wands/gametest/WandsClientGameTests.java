package com.grim3212.assorted.wands.gametest;

import com.grim3212.assorted.lib.client.key.ModeSwitchKey;
import com.grim3212.assorted.lib.util.NBTHelper;
import com.grim3212.assorted.wands.common.item.WandsItems;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.Arrays;
import java.util.List;

/**
 * The wand's tooltip as Fabric builds it, which only happens on the client, and Lib's switch-modes key, which wands
 * turn on. Run with {@code ./gradlew :wands:fabric:runClientGameTest}; it exits non-zero on a failure.
 */
public class WandsClientGameTests implements FabricClientGameTest {

    @Override
    public void runTest(ClientGameTestContext context) {
        try (TestSingleplayerContext world = context.worldBuilder().create()) {
            context.runOnClient(client -> {
                ItemStack wandStack = new ItemStack(WandsItems.MINING_WAND.get());
                NBTHelper.putString(wandStack, "Mode", "mineall");
                List<String> wand = wandStack.getTooltipLines(Item.TooltipContext.of(client.level), client.player, TooltipFlag.NORMAL).stream()
                        .map(line -> line.getContents() instanceof TranslatableContents translatable ? translatable.getKey() : line.getString())
                        .toList();
                if (!wand.contains("assortedtools.wand.current")) {
                    throw new AssertionError("a mining wand's tooltip is " + wand);
                }
            });

            modeKeyCyclesTheHeldWand(context, world);
        }
    }

    /** One key in Controls, and pressing it cycles the wand in hand on the server. */
    private static void modeKeyCyclesTheHeldWand(ClientGameTestContext context, TestSingleplayerContext world) {
        context.runOnClient(client -> {
            long keys = Arrays.stream(client.options.keyMappings).filter(key -> key.getName().equals("key.assortedlib.switch_modes")).count();
            if (keys != 1 || ModeSwitchKey.key() == null) {
                throw new AssertionError("expected one switch-modes key, found " + keys);
            }
        });

        world.getServer().runOnServer(server -> {
            ItemStack wand = new ItemStack(WandsItems.MINING_WAND.get());
            NBTHelper.putString(wand, "Mode", "mineall");
            server.getPlayerList().getPlayers().getFirst().setItemInHand(InteractionHand.MAIN_HAND, wand);
        });
        context.waitFor(client -> client.player.getMainHandItem().is(WandsItems.MINING_WAND.get()));

        context.getInput().pressKey(ModeSwitchKey.key());
        try {
            context.waitFor(client -> !NBTHelper.getString(client.player.getMainHandItem(), "Mode").equals("mineall"), 100);
        } catch (AssertionError e) {
            throw new AssertionError("pressing the switch-modes key did not change the held wand's mode", e);
        }
    }
}
