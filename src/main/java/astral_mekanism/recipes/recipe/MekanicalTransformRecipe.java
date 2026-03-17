package astral_mekanism.recipes.recipe;

import java.util.Collections;
import java.util.List;

import astral_mekanism.recipes.output.ItemFluidOutput;
import astral_mekanism.util.AMEInterface.PentaPredicate;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public abstract class MekanicalTransformRecipe extends MekanismRecipe
        implements PentaPredicate<ItemStack, ItemStack, ItemStack, FluidStack, FluidStack> {

    private final ItemStackIngredient inputItemA;
    private final ItemStackIngredient inputItemB;
    private final ItemStackIngredient inputItemC;
    private final FluidStackIngredient inputFluidA;
    private final FluidStackIngredient inputFluidB;
    private final ItemFluidOutput output;

    private final boolean itemAIsCatalyst;
    private final boolean itemBIsCatalyst;
    private final boolean itemCIsCatalyst;
    private final boolean fluidAIsCatalyst;
    private final boolean fluidBIsCatalyst;

    protected MekanicalTransformRecipe(ResourceLocation id,
            ItemStackIngredient inputItemA,
            ItemStackIngredient inputItemB,
            ItemStackIngredient inputItemC,
            FluidStackIngredient inputFluidA,
            FluidStackIngredient inputFluidB,
            ItemFluidOutput output,
            boolean itemAIsCatalyst,
            boolean itemBIsCatalyst,
            boolean itemCIsCatalyst,
            boolean fluidAIsCatalyst,
            boolean fluidBIsCatalyst) {
        super(id);
        this.inputItemA = inputItemA;
        this.inputItemB = inputItemB;
        this.inputItemC = inputItemC;
        this.inputFluidA = inputFluidA;
        this.inputFluidB = inputFluidB;
        this.output = output;
        this.itemAIsCatalyst = itemAIsCatalyst;
        this.itemBIsCatalyst = itemBIsCatalyst;
        this.itemCIsCatalyst = itemCIsCatalyst;
        this.fluidAIsCatalyst = fluidAIsCatalyst;
        this.fluidBIsCatalyst = fluidBIsCatalyst;
    }

    public ItemFluidOutput getOutput() {
        return output.copy();
    }

    @Override
    public boolean test(ItemStack ia, ItemStack ib, ItemStack ic, FluidStack fa, FluidStack fb) {
        return inputItemA.test(ia) && inputItemB.test(ib) && inputItemC.test(ic)
                && inputFluidA.test(fa) && inputFluidB.test(fb);
    }

    public boolean anotherTest(ItemStack ia, ItemStack ib, ItemStack ic, FluidStack fa, FluidStack fb) {
        return (ia.isEmpty() || inputItemA.test(ia))
                && (ib.isEmpty() || inputItemB.test(ib))
                && (ic.isEmpty() || inputItemC.test(ic))
                && (fa.isEmpty() || inputFluidA.test(fa))
                && (fb.isEmpty() || inputFluidB.test(fb));
    }

    @Override
    public boolean isIncomplete() {
        return inputItemA.hasNoMatchingInstances() ||
                inputItemB.hasNoMatchingInstances() ||
                inputItemC.hasNoMatchingInstances() ||
                inputFluidA.hasNoMatchingInstances() ||
                inputFluidB.hasNoMatchingInstances();
    }

    @Override
    public void logMissingTags() {
        inputItemA.logMissingTags();
        inputItemB.logMissingTags();
        inputItemC.logMissingTags();
        inputFluidA.logMissingTags();
        inputFluidB.logMissingTags();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        inputItemA.write(buffer);
        inputItemB.write(buffer);
        inputItemC.write(buffer);
        inputFluidA.write(buffer);
        inputFluidB.write(buffer);
        output.write(buffer);
        buffer.writeBoolean(itemAIsCatalyst);
        buffer.writeBoolean(itemBIsCatalyst);
        buffer.writeBoolean(itemCIsCatalyst);
        buffer.writeBoolean(fluidAIsCatalyst);
        buffer.writeBoolean(fluidBIsCatalyst);
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

    public FluidStackIngredient getInputFluidA() {
        return inputFluidA;
    }

    public FluidStackIngredient getInputFluidB() {
        return inputFluidB;
    }

    public boolean isItemACatalyst() {
        return itemAIsCatalyst;
    }

    public boolean isItemBCatalyst() {
        return itemBIsCatalyst;
    }

    public boolean isItemCCatalyst() {
        return itemCIsCatalyst;
    }

    public boolean isFluidACatalyst() {
        return fluidAIsCatalyst;
    }

    public boolean isFluidBCatalyst() {
        return fluidBIsCatalyst;
    }

    public List<ItemFluidOutput> getOutputDefinition() {
        return Collections.singletonList(output);
    }

}
