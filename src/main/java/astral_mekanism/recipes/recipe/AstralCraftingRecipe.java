package astral_mekanism.recipes.recipe;

import java.util.Collections;
import java.util.List;

import mekanism.api.chemical.gas.GasStack;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.fluids.FluidStack;

public abstract class AstralCraftingRecipe extends MekanismRecipe
        implements TriPredicate<ItemStack[], FluidStack, GasStack> {

    private final ItemStackIngredient[] inputItems;
    private final FluidStackIngredient inputFluid;
    private final GasStackIngredient inputGas;
    private final ItemStack output;

    protected AstralCraftingRecipe(ResourceLocation id, ItemStackIngredient[] inputItems,
            FluidStackIngredient inputFluid, GasStackIngredient inputGas, ItemStack output) {
        super(id);
        this.inputItems = new ItemStackIngredient[25];
        for (int i = 0; i < 25; i++) {
            this.inputItems[i] = inputItems[i % inputItems.length];
        }
        this.inputFluid = inputFluid;
        this.inputGas = inputGas;
        this.output = output;
    }

    public ItemStack getOutput(ItemStack[] t, FluidStack f, GasStack g) {
        return output.copy();
    }

    @Override
    public boolean test(ItemStack[] t, FluidStack f, GasStack g) {
        boolean result = inputFluid.test(f) && inputGas.test(g);
        int i = 0;
        while (result && i < 25) {
            result &= inputItems[i].test(t[i % t.length]);
            i++;
        }
        return result;
    }

    @Override
    public boolean isIncomplete() {
        boolean result = inputFluid.hasNoMatchingInstances() || inputGas.hasNoMatchingInstances();
        int i = 0;
        while (!result && i < 25) {
            result |= inputItems[i].hasNoMatchingInstances();
            i++;
        }
        return result;
    }

    @Override
    public void logMissingTags() {
        for (int i = 0; i < 25; i++) {
            inputItems[i].logMissingTags();
        }
        inputFluid.logMissingTags();
        inputGas.logMissingTags();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        for (int i = 0; i < 25; i++) {
            inputItems[i].write(buffer);
        }
        inputFluid.write(buffer);
        inputGas.write(buffer);
        buffer.writeItem(output);
    }

    public ItemStackIngredient[] getInputItems() {
        return inputItems;
    }

    public ItemStackIngredient getInputItem(int index) {
        return inputItems[index % 25];
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
