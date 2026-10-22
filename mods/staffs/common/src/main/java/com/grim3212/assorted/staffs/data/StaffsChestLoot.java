package com.grim3212.assorted.staffs.data;

import com.grim3212.assorted.staffs.Constants;
import com.grim3212.assorted.staffs.common.item.StaffsItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.UniformContainerBase;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

/** The rare staff pools, injected into vanilla chest loot by {@code StaffsLootHandlers}. */
public class StaffsChestLoot implements LootTableSubProvider {

    private final LootTableSubProvider.Context output;

    public StaffsChestLoot(LootTableSubProvider.Context output) {
        this.output = output;
    }

    @Override
    public void run() {
        // A staff in about one chest in fifty of those StaffsLootHandlers adds it to.
        staff("staffs_neptune_loot", StaffsItems.NEPTUNE_STAFF.get());
        staff("staffs_phoenix_loot", StaffsItems.PHOENIX_STAFF.get());
        staff("staffs_power_loot", StaffsItems.POWER_STAFF.get());
    }

    private void staff(String name, ItemLike staff) {
        LootPool.Builder pool = LootPool.lootPool().setRolls(ContextIntProviders.exactly(1)).add(addItem(staff, 1, 1, 1)).add(EmptyLootItem.emptyItem().setWeight(49));
        this.output.accept(key(name), LootTable.lootTable().withPool(pool));
    }

    /** A weighted entry dropping between {@code min} and {@code max} of {@code item}. */
    private UniformContainerBase.Builder<?> addItem(ItemLike item, int weight, int min, int max) {
        return LootItem.lootTableItem(item).setWeight(weight).apply(SetItemCountFunction.setCount(ContextIntProviders.between(min, max)));
    }

    private static ResourceKey<LootTable> key(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, path));
    }
}
