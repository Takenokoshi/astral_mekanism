package astral_mekanism.block.blockentity.enchantedmachine;

import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.jerry.mekanism_extras.api.ExtraUpgrade;

import astral_mekanism.block.blockentity.basemachine.BEAMEPressurizedReactionChamber;
import astral_mekanism.integration.AMEEmpowered;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.Upgrade;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.math.MathUtils;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.PressurizedReactionRecipe;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.common.capabilities.chemical.variable.VariableCapacityChemicalTankBuilder;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.fluid.VariableCapacityFluidTank;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.inventory.container.sync.SyncableLong;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class BEEnchantedPRC extends BEAMEPressurizedReactionChamber {
    private int baselineMaxOperations = 600;
    private int recipeTicksRequired = 200;
    private int ticksRequired = 200;
    private int operatingTicks = 0;
    private int fluidTankCapacity = 600 * 5000;
    private long gasTankCapacity = 600 * 5000;

    public BEEnchantedPRC(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
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
        container.track(SyncableInt.create(this::getTicksRequired, v -> ticksRequired = v));
        container.track(SyncableInt.create(() -> this.operatingTicks, this::setOperatingTicks));
        container.track(SyncableInt.create(() -> this.recipeTicksRequired, v -> recipeTicksRequired = v));
        container.track(SyncableInt.create(this::getFluidTankCapacity, v -> fluidTankCapacity = v));
        container.track(SyncableLong.create(this::getGasTankCapacity, v -> gasTankCapacity = v));
    }

    @Override
    public void recalculateUpgrades(Upgrade upgrade) {
        super.recalculateUpgrades(upgrade);
        if (upgrade == Upgrade.SPEED) {
            recalculateTicks();
        } else if (upgrade == ExtraUpgrade.STACK) {
            baselineMaxOperations = 600 << upgradeComponent.getUpgrades(ExtraUpgrade.STACK);
        } else if (AMEEmpowered.empoweredIsLoaded() && AMEEmpowered.isEmpoweredEnergy(upgrade)) {
            recalculateTicks();
        }
        gasTankCapacity = 5000l * baselineMaxOperations;
        fluidTankCapacity = MathUtils.clampToInt(gasTankCapacity);
    }

    private int getFluidTankCapacity() {
        return fluidTankCapacity;
    }

    private long getGasTankCapacity() {
        return gasTankCapacity;
    }

    @Override
    public @NotNull CachedRecipe<PressurizedReactionRecipe> createNewCachedRecipe(
            @NotNull PressurizedReactionRecipe recipe, int index) {
        return super.createNewCachedRecipe(recipe, index)
                .setRequiredTicks(this::getTicksRequired)
                .setOperatingTicksChanged(this::setOperatingTicks);
    }

    @Override
    public void onCachedRecipeChanged(@Nullable CachedRecipe<PressurizedReactionRecipe> cachedRecipe, int cacheIndex) {
        super.onCachedRecipeChanged(cachedRecipe, cacheIndex);
        recipeTicksRequired = cachedRecipe.getRecipe().getDuration();
        recalculateTicks();
    }

    private void recalculateTicks() {
        ticksRequired = AMEEmpowered.empoweredIsLoaded()
                ? AMEEmpowered.getTicks(recipeTicksRequired, this)
                : MekanismUtils.getTicks(this, recipeTicksRequired);
    }

    @Override
    public double getScaledProgress() {
        return getActive() ? operatingTicks / (double) recipeTicksRequired : 0;
    }

    private void setOperatingTicks(int v) {
        operatingTicks = v;
    }

    private int getTicksRequired() {
        return ticksRequired;
    }

    @Override
    protected IGasTank createInputGasTank(BiPredicate<Gas, AutomationType> canExtract,
            BiPredicate<Gas, AutomationType> canInsert, Predicate<Gas> validator,
            @Nullable ChemicalAttributeValidator attributeValidator, @Nullable IContentsListener listener) {
        return VariableCapacityChemicalTankBuilder.GAS.create(this::getGasTankCapacity, canExtract, canInsert,
                validator, attributeValidator, listener);
    }

    @Override
    protected BasicFluidTank createInputFluidTank(Predicate<FluidStack> canInsert, Predicate<FluidStack> validator,
            @Nullable IContentsListener listener) {
        return VariableCapacityFluidTank.create(this::getFluidTankCapacity,
                (fluid, type) -> type == AutomationType.MANUAL,
                (fluid, type) -> canInsert.test(fluid), validator, listener);
    }

}
