package astral_mekanism;

import com.mojang.logging.LogUtils;

import appeng.api.features.P2PTunnelAttunement;
import astral_mekanism.registries.AstralMekanismBlocks;
import astral_mekanism.registries.AstralMekanismCableParts;
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
import net.minecraft.network.chat.Component;
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

import org.slf4j.Logger;

@Mod(AstralMekanism.MODID)
public class AstralMekanism {
    public static final String MODID = AstralMekanismID.MODID;
    private static final Logger LOGGER = LogUtils.getLogger();

    public AstralMekanism(FMLJavaModLoadingContext context) {
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
        AstralMekanismCableParts.CABLE_PARTS.register(modEventBus);
        AstralMekanismRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
        AstralMekanismRecipeTypes.RECIPE_TYPES.register(modEventBus);
        AstralMekanismCreativeTab.CREATIVE_TABS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        context.registerConfig(ModConfig.Type.COMMON, AstralMekanismConfig.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info(MODID);
        event.enqueueWork(() -> {
            P2PTunnelAttunement.registerAttunementTag(AstralMekanismCableParts.HEAT_P2P);
            P2PTunnelAttunement.registerAttunementApi(
                    AstralMekanismCableParts.HEAT_P2P,
                    mekanism.common.capabilities.Capabilities.HEAT_HANDLER,
                    Component.nullToEmpty("heat"));
        });
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

}
