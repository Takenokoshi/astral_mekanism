package astral_mekanism.blocks.universal_storage;

import org.jetbrains.annotations.NotNull;

import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import net.minecraft.world.entity.player.Inventory;

public class ContainerUniversalStorage extends MekanismTileContainer<BlockEntityUniversalStorage> {

	public ContainerUniversalStorage(ContainerTypeRegistryObject<ContainerUniversalStorage> type, int id, Inventory inv,
			@NotNull BlockEntityUniversalStorage tile) {
		super(type, id, inv, tile);
	}

	public ContainerUniversalStorage(int id, Inventory inv,
			@NotNull BlockEntityUniversalStorage tile) {
		super(DataCacheUniversalStorage.getContainerTypeRegistryObject(), id, inv, tile);
	}

	@Override
	protected int getInventoryYOffset() {
		return 192;
	}

	@Override
	protected int getInventoryXOffset() {
		return 71;
	}

}
