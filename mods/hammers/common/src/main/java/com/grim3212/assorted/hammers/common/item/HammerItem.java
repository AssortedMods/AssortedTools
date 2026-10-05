package com.grim3212.assorted.hammers.common.item;

import com.grim3212.assorted.lib.core.tool.ConfigurableTieredItem;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class HammerItem extends ConfigurableTieredItem {

    public HammerItem(ToolTier tierHolder, Properties properties) {
        super(tierHolder, properties);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return 80f;
    }

    @Override
    public boolean isCorrectToolForDrops(ItemStack stack, BlockState state) {
        return true;
    }

    /**
     * {@code canDestroyBlock} is the hook both game modes ask before running the normal destroy path;
     * returning false aborts it, so the work happens here and then we say no, as {@code DebugStickItem} does.
     */
    @Override
    public boolean canDestroyBlock(ItemStack stack, BlockState state, Level level, BlockPos pos, LivingEntity user) {
        if (user instanceof Player player && !player.isCreative() && player.mayUseItemAt(pos, player.getDirection(), stack)) {
            if (!level.isClientSide()) {
                player.awardStat(Stats.BLOCK_MINED.get(level.getBlockState(pos).getBlock()));
                player.causeFoodExhaustion(0.005F);

                level.levelEvent(2001, pos, Block.getId(level.getBlockState(pos)));
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());

                stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
            }
            return false;
        }

        return true;
    }
}
