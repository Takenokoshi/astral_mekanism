
package astral_mekanism.registration;

import java.util.EnumSet;
import java.util.function.Supplier;
import mekanism.api.Upgrade;
import mekanism.api.text.ILangEntry;
import mekanism.common.block.attribute.AttributeFactoryType;
import mekanism.common.block.attribute.AttributeParticleFX;
import mekanism.common.block.attribute.AttributeStateFacing;
import mekanism.common.block.attribute.AttributeUpgradeSupport;
import mekanism.common.block.attribute.AttributeUpgradeable;
import mekanism.common.block.attribute.Attributes;
import mekanism.common.content.blocktype.BlockTypeTile;
import mekanism.common.content.blocktype.FactoryType;
import mekanism.common.lib.math.Pos3D;
import mekanism.common.registration.impl.TileEntityTypeRegistryObject;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.tier.FactoryTier;
import mekanism.common.tile.base.TileEntityMekanism;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleTypes;

//This class is almost thie same as `mekanism.common.content.blocktype.Machine`.
//This class allows me to use `ILangEntry` instead of `MekanismLang`.
public class BlockTypeMachine<TILE extends TileEntityMekanism> extends BlockTypeTile<TILE> {

    public BlockTypeMachine(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar, ILangEntry description) {
        super(tileEntityRegistrar, description);
        // add default particle effects
        add(new AttributeParticleFX()
                .add(ParticleTypes.SMOKE,
                        rand -> new Pos3D(rand.nextFloat() * 0.6F - 0.3F, rand.nextFloat() * 6.0F / 16.0F, 0.52))
                .add(DustParticleOptions.REDSTONE,
                        rand -> new Pos3D(rand.nextFloat() * 0.6F - 0.3F, rand.nextFloat() * 6.0F / 16.0F, 0.52)));
        add(Attributes.ACTIVE_LIGHT, new AttributeStateFacing(), Attributes.INVENTORY, Attributes.SECURITY,
                Attributes.REDSTONE, Attributes.COMPARATOR);
        add(new AttributeUpgradeSupport(EnumSet.of(Upgrade.SPEED, Upgrade.ENERGY, Upgrade.MUFFLING)));
    }

    public static class FactoryMachine<TILE extends TileEntityMekanism> extends BlockTypeMachine<TILE> {

        public FactoryMachine(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntitySupplier, ILangEntry description,
                FactoryType factoryType) {
            super(tileEntitySupplier, description);
            add(new AttributeFactoryType(factoryType),
                    new AttributeUpgradeable(() -> MekanismBlocks.getFactory(FactoryTier.BASIC, getFactoryType())));
        }

        public FactoryType getFactoryType() {
            return get(AttributeFactoryType.class).getFactoryType();
        }
    }

    public static class BlockMachineBuilder<MACHINE extends BlockTypeMachine<TILE>, TILE extends TileEntityMekanism>
            extends BlockTileBuilder<MACHINE, TILE, BlockMachineBuilder<MACHINE, TILE>> {

        protected BlockMachineBuilder(MACHINE holder) {
            super(holder);
        }

        public static <TILE extends TileEntityMekanism> BlockMachineBuilder<BlockTypeMachine<TILE>, TILE> createMachine(
                Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar, ILangEntry description) {
            return new BlockMachineBuilder<>(new BlockTypeMachine<>(tileEntityRegistrar, description));
        }

        public static <TILE extends TileEntityMekanism> BlockMachineBuilder<FactoryMachine<TILE>, TILE> createFactoryMachine(
                Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar,
                ILangEntry description, FactoryType factoryType) {
            return new BlockMachineBuilder<>(new FactoryMachine<>(tileEntityRegistrar, description, factoryType));
        }
    }
}
