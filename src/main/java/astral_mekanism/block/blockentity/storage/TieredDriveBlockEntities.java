package astral_mekanism.block.blockentity.storage;

import appeng.blockentity.storage.DriveBlockEntity;
import appeng.menu.ISubMenu;
import appeng.menu.MenuOpener;
import appeng.menu.locator.MenuLocators;
import astral_mekanism.AstralMekanismTier;
import astral_mekanism.AstralMekanismTier.AstralMekanismTierMap;
import astral_mekanism.registries.AstralMekanismMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class TieredDriveBlockEntities {

    private static final AstralMekanismTierMap<TieredDriveBlockEntities> INSTANCES = new AstralMekanismTierMap<TieredDriveBlockEntities>(TieredDriveBlockEntities::new);

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

        private TieredDriveBlockEntity(BlockEntityType<?> blockEntityType, BlockPos pos, BlockState blockState) {
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
