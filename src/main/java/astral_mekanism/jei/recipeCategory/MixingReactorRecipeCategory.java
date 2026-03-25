package astral_mekanism.jei.recipeCategory;

import java.util.List;

import astral_mekanism.jei.MixingReactorJEIrecipe;
import mekanism.api.providers.IItemProvider;
import mekanism.client.SpecialColors;
import mekanism.client.gui.element.GuiInnerScreen;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.MekanismJEI;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.generators.common.GeneratorsLang;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class MixingReactorRecipeCategory extends
        BaseRecipeCategory<MixingReactorJEIrecipe> {

    private final GuiGasGauge leftFuelGauge = addElement(GuiGasGauge.getDummy(GaugeType.STANDARD, this, 7, 14));
    private final GuiGasGauge mixedFuelGauge = addElement(GuiGasGauge.getDummy(GaugeType.STANDARD, this, 25, 14));
    private final GuiGasGauge rightFuelGauge = addElement(GuiGasGauge.getDummy(GaugeType.STANDARD, this, 43, 14));
    private final GuiFluidGauge waterGauge = addElement(GuiFluidGauge.getDummy(GaugeType.SMALL, this, 115, 44));
    private final GuiGasGauge steamGauge = addElement(GuiGasGauge.getDummy(GaugeType.SMALL, this, 151, 44));

    public MixingReactorRecipeCategory(IGuiHelper helper,
            MekanismJEIRecipeType<MixingReactorJEIrecipe> recipeType, Component title, IItemProvider provider) {
        super(helper, recipeType, title, createIcon(helper, provider), 3, 10, 170, 80);
        addElement(new GuiInnerScreen(this, 61, 14, 108, 30, () -> {
            return List.of(title, GeneratorsLang.REACTOR_INJECTION_RATE.translate(2));
        }));
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MixingReactorJEIrecipe recipe, IFocusGroup group) {
        initChemical(builder, MekanismJEI.TYPE_GAS, RecipeIngredientRole.INPUT, leftFuelGauge,
                List.of(recipe.leftFuel()));
        initChemical(builder, MekanismJEI.TYPE_GAS, RecipeIngredientRole.CATALYST, mixedFuelGauge,
                List.of(recipe.mixedFuel()));
        initChemical(builder, MekanismJEI.TYPE_GAS, RecipeIngredientRole.INPUT, rightFuelGauge,
                List.of(recipe.rightFuel()));
        initFluid(builder, RecipeIngredientRole.INPUT, waterGauge, List.of(recipe.water()));
        initChemical(builder, MekanismJEI.TYPE_GAS, RecipeIngredientRole.OUTPUT, steamGauge,
                List.of(recipe.steam()));
    }

    @Override
    public void draw(MixingReactorJEIrecipe recipe,
            IRecipeSlotsView recipeSlotsView,
            GuiGraphics guiGraphics,
            double mouseX,
            double mouseY) {
        super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        guiGraphics.drawString(getFont(), Component.literal("Requied Temperature:" + recipe.requierdTemp()),
                7, 74, SpecialColors.TEXT_TITLE.argb());
    }
}
