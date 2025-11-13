package astral_mekanism.registries;

import appeng.items.parts.PartItem;
import astral_mekanism.AstralMekanismID;
import astral_mekanism.cablepart.HeatP2PTunnelPart;
import astral_mekanism.registration.CablePartDeferredregister;
import astral_mekanism.registration.CablePartRegistyObject;

public class AstralMekanismCableParts {
    public static final CablePartDeferredregister CABLE_PARTS = new CablePartDeferredregister(AstralMekanismID.MODID);

    public static final CablePartRegistyObject<HeatP2PTunnelPart, PartItem<HeatP2PTunnelPart>> HEAT_P2P = CABLE_PARTS
            .register("heat_p2p", HeatP2PTunnelPart.class,
                    PartItem<HeatP2PTunnelPart>::new, HeatP2PTunnelPart::new);
}
