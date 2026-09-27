package com.grim3212.assorted.buckets.common.item;

import com.grim3212.assorted.buckets.BucketsCommonMod;
import com.grim3212.assorted.buckets.Constants;
import com.grim3212.assorted.buckets.Family;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import com.grim3212.assorted.lib.core.tool.ToolTiers;
import com.grim3212.assorted.lib.registry.IRegistryObject;
import com.grim3212.assorted.lib.registry.RegistryProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class BucketsItems {

    public static final RegistryProvider<Item> ITEMS = RegistryProvider.create(Registries.ITEM, Constants.MOD_ID).aliasFrom(Family.ID);

    private static final ToolTiers TIERS = ToolTiers.get();

    public static final BucketPair WOOD = pair("wood", TIERS.wood, false);
    public static final BucketPair STONE = pair("stone", TIERS.stone, false);
    public static final BucketPair GOLD = pair("gold", TIERS.gold, false);
    public static final BucketPair DIAMOND = pair("diamond", TIERS.diamond, false);
    public static final BucketPair NETHERITE = pair("netherite", TIERS.netherite, true);

    /** One pair for each extra material, in the shared tiers' order. */
    public static final List<BucketPair> EXTRAS = new ArrayList<>();

    static {
        TIERS.extras().forEach((name, tier) -> EXTRAS.add(pair(name, tier, false)));
    }

    /** Wood through netherite, then the extra materials. */
    public static List<BucketPair> pairs() {
        List<BucketPair> pairs = new ArrayList<>(List.of(WOOD, STONE, GOLD, DIAMOND, NETHERITE));
        pairs.addAll(EXTRAS);
        return pairs;
    }

    public static List<Item> buckets() {
        return pairs().stream().<Item>map(pair -> pair.bucket().get()).toList();
    }

    private static BucketPair pair(String name, ToolTier tier, boolean fireResistant) {
        IRegistryObject<BetterBucketItem> bucket = register(name + "_bucket", props -> new BetterBucketItem(fireResistant ? props.fireResistant() : props, tier, BucketsCommonMod.CONFIG.bucket(tier)));
        IRegistryObject<BetterMilkBucketItem> milk = register(name + "_milk_bucket", props -> new BetterMilkBucketItem(bucket::get, BucketsCommonMod.CONFIG.bucket(tier), fireResistant ? props.fireResistant() : props));
        return new BucketPair(tier, bucket, milk);
    }

    private static <T extends Item> IRegistryObject<T> register(final String name, final Function<Item.Properties, ? extends T> factory) {
        final ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(Constants.MOD_ID, name));
        return ITEMS.register(name, () -> factory.apply(new Item.Properties().setId(key)));
    }

    public static void init() {
    }
}
