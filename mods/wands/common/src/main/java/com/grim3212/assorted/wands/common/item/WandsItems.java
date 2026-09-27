package com.grim3212.assorted.wands.common.item;

import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.wands.Constants;
import com.grim3212.assorted.wands.Family;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Function;

public class WandsItems {

    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Family.ID);

    public static final IRegistryObject<WandBuildingItem> BUILDING_WAND = register("building_wand", props -> new WandBuildingItem(false, props.durability(30)));
    public static final IRegistryObject<WandBuildingItem> REINFORCED_BUILDING_WAND = register("reinforced_building_wand", props -> new WandBuildingItem(true, props.durability(200)));
    public static final IRegistryObject<WandBreakingItem> BREAKING_WAND = register("breaking_wand", props -> new WandBreakingItem(false, props.durability(15)));
    public static final IRegistryObject<WandBreakingItem> REINFORCED_BREAKING_WAND = register("reinforced_breaking_wand", props -> new WandBreakingItem(true, props.durability(120)));
    public static final IRegistryObject<WandMiningItem> MINING_WAND = register("mining_wand", props -> new WandMiningItem(false, props.durability(15)));
    public static final IRegistryObject<WandMiningItem> REINFORCED_MINING_WAND = register("reinforced_mining_wand", props -> new WandMiningItem(true, props.durability(120)));

    public static List<Item> wands() {
        return List.of(BUILDING_WAND.get(), BREAKING_WAND.get(), MINING_WAND.get(), REINFORCED_BUILDING_WAND.get(), REINFORCED_BREAKING_WAND.get(), REINFORCED_MINING_WAND.get());
    }

    private static <T extends Item> IRegistryObject<T> register(final String name, final Function<Item.Properties, ? extends T> factory) {
        final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        return ITEMS.register(name, () -> factory.apply(new Item.Properties().setId(key)));
    }

    public static void init() {
    }
}
