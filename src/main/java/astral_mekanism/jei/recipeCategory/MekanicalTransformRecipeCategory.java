package astral_mekanism.jei.recipeCategory;

import astral_mekanism.recipes.output.ItemFluidOutput;
import astral_mekanism.recipes.recipe.MekanicalTransformRecipe;
import mekanism.api.providers.IItemProvider;
import mekanism.client.gui.element.GuiDownArrow;
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
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.network.chat.Component;
import net.minecraftforge.fluids.FluidStack;

public class MekanicalTransformRecipeCategory extends BaseRecipeCategory<MekanicalTransformRecipe> {

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

    public MekanicalTransformRecipeCategory(IGuiHelper helper,
            MekanismJEIRecipeType<MekanicalTransformRecipe> recipeType, IItemProvider provider) {
        super(helper, recipeType, provider, 10, 10, 200, 60);
        addSlot(SlotType.NORMAL, 10, 17).with(SlotOverlay.MINUS);
        addSlot(SlotType.NORMAL, 10, 53);
        addSlot(SlotType.NORMAL, 82, 17).with(SlotOverlay.MINUS);
        addSlot(SlotType.NORMAL, 82, 53);
        addSlot(SlotType.NORMAL, 169, 17).with(SlotOverlay.PLUS);
        addSlot(SlotType.NORMAL, 169, 53);
        addSlot(SlotType.POWER, 190, 4).with(SlotOverlay.POWER);
        addSimpleProgress(ProgressType.BAR, 100, 38);
        addElement(new GuiVerticalPowerBar(this, FULL_BAR, 200, 25));
        addElement(new GuiDownArrow(this, 14, 39));
        addElement(new GuiDownArrow(this, 86, 39));
        addElement(new GuiDownArrow(this, 173, 39));
    }

    private static RecipeIngredientRole convertBoolRole(boolean val) {
        return val ? RecipeIngredientRole.CATALYST : RecipeIngredientRole.INPUT;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MekanicalTransformRecipe recipe, IFocusGroup focusGroup) {
        initItem(builder, convertBoolRole(recipe.isItemACatalyst()), inputSlotA,
                recipe.getInputItemA().getRepresentations())
                .addRichTooltipCallback((recipeSlotView, tooltip) -> {
                    if (recipe.isItemACatalyst()) {
                        tooltip.add(Component.literal("Non consume."));
                    }
                });
        initItem(builder, convertBoolRole(recipe.isItemBCatalyst()), inputSlotB,
                recipe.getInputItemB().getRepresentations())
                .addRichTooltipCallback((recipeSlotView, tooltip) -> {
                    if (recipe.isItemBCatalyst()) {
                        tooltip.add(Component.literal("Non consume."));
                    }
                });
        initItem(builder, convertBoolRole(recipe.isItemCCatalyst()), inputSlotC,
                recipe.getInputItemC().getRepresentations())
                .addRichTooltipCallback((recipeSlotView, tooltip) -> {
                    if (recipe.isItemCCatalyst()) {
                        tooltip.add(Component.literal("Non consume."));
                    }
                });
        initFluid(builder, convertBoolRole(recipe.isFluidACatalyst()), inputTankA,
                recipe.getInputFluidA().getRepresentations())
                .addRichTooltipCallback((recipeSlotView, tooltip) -> {
                    if (recipe.isFluidACatalyst()) {
                        tooltip.add(Component.literal("Non consume."));
                    }
                });
        initFluid(builder, convertBoolRole(recipe.isFluidBCatalyst()), inputTankB,
                recipe.getInputFluidB().getRepresentations())
                .addRichTooltipCallback((recipeSlotView, tooltip) -> {
                    if (recipe.isFluidBCatalyst()) {
                        tooltip.add(Component.literal("Non consume."));
                    }
                });
        initItem(builder, RecipeIngredientRole.OUTPUT, outputSlot,
                recipe.getOutputDefinition().stream().map(ItemFluidOutput::item).toList());
        initFluid(builder, RecipeIngredientRole.OUTPUT, outputTank,
                recipe.getOutputDefinition().stream().map(ItemFluidOutput::fluid).toList());
    }

}
