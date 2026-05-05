package astral_mekanism.block.blockentity.compact;

import astral_mekanism.AMEConstants;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.providers.IGasProvider;
import mekanism.common.registries.MekanismGases;
import mekanism.generators.common.GeneratorTags;
import mekanism.generators.common.config.MekanismGeneratorsConfig;
import mekanism.generators.common.registries.GeneratorsGases;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class BECompactFusionReactor extends BECompactMixingReactor {

    public static final FloatingLong ENERGY_CAPACITY = FloatingLong.createConst(10_000_000_000L);

    public BECompactFusionReactor(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state,
                MekanismGeneratorsConfig.generators.fusionCasingThermalConductivity,
                MekanismGeneratorsConfig.generators.fusionThermocoupleEfficiency,
                MekanismGeneratorsConfig.generators.energyPerFusionFuel,
                MekanismGeneratorsConfig.generators.fusionWaterHeatingRatio);
    }

    @Override
    protected IGasProvider mixedFuel() {
        return GeneratorsGases.FUSION_FUEL;
    }

    @Override
    protected double initBurnTemperature() {
        return 100000000;
    }

    @Override
    public ResourceLocation getJEICategoryName() {
        return AMEConstants.rl("astral_compact_fusion_reactor");
    }

    @Override
    protected double getInverseConductionCoefficient() {
        return 1 / MekanismGeneratorsConfig.generators.fusionCasingThermalConductivity.get();
    }

    @Override
    protected FloatingLong initEnergyCapacity() {
        return ENERGY_CAPACITY;
    }

    @Override
    protected boolean isLeftFuel(Gas gas) {
        return GeneratorTags.Gases.DEUTERIUM_LOOKUP.contains(gas);
    }

    @Override
    protected boolean isRightFuel(Gas gas) {
        return GeneratorTags.Gases.TRITIUM_LOOKUP.contains(gas);
    }

    @Override
    protected boolean isMixedFuel(Gas gas) {
        return GeneratorTags.Gases.FUSION_FUEL_LOOKUP.contains(gas);
    }

    @Override
    protected void generateSteam(int waterToSteam) {
        steamTank.insert(MekanismGases.STEAM.getStack(waterToSteam), Action.EXECUTE, AutomationType.INTERNAL);
    }

}
