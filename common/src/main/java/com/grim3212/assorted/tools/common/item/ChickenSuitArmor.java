package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.lib.core.tool.ConfigurableArmorItem;
import com.grim3212.assorted.tools.ToolsCommonMod;
import net.minecraft.world.item.equipment.ArmorType;

public class ChickenSuitArmor extends ConfigurableArmorItem {

    public ChickenSuitArmor(ArmorType type, Properties builderIn) {
        super(ToolsCommonMod.COMMON_CONFIG.chickenSuitArmorMaterial, type, builderIn);
    }
}
