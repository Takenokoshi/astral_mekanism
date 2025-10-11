package astral_mekanism.recipes.recipe;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

import org.jetbrains.annotations.NotNull;

import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.MekanismRecipe;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public abstract class GreenHouseRecipe extends MekanismRecipe implements BiPredicate<@NotNull ItemStack, @NotNull FluidStack> {

    protected GreenHouseRecipe(ResourceLocation id, ItemStackIngredient inputItem, FluidStackIngredient inputFluid,
            FloatingLong energyRequired, int duration, ItemStack outputItemA, ItemStack outputItemB) {
        super(id);
        this.inputItem = Objects.requireNonNull(inputItem, "");
        this.inputFluid = Objects.requireNonNull(inputFluid, "");
        this.energyRequired = Objects.requireNonNull(energyRequired, "").copyAsConst();
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be positive.");
        }
        this.duration = Objects.requireNonNull(duration, "");
        this.outputItemA = Objects.requireNonNull(outputItemA.copy(), "");
        this.outputItemB = Objects.requireNonNull(outputItemB.copy(), "");
    }

    private final ItemStackIngredient inputItem;
    private final FluidStackIngredient inputFluid;
    private final FloatingLong energyRequired;
    private final int duration;
    private final ItemStack outputItemA;
    private final ItemStack outputItemB;

    public record GreenHouseRecipeOutput(@NotNull ItemStack itemA, @NotNull ItemStack itemB) {
        public GreenHouseRecipeOutput {
        };
    }

    public GreenHouseRecipeOutput getOutput(ItemStack item,FluidStack fluid){
        return new GreenHouseRecipeOutput(this.outputItemA.copy(), this.outputItemB.copy());
    }

    @Override
    public boolean test(@NotNull ItemStack t, @NotNull FluidStack u) {
        return this.inputItem.test(t) && this.inputFluid.test(u);
    }

    @Override
    public boolean isIncomplete() {
        return inputItem.hasNoMatchingInstances() || inputFluid.hasNoMatchingInstances();
    }

    @Override
    public void logMissingTags() {
        inputItem.logMissingTags();
        inputFluid.logMissingTags();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        inputItem.write(buffer);
        inputFluid.write(buffer);
        energyRequired.writeToBuffer(buffer);
        buffer.writeVarInt(duration);
        buffer.writeItem(outputItemA);
        buffer.writeItem(outputItemB);
    }

    public ItemStackIngredient getInputItem(){
        return inputItem;
    }
    public FluidStackIngredient getInputFluid(){
        return inputFluid;
    }

    public int getDuration(){
        return duration;
    }

    public FloatingLong getEnergyRequired(){
        return energyRequired;
    }

    public List<GreenHouseRecipeOutput> getOutputDefinition() {
        return Collections.singletonList(new GreenHouseRecipeOutput(outputItemA, outputItemB));
    }

}
