package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.lib.core.tool.ToolTier;
import com.grim3212.assorted.tools.common.item.configurable.ConfigurableSpearItem;
import com.grim3212.assorted.tools.config.SpearConfig;

/** The vanilla-style spear of an extra material. The throwing spear is {@link BetterSpearItem}. */
public class MaterialSpearItem extends ConfigurableSpearItem {

    public MaterialSpearItem(ToolTier tier, SpearConfig spear, Properties builder) {
        super(tier, spear, builder);
    }
}
