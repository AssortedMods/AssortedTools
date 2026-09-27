package com.grim3212.assorted.throwingspears.common.item;

import com.grim3212.assorted.lib.core.tool.ToolTiers;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.throwingspears.Constants;
import com.grim3212.assorted.throwingspears.Family;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ThrowingSpearsItems {

    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Family.ID);

    private static final ToolTiers TIERS = ToolTiers.get();

    public static final IRegistryObject<BetterSpearItem> WOOD_THROWING_SPEAR = register("wood_throwing_spear", props -> new BetterSpearItem(props, TIERS.wood));
    public static final IRegistryObject<BetterSpearItem> STONE_THROWING_SPEAR = register("stone_throwing_spear", props -> new BetterSpearItem(props, TIERS.stone));
    public static final IRegistryObject<BetterSpearItem> IRON_THROWING_SPEAR = register("iron_throwing_spear", props -> new BetterSpearItem(props, TIERS.iron));
    public static final IRegistryObject<BetterSpearItem> GOLD_THROWING_SPEAR = register("gold_throwing_spear", props -> new BetterSpearItem(props, TIERS.gold));
    public static final IRegistryObject<BetterSpearItem> DIAMOND_THROWING_SPEAR = register("diamond_throwing_spear", props -> new BetterSpearItem(props, TIERS.diamond));
    public static final IRegistryObject<BetterSpearItem> NETHERITE_THROWING_SPEAR = register("netherite_throwing_spear", props -> new BetterSpearItem(props.fireResistant(), TIERS.netherite));

    /** One throwing spear for each extra material, keyed by the material's name, in the shared tiers' order. */
    public static final Map<String, IRegistryObject<BetterSpearItem>> EXTRA_SPEARS = new LinkedHashMap<>();

    static {
        TIERS.extras().forEach((name, tier) -> EXTRA_SPEARS.put(name, register(name + "_throwing_spear", props -> new BetterSpearItem(props, tier))));
    }

    /** Wood through netherite, then the extra materials. */
    public static List<BetterSpearItem> spears() {
        List<BetterSpearItem> spears = new ArrayList<>(List.of(WOOD_THROWING_SPEAR.get(), STONE_THROWING_SPEAR.get(), IRON_THROWING_SPEAR.get(), GOLD_THROWING_SPEAR.get(), DIAMOND_THROWING_SPEAR.get(), NETHERITE_THROWING_SPEAR.get()));
        EXTRA_SPEARS.values().forEach(spear -> spears.add(spear.get()));
        return spears;
    }

    private static <T extends Item> IRegistryObject<T> register(final String name, final Function<Item.Properties, ? extends T> factory) {
        final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        return ITEMS.register(name, () -> factory.apply(new Item.Properties().setId(key)));
    }

    public static void init() {
    }
}
