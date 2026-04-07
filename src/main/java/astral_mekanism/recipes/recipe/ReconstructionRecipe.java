package astral_mekanism.recipes.recipe;

import mekanism.api.chemical.gas.GasStack;
import mekanism.api.math.FloatingLong;
import mekanism.api.recipes.PressurizedReactionRecipe;
import mekanism.api.recipes.ingredients.ChemicalStackIngredient.GasStackIngredient;
import mekanism.api.recipes.ingredients.FluidStackIngredient;
import mekanism.api.recipes.ingredients.ItemStackIngredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public abstract class ReconstructionRecipe extends PressurizedReactionRecipe {

    private boolean itemNotConsumed;

    protected ReconstructionRecipe(ResourceLocation id, ItemStackIngredient inputSolid, FluidStackIngredient inputFluid,
            GasStackIngredient inputGas, FloatingLong energyRequired, int duration, ItemStack outputItem,GasStack outputGas) {
        super(id, inputSolid, inputFluid, inputGas, energyRequired, duration, outputItem, outputGas);
        this.itemNotConsumed=false;
    }

    public boolean getItemNotConsumed(){
        return itemNotConsumed;
    }

    public void setItemNotConsumed(boolean value){
        this.itemNotConsumed = value;
    }

    @Override
    public void write(FriendlyByteBuf buffer){
        super.write(buffer);
        buffer.writeBoolean(itemNotConsumed);
    }
    
}
