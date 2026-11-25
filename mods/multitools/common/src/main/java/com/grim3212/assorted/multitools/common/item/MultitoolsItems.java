package com.grim3212.assorted.multitools.common.item;

import com.grim3212.assorted.lib.core.tool.ToolTier;
import com.grim3212.assorted.lib.core.tool.ToolTiers;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.multitools.Constants;
import com.grim3212.assorted.multitools.MultitoolsCommonMod;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Function;

public class MultitoolsItems {

    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    private static final ToolTiers TIERS = ToolTiers.get();
    /** NeoForge's subclass if that loader names one, else the plain multitool. */
    private static final MultiToolFactory FACTORY = ServiceLoader.load(MultiToolFactory.class, MultitoolsItems.class.getClassLoader()).findFirst().orElse(MultiToolItem::new);

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
        IRegistryObject<MultiToolItem> multitool = register(name, props -> FACTORY.create(tier, MultitoolsCommonMod.COMMON_CONFIG.durabilityModifier(tier), fireResistant ? props.fireResistant() : props));
        // SwordItem fixes its own mining rules as it is built; these replace them once every item exists.
        Services.PLATFORM.modifyDefaultComponents(multitool, components -> components.set(DataComponents.TOOL, multitool.get().toolComponent()));
        return multitool;
    }

    private static <T extends Item> IRegistryObject<T> register(final String name, final Function<Item.Properties, ? extends T> factory) {
        return ITEMS.register(name, () -> factory.apply(new Item.Properties()));
    }

    public static void init() {
    }
}
