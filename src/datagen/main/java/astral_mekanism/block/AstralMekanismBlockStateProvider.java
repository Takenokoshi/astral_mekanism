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
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
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
            String baseName = factory.getRegistryName().getPath().replace(tier.nameForAstral + "_astral_", "")
                    .replace("_factory", "");
            ResourceLocation ledLocation = modLoc("block/factory/led/" + tier.nameForNormal);
            ModelFile model = models().withExistingParent("block/astral_factory/" + baseName + "/" + tier.nameForAstral,
                    modLoc("block/astral_factory/" + baseName + "/base"))
                    .parent(models().getExistingFile(ledLocation));

            getVariantBuilder(factory.getBlock()).forAllStates(state -> {
                Direction dir = state.getValue(HorizontalDirectionalBlock.FACING);
                return ConfiguredModel.builder().modelFile(model)
                        .rotationY((int) (dir.toYRot()) % 360)
                        .build();
            });
            itemModels().getBuilder(factory.getRegistryName().getPath()).parent(model);
        }
    }
}
