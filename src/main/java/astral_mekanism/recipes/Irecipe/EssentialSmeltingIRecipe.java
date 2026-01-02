package astral_mekanism.recipes.irecipe;

import astral_mekanism.recipes.output.ItemInfuseOutput;
import astral_mekanism.recipes.recipe.ItemToItemInfuseRecipe;
import astral_mekanism.registries.AstralMekanismInfuseTypes;
import astral_mekanism.registries.AstralMekanismRecipeSerializers;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;

public class EssentialSmeltingIRecipe extends ItemToItemInfuseRecipe {

    public EssentialSmeltingIRecipe(ResourceLocation id, ItemStackIngredient input, ItemInfuseOutput output) {
        super(id, input, output);
    }

    public static EssentialSmeltingIRecipe fromSmeltingRecipe(SmeltingRecipe smeltingRecipe) {
        return new EssentialSmeltingIRecipe(smeltingRecipe.getId(),
                IngredientCreatorAccess.item().createMulti(smeltingRecipe.getIngredients().stream()
                        .map(IngredientCreatorAccess.item()::from)
                        .toArray(ItemStackIngredient[]::new)),
                new ItemInfuseOutput(smeltingRecipe.assemble(null, null),
                        AstralMekanismInfuseTypes.XP.getStack((long) (smeltingRecipe.getExperience() * 100))));
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AstralMekanismRecipeSerializers.ESSENTIAL_SMELTING.get();
    }

    @Override
    public RecipeType<?> getType() {
        return AstralMekanismRecipeTypes.ESSENTIAL_SMELTING.get();
    }

}
