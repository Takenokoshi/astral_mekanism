package astral_mekanism.registration;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

import mekanism.api.text.ILangEntry;
import mekanism.common.block.attribute.AttributeStateFacing;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.BlockTypeTile.BlockTileBuilder;
import mekanism.common.inventory.container.MekanismContainer;
import mekanism.common.registration.impl.ContainerTypeRegistryObject;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.tile.base.TileEntityMekanism;

public class BlockTypeTileUtils {
    public static <KEY extends Enum<KEY>, BE extends TileEntityMekanism> Map<KEY, BlockTypeTile<BE>> buildMap(
            Class<KEY> keyClass,
            Function<KEY, Supplier<TileEntityTypeRegistryObject<BE>>> tFunction,
            ILangEntry description,
            Function<KEY, Supplier<ContainerTypeRegistryObject<? extends MekanismContainer>>> cFunction,
            BiFunction<KEY,BlockTileBuilder<BlockTypeTile<BE>,BE,?>,BlockTileBuilder<BlockTypeTile<BE>,BE,?>> bBiFunction) {
        Map<KEY, BlockTypeTile<BE>> result = new EnumMap<>(keyClass);
        for (KEY key : keyClass.getEnumConstants()) {
            result.put(key, bBiFunction.apply(key, BlockTileBuilder
                    .createBlock(tFunction.apply(key), description)
                    .withGui(cFunction.apply(key))
                    .with(new AttributeStateFacing()))
                    .build());
        }
        return result;
    }
}
