package astral_mekanism.registries;

import java.util.Map;
import java.util.function.UnaryOperator;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.block.blockentity.astralmachine.BEAstralElectrolyticSeparator;
import astral_mekanism.block.blockentity.astralmachine.BEAstralGNA;
import astral_mekanism.block.blockentity.astralmachine.BEAstralGreenHouse;
import astral_mekanism.block.blockentity.astralmachine.BEAstralPRC;
import astral_mekanism.block.blockentity.astralmachine.BEAstralPrecisionSawmill;
import astral_mekanism.block.blockentity.astralmachine.BEAstralSPS;
import astral_mekanism.block.blockentity.astralmachine.advanced.BEAstralChemicalInjectionChamber;
import astral_mekanism.block.blockentity.astralmachine.advanced.BEAstralOsmiumCompressor;
import astral_mekanism.block.blockentity.astralmachine.advanced.BEAstralPurificationChamber;
import astral_mekanism.block.blockentity.astralmachine.electric.BEAstralCrusher;
import astral_mekanism.block.blockentity.astralmachine.electric.BEAstralEnergizedSmelter;
import astral_mekanism.block.blockentity.astralmachine.electric.BEAstralEnrichmentChamber;
import astral_mekanism.block.blockentity.astralmachine.electric.BEAstralMekanicalCharger;
import astral_mekanism.block.blockentity.compact.BECompactFIR;
import astral_mekanism.block.blockentity.compact.BECompactSPS;
import astral_mekanism.block.blockentity.compact.BECompactTEP;
import astral_mekanism.block.blockentity.generator.AstralMekGeneratorTier;
import astral_mekanism.block.blockentity.generator.BEGasBurningGenerator;
import astral_mekanism.block.blockentity.generator.BEHeatGenerator;
import astral_mekanism.block.blockentity.normalmachine.BEAstralCrafter;
import astral_mekanism.block.blockentity.normalmachine.BEFluidInfuser;
import astral_mekanism.block.blockentity.normalmachine.BEGlowstoneNeutronActivator;
import astral_mekanism.block.blockentity.normalmachine.BEGreenHouse;
import astral_mekanism.block.blockentity.normalmachine.BEMekanicalCharger;
import astral_mekanism.block.blockentity.normalmachine.BEMelter;
import astral_mekanism.block.blockentity.other.BEItemSortableStorage;
import astral_mekanism.block.blockentity.other.BEUniversalStorage;
import astral_mekanism.block.blockentity.supplydevice.BECobblestoneSupplyDevice;
import astral_mekanism.block.blockentity.supplydevice.BEWaterSupplyDevice;
import astral_mekanism.block.container.normal_machine.ContainerAstralCrafter;
import astral_mekanism.block.container.other.ContainerItemSortableStorage;
import astral_mekanism.block.container.prefab.ContainerAbstractStorage;
import astral_mekanism.registration.BlockTypeMachine;
import astral_mekanism.registration.MachineDeferrdRegister;
import astral_mekanism.registration.MachineRegistryObject;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.item.block.machine.ItemBlockMachine;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;

public class AstralMekanismMachines {
    public static final MachineDeferrdRegister MACHINES = new MachineDeferrdRegister(AstralMekanismID.MODID);

    private static final UnaryOperator<Properties> normalOperator = p -> p
            .strength(1.5f, 6.0f).sound(SoundType.STONE).mapColor(MapColor.STONE);

    public static final Map<AstralMekGeneratorTier, MachineRegistryObject<BEGasBurningGenerator, BlockTypeTile<BEGasBurningGenerator>, BlockTileModel<BEGasBurningGenerator, BlockTypeTile<BEGasBurningGenerator>>, MekanismTileContainer<BEGasBurningGenerator>, ItemBlockMachine>> GAS_BURNING_GENERATORS = MACHINES
            .mapRegister(AstralMekGeneratorTier.class, k -> k.name + "_gas_burning_generator",
                    AstralMekanismBlockTypes.GAS_BURNING_GENERATORS,
                    bt -> new BlockTileModel<>(bt, normalOperator), ItemBlockMachine::new,
                    k -> BEGasBurningGenerator::new, BEGasBurningGenerator.class,
                    MekanismTileContainer<BEGasBurningGenerator>::new);

    public static final Map<AstralMekGeneratorTier, MachineRegistryObject<BEHeatGenerator, BlockTypeTile<BEHeatGenerator>, BlockTileModel<BEHeatGenerator, BlockTypeTile<BEHeatGenerator>>, MekanismTileContainer<BEHeatGenerator>, ItemBlockMachine>> HEAT_GENERATORS = MACHINES
            .mapRegister(AstralMekGeneratorTier.class, k -> k.name + "_heat_generator",
                    AstralMekanismBlockTypes.HEAT_GENERATORS,
                    bt -> new BlockTileModel<>(bt, normalOperator), ItemBlockMachine::new,
                    k -> BEHeatGenerator::new, BEHeatGenerator.class,
                    MekanismTileContainer<BEHeatGenerator>::new);

    public static final MachineRegistryObject<BEUniversalStorage, BlockTypeTile<BEUniversalStorage>, BlockTileModel<BEUniversalStorage, BlockTypeTile<BEUniversalStorage>>, ContainerAbstractStorage<BEUniversalStorage>, ItemBlockMachine> UNIVERSAL_STORAGE = MACHINES
            .register("universal_storage", AstralMekanismBlockTypes.UNIVERSAL_STORAGE,
                    bt -> new BlockTileModel<>(bt, normalOperator), ItemBlockMachine::new,
                    BEUniversalStorage::new,
                    BEUniversalStorage.class, ContainerAbstractStorage<BEUniversalStorage>::new);

    public static final MachineRegistryObject<BEItemSortableStorage, BlockTypeTile<BEItemSortableStorage>, BlockTileModel<BEItemSortableStorage, BlockTypeTile<BEItemSortableStorage>>, ContainerItemSortableStorage<BEItemSortableStorage>, ItemBlockMachine> ITEM_SORTABLE_STORAGE = MACHINES
            .register("item_sortable_storage", AstralMekanismBlockTypes.ITEM_SORTABLE_STORAGE,
                    bt -> new BlockTileModel<>(bt, normalOperator), ItemBlockMachine::new,
                    BEItemSortableStorage::new,
                    BEItemSortableStorage.class,
                    ContainerItemSortableStorage<BEItemSortableStorage>::new);

    public static final MachineRegistryObject<BECobblestoneSupplyDevice, BlockTypeTile<BECobblestoneSupplyDevice>, BlockTileModel<BECobblestoneSupplyDevice, BlockTypeTile<BECobblestoneSupplyDevice>>, MekanismTileContainer<BECobblestoneSupplyDevice>, ItemBlockMachine> COBBLESTONE_SUPPLY_DEVICE = MACHINES
            .register("cobblestone_supply_device", AstralMekanismBlockTypes.COBBLESTONE_SUPPLY_DEVICE,
                    BECobblestoneSupplyDevice::new, BECobblestoneSupplyDevice.class);

    public static final MachineRegistryObject<BEWaterSupplyDevice, BlockTypeTile<BEWaterSupplyDevice>, BlockTileModel<BEWaterSupplyDevice, BlockTypeTile<BEWaterSupplyDevice>>, MekanismTileContainer<BEWaterSupplyDevice>, ItemBlockMachine> WATER_SUPPLY_DEVICE = MACHINES
            .register("water_supply_device", AstralMekanismBlockTypes.WATER_SUPPLY_DEVICE,
                    BEWaterSupplyDevice::new, BEWaterSupplyDevice.class);

    public static final MachineRegistryObject<BEGlowstoneNeutronActivator, BlockTypeTile<BEGlowstoneNeutronActivator>, BlockTileModel<BEGlowstoneNeutronActivator, BlockTypeTile<BEGlowstoneNeutronActivator>>, MekanismTileContainer<BEGlowstoneNeutronActivator>, ItemBlockMachine> GLOWSTONE_NEUTRON_ACTIVATOR = MACHINES
            .register("glowstone_neutron_activator", AstralMekanismBlockTypes.GLOWSTONE_NEUTRON_ACTIVATOR,
                    BEGlowstoneNeutronActivator::new, BEGlowstoneNeutronActivator.class);

    public static final MachineRegistryObject<BEGreenHouse, BlockTypeMachine<BEGreenHouse>, BlockTileModel<BEGreenHouse, BlockTypeMachine<BEGreenHouse>>, MekanismTileContainer<BEGreenHouse>, ItemBlockMachine> GREENHOUSE = MACHINES
            .register("greenhouse", AstralMekanismBlockTypes.GREENHOUSE, BEGreenHouse.class,
                    BEGreenHouse::new);

    public static final MachineRegistryObject<BEMelter, BlockTypeTile<BEMelter>, BlockTileModel<BEMelter, BlockTypeTile<BEMelter>>, MekanismTileContainer<BEMelter>, ItemBlockMachine> MELTER = MACHINES
            .register("melter", AstralMekanismBlockTypes.MELTER, BEMelter::new, BEMelter.class);

    public static final MachineRegistryObject<BECompactTEP, BlockTypeTile<BECompactTEP>, BlockTileModel<BECompactTEP, BlockTypeTile<BECompactTEP>>, MekanismTileContainer<BECompactTEP>, ItemBlockMachine> COMPACT_TEP = MACHINES
            .register("compact_tep", AstralMekanismBlockTypes.COMPACT_TEP, BECompactTEP::new,
                    BECompactTEP.class);

    public static final MachineRegistryObject<BEFluidInfuser, BlockTypeMachine<BEFluidInfuser>, BlockTileModel<BEFluidInfuser, BlockTypeMachine<BEFluidInfuser>>, MekanismTileContainer<BEFluidInfuser>, ItemBlockMachine> FLUID_INFUSER = MACHINES
            .register("fluid_infuser", AstralMekanismBlockTypes.FLUID_INFUSER,
                    BEFluidInfuser.class, BEFluidInfuser::new);

    public static final MachineRegistryObject<BEMekanicalCharger, BlockTypeMachine<BEMekanicalCharger>, BlockTileModel<BEMekanicalCharger, BlockTypeMachine<BEMekanicalCharger>>, MekanismTileContainer<BEMekanicalCharger>, ItemBlockMachine> MEKANICAL_CHARGER = MACHINES
            .register("mekanical_charger", AstralMekanismBlockTypes.MEKANICAL_CHARGER,
                    BEMekanicalCharger.class,
                    BEMekanicalCharger::new);

    public static final MachineRegistryObject<BEAstralCrafter, BlockTypeMachine<BEAstralCrafter>, BlockTileModel<BEAstralCrafter, BlockTypeMachine<BEAstralCrafter>>, ContainerAstralCrafter, ItemBlockMachine> ASTRAL_CRAFTER = MACHINES
            .register("astral_crafter", AstralMekanismBlockTypes.ASTRAL_CRAFTER,
                    bt -> new BlockTileModel<>(bt, normalOperator), ItemBlockMachine::new,
                    BEAstralCrafter::new, BEAstralCrafter.class, ContainerAstralCrafter::new);

    public static final MachineRegistryObject<BECompactFIR, BlockTypeTile<BECompactFIR>, BlockTileModel<BECompactFIR, BlockTypeTile<BECompactFIR>>, MekanismTileContainer<BECompactFIR>, ItemBlockMachine> COMPACT_FIR = MACHINES
            .register("compact_fir", AstralMekanismBlockTypes.COMPACT_FIR, BECompactFIR::new,
                    BECompactFIR.class);

    public static final MachineRegistryObject<BECompactSPS, BlockTypeMachine<BECompactSPS>, BlockTileModel<BECompactSPS, BlockTypeMachine<BECompactSPS>>, MekanismTileContainer<BECompactSPS>, ItemBlockMachine> COMPACT_SPS = MACHINES
            .register("compact_sps", AstralMekanismBlockTypes.COMPACT_SPS, BECompactSPS.class,
                    BECompactSPS::new);

    public static final MachineRegistryObject<BEAstralGNA, BlockTypeTile<BEAstralGNA>, BlockTileModel<BEAstralGNA, BlockTypeTile<BEAstralGNA>>, MekanismTileContainer<BEAstralGNA>, ItemBlockMachine> ASTRAL_GNA = MACHINES
            .register("astral_gna", AstralMekanismBlockTypes.ASTRAL_GNA, BEAstralGNA::new,
                    BEAstralGNA.class);

    public static final MachineRegistryObject<BEAstralSPS, BlockTypeMachine<BEAstralSPS>, BlockTileModel<BEAstralSPS, BlockTypeMachine<BEAstralSPS>>, MekanismTileContainer<BEAstralSPS>, ItemBlockMachine> ASTRAL_SPS = MACHINES
            .register("astral_sps", AstralMekanismBlockTypes.ASTRAL_SPS, BEAstralSPS.class,
                    BEAstralSPS::new);

    public static final MachineRegistryObject<BEAstralPRC, BlockTypeMachine<BEAstralPRC>, BlockTileModel<BEAstralPRC, BlockTypeMachine<BEAstralPRC>>, MekanismTileContainer<BEAstralPRC>, ItemBlockMachine> ASTRAL_PRC = MACHINES
            .register("astral_prc", AstralMekanismBlockTypes.ASTRAL_PRC, BEAstralPRC.class,
                    BEAstralPRC::new);

    public static final MachineRegistryObject<BEAstralGreenHouse, BlockTypeMachine<BEAstralGreenHouse>, BlockTileModel<BEAstralGreenHouse, BlockTypeMachine<BEAstralGreenHouse>>, MekanismTileContainer<BEAstralGreenHouse>, ItemBlockMachine> ASTRAL_GREENHOUSE = MACHINES
            .register("astral_greenhouse", AstralMekanismBlockTypes.ASTRAL_GREENHOUSE,
                    BEAstralGreenHouse.class, BEAstralGreenHouse::new);

    public static final MachineRegistryObject<BEAstralElectrolyticSeparator, BlockTypeMachine<BEAstralElectrolyticSeparator>, BlockTileModel<BEAstralElectrolyticSeparator, BlockTypeMachine<BEAstralElectrolyticSeparator>>, MekanismTileContainer<BEAstralElectrolyticSeparator>, ItemBlockMachine> ASTRAL_ELECTROLYTIC_SEPARATOR = MACHINES
            .register("astral_electrolytic_separator",
                    AstralMekanismBlockTypes.ASTRAL_ELECTROLYTIC_SEPARATOR,
                    BEAstralElectrolyticSeparator.class, BEAstralElectrolyticSeparator::new);

    public static final MachineRegistryObject<BEAstralPrecisionSawmill, BlockTypeMachine<BEAstralPrecisionSawmill>, BlockTileModel<BEAstralPrecisionSawmill, BlockTypeMachine<BEAstralPrecisionSawmill>>, MekanismTileContainer<BEAstralPrecisionSawmill>, ItemBlockMachine> ASTRAL_PRECISION_SAWMILL = MACHINES
            .register("astral_precision_sawmill", AstralMekanismBlockTypes.ASTRAL_PRECISION_SAWMILL,
                    BEAstralPrecisionSawmill.class, BEAstralPrecisionSawmill::new);

    public static final MachineRegistryObject<BEAstralCrusher, BlockTypeMachine<BEAstralCrusher>, BlockTileModel<BEAstralCrusher, BlockTypeMachine<BEAstralCrusher>>, MekanismTileContainer<BEAstralCrusher>, ItemBlockMachine> ASTRAL_CRUSHER = MACHINES
            .register("astral_crusher", AstralMekanismBlockTypes.ASTRAL_CRUSHER,
                    BEAstralCrusher.class, BEAstralCrusher::new);

    public static final MachineRegistryObject<BEAstralEnergizedSmelter, BlockTypeMachine<BEAstralEnergizedSmelter>, BlockTileModel<BEAstralEnergizedSmelter, BlockTypeMachine<BEAstralEnergizedSmelter>>, MekanismTileContainer<BEAstralEnergizedSmelter>, ItemBlockMachine> ASTRAL_ENERGIZED_SMELTER = MACHINES
            .register("astral_energized_smelter", AstralMekanismBlockTypes.ASTRAL_ENERGIZED_SMELTER,
                    BEAstralEnergizedSmelter.class, BEAstralEnergizedSmelter::new);

    public static final MachineRegistryObject<BEAstralEnrichmentChamber, BlockTypeMachine<BEAstralEnrichmentChamber>, BlockTileModel<BEAstralEnrichmentChamber, BlockTypeMachine<BEAstralEnrichmentChamber>>, MekanismTileContainer<BEAstralEnrichmentChamber>, ItemBlockMachine> ASTRAL_ENRICHMENT_CHAMBER = MACHINES
            .register("astral_enrichment_chamber", AstralMekanismBlockTypes.ASTRAL_ENRICHMENT_CHAMBER,
                    BEAstralEnrichmentChamber.class, BEAstralEnrichmentChamber::new);

    public static final MachineRegistryObject<BEAstralMekanicalCharger, BlockTypeMachine<BEAstralMekanicalCharger>, BlockTileModel<BEAstralMekanicalCharger, BlockTypeMachine<BEAstralMekanicalCharger>>, MekanismTileContainer<BEAstralMekanicalCharger>, ItemBlockMachine> ASTRAL_MEKANICAL_CHARGER = MACHINES
            .register("astral_mekanical_charger", AstralMekanismBlockTypes.ASTRAL_MEKANICAL_CHARGER,
                    BEAstralMekanicalCharger.class, BEAstralMekanicalCharger::new);

    public static final MachineRegistryObject<BEAstralChemicalInjectionChamber, BlockTypeMachine<BEAstralChemicalInjectionChamber>, BlockTileModel<BEAstralChemicalInjectionChamber, BlockTypeMachine<BEAstralChemicalInjectionChamber>>, MekanismTileContainer<BEAstralChemicalInjectionChamber>, ItemBlockMachine> ASTRAL_CHEMICAL_INJECTION_CHAMBER = MACHINES
            .register("astral_chemical_injection_chamber", AstralMekanismBlockTypes.ASTRAL_CHEMICAL_INJECTION_CHAMBER,
                    BEAstralChemicalInjectionChamber.class, BEAstralChemicalInjectionChamber::new);

    public static final MachineRegistryObject<BEAstralOsmiumCompressor, BlockTypeMachine<BEAstralOsmiumCompressor>, BlockTileModel<BEAstralOsmiumCompressor, BlockTypeMachine<BEAstralOsmiumCompressor>>, MekanismTileContainer<BEAstralOsmiumCompressor>, ItemBlockMachine> ASTRAL_OSMIUM_COMPRESSOR = MACHINES
            .register("astral_osmium_compressor", AstralMekanismBlockTypes.ASTRAL_OSMIUM_COMPRESSOR,
                    BEAstralOsmiumCompressor.class, BEAstralOsmiumCompressor::new);

    public static final MachineRegistryObject<BEAstralPurificationChamber, BlockTypeMachine<BEAstralPurificationChamber>, BlockTileModel<BEAstralPurificationChamber, BlockTypeMachine<BEAstralPurificationChamber>>, MekanismTileContainer<BEAstralPurificationChamber>, ItemBlockMachine> ASTRAL_PURIFICATION_CHAMBER = MACHINES
            .register("astral_purification_chamber", AstralMekanismBlockTypes.ASTRAL_PURIFICATION_CHAMBER,
                    BEAstralPurificationChamber.class, BEAstralPurificationChamber::new);

}
