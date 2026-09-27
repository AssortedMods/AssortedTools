package com.grim3212.assorted.boomerangs.common.item;

import com.grim3212.assorted.boomerangs.Constants;
import com.grim3212.assorted.boomerangs.Family;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.function.Function;

public class BoomerangsItems {

    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Family.ID);

    public static final IRegistryObject<BoomerangItem> WOOD_BOOMERANG = register("wood_boomerang", props -> new BoomerangItem(true, props.stacksTo(1)));
    public static final IRegistryObject<BoomerangItem> DIAMOND_BOOMERANG = register("diamond_boomerang", props -> new BoomerangItem(false, props.stacksTo(1)));

    private static <T extends Item> IRegistryObject<T> register(final String name, final Function<Item.Properties, ? extends T> factory) {
        final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        return ITEMS.register(name, () -> factory.apply(new Item.Properties().setId(key)));
    }

    public static void init() {
    }
}
