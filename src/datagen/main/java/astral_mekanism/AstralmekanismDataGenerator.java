package astral_mekanism;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.config.ModConfig;

import java.util.concurrent.CompletableFuture;

import com.electronwill.nightconfig.core.CommentedConfig;

import astral_mekanism.block.AstralMekanismBlockStateProvider;
import astral_mekanism.item.AstralMekanismItemModelProvider;
import astral_mekanism.lang.AstralMekanismEnglishLangProvider;
import astral_mekanism.loottable.AstralMekanismLootTableProvider;
import astral_mekanism.recipe.AstralMekanismRecipeProvider;
import astral_mekanism.tag.AstralMekanismBlockTags;
import astral_mekanism.tag.AstralMekanismItemTags;
import mekanism.common.Mekanism;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider.TagLookup;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(modid = AstralMekanismID.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AstralMekanismDataGenerator {

    private AstralMekanismDataGenerator() {
    }

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
            gen.addProvider(true, new AstralMekanismItemTags(output, lookup,
                    CompletableFuture.completedFuture(TagLookup.empty()), helper));// */
            gen.addProvider(true, new AstralMekanismRecipeProvider(output));
        }
        if (event.includeClient()) {
            gen.addProvider(true, new AstralMekanismBlockStateProvider(output, helper));
            gen.addProvider(true, new AstralMekanismEnglishLangProvider(output));
            gen.addProvider(true, new AstralMekanismItemModelProvider(output, helper));
        }
        System.out.println("### AstralMekanism GatherDataEvent fired ###");
    }

    public static void bootstrapConfigs(String modid) {
        ConfigTracker.INSTANCE.configSets().forEach((type, configs) -> {
            for (ModConfig config : configs) {
                if (config.getModId().equals(modid)) {
                    CommentedConfig commentedConfig = CommentedConfig.inMemory();
                    config.getSpec().correct(commentedConfig);
                    config.getSpec().acceptConfig(commentedConfig);
                }
            }
        });
    }
}
