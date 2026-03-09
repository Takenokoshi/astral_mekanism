package astral_mekanism.block.blockentity.compact;

import com.jerry.generator_extras.common.config.GenLoadConfig;
import com.jerry.generator_extras.common.genregistry.ExtraGenGases;
import com.jerry.mekanism_extras.common.registry.ExtraGases;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.block.blockentity.prefab.BEAbstractCompactMixingReactor;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.providers.IGasProvider;
import mekanism.common.util.HeatUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class BECompactNaquadahReactor extends BEAbstractCompactMixingReactor {

    public BECompactNaquadahReactor(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected IGasProvider leftFuel() {
        return ExtraGases.RICH_NAQUADAH_FUEL;
    }

    @Override
    protected IGasProvider rightFuel() {
        return ExtraGases.RICH_URANIUM_FUEL;
    }

    @Override
    protected IGasProvider mixedFuel() {
        return ExtraGases.NAQUADAH_URANIUM_FUEL;
    }

    @Override
    protected void heat(long usedFuel) {
        heatCapacitor
                .handleHeat(GenLoadConfig.generatorConfig.energyPerReactorFuel.get().multiply(usedFuel).doubleValue());
    }

    @Override
    protected void generateSteam(long usedFuel) {
        if (heatCapacitor.getTemperature() > 400 && !waterTank.isEmpty() && steamTank.getNeeded() > 1 && usedFuel > 0) {
            int amount = (int) Math.min(waterTank.getFluidAmount(), Math.min(
                    (heatCapacitor.getHeat() - heatCapacitor.getHeatCapacity() * 400)
                            / HeatUtils.getWaterThermalEnthalpy(),
                    Math.min(steamTank.getNeeded(), Math.min(usedFuel, 9223372036854l) * 1000000)));
            waterTank.shrinkStack(amount, Action.EXECUTE);
            steamTank.insert(ExtraGenGases.POLONIUM_CONTAINING_STEAM.getStack(amount), Action.EXECUTE,
                    AutomationType.INTERNAL);
            heatCapacitor.handleHeat(-amount * HeatUtils.getWaterThermalEnthalpy());
        }
    }

    @Override
    protected double workableTemp() {
        return 400000000;
    }

    @Override
    public ResourceLocation getJEICategoryName() {
        return AstralMekanismID.rl("astral_compact_naquadah_reactor");
    }

}
