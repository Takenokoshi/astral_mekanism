package astral_mekanism.recipes.recipe;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.recipes.output.DoubleItemOutput;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public abstract class ItemToItemItemRecipe extends MekanismRecipe implements Predicate<@NotNull ItemStack> {

    private final ItemStackIngredient inputItem;
    private final ItemStack outputItemA;
    private final ItemStack outputItemB;

    protected ItemToItemItemRecipe(ResourceLocation id, ItemStackIngredient inputItem, ItemStack outputA,
            ItemStack outputB) {
        super(id);
        this.inputItem = inputItem;
        this.outputItemA = outputA.copy();
        this.outputItemB = outputB.copy();
    }

    public DoubleItemOutput getOutput(ItemStack input){
        return new DoubleItemOutput(outputItemA.copy(), outputItemB.copy());
    }

    public ItemStackIngredient getInput(){
        return inputItem;
    }

    @Override
    public boolean test(ItemStack input) {
        return inputItem.test(input);
    }

    @Override
    public boolean isIncomplete() {
        return inputItem.hasNoMatchingInstances();
    }

    @Override
    public void logMissingTags() {
        inputItem.logMissingTags();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        inputItem.write(buffer);
        buffer.writeItem(outputItemA);
        buffer.writeItem(outputItemB);
    }

    public List<DoubleItemOutput> getOutputDefinition(){
        return Collections.singletonList(new DoubleItemOutput(outputItemA, outputItemB));
    }

}
