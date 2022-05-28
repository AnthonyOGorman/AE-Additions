package com.the9grounds.aeadditions.data.provider

import appeng.core.definitions.AEBlocks
import appeng.core.definitions.AEItems
import com.the9grounds.aeadditions.AEAdditions
import com.the9grounds.aeadditions.integration.Mods
import com.the9grounds.aeadditions.item.storage.DiskCellWithoutMod
import com.the9grounds.aeadditions.item.storage.StorageCell
import com.the9grounds.aeadditions.registries.Blocks
import com.the9grounds.aeadditions.registries.Items
import io.github.projectet.ae2things.item.AETItems
import me.ramidzkh.mekae2.AMItems
import net.minecraft.data.DataGenerator
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.data.recipes.ShapelessRecipeBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.minecraftforge.common.Tags
import net.minecraftforge.common.crafting.ConditionalRecipe
import net.minecraftforge.common.crafting.conditions.IConditionBuilder
import java.util.function.Consumer

class AEAdditionsRecipeProvider(generatorIn: DataGenerator) : RecipeProvider(generatorIn), IConditionBuilder {
    
    val osmiumTag = ItemTags.create(ResourceLocation("forge", "ingots/osmium"))
    val infusedAlloy = ItemTags.create(ResourceLocation("mekanism", "alloys/infused"))
    val reinforcedAlloy = ItemTags.create(ResourceLocation("mekanism", "alloys/reinforced"))
    val atomicAlloy = ItemTags.create(ResourceLocation("mekanism", "alloys/atomic"))
    val osmiumDust = ItemTags.create(ResourceLocation("forge", "dusts/osmium"))
    
    override fun buildCraftingRecipes(consumer: Consumer<FinishedRecipe>) {
        val components = mapOf(
            1 to AEItems.CELL_COMPONENT_1K,
            4 to AEItems.CELL_COMPONENT_4K,
            16 to AEItems.CELL_COMPONENT_16K,
            64 to AEItems.CELL_COMPONENT_64K,
            256 to AEItems.CELL_COMPONENT_256K,
            1024 to Items.CELL_COMPONENT_1024k,
            4096 to Items.CELL_COMPONENT_4096k,
            16384 to Items.CELL_COMPONENT_16384k,
            65536 to Items.CELL_COMPONENT_65536k
        )
        val itemKbs = mapOf<Int, StorageCell>(
            1024 to Items.ITEM_STORAGE_CELL_1024k,
            4096 to Items.ITEM_STORAGE_CELL_4096k, 
            16384 to Items.ITEM_STORAGE_CELL_16384k,
            65536 to Items.ITEM_STORAGE_CELL_65536k
        )
        val fluidKbs = mapOf<Int, StorageCell>(
            1024 to Items.FLUID_STORAGE_CELL_1024k,
            4096 to Items.FLUID_STORAGE_CELL_4096k,
            16384 to Items.FLUID_STORAGE_CELL_16384k
        )
        val chemicalKbs = mapOf<Int, StorageCell>(
            1024 to Items.CHEMICAL_STORAGE_CELL_1024k as StorageCell,
            4096 to Items.CHEMICAL_STORAGE_CELL_4096k as StorageCell,
            16384 to Items.CHEMICAL_STORAGE_CELL_16384k as StorageCell
        )
        val mapOfRequired = mapOf(
            4 to 1,
            16 to 4,
            64 to 16,
            256 to 64,
            1024 to 256,
            4096 to 1024,
            16384 to 4096,
            65536 to 16384
        )
        val craftingStorageBlocks = mapOf(
            1024 to Blocks.BLOCK_CRAFTING_STORAGE_1024k,
            4096 to Blocks.BLOCK_CRAFTING_STORAGE_4096k,
            16384 to Blocks.BLOCK_CRAFTING_STORAGE_16384k,
            65536 to Blocks.BLOCK_CRAFTING_STORAGE_65536k,
        )
        val diskCells = mapOf(
            256 to Items.DISK_256k,
            1024 to Items.DISK_1024k,
            4096 to Items.DISK_4096k,
            16384 to Items.DISK_16384k,
            65536 to Items.DISK_65536k
        )
        val fluidCells = mapOf(
            1 to Items.DISK_FLUID_1k,
            4 to Items.DISK_FLUID_4k,
            16 to Items.DISK_FLUID_16k,
            64 to Items.DISK_FLUID_64k,
            256 to Items.DISK_FLUID_256k,
            1024 to Items.DISK_FLUID_1024k,
            4096 to Items.DISK_FLUID_4096k,
            16384 to Items.DISK_FLUID_16384k,
            65536 to Items.DISK_FLUID_65536k
        )
        val chemicalCells = mapOf(
            1 to Items.DISK_CHEMICAL_1k,
            4 to Items.DISK_CHEMICAL_4k,
            16 to Items.DISK_CHEMICAL_16k,
            64 to Items.DISK_CHEMICAL_64k,
            256 to Items.DISK_CHEMICAL_256k,
            1024 to Items.DISK_CHEMICAL_1024k,
            4096 to Items.DISK_CHEMICAL_4096k,
            16384 to Items.DISK_CHEMICAL_16384k,
            65536 to Items.DISK_CHEMICAL_65536k
        )
        
        for (fluidCell in fluidCells) {
            ConditionalRecipe.builder().addCondition(
                modLoaded(Mods.AE2THINGS.modId)
            ).addRecipe {
                ShapelessRecipeBuilder.shapeless(fluidCell.value)
                    .requires(Items.DISK_FLUID_HOUSING)
                    .requires(components[fluidCell.key])
                    .unlockedBy("has_item", has(components[fluidCell.key]))
                    .save(it)
            }.build(consumer, ResourceLocation(AEAdditions.ID, "cells/fluid/disk-${fluidCell.key}-casing"))
            ConditionalRecipe.builder().addCondition(
                modLoaded(Mods.AE2THINGS.modId)
            ).addRecipe {
                ShapedRecipeBuilder.shaped(fluidCell.value)
                    .pattern("aba")
                    .pattern("bcb")
                    .pattern("ded")
                    .define('a', AEBlocks.QUARTZ_GLASS)
                    .define('b', Tags.Items.DUSTS_REDSTONE)
                    .define('c', components[fluidCell.key])
                    .define('d', Tags.Items.INGOTS_NETHERITE)
                    .define('e', Tags.Items.GEMS_LAPIS)
                    .unlockedBy("has_item", has(components[fluidCell.key]))
                    .save(it)
            }.build(consumer, ResourceLocation(AEAdditions.ID, "cells/fluid/disk-${fluidCell.key}"))
        }

        for (chemicalCell in chemicalCells) {
            when(chemicalCell.key) {
                1, 4, 16 -> registerChemicalDiskRecipe(chemicalCell, infusedAlloy, components, consumer)
                64, 256, 1024 -> registerChemicalDiskRecipe(chemicalCell, reinforcedAlloy, components, consumer)
                4096, 16384, 65536 -> registerChemicalDiskRecipe(chemicalCell, atomicAlloy, components, consumer)
            }
        }

        ConditionalRecipe.builder().addCondition(
            and(
                modLoaded(Mods.AE2THINGS.modId),
                modLoaded(Mods.APPMEK.modId)
            )
        ).addRecipe {
            ShapedRecipeBuilder.shaped(Items.DISK_CHEMICAL_HOUSING)
                .pattern("aba")
                .pattern("b b")
                .pattern("ded")
                .define('a', AEBlocks.QUARTZ_GLASS)
                .define('b', Tags.Items.DUSTS_REDSTONE)
                .define('d', Tags.Items.INGOTS_NETHERITE)
                .define('e', osmiumDust)
                .unlockedBy("has_item", has(Tags.Items.INGOTS_NETHERITE))
                .save(it)
        }.build(consumer, ResourceLocation(AEAdditions.ID, "cells/chemical/disk-housing"))

        ConditionalRecipe.builder().addCondition(
            modLoaded(Mods.AE2THINGS.modId)
        ).addRecipe {
            ShapedRecipeBuilder.shaped(Items.DISK_FLUID_HOUSING)
                .pattern("aba")
                .pattern("b b")
                .pattern("ded")
                .define('a', AEBlocks.QUARTZ_GLASS)
                .define('b', Tags.Items.DUSTS_REDSTONE)
                .define('d', Tags.Items.INGOTS_NETHERITE)
                .define('e', Tags.Items.GEMS_LAPIS)
                .unlockedBy("has_item", has(Tags.Items.INGOTS_NETHERITE))
                .save(it)
        }.build(consumer, ResourceLocation(AEAdditions.ID, "cells/fluid/disk-housing"))
        
        for (diskCell in diskCells) {
            ConditionalRecipe.builder().addCondition(
                modLoaded(Mods.AE2THINGS.modId)
            ).addRecipe {
                ShapelessRecipeBuilder.shapeless(diskCell.value)
                    .requires(AETItems.DISK_HOUSING.get())
                    .requires(components[diskCell.key])
                    .unlockedBy("has_item", has(components[diskCell.key]))
                    .save(it)
            }.build(consumer, ResourceLocation(AEAdditions.ID, "cells/item/disk-${diskCell.key}-casing"))
            
            ConditionalRecipe.builder().addCondition(
                modLoaded(Mods.AE2THINGS.modId)
            ).addRecipe {
                ShapedRecipeBuilder.shaped(diskCell.value)
                    .pattern("aba")
                    .pattern("bcb")
                    .pattern("ded")
                    .define('a', AEBlocks.QUARTZ_GLASS)
                    .define('b', Tags.Items.DUSTS_REDSTONE)
                    .define('c', components[diskCell.key])
                    .define('d', Tags.Items.INGOTS_NETHERITE)
                    .define('e', Tags.Items.GEMS_AMETHYST)
                    .unlockedBy("has_item", has(components[diskCell.key]))
                    .save(it)
            }.build(consumer, ResourceLocation(AEAdditions.ID, "cells/item/disk-${diskCell.key}"))
        }
        
        for (block in craftingStorageBlocks) {
            ShapelessRecipeBuilder.shapeless(block.value.item)
                .requires(AEBlocks.CRAFTING_UNIT)
                .requires(components[block.key])
                .unlockedBy("has_item", has(components[block.key]))
                .save(consumer, ResourceLocation(AEAdditions.ID, "crafting/${block.key}k_storage"))
        }
        
        for (component in listOf(1024, 4096, 16384, 65536)) {
            ShapedRecipeBuilder.shaped(components[component])
                .pattern("aba")
                .pattern("cdc")
                .pattern("aca")
                .define('a', Tags.Items.GEMS_DIAMOND)
                .define('b', AEItems.ENGINEERING_PROCESSOR)
                .define('c', components[mapOfRequired[component]])
                .define('d', AEBlocks.QUARTZ_GLASS)
                .unlockedBy("has_item", has(components[mapOfRequired[component]]))
                .save(consumer, ResourceLocation(AEAdditions.ID, "components/${component}k"))
        }
        
        for (kb in itemKbs) {
            ShapelessRecipeBuilder.shapeless(kb.value)
                .requires(AEItems.ITEM_CELL_HOUSING)
                .requires(kb.value.component)
                .unlockedBy("has_item", has(kb.value.component))
                .save(consumer, ResourceLocation(AEAdditions.ID, "cells/item/casing-${kb.key}k"))

            ShapedRecipeBuilder.shaped(kb.value)
                .pattern("aba")
                .pattern("bcb")
                .pattern("ddd")
                .define('a', AEBlocks.QUARTZ_GLASS)
                .define('b', AEItems.FLUIX_DUST)
                .define('c', kb.value.component)
                .define('d', Tags.Items.GEMS_DIAMOND)
                .unlockedBy("has_item", has(kb.value.component))
                .save(consumer, ResourceLocation(AEAdditions.ID, "cells/item/${kb.key}k"))
        }
        for (kb in fluidKbs) {
            ShapelessRecipeBuilder.shapeless(kb.value)
                .requires(AEItems.FLUID_CELL_HOUSING)
                .requires(kb.value.component)
                .unlockedBy("has_item", has(kb.value.component))
                .save(consumer, ResourceLocation(AEAdditions.ID, "cells/fluid/casing-${kb.key}k"))

            ShapedRecipeBuilder.shaped(kb.value)
                .pattern("aba")
                .pattern("bcb")
                .pattern("ddd")
                .define('a', AEBlocks.QUARTZ_GLASS)
                .define('b', AEItems.FLUIX_DUST)
                .define('c', kb.value.component)
                .define('d', Tags.Items.INGOTS_COPPER)
                .unlockedBy("has_item", has(kb.value.component))
                .save(consumer, ResourceLocation(AEAdditions.ID, "cells/fluid/${kb.key}k"))
        }
        for (kb in chemicalKbs) {
            addConditionalRecipeForMekanism("cells/chemical/casing-${kb.key}k", consumer) {
                ShapelessRecipeBuilder.shapeless(kb.value)
                    .requires(AMItems.CHEMICAL_CELL_HOUSING.get())
                    .requires(kb.value.component)
                    .unlockedBy("has_item", has(kb.value.component))
                    .save(it)
            }
            addConditionalRecipeForMekanism("cells/chemical/${kb.key}k", consumer) {
                ShapedRecipeBuilder.shaped(kb.value)
                    .pattern("aba")
                    .pattern("bcb")
                    .pattern("ddd")
                    .define('a', AEBlocks.QUARTZ_GLASS)
                    .define('b', AEItems.FLUIX_DUST)
                    .define('c', kb.value.component)
                    .define('d', osmiumTag)
                    .unlockedBy("has_item", has(kb.value.component))
                    .save(it)
            }
        }
    }

    private fun registerChemicalDiskRecipe(
        chemicalCell: Map.Entry<Int, DiskCellWithoutMod>,
        alloy: TagKey<Item>,
        components: Map<Int, ItemLike>,
        consumer: Consumer<FinishedRecipe>
    ) {
        ConditionalRecipe.builder().addCondition(
            and(
                modLoaded(Mods.AE2THINGS.modId),
                modLoaded(Mods.APPMEK.modId)
            )
        ).addRecipe {
            ShapelessRecipeBuilder.shapeless(chemicalCell.value)
                .requires(Items.DISK_CHEMICAL_HOUSING)
                .requires(components[chemicalCell.key])
                .requires(alloy)
                .unlockedBy("has_item", has(components[chemicalCell.key]))
                .save(it)
        }.build(consumer, ResourceLocation(AEAdditions.ID, "cells/chemical/disk-${chemicalCell.key}-casing"))
        ConditionalRecipe.builder().addCondition(
            modLoaded(Mods.AE2THINGS.modId)
        ).addRecipe {
            ShapedRecipeBuilder.shaped(chemicalCell.value)
                .pattern("aba")
                .pattern("bcb")
                .pattern("ded")
                .define('a', AEBlocks.QUARTZ_GLASS)
                .define('b', Tags.Items.DUSTS_REDSTONE)
                .define('c', components[chemicalCell.key])
                .define('d', Tags.Items.INGOTS_NETHERITE)
                .define('e', alloy)
                .unlockedBy("has_item", has(components[chemicalCell.key]))
                .save(it)
        }.build(consumer, ResourceLocation(AEAdditions.ID, "cells/chemical/disk-${chemicalCell.key}"))
    }

    private fun addConditionalRecipeForMekanism(resourcePath: String, consumer: Consumer<FinishedRecipe>, factory: (Consumer<FinishedRecipe>) -> Unit) {
        ConditionalRecipe.builder().addCondition(
            modLoaded("appmek")
        ).addRecipe() {
            factory(it)
        }.build(consumer, ResourceLocation(AEAdditions.ID, resourcePath))
    }
}