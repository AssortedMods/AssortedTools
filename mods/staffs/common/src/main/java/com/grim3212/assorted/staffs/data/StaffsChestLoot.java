package com.grim3212.assorted.staffs.data;

import com.grim3212.assorted.staffs.Constants;
import com.grim3212.assorted.staffs.common.item.StaffsItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

/** The rare staff pools, injected into vanilla chest loot by {@code StaffsLootHandlers}. */
public class StaffsChestLoot implements LootTableSubProvider {

    public StaffsChestLoot(HolderLookup.Provider registries) {
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        // A staff in about one chest in fifty of those StaffsLootHandlers adds it to.
        staff(output, "staffs_neptune_loot", StaffsItems.NEPTUNE_STAFF.get());
        staff(output, "staffs_phoenix_loot", StaffsItems.PHOENIX_STAFF.get());
        staff(output, "staffs_power_loot", StaffsItems.POWER_STAFF.get());
    }

    private void staff(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output, String name, ItemLike staff) {
        LootPool.Builder pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(addItem(staff, 1, 1, 1)).add(EmptyLootItem.emptyItem().setWeight(49));
        output.accept(key(name), LootTable.lootTable().withPool(pool));
    }

    /** A weighted entry dropping between {@code min} and {@code max} of {@code item}. */
    private LootPoolSingletonContainer.Builder<?> addItem(ItemLike item, int weight, int min, int max) {
        return LootItem.lootTableItem(item).setWeight(weight).apply(SetItemCountFunction.setCount(UniformGenerator.between(min, max)));
    }

    private static ResourceKey<LootTable> key(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, path));
    }
}
