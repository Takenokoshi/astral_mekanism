package astral_mekanism.jei.recipeCategory;

import java.util.List;

import astral_mekanism.jei.MekanicalComposterJEIRecipe;
import mekanism.api.providers.IItemProvider;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.MekanismJEIRecipeType;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.Items;

public class MekanicalComposterRecipeCategory extends BaseRecipeCategory<MekanicalComposterJEIRecipe> {

    private final GuiSlot input;
    private final GuiSlot output;

    public MekanicalComposterRecipeCategory(IGuiHelper helper,
            MekanismJEIRecipeType<MekanicalComposterJEIRecipe> recipeType, IItemProvider provider) {
        super(helper, recipeType, provider, 28, 16, 144, 54);
        input = addSlot(SlotType.INPUT, 64, 35);
        output = addSlot(SlotType.OUTPUT, 116, 35);
        addSimpleProgress(ProgressType.BAR, 86, 38);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, MekanicalComposterJEIRecipe recipe, IFocusGroup group) {
        initItem(builder, RecipeIngredientRole.INPUT, input, List.of(recipe.input()));
        initItem(builder, RecipeIngredientRole.OUTPUT, output, List.of(Items.BONE_MEAL.getDefaultInstance()));
    }

}
