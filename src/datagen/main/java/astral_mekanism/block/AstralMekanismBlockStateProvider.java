package astral_mekanism.block;

import java.util.EnumMap;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.block.blockentity.base.AstralMekanismFactoryTier;
import astral_mekanism.registries.AstralMekanismMachines;
import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.CompositeModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

public class AstralMekanismBlockStateProvider extends BlockStateProvider {

    public AstralMekanismBlockStateProvider(PackOutput output, ExistingFileHelper efh) {
        super(output, AstralMekanismID.MODID, efh);
    }

    @Override
    protected void registerStatesAndModels() {
        registerAstralFactories(AstralMekanismMachines.ASTRAL_ENERGIZED_SMELTING_FACTRIES);
    }

    private void registerAstralFactories(EnumMap<AstralMekanismFactoryTier, ? extends IBlockProvider> map) {
        for (AstralMekanismFactoryTier tier : AstralMekanismFactoryTier.values()) {
            IBlockProvider factory = map.get(tier);

            String baseName = factory.getRegistryName().getPath()
                    .replace(tier.nameForAstral + "_astral_", "")
                    .replace("_factory", "");

            BlockModelBuilder base = models().getBuilder("block/astral_factory/" + baseName + "/base/p")
                    .parent(models().getExistingFile(
                            AstralMekanismID.rl("block/astral_factory/" + baseName + "/base")));

            BlockModelBuilder child = models().getBuilder("block/factory/led/" + tier.nameForNormal + "/p")
                    .parent(models().getExistingFile(
                            AstralMekanismID.rl("block/factory/led/" + tier.nameForNormal)));

            // ===== Composite Model =====
            ModelFile model = models().getBuilder(
                    "block/astral_factory/" + baseName + "/" + tier.nameForAstral)
                    // ★ parent は BlockModelBuilder 側
                    .parent(models().getExistingFile(new ResourceLocation("block/block")))
                    .texture("particle", AstralMekanismID.rl("block/factory/base"))

                    // ★ composite loader
                    .customLoader(CompositeModelBuilder::begin)
                    .child("base", base)
                    .child("front_led", child)
                    .end();

            // ===== BlockState =====
            getVariantBuilder(factory.getBlock())
                    .forAllStatesExcept(state -> {
                        Direction dir = state.getValue(HorizontalDirectionalBlock.FACING);
                        return ConfiguredModel.builder()
                                .modelFile(model)
                                .rotationY((int) dir.toYRot() + 180)
                                .build();
                    },
                            factory.getBlock().getStateDefinition().getProperties().stream()
                                    .filter(p -> p.getName().equals("fluid_logged"))
                                    .toArray(Property[]::new));

            // ===== Item Model =====
            itemModels().getBuilder(factory.getRegistryName().getPath())
                    .parent(model);
        }
    }

}
