package astral_mekanism.block;

import java.util.EnumMap;
import java.util.function.BiFunction;

import appeng.block.networking.EnergyCellBlock;
import astral_mekanism.AMETier;
import astral_mekanism.AMEConstants;
import astral_mekanism.registries.AMEBlockDefinitions;
import astral_mekanism.registries.AMEBlocks;
import astral_mekanism.registries.AMEMachines;
import astral_mekanism.registryenum.AMEProcessableMaterialType;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.registration.impl.BlockRegistryObject;
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
    private final ExistingFileHelper efh;

    public AstralMekanismBlockStateProvider(PackOutput output, ExistingFileHelper efh) {
        super(output, AMEConstants.MODID, efh);
        this.efh = efh;
    }

    @Override
    protected void registerStatesAndModels() {
        registerOres(AMEBlocks.RECONSTRUCTED_ORE);
        registerOres(AMEBlocks.ENRICHED_ORE);
        registerOres(AMEBlocks.SPARKLING_ORE);
        registerOres(AMEBlocks.COMPRESSED_ORE);
        registerAstralFactories(AMEMachines.ASTRAL_ENERGIZED_SMELTING_FACTRIES);
        registerNormalFactories(AMEMachines.ENERGIZED_SMELTING_FACTORIES);
        registerTierdMachines(AMEMachines.COMPACT_FIR,
                (t, s) -> "block/compact_machine/" + s + "/" + t.nameForNormal, "fir",
                "block/compact_machine/fir/base");
        registerTierdMachines(AMEMachines.COMPACT_TEP,
                (t, s) -> "block/compact_machine/" + s + "/" + t.nameForNormal, "tep",
                "block/compact_machine/tep/base");
        registerTierdMachines(AMEMachines.COMPACT_FUSION_REACTOR,
                (t, s) -> "block/compact_machine/" + s + "/" + t.nameForNormal, "fusion_reactor",
                "block/compact_machine/fusion_reactor/base");
        registerTierdMachines(AMEMachines.COMPACT_NAQUADAH_REACTOR,
                (t, s) -> "block/compact_machine/" + s + "/" + t.nameForNormal, "naquadah_reactor",
                "block/compact_machine/naquadah_reactor/base");
        registerEnergyCells();
    }

    private void registerOres(EnumMap<AMEProcessableMaterialType, BlockRegistryObject<?, ?>> ores) {
        ores.forEach((type, obj) -> {
            getVariantBuilder(obj.getBlock()).forAllStates(state -> ConfiguredModel
                    .builder()
                    .modelFile(models().getExistingFile(AMEConstants.rl("block/ore/" + type.name)))
                    .build());
            itemModels().getBuilder(obj.getRegistryName().getPath())
                    .parent(models().getExistingFile(AMEConstants.rl("block/ore/" + type.name)));
        });
    }

    private void registerAstralFactories(EnumMap<AMETier, ? extends IBlockProvider> map) {
        for (AMETier tier : AMETier.values()) {
            IBlockProvider factory = map.get(tier);

            String baseName = factory.getRegistryName().getPath()
                    .replace(tier.nameForAstral + "_astral_", "")
                    .replace("_factory", "");

            BlockModelBuilder base = models().getBuilder("block/aaaaa/astral/" + baseName)
                    .parent(models().getExistingFile(
                            AMEConstants.rl("block/astral_factory/" + baseName + "/base")));

            BlockModelBuilder child = models().getBuilder("block/aaaaa/astral/" + baseName + "/" + tier.nameForNormal)
                    .parent(models().getExistingFile(
                            AMEConstants.rl("block/factory/led/" + tier.nameForNormal)));

            ModelFile model = models().getBuilder(
                    "block/astral_factory/" + baseName + "/" + tier.nameForAstral)
                    .parent(models().getExistingFile(new ResourceLocation("block/block")))
                    .texture("particle", AMEConstants.rl("block/factory/base"))
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

    private void registerNormalFactories(EnumMap<AMETier, ? extends IBlockProvider> map) {
        for (AMETier tier : AMETier.values()) {
            IBlockProvider factory = map.get(tier);

            String baseName = factory.getRegistryName().getPath()
                    .replace(tier.nameForNormal + "_", "")
                    .replace("_factory", "");

            BlockModelBuilder base = models().getBuilder("block/aaaaa/" + baseName)
                    .parent(models().getExistingFile(
                            AMEConstants.rl("block/normal_factory/" + baseName + "/base")));

            BlockModelBuilder child = models().getBuilder("block/aaaaa/" + baseName + "/" + tier.nameForNormal)
                    .parent(models().getExistingFile(
                            AMEConstants.rl("block/factory/led/" + tier.nameForNormal)));

            ModelFile model = models().getBuilder(
                    "block/normal_factory/" + baseName + "/" + tier.nameForNormal)
                    .parent(models().getExistingFile(new ResourceLocation("block/block")))
                    .texture("particle", AMEConstants.rl("block/factory/base"))
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
            EnumMap<AMETier, ? extends IBlockProvider> map,
            BiFunction<AMETier, String, String> resultModelPathFunction,
            String baseName,
            String baseModelPath) {
        BlockModelBuilder base = models().getBuilder("block/aaaaa/" + baseName)
                .parent(models().getExistingFile(AMEConstants.rl(baseModelPath)));
        for (AMETier tier : AMETier.values()) {
            IBlockProvider machine = map.get(tier);
            BlockModelBuilder child = models().getBuilder("block/aaaaa/" + baseName + "/" + tier.nameForNormal)
                    .parent(models().getExistingFile(
                            AMEConstants.rl("block/factory/led/" + tier.nameForNormal)));

            ModelFile model = models().getBuilder(resultModelPathFunction.apply(tier, baseName))
                    .parent(models().getExistingFile(new ResourceLocation("block/block")))
                    .texture("particle", AMEConstants.rl("block/factory/base"))
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

    private void registerEnergyCells() {
        AMEBlockDefinitions.ENERGY_CELLS.forEach((tier, object) -> {
            ModelFile[] files = new ModelFile[5];
            for (int i = 0; i < files.length; i++) {
                files[i] = models().getBuilder("block/energy_cell/" + tier.nameForAE + "_" + i)
                        .parent(models().getExistingFile(AMEConstants.rl("block/energy_cell/base")))
                        .texture("frame", AMEConstants.rl("block/energy_cell/" + tier.nameForAE + "_frame"))
                        .texture("base", AMEConstants.rl("block/energy_cell/base_" + i));
            }
            getVariantBuilder(object.block()).forAllStates(state -> ConfiguredModel
                    .builder()
                    .modelFile(files[state.getValue(EnergyCellBlock.ENERGY_STORAGE)])
                    .build());
            itemModels().getBuilder(object.id().getPath()).parent(files[0]);
        });
    }

}
