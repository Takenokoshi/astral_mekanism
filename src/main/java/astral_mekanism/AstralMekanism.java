package astral_mekanism;

import com.mojang.logging.LogUtils;

import appeng.api.storage.StorageCells;
import astral_mekanism.config.AMEConfig;
import astral_mekanism.item.cell.bulkcell.AMEBulkCellHandler;
import astral_mekanism.network.AMEPacketHandler;
import astral_mekanism.registries.AMEBlockEntityRegistry;
import astral_mekanism.registries.AMEItemDefinitions;
import astral_mekanism.registries.AMEBlockDefinitions;
import astral_mekanism.registries.AMEBlocks;
import astral_mekanism.registries.AMECreativeTab;
import astral_mekanism.registries.AMEFluids;
import astral_mekanism.registries.AMEGases;
import astral_mekanism.registries.AMEInfuseTypes;
import astral_mekanism.registries.AMEItems;
import astral_mekanism.registries.AMEMachines;
import astral_mekanism.registries.AMERecipeSerializers;
import astral_mekanism.registries.AMERecipeTypes;
import astral_mekanism.registries.AMESlurries;
import astral_mekanism.registries.AMETileEntityTypes;
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
        AMEMachines.MACHINES.register(modEventBus);
        AMEGases.GASES.register(modEventBus);
        AMEInfuseTypes.INFUSE_TYPES.register(modEventBus);
        AMESlurries.SLURRIES.register(modEventBus);
        AMEBlocks.BLOCKS.register(modEventBus);
        AMEFluids.FLUIDS.register(modEventBus);
        AMEItems.ITEMS.register(modEventBus);
        AMETileEntityTypes.TILE_ENTITY_TYPES.register(modEventBus);
        AMERecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        AMERecipeTypes.RECIPE_TYPES.register(modEventBus);
        AMECreativeTab.CREATIVE_TABS.register(modEventBus);
        modEventBus.addListener(this::initCell);
        MinecraftForge.EVENT_BUS.register(this);
        version = new Version(context.getActiveContainer());
        this.packetHandler = new AMEPacketHandler();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info(MODID + " was initialized.");
        packetHandler.initialize();
    }

    private void initCell(final FMLCommonSetupEvent event) {
        StorageCells.addCellHandler(AMEBulkCellHandler.CHEMICAL_HANDLER);
        StorageCells.addCellHandler(AMEBulkCellHandler.FLUID_HANDLER);
    }

    public static AMEPacketHandler packetHandler() {
        return instance.packetHandler;
    }

}
