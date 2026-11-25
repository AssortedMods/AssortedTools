package com.grim3212.assorted.multitools.data;

import com.grim3212.assorted.lib.data.LibBlockTagProvider;
import com.grim3212.assorted.multitools.api.MultitoolsTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class MultitoolsBlockTagProvider extends LibBlockTagProvider {

    public MultitoolsBlockTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup) {
        super(packOutput, lookup);
    }

    @Override
    public void addCommonTags(Function<TagKey<Block>, TagAppender<Block>> appender) {
        appender.apply(MultitoolsTags.MINEABLE_MULTITOOL)
                .addOptionalTag(BlockTags.MINEABLE_WITH_AXE.location())
                .addOptionalTag(BlockTags.MINEABLE_WITH_HOE.location())
                .addOptionalTag(BlockTags.MINEABLE_WITH_PICKAXE.location())
                .addOptionalTag(BlockTags.MINEABLE_WITH_SHOVEL.location());
    }
}
