package astral_mekanism;

import appeng.init.client.InitScreens;
import astral_mekanism.block.gui.storage.TieredDriveScreen;
import astral_mekanism.registries.AstralMekanismMenus;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@OnlyIn(Dist.CLIENT)
public class AstralMekanismClient extends AstralMekanism {
    public AstralMekanismClient() {
        super();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::clientSetup);
    }

    @SuppressWarnings("unused")
    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(()->{
            Minecraft minecraft = Minecraft.getInstance();
            initScreens();
        });
    }

    private static void initScreens() {
        AstralMekanismMenus.DRIVES.forEach((tier, supplier) -> {
            InitScreens.register(supplier.get(), TieredDriveScreen::new,
                    "/screens/" + tier.nameForAE + "_drive.json");
        });
    }
}
