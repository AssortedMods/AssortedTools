package com.grim3212.assorted.throwingspears.data;

import com.grim3212.assorted.lib.data.LibDamageTypeTagsProvider;
import com.grim3212.assorted.throwingspears.api.ThrowingSpearsDamageSources;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagAppender;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/** Thrown spears are projectiles, as vanilla's tridents are: Projectile Protection counts, endermen dodge. */
public class ThrowingSpearsDamageTypeTagProvider extends LibDamageTypeTagsProvider {

    public ThrowingSpearsDamageTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup);
    }

    @Override
    public void addCommonTags(Function<TagKey<DamageType>, TagAppender<DamageType>> tagger) {
        // Optional only because the damage type is a hand-written resource, which datagen cannot see; it always loads.
        tagger.apply(DamageTypeTags.IS_PROJECTILE).addOptional(ThrowingSpearsDamageSources.SPEAR);
    }
}
