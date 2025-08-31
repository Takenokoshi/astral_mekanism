package astral_mekanism.core;

import java.util.List;
import java.util.Set;

import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.inventory.IInventorySlot;
import mekanism.common.lib.inventory.TileTransitRequest;
import mekanism.common.lib.inventory.TransitRequest.TransitResponse;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.TileEntityBin;
import mekanism.common.tile.component.config.ConfigInfo;
import mekanism.common.tile.component.config.DataType;
import mekanism.common.tile.component.config.slot.ISlotInfo;
import mekanism.common.tile.component.config.slot.InventorySlotInfo;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import mekanism.common.tile.transmitter.TileEntityLogisticalTransporterBase;
import mekanism.common.util.InventoryUtils;
import mekanism.common.util.WorldUtils;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;

public class BlockEntityUtils {

	private static class EjectTransitRequest extends TileTransitRequest {

		public Direction side;

		public EjectTransitRequest(BlockEntity tile, Direction side) {
			super(tile, side);
			this.side = side;
		}

		@Override
		public Direction getSide() {
			return side;
		}
	}

	/**
	 * アイテムを搬出する
	 *
	 * @param tile       対象の TileEntity
	 * @param dataTypes  搬出先として設定された DataType のリスト
	 * @param outputData 実際に搬出するスロットを持つ DataType
	 */
	public static void itemEject(TileEntityConfigurableMachine tile,
			List<DataType> dataTypes, DataType outputData) {
		ConfigInfo itemConfig = tile.configComponent.getConfig(TransmissionType.ITEM);
		if (itemConfig == null || !tile.ejectorComponent.isEjecting(itemConfig, TransmissionType.ITEM)) {
			return;
		}
		ISlotInfo slotInfo = itemConfig.getSlotInfo(outputData);
		for (DataType dataType : dataTypes) {
			Set<Direction> outputs = itemConfig.getSidesForData(dataType);
			if (slotInfo instanceof InventorySlotInfo inventorySlotInfo && !outputs.isEmpty()) {
				for (Direction side : outputs) {
					List<IInventorySlot> slots = inventorySlotInfo.getSlots();
					EjectTransitRequest sideEject = InventoryUtils.getEjectItemMap(
							new EjectTransitRequest(tile, side), slots);
					BlockEntity target = WorldUtils.getTileEntity(tile.getLevel(),
							tile.getBlockPos().relative(side));
					if (target != null && !sideEject.isEmpty()) {
						TransitResponse response;
						if (target instanceof TileEntityLogisticalTransporterBase transporter) {
							response = transporter.getTransmitter().insert(tile, sideEject,
									tile.ejectorComponent.getOutputColor(), true, 0);
						} else {
							response = sideEject.addToInventory(target, side, 0, false);
						}
						if (!response.isEmpty()) {
							removeItem(slots, response.getStack(), response.getSendingAmount());
						}
					}
				}
			}
		}
	}

	public static void removeItem(List<IInventorySlot> slots, ItemStack item, int amount) {
		for (int i = slots.size() - 1; i >= 0; i--) {
			IInventorySlot slot = slots.get(i);
			if (ItemStack.isSameItemSameTags(slot.getStack(), item)) {
				amount -= slot.shrinkStack(amount, Action.EXECUTE);
				if (amount <= 0) {
					break;
				}
			}
		}
	}

	public static void itemInsert(TileEntityConfigurableMachine tile,
			List<DataType> dataTypes) {
		ConfigInfo itemConfig = tile.configComponent.getConfig(TransmissionType.ITEM);
		if (itemConfig == null) {
			return;
		}
		for (DataType dataType : dataTypes) {
			ISlotInfo slotInfo = itemConfig.getSlotInfo(dataType);
			if (slotInfo instanceof InventorySlotInfo inventorySlotInfo) {
				List<IInventorySlot> slots = inventorySlotInfo.getSlots();
				Set<Direction> inputDirections = itemConfig.getSidesForData(dataType);
				for (Direction inputDirection : inputDirections) {
					BlockEntity from = WorldUtils.getTileEntity(tile.getLevel(),
							tile.getBlockPos().relative(inputDirection));
					if (from == null) {
					} else if (from instanceof RandomizableContainerBlockEntity RCBE) {
						for (int i = 0; i < RCBE.getContainerSize(); i++) {
							ItemStack moving = RCBE.getItem(i).copy();
							if (!moving.isEmpty()) {
								RCBE.setItem(i, insertItem(slots, moving));
								break;
							}
						}
					} else if (from instanceof AbstractFurnaceBlockEntity AFBE) {
						ItemStack furnaceOutput = AFBE.getItem(2).copy();
						AFBE.setItem(2, insertItem(slots, furnaceOutput));
						ItemStack fuelSlotItem = AFBE.getItem(1);
						if (!AbstractFurnaceBlockEntity.isFuel(fuelSlotItem)) {
							AFBE.setItem(1, insertItem(slots, fuelSlotItem));
						}
					} else if (from instanceof TileEntityBin bin) {
						ItemStack extract = bin.extractItem(0, 64, inputDirection.getOpposite(), Action.EXECUTE);
						bin.insertItem(0, insertItem(slots, extract), false);
					}
				}
			}
		}
	}

	public static ItemStack insertItem(List<IInventorySlot> slots, ItemStack item) {
		if (item.isEmpty()) {
			return ItemStack.EMPTY;
		}
		ItemStack remaining = item.copy();
		for (IInventorySlot slot : slots) {
			if (remaining.isEmpty()) {
				break;
			}
			remaining = slot.insertItem(remaining, Action.EXECUTE, AutomationType.INTERNAL);
		}
		return remaining;
	}
}
