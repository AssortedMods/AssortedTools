package com.grim3212.assorted.multitools.common.item;

import com.grim3212.assorted.lib.core.tool.ToolTier;
import com.grim3212.assorted.lib.core.tool.ToolTiers;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.multitools.Constants;
import com.grim3212.assorted.multitools.Family;
import com.grim3212.assorted.multitools.MultitoolsCommonMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MultitoolsItems {

    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Family.ID);

    private static final ToolTiers TIERS = ToolTiers.get();

    public static final IRegistryObject<MultiToolItem> WOODEN_MULTITOOL = register("wooden_multitool", TIERS.wood, false);
    public static final IRegistryObject<MultiToolItem> STONE_MULTITOOL = register("stone_multitool", TIERS.stone, false);
    public static final IRegistryObject<MultiToolItem> GOLDEN_MULTITOOL = register("golden_multitool", TIERS.gold, false);
    public static final IRegistryObject<MultiToolItem> IRON_MULTITOOL = register("iron_multitool", TIERS.iron, false);
    public static final IRegistryObject<MultiToolItem> DIAMOND_MULTITOOL = register("diamond_multitool", TIERS.diamond, false);
    public static final IRegistryObject<MultiToolItem> NETHERITE_MULTITOOL = register("netherite_multitool", TIERS.netherite, true);

    /** One multitool for each extra material, keyed by the material's name, in the shared tiers' order. */
    public static final Map<String, IRegistryObject<MultiToolItem>> EXTRA_MULTITOOLS = new LinkedHashMap<>();

    static {
        TIERS.extras().forEach((name, tier) -> EXTRA_MULTITOOLS.put(name, register(name + "_multitool", tier, false)));
    }

    /** Wood through netherite, then the extra materials. */
    public static List<MultiToolItem> multitools() {
        List<MultiToolItem> multitools = new ArrayList<>(List.of(WOODEN_MULTITOOL.get(), STONE_MULTITOOL.get(), GOLDEN_MULTITOOL.get(), IRON_MULTITOOL.get(), DIAMOND_MULTITOOL.get(), NETHERITE_MULTITOOL.get()));
        EXTRA_MULTITOOLS.values().forEach(multitool -> multitools.add(multitool.get()));
        return multitools;
    }

    private static IRegistryObject<MultiToolItem> register(String name, ToolTier tier, boolean fireResistant) {
        return register(name, props -> new MultiToolItem(tier, MultitoolsCommonMod.COMMON_CONFIG.durabilityModifier(tier), fireResistant ? props.fireResistant() : props));
    }

    private static <T extends Item> IRegistryObject<T> register(final String name, final Function<Item.Properties, ? extends T> factory) {
        final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        return ITEMS.register(name, () -> factory.apply(new Item.Properties().setId(key)));
    }

    public static void init() {
    }
}
