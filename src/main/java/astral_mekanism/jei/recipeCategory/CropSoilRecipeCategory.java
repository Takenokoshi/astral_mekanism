package astral_mekanism.jei.recipeCategory;

import java.util.List;

import astral_mekanism.generalrecipe.recipe.CropSoilRecipe;
import mekanism.api.providers.IItemProvider;
import mekanism.client.SpecialColors;
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
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.darkhax.botanypots.data.recipes.crop.HarvestEntry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class CropSoilRecipeCategory extends BaseRecipeCategory<CropSoilRecipe> {

    private final GuiSlot cropSlot;
    private final GuiSlot soilSlot;
    private final GuiGauge<?> fluidGauge;
    private final GuiSlot[] outputSlots;

    public CropSoilRecipeCategory(IGuiHelper helper, MekanismJEIRecipeType<CropSoilRecipe> recipeType, IItemProvider provider) {
        super(helper, recipeType, provider, 10, 10, 200, 80);
        this.cropSlot = addSlot(SlotType.INPUT, 46, 26);
        this.soilSlot = addSlot(SlotType.INPUT, 46, 44);
        this.fluidGauge = addElement(GuiFluidGauge.getDummy(GaugeType.STANDARD, this, 27, 12));
        this.outputSlots = new GuiSlot[12];
        for (int i = 0; i < 12; i++) {
            outputSlots[i] = addSlot(SlotType.OUTPUT, i % 4 * 18 + 98, i / 4 * 18 + 17);
        }
        addSlot(SlotType.POWER, 10, 17);
        addSlot(SlotType.NORMAL, 10, 53).with(SlotOverlay.MINUS);
        addElement(new GuiVerticalPowerBar(this, FULL_BAR, 170, 16));
        addSimpleProgress(ProgressType.BAR, 68, 38);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CropSoilRecipe recipe, IFocusGroup focusGroup) {
        initItem(builder, RecipeIngredientRole.CATALYST, cropSlot, recipe.getCrop().getRepresentations())
                .addRichTooltipCallback((recipeSlotView, tooltip) -> {
                    tooltip.add(Component.literal("Non consume."));
                });
        initItem(builder, RecipeIngredientRole.CATALYST, soilSlot, recipe.getSoil().getRepresentations())
                .addRichTooltipCallback((recipeSlotView, tooltip) -> {
                    tooltip.add(Component.literal("Non consume."));
                });
        initFluid(builder, RecipeIngredientRole.INPUT, fluidGauge, recipe.getWater().getRepresentations());
        final List<HarvestEntry> entries = recipe.getOutput();
        int size = entries.size();
        for (int i = 0; i < Math.min(12, size); i++) {
            int index = i;
            HarvestEntry entry = entries.get(index);
            ItemStack output = entry.getItem();
            initItem(builder, RecipeIngredientRole.OUTPUT, outputSlots[i], List.of(output))
                    .addRichTooltipCallback((recipeSlotView, tooltip) -> {
                        tooltip.add(Component.literal("Chance: " + Math.round(entry.getChance() * 100)));
                        tooltip.add(Component.literal("Rolls: " + entry.getMinRolls() + " - " + entry.getMaxRolls()));
                    });
        }
    }

    @Override
    public void draw(CropSoilRecipe recipe,
            IRecipeSlotsView recipeSlotsView,
            GuiGraphics guiGraphics,
            double mouseX,
            double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        guiGraphics.drawString(getFont(), Component.literal("Required ticks:" + recipe.requiredTicks),
                98, 60, SpecialColors.TEXT_TITLE.argb());
    }

}
