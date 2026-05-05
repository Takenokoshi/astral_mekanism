package astral_mekanism.registries;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import com.fxd927.mekanismelements.common.config.MSConfig;
import com.fxd927.mekanismelements.common.content.blocktype.MSBlockShapes;
import com.jerry.mekanism_extras.api.ExtraUpgrade;

import astral_mekanism.AMETier;
import astral_mekanism.AMEConstants;
import astral_mekanism.AMELang;
import astral_mekanism.block.blockentity.appliedmachine.BEAppliedRotaryCondensentrator;
import astral_mekanism.block.blockentity.appliedmachine.BEAppliedSPS;
import astral_mekanism.block.block.AttributeIntTier;
import astral_mekanism.block.blockentity.appliedmachine.BEAppliedCrystallizer;
import astral_mekanism.block.blockentity.appliedmachine.BEAppliedElectrolyticSeparator;
import astral_mekanism.block.blockentity.appliedmachine.BEAppliedFissionReactor;
import astral_mekanism.block.blockentity.appliedmachine.BEAppliedFusionReactor;
import astral_mekanism.block.blockentity.appliedmachine.BEAppliedIsotopicCentrifuge;
import astral_mekanism.block.blockentity.appliedmachine.BEAppliedNaquadahReactor;
import astral_mekanism.block.blockentity.appliedmachine.BEAppliedNeutronActivator;
import astral_mekanism.block.blockentity.appliedmachine.BEAppliedSmelter;
import astral_mekanism.block.blockentity.appliedmachine.BEAppliedTEP;
import astral_mekanism.block.blockentity.astralfactory.BEAstralEnergizedSmeltingFactory;
import astral_mekanism.block.blockentity.astralmachine.BEAstralAPT;
import astral_mekanism.block.blockentity.astralmachine.BEAstralAlloyer;
import astral_mekanism.block.blockentity.astralmachine.BEAstralAntiprotonicNucleosynthesizer;
import astral_mekanism.block.blockentity.astralmachine.BEAstralChemicalInfuser;
import astral_mekanism.block.blockentity.astralmachine.BEAstralChemicalOxidizer;
import astral_mekanism.block.blockentity.astralmachine.BEAstralChemicalWasher;
import astral_mekanism.block.blockentity.astralmachine.BEAstralChemixer;
import astral_mekanism.block.blockentity.astralmachine.BEAstralCombiner;
import astral_mekanism.block.blockentity.astralmachine.BEAstralComposter;
import astral_mekanism.block.blockentity.astralmachine.BEAstralCrystallizer;
import astral_mekanism.block.blockentity.astralmachine.BEAstralDissolutionChamber;
import astral_mekanism.block.blockentity.astralmachine.BEAstralElectrolyticSeparator;
import astral_mekanism.block.blockentity.astralmachine.BEAstralEnergizedSmelter;
import astral_mekanism.block.blockentity.astralmachine.BEAstralFluidInfuser;
import astral_mekanism.block.blockentity.astralmachine.BEAstralFormulaicAssemblicator;
import astral_mekanism.block.blockentity.astralmachine.BEAstralGNA;
import astral_mekanism.block.blockentity.astralmachine.BEAstralGreenHouse;
import astral_mekanism.block.blockentity.astralmachine.BEAstralIsotopicCentrifuge;
import astral_mekanism.block.blockentity.astralmachine.BEAstralMekanicalCharger;
import astral_mekanism.block.blockentity.astralmachine.BEAstralMekanicalInscriber;
import astral_mekanism.block.blockentity.astralmachine.BEAstralMelter;
import astral_mekanism.block.blockentity.astralmachine.BEAstralMetallurgicInfuser;
import astral_mekanism.block.blockentity.astralmachine.BEAstralPRC;
import astral_mekanism.block.blockentity.astralmachine.BEAstralPrecisionSawmill;
import astral_mekanism.block.blockentity.astralmachine.BEAstralRadiationIrradiator;
import astral_mekanism.block.blockentity.astralmachine.BEAstralReactionChamber;
import astral_mekanism.block.blockentity.astralmachine.BEAstralRotaryCondensentrator;
import astral_mekanism.block.blockentity.astralmachine.BEAstralSPS;
import astral_mekanism.block.blockentity.astralmachine.BEAstralSolidifier;
import astral_mekanism.block.blockentity.astralmachine.BEAstralTransformer;
import astral_mekanism.block.blockentity.astralmachine.advanced.BEAstralChemicalInjectionChamber;
import astral_mekanism.block.blockentity.astralmachine.advanced.BEAstralOsmiumCompressor;
import astral_mekanism.block.blockentity.astralmachine.advanced.BEAstralPurificationChamber;
import astral_mekanism.block.blockentity.astralmachine.electric.BEAstralCrusher;
import astral_mekanism.block.blockentity.astralmachine.electric.BEAstralEnrichmentChamber;
import astral_mekanism.block.blockentity.base.BlockEntityRecipeFactory;
import astral_mekanism.block.blockentity.compact.BECompactAPT;
import astral_mekanism.block.blockentity.compact.BECompactFissionReactor;
import astral_mekanism.block.blockentity.compact.BECompactFusionReactor;
import astral_mekanism.block.blockentity.compact.BECompactNaquadahReactor;
import astral_mekanism.block.blockentity.compact.BECompactSPS;
import astral_mekanism.block.blockentity.compact.BECompactTEP;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedAdsorptionSeparator;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedAirCompressor;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedAntiprotonicNucleosynthesizer;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedChemicalInfuser;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedChemicalOxidizer;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedChemicalWasher;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedChemixer;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedCrystallizer;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedDissolutionChamber;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedElectrolyticSeparator;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedEnergizedSmelter;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedFormulaicAssemblicator;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedIsotopicCentrifuge;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedMelter;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedMetallurgicInfuser;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedPRC;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedPaintingMachine;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedRotaryCondensentrator;
import astral_mekanism.block.blockentity.enchantedmachine.advanced.BEEnchantedChemicalInjectionChamber;
import astral_mekanism.block.blockentity.enchantedmachine.advanced.BEEnchantedOsmiumCompressor;
import astral_mekanism.block.blockentity.enchantedmachine.advanced.BEEnchantedPurificationChamber;
import astral_mekanism.block.blockentity.enchantedmachine.electric.BEEnchantedCrusher;
import astral_mekanism.block.blockentity.enchantedmachine.electric.BEEnchantedEnrichmentChamber;
import astral_mekanism.block.blockentity.generator.AstralMekGeneratorTier;
import astral_mekanism.block.blockentity.generator.BEAppliedGasBurningGenerator;
import astral_mekanism.block.blockentity.generator.BEGasBurningGenerator;
import astral_mekanism.block.blockentity.generator.BEHeatGenerator;
import astral_mekanism.block.blockentity.normalfactory.BEEnergizedSmeltingFactory;
import astral_mekanism.block.blockentity.normalmachine.BEAstralCrafter;
import astral_mekanism.block.blockentity.normalmachine.BEEssentialEnergizedSmelter;
import astral_mekanism.block.blockentity.normalmachine.BEEssentialFormulaicAssemblicator;
import astral_mekanism.block.blockentity.normalmachine.BEEssentialMetallurgicInfuser;
import astral_mekanism.block.blockentity.normalmachine.BEEssentialOsmiumCompressor;
import astral_mekanism.block.blockentity.normalmachine.BEEssentialReactionChamber;
import astral_mekanism.block.blockentity.normalmachine.BEFluidInfuser;
import astral_mekanism.block.blockentity.normalmachine.BEGasConverter;
import astral_mekanism.block.blockentity.normalmachine.BEGasSynthesizer;
import astral_mekanism.block.blockentity.normalmachine.BEGlowstoneNeutronActivator;
import astral_mekanism.block.blockentity.normalmachine.BEGreenHouse;
import astral_mekanism.block.blockentity.normalmachine.BEInfuseSynthesizer;
import astral_mekanism.block.blockentity.normalmachine.BEInfusingCondensentrator;
import astral_mekanism.block.blockentity.normalmachine.BEInterstellarAntineutronicMatterReconstructionApparatus;
import astral_mekanism.block.blockentity.normalmachine.BEItemCompressor;
import astral_mekanism.block.blockentity.normalmachine.BEItemUnzipper;
import astral_mekanism.block.blockentity.normalmachine.BEMekanicalCharger;
import astral_mekanism.block.blockentity.normalmachine.BEMekanicalComposter;
import astral_mekanism.block.blockentity.normalmachine.BEMekanicalInscriber;
import astral_mekanism.block.blockentity.normalmachine.BEMekanicalMatterCondenser;
import astral_mekanism.block.blockentity.normalmachine.BETransformer;
import astral_mekanism.block.blockentity.other.BEMekanicalMagmaBlock;
import astral_mekanism.block.blockentity.other.BEUpgradeExtractor;
import astral_mekanism.block.blockentity.storage.BEEvenlyInserter;
import astral_mekanism.block.blockentity.storage.BEItemSortableStorage;
import astral_mekanism.block.blockentity.storage.BERatioSeparator;
import astral_mekanism.block.blockentity.storage.BEUniversalStorage;
import astral_mekanism.block.blockentity.storage.BEXpTank;
import astral_mekanism.block.container.factory.ContainerAstralMekanismFactory;
import astral_mekanism.block.container.normalmachine.ContainerAstralCrafter;
import astral_mekanism.block.container.normalmachine.ContainerTransformer;
import astral_mekanism.block.container.other.ContainerItemSortableStorage;
import astral_mekanism.block.container.prefab.ContainerAbstractStorage;
import astral_mekanism.block.container.prefab.ContainerMachineCustomSize;
import astral_mekanism.block.container.prefab.ContainerPagedMachine;
import astral_mekanism.block.container.prefab.ContainerAMEFormulaicAssemblicator.ContainerAstralFormulaicAssemblicator;
import astral_mekanism.block.container.prefab.ContainerAMEFormulaicAssemblicator.ContainerEnchantedFormulaicAssemblicator;
import astral_mekanism.block.container.prefab.ContainerAMEFormulaicAssemblicator.ContainerEssentialFormulaicAseemblicator;
import astral_mekanism.block.shape.AMEBlockShapes;
import astral_mekanism.config.AMEConfig;
import astral_mekanism.enums.AMEUpgrade;
import astral_mekanism.registration.BlockTypeMachine;
import astral_mekanism.registration.MachineDeferredRegister;
import astral_mekanism.registration.MachineRegistryObject;
import astral_mekanism.registration.BlockTypeMachine.BlockMachineBuilder;
import astral_mekanism.registration.RegistrationInterfaces.BlockEntityConstructor;
import fr.iglee42.evolvedmekanism.config.EMConfig;
import mekanism.api.Upgrade;
import mekanism.api.math.FloatingLong;
import mekanism.api.math.FloatingLongSupplier;
import mekanism.api.text.ILangEntry;
import mekanism.common.MekanismLang;
import mekanism.common.block.attribute.AttributeTier;
import mekanism.common.block.prefab.BlockTile.BlockTileModel;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.blocktype.BlockShapes;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.item.block.machine.ItemBlockMachine;
import mekanism.common.registries.MekanismSounds;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.generators.common.registries.GeneratorsSounds;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

public class AMEMachines {
    public static final MachineDeferredRegister MACHINES = new MachineDeferredRegister(AMEConstants.MODID);

    public static final FloatingLongSupplier MAX_SUPPLIER = () -> {
        return FloatingLong.MAX_VALUE;
    };

    private static <BE extends BlockEntityRecipeFactory<?, BE>> EnumMap<AMETier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, ContainerAstralMekanismFactory<BE>, ItemBlockMachine>> registerFactories(
            Function<AMETier, String> nameBuilder,
            BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTileModel<BE, BlockTypeMachine<BE>>> constructor,
            Class<BE> beClass,
            ILangEntry langEntry,
            Function<AMETier, UnaryOperator<BlockMachineBuilder<BlockTypeMachine<BE>, BE>>> operator) {
        EnumMap<AMETier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, ContainerAstralMekanismFactory<BE>, ItemBlockMachine>> result = new EnumMap<>(
                AMETier.class);
        for (AMETier tier : AMETier.values()) {
            result.put(tier, MACHINES.registerDefaultBlockItem(nameBuilder.apply(tier),
                    constructor, beClass, ContainerAstralMekanismFactory<BE>::new, langEntry,
                    builder -> operator.apply(tier).apply(builder.with(new AttributeTier<>(tier)))));
        }
        return result;
    }

    private static <BE extends TileEntityMekanism> EnumMap<AMETier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, ContainerPagedMachine<BE>, ItemBlockMachine>> registerPagedMachines(
            Function<AMETier, String> nameBuilder,
            BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTileModel<BE, BlockTypeMachine<BE>>> constructor,
            Class<BE> beClass,
            ILangEntry langEntry,
            Function<AMETier, UnaryOperator<BlockMachineBuilder<BlockTypeMachine<BE>, BE>>> operator) {
        EnumMap<AMETier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, ContainerPagedMachine<BE>, ItemBlockMachine>> result = new EnumMap<>(
                AMETier.class);
        for (AMETier tier : AMETier.values()) {
            result.put(tier, MACHINES.registerDefaultBlockItem(
                    nameBuilder.apply(tier), constructor, beClass, ContainerPagedMachine<BE>::new, langEntry,
                    builder -> operator.apply(tier).apply(builder).with(new AttributeTier<>(tier))));
        }
        return result;
    }

    private static <BE extends TileEntityMekanism> EnumMap<AMETier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, MekanismTileContainer<BE>, ItemBlockMachine>> registerMachines(
            Function<AMETier, String> nameBuilder,
            BlockEntityConstructor<BE, BlockTypeMachine<BE>, BlockTileModel<BE, BlockTypeMachine<BE>>> constructor,
            Class<BE> beClass,
            ILangEntry langEntry,
            Function<AMETier, UnaryOperator<BlockMachineBuilder<BlockTypeMachine<BE>, BE>>> operator) {
        EnumMap<AMETier, MachineRegistryObject<BE, BlockTileModel<BE, BlockTypeMachine<BE>>, MekanismTileContainer<BE>, ItemBlockMachine>> result = new EnumMap<>(
                AMETier.class);
        for (AMETier tier : AMETier.values()) {
            result.put(tier, MACHINES.registerSimple(nameBuilder.apply(tier), constructor, beClass, langEntry,
                    builder -> operator.apply(tier).apply(builder).with(new AttributeTier<>(tier))));
        }
        return result;
    }

    public static final MachineRegistryObject<BEAppliedCrystallizer, BlockTileModel<BEAppliedCrystallizer, BlockTypeMachine<BEAppliedCrystallizer>>, MekanismTileContainer<BEAppliedCrystallizer>, ItemBlockMachine> APPLIED_CHEMICAL_CRYSTALLIZER = MACHINES
            .registerSimple("applied_crystallizer",
                    BEAppliedCrystallizer::new,
                    BEAppliedCrystallizer.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY, Upgrade.MUFFLING))
                            .withSound(MekanismSounds.CHEMICAL_CRYSTALLIZER));

    public static final MachineRegistryObject<BEAppliedElectrolyticSeparator, BlockTileModel<BEAppliedElectrolyticSeparator, BlockTypeMachine<BEAppliedElectrolyticSeparator>>, MekanismTileContainer<BEAppliedElectrolyticSeparator>, ItemBlockMachine> APPLIED_ELECTROLYTIC_SEPARATOR = MACHINES
            .registerSimple("applied_electrolytic_separator",
                    BEAppliedElectrolyticSeparator::new,
                    BEAppliedElectrolyticSeparator.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY, Upgrade.MUFFLING))
                            .withSound(MekanismSounds.ELECTROLYTIC_SEPARATOR));

    public static final MachineRegistryObject<BEAppliedFissionReactor, BlockTileModel<BEAppliedFissionReactor, BlockTypeMachine<BEAppliedFissionReactor>>, MekanismTileContainer<BEAppliedFissionReactor>, ItemBlockMachine> APPLIED_FISSION_REACTOR = MACHINES
            .registerSimple("applied_fission_reactor",
                    BEAppliedFissionReactor::new,
                    BEAppliedFissionReactor.class,
                    AMELang.DESCRIPTION_COMPACT_MACHINE,
                    BlockMachineBuilder::removeAttributeUpgrade);

    public static final MachineRegistryObject<BEAppliedFusionReactor, BlockTileModel<BEAppliedFusionReactor, BlockTypeMachine<BEAppliedFusionReactor>>, MekanismTileContainer<BEAppliedFusionReactor>, ItemBlockMachine> APPLIED_FUSION_REACTOR = MACHINES
            .registerSimple("applied_fusion_reactor",
                    BEAppliedFusionReactor::new,
                    BEAppliedFusionReactor.class,
                    AMELang.DESCRIPTION_COMPACT_MACHINE,
                    BlockMachineBuilder::removeAttributeUpgrade);

    public static final MachineRegistryObject<BEAppliedIsotopicCentrifuge, BlockTileModel<BEAppliedIsotopicCentrifuge, BlockTypeMachine<BEAppliedIsotopicCentrifuge>>, MekanismTileContainer<BEAppliedIsotopicCentrifuge>, ItemBlockMachine> APPLIED_ISOTPIC_CENTRIFUGE = MACHINES
            .registerSimple("applied_isotopic_centrifuge",
                    BEAppliedIsotopicCentrifuge::new,
                    BEAppliedIsotopicCentrifuge.class,
                    AMELang.DESCRIPTION_COMPACT_MACHINE,
                    builder -> builder
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY, Upgrade.MUFFLING)));

    public static final MachineRegistryObject<BEAppliedNaquadahReactor, BlockTileModel<BEAppliedNaquadahReactor, BlockTypeMachine<BEAppliedNaquadahReactor>>, MekanismTileContainer<BEAppliedNaquadahReactor>, ItemBlockMachine> APPLIED_NAQUADAH_REACTOR = MACHINES
            .registerSimple("applied_naquadah_reactor",
                    BEAppliedNaquadahReactor::new,
                    BEAppliedNaquadahReactor.class,
                    AMELang.DESCRIPTION_COMPACT_MACHINE,
                    BlockMachineBuilder::removeAttributeUpgrade);

    public static final MachineRegistryObject<BEAppliedNeutronActivator, BlockTileModel<BEAppliedNeutronActivator, BlockTypeMachine<BEAppliedNeutronActivator>>, MekanismTileContainer<BEAppliedNeutronActivator>, ItemBlockMachine> APPLIED_NEUTRON_ACTIVATOR = MACHINES
            .registerSimple("applied_neutron_activator",
                    BEAppliedNeutronActivator::new,
                    BEAppliedNeutronActivator.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .removeAttributeUpgrade());

    public static final MachineRegistryObject<BEAppliedRotaryCondensentrator, BlockTileModel<BEAppliedRotaryCondensentrator, BlockTypeMachine<BEAppliedRotaryCondensentrator>>, MekanismTileContainer<BEAppliedRotaryCondensentrator>, ItemBlockMachine> APPLIED_ROTALY_CONDENSENTRATOR = MACHINES
            .registerSimple("applied_rotary_condensentrator",
                    BEAppliedRotaryCondensentrator::new,
                    BEAppliedRotaryCondensentrator.class,
                    AMELang.DESCRIPTION_COMPACT_MACHINE,
                    builder -> builder
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY, Upgrade.MUFFLING)));

    public static final MachineRegistryObject<BEAppliedSmelter, BlockTileModel<BEAppliedSmelter, BlockTypeMachine<BEAppliedSmelter>>, MekanismTileContainer<BEAppliedSmelter>, ItemBlockMachine> APPLIED_SMELTER = MACHINES
            .registerSimple("applied_smelter",
                    BEAppliedSmelter::new,
                    BEAppliedSmelter.class,
                    AMELang.DESCRIPTION_COMPACT_MACHINE,
                    builder -> builder
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY, Upgrade.MUFFLING)));

    public static final MachineRegistryObject<BEAppliedSPS, BlockTileModel<BEAppliedSPS, BlockTypeMachine<BEAppliedSPS>>, MekanismTileContainer<BEAppliedSPS>, ItemBlockMachine> APPLIED_SPS = MACHINES
            .registerSimple("applied_sps",
                    BEAppliedSPS::new,
                    BEAppliedSPS.class,
                    AMELang.DESCRIPTION_COMPACT_MACHINE,
                    builder -> builder
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING)));

    public static final MachineRegistryObject<BEAppliedTEP, BlockTileModel<BEAppliedTEP, BlockTypeMachine<BEAppliedTEP>>, MekanismTileContainer<BEAppliedTEP>, ItemBlockMachine> APPLIED_TEP = MACHINES
            .registerSimple("applied_tep",
                    BEAppliedTEP::new,
                    BEAppliedTEP.class,
                    AMELang.DESCRIPTION_COMPACT_MACHINE,
                    builder -> builder
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING)));

    public static final EnumMap<AMETier, MachineRegistryObject<BEAstralEnergizedSmeltingFactory, BlockTileModel<BEAstralEnergizedSmeltingFactory, BlockTypeMachine<BEAstralEnergizedSmeltingFactory>>, ContainerAstralMekanismFactory<BEAstralEnergizedSmeltingFactory>, ItemBlockMachine>> ASTRAL_ENERGIZED_SMELTING_FACTRIES = registerFactories(
            t -> t.nameForAstral + "_astral_energized_smelting_factory",
            BEAstralEnergizedSmeltingFactory::new,
            BEAstralEnergizedSmeltingFactory.class,
            AMELang.DESCRIPTION_ASTRAL_MACHINE,
            t -> builder -> builder
                    .changeAttributeUpgrade(
                            EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                    AMEUpgrade.XP.getValue()))
                    .withSound(MekanismSounds.ENERGIZED_SMELTER)
                    .withEnergyConfig(MekanismConfig.usage.energizedSmelter, MAX_SUPPLIER));

    public static final MachineRegistryObject<BEAstralChemicalInjectionChamber, BlockTileModel<BEAstralChemicalInjectionChamber, BlockTypeMachine<BEAstralChemicalInjectionChamber>>, MekanismTileContainer<BEAstralChemicalInjectionChamber>, ItemBlockMachine> ASTRAL_CHEMICAL_INJECTION_CHAMBER = MACHINES
            .registerSimple("astral_chemical_injection_chamber",
                    BEAstralChemicalInjectionChamber::new,
                    BEAstralChemicalInjectionChamber.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.chemicalInjectionChamber, MAX_SUPPLIER)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                            AMEUpgrade.AIR_INTAKE.getValue()))
                            .withSound(MekanismSounds.CHEMICAL_INJECTION_CHAMBER));

    public static final MachineRegistryObject<BEAstralOsmiumCompressor, BlockTileModel<BEAstralOsmiumCompressor, BlockTypeMachine<BEAstralOsmiumCompressor>>, MekanismTileContainer<BEAstralOsmiumCompressor>, ItemBlockMachine> ASTRAL_OSMIUM_COMPRESSOR = MACHINES
            .registerSimple("astral_osmium_compressor",
                    BEAstralOsmiumCompressor::new,
                    BEAstralOsmiumCompressor.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.osmiumCompressor, MAX_SUPPLIER)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                            AMEUpgrade.AIR_INTAKE.getValue()))
                            .withSound(MekanismSounds.OSMIUM_COMPRESSOR));

    public static final MachineRegistryObject<BEAstralPurificationChamber, BlockTileModel<BEAstralPurificationChamber, BlockTypeMachine<BEAstralPurificationChamber>>, MekanismTileContainer<BEAstralPurificationChamber>, ItemBlockMachine> ASTRAL_PURIFICATION_CHAMBER = MACHINES
            .registerSimple("astral_purification_chamber",
                    BEAstralPurificationChamber::new,
                    BEAstralPurificationChamber.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.purificationChamber, MAX_SUPPLIER)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                            AMEUpgrade.AIR_INTAKE.getValue()))
                            .withSound(MekanismSounds.PURIFICATION_CHAMBER));

    public static final MachineRegistryObject<BEAstralCrusher, BlockTileModel<BEAstralCrusher, BlockTypeMachine<BEAstralCrusher>>, MekanismTileContainer<BEAstralCrusher>, ItemBlockMachine> ASTRAL_CRUSHER = MACHINES
            .registerSimple("astral_crusher",
                    BEAstralCrusher::new,
                    BEAstralCrusher.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.crusher, MAX_SUPPLIER)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                            .withSound(MekanismSounds.CRUSHER));

    public static final MachineRegistryObject<BEAstralEnrichmentChamber, BlockTileModel<BEAstralEnrichmentChamber, BlockTypeMachine<BEAstralEnrichmentChamber>>, MekanismTileContainer<BEAstralEnrichmentChamber>, ItemBlockMachine> ASTRAL_ENRICHMENT_CHAMBER = MACHINES
            .registerSimple("astral_enrichment_chamber",
                    BEAstralEnrichmentChamber::new,
                    BEAstralEnrichmentChamber.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.enrichmentChamber, MAX_SUPPLIER)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                            .withSound(MekanismSounds.ENRICHMENT_CHAMBER));

    public static final MachineRegistryObject<BEAstralAlloyer, BlockTileModel<BEAstralAlloyer, BlockTypeMachine<BEAstralAlloyer>>, MekanismTileContainer<BEAstralAlloyer>, ItemBlockMachine> ASTRAL_ALLOYER = MACHINES
            .registerSimple("astral_alloyer",
                    BEAstralAlloyer::new,
                    BEAstralAlloyer.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withSound(MekanismSounds.COMBINER)
                            .withEnergyConfig(MekanismConfig.usage.combiner, MAX_SUPPLIER)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue())));

    public static final MachineRegistryObject<BEAstralAPT, BlockTileModel<BEAstralAPT, BlockTypeMachine<BEAstralAPT>>, MekanismTileContainer<BEAstralAPT>, ItemBlockMachine> ASTRAL_APT = MACHINES
            .registerSimple("astral_apt",
                    BEAstralAPT::new,
                    BEAstralAPT.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(EMConfig.general.aptEnergyConsumption, MAX_SUPPLIER)
                            .changeAttributeUpgrade(EnumSet.of(AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                    AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                    AMEUpgrade.AIR_INTAKE.getValue())));

    public static final MachineRegistryObject<BEAstralAntiprotonicNucleosynthesizer, BlockTileModel<BEAstralAntiprotonicNucleosynthesizer, BlockTypeMachine<BEAstralAntiprotonicNucleosynthesizer>>, MekanismTileContainer<BEAstralAntiprotonicNucleosynthesizer>, ItemBlockMachine> ASTRAL_ANTIPROTONIC_NUCLEOSYNTHESIZER = MACHINES
            .registerSimple("astral_antiprotonic_nucleosynthesizer",
                    BEAstralAntiprotonicNucleosynthesizer::new,
                    BEAstralAntiprotonicNucleosynthesizer.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withSound(MekanismSounds.ANTIPROTONIC_NUCLEOSYNTHESIZER)
                            .withEnergyConfig(MekanismConfig.usage.antiprotonicNucleosynthesizer, MAX_SUPPLIER)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                            AMEUpgrade.AIR_INTAKE.getValue())));

    public static final MachineRegistryObject<BEAstralChemicalInfuser, BlockTileModel<BEAstralChemicalInfuser, BlockTypeMachine<BEAstralChemicalInfuser>>, MekanismTileContainer<BEAstralChemicalInfuser>, ItemBlockMachine> ASTRAL_CHEMICAL_INFUSER = MACHINES
            .registerSimple("astral_chemical_infuser",
                    BEAstralChemicalInfuser::new,
                    BEAstralChemicalInfuser.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.chemicalInfuser, MAX_SUPPLIER)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                                    AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                    AMEUpgrade.AIR_INTAKE.getValue()))
                            .withSound(MekanismSounds.CHEMICAL_INFUSER));

    public static final MachineRegistryObject<BEAstralChemicalOxidizer, BlockTileModel<BEAstralChemicalOxidizer, BlockTypeMachine<BEAstralChemicalOxidizer>>, MekanismTileContainer<BEAstralChemicalOxidizer>, ItemBlockMachine> ASTRAL_CHEMICAL_OXIDIZER = MACHINES
            .registerSimple("astral_chemical_oxidizer",
                    BEAstralChemicalOxidizer::new,
                    BEAstralChemicalOxidizer.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.oxidationChamber, MAX_SUPPLIER)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            AMEUpgrade.RADIOACTIVE_SEALING.getValue()))
                            .withSound(MekanismSounds.CHEMICAL_OXIDIZER));

    public static final MachineRegistryObject<BEAstralChemicalWasher, BlockTileModel<BEAstralChemicalWasher, BlockTypeMachine<BEAstralChemicalWasher>>, MekanismTileContainer<BEAstralChemicalWasher>, ItemBlockMachine> ASTRAL_CHEMICAL_WASHER = MACHINES
            .registerSimple("astral_chemical_washer",
                    BEAstralChemicalWasher::new,
                    BEAstralChemicalWasher.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.chemicalWasher, MAX_SUPPLIER)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, AMEUpgrade.WATER_SUPPLY.getValue(),
                                            AMEUpgrade.RADIOACTIVE_SEALING.getValue()))
                            .withSound(MekanismSounds.CHEMICAL_WASHER));

    public static final MachineRegistryObject<BEAstralChemixer, BlockTileModel<BEAstralChemixer, BlockTypeMachine<BEAstralChemixer>>, MekanismTileContainer<BEAstralChemixer>, ItemBlockMachine> ASTRAL_CHEMIXER = MACHINES
            .registerSimple("astral_chemixer",
                    BEAstralChemixer::new,
                    BEAstralChemixer.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.combiner, MAX_SUPPLIER)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                            AMEUpgrade.AIR_INTAKE.getValue()))
                            .withSound(MekanismSounds.PRESSURIZED_REACTION_CHAMBER));

    public static final MachineRegistryObject<BEAstralCombiner, BlockTileModel<BEAstralCombiner, BlockTypeMachine<BEAstralCombiner>>, MekanismTileContainer<BEAstralCombiner>, ItemBlockMachine> ASTRAL_COMBINER = MACHINES
            .registerSimple("astral_combiner",
                    BEAstralCombiner::new,
                    BEAstralCombiner.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.combiner, MAX_SUPPLIER)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                            .withSound(MekanismSounds.COMBINER));

    public static final MachineRegistryObject<BEAstralComposter, BlockTileModel<BEAstralComposter, BlockTypeMachine<BEAstralComposter>>, MekanismTileContainer<BEAstralComposter>, ItemBlockMachine> ASTRAL_COMPOSTER = MACHINES
            .registerSimple("astral_composter",
                    BEAstralComposter::new,
                    BEAstralComposter.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                            .withCustomShape(AMEBlockShapes.COMPOSTER));

    public static final MachineRegistryObject<BEAstralCrystallizer, BlockTileModel<BEAstralCrystallizer, BlockTypeMachine<BEAstralCrystallizer>>, MekanismTileContainer<BEAstralCrystallizer>, ItemBlockMachine> ASTRAL_CRYSTALLIZER = MACHINES
            .registerSimple("astral_crystallizer",
                    BEAstralCrystallizer::new,
                    BEAstralCrystallizer.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.chemicalCrystallizer, MAX_SUPPLIER)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                                    AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                    AMEUpgrade.AIR_INTAKE.getValue()))
                            .withSound(MekanismSounds.CHEMICAL_CRYSTALLIZER));

    public static final MachineRegistryObject<BEAstralDissolutionChamber, BlockTileModel<BEAstralDissolutionChamber, BlockTypeMachine<BEAstralDissolutionChamber>>, MekanismTileContainer<BEAstralDissolutionChamber>, ItemBlockMachine> ASTRAL_DISSOLUTION_CHAMBER = MACHINES
            .registerSimple("astral_dissolution_chamber",
                    BEAstralDissolutionChamber::new,
                    BEAstralDissolutionChamber.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.chemicalDissolutionChamber, MAX_SUPPLIER)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                            AMEUpgrade.AIR_INTAKE.getValue()))
                            .withSound(MekanismSounds.CHEMICAL_DISSOLUTION_CHAMBER));

    public static final MachineRegistryObject<BEAstralElectrolyticSeparator, BlockTileModel<BEAstralElectrolyticSeparator, BlockTypeMachine<BEAstralElectrolyticSeparator>>, MekanismTileContainer<BEAstralElectrolyticSeparator>, ItemBlockMachine> ASTRAL_ELECTROLYTIC_SEPARATOR = MACHINES
            .registerSimple("astral_electrolytic_separator",
                    BEAstralElectrolyticSeparator::new,
                    BEAstralElectrolyticSeparator.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(() -> MekanismConfig.general.FROM_H2.get().multiply(2),
                                    MAX_SUPPLIER)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, AMEUpgrade.WATER_SUPPLY.getValue(),
                                            AMEUpgrade.RADIOACTIVE_SEALING.getValue()))
                            .withSound(MekanismSounds.ELECTROLYTIC_SEPARATOR));

    public static final MachineRegistryObject<BEAstralEnergizedSmelter, BlockTileModel<BEAstralEnergizedSmelter, BlockTypeMachine<BEAstralEnergizedSmelter>>, MekanismTileContainer<BEAstralEnergizedSmelter>, ItemBlockMachine> ASTRAL_ENERGIZED_SMELTER = MACHINES
            .registerSimple("astral_energized_smelter",
                    BEAstralEnergizedSmelter::new,
                    BEAstralEnergizedSmelter.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.energizedSmelter, MAX_SUPPLIER)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue(), AMEUpgrade.XP.getValue()))
                            .withSound(MekanismSounds.ENERGIZED_SMELTER));

    public static final MachineRegistryObject<BEAstralFluidInfuser, BlockTileModel<BEAstralFluidInfuser, BlockTypeMachine<BEAstralFluidInfuser>>, MekanismTileContainer<BEAstralFluidInfuser>, ItemBlockMachine> ASTRAL_FLUID_INFUSER = MACHINES
            .registerSimple("astral_fluid_infuser",
                    BEAstralFluidInfuser::new,
                    BEAstralFluidInfuser.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(AMEConfig.usage.fluidInfuser, MAX_SUPPLIER)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, AMEUpgrade.WATER_SUPPLY.getValue()))
                            .withSound(MekanismSounds.CHEMICAL_INFUSER));

    public static final MachineRegistryObject<BEAstralFormulaicAssemblicator, BlockTileModel<BEAstralFormulaicAssemblicator, BlockTypeMachine<BEAstralFormulaicAssemblicator>>, ContainerAstralFormulaicAssemblicator, ItemBlockMachine> ASTRAL_FORMULAIC_ASSEMBLICATOR = MACHINES
            .registerDefaultBlockItem("astral_formulaic_assemblicator",
                    BEAstralFormulaicAssemblicator::new,
                    BEAstralFormulaicAssemblicator.class,
                    ContainerAstralFormulaicAssemblicator::new,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.formulaicAssemblicator, MAX_SUPPLIER)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.ENERGY, AMEUpgrade.COBBLESTONE_SUPPLY.getValue())));

    public static final MachineRegistryObject<BEAstralGNA, BlockTileModel<BEAstralGNA, BlockTypeMachine<BEAstralGNA>>, MekanismTileContainer<BEAstralGNA>, ItemBlockMachine> ASTRAL_GNA = MACHINES
            .registerSimple("astral_gna",
                    BEAstralGNA::new,
                    BEAstralGNA.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                            AMEUpgrade.AIR_INTAKE.getValue())));

    public static final MachineRegistryObject<BEAstralGreenHouse, BlockTileModel<BEAstralGreenHouse, BlockTypeMachine<BEAstralGreenHouse>>, MekanismTileContainer<BEAstralGreenHouse>, ItemBlockMachine> ASTRAL_GREEN_HOUSE = MACHINES
            .registerSimple("astral_green_house",
                    BEAstralGreenHouse::new,
                    BEAstralGreenHouse.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(AMEConfig.usage.greenHouse, MAX_SUPPLIER)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue(), AMEUpgrade.WATER_SUPPLY.getValue())));

    public static final MachineRegistryObject<BEAstralIsotopicCentrifuge, BlockTileModel<BEAstralIsotopicCentrifuge, BlockTypeMachine<BEAstralIsotopicCentrifuge>>, MekanismTileContainer<BEAstralIsotopicCentrifuge>, ItemBlockMachine> ASTRAL_ISOTOPIC_CENTRIFUGE = MACHINES
            .registerSimple("astral_isotopic_centrifuge",
                    BEAstralIsotopicCentrifuge::new,
                    BEAstralIsotopicCentrifuge.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.isotopicCentrifuge, MAX_SUPPLIER)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                                    AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                    AMEUpgrade.AIR_INTAKE.getValue()))
                            .withSound(MekanismSounds.ISOTOPIC_CENTRIFUGE));

    public static final MachineRegistryObject<BEAstralMekanicalCharger, BlockTileModel<BEAstralMekanicalCharger, BlockTypeMachine<BEAstralMekanicalCharger>>, MekanismTileContainer<BEAstralMekanicalCharger>, ItemBlockMachine> ASTRAL_MEKANICAL_CHARGER = MACHINES
            .registerSimple("astral_mekanical_charger",
                    BEAstralMekanicalCharger::new,
                    BEAstralMekanicalCharger.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(AMEConfig.usage.mekanicalCherger, MAX_SUPPLIER)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue())));

    public static final MachineRegistryObject<BEAstralMekanicalInscriber, BlockTileModel<BEAstralMekanicalInscriber, BlockTypeMachine<BEAstralMekanicalInscriber>>, MekanismTileContainer<BEAstralMekanicalInscriber>, ItemBlockMachine> ASTRAL_MEKANICAL_INSCRIBER = MACHINES
            .registerSimple("astral_mekanical_inscriber",
                    BEAstralMekanicalInscriber::new,
                    BEAstralMekanicalInscriber.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(AMEConfig.usage.mekanicalInscriber, MAX_SUPPLIER)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue())));

    public static final MachineRegistryObject<BEAstralMelter, BlockTileModel<BEAstralMelter, BlockTypeMachine<BEAstralMelter>>, MekanismTileContainer<BEAstralMelter>, ItemBlockMachine> ASTRAL_THERMALIZER = MACHINES
            .registerSimple("astral_thermalizer",
                    BEAstralMelter::new,
                    BEAstralMelter.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.oxidationChamber, MAX_SUPPLIER)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                            .withSound(MekanismSounds.CHEMICAL_OXIDIZER));

    public static final MachineRegistryObject<BEAstralMetallurgicInfuser, BlockTileModel<BEAstralMetallurgicInfuser, BlockTypeMachine<BEAstralMetallurgicInfuser>>, MekanismTileContainer<BEAstralMetallurgicInfuser>, ItemBlockMachine> ASTRAL_METALLURGIC_INFUSER = MACHINES
            .registerSimple("astral_metallurgic_infuser",
                    BEAstralMetallurgicInfuser::new,
                    BEAstralMetallurgicInfuser.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.metallurgicInfuser, MAX_SUPPLIER)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            AMEUpgrade.RADIOACTIVE_SEALING.getValue()))
                            .withSound(MekanismSounds.METALLURGIC_INFUSER));

    public static final MachineRegistryObject<BEAstralPRC, BlockTileModel<BEAstralPRC, BlockTypeMachine<BEAstralPRC>>, MekanismTileContainer<BEAstralPRC>, ItemBlockMachine> ASTRAL_PRC = MACHINES
            .registerSimple("astral_prc",
                    BEAstralPRC::new,
                    BEAstralPRC.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.pressurizedReactionBase, MAX_SUPPLIER)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue(), AMEUpgrade.WATER_SUPPLY.getValue(),
                                    AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                    AMEUpgrade.AIR_INTAKE.getValue()))
                            .withSound(MekanismSounds.PRESSURIZED_REACTION_CHAMBER));

    public static final MachineRegistryObject<BEAstralPrecisionSawmill, BlockTileModel<BEAstralPrecisionSawmill, BlockTypeMachine<BEAstralPrecisionSawmill>>, MekanismTileContainer<BEAstralPrecisionSawmill>, ItemBlockMachine> ASTRAL_PRECISION_SAWMILL = MACHINES
            .registerSimple("astral_precision_sawmill",
                    BEAstralPrecisionSawmill::new,
                    BEAstralPrecisionSawmill.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.precisionSawmill, MAX_SUPPLIER)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                            .withSound(MekanismSounds.PRECISION_SAWMILL));

    public static final MachineRegistryObject<BEAstralRadiationIrradiator, BlockTileModel<BEAstralRadiationIrradiator, BlockTypeMachine<BEAstralRadiationIrradiator>>, MekanismTileContainer<BEAstralRadiationIrradiator>, ItemBlockMachine> ASTRAL_RADIATION_IRRADIATOR = MACHINES
            .registerSimple("astral_radiation_irradiator",
                    BEAstralRadiationIrradiator::new,
                    BEAstralRadiationIrradiator.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(MSConfig.usageConfig.radiationIrradiator, MAX_SUPPLIER)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.ENERGY,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            AMEUpgrade.RADIOACTIVE_SEALING.getValue())));

    public static final MachineRegistryObject<BEAstralReactionChamber, BlockTileModel<BEAstralReactionChamber, BlockTypeMachine<BEAstralReactionChamber>>, MekanismTileContainer<BEAstralReactionChamber>, ItemBlockMachine> ASTRAL_REACTION_CHAMBER = MACHINES
            .registerSimple("astral_reaction_chamber",
                    BEAstralReactionChamber::new,
                    BEAstralReactionChamber.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(AMEConfig.usage.aaeReactionChamber, MAX_SUPPLIER)
                            .withCustomShape(AMEBlockShapes.AAE_REACTION_CHAMBER)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            AMEUpgrade.WATER_SUPPLY.getValue()))
                            .withSound(MekanismSounds.PRECISION_SAWMILL));

    public static final MachineRegistryObject<BEAstralRotaryCondensentrator, BlockTileModel<BEAstralRotaryCondensentrator, BlockTypeMachine<BEAstralRotaryCondensentrator>>, MekanismTileContainer<BEAstralRotaryCondensentrator>, ItemBlockMachine> ASTRAL_ROTARY_CONDENSENTRATOR = MACHINES
            .registerSimple("astral_rotary_condensentrator",
                    BEAstralRotaryCondensentrator::new,
                    BEAstralRotaryCondensentrator.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.rotaryCondensentrator, MAX_SUPPLIER)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, AMEUpgrade.WATER_SUPPLY.getValue(),
                                            AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                            AMEUpgrade.AIR_INTAKE.getValue()))
                            .withSound(MekanismSounds.ROTARY_CONDENSENTRATOR));

    public static final MachineRegistryObject<BEAstralSolidifier, BlockTileModel<BEAstralSolidifier, BlockTypeMachine<BEAstralSolidifier>>, MekanismTileContainer<BEAstralSolidifier>, ItemBlockMachine> ASTRAL_SOLIDIFICATION_CHAMBER = MACHINES
            .registerSimple("astral_solidification_chamber",
                    BEAstralSolidifier::new,
                    BEAstralSolidifier.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.pressurizedReactionBase, MAX_SUPPLIER)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY))
                            .withSound(MekanismSounds.PRESSURIZED_REACTION_CHAMBER));

    public static final MachineRegistryObject<BEAstralSPS, BlockTileModel<BEAstralSPS, BlockTypeMachine<BEAstralSPS>>, MekanismTileContainer<BEAstralSPS>, ItemBlockMachine> ASTRAL_SPS = MACHINES
            .registerSimple("astral_sps",
                    BEAstralSPS::new,
                    BEAstralSPS.class,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(() -> MekanismConfig.general.spsEnergyPerInput.get().multiply(1000),
                                    MAX_SUPPLIER)
                            .withSound(MekanismSounds.SPS)
                            .removeAttributeUpgrade()
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING,
                                    AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                    AMEUpgrade.AIR_INTAKE.getValue())));

    public static final MachineRegistryObject<BEAstralTransformer, BlockTileModel<BEAstralTransformer, BlockTypeMachine<BEAstralTransformer>>, ContainerTransformer<BEAstralTransformer>, ItemBlockMachine> ASTRAL_TRANSFORMER = MACHINES
            .registerDefaultBlockItem("astral_transformer",
                    BEAstralTransformer::new,
                    BEAstralTransformer.class,
                    ContainerTransformer<BEAstralTransformer>::new,
                    AMELang.DESCRIPTION_ASTRAL_MACHINE,
                    builder -> builder
                            .withEnergyConfig(AMEConfig.usage.transformer, MAX_SUPPLIER)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY, AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                    AMEUpgrade.WATER_SUPPLY.getValue())));

    public static final MachineRegistryObject<BECompactAPT, BlockTileModel<BECompactAPT, BlockTypeMachine<BECompactAPT>>, MekanismTileContainer<BECompactAPT>, ItemBlockMachine> COMPACT_APT = MACHINES
            .registerSimple("compact_apt",
                    BECompactAPT::new,
                    BECompactAPT.class,
                    AMELang.DESCRIPTION_COMPACT_MACHINE,
                    builder -> builder
                            .withSound(MekanismSounds.SPS)
                            .withEnergyConfig(EMConfig.general.aptEnergyConsumption, EMConfig.general.aptEnergyStorage)
                            .changeAttributeUpgrade(EnumSet.of(
                                    Upgrade.MUFFLING,
                                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                    AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                    AMEUpgrade.AIR_INTAKE.getValue(),
                                    AMEUpgrade.HYPER_SPEED.getValue())));

    public static final EnumMap<AMETier, MachineRegistryObject<BECompactFissionReactor, BlockTileModel<BECompactFissionReactor, BlockTypeMachine<BECompactFissionReactor>>, MekanismTileContainer<BECompactFissionReactor>, ItemBlockMachine>> COMPACT_FIR = registerMachines(
            tier -> tier.nameForNormal + "_compact_fir",
            BECompactFissionReactor::new,
            BECompactFissionReactor.class,
            AMELang.DESCRIPTION_COMPACT_MACHINE,
            tier -> builder -> builder
                    .changeAttributeUpgrade(EnumSet.of(AMEUpgrade.WATER_SUPPLY.getValue(),
                            AMEUpgrade.RADIOACTIVE_SEALING.getValue())));

    public static final EnumMap<AMETier, MachineRegistryObject<BECompactFusionReactor, BlockTileModel<BECompactFusionReactor, BlockTypeMachine<BECompactFusionReactor>>, MekanismTileContainer<BECompactFusionReactor>, ItemBlockMachine>> COMPACT_FUSION_REACTOR = registerMachines(
            tier -> tier.nameForNormal + "_compact_fusion_reactor",
            BECompactFusionReactor::new,
            BECompactFusionReactor.class,
            AMELang.DESCRIPTION_COMPACT_MACHINE,
            tier -> builder -> builder
                    .changeAttributeUpgrade(EnumSet.of(AMEUpgrade.WATER_SUPPLY.getValue(),
                            AMEUpgrade.RADIOACTIVE_SEALING.getValue())));

    public static final EnumMap<AMETier, MachineRegistryObject<BECompactNaquadahReactor, BlockTileModel<BECompactNaquadahReactor, BlockTypeMachine<BECompactNaquadahReactor>>, MekanismTileContainer<BECompactNaquadahReactor>, ItemBlockMachine>> COMPACT_NAQUADAH_REACTOR = registerMachines(
            tier -> tier.nameForNormal + "_compact_naquadah_reactor",
            BECompactNaquadahReactor::new,
            BECompactNaquadahReactor.class,
            AMELang.DESCRIPTION_COMPACT_MACHINE,
            tier -> builder -> builder
                    .changeAttributeUpgrade(EnumSet.of(AMEUpgrade.WATER_SUPPLY.getValue(),
                            AMEUpgrade.RADIOACTIVE_SEALING.getValue())));

    public static final MachineRegistryObject<BECompactSPS, BlockTileModel<BECompactSPS, BlockTypeMachine<BECompactSPS>>, MekanismTileContainer<BECompactSPS>, ItemBlockMachine> COMPACT_SPS = MACHINES
            .registerSimple("compact_sps",
                    BECompactSPS::new,
                    BECompactSPS.class,
                    AMELang.DESCRIPTION_COMPACT_MACHINE,
                    builder -> builder
                            .withEnergyConfig(() -> MekanismConfig.general.spsEnergyPerInput.get().multiply(1000),
                                    () -> MekanismConfig.general.spsEnergyPerInput.get().multiply(64000))
                            .withSound(MekanismSounds.SPS)
                            .removeAttributeUpgrade()
                            .changeAttributeUpgrade(EnumSet.of(
                                    Upgrade.MUFFLING,
                                    AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                    AMEUpgrade.AIR_INTAKE.getValue(),
                                    AMEUpgrade.HYPER_SPEED.getValue())));

    public static final EnumMap<AMETier, MachineRegistryObject<BECompactTEP, BlockTileModel<BECompactTEP, BlockTypeMachine<BECompactTEP>>, ContainerPagedMachine<BECompactTEP>, ItemBlockMachine>> COMPACT_TEP = registerPagedMachines(
            tier -> tier.nameForNormal + "_compact_tep",
            BECompactTEP::new,
            BECompactTEP.class,
            AMELang.DESCRIPTION_COMPACT_MACHINE,
            tier -> builder -> builder
                    .withEnergyConfig(() -> FloatingLong.create(100), () -> FloatingLong.create(40000))
                    .withSound(MekanismSounds.RESISTIVE_HEATER)
                    .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, AMEUpgrade.WATER_SUPPLY.getValue())));

    public static final MachineRegistryObject<BEEnchantedChemicalInjectionChamber, BlockTileModel<BEEnchantedChemicalInjectionChamber, BlockTypeMachine<BEEnchantedChemicalInjectionChamber>>, MekanismTileContainer<BEEnchantedChemicalInjectionChamber>, ItemBlockMachine> ENCHANTED_CHEMICAL_INJECTION_CHAMBER = MACHINES
            .registerSimple("enchanted_chemical_injection_chamber",
                    BEEnchantedChemicalInjectionChamber::new,
                    BEEnchantedChemicalInjectionChamber.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.chemicalInjectionChamber,
                                    () -> MekanismConfig.storage.chemicalInjectionChamber.get().multiply(200000))
                            .withSound(MekanismSounds.CHEMICAL_INJECTION_CHAMBER)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.SPEED, Upgrade.ENERGY,
                                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                    AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                    ExtraUpgrade.STACK)));

    public static final MachineRegistryObject<BEEnchantedOsmiumCompressor, BlockTileModel<BEEnchantedOsmiumCompressor, BlockTypeMachine<BEEnchantedOsmiumCompressor>>, MekanismTileContainer<BEEnchantedOsmiumCompressor>, ItemBlockMachine> ENCHANTED_OSMIUM_COMPRESSOR = MACHINES
            .registerSimple("enchanted_osmium_compressor",
                    BEEnchantedOsmiumCompressor::new,
                    BEEnchantedOsmiumCompressor.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.osmiumCompressor,
                                    () -> MekanismConfig.storage.osmiumCompressor.get().multiply(200000))
                            .withSound(MekanismSounds.OSMIUM_COMPRESSOR)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.SPEED, Upgrade.ENERGY,
                                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                    AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                    ExtraUpgrade.STACK)));

    public static final MachineRegistryObject<BEEnchantedPurificationChamber, BlockTileModel<BEEnchantedPurificationChamber, BlockTypeMachine<BEEnchantedPurificationChamber>>, MekanismTileContainer<BEEnchantedPurificationChamber>, ItemBlockMachine> ENCHANTED_PURIFICATION_CHAMBER = MACHINES
            .registerSimple("enchanted_purification_chamber",
                    BEEnchantedPurificationChamber::new,
                    BEEnchantedPurificationChamber.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.purificationChamber,
                                    () -> MekanismConfig.storage.purificationChamber.get().multiply(200000))
                            .withSound(MekanismSounds.PURIFICATION_CHAMBER)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.SPEED, Upgrade.ENERGY,
                                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                    AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                    ExtraUpgrade.STACK)));

    public static final MachineRegistryObject<BEEnchantedCrusher, BlockTileModel<BEEnchantedCrusher, BlockTypeMachine<BEEnchantedCrusher>>, MekanismTileContainer<BEEnchantedCrusher>, ItemBlockMachine> ENCHANTED_CRUSHER = MACHINES
            .registerSimple("enchanted_crusher",
                    BEEnchantedCrusher::new,
                    BEEnchantedCrusher.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.crusher,
                                    () -> MekanismConfig.storage.crusher.get().multiply(200000))
                            .withSound(MekanismSounds.CRUSHER)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.SPEED, Upgrade.ENERGY,
                                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                    ExtraUpgrade.STACK)));

    public static final MachineRegistryObject<BEEnchantedEnrichmentChamber, BlockTileModel<BEEnchantedEnrichmentChamber, BlockTypeMachine<BEEnchantedEnrichmentChamber>>, MekanismTileContainer<BEEnchantedEnrichmentChamber>, ItemBlockMachine> ENCHANTED_ENRICHMENT_CHAMBER = MACHINES
            .registerSimple("enchanted_enrichment_chamber",
                    BEEnchantedEnrichmentChamber::new,
                    BEEnchantedEnrichmentChamber.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.enrichmentChamber,
                                    () -> MekanismConfig.storage.enrichmentChamber.get().multiply(200000))
                            .withSound(MekanismSounds.ENRICHMENT_CHAMBER)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.SPEED, Upgrade.ENERGY,
                                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                    ExtraUpgrade.STACK)));

    public static final MachineRegistryObject<BEEnchantedAdsorptionSeparator, BlockTileModel<BEEnchantedAdsorptionSeparator, BlockTypeMachine<BEEnchantedAdsorptionSeparator>>, MekanismTileContainer<BEEnchantedAdsorptionSeparator>, ItemBlockMachine> ENCHANTED_ADSORPTION_SEPARATOR = MACHINES
            .registerSimple("enchanted_adsorption_separator",
                    BEEnchantedAdsorptionSeparator::new,
                    BEEnchantedAdsorptionSeparator.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(MSConfig.usageConfig.adsorptionSeparator,
                                    () -> MSConfig.storageConfig.adsorptionSeparator.get().multiply(200000))
                            .withSound(MekanismSounds.CHEMICAL_INFUSER)
                            .withCustomShape(MSBlockShapes.ADSORPTION_SEPARATOR)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.SPEED, Upgrade.ENERGY,
                                    AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                    ExtraUpgrade.STACK)));

    public static final MachineRegistryObject<BEEnchantedAirCompressor, BlockTileModel<BEEnchantedAirCompressor, BlockTypeMachine<BEEnchantedAirCompressor>>, MekanismTileContainer<BEEnchantedAirCompressor>, ItemBlockMachine> ENCHANTED_AIR_COMPRESSOR = MACHINES
            .registerSimple("enchanted_air_compressor",
                    BEEnchantedAirCompressor::new,
                    BEEnchantedAirCompressor.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(MSConfig.usageConfig.airCompressor,
                                    () -> MSConfig.storageConfig.airCompressor.get().multiply(200000))
                            .withSound(MekanismSounds.CHEMICAL_INFUSER)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.SPEED, Upgrade.ENERGY,
                                    AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                    ExtraUpgrade.STACK)));

    public static final MachineRegistryObject<BEEnchantedAntiprotonicNucleosynthesizer, BlockTileModel<BEEnchantedAntiprotonicNucleosynthesizer, BlockTypeMachine<BEEnchantedAntiprotonicNucleosynthesizer>>, MekanismTileContainer<BEEnchantedAntiprotonicNucleosynthesizer>, ItemBlockMachine> ENCHANTED_ANTIPROTONIC_NUCLEOSYNTHESIZER = MACHINES
            .registerSimple("enchanted_antiprotonic_nucleosynthesizer",
                    BEEnchantedAntiprotonicNucleosynthesizer::new,
                    BEEnchantedAntiprotonicNucleosynthesizer.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.antiprotonicNucleosynthesizer,
                                    () -> MekanismConfig.storage.antiprotonicNucleosynthesizer.get().multiply(200000))
                            .withSound(MekanismSounds.ANTIPROTONIC_NUCLEOSYNTHESIZER)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, AMEUpgrade.HYPER_SPEED.getValue(),
                                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                    AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                    AMEUpgrade.AIR_INTAKE.getValue(),
                                    ExtraUpgrade.STACK)));

    public static final MachineRegistryObject<BEEnchantedChemicalInfuser, BlockTileModel<BEEnchantedChemicalInfuser, BlockTypeMachine<BEEnchantedChemicalInfuser>>, MekanismTileContainer<BEEnchantedChemicalInfuser>, ItemBlockMachine> ENCHANTED_CHEMICAL_INFUSER = MACHINES
            .registerSimple("enchanted_chemical_infuser",
                    BEEnchantedChemicalInfuser::new,
                    BEEnchantedChemicalInfuser.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.chemicalInfuser,
                                    () -> MekanismConfig.storage.chemicalInfuser.get().multiply(200000))
                            .withSound(MekanismSounds.CHEMICAL_INFUSER)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.SPEED, Upgrade.ENERGY,
                                    AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                    AMEUpgrade.AIR_INTAKE.getValue(),
                                    ExtraUpgrade.STACK)));

    public static final MachineRegistryObject<BEEnchantedChemicalWasher, BlockTileModel<BEEnchantedChemicalWasher, BlockTypeMachine<BEEnchantedChemicalWasher>>, MekanismTileContainer<BEEnchantedChemicalWasher>, ItemBlockMachine> ENCHANTED_CHEMICAL_WASHER = MACHINES
            .registerSimple("enchanted_chemical_washer",
                    BEEnchantedChemicalWasher::new,
                    BEEnchantedChemicalWasher.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.chemicalWasher,
                                    () -> MekanismConfig.storage.chemicalWasher.get().multiply(200000))
                            .withSound(MekanismSounds.CHEMICAL_OXIDIZER)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.SPEED, Upgrade.ENERGY,
                                    AMEUpgrade.WATER_SUPPLY.getValue(),
                                    AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                    ExtraUpgrade.STACK)));

    public static final MachineRegistryObject<BEEnchantedChemicalOxidizer, BlockTileModel<BEEnchantedChemicalOxidizer, BlockTypeMachine<BEEnchantedChemicalOxidizer>>, MekanismTileContainer<BEEnchantedChemicalOxidizer>, ItemBlockMachine> ENCHANTED_CHEMICAL_OXIDIZER = MACHINES
            .registerSimple("enchanted_chemical_oxidizer",
                    BEEnchantedChemicalOxidizer::new,
                    BEEnchantedChemicalOxidizer.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.oxidationChamber,
                                    () -> MekanismConfig.storage.oxidationChamber.get().multiply(200000))
                            .withSound(MekanismSounds.CHEMICAL_OXIDIZER)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING, Upgrade.SPEED, Upgrade.ENERGY,
                                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                    AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                    ExtraUpgrade.STACK)));

    public static final MachineRegistryObject<BEEnchantedChemixer, BlockTileModel<BEEnchantedChemixer, BlockTypeMachine<BEEnchantedChemixer>>, MekanismTileContainer<BEEnchantedChemixer>, ItemBlockMachine> ENCHANTED_CHEMIXER = MACHINES
            .registerSimple("enchanted_chemixer",
                    BEEnchantedChemixer::new,
                    BEEnchantedChemixer.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.combiner,
                                    () -> MekanismConfig.storage.combiner.get().multiply(200000))
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                            AMEUpgrade.AIR_INTAKE.getValue(),
                                            ExtraUpgrade.STACK))
                            .withSound(MekanismSounds.PRESSURIZED_REACTION_CHAMBER));

    public static final MachineRegistryObject<BEEnchantedCrystallizer, BlockTileModel<BEEnchantedCrystallizer, BlockTypeMachine<BEEnchantedCrystallizer>>, MekanismTileContainer<BEEnchantedCrystallizer>, ItemBlockMachine> ENCHANTED_CRYSTALLIZER = MACHINES
            .registerSimple("enchanted_crystallizer",
                    BEEnchantedCrystallizer::new,
                    BEEnchantedCrystallizer.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.chemicalCrystallizer,
                                    () -> MekanismConfig.storage.chemicalCrystallizer.get().multiply(200000))
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                                            AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                            AMEUpgrade.AIR_INTAKE.getValue(),
                                            ExtraUpgrade.STACK))
                            .withSound(MekanismSounds.CHEMICAL_CRYSTALLIZER));

    public static final MachineRegistryObject<BEEnchantedDissolutionChamber, BlockTileModel<BEEnchantedDissolutionChamber, BlockTypeMachine<BEEnchantedDissolutionChamber>>, MekanismTileContainer<BEEnchantedDissolutionChamber>, ItemBlockMachine> ENCHANTED_DISSOLUTION_CHAMBER = MACHINES
            .registerSimple("enchanted_dissolution_chamber",
                    BEEnchantedDissolutionChamber::new,
                    BEEnchantedDissolutionChamber.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.chemicalDissolutionChamber,
                                    () -> MekanismConfig.storage.chemicalDissolutionChamber.get().multiply(200000))
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                                            AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            AMEUpgrade.AIR_INTAKE.getValue(),
                                            ExtraUpgrade.STACK))
                            .withSound(MekanismSounds.CHEMICAL_DISSOLUTION_CHAMBER));

    public static final MachineRegistryObject<BEEnchantedElectrolyticSeparator, BlockTileModel<BEEnchantedElectrolyticSeparator, BlockTypeMachine<BEEnchantedElectrolyticSeparator>>, MekanismTileContainer<BEEnchantedElectrolyticSeparator>, ItemBlockMachine> ENCHANTED_ELECTROLYTIC_SEPARATOR = MACHINES
            .registerSimple("enchanted_electrolytic_separator",
                    BEEnchantedElectrolyticSeparator::new,
                    BEEnchantedElectrolyticSeparator.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(() -> MekanismConfig.general.FROM_H2.get().multiply(2),
                                    () -> MekanismConfig.storage.electrolyticSeparator.get().multiply(200000))
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                                            AMEUpgrade.WATER_SUPPLY.getValue(),
                                            AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                            ExtraUpgrade.STACK))
                            .withSound(MekanismSounds.ELECTROLYTIC_SEPARATOR));

    public static final MachineRegistryObject<BEEnchantedEnergizedSmelter, BlockTileModel<BEEnchantedEnergizedSmelter, BlockTypeMachine<BEEnchantedEnergizedSmelter>>, MekanismTileContainer<BEEnchantedEnergizedSmelter>, ItemBlockMachine> ENCHANTED_ENERGIZED_SMELTER = MACHINES
            .registerSimple("enchanted_energized_smelter",
                    BEEnchantedEnergizedSmelter::new,
                    BEEnchantedEnergizedSmelter.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(() -> MekanismConfig.general.FROM_H2.get().multiply(2),
                                    () -> MekanismConfig.storage.electrolyticSeparator.get().multiply(200000))
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            AMEUpgrade.XP.getValue(),
                                            ExtraUpgrade.STACK))
                            .withSound(MekanismSounds.ELECTROLYTIC_SEPARATOR));

    public static final MachineRegistryObject<BEEnchantedFormulaicAssemblicator, BlockTileModel<BEEnchantedFormulaicAssemblicator, BlockTypeMachine<BEEnchantedFormulaicAssemblicator>>, ContainerEnchantedFormulaicAssemblicator, ItemBlockMachine> ENCHANTED_FORMULAIC_ASSEMBLICATOR = MACHINES
            .registerDefaultBlockItem("enchanted_formulaic_assemblicator",
                    BEEnchantedFormulaicAssemblicator::new,
                    BEEnchantedFormulaicAssemblicator.class,
                    ContainerEnchantedFormulaicAssemblicator::new,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.formulaicAssemblicator,
                                    () -> MekanismConfig.storage.formulaicAssemblicator.get().multiply(200000))
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.ENERGY, Upgrade.SPEED,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            ExtraUpgrade.STACK)));

    public static final MachineRegistryObject<BEEnchantedIsotopicCentrifuge, BlockTileModel<BEEnchantedIsotopicCentrifuge, BlockTypeMachine<BEEnchantedIsotopicCentrifuge>>, MekanismTileContainer<BEEnchantedIsotopicCentrifuge>, ItemBlockMachine> ENCHANTED_ISTOPIC_CENTRIFUGE = MACHINES
            .registerSimple("enchanted_isotopic_centrifuge",
                    BEEnchantedIsotopicCentrifuge::new,
                    BEEnchantedIsotopicCentrifuge.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.isotopicCentrifuge,
                                    () -> MekanismConfig.storage.isotopicCentrifuge.get().multiply(200000))
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                                            AMEUpgrade.AIR_INTAKE.getValue(),
                                            AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                            ExtraUpgrade.STACK))
                            .withSound(MekanismSounds.ISOTOPIC_CENTRIFUGE));

    public static final MachineRegistryObject<BEEnchantedMelter, BlockTileModel<BEEnchantedMelter, BlockTypeMachine<BEEnchantedMelter>>, MekanismTileContainer<BEEnchantedMelter>, ItemBlockMachine> ENCHANTED_MELTER = MACHINES
            .registerSimple("enchanted_thermalizer",
                    BEEnchantedMelter::new,
                    BEEnchantedMelter.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                                            AMEUpgrade.AIR_INTAKE.getValue(),
                                            AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                            ExtraUpgrade.STACK))
                            .withSound(MekanismSounds.CHEMICAL_OXIDIZER));

    public static final MachineRegistryObject<BEEnchantedMetallurgicInfuser, BlockTileModel<BEEnchantedMetallurgicInfuser, BlockTypeMachine<BEEnchantedMetallurgicInfuser>>, MekanismTileContainer<BEEnchantedMetallurgicInfuser>, ItemBlockMachine> ENCHANTED_METALLURGIC_INFUSER = MACHINES
            .registerSimple("enchanted_metallurgic_infuser",
                    BEEnchantedMetallurgicInfuser::new,
                    BEEnchantedMetallurgicInfuser.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.metallurgicInfuser,
                                    () -> MekanismConfig.storage.metallurgicInfuser.get().multiply(200000))
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            ExtraUpgrade.STACK))
                            .withSound(MekanismSounds.METALLURGIC_INFUSER));

    public static final MachineRegistryObject<BEEnchantedPaintingMachine, BlockTileModel<BEEnchantedPaintingMachine, BlockTypeMachine<BEEnchantedPaintingMachine>>, MekanismTileContainer<BEEnchantedPaintingMachine>, ItemBlockMachine> ENCHANTED_PAINTING_MACHINE = MACHINES
            .registerSimple("enchanted_painting_machine",
                    BEEnchantedPaintingMachine::new,
                    BEEnchantedPaintingMachine.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.paintingMachine,
                                    () -> MekanismConfig.storage.paintingMachine.get().multiply(200000))
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            ExtraUpgrade.STACK))
                            .withSound(MekanismSounds.PAINTING_MACHINE));

    public static final MachineRegistryObject<BEEnchantedPRC, BlockTileModel<BEEnchantedPRC, BlockTypeMachine<BEEnchantedPRC>>, MekanismTileContainer<BEEnchantedPRC>, ItemBlockMachine> ENCHANTED_PRC = MACHINES
            .registerSimple("enchanted_prc",
                    BEEnchantedPRC::new,
                    BEEnchantedPRC.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.pressurizedReactionBase,
                                    () -> MekanismConfig.storage.pressurizedReactionBase.get().multiply(2000000))
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            AMEUpgrade.WATER_SUPPLY.getValue(),
                                            AMEUpgrade.AIR_INTAKE.getValue(),
                                            ExtraUpgrade.STACK))
                            .withSound(MekanismSounds.PRESSURIZED_REACTION_CHAMBER));

    public static final MachineRegistryObject<BEEnchantedRotaryCondensentrator, BlockTileModel<BEEnchantedRotaryCondensentrator, BlockTypeMachine<BEEnchantedRotaryCondensentrator>>, MekanismTileContainer<BEEnchantedRotaryCondensentrator>, ItemBlockMachine> ENCHANTED_ROTARY_CONDENSENTRATOR = MACHINES
            .registerSimple("enchanted_rotary_condensentrator",
                    BEEnchantedRotaryCondensentrator::new,
                    BEEnchantedRotaryCondensentrator.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.rotaryCondensentrator,
                                    () -> MekanismConfig.storage.rotaryCondensentrator.get().multiply(200000))
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.MUFFLING, Upgrade.ENERGY, Upgrade.SPEED,
                                            AMEUpgrade.AIR_INTAKE.getValue(),
                                            AMEUpgrade.WATER_SUPPLY.getValue(),
                                            AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                            ExtraUpgrade.STACK))
                            .withSound(MekanismSounds.ROTARY_CONDENSENTRATOR));

    public static final MachineRegistryObject<BEAppliedGasBurningGenerator, BlockTileModel<BEAppliedGasBurningGenerator, BlockTypeMachine<BEAppliedGasBurningGenerator>>, MekanismTileContainer<BEAppliedGasBurningGenerator>, ItemBlockMachine> APPLIED_GAS_BURNING_GENERATOR = MACHINES
            .registerSimple("applied_gas_burning_generator",
                    BEAppliedGasBurningGenerator::new,
                    BEAppliedGasBurningGenerator.class,
                    AMELang.DESCRIPTION_AM_GENERATOR,
                    builder -> builder
                            .withCustomShape(AMEBlockShapes.GAS_BURNING_GENERATOR)
                            .withSound(GeneratorsSounds.GAS_BURNING_GENERATOR)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING,
                                    AMEUpgrade.RADIOACTIVE_SEALING.getValue())));

    public static final EnumMap<AstralMekGeneratorTier, MachineRegistryObject<BEGasBurningGenerator, BlockTileModel<BEGasBurningGenerator, BlockTypeMachine<BEGasBurningGenerator>>, MekanismTileContainer<BEGasBurningGenerator>, ItemBlockMachine>> GAS_BURNING_GENERATORS = MACHINES
            .registerSimpleMap(k -> k.name + "_gas_burning_generator",
                    BEGasBurningGenerator::new,
                    BEGasBurningGenerator.class,
                    AMELang.DESCRIPTION_AM_GENERATOR,
                    (key, btm) -> btm.with(new AttributeTier<>(key))
                            .withCustomShape(AMEBlockShapes.GAS_BURNING_GENERATOR)
                            .withSound(GeneratorsSounds.GAS_BURNING_GENERATOR)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.MUFFLING,
                                    AMEUpgrade.RADIOACTIVE_SEALING.getValue())),
                    AstralMekGeneratorTier.class);

    public static final EnumMap<AstralMekGeneratorTier, MachineRegistryObject<BEHeatGenerator, BlockTileModel<BEHeatGenerator, BlockTypeMachine<BEHeatGenerator>>, MekanismTileContainer<BEHeatGenerator>, ItemBlockMachine>> HEAT_GENERATORS = MACHINES
            .registerSimpleMap(k -> k.name + "_heat_generator",
                    BEHeatGenerator::new,
                    BEHeatGenerator.class,
                    AMELang.DESCRIPTION_AM_GENERATOR,
                    (key, btm) -> btm.with(new AttributeTier<>(key))
                            .withCustomShape(AMEBlockShapes.HEAT_GENERATOR)
                            .removeAttributeUpgrade(),
                    AstralMekGeneratorTier.class);

    public static final EnumMap<AMETier, MachineRegistryObject<BEEnergizedSmeltingFactory, BlockTileModel<BEEnergizedSmeltingFactory, BlockTypeMachine<BEEnergizedSmeltingFactory>>, ContainerAstralMekanismFactory<BEEnergizedSmeltingFactory>, ItemBlockMachine>> ENERGIZED_SMELTING_FACTORIES = registerFactories(
            tier -> tier.nameForNormal + "_energized_smelting_factory",
            BEEnergizedSmeltingFactory::new,
            BEEnergizedSmeltingFactory.class,
            MekanismLang.FACTORY_TYPE,
            tier -> builder -> builder
                    .withSound(MekanismSounds.ENERGIZED_SMELTER)
                    .changeAttributeUpgrade(
                            EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, Upgrade.MUFFLING, ExtraUpgrade.STACK,
                                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                    AMEUpgrade.XP.getValue()))
                    .withEnergyConfig(MekanismConfig.usage.energizedSmelter,
                            () -> MekanismConfig.storage.energizedSmelter.get().multiply(tier.processes)));

    public static final MachineRegistryObject<BEAstralCrafter, BlockTileModel<BEAstralCrafter, BlockTypeMachine<BEAstralCrafter>>, ContainerAstralCrafter, ItemBlockMachine> ASTRAL_CRAFTER = MACHINES
            .registerDefaultBlockItem("essential_crafter",
                    BEAstralCrafter::new,
                    BEAstralCrafter.class,
                    ContainerAstralCrafter::new,
                    AMELang.DESCRIPTION_ASTRAL_CRAFTER,
                    builder -> builder
                            .withEnergyConfig(AMEConfig.usage.essentialCrafter, AMEConfig.storage.essentialCrafter)
                            .changeAttributeUpgrade(EnumSet.of(AMEUpgrade.RADIOACTIVE_SEALING.getValue())));

    public static final MachineRegistryObject<BEEssentialEnergizedSmelter, BlockTileModel<BEEssentialEnergizedSmelter, BlockTypeMachine<BEEssentialEnergizedSmelter>>, MekanismTileContainer<BEEssentialEnergizedSmelter>, ItemBlockMachine> ESSENTIAL_ENERGIZED_SMELTER = MACHINES
            .registerSimple("essential_energized_smelter",
                    BEEssentialEnergizedSmelter::new,
                    BEEssentialEnergizedSmelter.class,
                    MekanismLang.DESCRIPTION_ENERGIZED_SMELTER,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.energizedSmelter,
                                    MekanismConfig.storage.energizedSmelter)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, Upgrade.MUFFLING, ExtraUpgrade.STACK,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            AMEUpgrade.XP.getValue()))
                            .withSound(MekanismSounds.ENERGIZED_SMELTER));

    public static final MachineRegistryObject<BEEssentialFormulaicAssemblicator, BlockTileModel<BEEssentialFormulaicAssemblicator, BlockTypeMachine<BEEssentialFormulaicAssemblicator>>, ContainerEssentialFormulaicAseemblicator, ItemBlockMachine> ESSENTIAL_FORMULAIC_ASSEMBLICATOR = MACHINES
            .registerDefaultBlockItem("essential_formulaic_assemblicator",
                    BEEssentialFormulaicAssemblicator::new,
                    BEEssentialFormulaicAssemblicator.class,
                    ContainerEssentialFormulaicAseemblicator::new,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.formulaicAssemblicator,
                                    () -> MekanismConfig.storage.formulaicAssemblicator.get().multiply(200000))
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.ENERGY, Upgrade.SPEED,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            ExtraUpgrade.STACK)));

    public static final MachineRegistryObject<BEEssentialMetallurgicInfuser, BlockTileModel<BEEssentialMetallurgicInfuser, BlockTypeMachine<BEEssentialMetallurgicInfuser>>, MekanismTileContainer<BEEssentialMetallurgicInfuser>, ItemBlockMachine> ESSENTIAL_METALLURGIC_INFUSER = MACHINES
            .registerSimple("essential_metallurgic_infuser",
                    BEEssentialMetallurgicInfuser::new,
                    BEEssentialMetallurgicInfuser.class,
                    MekanismLang.DESCRIPTION_METALLURGIC_INFUSER,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.metallurgicInfuser,
                                    MekanismConfig.storage.metallurgicInfuser)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, ExtraUpgrade.STACK,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            AMEUpgrade.RADIOACTIVE_SEALING.getValue()))
                            .withCustomShape(BlockShapes.METALLURGIC_INFUSER)
                            .withSound(MekanismSounds.METALLURGIC_INFUSER));

    public static final MachineRegistryObject<BEEssentialOsmiumCompressor, BlockTileModel<BEEssentialOsmiumCompressor, BlockTypeMachine<BEEssentialOsmiumCompressor>>, MekanismTileContainer<BEEssentialOsmiumCompressor>, ItemBlockMachine> ESSENTIAL_OSMIUM_COMPRESSOR = MACHINES
            .registerSimple("essential_osmium_compressor",
                    BEEssentialOsmiumCompressor::new,
                    BEEssentialOsmiumCompressor.class,
                    MekanismLang.DESCRIPTION_OSMIUM_COMPRESSOR,
                    builder -> builder
                            .withEnergyConfig(MekanismConfig.usage.osmiumCompressor,
                                    MekanismConfig.storage.osmiumCompressor)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, Upgrade.MUFFLING, ExtraUpgrade.STACK,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                            AMEUpgrade.AIR_INTAKE.getValue()))
                            .withSound(MekanismSounds.OSMIUM_COMPRESSOR));

    public static final MachineRegistryObject<BEEssentialReactionChamber, BlockTileModel<BEEssentialReactionChamber, BlockTypeMachine<BEEssentialReactionChamber>>, MekanismTileContainer<BEEssentialReactionChamber>, ItemBlockMachine> ESSENTIAL_REACTION_CHAMBER = MACHINES
            .registerSimple("essential_reaction_chamber",
                    BEEssentialReactionChamber::new,
                    BEEssentialReactionChamber.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(AMEConfig.usage.aaeReactionChamber, AMEConfig.storage.aaeReactionChamber)
                            .withCustomShape(AMEBlockShapes.AAE_REACTION_CHAMBER)
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, Upgrade.MUFFLING, ExtraUpgrade.STACK,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            AMEUpgrade.WATER_SUPPLY.getValue()))
                            .withSound(MekanismSounds.OSMIUM_COMPRESSOR));

    public static final MachineRegistryObject<BEFluidInfuser, BlockTileModel<BEFluidInfuser, BlockTypeMachine<BEFluidInfuser>>, MekanismTileContainer<BEFluidInfuser>, ItemBlockMachine> FLUID_INFUSER = MACHINES
            .registerSimple("fluid_infuser",
                    BEFluidInfuser::new,
                    BEFluidInfuser.class,
                    AMELang.DESCRIPTION_FLUID_INFUSER,
                    builder -> builder
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, AMEUpgrade.WATER_SUPPLY.getValue()))
                            .withEnergyConfig(AMEConfig.usage.fluidInfuser, AMEConfig.storage.fluidInfuser));

    public static final MachineRegistryObject<BEGasConverter, BlockTileModel<BEGasConverter, BlockTypeMachine<BEGasConverter>>, MekanismTileContainer<BEGasConverter>, ItemBlockMachine> GAS_CONVERTER = MACHINES
            .registerSimple("gas_converter", BEGasConverter::new, BEGasConverter.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder.changeAttributeUpgrade(
                            EnumSet.of(AMEUpgrade.RADIOACTIVE_SEALING.getValue(), AMEUpgrade.AIR_INTAKE.getValue())));

    public static final MachineRegistryObject<BEGasSynthesizer, BlockTileModel<BEGasSynthesizer, BlockTypeMachine<BEGasSynthesizer>>, MekanismTileContainer<BEGasSynthesizer>, ItemBlockMachine> GAS_SYNTHESIZER = MACHINES
            .registerSimple("gas_synthesizer",
                    BEGasSynthesizer::new,
                    BEGasSynthesizer.class,
                    AMELang.DESCRIPTION_INFUSE_SYNTHESIZER,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(AMEUpgrade.RADIOACTIVE_SEALING.getValue())));

    public static final MachineRegistryObject<BEGlowstoneNeutronActivator, BlockTileModel<BEGlowstoneNeutronActivator, BlockTypeMachine<BEGlowstoneNeutronActivator>>, MekanismTileContainer<BEGlowstoneNeutronActivator>, ItemBlockMachine> GLOWSTONE_NEUTRON_ACTIVATOR = MACHINES
            .registerSimple("glowstone_neutron_activator",
                    BEGlowstoneNeutronActivator::new,
                    BEGlowstoneNeutronActivator.class,
                    AMELang.DESCRIPTION_GLOWSTONE_NEUTRON_ACTIVATOR,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(Upgrade.SPEED,
                            AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                            AMEUpgrade.AIR_INTAKE.getValue())));

    public static final MachineRegistryObject<BEGreenHouse, BlockTileModel<BEGreenHouse, BlockTypeMachine<BEGreenHouse>>, MekanismTileContainer<BEGreenHouse>, ItemBlockMachine> GREEN_HOUSE = MACHINES
            .registerSimple("green_house",
                    BEGreenHouse::new,
                    BEGreenHouse.class,
                    AMELang.DESCRIPTION_GREENHOUSE, builder -> builder
                            .withEnergyConfig(AMEConfig.usage.greenHouse, AMEConfig.storage.greenHouse)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.ENERGY, Upgrade.SPEED, ExtraUpgrade.STACK,
                                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue(), AMEUpgrade.WATER_SUPPLY.getValue())));

    public static final MachineRegistryObject<BEInfuseSynthesizer, BlockTileModel<BEInfuseSynthesizer, BlockTypeMachine<BEInfuseSynthesizer>>, MekanismTileContainer<BEInfuseSynthesizer>, ItemBlockMachine> INFUSE_SYNTHESIZER = MACHINES
            .registerSimple("infuse_synthesizer",
                    BEInfuseSynthesizer::new,
                    BEInfuseSynthesizer.class,
                    AMELang.DESCRIPTION_INFUSE_SYNTHESIZER,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(AMEUpgrade.RADIOACTIVE_SEALING.getValue())));

    public static final MachineRegistryObject<BEInfusingCondensentrator, BlockTileModel<BEInfusingCondensentrator, BlockTypeMachine<BEInfusingCondensentrator>>, MekanismTileContainer<BEInfusingCondensentrator>, ItemBlockMachine> INFUSING_CONDENSENTRATOR = MACHINES
            .registerSimple("infusing_condensentrator",
                    BEInfusingCondensentrator::new,
                    BEInfusingCondensentrator.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(AMEConfig.usage.greenHouse, AMEConfig.storage.greenHouse)
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, Upgrade.MUFFLING,
                                    AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                    AMEUpgrade.AIR_INTAKE.getValue())));

    public static final MachineRegistryObject<BEInterstellarAntineutronicMatterReconstructionApparatus, BlockTileModel<BEInterstellarAntineutronicMatterReconstructionApparatus, BlockTypeMachine<BEInterstellarAntineutronicMatterReconstructionApparatus>>, ContainerMachineCustomSize<BEInterstellarAntineutronicMatterReconstructionApparatus>, ItemBlockMachine> INTERSTELLAR_POSITRONIC_MATTER_RECONSTRUCTION_APPARATUS = MACHINES
            .registerDefaultBlockItem("interstellar_antineutronic_matter_reconstruction_apparatus",
                    BEInterstellarAntineutronicMatterReconstructionApparatus::new,
                    BEInterstellarAntineutronicMatterReconstructionApparatus.class,
                    ContainerMachineCustomSize<BEInterstellarAntineutronicMatterReconstructionApparatus>::new,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .withEnergyConfig(() -> FloatingLong.create(2000000000), MAX_SUPPLIER)
                            .withSound(MekanismSounds.SPS)
                            .changeAttributeUpgrade(EnumSet.of(
                                    Upgrade.MUFFLING,
                                    AMEUpgrade.STARDUST_SPEED.getValue(),
                                    AMEUpgrade.RADIOACTIVE_SEALING.getValue())));

    public static final MachineRegistryObject<BEItemCompressor, BlockTileModel<BEItemCompressor, BlockTypeMachine<BEItemCompressor>>, MekanismTileContainer<BEItemCompressor>, ItemBlockMachine> ITEM_COMPRESSOR = MACHINES
            .registerSimple("item_compressor",
                    BEItemCompressor::new,
                    BEItemCompressor.class,
                    AMELang.DESCRIPTION_ITEM_COMPRESSOR,
                    builder -> builder
                            .changeAttributeUpgrade(EnumSet.of(AMEUpgrade.COBBLESTONE_SUPPLY.getValue())));

    public static final MachineRegistryObject<BEItemUnzipper, BlockTileModel<BEItemUnzipper, BlockTypeMachine<BEItemUnzipper>>, MekanismTileContainer<BEItemUnzipper>, ItemBlockMachine> ITEM_UNZIPPER = MACHINES
            .registerSimple("item_unzipper",
                    BEItemUnzipper::new,
                    BEItemUnzipper.class,
                    AMELang.DESCRIPTION_ITEM_UNZIPPER,
                    builder -> builder);

    public static final MachineRegistryObject<BEMekanicalCharger, BlockTileModel<BEMekanicalCharger, BlockTypeMachine<BEMekanicalCharger>>, MekanismTileContainer<BEMekanicalCharger>, ItemBlockMachine> MEKANICAL_CHARGER = MACHINES
            .registerSimple("mekanical_charger",
                    BEMekanicalCharger::new,
                    BEMekanicalCharger.class,
                    AMELang.DESCRIPTION_MEKANICAL_CHARGER,
                    builder -> builder
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, ExtraUpgrade.STACK,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                            .withEnergyConfig(AMEConfig.usage.mekanicalCherger, AMEConfig.storage.mekanicalCherger));

    public static final MachineRegistryObject<BEMekanicalComposter, BlockTileModel<BEMekanicalComposter, BlockTypeMachine<BEMekanicalComposter>>, MekanismTileContainer<BEMekanicalComposter>, ItemBlockMachine> MEKANICAL_COMPOSTER = MACHINES
            .registerSimple("mekanical_composter",
                    BEMekanicalComposter::new,
                    BEMekanicalComposter.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.SPEED, AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                            .withCustomShape(AMEBlockShapes.COMPOSTER));

    public static final MachineRegistryObject<BEMekanicalInscriber, BlockTileModel<BEMekanicalInscriber, BlockTypeMachine<BEMekanicalInscriber>>, MekanismTileContainer<BEMekanicalInscriber>, ItemBlockMachine> MEKANICAL_INSCRIBER = MACHINES
            .registerSimple("mekanical_inscriber",
                    BEMekanicalInscriber::new,
                    BEMekanicalInscriber.class,
                    AMELang.DESCRIPTION_MEKANICAL_INSCRIBER,
                    builder -> builder
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, ExtraUpgrade.STACK,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue()))
                            .withEnergyConfig(AMEConfig.usage.mekanicalInscriber,
                                    AMEConfig.storage.mekanicalInscriber));

    public static final MachineRegistryObject<BEMekanicalMatterCondenser, BlockTileModel<BEMekanicalMatterCondenser, BlockTypeMachine<BEMekanicalMatterCondenser>>, MekanismTileContainer<BEMekanicalMatterCondenser>, ItemBlockMachine> MEKANICAL_MATTER_CONDENSER = MACHINES
            .registerSimple("mekanical_matter_condenser",
                    BEMekanicalMatterCondenser::new, BEMekanicalMatterCondenser.class, AMELang.ITEM_GROUP,
                    builder -> builder
                            .changeAttributeUpgrade(EnumSet.of(Upgrade.SPEED,
                                    AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                    AMEUpgrade.WATER_SUPPLY.getValue())));

    public static final MachineRegistryObject<BETransformer, BlockTileModel<BETransformer, BlockTypeMachine<BETransformer>>, ContainerTransformer<BETransformer>, ItemBlockMachine> TRANSFORMER = MACHINES
            .registerDefaultBlockItem("transformer",
                    BETransformer::new,
                    BETransformer.class,
                    ContainerTransformer<BETransformer>::new,
                    AMELang.DESCRIPTION_MEKANICAL_TRANSFORMER,
                    builder -> builder
                            .changeAttributeUpgrade(
                                    EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, ExtraUpgrade.STACK,
                                            AMEUpgrade.COBBLESTONE_SUPPLY.getValue(),
                                            AMEUpgrade.WATER_SUPPLY.getValue()))
                            .withEnergyConfig(AMEConfig.usage.transformer, AMEConfig.storage.transformer));

    public static final String[] MEKANICAL_MAGMABLOCK_NAMES = { "essential", "basic", "standard", "advanced", "elite",
            "ultimate", "enchanted", "accumulation", "photon", "absolute_overclocked", "quantum", "supreme_quantum",
            "composite", "cosmic_dense", "origin", "infinite_multiversal", "autonomy", "firmament",
            "astral", "starry_sky" };
    public static final MachineRegistryObject<BEMekanicalMagmaBlock, ?, MekanismTileContainer<BEMekanicalMagmaBlock>, ?>[] MEKANICAL_MAGMABLOCKS = ((Supplier<MachineRegistryObject<BEMekanicalMagmaBlock, ?, MekanismTileContainer<BEMekanicalMagmaBlock>, ?>[]>) () -> {
        @SuppressWarnings("unchecked")
        MachineRegistryObject<BEMekanicalMagmaBlock, ?, MekanismTileContainer<BEMekanicalMagmaBlock>, ?>[] result = new MachineRegistryObject[MEKANICAL_MAGMABLOCK_NAMES.length];
        for (int index = 0; index < MEKANICAL_MAGMABLOCK_NAMES.length; index++) {
            int i = index;
            result[i] = MACHINES.registerDefaultBlockContainer(
                    MEKANICAL_MAGMABLOCK_NAMES[i] + "_mekanical_magma_block",
                    b -> new BlockItem(b, new Item.Properties().fireResistant()),
                    BEMekanicalMagmaBlock::new,
                    BEMekanicalMagmaBlock.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder.removeAttributeUpgrade()
                            .with(new AttributeIntTier(i + 1)));
        }
        return result;
    }).get();

    public static final MachineRegistryObject<BEUpgradeExtractor, BlockTileModel<BEUpgradeExtractor, BlockTypeMachine<BEUpgradeExtractor>>, MekanismTileContainer<BEUpgradeExtractor>, ItemBlockMachine> UPGRADE_EXTRACTOR = MACHINES
            .registerSimple("upgrade_extractor",
                    BEUpgradeExtractor::new,
                    BEUpgradeExtractor.class,
                    AMELang.WIP,
                    BlockMachineBuilder::removeAttributeUpgrade);

    public static final MachineRegistryObject<BEEvenlyInserter, BlockTileModel<BEEvenlyInserter, BlockTypeMachine<BEEvenlyInserter>>, MekanismTileContainer<BEEvenlyInserter>, ItemBlockMachine> EVENLY_INSERTER = MACHINES
            .registerSimple("evenly_inserter",
                    BEEvenlyInserter::new,
                    BEEvenlyInserter.class,
                    AMELang.ITEM_GROUP,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(AMEUpgrade.RADIOACTIVE_SEALING.getValue())));

    public static final MachineRegistryObject<BEUniversalStorage, BlockTileModel<BEUniversalStorage, BlockTypeMachine<BEUniversalStorage>>, ContainerAbstractStorage<BEUniversalStorage>, ItemBlockMachine> UNIVERSAL_STORAGE = MACHINES
            .registerDefaultBlockItem("universal_storage",
                    BEUniversalStorage::new,
                    BEUniversalStorage.class,
                    ContainerAbstractStorage<BEUniversalStorage>::new,
                    AMELang.DESCRIPTION_UNIVERSAL_STORAGE,
                    builder -> builder.changeAttributeUpgrade(
                            EnumSet.of(AMEUpgrade.COBBLESTONE_SUPPLY.getValue(), AMEUpgrade.WATER_SUPPLY.getValue(),
                                    AMEUpgrade.RADIOACTIVE_SEALING.getValue(),
                                    AMEUpgrade.AIR_INTAKE.getValue())));

    public static final MachineRegistryObject<BEItemSortableStorage, BlockTileModel<BEItemSortableStorage, BlockTypeMachine<BEItemSortableStorage>>, ContainerItemSortableStorage<BEItemSortableStorage>, ItemBlockMachine> ITEM_SORTABLE_STORAGE = MACHINES
            .registerDefaultBlockItem("item_sortable_storage",
                    BEItemSortableStorage::new,
                    BEItemSortableStorage.class,
                    ContainerItemSortableStorage<BEItemSortableStorage>::new,
                    AMELang.DESCRIPTION_ITEM_SORTABLE_STORAGE,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(AMEUpgrade.RADIOACTIVE_SEALING.getValue())));

    public static final MachineRegistryObject<BERatioSeparator, BlockTileModel<BERatioSeparator, BlockTypeMachine<BERatioSeparator>>, ContainerPagedMachine<BERatioSeparator>, ItemBlockMachine> RATIO_SEPARATOR = MACHINES
            .registerDefaultBlockItem("ratio_separator",
                    BERatioSeparator::new,
                    BERatioSeparator.class,
                    ContainerPagedMachine<BERatioSeparator>::new,
                    AMELang.ITEM_GROUP,
                    builder -> builder.changeAttributeUpgrade(EnumSet.of(AMEUpgrade.RADIOACTIVE_SEALING.getValue())));

    public static final MachineRegistryObject<BEXpTank, BlockTileModel<BEXpTank, BlockTypeMachine<BEXpTank>>, MekanismTileContainer<BEXpTank>, ItemBlockMachine> XP_TANK = MACHINES
            .registerSimple("xp_tank",
                    BEXpTank::new,
                    BEXpTank.class,
                    MekanismLang.ACTIVE,
                    BlockMachineBuilder::removeAttributeUpgrade);

}
