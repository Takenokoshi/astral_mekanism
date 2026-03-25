package astral_mekanism.block.blockentity.normalmachine;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import astral_mekanism.block.blockentity.base.BlockEntityProgressMachine;
import astral_mekanism.block.blockentity.interf.IGreenHouse;
import astral_mekanism.generalrecipe.cachedrecipe.CropSoilCachedRecipe.HarvestEntriesOutputHandler;
import astral_mekanism.generalrecipe.cachedrecipe.CropSoilCachedRecipe;
import astral_mekanism.generalrecipe.cachedrecipe.ICachedRecipe;
import astral_mekanism.generalrecipe.recipe.CropSoilRecipe;
import mekanism.api.IContentsListener;
import mekanism.api.Upgrade;
import mekanism.api.fluid.IExtendedFluidTank;
import mekanism.api.inventory.IInventorySlot;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public class BEGreenHouse extends BlockEntityProgressMachine<CropSoilRecipe> implements IGreenHouse<BEGreenHouse> {

    private BasicFluidTank fluidTank;
    private MachineEnergyContainer<BEGreenHouse> energyContainer;

    private InputInventorySlot cropSlot;
    private InputInventorySlot soilSlot;
    private OutputInventorySlot[] outputSlots;
    private FluidInventorySlot fluidSlot;
    private EnergyInventorySlot energySlot;

    private final IInputHandler<ItemStack> cropHandler;
    private final IInputHandler<ItemStack> soilHandler;
    private final IInputHandler<FluidStack> fluidHandler;
    private final HarvestEntriesOutputHandler outputHandler;

    public BEGreenHouse(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, TRACKED_ERROR_TYPES, 1200);
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM, TransmissionType.FLUID,
                TransmissionType.ENERGY);
        configComponent.setupItemIOConfig(List.<IInventorySlot>of(cropSlot, soilSlot),
                List.<IInventorySlot>of(outputSlots), energySlot, false);
        configComponent.setupInputConfig(TransmissionType.FLUID, fluidTank);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ejectorComponent = new TileComponentEjector(this).setOutputData(configComponent, TransmissionType.ITEM);
        cropHandler = InputHelper.getInputHandler(cropSlot, RecipeError.NOT_ENOUGH_INPUT);
        soilHandler = InputHelper.getInputHandler(soilSlot, RecipeError.NOT_ENOUGH_INPUT);
        fluidHandler = InputHelper.getInputHandler(fluidTank, RecipeError.NOT_ENOUGH_INPUT);
        outputHandler = new HarvestEntriesOutputHandler(RecipeError.NOT_ENOUGH_OUTPUT_SPACE, outputSlots);
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(cropSlot = InputInventorySlot.at(
                stack -> containsRecipeCropOther(stack, soilSlot.getStack(), fluidTank.getFluid()),
                this::containsRecipeCrop, recipeCacheListener, 46, 26));
        builder.addSlot(soilSlot = InputInventorySlot.at(
                stack -> containsRecipeSoilOther(cropSlot.getStack(), stack, fluidTank.getFluid()),
                this::containsRecipeSoil, recipeCacheListener, 46, 44));
        outputSlots = new OutputInventorySlot[12];
        for (int i = 0; i < 12; i++) {
            builder.addSlot(outputSlots[i] = OutputInventorySlot.at(listener, i % 4 * 18 + 98, i / 4 * 18 + 17));
        }
        builder.addSlot(fluidSlot = FluidInventorySlot.fill(fluidTank, listener, 10, 53))
                .setSlotOverlay(SlotOverlay.MINUS);
        builder.addSlot(
                energySlot = EnergyInventorySlot.fillOrConvert(energyContainer, this::getLevel, listener, 10, 17));
        return builder.build();
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(fluidTank = BasicFluidTank.input(20000,
                stack -> containsRecipeFluidOther(cropSlot.getStack(), soilSlot.getStack(), stack),
                this::containsRecipeFluid, recipeCacheListener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener,
            IContentsListener recipeCacheListener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener));
        return builder.build();
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        energySlot.fillContainerOrConvert();
        fluidSlot.fillTank();
        recipeCacheLookupMonitor.updateAndProcess();
    }

    @Override
    public @Nullable CropSoilRecipe getRecipe(int cacheIndex) {
        return findFirstRecipe(cropHandler, soilHandler, fluidHandler);
    }

    @Override
    public @NotNull ICachedRecipe<CropSoilRecipe> createNewCachedRecipe(@NotNull CropSoilRecipe recipe,
            int cacheIndex) {
        ticksRequired = recipe.requiredTicks;
        return new CropSoilCachedRecipe(recipe, recheckAllRecipeErrors, cropHandler, soilHandler, outputHandler,
                fluidHandler)
                .setErrorsChanged(this::onErrorsChanged)
                .setCanHolderFunction(() -> MekanismUtils.canFunction(this))
                .setEnergyRequirements(energyContainer::getEnergyPerTick, energyContainer)
                .setActive(this::setActive)
                .setRequiredTicks(() -> (int) Math
                        .round(Math.pow(0.75, upgradeComponent.getUpgrades(Upgrade.SPEED)) * recipe.requiredTicks))
                .setOnFinish(this::markForSave)
                .setOperatingTicksChanged(this::setOperatingTicks);
    }

    @Override
    public IExtendedFluidTank getFluidTank() {
        return fluidTank;
    }

    @Override
    public MachineEnergyContainer<BEGreenHouse> getEnergyContainer() {
        return energyContainer;
    }
}