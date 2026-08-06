package astral_mekanism.block.block;

import astral_mekanism.AMETier;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.states.BlockStateHelper;
import mekanism.common.registration.impl.BlockRegistryObject;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class AMEAttributeUpgradeable implements Attribute {

    private final Supplier<BlockRegistryObject<?, ?>> upgradeBlock;

    public AMEAttributeUpgradeable(Supplier<BlockRegistryObject<?, ?>> upgradeBlock) {
        this.upgradeBlock = upgradeBlock;
    }

    @NotNull
    public BlockState upgradeResult(@NotNull BlockState current, @NotNull AMETier tier) {
        return BlockStateHelper.copyStateData(current, upgradeBlock.get());
    }
}
