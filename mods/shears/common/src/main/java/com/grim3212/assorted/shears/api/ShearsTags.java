package com.grim3212.assorted.shears.api;

import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.shears.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class ShearsTags {

    /** Every coral, live and dead, which all of Coral Cutter's rules read. */
    public static final TagKey<Block> ALL_CORALS = commonBlockTag("corals/all");
    public static final TagKey<Block> DEAD_CORALS = commonBlockTag("corals/dead");
    /** What Coral Cutter may go on, vanilla shears included. */
    public static final TagKey<Item> SHEARS_ENCHANTABLE = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "enchantable/shears"));

    private ShearsTags() {
    }

    private static TagKey<Block> commonBlockTag(String name) {
        return TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(LibCommonTags.COMMON_NAMESPACE, name));
    }
}
