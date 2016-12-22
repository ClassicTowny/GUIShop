package us.alreadycoded.shop;


public class ItemSetting
{
  public int itemID;
  public int itemData;
  public double itemBuyPrice;
  public double itemSellPrice;
  public int tabID;
  public String itemName;
  
  public ItemSetting(int itemID, int itemData, double itemBuyPrice, double itemSellPrice, int tabID)
  {
    this.itemData = itemData;
    this.itemID = itemID;
    this.itemBuyPrice = itemBuyPrice;
    this.itemSellPrice = itemSellPrice;
    this.tabID = tabID;
  }
  
  public ItemSetting(String itemName, int itemID, int itemData, double itemBuyPrice, double itemSellPrice, int tabID)
  {
    this(itemID, itemData, itemBuyPrice, itemSellPrice, tabID);
    this.itemName = itemName;
  }
}
