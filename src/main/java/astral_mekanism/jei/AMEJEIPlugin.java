package astral_mekanism.jei;

import org.jetbrains.annotations.Nullable;

import com.fxd927.mekanismelements.client.MSJEIRecipeType;
import com.jerry.generator_extras.common.ExtraGenLang;
import com.jerry.generator_extras.common.genregistry.ExtraGenBlocks;
import com.jerry.generator_extras.common.genregistry.ExtraGenItem;

import appeng.integration.modules.jei.ChargerCategory;
import appeng.integration.modules.jei.TransformCategory;
import appeng.recipes.AERecipeTypes;
import astral_mekanism.AMEConstants;
import astral_mekanism.generalrecipe.recipe.CropSoilRecipe;
import astral_mekanism.jei.jeirecipe.GasBurningJEIRecipe;
import astral_mekanism.jei.jeirecipe.MekanicalComposterJEIRecipe;
import astral_mekanism.jei.jeirecipe.MixingReactorJEIrecipe;
import astral_mekanism.jei.recipeCategory.AstralCraftingRecipeCategory;
import astral_mekanism.jei.recipeCategory.CropSoilRecipeCategory;
import astral_mekanism.jei.recipeCategory.EssentialSmeltingRecipeCategory;
import astral_mekanism.jei.recipeCategory.FluidInfuserRecipeCategory;
import astral_mekanism.jei.recipeCategory.GasBurningRecipeCategory;
import astral_mekanism.jei.recipeCategory.GasInfusionToFluidRecipeCategory;
import astral_mekanism.jei.recipeCategory.MekanicalComposterRecipeCategory;
import astral_mekanism.jei.recipeCategory.MekanicalTransformRecipeCategory;
import astral_mekanism.jei.recipeCategory.MixingReactorRecipeCategory;
import astral_mekanism.jei.recipeCategory.ReconstructionRecipeCategory;
import astral_mekanism.jei.recipeCategory.TransformRecipeCategory;
import astral_mekanism.jei.transferHandler.AstralFormulaicAssemblicatorTransferHandler;
import astral_mekanism.registries.AMEMachines;
import astral_mekanism.registries.AMERecipeTypes;
import fr.iglee42.evolvedmekanism.jei.EMJEI;
import mekanism.api.providers.IItemProvider;
import mekanism.client.jei.CatalystRegistryHelper;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.client.jei.RecipeRegistryHelper;
import mekanism.client.jei.machine.GasToGasRecipeCategory;
import mekanism.client.jei.machine.ItemStackToItemStackRecipeCategory;
import mekanism.generators.client.jei.GeneratorsJEIRecipeType;
import mekanism.generators.common.GeneratorsLang;
import mekanism.generators.common.registries.GeneratorsBlocks;
import mekanism.generators.common.registries.GeneratorsItems;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import mezz.jei.api.runtime.IRecipesGui;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;

@JeiPlugin

public class AMEJEIPlugin implements IModPlugin {
    private static IJeiRuntime runtime;
    private static IRecipesGui recipesGui;

    @Override
    public ResourceLocation getPluginUid() {
        return AMEConstants.rl("jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registry) {
        IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();
        registry.addRecipeCategories(
                new IRecipeCategory[] {
                        new ReconstructionRecipeCategory(guiHelper, AMEJEIRecipeType.RECONSTRUCTION,
                                AMEMachines.INTERSTELLAR_POSITRONIC_MATTER_RECONSTRUCTION_APPARATUS),
                        new FluidInfuserRecipeCategory(guiHelper, AMEJEIRecipeType.FLUID_INFUSER_RECIPE),
                        new GasToGasRecipeCategory(guiHelper, AMEJEIRecipeType.SPS_RECIPE,
                                AMEMachines.COMPACT_SPS),
                        new AstralCraftingRecipeCategory(guiHelper, AMEJEIRecipeType.ASTRAL_CRAFTING,
                                AMEMachines.ASTRAL_CRAFTER),
                        new EssentialSmeltingRecipeCategory(guiHelper, AMEJEIRecipeType.ESSENTIAL_SMELTING,
                                AMEMachines.ESSENTIAL_ENERGIZED_SMELTER),
                        new GasInfusionToFluidRecipeCategory(guiHelper, AMEJEIRecipeType.INFUSING_CONDENSE,
                                AMEMachines.INFUSING_CONDENSENTRATOR),
                        new MekanicalComposterRecipeCategory(guiHelper,
                                AMEJEIRecipeType.MEKANICAL_COMPOSTER,
                                AMEMachines.MEKANICAL_COMPOSTER),
                        new MekanicalTransformRecipeCategory(guiHelper,
                                AMEJEIRecipeType.MEKANICAL_TRANSFORM,
                                AMEMachines.TRANSFORMER),
                        new TransformRecipeCategory(guiHelper,
                                AMEJEIRecipeType.TRANSFORM,
                                AMEMachines.TRANSFORMER),
                        new MixingReactorRecipeCategory(guiHelper,
                                AMEJEIRecipeType.FUSION_REACTOR,
                                GeneratorsLang.FUSION_REACTOR.translate(),
                                GeneratorsBlocks.FUSION_REACTOR_CONTROLLER),
                        new MixingReactorRecipeCategory(guiHelper,
                                AMEJEIRecipeType.NAQUADAH_REACTOR,
                                ExtraGenLang.NAQUADAH_REACTOR.translate(),
                                ExtraGenBlocks.NAQUADAH_REACTOR_CONTROLLER),
                        new CropSoilRecipeCategory(guiHelper,
                                AMEJEIRecipeType.CROP_SOIL,
                                AMEMachines.GREEN_HOUSE),
                        new GasBurningRecipeCategory(guiHelper,
                                AMEJEIRecipeType.GAS_BURNING,
                                GeneratorsBlocks.GAS_BURNING_GENERATOR),
                        new ItemStackToItemStackRecipeCategory(guiHelper,
                                AMEJEIRecipeType.ITEM_COMPRESSING,
                                AMEMachines.ITEM_COMPRESSOR),
                        new ItemStackToItemStackRecipeCategory(guiHelper,
                                AMEJEIRecipeType.ITEM_UNZIPPING,
                                AMEMachines.ITEM_UNZIPPER),
                        new GasToGasRecipeCategory(guiHelper,
                                AMEJEIRecipeType.GAS_CONVERSION,
                                AMEMachines.GAS_CONVERTER),
                });
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        RecipeRegistryHelper.register(registry, AMEJEIRecipeType.RECONSTRUCTION, AMERecipeTypes.RECONSTRUCTION);
        RecipeRegistryHelper.register(registry, AMEJEIRecipeType.FLUID_INFUSER_RECIPE,
                AMERecipeTypes.FLUID_INFUSER_RECIPE);
        RecipeRegistryHelper.register(registry, AMEJEIRecipeType.SPS_RECIPE, AMERecipeTypes.SPS_RECIPE);
        RecipeRegistryHelper.register(registry, AMEJEIRecipeType.ASTRAL_CRAFTING,
                AMERecipeTypes.ASTRAL_CRAFTING);
        RecipeRegistryHelper.register(registry, AMEJEIRecipeType.ITEM_COMPRESSING,
                AMERecipeTypes.ITEM_COMPRESSING);
        RecipeRegistryHelper.register(registry, AMEJEIRecipeType.ITEM_UNZIPPING,
                AMERecipeTypes.ITEM_UNZIPPING);
        RecipeRegistryHelper.register(registry, AMEJEIRecipeType.MEKANICAL_TRANSFORM,
                AMERecipeTypes.MEKANICAL_TRAMSFORM);
        RecipeRegistryHelper.register(registry, AMEJEIRecipeType.INFUSING_CONDENSE,
                AMERecipeTypes.INFUSING_CONDENSE);
        RecipeRegistryHelper.register(registry, AMEJEIRecipeType.FUSION_REACTOR,
                MixingReactorJEIrecipe.fusionRecipes);
        RecipeRegistryHelper.register(registry, AMEJEIRecipeType.NAQUADAH_REACTOR,
                MixingReactorJEIrecipe.naquadahRecipes);
        RecipeRegistryHelper.register(registry, AMEJEIRecipeType.MEKANICAL_COMPOSTER,
                MekanicalComposterJEIRecipe.getRecipes());
        RecipeRegistryHelper.register(registry, AMEJEIRecipeType.ESSENTIAL_SMELTING,
                Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(RecipeType.SMELTING));
        RecipeRegistryHelper.register(registry, AMEJEIRecipeType.TRANSFORM,
                Minecraft.getInstance().level.getRecipeManager().getAllRecipesFor(AERecipeTypes.TRANSFORM));
        RecipeRegistryHelper.register(registry, AMEJEIRecipeType.CROP_SOIL,
                CropSoilRecipe.getAllRecipes(Minecraft.getInstance().level.getRecipeManager()));
        RecipeRegistryHelper.register(registry, AMEJEIRecipeType.GAS_BURNING,
                GasBurningJEIRecipe.getRecipes());
        RecipeRegistryHelper.register(registry, AMEJEIRecipeType.GAS_CONVERSION,
                AMERecipeTypes.GAS_CONVERSION);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        // ae2
        registry.addRecipeCatalysts(ChargerCategory.RECIPE_TYPE,
                AMEMachines.MEKANICAL_CHARGER, AMEMachines.ASTRAL_MEKANICAL_CHARGER);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.INSCRIBE,
                AMEMachines.MEKANICAL_INSCRIBER,
                AMEMachines.ASTRAL_MEKANICAL_INSCRIBER);
        registry.addRecipeCatalysts(TransformCategory.RECIPE_TYPE,
                AMEMachines.TRANSFORMER, AMEMachines.ASTRAL_TRANSFORMER);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.TRANSFORM,
                AMEMachines.TRANSFORMER, AMEMachines.ASTRAL_TRANSFORMER);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.MATTER_CONDENSER,
                AMEMachines.MEKANICAL_MATTER_CONDENSER);
        // advanced ae
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.AAE_REACTION,
                AMEMachines.ESSENTIAL_REACTION_CHAMBER, AMEMachines.ASTRAL_REACTION_CHAMBER);
        // mekanism
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.INJECTING,
                AMEMachines.ASTRAL_CHEMICAL_INJECTION_CHAMBER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.COMPRESSING,
                AMEMachines.ASTRAL_OSMIUM_COMPRESSOR);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.PURIFYING,
                AMEMachines.ASTRAL_PURIFICATION_CHAMBER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.CRUSHING,
                AMEMachines.ASTRAL_CRUSHER);
        CatalystRegistryHelper.registerRecipeItem(registry, AMEMachines.ASTRAL_ENERGIZED_SMELTER,
                MekanismJEIRecipeType.SMELTING, RecipeTypes.SMELTING);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.SMELTING,
                AMEMachines.ENERGIZED_SMELTING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.SMELTING,
                AMEMachines.ASTRAL_ENERGIZED_SMELTING_FACTRIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.ENRICHING,
                AMEMachines.ASTRAL_ENRICHMENT_CHAMBER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.NUCLEOSYNTHESIZING,
                AMEMachines.ASTRAL_ANTIPROTONIC_NUCLEOSYNTHESIZER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.CHEMICAL_INFUSING,
                AMEMachines.ASTRAL_CHEMICAL_INFUSER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.OXIDIZING,
                AMEMachines.ASTRAL_CHEMICAL_OXIDIZER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.WASHING,
                AMEMachines.ASTRAL_CHEMICAL_WASHER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.COMBINING,
                AMEMachines.ASTRAL_COMBINER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.CRYSTALLIZING,
                AMEMachines.ASTRAL_CRYSTALLIZER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.DISSOLUTION,
                AMEMachines.ASTRAL_DISSOLUTION_CHAMBER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.SEPARATING,
                AMEMachines.ASTRAL_ELECTROLYTIC_SEPARATOR);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.ACTIVATING,
                AMEMachines.GLOWSTONE_NEUTRON_ACTIVATOR,
                AMEMachines.ASTRAL_GNA);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.CENTRIFUGING,
                AMEMachines.ASTRAL_ISOTOPIC_CENTRIFUGE);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.METALLURGIC_INFUSING,
                AMEMachines.ASTRAL_METALLURGIC_INFUSER,
                AMEMachines.ESSENTIAL_METALLURGIC_INFUSER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.REACTION,
                AMEMachines.ASTRAL_PRC);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.SAWING,
                AMEMachines.ASTRAL_PRECISION_SAWMILL);
        CatalystRegistryHelper.registerRecipeItem(registry, AMEMachines.ASTRAL_ROTARY_CONDENSENTRATOR,
                MekanismJEIRecipeType.CONDENSENTRATING, MekanismJEIRecipeType.DECONDENSENTRATING);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.SPS,
                AMEMachines.COMPACT_SPS,
                AMEMachines.ASTRAL_SPS);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.INFUSION_CONVERSION,
                AMEMachines.INFUSE_SYNTHESIZER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.EVAPORATING,
                AMEMachines.COMPACT_TEP.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, GeneratorsJEIRecipeType.FISSION,
                AMEMachines.COMPACT_FIR.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.GAS_CONVERSION,
                AMEMachines.GAS_SYNTHESIZER);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.FUSION_REACTOR,
                AMEMachines.COMPACT_FUSION_REACTOR.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.FUSION_REACTOR,
                GeneratorsBlocks.FUSION_REACTOR_CONTROLLER,
                GeneratorsBlocks.FUSION_REACTOR_FRAME,
                GeneratorsBlocks.FUSION_REACTOR_LOGIC_ADAPTER,
                GeneratorsBlocks.FUSION_REACTOR_PORT,
                GeneratorsBlocks.REACTOR_GLASS,
                GeneratorsItems.HOHLRAUM);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.GAS_BURNING,
                GeneratorsBlocks.GAS_BURNING_GENERATOR);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.GAS_BURNING,
                AMEMachines.GAS_BURNING_GENERATORS.values().toArray(IItemProvider[]::new));

        // Evolved Mekanism
        CatalystRegistryHelper.register(registry, EMJEI.APT,
                AMEMachines.COMPACT_APT,
                AMEMachines.ASTRAL_APT);
        CatalystRegistryHelper.register(registry, EMJEI.ALLOYING, AMEMachines.ASTRAL_ALLOYER);
        CatalystRegistryHelper.register(registry, EMJEI.CHEMIXING, AMEMachines.ASTRAL_CHEMIXER);
        CatalystRegistryHelper.register(registry, EMJEI.MELTING, AMEMachines.ASTRAL_THERMALIZER);
        CatalystRegistryHelper.register(registry, EMJEI.SOLIDIFICATION,
                AMEMachines.ASTRAL_SOLIDIFICATION_CHAMBER);

        // mekanism extras
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.NAQUADAH_REACTOR,
                AMEMachines.COMPACT_NAQUADAH_REACTOR.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.NAQUADAH_REACTOR,
                ExtraGenBlocks.NAQUADAH_REACTOR_CASING,
                ExtraGenBlocks.NAQUADAH_REACTOR_CONTROLLER,
                ExtraGenBlocks.NAQUADAH_REACTOR_LOGIC_ADAPTER,
                ExtraGenBlocks.NAQUADAH_REACTOR_PORT,
                ExtraGenBlocks.LEAD_COATED_GLASS,
                ExtraGenBlocks.LEAD_COATED_LASER_FOCUS_MATRIX,
                ExtraGenItem.HOHLRAUM);

        // mekanism elements
        CatalystRegistryHelper.register(registry, MSJEIRecipeType.RADIATION_IRRADIATOR,
                AMEMachines.ASTRAL_RADIATION_IRRADIATOR);

        // astral_mekanism
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.RECONSTRUCTION,
                AMEMachines.INTERSTELLAR_POSITRONIC_MATTER_RECONSTRUCTION_APPARATUS);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.ASTRAL_CRAFTING,
                AMEMachines.ASTRAL_CRAFTER);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.FLUID_INFUSER_RECIPE,
                AMEMachines.FLUID_INFUSER,
                AMEMachines.ASTRAL_FLUID_INFUSER);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.ITEM_COMPRESSING,
                AMEMachines.ITEM_COMPRESSOR);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.ITEM_UNZIPPING,
                AMEMachines.ITEM_UNZIPPER);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.MEKANICAL_TRANSFORM,
                AMEMachines.TRANSFORMER,
                AMEMachines.ASTRAL_TRANSFORMER);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.ESSENTIAL_SMELTING,
                AMEMachines.ESSENTIAL_ENERGIZED_SMELTER,
                AMEMachines.ASTRAL_ENERGIZED_SMELTER);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.ESSENTIAL_SMELTING,
                AMEMachines.ENERGIZED_SMELTING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.ESSENTIAL_SMELTING,
                AMEMachines.ASTRAL_ENERGIZED_SMELTING_FACTRIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.CROP_SOIL,
                AMEMachines.GREEN_HOUSE, AMEMachines.ASTRAL_GREEN_HOUSE);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.MEKANICAL_COMPOSTER,
                AMEMachines.MEKANICAL_COMPOSTER, AMEMachines.ASTRAL_COMPOSTER);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.INFUSING_CONDENSE,
                AMEMachines.INFUSING_CONDENSENTRATOR);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.GAS_CONVERSION,
                AMEMachines.GAS_CONVERTER);

        // minecraft
        registry.addRecipeCatalysts(RecipeTypes.SMELTING,
                AMEMachines.ENERGIZED_SMELTING_FACTORIES.values().toArray(ItemLike[]::new));
        registry.addRecipeCatalysts(RecipeTypes.SMELTING,
                AMEMachines.ASTRAL_ENERGIZED_SMELTING_FACTRIES.values().toArray(ItemLike[]::new));
        CatalystRegistryHelper.registerRecipeItem(registry, AMEMachines.ASTRAL_FORMULAIC_ASSEMBLICATOR,
                MekanismJEIRecipeType.VANILLA_CRAFTING, RecipeTypes.CRAFTING);
        registry.addRecipeCatalysts(RecipeTypes.COMPOSTING,
                AMEMachines.MEKANICAL_COMPOSTER, AMEMachines.ASTRAL_COMPOSTER);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(new AstralFormulaicAssemblicatorTransferHandler(), RecipeTypes.CRAFTING);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        //ここで各guiに登録
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
        recipesGui = runtime.getRecipesGui();
    }

    public static @Nullable IJeiRuntime getRuntime() {
        return runtime;
    }

    public static @Nullable IRecipesGui getRecipesGui() {
        return recipesGui;
    }
}
