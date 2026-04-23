package astral_mekanism.integration;

import astral_mekanism.AMEConstants;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;

public class AMEKubeJSPlugin extends KubeJSPlugin {
    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.register(AMEConstants.rl("fluid_infuser"), new RecipeSchema(
                new RecipeKey[] {
                        new RecipeKey<>(FluidComponents.INPUT, "fluidInputA"),
                        new RecipeKey<>(FluidComponents.INPUT, "fluidInputB"),
                        new RecipeKey<>(FluidComponents.OUTPUT, "fluidOutput"),
                }));
        event.register(AMEConstants.rl("mekanical_transform"), new RecipeSchema(
                new RecipeKey[] {
                        new RecipeKey<>(ItemComponents.INPUT, "itemInputA"),
                        new RecipeKey<>(ItemComponents.INPUT, "itemInputB"),
                        new RecipeKey<>(ItemComponents.INPUT, "itemInputC"),
                        new RecipeKey<>(FluidComponents.INPUT, "fluidInputA"),
                        new RecipeKey<>(FluidComponents.INPUT, "fluidInputB"),
                        new RecipeKey<>(ItemComponents.OUTPUT, "outputItem").allowEmpty(),
                        new RecipeKey<>(FluidComponents.OUTPUT, "fluidOutput").allowEmpty(),
                        new RecipeKey<>(BooleanComponent.BOOLEAN, "itemAIsCatalyst"),
                        new RecipeKey<>(BooleanComponent.BOOLEAN, "itemBIsCatalyst"),
                        new RecipeKey<>(BooleanComponent.BOOLEAN, "itemCIsCatalyst"),
                        new RecipeKey<>(BooleanComponent.BOOLEAN, "fluidAIsCatalyst"),
                        new RecipeKey<>(BooleanComponent.BOOLEAN, "fluidBIsCatalyst"),
                }));
    }
}
