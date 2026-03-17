package astral_mekanism.block.blockentity.interf;

import appeng.helpers.IPriorityHost;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface AMEPriorityHost<BE extends BlockEntity & AMEPriorityHost<BE>> extends IPriorityHost {
}
