package astral_mekanism.jei.transferHandler;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.AstralMekanism;
import astral_mekanism.block.container.astralmachine.ContainerAstralFAssemblicator;
import astral_mekanism.network.to_server.PacketGuiAstralFAssemblicator;
import astral_mekanism.registries.AstralMekanismMachines;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.CraftingRecipe;

public class AstralFormulaicAssemblicatorTransferHandler
        implements IRecipeTransferHandler<ContainerAstralFAssemblicator, CraftingRecipe> {

    @Override
    public Class<? extends ContainerAstralFAssemblicator> getContainerClass() {
        return ContainerAstralFAssemblicator.class;
    }

    @Override
    public Optional<MenuType<ContainerAstralFAssemblicator>> getMenuType() {
        return Optional.of(AstralMekanismMachines.ASTRAL_FORMULAIC_ASSEMBLICATOR.getContainer().get());
    }

    @Override
    public RecipeType<CraftingRecipe> getRecipeType() {
        return RecipeTypes.CRAFTING;
    }

    @Override
    public @Nullable IRecipeTransferError transferRecipe(
            ContainerAstralFAssemblicator container,
            CraftingRecipe recipe,
            IRecipeSlotsView recipeSlots,
            Player player,
            boolean maxTransfer,
            boolean doTransfer) {
        if (doTransfer) {
            AstralMekanism.packetHandler().sendToServer(
                    new PacketGuiAstralFAssemblicator(container.getTileEntity().getBlockPos(), recipe.getId()));
        }
        return null;
    }

}
