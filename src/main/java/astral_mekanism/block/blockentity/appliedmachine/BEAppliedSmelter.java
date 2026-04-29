package astral_mekanism.block.blockentity.appliedmachine;

import org.jetbrains.annotations.NotNull;

import appeng.api.config.Actionable;
import appeng.api.networking.security.IActionSource;
import appeng.api.stacks.AEItemKey;
import appeng.api.storage.MEStorage;
import astral_mekanism.block.blockentity.appliedmachine.prefab.BEAppliedEnergizedMachine;
import astral_mekanism.block.blockentity.interf.applied.IAppliedSingleToSingleMachine;
import astral_mekanism.item.recipecard.ItemIngredientCardItem;
import astral_mekanism.registries.AMEInfuseTypes;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.IContentsListener;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.CommonWorldTickHandler;
import mekanism.common.capabilities.holder.slot.IInventorySlotHolder;
import mekanism.common.capabilities.holder.slot.InventorySlotHelper;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.slot.BasicInventorySlot;
import mekanism.common.util.MekanismUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.state.BlockState;

public class BEAppliedSmelter extends BEAppliedEnergizedMachine implements IAppliedSingleToSingleMachine {

    private AEItemKey inputKey;
    private AEItemKey outputKey;
    private long xpAmount;
    private long xpCap;
    public final MekanismKey xpKey;

    private BasicInventorySlot cardSlot;

    public BEAppliedSmelter(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state, MekanismConfig.usage.energizedSmelter.get()
                .divideToLong(MekanismConfig.general.forgeConversionRate.get()));
        xpKey = MekanismKey.of(AMEInfuseTypes.XP.getStack(1));
    }

    @NotNull
    @Override
    protected IInventorySlotHolder getInitialInventory(IContentsListener listener) {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        builder.addSlot(
                cardSlot = BasicInventorySlot.at(stack -> stack.getItem() instanceof ItemIngredientCardItem, () -> {
                    listener.onContentsChanged();
                    recalculateRecipeInfo();
                }, 64, 53));
        return builder.build();
    }

    private void removeRecipeInfo() {
        inputKey = null;
        outputKey = null;
        xpAmount = 0;
        xpCap = 0;
    }

    private void recalculateRecipeInfo() {
        if (cardSlot.isEmpty()) {
            removeRecipeInfo();
            return;
        }
        ItemStack is = cardSlot.getStack();
        if (is.getItem() instanceof ItemIngredientCardItem cardItem) {
            AEItemKey key = cardItem.getKey(is);
            if (key != null && hasLevel()) {
                ItemStack stack = key.toStack();
                level.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING)
                        .stream().filter(r -> r.getIngredients().get(0).test(stack)).findFirst()
                        .ifPresentOrElse(recipe -> {
                            inputKey = key;
                            outputKey = AEItemKey.of(recipe.getResultItem(level.registryAccess()));
                            xpAmount = (long) (recipe.getExperience() * 100);
                            if (xpAmount > 0) {
                                xpCap = Long.MAX_VALUE / xpAmount;
                            } else {
                                xpCap = Long.MAX_VALUE;
                            }
                        }, this::removeRecipeInfo);
                return;
            }
        }
        removeRecipeInfo();
        return;
    }

    public AEItemKey getInputKey() {
        return inputKey;
    }

    public AEItemKey getOutputKey() {
        return outputKey;
    }

    @Override
    protected void onUpdateServer() {
        super.onUpdateServer();
        MEStorage storage = getMeStorage();
        if (CommonWorldTickHandler.flushTagAndRecipeCaches) {
            recalculateRecipeInfo();
        }
        if (MekanismUtils.canFunction(this) && xpAmount > 0 && storage != null) {
            IActionSource source = IActionSource.ofMachine(this);
            long operations = getSupportableOperations(storage, source);
            operations = storage.extract(inputKey, operations, Actionable.SIMULATE, source);
            operations = storage.insert(outputKey, operations, Actionable.MODULATE, source);
            if (operations > 0) {
                storage.extract(inputKey, operations, Actionable.MODULATE, source);
                storage.insert(xpKey, operations < xpCap ? operations * xpAmount : Long.MAX_VALUE, Actionable.MODULATE,
                        source);
                consumeEnergy(storage, source, operations);
                setActive(true);
                return;
            }
        }
        setActive(false);
    }

}
