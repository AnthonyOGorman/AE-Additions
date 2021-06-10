package extracells.registries;

import java.util.function.Function;

import appeng.block.crafting.BlockCraftingUnit;
import extracells.block.*;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.text.translation.I18n;

import extracells.integration.Integration;
import extracells.item.block.ItemBlockCertusTank;
import extracells.item.block.ItemBlockFluidFiller;
import extracells.item.block.ItemBlockFluidInterface;
import extracells.util.CreativeTabEC;

public enum BlockEnum {
	CERTUSTANK("certustank", new BlockCertusTank(), ItemBlockCertusTank::new),
	FLUIDCRAFTER("fluidcrafter", new BlockFluidCrafter()),
	FILLER("fluidfiller", new BlockFluidFiller(), ItemBlockFluidFiller::new),
	BLASTRESISTANTMEDRIVE("hardmedrive", new BlockHardMEDrive()),
	VIBRANTCHAMBERFLUID("vibrantchamberfluid", new BlockVibrationChamberFluid()),
	UPGRADEDCRAFTINGSTORAGE256("crafting_storage_256", new BlockCraftingStorage(BlockCraftingUnit.CraftingUnitType.STORAGE_1K)),
	UPGRADEDCRAFTINGSTORAGE1024("crafting_storage_1024", new BlockCraftingStorage(BlockCraftingUnit.CraftingUnitType.STORAGE_4K)),
	UPGRADEDCRAFTINGSTORAGE4096("crafting_storage_4096", new BlockCraftingStorage(BlockCraftingUnit.CraftingUnitType.STORAGE_16K)),
	UPGRADEDCRAFTINGSTORAGE16384("crafting_storage_16384", new BlockCraftingStorage(BlockCraftingUnit.CraftingUnitType.STORAGE_64K));

	private final String internalName;
	private Block block;
	private ItemBlock item;
	private Integration.Mods mod;

	BlockEnum(String internalName, Block block, Integration.Mods mod) {
		this(internalName, block, ItemBlock::new, mod);
	}

	BlockEnum(String internalName, Block block) {
		this(internalName, block, ItemBlock::new);
	}

	BlockEnum(String internalName, Block block, Function<Block, ItemBlock> itemFactory) {
		this(internalName, block, itemFactory, null);
	}

	BlockEnum(String internalName, Block block, Function<Block, ItemBlock> factory, Integration.Mods mod) {
		this.internalName = internalName;
		this.block = block;
		this.block.setTranslationKey("extracells.block." + this.internalName);
		this.block.setRegistryName(internalName);
		this.item = factory.apply(block);
		this.item.setRegistryName(block.getRegistryName());
		this.mod = mod;
		if (mod == null || mod.isEnabled()) {
			this.block.setCreativeTab(CreativeTabEC.INSTANCE);
		}
	}

	public Block getBlock() {
		return this.block;
	}

	public String getInternalName() {
		return this.internalName;
	}

	public ItemBlock getItem() {
		return item;
	}

	public String getStatName() {
		return I18n.translateToLocal(this.block.getTranslationKey() + ".name");
	}

	public Integration.Mods getMod() {
		return mod;
	}
}
