package astral_mekanism.block.blockentity.compact;

import com.jerry.mekanism_extras.common.ExtraTag;
import com.jerry.generator_extras.common.config.GenLoadConfig;
import com.jerry.generator_extras.common.genregistry.ExtraGenGases;
import com.jerry.mekanism_extras.common.registry.ExtraGases;

import astral_mekanism.AMEConstants;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.providers.IGasProvider;
import mekanism.common.registries.MekanismGases;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class BECompactNaquadahReactor extends BECompactMixingReactor {

    public static final FloatingLong ENERGY_CAPACITY = FloatingLong.createConst(10_000_000_000_000l);

    public BECompactNaquadahReactor(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state,
                GenLoadConfig.generatorConfig.reactorCasingThermalConductivity,
                GenLoadConfig.generatorConfig.reactorThermocoupleEfficiency,
                GenLoadConfig.generatorConfig.energyPerReactorFuel,
                GenLoadConfig.generatorConfig.reactorWaterHeatingRatio);
    }

    @Override
    protected IGasProvider mixedFuel() {
        return ExtraGases.NAQUADAH_URANIUM_FUEL;
    }

    @Override
    protected double initBurnTemperature() {
        return 400000000;
    }

    @Override
    public ResourceLocation getJEICategoryName() {
        return AMEConstants.rl("astral_compact_naquadah_reactor");
    }

    @Override
    protected double getInverseConductionCoefficient() {
        return 1 / GenLoadConfig.generatorConfig.reactorCasingThermalConductivity.get();
    }

    @Override
    protected FloatingLong initEnergyCapacity() {
        return ENERGY_CAPACITY;
    }

    @Override
    protected boolean isLeftFuel(Gas gas) {
        return ExtraTag.Gases.RICH_NAQUADAH_FUEL_LOOKUP.contains(gas);
    }

    @Override
    protected boolean isRightFuel(Gas gas) {
        return ExtraTag.Gases.RICH_URANIUM_FUEL_LOOKUP.contains(gas);
    }

    @Override
    protected boolean isMixedFuel(Gas gas) {
        return ExtraTag.Gases.NAQUADAH_URANIUM_FUEL_LOOKUP.contains(gas);
    }

    @Override
    protected void generateSteam(int waterToSteam) {
        if (lastBurnedFuel > 0) {
            steamTank.insert(
                    ExtraGenGases.POLONIUM_CONTAINING_STEAM.getStack(
                            Math.min(Math.min(lastBurnedFuel, 9223372036854l) * 1000000, waterToSteam)),
                    Action.EXECUTE, AutomationType.INTERNAL);
        } else {
            steamTank.insert(MekanismGases.STEAM.getStack(waterToSteam), Action.EXECUTE, AutomationType.INTERNAL);
        }
    }

}
