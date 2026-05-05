package astral_mekanism.jei.transferHandler;

import java.util.Optional;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.AstralMekanism;
import astral_mekanism.block.blockentity.basemachine.BEAMEFormulaicAssemblicator;
import astral_mekanism.block.container.prefab.ContainerAMEFormulaicAssemblicator;
import astral_mekanism.network.to_server.PacketGuiAstralFAssemblicator;
import astral_mekanism.registration.MachineRegistryObject;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.crafting.CraftingRecipe;

public class AMEFormulaicAssemblicatorTransferHandler<BE extends BEAMEFormulaicAssemblicator>
        implements IRecipeTransferHandler<ContainerAMEFormulaicAssemblicator<BE>, CraftingRecipe> {

    private final MachineRegistryObject<BE, ?, ContainerAMEFormulaicAssemblicator<BE>, ?> machine;

    public AMEFormulaicAssemblicatorTransferHandler(
            MachineRegistryObject<BE, ?, ContainerAMEFormulaicAssemblicator<BE>, ?> machine) {
        this.machine = machine;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Class<ContainerAMEFormulaicAssemblicator<BE>> getContainerClass() {
        return (Class<ContainerAMEFormulaicAssemblicator<BE>>) (Object) ContainerAMEFormulaicAssemblicator.class;
    }

    @Override
    public Optional<MenuType<ContainerAMEFormulaicAssemblicator<BE>>> getMenuType() {
        return Optional.of(machine.getContainer().get());
    }

    @Override
    public RecipeType<CraftingRecipe> getRecipeType() {
        return RecipeTypes.CRAFTING;
    }

    @Override
    public @Nullable IRecipeTransferError transferRecipe(
            ContainerAMEFormulaicAssemblicator<BE> container,
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
