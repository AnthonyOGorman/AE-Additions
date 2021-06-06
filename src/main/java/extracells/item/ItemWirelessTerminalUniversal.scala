//package extracells.item
//
//
//import java.util
//
//import appeng.api.AEApi
//import appeng.api.config.Actionable
//import appeng.api.features.IWirelessTermHandler
//import appeng.api.util.IConfigManager
//import baubles.api.BaubleType
//import extracells.Constants
//import extracells.api.{ECApi, IWirelessFluidTermHandler, IWirelessGasTermHandler}
//import extracells.integration.Integration
//import extracells.integration.wct.WirelessCrafting
//import extracells.models.ModelManager
//import extracells.util.HandlerUniversalWirelessTerminal
//import extracells.wireless.ConfigManager
//import net.minecraft.client.renderer.block.model.ModelResourceLocation
//import net.minecraft.client.util.ITooltipFlag
//import net.minecraft.creativetab.CreativeTabs
//import net.minecraft.entity.player.EntityPlayer
//import net.minecraft.item.{Item, ItemStack}
//import net.minecraft.nbt.NBTTagCompound
//import net.minecraft.util.text.translation.I18n
//import net.minecraft.util.{ActionResult, EnumActionResult, EnumHand, NonNullList, ResourceLocation}
//import net.minecraft.world.World
//import net.minecraftforge.fml.common.Optional
//import net.minecraftforge.fml.relauncher.{Side, SideOnly}
//import p455w0rd.ae2wtlib.api.client.IBaubleRender
//import p455w0rd.wct.api.IWirelessCraftingTerminalItem
//
//@Optional.Interface(iface = "p455w0rd.wct.api.IWirelessCraftingTerminalItem", modid = "wct", striprefs = true)
//class ItemWirelessTerminalUniversal extends ItemECBase with WirelessTermBase with IWirelessFluidTermHandler with IWirelessGasTermHandler with IWirelessTermHandler /*with EssensiaTerminal*/ with IWirelessCraftingTerminalItem {
//  val isTeEnabled: Boolean = Integration.Mods.THAUMATICENERGISTICS.isEnabled
//  val isMekEnabled: Boolean = Integration.Mods.MEKANISMGAS.isEnabled
//  val isWcEnabled: Boolean = Integration.Mods.WIRELESSCRAFTING.isEnabled
//
//  private var holder: EntityPlayer = _
//
//  if (isWcEnabled) {
//    ECApi.instance.registerWirelessTermHandler(this)
//    AEApi.instance.registries.wireless.registerWirelessHandler(this)
//  } else {
//    ECApi.instance.registerWirelessTermHandler(HandlerUniversalWirelessTerminal)
//    AEApi.instance.registries.wireless.registerWirelessHandler(HandlerUniversalWirelessTerminal)
//  }
//
//
//  override def isItemNormalWirelessTermToo(is: ItemStack): Boolean = true
//
//  override def getConfigManager(itemStack: ItemStack): IConfigManager = {
//    val nbt = ensureTagCompound(itemStack)
//    if (!nbt.hasKey("settings"))
//      nbt.setTag("settings", new NBTTagCompound)
//    val tag = nbt.getCompoundTag("settings")
//    new ConfigManager(tag)
//  }
//
//  private def ensureTagCompound(itemStack: ItemStack): NBTTagCompound = {
//    if (!itemStack.hasTagCompound) itemStack.setTagCompound(new NBTTagCompound)
//    itemStack.getTagCompound
//  }
//
//  override def getUnlocalizedName(itemStack: ItemStack): String =
//    super.getUnlocalizedName(itemStack).replace("item.extracells", "extracells.item")
//
//  override def getItemStackDisplayName(stack: ItemStack): String = {
//    val tag = ensureTagCompound(stack)
//    if (!tag.hasKey("type"))
//      tag.setByte("type", 0)
//    super.getItemStackDisplayName(stack) + " - " +I18n.translateToLocal("extracells.tooltip." + WirelessTerminalType.values().apply(tag.getByte("type")).toString.toLowerCase)
//  }
//
//  override def onItemRightClick(world: World, entityPlayer: EntityPlayer, hand: EnumHand): ActionResult[ItemStack] = {
//    val itemStack = entityPlayer.getHeldItem(hand)
//    if (world.isRemote) {
//      if (entityPlayer.isSneaking)
//        return new ActionResult(EnumActionResult.SUCCESS, itemStack)
//      val tag = ensureTagCompound(itemStack)
//      if (!tag.hasKey("type"))
//        tag.setByte("type", 0)
//      if (tag.getByte("type") == 4 && isWcEnabled)
//        WirelessCrafting.openCraftingTerminal(entityPlayer, entityPlayer.inventory.currentItem)
//      return new ActionResult(EnumActionResult.SUCCESS, itemStack)
//    }
//
//    val tag = ensureTagCompound(itemStack)
//    if (!tag.hasKey("type"))
//      tag.setByte("type", 0)
//    if (entityPlayer.isSneaking)
//      return new ActionResult(EnumActionResult.SUCCESS, changeMode(itemStack, entityPlayer, tag))
//    val matchted = tag.getByte("type") match {
//      case 0 => AEApi.instance.registries.wireless.openWirelessTerminalGui(itemStack, world, entityPlayer)
//      case 1 => ECApi.instance.openWirelessFluidTerminal(entityPlayer, hand, world)
//      case 2 => ECApi.instance.openWirelessGasTerminal(entityPlayer, hand, world)
//      //case 3 => if(isTeEnabled) ThaumaticEnergistics.openEssentiaTerminal(entityPlayer, this)
//      case _ =>
//    }
//    new ActionResult(EnumActionResult.SUCCESS, itemStack)
//  }
//
//  def changeMode(itemStack: ItemStack, player: EntityPlayer, tag: NBTTagCompound): ItemStack = {
//    val installed = getInstalledModules(itemStack)
//    tag.getByte("type") match {
//      case 0 =>
//        if (installed.contains(WirelessTerminalType.FLUID))
//          tag.setByte("type", 1)
//        else if (isMekEnabled && installed.contains(WirelessTerminalType.GAS))
//          tag.setByte("type", 2)
//        else if (isTeEnabled && installed.contains(WirelessTerminalType.ESSENTIA))
//          tag.setByte("type", 3)
//        else if (isWcEnabled && installed.contains(WirelessTerminalType.CRAFTING))
//          tag.setByte("type", 4)
//      case 1 =>
//        if (isMekEnabled && installed.contains(WirelessTerminalType.GAS))
//          tag.setByte("type", 2)
//        else if (isTeEnabled && installed.contains(WirelessTerminalType.ESSENTIA))
//          tag.setByte("type", 3)
//        else if (isWcEnabled && installed.contains(WirelessTerminalType.CRAFTING))
//          tag.setByte("type", 4)
//        else if (installed.contains(WirelessTerminalType.ITEM))
//          tag.setByte("type", 0)
//      case 2 =>
//        if (isTeEnabled && installed.contains(WirelessTerminalType.ESSENTIA))
//          tag.setByte("type", 3)
//        else if (isWcEnabled && installed.contains(WirelessTerminalType.CRAFTING))
//          tag.setByte("type", 4)
//        else if (installed.contains(WirelessTerminalType.ITEM))
//          tag.setByte("type", 0)
//        else if (installed.contains(WirelessTerminalType.FLUID))
//          tag.setByte("type", 1)
//      case 3 =>
//        if (isWcEnabled && installed.contains(WirelessTerminalType.CRAFTING))
//          tag.setByte("type", 4)
//        else if (installed.contains(WirelessTerminalType.ITEM))
//          tag.setByte("type", 0)
//        else if (installed.contains(WirelessTerminalType.FLUID))
//          tag.setByte("type", 1)
//        else if (isMekEnabled && installed.contains(WirelessTerminalType.GAS))
//          tag.setByte("type", 2)
//      case _ =>
//        if (installed.contains(WirelessTerminalType.ITEM))
//          tag.setByte("type", 0)
//        else if (installed.contains(WirelessTerminalType.FLUID))
//          tag.setByte("type", 1)
//        else if (isMekEnabled && installed.contains(WirelessTerminalType.GAS))
//          tag.setByte("type", 2)
//        else if (isTeEnabled && installed.contains(WirelessTerminalType.ESSENTIA))
//          tag.setByte("type", 3)
//        else if (isWcEnabled && installed.contains(WirelessTerminalType.CRAFTING))
//          tag.setByte("type", 4)
//        else
//          tag.setByte("type", 0)
//    }
//    itemStack
//  }
//
//  @SideOnly(Side.CLIENT)
//  override def registerModel(item: Item, manager: ModelManager): Unit =
//    manager.registerItemModel(item, 0, "terminals/universal_wireless")
//
//
//  @SideOnly(Side.CLIENT)
//  override def addInformation(itemStack: ItemStack, world: World, list: util.List[String], advanced: ITooltipFlag) {
//    val tag = ensureTagCompound(itemStack)
//    if (!tag.hasKey("type"))
//      tag.setByte("type", 0)
//    val list2 = list.asInstanceOf[util.List[String]];
//    list2.add(I18n.translateToLocal("extracells.tooltip.mode") + ": " + I18n.translateToLocal("extracells.tooltip." + WirelessTerminalType.values().apply(tag.getByte("type")).toString.toLowerCase))
//    list2.add(I18n.translateToLocal("extracells.tooltip.installed"))
//    val it = getInstalledModules(itemStack).iterator
//    while (it.hasNext)
//      list2.add("- " + I18n.translateToLocal("extracells.tooltip." + it.next.name.toLowerCase))
//    super.addInformation(itemStack, world, list, advanced);
//  }
//
//  def installModule(itemStack: ItemStack, module: WirelessTerminalType): Unit = {
//    if (isInstalled(itemStack, module))
//      return
//    val install = (1 << module.ordinal).toByte
//    val tag = ensureTagCompound(itemStack)
//    val installed: Byte = {
//      if (tag.hasKey("modules"))
//        (tag.getByte("modules") + install).toByte
//      else
//        install
//    }
//    tag.setByte("modules", installed)
//  }
//
//  def getInstalledModules(itemStack: ItemStack): util.EnumSet[WirelessTerminalType] = {
//    if (itemStack == null || itemStack.getItem == null)
//      return util.EnumSet.noneOf(classOf[WirelessTerminalType])
//    val tag = ensureTagCompound(itemStack)
//    val installed: Byte = {
//      if (tag.hasKey("modules"))
//        tag.getByte("modules")
//      else
//        0
//    }
//    val set = util.EnumSet.noneOf(classOf[WirelessTerminalType])
//    for (x <- WirelessTerminalType.values) {
//      if (1 == (installed >> x.ordinal) % 2)
//        set.add(x)
//    }
//    set
//  }
//
//  def isInstalled(itemStack: ItemStack, module: WirelessTerminalType): Boolean = {
//    if (itemStack == null || itemStack.getItem == null)
//      return false
//    val tag = ensureTagCompound(itemStack)
//    val installed: Byte = {
//      if (tag.hasKey("modules"))
//        tag.getByte("modules")
//      else
//        0
//    }
//    if (1 == (installed >> module.ordinal) % 2)
//      true
//    else
//      false
//
//  }
//
//
//  @SuppressWarnings(Array("unchecked", "rawtypes"))
//  override def getSubItems(creativeTab: CreativeTabs, itemList: NonNullList[ItemStack]) {
//    if (!this.isInCreativeTab(creativeTab)) return
//    val itemList2 = itemList.asInstanceOf[util.List[ItemStack]]
//    val tag = new NBTTagCompound
//    tag.setByte("modules", 23 /*No Essentia Terminal; 31 for All Terminals*/)
//    val itemStack: ItemStack = new ItemStack(this)
//    itemStack.setTagCompound(tag)
//    itemStack.setTagCompound(tag)
//    itemList2.add(itemStack.copy)
//    injectAEPower(itemStack, this.MAX_POWER, Actionable.MODULATE)
//    itemList2.add(itemStack)
//  }
//
//  override def isInCreativeTab2(targetTab: CreativeTabs): Boolean = isInCreativeTab(targetTab)
//
//  @Optional.Method(modid = "wct")
//  override def getRender: IBaubleRender = null
//
//  @Optional.Method(modid = "wct")
//  override def getBaubleType(itemStack: ItemStack): BaubleType = BaubleType.TRINKET
//
//  @Optional.Method(modid = "wct")
//  override def initModel(): Unit = {}
//
//  @Optional.Method(modid = "wct")
//  override def getModelResource(item: Item): ModelResourceLocation = null
//
//  @Optional.Method(modid = "wct")
//  override def openGui(player: EntityPlayer, isBauble: Boolean, playerSlot: Int): Unit =
//    WirelessCrafting.openCraftingTerminal(player, isBauble, playerSlot)
//
//  @Optional.Method(modid = "wct")
//  override def getPlayer: EntityPlayer = this.holder
//
//  @Optional.Method(modid = "wct")
//  override def setPlayer(entityPlayer: EntityPlayer): Unit = this.holder = entityPlayer
//
//  @Optional.Method(modid = "wct")
//  override def getColor: Int = 0xFF8F15D4
//
//  @Optional.Method(modid = "wct")
//  override def getMenuIcon: ResourceLocation = new ResourceLocation(Constants.MOD_ID, "textures/items/terminal.universal.wireless.png")
//}
