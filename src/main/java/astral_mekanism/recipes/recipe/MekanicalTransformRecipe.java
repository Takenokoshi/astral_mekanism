package astral_mekanism.recipes.recipe;

import java.util.Collections;
import java.util.List;

import astral_mekanism.recipes.output.ItemFluidOutput;
import astral_mekanism.util.AMInterface.QuadPredicate;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public abstract class MekanicalTransformRecipe extends MekanismRecipe
        implements QuadPredicate<ItemStack, ItemStack, ItemStack, ItemStack> {

    private final ItemStackIngredient inputItemA;
    private final ItemStackIngredient inputItemB;
    private final ItemStackIngredient inputItemC;
    private final ItemStackIngredient inputItemD;
    private final ItemFluidOutput output;

    private final boolean itemAIsCatalyst;
    private final boolean itemBIsCatalyst;
    private final boolean itemCIsCatalyst;
    private final boolean itemDIsCatalyst;

    protected MekanicalTransformRecipe(ResourceLocation id,
            ItemStackIngredient inputItemA,
            ItemStackIngredient inputItemB,
            ItemStackIngredient inputItemC,
            ItemStackIngredient inputItemD,
            ItemFluidOutput output,
            boolean itemAIsCatalyst,
            boolean itemBIsCatalyst,
            boolean itemCIsCatalyst,
            boolean itemDIsCatalyst) {
        super(id);
        this.inputItemA = inputItemA;
        this.inputItemB = inputItemB;
        this.inputItemC = inputItemC;
        this.inputItemD = inputItemD;
        this.output = output;
        this.itemAIsCatalyst = itemAIsCatalyst;
        this.itemBIsCatalyst = itemBIsCatalyst;
        this.itemCIsCatalyst = itemCIsCatalyst;
        this.itemDIsCatalyst = itemDIsCatalyst;
    }

    public ItemFluidOutput getOutput() {
        return output.copy();
    }

    @Override
    public boolean test(ItemStack a, ItemStack b, ItemStack c, ItemStack d) {
        return inputItemA.test(a) && inputItemB.test(b) && inputItemC.test(c) && inputItemD.test(d);
    }

    @Override
    public boolean isIncomplete() {
        return inputItemA.hasNoMatchingInstances() ||
                inputItemB.hasNoMatchingInstances() ||
                inputItemC.hasNoMatchingInstances() ||
                inputItemD.hasNoMatchingInstances();
    }

    @Override
    public void logMissingTags() {
        inputItemA.logMissingTags();
        inputItemB.logMissingTags();
        inputItemC.logMissingTags();
        inputItemD.logMissingTags();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        inputItemA.write(buffer);
        inputItemB.write(buffer);
        inputItemC.write(buffer);
        inputItemD.write(buffer);
        output.write(buffer);
        buffer.writeBoolean(itemAIsCatalyst);
        buffer.writeBoolean(itemBIsCatalyst);
        buffer.writeBoolean(itemCIsCatalyst);
        buffer.writeBoolean(itemDIsCatalyst);
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

    public ItemStackIngredient getInputItemD() {
        return inputItemD;
    }

    public boolean isACatalyst(){
        return itemAIsCatalyst;
    }

    public boolean isBCatalyst(){
        return itemBIsCatalyst;
    }

    public boolean isCCatalyst(){
        return itemCIsCatalyst;
    }

    public boolean isDCatalyst(){
        return itemDIsCatalyst;
    }

    public List<ItemFluidOutput> getOutputDefinition() {
        return Collections.singletonList(output);
    }

}
