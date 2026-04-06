package astral_mekanism.jei.jeirecipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import mekanism.api.MekanismAPI;
import mekanism.api.chemical.gas.GasStack;
import mekanism.api.chemical.gas.attribute.GasAttributes.Fuel;
import mekanism.api.math.FloatingLong;

public record GasBurningJEIRecipe(GasStack gasStack, FloatingLong value) {
    public static List<GasBurningJEIRecipe> getRecipes() {
        List<GasBurningJEIRecipe> result = new ArrayList<>();
        MekanismAPI.gasRegistry().forEach(gas -> {
            gas.ifAttributePresent(Fuel.class, fuel -> {
                result.add(new GasBurningJEIRecipe(gas.getStack(1),
                        fuel.getEnergyPerTick().multiply(fuel.getBurnTicks())));
            });
        });
        return Collections.unmodifiableList(result);
    }
}
