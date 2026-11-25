package com.grim3212.assorted.staffs.gametest;

import com.grim3212.assorted.staffs.Constants;
import com.grim3212.assorted.staffs.common.item.FrozenMobs;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cow;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/** A mob frozen when this was all one mod, Assorted Tools, was saved under assortedtools:frozen and loads still frozen. */
final class FrozenMigrationTests {

    private static final List<String> ATTACHMENT_KEYS = List.of("neoforge:attachments", "fabric:attachments");

    private FrozenMigrationTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("assortedtools_frozen_mobs_stay_frozen", FrozenMigrationTests::assortedtoolsFrozenMobsStayFrozen);
    }

    private static void assortedtoolsFrozenMobsStayFrozen(GameTestHelper helper) {
        Cow cow = helper.spawn(EntityType.COW, new BlockPos(4, 1, 4));
        FrozenMobs.freeze(cow);

        CompoundTag saved = cow.saveWithoutId(new CompoundTag());

        // The same save, with the attachment under the id Assorted Tools gave it.
        boolean renamed = false;
        for (String key : ATTACHMENT_KEYS) {
            CompoundTag attachments = saved.getCompound(key);
            Tag frozen = attachments.get(Constants.MOD_ID + ":frozen");
            if (frozen != null) {
                attachments.remove(Constants.MOD_ID + ":frozen");
                attachments.put(Constants.FAMILY_ID + ":frozen", frozen);
                saved.put(key, attachments);
                renamed = true;
            }
        }
        helper.assertTrue(renamed, "a frozen cow saved no " + Constants.MOD_ID + ":frozen attachment to rename, only " + saved.getAllKeys());

        Cow reloaded = helper.spawn(EntityType.COW, new BlockPos(6, 1, 4));
        reloaded.load(saved);

        helper.assertTrue(FrozenMobs.isFrozen(reloaded), "a cow frozen under " + Constants.FAMILY_ID + ":frozen thawed when it was loaded");
        helper.assertTrue(FrozenMobs.thaw(reloaded) && !FrozenMobs.isFrozen(reloaded), "a cow frozen under the old id could not be thawed");
        helper.succeed();
    }
}
