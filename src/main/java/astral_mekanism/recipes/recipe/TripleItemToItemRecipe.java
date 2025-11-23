package astral_mekanism.recipes.recipe;

import java.util.Collections;
import java.util.List;

import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.TriPredicate;

public abstract class TripleItemToItemRecipe extends MekanismRecipe
        implements TriPredicate<ItemStack, ItemStack, ItemStack> {

    private final ItemStackIngredient inputItemA;
    private final ItemStackIngredient inputItemB;
    private final ItemStackIngredient inputItemC;
    private final ItemStack output;

    protected TripleItemToItemRecipe(
            ResourceLocation id,
            ItemStackIngredient inputItemA,
            ItemStackIngredient inputItemB,
            ItemStackIngredient inputItemC,
            ItemStack output) {
        super(id);
        this.inputItemA = inputItemA;
        this.inputItemB = inputItemB;
        this.inputItemC = inputItemC;
        this.output = output;
    }

    public ItemStack getOutput(ItemStack a, ItemStack b, ItemStack c) {
        return output.copy();
    }

    @Override
    public boolean test(ItemStack a, ItemStack b, ItemStack c) {
        return inputItemA.test(a) && inputItemB.test(b) && inputItemC.test(c);
    }

    @Override
    public boolean isIncomplete() {
        return inputItemA.hasNoMatchingInstances() || inputItemB.hasNoMatchingInstances()
                || inputItemC.hasNoMatchingInstances();
    }

    @Override
    public void logMissingTags() {
        inputItemA.logMissingTags();
        inputItemB.logMissingTags();
        inputItemC.logMissingTags();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        inputItemA.write(buffer);
        inputItemB.write(buffer);
        inputItemC.write(buffer);
        buffer.writeItem(output);
    }

    public ItemStackIngredient getInputItemA() {
        return inputItemA;
    }

    public ItemStackIngredient getInputItemB() {
        return inputItemB;
    }

    public ItemStackIngredient getInputItemC() {
        return inputItemC;
    }

    public List<ItemStack> getOutputDefinition() {
        return Collections.singletonList(output);
    }

}
