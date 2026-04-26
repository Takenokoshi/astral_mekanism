package astral_mekanism.item.recipecard;

import java.util.function.BiPredicate;

import appeng.api.stacks.AEFluidKey;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import appeng.api.storage.AEKeyFilter;
import appeng.util.ConfigInventory;
import me.ramidzkh.mekae2.ae2.MekanismKey;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.chemical.pigment.PigmentStack;
import mekanism.api.chemical.slurry.SlurryStack;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.fluids.FluidStack;

public abstract class RecipeConfigInventory<RECIPE extends Recipe<?>> extends ConfigInventory {

    protected final BiPredicate<RecipeConfigInventory<RECIPE>, GenericStack>[] biPredicates;

    protected RecipeConfigInventory(BiPredicate<RecipeConfigInventory<RECIPE>, GenericStack>[] biPredicates,
            int outputSize) {
        super(AEKeyFilter.none(), Mode.CONFIG_TYPES, biPredicates.length + outputSize, () -> {
        }, false);
        this.biPredicates = biPredicates;
    }

    public abstract RECIPE getRecipe();

    public abstract void recalculateOutput();

    @Override
    public void setStack(int slot, GenericStack stack) {
        if (slot < biPredicates.length) {
            if (stack == null) {
                super.setStack(slot, stack);
            } else if (biPredicates[slot].test(this, stack)) {
                super.setStack(slot, stack);
            }
            recalculateOutput();
        }
    }

    public ItemStack getAsItem(int slot) {
        if (getKey(slot) instanceof AEItemKey key) {
            ItemStack result = key.toStack(1);
            result.setCount(result.getMaxStackSize());
            return result;
        }
        return ItemStack.EMPTY;
    }

    public FluidStack getAsFluid(int slot) {
        if (getKey(slot) instanceof AEFluidKey key) {
            return key.toStack(0x7fffffff);
        }
        return FluidStack.EMPTY;
    }

    public ChemicalStack<?> getAsChemical(int slot) {
        if (getKey(slot) instanceof MekanismKey key) {
            return key.withAmount(Long.MAX_VALUE);
        }
        return GasStack.EMPTY;
    }

    public GasStack getAsGas(int slot) {
        if (getKey(slot) instanceof MekanismKey key) {
            return key.getForm() == MekanismKey.GAS ? (GasStack) key.withAmount(Long.MAX_VALUE) : GasStack.EMPTY;
        }
        return GasStack.EMPTY;
    }

    public InfusionStack getAsInfusion(int slot) {
        if (getKey(slot) instanceof MekanismKey key) {
            return key.getForm() == MekanismKey.INFUSION ? (InfusionStack) key.withAmount(Long.MAX_VALUE)
                    : InfusionStack.EMPTY;
        }
        return InfusionStack.EMPTY;
    }

    public PigmentStack getAsPigment(int slot) {
        if (getKey(slot) instanceof MekanismKey key) {
            return key.getForm() == MekanismKey.PIGMENT ? (PigmentStack) key.withAmount(Long.MAX_VALUE)
                    : PigmentStack.EMPTY;
        }
        return PigmentStack.EMPTY;
    }

    public SlurryStack getAsSlurry(int slot) {
        if (getKey(slot) instanceof MekanismKey key) {
            return key.getForm() == MekanismKey.SLURRY ? (SlurryStack) key.withAmount(Long.MAX_VALUE)
                    : SlurryStack.EMPTY;
        }
        return SlurryStack.EMPTY;
    }

}
