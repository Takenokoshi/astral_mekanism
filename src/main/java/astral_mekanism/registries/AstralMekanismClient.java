package astral_mekanism.registries;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.block.blockentity.astralmachine.BEAstralCombiner;
import astral_mekanism.block.blockentity.astralmachine.BEAstralGNA;
import astral_mekanism.block.blockentity.astralmachine.BEAstralGreenhouse;
import astral_mekanism.block.blockentity.astralmachine.BEAstralIsotopicCentrifuge;
import astral_mekanism.block.blockentity.astralmachine.BEAstralMekanicalInscriber;
import astral_mekanism.block.blockentity.astralmachine.BEAstralMekanicalTransformer;
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
import astral_mekanism.block.blockentity.normalmachine.BEEssentialSmelter;
import astral_mekanism.block.blockentity.normalmachine.BEGlowstoneNeutronActivator;
import astral_mekanism.block.blockentity.normalmachine.BEGreenhouse;
import astral_mekanism.block.blockentity.normalmachine.BEMekanicalCharger;
import astral_mekanism.block.blockentity.normalmachine.BEMekanicalTransformer;
import astral_mekanism.block.blockentity.other.BEItemSortableStorage;
import astral_mekanism.block.blockentity.other.BEUniversalStorage;
import astral_mekanism.block.container.other.ContainerItemSortableStorage;
import astral_mekanism.block.container.prefab.ContainerAbstractStorage;
import astral_mekanism.block.gui.astralmachine.GuiAstralAdvancedMachine;
import astral_mekanism.block.gui.astralmachine.GuiAstralChemicalInfuser;
import astral_mekanism.block.gui.astralmachine.GuiAstralChemicalOxider;
import astral_mekanism.block.gui.astralmachine.GuiAstralChemicalWasher;
import astral_mekanism.block.gui.astralmachine.GuiAstralCrystallizer;
import astral_mekanism.block.gui.astralmachine.GuiAstralDissolutionChamber;
import astral_mekanism.block.gui.astralmachine.GuiAstralElectricMachine;
import astral_mekanism.block.gui.astralmachine.GuiAstralElectrolyticSeparator;
import astral_mekanism.block.gui.astralmachine.GuiAstralMetallurgicInfuser;
import astral_mekanism.block.gui.astralmachine.GuiAstralPRC;
import astral_mekanism.block.gui.astralmachine.GuiAstralPrecisionSawmill;
import astral_mekanism.block.gui.astralmachine.GuiAstralRotaryCondensentrator;
import astral_mekanism.block.gui.compact.GuiCompactFissionReactor;
import astral_mekanism.block.gui.compact.GuiCompactTEP;
import astral_mekanism.block.gui.generator.GuiGasBurningGenerator;
import astral_mekanism.block.gui.generator.GuiHeatGenerator;
import astral_mekanism.block.gui.normalmachine.GuiAstralCrafter;
import astral_mekanism.block.gui.normalmachine.GuiEssentialSmelter;
import astral_mekanism.block.gui.normalmachine.GuiFluidInfuser;
import astral_mekanism.block.gui.normalmachine.GuiGreenhouse;
import astral_mekanism.block.gui.normalmachine.GuiInfuseSynthesizer;
import astral_mekanism.block.gui.normalmachine.GuiMekanicalInscriber;
import astral_mekanism.block.gui.normalmachine.GuiMekanicalTransformer;
import astral_mekanism.block.gui.prefab.GuiAbstractStorage;
import astral_mekanism.block.gui.prefab.GuiDoubleItemToItemRecipeMachine;
import astral_mekanism.block.gui.prefab.GuiGasToGasBlock;
import astral_mekanism.block.gui.prefab.GuiGasToGasMachine;
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
            regScreen(AstralMekanismMachines.ASTRAL_CHEMICAL_INJECTION_CHAMBER,
                    GuiAstralAdvancedMachine<BEAstralChemicalInjectionChamber>::new);
            regScreen(AstralMekanismMachines.ASTRAL_OSMIUM_COMPRESSOR,
                    GuiAstralAdvancedMachine<BEAstralOsmiumCompressor>::new);
            regScreen(AstralMekanismMachines.ASTRAL_PURIFICATION_CHAMBER,
                    GuiAstralAdvancedMachine<BEAstralPurificationChamber>::new);
            regScreen(AstralMekanismMachines.ASTRAL_CRUSHER, GuiAstralElectricMachine<BEAstralCrusher>::new);
            regScreen(AstralMekanismMachines.ASTRAL_ENERGIZED_SMELTER,
                    GuiAstralElectricMachine<BEAstralEnergizedSmelter>::new);
            regScreen(AstralMekanismMachines.ASTRAL_ENRICHMENT_CHAMBER,
                    GuiAstralElectricMachine<BEAstralEnrichmentChamber>::new);
            regScreen(AstralMekanismMachines.ASTRAL_MEKANICAL_CHARGER,
                    GuiAstralElectricMachine<BEAstralMekanicalCharger>::new);
            regScreen(AstralMekanismMachines.ASTRAL_CHEMICAL_INFUSER, GuiAstralChemicalInfuser::new);
            regScreen(AstralMekanismMachines.ASTRAL_CHEMICAL_OXIDIZER, GuiAstralChemicalOxider::new);
            regScreen(AstralMekanismMachines.ASTRAL_CHEMICAL_WASHER, GuiAstralChemicalWasher::new);
            regScreen(AstralMekanismMachines.ASTRAL_COMBINER, GuiDoubleItemToItemRecipeMachine<BEAstralCombiner>::new);
            regScreen(AstralMekanismMachines.ASTRAL_CRYSTALLIZER, GuiAstralCrystallizer::new);
            regScreen(AstralMekanismMachines.ASTRAL_DISSOLUTION_CHAMBER, GuiAstralDissolutionChamber::new);
            regScreen(AstralMekanismMachines.ASTRAL_ELECTROLYTIC_SEPARATOR, GuiAstralElectrolyticSeparator::new);
            regScreen(AstralMekanismMachines.ASTRAL_GNA, GuiGasToGasBlock<BEAstralGNA>::new);
            regScreen(AstralMekanismMachines.ASTRAL_GREENHOUSE, GuiGreenhouse<BEAstralGreenhouse>::new);
            regScreen(AstralMekanismMachines.ASTRAL_ISOTOPIC_CENTRIFUGE,
                    GuiGasToGasMachine<BEAstralIsotopicCentrifuge>::new);
            regScreen(AstralMekanismMachines.ASTRAL_MEKANICAL_INSCRIBER,
                    GuiDoubleItemToItemRecipeMachine<BEAstralMekanicalInscriber>::new);
            regScreen(AstralMekanismMachines.ASTRAL_MEKANICAL_TRANSFOMER,
                    GuiMekanicalTransformer<BEAstralMekanicalTransformer>::new);
            regScreen(AstralMekanismMachines.ASTRAL_METALLURGIC_INFUSER, GuiAstralMetallurgicInfuser::new);
            regScreen(AstralMekanismMachines.ASTRAL_PRC, GuiAstralPRC::new);
            regScreen(AstralMekanismMachines.ASTRAL_PRECISION_SAWMILL, GuiAstralPrecisionSawmill::new);
            regScreen(AstralMekanismMachines.ASTRAL_ROTARY_CONDENSENTRATOR, GuiAstralRotaryCondensentrator::new);
            regScreen(AstralMekanismMachines.ASTRAL_SPS, GuiGasToGasMachine<BEAstralSPS>::new);
            regScreen(AstralMekanismMachines.COMPACT_FIR, GuiCompactFissionReactor::new);
            regScreen(AstralMekanismMachines.COMPACT_SPS, GuiGasToGasMachine<BECompactSPS>::new);
            regScreen(AstralMekanismMachines.COMPACT_TEP, GuiCompactTEP::new);
            for (AstralMekGeneratorTier tier : AstralMekGeneratorTier.values()) {
                regScreen(AstralMekanismMachines.GAS_BURNING_GENERATORS.get(tier),
                        GuiGasBurningGenerator::new);
                regScreen(AstralMekanismMachines.HEAT_GENERATORS.get(tier),
                        GuiHeatGenerator::new);
            }
            regScreen(AstralMekanismMachines.ASTRAL_CRAFTER, GuiAstralCrafter::new);
            regScreen(AstralMekanismMachines.ESSENTIAL_SMELTER, GuiEssentialSmelter<BEEssentialSmelter>::new);
            regScreen(AstralMekanismMachines.FLUID_INFUSER, GuiFluidInfuser::new);
            regScreen(AstralMekanismMachines.GLOWSTONE_NEUTRON_ACTIVATOR,
                    GuiGasToGasBlock<BEGlowstoneNeutronActivator>::new);
            regScreen(AstralMekanismMachines.GREENHOUSE, GuiGreenhouse<BEGreenhouse>::new);
            regScreen(AstralMekanismMachines.INFUSE_SYNTHESIZER, GuiInfuseSynthesizer::new);
            regScreen(AstralMekanismMachines.MEKANICAL_CHARGER,
                    GuiElectricMachine<BEMekanicalCharger, MekanismTileContainer<BEMekanicalCharger>>::new);
            regScreen(AstralMekanismMachines.MEKANICAL_INSCRIBER, GuiMekanicalInscriber::new);
            regScreen(AstralMekanismMachines.MEKANICAL_TRANSFORMER,
                    GuiMekanicalTransformer<BEMekanicalTransformer>::new);
            regScreen(AstralMekanismMachines.UNIVERSAL_STORAGE,
                    GuiAbstractStorage<BEUniversalStorage, ContainerAbstractStorage<BEUniversalStorage>>::new);
            regScreen(AstralMekanismMachines.ITEM_SORTABLE_STORAGE,
                    GuiAbstractStorage<BEItemSortableStorage, ContainerItemSortableStorage<BEItemSortableStorage>>::new);
        });
    }

    private static <BE extends TileEntityMekanism, CONTAINER extends MekanismTileContainer<BE>, U extends Screen & MenuAccess<CONTAINER>> void regScreen(
            MachineRegistryObject<BE, ?, CONTAINER, ?> registryObject,
            ScreenConstructor<CONTAINER, U> constructor) {
        ClientRegistrationUtil.registerScreen(registryObject.getContainer(), constructor);
    }

}
