package astral_mekanism.registries;

import java.util.HashMap;
import java.util.Map;

import astral_mekanism.AstralMekanismID;
import mekanism.common.registration.impl.FluidDeferredRegister;
import mekanism.common.registration.impl.FluidDeferredRegister.MekanismFluidType;
import mekanism.common.registration.impl.FluidRegistryObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraftforge.fluids.ForgeFlowingFluid.Flowing;
import net.minecraftforge.fluids.ForgeFlowingFluid.Source;

public class AstralMekanismFluids {
	public static final FluidDeferredRegister FLUIDS = new FluidDeferredRegister(AstralMekanismID.MODID);

	private static Map<OreType, FluidRegistryObject<MekanismFluidType, Source, Flowing, LiquidBlock, BucketItem>> oreFluidRegister() {
		Map<OreType, FluidRegistryObject<MekanismFluidType, Source, Flowing, LiquidBlock, BucketItem>> result = new HashMap<>();
		for (OreType type : OreType.values()) {
			result.put(type, FLUIDS.register("fluid_" + type.type,
					properties -> properties,
					renderProperties -> renderProperties
							.texture(new ResourceLocation("block/water_still"),
									new ResourceLocation("block/water_flow"))
							.tint(type.color + 0x80000000)));
		}
		return result;
	}

	private static Map<OreType, FluidRegistryObject<MekanismFluidType, Source, Flowing, LiquidBlock, BucketItem>> oreLavaRegister() {
		Map<OreType, FluidRegistryObject<MekanismFluidType, Source, Flowing, LiquidBlock, BucketItem>> result = new HashMap<>();
		for (OreType type : OreType.values()) {
			result.put(type, FLUIDS.register("lava_" + type.type,
					properties -> properties,
					renderProperties -> renderProperties
							.texture(new ResourceLocation("block/lava_still"),
									new ResourceLocation("block/lava_flow"))
							.tint(type.color + 0x80000000)));
		}
		return result;
	}

	public static final Map<OreType, FluidRegistryObject<MekanismFluidType, Source, Flowing, LiquidBlock, BucketItem>> ORE_FLUIDS = oreFluidRegister();
	public static final Map<OreType, FluidRegistryObject<MekanismFluidType, Source, Flowing, LiquidBlock, BucketItem>> ORE_LAVAS = oreLavaRegister();
}
