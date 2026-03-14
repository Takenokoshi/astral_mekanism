package astral_mekanism.registries;

import java.util.EnumMap;
import java.util.function.Supplier;

import appeng.menu.AEBaseMenu;
import appeng.menu.implementations.MenuTypeBuilder;
import astral_mekanism.AstralMekanismID;
import astral_mekanism.AstralMekanismTier;
import astral_mekanism.block.blockentity.storage.TieredDriveBlockEntities.TieredDriveBlockEntity;
import astral_mekanism.block.container.storage.TieredDriveMenu;
import net.minecraft.world.inventory.MenuType;
import net.pedroksl.ae2addonlib.registry.MenuRegistry;

public class AstralMekanismMenus extends MenuRegistry {

    public static final AstralMekanismMenus INSTANCE = new AstralMekanismMenus();

    public AstralMekanismMenus() {
        super(AstralMekanismID.MODID);
    }

    public static final EnumMap<AstralMekanismTier, Supplier<MenuType<TieredDriveMenu>>> DRIVES = new EnumMap<>(
            AstralMekanismTier.class);

    public static final Supplier<MenuType<TieredDriveMenu>> LOGIC_DRIVE = DRIVES.put(AstralMekanismTier.ESSENTIAL,
            drive(AstralMekanismTier.ESSENTIAL));
    public static final Supplier<MenuType<TieredDriveMenu>> CALCULATION_DRIVE = DRIVES.put(AstralMekanismTier.BASIC,
            drive(AstralMekanismTier.BASIC));
    public static final Supplier<MenuType<TieredDriveMenu>> ENGINEERING_DRIVE = DRIVES.put(AstralMekanismTier.ADVANCED,
            drive(AstralMekanismTier.ADVANCED));
    public static final Supplier<MenuType<TieredDriveMenu>> ACCUMULATION_DRIVE = DRIVES.put(AstralMekanismTier.ELITE,
            drive(AstralMekanismTier.ELITE));
    public static final Supplier<MenuType<TieredDriveMenu>> PHOTON_DRIVE = DRIVES.put(AstralMekanismTier.ULTIMATE,
            drive(AstralMekanismTier.ULTIMATE));
    public static final Supplier<MenuType<TieredDriveMenu>> QUANTUM_DRIVE = DRIVES.put(AstralMekanismTier.ABSOLUTE,
            drive(AstralMekanismTier.ABSOLUTE));
    public static final Supplier<MenuType<TieredDriveMenu>> COMPOSITE_DRIVE = DRIVES.put(AstralMekanismTier.SUPREME,
            drive(AstralMekanismTier.SUPREME));
    public static final Supplier<MenuType<TieredDriveMenu>> ORIGIN_DRIVE = DRIVES.put(AstralMekanismTier.COSMIC,
            drive(AstralMekanismTier.COSMIC));
    public static final Supplier<MenuType<TieredDriveMenu>> AUTONOMY_DRIVE = DRIVES.put(AstralMekanismTier.INFINITE,
            drive(AstralMekanismTier.INFINITE));
    public static final Supplier<MenuType<TieredDriveMenu>> FIRMAMENT_DRIVE = DRIVES.put(AstralMekanismTier.ASTRAL,
            drive(AstralMekanismTier.ASTRAL));

    private static <M extends AEBaseMenu, H> Supplier<MenuType<M>> create(
            String id, MenuTypeBuilder.MenuFactory<M, H> factory, Class<H> host) {
        return create(AstralMekanismID.MODID, id, factory, host);
    }

    private static <T extends AEBaseMenu> Supplier<MenuType<T>> create(String id, Supplier<MenuType<T>> supplier) {
        return create(AstralMekanismID.MODID, id, supplier);
    }

    private static Supplier<MenuType<TieredDriveMenu>> drive(AstralMekanismTier tier) {
        return create(tier.nameForAE + "_drive", TieredDriveMenu::new, TieredDriveBlockEntity.class);
    }

}
