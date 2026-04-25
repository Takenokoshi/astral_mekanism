package astral_mekanism.block.blockentity.appliedmachine;

import com.jerry.generator_extras.common.config.GenLoadConfig;
import com.jerry.generator_extras.common.genregistry.ExtraGenGases;
import com.jerry.mekanism_extras.common.registry.ExtraGases;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.storage.MEStorage;
import astral_mekanism.AMEConstants;
import astral_mekanism.block.blockentity.prefab.BEAbstractAppliedMixingReactor;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.util.HeatUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class BEAppliedNaquadahReactor extends BEAbstractAppliedMixingReactor {

    public BEAppliedNaquadahReactor(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected MekanismKey getMixedFuelKey() {
        return MekanismKey.of(ExtraGases.NAQUADAH_URANIUM_FUEL.getStack(1));
    }

    @Override
    protected MekanismKey getLeftFuelKey() {
        return MekanismKey.of(ExtraGases.RICH_NAQUADAH_FUEL.getStack(1));
    }

    @Override
    protected MekanismKey getRightFuelKey() {
        return MekanismKey.of(ExtraGases.RICH_URANIUM_FUEL.getStack(1));
    }

    @Override
    protected MekanismKey getSteamKey() {
        return MekanismKey.of(ExtraGenGases.POLONIUM_CONTAINING_STEAM.getStack(1));
    }

    @Override
    protected void heat(long burned) {
        heatCapacitor
                .handleHeat(GenLoadConfig.generatorConfig.energyPerReactorFuel.get().multiply(burned).doubleValue());
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
            steam = storage.extract(waterKey, Math.min(steam, Math.min(usedFuel, 9223372036854l) * 1000000),
                    Actionable.SIMULATE, source);
            steam = storage.insert(steamKey, steam, Actionable.SIMULATE, source);
            heatCapacitor.handleHeat(-steam * heatCapacitor.getHeatCapacity());
            storage.extract(waterKey, steam, Actionable.MODULATE, source);
            storage.insert(steamKey, steam, Actionable.MODULATE, source);
        }
    }

    @Override
    protected double workableTemp() {
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

}
