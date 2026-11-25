package com.grim3212.assorted.gearsets.common.item.configurable;

import com.grim3212.assorted.lib.core.tool.ITiered;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import net.minecraft.world.item.SwordItem;

/**
 * A sword built from a configured tool material. {@code SwordItem} also adds the cobweb and sword-efficient
 * mining rules and the sweep.
 */
public class ConfigurableSwordItem extends SwordItem implements ITiered {

    private final ToolTier tierHolder;

    public ConfigurableSwordItem(ToolTier tierHolder, Properties properties) {
        super(tierHolder, properties.attributes(SwordItem.createAttributes(tierHolder, ConfigurableTools.SWORD_DAMAGE, ConfigurableTools.SWORD_SPEED)));
        this.tierHolder = tierHolder;
    }

    @Override
    public ToolTier getToolTier() {
        return tierHolder;
    }
}
