package com.grim3212.assorted.extramaterials.api;

import com.grim3212.assorted.lib.util.LibCommonTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

import java.util.List;

public class ExtraMaterialsTags {

    /** The kinds of tool with a convention tag per material; see {@link #materialTools}. */
    public static final List<String> MATERIAL_TOOL_KINDS = List.of("swords", "pickaxes", "shovels", "axes", "hoes", "spears");

    /**
     * One kind of tool in one material, as {@code c:pickaxes/ruby}, so any mod can add its own or ask for a material's,
     * as Assorted Tech's extruders do. Filled here for vanilla's materials too.
     */
    public static TagKey<Item> materialTools(String kind, String material) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(LibCommonTags.COMMON_NAMESPACE, kind + "/" + material));
    }
}
