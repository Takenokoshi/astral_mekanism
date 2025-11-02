package astral_mekanism.registries;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import astral_mekanism.AstralMekanismConfig;
import astral_mekanism.AstralMekanismLang;
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
import astral_mekanism.block.shape.AMBlockShapes;
import astral_mekanism.registration.BlockTypeMachine;
import astral_mekanism.registration.BlockTypeTileUtils;
import astral_mekanism.registration.BlockTypeMachine.BlockMachineBuilder;
import mekanism.api.math.FloatingLong;
import mekanism.api.Upgrade;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.AttributeStateFacing;
import mekanism.common.block.attribute.AttributeTier;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.BlockTypeTile.BlockTileBuilder;
import mekanism.common.registries.MekanismSounds;
import mekanism.generators.common.GeneratorsLang;

public class AstralMekanismBlockTypes {
    private AstralMekanismBlockTypes() {
    }

    public static final Map<AstralMekGeneratorTier, BlockTypeTile<BEGasBurningGenerator>> GAS_BURNING_GENERATORS = BlockTypeTileUtils
            .buildMap(AstralMekGeneratorTier.class,
                    t -> () -> AstralMekanismMachines.GAS_BURNING_GENERATORS.get(t).getTileRO(),
                    GeneratorsLang.DESCRIPTION_GAS_BURNING_GENERATOR,
                    t -> () -> AstralMekanismMachines.GAS_BURNING_GENERATORS.get(t).getContainerRO(),
                    (t, b) -> b.with(new AttributeTier<AstralMekGeneratorTier>(t))
                            .withCustomShape(AMBlockShapes.GAS_BURNING_GENERATOR));

    public static final Map<AstralMekGeneratorTier, BlockTypeTile<BEHeatGenerator>> HEAT_GENERATORS = BlockTypeTileUtils
            .buildMap(AstralMekGeneratorTier.class,
                    t -> () -> AstralMekanismMachines.HEAT_GENERATORS.get(t).getTileRO(),
                    GeneratorsLang.DESCRIPTION_HEAT_GENERATOR,
                    t -> () -> AstralMekanismMachines.HEAT_GENERATORS.get(t).getContainerRO(),
                    (t, b) -> b.with(new AttributeTier<AstralMekGeneratorTier>(t))
                            .withCustomShape(AMBlockShapes.HEAT_GENERATOR));

    public static final BlockTypeTile<BEUniversalStorage> UNIVERSAL_STORAGE = BlockTileBuilder
            .createBlock(() -> AstralMekanismMachines.UNIVERSAL_STORAGE.getTileRO(),
                    AstralMekanismLang.DESCRIPTION_UNIVERSAL_STORAGE)
            .withGui(() -> AstralMekanismMachines.UNIVERSAL_STORAGE.getContainerRO())
            .with(new AttributeStateFacing())
            .build();

    public static final BlockTypeTile<BEItemSortableStorage> ITEM_SORTABLE_STORAGE = BlockTileBuilder
            .createBlock(() -> AstralMekanismMachines.ITEM_SORTABLE_STORAGE.getTileRO(),
                    AstralMekanismLang.DESCRIPTION_UNIVERSAL_STORAGE)
            .withGui(() -> AstralMekanismMachines.ITEM_SORTABLE_STORAGE.getContainerRO())
            .with(new AttributeStateFacing())
            .build();

    public static final BlockTypeTile<BECobblestoneSupplyDevice> COBBLESTONE_SUPPLY_DEVICE = BlockTileBuilder
            .createBlock(() -> AstralMekanismMachines.COBBLESTONE_SUPPLY_DEVICE.getTileRO(),
                    AstralMekanismLang.DESCRIPTION_COBBLESTONE_SUPPLY_DEVICE)
            .withGui(() -> AstralMekanismMachines.COBBLESTONE_SUPPLY_DEVICE.getContainerRO())
            .with(new AttributeStateFacing())
            .build();

    public static final BlockTypeTile<BEWaterSupplyDevice> WATER_SUPPLY_DEVICE = BlockTileBuilder
            .createBlock(() -> AstralMekanismMachines.WATER_SUPPLY_DEVICE.getTileRO(),
                    AstralMekanismLang.DESCRIPTION_WATER_SUPPLY_DEVICE)
            .withGui(() -> AstralMekanismMachines.WATER_SUPPLY_DEVICE.getContainerRO())
            .withSupportedUpgrades(Set.of(Upgrade.FILTER))
            .with(new AttributeStateFacing())
            .build();

    public static final BlockTypeTile<BEGlowstoneNeutronActivator> GLOWSTONE_NEUTRON_ACTIVATOR = BlockTileBuilder
            .createBlock(() -> AstralMekanismMachines.GLOWSTONE_NEUTRON_ACTIVATOR.getTileRO(),
                    AstralMekanismLang.DESCRIPTION_GLOWSTONE_NEUTRON_ACTIVATOR)
            .withGui(() -> AstralMekanismMachines.GLOWSTONE_NEUTRON_ACTIVATOR.getContainerRO())
            .withCustomShape(AMBlockShapes.GLOWSTONE_NEUTRON_ACTIVATOR)
            .with(new AttributeStateFacing())
            .build();

    public static final BlockTypeMachine<BEGreenHouse> GREENHOUSE = BlockMachineBuilder
            .createMachine(() -> AstralMekanismMachines.GREENHOUSE.getTileRO(),
                    AstralMekanismLang.DESCRIPTION_GREENHOUSE)
            .withEnergyConfig(() -> FloatingLong.create(1000 * AstralMekanismConfig.energyRate),
                    () -> FloatingLong.create(10000000))
            .withGui(() -> AstralMekanismMachines.GREENHOUSE.getContainerRO())
            .withCustomShape(AMBlockShapes.GREENHOUSE)
            .build();

    public static final BlockTypeTile<BEMelter> MELTER = BlockTileBuilder
            .createBlock(() -> AstralMekanismMachines.MELTER.getTileRO(), AstralMekanismLang.DESCRIPTION_MELTER)
            .withGui(() -> AstralMekanismMachines.MELTER.getContainerRO())
            .with(new AttributeStateFacing())
            .build();

    public static final BlockTypeTile<BECompactTEP> COMPACT_TEP = BlockTileBuilder
            .createBlock(() -> AstralMekanismMachines.COMPACT_TEP.getTileRO(),
                    AstralMekanismLang.DESCRIPTION_COMPACT_TEP)
            .withGui(() -> AstralMekanismMachines.COMPACT_TEP.getContainerRO())
            .with(new AttributeStateFacing())
            .build();

    public static final BlockTypeMachine<BEFluidInfuser> FLUID_INFUSER = BlockMachineBuilder
            .createMachine(() -> AstralMekanismMachines.FLUID_INFUSER.getTileRO(),
                    AstralMekanismLang.DESCRIPTION_FLUID_INFUSER)
            .withEnergyConfig(() -> FloatingLong.create(1000 * AstralMekanismConfig.energyRate),
                    () -> FloatingLong.create(10000000))
            .withGui(() -> AstralMekanismMachines.FLUID_INFUSER.getContainerRO())
            .withCustomShape(AMBlockShapes.FLUID_INFUSER)
            .build();

    public static final BlockTypeMachine<BEMekanicalCharger> MEKANICAL_CHARGER = BlockMachineBuilder
            .createMachine(() -> AstralMekanismMachines.MEKANICAL_CHARGER.getTileRO(),
                    AstralMekanismLang.DESCRIPTION_MEKANICAL_CHARGER)
            .withEnergyConfig(() -> FloatingLong.create(4 * AstralMekanismConfig.energyRate),
                    () -> FloatingLong.create(100000 * AstralMekanismConfig.energyRate))
            .withGui(() -> AstralMekanismMachines.MEKANICAL_CHARGER.getContainerRO())
            .withCustomShape(AMBlockShapes.MEKANICAL_CHARGER)
            .build();

    public static final BlockTypeMachine<BEAstralCrafter> ASTRAL_CRAFTER = BlockMachineBuilder
            .createMachine(() -> AstralMekanismMachines.ASTRAL_CRAFTER.getTileRO(), null)
            .withEnergyConfig(() -> FloatingLong.create(400 * AstralMekanismConfig.energyRate),
                    () -> FloatingLong.create(10000000 * AstralMekanismConfig.energyRate))
            .withGui(() -> AstralMekanismMachines.ASTRAL_CRAFTER.getContainerRO())
            .build();

    public static final BlockTypeTile<BECompactFIR> COMPACT_FIR = BlockTileBuilder
            .createBlock(() -> AstralMekanismMachines.COMPACT_FIR.getTileRO(),
                    GeneratorsLang.DESCRIPTION_FISSION_REACTOR_CASING)
            .withGui(() -> AstralMekanismMachines.COMPACT_FIR.getContainerRO())
            .with(new AttributeStateFacing())
            .build();

    public static final BlockTypeMachine<BECompactSPS> COMPACT_SPS = BlockMachineBuilder
            .createMachine(() -> AstralMekanismMachines.COMPACT_SPS.getTileRO(), MekanismLang.DESCRIPTION_SPS_CASING)
            .withGui(() -> AstralMekanismMachines.COMPACT_SPS.getContainerRO())
            .withEnergyConfig(() -> FloatingLong.create(1000000000), () -> FloatingLong.create(10000000000l))
            .withSupportedUpgrades(Set.of(Upgrade.MUFFLING))
            .build();

    public static final BlockTypeMachine<BEAstralCrusher> ASTRAL_CRUSHER = BlockMachineBuilder
            .createMachine(() -> AstralMekanismMachines.ASTRAL_CRUSHER.getTileRO(), MekanismLang.DESCRIPTION_CRUSHER)
            .withEnergyConfig(() -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                    () -> FloatingLong.MAX_VALUE)
            .withGui(() -> AstralMekanismMachines.ASTRAL_CRUSHER.getContainerRO())
            .withSound(MekanismSounds.CRUSHER)
            .build();

    public static final BlockTypeMachine<BEAstralEnergizedSmelter> ASTRAL_ENERGIZED_SMELTER = BlockMachineBuilder
            .createMachine(() -> AstralMekanismMachines.ASTRAL_ENERGIZED_SMELTER.getTileRO(),
                    MekanismLang.DESCRIPTION_ENERGIZED_SMELTER)
            .withEnergyConfig(() -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                    () -> FloatingLong.MAX_VALUE)
            .withGui(() -> AstralMekanismMachines.ASTRAL_ENERGIZED_SMELTER.getContainerRO())
            .withSound(MekanismSounds.ENERGIZED_SMELTER)
            .build();

    public static final BlockTypeMachine<BEAstralEnrichmentChamber> ASTRAL_ENRICHMENT_CHAMBER = BlockMachineBuilder
            .createMachine(() -> AstralMekanismMachines.ASTRAL_ENRICHMENT_CHAMBER.getTileRO(),
                    MekanismLang.DESCRIPTION_ENRICHMENT_CHAMBER)
            .withEnergyConfig(() -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                    () -> FloatingLong.MAX_VALUE)
            .withGui(() -> AstralMekanismMachines.ASTRAL_ENRICHMENT_CHAMBER.getContainerRO())
            .withSound(MekanismSounds.ENRICHMENT_CHAMBER)
            .build();

    public static final BlockTypeMachine<BEAstralMekanicalCharger> ASTRAL_MEKANICAL_CHARGER = BlockMachineBuilder
            .createMachine(() -> AstralMekanismMachines.ASTRAL_MEKANICAL_CHARGER.getTileRO(),
                    AstralMekanismLang.DESCRIPTION_MEKANICAL_CHARGER)
            .withEnergyConfig(() -> FloatingLong.create(800 * AstralMekanismConfig.energyRate),
                    () -> FloatingLong.MAX_VALUE)
            .withGui(() -> AstralMekanismMachines.ASTRAL_MEKANICAL_CHARGER.getContainerRO())
            .withCustomShape(AMBlockShapes.MEKANICAL_CHARGER)
            .build();

    public static final BlockTypeMachine<BEAstralChemicalInjectionChamber> ASTRAL_CHEMICAL_INJECTION_CHAMBER = BlockMachineBuilder
            .createMachine(() -> AstralMekanismTileEntityTypes.ASTRAL_CHEMICAL_INJECTION_CHAMBER,
                    MekanismLang.DESCRIPTION_CHEMICAL_INJECTION_CHAMBER)
            .withEnergyConfig(() -> FloatingLong.create(20000 * AstralMekanismConfig.energyRate),
                    () -> FloatingLong.MAX_VALUE)
            .withSupportedUpgrades(EnumSet.of(Upgrade.ENERGY, Upgrade.SPEED, Upgrade.MUFFLING, Upgrade.GAS))
            .withGui(() -> AstralMekanismContainers.ASTRAL_CHEMICAL_INJECTION_CHAMBER)
            .withSound(MekanismSounds.CHEMICAL_INJECTION_CHAMBER)
            .build();

    public static final BlockTypeMachine<BEAstralOsmiumCompressor> ASTRAL_OSMIUM_COMPRESSOR = BlockMachineBuilder
            .createMachine(() -> AstralMekanismTileEntityTypes.ASTRAL_OSMIUM_COMPRESSOR,
                    MekanismLang.DESCRIPTION_OSMIUM_COMPRESSOR)
            .withEnergyConfig(() -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                    () -> FloatingLong.MAX_VALUE)
            .withGui(() -> AstralMekanismContainers.ASTRAL_OSMIUM_COMPRESSOR)
            .withSound(MekanismSounds.OSMIUM_COMPRESSOR)
            .build();

    public static final BlockTypeMachine<BEAstralPurificationChamber> ASTRAL_PURIFICATION_CHAMBER = BlockMachineBuilder
            .createMachine(() -> AstralMekanismTileEntityTypes.ASTRAL_PURIFICATION_CHAMBER,
                    MekanismLang.DESCRIPTION_PURIFICATION_CHAMBER)
            .withEnergyConfig(() -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                    () -> FloatingLong.MAX_VALUE)
            .withSupportedUpgrades(EnumSet.of(Upgrade.ENERGY, Upgrade.SPEED, Upgrade.MUFFLING, Upgrade.GAS))
            .withGui(() -> AstralMekanismContainers.ASTRAL_PURIFICATION_CHAMBER)
            .withSound(MekanismSounds.PURIFICATION_CHAMBER)
            .build();

    public static final BlockTypeTile<BEAstralGNA> ASTRAL_GNA = BlockTileBuilder
            .createBlock(() -> AstralMekanismMachines.ASTRAL_GNA.getTileRO(),
                    AstralMekanismLang.DESCRIPTION_GLOWSTONE_NEUTRON_ACTIVATOR)
            .withGui(() -> AstralMekanismMachines.ASTRAL_GNA.getContainerRO())
            .withCustomShape(AMBlockShapes.GLOWSTONE_NEUTRON_ACTIVATOR)
            .with(new AttributeStateFacing())
            .build();

    public static final BlockTypeMachine<BEAstralSPS> ASTRAL_SPS = BlockMachineBuilder
            .createMachine(() -> AstralMekanismMachines.ASTRAL_SPS.getTileRO(), MekanismLang.DESCRIPTION_SPS_CASING)
            .withGui(() -> AstralMekanismMachines.ASTRAL_SPS.getContainerRO())
            .withEnergyConfig(() -> FloatingLong.create(1000000000), () -> FloatingLong.MAX_VALUE)
            .withSupportedUpgrades(Set.of(Upgrade.MUFFLING))
            .build();

    public static final BlockTypeMachine<BEAstralPRC> ASTRAL_PRC = BlockMachineBuilder
            .createMachine(() -> AstralMekanismMachines.ASTRAL_PRC.getTileRO(),
                    MekanismLang.DESCRIPTION_PRESSURIZED_REACTION_CHAMBER)
            .withGui(() -> AstralMekanismMachines.ASTRAL_PRC.getContainerRO())
            .withEnergyConfig(MekanismConfig.usage.pressurizedReactionBase, () -> FloatingLong.MAX_VALUE)
            .withSound(MekanismSounds.PRESSURIZED_REACTION_CHAMBER)
            .withCustomShape(AMBlockShapes.ASTRAL_PRC)
            .build();

    public static final BlockTypeMachine<BEAstralGreenHouse> ASTRAL_GREENHOUSE = BlockMachineBuilder
            .createMachine(() -> AstralMekanismMachines.ASTRAL_GREENHOUSE.getTileRO(),
                    AstralMekanismLang.DESCRIPTION_GREENHOUSE)
            .withGui(() -> AstralMekanismMachines.ASTRAL_GREENHOUSE.getContainerRO())
            .withEnergyConfig(() -> FloatingLong.create(1000 * AstralMekanismConfig.energyRate),
                    () -> FloatingLong.MAX_VALUE)
            .withCustomShape(AMBlockShapes.ASTRAL_GREENHOUSE)
            .build();

    public static final BlockTypeMachine<BEAstralElectrolyticSeparator> ASTRAL_ELECTROLYTIC_SEPARATOR = BlockMachineBuilder
            .createMachine(() -> AstralMekanismMachines.ASTRAL_ELECTROLYTIC_SEPARATOR.getTileRO(),
                    MekanismLang.DESCRIPTION_ELECTROLYTIC_SEPARATOR)
            .withGui(() -> AstralMekanismMachines.ASTRAL_ELECTROLYTIC_SEPARATOR.getContainerRO())
            .withEnergyConfig(() -> MekanismConfig.general.FROM_H2.get().multiply(2),
                    () -> FloatingLong.MAX_VALUE)
            .withCustomShape(mekanism.common.content.blocktype.BlockShapes.ELECTROLYTIC_SEPARATOR)
            .build();

    public static final BlockTypeMachine<BEAstralPrecisionSawmill> ASTRAL_PRECISION_SAWMILL = BlockMachineBuilder
            .createMachine(() -> AstralMekanismMachines.ASTRAL_PRECISION_SAWMILL.getTileRO(),
                    MekanismLang.DESCRIPTION_PRECISION_SAWMILL)
            .withGui(() -> AstralMekanismMachines.ASTRAL_PRECISION_SAWMILL.getContainerRO())
            .withEnergyConfig(MekanismConfig.usage.precisionSawmill, () -> FloatingLong.MAX_VALUE)
            .withCustomShape(AMBlockShapes.ASTRAL_MACHINE_BASIC)
            .build();
}
