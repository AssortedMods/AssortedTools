package com.grim3212.assorted.gearsets.common.item.configurable;

import com.grim3212.assorted.lib.core.tool.ITiered;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;

/**
 * An axe built from a configured tool material; {@link AxeItem} supplies stripping, scraping and
 * waxing off. Axe damage and speed are per material, hence {@code axeDamage} and {@code axeSpeed}.
 */
public class ConfigurableAxeItem extends AxeItem implements ITiered {

    private final ToolTier tierHolder;

    public ConfigurableAxeItem(ToolTier tierHolder, Item.Properties builder) {
        super(tierHolder.material(), tierHolder.getAxeDamage(), tierHolder.getAxeSpeed(), builder);
        this.tierHolder = tierHolder;
    }

    @Override
    public ToolTier getToolTier() {
        return tierHolder;
    }
}
