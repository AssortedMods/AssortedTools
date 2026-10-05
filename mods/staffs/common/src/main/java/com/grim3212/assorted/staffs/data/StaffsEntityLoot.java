package com.grim3212.assorted.staffs.data;

import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.staffs.Constants;
import com.grim3212.assorted.staffs.common.item.StaffsItems;
import net.minecraft.advancements.predicates.LocationPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LocationCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithEnchantedBonusCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

/**
 * The frost rod drops, injected into mob loot by {@code StaffsLootHandlers}: a stray drops one as a
 * blaze drops its rod, and any other monster sometimes does when killed in a snowy biome.
 */
public class StaffsEntityLoot implements LootTableSubProvider {

    private final HolderLookup.Provider registries;

    public StaffsEntityLoot(HolderLookup.Provider registries) {
        this.registries = registries;
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, Builder> builder) {
        LootPool.Builder stray = LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(StaffsItems.FROST_ROD.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
                        .apply(EnchantedCountIncreaseFunction.lootingMultiplier(this.registries, UniformGenerator.between(0.0F, 1.0F))))
                .when(LootItemKilledByPlayerCondition.killedByPlayer());
        builder.accept(key("frost_rod_stray"), LootTable.lootTable().withPool(stray));

        LootPool.Builder snowy = LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(StaffsItems.FROST_ROD.get()))
                .when(LootItemKilledByPlayerCondition.killedByPlayer())
                .when(LocationCheck.checkLocation(LocationPredicate.Builder.location().setBiomes(this.registries.lookupOrThrow(Registries.BIOME).getOrThrow(LibCommonTags.Biomes.IS_SNOWY))))
                .when(LootItemRandomChanceWithEnchantedBonusCondition.randomChanceAndLootingBoost(this.registries, 0.05F, 0.02F));
        builder.accept(key("frost_rod_snowy_biome"), LootTable.lootTable().withPool(snowy));
    }

    private static ResourceKey<LootTable> key(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, path));
    }
}
