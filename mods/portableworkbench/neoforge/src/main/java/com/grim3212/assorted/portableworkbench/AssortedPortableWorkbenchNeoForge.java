package com.grim3212.assorted.portableworkbench;

import com.grim3212.assorted.lib.data.ForgeRegistryEntries;
import com.grim3212.assorted.portableworkbench.client.data.PortableWorkbenchItemModelProvider;
import com.grim3212.assorted.portableworkbench.client.data.PortableWorkbenchLanguageProvider;
import com.grim3212.assorted.portableworkbench.client.data.PortableWorkbenchManualProvider;
import com.grim3212.assorted.portableworkbench.data.PortableWorkbenchRecipes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(Constants.MOD_ID)
public class AssortedPortableWorkbenchNeoForge {

    /** The mod event bus and container are injected into the {@code @Mod} constructor. */
    public AssortedPortableWorkbenchNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherData);

        PortableWorkbenchCommonMod.init();
    }

    /** One event for both halves; each provider is added for the half its output belongs to. */
    private void gatherData(final GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();

        ForgeRegistryEntries.of(Constants.MOD_ID).recipes(PortableWorkbenchRecipes::new).addTo(event);

        generator.addProvider(event.includeClient(), new PortableWorkbenchItemModelProvider(packOutput, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new PortableWorkbenchLanguageProvider(packOutput));
        generator.addProvider(event.includeClient(), new PortableWorkbenchManualProvider(packOutput));
    }
}
