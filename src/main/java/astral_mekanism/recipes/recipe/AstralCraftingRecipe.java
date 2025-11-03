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

    // 25スロット固定
    private static final int SLOT_COUNT = 25;

    private final ItemStackIngredient[] inputItems;
    private final FluidStackIngredient inputFluid;
    private final GasStackIngredient inputGas;
    private final ItemStack output;

    protected AstralCraftingRecipe(
            ResourceLocation id,
            ItemStackIngredient[] inputItems,
            FluidStackIngredient inputFluid,
            GasStackIngredient inputGas,
            ItemStack output
    ) {
        super(id);

        // 25スロットに「ループしてコピー」
        this.inputItems = new ItemStackIngredient[SLOT_COUNT];
        for (int i = 0; i < SLOT_COUNT; i++) {
            this.inputItems[i] = inputItems[i % inputItems.length];
        }

        this.inputFluid = inputFluid;
        this.inputGas = inputGas;
        this.output = output;
    }

    // --- 実際の出力を返す ---
    public ItemStack getOutput(ItemStack[] t, FluidStack f, GasStack g) {
        return output.copy();
    }

    @Override
    public boolean test(ItemStack[] t, FluidStack f, GasStack g) {
        // null セーフにもできる (必要なら)
        if (t == null || f == null || g == null) return false;

        // まず流体とガスが一致しないなら即 false
        if (!inputFluid.test(f) || !inputGas.test(g)) {
            return false;
        }

        // 25スロット全判定
        // t.length が 25 未満でも % によりループする仕様を維持
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (!inputItems[i].test(t[i % t.length])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isIncomplete() {
        // Fluid or Gas が揃わなければ incomplete
        if (inputFluid.hasNoMatchingInstances() || inputGas.hasNoMatchingInstances()) {
            return true;
        }

        // Item が一つでも無ければ incomplete
        for (int i = 0; i < SLOT_COUNT; i++) {
            if (inputItems[i].hasNoMatchingInstances()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void logMissingTags() {
        for (int i = 0; i < SLOT_COUNT; i++) {
            inputItems[i].logMissingTags();
        }
        inputFluid.logMissingTags();
        inputGas.logMissingTags();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        for (int i = 0; i < SLOT_COUNT; i++) {
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
        return inputItems[index % SLOT_COUNT];
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
