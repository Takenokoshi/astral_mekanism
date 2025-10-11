package astral_mekanism;

import com.mojang.logging.LogUtils;

import astral_mekanism.registries.AstralMekanismBlocks;
import astral_mekanism.registries.AstralMekanismContainers;
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

import mekanism.common.registration.impl.BlockDeferredRegister;
import mekanism.common.registration.impl.ContainerTypeDeferredRegister;
import mekanism.common.registration.impl.CreativeTabDeferredRegister;
import mekanism.common.registration.impl.FluidDeferredRegister;
import mekanism.common.registration.impl.GasDeferredRegister;
import mekanism.common.registration.impl.InfuseTypeDeferredRegister;
import mekanism.common.registration.impl.ItemDeferredRegister;
import mekanism.common.registration.impl.RecipeSerializerDeferredRegister;
import mekanism.common.registration.impl.RecipeTypeDeferredRegister;
import mekanism.common.registration.impl.SlurryDeferredRegister;
import mekanism.common.registration.impl.TileEntityTypeDeferredRegister;
import mekanism.common.tile.component.config.DataType;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(AstralMekanism.MODID)
public class AstralMekanism {
	public static final String MODID = AstralMekanismID.MODID;
	private static final Logger LOGGER = LogUtils.getLogger();

	public static final GasDeferredRegister GASES = AstralMekanismGases.GASES;
	public static final InfuseTypeDeferredRegister INFUSE_TYPES = AstralMekanismInfuseTypes.INFUSE_TYPES;
	public static final SlurryDeferredRegister SLURRIES = AstralMekanismSlurries.SLURRIES;
	public static final BlockDeferredRegister BLOCKS = AstralMekanismBlocks.BLOCKS;
	public static final FluidDeferredRegister FLUIDS = AstralMekanismFluids.FLUIDS;
	public static final ItemDeferredRegister ITEMS = AstralMekanismItems.ITEMS;
	public static final ContainerTypeDeferredRegister CONTAINER_TYPES = AstralMekanismContainers.CONTAINER_TYPES;
	public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = AstralMekanismTileEntityTypes.TILE_ENTITY_TYPES;
	public static final RecipeSerializerDeferredRegister RECIPE_SERIALIZERS = AstralMekanismRecipeSerializers.RECIPE_SERIALIZERS;
	public static final RecipeTypeDeferredRegister RECIPE_TYPES = AstralMekanismRecipeTypes.RECIPE_TYPES;
	public static final CreativeTabDeferredRegister CREATIVE_TABS = AstralMekanismCreativeTab.CREATIVE_TABS;

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
		CONTAINER_TYPES.register(modEventBus);
		TILE_ENTITY_TYPES.register(modEventBus);
		AstralMekanismRecipeSerializers.RECIPE_SERIALIZERS.register(modEventBus);
		AstralMekanismRecipeTypes.RECIPE_TYPES.register(modEventBus);
		AstralMekanismCreativeTab.CREATIVE_TABS.register(modEventBus);

		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);

		// Register our mod's ForgeConfigSpec so that Forge can create and load the
		// config file for us
		context.registerConfig(ModConfig.Type.COMMON, AstralMekanismConfig.SPEC);
	}

	private void commonSetup(final FMLCommonSetupEvent event) {
		LOGGER.info(MODID);
		for (DataType type : DataType.values()) {
			LOGGER.info(type.getTranslationKey());
		}
	}

	// You can use SubscribeEvent and let the Event Bus discover methods to call
	@SubscribeEvent
	public void onServerStarting(ServerStartingEvent event) {
		for (DataType type : DataType.values()) {
			LOGGER.info(type.getTranslationKey());
		}
		
	}

	// You can use EventBusSubscriber to automatically register all static methods
	// in the class annotated with @SubscribeEvent
	@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
	public static class ClientModEvents {
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
		}
	}

}
