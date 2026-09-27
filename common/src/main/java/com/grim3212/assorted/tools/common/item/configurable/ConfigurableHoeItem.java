package com.grim3212.assorted.tools.common.item.configurable;

import com.grim3212.assorted.lib.core.tool.ITiered;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;

/**
 * A hoe built from a configured tool material; {@link HoeItem} supplies tilling. Its attack damage
 * baseline is the harvest level negated: the better the material, the worse the hoe swings.
 */
public class ConfigurableHoeItem extends HoeItem implements ITiered {

    private final ToolTier tierHolder;

    public ConfigurableHoeItem(ToolTier tierHolder, Item.Properties properties) {
        super(tierHolder.material(), -tierHolder.getHarvestLevel(), ConfigurableTools.HOE_SPEED, properties);
        this.tierHolder = tierHolder;
    }

    @Override
    public ToolTier getToolTier() {
        return tierHolder;
    }
}
