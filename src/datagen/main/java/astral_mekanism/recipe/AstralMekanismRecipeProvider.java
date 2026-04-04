package astral_mekanism.recipe;

import java.util.EnumMap;
import java.util.function.Consumer;

import astral_mekanism.AMEConstants;
import astral_mekanism.AMEProcessingData;
import astral_mekanism.AMETier;
import astral_mekanism.registries.AMEBlockDefinitions;
import astral_mekanism.registries.AstralMekanismMachines;
import mekanism.api.providers.IItemProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;

public class AstralMekanismRecipeProvider extends RecipeProvider {

    public AstralMekanismRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        AMEProcessingData.buildRecipes(consumer);
        buildTierMachineUpgradeRecipes(AstralMekanismMachines.ASTRAL_ENERGIZED_SMELTING_FACTRIES, consumer,
                "astral_factory/energized_smelting");
        buildTierMachineUpgradeRecipes(AstralMekanismMachines.COMPACT_FIR, consumer,
                "compact_machine/fir");
        buildTierMachineUpgradeRecipes(AstralMekanismMachines.COMPACT_FUSION_REACTOR, consumer,
                "compact_machine/fusion_reactor");
        buildTierMachineUpgradeRecipes(AstralMekanismMachines.COMPACT_NAQUADAH_REACTOR, consumer,
                "compact_machine/naquadah_reactor");
        buildTierMachineUpgradeRecipes(AstralMekanismMachines.COMPACT_TEP, consumer,
                "compact_machine/tep");
        buildTierMachineUpgradeRecipes(AstralMekanismMachines.ENERGIZED_SMELTING_FACTORIES, consumer,
                "normal_factory/energized_smelting");
        buildEnergyCellRecipes(consumer);
    }

    private static void buildTierMachineUpgradeRecipes(EnumMap<AMETier, ? extends IItemProvider> machines,
            Consumer<FinishedRecipe> consumer, String path) {
        for (AstralMekanismTierRecipeData data : AstralMekanismTierRecipeData.values()) {
            ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, machines.get(data.afterTier))
                    .pattern("ABC")
                    .pattern("DEF")
                    .pattern("ABC")
                    .define('A', data.leftAlloy)
                    .define('B', data.centerItem)
                    .define('C', data.rightAlloy)
                    .define('D', data.leftCircuit)
                    .define('E', machines.get(data.beforeTier))
                    .define('F', data.rightCircuit)
                    .unlockedBy("has_before", has(machines.get(data.beforeTier)))
                    .save(consumer, AMEConstants.rl(path + "/" + data.afterTier.nameForNormal));
        }
    }

    private static void buildEnergyCellRecipes(Consumer<FinishedRecipe> consumer) {/* */
        for (AstralMekanismTierRecipeData data : AstralMekanismTierRecipeData.values()) {
            ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, AMEBlockDefinitions.ENERGY_CELLS.get(data.afterTier))
                    .pattern("BBB")
                    .pattern("BAB")
                    .pattern("BBB")
                    .define('A', data.processor)
                    .define('B', AMEBlockDefinitions.ENERGY_CELLS.get(data.beforeTier))
                    .unlockedBy("has_before", has(AMEBlockDefinitions.ENERGY_CELLS.get(data.beforeTier)))
                    .save(consumer, AMEConstants.rl("crafting/energy_cells/"+data.afterTier.nameForAE));
        }
    }

}
