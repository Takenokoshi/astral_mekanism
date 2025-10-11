package astral_mekanism.items;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.gear.IModule;
import mekanism.api.math.FloatingLong;
import mekanism.common.config.MekanismConfig;
import mekanism.common.content.gear.mekatool.ModuleAttackAmplificationUnit;
import mekanism.common.item.gear.ItemMekaTool;
import mekanism.common.registries.MekanismModules;
import mekanism.common.util.StorageUtils;
import mekanism.tools.common.registries.ToolsItems;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class ItemAstralMekaTool extends ItemMekaTool {

    private static final ToolAction PAXEL_DIG = ToolAction.get("paxel_dig");
    private static final Set<ToolAction> PAXEL_ACTIONS = Collections.newSetFromMap(new IdentityHashMap<>());

    static {
        PAXEL_ACTIONS.add(PAXEL_DIG);
        PAXEL_ACTIONS.addAll(ToolActions.DEFAULT_PICKAXE_ACTIONS);
        PAXEL_ACTIONS.addAll(ToolActions.DEFAULT_SHOVEL_ACTIONS);
        PAXEL_ACTIONS.addAll(ToolActions.DEFAULT_AXE_ACTIONS);
    }

    public ItemAstralMekaTool(Properties properties) {
        super(properties);
    }

    private static boolean isEnchantEnabled(Enchantment enchantment) {
        if (enchantment == Enchantments.MENDING) {
            return false;
        }
        return enchantment.canEnchant(new ItemStack(Items.DIAMOND_SWORD))
                || enchantment.canEnchant(new ItemStack(Items.DIAMOND_AXE))
                || enchantment.canEnchant(new ItemStack(Items.DIAMOND_PICKAXE))
                || enchantment.canEnchant(new ItemStack(Items.DIAMOND_SHOVEL))
                || enchantment.canEnchant(new ItemStack(Items.DIAMOND_HOE));
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        boolean result = true;
        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(book);
        for (Enchantment enchantment : enchants.keySet()) {
            result &= isEnchantEnabled(enchantment);
        }
        return result;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return isEnchantEnabled(enchantment);
    }

    @Override
    public int getEnchantmentValue() {
        return 45;
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction action) {
        if (PAXEL_ACTIONS.contains(action)) {
            return this.hasEnergyForDigAction(stack);
        }
        return super.canPerformAction(stack, action);
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult paxelResult = ToolsItems.REFINED_OBSIDIAN_PAXEL.getItemStack().useOn(context);
        return paxelResult == InteractionResult.PASS ? super.useOn(context) : paxelResult;
    }

    @Override
    public FloatingLong getDestroyEnergy(ItemStack itemStack, float hardness, boolean silk) {
        return super.getDestroyEnergy(itemStack, hardness, silk)
                .multiply(1f / (1f + this.getEnchantmentLevel(itemStack, Enchantments.UNBREAKING)));
    }

    @Override
    public boolean hurtEnemy(@NotNull ItemStack stack, @NotNull LivingEntity target, @NotNull LivingEntity attacker) {
        IModule<ModuleAttackAmplificationUnit> attackAmplificationUnit = getModule(stack,
                MekanismModules.ATTACK_AMPLIFICATION_UNIT);
        if (attackAmplificationUnit != null && attackAmplificationUnit.isEnabled()) {
            // Note: We only have an energy cost if the damage is above base, so we can skip
            // all those checks
            // if we don't have an enabled attack amplification unit
            int unitDamage = attackAmplificationUnit.getCustomInstance().getDamage();
            if (unitDamage > 0) {
                IEnergyContainer energyContainer = StorageUtils.getEnergyContainer(stack, 0);
                if (energyContainer != null && !energyContainer.isEmpty()) {
                    // Try to extract full energy, even if we have a lower damage amount this is
                    // fine as that just means
                    // we don't have enough energy, but we will remove as much as we can, which is
                    // how much corresponds
                    // to the amount of damage we will actually do
                    energyContainer
                            .extract(
                                    MekanismConfig.gear.mekaToolEnergyUsageWeapon.get().multiply(unitDamage / 4D)
                                            .multiply(1f
                                                    / (1f + this.getEnchantmentLevel(stack, Enchantments.UNBREAKING))),
                                    Action.EXECUTE, AutomationType.MANUAL);
                }
            }
        }
        return true;
    }

}
