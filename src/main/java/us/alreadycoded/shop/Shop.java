package us.alreadycoded.shop;

import java.util.ArrayList;
import java.util.List;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Shop
  extends JavaPlugin
{
  public static String pluginName = "[Shop] ";
  public static Economy economy = null;
  public static InventoryListener listener = null;
  public static Shop shop = null;
  
  public void onEnable()
  {
    shop = this;
    setupEconomy();
    FileUtil.mkdirs();
    Settings.saveDefaultSettings();
    Settings.loadSettings();
    setupListeners();
  }
  
  public void onDisable()
  {
    List<Player> listOfPlayer = new ArrayList<Player>();
    for(Player p : getServer().getOnlinePlayers()){
    	listOfPlayer.add(p);
    }
    if(listOfPlayer!=null){
    	int j = listOfPlayer.size(); 
        for (int i = 0; i < j; i++) 
        { 
          Player p = listOfPlayer.get(i);
          
          if (p.getOpenInventory().getTitle().contains(Settings.menuPrefix)) {
            p.closeInventory();
          }
        }
    }  
  }
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    Player p = (Player)sender;
    if (command.getName().equalsIgnoreCase("shop"))
    {
      if (!(sender instanceof Player)) {
        sender.sendMessage(Settings.menuPrefix + "§cYou can only use this command in-game!");
      }
      else
      {
        ((Player)sender).openInventory(MenuIcons.getMainMenu((Player)sender));
      }
      p.sendMessage(Settings.menuPrefix + "§aShop has been opened!");
      p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 2.0F, 1.0F);

    }
    else if (command.getName().equalsIgnoreCase("reloadshop"))
    {
      if (sender.hasPermission("shop.admin"))
      {
        sender.sendMessage(Settings.menuPrefix + "§aReloading shop configuration & prices!");
        setupEconomy();
        FileUtil.mkdirs();
        Settings.saveDefaultSettings();
        Settings.loadSettings();
        sender.sendMessage(Settings.menuPrefix + "§aDone!");
      }
      else {
        sender.sendMessage(Settings.menuPrefix + "§cYou do not have permission to reload the shop!");
      } }
    return false;
  }
  
  private void setupEconomy()
  {
    RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(Economy.class);
    if (economyProvider != null)
    {
      economy = (Economy)economyProvider.getProvider();
    }
    else
    {
      getServer().getConsoleSender().sendMessage(ChatColor.RED + pluginName + "Can not find an economy plugin!");
      getServer().getConsoleSender().sendMessage(ChatColor.RED + pluginName + "Deactivating shop plugin!");
      getPluginLoader().disablePlugin(this);
    }
  }
  

  private void setupListeners()
  {
    listener = new InventoryListener();
    getServer().getPluginManager().registerEvents(listener, this);
  }
  
}
