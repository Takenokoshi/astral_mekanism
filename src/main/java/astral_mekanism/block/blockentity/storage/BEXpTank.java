package astral_mekanism.block.blockentity.storage;

import astral_mekanism.registries.AMEInfuseTypes;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.IContentsListener;
import mekanism.api.RelativeSide;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.infuse.IInfusionTank;
import mekanism.api.chemical.infuse.InfuseType;
import mekanism.api.chemical.infuse.InfusionStack;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.capabilities.holder.chemical.ChemicalTankHelper;
import mekanism.common.capabilities.holder.chemical.IChemicalTankHolder;
import mekanism.common.lib.transmitter.TransmissionType;
import mekanism.common.tile.component.TileComponentConfig;
import mekanism.common.tile.component.TileComponentEjector;
import mekanism.common.tile.prefab.TileEntityConfigurableMachine;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.state.BlockState;

public class BEXpTank extends TileEntityConfigurableMachine {

    private IInfusionTank infusionTank;

    public BEXpTank(IBlockProvider blockProvider, BlockPos pos, BlockState state) {
        super(blockProvider, pos, state);
        configComponent = new TileComponentConfig(this, TransmissionType.INFUSION);
        configComponent.setupIOConfig(TransmissionType.INFUSION, infusionTank, infusionTank, RelativeSide.RIGHT, true);
        ejectorComponent = new TileComponentEjector(this, () -> Long.MAX_VALUE)
                .setOutputData(configComponent, TransmissionType.INFUSION);
    }

    @Override
    public IChemicalTankHolder<InfuseType, InfusionStack, IInfusionTank> getInitialInfusionTanks(
            IContentsListener listener) {
        ChemicalTankHelper<InfuseType, InfusionStack, IInfusionTank> builder = ChemicalTankHelper
                .forSideInfusionWithConfig(this::getDirection, this::getConfig);
        builder.addTank(infusionTank = ChemicalTankBuilder.INFUSION.create(Long.MAX_VALUE,
                type -> AMEInfuseTypes.XP.getStack(1).isTypeEqual(type), listener));
        return builder.build();
    }

    // note:xp(infuseType) 100mB = 1 vanilla xp.
    public void giveXp(ServerPlayer player, int value) {
        if (value < 0) {
            value *= -1;
        }
        long playerXp = getTotalXpOfPlayer(player);
        long amount = Math.min(Math.min(getXpToIncreaseLevels(player, value), infusionTank.getStored() / 100),
                Long.MAX_VALUE - playerXp);
        setTotalXpOfPlayer(player, amount + playerXp);
        infusionTank.shrinkStack(amount * 100, Action.EXECUTE);
        markForSave();
    }

    public void receiveXP(ServerPlayer player, int value) {
        if (value < 0) {
            value *= -1;
        }
        long playerXp = getTotalXpOfPlayer(player);
        long amount = Math.min(Math.min(getXpToDecreaseLevels(player, value), infusionTank.getNeeded() / 100),
                playerXp);
        setTotalXpOfPlayer(player, playerXp - amount);
        infusionTank.insert(new InfusionStack(AMEInfuseTypes.XP, amount * 100), Action.EXECUTE,
                AutomationType.MANUAL);
        markForSave();
    }

    public IInfusionTank getInfusionTank() {
        return infusionTank;
    }

    private static int getXpNeededForNextLevel(int level) {
        if (level >= 31) {
            return 9 * level - 158;
        } else if (level >= 16) {
            return 5 * level - 38;
        } else {
            return 2 * level + 7;
        }
    }

    private static long getXpToIncreaseLevels(ServerPlayer player, int deltaLevel) {
        int currentLevel = player.experienceLevel;
        float currentProgress = player.experienceProgress;
        long totalXp = (long) (-getXpNeededForNextLevel(currentLevel) * currentProgress);
        for (int level = currentLevel; level < currentLevel + deltaLevel; level++) {
            totalXp += getXpNeededForNextLevel(level);
        }
        return totalXp;
    }

    private static long getXpToDecreaseLevels(ServerPlayer player, int deltaLevel) {
        int currentLevel = player.experienceLevel;
        float currentProgress = player.experienceProgress;
        deltaLevel = Math.min(deltaLevel, currentLevel);
        long totalXp = (long) (getXpNeededForNextLevel(currentLevel) * currentProgress);
        for (int level = currentLevel - deltaLevel; level < currentLevel; level++) {
            totalXp += getXpNeededForNextLevel(level);
        }
        return totalXp;
    }

    private static long getTotalXpOfPlayer(ServerPlayer player) {
        int level = player.experienceLevel;
        float progress = player.experienceProgress;
        long totalXp = (long) (getXpNeededForNextLevel(level) * progress);
        for (int i = 0; i < level; i++) {
            totalXp += getXpNeededForNextLevel(i);
        }
        return totalXp;
    }

    private static void setTotalXpOfPlayer(ServerPlayer player, long totalXp) {
        int level = 0;
        long xp = 0;
        while (xp <= totalXp) {
            xp += getXpNeededForNextLevel(level);
            level++;
        }
        level--;
        xp -= getXpNeededForNextLevel(level);
        player.experienceLevel = level;
        player.experienceProgress = Math.min((float) (totalXp - xp) / getXpNeededForNextLevel(level), 0.999999f);
        player.totalExperience = (int) Math.min(totalXp, 0x7fffffff);
    }

}
