package com.grim3212.assorted.machetes.data;

import com.grim3212.assorted.lib.data.LibBlockTagProvider;
import com.grim3212.assorted.machetes.api.MachetesTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class MachetesBlockTagProvider extends LibBlockTagProvider {

    public MachetesBlockTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup) {
        super(packOutput, lookup);
    }

    @Override
    public void addCommonTags(Function<TagKey<Block>, TagAppender<Block>> appender) {
        TagAppender<Block> machete = appender.apply(MachetesTags.MINEABLE_MACHETE)
                .addTag(BlockTags.SWORD_EFFICIENT)
                .addTag(BlockTags.WOOL)
                .addTag(BlockTags.CAVE_VINES);
        for (Block block : new Block[]{Blocks.CACTUS, Blocks.SUGAR_CANE, Blocks.HANGING_ROOTS, Blocks.WEEPING_VINES, Blocks.WEEPING_VINES_PLANT, Blocks.TWISTING_VINES, Blocks.TWISTING_VINES_PLANT}) {
            machete.add(BuiltInRegistries.BLOCK.getResourceKey(block).orElseThrow());
        }
    }
}
