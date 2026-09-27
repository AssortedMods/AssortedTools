package com.grim3212.assorted.tools.common.item.configurable;

import com.grim3212.assorted.lib.core.tool.ITiered;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import net.minecraft.world.item.Item;

/**
 * A pickaxe built from a configured tool material. {@code Properties#pickaxe} applies the
 * material's speed, harvest level, durability and attributes.
 */
public class ConfigurablePickaxeItem extends Item implements ITiered {

    private final ToolTier tierHolder;

    public ConfigurablePickaxeItem(ToolTier tierHolder, Item.Properties builder) {
        super(builder.pickaxe(tierHolder.material(), ConfigurableTools.PICKAXE_DAMAGE, ConfigurableTools.PICKAXE_SPEED));
        this.tierHolder = tierHolder;
    }

    @Override
    public ToolTier getToolTier() {
        return tierHolder;
    }
}
