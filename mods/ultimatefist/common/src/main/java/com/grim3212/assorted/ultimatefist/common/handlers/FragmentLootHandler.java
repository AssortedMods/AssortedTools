package com.grim3212.assorted.ultimatefist.common.handlers;

import com.grim3212.assorted.lib.events.LootTableModifyEvent;
import com.grim3212.assorted.ultimatefist.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/** Hides the fragments in vanilla's chests: the first three in the overworld, two in the nether and three in the end. */
public class FragmentLootHandler {

    public static final ResourceKey<LootTable> OVERWORLD_UF_LOOT = lootTable("fragments_overworld_loot");
    public static final ResourceKey<LootTable> NETHER_UF_LOOT = lootTable("fragments_nether_loot");
    public static final ResourceKey<LootTable> END_UF_LOOT = lootTable("fragments_end_loot");

    private static final List<Identifier> OVERWORLD_UF_CHESTS = chests("stronghold_corridor", "stronghold_crossing", "stronghold_library", "woodland_mansion", "underwater_ruin_big", "underwater_ruin_small", "ancient_city");
    private static final List<Identifier> NETHER_UF_CHESTS = chests("nether_bridge", "ruined_portal");
    private static final List<Identifier> END_UF_CHESTS = chests("end_city_treasure");

    private static ResourceKey<LootTable> lootTable(String name) {
        return ResourceKey.create(Registries.LOOT_TABLE, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
    }

    private static List<Identifier> chests(String... names) {
        return Arrays.stream(names).map(name -> Identifier.withDefaultNamespace("chests/" + name)).toList();
    }

    public static void init(LootTableModifyEvent event) {
        inject(event, OVERWORLD_UF_CHESTS, OVERWORLD_UF_LOOT);
        inject(event, NETHER_UF_CHESTS, NETHER_UF_LOOT);
        inject(event, END_UF_CHESTS, END_UF_LOOT);
    }

    private static void inject(LootTableModifyEvent event, Collection<Identifier> tables, ResourceKey<LootTable> loot) {
        if (tables.contains(event.getId())) {
            event.getContext().addPool(LootPool.lootPool().add(NestedLootTable.lootTableReference(loot).setWeight(1)));
        }
    }
}
