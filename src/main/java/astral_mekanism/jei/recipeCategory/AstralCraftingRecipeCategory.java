package astral_mekanism.jei.recipeCategory;

import org.jetbrains.annotations.NotNull;

import astral_mekanism.recipes.recipe.AstralCraftingRecipe;
import mekanism.api.providers.IBlockProvider;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.MekanismJEI;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.tile.component.config.DataType;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraftforge.fluids.FluidStack;

public class AstralCraftingRecipeCategory extends BaseRecipeCategory<AstralCraftingRecipe> {

    private final GuiSlot[] inputItems;
    private final GuiGauge<FluidStack> inputFluid;
    private final GuiGauge<?> inputGas;
    private final GuiSlot output;

    public AstralCraftingRecipeCategory(IGuiHelper helper, MekanismJEIRecipeType<AstralCraftingRecipe> recipeType,
            IBlockProvider mekanismBlock) {
        super(helper, recipeType, mekanismBlock, 3, 10, 212, 90);
        inputItems = new GuiSlot[25];
        for (int i = 0; i < 25; i++) {
            inputItems[i] = this.addSlot(SlotType.INPUT, 44 + (i % 5) * 18, 18 + (i / 5) * 18);
        }
        this.inputFluid = this.addElement(GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), this, 6, 30));
        this.inputGas = this.addElement(GuiGasGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), this, 24, 30));
        this.output = this.addSlot(SlotType.OUTPUT, 170, 54);
        this.addSlot(SlotType.INPUT_2, 8, 90).with(SlotOverlay.MINUS);
        this.addSlot(SlotType.INPUT_2, 26, 90).with(SlotOverlay.MINUS);
        this.addSlot(SlotType.POWER, 170, 18).with(SlotOverlay.POWER);
        this.addElement(new GuiVerticalPowerBar(this, FULL_BAR, 188, 16));
        this.addSimpleProgress(ProgressType.RIGHT, 134, 60);
    }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, AstralCraftingRecipe recipe,
            @NotNull IFocusGroup focusGroup) {
        for (int i = 0; i < 25; i++) {
            this.initItem(builder, RecipeIngredientRole.INPUT, inputItems[i],
                    recipe.getInputItem(i).getRepresentations());
        }
        this.initFluid(builder, RecipeIngredientRole.INPUT, inputFluid, recipe.getInputFluid().getRepresentations());
        this.initChemical(builder, MekanismJEI.TYPE_GAS, RecipeIngredientRole.INPUT, inputGas,
                recipe.getInputGas().getRepresentations());
        this.initItem(builder, RecipeIngredientRole.OUTPUT, output, recipe.getOutputDefinition());
    }

}
