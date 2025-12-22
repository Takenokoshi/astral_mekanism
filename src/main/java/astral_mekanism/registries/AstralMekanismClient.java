package astral_mekanism.registries;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.block.blockentity.generator.AstralMekGeneratorTier;
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
import astral_mekanism.block.gui.normalmachine.GuiFluidInfuser;
import astral_mekanism.block.gui.normalmachine.GuiGreenhouse;
import astral_mekanism.block.gui.normalmachine.GuiInfuseSynthesizer;
import astral_mekanism.block.gui.normalmachine.GuiMekanicalInscriber;
import astral_mekanism.block.gui.normalmachine.GuiMekanicalTransformer;
import astral_mekanism.block.gui.prefab.GuiAbstractStorage;
import astral_mekanism.block.gui.prefab.GuiDoubleItemToItemRecipeMachine;
import astral_mekanism.block.gui.prefab.GuiGasToGasBlock;
import astral_mekanism.block.gui.prefab.GuiGasToGasMachine;
import astral_mekanism.block.gui.prefab.GuiTripleItemToItemMachine;
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
            regScreen(AstralMekanismMachines.ASTRAL_CHEMICAL_INJECTION_CHAMBER, GuiAstralAdvancedMachine::new);
            regScreen(AstralMekanismMachines.ASTRAL_OSMIUM_COMPRESSOR, GuiAstralAdvancedMachine::new);
            regScreen(AstralMekanismMachines.ASTRAL_PURIFICATION_CHAMBER, GuiAstralAdvancedMachine::new);
            regScreen(AstralMekanismMachines.ASTRAL_CRUSHER, GuiAstralElectricMachine::new);
            regScreen(AstralMekanismMachines.ASTRAL_ENERGIZED_SMELTER, GuiAstralElectricMachine::new);
            regScreen(AstralMekanismMachines.ASTRAL_ENRICHMENT_CHAMBER, GuiAstralElectricMachine::new);
            regScreen(AstralMekanismMachines.ASTRAL_MEKANICAL_CHARGER, GuiAstralElectricMachine::new);
            regScreen(AstralMekanismMachines.ASTRAL_CHEMICAL_INFUSER, GuiAstralChemicalInfuser::new);
            regScreen(AstralMekanismMachines.ASTRAL_CHEMICAL_OXIDIZER, GuiAstralChemicalOxider::new);
            regScreen(AstralMekanismMachines.ASTRAL_CHEMICAL_WASHER, GuiAstralChemicalWasher::new);
            regScreen(AstralMekanismMachines.ASTRAL_COMBINER, GuiDoubleItemToItemRecipeMachine::new);
            regScreen(AstralMekanismMachines.ASTRAL_CRYSTALLIZER, GuiAstralCrystallizer::new);
            regScreen(AstralMekanismMachines.ASTRAL_DISSOLUTION_CHAMBER, GuiAstralDissolutionChamber::new);
            regScreen(AstralMekanismMachines.ASTRAL_ELECTROLYTIC_SEPARATOR, GuiAstralElectrolyticSeparator::new);
            regScreen(AstralMekanismMachines.ASTRAL_GNA, GuiGasToGasBlock::new);
            regScreen(AstralMekanismMachines.ASTRAL_GREENHOUSE, GuiGreenhouse::new);
            regScreen(AstralMekanismMachines.ASTRAL_ISOTOPIC_CENTRIFUGE, GuiGasToGasMachine::new);
            regScreen(AstralMekanismMachines.ASTRAL_MEKANICAL_INSCRIBER, GuiDoubleItemToItemRecipeMachine::new);
            regScreen(AstralMekanismMachines.ASTRAL_MEKANICAL_PRESSER, GuiTripleItemToItemMachine::new);
            regScreen(AstralMekanismMachines.ASTRAL_MEKANICAL_TRANSFOMER, GuiMekanicalTransformer::new);
            regScreen(AstralMekanismMachines.ASTRAL_METALLURGIC_INFUSER, GuiAstralMetallurgicInfuser::new);
            regScreen(AstralMekanismMachines.ASTRAL_PRC, GuiAstralPRC::new);
            regScreen(AstralMekanismMachines.ASTRAL_PRECISION_SAWMILL, GuiAstralPrecisionSawmill::new);
            regScreen(AstralMekanismMachines.ASTRAL_ROTARY_CONDENSENTRATOR, GuiAstralRotaryCondensentrator::new);
            regScreen(AstralMekanismMachines.ASTRAL_SPS, GuiGasToGasMachine::new);
            regScreen(AstralMekanismMachines.COMPACT_FIR, GuiCompactFissionReactor::new);
            regScreen(AstralMekanismMachines.COMPACT_SPS, GuiGasToGasMachine::new);
            regScreen(AstralMekanismMachines.COMPACT_TEP, GuiCompactTEP::new);
            for (AstralMekGeneratorTier tier : AstralMekGeneratorTier.values()) {
                regScreen(AstralMekanismMachines.GAS_BURNING_GENERATORS.get(tier),
                        GuiGasBurningGenerator::new);
                regScreen(AstralMekanismMachines.HEAT_GENERATORS.get(tier),
                        GuiHeatGenerator::new);
            }
            regScreen(AstralMekanismMachines.ASTRAL_CRAFTER, GuiAstralCrafter::new);
            regScreen(AstralMekanismMachines.FLUID_INFUSER, GuiFluidInfuser::new);
            regScreen(AstralMekanismMachines.GLOWSTONE_NEUTRON_ACTIVATOR, GuiGasToGasBlock::new);
            regScreen(AstralMekanismMachines.GREENHOUSE, GuiGreenhouse::new);
            regScreen(AstralMekanismMachines.INFUSE_SYNTHESIZER, GuiInfuseSynthesizer::new);
            regScreen(AstralMekanismMachines.MEKANICAL_CHARGER, GuiElectricMachine::new);
            regScreen(AstralMekanismMachines.MEKANICAL_INSCRIBER, GuiMekanicalInscriber::new);
            regScreen(AstralMekanismMachines.MEKANICAL_PRESSER, GuiTripleItemToItemMachine::new);
            regScreen(AstralMekanismMachines.MEKANICAL_TRANSFORMER, GuiMekanicalTransformer::new);
            regScreen(AstralMekanismMachines.UNIVERSAL_STORAGE, GuiAbstractStorage::new);
            regScreen(AstralMekanismMachines.ITEM_SORTABLE_STORAGE, GuiAbstractStorage::new);
        });
    }

    private static <BE extends TileEntityMekanism, CONTAINER extends MekanismTileContainer<BE>, U extends Screen & MenuAccess<CONTAINER>> void regScreen(
            MachineRegistryObject<BE, ?, CONTAINER, ?> registryObject,
            ScreenConstructor<CONTAINER, U> constructor) {
        ClientRegistrationUtil.registerScreen(registryObject.getContainer(), constructor);
    }

}
