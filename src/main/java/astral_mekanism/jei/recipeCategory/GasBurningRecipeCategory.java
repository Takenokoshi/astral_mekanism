package astral_mekanism.jei.recipeCategory;

import java.util.Collections;
import java.util.List;

import astral_mekanism.jei.jeirecipe.GasBurningJEIRecipe;
import mekanism.api.math.FloatingLong;
import mekanism.api.providers.IItemProvider;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiEnergyGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.MekanismJEI;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.util.text.EnergyDisplay;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class GasBurningRecipeCategory extends BaseRecipeCategory<GasBurningJEIRecipe> {

    private final GuiGasGauge gasGauge;
    private final GuiEnergyGauge gauge;

    private static final String INPUT = "input";

    public GasBurningRecipeCategory(IGuiHelper helper, MekanismJEIRecipeType<GasBurningJEIRecipe> recipeType,
            IItemProvider provider) {
        super(helper, recipeType, provider, 28, 16, 144, 54);
        this.gasGauge = addElement(GuiGasGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT), this, 25, 13));
        this.gauge = addElement(GuiEnergyGauge.getDummy(GaugeType.STANDARD, this, 133, 13));
        addConstantProgress(ProgressType.LARGE_RIGHT, 64, 39);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GasBurningJEIRecipe recipe, IFocusGroup focusGroup) {
        initChemical(builder, MekanismJEI.TYPE_GAS, RecipeIngredientRole.INPUT, gasGauge, List.of(recipe.gasStack()));
    }

    @Override
    protected void renderElements(GasBurningJEIRecipe recipe, IRecipeSlotsView recipeSlotView, GuiGraphics guiGraphics, int x, int y) {
        super.renderElements(recipe, recipeSlotView, guiGraphics, x, y);
        if (!recipe.value().isZero()) {
            gauge.renderContents(guiGraphics);
        }
    }

    @Override
    public List<Component> getTooltipStrings(GasBurningJEIRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (gauge.isMouseOver(mouseX, mouseY)) {
            FloatingLong energy = recipe.value();
            if (!energy.isZero()) {
                Component energyOutput = EnergyDisplay.of(energy).getTextComponent();
                if (Minecraft.getInstance().options.advancedItemTooltips || Screen.hasShiftDown()) {
                    return List.of(energyOutput);
                }
                return Collections.singletonList(energyOutput);
            }
        }
        return Collections.emptyList();
    }

}
