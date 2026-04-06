package astral_mekanism.block.gui.element;

import java.util.List;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import mekanism.api.chemical.gas.IGasTank;
import mekanism.client.gui.IGuiWrapper;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.client.jei.interfaces.IJEIRecipeArea;

public class GuiJEIRecipeAreaGasGauge extends GuiGasGauge implements IJEIRecipeArea<GuiJEIRecipeAreaGasGauge> {

    private MekanismJEIRecipeType<?>[] recipeCategories;
    public GuiJEIRecipeAreaGasGauge(Supplier<IGasTank> tankSupplier, Supplier<List<IGasTank>> tanksSupplier,
            GaugeType type, IGuiWrapper gui, int x, int y) {
        super(tankSupplier, tanksSupplier, type, gui, x, y);
    }

    @Override
    public @Nullable MekanismJEIRecipeType<?>[] getRecipeCategories() {
        return recipeCategories;
    }

    @Override
    public GuiJEIRecipeAreaGasGauge jeiCategories(@NotNull MekanismJEIRecipeType<?>... recipeCategories) {
        this.recipeCategories = recipeCategories;
        return this;
    }
    
}
