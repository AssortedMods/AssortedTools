package com.grim3212.assorted.pokeball.common.item;

import com.grim3212.assorted.lib.core.item.IStackTooltipProvider;
import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.function.Consumer;

/**
 * The mob a pokeball holds: {@code Entity#save}'s tag plus a {@code pokeball_name} key naming the
 * entity type. Empty on an empty ball; every pokeball carries one. It is also the ball's tooltip.
 */
public record CapturedEntity(CompoundTag entity) implements IStackTooltipProvider {

    public static final CapturedEntity EMPTY = new CapturedEntity(new CompoundTag());
    public static final Codec<CapturedEntity> CODEC = CompoundTag.CODEC.xmap(CapturedEntity::new, CapturedEntity::entity);
    public static final StreamCodec<ByteBuf, CapturedEntity> STREAM_CODEC = ByteBufCodecs.COMPOUND_TAG.map(CapturedEntity::new, CapturedEntity::entity);

    // A CompoundTag is mutable, and a component must not change once it is on a stack.
    public CapturedEntity {
        entity = entity.copy();
    }

    @Override
    public CompoundTag entity() {
        return entity.copy();
    }

    public boolean isEmpty() {
        return entity.isEmpty();
    }

    public static CapturedEntity of(ItemStack stack) {
        return stack.getOrDefault(PokeballDataComponents.CAPTURED_ENTITY.get(), EMPTY);
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> tooltip, TooltipFlag flag, ItemStack stack) {
        if (this.isEmpty()) {
            tooltip.accept(Component.translatable("tooltip.pokeball.empty").withStyle(ChatFormatting.GRAY));
            return;
        }

        String entityName = entity.contains("pokeball_name", Tag.TAG_STRING) ? entity.getString("pokeball_name") : entity.getString("id");
        Component storedName = Component.translatable(entityName).withStyle(ChatFormatting.AQUA);

        // An entity's custom name is saved as a JSON string, read back against the world's registries.
        Component customName = entity.contains("CustomName", Tag.TAG_STRING) && context.registries() != null
                ? Component.Serializer.fromJson(entity.getString("CustomName"), context.registries()) : null;

        if (customName != null) {
            tooltip.accept(Component.translatable("tooltip.pokeball.stored_custom_name", customName.copy().withStyle(ChatFormatting.BLUE, ChatFormatting.ITALIC), storedName));
        } else {
            tooltip.accept(Component.translatable("tooltip.pokeball.stored", storedName));
        }
    }
}
