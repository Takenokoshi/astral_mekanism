package astral_mekanism.block.blockentity.enchantedmachine;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.prefab.BEGasToGasMachine;
import astral_mekanism.enums.AMEUpgrade;
import astral_mekanism.registries.AMEMachines;
import astral_mekanism.registries.AMERecipeTypes;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.Upgrade;
import mekanism.api.chemical.attribute.ChemicalAttributeValidator;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.GasToGasRecipe;
import mekanism.common.capabilities.chemical.variable.VariableCapacityChemicalTankBuilder;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableInt;
import mekanism.common.inventory.container.sync.SyncableLong;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.cache.InputRecipeCache.SingleChemical;
import mekanism.common.registries.MekanismBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

public class BEEnchantedSPS extends BEGasToGasMachine {

    int baselineMaxOperations = 200;
    private long inputTankCapacity = 200 * 5000;
    public BEEnchantedSPS(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
    }

    @Override
    public @NotNull IMekanismRecipeTypeProvider<GasToGasRecipe, SingleChemical<Gas, GasStack, GasToGasRecipe>> getRecipeType() {
        return AMERecipeTypes.SPS_RECIPE;
    }

    @Override
    protected int getBaselineMaxOperations() {
        return baselineMaxOperations;
    }

    private long getInputTankCapacity() {
        return inputTankCapacity;
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
        super.recalculateUpgrades(upgrade);
        if (upgrade == AMEUpgrade.HYPER_SPEED.getValue()) {
            int hyperSpeed = upgradeComponent.getUpgrades(AMEUpgrade.HYPER_SPEED.getValue());
            baselineMaxOperations = 2 << hyperSpeed;
        }
        inputTankCapacity = 5000l * baselineMaxOperations;
    }

    @Override
    protected IGasTank createInputTank(BiPredicate<Gas, AutomationType> canExtract,
            BiPredicate<Gas, AutomationType> canInsert, Predicate<Gas> validator,
            @Nullable ChemicalAttributeValidator attributeValidator, @Nullable IContentsListener listener) {
        return VariableCapacityChemicalTankBuilder.GAS.create(this::getInputTankCapacity, canExtract, canInsert,
                validator, attributeValidator, listener);
    }

    @Override
    public List<ResourceLocation> getJEI() {
        return List.of(MekanismBlocks.SPS_CASING.getRegistryName(), AMEMachines.COMPACT_SPS.getRegistryName());
    }
    
}
