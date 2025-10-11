package astral_mekanism.jei.recipeCategory;

import astral_mekanism.registries.AstralMekanismMachines;
import mekanism.api.recipes.ItemStackToFluidRecipe;
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

public class MelterRecipeCategory extends BaseRecipeCategory<ItemStackToFluidRecipe> {

	private final GuiSlot inputItem;
	private final GuiGauge<?> outputFluid;

	public MelterRecipeCategory(IGuiHelper helper, MekanismJEIRecipeType<ItemStackToFluidRecipe> recipeType) {
		super(helper, recipeType, AstralMekanismMachines.MELTER, 3, 30, 170, 60);
		this.inputItem = this.addSlot(SlotType.INPUT, 54, 35);
		this.outputFluid = this.addElement(
				GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.OUTPUT), this, 116, 10));
		this.addSimpleProgress(ProgressType.RIGHT, 77, 38);
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, ItemStackToFluidRecipe recipe,
			IFocusGroup focusGroup) {
		this.initItem(builder, RecipeIngredientRole.INPUT, inputItem, recipe.getInput().getRepresentations());
		this.initFluid(builder, RecipeIngredientRole.OUTPUT, outputFluid, recipe.getOutputDefinition());
	}

}
