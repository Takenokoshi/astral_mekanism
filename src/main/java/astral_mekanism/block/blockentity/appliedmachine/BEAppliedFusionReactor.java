package astral_mekanism.block.blockentity.appliedmachine;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.storage.MEStorage;
import astral_mekanism.AMEConstants;
import astral_mekanism.block.blockentity.prefab.BEAbstractAppliedMixingReactor;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.registries.MekanismGases;
import mekanism.common.util.HeatUtils;
import mekanism.generators.common.config.MekanismGeneratorsConfig;
import mekanism.generators.common.registries.GeneratorsGases;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class BEAppliedFusionReactor extends BEAbstractAppliedMixingReactor {

    public BEAppliedFusionReactor(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected MekanismKey getMixedFuelKey() {
        return MekanismKey.of(GeneratorsGases.FUSION_FUEL.getStack(1));
    }

    @Override
    protected MekanismKey getLeftFuelKey() {
        return MekanismKey.of(GeneratorsGases.DEUTERIUM.getStack(1));
    }

    @Override
    protected MekanismKey getRightFuelKey() {
        return MekanismKey.of(GeneratorsGases.TRITIUM.getStack(1));
    }

    @Override
    protected MekanismKey getSteamKey() {
        return MekanismKey.of(MekanismGases.STEAM.getStack(1));
    }

    @Override
    protected void heat(long burned) {
        heatCapacitor.handleHeat(
                MekanismGeneratorsConfig.generators.energyPerFusionFuel.get().multiply(burned).doubleValue());
    }

    @Override
    protected void generateSteam(long usedFuel) {
        MEStorage storage = getMeStorage();
        if (storage == null) {
            return;
        }
        IActionSource source = IActionSource.ofMachine(this);
        if (heatCapacitor.getTemperature() > workableTemp()) {
            long steam = (long) ((heatCapacitor.getHeat() - workableTemp() * heatCapacitor.getHeatCapacity())
                    / HeatUtils.getWaterThermalEnthalpy());
            steam = storage.extract(waterKey, steam, Actionable.SIMULATE, source);
            steam = storage.insert(steamKey, steam, Actionable.SIMULATE, source);
            heatCapacitor.handleHeat(-steam * heatCapacitor.getHeatCapacity());
            storage.extract(waterKey, steam, Actionable.MODULATE, source);
            storage.insert(steamKey, steam, Actionable.MODULATE, source);
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

    @Override
    protected double getInverseConductionCoefficient() {
        return 1 / MekanismGeneratorsConfig.generators.fusionCasingThermalConductivity.get();
    }
}
