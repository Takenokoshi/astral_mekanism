package astral_mekanism.jei.recipeCategory;

import astral_mekanism.recipes.recipe.GreenhouseRecipe;
import mekanism.api.providers.IItemProvider;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.tile.component.config.DataType;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraftforge.fluids.FluidStack;

public class GreenhouseRecipeCategory extends BaseRecipeCategory<GreenhouseRecipe> {

    private final GuiSlot inputSeed;
    private final GuiSlot farmland;
    private final GuiSlot outputMain;
    private final GuiSlot outputSeed;
    private final GuiSlot outputExtra;
    private final GuiGauge<FluidStack> fluidGauge;

    public GreenhouseRecipeCategory(IGuiHelper helper, MekanismJEIRecipeType<GreenhouseRecipe> recipeType,
            IItemProvider provider) {
        super(helper, recipeType, provider, 28, 16, 144, 54);
        inputSeed = addSlot(SlotType.INPUT, 64, 17);
        farmland = addSlot(SlotType.INPUT, 64, 53);
        outputMain = addSlot(SlotType.OUTPUT, 116, 17);
        outputSeed = addSlot(SlotType.OUTPUT, 116, 35);
        outputExtra = addSlot(SlotType.OUTPUT, 116, 53);
        fluidGauge = addElement(GuiFluidGauge.getDummy(GaugeType.SMALL.with(DataType.INPUT), this, 46, 17));
        addSlot(SlotType.POWER, 155, 14);
        addSlot(SlotType.EXTRA, 46, 45);
        addElement(new GuiVerticalPowerBar(this, FULL_BAR, 164, 15));
        addSimpleProgress(ProgressType.BAR, 86, 38);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GreenhouseRecipe recipe, IFocusGroup focusGroup) {
        initItem(builder, RecipeIngredientRole.CATALYST, inputSeed, recipe.getInputSeed().getRepresentations());
        initItem(builder, RecipeIngredientRole.CATALYST, farmland, recipe.getFarmland().getRepresentations());
        initItem(builder, RecipeIngredientRole.OUTPUT, outputMain,
                recipe.getOutputDefinition().stream().map(output -> output.itemA()).toList());
        initItem(builder, RecipeIngredientRole.OUTPUT, outputSeed,
                recipe.getOutputDefinition().stream().map(output -> output.itemB()).toList());
        initItem(builder, RecipeIngredientRole.OUTPUT, outputExtra,
                recipe.getOutputDefinition().stream().map(output -> output.itemC()).toList());
        initFluid(builder, RecipeIngredientRole.INPUT, fluidGauge, recipe.getInputFluid().getRepresentations());
    }

}
