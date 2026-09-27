package com.grim3212.assorted.ultimatefist.gametest;

import com.grim3212.assorted.ultimatefist.common.item.FragmentItem;
import com.grim3212.assorted.ultimatefist.common.item.UltimateFistItems;
import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest;
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext;
import net.fabricmc.fabric.api.client.gametest.v1.context.TestSingleplayerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

/**
 * A fragment's description as Fabric builds its tooltip, which only happens on the client; the server gametests cover
 * NeoForge. Run with {@code ./gradlew :ultimatefist:fabric:runClientGameTest}; it exits non-zero on a failure.
 */
public class UltimateFistClientGameTests implements FabricClientGameTest {

    @Override
    public void runTest(ClientGameTestContext context) {
        // An ItemStack cannot be made on the title screen: default components only bind once a world's registries load.
        try (TestSingleplayerContext world = context.worldBuilder().create()) {
            context.runOnClient(client -> {
                // Under its key, or as the pieces AssortedLib wraps its line into.
                List<String> fragment = tooltipKeys(client, new ItemStack(UltimateFistItems.U_FRAGMENT.get()));
                String description = Component.translatable(FragmentItem.DESCRIPTION_KEY).getString();
                String shown = String.join(" ", fragment.stream().filter(line -> !line.isEmpty() && description.contains(line)).toList());
                if (!fragment.contains(FragmentItem.DESCRIPTION_KEY) && !shown.equals(description)) {
                    throw new AssertionError("a fragment's tooltip is " + fragment);
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
