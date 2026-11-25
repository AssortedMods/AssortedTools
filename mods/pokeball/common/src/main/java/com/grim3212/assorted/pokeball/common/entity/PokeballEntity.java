package com.grim3212.assorted.pokeball.common.entity;

import com.grim3212.assorted.pokeball.common.item.PokeballDataComponents;
import com.grim3212.assorted.pokeball.common.item.CapturedEntity;
import com.grim3212.assorted.pokeball.common.item.PokeballItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

import java.util.Optional;

public class PokeballEntity extends ThrowableItemProjectile {

    public PokeballEntity(EntityType<? extends PokeballEntity> type, Level worldIn) {
        super(type, worldIn);
    }

    public PokeballEntity(EntityType<? extends PokeballEntity> type, double x, double y, double z, Level worldIn, ItemStack stack) {
        super(type, x, y, z, worldIn);
        this.setItem(stack);
    }

    public PokeballEntity(LivingEntity livingEntityIn, Level worldIn, ItemStack stack) {
        // ThrowableItemProjectile syncs the thrown stack itself, so getItem()/setItem() are the pokeball.
        super(PokeballEntities.POKEBALL.get(), livingEntityIn, worldIn);
        this.setItem(stack);
    }

    @Override
    protected void onHit(HitResult result) {
        if (!(this.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        // getItem() hands back the live synched stack, so it is copied before being changed.
        ItemStack currentPokeball = this.getItem().copy();
        // Read off the ball itself, which saves with the entity: a field set when thrown was lost on a reload mid flight.
        boolean hasEntity = !CapturedEntity.of(currentPokeball).isEmpty();

        if (result.getType() == HitResult.Type.BLOCK) {
            if (hasEntity) {
                Optional<Entity> loadEntity = EntityType.create(CapturedEntity.of(currentPokeball).entity(), serverLevel);
                if (loadEntity.isPresent()) {
                    Entity spawnEntity = loadEntity.get();
                    spawnEntity.moveTo(this.getX(), this.getY() + 1.0D, this.getZ(), this.getYRot(), 0.0F);
                    serverLevel.addFreshEntity(spawnEntity);
                }

                // Always reset pokeball
                currentPokeball = new ItemStack(PokeballItems.POKEBALL.get());
            }
        } else if (result.getType() == HitResult.Type.ENTITY) {
            Entity hitEntity = ((EntityHitResult) result).getEntity();
            if (hitEntity != null && !hasEntity && !(hitEntity instanceof Player || hitEntity instanceof EnderDragon || hitEntity instanceof EnderDragonPart)) {
                if (hitEntity instanceof LivingEntity livingEntity) {
                    CompoundTag entity = new CompoundTag();
                    // Entity#save writes the encode id itself, so no accessor mixin is needed here.
                    if (livingEntity.save(entity)) {
                        entity.putString("pokeball_name", livingEntity.getType().getDescriptionId());

                        currentPokeball.set(PokeballDataComponents.CAPTURED_ENTITY.get(), new CapturedEntity(entity));
                        currentPokeball.setCount(1);

                        hitEntity.discard();
                    }
                }
            }
        }

        this.spawnAtLocation(currentPokeball, 0.2F);
        serverLevel.broadcastEntityEvent(this, (byte) 3);
        this.discard();
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            for (int i = 0; i < 8; ++i) {
                this.level().addParticle(ParticleTypes.ITEM_SNOWBALL, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    protected Item getDefaultItem() {
        return PokeballItems.POKEBALL.get();
    }

}
