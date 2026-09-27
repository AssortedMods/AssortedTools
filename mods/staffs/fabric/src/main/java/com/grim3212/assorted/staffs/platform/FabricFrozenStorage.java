package com.grim3212.assorted.staffs.platform;

import com.grim3212.assorted.staffs.Constants;
import com.grim3212.assorted.staffs.Family;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

/**
 * Frozen as a Fabric data attachment: saved with the mob, synced to every client tracking it. Only a
 * frozen mob carries it; removing it syncs like any other change.
 */
public class FabricFrozenStorage implements IFrozenStorage {

    public static final AttachmentType<Boolean> FROZEN = AttachmentRegistry.create(Identifier.fromNamespaceAndPath(Constants.MOD_ID, "frozen"),
            builder -> builder.persistent(Codec.BOOL).syncWith(ByteBufCodecs.BOOL, AttachmentSyncPredicate.all()));

    // A mob frozen when this was all one mod carries this; Fabric has no attachment aliases, so it is moved over when read.
    private static final AttachmentType<Boolean> OLD_FROZEN = AttachmentRegistry.create(Identifier.fromNamespaceAndPath(Family.ID, "frozen"), builder -> builder.persistent(Codec.BOOL));

    /**
     * Registers {@link #FROZEN} from mod init. A client only accepts synced attachment types it had
     * registered when it connected, so one first created when a mob is frozen never reaches it.
     */
    public static void init() {
    }

    @Override
    public boolean isFrozen(Entity entity) {
        if (entity.hasAttached(OLD_FROZEN) && Boolean.TRUE.equals(entity.removeAttached(OLD_FROZEN))) {
            entity.setAttached(FROZEN, true);
        }
        return entity.getAttachedOrElse(FROZEN, false);
    }

    @Override
    public void setFrozen(Entity entity, boolean frozen) {
        if (frozen) {
            entity.setAttached(FROZEN, true);
        } else {
            entity.removeAttached(FROZEN);
        }
    }
}
