package astral_mekanism.recipes.recipe;

import java.util.Collections;
import java.util.List;

import astral_mekanism.recipes.ingredient.ArrayItemStackIngredient;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.fluids.FluidStack;

public abstract class ExpandedCrafterRecipe extends MekanismRecipe
        implements TriPredicate<ItemStack[], FluidStack, GasStack> {

    private final ArrayItemStackIngredient inputItems;
    private final FluidStackIngredient inputFluid;
    private final GasStackIngredient inputGas;
    private final ItemStack output;

    protected ExpandedCrafterRecipe(
            ResourceLocation id,
            ArrayItemStackIngredient inputItems,
            FluidStackIngredient inputFluid,
            GasStackIngredient inputGas,
            ItemStack output) {
        super(id);
        this.inputItems = inputItems;
        this.inputFluid = inputFluid;
        this.inputGas = inputGas;
        this.output = output;
    }

    public ItemStack getOutput(ItemStack[] t, FluidStack f, GasStack g) {
        return output.copy();
    }

    @Override
    public boolean test(ItemStack[] t, FluidStack f, GasStack g) {
        return inputItems.test(t) && inputFluid.test(f) && inputGas.test(g);
    }

    @Override
    public boolean isIncomplete() {
        return inputItems.hasNoMatchingInstances() || inputFluid.hasNoMatchingInstances()
                || inputGas.hasNoMatchingInstances();
    }

    @Override
    public void logMissingTags() {
        inputItems.logMissingTags();
        inputFluid.logMissingTags();
        inputGas.logMissingTags();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        inputItems.write(buffer);
        inputFluid.write(buffer);
        inputGas.write(buffer);
        buffer.writeItem(output);
    }

    public ArrayItemStackIngredient getInputItems() {
        return inputItems;
    }

    public FluidStackIngredient getInputFluid() {
        return inputFluid;
    }

    public GasStackIngredient getInputGas() {
        return inputGas;
    }

    public List<ItemStack> getOutputDefinition() {
        return Collections.singletonList(output.copy());
    }
}
