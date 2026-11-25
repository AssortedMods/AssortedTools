package com.grim3212.assorted.throwingspears.common.item;

import com.grim3212.assorted.lib.core.item.IItemEnchantmentCondition;
import com.grim3212.assorted.lib.core.tool.ITiered;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import com.grim3212.assorted.throwingspears.common.entity.BetterSpearEntity;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;


/**
 * A throwable spear made of a configured material; its stats are data components fixed at construction.
 * It extends {@link TridentItem} for the charge-and-throw, and its use time and animation.
 */
public class BetterSpearItem extends TridentItem implements ITiered, IItemEnchantmentCondition {

    private static final float ATTACK_DAMAGE_BASE = 2.0F;
    private static final float ATTACK_SPEED = -2.4F;

    private final ToolTier tierHolder;

    public BetterSpearItem(Properties props, ToolTier tierHolder) {
        // TridentItem already spends one durability per hit, which 26.x needs a weapon component for.
        super(props.durability(tierHolder.getMaxUses())
                .attributes(attributes(tierHolder))
                .component(DataComponents.TOOL, TridentItem.createToolProperties()));
        this.tierHolder = tierHolder;
    }

    private static ItemAttributeModifiers attributes(ToolTier tierHolder) {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID, ATTACK_DAMAGE_BASE + tierHolder.getDamage(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID, ATTACK_SPEED, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    /** 1.21.1 has no repairable component; the tier's repair ingredient is this override. */
    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return this.tierHolder.getRepairIngredient().test(repairCandidate);
    }

    /**
     * 1.21.1 has no enchantable component; an item's enchantability is this override. NeoForge deprecates it for a
     * stack-aware overload that only it has and that defaults to this one, so common code can only override this.
     */
    @SuppressWarnings("deprecation")
    @Override
    public int getEnchantmentValue() {
        return this.tierHolder.getEnchantability();
    }

    @Override
    public void releaseUsing(ItemStack stack, Level level, LivingEntity entity, int ticks) {
        if (!(entity instanceof Player playerentity)) {
            return;
        }

        int i = this.getUseDuration(stack, entity) - ticks;
        if (i < 10) {
            return;
        }

        if (!level.isClientSide()) {
            stack.hurtAndBreak(1, playerentity, LivingEntity.getSlotForHand(entity.getUsedItemHand()));

            BetterSpearEntity spearEntity = new BetterSpearEntity(level, playerentity, stack);
            spearEntity.shootFromRotation(playerentity, playerentity.getXRot(), playerentity.getYRot(), 0.0F, 2.5F, 1.0F);
            if (playerentity.getAbilities().instabuild) {
                spearEntity.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }

            level.addFreshEntity(spearEntity);
            level.playSound(null, spearEntity, SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (!playerentity.getAbilities().instabuild) {
                playerentity.getInventory().removeItem(stack);
            }
        }

        playerentity.awardStat(Stats.ITEM_USED.get(this));
    }

    /**
     * Riptide and Channeling are vetoed: Riptide's spin attack lives in the {@code TridentItem#releaseUsing}
     * this overrides, and Channeling needs a {@code minecraft:trident} projectile. Everything else follows {@code #minecraft:enchantable/trident}.
     */
    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return !isTridentOnly(enchantment) && IItemEnchantmentCondition.supportedByDefault(stack, enchantment);
    }

    @Override
    public boolean isPrimaryItemFor(ItemStack stack, Holder<Enchantment> enchantment) {
        return !isTridentOnly(enchantment) && IItemEnchantmentCondition.primaryByDefault(stack, enchantment);
    }

    private static boolean isTridentOnly(Holder<Enchantment> enchantment) {
        return enchantment.is(Enchantments.RIPTIDE) || enchantment.is(Enchantments.CHANNELING);
    }

    @Override
    public ToolTier getToolTier() {
        return tierHolder;
    }
}
