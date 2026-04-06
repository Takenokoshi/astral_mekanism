package astral_mekanism.jei.recipeCategory;

import astral_mekanism.recipes.recipe.GasInfusionToFluidRecipe;
import mekanism.api.providers.IItemProvider;
import mekanism.client.gui.element.bar.GuiBar;
import mekanism.client.gui.element.bar.GuiEmptyBar;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.MekanismJEI;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;

public class GasInfusionToFluidRecipeCategory extends BaseRecipeCategory<GasInfusionToFluidRecipe> {
    private final GuiGasGauge gasGauge;
    private final GuiBar<?> infusionBar;
    private final GuiFluidGauge outputGauge;

    public GasInfusionToFluidRecipeCategory(IGuiHelper helper,
            MekanismJEIRecipeType<GasInfusionToFluidRecipe> recipeType, IItemProvider provider) {
        super(helper, recipeType, provider, 5, 16, 166, 54);
        gasGauge = addElement(GuiGasGauge.getDummy(GaugeType.STANDARD, this, 50, 12));
        infusionBar = addElement(new GuiEmptyBar(this, 7, 15, 4, 52));
        outputGauge = addElement(GuiFluidGauge.getDummy(GaugeType.STANDARD, this, 108, 12));
        addSlot(SlotType.INPUT, 33, 53).with(SlotOverlay.MINUS);
        addSlot(SlotType.INPUT_2, 17, 35).with(SlotOverlay.MINUS);
        addSlot(SlotType.POWER, 143, 35).with(SlotOverlay.POWER);
        addElement(new GuiVerticalPowerBar(this, FULL_BAR, 164, 15));
        addSimpleProgress(ProgressType.RIGHT, 72, 47);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GasInfusionToFluidRecipe recipe, IFocusGroup focusGroup) {
        initChemical(builder, MekanismJEI.TYPE_GAS, RecipeIngredientRole.INPUT, gasGauge,
                recipe.getGasInput().getRepresentations());
        initChemical(builder, MekanismJEI.TYPE_INFUSION, RecipeIngredientRole.INPUT, infusionBar,
                recipe.getInfusionInput().getRepresentations());
        initFluid(builder, RecipeIngredientRole.OUTPUT, outputGauge, recipe.getOutputDefinition());
    }

}
