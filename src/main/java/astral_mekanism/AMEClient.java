package astral_mekanism;

import astral_mekanism.block.blockentity.appliedmachine.BEAppliedFusionReactor;
import astral_mekanism.block.blockentity.appliedmachine.BEAppliedNaquadahReactor;
import astral_mekanism.block.blockentity.astralfactory.BEAstralEnergizedSmeltingFactory;
import astral_mekanism.block.blockentity.astralmachine.BEAstralAPT;
import astral_mekanism.block.blockentity.astralmachine.BEAstralAntiprotonicNucleosynthesizer;
import astral_mekanism.block.blockentity.astralmachine.BEAstralChemicalInfuser;
import astral_mekanism.block.blockentity.astralmachine.BEAstralChemicalOxidizer;
import astral_mekanism.block.blockentity.astralmachine.BEAstralChemixer;
import astral_mekanism.block.blockentity.astralmachine.BEAstralCombiner;
import astral_mekanism.block.blockentity.astralmachine.BEAstralComposter;
import astral_mekanism.block.blockentity.astralmachine.BEAstralElectrolyticSeparator;
import astral_mekanism.block.blockentity.astralmachine.BEAstralEnergizedSmelter;
import astral_mekanism.block.blockentity.astralmachine.BEAstralFluidInfuser;
import astral_mekanism.block.blockentity.astralmachine.BEAstralGNA;
import astral_mekanism.block.blockentity.astralmachine.BEAstralGreenHouse;
import astral_mekanism.block.blockentity.astralmachine.BEAstralIsotopicCentrifuge;
import astral_mekanism.block.blockentity.astralmachine.BEAstralMekanicalCharger;
import astral_mekanism.block.blockentity.astralmachine.BEAstralMekanicalInscriber;
import astral_mekanism.block.blockentity.astralmachine.BEAstralReactionChamber;
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
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedAntiprotonicNucleosynthesizer;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedChemicalInfuser;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedChemicalOxidizer;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedChemixer;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedElectrolyticSeparator;
import astral_mekanism.block.blockentity.enchantedmachine.BEEnchantedIsotopicCentrifuge;
import astral_mekanism.block.blockentity.normalfactory.BEEnergizedSmeltingFactory;
import astral_mekanism.block.blockentity.normalmachine.BEEssentialEnergizedSmelter;
import astral_mekanism.block.blockentity.normalmachine.BEEssentialOsmiumCompressor;
import astral_mekanism.block.blockentity.normalmachine.BEEssentialReactionChamber;
import astral_mekanism.block.blockentity.normalmachine.BEFluidInfuser;
import astral_mekanism.block.blockentity.normalmachine.BEGasConverter;
import astral_mekanism.block.blockentity.normalmachine.BEGlowstoneNeutronActivator;
import astral_mekanism.block.blockentity.normalmachine.BEGreenHouse;
import astral_mekanism.block.blockentity.normalmachine.BEInfusingCondensentrator;
import astral_mekanism.block.blockentity.normalmachine.BEItemCompressor;
import astral_mekanism.block.blockentity.normalmachine.BEItemUnzipper;
import astral_mekanism.block.blockentity.normalmachine.BEMekanicalCharger;
import astral_mekanism.block.blockentity.normalmachine.BEMekanicalComposter;
import astral_mekanism.block.blockentity.normalmachine.BEMekanicalInscriber;
import astral_mekanism.block.blockentity.normalmachine.BETransformer;
import astral_mekanism.block.blockentity.storage.BEItemSortableStorage;
import astral_mekanism.block.blockentity.storage.BEUniversalStorage;
import astral_mekanism.block.container.other.ContainerItemSortableStorage;
import astral_mekanism.block.container.prefab.ContainerAbstractStorage;
import astral_mekanism.block.gui.appliedmachine.GuiAppliedRotaryCondensentrator;
import astral_mekanism.block.gui.appliedmachine.GuiAppliedSPS;
import astral_mekanism.block.gui.appliedmachine.GuiAppliedCrystallizer;
import astral_mekanism.block.gui.appliedmachine.GuiAppliedElectrolyticSeparator;
import astral_mekanism.block.gui.appliedmachine.GuiAppliedFissionReactor;
import astral_mekanism.block.gui.appliedmachine.GuiAppliedIsotopicCentrifuge;
import astral_mekanism.block.gui.appliedmachine.GuiAppliedMixingReactor;
import astral_mekanism.block.gui.appliedmachine.GuiAppliedNeutronActivator;
import astral_mekanism.block.gui.appliedmachine.GuiAppliedSmelter;
import astral_mekanism.block.gui.appliedmachine.GuiAppliedTEP;
import astral_mekanism.block.gui.astralmachine.GuiAstralAdvancedMachine;
import astral_mekanism.block.gui.astralmachine.GuiAstralAlloyer;
import astral_mekanism.block.gui.astralmachine.GuiAMEAntiprotonicNucleoSynthesizer;
import astral_mekanism.block.gui.astralmachine.GuiAstralChemicalWasher;
import astral_mekanism.block.gui.astralmachine.GuiAstralCrystallizer;
import astral_mekanism.block.gui.astralmachine.GuiAstralDissolutionChamber;
import astral_mekanism.block.gui.astralmachine.GuiAstralElectricMachine;
import astral_mekanism.block.gui.astralmachine.GuiAstralFormulaicAssemblicator;
import astral_mekanism.block.gui.astralmachine.GuiAstralMelter;
import astral_mekanism.block.gui.astralmachine.GuiAstralMetallurgicInfuser;
import astral_mekanism.block.gui.astralmachine.GuiAstralPRC;
import astral_mekanism.block.gui.astralmachine.GuiAstralPrecisionSawmill;
import astral_mekanism.block.gui.astralmachine.GuiAstralRadiationIrradiator;
import astral_mekanism.block.gui.astralmachine.GuiAstralRotaryCondensentrator;
import astral_mekanism.block.gui.astralmachine.GuiAstralSolidifier;
import astral_mekanism.block.gui.compact.GuiCompactAPT;
import astral_mekanism.block.gui.compact.GuiCompactFissionReactor;
import astral_mekanism.block.gui.compact.GuiCompactMixingReactor;
import astral_mekanism.block.gui.compact.GuiCompactTEP;
import astral_mekanism.block.gui.enchantedmachine.GuiAMEElectrolyticSeparator;
import astral_mekanism.block.gui.enchantedmachine.GuiAMEChemixer;
import astral_mekanism.block.gui.enchantedmachine.GuiAMEChemicalOxider;
import astral_mekanism.block.gui.enchantedmachine.GuiAMEChemicalInfuser;
import astral_mekanism.block.gui.factory.GuiEnergizedSmeltingFactory;
import astral_mekanism.block.gui.generator.GuiAppliedGasBurningGenerator;
import astral_mekanism.block.gui.generator.GuiGasBurningGenerator;
import astral_mekanism.block.gui.generator.GuiHeatGenerator;
import astral_mekanism.block.gui.normalmachine.GuiAAEReactionChamber;
import astral_mekanism.block.gui.normalmachine.GuiAstralCrafter;
import astral_mekanism.block.gui.normalmachine.GuiEssentialEnergizedSmelter;
import astral_mekanism.block.gui.normalmachine.GuiEssentialItemGasToItem;
import astral_mekanism.block.gui.normalmachine.GuiEssentialMetallurgicInfuser;
import astral_mekanism.block.gui.normalmachine.GuiFluidInfuser;
import astral_mekanism.block.gui.normalmachine.GuiGasSynthesizer;
import astral_mekanism.block.gui.normalmachine.GuiGreenHouse;
import astral_mekanism.block.gui.normalmachine.GuiInfuseSynthesizer;
import astral_mekanism.block.gui.normalmachine.GuiInfusingCondensentrator;
import astral_mekanism.block.gui.normalmachine.GuiInterstellarAntineutronicMatterReconstructionApparatus;
import astral_mekanism.block.gui.normalmachine.GuiMekanicalCharger;
import astral_mekanism.block.gui.normalmachine.GuiMekanicalComposter;
import astral_mekanism.block.gui.normalmachine.GuiMekanicalInscriber;
import astral_mekanism.block.gui.normalmachine.GuiMekanicalMatterCondenser;
import astral_mekanism.block.gui.normalmachine.GuiTransformer;
import astral_mekanism.block.gui.prefab.GuiAbstractStorage;
import astral_mekanism.block.gui.prefab.GuiDoubleItemToItemRecipeMachine;
import astral_mekanism.block.gui.prefab.GuiGasToGasBlock;
import astral_mekanism.block.gui.prefab.GuiGasToGasMachine;
import astral_mekanism.block.gui.prefab.GuiItemToItemBlock;
import astral_mekanism.block.gui.storage.GuiEvenlyInserter;
import astral_mekanism.block.gui.storage.GuiRatioSeparator;
import astral_mekanism.block.gui.storage.GuiXpTank;
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
                GuiAstralAdvancedMachine<BEAstralChemicalInjectionChamber>::new);
        registerScreenMek(AMEMachines.ASTRAL_OSMIUM_COMPRESSOR,
                GuiAstralAdvancedMachine<BEAstralOsmiumCompressor>::new);
        registerScreenMek(AMEMachines.ASTRAL_PURIFICATION_CHAMBER,
                GuiAstralAdvancedMachine<BEAstralPurificationChamber>::new);
        registerScreenMek(AMEMachines.ASTRAL_CRUSHER, GuiAstralElectricMachine<BEAstralCrusher>::new);
        registerScreenMek(AMEMachines.ASTRAL_ENRICHMENT_CHAMBER,
                GuiAstralElectricMachine<BEAstralEnrichmentChamber>::new);
        registerScreenMek(AMEMachines.ASTRAL_ALLOYER, GuiAstralAlloyer::new);
        registerScreenMek(AMEMachines.ASTRAL_ANTIPROTONIC_NUCLEOSYNTHESIZER,
                GuiAMEAntiprotonicNucleoSynthesizer<BEAstralAntiprotonicNucleosynthesizer>::new);
        registerScreenMek(AMEMachines.ASTRAL_APT, GuiCompactAPT<BEAstralAPT>::new);
        registerScreenMek(AMEMachines.ASTRAL_CHEMICAL_INFUSER, GuiAMEChemicalInfuser<BEAstralChemicalInfuser>::new);
        registerScreenMek(AMEMachines.ASTRAL_CHEMICAL_OXIDIZER, GuiAMEChemicalOxider<BEAstralChemicalOxidizer>::new);
        registerScreenMek(AMEMachines.ASTRAL_CHEMICAL_WASHER, GuiAstralChemicalWasher::new);
        registerScreenMek(AMEMachines.ASTRAL_CHEMIXER, GuiAMEChemixer<BEAstralChemixer>::new);
        registerScreenMek(AMEMachines.ASTRAL_COMBINER,
                GuiDoubleItemToItemRecipeMachine<BEAstralCombiner>::new);
        registerScreenMek(AMEMachines.ASTRAL_COMPOSTER, GuiMekanicalComposter<BEAstralComposter>::new);
        registerScreenMek(AMEMachines.ASTRAL_CRYSTALLIZER, GuiAstralCrystallizer::new);
        registerScreenMek(AMEMachines.ASTRAL_DISSOLUTION_CHAMBER, GuiAstralDissolutionChamber::new);
        registerScreenMek(AMEMachines.ASTRAL_ELECTROLYTIC_SEPARATOR,
                GuiAMEElectrolyticSeparator<BEAstralElectrolyticSeparator>::new);
        registerScreenMek(AMEMachines.ASTRAL_ENERGIZED_SMELTER,
                GuiEssentialEnergizedSmelter<BEAstralEnergizedSmelter>::new);
        registerScreenMek(AMEMachines.ASTRAL_FLUID_INFUSER, GuiFluidInfuser<BEAstralFluidInfuser>::new);
        registerScreenMek(AMEMachines.ASTRAL_FORMULAIC_ASSEMBLICATOR, GuiAstralFormulaicAssemblicator::new);
        registerScreenMek(AMEMachines.ASTRAL_GNA, GuiGasToGasBlock<BEAstralGNA>::new);
        registerScreenMek(AMEMachines.ASTRAL_GREEN_HOUSE, GuiGreenHouse<BEAstralGreenHouse>::new);
        registerScreenMek(AMEMachines.ASTRAL_ISOTOPIC_CENTRIFUGE,
                GuiGasToGasMachine<BEAstralIsotopicCentrifuge>::new);
        registerScreenMek(AMEMachines.ASTRAL_MEKANICAL_CHARGER,
                GuiMekanicalCharger<BEAstralMekanicalCharger>::new);
        registerScreenMek(AMEMachines.ASTRAL_MEKANICAL_INSCRIBER,
                GuiMekanicalInscriber<BEAstralMekanicalInscriber>::new);
        registerScreenMek(AMEMachines.ASTRAL_THERMALIZER, GuiAstralMelter::new);
        registerScreenMek(AMEMachines.ASTRAL_METALLURGIC_INFUSER, GuiAstralMetallurgicInfuser::new);
        registerScreenMek(AMEMachines.ASTRAL_PRC, GuiAstralPRC::new);
        registerScreenMek(AMEMachines.ASTRAL_PRECISION_SAWMILL, GuiAstralPrecisionSawmill::new);
        registerScreenMek(AMEMachines.ASTRAL_RADIATION_IRRADIATOR, GuiAstralRadiationIrradiator::new);
        registerScreenMek(AMEMachines.ASTRAL_REACTION_CHAMBER,
                GuiAAEReactionChamber<BEAstralReactionChamber>::new);
        registerScreenMek(AMEMachines.ASTRAL_ROTARY_CONDENSENTRATOR, GuiAstralRotaryCondensentrator::new);
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
        registerScreenMek(AMEMachines.ENCHANTED_ANTIPROTONIC_NUCLEOSYNTHESIZER,
                GuiAMEAntiprotonicNucleoSynthesizer<BEEnchantedAntiprotonicNucleosynthesizer>::new);
        registerScreenMek(AMEMachines.ENCHANTED_CHEMICAL_INFUSER,
                GuiAMEChemicalInfuser<BEEnchantedChemicalInfuser>::new);
        registerScreenMek(AMEMachines.ENCHANTED_CHEMICAL_OXIDIZER,
                GuiAMEChemicalOxider<BEEnchantedChemicalOxidizer>::new);
        registerScreenMek(AMEMachines.ENCHANTED_CHEMIXER, GuiAMEChemixer<BEEnchantedChemixer>::new);
        registerScreenMek(AMEMachines.ENCHANTED_ELECTROLYTIC_SEPARATOR,
                GuiAMEElectrolyticSeparator<BEEnchantedElectrolyticSeparator>::new);
        registerScreenMek(AMEMachines.ENCHANTED_ISTOPIC_CENTRIFUGE,
                GuiGasToGasMachine<BEEnchantedIsotopicCentrifuge>::new);
        registerScreenMek(AMEMachines.APPLIED_GAS_BURNING_GENERATOR, GuiAppliedGasBurningGenerator::new);
        AMEMachines.GAS_BURNING_GENERATORS
                .forEach((t, m) -> registerScreenMek(m, GuiGasBurningGenerator::new));
        AMEMachines.HEAT_GENERATORS.forEach((t, m) -> registerScreenMek(m, GuiHeatGenerator::new));
        registerScreenMek(AMEMachines.ASTRAL_CRAFTER, GuiAstralCrafter::new);
        registerScreenMek(AMEMachines.ESSENTIAL_ENERGIZED_SMELTER,
                GuiEssentialEnergizedSmelter<BEEssentialEnergizedSmelter>::new);
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
        registerScreenMek(AMEMachines.EVENLY_INSERTER, GuiEvenlyInserter::new);
        registerScreenMek(AMEMachines.UNIVERSAL_STORAGE,
                GuiAbstractStorage<BEUniversalStorage, ContainerAbstractStorage<BEUniversalStorage>>::new);
        registerScreenMek(AMEMachines.ITEM_SORTABLE_STORAGE,
                GuiAbstractStorage<BEItemSortableStorage, ContainerItemSortableStorage<BEItemSortableStorage>>::new);
        registerScreenMek(AMEMachines.RATIO_SEPARATOR, GuiRatioSeparator::new);
        registerScreenMek(AMEMachines.XP_TANK, GuiXpTank::new);
    }

    private static <BE extends TileEntityMekanism, CONTAINER extends MekanismTileContainer<BE>, U extends Screen & MenuAccess<CONTAINER>> void registerScreenMek(
            MachineRegistryObject<BE, ?, CONTAINER, ?> registryObject,
            ScreenConstructor<CONTAINER, U> constructor) {
        MenuScreens.register(registryObject.getContainer().get(), constructor);
    }
}
