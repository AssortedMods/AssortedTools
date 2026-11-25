package com.grim3212.assorted.multitools.common.item;

import com.grim3212.assorted.lib.core.item.IItemEnchantmentCondition;
import com.grim3212.assorted.lib.core.tool.ITiered;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import com.grim3212.assorted.multitools.api.MultitoolsTags;
import net.minecraft.core.Holder;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Mines everything a shovel, pickaxe, axe or hoe would. A {@code SwordItem}, since on 1.21.1 that is what sweeps and
 * what a creative player cannot break blocks with; its sword mining rules are replaced by {@link #toolComponent()}.
 * Durability is the tier's scaled by {@code multiToolModifier}.
 */
public class MultiToolItem extends SwordItem implements ITiered, IItemEnchantmentCondition {

    private static final float ATTACK_SPEED = -2.8F;
    private static final List<Item> DELEGATE_TOOLS = List.of(Items.IRON_SWORD, Items.IRON_SHOVEL, Items.IRON_PICKAXE, Items.IRON_HOE, Items.IRON_AXE);
    /** Tried in turn on a right click: strip, scrape or wax off; path or douse a campfire; till. */
    private static final List<Item> TRANSFORMING_TOOLS = List.of(Items.IRON_AXE, Items.IRON_SHOVEL, Items.IRON_HOE);

    private final ToolTier tierHolder;

    public MultiToolItem(ToolTier tier, float durabilityModifier, Item.Properties builderIn) {
        super(new ScaledTier(tier, durabilityModifier), builderIn.attributes(DiggerItem.createAttributes(tier, attackDamage(tier), ATTACK_SPEED)));
        this.tierHolder = tier;
    }

    private static float attackDamage(ToolTier tier) {
        return tier.getAxeDamage() > tier.getDamage() ? tier.getAxeDamage() : tier.getDamage() + tier.getDamage();
    }

    /**
     * What the multitool mines and drops: everything in {@code mineable/multitool} at its material's speed, held back
     * by its material's harvest level, for one durability a block.
     */
    public Tool toolComponent() {
        return this.getTier().createToolProperties(MultitoolsTags.MINEABLE_MULTITOOL);
    }

    @Override
    public ToolTier getToolTier() {
        return this.tierHolder;
    }

    /**
     * Anything a sword, shovel, pickaxe, hoe or axe accepts can go on at an anvil. What the
     * enchanting table rolls stays with the multitool's own tags.
     */
    @Override
    public boolean supportsEnchantment(ItemStack stack, Holder<Enchantment> enchantment) {
        return IItemEnchantmentCondition.supportedByDefault(stack, enchantment) || DELEGATE_TOOLS.stream().anyMatch(tool -> IItemEnchantmentCondition.supportedByDefault(new ItemStack(tool), enchantment));
    }

    @Override
    public boolean isPrimaryItemFor(ItemStack stack, Holder<Enchantment> enchantment) {
        return IItemEnchantmentCondition.primaryByDefault(stack, enchantment);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        if (state.is(Blocks.COBWEB)) {
            return 15.0F;
        }

        boolean validBlock = state.is(BlockTags.MINEABLE_WITH_SHOVEL) ||
                state.is(BlockTags.MINEABLE_WITH_PICKAXE) ||
                state.is(BlockTags.MINEABLE_WITH_AXE) ||
                state.is(BlockTags.MINEABLE_WITH_HOE);

        return validBlock ? this.getToolTier().getEfficiency() : state.is(BlockTags.SWORD_EFFICIENT) ? 1.5F : super.getDestroySpeed(stack, state);
    }

    /**
     * Uses the axe's, shovel's and hoe's right clicks in turn, to strip, scrape, wax off, path, douse or till; each
     * wears the multitool in hand, not the tool it borrows from.
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        for (Item tool : TRANSFORMING_TOOLS) {
            InteractionResult result = tool.useOn(context);
            if (result != InteractionResult.PASS) {
                return result;
            }
        }

        return super.useOn(context);
    }

    /** The tier with the multitool's durability modifier applied; everything else is the tier's own. */
    private record ScaledTier(ToolTier tier, float durabilityModifier) implements Tier {

        @Override
        public int getUses() {
            return Math.max(1, (int) (this.tier.getUses() * this.durabilityModifier));
        }

        @Override
        public float getSpeed() {
            return this.tier.getSpeed();
        }

        @Override
        public float getAttackDamageBonus() {
            return this.tier.getAttackDamageBonus();
        }

        @Override
        public TagKey<Block> getIncorrectBlocksForDrops() {
            return this.tier.getIncorrectBlocksForDrops();
        }

        @Override
        public int getEnchantmentValue() {
            return this.tier.getEnchantmentValue();
        }

        @Override
        public Ingredient getRepairIngredient() {
            return this.tier.getRepairIngredient();
        }
    }
}
