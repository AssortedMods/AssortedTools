package com.grim3212.assorted.shears.common.item;

import com.grim3212.assorted.lib.core.tool.ITiered;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.dispenser.ShearsDispenseItemBehavior;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Shearable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * Shears made of a configured material. {@code ShearsItem} supplies {@code useOn} and the wool,
 * leaves and vine mining rules, but not shearing mobs; see {@link #interactLivingEntity}.
 */
public class MaterialShears extends ShearsItem implements ITiered {

    private final ToolTier tierHolder;

    public MaterialShears(Properties props, ToolTier tierHolder) {
        // 0.952 is vanilla iron shears' durability ratio to iron tools, kept so every material stays proportional.
        super(props.durability((int) Math.rint(tierHolder.getMaxUses() * 0.952F))
                .component(DataComponents.TOOL, ShearsItem.createToolProperties()));
        this.tierHolder = tierHolder;

        DispenserBlock.registerBehavior(this, new ShearsDispenseItemBehavior());
    }

    @Override
    public ToolTier getToolTier() {
        return this.tierHolder;
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

    /**
     * NeoForge's patched {@code super} routes through {@code IShearable} and covers modded mobs, so
     * it goes first; on Fabric it passes and the vanilla {@link Shearable} fallback runs instead. NeoForge deprecates
     * {@code Shearable} for its {@code IShearable}, but vanilla's interface is all Fabric has.
     */
    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        InteractionResult result = super.interactLivingEntity(stack, player, target, hand);
        if (result != InteractionResult.PASS) {
            return result;
        }

        if (!(target instanceof Shearable shearable)) {
            return InteractionResult.PASS;
        }

        if (!(target.level() instanceof ServerLevel)) {
            return shearable.readyForShearing() ? InteractionResult.SUCCESS : InteractionResult.PASS;
        }

        if (!shearable.readyForShearing()) {
            return InteractionResult.CONSUME;
        }

        shearable.shear(SoundSource.PLAYERS);
        target.gameEvent(GameEvent.SHEAR, player);
        stack.hurtAndBreak(1, player, LivingEntity.getSlotForHand(hand));

        // What vanilla's own shearing returns on the server.
        return InteractionResult.CONSUME;
    }
}
