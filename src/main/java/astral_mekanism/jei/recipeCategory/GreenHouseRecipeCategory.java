package astral_mekanism.jei.recipeCategory;

import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.misc.NotNull;

import astral_mekanism.recipes.recipe.GreenHouseRecipe;
import astral_mekanism.registries.AstralMekanismMachines;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.tile.component.config.DataType;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;

public class GreenHouseRecipeCategory extends BaseRecipeCategory<GreenHouseRecipe> {

	private final GuiGauge<?> inputFluid;
	private final GuiSlot inputItem;
	private final GuiSlot outputItemA;
	private final GuiSlot outputItemB;

	public GreenHouseRecipeCategory(IGuiHelper helper, MekanismJEIRecipeType<GreenHouseRecipe> recipeType) {
		super(helper, recipeType, AstralMekanismMachines.GREENHOUSE.getBlockRO(), 3, 10, 170, 60);
		addSlot(SlotType.POWER, 141, 17).with(SlotOverlay.POWER);
		this.inputItem = this.addSlot(SlotType.INPUT, 54, 35);
		this.outputItemA = this.addSlot(SlotType.OUTPUT, 116, 35);
		this.outputItemB = this.addSlot(SlotType.OUTPUT, 116, 55);
		this.inputFluid = this
				.addElement(GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), this, 5,
						10));
		this.addElement(new GuiVerticalPowerBar(this, FULL_BAR, 164, 15));
		this.addSimpleProgress(ProgressType.RIGHT, 77, 38);
	}

	@Override
	public void setRecipe(@NotNull IRecipeLayoutBuilder builder, GreenHouseRecipe recipe,
			@NotNull IFocusGroup focusGroup) {
		this.initItem(builder, RecipeIngredientRole.INPUT, this.inputItem,
				recipe.getInputItem().getRepresentations());
		this.initFluid(builder, RecipeIngredientRole.INPUT, this.inputFluid,
				recipe.getInputFluid().getRepresentations());
		List<ItemStack> itemOutputsA = new ArrayList<ItemStack>();
		List<ItemStack> itemOutputsB = new ArrayList<ItemStack>();
		for (GreenHouseRecipe.GreenHouseRecipeOutput output : recipe.getOutputDefinition()) {
			itemOutputsA.add(output.itemA());
			itemOutputsB.add(output.itemB());
		}
		this.initItem(builder, RecipeIngredientRole.OUTPUT, outputItemA, itemOutputsA);
		this.initItem(builder, RecipeIngredientRole.OUTPUT, outputItemB, itemOutputsB);
	}

}
