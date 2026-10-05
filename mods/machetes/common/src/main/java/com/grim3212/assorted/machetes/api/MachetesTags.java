package com.grim3212.assorted.machetes.api;

import com.grim3212.assorted.machetes.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class MachetesTags {

    /** What a machete cuts at its material's speed. */
    public static final TagKey<Block> MINEABLE_MACHETE = TagKey.create(Registries.BLOCK, id("mineable/machete"));
    public static final TagKey<Item> MACHETES = TagKey.create(Registries.ITEM, id("machetes"));

    private MachetesTags() {
    }

    private static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(Constants.MOD_ID, path);
    }
}
