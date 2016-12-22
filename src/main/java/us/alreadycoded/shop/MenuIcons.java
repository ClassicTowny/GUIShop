package us.alreadycoded.shop;

import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings("deprecation")
public class MenuIcons
{
  public static ItemStack mainMenu = new ItemStack(388, 1);
  public static ItemStack helpMenu1 = new ItemStack(323, 1);
  public static ItemStack helpMenu2 = new ItemStack(323, 1);
  public static ItemStack sellMenu = new ItemStack(56, 1);
  public static ItemStack buyMenu = new ItemStack(264, 1);
  public static ItemStack enchMenu = new ItemStack(403, 1);
  
  public static ArrayList<IconStack> tabIcons = new ArrayList<IconStack>();
  
  static {
    ItemMeta meta = helpMenu1.getItemMeta();
    meta.setDisplayName("How to shop!");
    ArrayList<String> lore = new ArrayList<String>();
    lore.add("Click on one of the items");
    lore.add("to select that category.");
    lore.add("Hover over items to see");
    lore.add("how much they cost.");
    meta.setLore(lore);
    helpMenu1.setItemMeta(meta);
    
    meta = helpMenu2.getItemMeta();
    meta.setDisplayName("How to shop!");
    lore = new ArrayList<String>();
    lore.add("Left click to buy 1 item at a time.");
    lore.add("Right click to buy 16 items at a time.");
    lore.add("Hold shift and left click to buy 64");
    lore.add("items at a time.");
    meta.setLore(lore);
    helpMenu2.setItemMeta(meta);
    
    meta = enchMenu.getItemMeta();
    meta.setDisplayName("Enchantments");
    enchMenu.setItemMeta(meta);
    
    meta = sellMenu.getItemMeta();
    meta.setDisplayName("Sell Menu");
    sellMenu.setItemMeta(meta);
    
    meta = buyMenu.getItemMeta();
    meta.setDisplayName("Buy Menu");
    buyMenu.setItemMeta(meta);
    
    meta = mainMenu.getItemMeta();
    meta.setDisplayName("Main Menu");
    mainMenu.setItemMeta(meta);
  }
  
  public static Inventory getMainMenu(Player p)
  {
    Inventory inv = Bukkit.createInventory(p, 9, Settings.menuPrefix + "Main Menu");
    inv.setItem(7, helpMenu1);
    inv.setItem(8, helpMenu2);
    
    inv.setItem(1, sellMenu);
    inv.setItem(0, buyMenu);
    
    inv.setItem(4, enchMenu);
    return inv;
  }
  
  public static Inventory getBuyMenu(Player p)
  {
    ArrayList<ItemStack> tabIcons1 = new ArrayList<ItemStack>();
    for (IconStack s : tabIcons)
      if ((s.isBuyIcon) && (s.pageIndex == 1))
        tabIcons1.add(s.itemStack);
    int size = tabIcons1.size() + (int)(Math.ceil(tabIcons1.size() / 7) * 2.0D);
    while (size % 9 != 0)
      size++;
    if (size < 9)
      size = 9;
    Inventory inv = Bukkit.createInventory(p, size, Settings.menuPrefix + "Buy Menu");
    inv.setItem(0, mainMenu);
    
    int x = 2;
    for (ItemStack d : tabIcons1)
    {
      if (x > 53)
        break;
      inv.setItem(x, d);
      x++;
      if (x % 9 == 0)
        x += 2;
    }
    return inv;
  }
  
  public static Inventory getSellMenu(Player p)
  {
    ArrayList<ItemStack> tabIcons1 = new ArrayList<ItemStack>();
    for (IconStack s : tabIcons)
      if ((!s.isBuyIcon) && (s.pageIndex == 1))
        tabIcons1.add(s.itemStack);
    int size = tabIcons1.size() + (int)(Math.ceil(tabIcons1.size() / 7) * 2.0D);
    while (size % 9 != 0)
      size++;
    if (size < 9)
      size = 9;
    Inventory inv = Bukkit.createInventory(p, size, Settings.menuPrefix + "Sell Menu");
    inv.setItem(0, mainMenu);
    
    int x = 2;
    for (ItemStack d : tabIcons1)
    {
      if (x > 53)
        break;
      inv.setItem(x, d);
      x++;
      if (x % 9 == 0)
        x += 2;
    }
    return inv;
  }
  
  public static Inventory getEnchantMenu(Player p)
  {
    ArrayList<EnchantmentData> data = Settings.enchantments;
    int size = data.size() + (int)(Math.ceil(data.size() / 7) * 2.0D);
    while (size % 9 != 0)
      size++;
    if (size < 9)
      size = 9;
    Inventory inv = Bukkit.createInventory(p, size, Settings.menuPrefix + "Enchantments");
    inv.setItem(0, mainMenu);
    int x = 2;
    for (EnchantmentData d : data)
    {
      if (x > 53)
        break;
      ItemStack stack = new ItemStack(403, 1);
      
      ArrayList<String> lore = new ArrayList<String>();
      
      lore.add("Click to buy.");
      lore.add("Price: " + d.price + " " + Shop.economy.currencyNamePlural());
      
      EnchantmentStorageMeta meta = (EnchantmentStorageMeta)stack.getItemMeta();
      meta.setLore(lore);
      meta.addStoredEnchant(Enchantment.getById(d.enchantmentID), d.enchantmentLevel, true);
      stack.setItemMeta(meta);
      
      inv.setItem(x, stack);
      
      x++;
      if (x % 9 == 0)
        x += 2;
    }
    return inv;
  }
}
