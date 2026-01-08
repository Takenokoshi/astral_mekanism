package astral_mekanism;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;

import java.util.concurrent.CompletableFuture;

import com.electronwill.nightconfig.core.CommentedConfig;

import astral_mekanism.block.AstralMekanismBlockStateProvider;
import astral_mekanism.lang.AstralMekanismEnglishLangProvider;
import astral_mekanism.loottable.AstralMekanismLootTableProvider;
import astral_mekanism.tag.AstralMekanismBlockTags;
import mekanism.common.Mekanism;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = AstralMekanismID.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralMekanismDataGenerator {

    private AstralMekanismDataGenerator(){}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {

        bootstrapConfigs(AstralMekanismID.MODID);
        bootstrapConfigs(Mekanism.MODID);

        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookup = event.getLookupProvider();

        if (event.includeServer()) {
            gen.addProvider(true, new AstralMekanismLootTableProvider(output));
            gen.addProvider(true, new AstralMekanismBlockTags(output, lookup, helper));
        }
        if (event.includeClient()) {
            gen.addProvider(true, new AstralMekanismBlockStateProvider(output, helper));
            //gen.addProvider(true, new AstralMekanismEnglishLangProvider(output));
        }
        System.out.println("### AstralMekanism GatherDataEvent fired ###");
    }
    public static void bootstrapConfigs(String modid) {
        ConfigTracker.INSTANCE.configSets().forEach((type, configs) -> {
            for (ModConfig config : configs) {
                if (config.getModId().equals(modid)) {
                    //Similar to how ConfigTracker#loadDefaultServerConfigs works for loading default server configs on the client
                    // except we don't bother firing an event as it is private, and we are already at defaults if we had called earlier,
                    // and we also don't fully initialize the mod config as the spec is what we care about, and we can do so without having
                    // to reflect into package private methods
                    CommentedConfig commentedConfig = CommentedConfig.inMemory();
                    config.getSpec().correct(commentedConfig);
                    config.getSpec().acceptConfig(commentedConfig);
                }
            }
        });
    }
}
