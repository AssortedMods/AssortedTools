package com.grim3212.assorted.ultimatefist.gametest;

import com.grim3212.assorted.ultimatefist.common.item.UltimateFistItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** Every fragment can be found, in the chests of the dimension the site lists it under, so the fist can always be made. */
final class FragmentLootTests {

    /** One chest of each dimension, and the fragments it alone can hold. */
    private static final Map<String, List<String>> CHESTS = Map.of(
            "stronghold_library", List.of("u_fragment", "l_fragment", "t_fragment"),
            "nether_bridge", List.of("i_fragment", "m_fragment"),
            "end_city_treasure", List.of("a_fragment", "e_fragment", "missing_fragment"));

    private FragmentLootTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("every_fragment_turns_up_in_its_dimensions_chests", FragmentLootTests::everyFragmentTurnsUpInItsDimensionsChests);
    }

    /** Two thousand chests each, so even a rare fragment shows. */
    private static void everyFragmentTurnsUpInItsDimensionsChests(GameTestHelper helper) {
        LootParams params = new LootParams.Builder(helper.getLevel()).withParameter(LootContextParams.ORIGIN, helper.absoluteVec(Vec3.ZERO)).create(LootContextParamSets.CHEST);
        List<String> missing = new ArrayList<>();

        for (Map.Entry<String, List<String>> chest : CHESTS.entrySet()) {
            LootTable table = helper.getLevel().getServer().reloadableRegistries()
                    .getLootTable(ResourceKey.create(Registries.LOOT_TABLE, Identifier.withDefaultNamespace("chests/" + chest.getKey())));
            Set<Item> found = new HashSet<>();
            for (int i = 0; i < 2000; i++) {
                for (ItemStack stack : table.getRandomItems(params)) {
                    found.add(stack.getItem());
                }
            }
            for (String fragment : chest.getValue()) {
                Item item = UltimateFistItems.fragments().stream().filter(candidate -> BuiltInRegistries.ITEM.getKey(candidate).getPath().equals(fragment))
                        .findFirst().orElseThrow();
                if (!found.contains(item)) {
                    missing.add(fragment + " never turned up in " + chest.getKey());
                }
            }
        }

        helper.assertTrue(missing.isEmpty(), String.join("\n", missing));
        helper.succeed();
    }
}
