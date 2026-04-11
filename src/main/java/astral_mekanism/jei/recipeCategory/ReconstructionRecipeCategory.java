package astral_mekanism.jei.recipeCategory;

import java.util.Collections;
import java.util.List;

import astral_mekanism.recipes.recipe.ReconstructionRecipe;
import fr.iglee42.evolvedmekanism.client.bars.GuiCustomDynamicHorizontalRateBar;
import mekanism.api.providers.IItemProvider;
import mekanism.api.recipes.PressurizedReactionRecipe.PressurizedReactionRecipeOutput;
import mekanism.client.SpecialColors;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiEnergyGauge;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.MekanismJEI;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.lib.Color;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class ReconstructionRecipeCategory extends BaseRecipeCategory<ReconstructionRecipe> {

    private final GuiFluidGauge fluidGauge;
    private final GuiGasGauge inputGasGauge;
    private final GuiGasGauge outputGasGauge;
    private final GuiEnergyGauge energyGauge;
    private final GuiSlot inputSlot;
    private final GuiSlot outputSlot;
    private final GuiInnerScreen innerScreen;
    private final GuiCustomDynamicHorizontalRateBar bar;
    private Color chemicalColor = Color.WHITE;

    public ReconstructionRecipeCategory(IGuiHelper helper, MekanismJEIRecipeType<ReconstructionRecipe> recipeType,
            IItemProvider provider) {
        super(helper, recipeType, provider, 3, 12, 222, 74);
        fluidGauge = addElement(GuiFluidGauge.getDummy(GaugeType.STANDARD, this, 7, 17));
        inputGasGauge = addElement(GuiGasGauge.getDummy(GaugeType.STANDARD, this, 30, 17));
        outputGasGauge = addElement(GuiGasGauge.getDummy(GaugeType.STANDARD, this, 174, 17));
        energyGauge = addElement(GuiEnergyGauge.getDummy(GaugeType.STANDARD, this, 197, 17));
        inputSlot = addSlot(SlotType.INPUT, 51, 40);
        outputSlot = addSlot(SlotType.OUTPUT, 155, 40);
        innerScreen = addElement(new GuiInnerScreen(this, 70, 17, 82, 60));
        bar = addElement(new GuiCustomDynamicHorizontalRateBar(this, getBarProgressTimer(), 30, 79, 160));
        bar.setColorFunction(this::getColor);
    }

    private Color getColor(float value) {
        return chemicalColor.blend(Color.WHITE, value);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ReconstructionRecipe recipe, IFocusGroup group) {
        initFluid(builder, RecipeIngredientRole.INPUT, fluidGauge, recipe.getInputFluid().getRepresentations());
        initChemical(builder, MekanismJEI.TYPE_GAS, RecipeIngredientRole.INPUT, inputGasGauge,
                recipe.getInputGas().getRepresentations());
        initItem(builder, recipe.getItemNotConsumed() ? RecipeIngredientRole.CATALYST : RecipeIngredientRole.INPUT,
                inputSlot, recipe.getInputSolid().getRepresentations());
        PressurizedReactionRecipeOutput output = recipe.getOutput(null, null, null);
        if (!output.item().isEmpty()) {
            initItem(builder, RecipeIngredientRole.OUTPUT, outputSlot, List.of(output.item()));
        }
        if (!output.gas().isEmpty()) {
            initChemical(builder, MekanismJEI.TYPE_GAS, RecipeIngredientRole.OUTPUT, outputGasGauge,
                    List.of(output.gas()));
        }
        chemicalColor = Color.rgb(recipe.getInputGas().getRepresentations().get(0).getType().getTint());
    }

    @Override
    protected void renderElements(ReconstructionRecipe recipe, IRecipeSlotsView recipeSlotView, GuiGraphics guiGraphics,
            int x, int y) {
        super.renderElements(recipe, recipeSlotView, guiGraphics, x, y);
        energyGauge.renderContents(guiGraphics);
    }

    @Override
    public List<Component> getTooltipStrings(ReconstructionRecipe recipe, IRecipeSlotsView recipeSlotsView,
            double mouseX, double mouseY) {
        if (energyGauge.isMouseOver(mouseX, mouseY)) {
            return Collections.singletonList(Component
                    .literal("Energy per tick:" + recipe.getEnergyRequired().add(2000000000).toString() + " Joles."));
        }
        return super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
    }

    @Override
    public void draw(ReconstructionRecipe recipe,
            IRecipeSlotsView recipeSlotsView,
            GuiGraphics guiGraphics,
            double mouseX,
            double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        float scale = 0.8F;
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scale, scale, 1.0F);
        guiGraphics.drawString(
                getFont(),
                recipe.getItemNotConsumed() ? "Item won't be consumed." : "Item will be consumed.",
                (int) (70 / scale),
                (int) (30 / scale),
                SpecialColors.TEXT_SCREEN.argb());
        guiGraphics.drawString(
                getFont(),
                recipe.getDuration() + " Ticks Required.",
                (int) (70 / scale),
                (int) (40 / scale),
                SpecialColors.TEXT_SCREEN.argb());
        guiGraphics.pose().popPose();
    }
}
