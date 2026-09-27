package com.grim3212.assorted.extramaterials.common.item.configurable;

import com.grim3212.assorted.lib.core.tool.ITiered;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ShovelItem;

/**
 * A shovel built from a configured tool material. {@link ShovelItem} survives because it still
 * carries the path-making right click; everything numeric comes from the material record.
 */
public class ConfigurableShovelItem extends ShovelItem implements ITiered {

    private final ToolTier tierHolder;

    public ConfigurableShovelItem(ToolTier tierHolder, Item.Properties builder) {
        super(tierHolder.material(), ConfigurableTools.SHOVEL_DAMAGE, ConfigurableTools.SHOVEL_SPEED, builder);
        this.tierHolder = tierHolder;
    }

    @Override
    public ToolTier getToolTier() {
        return tierHolder;
    }
}
