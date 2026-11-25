package com.grim3212.assorted.machetes.common.item;

import com.grim3212.assorted.lib.core.tool.ITiered;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import com.grim3212.assorted.machetes.api.MachetesTags;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

/**
 * A sword that clears plants: hits a little softer and swings a little faster than a sword of its
 * material, and cuts everything in {@code #assortedmachetes:mineable/machete} at the material's speed.
 * A {@code SwordItem}, so it sweeps and cuts bamboo in one hit as swords do; its sword mining rules are replaced by
 * {@link #toolComponent()}.
 */
public class MacheteItem extends SwordItem implements ITiered {

    public static final float ATTACK_DAMAGE = 2.2F;
    public static final float ATTACK_SPEED = -2.15F;

    private final ToolTier tierHolder;

    public MacheteItem(ToolTier tierHolder, Properties properties) {
        super(tierHolder, properties.attributes(DiggerItem.createAttributes(tierHolder, ATTACK_DAMAGE, ATTACK_SPEED)));
        this.tierHolder = tierHolder;
    }

    /** A sword's cobweb rule, then the plants at the material's speed, for one durability a block. */
    public Tool toolComponent() {
        return new Tool(List.of(
                Tool.Rule.minesAndDrops(List.of(Blocks.COBWEB), 15.0F),
                Tool.Rule.overrideSpeed(MachetesTags.MINEABLE_MACHETE, Math.max(this.tierHolder.getSpeed(), 1.5F))),
                1.0F, 1);
    }

    @Override
    public ToolTier getToolTier() {
        return this.tierHolder;
    }
}
