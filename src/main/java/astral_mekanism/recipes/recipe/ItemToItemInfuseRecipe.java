package astral_mekanism.recipes.recipe;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import astral_mekanism.recipes.output.ItemInfuseOutput;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public abstract class ItemToItemInfuseRecipe extends MekanismRecipe implements Predicate<ItemStack> {

    protected final ItemStackIngredient input;
    protected final ItemInfuseOutput output;

    protected ItemToItemInfuseRecipe(ResourceLocation id, ItemStackIngredient input, ItemInfuseOutput output) {
        super(id);
        this.input = input;
        this.output = output;
    }

    public ItemStackIngredient getInput() {
        return input;
    }

    @Override
    public boolean test(ItemStack stack) {
        return input.test(stack);
    }

    public ItemInfuseOutput getOutput() {
        return output.copy();
    }

    @Override
    public boolean isIncomplete() {
        return input.hasNoMatchingInstances();
    }

    @Override
    public void logMissingTags() {
        input.logMissingTags();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        input.write(buffer);
        output.write(buffer);
    }

    public List<ItemInfuseOutput> getOutputDefinition() {
        return Collections.singletonList(output.copy());
    }

}
