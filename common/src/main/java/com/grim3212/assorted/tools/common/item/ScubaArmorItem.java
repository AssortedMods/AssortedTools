package com.grim3212.assorted.tools.common.item;

import com.grim3212.assorted.tools.api.item.ToolsArmorMaterials;
import com.grim3212.assorted.tools.common.item.configurable.ConfigurableArmorItem;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorType;
import org.jetbrains.annotations.Nullable;

/**
 * The scuba suit, which works in halves rather than as one set: the mask and tank let you breathe
 * and see down there, the legs and fins move you through the water.
 */
public class ScubaArmorItem extends ConfigurableArmorItem {

    /** Long enough that the wearer keeps it between refreshes, short enough to lapse on surfacing. */
    private static final int EFFECT_DURATION = 60;
    /**
     * Only refresh once the effect has nearly run out. Re-adding every tick would send the wearer
     * an effect update packet every tick, for an effect that has not changed.
     */
    private static final int REFRESH_BELOW = 20;
    /**
     * How often a working half costs its two pieces a point of durability. At five seconds a point
     * the mask outlasts eleven minutes underwater, which is a long dive and still a tank that runs
     * down. A half that is not doing anything - out of water, or missing its partner - is free.
     */
    private static final int WEAR_INTERVAL = 100;

    public ScubaArmorItem(ArmorType type, Properties builderIn) {
        super(ToolsArmorMaterials.SCUBA, type, builderIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        super.inventoryTick(stack, level, owner, slot);

        if (slot == null || !(owner instanceof LivingEntity wearer) || !wearer.isInWater()) {
            return;
        }

        // One piece of each half drives it, so the pair is checked once a tick rather than twice.
        if (slot == EquipmentSlot.HEAD && isScuba(wearer, EquipmentSlot.CHEST)) {
            refresh(wearer, MobEffects.CONDUIT_POWER);
            wear(level, wearer, EquipmentSlot.HEAD, EquipmentSlot.CHEST);
        } else if (slot == EquipmentSlot.LEGS && isScuba(wearer, EquipmentSlot.FEET)) {
            refresh(wearer, MobEffects.DOLPHINS_GRACE);
            wear(level, wearer, EquipmentSlot.LEGS, EquipmentSlot.FEET);
        }
    }

    /**
     * Runs the working half down. Both of its pieces wear at once, so a half fails as a half: the
     * first one to break takes the bonus with it, and the other is left to be repaired or paired up
     * again.
     */
    private static void wear(ServerLevel level, LivingEntity wearer, EquipmentSlot... slots) {
        if (level.getGameTime() % WEAR_INTERVAL != 0L) {
            return;
        }

        for (EquipmentSlot slot : slots) {
            wearer.getItemBySlot(slot).hurtAndBreak(1, wearer, slot);
        }
    }

    private static boolean isScuba(LivingEntity wearer, EquipmentSlot slot) {
        return wearer.getItemBySlot(slot).getItem() instanceof ScubaArmorItem;
    }

    private static void refresh(LivingEntity wearer, Holder<MobEffect> effect) {
        MobEffectInstance worn = wearer.getEffect(effect);
        if (worn != null && worn.getDuration() > REFRESH_BELOW) {
            return;
        }

        // Ambient and particle-free, like a beacon's: the suit should not trail bubbles of its own.
        wearer.addEffect(new MobEffectInstance(effect, EFFECT_DURATION, 0, true, false, true));
    }
}
