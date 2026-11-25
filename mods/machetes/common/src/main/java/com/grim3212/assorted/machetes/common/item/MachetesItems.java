package com.grim3212.assorted.machetes.common.item;

import com.grim3212.assorted.lib.core.tool.ToolTier;
import com.grim3212.assorted.lib.core.tool.ToolTiers;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.machetes.Constants;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MachetesItems {

    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    private static final ToolTiers TIERS = ToolTiers.get();

    public static final IRegistryObject<MacheteItem> WOOD_MACHETE = register("wood_machete", TIERS.wood, false);
    public static final IRegistryObject<MacheteItem> STONE_MACHETE = register("stone_machete", TIERS.stone, false);
    public static final IRegistryObject<MacheteItem> GOLD_MACHETE = register("gold_machete", TIERS.gold, false);
    public static final IRegistryObject<MacheteItem> IRON_MACHETE = register("iron_machete", TIERS.iron, false);
    public static final IRegistryObject<MacheteItem> DIAMOND_MACHETE = register("diamond_machete", TIERS.diamond, false);
    public static final IRegistryObject<MacheteItem> NETHERITE_MACHETE = register("netherite_machete", TIERS.netherite, true);

    /** One machete for each extra material, keyed by the material's name, in the shared tiers' order. */
    public static final Map<String, IRegistryObject<MacheteItem>> EXTRA_MACHETES = new LinkedHashMap<>();

    static {
        TIERS.extras().forEach((name, tier) -> EXTRA_MACHETES.put(name, register(name + "_machete", tier, false)));
    }

    /** Wood through netherite, then the extra materials. */
    public static List<MacheteItem> machetes() {
        List<MacheteItem> machetes = new ArrayList<>(List.of(WOOD_MACHETE.get(), STONE_MACHETE.get(), GOLD_MACHETE.get(), IRON_MACHETE.get(), DIAMOND_MACHETE.get(), NETHERITE_MACHETE.get()));
        EXTRA_MACHETES.values().forEach(machete -> machetes.add(machete.get()));
        return machetes;
    }

    private static IRegistryObject<MacheteItem> register(String name, ToolTier tier, boolean fireResistant) {
        IRegistryObject<MacheteItem> machete = ITEMS.register(name, () -> new MacheteItem(tier, fireResistant ? new Item.Properties().fireResistant() : new Item.Properties()));
        // SwordItem fixes its own mining rules as it is built; these replace them once every item exists.
        Services.PLATFORM.modifyDefaultComponents(machete, components -> components.set(DataComponents.TOOL, machete.get().toolComponent()));
        return machete;
    }

    public static void init() {
    }
}
