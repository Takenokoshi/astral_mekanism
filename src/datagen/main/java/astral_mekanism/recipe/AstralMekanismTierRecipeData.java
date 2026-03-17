package astral_mekanism.recipe;

import com.jerry.mekanism_extras.common.registry.ExtraItem;

import appeng.core.definitions.AEItems;
import astral_mekanism.AMETier;
import astral_mekanism.registries.AstralMekanismItems;
import fr.iglee42.evolvedmekanism.registries.EMItems;
import gripe._90.megacells.definition.MEGAItems;
import io.github.masyumero.emextras.common.registry.EMExtrasItem;
import mekanism.common.registries.MekanismItems;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.pedroksl.advanced_ae.common.definitions.AAEItems;

public enum AstralMekanismTierRecipeData {
    ESSENTIAL_TO_BASIC(
            AMETier.ESSENTIAL,
            AMETier.BASIC,
            MekanismItems.BRONZE_INGOT,
            AstralMekanismItems.CONVERGENT_ALLOY,
            AstralMekanismItems.RESONANCE_CONTROL_CIRCUIT,
            Items.IRON_INGOT,
            MekanismItems.BASIC_CONTROL_CIRCUIT,
            AEItems.CALCULATION_PROCESSOR),
    BASIC_TO_ADVANCED(
            AMETier.BASIC,
            AMETier.ADVANCED,
            EMItems.REFINED_REDSTONE_INGOT,
            MekanismItems.INFUSED_ALLOY,
            MekanismItems.ADVANCED_CONTROL_CIRCUIT,
            AEItems.ENGINEERING_PROCESSOR),
    ADVANCED_TO_ELITE(
            AMETier.ADVANCED,
            AMETier.ELITE,
            Items.GOLD_INGOT,
            MekanismItems.REINFORCED_ALLOY,
            MekanismItems.ELITE_CONTROL_CIRCUIT,
            MEGAItems.ACCUMULATION_PROCESSOR),
    ELITE_TO_ULTIMATE(
            AMETier.ELITE,
            AMETier.ULTIMATE,
            AEItems.CERTUS_QUARTZ_CRYSTAL_CHARGED,
            AstralMekanismItems.ENCHANTED_ALLOY,
            AstralMekanismItems.ENHANCED_CONTROL_CIRCUIT,
            MekanismItems.ATOMIC_ALLOY,
            MekanismItems.ULTIMATE_CONTROL_CIRCUIT,
            AstralMekanismItems.PHOTON_PROCESSOR),
    ULTIMATE_TO_ABSOLUTE(
            AMETier.ULTIMATE,
            AMETier.ABSOLUTE,
            Items.GLOWSTONE,
            EMItems.HYPERCHARGED_ALLOY,
            ExtraItem.RADIANCE_ALLOY,
            EMExtrasItem.ABSOLUTE_OVERCLOCKED_CONTROL_CIRCUIT,
            AAEItems.QUANTUM_PROCESSOR),
    ABSOLUTE_TO_SUPREME(
            AMETier.ABSOLUTE,
            AMETier.SUPREME,
            Items.SHULKER_SHELL,
            EMItems.SUBATOMIC_ALLOY,
            ExtraItem.THERMONUCLEAR_ALLOY,
            EMExtrasItem.SUPREME_QUANTUM_CONTROL_CIRCUIT,
            AstralMekanismItems.COMPOSITE_PROCESSOR),
    SUPREME_TO_COSMIC(
            AMETier.SUPREME,
            AMETier.COSMIC,
            MekanismItems.REFINED_OBSIDIAN_INGOT,
            EMItems.SINGULAR_ALLOY,
            ExtraItem.SHINING_ALLOY,
            EMExtrasItem.COSMIC_DENSE_CONTROL_CIRCUIT,
            AstralMekanismItems.ORIGIN_PROCESSOR),
    COSMIC_TO_INFINITE(
            AMETier.COSMIC,
            AMETier.INFINITE,
            Items.NETHERITE_INGOT,
            EMItems.EXOVERSAL_ALLOY,
            ExtraItem.SPECTRUM_ALLOY,
            EMExtrasItem.INFINITE_MULTIVERSAL_CONTROL_CIRCUIT,
            AstralMekanismItems.AUTONOMY_PROCESSOR),
    INFINITE_TO_ASTRAL(
            AMETier.INFINITE,
            AMETier.ASTRAL,
            MekanismItems.POLONIUM_PELLET,
            AstralMekanismItems.STARDUST_ALLOY,
            AstralMekanismItems.ILLUSION_CONTROL_CIRCUIT,
            AstralMekanismItems.FIRMAMENT_PROCESSOR),
            ;

    private AstralMekanismTierRecipeData(
            AMETier beforeTier,
            AMETier afterTier,
            ItemLike centerItem,
            ItemLike leftAlloy,
            ItemLike leftCircuit,
            ItemLike rightAlloy,
            ItemLike rightCircuit,
            ItemLike processor) {
        this.beforeTier = beforeTier;
        this.afterTier = afterTier;
        this.centerItem = centerItem;
        this.leftAlloy = leftAlloy;
        this.leftCircuit = leftCircuit;
        this.rightAlloy = rightAlloy;
        this.rightCircuit = rightCircuit;
        this.processor = processor;
    }

    private AstralMekanismTierRecipeData(
            AMETier beforeTier,
            AMETier afterTier,
            ItemLike centerItem,
            ItemLike alloy,
            ItemLike circuit,
            ItemLike processor) {
        this(beforeTier, afterTier, centerItem, alloy, circuit, alloy, circuit, processor);
    }

    private AstralMekanismTierRecipeData(
            AMETier beforeTier,
            AMETier afterTier,
            ItemLike centerItem,
            ItemLike leftAlloy,
            ItemLike rightAlloy,
            ItemLike circuit,
            ItemLike processor) {
        this(beforeTier, afterTier, centerItem, leftAlloy, circuit, rightAlloy, circuit, processor);
    }

    public final AMETier beforeTier;
    public final AMETier afterTier;
    public final ItemLike centerItem;
    public final ItemLike leftAlloy;
    public final ItemLike leftCircuit;
    public final ItemLike rightAlloy;
    public final ItemLike rightCircuit;
    public final ItemLike processor;
}
