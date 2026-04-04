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
import astral_mekanism.jei.recipeCategory.TransformRecipeCategory;
import astral_mekanism.jei.transferHandler.AstralFormulaicAssemblicatorTransferHandler;
import astral_mekanism.registries.AstralMekanismMachines;
import astral_mekanism.registries.AstralMekanismRecipeTypes;
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
                        new FluidInfuserRecipeCategory(guiHelper, AMEJEIRecipeType.FLUID_INFUSER_RECIPE),
                        new GasToGasRecipeCategory(guiHelper, AMEJEIRecipeType.SPS_RECIPE,
                                AstralMekanismMachines.COMPACT_SPS),
                        new AstralCraftingRecipeCategory(guiHelper, AMEJEIRecipeType.ASTRAL_CRAFTING,
                                AstralMekanismMachines.ASTRAL_CRAFTER),
                        new EssentialSmeltingRecipeCategory(guiHelper, AMEJEIRecipeType.ESSENTIAL_SMELTING,
                                AstralMekanismMachines.ESSENTIAL_ENERGIZED_SMELTER),
                        new GasInfusionToFluidRecipeCategory(guiHelper, AMEJEIRecipeType.INFUSING_CONDENSE,
                                AstralMekanismMachines.INFUSING_CONDENSENTRATOR),
                        new MekanicalComposterRecipeCategory(guiHelper,
                                AMEJEIRecipeType.MEKANICAL_COMPOSTER,
                                AstralMekanismMachines.MEKANICAL_COMPOSTER),
                        new MekanicalTransformRecipeCategory(guiHelper,
                                AMEJEIRecipeType.MEKANICAL_TRANSFORM,
                                AstralMekanismMachines.TRANSFORMER),
                        new TransformRecipeCategory(guiHelper,
                                AMEJEIRecipeType.TRANSFORM,
                                AstralMekanismMachines.TRANSFORMER),
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
                                AstralMekanismMachines.GREEN_HOUSE),
                        new GasBurningRecipeCategory(guiHelper,
                                AMEJEIRecipeType.GAS_BURNING,
                                GeneratorsBlocks.GAS_BURNING_GENERATOR),
                        new ItemStackToItemStackRecipeCategory(guiHelper,
                                AMEJEIRecipeType.ITEM_COMPRESSING,
                                AstralMekanismMachines.ITEM_COMPRESSOR),
                        new ItemStackToItemStackRecipeCategory(guiHelper,
                                AMEJEIRecipeType.ITEM_UNZIPPING,
                                AstralMekanismMachines.ITEM_UNZIPPER),
                        new GasToGasRecipeCategory(guiHelper,
                                AMEJEIRecipeType.GAS_CONVERSION,
                                AstralMekanismMachines.GAS_CONVERTER),
                });
    }

    @Override
    public void registerRecipes(IRecipeRegistration registry) {
        RecipeRegistryHelper.register(registry, AMEJEIRecipeType.FLUID_INFUSER_RECIPE,
                AstralMekanismRecipeTypes.FLUID_INFUSER_RECIPE);
        RecipeRegistryHelper.register(registry, AMEJEIRecipeType.SPS_RECIPE,
                AstralMekanismRecipeTypes.SPS_RECIPE);
        RecipeRegistryHelper.register(registry, AMEJEIRecipeType.ASTRAL_CRAFTING,
                AstralMekanismRecipeTypes.ASTRAL_CRAFTING);
        RecipeRegistryHelper.register(registry, AMEJEIRecipeType.ITEM_COMPRESSING,
                AstralMekanismRecipeTypes.ITEM_COMPRESSING);
        RecipeRegistryHelper.register(registry, AMEJEIRecipeType.ITEM_UNZIPPING,
                AstralMekanismRecipeTypes.ITEM_UNZIPPING);
        RecipeRegistryHelper.register(registry, AMEJEIRecipeType.MEKANICAL_TRANSFORM,
                AstralMekanismRecipeTypes.MEKANICAL_TRAMSFORM);
        RecipeRegistryHelper.register(registry, AMEJEIRecipeType.INFUSING_CONDENSE,
                AstralMekanismRecipeTypes.INFUSING_CONDENSE);
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
                AstralMekanismRecipeTypes.GAS_CONVERSION);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registry) {
        // ae2
        registry.addRecipeCatalysts(ChargerCategory.RECIPE_TYPE,
                AstralMekanismMachines.MEKANICAL_CHARGER, AstralMekanismMachines.ASTRAL_MEKANICAL_CHARGER);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.INSCRIBE,
                AstralMekanismMachines.MEKANICAL_INSCRIBER,
                AstralMekanismMachines.ASTRAL_MEKANICAL_INSCRIBER);
        registry.addRecipeCatalysts(TransformCategory.RECIPE_TYPE,
                AstralMekanismMachines.TRANSFORMER, AstralMekanismMachines.ASTRAL_TRANSFORMER);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.TRANSFORM,
                AstralMekanismMachines.TRANSFORMER, AstralMekanismMachines.ASTRAL_TRANSFORMER);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.MATTER_CONDENSER,
                AstralMekanismMachines.MEKANICAL_MATTER_CONDENSER);
        // advanced ae
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.AAE_REACTION,
                AstralMekanismMachines.ESSENTIAL_REACTION_CHAMBER, AstralMekanismMachines.ASTRAL_REACTION_CHAMBER);
        // mekanism
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.INJECTING,
                AstralMekanismMachines.ASTRAL_CHEMICAL_INJECTION_CHAMBER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.COMPRESSING,
                AstralMekanismMachines.ASTRAL_OSMIUM_COMPRESSOR);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.PURIFYING,
                AstralMekanismMachines.ASTRAL_PURIFICATION_CHAMBER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.CRUSHING,
                AstralMekanismMachines.ASTRAL_CRUSHER);
        CatalystRegistryHelper.registerRecipeItem(registry, AstralMekanismMachines.ASTRAL_ENERGIZED_SMELTER,
                MekanismJEIRecipeType.SMELTING, RecipeTypes.SMELTING);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.SMELTING,
                AstralMekanismMachines.ENERGIZED_SMELTING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.SMELTING,
                AstralMekanismMachines.ASTRAL_ENERGIZED_SMELTING_FACTRIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.ENRICHING,
                AstralMekanismMachines.ASTRAL_ENRICHMENT_CHAMBER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.NUCLEOSYNTHESIZING,
                AstralMekanismMachines.ASTRAL_ANTIPROTONIC_NUCLEOSYNTHESIZER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.CHEMICAL_INFUSING,
                AstralMekanismMachines.ASTRAL_CHEMICAL_INFUSER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.OXIDIZING,
                AstralMekanismMachines.ASTRAL_CHEMICAL_OXIDIZER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.WASHING,
                AstralMekanismMachines.ASTRAL_CHEMICAL_WASHER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.COMBINING,
                AstralMekanismMachines.ASTRAL_COMBINER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.CRYSTALLIZING,
                AstralMekanismMachines.ASTRAL_CRYSTALLIZER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.DISSOLUTION,
                AstralMekanismMachines.ASTRAL_DISSOLUTION_CHAMBER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.SEPARATING,
                AstralMekanismMachines.ASTRAL_ELECTROLYTIC_SEPARATOR);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.ACTIVATING,
                AstralMekanismMachines.GLOWSTONE_NEUTRON_ACTIVATOR,
                AstralMekanismMachines.ASTRAL_GNA);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.CENTRIFUGING,
                AstralMekanismMachines.ASTRAL_ISOTOPIC_CENTRIFUGE);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.METALLURGIC_INFUSING,
                AstralMekanismMachines.ASTRAL_METALLURGIC_INFUSER,
                AstralMekanismMachines.ESSENTIAL_METALLURGIC_INFUSER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.REACTION,
                AstralMekanismMachines.ASTRAL_PRC);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.SAWING,
                AstralMekanismMachines.ASTRAL_PRECISION_SAWMILL);
        CatalystRegistryHelper.registerRecipeItem(registry, AstralMekanismMachines.ASTRAL_ROTARY_CONDENSENTRATOR,
                MekanismJEIRecipeType.CONDENSENTRATING, MekanismJEIRecipeType.DECONDENSENTRATING);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.SPS,
                AstralMekanismMachines.COMPACT_SPS,
                AstralMekanismMachines.ASTRAL_SPS);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.INFUSION_CONVERSION,
                AstralMekanismMachines.INFUSE_SYNTHESIZER);
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.EVAPORATING,
                AstralMekanismMachines.COMPACT_TEP.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, GeneratorsJEIRecipeType.FISSION,
                AstralMekanismMachines.COMPACT_FIR.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, MekanismJEIRecipeType.GAS_CONVERSION,
                AstralMekanismMachines.GAS_SYNTHESIZER);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.FUSION_REACTOR,
                AstralMekanismMachines.COMPACT_FUSION_REACTOR.values().toArray(IItemProvider[]::new));
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
                AstralMekanismMachines.GAS_BURNING_GENERATORS.values().toArray(IItemProvider[]::new));

        // Evolved Mekanism
        CatalystRegistryHelper.register(registry, EMJEI.APT,
                AstralMekanismMachines.COMPACT_APT,
                AstralMekanismMachines.ASTRAL_APT);
        CatalystRegistryHelper.register(registry, EMJEI.ALLOYING, AstralMekanismMachines.ASTRAL_ALLOYER);
        CatalystRegistryHelper.register(registry, EMJEI.CHEMIXING, AstralMekanismMachines.ASTRAL_CHEMIXER);
        CatalystRegistryHelper.register(registry, EMJEI.MELTING, AstralMekanismMachines.ASTRAL_THERMALIZER);
        CatalystRegistryHelper.register(registry, EMJEI.SOLIDIFICATION,
                AstralMekanismMachines.ASTRAL_SOLIDIFICATION_CHAMBER);

        // mekanism extras
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.NAQUADAH_REACTOR,
                AstralMekanismMachines.COMPACT_NAQUADAH_REACTOR.values().toArray(IItemProvider[]::new));
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
                AstralMekanismMachines.ASTRAL_RADIATION_IRRADIATOR);

        // astral_mekanism
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.ASTRAL_CRAFTING,
                AstralMekanismMachines.ASTRAL_CRAFTER);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.FLUID_INFUSER_RECIPE,
                AstralMekanismMachines.FLUID_INFUSER,
                AstralMekanismMachines.ASTRAL_FLUID_INFUSER);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.ITEM_COMPRESSING,
                AstralMekanismMachines.ITEM_COMPRESSOR);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.ITEM_UNZIPPING,
                AstralMekanismMachines.ITEM_UNZIPPER);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.MEKANICAL_TRANSFORM,
                AstralMekanismMachines.TRANSFORMER,
                AstralMekanismMachines.ASTRAL_TRANSFORMER);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.ESSENTIAL_SMELTING,
                AstralMekanismMachines.ESSENTIAL_ENERGIZED_SMELTER,
                AstralMekanismMachines.ASTRAL_ENERGIZED_SMELTER);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.ESSENTIAL_SMELTING,
                AstralMekanismMachines.ENERGIZED_SMELTING_FACTORIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.ESSENTIAL_SMELTING,
                AstralMekanismMachines.ASTRAL_ENERGIZED_SMELTING_FACTRIES.values().toArray(IItemProvider[]::new));
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.CROP_SOIL,
                AstralMekanismMachines.GREEN_HOUSE, AstralMekanismMachines.ASTRAL_GREEN_HOUSE);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.MEKANICAL_COMPOSTER,
                AstralMekanismMachines.MEKANICAL_COMPOSTER, AstralMekanismMachines.ASTRAL_COMPOSTER);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.INFUSING_CONDENSE,
                AstralMekanismMachines.INFUSING_CONDENSENTRATOR);
        CatalystRegistryHelper.register(registry, AMEJEIRecipeType.GAS_CONVERSION,
                AstralMekanismMachines.GAS_CONVERTER);

        // minecraft
        registry.addRecipeCatalysts(RecipeTypes.SMELTING,
                AstralMekanismMachines.ENERGIZED_SMELTING_FACTORIES.values().toArray(ItemLike[]::new));
        registry.addRecipeCatalysts(RecipeTypes.SMELTING,
                AstralMekanismMachines.ASTRAL_ENERGIZED_SMELTING_FACTRIES.values().toArray(ItemLike[]::new));
        CatalystRegistryHelper.registerRecipeItem(registry, AstralMekanismMachines.ASTRAL_FORMULAIC_ASSEMBLICATOR,
                MekanismJEIRecipeType.VANILLA_CRAFTING, RecipeTypes.CRAFTING);
        registry.addRecipeCatalysts(RecipeTypes.COMPOSTING,
                AstralMekanismMachines.MEKANICAL_COMPOSTER, AstralMekanismMachines.ASTRAL_COMPOSTER);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(new AstralFormulaicAssemblicatorTransferHandler(), RecipeTypes.CRAFTING);
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
