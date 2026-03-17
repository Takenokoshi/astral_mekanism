package astral_mekanism.block.blockentity.compact;

import astral_mekanism.AMEConstants;
import astral_mekanism.block.blockentity.prefab.BEAbstractCompactMixingReactor;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.providers.IGasProvider;
import mekanism.common.registries.MekanismGases;
import mekanism.common.util.HeatUtils;
import mekanism.generators.common.config.MekanismGeneratorsConfig;
import mekanism.generators.common.registries.GeneratorsGases;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class BECompactFusionReactor extends BEAbstractCompactMixingReactor {

    public BECompactFusionReactor(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected IGasProvider leftFuel() {
        return GeneratorsGases.DEUTERIUM;
    }

    @Override
    protected IGasProvider rightFuel() {
        return GeneratorsGases.TRITIUM;
    }

    @Override
    protected IGasProvider mixedFuel() {
        return GeneratorsGases.FUSION_FUEL;
    }

    @Override
    protected void heat(long usedFuel) {
        heatCapacitor.handleHeat(
                MekanismGeneratorsConfig.generators.energyPerFusionFuel.get().multiply(usedFuel).doubleValue());
    }

    @Override
    protected void generateSteam(long usedFuel) {
        if (heatCapacitor.getTemperature() > 400 && !waterTank.isEmpty() && steamTank.getNeeded() > 1) {
            int amount = (int) Math.min(waterTank.getFluidAmount(), Math.min(
                    (heatCapacitor.getHeat() - heatCapacitor.getHeatCapacity() * 400)
                            / HeatUtils.getWaterThermalEnthalpy(),
                    steamTank.getNeeded()));
            waterTank.shrinkStack(amount, Action.EXECUTE);
            steamTank.insert(MekanismGases.STEAM.getStack(amount), Action.EXECUTE, AutomationType.INTERNAL);
            heatCapacitor.handleHeat(-amount * HeatUtils.getWaterThermalEnthalpy());
        }
    }

    @Override
    protected double workableTemp() {
        return 100000000;
    }

    @Override
    public ResourceLocation getJEICategoryName() {
        return AMEConstants.rl("astral_compact_fusion_reactor");
    }

}
