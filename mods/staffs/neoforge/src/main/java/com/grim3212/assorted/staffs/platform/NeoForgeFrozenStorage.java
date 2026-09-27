package com.grim3212.assorted.staffs.platform;

import com.grim3212.assorted.staffs.Constants;
import com.grim3212.assorted.staffs.Family;
import com.mojang.serialization.Codec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/** Frozen as a NeoForge data attachment: saved with the mob, synced to every client tracking it. */
public class NeoForgeFrozenStorage implements IFrozenStorage {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Constants.MOD_ID);

    // Thawing sets false rather than removing, so the change reaches clients as a sync; only true is saved.
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> FROZEN = ATTACHMENT_TYPES.register("frozen",
            () -> AttachmentType.builder(() -> false).serialize(Codec.BOOL.fieldOf("frozen"), frozen -> frozen).sync(ByteBufCodecs.BOOL).build());

    /**
     * A mob frozen when this was all one mod is saved under assortedtools:frozen; the alias reads it back as this.
     * Lib's aliases only find built-in registries, and NeoForge's own are not among them yet while mods are constructed.
     */
    public static void aliasOldId() {
        // Mods are constructed in parallel, and the registry keeps its aliases in a plain map.
        synchronized (NeoForgeRegistries.ATTACHMENT_TYPES) {
            NeoForgeRegistries.ATTACHMENT_TYPES.addAlias(Identifier.fromNamespaceAndPath(Family.ID, "frozen"), Identifier.fromNamespaceAndPath(Constants.MOD_ID, "frozen"));
        }
    }

    @Override
    public boolean isFrozen(Entity entity) {
        // getData would attach a default to every mob it is asked about.
        return entity.hasData(FROZEN) && entity.getData(FROZEN);
    }

    @Override
    public void setFrozen(Entity entity, boolean frozen) {
        entity.setData(FROZEN, frozen);
    }
}
