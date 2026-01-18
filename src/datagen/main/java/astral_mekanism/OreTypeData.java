package astral_mekanism;

import java.util.EnumMap;
import java.util.function.Function;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import com.jerry.mekanism_extras.MekanismExtras;
import com.jerry.mekanism_extras.common.registry.ExtraBlock;
import com.jerry.mekanism_extras.common.registry.ExtraItem;
import com.jerry.mekanism_extras.common.resource.ore.ExtraOreType;

import appeng.api.ids.AEConstants;
import appeng.core.definitions.AEBlocks;
import appeng.core.definitions.AEItems;
import astral_mekanism.registries.OreType;
import astral_mekanism.registries.AstralMekanismItems;
import astral_mekanism.registries.AstralMekanismItems.IntermediateState;
import mekanism.common.Mekanism;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.registries.MekanismItems;
import mekanism.common.resource.PrimaryResource;
import mekanism.common.resource.ResourceType;
import mekanism.common.tags.MekanismTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public enum OreTypeData {
    COAL(OreType.COAL, 1, new ItemStack(Items.COAL.asItem()), new ItemStack(Items.COAL_ORE.asItem()),
            new ResourceLocation("item/coal")),
    DIAMOND(OreType.DIAMOND, 1, new ItemStack(Items.DIAMOND.asItem()), new ItemStack(Items.DIAMOND_ORE.asItem()),
            new ResourceLocation("item/diamond")),
    EMERALD(OreType.EMERALD, 1, new ItemStack(Items.EMERALD.asItem()), new ItemStack(Items.EMERALD_ORE.asItem()),
            new ResourceLocation("item/emerald")),
    FLUORITE(OreType.FLUORITE, 3,
            MekanismItems.FLUORITE_GEM.getItemStack(),
            new ItemStack(MekanismBlocks.ORES.get(mekanism.common.resource.ore.OreType.FLUORITE).stoneBlock().asItem()),
            Mekanism.rl("item/fluorite_gem")),
    LAPIS_LAZULI(OreType.LAPIS_LAZULI, 6,
            new ItemStack(Items.LAPIS_LAZULI.asItem()),
            new ItemStack(Items.LAPIS_ORE.asItem()),
            new ResourceLocation("item/lapis_lazuli")),
    QUARTZ(OreType.QUARTZ, 1,
            new ItemStack(Items.QUARTZ.asItem()),
            new ItemStack(Items.NETHER_QUARTZ_ORE.asItem()),
            new ResourceLocation("item/quartz")),
    REDSTONE(OreType.REDSTONE, 6, new ItemStack(Items.REDSTONE.asItem()), new ItemStack(Items.REDSTONE_ORE.asItem()),
            new ResourceLocation("item/redstone")),
    CERTUS_QUARTZ(OreType.CERTUS_QUARTZ, 1, AEItems.CERTUS_QUARTZ_CRYSTAL.stack(),
            AEBlocks.QUARTZ_CLUSTER.stack(),
            new ResourceLocation(AEConstants.MOD_ID, "item/certus_quartz_crystal")),
    AMETHYST(OreType.AMETHYST, 24, new ItemStack(Items.AMETHYST_SHARD.asItem()),
            new ItemStack(Items.AMETHYST_CLUSTER.asItem()),
            new ResourceLocation("item/amethyst_shard")),
    IRON(OreType.IRON, 1, new ItemStack(Items.IRON_INGOT.asItem()), new ItemStack(Items.IRON_ORE.asItem()),
            new ResourceLocation("item/iron_ingot"),
            Mekanism::rl),
    GOLD(OreType.GOLD, 1, new ItemStack(Items.GOLD_INGOT.asItem()), new ItemStack(Items.GOLD_ORE.asItem()),
            new ResourceLocation("item/gold_ingot"),
            Mekanism::rl),
    COPPER(OreType.COPPER, 1, new ItemStack(Items.COPPER_INGOT.asItem()), new ItemStack(Items.COPPER_ORE.asItem()),
            new ResourceLocation("item/copper_ingot"),
            Mekanism::rl),
    TIN(OreType.TIN, 1,
            MekanismItems.PROCESSED_RESOURCES.column(PrimaryResource.TIN).get(ResourceType.INGOT).getItemStack(),
            new ItemStack(MekanismBlocks.ORES.get(mekanism.common.resource.ore.OreType.TIN).stoneBlock().asItem()),
            Mekanism.rl("ingot_tin"), Mekanism::rl),
    LEAD(OreType.LEAD, 1,
            MekanismItems.PROCESSED_RESOURCES.column(PrimaryResource.LEAD).get(ResourceType.INGOT).getItemStack(),
            new ItemStack(MekanismBlocks.ORES.get(mekanism.common.resource.ore.OreType.LEAD).stoneBlock().asItem()),
            Mekanism.rl("ingot_lead"),
            Mekanism::rl),
    URANIUM(OreType.URANIUM, 1,
            MekanismItems.PROCESSED_RESOURCES.column(PrimaryResource.URANIUM).get(ResourceType.INGOT).getItemStack(),
            new ItemStack(MekanismBlocks.ORES.get(mekanism.common.resource.ore.OreType.URANIUM).stoneBlock().asItem()),
            Mekanism.rl("ingot_uranium"),
            Mekanism::rl),
    OSMIUM(OreType.OSMIUM, 1,
            MekanismItems.PROCESSED_RESOURCES.column(PrimaryResource.OSMIUM).get(ResourceType.INGOT).getItemStack(),
            new ItemStack(MekanismBlocks.ORES.get(mekanism.common.resource.ore.OreType.OSMIUM).stoneBlock().asItem()),
            Mekanism.rl("ingot_osmium"),
            Mekanism::rl),
    NETHERITE(OreType.NETHERITE, 1,
            new ItemStack(Items.NETHERITE_INGOT.asItem()),
            AstralMekanismItems.NETHERITE_CLUSTER.getItemStack(),
            new ResourceLocation("item/netherite_ingot"),
            ((Supplier<EnumMap<IntermediateState, ResourceLocation>>) () -> {
                EnumMap<IntermediateState, ResourceLocation> map = new EnumMap<>(IntermediateState.class);
                map.put(IntermediateState.CLUMP, AstralMekanismID.rl("item/netherite/clump_netherite"));
                map.put(IntermediateState.CRUSHED, AstralMekanismID.rl("item/netherite/crushed_netherite"));
                map.put(IntermediateState.CRYSTAL, AstralMekanismID.rl("item/netherite/crystal_netherite"));
                map.put(IntermediateState.RAW, Mekanism.rl("item/dust_netherite"));
                map.put(IntermediateState.SHARD, AstralMekanismID.rl("item/netherite/shard_netherite"));
                return map;
            }).get()),
    NAQUADAH(OreType.NAQUADAH, 1,
            ExtraItem.INGOT_NAQUADAH.getItemStack(),
            ExtraBlock.ORES.get(ExtraOreType.NAQUADAH).stone().getItemStack(),
            MekanismExtras.rl("ingot_naquadah"),
            MekanismExtras::rl),
            ;

    private OreTypeData(OreType oreType,
            int recipeMultiply,
            ItemStack finalItem,
            ItemStack oreItem,
            ResourceLocation resultTexture,
            EnumMap<IntermediateState, ResourceLocation> processingTexture) {
        this.oreType = oreType;
        this.recipeMultiply = recipeMultiply;
        this.finalItem = finalItem.copyWithCount(64);
        this.oreItem = oreItem;
        this.resultTexture = resultTexture;
        this.processingTexture = processingTexture;
        this.processingTags = new EnumMap<>(IntermediateState.class);
        if (!oreType.hasMekprocessing) {
            processingTags.put(IntermediateState.CLUMP, ItemTags.create(Mekanism.rl("clumps/" + this.oreType.type)));
            processingTags.put(IntermediateState.CRUSHED,
                    ItemTags.create(Mekanism.rl("debris/" + this.oreType.type)));
            processingTags.put(IntermediateState.CRYSTAL,
                    ItemTags.create(Mekanism.rl("crystals/" + this.oreType.type)));
            processingTags.put(IntermediateState.RAW,
                    ItemTags.create(Mekanism.rl("raw/" + this.oreType.type)));
            processingTags.put(IntermediateState.SHARD, ItemTags.create(Mekanism.rl("shards/" + this.oreType.type)));
        } else if (this.oreType == OreType.NETHERITE) {
            processingTags.put(IntermediateState.CLUMP, ItemTags.create(Mekanism.rl("clumps/netherite")));
            processingTags.put(IntermediateState.CRUSHED, ItemTags.create(Mekanism.rl("dirty_dusts/netherite")));
            processingTags.put(IntermediateState.CRYSTAL, ItemTags.create(Mekanism.rl("crystals/netherite")));
            processingTags.put(IntermediateState.RAW, MekanismTags.Items.DUSTS_NETHERITE);
            processingTags.put(IntermediateState.SHARD, ItemTags.create(Mekanism.rl("shards/netherite")));
        }
    }

    private OreTypeData(OreType oreType,
            int recipeMultiply,
            ItemStack finalItem,
            ItemStack oreItem,
            ResourceLocation resultTexture) {
        this(oreType, recipeMultiply, finalItem, oreItem, resultTexture,
                ((Supplier<EnumMap<IntermediateState, ResourceLocation>>) () -> {
                    EnumMap<IntermediateState, ResourceLocation> map = new EnumMap<>(IntermediateState.class);
                    map.put(IntermediateState.CLUMP, AstralMekanismID.rl("item/clump_gem/clump_" + oreType.type));
                    map.put(IntermediateState.CRUSHED, AstralMekanismID.rl("item/crushed_gem/crushed_" + oreType.type));
                    map.put(IntermediateState.CRYSTAL, AstralMekanismID.rl("item/crystal_gem/crystal_" + oreType.type));
                    map.put(IntermediateState.RAW, AstralMekanismID.rl("item/raw_gem/raw_" + oreType.type));
                    map.put(IntermediateState.SHARD, AstralMekanismID.rl("item/shard_gem/shard_" + oreType.type));
                    return map;
                }).get());
    }

    private OreTypeData(OreType oreType,
            int recipeMultiply,
            ItemStack finalItem,
            ItemStack oreItem,
            ResourceLocation resultTexture,
            Function<String, ResourceLocation> locationBuilder) {
        this(oreType, recipeMultiply, finalItem, oreItem, resultTexture,
                ((Supplier<EnumMap<IntermediateState, ResourceLocation>>) () -> {
                    EnumMap<IntermediateState, ResourceLocation> map = new EnumMap<>(IntermediateState.class);
                    map.put(IntermediateState.CLUMP, locationBuilder.apply("item/clump_" + oreType.type));
                    map.put(IntermediateState.CRUSHED, locationBuilder.apply("item/dirty_dust_" + oreType.type));
                    map.put(IntermediateState.CRYSTAL, locationBuilder.apply("item/crystal_" + oreType.type));
                    map.put(IntermediateState.RAW, locationBuilder.apply("item/dust_" + oreType.type));
                    map.put(IntermediateState.SHARD, locationBuilder.apply("item/shard_" + oreType.type));
                    return map;
                }).get());
    }

    public final OreType oreType;
    public final int recipeMultiply;
    public final ItemStack finalItem;
    public final ItemStack oreItem;
    public final ResourceLocation resultTexture;
    public final EnumMap<IntermediateState, ResourceLocation> processingTexture;
    @Nullable
    public final EnumMap<IntermediateState, TagKey<Item>> processingTags;
}
