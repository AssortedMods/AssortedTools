package com.grim3212.assorted.machetes.common.item;

import com.grim3212.assorted.lib.core.tool.ToolTiers;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.machetes.Constants;
import com.grim3212.assorted.machetes.Family;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MachetesItems {

    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Family.ID);

    private static final ToolTiers TIERS = ToolTiers.get();

    public static final IRegistryObject<MacheteItem> WOOD_MACHETE = register("wood_machete", props -> new MacheteItem(TIERS.wood, props));
    public static final IRegistryObject<MacheteItem> STONE_MACHETE = register("stone_machete", props -> new MacheteItem(TIERS.stone, props));
    public static final IRegistryObject<MacheteItem> GOLD_MACHETE = register("gold_machete", props -> new MacheteItem(TIERS.gold, props));
    public static final IRegistryObject<MacheteItem> IRON_MACHETE = register("iron_machete", props -> new MacheteItem(TIERS.iron, props));
    public static final IRegistryObject<MacheteItem> DIAMOND_MACHETE = register("diamond_machete", props -> new MacheteItem(TIERS.diamond, props));
    public static final IRegistryObject<MacheteItem> NETHERITE_MACHETE = register("netherite_machete", props -> new MacheteItem(TIERS.netherite, props.fireResistant()));

    /** One machete for each extra material, keyed by the material's name, in the shared tiers' order. */
    public static final Map<String, IRegistryObject<MacheteItem>> EXTRA_MACHETES = new LinkedHashMap<>();

    static {
        TIERS.extras().forEach((name, tier) -> EXTRA_MACHETES.put(name, register(name + "_machete", props -> new MacheteItem(tier, props))));
    }

    /** Wood through netherite, then the extra materials. */
    public static List<MacheteItem> machetes() {
        List<MacheteItem> machetes = new ArrayList<>(List.of(WOOD_MACHETE.get(), STONE_MACHETE.get(), GOLD_MACHETE.get(), IRON_MACHETE.get(), DIAMOND_MACHETE.get(), NETHERITE_MACHETE.get()));
        EXTRA_MACHETES.values().forEach(machete -> machetes.add(machete.get()));
        return machetes;
    }

    private static <T extends Item> IRegistryObject<T> register(final String name, final Function<Item.Properties, ? extends T> factory) {
        final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        return ITEMS.register(name, () -> factory.apply(new Item.Properties().setId(key)));
    }

    public static void init() {
    }
}
