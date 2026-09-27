package com.grim3212.assorted.multitools.common.item;

import com.grim3212.assorted.lib.core.item.IItemEnchantmentCondition;
import com.grim3212.assorted.lib.core.tool.ITiered;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import com.grim3212.assorted.multitools.api.MultitoolsTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.Weapon;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

/**
 * Mines everything a shovel, pickaxe, axe or hoe would; builds its own properties since durability
 * is the tier's scaled by {@code multiToolModifier}, which {@code Properties#tool} can't express.
 */
public class MultiToolItem extends Item implements ITiered, IItemEnchantmentCondition {

    private static final float ATTACK_SPEED = -2.8F;
    private static final List<Item> DELEGATE_TOOLS = List.of(Items.IRON_SWORD, Items.IRON_SHOVEL, Items.IRON_PICKAXE, Items.IRON_HOE, Items.IRON_AXE);

    private final ToolTier tierHolder;

    public MultiToolItem(ToolTier tier, float durabilityModifier, Item.Properties builderIn) {
        super(builderIn.tool(scaledMaterial(tier, durabilityModifier), MultitoolsTags.MINEABLE_MULTITOOL, attackDamage(tier), ATTACK_SPEED, 0.0F)
                // Properties#tool defaults to Weapon(2); the multitool costs one durability per swing, not two.
                .component(DataComponents.WEAPON, new Weapon(1)));
        this.tierHolder = tier;
    }

    /**
     * The tier's material with the multitool durability modifier already applied.
     */
    private static ToolMaterial scaledMaterial(ToolTier tier, float durabilityModifier) {
        ToolMaterial material = tier.material();
        return new ToolMaterial(material.incorrectBlocksForDrops(), Math.max(1, (int) (material.durability() * durabilityModifier)), material.speed(), material.attackDamageBonus(), material.enchantmentValue(), material.repairItems());
    }

    private static float attackDamage(ToolTier tier) {
        return tier.getAxeDamage() > tier.getDamage() ? tier.getAxeDamage() : tier.getDamage() + tier.getDamage();
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

    /**
     * {@code canAttackBlock} is gone. This is the same rule expressed against its replacement: a
     * creative player cannot break blocks with a multitool.
     */
    @Override
    public boolean canDestroyBlock(ItemStack stack, BlockState state, Level level, BlockPos pos, LivingEntity user) {
        return !(user instanceof Player player && player.getAbilities().instabuild);
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
     * Vanilla's tool {@code useOn} reads only the context, never the item, so each delegate tool
     * (axe, shovel, hoe) is simply asked in turn to strip, scrape, wax, path, douse or till.
     */
    @Override
    public InteractionResult useOn(UseOnContext context) {
        for (Item tool : List.of(Items.IRON_AXE, Items.IRON_SHOVEL, Items.IRON_HOE)) {
            InteractionResult result = tool.useOn(context);
            if (result != InteractionResult.PASS) {
                return result;
            }
        }

        return super.useOn(context);
    }
}
