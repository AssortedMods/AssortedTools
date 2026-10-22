package com.grim3212.assorted.staffs.data;

import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.staffs.Constants;
import com.grim3212.assorted.staffs.common.item.StaffsItems;
import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.floats.ContextFloatProviders;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;

/**
 * The frost rod drops, injected into mob loot by {@code StaffsLootHandlers}: a stray drops one as a
 * blaze drops its rod, and any other monster sometimes does when killed in a snowy biome.
 */
public class StaffsEntityLoot implements LootTableSubProvider {

    private final LootTableSubProvider.Context output;
    private final HolderGetter<Enchantment> enchantments;

    public StaffsEntityLoot(LootTableSubProvider.Context output) {
        this.output = output;
        this.enchantments = output.lookup(Registries.ENCHANTMENT);
    }

    @Override
    public void run() {
        LootPool.Builder stray = LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
                .add(LootItem.lootTableItem(StaffsItems.FROST_ROD.get())
                        .apply(SetItemCountFunction.setCount(ContextIntProviders.between(0, 1)))
                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.enchantments, ContextFloatProviders.between(0.0F, 1.0F))))
                .when(LootItemKilledByPlayerCondition.killedByPlayer());
        this.output.accept(key("frost_rod_stray"), LootTable.lootTable().withPool(stray));

        LootPool.Builder snowy = LootPool.lootPool().setRolls(ContextIntProviders.exactly(1))
                .add(LootItem.lootTableItem(StaffsItems.FROST_ROD.get()))
                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(this.output.lookup(Registries.BIOME).getOrThrow(LibCommonTags.Biomes.IS_SNOWY))))
                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.enchantments, 0.05F, 0.02F));
        this.output.accept(key("frost_rod_snowy_biome"), LootTable.lootTable().withPool(snowy));
    }

    private static ResourceKey<LootTable> key(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, path));
    }
}
