package com.grim3212.assorted.gearsets.common.item;

import com.grim3212.assorted.lib.core.tool.ArmorMaterialConfig;
import com.grim3212.assorted.lib.core.tool.ConfigurableArmorItem;
import net.minecraft.world.item.ArmorItem;

public class MaterialArmorItem extends ConfigurableArmorItem {

    public MaterialArmorItem(ArmorMaterialConfig material, ArmorItem.Type type, Properties builderIn) {
        super(material, type, builderIn);
    }
}
