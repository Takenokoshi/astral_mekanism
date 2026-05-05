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

public class AMEFormulaicAssemblicatorTransferHandler<BE extends BEAMEFormulaicAssemblicator, CONTAINER extends ContainerAMEFormulaicAssemblicator<BE>>
        implements IRecipeTransferHandler<CONTAINER, CraftingRecipe> {

    private final MachineRegistryObject<BE, ?, CONTAINER, ?> machine;
    private final Class<CONTAINER> clazz;

    public AMEFormulaicAssemblicatorTransferHandler(
            MachineRegistryObject<BE, ?, CONTAINER, ?> machine, Class<CONTAINER> clazz) {
        this.machine = machine;
        this.clazz = clazz;
    }

    @Override
    public Class<CONTAINER> getContainerClass() {
        return clazz;
    }

    @Override
    public Optional<MenuType<CONTAINER>> getMenuType() {
        return Optional.of(machine.getContainer().get());
    }

    @Override
    public RecipeType<CraftingRecipe> getRecipeType() {
        return RecipeTypes.CRAFTING;
    }

    @Override
    public @Nullable IRecipeTransferError transferRecipe(
            CONTAINER container,
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
