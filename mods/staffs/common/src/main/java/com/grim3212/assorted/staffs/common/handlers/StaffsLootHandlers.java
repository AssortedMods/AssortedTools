package com.grim3212.assorted.staffs.common.handlers;

import com.grim3212.assorted.lib.conditions.LibParts;
import com.grim3212.assorted.lib.events.LootTableModifyEvent;
import com.grim3212.assorted.staffs.Constants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** Adds the staffs to chests where their element turns up, and frost rods to what cold monsters drop. */
public class StaffsLootHandlers {

    public static final ResourceKey<LootTable> NEPTUNE_STAFF_LOOT = lootTable("staffs_neptune_loot");
    public static final ResourceKey<LootTable> PHOENIX_STAFF_LOOT = lootTable("staffs_phoenix_loot");
    public static final ResourceKey<LootTable> POWER_STAFF_LOOT = lootTable("staffs_power_loot");
    public static final ResourceKey<LootTable> FROST_ROD_STRAY_LOOT = lootTable("frost_rod_stray");
    public static final ResourceKey<LootTable> FROST_ROD_SNOWY_LOOT = lootTable("frost_rod_snowy_biome");

    // Each staff turns up where its element does.
    private static final List<ResourceLocation> NEPTUNE_STAFF_CHESTS = chests("underwater_ruin_big", "shipwreck_treasure", "buried_treasure", "igloo_chest");
    private static final List<ResourceLocation> PHOENIX_STAFF_CHESTS = chests("nether_bridge", "bastion_treasure", "ruined_portal");
    private static final List<ResourceLocation> POWER_STAFF_CHESTS = chests("stronghold_library", "woodland_mansion", "ancient_city", "desert_pyramid", "jungle_temple");

    /** Every monster's loot table but the stray's, which has its own rod pool. Built on first use, once the registries are done. */
    private static Set<ResourceLocation> monsterTables;

    private static ResourceKey<LootTable> lootTable(String name) {
        return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
    }

    private static List<ResourceLocation> chests(String... names) {
        return Arrays.stream(names).map(name -> ResourceLocation.withDefaultNamespace("chests/" + name)).toList();
    }

    public static void init(LootTableModifyEvent event) {
        if (!LibParts.isEnabled(Constants.MOD_ID)) {
            return;
        }

        inject(event, NEPTUNE_STAFF_CHESTS, NEPTUNE_STAFF_LOOT);
        inject(event, PHOENIX_STAFF_CHESTS, PHOENIX_STAFF_LOOT);
        inject(event, POWER_STAFF_CHESTS, POWER_STAFF_LOOT);
        inject(event, List.of(EntityType.STRAY.getDefaultLootTable().location()), FROST_ROD_STRAY_LOOT);
        inject(event, monsterTables(), FROST_ROD_SNOWY_LOOT);
    }

    private static void inject(LootTableModifyEvent event, Collection<ResourceLocation> tables, ResourceKey<LootTable> loot) {
        if (tables.contains(event.getId())) {
            event.getContext().addPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(loot).setWeight(1)));
        }
    }

    private static Set<ResourceLocation> monsterTables() {
        if (monsterTables == null) {
            monsterTables = BuiltInRegistries.ENTITY_TYPE.stream()
                    .filter(type -> type.getCategory() == MobCategory.MONSTER && type != EntityType.STRAY)
                    .map(EntityType::getDefaultLootTable)
                    .map(ResourceKey::location)
                    .collect(Collectors.toUnmodifiableSet());
        }
        return monsterTables;
    }
}
