package com.grim3212.assorted.multitools.api;

import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.List;

public final class MultitoolsTags {

    /** Everything a pickaxe, axe, shovel or hoe mines, which a multitool mines at its material's speed. */
    public static final TagKey<Block> MINEABLE_MULTITOOL = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(LibCommonTags.COMMON_NAMESPACE, "mineable/multitool"));

    /** The five tools an extra material's multitool is made from, in recipe order. */
    public static final List<String> RECIPE_TOOL_KINDS = List.of("pickaxes", "shovels", "axes", "hoes", "swords");

    private MultitoolsTags() {
    }

    /** One kind of tool in one material, as {@code c:pickaxes/ruby}, which whatever mod adds those tools fills. */
    public static TagKey<Item> materialTools(String kind, String material) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(LibCommonTags.COMMON_NAMESPACE, kind + "/" + material));
    }
}
