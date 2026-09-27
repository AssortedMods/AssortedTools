package com.grim3212.assorted.shears.common.item;

import com.grim3212.assorted.lib.core.tool.ToolTiers;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.shears.Constants;
import com.grim3212.assorted.shears.Family;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class ShearsItems {

    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Family.ID);

    private static final ToolTiers TIERS = ToolTiers.get();

    public static final IRegistryObject<MaterialShears> WOOD_SHEARS = register("wood_shears", props -> new MaterialShears(props, TIERS.wood));
    public static final IRegistryObject<MaterialShears> STONE_SHEARS = register("stone_shears", props -> new MaterialShears(props, TIERS.stone));
    public static final IRegistryObject<MaterialShears> GOLD_SHEARS = register("gold_shears", props -> new MaterialShears(props, TIERS.gold));
    public static final IRegistryObject<MaterialShears> DIAMOND_SHEARS = register("diamond_shears", props -> new MaterialShears(props, TIERS.diamond));
    public static final IRegistryObject<MaterialShears> NETHERITE_SHEARS = register("netherite_shears", props -> new MaterialShears(props.fireResistant(), TIERS.netherite));

    /** One pair for each extra material, keyed by the material's name, in the shared tiers' order. */
    public static final Map<String, IRegistryObject<MaterialShears>> EXTRA_SHEARS = new LinkedHashMap<>();

    static {
        TIERS.extras().forEach((name, tier) -> EXTRA_SHEARS.put(name, register(name + "_shears", props -> new MaterialShears(props, tier))));
    }

    /** Wood through netherite, then the extra materials. */
    public static List<MaterialShears> shears() {
        List<MaterialShears> shears = new ArrayList<>(List.of(WOOD_SHEARS.get(), STONE_SHEARS.get(), GOLD_SHEARS.get(), DIAMOND_SHEARS.get(), NETHERITE_SHEARS.get()));
        EXTRA_SHEARS.values().forEach(item -> shears.add(item.get()));
        return shears;
    }

    private static <T extends Item> IRegistryObject<T> register(final String name, final Function<Item.Properties, ? extends T> factory) {
        final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        return ITEMS.register(name, () -> factory.apply(new Item.Properties().setId(key)));
    }

    public static void init() {
    }
}
