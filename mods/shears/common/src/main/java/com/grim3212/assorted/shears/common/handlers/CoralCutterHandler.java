package com.grim3212.assorted.shears.common.handlers;

import com.grim3212.assorted.lib.events.CorrectToolForDropEvent;
import com.grim3212.assorted.lib.events.OnDropStacksEvent;
import com.grim3212.assorted.shears.api.ShearsTags;
import com.grim3212.assorted.shears.common.enchantment.ShearsEnchantments;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public class CoralCutterHandler {
    public static void handleDrop(final OnDropStacksEvent event) {
        // We only will ever modify corals, and only for a tool carrying Coral Cutter: any other tool keeps its own drops.
        if (event.getState().is(ShearsTags.ALL_CORALS) && ShearsEnchantments.hasCoralCutter(event.getStack())) {
            ServerLevel level = event.getWorld();
            ItemStack toolStack = event.getStack();

            // Enchantments::* are ResourceKeys, so reaching the Holder needs a registry lookup.
            Holder<Enchantment> silkTouch = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH);

            // Silk touch will handle this already
            if (toolStack.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY).getLevel(silkTouch) > 0) {
                return;
            }

            // A block without loot names BuiltInLootTables.EMPTY rather than having no table.
            ResourceKey<LootTable> lootTableKey = event.getState().getBlock().getLootTable();
            if (lootTableKey == BuiltInLootTables.EMPTY) {
                return;
            }

            ItemStack fakeTool = toolStack.copy();
            fakeTool.enchant(silkTouch, 1);
            LootParams fakeContext = new LootParams.Builder(level)
                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(event.getPos()))
                    .withParameter(LootContextParams.BLOCK_STATE, event.getState())
                    .withParameter(LootContextParams.TOOL, fakeTool)
                    .create(LootContextParamSets.BLOCK);
            LootTable loottable = level.getServer().reloadableRegistries().getLootTable(lootTableKey);
            List<ItemStack> droppedItems = loottable.getRandomItems(fakeContext);
            event.setDrops(droppedItems);
        }
    }

    public static void handleCorrectTool(final CorrectToolForDropEvent event) {
        ItemStack stack = event.getStack();
        BlockState state = event.getState();

        if (ShearsEnchantments.hasCoralCutter(stack) && state.is(ShearsTags.ALL_CORALS)) {
            event.setResponse(Optional.of(true));
        }
    }
}
