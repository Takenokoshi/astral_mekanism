package astral_mekanism.tag;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import astral_mekanism.AstralMekanismID;
import astral_mekanism.registries.AstralMekanismBlocks;
import astral_mekanism.registries.AstralMekanismMachines;
import mekanism.api.providers.IBlockProvider;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class AstralMekanismBlockTags extends BlockTagsProvider {

    public AstralMekanismBlockTags(PackOutput output, CompletableFuture<Provider> lookupProvider,
            @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, AstralMekanismID.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(Provider provider) {
        final List<IBlockProvider> MINEABLE_WITH_PICKAXE = new ArrayList<>();
        MINEABLE_WITH_PICKAXE.addAll(AstralMekanismMachines.MACHINES.getAllMachines());
        MINEABLE_WITH_PICKAXE.addAll(AstralMekanismBlocks.BLOCKS.getAllBlocks());
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(MINEABLE_WITH_PICKAXE.stream().map(IBlockProvider::getBlock).toArray(Block[]::new));
    }

}
