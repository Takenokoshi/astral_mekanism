package astral_mekanism.block;

import java.util.EnumMap;
import java.util.function.BiFunction;

import astral_mekanism.AstralMekanismTier;
import astral_mekanism.AstralMekanismID;
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
        registerNormalFactories(AstralMekanismMachines.ENERGIZED_SMELTING_FACTORIES);
        registerTierdMachines(AstralMekanismMachines.COMPACT_FIR,
                (t, s) -> "block/compact_machine/" + s + "/" + t.nameForNormal, "fir",
                "block/compact_machine/fir/base");
        registerTierdMachines(AstralMekanismMachines.COMPACT_TEP,
                (t, s) -> "block/compact_machine/" + s + "/" + t.nameForNormal, "tep",
                "block/compact_machine/tep/base");
    }

    private void registerAstralFactories(EnumMap<AstralMekanismTier, ? extends IBlockProvider> map) {
        for (AstralMekanismTier tier : AstralMekanismTier.values()) {
            IBlockProvider factory = map.get(tier);

            String baseName = factory.getRegistryName().getPath()
                    .replace(tier.nameForAstral + "_astral_", "")
                    .replace("_factory", "");

            BlockModelBuilder base = models().getBuilder("block/aaaaa/astral/"+baseName)
                    .parent(models().getExistingFile(
                            AstralMekanismID.rl("block/astral_factory/" + baseName + "/base")));

            BlockModelBuilder child = models().getBuilder("block/aaaaa/astral/"+baseName+"/"+tier.nameForNormal)
                    .parent(models().getExistingFile(
                            AstralMekanismID.rl("block/factory/led/" + tier.nameForNormal)));

            ModelFile model = models().getBuilder(
                    "block/astral_factory/" + baseName + "/" + tier.nameForAstral)
                    .parent(models().getExistingFile(new ResourceLocation("block/block")))
                    .texture("particle", AstralMekanismID.rl("block/factory/base"))
                    .customLoader(CompositeModelBuilder::begin)
                    .child("base", base)
                    .child("front_led", child)
                    .end();

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

            itemModels().getBuilder(factory.getRegistryName().getPath())
                    .parent(model);
        }
    }

    private void registerNormalFactories(EnumMap<AstralMekanismTier, ? extends IBlockProvider> map) {
        for (AstralMekanismTier tier : AstralMekanismTier.values()) {
            IBlockProvider factory = map.get(tier);

            String baseName = factory.getRegistryName().getPath()
                    .replace(tier.nameForNormal + "_", "")
                    .replace("_factory", "");

            BlockModelBuilder base = models().getBuilder("block/aaaaa/"+baseName)
                    .parent(models().getExistingFile(
                            AstralMekanismID.rl("block/normal_factory/" + baseName + "/base")));

            BlockModelBuilder child = models().getBuilder("block/aaaaa/"+baseName+"/"+tier.nameForNormal)
                    .parent(models().getExistingFile(
                            AstralMekanismID.rl("block/factory/led/" + tier.nameForNormal)));

            ModelFile model = models().getBuilder(
                    "block/normal_factory/" + baseName + "/" + tier.nameForNormal)
                    .parent(models().getExistingFile(new ResourceLocation("block/block")))
                    .texture("particle", AstralMekanismID.rl("block/factory/base"))
                    .customLoader(CompositeModelBuilder::begin)
                    .child("base", base)
                    .child("front_led", child)
                    .end();

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

            itemModels().getBuilder(factory.getRegistryName().getPath())
                    .parent(model);
        }
    }

    private void registerTierdMachines(
            EnumMap<AstralMekanismTier, ? extends IBlockProvider> map,
            BiFunction<AstralMekanismTier, String, String> resultModelPathFunction,
            String baseName,
            String baseModelPath) {
        BlockModelBuilder base = models().getBuilder("block/aaaaa/"+baseName)
                .parent(models().getExistingFile(AstralMekanismID.rl(baseModelPath)));
        for (AstralMekanismTier tier : AstralMekanismTier.values()) {
            IBlockProvider machine = map.get(tier);
            BlockModelBuilder child = models().getBuilder("block/aaaaa/"+baseName+"/"+tier.nameForNormal)
                    .parent(models().getExistingFile(
                            AstralMekanismID.rl("block/factory/led/" + tier.nameForNormal)));

            ModelFile model = models().getBuilder(resultModelPathFunction.apply(tier, baseName))
                    .parent(models().getExistingFile(new ResourceLocation("block/block")))
                    .texture("particle", AstralMekanismID.rl("block/factory/base"))
                    .customLoader(CompositeModelBuilder::begin)
                    .child("base", base)
                    .child("front_led", child)
                    .end();

            getVariantBuilder(machine.getBlock())
                    .forAllStatesExcept(state -> {
                        Direction dir = state.getValue(HorizontalDirectionalBlock.FACING);
                        return ConfiguredModel.builder()
                                .modelFile(model)
                                .rotationY((int) dir.toYRot() + 180)
                                .build();
                    },
                            machine.getBlock().getStateDefinition().getProperties().stream()
                                    .filter(p -> p.getName().equals("fluid_logged"))
                                    .toArray(Property[]::new));

            itemModels().getBuilder(machine.getRegistryName().getPath())
                    .parent(model);
        }
    }

}
