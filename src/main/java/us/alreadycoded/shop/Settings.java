package us.alreadycoded.shop;

import java.io.File;
import java.util.ArrayList;
import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;

public class Settings
{
  public static ArrayList<TabSetting> tabs = new ArrayList<TabSetting>();
  public static ArrayList<ItemSetting> allItems = new ArrayList<ItemSetting>();
  public static ArrayList<EnchantmentData> enchantments = new ArrayList<EnchantmentData>();
  public static String menuPrefix;
  public static String donateMessage;
  public static boolean donateMessageOn;
  public static boolean allowBrowse;
  public static double priceMultiplier;
  
  @SuppressWarnings("deprecation")
public static void saveDefaultSettings()
  {
    menuPrefix = ChatColor.GREEN + "[Shop] ";
    donateMessage = "§cDonate to gain access!";
    donateMessageOn = true;
    allowBrowse = false;
    priceMultiplier = 1.0D;
    if (!new File(Shop.shop.getDataFolder() + "/Prices.txt").exists())
    {
      ArrayList<String> list = new ArrayList<String>();
      DefaultPrices.setupDefaultPrices();
      for (ItemSetting m : DefaultPrices.defaultItems)
        list.add(m.itemName + " itemID=" + m.itemID + " buyPrice=" + m.itemBuyPrice + " sellPrice=" + m.itemSellPrice + " tabID=" + m.tabID);
      FileUtil.saveFile("Prices.txt", list);
      DefaultPrices.defaultItems.clear();
    }
    
    if (!new File(Shop.shop.getDataFolder() + "/Tabs.txt").exists())
    {
      ArrayList<String> list = new ArrayList<String>();
      list.add("tabID=0 tabName=Building_Blocks IconID=45");
      list.add("tabID=1 tabName=Decorations IconID=38");
      list.add("tabID=2 tabName=Redstone IconID=331");
      list.add("tabID=3 tabName=Transportation IconID=27");
      list.add("tabID=4 tabName=Miscelaneus IconID=327");
      list.add("tabID=5 tabName=Foodstuffs IconID=260");
      list.add("tabID=6 tabName=Tools IconID=257");
      list.add("tabID=7 tabName=Combat IconID=283");
      list.add("tabID=8 tabName=Brewing IconID=373");
      list.add("tabID=9 tabName=Materials IconID=265");
      FileUtil.saveFile("Tabs.txt", list);
    }
    
    if (!new File(Shop.shop.getDataFolder() + "/Options.txt").exists())
    {
      ArrayList<String> list = new ArrayList<String>();
      list.add("SHOP_TITLE=" + menuPrefix);
      list.add("DONATE_MESSAGE_ON=" + donateMessageOn);
      list.add("DONATE_MESSAGE=" + donateMessage);
      list.add("ALLOW_BROWSE=" + allowBrowse);
      list.add("PRICE_MULTIPLIER=" + priceMultiplier);
      FileUtil.saveFile("Options.txt", list);
    }
    
    if (!new File(Shop.shop.getDataFolder() + "/EnchantmentPrices.txt").exists())
    {
      ArrayList<String> list = new ArrayList<String>();
      Enchantment[] arrayOfEnchantment; int j = (arrayOfEnchantment = Enchantment.values()).length; for (int i = 0; i < j; i++) { Enchantment e = arrayOfEnchantment[i];
        list.add(e.getName() + " enchantmentID=" + e.getId() + " enchantmentLevel=" + e.getMaxLevel() + " enchantmentPrice=0"); }
      FileUtil.saveFile("EnchantmentPrices.txt", list);
    }
  }
  

  public static void loadSettings()
  {
    tabs.clear();
    allItems.clear();
    enchantments.clear();
    MenuIcons.tabIcons.clear();
    
    ArrayList<String> list = FileUtil.readFile("Options.txt");
    donateMessageOn = list.contains("DONATE_MESSAGE_ON=true");
    allowBrowse = list.contains("ALLOW_BROWSE=true");
    for (String s : list)
    {
      try
      {
        if (s.contains("SHOP_TITLE="))
          menuPrefix = s.split("=")[1] + " ";
        if (s.contains("DONATE_MESSAGE="))
          donateMessage = s.split("=")[1];
        if (s.contains("PRICE_MULTIPLIER=")) {
          priceMultiplier = Double.parseDouble(s.split("=")[1]);
        }
      }
      catch (Exception e) {
        Shop.shop.getServer().getConsoleSender().sendMessage(ChatColor.RED + Shop.pluginName + "Error loading Option.txt");
        Shop.shop.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + Shop.pluginName + "Using default options.");
        Shop.shop.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + Shop.pluginName + e.toString().replace("java.lang.", ""));
      }
    }
    

    list = FileUtil.readFile("Tabs.txt");
    int x = 0;
    for (String s : list)
    {
      try
      {
        int tabID = Integer.parseInt(s.split(" ")[0].split("=")[1]);
        int iconID = Integer.parseInt(s.split(" ")[2].split("=")[1]);
        String tabName = s.split(" ")[1].split("=")[1].replace("_", " ");
        TabSetting tab = new TabSetting(tabID, tabName, iconID);
        tabs.add(tab);
      }
      catch (Exception e)
      {
        Shop.shop.getServer().getConsoleSender().sendMessage(ChatColor.RED + Shop.pluginName + "Error found in tabs config on line " + x + ":");
        Shop.shop.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + Shop.pluginName + (String)list.get(x));
        Shop.shop.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + Shop.pluginName + e.toString().replace("java.lang.", ""));
      }
      x++;
    }
    
    list = FileUtil.readFile("Prices.txt");
    x = 0;
    for (String s : list)
    {
      try
      {
        int itemID = 0;
        int itemData = 0;
        if (s.split(" ")[1].split("=")[1].contains(":"))
        {
          itemID = Integer.parseInt(s.split(" ")[1].split("=")[1].split(":")[0]);
          itemData = Integer.parseInt(s.split(" ")[1].split("=")[1].split(":")[1]);
        }
        else
        {
          itemID = Integer.parseInt(s.split(" ")[1].split("=")[1]);
          itemData = 0;
        }
        double itemBuyPrice = priceMultiplier * Double.parseDouble(s.split(" ")[2].split("=")[1]);
        double itemSellPrice = priceMultiplier * Double.parseDouble(s.split(" ")[3].split("=")[1]);
        int tabID = Integer.parseInt(s.split(" ")[4].split("=")[1]);
        
        ItemSetting setting = new ItemSetting(itemID, itemData, itemBuyPrice, itemSellPrice, tabID);
        if (itemBuyPrice > 0.0D)
          getTab(tabID).buyItems.add(setting);
        if (itemSellPrice > 0.0D)
          getTab(tabID).sellItems.add(setting);
        if ((itemSellPrice > 0.0D) || (itemBuyPrice > 0.0D)) {
          allItems.add(setting);
        }
      }
      catch (Exception e) {
        Shop.shop.getServer().getConsoleSender().sendMessage(ChatColor.RED + Shop.pluginName + "Error found in price config on line " + x + ":");
        Shop.shop.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + Shop.pluginName + (String)list.get(x));
        Shop.shop.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + Shop.pluginName + e.toString().replace("java.lang.", ""));
      }
      x++;
    }
    
    list = FileUtil.readFile("EnchantmentPrices.txt");
    x = 0;
    for (String s : list)
    {
      try
      {
        int enchantmentID = Integer.parseInt(s.split(" ")[1].split("=")[1]);
        int enchantmentLevel = Integer.parseInt(s.split(" ")[2].split("=")[1]);
        double enchantmentPrice = priceMultiplier * Double.parseDouble(s.split(" ")[3].split("=")[1]);
        if (enchantmentPrice > 0.0D) {
          enchantments.add(new EnchantmentData(enchantmentID, enchantmentLevel, enchantmentPrice));
        }
      }
      catch (Exception e) {
        Shop.shop.getServer().getConsoleSender().sendMessage(ChatColor.RED + Shop.pluginName + "Error found in price config on line " + x + ":");
        Shop.shop.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + Shop.pluginName + (String)list.get(x));
        Shop.shop.getServer().getConsoleSender().sendMessage(ChatColor.DARK_RED + Shop.pluginName + e.toString().replace("java.lang.", ""));
      }
      x++;
    }
    
    for (TabSetting t : tabs) {
      t.setupIcons();
    }
  }
  
  public static TabSetting getTab(int tabID) {
    for (TabSetting t : tabs)
      if (t.tabID == tabID)
        return t;
    return null;
  }
  
  public static ItemSetting getItem(int itemID, int itemData)
  {
    for (ItemSetting t : allItems)
      if ((t.itemID == itemID) && (t.itemData == itemData))
        return t;
    return null;
  }
  
  public static EnchantmentData getEnchant(int enchantID, int enchantLevel)
  {
    for (EnchantmentData t : enchantments)
      if ((t.enchantmentID == enchantID) && (t.enchantmentLevel == enchantLevel))
        return t;
    return null;
  }
}
