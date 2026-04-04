package astral_mekanism;

import com.mojang.logging.LogUtils;

import astral_mekanism.config.AMEConfig;
import astral_mekanism.network.AMEPacketHandler;
import astral_mekanism.registries.AMEBlockEntityRegistry;
import astral_mekanism.registries.AMEItemDefinitions;
import astral_mekanism.registries.AMEBlockDefinitions;
import astral_mekanism.registries.AstralMekanismBlocks;
import astral_mekanism.registries.AstralMekanismCreativeTab;
import astral_mekanism.registries.AstralMekanismFluids;
import astral_mekanism.registries.AstralMekanismGases;
import astral_mekanism.registries.AstralMekanismInfuseTypes;
import astral_mekanism.registries.AstralMekanismItems;
import astral_mekanism.registries.AstralMekanismMachines;
import astral_mekanism.registries.AstralMekanismRecipeSerializers;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import astral_mekanism.registries.AstralMekanismSlurries;
import astral_mekanism.registries.AstralMekanismTileEntityTypes;
import mekanism.common.lib.Version;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

public class AstralMekanism {
    public static final String MODID = AMEConstants.MODID;
    public static final Logger LOGGER = LogUtils.getLogger();

    public static AstralMekanism instance;

    private final AMEPacketHandler packetHandler;
    public final Version version;

    public AstralMekanism() {
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();
        context.registerConfig(ModConfig.Type.COMMON, AMEConfig.SPEC);
        AMEConfig.registerConfigs(context);
        instance = this;
        IEventBus modEventBus = context.getModEventBus();
        AMEItemDefinitions.INSTANCE.register(modEventBus);
        AMEBlockDefinitions.INSTANCE.register(modEventBus);
        AMEBlockEntityRegistry.INSTANCE.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        AstralMekanismMachines.MACHINES.register(modEventBus);
        AstralMekanismGases.GASES.register(modEventBus);
        AstralMekanismInfuseTypes.INFUSE_TYPES.register(modEventBus);
        AstralMekanismSlurries.SLURRIES.register(modEventBus);
        AstralMekanismBlocks.BLOCKS.register(modEventBus);
        AstralMekanismFluids.FLUIDS.register(modEventBus);
        AstralMekanismItems.ITEMS.register(modEventBus);
        AstralMekanismTileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        AstralMekanismRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        AstralMekanismRecipeTypes.RECIPE_TYPES.register(modEventBus);
        AstralMekanismCreativeTab.CREATIVE_TABS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        version = new Version(context.getActiveContainer());
        this.packetHandler = new AMEPacketHandler();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info(MODID + " was initialized.");
        packetHandler.initialize();
    }

    public static AMEPacketHandler packetHandler() {
        return instance.packetHandler;
    }

}
