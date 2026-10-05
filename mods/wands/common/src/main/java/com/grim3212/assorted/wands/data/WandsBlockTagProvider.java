package com.grim3212.assorted.wands.data;

import com.grim3212.assorted.lib.data.LibBlockTagProvider;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.wands.api.WandsTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class WandsBlockTagProvider extends LibBlockTagProvider {

    public WandsBlockTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup) {
        super(packOutput, lookup);
    }

    @Override
    public void addCommonTags(Function<TagKey<Block>, TagAppender<Block>> tagger) {
        tagger.apply(WandsTags.DESTRUCTIVE_SPARED_BLOCKS)
                .add(BuiltInRegistries.BLOCK.getResourceKey(Blocks.SPAWNER).orElseThrow())
                .addOptionalTag(LibCommonTags.Blocks.ORES)
                .addOptionalTag(LibCommonTags.Blocks.CHESTS);
        tagger.apply(WandsTags.MINING_SURFACE_BLOCKS)
                .addOptionalTag(LibCommonTags.Blocks.ORES);
    }
}
