package astral_mekanism.registries;

import astral_mekanism.AMEConstants;
import astral_mekanism.AstralMekanismLang;
import mekanism.common.registration.impl.CreativeTabDeferredRegister;
import mekanism.common.registration.impl.CreativeTabRegistryObject;
import net.minecraft.world.level.ItemLike;

public class AstralMekanismCreativeTab {
    public static final CreativeTabDeferredRegister CREATIVE_TABS = new CreativeTabDeferredRegister(
            AMEConstants.MODID);

    public static final CreativeTabRegistryObject ASTRAL_MEKANISM_TAB = CREATIVE_TABS.register("astral_mekanism_tab",
            AstralMekanismLang.ITEM_GROUP, AstralMekanismItems.ASTRAL_DIAMOND,
            builder -> builder.displayItems((displayParameters, output) -> {
                CreativeTabDeferredRegister.addToDisplay(AstralMekanismMachines.MACHINES.blockRegister, output);
                CreativeTabDeferredRegister.addToDisplay(AstralMekanismBlocks.BLOCKS, output);
                CreativeTabDeferredRegister.addToDisplay(output, AMEBlockDefinitions
                        .getBlocks(AMEConstants.MODID).stream().toArray(ItemLike[]::new));
                CreativeTabDeferredRegister.addToDisplay(AstralMekanismItems.ITEMS, output);
                CreativeTabDeferredRegister.addToDisplay(AstralMekanismFluids.FLUIDS, output);
            }));
}
