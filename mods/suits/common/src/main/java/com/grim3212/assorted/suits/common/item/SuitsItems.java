package com.grim3212.assorted.suits.common.item;

import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import com.grim3212.assorted.suits.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.List;
import java.util.function.Function;

public class SuitsItems {

    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Constants.FAMILY_ID);

    public static final IRegistryObject<ChickenSuitArmor> CHICKEN_SUIT_HELMET = register("chicken_suit_helmet", props -> new ChickenSuitArmor(ArmorType.HELMET, props));
    public static final IRegistryObject<ChickenSuitArmor> CHICKEN_SUIT_CHESTPLATE = register("chicken_suit_chestplate", props -> new ChickenSuitArmor(ArmorType.CHESTPLATE, props));
    public static final IRegistryObject<ChickenSuitArmor> CHICKEN_SUIT_LEGGINGS = register("chicken_suit_leggings", props -> new ChickenSuitArmor(ArmorType.LEGGINGS, props));
    public static final IRegistryObject<ChickenSuitArmor> CHICKEN_SUIT_BOOTS = register("chicken_suit_boots", props -> new ChickenSuitArmor(ArmorType.BOOTS, props));

    public static final IRegistryObject<ScubaArmorItem> SCUBA_HELMET = register("scuba_helmet", props -> new ScubaArmorItem(ArmorType.HELMET, props));
    public static final IRegistryObject<ScubaArmorItem> SCUBA_CHESTPLATE = register("scuba_chestplate", props -> new ScubaArmorItem(ArmorType.CHESTPLATE, props));
    public static final IRegistryObject<ScubaArmorItem> SCUBA_LEGGINGS = register("scuba_leggings", props -> new ScubaArmorItem(ArmorType.LEGGINGS, props));
    public static final IRegistryObject<ScubaArmorItem> SCUBA_BOOTS = register("scuba_boots", props -> new ScubaArmorItem(ArmorType.BOOTS, props));

    public static final IRegistryObject<LavaArmorItem> LAVA_HELMET = register("lava_helmet", props -> new LavaArmorItem(ArmorType.HELMET, props.fireResistant()));
    public static final IRegistryObject<LavaArmorItem> LAVA_CHESTPLATE = register("lava_chestplate", props -> new LavaArmorItem(ArmorType.CHESTPLATE, props.fireResistant()));
    public static final IRegistryObject<LavaArmorItem> LAVA_LEGGINGS = register("lava_leggings", props -> new LavaArmorItem(ArmorType.LEGGINGS, props.fireResistant()));
    public static final IRegistryObject<LavaArmorItem> LAVA_BOOTS = register("lava_boots", props -> new LavaArmorItem(ArmorType.BOOTS, props.fireResistant()));

    /** Chicken, scuba then lava, each helmet to boots. */
    public static List<Item> suits() {
        return List.of(CHICKEN_SUIT_HELMET.get(), CHICKEN_SUIT_CHESTPLATE.get(), CHICKEN_SUIT_LEGGINGS.get(), CHICKEN_SUIT_BOOTS.get(),
                SCUBA_HELMET.get(), SCUBA_CHESTPLATE.get(), SCUBA_LEGGINGS.get(), SCUBA_BOOTS.get(),
                LAVA_HELMET.get(), LAVA_CHESTPLATE.get(), LAVA_LEGGINGS.get(), LAVA_BOOTS.get());
    }

    private static <T extends Item> IRegistryObject<T> register(final String name, final Function<Item.Properties, ? extends T> factory) {
        final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        return ITEMS.register(name, () -> factory.apply(new Item.Properties().setId(key)));
    }

    public static void init() {
    }
}
