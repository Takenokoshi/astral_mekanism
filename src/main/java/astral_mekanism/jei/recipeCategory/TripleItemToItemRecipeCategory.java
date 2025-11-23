package astral_mekanism.jei.recipeCategory;

import astral_mekanism.recipes.recipe.TripleItemToItemRecipe;
import mekanism.api.providers.IItemProvider;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.MekanismJEIRecipeType;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;

public class TripleItemToItemRecipeCategory extends BaseRecipeCategory<TripleItemToItemRecipe> {
    private final GuiSlot inputItemA;
    private final GuiSlot inputItemB;
    private final GuiSlot inputItemC;
    private final GuiSlot outputItem;

    public TripleItemToItemRecipeCategory(IGuiHelper helper,
            MekanismJEIRecipeType<TripleItemToItemRecipe> recipeType, IItemProvider provider) {
        super(helper, recipeType, provider, 3, 10, 170, 60);
        inputItemA = addSlot(SlotType.INPUT, 46, 35);
        inputItemB = addSlot(SlotType.INPUT, 64, 35);
        inputItemC = addSlot(SlotType.INPUT, 82, 35);
        outputItem = addSlot(SlotType.OUTPUT, 134, 35);
        addSlot(SlotType.POWER, 155, 14);
        addElement(new GuiVerticalPowerBar(this, FULL_BAR, 164, 15));
        this.addSimpleProgress(ProgressType.RIGHT, 100, 39);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TripleItemToItemRecipe recipe, IFocusGroup focusGroup) {
        initItem(builder, RecipeIngredientRole.INPUT, inputItemA, recipe.getInputItemA().getRepresentations());
        initItem(builder, RecipeIngredientRole.INPUT, inputItemB, recipe.getInputItemB().getRepresentations());
        initItem(builder, RecipeIngredientRole.INPUT, inputItemC, recipe.getInputItemC().getRepresentations());
        initItem(builder, RecipeIngredientRole.OUTPUT, outputItem, recipe.getOutputDefinition());
    }

}
