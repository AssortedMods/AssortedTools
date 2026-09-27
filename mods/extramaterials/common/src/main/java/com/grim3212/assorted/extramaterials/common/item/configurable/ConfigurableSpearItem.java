package com.grim3212.assorted.extramaterials.common.item.configurable;

import com.grim3212.assorted.extramaterials.config.SpearConfig;
import com.grim3212.assorted.lib.core.tool.ITiered;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import net.minecraft.world.item.Item;

/**
 * A spear as vanilla makes them, a lunge weapon rather than a thrown one, from a configured tool material and the
 * nine lunge values in its {@link SpearConfig}.
 */
public class ConfigurableSpearItem extends Item implements ITiered {

    private final ToolTier tier;

    public ConfigurableSpearItem(ToolTier tier, SpearConfig spear, Properties props) {
        super(spear.getSpearStats().apply(props, tier.material()));
        this.tier = tier;
    }

    @Override
    public ToolTier getToolTier() {
        return this.tier;
    }
}
