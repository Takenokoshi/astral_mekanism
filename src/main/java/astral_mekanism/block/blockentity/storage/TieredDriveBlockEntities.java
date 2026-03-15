package astral_mekanism.block.blockentity.storage;

import java.util.EnumMap;

import appeng.blockentity.storage.DriveBlockEntity;
import appeng.menu.ISubMenu;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;
import astral_mekanism.AstralMekanismTier;
import astral_mekanism.registries.AstralMekanismMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TieredDriveBlockEntities {

    private static final EnumMap<AstralMekanismTier, TieredDriveBlockEntities> INSTANCES = new EnumMap<>(
            AstralMekanismTier.class);

    private static TieredDriveBlockEntities build(AstralMekanismTier tier) {
        INSTANCES.put(tier, new TieredDriveBlockEntities(tier));
        return INSTANCES.get(tier);
    }

    private static final TieredDriveBlockEntities LOGIC = build(AstralMekanismTier.ESSENTIAL);
    private static final TieredDriveBlockEntities CALCULATION = build(AstralMekanismTier.BASIC);
    private static final TieredDriveBlockEntities ENGINEERING = build(AstralMekanismTier.ADVANCED);
    private static final TieredDriveBlockEntities ACCUMULATION = build(AstralMekanismTier.ELITE);
    private static final TieredDriveBlockEntities PHOTON = build(AstralMekanismTier.ULTIMATE);
    private static final TieredDriveBlockEntities QUANTUM = build(AstralMekanismTier.ABSOLUTE);
    private static final TieredDriveBlockEntities COMPOSITE = build(AstralMekanismTier.SUPREME);
    private static final TieredDriveBlockEntities ORIGIN = build(AstralMekanismTier.COSMIC);
    private static final TieredDriveBlockEntities AUTONOMY = build(AstralMekanismTier.INFINITE);
    private static final TieredDriveBlockEntities FIRMAMENT = build(AstralMekanismTier.ASTRAL);

    public final AstralMekanismTier tier;

    private TieredDriveBlockEntities(AstralMekanismTier tier) {
        this.tier = tier;
    }

    public static TieredDriveBlockEntities get(AstralMekanismTier tier) {
        return INSTANCES.get(tier);
    }

    public TieredDriveBlockEntity create(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
        return new TieredDriveBlockEntity(blockEntityType, pos, blockState);
    }

    public class TieredDriveBlockEntity extends DriveBlockEntity {

        public TieredDriveBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
            super(blockEntityType, pos, blockState);
        }

        @Override
        public int getCellCount() {
            return tier.processes;
        }

        public AstralMekanismTier getTier() {
            return tier;
        }

        @Override
        public void openMenu(Player player) {
            MenuOpener.open(AstralMekanismMenus.DRIVES.get(tier).get(), player, MenuLocators.forBlockEntity(this));
        }

        @Override
        public void returnToMainMenu(Player player, ISubMenu subMenu) {
            MenuOpener.returnTo(AstralMekanismMenus.DRIVES.get(tier).get(), player, MenuLocators.forBlockEntity(this));
        }

    }
}
