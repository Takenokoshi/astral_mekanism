package astral_mekanism.block.blockentity.enchantedmachine;

import java.util.function.Predicate;

import org.jetbrains.annotations.Nullable;

import com.jerry.mekanism_extras.api.ExtraUpgrade;

import astral_mekanism.block.blockentity.basemachine.BEAMECrystallizer;
import astral_mekanism.integration.AMEEmpowered;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.Upgrade;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.pigment.IPigmentTank;
import mekanism.api.chemical.pigment.Pigment;
import mekanism.api.chemical.slurry.ISlurryTank;
import mekanism.api.chemical.slurry.Slurry;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.chemical.variable.VariableCapacityChemicalTankBuilder;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.inventory.container.sync.SyncableLong;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BEEnchantedCrystallizer extends BEAMECrystallizer {
    private int baselineMaxOperations = 200;
    private long inputTankCapacity = 200 * 5000;

    public BEEnchantedCrystallizer(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    protected int getBaselineMaxOperations() {
        return baselineMaxOperations;
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.track(SyncableInt.create(this::getBaselineMaxOperations, v -> baselineMaxOperations = v));
        container.track(SyncableLong.create(this::getInputTankCapacity, v -> inputTankCapacity = v));
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (AMEEmpowered.empoweredIsLoaded()) {
            if (AMEEmpowered.isEmpoweredSpeed(upgrade) || upgrade == Upgrade.SPEED || upgrade == ExtraUpgrade.STACK) {
                baselineMaxOperations = 200 * ((1 << upgradeComponent.getUpgrades(Upgrade.SPEED)) + (2 << AMEEmpowered
                        .getEmpoweredSpeeds(this))) << upgradeComponent.getUpgrades(ExtraUpgrade.STACK);
            }
        } else if (upgrade == Upgrade.SPEED || upgrade == ExtraUpgrade.STACK) {
            baselineMaxOperations = 200 << (upgradeComponent.getUpgrades(Upgrade.SPEED)
                    + upgradeComponent.getUpgrades(ExtraUpgrade.STACK));
        }
        inputTankCapacity = 5000l * baselineMaxOperations;
    }

    private long getInputTankCapacity() {
        return inputTankCapacity;
    }

    @Override
    protected IGasTank createGasTank(Predicate<Gas> validator, @Nullable IContentsListener listener) {
        return VariableCapacityChemicalTankBuilder.GAS.create(this::getInputTankCapacity,
                (chemical, type) -> type == AutomationType.MANUAL,
                (chemical, type) -> validator.test(chemical),
                validator,
                ChemicalAttributeValidator.ALWAYS_ALLOW, listener);
    }

    @Override
    protected IInfusionTank createInfusionTank(Predicate<InfuseType> validator, @Nullable IContentsListener listener) {
        return VariableCapacityChemicalTankBuilder.INFUSION.create(() -> getInputTankCapacity() * 1000,
                (chemical, type) -> type == AutomationType.MANUAL,
                (chemical, type) -> validator.test(chemical),
                validator,
                ChemicalAttributeValidator.ALWAYS_ALLOW, listener);
    }

    @Override
    protected IPigmentTank createPigmentTank(Predicate<Pigment> validator, @Nullable IContentsListener listener) {
        return VariableCapacityChemicalTankBuilder.PIGMENT.create(this::getInputTankCapacity,
                (chemical, type) -> type == AutomationType.MANUAL,
                (chemical, type) -> validator.test(chemical),
                validator,
                ChemicalAttributeValidator.ALWAYS_ALLOW, listener);
    }

    @Override
    protected ISlurryTank createSlurryTank(Predicate<Slurry> validator, @Nullable IContentsListener listener) {
        return VariableCapacityChemicalTankBuilder.SLURRY.create(this::getInputTankCapacity,
                (chemical, type) -> type == AutomationType.MANUAL,
                (chemical, type) -> validator.test(chemical),
                validator,
                ChemicalAttributeValidator.ALWAYS_ALLOW, listener);
    }

}
