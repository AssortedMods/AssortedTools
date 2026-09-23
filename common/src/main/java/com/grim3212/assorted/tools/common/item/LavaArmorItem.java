package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.api.item.ToolsArmorMaterials;
import com.grim3212.assorted.tools.common.effect.ToolsMobEffects;
import com.grim3212.assorted.tools.common.item.configurable.ConfigurableArmorItem;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

/**
 * The lava suit. Lava and fire take all four pieces, magma and campfires only the boots, and the
 * legs and boots give Lava Striding. The fire resistance is for the lava fog, which reads that
 * effect and nothing else - the protection itself is the two entity mixins' doing.
 */
public class LavaArmorItem extends ConfigurableArmorItem {

    private static final int EFFECT_DURATION = 60;
    private static final int REFRESH_BELOW = 20;
    /** A point of durability every four seconds. */
    private static final int WEAR_INTERVAL = 80;

    public LavaArmorItem(ArmorType type, Properties builderIn) {
        super(ToolsArmorMaterials.LAVA, type, builderIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, level, owner, slot);

        if (slot == null || !(owner instanceof LivingEntity wearer)) {
            return;
        }

        boolean whole = isWholeSuit(wearer);
        boolean striding = wearer.isInLava() && isLava(wearer, EquipmentSlot.LEGS) && isLava(wearer, EquipmentSlot.FEET);

        // One piece drives each effect, so it is refreshed once a tick rather than two or four times.
        if (whole && slot == EquipmentSlot.HEAD) {
            refresh(wearer, MobEffects.FIRE_RESISTANCE);
        }
        if (striding && slot == EquipmentSlot.LEGS) {
            refresh(wearer, ToolsMobEffects.LAVA_STRIDING.asHolder());
        }

        // A piece is only charged for what it is doing, so wearing the suit dry is free.
        boolean burning = wearer.isInLava() || inFire(wearer);
        boolean working = (whole && burning)
                || (striding && (slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET))
                || (slot == EquipmentSlot.FEET && onHotFloor(wearer));
        if (working) {
            wear(level, wearer, slot);
        }
    }

    /** What the whole suit is keeping off: the wearer is fire immune, so nothing burns them. */
    private static boolean inFire(LivingEntity wearer) {
        return wearer.level().getBlockState(wearer.blockPosition()).is(BlockTags.FIRE);
    }

    /** What burns only the feet, and so costs only the boots. Magma is stood on rather than in. */
    private static boolean onHotFloor(LivingEntity wearer) {
        BlockState inside = wearer.level().getBlockState(wearer.blockPosition());
        if (inside.is(BlockTags.CAMPFIRES) && inside.getValueOrElse(CampfireBlock.LIT, false)) {
            return true;
        }

        // Stepping carefully over magma burns nobody, so it costs nothing either.
        return !wearer.isSteppingCarefully() && wearer.getBlockStateOn().is(Blocks.MAGMA_BLOCK);
    }

    /** The boots on their own, which is all a hot floor takes. */
    public static boolean hasLavaBoots(LivingEntity wearer) {
        return isLava(wearer, EquipmentSlot.FEET);
    }

    private static boolean isLava(LivingEntity wearer, EquipmentSlot slot) {
        return wearer.getItemBySlot(slot).getItem() instanceof LavaArmorItem;
    }

    /** Lava touches all of a wearer at once, so protection is all or nothing. */
    public static boolean isWholeSuit(LivingEntity wearer) {
        return isLava(wearer, EquipmentSlot.HEAD) && isLava(wearer, EquipmentSlot.CHEST)
                && isLava(wearer, EquipmentSlot.LEGS) && isLava(wearer, EquipmentSlot.FEET);
    }

    private static void refresh(LivingEntity wearer, Holder<MobEffect> effect) {
        MobEffectInstance worn = wearer.getEffect(effect);
        if (worn != null && worn.getDuration() > REFRESH_BELOW) {
            return;
        }

        wearer.addEffect(new MobEffectInstance(effect, EFFECT_DURATION, 0, true, false, true));
    }

    private static void wear(ServerLevel level, LivingEntity wearer, EquipmentSlot... slots) {
        if (level.getGameTime() % WEAR_INTERVAL != 0L) {
            return;
        }

        for (EquipmentSlot slot : slots) {
            wearer.getItemBySlot(slot).hurtAndBreak(1, wearer, slot);
        }
    }
}
