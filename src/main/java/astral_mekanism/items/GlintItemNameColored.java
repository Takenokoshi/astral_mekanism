package astral_mekanism.items;

import java.util.function.Function;

import org.jetbrains.annotations.NotNull;

import mekanism.api.text.EnumColor;
import mekanism.api.text.TextComponentUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class GlintItemNameColored extends GlintItem {

    private final EnumColor color;

    public GlintItemNameColored(Properties properties, EnumColor color) {
        super(properties);
        this.color = color;
    }

    public static Function<Properties, GlintItemNameColored> getSup(EnumColor color) {
        return p -> new GlintItemNameColored(p, color);
    }

    @NotNull
    @Override
    public Component getName(@NotNull ItemStack stack) {
        return TextComponentUtil.build(color, super.getName(stack));
    }

}
