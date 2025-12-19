package astral_mekanism.registries;

import java.util.Map;
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
import astral_mekanism.block.blockentity.normalmachine.BEMelter;
import astral_mekanism.block.blockentity.other.BEItemSortableStorage;
import astral_mekanism.block.blockentity.other.BEUniversalStorage;
import astral_mekanism.block.container.normal_machine.ContainerAstralCrafter;
import astral_mekanism.block.container.other.ContainerItemSortableStorage;
import astral_mekanism.block.container.prefab.ContainerAbstractStorage;
import astral_mekanism.block.shape.AMBlockShapes;
import astral_mekanism.registration.BlockTypeMachine;
import astral_mekanism.registration.MachineDeferredRegister;
import astral_mekanism.registration.MachineRegistryObject;
import astral_mekanism.registration.MachineRegistryObject2;
import mekanism.api.math.FloatingLong;
import mekanism.common.MekanismLang;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.registries.MekanismSounds;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.material.MapColor;

public class AstralMekanismMachines {
    public static final MachineDeferredRegister MACHINES = new MachineDeferredRegister(AstralMekanismID.MODID);

    private static final UnaryOperator<Properties> normalOperator = p -> p
            .strength(1.5f, 6.0f).sound(SoundType.STONE).mapColor(MapColor.STONE);

    public static final MachineRegistryObject2<BEAstralChemicalInjectionChamber, BlockTileModel<BEAstralChemicalInjectionChamber, BlockTypeMachine<BEAstralChemicalInjectionChamber>>, MekanismTileContainer<BEAstralChemicalInjectionChamber>, ItemBlockMachine> ASTRAL_CHEMICAL_INJECTION_CHAMBER = MACHINES
            .regSimple("astral_chemical_injection_chamber",
                    BEAstralChemicalInjectionChamber::new,
                    BEAstralChemicalInjectionChamber.class,
                    MekanismLang.DESCRIPTION_CHEMICAL_INJECTION_CHAMBER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(20000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.CHEMICAL_INJECTION_CHAMBER));

    public static final MachineRegistryObject2<BEAstralOsmiumCompressor, BlockTileModel<BEAstralOsmiumCompressor, BlockTypeMachine<BEAstralOsmiumCompressor>>, MekanismTileContainer<BEAstralOsmiumCompressor>, ItemBlockMachine> ASTRAL_OSMIUM_COMPRESSOR = MACHINES
            .regSimple("astral_osmium_compressor",
                    BEAstralOsmiumCompressor::new,
                    BEAstralOsmiumCompressor.class,
                    MekanismLang.DESCRIPTION_OSMIUM_COMPRESSOR,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.OSMIUM_COMPRESSOR));

    public static final MachineRegistryObject2<BEAstralPurificationChamber, BlockTileModel<BEAstralPurificationChamber, BlockTypeMachine<BEAstralPurificationChamber>>, MekanismTileContainer<BEAstralPurificationChamber>, ItemBlockMachine> ASTRAL_PURIFICATION_CHAMBER = MACHINES
            .regSimple("astral_purification_chamber",
                    BEAstralPurificationChamber::new,
                    BEAstralPurificationChamber.class,
                    MekanismLang.DESCRIPTION_PURIFICATION_CHAMBER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.PURIFICATION_CHAMBER));

    public static final MachineRegistryObject2<BEAstralCrusher, BlockTileModel<BEAstralCrusher, BlockTypeMachine<BEAstralCrusher>>, MekanismTileContainer<BEAstralCrusher>, ItemBlockMachine> ASTRAL_CRUSHER = MACHINES
            .regSimple("astral_crusher",
                    BEAstralCrusher::new,
                    BEAstralCrusher.class,
                    MekanismLang.DESCRIPTION_CRUSHER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.CRUSHER));

    public static final MachineRegistryObject2<BEAstralEnergizedSmelter, BlockTileModel<BEAstralEnergizedSmelter, BlockTypeMachine<BEAstralEnergizedSmelter>>, MekanismTileContainer<BEAstralEnergizedSmelter>, ItemBlockMachine> ASTRAL_ENERGIZED_SMELTER = MACHINES
            .regSimple("astral_energized_smelter",
                    BEAstralEnergizedSmelter::new,
                    BEAstralEnergizedSmelter.class,
                    MekanismLang.DESCRIPTION_ENERGIZED_SMELTER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.ENERGIZED_SMELTER));

    public static final MachineRegistryObject2<BEAstralEnrichmentChamber, BlockTileModel<BEAstralEnrichmentChamber, BlockTypeMachine<BEAstralEnrichmentChamber>>, MekanismTileContainer<BEAstralEnrichmentChamber>, ItemBlockMachine> ASTRAL_ENRICHMENT_CHAMBER = MACHINES
            .regSimple("astral_enrichment_chamber",
                    BEAstralEnrichmentChamber::new,
                    BEAstralEnrichmentChamber.class,
                    MekanismLang.DESCRIPTION_ENRICHMENT_CHAMBER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.ENRICHMENT_CHAMBER));

    public static final MachineRegistryObject2<BEAstralMekanicalCharger, BlockTileModel<BEAstralMekanicalCharger, BlockTypeMachine<BEAstralMekanicalCharger>>, MekanismTileContainer<BEAstralMekanicalCharger>, ItemBlockMachine> ASTRAL_MEKANICAL_CHARGER = MACHINES
            .regSimple("astral_mekanical_charger",
                    BEAstralMekanicalCharger::new,
                    BEAstralMekanicalCharger.class,
                    AstralMekanismLang.DESCRIPTION_MEKANICAL_CHARGER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(800 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withCustomShape(AMBlockShapes.MEKANICAL_CHARGER));

    public static final MachineRegistryObject2<BEAstralChemicalInfuser, BlockTileModel<BEAstralChemicalInfuser, BlockTypeMachine<BEAstralChemicalInfuser>>, MekanismTileContainer<BEAstralChemicalInfuser>, ItemBlockMachine> ASTRAL_CHEMICAL_INFUSER = MACHINES
            .regSimple("astral_chemical_infuser",
                    BEAstralChemicalInfuser::new,
                    BEAstralChemicalInfuser.class,
                    MekanismLang.DESCRIPTION_CHEMICAL_INFUSER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.CHEMICAL_INFUSER));

    public static final MachineRegistryObject2<BEAstralChemicalOxidizer, BlockTileModel<BEAstralChemicalOxidizer, BlockTypeMachine<BEAstralChemicalOxidizer>>, MekanismTileContainer<BEAstralChemicalOxidizer>, ItemBlockMachine> ASTRAL_CHEMICAL_OXIDIZER = MACHINES
            .regSimple("astral_chemical_oxidizer",
                    BEAstralChemicalOxidizer::new,
                    BEAstralChemicalOxidizer.class,
                    MekanismLang.DESCRIPTION_CHEMICAL_OXIDIZER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.CHEMICAL_OXIDIZER));

    public static final MachineRegistryObject2<BEAstralChemicalWasher, BlockTileModel<BEAstralChemicalWasher, BlockTypeMachine<BEAstralChemicalWasher>>, MekanismTileContainer<BEAstralChemicalWasher>, ItemBlockMachine> ASTRAL_CHEMICAL_WASHER = MACHINES
            .regSimple("astral_chemical_washer",
                    BEAstralChemicalWasher::new,
                    BEAstralChemicalWasher.class,
                    MekanismLang.DESCRIPTION_CHEMICAL_WASHER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.CHEMICAL_WASHER));

    public static final MachineRegistryObject2<BEAstralCombiner, BlockTileModel<BEAstralCombiner, BlockTypeMachine<BEAstralCombiner>>, MekanismTileContainer<BEAstralCombiner>, ItemBlockMachine> ASTRAL_COMBINER = MACHINES
            .regSimple("astral_combiner",
                    BEAstralCombiner::new,
                    BEAstralCombiner.class,
                    MekanismLang.DESCRIPTION_COMBINER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.COMBINER));

    public static final MachineRegistryObject2<BEAstralCrystallizer, BlockTileModel<BEAstralCrystallizer, BlockTypeMachine<BEAstralCrystallizer>>, MekanismTileContainer<BEAstralCrystallizer>, ItemBlockMachine> ASTRAL_CRYSTALLIZER = MACHINES
            .regSimple("astral_crystallizer",
                    BEAstralCrystallizer::new,
                    BEAstralCrystallizer.class,
                    MekanismLang.DESCRIPTION_CHEMICAL_CRYSTALLIZER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.CHEMICAL_CRYSTALLIZER));

    public static final MachineRegistryObject2<BEAstralDissolutionChamber, BlockTileModel<BEAstralDissolutionChamber, BlockTypeMachine<BEAstralDissolutionChamber>>, MekanismTileContainer<BEAstralDissolutionChamber>, ItemBlockMachine> ASTRAL_DISSOLUTION_CHAMBER = MACHINES
            .regSimple("astral_dissolution_chamber",
                    BEAstralDissolutionChamber::new,
                    BEAstralDissolutionChamber.class,
                    MekanismLang.DESCRIPTION_CHEMICAL_DISSOLUTION_CHAMBER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.CHEMICAL_DISSOLUTION_CHAMBER));

    public static final MachineRegistryObject2<BEAstralElectrolyticSeparator, BlockTileModel<BEAstralElectrolyticSeparator, BlockTypeMachine<BEAstralElectrolyticSeparator>>, MekanismTileContainer<BEAstralElectrolyticSeparator>, ItemBlockMachine> ASTRAL_ELECTROLYTIC_SEPARATOR = MACHINES
            .regSimple("astral_electrolytic_separator",
                    BEAstralElectrolyticSeparator::new,
                    BEAstralElectrolyticSeparator.class,
                    MekanismLang.DESCRIPTION_ELECTROLYTIC_SEPARATOR,
                    builder -> builder.withEnergyConfig(
                            () -> MekanismConfig.general.FROM_H2.get().multiply(2),
                            () -> FloatingLong.MAX_VALUE)
                            .withCustomShape(mekanism.common.content.blocktype.BlockShapes.ELECTROLYTIC_SEPARATOR)
                            .withSound(MekanismSounds.ELECTROLYTIC_SEPARATOR));

    public static final MachineRegistryObject2<BEAstralGNA, BlockTileModel<BEAstralGNA, BlockTypeMachine<BEAstralGNA>>, MekanismTileContainer<BEAstralGNA>, ItemBlockMachine> ASTRAL_GNA = MACHINES
            .regSimple("astral_gna",
                    BEAstralGNA::new,
                    BEAstralGNA.class,
                    MekanismLang.DESCRIPTION_CHEMICAL_DISSOLUTION_CHAMBER,
                    builder -> builder
                            .withCustomShape(AMBlockShapes.GLOWSTONE_NEUTRON_ACTIVATOR));

    public static final MachineRegistryObject2<BEAstralGreenhouse, BlockTileModel<BEAstralGreenhouse, BlockTypeMachine<BEAstralGreenhouse>>, MekanismTileContainer<BEAstralGreenhouse>, ItemBlockMachine> ASTRAL_GREENHOUSE = MACHINES
            .regSimple("astral_greenhouse",
                    BEAstralGreenhouse::new,
                    BEAstralGreenhouse.class,
                    MekanismLang.DESCRIPTION_CHEMICAL_CRYSTALLIZER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE));

    public static final MachineRegistryObject2<BEAstralIsotopicCentrifuge, BlockTileModel<BEAstralIsotopicCentrifuge, BlockTypeMachine<BEAstralIsotopicCentrifuge>>, MekanismTileContainer<BEAstralIsotopicCentrifuge>, ItemBlockMachine> ASTRAL_ISOTOPIC_CENTRIFUGE = MACHINES
            .regSimple("astral_isotopic_centrifuge",
                    BEAstralIsotopicCentrifuge::new,
                    BEAstralIsotopicCentrifuge.class,
                    MekanismLang.DESCRIPTION_ISOTOPIC_CENTRIFUGE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.ISOTOPIC_CENTRIFUGE));

    public static final MachineRegistryObject2<BEAstralMekanicalInscriber, BlockTileModel<BEAstralMekanicalInscriber, BlockTypeMachine<BEAstralMekanicalInscriber>>, MekanismTileContainer<BEAstralMekanicalInscriber>, ItemBlockMachine> ASTRAL_MEKANICAL_INSCRIBER = MACHINES
            .regSimple("astral_mekanical_inscriber",
                    BEAstralMekanicalInscriber::new,
                    BEAstralMekanicalInscriber.class,
                    MekanismLang.DESCRIPTION_CHEMICAL_CRYSTALLIZER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE));

    public static final MachineRegistryObject2<BEAstralMekanicalTransformer, BlockTileModel<BEAstralMekanicalTransformer, BlockTypeMachine<BEAstralMekanicalTransformer>>, MekanismTileContainer<BEAstralMekanicalTransformer>, ItemBlockMachine> ASTRAL_MEKANICAL_TRANSFOMER = MACHINES
            .regSimple("astral_mekanical_transformer",
                    BEAstralMekanicalTransformer::new,
                    BEAstralMekanicalTransformer.class,
                    MekanismLang.DESCRIPTION_CHEMICAL_CRYSTALLIZER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE));

    public static final MachineRegistryObject2<BEAstralMetallurgicInfuser, BlockTileModel<BEAstralMetallurgicInfuser, BlockTypeMachine<BEAstralMetallurgicInfuser>>, MekanismTileContainer<BEAstralMetallurgicInfuser>, ItemBlockMachine> ASTRAL_METALLURGIC_INFUSER = MACHINES
            .regSimple("astral_metallurgic_infuser",
                    BEAstralMetallurgicInfuser::new,
                    BEAstralMetallurgicInfuser.class,
                    MekanismLang.DESCRIPTION_CHEMICAL_CRYSTALLIZER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.METALLURGIC_INFUSER));

    public static final MachineRegistryObject2<BEAstralPRC, BlockTileModel<BEAstralPRC, BlockTypeMachine<BEAstralPRC>>, MekanismTileContainer<BEAstralPRC>, ItemBlockMachine> ASTRAL_PRC = MACHINES
            .regSimple("astral_prc",
                    BEAstralPRC::new,
                    BEAstralPRC.class,
                    MekanismLang.DESCRIPTION_PRESSURIZED_REACTION_CHAMBER,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.pressurizedReactionBase,
                                    () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.PRESSURIZED_REACTION_CHAMBER)
                            .withCustomShape(AMBlockShapes.ASTRAL_PRC));

    public static final MachineRegistryObject<BEAstralPrecisionSawmill, BlockTypeMachine<BEAstralPrecisionSawmill>, BlockTileModel<BEAstralPrecisionSawmill, BlockTypeMachine<BEAstralPrecisionSawmill>>, MekanismTileContainer<BEAstralPrecisionSawmill>, ItemBlockMachine> ASTRAL_PRECISION_SAWMILL = MACHINES
            .register("astral_precision_sawmill", AstralMekanismBlockTypes.ASTRAL_PRECISION_SAWMILL,
                    BEAstralPrecisionSawmill.class, BEAstralPrecisionSawmill::new);

    public static final MachineRegistryObject<BEAstralRotaryCondensentrator, BlockTypeMachine<BEAstralRotaryCondensentrator>, BlockTileModel<BEAstralRotaryCondensentrator, BlockTypeMachine<BEAstralRotaryCondensentrator>>, MekanismTileContainer<BEAstralRotaryCondensentrator>, ItemBlockMachine> ASTRAL_ROTARY_CONDENSENTRATOR = MACHINES
            .register("astral_rotary_condensentrator", null,
                    BEAstralRotaryCondensentrator.class, BEAstralRotaryCondensentrator::new);

    public static final MachineRegistryObject<BEAstralSPS, BlockTypeMachine<BEAstralSPS>, BlockTileModel<BEAstralSPS, BlockTypeMachine<BEAstralSPS>>, MekanismTileContainer<BEAstralSPS>, ItemBlockMachine> ASTRAL_SPS = MACHINES
            .register("astral_sps", AstralMekanismBlockTypes.ASTRAL_SPS, BEAstralSPS.class,
                    BEAstralSPS::new);

    public static final MachineRegistryObject<BECompactFIR, BlockTypeTile<BECompactFIR>, BlockTileModel<BECompactFIR, BlockTypeTile<BECompactFIR>>, MekanismTileContainer<BECompactFIR>, ItemBlockMachine> COMPACT_FIR = MACHINES
            .register("compact_fir", AstralMekanismBlockTypes.COMPACT_FIR, BECompactFIR::new,
                    BECompactFIR.class);

    public static final MachineRegistryObject<BECompactSPS, BlockTypeMachine<BECompactSPS>, BlockTileModel<BECompactSPS, BlockTypeMachine<BECompactSPS>>, MekanismTileContainer<BECompactSPS>, ItemBlockMachine> COMPACT_SPS = MACHINES
            .register("compact_sps", AstralMekanismBlockTypes.COMPACT_SPS, BECompactSPS.class,
                    BECompactSPS::new);

    public static final MachineRegistryObject<BECompactTEP, BlockTypeTile<BECompactTEP>, BlockTileModel<BECompactTEP, BlockTypeTile<BECompactTEP>>, MekanismTileContainer<BECompactTEP>, ItemBlockMachine> COMPACT_TEP = MACHINES
            .register("compact_tep", AstralMekanismBlockTypes.COMPACT_TEP, BECompactTEP::new,
                    BECompactTEP.class);

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

    public static final MachineRegistryObject<BEAstralCrafter, BlockTypeMachine<BEAstralCrafter>, BlockTileModel<BEAstralCrafter, BlockTypeMachine<BEAstralCrafter>>, ContainerAstralCrafter, ItemBlockMachine> ASTRAL_CRAFTER = MACHINES
            .register("astral_crafter", AstralMekanismBlockTypes.ASTRAL_CRAFTER,
                    bt -> new BlockTileModel<>(bt, normalOperator), ItemBlockMachine::new,
                    BEAstralCrafter::new, BEAstralCrafter.class, ContainerAstralCrafter::new);

    public static final MachineRegistryObject<BEFluidInfuser, BlockTypeMachine<BEFluidInfuser>, BlockTileModel<BEFluidInfuser, BlockTypeMachine<BEFluidInfuser>>, MekanismTileContainer<BEFluidInfuser>, ItemBlockMachine> FLUID_INFUSER = MACHINES
            .register("fluid_infuser", AstralMekanismBlockTypes.FLUID_INFUSER,
                    BEFluidInfuser.class, BEFluidInfuser::new);

    public static final MachineRegistryObject<BEGlowstoneNeutronActivator, BlockTypeTile<BEGlowstoneNeutronActivator>, BlockTileModel<BEGlowstoneNeutronActivator, BlockTypeTile<BEGlowstoneNeutronActivator>>, MekanismTileContainer<BEGlowstoneNeutronActivator>, ItemBlockMachine> GLOWSTONE_NEUTRON_ACTIVATOR = MACHINES
            .register("glowstone_neutron_activator", AstralMekanismBlockTypes.GLOWSTONE_NEUTRON_ACTIVATOR,
                    BEGlowstoneNeutronActivator::new, BEGlowstoneNeutronActivator.class);

    public static final MachineRegistryObject<BEGreenhouse, BlockTypeMachine<BEGreenhouse>, BlockTileModel<BEGreenhouse, BlockTypeMachine<BEGreenhouse>>, MekanismTileContainer<BEGreenhouse>, ItemBlockMachine> GREENHOUSE = MACHINES
            .register("greenhouse", null,
                    BEGreenhouse.class, BEGreenhouse::new);

    public static final MachineRegistryObject<BEInfuseSynthesizer, BlockTypeTile<BEInfuseSynthesizer>, BlockTileModel<BEInfuseSynthesizer, BlockTypeTile<BEInfuseSynthesizer>>, MekanismTileContainer<BEInfuseSynthesizer>, ItemBlockMachine> INFUSE_SYNTHESIZER = MACHINES
            .register("infuse_synthesizer", null,
                    BEInfuseSynthesizer::new, BEInfuseSynthesizer.class);

    public static final MachineRegistryObject<BEMekanicalCharger, BlockTypeMachine<BEMekanicalCharger>, BlockTileModel<BEMekanicalCharger, BlockTypeMachine<BEMekanicalCharger>>, MekanismTileContainer<BEMekanicalCharger>, ItemBlockMachine> MEKANICAL_CHARGER = MACHINES
            .register("mekanical_charger", AstralMekanismBlockTypes.MEKANICAL_CHARGER,
                    BEMekanicalCharger.class, BEMekanicalCharger::new);

    public static final MachineRegistryObject<BEMekanicalInscriber, BlockTypeMachine<BEMekanicalInscriber>, BlockTileModel<BEMekanicalInscriber, BlockTypeMachine<BEMekanicalInscriber>>, MekanismTileContainer<BEMekanicalInscriber>, ItemBlockMachine> MEKANICAL_INSCRIBER = MACHINES
            .register("mekanical_inscriber", AstralMekanismBlockTypes.MEKANICAL_INSCRIBER,
                    BEMekanicalInscriber.class, BEMekanicalInscriber::new);

    public static final MachineRegistryObject<BEMekanicalPresser, BlockTypeMachine<BEMekanicalPresser>, BlockTileModel<BEMekanicalPresser, BlockTypeMachine<BEMekanicalPresser>>, MekanismTileContainer<BEMekanicalPresser>, ItemBlockMachine> MEKANICAL_PRESSER = MACHINES
            .register("mekanical_presser", AstralMekanismBlockTypes.MEKANICAL_PRESSER,
                    BEMekanicalPresser.class, BEMekanicalPresser::new);

    public static final MachineRegistryObject<BEMekanicalTransformer, BlockTypeMachine<BEMekanicalTransformer>, BlockTileModel<BEMekanicalTransformer, BlockTypeMachine<BEMekanicalTransformer>>, MekanismTileContainer<BEMekanicalTransformer>, ItemBlockMachine> MEKANICAL_TRANSFORMER = MACHINES
            .register("mekanical_transformer", null,
                    BEMekanicalTransformer.class, BEMekanicalTransformer::new);

    public static final MachineRegistryObject<BEMelter, BlockTypeTile<BEMelter>, BlockTileModel<BEMelter, BlockTypeTile<BEMelter>>, MekanismTileContainer<BEMelter>, ItemBlockMachine> MELTER = MACHINES
            .register("melter", AstralMekanismBlockTypes.MELTER, BEMelter::new, BEMelter.class);

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

}
