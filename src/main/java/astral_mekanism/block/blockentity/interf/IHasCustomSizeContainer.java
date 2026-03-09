package astral_mekanism.block.blockentity.interf;

public interface IHasCustomSizeContainer {
    default int getInventoryXOffset() {
        return 8;
    }

    default int getInventoryYOffset() {
        return 84;
    }
}
