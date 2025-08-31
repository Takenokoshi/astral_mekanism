package astral_mekanism.registries;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.blocks.cobblestone_supply_device.GuiCobblestoneSupplyDevice;
import astral_mekanism.blocks.compact_tep.GuiCompactTEP;
import astral_mekanism.blocks.fluid_infuser.GuiFluidInfuser;
import astral_mekanism.blocks.gas_burning_generator_reformed.GuiGasBurningGeneratorReformed;
import astral_mekanism.blocks.glowstone_neutron_activator.GuiGlowstoneNeutronActivator;
import astral_mekanism.blocks.green_house.GuiGreenHouse;
import astral_mekanism.blocks.melter.GuiMelter;
import astral_mekanism.blocks.universal_storage.GuiUniversalStorage;
import astral_mekanism.blocks.water_supply_device.GuiWaterSupplyDevice;
import mekanism.client.ClientRegistrationUtil;
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
            ClientRegistrationUtil.registerScreen(AstralMekanismContainers.Universal_storage, GuiUniversalStorage::new);
            ClientRegistrationUtil.registerScreen(AstralMekanismContainers.Water_supply_device, GuiWaterSupplyDevice::new);
            ClientRegistrationUtil.registerScreen(AstralMekanismContainers.Cobblestone_supply_device,
                    GuiCobblestoneSupplyDevice::new);
            ClientRegistrationUtil.registerScreen(AstralMekanismContainers.Gas_BGR, GuiGasBurningGeneratorReformed::new);
            ClientRegistrationUtil.registerScreen(AstralMekanismContainers.Glowstone_neutron_activator,
                    GuiGlowstoneNeutronActivator::new);
            ClientRegistrationUtil.registerScreen(AstralMekanismContainers.Greenhouse, GuiGreenHouse::new);
            ClientRegistrationUtil.registerScreen(AstralMekanismContainers.Melter, GuiMelter::new);
            ClientRegistrationUtil.registerScreen(AstralMekanismContainers.CompactTEP, GuiCompactTEP::new);
            ClientRegistrationUtil.registerScreen(AstralMekanismContainers.FLUID_INFUSER, GuiFluidInfuser::new);
        });
    }
}
