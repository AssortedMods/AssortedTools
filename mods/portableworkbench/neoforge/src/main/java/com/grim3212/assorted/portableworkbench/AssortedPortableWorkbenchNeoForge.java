package com.grim3212.assorted.portableworkbench;

import com.grim3212.assorted.portableworkbench.client.data.PortableWorkbenchItemModelProvider;
import com.grim3212.assorted.portableworkbench.client.data.PortableWorkbenchLanguageProvider;
import com.grim3212.assorted.portableworkbench.client.data.PortableWorkbenchManualProvider;
import com.grim3212.assorted.portableworkbench.data.PortableWorkbenchRecipes;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(Constants.MOD_ID)
public class AssortedPortableWorkbenchNeoForge {

    /** The mod event bus and container are injected into the {@code @Mod} constructor. */
    public AssortedPortableWorkbenchNeoForge(IEventBus modBus, ModContainer modContainer) {
        modBus.addListener(this::gatherServerData);
        modBus.addListener(this::gatherClientData);

        PortableWorkbenchCommonMod.init();
    }

    /** Server datagen. If the client half runs instead, the build still succeeds, with "All providers took: 0 ms". */
    private void gatherServerData(final GatherDataEvent.Server event) {
        event.addProvider(new PortableWorkbenchRecipes.Runner(event.getGenerator().getPackOutput(), event.getLookupProvider()));
    }

    /** Client datagen: models, the language file and the manual, written into common for both loaders. */
    private void gatherClientData(final GatherDataEvent.Client event) {
        PackOutput packOutput = event.getGenerator().getPackOutput();

        event.addProvider(new PortableWorkbenchItemModelProvider(packOutput));
        event.addProvider(new PortableWorkbenchLanguageProvider(packOutput));
        event.addProvider(new PortableWorkbenchManualProvider(packOutput));
    }
}
