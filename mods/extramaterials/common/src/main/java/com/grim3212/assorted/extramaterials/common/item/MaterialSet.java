package com.grim3212.assorted.extramaterials.common.item;

import com.grim3212.assorted.lib.core.tool.ArmorMaterialConfig;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.List;

/** One extra material's tools, spear and armor, and the tag of what they are made from. */
public record MaterialSet(ToolTier tier, ArmorMaterialConfig armor, TagKey<Item> material,
                          IRegistryObject<MaterialSwordItem> sword, IRegistryObject<MaterialPickaxeItem> pickaxe,
                          IRegistryObject<MaterialAxeItem> axe, IRegistryObject<MaterialShovelItem> shovel,
                          IRegistryObject<MaterialHoeItem> hoe, IRegistryObject<MaterialSpearItem> spear,
                          IRegistryObject<MaterialArmorItem> helmet, IRegistryObject<MaterialArmorItem> chestplate,
                          IRegistryObject<MaterialArmorItem> leggings, IRegistryObject<MaterialArmorItem> boots) {

    public String name() {
        return this.tier.getName();
    }

    /** In the creative tab's order. */
    public List<Item> items() {
        return List.of(this.sword.get(), this.pickaxe.get(), this.axe.get(), this.shovel.get(), this.hoe.get(), this.spear.get(),
                this.helmet.get(), this.chestplate.get(), this.leggings.get(), this.boots.get());
    }
}
