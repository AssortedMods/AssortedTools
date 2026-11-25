package com.grim3212.assorted.boomerangs.common.entity;

import com.grim3212.assorted.boomerangs.BoomerangsCommonMod;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public abstract class BoomerangEntity extends Entity {

    private BlockPos activatedPos;
    protected boolean isBouncing;
    private double bounceFactor;
    private float prevBoomerangRotation;
    private boolean turningAround;
    protected int timeBeforeTurnAround;
    List<ItemEntity> itemsPickedUp;
    private ItemStack selfStack = ItemStack.EMPTY;
    private InteractionHand hand;
    private static final EntityDataAccessor<Float> ROTATION = SynchedEntityData.defineId(BoomerangEntity.class, EntityDataSerializers.FLOAT);
    /** The player the boomerang flies back to, by UUID, resolved against the level each time it is needed. */
    private static final EntityDataAccessor<Optional<UUID>> RETURN_TO = SynchedEntityData.defineId(BoomerangEntity.class, EntityDataSerializers.OPTIONAL_UUID);

    /** A thrown boomerang flies out at half a block a tick. */
    private static final double SPEED = 0.5D;

    public BoomerangEntity(EntityType<BoomerangEntity> type, Level world) {
        super(type, world);
        this.bounceFactor = 0.84999999999999998D;
        this.turningAround = false;
        this.timeBeforeTurnAround = 30;
        this.itemsPickedUp = new ArrayList<ItemEntity>();
        this.hand = InteractionHand.MAIN_HAND;
    }

    public BoomerangEntity(EntityType<BoomerangEntity> type, Level worldIn, Player entity, ItemStack itemstack, InteractionHand hand) {
        this(type, worldIn);
        this.selfStack = itemstack;
        this.setRot(entity.getYRot(), entity.getXRot());
        double x = -Mth.sin((entity.getYRot() * 3.141593F) / 180F);
        double z = Mth.cos((entity.getYRot() * 3.141593F) / 180F);

        double motionX = SPEED * x * (double) Mth.cos((entity.getXRot() / 180F) * 3.141593F);
        double motionY = -SPEED * (double) Mth.sin((entity.getXRot() / 180F) * 3.141593F);
        double motionZ = SPEED * z * (double) Mth.cos((entity.getXRot() / 180F) * 3.141593F);
        this.setDeltaMovement(new Vec3(motionX, motionY, motionZ));
        setPos(entity.getX(), this.getReturnEntityY(entity), entity.getZ());
        xo = getX();
        yo = getY();
        zo = getZ();
        this.isBouncing = false;
        this.turningAround = false;
        this.hand = hand;
        this.setReturnTo(entity);
    }

    /** How many ticks a boomerang flies out to cover its range in blocks. */
    protected static int ticksForRange(int blocks) {
        return (int) Math.round(blocks / SPEED);
    }

    public double getReturnEntityY(Player entity) {
        return entity.getY() + entity.getEyeHeight() - 0.10000000149011612D;
    }

    @Override
    public void tick() {
        Player player = this.getReturnTo();

        Vec3 vec3d1 = this.position();
        Vec3 vec3d = this.position().add(this.getDeltaMovement());
        HitResult raytraceresult = this.level().clip(new ClipContext(vec3d1, vec3d, ClipContext.Block.OUTLINE, ClipContext.Fluid.ANY, this));

        if (raytraceresult != null) {
            if (raytraceresult.getType() == HitResult.Type.BLOCK) {
                BlockPos pos = BlockPos.containing(raytraceresult.getLocation());
                BlockState state = level().getBlockState(pos);

                if (state.canBeReplaced() && BoomerangsCommonMod.COMMON_CONFIG.breaksPlants.get() || state.getBlock() == Blocks.TORCH && BoomerangsCommonMod.COMMON_CONFIG.breaksTorches.get()) {
                    level().destroyBlock(pos, true);
                }

                if ((state.getBlock() instanceof LeverBlock || state.getBlock() instanceof ButtonBlock) && BoomerangsCommonMod.COMMON_CONFIG.hitsButtons.get()) {
                    if (timeBeforeTurnAround > 0 && BoomerangsCommonMod.COMMON_CONFIG.turnAroundButton.get()) {
                        timeBeforeTurnAround = 0;
                    }
                    if (player != null && (activatedPos == null || !activatedPos.equals(pos))) {
                        activatedPos = pos;
                        // useWithoutItem is the no-item interaction path and doesn't take a hand.
                        state.useWithoutItem(level(), player, (BlockHitResult) raytraceresult);
                    }
                }
            }
        }

        if (!turningAround) {
            Vec3 motionBefore = this.getDeltaMovement();
            this.move(MoverType.SELF, motionBefore);

            Vec3 motionAfter = this.getDeltaMovement();
            double newX = motionAfter.x;
            double newY = motionAfter.y;
            double newZ = motionAfter.z;

            boolean flag = false;
            if (motionAfter.x != motionBefore.x) {
                newX = -motionBefore.x;
                flag = true;
            }
            if (motionAfter.y != motionBefore.y) {
                newY = -motionBefore.y;
                flag = true;
            }
            if (motionAfter.z != motionBefore.z) {
                newZ = -motionBefore.z;
                flag = true;
            }
            if (flag) {
                isBouncing = true;
                this.setDeltaMovement(new Vec3(newX, newY, newZ).multiply(bounceFactor, bounceFactor, bounceFactor));
            }

            this.beforeTurnAround(player);

            if (timeBeforeTurnAround-- <= 0) {
                turningAround = true;
            }
        } else if (player != null) {
            double x = player.getX() - this.getX();
            double y = this.getReturnEntityY(player) - this.getY();
            double z = player.getZ() - this.getZ();
            double d = Math.sqrt(x * x + y * y + z * z);
            if (d < 1.5D) {
                setEntityDead();
            }
            this.setDeltaMovement((0.5D * x) / d, (0.5D * y) / d, (0.5D * z) / d);
            this.setPos(this.getX() + this.getDeltaMovement().x, this.getY() + this.getDeltaMovement().y, this.getZ() + this.getDeltaMovement().z);
        }

        determineRotation();
        prevBoomerangRotation = getBoomerangRotation();
        for (this.setBoomerangRotation(this.getBoomerangRotation() + 36F); this.getBoomerangRotation() > 360F; this.setBoomerangRotation(this.getBoomerangRotation() - 360F)) {
        }
        List<Entity> list = level().getEntities(this, this.getBoundingBox().expandTowards(0.5D, 0.5D, 0.5D));
        for (int i = 0; i < list.size(); i++) {
            Entity entity = list.get(i);
            if (entity instanceof ItemEntity) {
                itemsPickedUp.add((ItemEntity) entity);
                if (timeBeforeTurnAround > 0 && BoomerangsCommonMod.COMMON_CONFIG.turnAroundItem.get()) {
                    timeBeforeTurnAround = 0;
                }
                continue;
            }
            if (!(entity instanceof LivingEntity) || entity == player) {
                continue;
            }

            this.onEntityHit(entity, player);

            if (timeBeforeTurnAround > 0 && BoomerangsCommonMod.COMMON_CONFIG.turnAroundMob.get()) {
                timeBeforeTurnAround = 0;
            }
        }

        Iterator<ItemEntity> iterator = itemsPickedUp.iterator();
        while (iterator.hasNext()) {
            ItemEntity item = iterator.next();
            item.setDeltaMovement(0, 0, 0);
            if (item.isAlive()) {
                Vec3 pos = this.position();
                item.setPos(pos.x, pos.y, pos.z);
            }
        }

        super.tick();
    }

    public void beforeTurnAround(Player player) {
        // NO-OP
    }

    public void onEntityHit(Entity hitEntity, Player player) {
        // Damage is only dealt on the server.
        if (!this.level().isClientSide()) {
            hitEntity.hurt(causeNewDamage(this, player), getDamage(hitEntity, player));
        }
    }

    protected abstract int getDamage(Entity hitEntity, Player player);

    public abstract DamageSource causeNewDamage(BoomerangEntity entityboomerang, Entity entity);

    public void setEntityDead() {
        Player returnTo = this.getReturnTo();
        if (returnTo != null && !this.selfStack.isEmpty()) {
            if (this.hand == InteractionHand.OFF_HAND && returnTo.getOffhandItem().isEmpty()) {
                returnTo.setItemInHand(InteractionHand.OFF_HAND, selfStack);
            } else {
                returnTo.getInventory().add(selfStack);
            }
        }
        super.removeAfterChangingDimensions();
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(ROTATION, 0.0F);
        builder.define(RETURN_TO, Optional.empty());
    }

    public float getBoomerangRotation() {
        return this.getEntityData().get(ROTATION);
    }

    public void setBoomerangRotation(float rotationIn) {
        this.getEntityData().set(ROTATION, rotationIn);
    }

    @Nullable
    public UUID getReturnToId() {
        return this.entityData.get(RETURN_TO).orElse(null);
    }

    public void setReturnTo(@Nullable Player player) {
        this.setReturnToId(player == null ? null : player.getUUID());
    }

    private void setReturnToId(@Nullable UUID id) {
        this.entityData.set(RETURN_TO, Optional.ofNullable(id));
    }

    @Nullable
    public Player getReturnTo() {
        UUID id = this.getReturnToId();
        return id == null ? null : this.level().getPlayerByUUID(id);
    }

    public boolean isReturnTo(LivingEntity entityIn) {
        return entityIn == this.getReturnTo();
    }

    public void determineRotation() {
        Vec3 motion = this.getDeltaMovement();

        this.setYRot(-57.29578F * (float) Math.atan2(motion.x, motion.z));
        double d1 = Math.sqrt(motion.z * motion.z + motion.x * motion.x);
        this.setXRot(-57.29578F * (float) Math.atan2(motion.y, d1));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        this.isBouncing = tag.getBoolean("IsBouncing");
        this.bounceFactor = tag.contains("BounceFactor") ? tag.getDouble("BounceFactor") : 0.84999999999999998D;
        this.prevBoomerangRotation = tag.getFloat("PrevBoomerangRotation");
        this.setBoomerangRotation(tag.getFloat("BoomerangRotation"));
        this.turningAround = tag.getBoolean("TurningAround");
        this.timeBeforeTurnAround = tag.contains("TimeBeforeTurnAround") ? tag.getInt("TimeBeforeTurnAround") : 30;

        int[] activated = tag.getIntArray("ActivatedPos");
        this.activatedPos = activated.length == 3 ? new BlockPos(activated[0], activated[1], activated[2]) : null;

        // An int array UUID, which is also how the 26.x line stores it.
        this.setReturnToId(tag.hasUUID("ReturnTo") ? tag.getUUID("ReturnTo") : null);

        this.selfStack = ItemStack.parseOptional(this.registryAccess(), tag.getCompound("SelfStack"));

        this.hand = "OFF_HAND".equals(tag.getString("hand")) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putBoolean("IsBouncing", isBouncing);
        tag.putDouble("BounceFactor", bounceFactor);
        tag.putFloat("PrevBoomerangRotation", prevBoomerangRotation);
        tag.putFloat("BoomerangRotation", this.getBoomerangRotation());
        tag.putBoolean("TurningAround", turningAround);
        tag.putInt("TimeBeforeTurnAround", timeBeforeTurnAround);
        if (activatedPos != null) {
            tag.putIntArray("ActivatedPos", new int[]{activatedPos.getX(), activatedPos.getY(), activatedPos.getZ()});
        }

        UUID returnTo = this.getReturnToId();
        if (returnTo != null) {
            tag.putUUID("ReturnTo", returnTo);
        }

        // An empty stack cannot be saved, which a boomerang spawned without one still has.
        if (!this.selfStack.isEmpty()) {
            tag.put("SelfStack", this.selfStack.save(this.registryAccess()));
        }
        tag.putString("hand", this.hand.name());
    }
}
