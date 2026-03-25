package astral_mekanism.generalrecipe.recipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import astral_mekanism.AMEConstants;
import astral_mekanism.generalrecipe.AMEFakeRecipeType;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import mekanism.api.recipes.ingredients.creator.IngredientCreatorAccess;
import mekanism.common.registries.MekanismFluids;
import net.darkhax.botanypots.BotanyPotHelper;
import net.darkhax.botanypots.data.recipes.crop.BasicCrop;
import net.darkhax.botanypots.data.recipes.crop.HarvestEntry;
import net.darkhax.botanypots.data.recipes.soil.BasicSoil;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.util.TriPredicate;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public class CropSoilRecipe implements Recipe<Container>, TriPredicate<ItemStack, ItemStack, FluidStack> {

    private final BasicCrop crop;
    private final BasicSoil soil;
    private final String soilCategory;
    private final ResourceLocation id;
    public final int requiredTicks;
    private final ItemStackIngredient cropIngredient;
    private final ItemStackIngredient soilIngredient;
    private final FluidStackIngredient waterIngredient;
    private final List<HarvestEntry> outputEntries;

    private CropSoilRecipe(BasicCrop crop, BasicSoil soil, FluidStack fluidStack, int requiredTicks,
            int outputMultiply) {
        this.crop = crop;
        this.soil = soil;
        soilCategory = this.crop.getSoilCategories().stream()
                .filter(st -> this.soil.getCategories().contains(st))
                .findFirst().orElse(null);
        if (soilCategory == null) {
            throw new NullPointerException();
        }
        id = AMEConstants.rl(
                "cropsoil/" + this.crop.getId().getPath() + "/" + this.soil.getId().getPath() + "/" + soilCategory + "/"
                        + ForgeRegistries.FLUID_TYPES.get().getKey(fluidStack.getFluid().getFluidType()).getPath());
        this.requiredTicks = requiredTicks;
        cropIngredient = IngredientCreatorAccess.item().from(this.crop.getSeed());
        soilIngredient = IngredientCreatorAccess.item().from(this.soil.getIngredient());
        waterIngredient = IngredientCreatorAccess.fluid().from(fluidStack);
        outputEntries = this.crop.getResults().stream()
                .map(entry -> new HarvestEntry(entry.getChance(), entry.getItem(), entry.getMinRolls() * outputMultiply,
                        entry.getMaxRolls() * outputMultiply))
                .toList();
    }

    @Override
    public boolean matches(Container p_44002_, Level p_44003_) {
        return crop.matches(p_44002_, p_44003_);
    }

    @Override
    public ItemStack assemble(Container p_44001_, RegistryAccess p_267165_) {
        return crop.assemble(p_44001_, p_267165_);
    }

    @Override
    public boolean canCraftInDimensions(int p_43999_, int p_44000_) {
        return crop.canCraftInDimensions(p_43999_, p_44000_);
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
        return crop.getResultItem(p_267052_);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return AMEFakeRecipeType.CROP_SOIL_FLUID;
    }

    @Override
    public boolean test(ItemStack cropStack, ItemStack soilStack, FluidStack fluidStack) {
        return crop.getSeed().test(cropStack) && soil.getIngredient().test(soilStack)
                && waterIngredient.test(fluidStack);
    }

    public boolean emptyableTest(ItemStack cropStack, ItemStack soilStack, FluidStack fluidStack) {
        return (cropStack.isEmpty() || crop.getSeed().test(cropStack))
                && (soilStack.isEmpty() || soil.getIngredient().test(soilStack))
                && (fluidStack.isEmpty() || waterIngredient.test(fluidStack));
    }

    public ItemStackIngredient getCrop() {
        return cropIngredient;
    }

    public ItemStackIngredient getSoil() {
        return soilIngredient;
    }

    public FluidStackIngredient getWater() {
        return waterIngredient;
    }

    public List<HarvestEntry> getOutput() {
        return outputEntries;
    }

    public static List<CropSoilRecipe> getAllRecipes(RecipeManager manager) {
        List<BasicCrop> basicCrops = manager.getAllRecipesFor(BotanyPotHelper.CROP_TYPE.get())
                .stream().filter(cr -> cr instanceof BasicCrop)
                .map(p -> (BasicCrop) p)
                .toList();
        List<BasicSoil> basicSoils = manager.getAllRecipesFor(BotanyPotHelper.SOIL_TYPE.get())
                .stream().filter(cr -> cr instanceof BasicSoil)
                .map(p -> (BasicSoil) p)
                .toList();
        List<CropSoilRecipe> result = new ArrayList<>();
        for (BasicCrop basicCrop : basicCrops) {
            for (BasicSoil basicSoil : basicSoils) {
                if (!Collections.disjoint(basicCrop.getSoilCategories(), basicSoil.getCategories())) {
                    result.add(new CropSoilRecipe(basicCrop, basicSoil,
                            new FluidStack(Fluids.WATER, 100),
                            Math.round(800 / basicSoil.getGrowthModifier()), 2));
                    result.add(new CropSoilRecipe(basicCrop, basicSoil,
                            MekanismFluids.NUTRITIONAL_PASTE.getFluidStack(100),
                            Math.round(400 / basicSoil.getGrowthModifier()), 6));
                }
            }
        }
        return Collections.unmodifiableList(result);
    }

}
