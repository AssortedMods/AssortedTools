package com.grim3212.assorted.extramaterials.common.item;

import com.grim3212.assorted.extramaterials.common.item.configurable.ConfigurableSpearItem;
import com.grim3212.assorted.extramaterials.config.SpearConfig;
import com.grim3212.assorted.lib.core.tool.ToolTier;

/** The vanilla-style spear of an extra material, a lunge weapon. Assorted Throwing Spears has the thrown kind. */
public class MaterialSpearItem extends ConfigurableSpearItem {

    public MaterialSpearItem(ToolTier tier, SpearConfig spear, Properties builder) {
        super(tier, spear, builder);
    }
}
