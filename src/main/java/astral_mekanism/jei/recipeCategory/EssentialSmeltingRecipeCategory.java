package astral_mekanism.jei.recipeCategory;

import java.util.Arrays;
import java.util.List;

import astral_mekanism.registries.AstralMekanismInfuseTypes;
import mekanism.api.providers.IItemProvider;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.gauge.GuiInfusionGauge;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.jei.MekanismJEI;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.tile.component.config.DataType;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;

public class EssentialSmeltingRecipeCategory extends BaseGeneralRecipeCategory<SmeltingRecipe> {
    private final GuiGauge<?> xpGauge;
    private final GuiSlot output;
    private final GuiSlot input;

    public EssentialSmeltingRecipeCategory(IGuiHelper helper, RecipeType<SmeltingRecipe> recipeType,
            IItemProvider provider) {
        super(helper, recipeType, provider, 28, 16, 144, 54);
        this.xpGauge = this
                .addElement(GuiInfusionGauge.getDummy(GaugeType.STANDARD.with(DataType.OUTPUT), this, 137, 13));
        this.output = this.addSlot(SlotType.OUTPUT, 116, 35);
        this.input = this.addSlot(SlotType.INPUT, 64, 17);
        addSlot(SlotType.POWER, 64, 53).with(SlotOverlay.POWER);
        addSlot(SlotType.NORMAL, 116, 53).with(SlotOverlay.PLUS);
        addElement(new GuiVerticalPowerBar(this, FULL_BAR, 164, 15));
        addSimpleProgress(ProgressType.BAR, 86, 38);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SmeltingRecipe recipe, IFocusGroup focusGroup) {
        initItem(builder, RecipeIngredientRole.INPUT, input, recipe.getIngredients().stream()
                .flatMap(ingredient -> Arrays.stream(ingredient.getItems()))
                .toList());
        initItem(builder, RecipeIngredientRole.OUTPUT, output, List.of(recipe.getResultItem(null)));
        float xp = recipe.getExperience();
        if (xp != 0) {
            initChemical(builder, MekanismJEI.TYPE_INFUSION, RecipeIngredientRole.OUTPUT, xpGauge,
                    List.of(AstralMekanismInfuseTypes.XP.getStack((long) (xp * 100))));
        }
    }

}
