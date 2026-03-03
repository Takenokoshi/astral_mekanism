package astral_mekanism.jei.recipeCategory;

import java.util.List;

import appeng.recipes.transform.TransformRecipe;
import astral_mekanism.AstralMekanismID;
import mekanism.api.providers.IItemProvider;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraftforge.fluids.FluidStack;

public class TransformRecipeCategory extends BaseGeneralRecipeCategory<TransformRecipe> {

    private final GuiSlot inputSlotA = addSlot(SlotType.INPUT, 46, 17);
    private final GuiSlot inputSlotB = addSlot(SlotType.INPUT, 46, 35);
    private final GuiSlot inputSlotC = addSlot(SlotType.INPUT, 46, 53);
    private final GuiSlot outputSlot = addSlot(SlotType.OUTPUT, 133, 35);
    private final GuiGauge<FluidStack> inputTankA = addElement(
            GuiFluidGauge.getDummy(GaugeType.STANDARD, this, 27, 10));
    private final GuiGauge<FluidStack> inputTankB = addElement(
            GuiFluidGauge.getDummy(GaugeType.STANDARD, this, 63, 10));
    private final GuiGauge<FluidStack> outputTank = addElement(
            GuiFluidGauge.getDummy(GaugeType.STANDARD, this, 150, 10));

    public TransformRecipeCategory(IGuiHelper helper, RecipeType<TransformRecipe> recipeType, IItemProvider provider) {
        super(helper, recipeType, provider, 28, 16, 180, 54);
        addSlot(SlotType.NORMAL, 10, 17).with(SlotOverlay.MINUS);
        addSlot(SlotType.NORMAL, 10, 53);
        addSlot(SlotType.NORMAL, 82, 17).with(SlotOverlay.MINUS);
        addSlot(SlotType.NORMAL, 82, 53);
        addSlot(SlotType.NORMAL, 169, 17).with(SlotOverlay.PLUS);
        addSlot(SlotType.NORMAL, 169, 53);
        addSlot(SlotType.POWER, 190, 4).with(SlotOverlay.POWER);
        addSimpleProgress(ProgressType.BAR, 100, 38);
        addElement(new GuiVerticalPowerBar(this, FULL_BAR, 200, 25));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TransformRecipe recipe, IFocusGroup focusGroup) {
        int size = recipe.ingredients.size();
        if (size > 3) {
            return;
        }
        initFluid(builder, RecipeIngredientRole.CATALYST, inputTankA,
                AstralMekanismID.transformFluidExtractor.apply(recipe).getRepresentations());
        initItem(builder, RecipeIngredientRole.INPUT, inputSlotA, List.of(recipe.ingredients.get(0).getItems()));
        if (size > 1) {
            initItem(builder, RecipeIngredientRole.INPUT, inputSlotB, List.of(recipe.ingredients.get(1).getItems()));
        }
        if (size > 2) {
            initItem(builder, RecipeIngredientRole.INPUT, inputSlotC, List.of(recipe.ingredients.get(2).getItems()));
        }
        initItem(builder, RecipeIngredientRole.OUTPUT, outputSlot, List.of(recipe.getResultItem()));
    }

}
