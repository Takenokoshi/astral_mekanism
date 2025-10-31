package astral_mekanism.jei.recipeCategory;

import java.util.List;
import java.util.stream.Collectors;

import astral_mekanism.recipes.recipe.ExpandedCrafterRecipe;
import mekanism.api.chemical.gas.Gas;
import mekanism.api.providers.IItemProvider;
import mekanism.client.gui.element.bar.GuiVerticalPowerBar;
import mekanism.client.gui.element.gauge.GaugeType;
import mekanism.client.gui.element.gauge.GuiFluidGauge;
import mekanism.client.gui.element.gauge.GuiGasGauge;
import mekanism.client.gui.element.gauge.GuiGauge;
import mekanism.client.gui.element.slot.GuiSlot;
import mekanism.client.gui.element.slot.SlotType;
import mekanism.client.jei.BaseRecipeCategory;
import mekanism.client.jei.MekanismJEI;
import mekanism.client.jei.MekanismJEIRecipeType;
import mekanism.common.inventory.container.slot.SlotOverlay;
import mekanism.common.tile.component.config.DataType;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ExpandedCrafterRecipeCategory extends BaseRecipeCategory<ExpandedCrafterRecipe> {

    private final GuiSlot[] inputItems;
    private final GuiGauge<FluidStack> inputFluid;
    private final GuiGauge<Gas> inputGas;
    private final GuiSlot output;

    public ExpandedCrafterRecipeCategory(IGuiHelper helper,
            MekanismJEIRecipeType<ExpandedCrafterRecipe> recipeType, IItemProvider provider) {
        super(helper, recipeType, provider, 3, 10, 194, 108);
        inputItems = new GuiSlot[25];
        for (int i = 0; i < 25; i++) {
            inputItems[i] = addSlot(SlotType.INPUT, 44 + (i % 5) * 18, 18 + (i / 5) * 18);
        }
        inputFluid = this.addElement(GuiFluidGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT),
                this, 7, 17));
        inputGas = this.addElement(GuiGasGauge.getDummy(GaugeType.STANDARD.with(DataType.INPUT),
                this, 25, 27));
        output = this.addSlot(SlotType.OUTPUT, 170, 54);
        this.addElement(new GuiVerticalPowerBar(this, FULL_BAR, 190, 17));
        addSlot(SlotType.POWER, 174, 18).with(SlotOverlay.POWER);
        addSlot(SlotType.INPUT, 8, 76).with(SlotOverlay.INPUT);
        addSlot(SlotType.INPUT, 26, 76).with(SlotOverlay.INPUT);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ExpandedCrafterRecipe recipe, IFocusGroup focusGroup) {
        initArrayItem(builder, RecipeIngredientRole.INPUT, inputItems, recipe.getInputItems().getRepresentations());
        initFluid(builder, RecipeIngredientRole.INPUT, inputFluid, recipe.getInputFluid().getRepresentations());
        initChemical(builder, MekanismJEI.TYPE_GAS, RecipeIngredientRole.INPUT, inputGas,
                recipe.getInputGas().getRepresentations());
        initItem(builder, RecipeIngredientRole.OUTPUT, output, recipe.getOutputDefinition());
    }

    private IRecipeSlotBuilder[] initArrayItem(IRecipeLayoutBuilder builder,
            RecipeIngredientRole role, GuiSlot[] slots, List<ItemStack[]> stacksList) {
        IRecipeSlotBuilder[] result = new IRecipeSlotBuilder[25];
        for (int i : ints) {
            result[i] = initItem(builder, role, slots[i],
                    stacksList.stream().map(p -> p[i]).collect(Collectors.toList()));
        }
        return result;
    }

    private static final int[] ints = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19,
            20, 21, 22, 23, 24 };

}
