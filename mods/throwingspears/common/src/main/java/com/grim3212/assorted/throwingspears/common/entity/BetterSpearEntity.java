package com.grim3212.assorted.throwingspears.common.entity;

import com.grim3212.assorted.throwingspears.ThrowingSpearsCommonMod;
import com.grim3212.assorted.throwingspears.api.ThrowingSpearsDamageSources;
import com.grim3212.assorted.throwingspears.common.enchantment.ThrowingSpearsEnchantments;
import com.grim3212.assorted.throwingspears.common.item.BetterSpearItem;
import com.grim3212.assorted.throwingspears.common.item.ThrowingSpearsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BetterSpearEntity extends AbstractArrow {
    private static final EntityDataAccessor<Byte> ID_LOYALTY = SynchedEntityData.defineId(BetterSpearEntity.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Boolean> ID_FOIL = SynchedEntityData.defineId(BetterSpearEntity.class, EntityDataSerializers.BOOLEAN);
    /**
     * {@link AbstractArrow} owns the pickup stack but never syncs it, and the renderer needs it for
     * texture, so it's mirrored into synched data for the client; server side lives in {@code getPickupItemStackOrigin()}.
     */
    private static final EntityDataAccessor<ItemStack> SPEAR_STACK = SynchedEntityData.defineId(BetterSpearEntity.class, EntityDataSerializers.ITEM_STACK);
    public int clientSideReturnSpearTickCount;
    private int bounceCount;
    private boolean effectTriggered;
    private boolean dealtDamage;

    public BetterSpearEntity(EntityType<? extends BetterSpearEntity> type, Level level) {
        super(type, level);
    }

    public BetterSpearEntity(Level level, LivingEntity entity, ItemStack stack) {
        super(ThrowingSpearsEntities.BETTER_SPEAR.get(), entity, level, stack, null);
        this.entityData.set(SPEAR_STACK, stack.copy());
        this.entityData.set(ID_LOYALTY, this.getLoyaltyFromItem(stack));
        this.entityData.set(ID_FOIL, stack.hasFoil());
    }

    private float getDamage(ItemStack stack) {
        Item item = stack.getItem();
        if (item instanceof BetterSpearItem) {
            return 5.0F + ((BetterSpearItem) item).getToolTier().getDamage();
        }
        return 5.0F;
    }

    /**
     * Loyalty is an enchantment value effect resolved against the server's enchantment registry, so
     * it's only knowable server side. Mirrors {@code ThrownTrident#getLoyaltyFromItem}.
     */
    private byte getLoyaltyFromItem(ItemStack stack) {
        return this.level() instanceof ServerLevel serverLevel
                ? (byte) Mth.clamp(EnchantmentHelper.getTridentReturnToOwnerAcceleration(serverLevel, stack, this), 0, 127)
                : 0;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ID_LOYALTY, (byte) 0);
        builder.define(ID_FOIL, false);
        builder.define(SPEAR_STACK, ItemStack.EMPTY);
    }

    public ItemStack getSpearStack() {
        return this.entityData.get(SPEAR_STACK);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ThrowingSpearsItems.WOOD_THROWING_SPEAR.get());
    }

    @Override
    public ItemStack getWeaponItem() {
        return this.getPickupItemStackOrigin();
    }

    public boolean isFoil() {
        return this.entityData.get(ID_FOIL);
    }

    @Override
    public void tick() {
        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        Entity entity = this.getOwner();
        if ((this.dealtDamage || this.isNoPhysics()) && entity != null) {
            int i = this.entityData.get(ID_LOYALTY);
            if (i > 0 && !this.isAcceptibleReturnOwner()) {
                if (this.level() instanceof ServerLevel serverLevel && this.pickup == AbstractArrow.Pickup.ALLOWED) {
                    this.spawnAtLocation(serverLevel, this.getPickupItem(), 0.1F);
                }

                this.discard();
            } else if (i > 0) {
                this.setNoPhysics(true);
                Vec3 vector3d = new Vec3(entity.getX() - this.getX(), entity.getEyeY() - this.getY(), entity.getZ() - this.getZ());
                this.setPosRaw(this.getX(), this.getY() + vector3d.y * 0.015D * (double) i, this.getZ());
                if (this.level().isClientSide()) {
                    this.yOld = this.getY();
                }

                double d0 = 0.05D * (double) i;
                this.setDeltaMovement(this.getDeltaMovement().scale(0.95D).add(vector3d.normalize().scale(d0)));
                if (this.clientSideReturnSpearTickCount == 0) {
                    this.playSound(SoundEvents.TRIDENT_RETURN, 10.0F, 1.0F);
                }

                ++this.clientSideReturnSpearTickCount;
            }
        }

        super.tick();
    }

    private boolean isAcceptibleReturnOwner() {
        Entity entity = this.getOwner();
        if (entity != null && entity.isAlive()) {
            return !(entity instanceof ServerPlayer) || !entity.isSpectator();
        } else {
            return false;
        }
    }

    @Override
    @Nullable
    protected EntityHitResult findHitEntity(Vec3 vector3D, Vec3 vector3D1) {
        return this.dealtDamage ? null : super.findHitEntity(vector3D, vector3D1);
    }

    @Override
    protected void onHitBlock(BlockHitResult rayTrace) {
        if (!this.effectTriggered) {
            int maxBounces = ThrowingSpearsEnchantments.getMaxBounces(this.getSpearStack());

            if (this.bounceCount < maxBounces) {
                bounceCount++;
                Vec3 motion = this.getDeltaMovement();

                motion = motion.scale(this.bounceCount == 1 ? 0.42F : 0.99F);
                this.setDeltaMovement(motion.x, motion.y * -1D, motion.z);
                level().playSound((Player) null, this.blockPosition(), SoundEvents.SLIME_SQUISH_SMALL, SoundSource.PLAYERS, 1.0F, 1.2F / (this.random.nextFloat() * 0.2F + 0.9F));
                this.spawnSlimeParticles();
            } else {
                super.onHitBlock(rayTrace);
                if (maxBounces > 0) {
                    this.spawnSlimeParticles();
                }
                this.tryUnstable(rayTrace.getBlockPos());
                this.tryConductivity(blockPosition());
                this.tryFlammable(rayTrace.getBlockPos());
                this.effectTriggered = true;
            }
        } else {
            super.onHitBlock(rayTrace);
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult rayTrace) {
        Entity hitEntity = rayTrace.getEntity();
        Entity owner = this.getOwner();
        float f = this.getDamage(this.getSpearStack());
        DamageSource damageSource = ThrowingSpearsDamageSources.source(this.level(), ThrowingSpearsDamageSources.SPEAR, this, owner == null ? this : owner);

        // Weapon-specific damage bonuses are enchantment value effects applied by EnchantmentHelper against the victim and damage source.
        if (this.level() instanceof ServerLevel serverLevel) {
            f = EnchantmentHelper.modifyDamage(serverLevel, this.getWeaponItem(), hitEntity, damageSource, f);
        }

        this.dealtDamage = true;
        SoundEvent soundevent = SoundEvents.TRIDENT_HIT;
        // Entity#hurtOrSimulate is @Deprecated; this is its body, split over the two sides.
        boolean hurt = this.level() instanceof ServerLevel hurtLevel ? hitEntity.hurtServer(hurtLevel, damageSource, f) : hitEntity.hurtClient(damageSource);
        if (hurt) {
            if (hitEntity.is(EntityTypes.ENDERMAN)) {
                return;
            }

            if (hitEntity instanceof LivingEntity livingentity1) {
                if (this.level() instanceof ServerLevel serverLevel) {
                    EnchantmentHelper.doPostAttackEffectsWithItemSource(serverLevel, livingentity1, damageSource, this.getWeaponItem());
                }

                this.doPostHurtEffects(livingentity1);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01D, -0.1D, -0.01D));

        if (!this.effectTriggered) {
            this.tryConductivity(hitEntity.blockPosition());
            this.tryFlammable(hitEntity.blockPosition());
            this.tryUnstable(hitEntity.blockPosition());
            this.effectTriggered = true;
        }

        this.playSound(soundevent, 1.0F, 1.0F);
    }

    private void spawnSlimeParticles() {
        for (int j = 0; j < 8; ++j) {
            float f = this.random.nextFloat() * ((float) Math.PI * 2F);
            float f1 = this.random.nextFloat() * 0.5F + 0.5F;
            float f2 = Mth.sin(f) * (float) 1 * 0.5F * f1;
            float f3 = Mth.cos(f) * (float) 1 * 0.5F * f1;
            this.level().addParticle(ParticleTypes.ITEM_SLIME, this.getX() + (double) f2, this.getY(), this.getZ() + (double) f3, 0.0D, 0.0D, 0.0D);
        }
    }

    /**
     * The configured chance modifier for a level of Conductivity: a smaller number strikes more often.
     * The configured list is used only when it has one entry per level, each a valid chance; otherwise defaults apply.
     */
    private float conductiveChances(int conductivityLevel) {
        List<Float> defaultChances = List.of(0.6F, 0.3F, 0.1F);
        List<? extends Float> chances = ThrowingSpearsCommonMod.COMMON_CONFIG.conductivityLightningChances.get();

        // An enchantment level is not bounded by its definition's max_level.
        int index = Mth.clamp(conductivityLevel, 0, defaultChances.size() - 1);

        if (chances != null && chances.size() == defaultChances.size() && chances.stream().allMatch(chance -> chance >= 0.0F && chance < 1.0F)) {
            return chances.get(index);
        }

        return defaultChances.get(index);
    }

    private void tryConductivity(BlockPos pos) {
        int conductivity = ThrowingSpearsEnchantments.getConductivity(this.getSpearStack());
        boolean flag = conductivity > 0 && this.random.nextDouble() <= 1.0D - conductiveChances(conductivity - 1);

        if (this.level() instanceof ServerLevel serverLevel && flag) {
            if (serverLevel.canSeeSky(pos)) {
                Entity owner = this.getOwner();
                LightningBolt lightningboltentity = EntityTypes.LIGHTNING_BOLT.create(serverLevel, EntitySpawnReason.TRIGGERED);
                if (lightningboltentity != null) {
                    lightningboltentity.snapTo(Vec3.atBottomCenterOf(pos));
                    lightningboltentity.setCause(owner instanceof ServerPlayer ? (ServerPlayer) owner : null);
                    serverLevel.addFreshEntity(lightningboltentity);
                }
            }
        }
    }

    private void tryUnstable(BlockPos pos) {
        int instability = ThrowingSpearsEnchantments.getInstability(this.getSpearStack());

        if (instability > 0) {
            if (!level().isClientSide()) {
                level().explode(null, this.getX(), this.getY(), this.getZ(), instability * 2F, Level.ExplosionInteraction.BLOCK);
            }
        }
    }

    private void tryFlammable(BlockPos pos) {
        boolean flammable = ThrowingSpearsEnchantments.hasFlammable(this.getSpearStack());

        if (flammable) {
            for (int fire = 0; fire < 6; ++fire) {
                BlockPos blockPos = pos.offset(this.random.nextInt(3) - 1, this.random.nextInt(3) - 1, this.random.nextInt(3) - 1);

                if (this.level().getBlockState(blockPos).isAir() && BaseFireBlock.canBePlacedAt(level(), blockPos, getDirection())) {
                    level().setBlockAndUpdate(blockPos, BaseFireBlock.getState(this.level(), blockPos));
                }
            }
        }
    }

    @Override
    public boolean fireImmune() {
        return ThrowingSpearsEnchantments.hasFlammable(this.getSpearStack()) || ThrowingSpearsEnchantments.getConductivity(this.getSpearStack()) > 0 || super.fireImmune();
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.TRIDENT_HIT_GROUND;
    }

    @Override
    public void playerTouch(Player player) {
        Entity entity = this.getOwner();
        if (entity == null || entity.getUUID() == player.getUUID()) {
            super.playerTouch(player);
        }
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("DealtDamage", this.dealtDamage);
        output.putBoolean("EffectTriggered", this.effectTriggered);
        output.putInt("BounceCount", this.bounceCount);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.dealtDamage = input.getBooleanOr("DealtDamage", false);
        this.effectTriggered = input.getBooleanOr("EffectTriggered", false);
        this.bounceCount = input.getIntOr("BounceCount", 0);
        // The synched copy is refreshed from the pickup stack the superclass just read back.
        ItemStack stack = this.getPickupItemStackOrigin();
        this.entityData.set(SPEAR_STACK, stack.copy());
        this.entityData.set(ID_LOYALTY, this.getLoyaltyFromItem(stack));
        this.entityData.set(ID_FOIL, stack.hasFoil());
    }

    @Override
    public void tickDespawn() {
        int i = this.entityData.get(ID_LOYALTY);
        if (this.pickup != AbstractArrow.Pickup.ALLOWED || i <= 0) {
            super.tickDespawn();
        }

    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }
}
