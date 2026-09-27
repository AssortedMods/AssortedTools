package com.grim3212.assorted.tools.config;

import com.google.common.collect.Lists;
import com.grim3212.assorted.lib.config.ConfigurationType;
import com.grim3212.assorted.lib.config.IConfigurationBuilder;
import com.grim3212.assorted.lib.core.tool.ArmorMaterialConfig;
import com.grim3212.assorted.lib.core.tool.ToolTier;
import com.grim3212.assorted.lib.core.tool.ToolTiers;
import com.grim3212.assorted.lib.platform.Services;
import com.grim3212.assorted.lib.util.LibCommonTags;
import com.grim3212.assorted.tools.Constants;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ToolsCommonConfig {
    public final Supplier<Boolean> hideUncraftableItems;
    public final Supplier<Boolean> allowPartialBucketAmounts;
    public final Supplier<Boolean> freeBuildMode;
    public final Supplier<Boolean> bedrockBreaking;
    public final Supplier<Boolean> easyMiningObsidian;

    public final Supplier<Boolean> turnAroundItem;
    public final Supplier<Boolean> turnAroundMob;
    public final Supplier<Boolean> breaksTorches;
    public final Supplier<Boolean> breaksPlants;
    public final Supplier<Boolean> hitsButtons;
    public final Supplier<Boolean> turnAroundButton;
    public final Supplier<Integer> woodBoomerangRange;
    public final Supplier<Integer> woodBoomerangDamage;
    public final Supplier<Integer> diamondBoomerangRange;
    public final Supplier<Integer> diamondBoomerangDamage;
    public final Supplier<Boolean> diamondBoomerangFollows;
    public final Supplier<List<? extends Float>> conductivityLightningChances;

    /** Wood through netherite and the extra materials, shared with the other Assorted mods through Lib. */
    public final ToolTiers tiers;
    public final ToolTier ultimateItemTier;
    public final ArmorMaterialConfig chickenSuitArmorMaterial;
    public final ArmorMaterialConfig scubaSuitArmorMaterial;
    public final ArmorMaterialConfig lavaSuitArmorMaterial;

    // Keyed by tier name.
    public final Map<String, BucketConfig> buckets = new HashMap<>();
    public final Map<String, Supplier<Double>> multiToolModifiers = new HashMap<>();
    public final Map<String, SpearConfig> spears = new HashMap<>();
    public final Map<String, ArmorMaterialConfig> moddedArmors = new HashMap<>();

    public ToolsCommonConfig() {
        // Needed at registration: nearly everything here decides what an item is built from, and items bake it in as they are constructed.
        final IConfigurationBuilder builder = Services.CONFIG.createBuilder(ConfigurationType.NEEDED_AT_REGISTRATION, Constants.MOD_ID + "-common");
        this.tiers = ToolTiers.get();

        hideUncraftableItems = builder.defineBoolean("general.hideUncraftableItems", false, "For any item that is unobtainable (like missing materials from other mods) hide it from the creative menu / JEI.");

        allowPartialBucketAmounts = builder.defineBoolean("better_buckets.allowPartialBucketAmounts", false, "Set to true if you would like the better buckets to be able to accept partial bucket amounts. Meaning some can get left over after placing all the full buckets.");

        freeBuildMode = builder.defineBoolean("wands.freeBuildMode", false, "Set to true if you would like the wands to not require any blocks to build with.");
        bedrockBreaking = builder.defineBoolean("wands.bedrockBreaking", false, "Set to true if you would like the breaking wands to be able to break bedrock.");
        easyMiningObsidian = builder.defineBoolean("wands.easyMiningObsidian", false, "Set to true if you would like the mining wands to be able to mine obsidian.");

        turnAroundItem = builder.defineBoolean("boomerangs.turnAroundItem", false, "Set this to true if you would like boomerangs to turn around after they have picked up items.");
        turnAroundMob = builder.defineBoolean("boomerangs.turnAroundMob", false, "Set this to true if you would like boomerangs to turn around after they have hit a mob.");
        breaksTorches = builder.defineBoolean("boomerangs.breaksTorches", false, "Set this to true if you would like boomerangs to be able to break torches.");
        breaksPlants = builder.defineBoolean("boomerangs.breaksPlants", false, "Set this to true if you would like boomerangs to be able to break plants and any other weak blocks.");
        hitsButtons = builder.defineBoolean("boomerangs.hitsButtons", true, "Set this to false if you would like boomerangs to not be able to hit buttons or levers.");
        turnAroundButton = builder.defineBoolean("boomerangs.turnAroundButton", true, "Set this to false if you would like boomerangs to not turn around after they have hit a button or a lever.");
        woodBoomerangRange = builder.defineInteger("boomerangs.woodBoomerangRange", 20, 1, 200, "The maximum range away from the player the wood boomerang will travel before turning around.");
        woodBoomerangDamage = builder.defineInteger("boomerangs.woodBoomerangDamage", 1, 1, 500, "The amount of damage the wood boomerang does to mobs.");
        diamondBoomerangRange = builder.defineInteger("boomerangs.diamondBoomerangRange", 30, 1, 200, "The maximum range away from the player the diamond boomerang will travel before turning around.");
        diamondBoomerangDamage = builder.defineInteger("boomerangs.diamondBoomerangDamage", 5, 1, 200, "The amount of damage the diamond boomerang does to mobs.");
        diamondBoomerangFollows = builder.defineBoolean("boomerangs.diamondBoomerangFollows", false, "Set to true if you would like the diamond boomerang to follow where the player is looking.");

        conductivityLightningChances = builder.defineList("better_spears.conductivityLightningChances", Lists.newArrayList(0.6F, 0.3F, 0.1F), Float.class, "The chances modifier for lightning to spawn at each level of conductivity. The smaller the number the higher chance.");

        ultimateItemTier = new ToolTier(builder, "ultimate_fist", "ultimate", new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, 1561, 64F, 64F, 0, LibCommonTags.Items.NETHER_STARS), 0F, 0F);

        chickenSuitArmorMaterial = armor(builder, "chicken_suit", "chicken_suit", 5, 15, 0.0F, 0.0F, new int[]{1, 2, 3, 1}, () -> BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.WOOL_PLACE), LibCommonTags.Items.FEATHERS, "chicken_suit");
        scubaSuitArmorMaterial = armor(builder, "scuba_suit", "scuba_suit", 12, 12, 0.0F, 0.0F, new int[]{1, 3, 4, 2}, () -> SoundEvents.ARMOR_EQUIP_LEATHER, LibCommonTags.Items.LEATHER, "scuba");
        lavaSuitArmorMaterial = armor(builder, "lava_suit", "lava_suit", 20, 9, 0.0F, 0.0F, new int[]{2, 6, 7, 2}, () -> SoundEvents.ARMOR_EQUIP_NETHERITE, LibCommonTags.Items.INGOTS_NETHERITE, "lava");

        bucket(builder, "wood", 1, 0, 1000f, true);
        bucket(builder, "stone", 1, 0, 5000f, true);
        bucket(builder, "gold", 4, 0, 5000f, false);
        bucket(builder, "diamond", 16, 1, 5000f, false);
        bucket(builder, "netherite", 64, 2, 10000f, false);

        extra(builder, "tin", 4, 0, 8, 14, 0.0F, 0.0F, new int[]{1, 3, 5, 2}, SoundEvents.ARMOR_EQUIP_IRON);
        extra(builder, "copper", 8, 0, 11, 14, 0.0F, 0.0F, new int[]{2, 5, 6, 2}, SoundEvents.ARMOR_EQUIP_IRON);
        extra(builder, "silver", 12, 1, 27, 14, 0.0F, 0.0F, new int[]{3, 6, 8, 3}, SoundEvents.ARMOR_EQUIP_DIAMOND);
        extra(builder, "aluminum", 2, 0, 13, 10, 0.0F, 0.0F, new int[]{2, 5, 6, 2}, SoundEvents.ARMOR_EQUIP_IRON);
        extra(builder, "nickel", 8, 1, 13, 10, 0.0F, 0.0F, new int[]{2, 3, 4, 2}, SoundEvents.ARMOR_EQUIP_IRON);
        extra(builder, "platinum", 24, 2, 36, 18, 3.0F, 0.2F, new int[]{3, 6, 8, 3}, SoundEvents.ARMOR_EQUIP_DIAMOND);
        extra(builder, "lead", 4, 0, 13, 4, 0.0F, 0.0F, new int[]{2, 3, 4, 2}, SoundEvents.ARMOR_EQUIP_IRON);
        extra(builder, "bronze", 8, 1, 14, 13, 0.0F, 0.1F, new int[]{2, 5, 6, 2}, SoundEvents.ARMOR_EQUIP_IRON);
        extra(builder, "electrum", 18, 1, 13, 13, 0.0F, 0.0F, new int[]{2, 5, 6, 2}, SoundEvents.ARMOR_EQUIP_IRON);
        extra(builder, "invar", 10, 1, 15, 11, 0.2F, 0.1F, new int[]{2, 5, 6, 2}, SoundEvents.ARMOR_EQUIP_IRON);
        extra(builder, "steel", 16, 1, 26, 10, 0.5F, 0.3F, new int[]{2, 6, 7, 2}, SoundEvents.ARMOR_EQUIP_IRON);
        extra(builder, "ruby", 8, 1, 34, 10, 2.0F, 0.0F, new int[]{3, 6, 8, 3}, SoundEvents.ARMOR_EQUIP_DIAMOND);
        extra(builder, "amethyst", 8, 0, 31, 14, 1.0F, 0.0F, new int[]{3, 6, 8, 3}, SoundEvents.ARMOR_EQUIP_DIAMOND);
        extra(builder, "sapphire", 8, 0, 31, 14, 1.0F, 0.0F, new int[]{3, 6, 8, 3}, SoundEvents.ARMOR_EQUIP_DIAMOND);
        extra(builder, "topaz", 8, 0, 30, 8, 1.0F, 0.0F, new int[]{3, 6, 8, 3}, SoundEvents.ARMOR_EQUIP_DIAMOND);
        extra(builder, "emerald", 10, 1, 32, 14, 2.0F, 0.0F, new int[]{3, 6, 8, 3}, SoundEvents.ARMOR_EQUIP_DIAMOND);
        extra(builder, "peridot", 8, 0, 30, 8, 1.0F, 0.0F, new int[]{3, 6, 8, 3}, SoundEvents.ARMOR_EQUIP_DIAMOND);

        for (ToolTier tier : this.tiers.vanilla()) {
            multiTool(builder, tier.getName());
        }
        this.tiers.extras().keySet().forEach(name -> multiTool(builder, name));

        builder.setup();
    }

    public BucketConfig bucket(ToolTier tier) {
        return this.buckets.get(tier.getName());
    }

    public float multiToolModifier(ToolTier tier) {
        return this.multiToolModifiers.get(tier.getName()).get().floatValue();
    }

    private void bucket(IConfigurationBuilder builder, String name, int maxBuckets, int milkingLevel, float maxPickupTemp, boolean breaksAfterUse) {
        this.buckets.put(name, new BucketConfig(builder, "better_buckets", name, maxBuckets, milkingLevel, maxPickupTemp, breaksAfterUse));
    }

    private void multiTool(IConfigurationBuilder builder, String name) {
        this.multiToolModifiers.put(name, builder.defineDouble("multitools." + name + ".durabilityModifier", 1.5F, 0F, 1000F, "The modifier that will be used to calculate the multitool maximum uses. Normal tool material maxUses * this modifier."));
    }

    /** An extra material's bucket, spear and armour; its tool numbers are the shared tier's. */
    private void extra(IConfigurationBuilder builder, String name, int maxBuckets, int milkingLevel, int durability, int enchantability, float toughness, float knockbackResistance, int[] reductionAmounts, Holder<SoundEvent> equipSound) {
        ToolTier tier = this.tiers.extra(name);
        bucket(builder, name, maxBuckets, milkingLevel, 5000f, false);
        this.spears.put(name, new SpearConfig(builder, "spears", name, tier.getHarvestLevel()));
        this.moddedArmors.put(name, armor(builder, "extra_armor", name, durability, enchantability, toughness, knockbackResistance, reductionAmounts, () -> equipSound, tier.getRepairItems(), name));
    }

    private static ArmorMaterialConfig armor(IConfigurationBuilder builder, String path, String name, int durability, int enchantability, float toughness, float knockbackResistance, int[] reductionAmounts, Supplier<Holder<SoundEvent>> equipSound, TagKey<Item> repairItems, String asset) {
        ResourceKey<EquipmentAsset> assetId = ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(Constants.MOD_ID, asset));
        return new ArmorMaterialConfig(builder, path, name, durability, enchantability, toughness, knockbackResistance, reductionAmounts, equipSound, repairItems, assetId);
    }
}
