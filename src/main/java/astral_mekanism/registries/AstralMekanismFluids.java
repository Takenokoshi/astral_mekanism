package astral_mekanism.registries;

import astral_mekanism.AstralMekanismID;
import mekanism.common.registration.impl.FluidDeferredRegister;
import mekanism.common.registration.impl.FluidRegistryObject;
import mekanism.common.registration.impl.FluidDeferredRegister.MekanismFluidType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraftforge.fluids.ForgeFlowingFluid.Flowing;
import net.minecraftforge.fluids.ForgeFlowingFluid.Source;

public class AstralMekanismFluids {
    public static final FluidDeferredRegister FLUIDS = new FluidDeferredRegister(AstralMekanismID.MODID);

    public static final FluidRegistryObject<MekanismFluidType, Source, Flowing, LiquidBlock, BucketItem> RED_LAVA = FLUIDS
            .register("red_lava", renderProperties -> renderProperties.texture(
                    new ResourceLocation("block/lava_still"),
                    new ResourceLocation("block/lava_flow")).tint(0x80800000));
    public static final FluidRegistryObject<MekanismFluidType, Source, Flowing, LiquidBlock, BucketItem> NETHERRACK = FLUIDS
            .register("netherrack", renderProperties -> renderProperties.texture(
                    new ResourceLocation("block/netherrack"),
                    new ResourceLocation("block/netherrack")));

    public static final FluidRegistryObject<MekanismFluidType, Source, Flowing, LiquidBlock, BucketItem> NETHERRACK_LAVA = FLUIDS
            .register("netherrack_lava", renderProperties -> renderProperties.texture(
                    new ResourceLocation("block/lava_still"),
                    new ResourceLocation("block/lava_flow")).tint(0x80700000));

    public static final FluidRegistryObject<MekanismFluidType, Source, Flowing, LiquidBlock, BucketItem> SOUL_LAVA = FLUIDS
            .register("soul_lava", renderProperties -> renderProperties.texture(
                    new ResourceLocation("block/lava_still"),
                    new ResourceLocation("block/lava_flow")).tint(0x80004060));

    public static final FluidRegistryObject<MekanismFluidType, Source, Flowing, LiquidBlock, BucketItem> MIXED_LAVA = FLUIDS
            .register("mixed_lava", renderProperties -> renderProperties.texture(
                    new ResourceLocation("block/lava_still"),
                    new ResourceLocation("block/lava_flow")).tint(0x80804060));
}
