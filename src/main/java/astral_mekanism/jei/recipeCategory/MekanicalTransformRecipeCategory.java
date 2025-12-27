package astral_mekanism.jei.recipeCategory;

import astral_mekanism.recipes.output.ItemFluidOutput;
import astral_mekanism.recipes.recipe.MekanicalTransformRecipe;
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

public class MekanicalTransformRecipeCategory extends BaseRecipeCategory<MekanicalTransformRecipe> {

    private final GuiSlot inputA;
    private final GuiSlot inputB;
    private final GuiSlot inputC;
    private final GuiSlot inputD;
    private final GuiSlot output;
    private final GuiGauge<FluidStack> outputGauge;

    public MekanicalTransformRecipeCategory(IGuiHelper helper,
            MekanismJEIRecipeType<MekanicalTransformRecipe> recipeType, IItemProvider provider) {
        super(helper, recipeType, provider, 28, 16, 144, 54);
        inputA = addSlot(SlotType.INPUT, 10, 35);
        inputB = addSlot(SlotType.INPUT, 28, 35);
        inputC = addSlot(SlotType.INPUT, 46, 35);
        inputD = addSlot(SlotType.INPUT, 64, 35);
        output = addSlot(SlotType.OUTPUT, 116, 35);
        this.outputGauge = this.addElement(GuiFluidGauge.getDummy(GaugeType.SMALL.with(DataType.OUTPUT),
                this, 133, 18));
        addSlot(SlotType.POWER, 155, 14);
        addSlot(SlotType.EXTRA, 134, 45);
        addElement(new GuiVerticalPowerBar(this, FULL_BAR, 164, 15));
        addSimpleProgress(ProgressType.BAR, 86, 38);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MekanicalTransformRecipe recipe, IFocusGroup focusGroup) {
        initItem(builder,
                recipe.isACatalyst() ? RecipeIngredientRole.CATALYST : RecipeIngredientRole.INPUT,
                inputA, recipe.getInputItemA().getRepresentations());
        initItem(builder,
                recipe.isBCatalyst() ? RecipeIngredientRole.CATALYST : RecipeIngredientRole.INPUT,
                inputB, recipe.getInputItemB().getRepresentations());
        initItem(builder,
                recipe.isCCatalyst() ? RecipeIngredientRole.CATALYST : RecipeIngredientRole.INPUT,
                inputC, recipe.getInputItemC().getRepresentations());
        initItem(builder,
                recipe.isDCatalyst() ? RecipeIngredientRole.CATALYST : RecipeIngredientRole.INPUT,
                inputD, recipe.getInputItemD().getRepresentations());
        initItem(builder, RecipeIngredientRole.OUTPUT, output,
                recipe.getOutputDefinition().stream().map(ItemFluidOutput::item).toList());
        initFluid(builder, RecipeIngredientRole.OUTPUT, outputGauge,
                recipe.getOutputDefinition().stream().map(ItemFluidOutput::fluid).toList());
    }

}
