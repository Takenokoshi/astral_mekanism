package astral_mekanism;

import net.minecraftforge.fml.common.Mod;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

@Mod.EventBusSubscriber(
    modid = AstralMekanismID.MODID,
    bus = Mod.EventBusSubscriber.Bus.MOD
)
public class AstralMekanismDataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {

        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
    }
}
