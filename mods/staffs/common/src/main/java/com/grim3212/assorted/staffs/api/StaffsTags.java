package com.grim3212.assorted.staffs.api;

import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.staffs.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class StaffsTags {

    /** Blocks the power staff will not move, beyond the unbreakable ones it always refuses. */
    public static final TagKey<Block> POWER_STAFF_IMMOVABLE = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(Constants.MOD_ID, "power_staff_immovable"));
    /** Beside the loaders' {@code c:rods/blaze} and {@code c:rods/breeze}, and in {@code c:rods} with them. */
    public static final TagKey<Item> RODS_FROST = TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(LibCommonTags.COMMON_NAMESPACE, "rods/frost"));
}
