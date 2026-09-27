package com.grim3212.assorted.extramaterials.common.item;

import com.grim3212.assorted.lib.core.tool.ArmorMaterialConfig;
import com.grim3212.assorted.lib.core.tool.ConfigurableArmorItem;
import net.minecraft.world.item.equipment.ArmorType;

public class MaterialArmorItem extends ConfigurableArmorItem {

    public MaterialArmorItem(ArmorMaterialConfig material, ArmorType type, Properties builderIn) {
        super(material, type, builderIn);
    }
}
