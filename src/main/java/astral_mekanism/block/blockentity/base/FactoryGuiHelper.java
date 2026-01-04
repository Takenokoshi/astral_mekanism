package astral_mekanism.block.blockentity.base;

public class FactoryGuiHelper {

    public static int getALLWidth(AstralMekanismFactoryTier tier, int widthPerProcess,
            int sideSpaceWidth) {
        return Math.max(widthPerProcess * tier.horizontalProcesses + sideSpaceWidth * 2, 176);
    }

    public static int getALLHeight(AstralMekanismFactoryTier tier, int heightPerProcess) {
        return Math.max((heightPerProcess + 18) * tier.verticalProcesses + 94, 166);
    }

    public static int getXofOneLine(int index, AstralMekanismFactoryTier tier, int widthPerProcess,
            int sideSpaceWidth) {
        return sideSpaceWidth
                + (widthPerProcess + (getALLWidth(tier, widthPerProcess, sideSpaceWidth) - sideSpaceWidth * 2
                        - widthPerProcess * tier.horizontalProcesses) / (tier.horizontalProcesses - 1))
                        * (index % tier.horizontalProcesses);
    }

    public static int getYofOneLine(int index, AstralMekanismFactoryTier tier, int heightPerProcess) {
        if (tier.verticalProcesses == 1) {
            return 18 + (getALLHeight(tier, heightPerProcess) - 112 - heightPerProcess) / 2;
        } else {
            return 18 + (heightPerProcess
                    + ((getALLHeight(tier, heightPerProcess) - 112 - heightPerProcess * tier.verticalProcesses)
                            / (tier.verticalProcesses - 1)))
                    * (index / tier.horizontalProcesses);
        }
    }
}
