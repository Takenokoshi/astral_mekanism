package astral_mekanism.mixin.mekanism;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import astral_mekanism.enumexpansion.AMEUpgrade;
import mekanism.api.DataHandlerUtils;
import mekanism.api.NBTConstants;
import mekanism.api.Upgrade;
import mekanism.api.chemical.ChemicalTankBuilder;
import mekanism.api.chemical.gas.IGasTank;
import mekanism.api.chemical.gas.attribute.GasAttributes;
import mekanism.common.block.BlockMekanism;
import mekanism.common.util.ItemDataUtils;
import mekanism.common.util.NBTUtils;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

@Mixin(value = BlockMekanism.class, remap = false)
public class BlockMekanismMixin {

    @Inject(method = "getDrops", at = @At("HEAD"), cancellable = true, remap = true)
    private void astral_mekanism$getDropsInject(BlockState state, LootParams.Builder builder,
            CallbackInfoReturnable<List<ItemStack>> cir) {
        ResourceLocation resourcelocation = ((BlockMekanism) (Object) this).getLootTable();
        if (resourcelocation == BuiltInLootTables.EMPTY) {
            cir.setReturnValue(Collections.emptyList());
            cir.cancel();
            return;
        } else {
            LootParams lootparams = builder.withParameter(LootContextParams.BLOCK_STATE, state)
                    .create(LootContextParamSets.BLOCK);
            ServerLevel serverlevel = lootparams.getLevel();
            LootTable loottable = serverlevel.getServer().getLootData().getLootTable(resourcelocation);
            List<ItemStack> result = loottable.getRandomItems(lootparams);
            for (ItemStack stack : result) {
                NBTUtils.setCompoundIfPresent(ItemDataUtils.getDataMap(stack), NBTConstants.COMPONENT_UPGRADE,
                        upgradeNBT -> {
                            Map<Upgrade, Integer> upgrades = Upgrade.buildMap(upgradeNBT);
                            ListTag gasTankList = ItemDataUtils.getList(stack, NBTConstants.GAS_TANKS);
                            if (!upgrades.containsKey(AMEUpgrade.RADIOACTIVE_SEALING.getValue())
                                    && !gasTankList.isEmpty()) {

                                int count = DataHandlerUtils.getMaxId(gasTankList, NBTConstants.TANK);
                                List<IGasTank> tanks = new ArrayList<>(count);
                                for (int i = 0; i < count; i++) {
                                    tanks.add(ChemicalTankBuilder.GAS.createDummy(Long.MAX_VALUE));
                                }
                                DataHandlerUtils.readContainers(tanks, gasTankList);
                                boolean hasRadioactive = false;
                                for (IGasTank tank : tanks) {
                                    if (!tank.isEmpty() && tank.getStack().has(GasAttributes.Radiation.class)) {
                                        hasRadioactive = true;
                                        tank.setEmpty();
                                    }
                                }
                                if (hasRadioactive) {
                                    ListTag newGasTankList = DataHandlerUtils.writeContainers(tanks);
                                    ItemDataUtils.setListOrRemove(stack, NBTConstants.GAS_TANKS, newGasTankList);
                                }
                            }
                        });
            }
            cir.setReturnValue(result);
            cir.cancel();
            return;
        }
    }
}
