package com.grim3212.assorted.ultimatefist.common.item;

import com.grim3212.assorted.ultimatefist.UltimateFistCommonMod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The reward for assembling all eight fragments. Not a tiered item: it reads its numbers from the
 * ultimate tier's configuration, and is not enchantable, so an enchanting table won't take it.
 */
public class UltimateFistItem extends Item {

    private static final float ATTACK_SPEED = -0.5F;

    public UltimateFistItem(Properties props) {
        super(props.durability(UltimateFistCommonMod.COMMON_CONFIG.ultimateItemTier.getMaxUses())
                .fireResistant()
                .attributes(attributes()));
    }

    private static ItemAttributeModifiers attributes() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, UltimateFistCommonMod.COMMON_CONFIG.ultimateItemTier.getDamage(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, ATTACK_SPEED, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    /** A hit counts, so {@link #postHurtEnemy} spends the durability, as a sword's does. */
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        return true;
    }

    /** The fist costs one durability per swing. */
    @Override
    public void postHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.hurtAndBreak(1, attacker, EquipmentSlot.MAINHAND);
    }

    /** It has durability, which alone would let it into an enchanting table. */
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return UltimateFistCommonMod.COMMON_CONFIG.ultimateItemTier.getEfficiency();
    }

    /**
     * The fist carries no {@code minecraft:tool} component, so vanilla's mineBlock would do nothing
     * at all. This keeps the durability cost per mined block it has always had.
     */
    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity user) {
        if (!level.isClientSide() && state.getDestroySpeed(level, pos) != 0.0F) {
            stack.hurtAndBreak(1, user, EquipmentSlot.MAINHAND);
        }

        return true;
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return true;
    }

}
