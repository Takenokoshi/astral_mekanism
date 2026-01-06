package astral_mekanism.registries;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import astral_mekanism.AstralMekanismConfig;
import astral_mekanism.AstralMekanismID;
import astral_mekanism.AstralMekanismLang;
import astral_mekanism.block.blockentity.astralfactory.BEAstralEnergizedSmeltingFactory;
import astral_mekanism.block.blockentity.astralmachine.BEAstralChemicalInfuser;
import astral_mekanism.block.blockentity.astralmachine.BEAstralChemicalOxidizer;
import astral_mekanism.block.blockentity.astralmachine.BEAstralChemicalWasher;
import astral_mekanism.block.blockentity.astralmachine.BEAstralCombiner;
import astral_mekanism.block.blockentity.astralmachine.BEAstralCrystallizer;
import astral_mekanism.block.blockentity.astralmachine.BEAstralDissolutionChamber;
import astral_mekanism.block.blockentity.astralmachine.BEAstralElectrolyticSeparator;
import astral_mekanism.block.blockentity.astralmachine.BEAstralEnergizedSmelter;
import astral_mekanism.block.blockentity.astralmachine.BEAstralGNA;
import astral_mekanism.block.blockentity.astralmachine.BEAstralGreenhouse;
import astral_mekanism.block.blockentity.astralmachine.BEAstralIsotopicCentrifuge;
import astral_mekanism.block.blockentity.astralmachine.BEAstralMekanicalCharger;
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
import astral_mekanism.block.blockentity.astralmachine.electric.BEAstralEnrichmentChamber;
import astral_mekanism.block.blockentity.base.AstralMekanismFactoryTier;
import astral_mekanism.block.blockentity.base.BlockEntityGeneralRecipeFactory;
import astral_mekanism.block.blockentity.compact.BECompactFIR;
import astral_mekanism.block.blockentity.compact.BECompactSPS;
import astral_mekanism.block.blockentity.compact.BECompactTEP;
import astral_mekanism.block.blockentity.generator.AstralMekGeneratorTier;
import astral_mekanism.block.blockentity.generator.BEGasBurningGenerator;
import astral_mekanism.block.blockentity.generator.BEHeatGenerator;
import astral_mekanism.block.blockentity.normalmachine.BEAstralCrafter;
import astral_mekanism.block.blockentity.normalmachine.BEEssentialEnergizedSmelter;
import astral_mekanism.block.blockentity.normalmachine.BEEssentialMetallurgicInfuser;
import astral_mekanism.block.blockentity.normalmachine.BEFluidInfuser;
import astral_mekanism.block.blockentity.normalmachine.BEGlowstoneNeutronActivator;
import astral_mekanism.block.blockentity.normalmachine.BEGreenhouse;
import astral_mekanism.block.blockentity.normalmachine.BEInfuseSynthesizer;
import astral_mekanism.block.blockentity.normalmachine.BEMekanicalCharger;
import astral_mekanism.block.blockentity.normalmachine.BEMekanicalInscriber;
import astral_mekanism.block.blockentity.normalmachine.BEMekanicalTransformer;
import astral_mekanism.block.blockentity.other.BEItemSortableStorage;
import astral_mekanism.block.blockentity.other.BEUniversalStorage;
import astral_mekanism.block.container.factory.ContainerAstralMekanismFactory;
import astral_mekanism.block.container.normal_machine.ContainerAstralCrafter;
import astral_mekanism.block.container.other.ContainerItemSortableStorage;
import astral_mekanism.block.container.prefab.ContainerAbstractStorage;
import astral_mekanism.block.shape.AMBlockShapes;
import astral_mekanism.registration.BlockTypeMachine;
import astral_mekanism.registration.MachineDeferredRegister;
import astral_mekanism.registration.MachineRegistryObject;
import astral_mekanism.registration.BlockTypeMachine.BlockMachineBuilder;
import astral_mekanism.registration.RegistrationInterfaces.BlockEntityConstructor;
import mekanism.api.Upgrade;
import mekanism.api.math.FloatingLong;
import mekanism.api.text.ILangEntry;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.AttributeTier;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.blocktype.BlockShapes;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.registries.MekanismSounds;
import mekanism.generators.common.registries.GeneratorsSounds;

public class AstralMekanismMachines {
    public static final MachineDeferredRegister MACHINES = new MachineDeferredRegister(AstralMekanismID.MODID);

    private static <BE extends BlockEntityGeneralRecipeFactory<?, BE>> EnumMap<AstralMekanismFactoryTier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, ContainerAstralMekanismFactory<BE>, ItemBlockMachine>> registerFactories(
            Function<AstralMekanismFactoryTier, String> nameBuilder,
            BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTileModel<BE, BlockTypeMachine<BE>>> constructor,
            Class<BE> beClass,
            ILangEntry langEntry,
            UnaryOperator<BlockMachineBuilder<BlockTypeMachine<BE>, BE>> operator) {
        EnumMap<AstralMekanismFactoryTier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, ContainerAstralMekanismFactory<BE>, ItemBlockMachine>> result = new EnumMap<>(
                AstralMekanismFactoryTier.class);
        for (AstralMekanismFactoryTier tier : AstralMekanismFactoryTier.values()) {
            result.put(tier, MACHINES.registerDefaultBlockItem(nameBuilder.apply(tier),
                    constructor, beClass, ContainerAstralMekanismFactory<BE>::new, langEntry,
                    builder -> operator.apply(builder.with(new AttributeTier<>(tier)))));
        }
        return result;
    }

    public static final EnumMap<AstralMekanismFactoryTier, MachineRegistryObject<BEAstralEnergizedSmeltingFactory, BlockTileModel<BEAstralEnergizedSmeltingFactory, BlockTypeMachine<BEAstralEnergizedSmeltingFactory>>, ContainerAstralMekanismFactory<BEAstralEnergizedSmeltingFactory>, ItemBlockMachine>> ASTRAL_ENERGIZED_SMELTING_FACTRIES = registerFactories(
            t -> t.nameForAstral + "_astral_energized_smelting_factory",
            BEAstralEnergizedSmeltingFactory::new,
            BEAstralEnergizedSmeltingFactory.class,
            AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
            builder -> builder.withEnergyConfig(
                    () -> FloatingLong.create(20000 * AstralMekanismConfig.energyRate),
                    () -> FloatingLong.MAX_VALUE)
                    .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY))
                    .withSound(MekanismSounds.ENERGIZED_SMELTER));

    public static final MachineRegistryObject<BEAstralChemicalInjectionChamber, BlockTileModel<BEAstralChemicalInjectionChamber, BlockTypeMachine<BEAstralChemicalInjectionChamber>>, MekanismTileContainer<BEAstralChemicalInjectionChamber>, ItemBlockMachine> ASTRAL_CHEMICAL_INJECTION_CHAMBER = MACHINES
            .registerSimple("astral_chemical_injection_chamber",
                    BEAstralChemicalInjectionChamber::new,
                    BEAstralChemicalInjectionChamber.class,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(20000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY))
                            .withSound(MekanismSounds.CHEMICAL_INJECTION_CHAMBER));

    public static final MachineRegistryObject<BEAstralOsmiumCompressor, BlockTileModel<BEAstralOsmiumCompressor, BlockTypeMachine<BEAstralOsmiumCompressor>>, MekanismTileContainer<BEAstralOsmiumCompressor>, ItemBlockMachine> ASTRAL_OSMIUM_COMPRESSOR = MACHINES
            .registerSimple("astral_osmium_compressor",
                    BEAstralOsmiumCompressor::new,
                    BEAstralOsmiumCompressor.class,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY))
                            .withSound(MekanismSounds.OSMIUM_COMPRESSOR));

    public static final MachineRegistryObject<BEAstralPurificationChamber, BlockTileModel<BEAstralPurificationChamber, BlockTypeMachine<BEAstralPurificationChamber>>, MekanismTileContainer<BEAstralPurificationChamber>, ItemBlockMachine> ASTRAL_PURIFICATION_CHAMBER = MACHINES
            .registerSimple("astral_purification_chamber",
                    BEAstralPurificationChamber::new,
                    BEAstralPurificationChamber.class,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY))
                            .withSound(MekanismSounds.PURIFICATION_CHAMBER));

    public static final MachineRegistryObject<BEAstralCrusher, BlockTileModel<BEAstralCrusher, BlockTypeMachine<BEAstralCrusher>>, MekanismTileContainer<BEAstralCrusher>, ItemBlockMachine> ASTRAL_CRUSHER = MACHINES
            .registerSimple("astral_crusher",
                    BEAstralCrusher::new,
                    BEAstralCrusher.class,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY))
                            .withSound(MekanismSounds.CRUSHER));

    public static final MachineRegistryObject<BEAstralEnrichmentChamber, BlockTileModel<BEAstralEnrichmentChamber, BlockTypeMachine<BEAstralEnrichmentChamber>>, MekanismTileContainer<BEAstralEnrichmentChamber>, ItemBlockMachine> ASTRAL_ENRICHMENT_CHAMBER = MACHINES
            .registerSimple("astral_enrichment_chamber",
                    BEAstralEnrichmentChamber::new,
                    BEAstralEnrichmentChamber.class,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY))
                            .withSound(MekanismSounds.ENRICHMENT_CHAMBER));

    public static final MachineRegistryObject<BEAstralMekanicalCharger, BlockTileModel<BEAstralMekanicalCharger, BlockTypeMachine<BEAstralMekanicalCharger>>, MekanismTileContainer<BEAstralMekanicalCharger>, ItemBlockMachine> ASTRAL_MEKANICAL_CHARGER = MACHINES
            .registerSimple("astral_mekanical_charger",
                    BEAstralMekanicalCharger::new,
                    BEAstralMekanicalCharger.class,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(800 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY)));

    public static final MachineRegistryObject<BEAstralChemicalInfuser, BlockTileModel<BEAstralChemicalInfuser, BlockTypeMachine<BEAstralChemicalInfuser>>, MekanismTileContainer<BEAstralChemicalInfuser>, ItemBlockMachine> ASTRAL_CHEMICAL_INFUSER = MACHINES
            .registerSimple("astral_chemical_infuser",
                    BEAstralChemicalInfuser::new,
                    BEAstralChemicalInfuser.class,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY))
                            .withSound(MekanismSounds.CHEMICAL_INFUSER));

    public static final MachineRegistryObject<BEAstralChemicalOxidizer, BlockTileModel<BEAstralChemicalOxidizer, BlockTypeMachine<BEAstralChemicalOxidizer>>, MekanismTileContainer<BEAstralChemicalOxidizer>, ItemBlockMachine> ASTRAL_CHEMICAL_OXIDIZER = MACHINES
            .registerSimple("astral_chemical_oxidizer",
                    BEAstralChemicalOxidizer::new,
                    BEAstralChemicalOxidizer.class,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY))
                            .withSound(MekanismSounds.CHEMICAL_OXIDIZER));

    public static final MachineRegistryObject<BEAstralChemicalWasher, BlockTileModel<BEAstralChemicalWasher, BlockTypeMachine<BEAstralChemicalWasher>>, MekanismTileContainer<BEAstralChemicalWasher>, ItemBlockMachine> ASTRAL_CHEMICAL_WASHER = MACHINES
            .registerSimple("astral_chemical_washer",
                    BEAstralChemicalWasher::new,
                    BEAstralChemicalWasher.class,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY))
                            .withSound(MekanismSounds.CHEMICAL_WASHER));

    public static final MachineRegistryObject<BEAstralCombiner, BlockTileModel<BEAstralCombiner, BlockTypeMachine<BEAstralCombiner>>, MekanismTileContainer<BEAstralCombiner>, ItemBlockMachine> ASTRAL_COMBINER = MACHINES
            .registerSimple("astral_combiner",
                    BEAstralCombiner::new,
                    BEAstralCombiner.class,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY))
                            .withSound(MekanismSounds.COMBINER));

    public static final MachineRegistryObject<BEAstralCrystallizer, BlockTileModel<BEAstralCrystallizer, BlockTypeMachine<BEAstralCrystallizer>>, MekanismTileContainer<BEAstralCrystallizer>, ItemBlockMachine> ASTRAL_CRYSTALLIZER = MACHINES
            .registerSimple("astral_crystallizer",
                    BEAstralCrystallizer::new,
                    BEAstralCrystallizer.class,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY))
                            .withSound(MekanismSounds.CHEMICAL_CRYSTALLIZER));

    public static final MachineRegistryObject<BEAstralDissolutionChamber, BlockTileModel<BEAstralDissolutionChamber, BlockTypeMachine<BEAstralDissolutionChamber>>, MekanismTileContainer<BEAstralDissolutionChamber>, ItemBlockMachine> ASTRAL_DISSOLUTION_CHAMBER = MACHINES
            .registerSimple("astral_dissolution_chamber",
                    BEAstralDissolutionChamber::new,
                    BEAstralDissolutionChamber.class,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY))
                            .withSound(MekanismSounds.CHEMICAL_DISSOLUTION_CHAMBER));

    public static final MachineRegistryObject<BEAstralElectrolyticSeparator, BlockTileModel<BEAstralElectrolyticSeparator, BlockTypeMachine<BEAstralElectrolyticSeparator>>, MekanismTileContainer<BEAstralElectrolyticSeparator>, ItemBlockMachine> ASTRAL_ELECTROLYTIC_SEPARATOR = MACHINES
            .registerSimple("astral_electrolytic_separator",
                    BEAstralElectrolyticSeparator::new,
                    BEAstralElectrolyticSeparator.class,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.withEnergyConfig(
                            () -> MekanismConfig.general.FROM_H2.get().multiply(2),
                            () -> FloatingLong.MAX_VALUE)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY))
                            .withSound(MekanismSounds.ELECTROLYTIC_SEPARATOR));

    public static final MachineRegistryObject<BEAstralEnergizedSmelter, BlockTileModel<BEAstralEnergizedSmelter, BlockTypeMachine<BEAstralEnergizedSmelter>>, MekanismTileContainer<BEAstralEnergizedSmelter>, ItemBlockMachine> ASTRAL_ENERGIZED_SMELTER = MACHINES
            .registerSimple("astral_energized_smelter",
                    BEAstralEnergizedSmelter::new,
                    BEAstralEnergizedSmelter.class,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY))
                            .withSound(MekanismSounds.ENERGIZED_SMELTER));

    public static final MachineRegistryObject<BEAstralGNA, BlockTileModel<BEAstralGNA, BlockTypeMachine<BEAstralGNA>>, MekanismTileContainer<BEAstralGNA>, ItemBlockMachine> ASTRAL_GNA = MACHINES
            .registerSimple("astral_gna",
                    BEAstralGNA::new,
                    BEAstralGNA.class,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
                    BlockMachineBuilder::removeAttributeUpgrade);

    public static final MachineRegistryObject<BEAstralGreenhouse, BlockTileModel<BEAstralGreenhouse, BlockTypeMachine<BEAstralGreenhouse>>, MekanismTileContainer<BEAstralGreenhouse>, ItemBlockMachine> ASTRAL_GREENHOUSE = MACHINES
            .registerSimple("astral_greenhouse",
                    BEAstralGreenhouse::new,
                    BEAstralGreenhouse.class,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY)));

    public static final MachineRegistryObject<BEAstralIsotopicCentrifuge, BlockTileModel<BEAstralIsotopicCentrifuge, BlockTypeMachine<BEAstralIsotopicCentrifuge>>, MekanismTileContainer<BEAstralIsotopicCentrifuge>, ItemBlockMachine> ASTRAL_ISOTOPIC_CENTRIFUGE = MACHINES
            .registerSimple("astral_isotopic_centrifuge",
                    BEAstralIsotopicCentrifuge::new,
                    BEAstralIsotopicCentrifuge.class,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY))
                            .withSound(MekanismSounds.ISOTOPIC_CENTRIFUGE));

    public static final MachineRegistryObject<BEAstralMekanicalInscriber, BlockTileModel<BEAstralMekanicalInscriber, BlockTypeMachine<BEAstralMekanicalInscriber>>, MekanismTileContainer<BEAstralMekanicalInscriber>, ItemBlockMachine> ASTRAL_MEKANICAL_INSCRIBER = MACHINES
            .registerSimple("astral_mekanical_inscriber",
                    BEAstralMekanicalInscriber::new,
                    BEAstralMekanicalInscriber.class,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY)));

    public static final MachineRegistryObject<BEAstralMekanicalTransformer, BlockTileModel<BEAstralMekanicalTransformer, BlockTypeMachine<BEAstralMekanicalTransformer>>, MekanismTileContainer<BEAstralMekanicalTransformer>, ItemBlockMachine> ASTRAL_MEKANICAL_TRANSFOMER = MACHINES
            .registerSimple("astral_mekanical_transformer",
                    BEAstralMekanicalTransformer::new,
                    BEAstralMekanicalTransformer.class,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY)));

    public static final MachineRegistryObject<BEAstralMetallurgicInfuser, BlockTileModel<BEAstralMetallurgicInfuser, BlockTypeMachine<BEAstralMetallurgicInfuser>>, MekanismTileContainer<BEAstralMetallurgicInfuser>, ItemBlockMachine> ASTRAL_METALLURGIC_INFUSER = MACHINES
            .registerSimple("astral_metallurgic_infuser",
                    BEAstralMetallurgicInfuser::new,
                    BEAstralMetallurgicInfuser.class,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY))
                            .withSound(MekanismSounds.METALLURGIC_INFUSER));

    public static final MachineRegistryObject<BEAstralPRC, BlockTileModel<BEAstralPRC, BlockTypeMachine<BEAstralPRC>>, MekanismTileContainer<BEAstralPRC>, ItemBlockMachine> ASTRAL_PRC = MACHINES
            .registerSimple("astral_prc",
                    BEAstralPRC::new,
                    BEAstralPRC.class,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.pressurizedReactionBase,
                                    () -> FloatingLong.MAX_VALUE)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY))
                            .withSound(MekanismSounds.PRESSURIZED_REACTION_CHAMBER));

    public static final MachineRegistryObject<BEAstralPrecisionSawmill, BlockTileModel<BEAstralPrecisionSawmill, BlockTypeMachine<BEAstralPrecisionSawmill>>, MekanismTileContainer<BEAstralPrecisionSawmill>, ItemBlockMachine> ASTRAL_PRECISION_SAWMILL = MACHINES
            .registerSimple("astral_precision_sawmill",
                    BEAstralPrecisionSawmill::new,
                    BEAstralPrecisionSawmill.class,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY))
                            .withSound(MekanismSounds.PRECISION_SAWMILL));

    public static final MachineRegistryObject<BEAstralRotaryCondensentrator, BlockTileModel<BEAstralRotaryCondensentrator, BlockTypeMachine<BEAstralRotaryCondensentrator>>, MekanismTileContainer<BEAstralRotaryCondensentrator>, ItemBlockMachine> ASTRAL_ROTARY_CONDENSENTRATOR = MACHINES
            .registerSimple("astral_rotary_condensentrator",
                    BEAstralRotaryCondensentrator::new,
                    BEAstralRotaryCondensentrator.class,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.MAX_VALUE)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY))
                            .withSound(MekanismSounds.ROTARY_CONDENSENTRATOR));

    public static final MachineRegistryObject<BEAstralSPS, BlockTileModel<BEAstralSPS, BlockTypeMachine<BEAstralSPS>>, MekanismTileContainer<BEAstralSPS>, ItemBlockMachine> ASTRAL_SPS = MACHINES
            .registerSimple("astral_sps",
                    BEAstralSPS::new,
                    BEAstralSPS.class,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(1000000000),
                            () -> FloatingLong.MAX_VALUE)
                            .withSound(MekanismSounds.SPS)
                            .removeAttributeUpgrade()
                            .withSupportedUpgrades(EnumSet.of(Upgrade.MUFFLING)));

    public static final MachineRegistryObject<BECompactFIR, BlockTileModel<BECompactFIR, BlockTypeMachine<BECompactFIR>>, MekanismTileContainer<BECompactFIR>, ItemBlockMachine> COMPACT_FIR = MACHINES
            .registerSimple("compact_fir",
                    BECompactFIR::new,
                    BECompactFIR.class,
                    AstralMekanismLang.DESCRIPTION_COMPACT_MACHINE,
                    BlockMachineBuilder::removeAttributeUpgrade);

    public static final MachineRegistryObject<BECompactSPS, BlockTileModel<BECompactSPS, BlockTypeMachine<BECompactSPS>>, MekanismTileContainer<BECompactSPS>, ItemBlockMachine> COMPACT_SPS = MACHINES
            .registerSimple("compact_sps",
                    BECompactSPS::new,
                    BECompactSPS.class,
                    AstralMekanismLang.DESCRIPTION_COMPACT_MACHINE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(1000000000),
                            () -> FloatingLong.create(10000000000l))
                            .withSound(MekanismSounds.SPS)
                            .removeAttributeUpgrade()
                            .withSupportedUpgrades(EnumSet.of(Upgrade.MUFFLING)));

    public static final MachineRegistryObject<BECompactTEP, BlockTileModel<BECompactTEP, BlockTypeMachine<BECompactTEP>>, MekanismTileContainer<BECompactTEP>, ItemBlockMachine> COMPACT_TEP = MACHINES
            .registerSimple("compact_tep",
                    BECompactTEP::new,
                    BECompactTEP.class,
                    AstralMekanismLang.DESCRIPTION_COMPACT_MACHINE,
                    builder -> builder.removeAttributeUpgrade());

    public static final EnumMap<AstralMekGeneratorTier, MachineRegistryObject<BEGasBurningGenerator, BlockTileModel<BEGasBurningGenerator, BlockTypeMachine<BEGasBurningGenerator>>, MekanismTileContainer<BEGasBurningGenerator>, ItemBlockMachine>> GAS_BURNING_GENERATORS = MACHINES
            .registerSimpleMap(k -> k.name + "_gas_burning_generator",
                    BEGasBurningGenerator::new,
                    BEGasBurningGenerator.class,
                    AstralMekanismLang.DESCRIPTION_AM_GENERATOR,
                    (key, btm) -> btm.with(new AttributeTier<>(key))
                            .withCustomShape(AMBlockShapes.GAS_BURNING_GENERATOR)
                            .withSound(GeneratorsSounds.GAS_BURNING_GENERATOR)
                            .removeAttributeUpgrade()
                            .withSupportedUpgrades(EnumSet.of(Upgrade.MUFFLING)),
                    AstralMekGeneratorTier.class);

    public static final EnumMap<AstralMekGeneratorTier, MachineRegistryObject<BEHeatGenerator, BlockTileModel<BEHeatGenerator, BlockTypeMachine<BEHeatGenerator>>, MekanismTileContainer<BEHeatGenerator>, ItemBlockMachine>> HEAT_GENERATORS = MACHINES
            .registerSimpleMap(k -> k.name + "_heat_generator",
                    BEHeatGenerator::new,
                    BEHeatGenerator.class,
                    AstralMekanismLang.DESCRIPTION_AM_GENERATOR,
                    (key, btm) -> btm.with(new AttributeTier<>(key))
                            .withCustomShape(AMBlockShapes.HEAT_GENERATOR)
                            .removeAttributeUpgrade(),
                    AstralMekGeneratorTier.class);

    public static final MachineRegistryObject<BEAstralCrafter, BlockTileModel<BEAstralCrafter, BlockTypeMachine<BEAstralCrafter>>, ContainerAstralCrafter, ItemBlockMachine> ASTRAL_CRAFTER = MACHINES
            .registerDefaultBlockItem("essential_crafter",
                    BEAstralCrafter::new,
                    BEAstralCrafter.class,
                    ContainerAstralCrafter::new,
                    AstralMekanismLang.DESCRIPTION_ASTRAL_CRAFTER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(400 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.create(10000000 * AstralMekanismConfig.energyRate)));

    public static final MachineRegistryObject<BEEssentialEnergizedSmelter, BlockTileModel<BEEssentialEnergizedSmelter, BlockTypeMachine<BEEssentialEnergizedSmelter>>, MekanismTileContainer<BEEssentialEnergizedSmelter>, ItemBlockMachine> ESSENTIAL_ENERGIZED_SMELTER = MACHINES
            .registerSimple("essential_energized_smelter",
                    BEEssentialEnergizedSmelter::new,
                    BEEssentialEnergizedSmelter.class,
                    MekanismLang.DESCRIPTION_ENERGIZED_SMELTER,
                    builder -> builder.withEnergyConfig(
                            MekanismConfig.usage.energizedSmelter,
                            MekanismConfig.storage.energizedSmelter)
                            .withSound(MekanismSounds.ENERGIZED_SMELTER));

    public static final MachineRegistryObject<BEEssentialMetallurgicInfuser, BlockTileModel<BEEssentialMetallurgicInfuser, BlockTypeMachine<BEEssentialMetallurgicInfuser>>, MekanismTileContainer<BEEssentialMetallurgicInfuser>, ItemBlockMachine> ESSENTIAL_METALLURGIC_INFUSER = MACHINES
            .registerSimple("essential_metallurgic_infuser",
                    BEEssentialMetallurgicInfuser::new,
                    BEEssentialMetallurgicInfuser.class,
                    MekanismLang.DESCRIPTION_METALLURGIC_INFUSER,
                    builder -> builder.withEnergyConfig(
                            MekanismConfig.usage.metallurgicInfuser,
                            MekanismConfig.storage.metallurgicInfuser)
                            .withCustomShape(BlockShapes.METALLURGIC_INFUSER)
                            .withSound(MekanismSounds.METALLURGIC_INFUSER));

    public static final MachineRegistryObject<BEFluidInfuser, BlockTileModel<BEFluidInfuser, BlockTypeMachine<BEFluidInfuser>>, MekanismTileContainer<BEFluidInfuser>, ItemBlockMachine> FLUID_INFUSER = MACHINES
            .registerSimple("fluid_infuser",
                    BEFluidInfuser::new,
                    BEFluidInfuser.class,
                    AstralMekanismLang.DESCRIPTION_FLUID_INFUSER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(1000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.create(10000000)));

    public static final MachineRegistryObject<BEGlowstoneNeutronActivator, BlockTileModel<BEGlowstoneNeutronActivator, BlockTypeMachine<BEGlowstoneNeutronActivator>>, MekanismTileContainer<BEGlowstoneNeutronActivator>, ItemBlockMachine> GLOWSTONE_NEUTRON_ACTIVATOR = MACHINES
            .registerSimple("glowstone_neutron_activator",
                    BEGlowstoneNeutronActivator::new,
                    BEGlowstoneNeutronActivator.class,
                    AstralMekanismLang.DESCRIPTION_GLOWSTONE_NEUTRON_ACTIVATOR,
                    BlockMachineBuilder::removeAttributeUpgrade);

    public static final MachineRegistryObject<BEGreenhouse, BlockTileModel<BEGreenhouse, BlockTypeMachine<BEGreenhouse>>, MekanismTileContainer<BEGreenhouse>, ItemBlockMachine> GREENHOUSE = MACHINES
            .registerSimple("greenhouse",
                    BEGreenhouse::new,
                    BEGreenhouse.class,
                    AstralMekanismLang.DESCRIPTION_GREENHOUSE,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(10000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.create(1000000l * AstralMekanismConfig.energyRate)));

    public static final MachineRegistryObject<BEInfuseSynthesizer, BlockTileModel<BEInfuseSynthesizer, BlockTypeMachine<BEInfuseSynthesizer>>, MekanismTileContainer<BEInfuseSynthesizer>, ItemBlockMachine> INFUSE_SYNTHESIZER = MACHINES
            .registerSimple("infuse_synthesizer",
                    BEInfuseSynthesizer::new,
                    BEInfuseSynthesizer.class,
                    AstralMekanismLang.DESCRIPTION_INFUSE_SYNTHESIZER,
                    BlockMachineBuilder::removeAttributeUpgrade);

    public static final MachineRegistryObject<BEMekanicalCharger, BlockTileModel<BEMekanicalCharger, BlockTypeMachine<BEMekanicalCharger>>, MekanismTileContainer<BEMekanicalCharger>, ItemBlockMachine> MEKANICAL_CHARGER = MACHINES
            .registerSimple("mekanical_charger",
                    BEMekanicalCharger::new,
                    BEMekanicalCharger.class,
                    AstralMekanismLang.DESCRIPTION_MEKANICAL_CHARGER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(1000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.create(100000l * AstralMekanismConfig.energyRate)));

    public static final MachineRegistryObject<BEMekanicalInscriber, BlockTileModel<BEMekanicalInscriber, BlockTypeMachine<BEMekanicalInscriber>>, MekanismTileContainer<BEMekanicalInscriber>, ItemBlockMachine> MEKANICAL_INSCRIBER = MACHINES
            .registerSimple("mekanical_inscriber",
                    BEMekanicalInscriber::new,
                    BEMekanicalInscriber.class,
                    AstralMekanismLang.DESCRIPTION_MEKANICAL_INSCRIBER,
                    builder -> builder.withEnergyConfig(
                            () -> FloatingLong.create(1000 * AstralMekanismConfig.energyRate),
                            () -> FloatingLong.create(100000l * AstralMekanismConfig.energyRate)));

    public static final MachineRegistryObject<BEMekanicalTransformer, BlockTileModel<BEMekanicalTransformer, BlockTypeMachine<BEMekanicalTransformer>>, MekanismTileContainer<BEMekanicalTransformer>, ItemBlockMachine> MEKANICAL_TRANSFORMER = MACHINES
            .registerSimple("mekanical_transformer",
                    BEMekanicalTransformer::new,
                    BEMekanicalTransformer.class,
                    AstralMekanismLang.DESCRIPTION_MEKANICAL_TRANSFORMER,
                    builder -> builder.withEnergyConfig(
                            () -> MekanismConfig.usage.electricPump.get().multiply(2.1)
                                    .multiply(AstralMekanismConfig.energyRate),
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
                    AstralMekanismLang.DESCRIPTION_ITEM_SORTABLE_STORAGE,
                    builder -> builder.removeAttributeUpgrade());

}
