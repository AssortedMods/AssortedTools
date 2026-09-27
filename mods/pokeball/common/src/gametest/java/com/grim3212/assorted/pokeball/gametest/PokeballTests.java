package com.grim3212.assorted.pokeball.gametest;

import com.grim3212.assorted.pokeball.common.entity.PokeballEntity;
import com.grim3212.assorted.pokeball.common.item.CapturedEntity;
import com.grim3212.assorted.pokeball.common.item.PokeballDataComponents;
import com.grim3212.assorted.pokeball.common.item.PokeballItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.grim3212.assorted.lib.test.TestSupport.*;

/** A pokeball catches the mob it hits and lets it out where it lands, and its tooltip says what it holds. */
final class PokeballTests {

    private PokeballTests() {
    }

    static void register(BiConsumer<String, Consumer<GameTestHelper>> out) {
        out.accept("pokeball_captures_and_releases", PokeballTests::pokeballCapturesAndReleases);
        out.accept("pokeball_tooltip_names_what_it_holds", PokeballTests::pokeballTooltipNamesWhatItHolds);
    }

    private static void pokeballCapturesAndReleases(GameTestHelper helper) {
        ServerPlayer player = survivalPlayer(helper, ItemStack.EMPTY);
        stand(helper, player, new BlockPos(1, 1, 1));

        Cow cow = helper.spawn(EntityTypes.COW, new BlockPos(4, 1, 6));
        throwPokeball(helper, player, new ItemStack(PokeballItems.POKEBALL.get()), cow.getBoundingBox().getCenter());

        helper.startSequence()
                .thenWaitUntil(() -> {
                    helper.assertEntityNotPresent(EntityTypes.COW);
                    helper.assertTrue(!filledPokeballs(helper).isEmpty(), "the pokeball dropped nothing after capturing");
                })
                .thenExecute(() -> {
                    ItemStack captured = filledPokeballs(helper).get(0).copy();
                    CompoundTag stored = CapturedEntity.of(captured).entity();
                    helper.assertValueEqual(stored.getStringOr("id", ""), "minecraft:cow", "the captured entity id");

                    helper.killAllEntitiesOfClass(ItemEntity.class);
                    // Aimed at the floor: a block hit is what releases, an entity hit is what captures.
                    throwPokeball(helper, player, captured, helper.absoluteVec(Vec3.atCenterOf(new BlockPos(4, 0, 6))));
                })
                .thenWaitUntil(() -> helper.assertEntityPresent(EntityTypes.COW))
                .thenSucceed();
    }

    private static void pokeballTooltipNamesWhatItHolds(GameTestHelper helper) {
        DataComponentType<CapturedEntity> type = PokeballDataComponents.CAPTURED_ENTITY.get();

        ItemStack empty = new ItemStack(PokeballItems.POKEBALL.get());
        helper.assertValueEqual(tooltipKeys(helper, empty, type), List.of("tooltip.pokeball.empty"), "an empty pokeball's tooltip");

        CompoundTag cow = new CompoundTag();
        cow.putString("id", "minecraft:cow");
        cow.putString("pokeball_name", EntityTypes.COW.getDescriptionId());
        ItemStack full = new ItemStack(PokeballItems.POKEBALL.get());
        full.set(type, new CapturedEntity(cow));
        helper.assertValueEqual(tooltipKeys(helper, full, type), List.of("tooltip.pokeball.stored"), "a full pokeball's tooltip");

        if (onNeoForge()) {
            helper.assertTrue(fullTooltipKeys(helper, full).contains("tooltip.pokeball.stored"), "the pokeball's component line is missing from its tooltip");
        }
        helper.succeed();
    }

    private static void throwPokeball(GameTestHelper helper, ServerPlayer player, ItemStack ball, Vec3 at) {
        Vec3 from = helper.absoluteVec(new Vec3(4.5D, 1.8D, 1.5D));
        PokeballEntity thrown = new PokeballEntity(player, helper.getLevel(), ball);
        thrown.snapTo(from.x, from.y, from.z, 0.0F, 0.0F);

        Vec3 aim = at.subtract(from);
        thrown.shoot(aim.x, aim.y, aim.z, 1.0F, 0.0F);
        helper.getLevel().addFreshEntity(thrown);
    }

    private static List<ItemStack> filledPokeballs(GameTestHelper helper) {
        return helper.getEntities(EntityTypes.ITEM).stream()
                .map(ItemEntity::getItem)
                .filter(stack -> stack.is(PokeballItems.POKEBALL.get()) && !CapturedEntity.of(stack).isEmpty())
                .toList();
    }
}
