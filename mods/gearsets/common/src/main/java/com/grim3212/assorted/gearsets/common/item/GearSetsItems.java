package com.grim3212.assorted.gearsets.common.item;

import com.grim3212.assorted.gearsets.Constants;
import com.grim3212.assorted.gearsets.GearSetsCommonMod;
import com.grim3212.assorted.gearsets.Family;
import com.grim3212.assorted.gearsets.config.SpearConfig;
import com.grim3212.assorted.lib.core.tool.ArmorMaterialConfig;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import com.grim3212.assorted.lib.core.tool.ToolTiers;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

public class GearSetsItems {

    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Family.ID);

    /** Every extra material's set, keyed by the material's name, in the shared tiers' order. */
    public static final Map<String, MaterialSet> MATERIALS = new LinkedHashMap<>();

    static {
        ToolTiers.get().extras().forEach((name, tier) -> MATERIALS.put(name, materialSet(tier)));
    }

    private static MaterialSet materialSet(ToolTier tier) {
        String name = tier.getName();
        SpearConfig spear = GearSetsCommonMod.COMMON_CONFIG.spears.get(name);
        ArmorMaterialConfig armor = GearSetsCommonMod.COMMON_CONFIG.armors.get(name);
        return new MaterialSet(tier, armor, tier.getRepairItems(),
                register(name + "_sword", props -> new MaterialSwordItem(tier, props)),
                register(name + "_pickaxe", props -> new MaterialPickaxeItem(tier, props)),
                register(name + "_axe", props -> new MaterialAxeItem(tier, props)),
                register(name + "_shovel", props -> new MaterialShovelItem(tier, props)),
                register(name + "_hoe", props -> new MaterialHoeItem(tier, props)),
                register(name + "_spear", props -> new MaterialSpearItem(tier, spear, props)),
                register(name + "_helmet", props -> new MaterialArmorItem(armor, ArmorType.HELMET, props)),
                register(name + "_chestplate", props -> new MaterialArmorItem(armor, ArmorType.CHESTPLATE, props)),
                register(name + "_leggings", props -> new MaterialArmorItem(armor, ArmorType.LEGGINGS, props)),
                register(name + "_boots", props -> new MaterialArmorItem(armor, ArmorType.BOOTS, props)));
    }

    private static <T extends Item> IRegistryObject<T> register(final String name, final Function<Item.Properties, ? extends T> factory) {
        final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        return ITEMS.register(name, () -> factory.apply(new Item.Properties().setId(key)));
    }

    public static void init() {
    }
}
