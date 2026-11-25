package com.grim3212.assorted.gearsets.common.item.configurable;

import com.grim3212.assorted.lib.core.tool.ITiered;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.HoeItem;

/**
 * A hoe built from a configured tool material; {@code HoeItem} adds tilling. Its attack damage baseline is the
 * harvest level negated: the better the material, the worse the hoe swings.
 */
public class ConfigurableHoeItem extends HoeItem implements ITiered {

    private final ToolTier tierHolder;

    public ConfigurableHoeItem(ToolTier tierHolder, Properties properties) {
        super(tierHolder, properties.attributes(DiggerItem.createAttributes(tierHolder, -tierHolder.getHarvestLevel(), ConfigurableTools.HOE_SPEED)));
        this.tierHolder = tierHolder;
    }

    @Override
    public ToolTier getToolTier() {
        return tierHolder;
    }
}
