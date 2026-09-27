package com.grim3212.assorted.extramaterials.common.item.configurable;

import com.grim3212.assorted.lib.core.tool.ITiered;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import net.minecraft.world.item.Item;

/**
 * A sword built from a configured tool material. {@code Properties#sword} also adds the cobweb
 * mining rule and the blocking-disable weapon component.
 */
public class ConfigurableSwordItem extends Item implements ITiered {

    private final ToolTier tierHolder;

    public ConfigurableSwordItem(ToolTier tierHolder, Properties builder) {
        super(builder.sword(tierHolder.material(), ConfigurableTools.SWORD_DAMAGE, ConfigurableTools.SWORD_SPEED));
        this.tierHolder = tierHolder;
    }

    @Override
    public ToolTier getToolTier() {
        return tierHolder;
    }
}
