package astral_mekanism.block.blockitem;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import mekanism.api.text.EnumColor;
import mekanism.api.text.TextComponentUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class GlintBlockItem extends BlockItem {

    private final EnumColor color;

    public GlintBlockItem(Block block, Properties p) {
        super(block, p);
        color = EnumColor.WHITE;
    }

    public GlintBlockItem(Block b, Properties p, EnumColor c) {
        super(b, p);
        color = c;
    }

    public static Function<Block, GlintBlockItem> factory(EnumColor color) {
        return (b) -> new GlintBlockItem(b, new Properties(), color);
    }

    @NotNull
    @Override
    public Component getName(@NotNull ItemStack stack) {
        return TextComponentUtil.build(color, super.getName(stack));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

}
