package com.grim3212.assorted.staffs.data;

import com.grim3212.assorted.lib.data.LibBlockTagProvider;
import com.grim3212.assorted.staffs.api.StaffsTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class StaffsBlockTagProvider extends LibBlockTagProvider {

    public StaffsBlockTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookup) {
        super(packOutput, lookup);
    }

    @Override
    public void addCommonTags(Function<TagKey<Block>, TagAppender<Block>> tagger) {
        // Empty on purpose: a place for packs to add to. What pistons cannot push is already refused.
        tagger.apply(StaffsTags.POWER_STAFF_IMMOVABLE);
    }
}
