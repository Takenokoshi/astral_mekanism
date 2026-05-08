package astral_mekanism.recipe;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.fxd927.mekanismelements.common.registries.MSBlocks;

import astral_mekanism.AMEConstants;
import astral_mekanism.registries.AMEItems;
import astral_mekanism.registries.AMEMachines;
import fr.iglee42.evolvedmekanism.registries.EMBlocks;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.registries.MekanismItems;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.level.ItemLike;

public class EnchatedMachineRecipeBuilding {

    public static void build(Consumer<FinishedRecipe> consumer, Function<ItemLike, CriterionTriggerInstance> function) {
        for (EnchantedAndNormal enchantedAndNormal : LIST_NORMAL) {
            ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, enchantedAndNormal.enchanted)
                    .pattern("ACA")
                    .pattern("INI")
                    .pattern("ACA")
                    .define('A', AMEItems.ENCHANTED_ALLOY)
                    .define('C', AMEItems.ENHANCED_CONTROL_CIRCUIT)
                    .define('I', MekanismItems.REFINED_OBSIDIAN_INGOT)
                    .define('N', enchantedAndNormal.normal)
                    .unlockedBy("unlock_" + enchantedAndNormal.enchanted.getRegistryName().getPath(),
                            function.apply(AMEItems.ENCHANTED_ALLOY))
                    .save(consumer, AMEConstants.rl(
                            "craftting/enchanted_machine/" + enchantedAndNormal.enchanted.getRegistryName().getPath()));
        }
        for (EnchantedAndNormal enchantedAndNormal : LIST_GASUPGRADE) {
            ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, enchantedAndNormal.enchanted)
                    .pattern("ACA")
                    .pattern("INI")
                    .pattern("ACA")
                    .define('A', AMEItems.ENCHANTED_ALLOY)
                    .define('C', AMEItems.ENHANCED_CONTROL_CIRCUIT)
                    .define('I', AMEItems.BUNDLED_GAS_UPGRADE)
                    .define('N', enchantedAndNormal.normal)
                    .unlockedBy("unlock_" + enchantedAndNormal.enchanted.getRegistryName().getPath(),
                            function.apply(AMEItems.ENCHANTED_ALLOY))
                    .save(consumer, AMEConstants.rl(
                            "craftting/enchanted_machine/" + enchantedAndNormal.enchanted.getRegistryName().getPath()));
        }
    }

    private static final List<EnchantedAndNormal> LIST_NORMAL = new ArrayList<>();
    private static final List<EnchantedAndNormal> LIST_GASUPGRADE = new ArrayList<>();

    private static record EnchantedAndNormal(IBlockProvider enchanted, ItemLike normal) {
    }

    static {
        LIST_NORMAL
                .add(new EnchantedAndNormal(AMEMachines.ENCHANTED_ADSORPTION_SEPARATOR, MSBlocks.ADSORPTION_SEPARATOR));
        LIST_NORMAL.add(new EnchantedAndNormal(AMEMachines.ENCHANTED_AIR_COMPRESSOR, MSBlocks.AIR_COMPRESSOR));
        LIST_NORMAL.add(new EnchantedAndNormal(AMEMachines.ENCHANTED_ANTIPROTONIC_NUCLEOSYNTHESIZER,
                MekanismBlocks.ANTIPROTONIC_NUCLEOSYNTHESIZER));
        LIST_NORMAL
                .add(new EnchantedAndNormal(AMEMachines.ENCHANTED_CHEMICAL_INFUSER, MekanismBlocks.CHEMICAL_INFUSER));
        LIST_NORMAL
                .add(new EnchantedAndNormal(AMEMachines.ENCHANTED_CHEMICAL_OXIDIZER, MekanismBlocks.CHEMICAL_OXIDIZER));
        LIST_NORMAL.add(new EnchantedAndNormal(AMEMachines.ENCHANTED_CHEMICAL_WASHER, MekanismBlocks.CHEMICAL_WASHER));
        LIST_NORMAL.add(new EnchantedAndNormal(AMEMachines.ENCHANTED_CHEMIXER, EMBlocks.CHEMIXER));
        LIST_NORMAL.add(new EnchantedAndNormal(AMEMachines.ENCHANTED_CRUSHER, MekanismBlocks.CRUSHER));
        LIST_NORMAL
                .add(new EnchantedAndNormal(AMEMachines.ENCHANTED_CRYSTALLIZER, MekanismBlocks.CHEMICAL_CRYSTALLIZER));
        LIST_NORMAL.add(new EnchantedAndNormal(AMEMachines.ENCHANTED_ELECTROLYTIC_SEPARATOR,
                MekanismBlocks.ELECTROLYTIC_SEPARATOR));
        LIST_NORMAL.add(new EnchantedAndNormal(AMEMachines.ENCHANTED_ENERGIZED_SMELTER,
                AMEMachines.ESSENTIAL_ENERGIZED_SMELTER));
        LIST_NORMAL.add(
                new EnchantedAndNormal(AMEMachines.ENCHANTED_ENRICHMENT_CHAMBER, MekanismBlocks.ENRICHMENT_CHAMBER));
        LIST_NORMAL.add(new EnchantedAndNormal(AMEMachines.ENCHANTED_FORMULAIC_ASSEMBLICATOR,
                AMEMachines.ESSENTIAL_FORMULAIC_ASSEMBLICATOR));
        LIST_NORMAL.add(
                new EnchantedAndNormal(AMEMachines.ENCHANTED_ISTOPIC_CENTRIFUGE, MekanismBlocks.ISOTOPIC_CENTRIFUGE));
        LIST_NORMAL.add(new EnchantedAndNormal(AMEMachines.ENCHANTED_MELTER, EMBlocks.MELTER));
        LIST_NORMAL
                .add(new EnchantedAndNormal(AMEMachines.ENCHANTED_OSMIUM_COMPRESSOR, MekanismBlocks.OSMIUM_COMPRESSOR));
        LIST_NORMAL
                .add(new EnchantedAndNormal(AMEMachines.ENCHANTED_PAINTING_MACHINE, MekanismBlocks.PAINTING_MACHINE));
        LIST_NORMAL.add(new EnchantedAndNormal(AMEMachines.ENCHANTED_PRC, MekanismBlocks.PRESSURIZED_REACTION_CHAMBER));
        LIST_NORMAL.add(new EnchantedAndNormal(AMEMachines.ENCHANTED_ROTARY_CONDENSENTRATOR,
                MekanismBlocks.ROTARY_CONDENSENTRATOR));
        LIST_NORMAL.add(
                new EnchantedAndNormal(AMEMachines.ENCHANTED_METALLURGIC_INFUSER, MekanismBlocks.METALLURGIC_INFUSER));
        LIST_NORMAL.add(new EnchantedAndNormal(AMEMachines.ENCHANTED_ALLOYER, EMBlocks.ALLOYER));
        LIST_NORMAL.add(new EnchantedAndNormal(AMEMachines.ENCHANTED_SPS, AMEMachines.COMPACT_SPS));

        LIST_GASUPGRADE.add(new EnchantedAndNormal(AMEMachines.ENCHANTED_CHEMICAL_INJECTION_CHAMBER,
                MekanismBlocks.CHEMICAL_INJECTION_CHAMBER));
        LIST_GASUPGRADE.add(new EnchantedAndNormal(AMEMachines.ENCHANTED_PURIFICATION_CHAMBER,
                MekanismBlocks.PURIFICATION_CHAMBER));
        LIST_GASUPGRADE.add(new EnchantedAndNormal(AMEMachines.ENCHANTED_DISSOLUTION_CHAMBER,
                MekanismBlocks.CHEMICAL_DISSOLUTION_CHAMBER));
    }
}
