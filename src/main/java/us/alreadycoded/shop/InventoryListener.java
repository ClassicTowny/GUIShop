package us.alreadycoded.shop;

import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings("deprecation")
public class InventoryListener implements org.bukkit.event.Listener
{
  
@EventHandler
  public void menuClick(InventoryClickEvent event)
  {
    Player player = org.bukkit.Bukkit.getPlayer(event.getWhoClicked().getName());
    if (event.getCurrentItem() == null) {
      return;
    }
    if (event.getCurrentItem().equals(MenuIcons.buyMenu))
    {
      if ((!Settings.allowBrowse) && (!player.hasPermission("shop.buy")))
      {
        player.sendMessage(Settings.menuPrefix + "§cYou do not have permission to buy items.");
        if (Settings.donateMessageOn) {
          player.sendMessage(Settings.menuPrefix + Settings.donateMessage);
        }
        return;
      }
      open(player, MenuIcons.getBuyMenu(player));
      event.setCancelled(true);
    }
    else if (event.getCurrentItem().equals(MenuIcons.sellMenu))
    {
      if ((!Settings.allowBrowse) && (!player.hasPermission("shop.sell")))
      {
        player.sendMessage(Settings.menuPrefix + "§cYou do not have permission to sell items.");
        if (Settings.donateMessageOn) {
          player.sendMessage(Settings.menuPrefix + Settings.donateMessage);
        }
        return;
      }
      open(player, MenuIcons.getSellMenu(player));
      event.setCancelled(true);
    }
    else if (event.getCurrentItem().equals(MenuIcons.mainMenu))
    {
      open(player, MenuIcons.getMainMenu(player));
      event.setCancelled(true);
    }
    else if (event.getCurrentItem().equals(MenuIcons.enchMenu))
    {
      if ((!Settings.allowBrowse) && (!player.hasPermission("shop.enchantments")))
      {
        player.sendMessage(Settings.menuPrefix + "§cYou do not have permission to buy enchantments.");
        if (Settings.donateMessageOn) {
          player.sendMessage(Settings.menuPrefix + Settings.donateMessage);
        }
        return;
      }
      open(player, MenuIcons.getEnchantMenu(player));
      event.setCancelled(true);
    }
    else if (getStack(event.getCurrentItem()) != null)
    {
      if ((!Settings.allowBrowse) && (!player.hasPermission("shop.tab." + getStack(event.getCurrentItem()).tab.tabID)) && (!player.hasPermission("shop.tab.*")))
      {
        player.sendMessage(Settings.menuPrefix + "§cYou do not have permission to use this tab.");
        if (Settings.donateMessageOn) {
          player.sendMessage(Settings.menuPrefix + Settings.donateMessage);
        }
        return;
      }
      if (getStack(event.getCurrentItem()).isBuyIcon) {
        open(player, getStack(event.getCurrentItem()).tab.makeBuyTab(player, getStack(event.getCurrentItem()).pageIndex));
      } else {
        open(player, getStack(event.getCurrentItem()).tab.makeSellTab(player, getStack(event.getCurrentItem()).pageIndex));
      }
      event.setCancelled(true);
    }
    else if ((event.getCurrentItem().equals(MenuIcons.helpMenu1)) || (event.getCurrentItem().equals(MenuIcons.helpMenu2)))
    {
      event.setCancelled(true);
    }
  }
  
  public IconStack getStack(ItemStack i)
  {
    for (IconStack s : MenuIcons.tabIcons) {
      if ((s.itemStack.equals(i)) && (s.isBuyIcon == i.getItemMeta().getDisplayName().endsWith("§1"))) {
        return s;
      }
    }
    return null;
  }
  

  @EventHandler
  public void drop(InventoryClickEvent event)
  {
    if ((event.getCurrentItem() == null) && (event.getCurrentItem() == null)) {
      return;
    }
    if (event.getInventory().getName().contains(Settings.menuPrefix)) {
      if (event.getRawSlot() < event.getView().getTopInventory().getSize())
      {
        event.setCancelled(true);
        ((Player)event.getWhoClicked()).updateInventory();
      }
      else if (event.isShiftClick())
      {
        event.setCancelled(true);
      }
    }
  }
  
  @EventHandler
  public void pick(InventoryClickEvent event)
  {
    if ((event.getCurrentItem() == null) && (event.getCurrentItem() == null)) {
      return;
    }
    if (event.getInventory().getName().contains(Settings.menuPrefix)) {
      if ((event.getCurrentItem() != null) && (event.getCurrentItem().getItemMeta() != null) && (event.getCurrentItem().getItemMeta().getLore() != null) && (event.getCurrentItem().getItemMeta().getLore().get(0) != null) && (((String)event.getCurrentItem().getItemMeta().getLore().get(0)).endsWith("buy.")))
      {
        if (event.getRawSlot() == 64537)
        {
          event.getView().setCursor(null);
          event.setCancelled(true);
          return;
        }
        if (event.isShiftClick())
        {
          event.setCancelled(true);
          ItemStack stack = event.getCurrentItem().clone();
          ItemMeta meta = stack.getItemMeta();
          meta.setLore(new ArrayList<String>());
          stack.setItemMeta(meta);
          if (stackAllowed(stack)) {
            stack.setAmount(64);
          } else if (!stackAllowed(stack))
          {
            stack.setAmount(1);
            ((Player)event.getWhoClicked()).sendMessage(ChatColor.DARK_RED + "Not Allowed to buy this item in stacks.");
          }
          buyItem((Player)event.getWhoClicked(), stack);
        }
        else if (event.isRightClick())
        {
          event.setCancelled(true);
          ItemStack stack = event.getCurrentItem().clone();
          ItemMeta meta = stack.getItemMeta();
          meta.setLore(new ArrayList<String>());
          stack.setItemMeta(meta);
          stack.setAmount(16);
          buyItem((Player)event.getWhoClicked(), stack);
        }
        else if (event.isLeftClick())
        {
          event.setCancelled(true);
          ItemStack stack = event.getCurrentItem().clone();
          ItemMeta meta = stack.getItemMeta();
          meta.setLore(new ArrayList<String>());
          stack.setItemMeta(meta);
          stack.setAmount(1);
          buyItem((Player)event.getWhoClicked(), stack);
        }
      }
      else if ((event.getCurrentItem() != null) && (event.getCurrentItem().getItemMeta() != null) && (event.getCurrentItem().getItemMeta().getLore() != null) && (event.getCurrentItem().getItemMeta().getLore().get(0) != null) && (((String)event.getCurrentItem().getItemMeta().getLore().get(0)).endsWith("sell.")))
      {
        if (event.getRawSlot() == 64537)
        {
          event.getView().setCursor(null);
          event.setCancelled(true);
          return;
        }
        if (event.isShiftClick())
        {
          event.setCancelled(true);
          ItemStack stack = event.getCurrentItem().clone();
          ItemMeta meta = stack.getItemMeta();
          meta.setLore(new ArrayList<String>());
          stack.setItemMeta(meta);
          if (stackAllowed(stack))
          {
            stack.setAmount(64);
          } else if (!stackAllowed(stack))
          {
            stack.setAmount(1);
            ((Player)event.getWhoClicked()).sendMessage(ChatColor.DARK_RED + "Not Allowed to sell this item in stacks.");
          }
          sellItem((Player)event.getWhoClicked(), stack);
        }
        else if (event.isRightClick())
        {
          event.setCancelled(true);
          ItemStack stack = event.getCurrentItem().clone();
          ItemMeta meta = stack.getItemMeta();
          meta.setLore(new ArrayList<String>());
          stack.setItemMeta(meta);
          stack.setAmount(16);
          sellItem((Player)event.getWhoClicked(), stack);
        }
        else if (event.isLeftClick())
        {
          event.setCancelled(true);
          ItemStack stack = event.getCurrentItem().clone();
          ItemMeta meta = stack.getItemMeta();
          meta.setLore(new ArrayList<String>());
          stack.setItemMeta(meta);
          stack.setAmount(1);
          sellItem((Player)event.getWhoClicked(), stack);
        }
      }
    }
  }
  

  public boolean buyItem(Player p, ItemStack i)
  {
    if ((p.getGameMode() == org.bukkit.GameMode.CREATIVE) && (!p.hasPermission("shop.creative")))
    {
      p.sendMessage(Settings.menuPrefix + "§cYou do not have permission to buy in creative mode.");
      return false;
    }
    if (i.getTypeId() == 403)
    {
      if (!p.hasPermission("shop.enchantments"))
      {
        p.sendMessage(Settings.menuPrefix + "§cYou do not have permission to buy enchantments.");
        if (Settings.donateMessageOn) {
          p.sendMessage(Settings.menuPrefix + Settings.donateMessage);
        }
        return false;
      }
      EnchantmentStorageMeta meta = (EnchantmentStorageMeta)i.getItemMeta();
      Enchantment e = (Enchantment)meta.getStoredEnchants().keySet().toArray()[0];
      double price = i.getAmount() * Settings.getEnchant(e.getId(), meta.getStoredEnchantLevel(e)).price;
      if (Shop.economy.getBalance(p.getName()) > price)
      {
        Shop.economy.withdrawPlayer(p.getName(), price);
        p.getInventory().addItem(new ItemStack[] { i });
        p.sendMessage(Shop.pluginName + "§aPurchased item for " + price + " " + Shop.economy.currencyNamePlural() + "!");
        return true;
      }
      p.sendMessage(Shop.pluginName + "§cYou can not afford " + price + " " + Shop.economy.currencyNamePlural() + "!");
      return false;
    }
    if (!p.hasPermission("shop.buy"))
    {
      p.sendMessage(Settings.menuPrefix + "§cYou do not have permission to buy items.");
      if (Settings.donateMessageOn) {
        p.sendMessage(Settings.menuPrefix + Settings.donateMessage);
      }
      return false;
    }
    double price = i.getAmount() * Settings.getItem(i.getTypeId(), i.getData().getData()).itemBuyPrice;
    if (Shop.economy.getBalance(p.getName()) > price)
    {
      Shop.economy.withdrawPlayer(p.getName(), price);
      p.getInventory().addItem(new ItemStack[] { i });
      p.sendMessage(Shop.pluginName + "§aPurchased item for " + price + " " + Shop.economy.currencyNamePlural() + "!");
      return true;
    }
    p.sendMessage(Shop.pluginName + "§cYou can not afford " + price + " " + Shop.economy.currencyNamePlural() + "!");
    return false;
  }
  

  public boolean sellItem(Player p, ItemStack i)
  {
    if ((p.getGameMode() == org.bukkit.GameMode.CREATIVE) && (!p.hasPermission("shop.creative")))
    {
      p.sendMessage(Settings.menuPrefix + "§cYou do not have permission to sell in creative mode.");
      return false;
    }
    if (!p.hasPermission("shop.sell"))
    {
      p.sendMessage(Settings.menuPrefix + "§cYou do not have permission to sell items.");
      if (Settings.donateMessageOn) {
        p.sendMessage(Settings.menuPrefix + Settings.donateMessage);
      }
      return false;
    }
    double price = i.getAmount() * Settings.getItem(i.getTypeId(), i.getData().getData()).itemSellPrice;
    if (p.getInventory().containsAtLeast(i, i.getAmount()))
    {
      p.getInventory().removeItem(new ItemStack[] { i });
      Shop.economy.depositPlayer(p.getName(), price);
      p.sendMessage(Shop.pluginName + "§aSold item for " + price + " " + Shop.economy.currencyNamePlural() + "!");
      return true;
    }
    if (stackAllowed(i))
      p.sendMessage(Shop.pluginName + "§cYou do not have that item!");
    return false;
  }
  
  public void open(final Player p, final Inventory i)
  {
    Shop.shop.getServer().getScheduler().scheduleSyncDelayedTask(Shop.shop, new Runnable()
    {

      public void run() {
        p.openInventory(i); } }, 1L);
  }
  



  public boolean stackAllowed(ItemStack stack)
  {
    if ((stack.getType() == Material.IRON_AXE) || (stack.getType() == Material.STONE_AXE) || (stack.getType() == Material.GOLD_AXE) || (stack.getType() == Material.DIAMOND_AXE) || (stack.getType() == Material.WOOD_AXE))
      return false;
    if ((stack.getType() == Material.IRON_SPADE) || (stack.getType() == Material.STONE_SPADE) || (stack.getType() == Material.GOLD_SPADE) || (stack.getType() == Material.DIAMOND_SPADE) || (stack.getType() == Material.WOOD_SPADE))
      return false;
    if ((stack.getType() == Material.IRON_SWORD) || (stack.getType() == Material.STONE_SWORD) || (stack.getType() == Material.GOLD_SWORD) || (stack.getType() == Material.DIAMOND_SWORD) || (stack.getType() == Material.WOOD_SWORD))
      return false;
    if ((stack.getType() == Material.IRON_HOE) || (stack.getType() == Material.STONE_HOE) || (stack.getType() == Material.GOLD_HOE) || (stack.getType() == Material.DIAMOND_HOE) || (stack.getType() == Material.WOOD_HOE))
      return false;
    if ((stack.getType() == Material.IRON_PICKAXE) || (stack.getType() == Material.STONE_PICKAXE) || (stack.getType() == Material.GOLD_PICKAXE) || (stack.getType() == Material.DIAMOND_PICKAXE) || (stack.getType() == Material.WOOD_PICKAXE))
      return false;
    if ((stack.getType() == Material.FISHING_ROD) || (stack.getType() == Material.BOW) || (stack.getType() == Material.BOOK)) {
      return false;
    }
    if ((stack.getType() == Material.IRON_HELMET) || (stack.getType() == Material.CHAINMAIL_HELMET) || (stack.getType() == Material.GOLD_HELMET) || (stack.getType() == Material.DIAMOND_HELMET) || (stack.getType() == Material.LEATHER_HELMET))
      return false;
    if ((stack.getType() == Material.IRON_CHESTPLATE) || (stack.getType() == Material.CHAINMAIL_CHESTPLATE) || (stack.getType() == Material.GOLD_CHESTPLATE) || (stack.getType() == Material.DIAMOND_CHESTPLATE) || (stack.getType() == Material.LEATHER_CHESTPLATE))
      return false;
    if ((stack.getType() == Material.IRON_LEGGINGS) || (stack.getType() == Material.CHAINMAIL_LEGGINGS) || (stack.getType() == Material.GOLD_LEGGINGS) || (stack.getType() == Material.DIAMOND_LEGGINGS) || (stack.getType() == Material.LEATHER_LEGGINGS))
      return false;
    if ((stack.getType() == Material.IRON_BOOTS) || (stack.getType() == Material.CHAINMAIL_BOOTS) || (stack.getType() == Material.GOLD_BOOTS) || (stack.getType() == Material.DIAMOND_BOOTS) || (stack.getType() == Material.LEATHER_BOOTS)) {
      return false;
    }
    return true;
  }
}
