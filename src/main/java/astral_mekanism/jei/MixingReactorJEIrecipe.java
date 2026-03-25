package astral_mekanism.jei;

import java.util.List;

import com.jerry.generator_extras.common.genregistry.ExtraGenGases;
import com.jerry.mekanism_extras.common.registry.ExtraGases;

import mekanism.api.chemical.gas.GasStack;
import mekanism.common.registries.MekanismGases;
import mekanism.generators.common.registries.GeneratorsGases;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;

public record MixingReactorJEIrecipe(GasStack leftFuel, GasStack rightFuel, GasStack mixedFuel,
        FluidStack water, GasStack steam, String requierdTemp) {

    public static final List<MixingReactorJEIrecipe> fusionRecipes = List.of(
            new MixingReactorJEIrecipe(
                    GeneratorsGases.DEUTERIUM.getStack(1),
                    GeneratorsGases.TRITIUM.getStack(1),
                    GeneratorsGases.FUSION_FUEL.getStack(2),
                    new FluidStack(Fluids.WATER, 2),
                    MekanismGases.STEAM.getStack(2),
                    "100MK"));

    public static final List<MixingReactorJEIrecipe> naquadahRecipes = List.of(
            new MixingReactorJEIrecipe(
                    ExtraGases.RICH_NAQUADAH_FUEL.getStack(1),
                    ExtraGases.RICH_URANIUM_FUEL.getStack(1),
                    ExtraGases.NAQUADAH_URANIUM_FUEL.getStack(2),
                    new FluidStack(Fluids.WATER, 2),
                    ExtraGenGases.POLONIUM_CONTAINING_STEAM.getStack(2),
                    "400MK"));
}
