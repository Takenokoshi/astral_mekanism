package astral_mekanism.recipes.recipe;

import java.util.Collections;
import java.util.List;
import astral_mekanism.recipes.output.TripleItemOutput;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.fluids.FluidStack;

public abstract class GreenhouseRecipe extends MekanismRecipe
        implements TriPredicate<ItemStack, ItemStack, FluidStack> {

    protected final ItemStackIngredient inputSeed;
    protected final ItemStackIngredient farmland;
    protected final FluidStackIngredient inputFluid;
    protected final TripleItemOutput output;

    protected GreenhouseRecipe(ResourceLocation id, ItemStackIngredient inputSeed, ItemStackIngredient farmland,
            FluidStackIngredient inputFluid,
            TripleItemOutput output) {
        super(id);
        this.inputSeed = inputSeed;
        this.farmland = farmland;
        this.inputFluid = inputFluid;
        this.output = output;
    }

    public ItemStackIngredient getInputSeed(){
        return inputSeed;
    }

    public ItemStackIngredient getFarmland(){
        return farmland;
    }

    public FluidStackIngredient getInputFluid(){
        return inputFluid;
    }

    @Override
    public boolean test(ItemStack seed, ItemStack far, FluidStack fluid) {
        return inputSeed.test(seed) && farmland.test(far) && inputFluid.test(fluid);
    }

    public TripleItemOutput getOutput() {
        return output.copy();
    }

    @Override
    public boolean isIncomplete() {
        return inputSeed.hasNoMatchingInstances() || farmland.hasNoMatchingInstances()
                || inputFluid.hasNoMatchingInstances();
    }

    @Override
    public void logMissingTags() {
        inputSeed.logMissingTags();
        farmland.logMissingTags();
        inputFluid.logMissingTags();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        inputSeed.write(buffer);
        farmland.write(buffer);
        inputFluid.write(buffer);
        output.write(buffer);
    }

    public List<TripleItemOutput> getOutputDefinition() {
        return Collections.singletonList(output);
    }
}