package astral_mekanism.registries;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.function.UnaryOperator;

import astral_mekanism.AstralMekanismConfig;
import astral_mekanism.AstralMekanismID;
import astral_mekanism.AstralMekanismLang;
import astral_mekanism.block.blockentity.astralmachine.BEAstralChemicalInfuser;
import astral_mekanism.block.blockentity.astralmachine.BEAstralChemicalOxidizer;
import astral_mekanism.block.blockentity.astralmachine.BEAstralChemicalWasher;
import astral_mekanism.block.blockentity.astralmachine.BEAstralCombiner;
import astral_mekanism.block.blockentity.astralmachine.BEAstralCrystallizer;
import astral_mekanism.block.blockentity.astralmachine.BEAstralDissolutionChamber;
import astral_mekanism.block.blockentity.astralmachine.BEAstralElectrolyticSeparator;
import astral_mekanism.block.blockentity.astralmachine.BEAstralGNA;
import astral_mekanism.block.blockentity.astralmachine.BEAstralGreenhouse;
import astral_mekanism.block.blockentity.astralmachine.BEAstralIsotopicCentrifuge;
import astral_mekanism.block.blockentity.astralmachine.BEAstralMekanicalInscriber;
import astral_mekanism.block.blockentity.astralmachine.BEAstralMekanicalPresser;
import astral_mekanism.block.blockentity.astralmachine.BEAstralMekanicalTransformer;
import astral_mekanism.block.blockentity.astralmachine.BEAstralMetallurgicInfuser;
import astral_mekanism.block.blockentity.astralmachine.BEAstralPRC;
import astral_mekanism.block.blockentity.astralmachine.BEAstralPrecisionSawmill;
import astral_mekanism.block.blockentity.astralmachine.BEAstralRotaryCondensentrator;
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
import astral_mekanism.block.blockentity.normalmachine.BEGreenhouse;
import astral_mekanism.block.blockentity.normalmachine.BEInfuseSynthesizer;
import astral_mekanism.block.blockentity.normalmachine.BEMekanicalCharger;
import astral_mekanism.block.blockentity.normalmachine.BEMekanicalInscriber;
import astral_mekanism.block.blockentity.normalmachine.BEMekanicalPresser;
import astral_mekanism.block.blockentity.normalmachine.BEMekanicalTransformer;
import astral_mekanism.block.blockentity.other.BEItemSortableStorage;
import astral_mekanism.block.blockentity.other.BEUniversalStorage;
import astral_mekanism.block.container.normal_machine.ContainerAstralCrafter;
import astral_mekanism.block.container.other.ContainerItemSortableStorage;
import astral_mekanism.block.container.prefab.ContainerAbstractStorage;
import astral_mekanism.block.shape.AMBlockShapes;
import astral_mekanism.registration.BlockTypeMachine;
import astral_mekanism.registration.MachineDeferredRegister;
import astral_mekanism.registration.MachineRegistryObject;
import mekanism.api.Upgrade;
import mekanism.api.math.FloatingLong;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.AttributeTier;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.config.MekanismConfig;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.registries.MekanismSounds;
import mekanism.generators.common.GeneratorsLang;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;

public class AstralMekanismMachines {
    public static final MachineDeferredRegister MACHINES = new MachineDeferredRegister(AstralMekanismID.MODID);

    private static final UnaryOperator<Properties> normalOperator = p -> p
            .strength(1.5f, 6.0f).sound(SoundType.STONE).mapColor(MapColor.STONE);

    public static final MachineRegistryObject<BEAstralChemicalInjectionChamber, BlockTileModel<BEAstralChemicalInjectionChamber, BlockTypeMachine<BEAstralChemicalInjectionChamber>>, MekanismTileContainer<BEAstralChemicalInjectionChamber>, ItemBlockMachine> ASTRAL_CHEMICAL_INJECTION_CHAMBER = MACHINES
            .regSimple("astral_chemical_injection_chamber",
                    BEAstralChemicalInjectionChamber::new,
                    BEAstralChemicalInjectionChamber.class,
                    MekanismLang.DESCRIPTION_CHEMICAL_INJECTION_CHAMBER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(20000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.CHEMICAL_INJECTION_CHAMBER));

    public static final MachineRegistryObject<BEAstralOsmiumCompressor, BlockTileModel<BEAstralOsmiumCompressor, BlockTypeMachine<BEAstralOsmiumCompressor>>, MekanismTileContainer<BEAstralOsmiumCompressor>, ItemBlockMachine> ASTRAL_OSMIUM_COMPRESSOR = MACHINES
            .regSimple("astral_osmium_compressor",
                    BEAstralOsmiumCompressor::new,
                    BEAstralOsmiumCompressor.class,
                    MekanismLang.DESCRIPTION_OSMIUM_COMPRESSOR,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.OSMIUM_COMPRESSOR));

    public static final MachineRegistryObject<BEAstralPurificationChamber, BlockTileModel<BEAstralPurificationChamber, BlockTypeMachine<BEAstralPurificationChamber>>, MekanismTileContainer<BEAstralPurificationChamber>, ItemBlockMachine> ASTRAL_PURIFICATION_CHAMBER = MACHINES
            .regSimple("astral_purification_chamber",
                    BEAstralPurificationChamber::new,
                    BEAstralPurificationChamber.class,
                    MekanismLang.DESCRIPTION_PURIFICATION_CHAMBER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.PURIFICATION_CHAMBER));

    public static final MachineRegistryObject<BEAstralCrusher, BlockTileModel<BEAstralCrusher, BlockTypeMachine<BEAstralCrusher>>, MekanismTileContainer<BEAstralCrusher>, ItemBlockMachine> ASTRAL_CRUSHER = MACHINES
            .regSimple("astral_crusher",
                    BEAstralCrusher::new,
                    BEAstralCrusher.class,
                    MekanismLang.DESCRIPTION_CRUSHER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.CRUSHER));

    public static final MachineRegistryObject<BEAstralEnergizedSmelter, BlockTileModel<BEAstralEnergizedSmelter, BlockTypeMachine<BEAstralEnergizedSmelter>>, MekanismTileContainer<BEAstralEnergizedSmelter>, ItemBlockMachine> ASTRAL_ENERGIZED_SMELTER = MACHINES
            .regSimple("astral_energized_smelter",
                    BEAstralEnergizedSmelter::new,
                    BEAstralEnergizedSmelter.class,
                    MekanismLang.DESCRIPTION_ENERGIZED_SMELTER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.ENERGIZED_SMELTER));

    public static final MachineRegistryObject<BEAstralEnrichmentChamber, BlockTileModel<BEAstralEnrichmentChamber, BlockTypeMachine<BEAstralEnrichmentChamber>>, MekanismTileContainer<BEAstralEnrichmentChamber>, ItemBlockMachine> ASTRAL_ENRICHMENT_CHAMBER = MACHINES
            .regSimple("astral_enrichment_chamber",
                    BEAstralEnrichmentChamber::new,
                    BEAstralEnrichmentChamber.class,
                    MekanismLang.DESCRIPTION_ENRICHMENT_CHAMBER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.ENRICHMENT_CHAMBER));

    public static final MachineRegistryObject<BEAstralMekanicalCharger, BlockTileModel<BEAstralMekanicalCharger, BlockTypeMachine<BEAstralMekanicalCharger>>, MekanismTileContainer<BEAstralMekanicalCharger>, ItemBlockMachine> ASTRAL_MEKANICAL_CHARGER = MACHINES
            .regSimple("astral_mekanical_charger",
                    BEAstralMekanicalCharger::new,
                    BEAstralMekanicalCharger.class,
                    AstralMekanismLang.DESCRIPTION_MEKANICAL_CHARGER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(800 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withCustomShape(AMBlockShapes.MEKANICAL_CHARGER));

    public static final MachineRegistryObject<BEAstralChemicalInfuser, BlockTileModel<BEAstralChemicalInfuser, BlockTypeMachine<BEAstralChemicalInfuser>>, MekanismTileContainer<BEAstralChemicalInfuser>, ItemBlockMachine> ASTRAL_CHEMICAL_INFUSER = MACHINES
            .regSimple("astral_chemical_infuser",
                    BEAstralChemicalInfuser::new,
                    BEAstralChemicalInfuser.class,
                    MekanismLang.DESCRIPTION_CHEMICAL_INFUSER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.CHEMICAL_INFUSER));

    public static final MachineRegistryObject<BEAstralChemicalOxidizer, BlockTileModel<BEAstralChemicalOxidizer, BlockTypeMachine<BEAstralChemicalOxidizer>>, MekanismTileContainer<BEAstralChemicalOxidizer>, ItemBlockMachine> ASTRAL_CHEMICAL_OXIDIZER = MACHINES
            .regSimple("astral_chemical_oxidizer",
                    BEAstralChemicalOxidizer::new,
                    BEAstralChemicalOxidizer.class,
                    MekanismLang.DESCRIPTION_CHEMICAL_OXIDIZER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.CHEMICAL_OXIDIZER));

    public static final MachineRegistryObject<BEAstralChemicalWasher, BlockTileModel<BEAstralChemicalWasher, BlockTypeMachine<BEAstralChemicalWasher>>, MekanismTileContainer<BEAstralChemicalWasher>, ItemBlockMachine> ASTRAL_CHEMICAL_WASHER = MACHINES
            .regSimple("astral_chemical_washer",
                    BEAstralChemicalWasher::new,
                    BEAstralChemicalWasher.class,
                    MekanismLang.DESCRIPTION_CHEMICAL_WASHER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.CHEMICAL_WASHER));

    public static final MachineRegistryObject<BEAstralCombiner, BlockTileModel<BEAstralCombiner, BlockTypeMachine<BEAstralCombiner>>, MekanismTileContainer<BEAstralCombiner>, ItemBlockMachine> ASTRAL_COMBINER = MACHINES
            .regSimple("astral_combiner",
                    BEAstralCombiner::new,
                    BEAstralCombiner.class,
                    MekanismLang.DESCRIPTION_COMBINER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.COMBINER));

    public static final MachineRegistryObject<BEAstralCrystallizer, BlockTileModel<BEAstralCrystallizer, BlockTypeMachine<BEAstralCrystallizer>>, MekanismTileContainer<BEAstralCrystallizer>, ItemBlockMachine> ASTRAL_CRYSTALLIZER = MACHINES
            .regSimple("astral_crystallizer",
                    BEAstralCrystallizer::new,
                    BEAstralCrystallizer.class,
                    MekanismLang.DESCRIPTION_CHEMICAL_CRYSTALLIZER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.CHEMICAL_CRYSTALLIZER));

    public static final MachineRegistryObject<BEAstralDissolutionChamber, BlockTileModel<BEAstralDissolutionChamber, BlockTypeMachine<BEAstralDissolutionChamber>>, MekanismTileContainer<BEAstralDissolutionChamber>, ItemBlockMachine> ASTRAL_DISSOLUTION_CHAMBER = MACHINES
            .regSimple("astral_dissolution_chamber",
                    BEAstralDissolutionChamber::new,
                    BEAstralDissolutionChamber.class,
                    MekanismLang.DESCRIPTION_CHEMICAL_DISSOLUTION_CHAMBER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.CHEMICAL_DISSOLUTION_CHAMBER));

    public static final MachineRegistryObject<BEAstralElectrolyticSeparator, BlockTileModel<BEAstralElectrolyticSeparator, BlockTypeMachine<BEAstralElectrolyticSeparator>>, MekanismTileContainer<BEAstralElectrolyticSeparator>, ItemBlockMachine> ASTRAL_ELECTROLYTIC_SEPARATOR = MACHINES
            .regSimple("astral_electrolytic_separator",
                    BEAstralElectrolyticSeparator::new,
                    BEAstralElectrolyticSeparator.class,
                    MekanismLang.DESCRIPTION_ELECTROLYTIC_SEPARATOR,
                    builder -> builder.withEnergyConfig(
                            () -> MekanismConfig.general.FROM_H2.get().multiply(2),
                            () -> FloatingLong.MAX_VALUE)
                            .withCustomShape(mekanism.common.content.blocktype.BlockShapes.ELECTROLYTIC_SEPARATOR)
                            .withSound(MekanismSounds.ELECTROLYTIC_SEPARATOR));

    public static final MachineRegistryObject<BEAstralGNA, BlockTileModel<BEAstralGNA, BlockTypeMachine<BEAstralGNA>>, MekanismTileContainer<BEAstralGNA>, ItemBlockMachine> ASTRAL_GNA = MACHINES
            .regSimple("astral_gna",
                    BEAstralGNA::new,
                    BEAstralGNA.class,
                    MekanismLang.DESCRIPTION_CHEMICAL_DISSOLUTION_CHAMBER,
                    builder -> builder
                            .withCustomShape(AMBlockShapes.GLOWSTONE_NEUTRON_ACTIVATOR));

    public static final MachineRegistryObject<BEAstralGreenhouse, BlockTileModel<BEAstralGreenhouse, BlockTypeMachine<BEAstralGreenhouse>>, MekanismTileContainer<BEAstralGreenhouse>, ItemBlockMachine> ASTRAL_GREENHOUSE = MACHINES
            .regSimple("astral_greenhouse",
                    BEAstralGreenhouse::new,
                    BEAstralGreenhouse.class,
                    AstralMekanismLang.DESCRIPTION_GREENHOUSE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE));

    public static final MachineRegistryObject<BEAstralIsotopicCentrifuge, BlockTileModel<BEAstralIsotopicCentrifuge, BlockTypeMachine<BEAstralIsotopicCentrifuge>>, MekanismTileContainer<BEAstralIsotopicCentrifuge>, ItemBlockMachine> ASTRAL_ISOTOPIC_CENTRIFUGE = MACHINES
            .regSimple("astral_isotopic_centrifuge",
                    BEAstralIsotopicCentrifuge::new,
                    BEAstralIsotopicCentrifuge.class,
                    MekanismLang.DESCRIPTION_ISOTOPIC_CENTRIFUGE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.ISOTOPIC_CENTRIFUGE));

    public static final MachineRegistryObject<BEAstralMekanicalInscriber, BlockTileModel<BEAstralMekanicalInscriber, BlockTypeMachine<BEAstralMekanicalInscriber>>, MekanismTileContainer<BEAstralMekanicalInscriber>, ItemBlockMachine> ASTRAL_MEKANICAL_INSCRIBER = MACHINES
            .regSimple("astral_mekanical_inscriber",
                    BEAstralMekanicalInscriber::new,
                    BEAstralMekanicalInscriber.class,
                    MekanismLang.DESCRIPTION_CHEMICAL_CRYSTALLIZER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE));

    public static final MachineRegistryObject<BEAstralMekanicalPresser, BlockTileModel<BEAstralMekanicalPresser, BlockTypeMachine<BEAstralMekanicalPresser>>, MekanismTileContainer<BEAstralMekanicalPresser>, ItemBlockMachine> ASTRAL_MEKANICAL_PRESSER = MACHINES
            .regSimple("astral_mekanical_presser",
                    BEAstralMekanicalPresser::new,
                    BEAstralMekanicalPresser.class,
                    MekanismLang.DESCRIPTION_CHEMICAL_CRYSTALLIZER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE));

    public static final MachineRegistryObject<BEAstralMekanicalTransformer, BlockTileModel<BEAstralMekanicalTransformer, BlockTypeMachine<BEAstralMekanicalTransformer>>, MekanismTileContainer<BEAstralMekanicalTransformer>, ItemBlockMachine> ASTRAL_MEKANICAL_TRANSFOMER = MACHINES
            .regSimple("astral_mekanical_transformer",
                    BEAstralMekanicalTransformer::new,
                    BEAstralMekanicalTransformer.class,
                    MekanismLang.DESCRIPTION_CHEMICAL_CRYSTALLIZER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE));

    public static final MachineRegistryObject<BEAstralMetallurgicInfuser, BlockTileModel<BEAstralMetallurgicInfuser, BlockTypeMachine<BEAstralMetallurgicInfuser>>, MekanismTileContainer<BEAstralMetallurgicInfuser>, ItemBlockMachine> ASTRAL_METALLURGIC_INFUSER = MACHINES
            .regSimple("astral_metallurgic_infuser",
                    BEAstralMetallurgicInfuser::new,
                    BEAstralMetallurgicInfuser.class,
                    MekanismLang.DESCRIPTION_CHEMICAL_CRYSTALLIZER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.METALLURGIC_INFUSER));

    public static final MachineRegistryObject<BEAstralPRC, BlockTileModel<BEAstralPRC, BlockTypeMachine<BEAstralPRC>>, MekanismTileContainer<BEAstralPRC>, ItemBlockMachine> ASTRAL_PRC = MACHINES
            .regSimple("astral_prc",
                    BEAstralPRC::new,
                    BEAstralPRC.class,
                    MekanismLang.DESCRIPTION_PRESSURIZED_REACTION_CHAMBER,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.pressurizedReactionBase,
                                    () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.PRESSURIZED_REACTION_CHAMBER)
                            .withCustomShape(AMBlockShapes.ASTRAL_PRC));

    public static final MachineRegistryObject<BEAstralPrecisionSawmill, BlockTileModel<BEAstralPrecisionSawmill, BlockTypeMachine<BEAstralPrecisionSawmill>>, MekanismTileContainer<BEAstralPrecisionSawmill>, ItemBlockMachine> ASTRAL_PRECISION_SAWMILL = MACHINES
            .regSimple("astral_precision_sawmill",
                    BEAstralPrecisionSawmill::new,
                    BEAstralPrecisionSawmill.class,
                    MekanismLang.DESCRIPTION_CHEMICAL_CRYSTALLIZER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.PRECISION_SAWMILL));

    public static final MachineRegistryObject<BEAstralRotaryCondensentrator, BlockTileModel<BEAstralRotaryCondensentrator, BlockTypeMachine<BEAstralRotaryCondensentrator>>, MekanismTileContainer<BEAstralRotaryCondensentrator>, ItemBlockMachine> ASTRAL_ROTARY_CONDENSENTRATOR = MACHINES
            .regSimple("astral_rotary_condensentrator",
                    BEAstralRotaryCondensentrator::new,
                    BEAstralRotaryCondensentrator.class,
                    MekanismLang.DESCRIPTION_CHEMICAL_CRYSTALLIZER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.ROTARY_CONDENSENTRATOR));

    public static final MachineRegistryObject<BEAstralSPS, BlockTileModel<BEAstralSPS, BlockTypeMachine<BEAstralSPS>>, MekanismTileContainer<BEAstralSPS>, ItemBlockMachine> ASTRAL_SPS = MACHINES
            .regSimple("astral_sps",
                    BEAstralSPS::new,
                    BEAstralSPS.class,
                    MekanismLang.SPS,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(1000000000),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.SPS)
                            .removeAttributeUpgrade()
                            .withSupportedUpgrades(EnumSet.of(Upgrade.MUFFLING)));

    public static final MachineRegistryObject<BECompactFIR, BlockTileModel<BECompactFIR, BlockTypeMachine<BECompactFIR>>, MekanismTileContainer<BECompactFIR>, ItemBlockMachine> COMPACT_FIR = MACHINES
            .regSimple("compact_fir",
                    BECompactFIR::new,
                    BECompactFIR.class,
                    GeneratorsLang.FUSION_REACTOR,
                    builder -> builder);

    public static final MachineRegistryObject<BECompactSPS, BlockTileModel<BECompactSPS, BlockTypeMachine<BECompactSPS>>, MekanismTileContainer<BECompactSPS>, ItemBlockMachine> COMPACT_SPS = MACHINES
            .regSimple("compact_sps",
                    BECompactSPS::new,
                    BECompactSPS.class,
                    MekanismLang.SPS,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(1000000000),
                            () -> FloatingLong.create(10000000000l))
                            .withSound(MekanismSounds.SPS)
                            .removeAttributeUpgrade()
                            .withSupportedUpgrades(EnumSet.of(Upgrade.MUFFLING)));

    public static final MachineRegistryObject<BECompactTEP, BlockTileModel<BECompactTEP, BlockTypeMachine<BECompactTEP>>, MekanismTileContainer<BECompactTEP>, ItemBlockMachine> COMPACT_TEP = MACHINES
            .regSimple("compact_tep",
                    BECompactTEP::new,
                    BECompactTEP.class,
                    MekanismLang.EVAPORATION_PLANT,
                    builder -> builder.removeAttributeUpgrade());

    public static final EnumMap<AstralMekGeneratorTier, MachineRegistryObject<BEGasBurningGenerator, BlockTileModel<BEGasBurningGenerator, BlockTypeMachine<BEGasBurningGenerator>>, MekanismTileContainer<BEGasBurningGenerator>, ItemBlockMachine>> GAS_BURNING_GENERATORS = MACHINES
            .regSimpleMap(k -> k.name + "_gas_burning_generator",
                    BEGasBurningGenerator::new,
                    BEGasBurningGenerator.class,
                    GeneratorsLang.DESCRIPTION_GAS_BURNING_GENERATOR,
                    (key, btm) -> btm.with(new AttributeTier<>(key))
                            .withCustomShape(AMBlockShapes.GAS_BURNING_GENERATOR)
                            .removeAttributeUpgrade()
                            .withSupportedUpgrades(EnumSet.of(Upgrade.MUFFLING)),
                    AstralMekGeneratorTier.class);

    public static final EnumMap<AstralMekGeneratorTier, MachineRegistryObject<BEHeatGenerator, BlockTileModel<BEHeatGenerator, BlockTypeMachine<BEHeatGenerator>>, MekanismTileContainer<BEHeatGenerator>, ItemBlockMachine>> HEAT_GENERATORS = MACHINES
            .regSimpleMap(k -> k.name + "_heat_generator",
                    BEHeatGenerator::new,
                    BEHeatGenerator.class,
                    GeneratorsLang.DESCRIPTION_HEAT_GENERATOR,
                    (key, btm) -> btm.with(new AttributeTier<>(key))
                            .withCustomShape(AMBlockShapes.HEAT_GENERATOR)
                            .removeAttributeUpgrade()
                            .withSupportedUpgrades(EnumSet.of(Upgrade.MUFFLING)),
                    AstralMekGeneratorTier.class);

    public static final MachineRegistryObject<BEAstralCrafter, BlockTileModel<BEAstralCrafter, BlockTypeMachine<BEAstralCrafter>>, ContainerAstralCrafter, ItemBlockMachine> ASTRAL_CRAFTER = MACHINES
            .registerDefaultBlockItem("astral_crafter",
                    BEAstralCrafter::new,
                    BEAstralCrafter.class,
                    ContainerAstralCrafter::new,
                    AstralMekanismLang.ITEM_GROUP,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(400 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.create(10000000 * AstralMekanismConfig.energyRate)));

    public static final MachineRegistryObject<BEFluidInfuser, BlockTileModel<BEFluidInfuser, BlockTypeMachine<BEFluidInfuser>>, MekanismTileContainer<BEFluidInfuser>, ItemBlockMachine> FLUID_INFUSER = MACHINES
            .regSimple("fluid_infuser",
                    BEFluidInfuser::new,
                    BEFluidInfuser.class,
                    AstralMekanismLang.DESCRIPTION_FLUID_INFUSER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(1000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.create(10000000))
                            .withCustomShape(AMBlockShapes.FLUID_INFUSER));

    public static final MachineRegistryObject<BEGlowstoneNeutronActivator, BlockTileModel<BEGlowstoneNeutronActivator, BlockTypeMachine<BEGlowstoneNeutronActivator>>, MekanismTileContainer<BEGlowstoneNeutronActivator>, ItemBlockMachine> GLOWSTONE_NEUTRON_ACTIVATOR = MACHINES
            .regSimple("glowstone_neutron_activator",
                    BEGlowstoneNeutronActivator::new,
                    BEGlowstoneNeutronActivator.class,
                    AstralMekanismLang.DESCRIPTION_GLOWSTONE_NEUTRON_ACTIVATOR,
                    builder -> builder
                            .withCustomShape(AMBlockShapes.GLOWSTONE_NEUTRON_ACTIVATOR)
                            .removeAttributeUpgrade());

    public static final MachineRegistryObject<BEGreenhouse, BlockTileModel<BEGreenhouse, BlockTypeMachine<BEGreenhouse>>, MekanismTileContainer<BEGreenhouse>, ItemBlockMachine> GREENHOUSE = MACHINES
            .regSimple("greenhouse",
                    BEGreenhouse::new,
                    BEGreenhouse.class,
                    AstralMekanismLang.DESCRIPTION_GREENHOUSE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.create(1000000l * AstralMekanismConfig.energyRate)));

    public static final MachineRegistryObject<BEInfuseSynthesizer, BlockTileModel<BEInfuseSynthesizer, BlockTypeMachine<BEInfuseSynthesizer>>, MekanismTileContainer<BEInfuseSynthesizer>, ItemBlockMachine> INFUSE_SYNTHESIZER = MACHINES
            .regSimple("infuse_synthesizer",
                    BEInfuseSynthesizer::new,
                    BEInfuseSynthesizer.class,
                    MekanismLang.EVAPORATION_PLANT,
                    builder -> builder.removeAttributeUpgrade());

    public static final MachineRegistryObject<BEMekanicalCharger, BlockTileModel<BEMekanicalCharger, BlockTypeMachine<BEMekanicalCharger>>, MekanismTileContainer<BEMekanicalCharger>, ItemBlockMachine> MEKANICAL_CHARGER = MACHINES
            .regSimple("mekanical_charger",
                    BEMekanicalCharger::new,
                    BEMekanicalCharger.class,
                    AstralMekanismLang.DESCRIPTION_MEKANICAL_CHARGER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(1000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.create(100000l * AstralMekanismConfig.energyRate))
                            .withCustomShape(AMBlockShapes.MEKANICAL_CHARGER));

    public static final MachineRegistryObject<BEMekanicalInscriber, BlockTileModel<BEMekanicalInscriber, BlockTypeMachine<BEMekanicalInscriber>>, MekanismTileContainer<BEMekanicalInscriber>, ItemBlockMachine> MEKANICAL_INSCRIBER = MACHINES
            .regSimple("mekanical_inscriber",
                    BEMekanicalInscriber::new,
                    BEMekanicalInscriber.class,
                    AstralMekanismLang.DESCRIPTION_MEKANICAL_CHARGER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(1000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.create(100000l * AstralMekanismConfig.energyRate)));

    public static final MachineRegistryObject<BEMekanicalPresser, BlockTileModel<BEMekanicalPresser, BlockTypeMachine<BEMekanicalPresser>>, MekanismTileContainer<BEMekanicalPresser>, ItemBlockMachine> MEKANICAL_PRESSER = MACHINES
            .regSimple("mekanical_presser",
                    BEMekanicalPresser::new,
                    BEMekanicalPresser.class,
                    AstralMekanismLang.DESCRIPTION_MEKANICAL_CHARGER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(1000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.create(100000l * AstralMekanismConfig.energyRate)));

    public static final MachineRegistryObject<BEMekanicalTransformer, BlockTileModel<BEMekanicalTransformer, BlockTypeMachine<BEMekanicalTransformer>>, MekanismTileContainer<BEMekanicalTransformer>, ItemBlockMachine> MEKANICAL_TRANSFORMER = MACHINES
            .regSimple("mekanical_transformer",
                    BEMekanicalTransformer::new,
                    BEMekanicalTransformer.class,
                    AstralMekanismLang.DESCRIPTION_MEKANICAL_CHARGER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(1000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.create(100000l * AstralMekanismConfig.energyRate)));

    public static final MachineRegistryObject<BEUniversalStorage, BlockTileModel<BEUniversalStorage, BlockTypeMachine<BEUniversalStorage>>, ContainerAbstractStorage<BEUniversalStorage>, ItemBlockMachine> UNIVERSAL_STORAGE = MACHINES
            .registerDefaultBlockItem("universal_storage",
                    BEUniversalStorage::new,
                    BEUniversalStorage.class,
                    ContainerAbstractStorage<BEUniversalStorage>::new,
                    AstralMekanismLang.DESCRIPTION_UNIVERSAL_STORAGE,
                    builder -> builder.removeAttributeUpgrade());

    public static final MachineRegistryObject<BEItemSortableStorage, BlockTileModel<BEItemSortableStorage, BlockTypeMachine<BEItemSortableStorage>>, ContainerItemSortableStorage<BEItemSortableStorage>, ItemBlockMachine> ITEM_SORTABLE_STORAGE = MACHINES
            .registerDefaultBlockItem("item_sortable_storage",
                    BEItemSortableStorage::new,
                    BEItemSortableStorage.class,
                    ContainerItemSortableStorage<BEItemSortableStorage>::new,
                    AstralMekanismLang.DESCRIPTION_UNIVERSAL_STORAGE,
                    builder -> builder.removeAttributeUpgrade());

}
