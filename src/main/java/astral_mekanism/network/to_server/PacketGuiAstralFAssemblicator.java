package astral_mekanism.network.to_server;

import astral_mekanism.block.blockentity.interf.IAMEFormulaicAssemblicator;
import mekanism.common.network.IMekanismPacket;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent.Context;

public class PacketGuiAstralFAssemblicator implements IMekanismPacket {

    private final BlockPos pos;
    private final ResourceLocation id;

    public PacketGuiAstralFAssemblicator(BlockPos pos, ResourceLocation id) {
        this.pos = pos;
        this.id = id;

    }

    @Override
    public void handle(Context context) {
        Player player = context.getSender();
        if (player != null) {
            Level world = player.level();
            Recipe<?> recipe = world.getRecipeManager().byKey(id).orElse(null);
            if (WorldUtils.getTileEntity(world, pos) instanceof IAMEFormulaicAssemblicator assemblicator
                    && recipe != null && recipe instanceof CraftingRecipe craftingRecipe) {
                assemblicator.setSavedRecipe(craftingRecipe);
            }
        }
    }

    @Override
    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(pos);
        buffer.writeResourceLocation(id);
    }

    public static PacketGuiAstralFAssemblicator decode(FriendlyByteBuf buf) {
        return new PacketGuiAstralFAssemblicator(buf.readBlockPos(), buf.readResourceLocation());
    }

}
