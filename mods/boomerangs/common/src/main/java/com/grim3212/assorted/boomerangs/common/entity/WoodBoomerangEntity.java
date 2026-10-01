package com.grim3212.assorted.boomerangs.common.entity;

import com.grim3212.assorted.boomerangs.BoomerangsCommonMod;
import com.grim3212.assorted.boomerangs.api.BoomerangsDamageSources;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class WoodBoomerangEntity extends BoomerangEntity {

    public WoodBoomerangEntity(EntityType<BoomerangEntity> type, Level world) {
        super(type, world);
        this.timeBeforeTurnAround = ticksForRange(BoomerangsCommonMod.COMMON_CONFIG.woodBoomerangRange.get());
    }

    public WoodBoomerangEntity(Level worldIn, Player entity, ItemStack itemstack, InteractionHand hand) {
        super(BoomerangsEntities.WOOD_BOOMERANG.get(), worldIn, entity, itemstack, hand);
        this.timeBeforeTurnAround = ticksForRange(BoomerangsCommonMod.COMMON_CONFIG.woodBoomerangRange.get());
    }

    @Override
    protected int getDamage(Entity hitEntity, Player player) {
        if (BoomerangsCommonMod.COMMON_CONFIG.woodBoomerangDamage.get() > 0) {
            return BoomerangsCommonMod.COMMON_CONFIG.woodBoomerangDamage.get();
        }

        return 0;
    }

    @Override
    public DamageSource causeNewDamage(BoomerangEntity entityboomerang, Entity entity) {
        return BoomerangsDamageSources.source(this.level(), BoomerangsDamageSources.BOOMERANG, entityboomerang, entity);
    }

}
