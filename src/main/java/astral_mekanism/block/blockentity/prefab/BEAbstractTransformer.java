package astral_mekanism.block.blockentity.prefab;

import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import appeng.recipes.transform.TransformRecipe;
import astral_mekanism.block.blockentity.elements.AstralMekDataType;
import astral_mekanism.block.blockentity.elements.ExtendedComponentEjector;
import astral_mekanism.generalrecipe.GeneralRecipeType;
import astral_mekanism.generalrecipe.IUnifiedRecipeTypeProvider;
import astral_mekanism.generalrecipe.cachedrecipe.ICachedRecipe;
import astral_mekanism.generalrecipe.cachedrecipe.TransformCachedRecipe;
import astral_mekanism.generalrecipe.cachedrecipe.TransformCachedRecipe.TransformItemOutputHandler;
import astral_mekanism.generalrecipe.lookup.cache.recipe.TransformRecipeInputRecipeCache;
import astral_mekanism.generalrecipe.lookup.handler.IUnifiedRecipeTypedLookupHandler;
import astral_mekanism.generalrecipe.lookup.monitor.UnifiedRecipeCacheLookupMonitor;
import astral_mekanism.recipes.cachedRecipe.MekanicalTransformCachedRecipe;
import astral_mekanism.recipes.inputRecipeCache.MekanicalTransformRecipeCache;
import astral_mekanism.recipes.lookup.MekanicalTransformRecipeLookUpHandler;
import astral_mekanism.recipes.output.AMOutputHelper;
import astral_mekanism.recipes.output.ItemFluidOutput;
import astral_mekanism.recipes.recipe.MekanicalTransformRecipe;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.IContentsListener;
import mekanism.api.providers.IBlockProvider;
import mekanism.api.recipes.cache.CachedRecipe;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.inputs.InputHelper;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.common.capabilities.energy.MachineEnergyContainer;
import mekanism.common.capabilities.fluid.BasicFluidTank;
import mekanism.common.capabilities.holder.energy.EnergyContainerHelper;
import mekanism.common.capabilities.holder.energy.IEnergyContainerHolder;
import mekanism.common.capabilities.holder.fluid.FluidTankHelper;
import mekanism.common.capabilities.holder.fluid.IFluidTankHolder;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableBoolean;
import mekanism.common.inventory.slot.EnergyInventorySlot;
import mekanism.common.inventory.slot.FluidInventorySlot;
import mekanism.common.inventory.slot.InputInventorySlot;
import mekanism.common.inventory.slot.OutputInventorySlot;
import mekanism.common.inventory.warning.WarningTracker.WarningType;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.recipe.IMekanismRecipeTypeProvider;
import mekanism.common.recipe.lookup.monitor.RecipeCacheLookupMonitor;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.FluidSlotInfo;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.tile.prefab.TileEntityRecipeMachine;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;

public abstract class BEAbstractTransformer extends TileEntityConfigurableMachine {

    private static final String modeNBTtag = "mode";
    public static final RecipeError NOT_ENOUGH_INPUT_IA = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_INPUT_IB = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_INPUT_IC = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_INPUT_FA = RecipeError.create();
    public static final RecipeError NOT_ENOUGH_INPUT_FB = RecipeError.create();

    protected static final List<RecipeError> TRACKED_ERROR_TYPES = List.of(RecipeError.NOT_ENOUGH_ENERGY,
            NOT_ENOUGH_INPUT_IA, NOT_ENOUGH_INPUT_IB, NOT_ENOUGH_INPUT_IC, NOT_ENOUGH_INPUT_FA, NOT_ENOUGH_INPUT_FB,
            RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
            RecipeError.INPUT_DOESNT_PRODUCE_OUTPUT);

    protected boolean mode = false;// false=AE2Transform,true=MekanicalTransform
    protected final BooleanSupplier recheckAllRecipeErrors;
    private final List<RecipeError> errorTypes;
    private final boolean[] trackedErrors;
    protected InputInventorySlot inputSlotA;
    protected InputInventorySlot inputSlotB;
    protected InputInventorySlot inputSlotC;
    protected BasicFluidTank inputTankA;
    protected BasicFluidTank inputTankB;
    protected OutputInventorySlot outputSlot;
    protected BasicFluidTank outputTank;
    protected MachineEnergyContainer<BEAbstractTransformer> energyContainer;
    protected EnergyInventorySlot energySlot;
    protected FluidInventorySlot fluidSlotIA;
    protected OutputInventorySlot fluidSlotOA;
    protected FluidInventorySlot fluidSlotIB;
    protected OutputInventorySlot fluidSlotOB;
    protected FluidInventorySlot fluidSlotIO;
    protected OutputInventorySlot fluidSlotOO;
    protected final IInputHandler<ItemStack> inputHandlerIA;
    protected final IInputHandler<ItemStack> inputHandlerIB;
    protected final IInputHandler<ItemStack> inputHandlerIC;
    protected final IInputHandler<FluidStack> inputHandlerFA;
    protected final AE2TransformRecipeLookUpObject ae2LookUpObject;
    protected final UnifiedRecipeCacheLookupMonitor<TransformRecipe> ae2LookupMonitor;
    protected final MekanicalTransformRecipeLookUpObject mekanicalLookUpObject;
    protected final RecipeCacheLookupMonitor<MekanicalTransformRecipe> mekanicalLookupMonitor;

    public BEAbstractTransformer(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        recheckAllRecipeErrors = TileEntityRecipeMachine.shouldRecheckAllErrors(this);
        errorTypes = List.copyOf(TRACKED_ERROR_TYPES);
        trackedErrors = new boolean[this.errorTypes.size()];
        configComponent = new TileComponentConfig(
                this, TransmissionType.ITEM, TransmissionType.FLUID, TransmissionType.ENERGY);
        ConfigInfo itemInfo = configComponent.getConfig(TransmissionType.ITEM);
        itemInfo.addSlotInfo(DataType.INPUT, new InventorySlotInfo(true, false,
                inputSlotA, inputSlotB, inputSlotC));
        itemInfo.addSlotInfo(DataType.OUTPUT, new InventorySlotInfo(false, true, outputSlot));
        itemInfo.addSlotInfo(DataType.INPUT_OUTPUT, new InventorySlotInfo(true, true,
                inputSlotA, inputSlotB, inputSlotC, outputSlot));
        itemInfo.addSlotInfo(DataType.INPUT_1, new InventorySlotInfo(true, false, inputSlotA));
        itemInfo.addSlotInfo(DataType.INPUT_2, new InventorySlotInfo(true, false, inputSlotB));
        itemInfo.addSlotInfo(AstralMekDataType.INPUT3, new InventorySlotInfo(true, false, inputSlotC));
        itemInfo.addSlotInfo(AstralMekDataType.INPUT1_OUTPUT, new InventorySlotInfo(true, true,
                inputSlotA, outputSlot));
        itemInfo.addSlotInfo(AstralMekDataType.INPUT2_OUTPUT, new InventorySlotInfo(true, true,
                inputSlotB, outputSlot));
        itemInfo.addSlotInfo(AstralMekDataType.INPUT3_OUTPUT, new InventorySlotInfo(true, true,
                inputSlotC, outputSlot));
        itemInfo.setCanEject(true);
        ConfigInfo fluidInfo = configComponent.getConfig(TransmissionType.FLUID);
        fluidInfo.addSlotInfo(DataType.INPUT, new FluidSlotInfo(true, false, inputTankA, inputTankB));
        fluidInfo.addSlotInfo(DataType.OUTPUT, new FluidSlotInfo(false, true, outputTank));
        fluidInfo.addSlotInfo(DataType.INPUT_OUTPUT, new FluidSlotInfo(true, true,
                inputTankA, inputTankB, outputTank));
        fluidInfo.addSlotInfo(DataType.INPUT_1, new FluidSlotInfo(true, false, inputTankA));
        fluidInfo.addSlotInfo(DataType.INPUT_2, new FluidSlotInfo(true, false, inputTankB));
        fluidInfo.addSlotInfo(AstralMekDataType.INPUT1_OUTPUT, new FluidSlotInfo(true, true,
                inputTankA, outputTank));
        fluidInfo.addSlotInfo(AstralMekDataType.INPUT2_OUTPUT, new FluidSlotInfo(true, true,
                inputTankB, outputTank));
        fluidInfo.setCanEject(true);
        configComponent.setupInputConfig(TransmissionType.ENERGY, energyContainer);
        ejectorComponent = new ExtendedComponentEjector(this, () -> 0x7fffffff)
                .setOutputData(configComponent, TransmissionType.ITEM, TransmissionType.FLUID)
                .setCanTypeEject((typeT, type) -> {
                    if (typeT == TransmissionType.ITEM) {
                        return type == DataType.OUTPUT
                                || type == DataType.INPUT_OUTPUT
                                || type == AstralMekDataType.INPUT1_OUTPUT
                                || type == AstralMekDataType.INPUT2_OUTPUT
                                || type == AstralMekDataType.INPUT3_OUTPUT;
                    } else if (typeT == TransmissionType.FLUID) {
                        return type == DataType.OUTPUT
                                || type == DataType.INPUT_OUTPUT
                                || type == AstralMekDataType.INPUT1_OUTPUT
                                || type == AstralMekDataType.INPUT2_OUTPUT;
                    } else {
                        return false;
                    }
                })
                .setCanInventorySlotEject((slot, type) -> {
                    boolean result = slot == outputSlot;
                    result &= type == DataType.OUTPUT
                            || type == DataType.INPUT_OUTPUT
                            || type == AstralMekDataType.INPUT1_OUTPUT
                            || type == AstralMekDataType.INPUT2_OUTPUT
                            || type == AstralMekDataType.INPUT3_OUTPUT;
                    return result;
                })
                .setCanFluidTankEject((tank, type) -> {
                    boolean result = tank == outputTank;
                    result &= type == DataType.OUTPUT
                            || type == DataType.INPUT_OUTPUT
                            || type == AstralMekDataType.INPUT1_OUTPUT
                            || type == AstralMekDataType.INPUT2_OUTPUT;
                    return result;
                });
        inputHandlerIA = InputHelper.getInputHandler(inputSlotA, NOT_ENOUGH_INPUT_IA);
        inputHandlerIB = InputHelper.getInputHandler(inputSlotB, NOT_ENOUGH_INPUT_IB);
        inputHandlerIC = InputHelper.getInputHandler(inputSlotC, NOT_ENOUGH_INPUT_IC);
        inputHandlerFA = InputHelper.getInputHandler(inputTankA, NOT_ENOUGH_INPUT_FA);
        ae2LookUpObject = new AE2TransformRecipeLookUpObject(this);
        ae2LookupMonitor = new UnifiedRecipeCacheLookupMonitor<>(ae2LookUpObject);
        mekanicalLookUpObject = new MekanicalTransformRecipeLookUpObject(this);
        mekanicalLookupMonitor = new RecipeCacheLookupMonitor<>(mekanicalLookUpObject);
    }

    @Override
    protected void presetVariables() {
        super.presetVariables();
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addSlot(inputSlotA = InputInventorySlot.at(stack -> mode
                ? mekanicalLookUpObject.containsRecipeFAOther(stack, inputSlotB.getStack(), inputSlotC.getStack(),
                        inputTankA.getFluid(), inputTankB.getFluid())
                : ae2LookUpObject.containsInputIAOther(stack, inputSlotB.getStack(), inputSlotC.getStack(),
                        inputTankA.getFluid()),
                stack -> mode ? mekanicalLookUpObject.containsRecipeIA(stack) : ae2LookUpObject.containsInputIA(stack),
                this::saveCache, 64, 17))
                .tracksWarnings(
                        slot -> slot.warning(WarningType.NO_MATCHING_RECIPE, getWarningCheck(NOT_ENOUGH_INPUT_IA)));
        builder.addSlot(inputSlotB = InputInventorySlot.at(stack -> mode
                ? mekanicalLookUpObject.containsRecipeIBOther(inputSlotA.getStack(), stack, inputSlotC.getStack(),
                        inputTankA.getFluid(), inputTankB.getFluid())
                : ae2LookUpObject.containsInputIBOther(inputSlotA.getStack(), stack, inputSlotC.getStack(),
                        inputTankA.getFluid()),
                stack -> mode ? mekanicalLookUpObject.containsRecipeIB(stack) : ae2LookUpObject.containsInputIB(stack),
                this::saveCache, 64, 35))
                .tracksWarnings(
                        slot -> slot.warning(WarningType.NO_MATCHING_RECIPE, getWarningCheck(NOT_ENOUGH_INPUT_IB)));
        builder.addSlot(inputSlotC = InputInventorySlot.at(stack -> mode
                ? mekanicalLookUpObject.containsRecipeICOther(inputSlotA.getStack(), inputSlotB.getStack(), stack,
                        inputTankA.getFluid(), inputTankB.getFluid())
                : ae2LookUpObject.containsInputICOther(inputSlotA.getStack(), inputSlotB.getStack(), stack,
                        inputTankA.getFluid()),
                stack -> mode ? mekanicalLookUpObject.containsRecipeIC(stack) : ae2LookUpObject.containsInputIC(stack),
                this::saveCache, 64, 53))
                .tracksWarnings(
                        slot -> slot.warning(WarningType.NO_MATCHING_RECIPE, getWarningCheck(NOT_ENOUGH_INPUT_IC)));
        builder.addSlot(outputSlot = OutputInventorySlot.at(listener, 136, 35))
                .tracksWarnings(slot -> slot
                        .warning(WarningType.NO_SPACE_IN_OUTPUT, getWarningCheck(RecipeError.NOT_ENOUGH_OUTPUT_SPACE)));
        builder.addSlot(fluidSlotIA = FluidInventorySlot.fill(inputTankA, listener, 28, 17));
        builder.addSlot(fluidSlotOA = OutputInventorySlot.at(listener, 28, 53));
        builder.addSlot(fluidSlotIB = FluidInventorySlot.fill(inputTankB, listener, 100, 17));
        builder.addSlot(fluidSlotOB = OutputInventorySlot.at(listener, 100, 53));
        builder.addSlot(fluidSlotIO = FluidInventorySlot.drain(outputTank, listener, 172, 17));
        builder.addSlot(fluidSlotOO = OutputInventorySlot.at(listener, 172, 53));
        builder.addSlot(energySlot = EnergyInventorySlot
                .fillOrConvert(energyContainer, this::getLevel, listener, 200, 8));
        return builder.build();
    }

    @NotNull
    @Override
    protected IFluidTankHolder getInitialFluidTanks(IContentsListener listener) {
        FluidTankHelper builder = FluidTankHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addTank(inputTankA = BasicFluidTank.input(fluidTankCapacity(), stack -> mode
                ? mekanicalLookUpObject.containsRecipeFAOther(
                        inputSlotA.getStack(), inputSlotB.getStack(), inputSlotC.getStack(),
                        stack, inputTankB.getFluid())
                : ae2LookUpObject.containsInputFAOther(
                        inputSlotA.getStack(), inputSlotB.getStack(), inputSlotC.getStack(), stack),
                stack -> mode ? mekanicalLookUpObject.containsRecipeFA(stack) : ae2LookUpObject.containsInputFA(stack),
                this::saveCache));
        builder.addTank(inputTankB = BasicFluidTank.input(fluidTankCapacity(),
                stack -> mode && mekanicalLookUpObject.containsRecipeFBOther(
                        inputSlotA.getStack(), inputSlotB.getStack(), inputSlotC.getStack(),
                        inputTankA.getFluid(), stack),
                stack -> mode && mekanicalLookUpObject.containsRecipeFB(stack),
                this::saveCache));
        builder.addTank(outputTank = BasicFluidTank.output(fluidTankCapacity(), listener));
        return builder.build();
    }

    @NotNull
    @Override
    protected IEnergyContainerHolder getInitialEnergyContainers(IContentsListener listener) {
        EnergyContainerHelper builder = EnergyContainerHelper.forSideWithConfig(this::getDirection, this::getConfig);
        builder.addContainer(energyContainer = MachineEnergyContainer.input(this, listener));
        return builder.build();
    }

    protected void saveCache() {
        markForSave();
        if (mode) {
            mekanicalLookupMonitor.onChange();
        } else {
            ae2LookupMonitor.onChange();
        }
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        if (mode) {
            mekanicalLookupMonitor.updateAndProcess();
        } else {
            ae2LookupMonitor.updateAndProcess();
        }
        fluidSlotIA.fillTank(fluidSlotOA);
        fluidSlotIB.fillTank(fluidSlotOB);
        fluidSlotIO.fillTank(fluidSlotOO);
        energySlot.fillContainerOrConvert();
    }

    public void changeMode() {
        mode = (!mode);
        if (mode) {
            ae2LookupMonitor.setHasNoRecipe(0);
            mekanicalLookupMonitor.onChange();
        } else {
            mekanicalLookupMonitor.setHasNoRecipe(0);
            ae2LookupMonitor.onChange();
        }
        markForSave();
        onContentsChanged();
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        if (nbt.contains(modeNBTtag)) {
            mode = nbt.getBoolean(modeNBTtag);
        }
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbtTags) {
        super.saveAdditional(nbtTags);
        nbtTags.putBoolean(modeNBTtag, mode);
    }

    @Override
    public void addContainerTrackers(MekanismContainer container) {
        super.addContainerTrackers(container);
        container.trackArray(trackedErrors);
        container.track(SyncableBoolean.create(() -> mode, v -> mode = v));
    }

    protected void onErrorsChanged(Set<RecipeError> errors) {
        for (int i = 0; i < trackedErrors.length; i++) {
            trackedErrors[i] = errors.contains(errorTypes.get(i));
        }
    }

    public BooleanSupplier getWarningCheck(RecipeError error) {
        int errorIndex = errorTypes.indexOf(error);
        if (errorIndex == -1) {
            return () -> false;
        }
        return () -> trackedErrors[errorIndex];
    }

    protected abstract int fluidTankCapacity();

    protected abstract ICachedRecipe<TransformRecipe> consumeCachedRecipe(ICachedRecipe<TransformRecipe> cachedRecipe);

    protected abstract CachedRecipe<MekanicalTransformRecipe> consumeCachedRecipe(
            CachedRecipe<MekanicalTransformRecipe> cachedRecipe);

    public static class AE2TransformRecipeLookUpObject
            implements IUnifiedRecipeTypedLookupHandler<TransformRecipe, TransformRecipeInputRecipeCache> {

        private final BEAbstractTransformer transformer;
        private final TransformItemOutputHandler outputHandler;

        public AE2TransformRecipeLookUpObject(BEAbstractTransformer transformer) {
            this.transformer = transformer;
            this.outputHandler = new TransformItemOutputHandler(this.transformer.outputSlot,
                    RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
        }

        @Override
        public @Nullable TransformRecipe getRecipe(int cacheIndex) {
            return getRecipeType().getInputCache().findFirstRecipe(getHandlerWorld(),
                    transformer.inputHandlerFA.getInput(),
                    transformer.inputHandlerIA.getInput(),
                    transformer.inputHandlerIB.getInput(),
                    transformer.inputHandlerIC.getInput());
        }

        @Override
        public @NotNull ICachedRecipe<TransformRecipe> createNewCachedRecipe(@NotNull TransformRecipe recipe,
                int cacheIndex) {
            ICachedRecipe<TransformRecipe> cachedRecipe = new TransformCachedRecipe(recipe,
                    transformer.recheckAllRecipeErrors,
                    transformer.inputHandlerIA, transformer.inputHandlerIB, transformer.inputHandlerIC,
                    transformer.inputHandlerFA, outputHandler)
                    .setErrorsChanged(transformer::onErrorsChanged)
                    .setCanHolderFunction(() -> MekanismUtils.canFunction(transformer))
                    .setActive(transformer::setActive)
                    .setEnergyRequirements(transformer.energyContainer::getEnergyPerTick, transformer.energyContainer)
                    .setOnFinish(transformer::markForSave);
            return transformer.consumeCachedRecipe(cachedRecipe);
        }

        @Override
        public void onContentsChanged() {
            transformer.onContentsChanged();
        }

        @Override
        public @NotNull IUnifiedRecipeTypeProvider<TransformRecipe, TransformRecipeInputRecipeCache> getRecipeType() {
            return GeneralRecipeType.TRANSFORM;
        }

        public boolean containsInputIA(ItemStack input) {
            return getRecipeType().getInputCache().containsInputFirst(getHandlerWorld(), input);
        }

        public boolean containsInputIB(ItemStack input) {
            return getRecipeType().getInputCache().containsInputSecond(getHandlerWorld(), input);
        }

        public boolean containsInputIC(ItemStack input) {
            return getRecipeType().getInputCache().containsInputThird(getHandlerWorld(), input);
        }

        public boolean containsInputFA(FluidStack input) {
            return getRecipeType().getInputCache().containsInputFluid(getHandlerWorld(), input);
        }

        public boolean containsInputIAOther(ItemStack inputIA, ItemStack inputIB, ItemStack inputIC,
                FluidStack inputFA) {
            return getRecipeType().getInputCache().containsFirstOther(getHandlerWorld(),
                    inputFA, inputIA, inputIB, inputIC);
        }

        public boolean containsInputIBOther(ItemStack inputIA, ItemStack inputIB, ItemStack inputIC,
                FluidStack inputFA) {
            return getRecipeType().getInputCache().containsSecondOther(getHandlerWorld(),
                    inputFA, inputIA, inputIB, inputIC);
        }

        public boolean containsInputICOther(ItemStack inputIA, ItemStack inputIB, ItemStack inputIC,
                FluidStack inputFA) {
            return getRecipeType().getInputCache().containsThirdOther(getHandlerWorld(),
                    inputFA, inputIA, inputIB, inputIC);
        }

        public boolean containsInputFAOther(ItemStack inputIA, ItemStack inputIB, ItemStack inputIC,
                FluidStack inputFA) {
            return getRecipeType().getInputCache().containsFluidOther(getHandlerWorld(),
                    inputFA, inputIA, inputIB, inputIC);
        }

    }

    public static class MekanicalTransformRecipeLookUpObject implements MekanicalTransformRecipeLookUpHandler {

        private final BEAbstractTransformer transformer;
        private final IInputHandler<FluidStack> inputHandlerFB;
        private final IOutputHandler<ItemFluidOutput> outputHandler;

        public MekanicalTransformRecipeLookUpObject(BEAbstractTransformer transformer) {
            this.transformer = transformer;
            this.inputHandlerFB = InputHelper.getInputHandler(transformer.inputTankB, NOT_ENOUGH_INPUT_FB);
            this.outputHandler = AMOutputHelper.getOutputHandler(transformer.outputSlot,
                    RecipeError.NOT_ENOUGH_OUTPUT_SPACE,
                    transformer.outputTank, RecipeError.NOT_ENOUGH_OUTPUT_SPACE);
        }

        @Override
        public @NotNull IMekanismRecipeTypeProvider<MekanicalTransformRecipe, MekanicalTransformRecipeCache> getRecipeType() {
            return AstralMekanismRecipeTypes.MEKANICAL_TRAMSFORM;
        }

        @Override
        public @Nullable MekanicalTransformRecipe getRecipe(int cacheIndex) {
            return findFirstRecipe(transformer.inputHandlerIA, transformer.inputHandlerIB, transformer.inputHandlerIC,
                    transformer.inputHandlerFA, inputHandlerFB);
        }

        @Override
        public @NotNull CachedRecipe<MekanicalTransformRecipe> createNewCachedRecipe(
                @NotNull MekanicalTransformRecipe recipe, int cacheIndex) {
            CachedRecipe<MekanicalTransformRecipe> cachedRecipe = new MekanicalTransformCachedRecipe(recipe,
                    transformer.recheckAllRecipeErrors,
                    transformer.inputHandlerIA, transformer.inputHandlerIB, transformer.inputHandlerIC,
                    transformer.inputHandlerFA, inputHandlerFB, outputHandler)
                    .setErrorsChanged(transformer::onErrorsChanged)
                    .setCanHolderFunction(() -> MekanismUtils.canFunction(transformer))
                    .setActive(transformer::setActive)
                    .setEnergyRequirements(transformer.energyContainer::getEnergyPerTick, transformer.energyContainer)
                    .setOnFinish(transformer::markForSave);
            return transformer.consumeCachedRecipe(cachedRecipe);
        }

        @Override
        public void onContentsChanged() {
            transformer.onContentsChanged();
        }

    }

}
