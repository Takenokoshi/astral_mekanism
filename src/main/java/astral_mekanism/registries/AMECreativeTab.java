package astral_mekanism.registries;

import astral_mekanism.AMEConstants;
import astral_mekanism.AMELang;
import mekanism.common.registration.impl.CreativeTabDeferredRegister;
import mekanism.common.registration.impl.CreativeTabRegistryObject;
import net.minecraft.world.level.ItemLike;

public class AMECreativeTab {
    public static final CreativeTabDeferredRegister CREATIVE_TABS = new CreativeTabDeferredRegister(
            AMEConstants.MODID);

    public static final CreativeTabRegistryObject ASTRAL_MEKANISM_TAB = CREATIVE_TABS.register("astral_mekanism_tab",
            AMELang.ITEM_GROUP, AMEItems.STARRY_SKY_ALLOY_INGOT,
            builder -> builder.displayItems((displayParameters, output) -> {
                CreativeTabDeferredRegister.addToDisplay(AMEMachines.MACHINES.blockRegister, output);
                CreativeTabDeferredRegister.addToDisplay(AMEBlocks.BLOCKS, output);
                CreativeTabDeferredRegister.addToDisplay(output, AMEBlockDefinitions
                        .getBlocks(AMEConstants.MODID).stream().toArray(ItemLike[]::new));
                CreativeTabDeferredRegister.addToDisplay(AMEItems.ITEMS, output);
                CreativeTabDeferredRegister.addToDisplay(AMEFluids.FLUIDS, output);
            }));
}
