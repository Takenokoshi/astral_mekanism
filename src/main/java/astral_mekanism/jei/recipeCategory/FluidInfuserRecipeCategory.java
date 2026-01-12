package astral_mekanism.jei.recipeCategory;

import astral_mekanism.recipes.recipe.FluidFluidToFluidRecipe;
import astral_mekanism.registries.AstralMekanismMachines;
import mekanism.client.gui.element.bar.GuiHorizontalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.progress.GuiProgress;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.tile.component.config.DataType;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraftforge.fluids.FluidStack;

public class FluidInfuserRecipeCategory extends BaseRecipeCategory<FluidFluidToFluidRecipe> {

	private final GuiGauge<FluidStack> inputGaugeA;
	private final GuiGauge<FluidStack> inputGaugeB;
	private final GuiGauge<FluidStack> outputGauge;
	protected final GuiProgress rightArrow;
	protected final GuiProgress leftArrow;

	public FluidInfuserRecipeCategory(IGuiHelper helper, MekanismJEIRecipeType<FluidFluidToFluidRecipe> recipeType) {
		super(helper, recipeType, AstralMekanismMachines.FLUID_INFUSER, 3, 10, 170, 80);
		this.inputGaugeA = this.addElement(GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT_1), this, 25,
				13));
		this.inputGaugeB = this.addElement(GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT_2), this, 133,
				13));
		this.outputGauge = this.addElement(GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.OUTPUT), this, 79,
				4));
        addSlot(SlotType.INPUT, 6, 56).with(SlotOverlay.MINUS);
        addSlot(SlotType.INPUT_2, 154, 56).with(SlotOverlay.MINUS);
        addSlot(SlotType.OUTPUT, 80, 65).with(SlotOverlay.PLUS);
        addSlot(SlotType.POWER, 154, 14).with(SlotOverlay.POWER);
        rightArrow = addConstantProgress(ProgressType.SMALL_RIGHT, 47, 39);
        leftArrow = addConstantProgress(ProgressType.SMALL_LEFT, 101, 39);
        addElement(new GuiHorizontalPowerBar(this, FULL_BAR, 115, 75));
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, FluidFluidToFluidRecipe recipe, IFocusGroup focusGroup) {
		initFluid(builder, RecipeIngredientRole.INPUT, inputGaugeA, recipe.getInputA().getRepresentations());
		initFluid(builder, RecipeIngredientRole.INPUT, inputGaugeB, recipe.getInputB().getRepresentations());
		initFluid(builder, RecipeIngredientRole.OUTPUT, outputGauge, recipe.getOutputDefinition());
	}

}
