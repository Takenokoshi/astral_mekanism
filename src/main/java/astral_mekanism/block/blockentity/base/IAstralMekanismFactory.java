package astral_mekanism.block.blockentity.base;

public interface IAstralMekanismFactory<BE extends IAstralMekanismFactory<BE>> {

    public abstract BE getSelf();

    public abstract int getWidthPerProcess();

    public abstract int getHeightPerProcess();

    public abstract int getSideSpaceWidth();

    public default int getAdditionalPages() {
        return 0;
    };

    public abstract AstralMekanismFactoryTier getTier();

    private int getProcessPerPage() {
        int w = getWidthPerProcess() + 1;
        return 304 / w;
    }

    private int getBetweenSpaceWidth() {
        int p = getProcessPerPage();
        return (305 - getWidthPerProcess() * p) / (p + 1);
    }

    public default int getAllPages() {
        int p = getProcessPerPage();
        return (getTier().processes + p - 1) / p + getAdditionalPages();
    }

    public default int getPageByIndex(int index) {
        return index / getProcessPerPage();
    }

    public default int getXByIndex(int index) {
        return getSideSpaceWidth() + (getWidthPerProcess() + getBetweenSpaceWidth()) * (index % getProcessPerPage());
    }

    public default int getY() {
        int h = getHeightPerProcess();
        return h > 54 ? 18 : 18 + (54 - h) / 2;
    }

    public default int getPageWidth() {
        return 305 + getSideSpaceWidth() * 2;
    }

    public default int getPageHeight() {
        int h = getHeightPerProcess();
        return h > 54 ? 112 + h : 166;
    }

}
