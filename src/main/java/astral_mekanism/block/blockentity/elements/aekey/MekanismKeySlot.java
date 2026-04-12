package astral_mekanism.block.blockentity.elements.aekey;

import java.util.Collections;
import java.util.List;
import java.util.function.LongSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.storage.MEStorage;
import astral_mekanism.recipes.handler.IBoxedChemicalInputHandler;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import me.ramidzkh.mekae2.ae2.MekanismKeyType;
import mekanism.api.IContentsListener;
import mekanism.api.MekanismAPI;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.merged.BoxedChemicalStack;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.SlurryStack;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.InfusionStackIngredient;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.PigmentStackIngredient;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.SlurryStackIngredient;
import mekanism.api.recipes.inputs.IInputHandler;
import mekanism.api.recipes.outputs.IOutputHandler;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.list.SyncableStringList;
import net.minecraft.resources.ResourceLocation;

public class MekanismKeySlot extends AEKeySlot<MekanismKey> {

    public MekanismKeySlot(Supplier<MEStorage> storageSupplier, Predicate<? super MekanismKey> keyPredicate,
            IContentsListener listener, int slotIndex) {
        super(storageSupplier, keyPredicate, listener, t -> (MekanismKey) (MekanismKeyType.TYPE.loadKeyFromTag(t)),
                MekanismKey::toTag, k -> k instanceof MekanismKey ? (MekanismKey) k : null, slotIndex);
    }

    public MekanismKeySlot(Supplier<MEStorage> storageSupplier,
            IContentsListener listener, int slotIndex) {
        this(storageSupplier, alwaysTrue, listener, slotIndex);
    }

    public IInputHandler<GasStack> getGasInputHandler(LongSupplier longSupplier, @Nullable IActionSource actionSource,
            RecipeError notEnoughError) {
        return new IInputHandler<GasStack>() {
            @Override
            public GasStack getInput() {
                return key != null ? key.getForm() == MekanismKey.GAS ? (GasStack) key.getStack() : GasStack.EMPTY
                        : GasStack.EMPTY;
            }

            @Override
            public GasStack getRecipeInput(InputIngredient<GasStack> recipeIngredient) {
                return recipeIngredient.getMatchingInstance(getInput());
            }

            @Override
            public void use(GasStack recipeInput, int operations) {
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    return;
                }
                storage.extract(key, recipeInput.getAmount() * operations, Actionable.MODULATE, actionSource);
            }

            @Override
            public void calculateOperationsCanSupport(OperationTracker tracker, GasStack recipeInput,
                    int usageMultiplier) {
                if (!recipeInput.isTypeEqual(getInput())) {
                    tracker.mismatchedRecipe();
                    return;
                }
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    tracker.updateOperations(0);
                    tracker.addError(notEnoughError);
                    return;
                }
                long usaAble = storage.getAvailableStacks().get(key) - longSupplier.getAsLong();
                if (usaAble < 1) {
                    tracker.updateOperations(0);
                    tracker.addError(notEnoughError);
                } else {
                    tracker.updateOperations(
                            (int) Math.min(0x7fffffff, usaAble / (recipeInput.getAmount() * usageMultiplier)));
                }
            }
        };
    }

    public IInputHandler<InfusionStack> getInfusionInputHandler(LongSupplier longSupplier,
            @Nullable IActionSource actionSource,
            RecipeError notEnoughError) {
        return new IInputHandler<InfusionStack>() {

            @Override
            public InfusionStack getInput() {
                return key != null
                        ? key.getForm() == MekanismKey.INFUSION ? (InfusionStack) key.getStack() : InfusionStack.EMPTY
                        : InfusionStack.EMPTY;
            }

            @Override
            public InfusionStack getRecipeInput(InputIngredient<InfusionStack> recipeIngredient) {
                return recipeIngredient.getMatchingInstance(getInput());
            }

            @Override
            public void use(InfusionStack recipeInput, int operations) {
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    return;
                }
                storage.extract(key, recipeInput.getAmount() * operations, Actionable.MODULATE, actionSource);
            }

            @Override
            public void calculateOperationsCanSupport(OperationTracker tracker, InfusionStack recipeInput,
                    int usageMultiplier) {
                if (!recipeInput.isTypeEqual(getInput())) {
                    tracker.mismatchedRecipe();
                    return;
                }
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    tracker.updateOperations(0);
                    tracker.addError(notEnoughError);
                    return;
                }
                long usaAble = storage.getAvailableStacks().get(key) - longSupplier.getAsLong();
                if (usaAble < 1) {
                    tracker.updateOperations(0);
                    tracker.addError(notEnoughError);
                } else {
                    tracker.updateOperations(
                            (int) Math.min(0x7fffffff, usaAble / (recipeInput.getAmount() * usageMultiplier)));
                }
            }
        };
    }

    public IInputHandler<PigmentStack> getPigmentInputHandler(LongSupplier longSupplier,
            @Nullable IActionSource actionSource,
            RecipeError notEnoughError) {
        return new IInputHandler<PigmentStack>() {

            @Override
            public PigmentStack getInput() {
                return key != null
                        ? key.getForm() == MekanismKey.PIGMENT ? (PigmentStack) key.getStack() : PigmentStack.EMPTY
                        : PigmentStack.EMPTY;
            }

            @Override
            public PigmentStack getRecipeInput(InputIngredient<PigmentStack> recipeIngredient) {
                return recipeIngredient.getMatchingInstance(getInput());
            }

            @Override
            public void use(PigmentStack recipeInput, int operations) {
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    return;
                }
                storage.extract(key, recipeInput.getAmount() * operations, Actionable.MODULATE, actionSource);
            }

            @Override
            public void calculateOperationsCanSupport(OperationTracker tracker, PigmentStack recipeInput,
                    int usageMultiplier) {
                if (!recipeInput.isTypeEqual(getInput())) {
                    tracker.mismatchedRecipe();
                    return;
                }
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    tracker.updateOperations(0);
                    tracker.addError(notEnoughError);
                    return;
                }
                long usaAble = storage.getAvailableStacks().get(key) - longSupplier.getAsLong();
                if (usaAble < 1) {
                    tracker.updateOperations(0);
                    tracker.addError(notEnoughError);
                } else {
                    tracker.updateOperations(
                            (int) Math.min(0x7fffffff, usaAble / (recipeInput.getAmount() * usageMultiplier)));
                }
            }
        };
    }

    public IInputHandler<SlurryStack> getSlurryInputHandler(LongSupplier longSupplier,
            @Nullable IActionSource actionSource,
            RecipeError notEnoughError) {
        return new IInputHandler<SlurryStack>() {

            @Override
            public SlurryStack getInput() {
                return key != null
                        ? key.getForm() == MekanismKey.SLURRY ? (SlurryStack) key.getStack() : SlurryStack.EMPTY
                        : SlurryStack.EMPTY;
            }

            @Override
            public SlurryStack getRecipeInput(InputIngredient<SlurryStack> recipeIngredient) {
                return recipeIngredient.getMatchingInstance(getInput());
            }

            @Override
            public void use(SlurryStack recipeInput, int operations) {
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    return;
                }
                storage.extract(key, recipeInput.getAmount() * operations, Actionable.MODULATE, actionSource);
            }

            @Override
            public void calculateOperationsCanSupport(OperationTracker tracker, SlurryStack recipeInput,
                    int usageMultiplier) {
                if (!recipeInput.isTypeEqual(getInput())) {
                    tracker.mismatchedRecipe();
                    return;
                }
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    tracker.updateOperations(0);
                    tracker.addError(notEnoughError);
                    return;
                }
                long usaAble = storage.getAvailableStacks().get(key) - longSupplier.getAsLong();
                if (usaAble < 1) {
                    tracker.updateOperations(0);
                    tracker.addError(notEnoughError);
                } else {
                    tracker.updateOperations(
                            (int) Math.min(0x7fffffff, usaAble / (recipeInput.getAmount() * usageMultiplier)));
                }
            }
        };
    }

    public IBoxedChemicalInputHandler getBoxedInputHandler(LongSupplier longSupplier,
            @Nullable IActionSource actionSource,
            RecipeError notEnoughError) {
        return new IBoxedChemicalInputHandler() {

            @Override
            public BoxedChemicalStack getInput() {
                return key != null ? BoxedChemicalStack.box(key.getStack()) : BoxedChemicalStack.EMPTY;
            }

            @Override
            public BoxedChemicalStack getRecipeInput(ChemicalStackIngredient<?, ?> recipeIngredient) {
                BoxedChemicalStack boxedStack = getInput();
                if (boxedStack.isEmpty()) {
                    return BoxedChemicalStack.EMPTY;
                }
                if (recipeIngredient instanceof GasStackIngredient ingredient
                        && boxedStack.getChemicalStack() instanceof GasStack stack) {
                    return BoxedChemicalStack.box(ingredient.getMatchingInstance(stack));
                }
                if (recipeIngredient instanceof InfusionStackIngredient ingredient
                        && boxedStack.getChemicalStack() instanceof InfusionStack stack) {
                    return BoxedChemicalStack.box(ingredient.getMatchingInstance(stack));
                }
                if (recipeIngredient instanceof PigmentStackIngredient ingredient
                        && boxedStack.getChemicalStack() instanceof PigmentStack stack) {
                    return BoxedChemicalStack.box(ingredient.getMatchingInstance(stack));
                }
                if (recipeIngredient instanceof SlurryStackIngredient ingredient
                        && boxedStack.getChemicalStack() instanceof SlurryStack stack) {
                    return BoxedChemicalStack.box(ingredient.getMatchingInstance(stack));
                }
                return BoxedChemicalStack.EMPTY;
            }

            @Override
            public void use(BoxedChemicalStack recipeInput, long operations) {
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    return;
                }
                storage.extract(key, recipeInput.getChemicalStack().getAmount() * operations, Actionable.MODULATE,
                        actionSource);
            }

            @Override
            public void calculateOperationsCanSupport(OperationTracker tracker, BoxedChemicalStack recipeInput,
                    long usageMultiplier) {
                if (!recipeInput.getType().equals(getInput().getType())) {
                    tracker.mismatchedRecipe();
                    return;
                }
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    tracker.updateOperations(0);
                    tracker.addError(notEnoughError);
                    return;
                }
                long usaAble = storage.getAvailableStacks().get(key) - longSupplier.getAsLong();
                if (usaAble < 1) {
                    tracker.updateOperations(0);
                    tracker.addError(notEnoughError);
                } else {
                    tracker.updateOperations((int) Math.min(0x7fffffff,
                            usaAble / (recipeInput.getChemicalStack().getAmount() * usageMultiplier)));
                }
            }
        };
    }

    public IOutputHandler<GasStack> getGasOutputHandler(LongSupplier longSupplier, @Nullable IActionSource actionSource,
            RecipeError notEnuoghSpaceError) {
        return new IOutputHandler<GasStack>() {

            @Override
            public void handleOutput(GasStack toOutput, int operations) {
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    return;
                }
                storage.insert(key, 1l * toOutput.getAmount() * operations, Actionable.MODULATE, actionSource);
            }

            @Override
            public void calculateOperationsCanSupport(OperationTracker tracker, GasStack toOutput) {
                MekanismKey outputKey = MekanismKey.of(toOutput);
                if (outputKey.equals(key)) {
                    setKey(outputKey);
                }
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    tracker.updateOperations(0);
                    tracker.addError(notEnuoghSpaceError);
                    return;
                }
                long outputAble = storage.insert(key, longSupplier.getAsLong() - getAmount(), Actionable.SIMULATE,
                        actionSource);
                if (outputAble < 1) {
                    tracker.updateOperations(0);
                    tracker.addError(notEnuoghSpaceError);
                } else {
                    tracker.updateOperations((int) Math.min(0x7fffffff, outputAble / toOutput.getAmount()));
                }
            }
        };
    }

    public IOutputHandler<InfusionStack> getInfusionOutputHandler(LongSupplier longSupplier,
            @Nullable IActionSource actionSource,
            RecipeError notEnuoghSpaceError) {
        return new IOutputHandler<InfusionStack>() {

            @Override
            public void handleOutput(InfusionStack toOutput, int operations) {
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    return;
                }
                storage.insert(key, 1l * toOutput.getAmount() * operations, Actionable.MODULATE, actionSource);
            }

            @Override
            public void calculateOperationsCanSupport(OperationTracker tracker, InfusionStack toOutput) {
                MekanismKey outputKey = MekanismKey.of(toOutput);
                if (outputKey.equals(key)) {
                    setKey(outputKey);
                }
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    tracker.updateOperations(0);
                    tracker.addError(notEnuoghSpaceError);
                    return;
                }
                long outputAble = storage.insert(key, longSupplier.getAsLong() - getAmount(), Actionable.SIMULATE,
                        actionSource);
                if (outputAble < 1) {
                    tracker.updateOperations(0);
                    tracker.addError(notEnuoghSpaceError);
                } else {
                    tracker.updateOperations((int) Math.min(0x7fffffff, outputAble / toOutput.getAmount()));
                }
            }
        };
    }

    public IOutputHandler<PigmentStack> getPigmentOutputHandler(LongSupplier longSupplier,
            @Nullable IActionSource actionSource,
            RecipeError notEnuoghSpaceError) {
        return new IOutputHandler<PigmentStack>() {

            @Override
            public void handleOutput(PigmentStack toOutput, int operations) {
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    return;
                }
                storage.insert(key, 1l * toOutput.getAmount() * operations, Actionable.MODULATE, actionSource);
            }

            @Override
            public void calculateOperationsCanSupport(OperationTracker tracker, PigmentStack toOutput) {
                MekanismKey outputKey = MekanismKey.of(toOutput);
                if (outputKey.equals(key)) {
                    setKey(outputKey);
                }
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    tracker.updateOperations(0);
                    tracker.addError(notEnuoghSpaceError);
                    return;
                }
                long outputAble = storage.insert(key, longSupplier.getAsLong() - getAmount(), Actionable.SIMULATE,
                        actionSource);
                if (outputAble < 1) {
                    tracker.updateOperations(0);
                    tracker.addError(notEnuoghSpaceError);
                } else {
                    tracker.updateOperations((int) Math.min(0x7fffffff, outputAble / toOutput.getAmount()));
                }
            }
        };
    }

    public IOutputHandler<SlurryStack> getSlurryOutputHandler(LongSupplier longSupplier,
            @Nullable IActionSource actionSource,
            RecipeError notEnuoghSpaceError) {
        return new IOutputHandler<SlurryStack>() {

            @Override
            public void handleOutput(SlurryStack toOutput, int operations) {
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    return;
                }
                storage.insert(key, 1l * toOutput.getAmount() * operations, Actionable.MODULATE, actionSource);
            }

            @Override
            public void calculateOperationsCanSupport(OperationTracker tracker, SlurryStack toOutput) {
                MekanismKey outputKey = MekanismKey.of(toOutput);
                if (outputKey.equals(key)) {
                    setKey(outputKey);
                }
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    tracker.updateOperations(0);
                    tracker.addError(notEnuoghSpaceError);
                    return;
                }
                long outputAble = storage.insert(key, longSupplier.getAsLong() - getAmount(), Actionable.SIMULATE,
                        actionSource);
                if (outputAble < 1) {
                    tracker.updateOperations(0);
                    tracker.addError(notEnuoghSpaceError);
                } else {
                    tracker.updateOperations((int) Math.min(0x7fffffff, outputAble / toOutput.getAmount()));
                }
            }
        };
    }

    public IOutputHandler<BoxedChemicalStack> getBoxedOutputHandler(LongSupplier longSupplier,
            @Nullable IActionSource actionSource,
            RecipeError notEnuoghSpaceError) {
        return new IOutputHandler<BoxedChemicalStack>() {

            @Override
            public void handleOutput(BoxedChemicalStack toOutput, int operations) {
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    return;
                }
                storage.insert(key, 1l * toOutput.getChemicalStack().getAmount() * operations, Actionable.MODULATE,
                        actionSource);
            }

            @Override
            public void calculateOperationsCanSupport(OperationTracker tracker, BoxedChemicalStack toOutput) {
                MekanismKey outputKey = MekanismKey.of(toOutput.getChemicalStack());
                if (outputKey.equals(key)) {
                    setKey(outputKey);
                }
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    tracker.updateOperations(0);
                    tracker.addError(notEnuoghSpaceError);
                    return;
                }
                long outputAble = storage.insert(key, longSupplier.getAsLong() - getAmount(), Actionable.SIMULATE,
                        actionSource);
                if (outputAble < 1) {
                    tracker.updateOperations(0);
                    tracker.addError(notEnuoghSpaceError);
                } else {
                    tracker.updateOperations(
                            (int) Math.min(0x7fffffff, outputAble / toOutput.getChemicalStack().getAmount()));
                }
            }
        };
    }

    @Override
    public void trackContainer(MekanismContainer container) {
        container.track(SyncableStringList.create(() -> {
            if (key != null) {
                ResourceLocation rl = key.getStack().getTypeRegistryName();
                return List.of(key.getForm() + "", rl.getNamespace(), rl.getPath());
            }
            return Collections.emptyList();
        }, list -> {
            if (list.isEmpty()) {
                setKey(null);
            } else {
                byte form = Byte.parseByte(list.get(0));
                ResourceLocation rl = new ResourceLocation(list.get(1), list.get(2));
                switch (form) {
                    case MekanismKey.GAS:
                        setKey(MekanismKey.of(MekanismAPI.gasRegistry().getValue(rl).getStack(1)));
                        break;
                    case MekanismKey.INFUSION:
                        setKey(MekanismKey.of(MekanismAPI.infuseTypeRegistry().getValue(rl).getStack(1)));
                        break;
                    case MekanismKey.PIGMENT:
                        setKey(MekanismKey.of(MekanismAPI.pigmentRegistry().getValue(rl).getStack(1)));
                        break;
                    case MekanismKey.SLURRY:
                        setKey(MekanismKey.of(MekanismAPI.slurryRegistry().getValue(rl).getStack(1)));
                        break;
                    default:
                        break;
                }
            }
        }));
    }
}
