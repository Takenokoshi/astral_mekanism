package astral_mekanism;

import com.mojang.logging.LogUtils;

import appeng.blockentity.networking.EnergyCellBlockEntity;
import astral_mekanism.config.AstralMekanismConfig;
import astral_mekanism.network.AstralMekanismPacketHandler;
import astral_mekanism.registries.AstralMekanismAEBlockEntityTypes;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

import org.slf4j.Logger;

@Mod(AstralMekanism.MODID)
public class AstralMekanism {
    public static final String MODID = AstralMekanismID.MODID;
    public static final Logger LOGGER = LogUtils.getLogger();

    public static AstralMekanism instance;

    private final AstralMekanismPacketHandler packetHandler;
    public final Version version;

    public AstralMekanism() {
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();
        context.registerConfig(ModConfig.Type.COMMON, AstralMekanismConfig.SPEC);
        AstralMekanismConfig.registerConfigs(context);
        instance = this;
        IEventBus modEventBus = context.getModEventBus();
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
        this.packetHandler = new AstralMekanismPacketHandler();
    }

    @SubscribeEvent
    public static void register(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES, helper -> {
            AstralMekanismAEBlockEntityTypes.init();
            helper.register(AstralMekanismID.rl("tiered_energy_cell"), AstralMekanismAEBlockEntityTypes.ENERGY_CELL);
            AstralMekanismBlocks.ENERGY_CELLS
                    .forEach((t, object) -> object.getBlock().setBlockEntity(EnergyCellBlockEntity.class,
                            AstralMekanismAEBlockEntityTypes.ENERGY_CELL, null, null));
        });
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info(MODID);
        packetHandler.initialize();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }
    }

    public static AstralMekanismPacketHandler packetHandler() {
        return instance.packetHandler;
    }

}
