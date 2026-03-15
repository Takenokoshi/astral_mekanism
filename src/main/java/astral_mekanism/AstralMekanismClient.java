package astral_mekanism;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private static final Logger LOGGER = LoggerFactory.getLogger(AstralMekanismClient.class);

    private static AstralMekanismClient INSTANCE;

    public AstralMekanismClient() {
        super();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        INSTANCE = this;
        eventBus.addListener(this::clientSetup);
    }

    @SuppressWarnings("unused")
    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            initScreens();
        });
    }

    private static void initScreens() {
        InitScreens.register(AstralMekanismMenus.LOGIC_DRIVE.get(), TieredDriveScreen::new,
                "/screens/logic_drive.json");
        InitScreens.register(AstralMekanismMenus.CALCULATION_DRIVE.get(), TieredDriveScreen::new,
                "/screens/calculation_drive.json");
        InitScreens.register(AstralMekanismMenus.ENGINEERING_DRIVE.get(), TieredDriveScreen::new,
                "/screens/engineering_drive.json");
        InitScreens.register(AstralMekanismMenus.ACCUMULATION_DRIVE.get(), TieredDriveScreen::new,
                "/screens/accumulation_drive.json");
        InitScreens.register(AstralMekanismMenus.PHOTON_DRIVE.get(), TieredDriveScreen::new,
                "/screens/photon_drive.json");
        InitScreens.register(AstralMekanismMenus.QUANTUM_DRIVE.get(), TieredDriveScreen::new,
                "/screens/quantum_drive.json");
        InitScreens.register(AstralMekanismMenus.COMPOSITE_DRIVE.get(), TieredDriveScreen::new,
                "/screens/composite_drive.json");
        InitScreens.register(AstralMekanismMenus.ORIGIN_DRIVE.get(), TieredDriveScreen::new,
                "/screens/origin_drive.json");
        InitScreens.register(AstralMekanismMenus.AUTONOMY_DRIVE.get(), TieredDriveScreen::new,
                "/screens/autonomy_drive.json");
        InitScreens.register(AstralMekanismMenus.FIRMAMENT_DRIVE.get(), TieredDriveScreen::new,
                "/screens/firmament_drive.json");
    }
}
