package us.alreadycoded.shop;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class IconStack
{
  public TabSetting tab;
  public int pageIndex;
  public boolean isBuyIcon;
  public ItemStack itemStack;
  
  @SuppressWarnings("deprecation")
public IconStack(TabSetting tab, int pageIndex, boolean isBuyIcon)
  {
    this.itemStack = new ItemStack(tab.iconID, pageIndex);
    this.tab = tab;
    this.pageIndex = pageIndex;
    ItemMeta meta = this.itemStack.getItemMeta();
    meta.setDisplayName(tab.tabName + (isBuyIcon ? "§1" : "§2"));
    this.itemStack.setItemMeta(meta);
    this.isBuyIcon = isBuyIcon;
  }
}
