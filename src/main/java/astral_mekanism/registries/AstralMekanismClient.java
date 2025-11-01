package astral_mekanism.registries;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.block.blockentity.astralmachine.BEAstralGNA;
import astral_mekanism.block.blockentity.astralmachine.BEAstralSPS;
import astral_mekanism.block.blockentity.astralmachine.advanced.BEAstralChemicalInjectionChamber;
import astral_mekanism.block.blockentity.astralmachine.advanced.BEAstralOsmiumCompressor;
import astral_mekanism.block.blockentity.astralmachine.advanced.BEAstralPurificationChamber;
import astral_mekanism.block.blockentity.astralmachine.electric.BEAstralCrusher;
import astral_mekanism.block.blockentity.astralmachine.electric.BEAstralEnergizedSmelter;
import astral_mekanism.block.blockentity.astralmachine.electric.BEAstralEnrichmentChamber;
import astral_mekanism.block.blockentity.astralmachine.electric.BEAstralMekanicalCharger;
import astral_mekanism.block.blockentity.compact.BECompactSPS;
import astral_mekanism.block.blockentity.generator.AstralMekGeneratorTier;
import astral_mekanism.block.blockentity.normalmachine.BEGlowstoneNeutronActivator;
import astral_mekanism.block.blockentity.normalmachine.BEMekanicalCharger;
import astral_mekanism.block.blockentity.other.BEItemSortableStorage;
import astral_mekanism.block.blockentity.other.BEUniversalStorage;
import astral_mekanism.block.container.other.ContainerItemSortableStorage;
import astral_mekanism.block.container.prefab.ContainerAbstractStorage;
import astral_mekanism.block.gui.astralmachine.GuiAstralAdvancedMachine;
import astral_mekanism.block.gui.astralmachine.GuiAstralElectricMachine;
import astral_mekanism.block.gui.astralmachine.GuiAstralElectrolyticSeparator;
import astral_mekanism.block.gui.astralmachine.GuiAstralGreenHouse;
import astral_mekanism.block.gui.astralmachine.GuiAstralPRC;
import astral_mekanism.block.gui.astralmachine.GuiAstralPrecisionSawmill;
import astral_mekanism.block.gui.compact.GuiCompactFissionReactor;
import astral_mekanism.block.gui.compact.GuiCompactTEP;
import astral_mekanism.block.gui.generator.GuiGasBurningGenerator;
import astral_mekanism.block.gui.generator.GuiHeatGenerator;
import astral_mekanism.block.gui.normalmachine.GuiExpandedCrafter;
import astral_mekanism.block.gui.normalmachine.GuiFluidInfuser;
import astral_mekanism.block.gui.normalmachine.GuiGreenHouse;
import astral_mekanism.block.gui.normalmachine.GuiMelter;
import astral_mekanism.block.gui.prefab.GuiAbstractStorage;
import astral_mekanism.block.gui.prefab.GuiGasToGasBlock;
import astral_mekanism.block.gui.prefab.GuiGasToGasMachine;
import astral_mekanism.block.gui.supplydevice.GuiCobblestoneSupplyDevice;
import astral_mekanism.block.gui.supplydevice.GuiWaterSupplyDevice;
import astral_mekanism.registration.MachineRegistryObject;
import mekanism.client.ClientRegistrationUtil;
import mekanism.client.gui.machine.GuiElectricMachine;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.gui.screens.MenuScreens.ScreenConstructor;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = AstralMekanismID.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralMekanismClient {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerContiners(RegisterEvent event) {
        event.register(Registries.MENU, helper -> {

            for (AstralMekGeneratorTier tier : AstralMekGeneratorTier.values()) {
                regScreen(AstralMekanismMachines.GAS_BURNING_GENERATORS.get(tier),
                        GuiGasBurningGenerator::new);
                regScreen(AstralMekanismMachines.HEAT_GENERATORS.get(tier),
                        GuiHeatGenerator::new);
            }
            regScreen(AstralMekanismMachines.UNIVERSAL_STORAGE,
                    GuiAbstractStorage<BEUniversalStorage, ContainerAbstractStorage<BEUniversalStorage>>::new);
            regScreen(AstralMekanismMachines.ITEM_SORTABLE_STORAGE,
                    GuiAbstractStorage<BEItemSortableStorage, ContainerItemSortableStorage<BEItemSortableStorage>>::new);
            regScreen(AstralMekanismMachines.COBBLESTONE_SUPPLY_DEVICE, GuiCobblestoneSupplyDevice::new);
            regScreen(AstralMekanismMachines.WATER_SUPPLY_DEVICE, GuiWaterSupplyDevice::new);
            regScreen(AstralMekanismMachines.GLOWSTONE_NEUTRON_ACTIVATOR,
                    GuiGasToGasBlock<BEGlowstoneNeutronActivator>::new);
            regScreen(AstralMekanismMachines.GREENHOUSE, GuiGreenHouse::new);
            regScreen(AstralMekanismMachines.MELTER, GuiMelter::new);
            regScreen(AstralMekanismMachines.COMPACT_TEP, GuiCompactTEP::new);
            regScreen(AstralMekanismMachines.FLUID_INFUSER, GuiFluidInfuser::new);
            regScreen(AstralMekanismMachines.MEKANICAL_CHARGER,
                    GuiElectricMachine<BEMekanicalCharger, MekanismTileContainer<BEMekanicalCharger>>::new);
            regScreen(AstralMekanismMachines.COMPACT_FIR, GuiCompactFissionReactor::new);
            regScreen(AstralMekanismMachines.COMPACT_SPS, GuiGasToGasMachine<BECompactSPS>::new);
            regScreen(AstralMekanismMachines.EXPANDED_CRAFTER, GuiExpandedCrafter::new);
            regScreen(AstralMekanismMachines.ASTRAL_GNA, GuiGasToGasBlock<BEAstralGNA>::new);
            regScreen(AstralMekanismMachines.ASTRAL_SPS, GuiGasToGasMachine<BEAstralSPS>::new);
            regScreen(AstralMekanismMachines.ASTRAL_PRC, GuiAstralPRC::new);
            regScreen(AstralMekanismMachines.ASTRAL_GREENHOUSE, GuiAstralGreenHouse::new);
            regScreen(AstralMekanismMachines.ASTRAL_ELECTROLYTIC_SEPARATOR, GuiAstralElectrolyticSeparator::new);
            regScreen(AstralMekanismMachines.ASTRAL_PRECISION_SAWMILL, GuiAstralPrecisionSawmill::new);
            regScreen(AstralMekanismMachines.ASTRAL_CRUSHER, GuiAstralElectricMachine<BEAstralCrusher>::new);
            regScreen(AstralMekanismMachines.ASTRAL_ENERGIZED_SMELTER, GuiAstralElectricMachine<BEAstralEnergizedSmelter>::new);
            regScreen(AstralMekanismMachines.ASTRAL_ENRICHMENT_CHAMBER, GuiAstralElectricMachine<BEAstralEnrichmentChamber>::new);
            regScreen(AstralMekanismMachines.ASTRAL_MEKANICAL_CHARGER, GuiAstralElectricMachine<BEAstralMekanicalCharger>::new);
            ClientRegistrationUtil.registerScreen(
                    AstralMekanismContainers.ASTRAL_CHEMICAL_INJECTION_CHAMBER,
                    GuiAstralAdvancedMachine<BEAstralChemicalInjectionChamber>::new);
            ClientRegistrationUtil.registerScreen(AstralMekanismContainers.ASTRAL_OSMIUM_COMPRESSOR,
                    GuiAstralAdvancedMachine<BEAstralOsmiumCompressor>::new);
            ClientRegistrationUtil.registerScreen(AstralMekanismContainers.ASTRAL_PURIFICATION_CHAMBER,
                    GuiAstralAdvancedMachine<BEAstralPurificationChamber>::new);
        });
    }

    private static <BE extends TileEntityMekanism, CONTAINER extends MekanismTileContainer<BE>, U extends Screen & MenuAccess<CONTAINER>> void regScreen(
            MachineRegistryObject<BE, ?, ?, CONTAINER, ?> registryObject,
            ScreenConstructor<CONTAINER, U> constructor) {
        ClientRegistrationUtil.registerScreen(registryObject.getContainerRO(), constructor);
    }

}
