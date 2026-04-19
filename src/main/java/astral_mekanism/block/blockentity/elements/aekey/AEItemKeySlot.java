package astral_mekanism.block.blockentity.elements.aekey;

import java.util.function.LongSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.MEStorage;
import astral_mekanism.longRecipe.LongOperationTracker;
import astral_mekanism.longRecipe.handler.ILongInputHandler;
import astral_mekanism.longRecipe.handler.ILongOutputHandler;
import mekanism.api.IContentsListener;
import mekanism.api.recipes.cache.CachedRecipe.OperationTracker.RecipeError;
import mekanism.api.recipes.ingredients.InputIngredient;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.inventory.container.sync.SyncableItemStack;
import net.minecraft.world.item.ItemStack;

public class AEItemKeySlot extends AEKeySlot<AEItemKey> {

    public AEItemKeySlot(Supplier<MEStorage> storageSupplier, Predicate<? super AEItemKey> keyPredicate,
            IContentsListener listener, int slotIndex) {
        super(storageSupplier, keyPredicate, listener, AEItemKey::fromTag, AEItemKey::toTag,
                k -> k instanceof AEItemKey ? (AEItemKey) k : null, slotIndex);
    }

    public AEItemKeySlot(Supplier<MEStorage> storageSupplier,
            IContentsListener listener, int slotIndex) {
        this(storageSupplier, alwaysTrue, listener, slotIndex);
    }

    public ILongInputHandler<ItemStack> getInputHandler(LongSupplier longSupplier, @Nullable IActionSource actionSource,
            RecipeError notEnoughError) {
        return new ILongInputHandler<ItemStack>() {
            @Override
            public ItemStack getInput() {
                return key != null ? key.toStack() : ItemStack.EMPTY;
            }

            @Override
            public ItemStack getRecipeInput(InputIngredient<ItemStack> recipeIngredient) {
                return recipeIngredient.getMatchingInstance(getInput());
            }

            @Override
            public void use(ItemStack recipeInput, long operations) {
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    return;
                }
                storage.extract(key, 1l * recipeInput.getCount() * operations, Actionable.MODULATE, actionSource);
            }

            @Override
            public void calculateOperationsCanSupport(LongOperationTracker tracker, ItemStack recipeInput,
                    int usageMultiplier) {
                if (!ItemStack.isSameItemSameTags(getInput(), recipeInput)) {
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
                    tracker.updateOperations(usaAble / (recipeInput.getCount() * usageMultiplier));
                }
            }
        };
    }

    public ILongOutputHandler<ItemStack> getOutputHandler(LongSupplier longSupplier,
            @Nullable IActionSource actionSource,
            RecipeError notEnuoghSpaceError) {
        return new ILongOutputHandler<ItemStack>() {
            @Override
            public void handleOutput(ItemStack toOutput, long operations) {
                MEStorage storage = storageSupplier.get();
                if (key == null || storage == null) {
                    return;
                }
                storage.insert(key, 1l * toOutput.getCount() * operations, Actionable.MODULATE, actionSource);
            }

            @Override
            public void calculateOperationsCanSupport(LongOperationTracker tracker, ItemStack toOutput) {
                AEItemKey outputKey = AEItemKey.of(toOutput);
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
                    tracker.updateOperations(outputAble / toOutput.getCount());
                }
            }
        };
    }

    @Override
    public void trackContainer(MekanismContainer container) {
        container.track(SyncableItemStack.create(() -> key != null ? key.getReadOnlyStack() : ItemStack.EMPTY,
                stack -> {
                    if (!stack.isEmpty()) {
                        setKey(AEItemKey.of(stack));
                    } else {
                        setKey(null);
                    }
                }));
    }
}
