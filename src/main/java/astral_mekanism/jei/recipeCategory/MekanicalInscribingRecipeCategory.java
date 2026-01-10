package astral_mekanism.jei.recipeCategory;

import java.util.List;

import appeng.recipes.handlers.InscriberProcessType;
import appeng.recipes.handlers.InscriberRecipe;
import mekanism.api.providers.IItemProvider;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.progress.ProgressType;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class MekanicalInscribingRecipeCategory extends BaseGeneralRecipeCategory<InscriberRecipe> {

    private final GuiSlot topInput;
    private final GuiSlot middleInput;
    private final GuiSlot bottomInput;
    private final GuiSlot output;

    public MekanicalInscribingRecipeCategory(IGuiHelper helper, RecipeType<InscriberRecipe> recipeType,
            IItemProvider provider) {
        super(helper, recipeType, provider, 28, 16, 144, 54);
        this.topInput = addSlot(SlotType.INPUT_2, 46, 17);
        this.middleInput = addSlot(SlotType.INPUT, 64, 35);
        this.bottomInput = addSlot(SlotType.INPUT_2, 46, 53);
        this.output = addSlot(SlotType.OUTPUT, 116, 35);
        addElement(new GuiVerticalPowerBar(this, FULL_BAR, 164, 15));
        addSimpleProgress(ProgressType.BAR, 86, 38);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, InscriberRecipe recipe, IFocusGroup focusGroup) {
        initItem(builder, RecipeIngredientRole.INPUT, middleInput,
                List.<ItemStack>of(recipe.getMiddleInput().getItems()));
        initItem(builder, RecipeIngredientRole.OUTPUT, output, List.of(recipe.getResultItem()));
        Ingredient topIngredient = recipe.getTopOptional();
        if (!topIngredient.isEmpty()) {
            initItem(builder,
                    recipe.getProcessType() == InscriberProcessType.INSCRIBE
                            ? RecipeIngredientRole.CATALYST
                            : RecipeIngredientRole.INPUT,
                    topInput,
                    List.<ItemStack>of(topIngredient.getItems()));
        }
        Ingredient bottomIngredient = recipe.getBottomOptional();
        if (!bottomIngredient.isEmpty()) {
            initItem(builder,
                    recipe.getProcessType() == InscriberProcessType.INSCRIBE
                            ? RecipeIngredientRole.CATALYST
                            : RecipeIngredientRole.INPUT,
                    bottomInput,
                    List.<ItemStack>of(bottomIngredient.getItems()));
        }
    }

}
