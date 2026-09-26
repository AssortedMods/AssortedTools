package com.grim3212.assorted.tools.data;

import com.grim3212.assorted.lib.data.LibDamageTypeTagsProvider;
import com.grim3212.assorted.tools.api.util.ToolsDamageSources;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/** Thrown spears and boomerangs are projectiles, as vanilla's tridents are: Projectile Protection counts, endermen dodge. */
public class ToolsDamageTypeTagProvider extends LibDamageTypeTagsProvider {

    public ToolsDamageTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup);
    }

    @Override
    public void addCommonTags(Function<TagKey<DamageType>, TagAppender<DamageType>> tagger) {
        // Optional only because the damage types are hand-written resources, which datagen cannot see; they always load.
        tagger.apply(DamageTypeTags.IS_PROJECTILE).addOptional(ToolsDamageSources.SPEAR).addOptional(ToolsDamageSources.BOOMERANG);
    }
}
