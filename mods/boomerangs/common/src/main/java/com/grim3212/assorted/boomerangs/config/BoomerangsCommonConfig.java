package com.grim3212.assorted.boomerangs.config;

import com.grim3212.assorted.boomerangs.Constants;
import com.grim3212.assorted.lib.config.ConfigurationType;
import com.grim3212.assorted.lib.config.IConfigurationBuilder;
import com.grim3212.assorted.lib.platform.Services;

import java.util.function.Supplier;

public class BoomerangsCommonConfig {
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

    public BoomerangsCommonConfig() {
        final IConfigurationBuilder builder = Services.CONFIG.createBuilder(ConfigurationType.NOT_SYNCED, Constants.MOD_ID + "-common");

        turnAroundItem = builder.defineBoolean("boomerangs.turnAroundItem", false, "Set this to true if you would like boomerangs to turn around after they have picked up items.");
        turnAroundMob = builder.defineBoolean("boomerangs.turnAroundMob", false, "Set this to true if you would like boomerangs to turn around after they have hit a mob.");
        breaksTorches = builder.defineBoolean("boomerangs.breaksTorches", false, "Set this to true if you would like boomerangs to be able to break torches.");
        breaksPlants = builder.defineBoolean("boomerangs.breaksPlants", false, "Set this to true if you would like boomerangs to be able to break plants and any other weak blocks.");
        hitsButtons = builder.defineBoolean("boomerangs.hitsButtons", true, "Set this to false if you would like boomerangs to not be able to hit buttons or levers.");
        turnAroundButton = builder.defineBoolean("boomerangs.turnAroundButton", true, "Set this to false if you would like boomerangs to not turn around after they have hit a button or a lever.");
        woodBoomerangRange = builder.defineInteger("boomerangs.woodBoomerangRange", 10, 1, 100, "How many blocks the wood boomerang flies out before it turns around.");
        woodBoomerangDamage = builder.defineInteger("boomerangs.woodBoomerangDamage", 1, 1, 500, "The amount of damage the wood boomerang does to mobs.");
        diamondBoomerangRange = builder.defineInteger("boomerangs.diamondBoomerangRange", 15, 1, 100, "How many blocks the diamond boomerang flies out before it turns around.");
        diamondBoomerangDamage = builder.defineInteger("boomerangs.diamondBoomerangDamage", 5, 1, 200, "The amount of damage the diamond boomerang does to mobs.");
        diamondBoomerangFollows = builder.defineBoolean("boomerangs.diamondBoomerangFollows", false, "Set to true if you would like the diamond boomerang to follow where the player is looking.");

        builder.setup();
    }
}
