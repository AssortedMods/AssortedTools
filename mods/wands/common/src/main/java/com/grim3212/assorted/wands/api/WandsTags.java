package com.grim3212.assorted.wands.api;

import com.grim3212.assorted.wands.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class WandsTags {

    /** What a breaking wand in its destructive modes leaves standing. */
    public static final TagKey<Block> DESTRUCTIVE_SPARED_BLOCKS = tag("wands/destructive_spared_blocks");
    /** What a reinforced mining wand's surface mining digs out. */
    public static final TagKey<Block> MINING_SURFACE_BLOCKS = tag("wands/mining_surface_blocks");

    private static TagKey<Block> tag(String name) {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
    }
}
