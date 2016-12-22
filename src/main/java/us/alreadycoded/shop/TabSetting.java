package us.alreadycoded.shop;

import java.util.ArrayList;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TabSetting
{
  public int tabID;
  public String tabName;
  public int iconID;
  public ArrayList<ItemSetting> buyItems = new ArrayList<ItemSetting>();
  public ArrayList<ItemSetting> sellItems = new ArrayList<ItemSetting>();
  
  public TabSetting(int tabID, String tabName, int iconID)
  {
    this.tabID = tabID;
    this.tabName = tabName;
    this.iconID = iconID;
  }
  
  public void setupIcons()
  {
    int size = this.buyItems.size() + (int)(Math.ceil(this.buyItems.size() / 7) * 2.0D);
    while (size % 9 != 0)
      size++;
    for (int x = 0; x <= Math.ceil(size / 54); x++) {
      MenuIcons.tabIcons.add(new IconStack(this, x + 1, true));
    }
    size = this.sellItems.size() + (int)(Math.ceil(this.sellItems.size() / 7) * 2.0D);
    while (size % 9 != 0)
      size++;
    for (int x = 0; x <= Math.ceil(size / 54); x++) {
      MenuIcons.tabIcons.add(new IconStack(this, x + 1, false));
    }
  }
  
  @SuppressWarnings("deprecation")
  public Inventory makeBuyTab(Player p, int i) {
    ArrayList<ItemSetting> subList = new ArrayList<ItemSetting>();
    subList.addAll(this.buyItems.subList(42 * (i - 1) < this.buyItems.size() ? 42 * (i - 1) : this.buyItems.size() - 1, 42 * i < this.buyItems.size() ? 42 * i : this.buyItems.size() - 1));
    int size = subList.size() + (int)(Math.ceil(subList.size() / 7) * 2.0D);
    while (size % 9 != 0)
      size++;
    if (size < 9)
      size = 9;
    if ((size > 54) || (i != 1))
      size = 54;
    Inventory inv = org.bukkit.Bukkit.createInventory(p, size, Settings.menuPrefix + this.tabName);
    
    inv.setItem(0, MenuIcons.buyMenu);
    int x1 = 18;
    if (this.buyItems.size() > 42) {
      for (IconStack is : getOtherTabs(this))
        if (is.isBuyIcon)
        {
          inv.setItem(x1, is.itemStack);
          x1 += 9;
        }
    }
    int x = 2;
    for (ItemSetting d : subList)
    {
      if (x >= 54)
        break;
      ItemStack stack = new ItemStack(d.itemID, 1, (short)d.itemData);
      
      ArrayList<String> lore = new ArrayList<String>();
      
      lore.add("Click to buy.");
      lore.add("Price: " + d.itemBuyPrice + " " + Shop.economy.currencyNamePlural());
      
      ItemMeta meta = stack.getItemMeta();
      meta.setLore(lore);
      stack.setItemMeta(meta);
      
      inv.setItem(x, stack);
      x++;
      if (x % 9 == 0)
        x += 2;
    }
    return inv;
  }
  
  @SuppressWarnings("deprecation")
public Inventory makeSellTab(Player p, int i)
  {
    ArrayList<ItemSetting> subList = new ArrayList<ItemSetting>();
    subList.addAll(this.sellItems.subList(42 * (i - 1) < this.sellItems.size() ? 42 * (i - 1) : this.sellItems.size() - 1, 42 * i < this.sellItems.size() ? 42 * i : this.sellItems.size() - 1));
    int size = subList.size() + (int)(Math.ceil(subList.size() / 7) * 2.0D);
    while (size % 9 != 0)
      size++;
    if (size < 9)
      size = 9;
    if ((size > 54) || (i != 1))
      size = 54;
    Inventory inv = org.bukkit.Bukkit.createInventory(p, size, Settings.menuPrefix + this.tabName);
    inv.setItem(0, MenuIcons.sellMenu);
    
    int x1 = 18;
    if (this.sellItems.size() > 42) {
      for (IconStack is : getOtherTabs(this))
        if (!is.isBuyIcon)
        {
          inv.setItem(x1, is.itemStack);
          x1 += 9;
        }
    }
    int x = 2;
    for (ItemSetting d : subList)
    {
      if (x >= 54)
        break;
      ItemStack stack = new ItemStack(d.itemID, 1, (short)d.itemData);
      
      ArrayList<String> lore = new ArrayList<String>();
      
      lore.add("Click to sell.");
      lore.add("Price: " + d.itemSellPrice + " " + Shop.economy.currencyNamePlural());
      
      ItemMeta meta = stack.getItemMeta();
      meta.setLore(lore);
      stack.setItemMeta(meta);
      
      inv.setItem(x, stack);
      x++;
      if (x % 9 == 0)
        x += 2;
    }
    return inv;
  }
  
  public ArrayList<IconStack> getOtherTabs(TabSetting i)
  {
    ArrayList<IconStack> list = new ArrayList<IconStack>();
    for (IconStack s : MenuIcons.tabIcons)
      if (s.tab.equals(i))
        list.add(s);
    return list;
  }
}
