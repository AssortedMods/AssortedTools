package com.grim3212.assorted.hammers.common.item;

import com.grim3212.assorted.hammers.Constants;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import com.grim3212.assorted.lib.core.tool.ToolTiers;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class HammersItems {

    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    private static final ToolTiers TIERS = ToolTiers.get();

    public static final IRegistryObject<HammerItem> WOOD_HAMMER = register("wood_hammer", props -> new HammerItem(TIERS.wood, props));
    public static final IRegistryObject<HammerItem> STONE_HAMMER = register("stone_hammer", props -> new HammerItem(TIERS.stone, props));
    public static final IRegistryObject<HammerItem> GOLD_HAMMER = register("gold_hammer", props -> new HammerItem(TIERS.gold, props));
    public static final IRegistryObject<HammerItem> IRON_HAMMER = register("iron_hammer", props -> new HammerItem(TIERS.iron, props));
    public static final IRegistryObject<HammerItem> DIAMOND_HAMMER = register("diamond_hammer", props -> new HammerItem(TIERS.diamond, props));
    public static final IRegistryObject<HammerItem> NETHERITE_HAMMER = register("netherite_hammer", props -> new HammerItem(TIERS.netherite, props.fireResistant()));

    /** One hammer for each extra material, keyed by the material's name, in the shared tiers' order. */
    public static final Map<String, IRegistryObject<HammerItem>> EXTRA_HAMMERS = new LinkedHashMap<>();

    static {
        TIERS.extras().forEach((name, tier) -> EXTRA_HAMMERS.put(name, register(name + "_hammer", props -> new HammerItem(tier, props))));
    }

    /** Wood through netherite, then the extra materials. */
    public static List<HammerItem> hammers() {
        List<HammerItem> hammers = new ArrayList<>(List.of(WOOD_HAMMER.get(), STONE_HAMMER.get(), GOLD_HAMMER.get(), IRON_HAMMER.get(), DIAMOND_HAMMER.get(), NETHERITE_HAMMER.get()));
        EXTRA_HAMMERS.values().forEach(hammer -> hammers.add(hammer.get()));
        return hammers;
    }

    private static <T extends Item> IRegistryObject<T> register(final String name, final Function<Item.Properties, ? extends T> factory) {
        final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        return ITEMS.register(name, () -> factory.apply(new Item.Properties().setId(key)));
    }

    public static void init() {
    }
}
