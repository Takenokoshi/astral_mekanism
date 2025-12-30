package astral_mekanism.block.block;

import org.jetbrains.annotations.NotNull;

import mekanism.api.text.ILangEntry;
import mekanism.common.block.interfaces.IHasDescription;
import net.minecraft.world.level.block.Block;

public class BlockSimpleDiscription extends Block implements IHasDescription {

    private final ILangEntry entry;

    public BlockSimpleDiscription(Properties properties, ILangEntry entry) {
        super(properties);
        this.entry = entry;
    }

    @Override
    public @NotNull ILangEntry getDescription() {
        return entry;
    }

}
