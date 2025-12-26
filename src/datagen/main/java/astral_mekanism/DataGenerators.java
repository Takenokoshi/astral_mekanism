package astral_mekanism;

import astral_mekanism.AstralMekanismID;
import net.minecraftforge.fml.common.Mod;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.common.data.ExistingFileHelper;

@Mod.EventBusSubscriber(
    modid = AstralMekanismID.MODID,
    bus = Mod.EventBusSubscriber.Bus.MOD
)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {

        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
    }
}
