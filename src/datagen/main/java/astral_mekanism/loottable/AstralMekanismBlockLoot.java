package astral_mekanism.loottable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import astral_mekanism.registration.MachineRegistryObject;
import astral_mekanism.registries.AstralMekanismBlocks;
import astral_mekanism.registries.AstralMekanismMachines;
import mekanism.api.NBTConstants;
import mekanism.api.annotations.NothingNullByDefault;
import mekanism.api.annotations.ParametersAreNotNullByDefault;
import mekanism.api.providers.IBlockProvider;
import mekanism.common.block.attribute.Attribute;
import mekanism.common.block.attribute.AttributeUpgradeSupport;
import mekanism.common.block.attribute.Attributes.AttributeInventory;
import mekanism.common.block.attribute.Attributes.AttributeRedstone;
import mekanism.common.block.attribute.Attributes.AttributeSecurity;
import mekanism.common.block.interfaces.IHasTileEntity;
import mekanism.common.lib.frequency.IFrequencyHandler;
import mekanism.common.tile.base.SubstanceType;
import mekanism.common.tile.base.TileEntityMekanism;
import mekanism.common.tile.interfaces.ISideConfiguration;
import mekanism.common.tile.interfaces.ISustainedData;
import mekanism.common.util.EnumUtils;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction.MergeStrategy;
import net.minecraft.world.level.storage.loot.functions.FunctionUserBuilder;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.predicates.ConditionUserBuilder;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.nbt.NbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.items.IItemHandler;

public class AstralMekanismBlockLoot extends BlockLootSubProvider {

    public AstralMekanismBlockLoot() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        AstralMekanismMachines.MACHINES.getAllMachines().forEach(this::createMachineDrop);
        AstralMekanismBlocks.BLOCKS.getAllBlocks().forEach(b->{
            dropSelf(b.getBlock());
        });
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        List<Block> list = new ArrayList<>();
        for (IBlockProvider blockProvider : AstralMekanismMachines.MACHINES.getAllMachines()) {
            list.add(blockProvider.getBlock());
        }
        for (IBlockProvider blockProvider : AstralMekanismBlocks.BLOCKS.getAllBlocks()) {
            list.add(blockProvider.getBlock());
        }
        return list;
    }

    protected void createMachineDrop(MachineRegistryObject<?, ?, ?, ?> machine) {
        TrackingNbtBuilder nbtBuilder = new TrackingNbtBuilder(ContextNbtProvider.BLOCK_ENTITY);
        Block block = machine.getBlock();
        BlockEntity be = ((IHasTileEntity<?>) (block)).createDummyBlockEntity();
        LootItem.Builder<?> itemLootPool = LootItem.lootTableItem(block);
        DelayedLootItemBuilder delayedPool = new DelayedLootItemBuilder();
        if (be instanceof IFrequencyHandler frequencyHandler
                && frequencyHandler.getFrequencyComponent().hasCustomFrequencies()) {
            nbtBuilder.copy(NBTConstants.COMPONENT_FREQUENCY,
                    NBTConstants.MEK_DATA + "." + NBTConstants.COMPONENT_FREQUENCY);
        }
        if (Attribute.has(block, AttributeSecurity.class)) {
            nbtBuilder.copy(NBTConstants.COMPONENT_SECURITY + "." + NBTConstants.OWNER_UUID,
                    NBTConstants.MEK_DATA + "." + NBTConstants.OWNER_UUID);
            nbtBuilder.copy(NBTConstants.COMPONENT_SECURITY + "." + NBTConstants.SECURITY_MODE,
                    NBTConstants.MEK_DATA + "." + NBTConstants.SECURITY_MODE);
        }
        if (Attribute.has(block, AttributeUpgradeSupport.class)) {
            nbtBuilder.copy(NBTConstants.COMPONENT_UPGRADE,
                    NBTConstants.MEK_DATA + "." + NBTConstants.COMPONENT_UPGRADE);
        }
        if (be instanceof ISideConfiguration) {
            nbtBuilder.copy(NBTConstants.COMPONENT_CONFIG, NBTConstants.MEK_DATA + "." + NBTConstants.COMPONENT_CONFIG);
            nbtBuilder.copy(NBTConstants.COMPONENT_EJECTOR,
                    NBTConstants.MEK_DATA + "." + NBTConstants.COMPONENT_EJECTOR);
        }
        if (be instanceof ISustainedData sustainedData) {
            Set<Entry<String, String>> remapEntries = sustainedData.getTileDataRemap().entrySet();
            for (Entry<String, String> remapEntry : remapEntries) {
                nbtBuilder.copy(remapEntry.getKey(), NBTConstants.MEK_DATA + "." + remapEntry.getValue());
            }
        }
        if (Attribute.has(block, AttributeRedstone.class)) {
            nbtBuilder.copy(NBTConstants.CONTROL_TYPE, NBTConstants.MEK_DATA + "." + NBTConstants.CONTROL_TYPE);
        }
        if (be instanceof TileEntityMekanism tileEntity) {
            if (tileEntity.isNameable()) {
                itemLootPool.apply(CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY));
            }
            for (SubstanceType type : EnumUtils.SUBSTANCES) {
                if (tileEntity.handles(type) && !type.getContainers(tileEntity).isEmpty()) {
                    nbtBuilder.copy(type.getContainerTag(), NBTConstants.MEK_DATA + "." + type.getContainerTag());
                }
            }
        }
        @SuppressWarnings("unchecked")
        AttributeInventory<DelayedLootItemBuilder> attributeInventory = Attribute.get(block, AttributeInventory.class);
            if (attributeInventory != null) {
                if (attributeInventory.hasCustomLoot()) {
                    attributeInventory.applyLoot(delayedPool, nbtBuilder);
                }
                //If the block has an inventory and no custom loot function, copy the inventory slots,
                // but if it is an IItemHandler, which for most cases of ours it will be,
                // then only copy the slots if we actually have any slots because otherwise maybe something just went wrong
                else if (!(be instanceof IItemHandler handler) || handler.getSlots() > 0) {
                    //If we don't actually handle saving an inventory (such as the quantum entangloporter, don't actually add it as something to copy)
                    if (!(be instanceof TileEntityMekanism tileMek) || tileMek.persistInventory()) {
                        nbtBuilder.copy(NBTConstants.ITEMS, NBTConstants.MEK_DATA + "." + NBTConstants.ITEMS);
                    }
                }
            }
        if (nbtBuilder.hasData()) {
            itemLootPool.apply(nbtBuilder);
        }
        // apply the delayed ones last, so that NBT funcs have happened first
        for (LootItemFunction.Builder function : delayedPool.functions) {
            itemLootPool.apply(function);
        }
        for (LootItemCondition.Builder condition : delayedPool.conditions) {
            itemLootPool.when(condition);
        }

        add(block, LootTable.lootTable().withPool(applyExplosionCondition(true, LootPool.lootPool()
                .name("main")
                .setRolls(ConstantValue.exactly(1))
                .add(itemLootPool))));
    }

    private static <T extends ConditionUserBuilder<T>> T applyExplosionCondition(boolean explosionResistant,
            ConditionUserBuilder<T> condition) {
        return explosionResistant ? condition.unwrap() : condition.when(ExplosionCondition.survivesExplosion());
    }

    @MethodsReturnNonnullByDefault
    @ParametersAreNotNullByDefault
    public static class TrackingNbtBuilder extends CopyNbtFunction.Builder {
        private boolean hasData = false;

        public TrackingNbtBuilder(NbtProvider pNbtSource) {
            super(pNbtSource);
        }

        public boolean hasData() {
            return this.hasData;
        }

        @Override
        public CopyNbtFunction.Builder copy(String pSourcePath, String pTargetPath, MergeStrategy pCopyAction) {
            this.hasData = true;
            return super.copy(pSourcePath, pTargetPath, pCopyAction);
        }
    }

    @NothingNullByDefault
    public static class DelayedLootItemBuilder
            implements ConditionUserBuilder<DelayedLootItemBuilder>, FunctionUserBuilder<DelayedLootItemBuilder> {
        private final List<LootItemFunction.Builder> functions = new ArrayList<>();
        private final List<LootItemCondition.Builder> conditions = new ArrayList<>();

        @Override
        public DelayedLootItemBuilder apply(LootItemFunction.Builder pFunctionBuilder) {
            functions.add(pFunctionBuilder);
            return this;
        }

        @Override
        public DelayedLootItemBuilder when(LootItemCondition.Builder pConditionBuilder) {
            conditions.add(pConditionBuilder);
            return this;
        }

        @Override
        public DelayedLootItemBuilder unwrap() {
            return this;
        }
    }

}
