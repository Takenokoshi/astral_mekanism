package astral_mekanism;

import java.util.Arrays;

import astral_mekanism.block.blockentity.appliedmachine.BEAppliedFusionReactor;
import astral_mekanism.block.blockentity.appliedmachine.BEAppliedNaquadahReactor;
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
import astral_mekanism.block.blockentity.astralmachine.BEAstralReactionChamber;
import astral_mekanism.block.blockentity.astralmachine.BEAstralRotaryCondensentrator;
import astral_mekanism.block.blockentity.astralmachine.BEAstralSPS;
import astral_mekanism.block.blockentity.astralmachine.BEAstralTransformer;
import astral_mekanism.block.blockentity.astralmachine.advanced.BEAstralChemicalInjectionChamber;
import astral_mekanism.block.blockentity.astralmachine.advanced.BEAstralOsmiumCompressor;
import astral_mekanism.block.blockentity.astralmachine.advanced.BEAstralPurificationChamber;
import astral_mekanism.block.blockentity.astralmachine.electric.BEAstralCrusher;
import astral_mekanism.block.blockentity.astralmachine.electric.BEAstralEnrichmentChamber;
import astral_mekanism.block.blockentity.compact.BECompactAPT;
import astral_mekanism.block.blockentity.compact.BECompactFusionReactor;
import astral_mekanism.block.blockentity.compact.BECompactNaquadahReactor;
import astral_mekanism.block.blockentity.compact.BECompactSPS;
import astral_mekanism.block.blockentity.enchantedmachine.*;
import astral_mekanism.block.blockentity.enchantedmachine.advanced.BEEnchantedChemicalInjectionChamber;
import astral_mekanism.block.blockentity.enchantedmachine.advanced.BEEnchantedOsmiumCompressor;
import astral_mekanism.block.blockentity.enchantedmachine.advanced.BEEnchantedPurificationChamber;
import astral_mekanism.block.blockentity.enchantedmachine.electric.BEEnchantedCrusher;
import astral_mekanism.block.blockentity.enchantedmachine.electric.BEEnchantedEnrichmentChamber;
import astral_mekanism.block.blockentity.normalfactory.BEEnergizedSmeltingFactory;
import astral_mekanism.block.blockentity.normalmachine.*;
import astral_mekanism.block.blockentity.other.BEUpgradeExtractor;
import astral_mekanism.block.blockentity.storage.BEItemSortableStorage;
import astral_mekanism.block.blockentity.storage.BEUniversalStorage;
import astral_mekanism.block.container.other.ContainerItemSortableStorage;
import astral_mekanism.block.container.prefab.ContainerAbstractStorage;
import astral_mekanism.block.gui.appliedmachine.*;
import astral_mekanism.block.gui.astralmachine.GuiAstralPrecisionSawmill;
import astral_mekanism.block.gui.astralmachine.GuiAstralRadiationIrradiator;
import astral_mekanism.block.gui.astralmachine.GuiAstralSolidifier;
import astral_mekanism.block.gui.basemachine.*;
import astral_mekanism.block.gui.compact.*;
import astral_mekanism.block.gui.factory.GuiEnergizedSmeltingFactory;
import astral_mekanism.block.gui.generator.GuiAppliedGasBurningGenerator;
import astral_mekanism.block.gui.generator.GuiGasBurningGenerator;
import astral_mekanism.block.gui.generator.GuiHeatGenerator;
import astral_mekanism.block.gui.normalmachine.*;
import astral_mekanism.block.gui.other.GuiMekanicalMagmaBlock;
import astral_mekanism.block.gui.other.GuiNothing;
import astral_mekanism.block.gui.prefab.GuiAbstractStorage;
import astral_mekanism.block.gui.prefab.GuiDoubleItemToItemRecipeMachine;
import astral_mekanism.block.gui.prefab.GuiGasToGasBlock;
import astral_mekanism.block.gui.prefab.GuiGasToGasMachine;
import astral_mekanism.block.gui.prefab.GuiItemToItemBlock;
import astral_mekanism.block.gui.storage.*;
import astral_mekanism.registration.MachineRegistryObject;
import astral_mekanism.registries.AMEFluids;
import astral_mekanism.registries.AMEMachines;
import mekanism.client.ClientRegistrationUtil;
import mekanism.common.inventory.container.tile.MekanismTileContainer;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens.ScreenConstructor;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@OnlyIn(Dist.CLIENT)
public class AMEClient extends AstralMekanism {

    private static AMEClient INSTANCE;

    public AMEClient() {
        super();
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        INSTANCE = this;
        eventBus.addListener(this::clientSetup);
        LOGGER.info(MODID + " client was initialized");
    }

    @SuppressWarnings("unused")
    private void clientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Minecraft minecraft = Minecraft.getInstance();
            AMEFluids.FLUIDS.getAllFluids()
                    .forEach(fluidRO -> ClientRegistrationUtil.setRenderLayer(RenderType.translucent(), fluidRO));
            initScreens();
        });
    }

    private static void initScreens() {
        registerScreenMek(AMEMachines.APPLIED_CHEMICAL_CRYSTALLIZER, GuiAppliedCrystallizer::new);
        registerScreenMek(AMEMachines.APPLIED_ELECTROLYTIC_SEPARATOR, GuiAppliedElectrolyticSeparator::new);
        registerScreenMek(AMEMachines.APPLIED_FISSION_REACTOR, GuiAppliedFissionReactor::new);
        registerScreenMek(AMEMachines.APPLIED_FUSION_REACTOR, GuiAppliedMixingReactor<BEAppliedFusionReactor>::new);
        registerScreenMek(AMEMachines.APPLIED_ISOTPIC_CENTRIFUGE, GuiAppliedIsotopicCentrifuge::new);
        registerScreenMek(AMEMachines.APPLIED_NAQUADAH_REACTOR, GuiAppliedMixingReactor<BEAppliedNaquadahReactor>::new);
        registerScreenMek(AMEMachines.APPLIED_NEUTRON_ACTIVATOR, GuiAppliedNeutronActivator::new);
        registerScreenMek(AMEMachines.APPLIED_ROTALY_CONDENSENTRATOR, GuiAppliedRotaryCondensentrator::new);
        registerScreenMek(AMEMachines.APPLIED_SMELTER, GuiAppliedSmelter::new);
        registerScreenMek(AMEMachines.APPLIED_SPS, GuiAppliedSPS::new);
        registerScreenMek(AMEMachines.APPLIED_TEP, GuiAppliedTEP::new);
        AMEMachines.ASTRAL_ENERGIZED_SMELTING_FACTRIES.forEach(
                (t, object) -> registerScreenMek(object,
                        GuiEnergizedSmeltingFactory<BEAstralEnergizedSmeltingFactory>::new));
        AMEMachines.ENERGIZED_SMELTING_FACTORIES.forEach(
                (t, object) -> registerScreenMek(object, GuiEnergizedSmeltingFactory<BEEnergizedSmeltingFactory>::new));
        registerScreenMek(AMEMachines.ASTRAL_CHEMICAL_INJECTION_CHAMBER,
                GuiAMEAdvancedMachine<BEAstralChemicalInjectionChamber>::new);
        registerScreenMek(AMEMachines.ASTRAL_OSMIUM_COMPRESSOR,
                GuiAMEAdvancedMachine<BEAstralOsmiumCompressor>::new);
        registerScreenMek(AMEMachines.ASTRAL_PURIFICATION_CHAMBER,
                GuiAMEAdvancedMachine<BEAstralPurificationChamber>::new);
        registerScreenMek(AMEMachines.ASTRAL_CRUSHER, GuiAMEElectricMachine<BEAstralCrusher>::new);
        registerScreenMek(AMEMachines.ASTRAL_ENRICHMENT_CHAMBER,
                GuiAMEElectricMachine<BEAstralEnrichmentChamber>::new);
        registerScreenMek(AMEMachines.ASTRAL_ALLOYER, GuiAMEAlloyer<BEAstralAlloyer>::new);
        registerScreenMek(AMEMachines.ASTRAL_ANTIPROTONIC_NUCLEOSYNTHESIZER,
                GuiAMEAntiprotonicNucleoSynthesizer<BEAstralAntiprotonicNucleosynthesizer>::new);
        registerScreenMek(AMEMachines.ASTRAL_APT, GuiCompactAPT<BEAstralAPT>::new);
        registerScreenMek(AMEMachines.ASTRAL_CHEMICAL_INFUSER, GuiAMEChemicalInfuser<BEAstralChemicalInfuser>::new);
        registerScreenMek(AMEMachines.ASTRAL_CHEMICAL_OXIDIZER, GuiAMEChemicalOxider<BEAstralChemicalOxidizer>::new);
        registerScreenMek(AMEMachines.ASTRAL_CHEMICAL_WASHER, GuiAMEChemicalWasher<BEAstralChemicalWasher>::new);
        registerScreenMek(AMEMachines.ASTRAL_CHEMIXER, GuiAMEChemixer<BEAstralChemixer>::new);
        registerScreenMek(AMEMachines.ASTRAL_COMBINER,
                GuiDoubleItemToItemRecipeMachine<BEAstralCombiner>::new);
        registerScreenMek(AMEMachines.ASTRAL_COMPOSTER, GuiMekanicalComposter<BEAstralComposter>::new);
        registerScreenMek(AMEMachines.ASTRAL_CRYSTALLIZER, GuiAMECrystallizer<BEAstralCrystallizer>::new);
        registerScreenMek(AMEMachines.ASTRAL_DISSOLUTION_CHAMBER,
                GuiAMEDissolutionChamber<BEAstralDissolutionChamber>::new);
        registerScreenMek(AMEMachines.ASTRAL_ELECTROLYTIC_SEPARATOR,
                GuiAMEElectrolyticSeparator<BEAstralElectrolyticSeparator>::new);
        registerScreenMek(AMEMachines.ASTRAL_ENERGIZED_SMELTER,
                GuiEssentialEnergizedSmelter<BEAstralEnergizedSmelter>::new);
        registerScreenMek(AMEMachines.ASTRAL_FLUID_INFUSER, GuiFluidInfuser<BEAstralFluidInfuser>::new);
        registerScreenMek(AMEMachines.ASTRAL_FORMULAIC_ASSEMBLICATOR,
                GuiAMEFormulaicAssemblicator<BEAstralFormulaicAssemblicator>::new);
        registerScreenMek(AMEMachines.ASTRAL_GNA, GuiGasToGasBlock<BEAstralGNA>::new);
        registerScreenMek(AMEMachines.ASTRAL_GREEN_HOUSE, GuiGreenHouse<BEAstralGreenHouse>::new);
        registerScreenMek(AMEMachines.ASTRAL_ISOTOPIC_CENTRIFUGE,
                GuiGasToGasMachine<BEAstralIsotopicCentrifuge>::new);
        registerScreenMek(AMEMachines.ASTRAL_MEKANICAL_CHARGER,
                GuiMekanicalCharger<BEAstralMekanicalCharger>::new);
        registerScreenMek(AMEMachines.ASTRAL_MEKANICAL_INSCRIBER,
                GuiMekanicalInscriber<BEAstralMekanicalInscriber>::new);
        registerScreenMek(AMEMachines.ASTRAL_THERMALIZER, GuiAMEMelter<BEAstralMelter>::new);
        registerScreenMek(AMEMachines.ASTRAL_METALLURGIC_INFUSER,
                GuiAMEMetallurgicInfuser<BEAstralMetallurgicInfuser>::new);
        registerScreenMek(AMEMachines.ASTRAL_PRC, GuiAMEPRC<BEAstralPRC>::new);
        registerScreenMek(AMEMachines.ASTRAL_PRECISION_SAWMILL, GuiAstralPrecisionSawmill::new);
        registerScreenMek(AMEMachines.ASTRAL_RADIATION_IRRADIATOR, GuiAstralRadiationIrradiator::new);
        registerScreenMek(AMEMachines.ASTRAL_REACTION_CHAMBER,
                GuiAAEReactionChamber<BEAstralReactionChamber>::new);
        registerScreenMek(AMEMachines.ASTRAL_ROTARY_CONDENSENTRATOR,
                GuiAMERotaryCondensentrator<BEAstralRotaryCondensentrator>::new);
        registerScreenMek(AMEMachines.ASTRAL_SPS, GuiGasToGasMachine<BEAstralSPS>::new);
        registerScreenMek(AMEMachines.ASTRAL_SOLIDIFICATION_CHAMBER, GuiAstralSolidifier::new);
        registerScreenMek(AMEMachines.ASTRAL_TRANSFORMER, GuiTransformer<BEAstralTransformer>::new);
        registerScreenMek(AMEMachines.COMPACT_APT, GuiCompactAPT<BECompactAPT>::new);
        AMEMachines.COMPACT_FIR
                .forEach((t, m) -> registerScreenMek(m, GuiCompactFissionReactor::new));
        AMEMachines.COMPACT_FUSION_REACTOR
                .forEach((t, m) -> registerScreenMek(m, GuiCompactMixingReactor<BECompactFusionReactor>::new));
        AMEMachines.COMPACT_NAQUADAH_REACTOR
                .forEach((t, m) -> registerScreenMek(m, GuiCompactMixingReactor<BECompactNaquadahReactor>::new));
        registerScreenMek(AMEMachines.COMPACT_SPS, GuiGasToGasMachine<BECompactSPS>::new);
        AMEMachines.COMPACT_TEP
                .forEach((t, m) -> registerScreenMek(m, GuiCompactTEP::new));
        registerScreenMek(AMEMachines.ENCHANTED_CHEMICAL_INJECTION_CHAMBER,
                GuiAMEAdvancedMachine<BEEnchantedChemicalInjectionChamber>::new);
        registerScreenMek(AMEMachines.ENCHANTED_OSMIUM_COMPRESSOR,
                GuiAMEAdvancedMachine<BEEnchantedOsmiumCompressor>::new);
        registerScreenMek(AMEMachines.ENCHANTED_PURIFICATION_CHAMBER,
                GuiAMEAdvancedMachine<BEEnchantedPurificationChamber>::new);
        registerScreenMek(AMEMachines.ENCHANTED_CRUSHER, GuiAMEElectricMachine<BEEnchantedCrusher>::new);
        registerScreenMek(AMEMachines.ENCHANTED_ENRICHMENT_CHAMBER,
                GuiAMEElectricMachine<BEEnchantedEnrichmentChamber>::new);
        registerScreenMek(AMEMachines.ENCHANTED_ADSORPTION_SEPARATOR,
                GuiAMEAdsorptionSeparator<BEEnchantedAdsorptionSeparator>::new);
        registerScreenMek(AMEMachines.ENCHANTED_AIR_COMPRESSOR, GuiAMEAirCompressor<BEEnchantedAirCompressor>::new);
        registerScreenMek(AMEMachines.ENCHANTED_ALLOYER, GuiAMEAlloyer<BEEnchantedAlloyer>::new);
        registerScreenMek(AMEMachines.ENCHANTED_ANTIPROTONIC_NUCLEOSYNTHESIZER,
                GuiAMEAntiprotonicNucleoSynthesizer<BEEnchantedAntiprotonicNucleosynthesizer>::new);
        registerScreenMek(AMEMachines.ENCHANTED_CHEMICAL_INFUSER,
                GuiAMEChemicalInfuser<BEEnchantedChemicalInfuser>::new);
        registerScreenMek(AMEMachines.ENCHANTED_CHEMICAL_OXIDIZER,
                GuiAMEChemicalOxider<BEEnchantedChemicalOxidizer>::new);
        registerScreenMek(AMEMachines.ENCHANTED_CHEMIXER, GuiAMEChemixer<BEEnchantedChemixer>::new);
        registerScreenMek(AMEMachines.ENCHANTED_CRYSTALLIZER, GuiAMECrystallizer<BEEnchantedCrystallizer>::new);
        registerScreenMek(AMEMachines.ENCHANTED_DISSOLUTION_CHAMBER,
                GuiAMEDissolutionChamber<BEEnchantedDissolutionChamber>::new);
        registerScreenMek(AMEMachines.ENCHANTED_ELECTROLYTIC_SEPARATOR,
                GuiAMEElectrolyticSeparator<BEEnchantedElectrolyticSeparator>::new);
        registerScreenMek(AMEMachines.ENCHANTED_ENERGIZED_SMELTER,
                GuiEssentialEnergizedSmelter<BEEnchantedEnergizedSmelter>::new);
        registerScreenMek(AMEMachines.ENCHANTED_FORMULAIC_ASSEMBLICATOR,
                GuiAMEFormulaicAssemblicator<BEEnchantedFormulaicAssemblicator>::new);
        registerScreenMek(AMEMachines.ENCHANTED_ISTOPIC_CENTRIFUGE,
                GuiGasToGasMachine<BEEnchantedIsotopicCentrifuge>::new);
        registerScreenMek(AMEMachines.ENCHANTED_MELTER, GuiAMEMelter<BEEnchantedMelter>::new);
        registerScreenMek(AMEMachines.ENCHANTED_METALLURGIC_INFUSER,
                GuiAMEMetallurgicInfuser<BEEnchantedMetallurgicInfuser>::new);
        registerScreenMek(AMEMachines.ENCHANTED_PAINTING_MACHINE,
                GuiAMEPaintingMachine<BEEnchantedPaintingMachine>::new);
        registerScreenMek(AMEMachines.ENCHANTED_PRC, GuiAMEPRC<BEEnchantedPRC>::new);
        registerScreenMek(AMEMachines.ENCHANTED_ROTARY_CONDENSENTRATOR,
                GuiAMERotaryCondensentrator<BEEnchantedRotaryCondensentrator>::new);
        registerScreenMek(AMEMachines.APPLIED_GAS_BURNING_GENERATOR, GuiAppliedGasBurningGenerator::new);
        AMEMachines.GAS_BURNING_GENERATORS
                .forEach((t, m) -> registerScreenMek(m, GuiGasBurningGenerator::new));
        AMEMachines.HEAT_GENERATORS.forEach((t, m) -> registerScreenMek(m, GuiHeatGenerator::new));
        registerScreenMek(AMEMachines.ASTRAL_CRAFTER, GuiAstralCrafter::new);
        registerScreenMek(AMEMachines.ESSENTIAL_ENERGIZED_SMELTER,
                GuiEssentialEnergizedSmelter<BEEssentialEnergizedSmelter>::new);
        registerScreenMek(AMEMachines.ESSENTIAL_FORMULAIC_ASSEMBLICATOR,
                GuiAMEFormulaicAssemblicator<BEEssentialFormulaicAssemblicator>::new);
        registerScreenMek(AMEMachines.ESSENTIAL_METALLURGIC_INFUSER,
                GuiEssentialMetallurgicInfuser::new);
        registerScreenMek(AMEMachines.ESSENTIAL_OSMIUM_COMPRESSOR,
                GuiEssentialItemGasToItem<BEEssentialOsmiumCompressor>::new);
        registerScreenMek(AMEMachines.ESSENTIAL_REACTION_CHAMBER,
                GuiAAEReactionChamber<BEEssentialReactionChamber>::new);
        registerScreenMek(AMEMachines.FLUID_INFUSER, GuiFluidInfuser<BEFluidInfuser>::new);
        registerScreenMek(AMEMachines.GAS_CONVERTER, GuiGasToGasBlock<BEGasConverter>::new);
        registerScreenMek(AMEMachines.GAS_SYNTHESIZER, GuiGasSynthesizer::new);
        registerScreenMek(AMEMachines.GLOWSTONE_NEUTRON_ACTIVATOR,
                GuiGasToGasBlock<BEGlowstoneNeutronActivator>::new);
        registerScreenMek(AMEMachines.GREEN_HOUSE, GuiGreenHouse<BEGreenHouse>::new);
        registerScreenMek(AMEMachines.INFUSE_SYNTHESIZER, GuiInfuseSynthesizer::new);
        registerScreenMek(AMEMachines.INFUSING_CONDENSENTRATOR,
                GuiInfusingCondensentrator<BEInfusingCondensentrator>::new);
        registerScreenMek(AMEMachines.INTERSTELLAR_POSITRONIC_MATTER_RECONSTRUCTION_APPARATUS,
                GuiInterstellarAntineutronicMatterReconstructionApparatus::new);
        registerScreenMek(AMEMachines.ITEM_COMPRESSOR,
                GuiItemToItemBlock<BEItemCompressor>::new);
        registerScreenMek(AMEMachines.ITEM_UNZIPPER,
                GuiItemToItemBlock<BEItemUnzipper>::new);
        registerScreenMek(AMEMachines.MEKANICAL_CHARGER,
                GuiMekanicalCharger<BEMekanicalCharger>::new);
        registerScreenMek(AMEMachines.MEKANICAL_COMPOSTER, GuiMekanicalComposter<BEMekanicalComposter>::new);
        registerScreenMek(AMEMachines.MEKANICAL_INSCRIBER, GuiMekanicalInscriber<BEMekanicalInscriber>::new);
        registerScreenMek(AMEMachines.MEKANICAL_MATTER_CONDENSER, GuiMekanicalMatterCondenser::new);
        registerScreenMek(AMEMachines.TRANSFORMER, GuiTransformer<BETransformer>::new);
        Arrays.asList(AMEMachines.MEKANICAL_MAGMABLOCKS)
                .forEach(obj -> registerScreenMek(obj, GuiMekanicalMagmaBlock::new));
        registerScreenMek(AMEMachines.UPGRADE_EXTRACTOR, GuiNothing<BEUpgradeExtractor>::new);
        registerScreenMek(AMEMachines.EVENLY_INSERTER, GuiEvenlyInserter::new);
        registerScreenMek(AMEMachines.UNIVERSAL_STORAGE,
                GuiAbstractStorage<BEUniversalStorage, ContainerAbstractStorage<BEUniversalStorage>>::new);
        registerScreenMek(AMEMachines.ITEM_SORTABLE_STORAGE,
                GuiAbstractStorage<BEItemSortableStorage, ContainerItemSortableStorage<BEItemSortableStorage>>::new);
        registerScreenMek(AMEMachines.RATIO_SEPARATOR, GuiRatioSeparator::new);
        registerScreenMek(AMEMachines.XP_TANK, GuiXpTank::new);
    }

    private static <BE extends TileEntityMekanism, CONTAINER extends MekanismTileContainer<BE>, U extends Screen & MenuAccess<CONTAINER>> void registerScreenMek(
            MachineRegistryObject<BE, ?, ? extends CONTAINER, ?> registryObject,
            ScreenConstructor<CONTAINER, U> constructor) {
        MenuScreens.register(registryObject.getContainer().get(), constructor);
    }
}
