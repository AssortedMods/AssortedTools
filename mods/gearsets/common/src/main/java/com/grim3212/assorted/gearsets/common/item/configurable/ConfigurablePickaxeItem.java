package com.grim3212.assorted.gearsets.common.item.configurable;

import com.grim3212.assorted.lib.core.tool.ITiered;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.PickaxeItem;

/**
 * A pickaxe built from a configured tool material, which is its {@code Tier}: speed, harvest level, durability,
 * enchantability and repair all come from it.
 */
public class ConfigurablePickaxeItem extends PickaxeItem implements ITiered {

    private final ToolTier tierHolder;

    public ConfigurablePickaxeItem(ToolTier tierHolder, Properties properties) {
        super(tierHolder, properties.attributes(DiggerItem.createAttributes(tierHolder, ConfigurableTools.PICKAXE_DAMAGE, ConfigurableTools.PICKAXE_SPEED)));
        this.tierHolder = tierHolder;
    }

    @Override
    public ToolTier getToolTier() {
        return tierHolder;
    }
}
