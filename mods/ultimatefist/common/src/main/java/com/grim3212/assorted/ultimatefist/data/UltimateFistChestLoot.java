package com.grim3212.assorted.ultimatefist.data;

import com.grim3212.assorted.ultimatefist.common.handlers.FragmentLootHandler;
import com.grim3212.assorted.ultimatefist.common.item.UltimateFistItems;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

/** The three fragment pools {@link FragmentLootHandler} adds to vanilla's chests. */
public class UltimateFistChestLoot implements LootTableSubProvider {

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, Builder> builder) {
        LootPool.Builder overworldPool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(fragment(UltimateFistItems.U_FRAGMENT.get())).add(fragment(UltimateFistItems.L_FRAGMENT.get()))
                .add(fragment(UltimateFistItems.T_FRAGMENT.get())).add(EmptyLootItem.emptyItem().setWeight(10));
        builder.accept(FragmentLootHandler.OVERWORLD_UF_LOOT, LootTable.lootTable().withPool(overworldPool));

        LootPool.Builder netherPool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(fragment(UltimateFistItems.I_FRAGMENT.get())).add(fragment(UltimateFistItems.M_FRAGMENT.get()))
                .add(EmptyLootItem.emptyItem().setWeight(15));
        builder.accept(FragmentLootHandler.NETHER_UF_LOOT, LootTable.lootTable().withPool(netherPool));

        LootPool.Builder endPool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(fragment(UltimateFistItems.A_FRAGMENT.get())).add(fragment(UltimateFistItems.MISSING_FRAGMENT.get()))
                .add(fragment(UltimateFistItems.E_FRAGMENT.get())).add(EmptyLootItem.emptyItem().setWeight(15));
        builder.accept(FragmentLootHandler.END_UF_LOOT, LootTable.lootTable().withPool(endPool));
    }

    private static LootPoolEntryContainer.Builder<?> fragment(ItemLike item) {
        return LootItem.lootTableItem(item).setWeight(1).apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 1)));
    }
}
