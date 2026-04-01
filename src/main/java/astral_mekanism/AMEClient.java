package astral_mekanism;

import astral_mekanism.block.blockentity.astralfactory.BEAstralEnergizedSmeltingFactory;
import astral_mekanism.block.blockentity.astralmachine.BEAstralAPT;
import astral_mekanism.block.blockentity.astralmachine.BEAstralCombiner;
import astral_mekanism.block.blockentity.astralmachine.BEAstralComposter;
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
import astral_mekanism.block.blockentity.normalfactory.BEEnergizedSmeltingFactory;
import astral_mekanism.block.blockentity.normalmachine.BEEssentialEnergizedSmelter;
import astral_mekanism.block.blockentity.normalmachine.BEEssentialOsmiumCompressor;
import astral_mekanism.block.blockentity.normalmachine.BEEssentialReactionChamber;
import astral_mekanism.block.blockentity.normalmachine.BEFluidInfuser;
import astral_mekanism.block.blockentity.normalmachine.BEGlowstoneNeutronActivator;
import astral_mekanism.block.blockentity.normalmachine.BEGreenHouse;
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
import astral_mekanism.block.gui.astralmachine.GuiAstralAdvancedMachine;
import astral_mekanism.block.gui.astralmachine.GuiAstralAlloyer;
import astral_mekanism.block.gui.astralmachine.GuiAstralAntiprotonicNucleoSynthesizer;
import astral_mekanism.block.gui.astralmachine.GuiAstralChemicalInfuser;
import astral_mekanism.block.gui.astralmachine.GuiAstralChemicalOxider;
import astral_mekanism.block.gui.astralmachine.GuiAstralChemicalWasher;
import astral_mekanism.block.gui.astralmachine.GuiAstralChemixer;
import astral_mekanism.block.gui.astralmachine.GuiAstralCrystallizer;
import astral_mekanism.block.gui.astralmachine.GuiAstralDissolutionChamber;
import astral_mekanism.block.gui.astralmachine.GuiAstralElectricMachine;
import astral_mekanism.block.gui.astralmachine.GuiAstralElectrolyticSeparator;
import astral_mekanism.block.gui.astralmachine.GuiAstralFormulaicAssemblicator;
import astral_mekanism.block.gui.astralmachine.GuiAstralMelter;
import astral_mekanism.block.gui.astralmachine.GuiAstralMetallurgicInfuser;
import astral_mekanism.block.gui.astralmachine.GuiAstralPRC;
import astral_mekanism.block.gui.astralmachine.GuiAstralPrecisionSawmill;
import astral_mekanism.block.gui.astralmachine.GuiAstralRotaryCondensentrator;
import astral_mekanism.block.gui.astralmachine.GuiAstralSolidifier;
import astral_mekanism.block.gui.compact.GuiCompactAPT;
import astral_mekanism.block.gui.compact.GuiCompactFissionReactor;
import astral_mekanism.block.gui.compact.GuiCompactMixingReactor;
import astral_mekanism.block.gui.compact.GuiCompactTEP;
import astral_mekanism.block.gui.factory.GuiEnergizedSmeltingFactory;
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
import astral_mekanism.block.gui.normalmachine.GuiMekanicalCharger;
import astral_mekanism.block.gui.normalmachine.GuiMekanicalComposter;
import astral_mekanism.block.gui.normalmachine.GuiMekanicalInscriber;
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
import astral_mekanism.registries.AstralMekanismFluids;
import astral_mekanism.registries.AstralMekanismMachines;
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
            AstralMekanismFluids.FLUIDS.getAllFluids()
                    .forEach(fluidRO -> ClientRegistrationUtil.setRenderLayer(RenderType.translucent(), fluidRO));
            initScreens();
        });
    }

    private static void initScreens() {
        AstralMekanismMachines.ASTRAL_ENERGIZED_SMELTING_FACTRIES.forEach(
                (t, object) -> registerScreenMek(object,
                        GuiEnergizedSmeltingFactory<BEAstralEnergizedSmeltingFactory>::new));
        AstralMekanismMachines.ENERGIZED_SMELTING_FACTORIES.forEach(
                (t, object) -> registerScreenMek(object, GuiEnergizedSmeltingFactory<BEEnergizedSmeltingFactory>::new));
        registerScreenMek(AstralMekanismMachines.ASTRAL_CHEMICAL_INJECTION_CHAMBER,
                GuiAstralAdvancedMachine<BEAstralChemicalInjectionChamber>::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_OSMIUM_COMPRESSOR,
                GuiAstralAdvancedMachine<BEAstralOsmiumCompressor>::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_PURIFICATION_CHAMBER,
                GuiAstralAdvancedMachine<BEAstralPurificationChamber>::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_CRUSHER, GuiAstralElectricMachine<BEAstralCrusher>::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_ENRICHMENT_CHAMBER,
                GuiAstralElectricMachine<BEAstralEnrichmentChamber>::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_ALLOYER, GuiAstralAlloyer::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_ANTIPROTONIC_NUCLEOSYNTHESIZER,
                GuiAstralAntiprotonicNucleoSynthesizer::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_APT, GuiCompactAPT<BEAstralAPT>::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_CHEMICAL_INFUSER, GuiAstralChemicalInfuser::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_CHEMICAL_OXIDIZER, GuiAstralChemicalOxider::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_CHEMICAL_WASHER, GuiAstralChemicalWasher::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_CHEMIXER, GuiAstralChemixer::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_COMBINER,
                GuiDoubleItemToItemRecipeMachine<BEAstralCombiner>::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_COMPOSTER, GuiMekanicalComposter<BEAstralComposter>::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_CRYSTALLIZER, GuiAstralCrystallizer::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_DISSOLUTION_CHAMBER, GuiAstralDissolutionChamber::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_ELECTROLYTIC_SEPARATOR, GuiAstralElectrolyticSeparator::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_ENERGIZED_SMELTER,
                GuiEssentialEnergizedSmelter<BEAstralEnergizedSmelter>::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_FLUID_INFUSER, GuiFluidInfuser<BEAstralFluidInfuser>::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_FORMULAIC_ASSEMBLICATOR, GuiAstralFormulaicAssemblicator::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_GNA, GuiGasToGasBlock<BEAstralGNA>::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_GREEN_HOUSE, GuiGreenHouse<BEAstralGreenHouse>::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_ISOTOPIC_CENTRIFUGE,
                GuiGasToGasMachine<BEAstralIsotopicCentrifuge>::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_MEKANICAL_CHARGER,
                GuiMekanicalCharger<BEAstralMekanicalCharger>::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_MEKANICAL_INSCRIBER,
                GuiMekanicalInscriber<BEAstralMekanicalInscriber>::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_THERMALIZER, GuiAstralMelter::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_METALLURGIC_INFUSER, GuiAstralMetallurgicInfuser::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_PRC, GuiAstralPRC::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_PRECISION_SAWMILL, GuiAstralPrecisionSawmill::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_REACTION_CHAMBER, GuiAAEReactionChamber<BEAstralReactionChamber>::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_ROTARY_CONDENSENTRATOR, GuiAstralRotaryCondensentrator::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_SPS, GuiGasToGasMachine<BEAstralSPS>::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_SOLIDIFICATION_CHAMBER, GuiAstralSolidifier::new);
        registerScreenMek(AstralMekanismMachines.ASTRAL_TRANSFORMER, GuiTransformer<BEAstralTransformer>::new);
        registerScreenMek(AstralMekanismMachines.COMPACT_APT, GuiCompactAPT<BECompactAPT>::new);
        AstralMekanismMachines.COMPACT_FIR
                .forEach((t, m) -> registerScreenMek(m, GuiCompactFissionReactor::new));
        AstralMekanismMachines.COMPACT_FUSION_REACTOR
                .forEach((t, m) -> registerScreenMek(m, GuiCompactMixingReactor<BECompactFusionReactor>::new));
        AstralMekanismMachines.COMPACT_NAQUADAH_REACTOR
                .forEach((t, m) -> registerScreenMek(m, GuiCompactMixingReactor<BECompactNaquadahReactor>::new));
        registerScreenMek(AstralMekanismMachines.COMPACT_SPS, GuiGasToGasMachine<BECompactSPS>::new);
        AstralMekanismMachines.COMPACT_TEP
                .forEach((t, m) -> registerScreenMek(m, GuiCompactTEP::new));
        AstralMekanismMachines.GAS_BURNING_GENERATORS
                .forEach((t, m) -> registerScreenMek(m, GuiGasBurningGenerator::new));
        AstralMekanismMachines.HEAT_GENERATORS.forEach((t, m) -> registerScreenMek(m, GuiHeatGenerator::new));
        registerScreenMek(AstralMekanismMachines.ASTRAL_CRAFTER, GuiAstralCrafter::new);
        registerScreenMek(AstralMekanismMachines.ESSENTIAL_ENERGIZED_SMELTER,
                GuiEssentialEnergizedSmelter<BEEssentialEnergizedSmelter>::new);
        registerScreenMek(AstralMekanismMachines.ESSENTIAL_METALLURGIC_INFUSER,
                GuiEssentialMetallurgicInfuser::new);
        registerScreenMek(AstralMekanismMachines.ESSENTIAL_OSMIUM_COMPRESSOR,
                GuiEssentialItemGasToItem<BEEssentialOsmiumCompressor>::new);
        registerScreenMek(AstralMekanismMachines.ESSENTIAL_REACTION_CHAMBER,
                GuiAAEReactionChamber<BEEssentialReactionChamber>::new);
        registerScreenMek(AstralMekanismMachines.FLUID_INFUSER, GuiFluidInfuser<BEFluidInfuser>::new);
        registerScreenMek(AstralMekanismMachines.GAS_SYNTHESIZER, GuiGasSynthesizer::new);
        registerScreenMek(AstralMekanismMachines.GLOWSTONE_NEUTRON_ACTIVATOR,
                GuiGasToGasBlock<BEGlowstoneNeutronActivator>::new);
        registerScreenMek(AstralMekanismMachines.GREEN_HOUSE, GuiGreenHouse<BEGreenHouse>::new);
        registerScreenMek(AstralMekanismMachines.INFUSE_SYNTHESIZER, GuiInfuseSynthesizer::new);
        registerScreenMek(AstralMekanismMachines.ITEM_COMPRESSOR,
                GuiItemToItemBlock<BEItemCompressor>::new);
        registerScreenMek(AstralMekanismMachines.ITEM_UNZIPPER,
                GuiItemToItemBlock<BEItemUnzipper>::new);
        registerScreenMek(AstralMekanismMachines.MEKANICAL_CHARGER,
                GuiMekanicalCharger<BEMekanicalCharger>::new);
        registerScreenMek(AstralMekanismMachines.MEKANICAL_COMPOSTER, GuiMekanicalComposter<BEMekanicalComposter>::new);
        registerScreenMek(AstralMekanismMachines.MEKANICAL_INSCRIBER, GuiMekanicalInscriber<BEMekanicalInscriber>::new);
        registerScreenMek(AstralMekanismMachines.TRANSFORMER, GuiTransformer<BETransformer>::new);
        registerScreenMek(AstralMekanismMachines.EVENLY_INSERTER, GuiEvenlyInserter::new);
        registerScreenMek(AstralMekanismMachines.UNIVERSAL_STORAGE,
                GuiAbstractStorage<BEUniversalStorage, ContainerAbstractStorage<BEUniversalStorage>>::new);
        registerScreenMek(AstralMekanismMachines.ITEM_SORTABLE_STORAGE,
                GuiAbstractStorage<BEItemSortableStorage, ContainerItemSortableStorage<BEItemSortableStorage>>::new);
        registerScreenMek(AstralMekanismMachines.RATIO_SEPARATOR, GuiRatioSeparator::new);
        registerScreenMek(AstralMekanismMachines.XP_TANK, GuiXpTank::new);
    }

    private static <BE extends TileEntityMekanism, CONTAINER extends MekanismTileContainer<BE>, U extends Screen & MenuAccess<CONTAINER>> void registerScreenMek(
            MachineRegistryObject<BE, ?, CONTAINER, ?> registryObject,
            ScreenConstructor<CONTAINER, U> constructor) {
        MenuScreens.register(registryObject.getContainer().get(), constructor);
    }
}
