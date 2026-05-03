package astral_mekanism.recipe;

import java.util.EnumMap;
import java.util.function.Consumer;

import astral_mekanism.AMEConstants;
import astral_mekanism.AMEProcessingData;
import astral_mekanism.AMETier;
import astral_mekanism.registries.AMEBlockDefinitions;
import astral_mekanism.registries.AMEItems;
import astral_mekanism.registries.AMEMachines;
import gripe._90.megacells.definition.MEGAItems;
import io.github.masyumero.emextras.common.registry.EMExtrasItem;
import mekanism.api.providers.IItemProvider;
import mekanism.common.registries.MekanismItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.pedroksl.advanced_ae.common.definitions.AAEItems;

public class AstralMekanismRecipeProvider extends RecipeProvider {

    public AstralMekanismRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        AMEProcessingData.buildRecipes(consumer);
        buildTierMachineUpgradeRecipes(AMEMachines.ASTRAL_ENERGIZED_SMELTING_FACTRIES, consumer,
                "astral_factory/energized_smelting");
        buildTierMachineUpgradeRecipes(AMEMachines.COMPACT_FIR, consumer,
                "compact_machine/fir");
        buildTierMachineUpgradeRecipes(AMEMachines.COMPACT_FUSION_REACTOR, consumer,
                "compact_machine/fusion_reactor");
        buildTierMachineUpgradeRecipes(AMEMachines.COMPACT_NAQUADAH_REACTOR, consumer,
                "compact_machine/naquadah_reactor");
        buildTierMachineUpgradeRecipes(AMEMachines.COMPACT_TEP, consumer,
                "compact_machine/tep");
        buildTierMachineUpgradeRecipes(AMEMachines.ENERGIZED_SMELTING_FACTORIES, consumer,
                "normal_factory/energized_smelting");
        buildEnergyCellRecipes(consumer);
        buildMekanicalMagmaBlockRecipes(consumer);
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
                    .save(consumer, AMEConstants.rl("crafting/energy_cells/" + data.afterTier.nameForAE));
        }
    }

    private static void buildMekanicalMagmaBlockRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, AMEMachines.MEKANICAL_MAGMABLOCKS[0])
                .pattern("MMM")
                .pattern("MCM")
                .pattern("MMM")
                .define('M', Items.MAGMA_BLOCK)
                .define('C', AMEItems.VIBRATION_CONTROL_CIRCUIT)
                .unlockedBy("has_center", has(AMEItems.VIBRATION_CONTROL_CIRCUIT))
                .save(consumer, AMEConstants.rl("crafting/mekanical_magma_block/essential"));
        ItemLike[] circuits = { MekanismItems.BASIC_CONTROL_CIRCUIT, AMEItems.RESONANCE_CONTROL_CIRCUIT,
                MekanismItems.ADVANCED_CONTROL_CIRCUIT, MekanismItems.ELITE_CONTROL_CIRCUIT,
                MekanismItems.ULTIMATE_CONTROL_CIRCUIT, AMEItems.ENHANCED_CONTROL_CIRCUIT,
                MEGAItems.ACCUMULATION_PROCESSOR, AMEItems.PHOTON_PROCESSOR,
                EMExtrasItem.ABSOLUTE_OVERCLOCKED_CONTROL_CIRCUIT, AAEItems.QUANTUM_PROCESSOR,
                EMExtrasItem.SUPREME_QUANTUM_CONTROL_CIRCUIT, AMEItems.COMPOSITE_PROCESSOR,
                EMExtrasItem.COSMIC_DENSE_CONTROL_CIRCUIT, AMEItems.ORIGIN_PROCESSOR,
                EMExtrasItem.INFINITE_MULTIVERSAL_CONTROL_CIRCUIT, AMEItems.AUTONOMY_PROCESSOR,
                AMEItems.FIRMAMENT_PROCESSOR,
                AMEItems.ILLUSION_CONTROL_CIRCUIT, AMEItems.STARRY_SKY_CONTROL_PROCESSOR };
        for (int i = 1; i < AMEMachines.MEKANICAL_MAGMABLOCK_NAMES.length; i++) {
            ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, AMEMachines.MEKANICAL_MAGMABLOCKS[i])
                    .pattern("MMM")
                    .pattern("MCM")
                    .pattern("MMM")
                    .define('M', AMEMachines.MEKANICAL_MAGMABLOCKS[i - 1])
                    .define('C', circuits[i - 1])
                    .unlockedBy("has_center", has(circuits[i - 1]))
                    .save(consumer, AMEConstants
                            .rl("crafting/mekanical_magma_block/" + AMEMachines.MEKANICAL_MAGMABLOCK_NAMES[i]));
        }
    }

}
